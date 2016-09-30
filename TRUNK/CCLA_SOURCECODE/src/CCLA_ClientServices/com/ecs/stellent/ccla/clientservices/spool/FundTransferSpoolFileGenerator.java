package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.Workspace;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.aurora.webservice.Address;
import com.aurora.webservice.Client;
import com.aurora.webservice.Correspondent;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.aurora.AuroraWebServiceHandler;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.UCMForm;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Generates a Create!Form spool file, which contains name-value 
 *  pairs required for form generation. 
 *  
 *  @deprecated not tested in a long time. Based on a very old build of the
 *  ClientServices component. Don't trust this class.
 *  
 *  @author Tom Marchant
 */
public class FundTransferSpoolFileGenerator extends SpoolFileGenerator {
	
	public static final String FORM_TYPE = "EthicalFundTransfer_Confirm";
	
	/* Create!Form Report IDs */
	// Fund Transfer cover letter
	static final String COVER_LETTER_REPORT_ID = "TUEFLTR";
	// Fund Transfer cover letter, with AML request
	static final String AML_COVER_LETTER_REPORT_ID = "TUEFLTRAML";
	// Client fund transfer form (may appear more than once in a single spool)
	static final String CLIENT_FUND_TRANSFERS_REPORT_ID = "TUEFSEC12";
	// Terms and Conditions page
	static final String TERMS_AND_CONDS_REPORT_ID = "TUEFSEC4";
	
	private Correspondent correspondent;
	
	private boolean coverLetter = false;
	private boolean amlNotify = false;
	
	/** Vector of FundTransferSpool instances */
	private Vector transfers;
	
	private FWFacade facade;
	
	/** Takes an extra Correspondent argument.
	 * 
	 *  By default, the covering letter flag is set but the
	 *  AML notification is not.
	 *
	 * @param spoolHeader
	 * @param docName
	 * @param processId
	 * @param corr Correspondent whom the transfer form(s) will apply
	 */
	public FundTransferSpoolFileGenerator(SpoolHeader spoolHeader,
	 String docName, Integer processId, 
	 Correspondent corr) {
		super(spoolHeader, docName, processId);
		
		this.correspondent 	= corr;
		
		this.coverLetter 	= true;
		this.amlNotify		= false;
		
		this.transfers		= null;
		this.facade			= null;
	}
	
	/** Sets whether or not to include a covering letter. */
	public void setCoverLetter(boolean coverLetter) {
		this.coverLetter = coverLetter;
	}
	
	/** Sets whether or not to include an AML notification on the
	 *  covering letter. */
	public void setAmlNotify(boolean amlNotify) {
		this.amlNotify = amlNotify;
	}
	
	/** Sets the list of FundTransferSpool instances.
	 * 
	 * @param transfers
	 */
	public void setTransfers(Vector transfers) {
		this.transfers = transfers;
	}
	
	/** Sets the Workspace instance.
	 * 
	 * @param transfers
	 */
	public void setFacade(FWFacade facade) {
		this.facade = facade;
	}
	
	
	/** Wrapper class for holding information for a single fund transfer 
	 *  on the form.
	 *  
	 * @author Tom Marchant
	 */
	public class FundTransferSpool {
		public String clientId;
		public String accountName;
		public String accountNumberExt;
		public String transType;
		public double unitsValue;
		public double numberOfUnits;
		
		public FundTransferSpool() {}
		
		public FundTransferSpool(String clientId, String accountName, 
		 String accountNumberExt, 
		 String transType, double unitsValue, double numberOfUnits) {
			this.clientId			= clientId;
			this.accountName 		= accountName;
			this.accountNumberExt 	= accountNumberExt;
			this.transType 			= transType;
			this.unitsValue 		= unitsValue;
			this.numberOfUnits 		= numberOfUnits;
		}
	}
	
	/** Factory method for generating empty FundTransferSpool
	 *  instance.
	 *  
	 * @return new FundTransferSpool instance
	 */
	public FundTransferSpool createFundTransferSpool() {
		FundTransferSpool transfer = new FundTransferSpool();
		return transfer;
	}
	
	public ByteArrayOutputStream createSpoolFile() 
	 throws IOException, DataException, ServiceException {
		
		if (correspondent == null)
			throw new SpoolFileException("Correspondent was not initialized.");
		
		if (transfers == null)
			throw new SpoolFileException("Transfer list was not initialized.");
		
		ByteArrayOutputStream outputStream 	= new ByteArrayOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		// Generate HashMap which maps clients -> account transfers.
		// We can use this to determine how many Transfer Forms must be generated.
		HashMap clientTransferMap = getClientTransferMapping(transfers);
		Iterator clientIter = clientTransferMap.entrySet().iterator();
		
		int numRecords = clientTransferMap.size();
		
		// Increment record count if cover letter is required
		if (coverLetter)
			numRecords++;
		
		// Insert header elements
		this.writeHeader();
		
		// Add cover letter if required.
		if (coverLetter) {
			// A slightly altered letter is used if the AML check
			// flag has been set.
			if (amlNotify) {
				addCoverLetter(AML_COVER_LETTER_REPORT_ID);
			} else {
				addCoverLetter(COVER_LETTER_REPORT_ID);
			}
		}
		
		while (clientIter.hasNext()) {
			Map.Entry clientTransfersEntry	= (Map.Entry)clientIter.next(); 
			
			String clientId 		= (String)clientTransfersEntry.getKey();
			Vector clientTransfers	= (Vector)clientTransfersEntry.getValue();
			
			addClientTransferForm(clientId, clientTransfers, facade);
		}
		
		// Print T&C page
		SpoolFileUtils.addReportHeader(writer, TERMS_AND_CONDS_REPORT_ID);
		
		// EOF
		writer.write("#");
		
		writer.flush();
		writer.close();
		
		return outputStream;
	}
	
	/** Generates the Fund Transfer spool file as as byte stream.
	 *  
	 *  This requires a fully-populated FundTransferHeader instance.
	 *  
	 *  The passed Vector should be a set of FundTransferSpool instances.
	 *  These will be divided up into client sub-sets, each of these
	 *  sub-sets will generate its own Report section in the spool file.
	 *  
	 *  A total of 2 cover letters can be included in the spool file,
	 *  based on the passed flags. The first of these is the Fund Transfer
	 *  cover letter, generally this will always be included.
	 *  
	 *  The AML cover letter should only be included if the client has
	 *  an outstanding AML check.
	 *  
	 *  A Terms & Conditions page will always be printed at the end of
	 *  the spool file.
	 *  
	 *  @param spoolHeader 			fully-populated FundTransferSpoolHeader
	 *  @param transferCoverLetter 	determines whether the standard transfer
	 *  							covering letter is included
	 *  @param amlLetter			determines whether the AML check paragraph
	 *  							is included in the covering letter
	 *  @param transfers			Vector of FundTransferSpool instances for
	 *  							correspondent.
	 *  @param workspace			used to fetch stored client/correspondent/
	 *  							account data in UCM
	 *  
	 *  @return						spool file as a byte stream
	 **/
	/*
	public static ByteArrayOutputStream createFundTransferSpoolXXX
	 (SpoolHeader spoolHeader, 
	  boolean transferCoverLetter, boolean amlLetter,
	  Vector transfers, Workspace workspace) 
	 throws IOException, DataException, ServiceException {
		
		ByteArrayOutputStream outputStream 	= new ByteArrayOutputStream();
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		// Generate HashMap which maps clients -> account transfers.
		// We can use this to determine how many Transfer Forms must be generated.
		HashMap clientTransferMap = getClientTransferMapping(transfers);
		Iterator clientIter = clientTransferMap.entrySet().iterator();
		
		int numRecords = clientTransferMap.size();
		
		// Increment record count if cover letter is required
		if (transferCoverLetter)
			numRecords++;
		
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("Company", spoolHeader.company));
		writer.newLine();
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("WorkingDate", spoolHeader.workingDate));
		writer.newLine();
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("User Name", spoolHeader.userName));
		writer.newLine();
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("Workstation", spoolHeader.workstation));
		writer.newLine();
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("PrintDate", DateFormatter.getTimeStamp()));
		writer.newLine();
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("Records", Integer.toString(numRecords)));
		writer.newLine();
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("TemplatePath", spoolHeader.templatePath));
		writer.newLine();
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("AnacompDocType", spoolHeader.anacompDocType));
		writer.newLine();
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("AnacompRetGrpNo", spoolHeader.anacompRetGrpNo));
		writer.newLine();
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("ReleaseVersion", spoolHeader.releaseVersion));
		writer.newLine();
		
		// Add cover letter if required.
		if (transferCoverLetter) {
			// A slightly altered letter is used if the AML check
			// flag has been set.
			if (amlLetter) {
				addCoverLetter(AML_COVER_LETTER_REPORT_ID, spoolHeader);
			} else {
				addCoverLetter(COVER_LETTER_REPORT_ID, spoolHeader);
			}
		}
		
		while (clientIter.hasNext()) {
			Map.Entry clientTransfersEntry	= (Map.Entry)clientIter.next(); 
			
			String clientId 		= (String)clientTransfersEntry.getKey();
			Vector clientTransfers	= (Vector)clientTransfersEntry.getValue();
			
			addClientTransferForm(clientId, clientTransfers, workspace);
		}
		
		// Print T&C page
		SpoolFileUtils.addReportHeader(writer, TERMS_AND_CONDS_REPORT_ID);
		
		// EOF
		writer.write("#");
		
		writer.flush();
		writer.close();
		
		return outputStream;
	}
	*/
	
	/** Appends a cover letter Report section to the BufferedWriter.
	 *  The passed reportId must correspond to a valid cover letter
	 *  Create!Form report ID.
	 *  
	 * @param writer
	 * @param reportId
	 * @param spoolHeader
	 */
	private void addCoverLetter(String reportId)
	 throws DataException, IOException {
		
		SpoolFileUtils.addReportHeader(writer, reportId);
		
		String letterDate = COVER_LETTER_DATE_FORMAT.format
		 (this.header.printDate);
		
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("LetterDate", letterDate));
		writer.newLine();
		
		writer.write(SpoolFileUtils.getSpoolNameValueString
		 ("Correspondent", this.correspondent.getSalutation()));
		writer.newLine();
		
		addAddress(this.correspondent.getAddress());
	}
	
	/** Appends the Report elements to the BufferedWriter required to
	 *  render a single client transfer form.
	 *  
	 *  The transfers Vector is expected to contain a set of
	 *  transfers for the given clientId.
	 */
	private void addClientTransferForm
	 (String entityId, Vector transfers, FWFacade facade) 
	  throws IOException, DataException, ServiceException {
		
		String clientId = null;
		
		String formIdStr = "000000000";
		
		String corrId = Integer.toString(this.correspondent.getCorrespondentCode());
		
		Company coifCompany = Company.getCache().getCachedInstance(Company.COIF);
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (Integer.parseInt(entityId), coifCompany, facade);
		
		// Fetch form ID
		if (facade != null) {
			// Check for existing form ID
			Integer formId = FormUtils.getFormId(
			 Integer.toString(this.processId), FORM_TYPE, entityId, 
			 corrId, facade);
			
			if (formId == null) {
				// Create new form mapping
				UCMForm form = UCMForm.add(this.processId, 
				 FORM_TYPE, null, Integer.parseInt(entityId),
				 auroraClient,
				 Integer.parseInt(corrId), 
				 this.docName, null, facade);
		
				/*
				Form form = Form.add(Integer.parseInt(this.processId), 
				 FORM_TYPE, null, Integer.parseInt(clientId), "COIF", 
				 Integer.parseInt(corrId), 
				 this.docName, null, facade);
				*/
				
				formId = form.getFormId();
			}
			
			formIdStr = formId.toString();
			
			// Always pad form ID to 9 digits
			while (formIdStr.length() < 9)
				formIdStr = "0" + formIdStr;
		} else {
			Log.warn("Unable to generate new form ID, passed Workspace was null.");
		}
		
		Log.debug("Creating client transfer spool: " +
		 "correspondent=" + corrId + ", client=" + 
		 clientId + ", form ID=" + formIdStr);
		
		SpoolFileUtils.addReportHeader(writer, CLIENT_FUND_TRANSFERS_REPORT_ID);
		
		writer.write(SpoolFileUtils.getSpoolNameValueString("ClientID", clientId));
		writer.newLine();
		
		// UCM form ID
		writer.write(SpoolFileUtils.getSpoolNameValueString("DocumentID", formIdStr));
		writer.newLine();
		writer.write(SpoolFileUtils.getSpoolNameValueString("Correspondent", 
		 this.correspondent.getName()));
		writer.newLine();
		
		// Correspondent address
		addAddress(this.correspondent.getAddress());
		
		// Client name. Must be fetched via Aurora web service call
		String clientName = null;
		
		try {
			Client client 	= AuroraWebServiceHandler.getAuroraWS().
							   getClientByClientNumber
							   ("COIF", Integer.parseInt(clientId));
			
			clientName 	= client.getName();
		} catch (Exception e) {
			Log.error("Unable to resolve client name for client ID: " + clientId);
		}
		
		writer.write(SpoolFileUtils.getSpoolNameValueString("Client", clientName));
		writer.newLine();
		
		// Add all account transfer information, spread across multiple reports.
		for (int i=0; i<transfers.size(); i++) {
			FundTransferSpool transfer = (FundTransferSpool)transfers.get(i);
			int index = i+1;
			
			if (index <= 8) {
				// First 8 accounts go on the first Report. Index value remains the same
				addAccountTransfer(transfer, index);
			} else {
				if (index == 9) {
					// Add first account page Report Header
					SpoolFileUtils.addReportHeader(writer, "TUEFSEC2");
				}
				
				index = (index - 8) % 15;
				
				if (index == 0) {
					addAccountTransfer(transfer, 15);
					
					// We've gone through another 15 accounts. Add another Report Header
					SpoolFileUtils.addReportHeader(writer, "TUEFSEC2");
				} else {
					addAccountTransfer(transfer, index);
				}
			}
		}
		
		SpoolFileUtils.addReportHeader(writer, "TUEFSEC34");
	}
	
	private void addAccountTransfer(FundTransferSpool transfer, int index) 
	 throws IOException, DataException {
	
		writer.write(
		 SpoolFileUtils.getSpoolNameValueString("AccNo" + index, transfer.accountNumberExt));
		writer.newLine();
		
		writer.write(
		 SpoolFileUtils.getSpoolNameValueString("AccNo" + index + "Name", transfer.accountName));
		 writer.newLine();
		
		// Ensure first letter of transfer type is upper-case
		String transType = transfer.transType;
		
		boolean undecided	= false;
		boolean transferAll = false;
		
		if (transType.equals("all")) {
			transType = "All";
			transferAll = true;
		} else {
			if (transType.equals("undecided"))
				undecided = true;
			
			transType = "Amount";
		}
		
		writer.write(
		 SpoolFileUtils.getSpoolNameValueString("AccNo" + index + "TransType", transType));
		writer.newLine();
		
		if (transferAll) {
			writer.write(
			 SpoolFileUtils.getSpoolNameValueString("AccNo" + index + "NoOfUnits", null));
			writer.newLine();
		} else {
			String numUnits = null;
			
			// If the transfer type is 'undecided', it needs to be printed with
			// a blank amount value.
			if (!undecided) {
				// Correctly format the unit value (puts a cap on no. of decimal places)
				numUnits = UNIT_COUNT_FORMAT.format(transfer.numberOfUnits);
			
				// Ensure units value is padded to 8 characters in length
				while (numUnits.length() < 8)
					numUnits = " " + numUnits;
			}
				
			writer.write(
			 SpoolFileUtils.getSpoolNameValueString("AccNo" + index + "NoOfUnits", numUnits));
			writer.newLine();
		}
	}
	
	/** Adds an address to the BufferedWrtier, in normalized format 
	 *  (i.e. AddressLine1, AddressLine2)
	 *  
	 * @param writer
	 * @param transfer
	 * @throws IOException
	 * @throws DataException
	 */
	private void addAddress(Address address) 
	 throws IOException, DataException {
		
		if (address != null) {
			Vector normalizedAddress = 
			 SpoolFileUtils.getNormalizedAuroraAddress(address);
			
			for (int i=1; i<=8; i++) {
				String thisAddressLine = null;
				
				if (normalizedAddress.size() >= i)
					thisAddressLine = (String)normalizedAddress.get(i-1);
				
				writer.write(SpoolFileUtils.getSpoolNameValueString
				 ("AddressLine" + i, thisAddressLine));
				writer.newLine();
			}
		} else {
			Log.warn("Passed address to addAddress method was null - " +
			 "unable to append address to spool file.");
		}
	}
	
	/** Takes a Vector of FundTransferSpool instances and
	 *  returns a HashMap:
	 *  
	 *  The keys of the HashMap relate to client ID strings.
	 *  
	 *  The values are Vectors of FundTransferSpool instances
	 *  which belong to the given client. 
	 * 
	 * @param transfers
	 */
	private static HashMap getClientTransferMapping(Vector transfers) {
		
		HashMap clientTransfers = new HashMap();
		
		for (int i=0; i<transfers.size(); i++) {
			FundTransferSpool thisTransfer = 
			 (FundTransferSpool)transfers.get(i);
		
			String thisClientId = thisTransfer.clientId;
			
			if (clientTransfers.containsKey(thisClientId)) {
				// Add this transfer to existing Vector
				Vector v = (Vector)clientTransfers.get(thisClientId);
			
				v.add(thisTransfer);
			} else {
				// Create new Vector and add new HashMap entry
				Vector v = new Vector();
				v.add(thisTransfer);
				
				clientTransfers.put(thisClientId, v);
			}
		}
				
		return clientTransfers;
	}
	
	public static void main (String[] args) 
	 throws IOException, DataException, ServiceException {
		
		File file = new File("testspool.txt");
        
		Correspondent corr = new Correspondent();
		Vector transfers = new Vector();

		SpoolHeader header = new SpoolHeader
		 ("COIF", "00001", "CCLA\\adminTM", 0, null, null, "path.txt");
		
		FundTransferSpoolFileGenerator g = 
		 new FundTransferSpoolFileGenerator
		 (header, "DOC_NAME", 123, corr);
		
		for (int i=0; i<50; i++) {
			FundTransferSpool transfer = g.new FundTransferSpool
			 ("00" + (i%3), "accName" + i, (i*10) + "D", "All", 1234.56D, 1234); 
			
			transfers.add(transfer);
		}
		
		g.setTransfers(transfers);
		
		FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(g.createSpoolFile().toByteArray());
	}
}
