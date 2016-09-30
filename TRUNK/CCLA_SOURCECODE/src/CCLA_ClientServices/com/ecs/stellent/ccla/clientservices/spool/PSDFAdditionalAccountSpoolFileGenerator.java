package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

import com.aurora.webservice.BankDetails;
import com.ecs.stellent.ccla.clientservices.form.PSDFRegistrationForm;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.PersonTitle;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;


/** Generates a pre-filled PSDF Additional Account Opening Form spool file.
 * 
 *  This will be addressed to the nominated correspondent/address for the client, as
 *  specified by the CampaignEnrolment data.
 *  
 *  It is expected that this form will only be sent to previously-enrolled PSDF clients,
 *  so it doesn't require as much data as the Registration Form.
 *  
 *  Each form is tied to a single pre-existing PC account belonging to the client.
 *  
 * @author Tom Marchant
 *
 */
public class PSDFAdditionalAccountSpoolFileGenerator extends SpoolFileGenerator {

	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	public static final int MAX_SIGNATORIES = 3;

	
	// CreateForm report IDs.
	
	/** Prefix string for each CreateForm page template */
	protected static String PAGE_REPORT_ID_PREFIX = "PSICADD";
	
	protected static class ReportSections {
		static final String ACCOUNT_DETAILS 			= "S1";
		static final String PAYMENT_DETAILS 			= "S2";
		static final String AUTHORISING_SIGS 			= "S3";
	}
	
	/** Container for all the data objects required to generate the spool file, e.g.
	 *  Organisation, Person, Contact Details.
	 *  
	 */
	protected PSDFRegistrationForm regForm;
	
	/** Page counters, used for printing 'Page x of y' captions on each report page.
	 * 
	 */
	int currentPage 	= 1;
	int totalPages 		= 3;
	
	public PSDFAdditionalAccountSpoolFileGenerator
	 (SpoolHeader spoolHeader, String docName,
	 Integer enrolmentId, PSDFRegistrationForm regForm) {
		
		super(spoolHeader, docName, enrolmentId);
		
		this.regForm	= regForm;
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
		 Integer.toString(this.regForm.getFormId()), '0', 9));
		
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
	
	public ByteArrayOutputStream createSpoolFile() 
	 throws IOException, DataException, ServiceException {
		
		if (regForm == null)
			throw new SpoolFileException(
			 "RegForm instance was empty or not initialized");
	
		ByteArrayOutputStream outputStream 	= new ByteArrayOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		// TODO: compute no. of records for header
		this.header.setRecordCount(1);
		
		this.writeNewHeader();
		
		// Add required report elements for the PSDFRegForm instance
		appendFormData();

		// EOF
		writer.write("#");
		
		writer.flush();
		writer.close();
		
		return outputStream;
	}
	
	/** Appends all required spool data for a single Mandate form.
	 * 
	 * @param regForm
	 * @throws IOException 
	 * @throws ServiceException 
	 */
	protected void appendFormData()
	 throws DataException, IOException, ServiceException {
		
		// Investor Details section (page 1)
		// ---------------------------------
		// Displays correspondent name, postal address and basic Org details
		addReportHeader(getReportId(ReportSections.ACCOUNT_DETAILS, null));
		
		// Add postal address with correspondent name at the top
		addNameValueString("Correspondent", 
		 regForm.getCorrespondent().getCompleteName());
		
		// Output postal address
		Address postalAddress = regForm.getContactAddress().getAddress();
		
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
		
		addNameValueString("OrgAccountCode", regForm.getEntity().getOrgAccountCode());
		addNameValueString("OrgName", 
		 regForm.getEntity().getOrganisationName());
		
		addNameValueString("AccountName", regForm.getAccount().getSubtitle());
		addNameValueString("AccountNumber", 
		 CCLAUtils.padString(
		  Integer.toString(regForm.getAccount().getAccountNumber()), '0', 8));
		
		addNameValueString("EmailIndemnityReceived", 
		 this.regForm.isEmailIndemnityReceived() ? "Y" : null);
		
		// Payment Details section
		// -----------------------
		// Contains bank account details and income distribution preferences
		addReportHeader(getReportId(ReportSections.PAYMENT_DETAILS, null));
		
		// Nominated bank account details
		BankAccount bankAccount = regForm.getBankAccount();
		
		addBankAccountDetails(bankAccount);
		
		// Fetch income dist method from the mandated PI account.
		String incDistMethod = regForm.getMandatedAccount().getIncomeDistMethod();

		boolean reinvestDividend = incDistMethod.equals
		 (Account.IncomeDistMethod.REIN.toString());
		
		addNameValueString("ReinvestDividend", reinvestDividend ? "Y" : null);
		
		if (reinvestDividend) {
			// If account is set for re-investment, add blank pay-away preferences
			addNameValueString("PayToNominatedAccount", null);
			addNameValueString("PayToOtherAccount", null);

			addBankAccountDetails(null);
		} else {
			BankAccount payAwayBankAccount = null;
			
			// Determine whether a different bank account has been configured to pay
			// away dividends
			if ((regForm.getBankAccount() != null
				 &&
				 regForm.getPayAwayBankAccount() != null)
				&&	
				(regForm.getBankAccount().getBankAccountId() != 
				regForm.getPayAwayBankAccount().getBankAccountId())) {
				
				payAwayBankAccount = regForm.getPayAwayBankAccount();
			}
			
			addNameValueString("PayToNominatedAccount", 
			 payAwayBankAccount == null ? "Y" : null);
			
			addNameValueString("PayToOtherAccount",  
			 payAwayBankAccount != null ? "Y" : null);
			
			addBankAccountDetails(payAwayBankAccount);
		}

		// Authorisation section
		// ----------------------
		// Contains details for up to 3 authorising signatories
		addReportHeader(getReportId(ReportSections.AUTHORISING_SIGS, null));
			
		// Add space for authorising signatories. There is up to 3 slots - the exact
		// number of slots printed is determined by the no. of required sigs on the
		// corresponding account.
		int numSigs = Math.min
		 (MAX_SIGNATORIES, regForm.getAccount().getRequiredSignatures());

		addNameValueString("NumberOfSignatories", Integer.toString(numSigs));
		
		for (int i=0; i<numSigs; i++) {
			String linePrefix = "Sig" + (i+1);

			addBasicPersonDetails(null, linePrefix);
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
	protected void addBasicPersonDetails(Person person, String linePrefix) 
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
	
	public static String getTickboxValue(Boolean val) {
		if (val == null) return null;
		return (val) ? "Y" : "N";
	}

	/** Adds Bank Account data fields for the passed BankAccount instance.
	 *  
	 *  The passed instance can be null.
	 *  
	 * @param bankAccount
	 * @throws IOException 
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	protected void addBankAccountDetails(BankAccount bankAccount) 
	 throws DataException, IOException, ServiceException {
		
		BankDetails bankDetails = null;
		
		if (bankAccount != null)
			bankDetails = bankAccount.getBankDetails();
		
		addNameValueString("BankName", 
		 bankDetails != null ? bankDetails.getBankName() : null);
		addNameValueString("BranchName",
		 bankDetails != null ? bankDetails.getBranchTitle() : null);
		
		addNameValueString("AccountName", 
		 bankAccount != null ? bankAccount.getAccountName() : null);
		addNameValueString("SortCode", 
		 bankAccount != null ? bankAccount.getSortCode() : null);
		addNameValueString("AccountNumber", 
		 bankAccount != null ? bankAccount.getAccountNumber() : null);
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