package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.RpiSurplusDetails;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CCLAUtils.PadType;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.ucm.ccla.data.subscription.RpiDrawdown;

/** Used for creation of annual RPI Draw-down forms. Only applicable since the 2014 draw-
 *  down, prior to this the spool files were generated by eDBA.
 *  
 *  Each form is targeted at a single LCF organisation, and lists all their donors and
 *  available draw-down amounts, taken from the RpiDrawdown table.
 *  
 * @author tm
 *
 */
public class CommunityFirstRPISurplusSpoolFileGenerator extends
		SpoolFileGenerator {

	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	public static final int	DEFAULT_NUM_SIGNATORIES = 2;
	
	/** Width in characters for the value columns, not including pipe delimiters */
	private static final int VALUE_COLUMN_WIDTH = 21;
	
	private static final DecimalFormat CURRENCY_FORMAT = 
		new DecimalFormat("#,###,###,##0.00");

	private static final SimpleDateFormat REPORT_DATE_FORMAT = 
	 new SimpleDateFormat("dd MMMM yyyy");
	
	private static final String DEADLINE_TIME = "5PM";
	
	/** Prefix string for each CreateForm page template */
	private static final String REPORT_ID = "CFRPI";
	
	private RpiSurplusDetails rpiSurplusDetails;
	
	public CommunityFirstRPISurplusSpoolFileGenerator(SpoolHeader header) {
		super(header);
	}
	
	public CommunityFirstRPISurplusSpoolFileGenerator
	 (SpoolHeader header, RpiSurplusDetails rpiSurplusDetails) {
		super(header);
		
		this.rpiSurplusDetails = rpiSurplusDetails;
	}

	@Override
	public ByteArrayOutputStream createSpoolFile() throws IOException,
			DataException, ServiceException 
	{
		if (rpiSurplusDetails == null)
			throw new SpoolFileException("RPI Surplus details missing");
		
		ByteArrayOutputStream outputStream 	= new ByteArrayOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));

		this.header.setRecordCount(1);	
		this.writeNewHeader();
		
		this.writeFormBody();
		
		// EOF
		writer.write("#");
		
		writer.flush();
		writer.close();
		return outputStream;
	}
	
	/** Comm First spool header is different to previous ones. Includes comment lines.
	 * 
	 * @throws SpoolFileException
	 * @throws DataException
	 * @throws IOException
	 */
	protected void writeNewHeader() throws SpoolFileException, DataException, 
	 IOException {
		
		if (writer == null)
			throw new SpoolFileException("BufferedWriter not initialized");
		
		if (header == null)
			throw new SpoolFileException("SpoolHeader not initialized");
		
		addNameValueString("Company", header.company);
		addNameValueString("WorkingDate", header.workingDate);
		addNameValueString("User Name", header.userName);
		addNameValueString("Workstation", header.workstation);
		addNameValueString("PrintDate", header.printDate);

		addNameValueString("Records", Integer.toString(header.records));
		addNameValueString("SpoolFilePath", header.templatePath);
		
		addNameValueString("Comment", null);
		addNameValueString("Comment", "Community First RPI Drawdown");
		addNameValueString("Comment", "Created by " + header.userName + 
			" using WebCenter Content");
		addNameValueString("Comment", null);
	}

	private void writeFormBody() throws DataException, IOException {
		this.addReportHeader(REPORT_ID);				
		// Add postal address with correspondent name at the top
		addNameValueString("Correspondent", 
			rpiSurplusDetails.getCorrespondent().getCompleteName());
		
		// Output postal address
		Address postalAddress = rpiSurplusDetails.getAddress();
		
		Vector<String> postalAddressLines = 
		 postalAddress.getPrintableAddress(true, false);
		
		// Print the postal address over 8 lines.
		for (int j=0; j<8; j++) {
			String addressPiece = null;
			
			if (postalAddressLines.size() > j)
				addressPiece = postalAddressLines.get(j);
			
			addNameValueString("AddressLine" + (j+1), addressPiece);
		}
		
		addNameValueString
		 ("OrganisationId", rpiSurplusDetails.getOrg().getOrgAccountCode());
		addNameValueString
		 ("OrganisationName", rpiSurplusDetails.getOrg().getOrganisationName());
		
		// RPI Surplus table headers
		addNameValueString("DonorHeadings1", "               |                                        |                    1|                    2|                    3|                    4");
		addNameValueString("DonorHeadings2", "      AccNumExt|Donor Name                              |         Market Value|    RPI Indexed Value|        Surplus Value|   Surplus to be Sold");
		
		int numPrintedDonors = 0;
		
		// For each donor with an RPI surplus entry, print the donor name on one row,
		// and their surplus values on the next row.
		for (Element donor : rpiSurplusDetails.getDonors()) {
			
			RpiDrawdown donorDrawdown = rpiSurplusDetails.getDonorDrawdown
			 (donor.getElementId());
			
			if (donorDrawdown == null)
				continue; // No matching drawdown entry for this donor.
			
			Account donorAccount = rpiSurplusDetails.getDonorAccount
			 (donor.getElementId());
			
			if (donorAccount == null)
				throw new DataException("No Donor Account for donor ID " 
				 + donor.getElementId());
			
			// Determine donor name.
			String donorName = null;
			
			if (donor instanceof Person)
				donorName = ((Person)donor).getFullName();
			else if (donor instanceof Entity)
				donorName = ((Entity)donor).getOrganisationName();
			else
				throw new DataException("Donor Element ID " + donor.getElementId() + 
				 " was neither a person or organisation");
			
			donorName = donorName.trim();
			
			// Won't be printed - just here to ensure spool file has correct layout
			addNameValueString("DonorName", null);
			
			// Add the balance line.
			addNameValueString("DonorBalance", getDonorBalanceString
			 (donorAccount, donorName, donorDrawdown));
			
			numPrintedDonors++;
		}
		
		addNameValueString("DonorCount", Integer.toString(numPrintedDonors));
		
		// Add deadline dates etc.
		addNameValueString("DeadlineDate", DEADLINE_TIME + " on " + 
		 getPrettyDate(rpiSurplusDetails.getDeadlineDate()));
		addNameValueString("SaleDate", 
		 getPrettyDate(rpiSurplusDetails.getSaleDate()));
		addNameValueString("ReportDate",
		 getPrettyDate(rpiSurplusDetails.getReportDate()));
	}
	
	/** Builds and returns the string used to display the donor name and their surplus
	 *  amounts in columnar format.
	 *  
	 *  The string is delimited with pipe characters and space padding. Cash value figures
	 *  are right-justified in their columns.
	 *  
	 * @param donorAccount
	 * @param donorName
	 * @param drawdown
	 * @return
	 */
	private String getDonorBalanceString
	 (Account donorAccount, String donorName, RpiDrawdown drawdown) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("      ");
		
		// The 'donor account' is the AccNumExt with the fund code removed from the end.
		String accString = donorAccount.getAccNumExt().trim();
		accString = accString.substring(0, accString.length()-1);
		
		sb.append(accString);
		
		// Donor Name
		// ------
		sb.append("|");
		String donorNamePadded = CCLAUtils.padString(donorName, ' ', 40, PadType.RIGHT);
		
		if (donorNamePadded.length() > 40) // Truncate long donor name if necessary
			donorNamePadded = donorNamePadded.substring(0, 40);
		
		sb.append(donorNamePadded);
		
		// Market Value
		// ------
		sb.append("|");
		
		sb.append(CCLAUtils.padString
		 (CURRENCY_FORMAT.format(drawdown.getMarketValue().doubleValue()), ' ', VALUE_COLUMN_WIDTH));
		
		// RPI Indexed Value
		// ------
		sb.append("|");
		
		sb.append(CCLAUtils.padString
		 (CURRENCY_FORMAT.format(drawdown.getRpiValue().doubleValue()), ' ', VALUE_COLUMN_WIDTH));
		
		// Surplus Value
		// ------
		sb.append("|");
		
		// Set to zero if negative.
		double surplus = drawdown.getSurplusValue().doubleValue();
		
		if (surplus < 0D)
			surplus = 0D;
		
		sb.append(CCLAUtils.padString
		 (CURRENCY_FORMAT.format(surplus), ' ', VALUE_COLUMN_WIDTH));
		
		// Surplus to be Sold (entered by client)
		// ------
		sb.append("|");				
		
		return sb.toString();
	}
	
	/** Returns the date in the form '2nd December 2013'
	 * 
	 * @param date
	 * @return
	 */
	private static String getPrettyDate(Date date) {
		String monthDateStr = new SimpleDateFormat("d").format(date);
		int monthDate = Integer.parseInt(monthDateStr);
		
		return monthDateStr + CCLAUtils.getNumericSuffix(monthDate) + " " +
		 new SimpleDateFormat("MMMM yyyy").format(date);
	}

	/** Adds a report header to the BufferedWriter. */
	protected void addReportHeader(String reportId) 
	 throws IOException, DataException {

		writer.write(SpoolFileUtils.REPORT_DELIMITER);
		writer.newLine();
		addNameValueString("ReportID", reportId);
		
		addNameValueString("FormID", 
		 CCLAUtils.padString(
		 Integer.toString(header.formId), '0', 9));
		
		addNameValueString("StatementType", "RPI Calculation");
		addNameValueString("Company", header.company);
		addNameValueString("Campaign", "Community First");
		addNameValueString("CurrencySymbol", "\u00A3");
		
		addNameValueString("CalculationDate", getPrettyDate(rpiSurplusDetails.getCalcDate()));
		
		// Not shown anywhere on the printed form.
		addNameValueString("ReportDate", getPrettyDate(rpiSurplusDetails.getReportDate()));
	}
	
	/** Adds a name-value line to the BufferedWriter. */
	public void addNameValueString(String name, String value) 
	 throws DataException, IOException {
		addNameValueString(name, value, NAME_VALUE_PADDING);
	}

}
