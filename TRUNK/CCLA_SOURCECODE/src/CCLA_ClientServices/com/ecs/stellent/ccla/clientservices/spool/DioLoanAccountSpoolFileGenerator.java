package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.AccountForm;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.NumberToWords;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.PersonTitle;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;

/** Generates 2 types of form for a single Dio Loan account:
 * 
 *  -Subscrption (purchase of units by the client)
 *  -Redemption (sale of units by the client)
 * 
 * @author Tom
 *
 */
public class DioLoanAccountSpoolFileGenerator  extends SpoolFileGenerator {

	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	/** Prefix string for each CreateForm page template */
	private String PAGE_REPORT_ID_PREFIX = null;

	public static final int MAX_SIGNATORIES = 3;
	
	private static class ReportSections {
		static final String COVER_PAGE				= "S1";
		static final String PAYMENT_METHOD_AND_SIGS = "S23";
	}
	
	/** Container for all the data objects required to generate the spool file, e.g.
	 *  Organisation, Person, Contact Details.
	 *  
	 */
	private AccountForm accountForm;
	
	/** Page counters, used for printing 'Page x of y' captions on each report page.
	 * 
	 */
	int currentPage 	= 1;
	int totalPages 		= 2;
	
	public DioLoanAccountSpoolFileGenerator(SpoolHeader spoolHeader, String docName,
	 Integer enrolmentId, AccountForm subsForm) {
		
		super(spoolHeader, docName, enrolmentId);
		this.accountForm	= subsForm;
	}
	
	/** Adds a name-value line to the BufferedWriter. */
	public void addNameValueString(String name, String value) 
	 throws DataException, IOException {
		addNameValueString(name, value, NAME_VALUE_PADDING);
	}
	
	/** Returns the given name/value pair formatted for use in a
	 *  spool file.
	 *  
	 * @param name
	 * @param value
	 * @return
	 */
	public static String getSpoolNameValueString(String name, String value)
	 throws DataException {
		
		String nameValue 	= SpoolFileUtils.getSpoolValueLabel
		 (name, NAME_VALUE_PADDING);
		
		if (value != null)
			nameValue 			+= value;
		
		return nameValue;
	}
	
	/** Adds a report header to the BufferedWriter.
	 * 
	 *  Includes FormID and PageNumber label.
	 *  
	 **/
	protected void addReportHeader(String reportId) 
	 throws IOException, DataException {
		
		writer.write(SpoolFileUtils.REPORT_DELIMITER);
		writer.newLine();
		
		addNameValueString("ReportID", reportId);

		// Add Form ID and PageNumber under every page delimiter.
		addNameValueString("FormID", 
		 CCLAUtils.padString(
		 Integer.toString(this.accountForm.getForm().getFormId()), '0', 9));
		
		addNameValueString
		 ("PageNumber", "Page " + (currentPage++) + " of " + totalPages);
	}

	/** Constructs and returns a CreateForm Report ID. If pageNumber is non-null,
	 *  this will be in the form:
	 *   
	 *   PAGE_REPORT_ID_PREFIX + sectionRef + "P" + pageNumber
	 *   
	 *  Otherwise:
	 *  
	 *   PAGE_REPORT_ID_PREFIX + sectionRef
	 *  
	 * @param sectionRef
	 * @param pageNumber
	 * @return
	 */
	public String getReportId(String sectionRef, Integer pageNumber) {
		return PAGE_REPORT_ID_PREFIX + sectionRef + 
				(pageNumber != null ? "P" + pageNumber : ""); 
	}
	
	@Override
	public ByteArrayOutputStream createSpoolFile() 
	 throws IOException, DataException, ServiceException {
		
		if (accountForm == null)
			throw new SpoolFileException(
			 "Subs Form instance was empty or not initialized");
	
		ByteArrayOutputStream outputStream 	= new ByteArrayOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		this.header.setRecordCount(1);
		
		this.writeNewHeader();
		
		appendFormData();

		// EOF
		writer.write("#");
		
		writer.flush();
		writer.close();
		
		return outputStream;
	}
	
	private void appendFormData() throws IOException, DataException {
		
		// Determine Report ID prefix from form type.
		FormType formType = accountForm.getForm().getFormType();
		
		if (formType.getFormTypeId() == FormType.DIOLOAN_DRAWDOWN)
			PAGE_REPORT_ID_PREFIX = "CBFDLDD";
		else if (formType.getFormTypeId() == FormType.DIOLOAN_REDEMPTION)
			PAGE_REPORT_ID_PREFIX = "CBFDLRED";
		else 
			throw new DataException("Unable to generate Dio Loan Account Form " +
			 "for form type: " + formType.getName());
			
		// Cover page (page 1)
		// ---------------------------------
		// Displays correspondent name, postal address, basic Org/Account details plus
		// the transfer amount (never pre-populated, specified by the client)
		addReportHeader(getReportId(ReportSections.COVER_PAGE, null));

		// Add postal address with correspondent name at the top
		addNameValueString("Correspondent", 
		 accountForm.getCorrespondent().getCompleteName());
		
		// Output postal address
		Address postalAddress = accountForm.getPostalAddress().getAddress();
		
		Vector<String> postalAddressLines = 
		 postalAddress.getPrintableAddress(true, false);
		
		// Print the postal address over 8 lines.
		for (int j=0; j<8; j++) {
			String addressPiece = null;
			
			if (postalAddressLines.size() > j)
				addressPiece = postalAddressLines.get(j);
			
			addNameValueString(
			 "AddressLine" + (j+1), addressPiece);
		}
		
		//addNameValueString("LetterDate", EIGHT_CHAR_DATE_FORMAT.format(new Date()));
		
		addNameValueString("OrgAccountCode", 
		 accountForm.getOrganisation().getOrgAccountCode());
		addNameValueString("OrgName", 
		 accountForm.getOrganisation().getOrganisationName());
		
		addNameValueString("AccountName", accountForm.getAccount().getSubtitle());
		addNameValueString("AccountNumber", 
		 CCLAUtils.padString(
		  Integer.toString(accountForm.getAccount().getAccountNumber()), '0', 8));
		
		if (formType.getFormTypeId() == FormType.DIOLOAN_DRAWDOWN) {
			// Add intention amount, as whole number (no decimals)
			
			NumberFormat cashAmountFormat = NumberFormat.getInstance();
			cashAmountFormat.setGroupingUsed(false);
			cashAmountFormat.setMaximumFractionDigits(0);
			
			addNameValueString("AmountOfLoanRequest", 
			 cashAmountFormat.format(accountForm.getIntention().getCash()));
			 
			addNameValueString("AmountInWords", 
					NumberToWords.convertToGBP(
							new BigDecimal(accountForm.getIntention().getCash())
								).toUpperCase());
			
		} else if (formType.getFormTypeId() == FormType.DIOLOAN_REDEMPTION) {
			// Client will specify these, always leave blank.
			addNameValueString("RepaymentAmount", null);
			addNameValueString("AmountInWords", null);
		}
		
		addNameValueString("EmailIndemnityReceived", 
		 this.accountForm.isEmailIndemnityReceived() ? "Y" : null);
		
		// Payment Method and Signatories (page 2)
		// ---------------------------------------
		addReportHeader(getReportId(ReportSections.PAYMENT_METHOD_AND_SIGS, null));
		
		if (formType.getFormTypeId() == FormType.DIOLOAN_DRAWDOWN) {
			
			String loanType = null;
			
			if (accountForm.getAccount().getLoanType() 
				== Account.LoanType.INTERST_ONLY)
				loanType = "InterestOnly";
			else
				loanType = "Repayment";
			
			addNameValueString("TypeOfLoan", loanType);
			
			addNameValueString("TermOfLoanMonths",
			 accountForm.getAccount().getLoanTerm().toString());
			
		} else if (formType.getFormTypeId() == FormType.DIOLOAN_REDEMPTION) {
			// TODO
			addNameValueString("PaymentMethod", null);
			addNameValueString("PaymentDate", null);
			addNameValueString("ReferenceNumber", null);
		}
		
		// Calculate no. of empty signature sections to display. Minimum number out of 
		// MAX_SIGNATORIES and the number of required signatories for the given account.
		int numSigs = Math.min
		 (MAX_SIGNATORIES, accountForm.getAccount().getRequiredSignatures());
		
		addNameValueString("NumberOfSignatories", Integer.toString(numSigs));
		
		for (int i=0; i<numSigs; i++) {
			addBasicPersonDetails(null, "Sig" + (i+1));
		}
	}
	
	/** Adds title/forename/surname plus a blank date field, mapped to the given
	 *  Person.
	 *  
	 * @param person
	 * @param linePrefix
	 * @throws IOException 
	 * @throws DataException 
	 * @throws IOException 
	 */
	private void addBasicPersonDetails(Person person, String linePrefix) 
	 throws DataException, IOException {
		
		String title = null;
		
		if (person != null) {
			Integer titleId = person.getTitleId();
			
			if (titleId != null)	
				title = PersonTitle.getCache().getCachedInstance(titleId).getTitle();
			else
				throw new DataException("Person " + person.getFullName() + " has " +
				 "incomplete name record. Ensure the Title/First Name/Surname fields " +
				 "are set.");
		}
		
		addNameValueString(linePrefix + "Title", title);
		addNameValueString(linePrefix + "Forename", person != null ?
		 person.getFirstName() : null);
		addNameValueString(linePrefix + "Surname", person != null ?
		 person.getLastName() : null);
		
		addNameValueString(linePrefix + "Date", null);		
	}
	
	/** Writes the SpoolHeader contents to the BufferedWriter instance.
	 * 
	 *  Overriden here to account for different padding size.
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
		addNameValueString("ClientNumber", header.clientNumber);
		addNameValueString("WorkingDate", header.workingDate);
		addNameValueString("User Name", header.userName);
		addNameValueString("Workstation", header.workstation);
		addNameValueString("PrintDate", header.printDate);

		addNameValueString("Records", Integer.toString(header.records));
		addNameValueString("TemplatePath", header.templatePath);
		
		addNameValueString("FormID", CCLAUtils.padString(
		 Integer.toString(header.formId), '0', BARCODE_PADDING));
		
		addNameValueString("OrganisationID", Integer.toString(header.orgId));
		
		addNameValueString("ReleaseVersion", header.releaseVersion);
	}
}
