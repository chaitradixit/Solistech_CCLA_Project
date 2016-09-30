package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.MandateForm;
import com.ecs.ucm.ccla.data.*;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;


/** Generates a pre-filled Mandate Form spool file.
 * 
 *  This will be addressed to a single account correspondent, who requires
 *  an AML check on one or more of his accounts for a given client.
 *  
 *  The passed client data will also be displayed on the form. 
 *  
 *  A single correspondent may receive multiple versions of this form,
 *  if they have outstanding accounts for more than one client.
 *  
 *  
 * @author Tom Marchant
 *
 */
public class MandateSpoolFileGenerator extends SpoolFileGenerator {

	public static final String FORM_TYPE 			= "Mandate";
	
	// Form sub-types
	public static final String FORM_SUBTYPE_SINGLE_ACCOUNT 
	 = "Single account";
	public static final String FORM_SUBTYPE_MULTI_ACCOUNTS 
	 = "Multiple accounts";
	public static final String FORM_SUBTYPE_MULTI_MIXED_ACCOUNTS 
	 = "Multiple mixed accounts";
	
	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 26;
	
	private static final int MAX_LISTED_ACCOUNTS 	= 20;
	private static final int MAX_ACCOUNTS_PAGE1		= 14;
	
	private static final int MAX_LISTED_SIGNATORIES	= 3;
	
	private static final int MAX_LISTED_TRUSTEES 	= 8;
	private static final int MAX_LISTED_AUTH_TRUSTEES	= 3;

	private static final int NUM_BLANK_PAGES = 0; // max = 3
	
	// CreateForm report IDs.
	private String FRONT_PAGE_REPORT_ID;
	private String PAGE_REPORT_ID_PREFIX;
	private String CHECKLIST_REPORT_ID;
	private String COVERLETTER_REPORT_ID;
	
	private Entity entity;
	private AuroraClient auroraClient;
	private MandateForm mandateForm;
	
	public MandateSpoolFileGenerator(SpoolHeader spoolHeader, String docName,
	 Integer processId, Entity entity, AuroraClient auroraClient, 
	 MandateForm mandateForm) {
		super(spoolHeader, docName, processId);
			
		this.entity			= entity;
		this.auroraClient	= auroraClient;
		
		this.mandateForm	= mandateForm;
		
		FRONT_PAGE_REPORT_ID	= "MAN" + header.company + "TP";
		PAGE_REPORT_ID_PREFIX	= "MAN" + header.company + "P";
		CHECKLIST_REPORT_ID		= "MAN" + header.company + "CL";
		COVERLETTER_REPORT_ID	= "MAN" + header.company + "COVLET";
	}
	
	/** Adds a name-value line to the BufferedWriter. */
	public void addNameValueString(String name, String value) 
	 throws DataException, IOException {
		
		String nameValue = getSpoolNameValueString(name, value);
		writer.write(nameValue);
		writer.newLine();
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
	 *  Overriden from superclass to deal with quirky report headers.
	 *  Most report headers in the mandate form have the Client ID
	 *  included just after the tilde and before the Report ID itself.
	 *  
	 *  */
	protected void addReportHeader(String reportId) 
	 throws IOException, DataException {
		
		writer.write(SpoolFileUtils.REPORT_DELIMITER);
		writer.newLine();
		
		addNameValueString("ReportID", reportId);
		addNameValueString("ClientID", auroraClient.getPaddedClientNumber());
	}
	
	public ByteArrayOutputStream createSpoolFile() 
	 throws IOException, DataException, ServiceException {
		
		if (mandateForm == null)
			throw new SpoolFileException(
			 "MandateForm instance was empty or not initialized");
	
		ByteArrayOutputStream outputStream 	= new ByteArrayOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		// TODO: compute no. of records for header
		this.header.setRecordCount(1);
		
		this.writeHeader();
		
		// Add required report elements for the MandateForm instance
		appendFormData(mandateForm);

		// EOF
		writer.write("#");
		
		writer.flush();
		writer.close();
		
		return outputStream;
	}
	
	/** Appends all required spool data for a single Mandate form.
	 * 
	 * @param mandateForm
	 * @throws IOException 
	 * @throws ServiceException 
	 */
	private void appendFormData(MandateForm mandateForm)
	 throws DataException, IOException, ServiceException {
		
		Person correspondent 		= mandateForm.getCorrespondent();
		Vector<Person> signatories	= mandateForm.getSignatories();
		
		// Covering letter.
		addCoveringLetter(correspondent);
		
		// Checklist page.
		addReportHeader(CHECKLIST_REPORT_ID);
		
		// Header page. Displays barcode (form ID)
		addReportHeader(FRONT_PAGE_REPORT_ID);
		// Form ID
		addNameValueString("FormID", 
		 SpoolFileUtils.getPaddedBarcode(mandateForm.getFormId()));
		
		// First page. Displays Client Name and list of affected accounts
		addReportHeader(getReportId(1));
		
		addNameValueString("Date", null);
		addNameValueString("CharityName", this.entity.getOrganisationName());
		addNameValueString("CharityNameChange", null);
		
		addAccountNumbers(mandateForm.getAccounts());
		
		// If this is a CBF form, an extra Charity PCC no. field is required.
		if (header.company.equals("CBF"))
			addNameValueString("CharityIsPCC", null);
		
		addNameValueString("CharityRegistered", null);
		addNameValueString("CharityRegNumber", null);
		addNameValueString("HMRCReferenceNo", null);
		
		// Normalized address over 3 lines.
		// Don't include the Country field here - the data is currently invalid
		// (used to store telephone numbers!)
		
		Address.AddressElement[] elems = 
		 new Address.AddressElement[] {
			Address.AddressElement.FLAT,
			Address.AddressElement.HOUSE_NAME,
			Address.AddressElement.HOUSE_NUMBER,
			Address.AddressElement.STREET,
			Address.AddressElement.CITY
		};
		
		Vector<String> clientAddress = new Vector<String>();
		boolean clientHasAddress = false;
		
		Vector<Contact> entityContacts = entity.getContacts();
		if (entityContacts == null)
			Log.warn("Unable to fill Entity contact data, " +
			 "contact map not initialized");
		
		// Attempt to find the default Entity address
		Contact entityAddress =  
		 Contact.getDefaultContact(entityContacts, Contact.ADDRESS);
		
		// Attempt to find the default Entity phone number
		Contact entityPhone = 
		 Contact.getDefaultContact(entityContacts, Contact.PHONE);
		
		if (entityAddress != null) {
			clientHasAddress = true;
			clientAddress = entityAddress.getAddress().getNormalizedAddress(elems);
		}
		 
		if (clientAddress.size() > 0)
			addNameValueString("OfficeAddress", "Y");
		else
			addNameValueString("OfficeAddress", null);
		
		clientAddress = SpoolFileUtils.wrapToLines
		 (clientAddress, new int[] {23,23,23,23}, false);
		
		// SpoolFileUtils.getNormalizedCCLAAddress(client.getAddress(), false, false);
		
		for (int j=0; j<4; j++) {
			String addressPiece = null;
			
			if (clientAddress.size() > j)
				addressPiece = clientAddress.get(j);
			
			addNameValueString(
			 "CharityOfficeAddress" + (j+1), addressPiece);
		}
		
		String clientPostCode 	= null;
		String clientTel		= null;
		
		if (clientHasAddress) {
			clientPostCode = SpoolFileUtils.stripAllSpaces
			(entityAddress.getAddress().getPostCode());
		}
		
		if (entityPhone != null) {
			clientTel = SpoolFileUtils.stripAllSpaces
			((entityPhone.getValue()));
		}
		
		addNameValueString("CharityOfficePostcode", clientPostCode);
		addNameValueString("CharityOfficeTel", clientTel);

		// no client email address on file.
		addNameValueString("EmailAddress", null); 
		
		// Third page. Displays Company's registered address.
		// Not stored anywhere so all these fields are blank.
		addReportHeader(getReportId(3));
		
		addNameValueString("LimitedCompany", null);
		addNameValueString("CompanyRegNumber", null);
		addNameValueString("CompanyName", null);
		addNameValueString("CompanyRegAddress1", null);
		addNameValueString("CompanyRegAddress2", null);
		addNameValueString("CompanyRegAddress3", null);
		addNameValueString("CompanyRegAddress4", null);
		addNameValueString("CompanyRegAddress5", null);
		addNameValueString("CompanyRegPostcode", null);
		addNameValueString("CompanyRegTel", null);
		addNameValueString("CompanyRegEmail", null);
		addNameValueString("FinancialYearEnd", null);
		
		// Fourth page. Displays Correspondent details
		addReportHeader(getReportId(4));
		
		Vector<Contact> corrContacts = correspondent.getContacts();
		if (corrContacts == null)
			Log.warn("Unable to fill Correspondent contact data, " +
			 "contact map not initialized");
		
		Contact corrContactAddress = 
		 Contact.getDefaultContact(corrContacts, Contact.ADDRESS);
		
		Contact corrContactPhone = Contact.getDefaultContact
		 (corrContacts, Contact.PHONE);
		
		Contact corrContactEmail = Contact.getDefaultContact
		 (corrContacts, Contact.EMAIL);
		
		String corrPhoneNumber 	= null;
		String corrEmail		= null;
		
		if (corrContactPhone != null)
			corrPhoneNumber = SpoolFileUtils.stripAllSpaces
			 (corrContactPhone.getValue());
		
		if (corrContactEmail != null)
			corrEmail = corrContactEmail.getValue();
		
		addNameValueString("Fullname", correspondent.getFullName());
		addNameValueString("Title", correspondent.getTitleId().toString());
		addNameValueString("Forename", correspondent.getFirstName());
		addNameValueString("MiddleName", correspondent.getMiddleName());
		addNameValueString("Surname", correspondent.getLastName());
		addNameValueString("DOB", null);
		addNameValueString("Position", correspondent.getJobTitle());
		addNameValueString("DaytimeTel", corrPhoneNumber);

		Address.AddressElement[] corrElems = 
		 new Address.AddressElement[] {
				Address.AddressElement.FLAT,
				Address.AddressElement.HOUSE_NAME,
				Address.AddressElement.HOUSE_NUMBER,
				Address.AddressElement.STREET,
				Address.AddressElement.CITY,
				Address.AddressElement.COUNTY
		};
		
		Log.debug("Correspondent address: " + corrContactAddress.getAddress().toString());
		
		Vector<String> corrAddress = 
		 corrContactAddress.getAddress().getNormalizedAddress(corrElems);
		
		Log.debug("Normalised address: ");
		
		for (String addrLine:corrAddress) {
			Log.debug(addrLine);
		}
		
		corrAddress = SpoolFileUtils.wrapToLines
		 (corrAddress, new int[] {18,23,23,23,23}, false);
		
		for (int j=0; j<5; j++) {
			String addrLine = null;
			
			if (corrAddress.size() > j)
				addrLine = corrAddress.get(j);
			
			addNameValueString(
			 "HomeAddress" + (j+1), addrLine);
		}
		
		addNameValueString("HomePostcode", 
		 SpoolFileUtils.stripAllSpaces
		  (corrContactAddress.getAddress().getPostCode()));
		
		addNameValueString("DateMovedToAddress", null);
		addNameValueString("EmailAddress", corrEmail);
		
		// Fifth page. Displays extra Correspondent details and
		// first signatory data
		addReportHeader(getReportId(5));
		
		// Previous address info (not available)
		addNameValueString("PreviousAddress1", null);
		addNameValueString("PreviousAddress2", null);
		addNameValueString("PreviousAddress3", null);
		addNameValueString("PreviousAddress4", null);
		addNameValueString("PreviousPostcode", null);
		addNameValueString("PrevDateMovToAddress", null);
		
		addNameValueString("SendCorresToHome", null);
		addNameValueString("NoOtherProductCorres", null);
		addNameValueString("AccountOperatedByOne", null);
		addNameValueString("CorAuthorisedSignatory", null);
				
		addSignatories(signatories);
		
		// Eighth page. Displays trustee data
		addReportHeader(getReportId(8));
		
		addTrustees();
		
		addBankingDetails();
		
		// 12th page. Start of Trustees Authorisation
		addReportHeader(getReportId(12));
		
		addTrusteeAuthDetails();
		
		addReportHeader(getReportId(15));
		
		// Add set number of blank pages
		for (int i=16; i<(16+NUM_BLANK_PAGES); i++) {
			addReportHeader(getReportId(i));
		}
		
		// Add T&Cs page
		addReportHeader(getReportId(19));
	}
	
	private void addCoveringLetter(Person correspondent) 
	 throws IOException, DataException {
		
		addReportHeader(COVERLETTER_REPORT_ID);
		
		addNameValueString("DocumentName", this.docName);
		addNameValueString("CorrespondentID", Integer.toString(correspondent.getPersonId()));
		
		addNameValueString("LetterDate", COVER_LETTER_DATE_FORMAT.format(new Date()));
		addNameValueString("Salutation", correspondent.getSalutation());
		addNameValueString("Correspondent", correspondent.getFullName());
		
		Contact corrContactAddress = 
		 Contact.getDefaultContact(correspondent.getContacts(), Contact.ADDRESS);
		
		Vector<String> fullCorrAddress = 
		 SpoolFileUtils.getNormalizedAddress
		 (corrContactAddress.getAddress(), true, false);
		
		addAddress(fullCorrAddress);
	}
	
	private void addBankingDetails() throws DataException, IOException {
		
		return;
		
		/*
		// Eighth page. Provides space for changing 2 account subtitles
		// and changing banking details
		addReportHeader(getReportId(10));
		
		addNameValueString("ChangeSubTitle", null);
		addNameValueString("AccountNumber1", null);
		addNameValueString("NewSubTitle1", null);
		
		addNameValueString("AccountNumber2", null);
		addNameValueString("NewSubTitle2", null);
		
		addNameValueString("ChangeIncomePayment", null);
		
		CCLAAccount firstAccount = null;
		PaymentPref paymentPref  = null;
		
		if (mandateForm.isDisplayBankingDetails()) {
			Log.debug("Adding payment preferences to form. Account payment pref: " 
			 + mandateForm.getAccounts().get(0).getPaymentPref().toString());
			
			firstAccount = mandateForm.getAccounts().get(0);
			paymentPref = firstAccount.getPaymentPref();
		} else {
			Log.debug("No banking details will be printed on this form");
		}
		
		boolean isDeposit = 
		 (firstAccount != null && firstAccount.getFund().getTypeCode().equals("DEP"));
		
		if (firstAccount != null &&
			paymentPref.equals(PaymentPref.PAY_TO_THIS_ACCOUNT) && isDeposit)
			addNameValueString("AddInterestToThisAcc", "Y");
		else 
			addNameValueString("AddInterestToThisAcc", null);
		
		if (firstAccount != null &&
			paymentPref.equals(PaymentPref.PAY_TO_DIFF_DEP_FUND_ACCOUNT) && isDeposit) {
			addNameValueString("AddInterestToAltCOIF", "Y");
			addNameValueString("PayInterestToAltCOIF", 
			 firstAccount.getMandatedAccountNumberExt());
			
		} else {
			addNameValueString("AddInterestToAltCOIF", null);
			addNameValueString("PayInterestToAltCOIF", null);
		}
		
		if (firstAccount != null &&
			paymentPref.equals(PaymentPref.PAY_TO_BANK_ACCOUNT) && isDeposit) {
			addNameValueString("PayInterestToBankAcc", "Y");
			addIncomeBankingDetails(firstAccount);
		} else {
			addNameValueString("PayInterestToBankAcc", null);
			addIncomeBankingDetails(null);
		}
		
		// Ninth page. Unitised account pref and withdrawal banking details
		addReportHeader(getReportId(11));
		
		// If the account isn't a deposit account, it must be a unitised one.
		// Section 6.3 of the form only refers to Unitised Income accounts.
		boolean isIncome = 
		 (firstAccount != null && 
		  firstAccount.getFund().getIncomeTypeCode().equals("INC"));
		
		if (firstAccount != null && !isDeposit && isIncome) {
			if (paymentPref.equals(PaymentPref.PAY_DIVIDENDS_TO_DEP_FUND)) {
				addNameValueString("PayDividendsToCOIF", "Y");
				
				if (firstAccount.getMandatedAccountNumberExt() != null) {
					addNameValueString("DividendsCOIFAccNo", 
					 firstAccount.getMandatedAccountNumberExt());
				} else {
					addNameValueString("DividendsCOIFAccNo", null);
				}
				
				addNameValueString("PayDividendsToBank", null);
				
			} else {
				addNameValueString("PayDividendsToCOIF", null);
				addNameValueString("DividendsCOIFAccNo", null);
				addNameValueString("PayDividendsToBank", "Y");
			}
		} else {
			addNameValueString("PayDividendsToCOIF", null);
			addNameValueString("DividendsCOIFAccNo", null);
			addNameValueString("PayDividendsToBank", null);
		}
		
		addNameValueString("ChangeWithdrawalIns", null);

		addWithdrawalBankingDetails(firstAccount);
		
		*/
	}
	
	/** Adds an address to the BufferedWrtier, in normalized format 
	 *  (i.e. AddressLine1, AddressLine2)
	 *  
	 * @param writer
	 * @param transfer
	 * @throws IOException
	 * @throws DataException
	 */
	private void addAddress(Vector<String> addressLines) 
	 throws IOException, DataException {
		
		if (addressLines != null) {
			for (int i=1; i<=8; i++) {
				String thisAddressLine = null;
				
				if (addressLines.size() >= i)
					thisAddressLine = (String)addressLines.get(i-1);
				
				addNameValueString("AddressLine" + i, thisAddressLine);
			}
		} else {
			Log.warn("Passed address to addAddress method was null - " +
			 "unable to append address to spool file.");
		}
	}
	
	private String getReportId(int page) {
		return PAGE_REPORT_ID_PREFIX + page;
	}
	
	/** Adds account number entries to the spool output.
	 *  
	 *  If the number of passed accounts does not reach the
	 *  MAX_LISTED_ACCOUNTS threshold, empty rows are added to
	 *  fill the space.
	 *  
	 *  The list of accounts spans two report pages - this
	 *  explains the report header insert.
	 *  
	 * @throws IOException
	 * @throws DataException
	 */
	private void addAccountNumbers(Vector<Account> accounts) 
	 throws IOException, DataException {
		
		for (int i=0; i<MAX_LISTED_ACCOUNTS; i++) {
			
			String accNumExt = null;
			
			if (accounts.size() > i) {
				Account account = accounts.get(i);
				accNumExt = account.getAccNumExt();
			}
				
			addNameValueString("AccountNumber" + (i+1), accNumExt);
			
			if ((i+1) == MAX_ACCOUNTS_PAGE1)
				addReportHeader(getReportId(2));
		}
	}
	
	private void addSignatories(Vector<Person> signatories) 
	 throws IOException, DataException {
		
		Iterator<Person> j = null;
		
		if (signatories != null)
			j = signatories.iterator();
		
		for (int i=0; i<MAX_LISTED_SIGNATORIES; i++) {
			Person signatory = null;
			
			if (j != null && j.hasNext())
				signatory = j.next();
			
			// Insert page 6 report header before second signatory
			if (i == 1)
				addReportHeader(getReportId(6));
			
			if (i == 2)
				addReportHeader(getReportId(7));
			
			addSignatory(signatory, (i+1));
		}
	}
	
	/** Adds information for a single signatory to the spool output.
	 *  
	 *  If the passed Person instance is null, the fields will be inserted
	 *  with blank values.
	 *  
	 *  TODO: fix
	 *  */
	private void addSignatory(Person signatory, int index)
	throws IOException, DataException {
		
		String fullName = null;
		String phone	= null;
		String postCode	= null;
		
		/*
		if (signatory != null) {
			fullName 	= signatory.getFullName();
			phone		= signatory.getTelephone();
			postCode	= signatory.getPostCode();
		}
		*/
		
		addNameValueString("Sig" + index + "Fullname", fullName);
		addNameValueString("Sig" + index + "Title", null);
		addNameValueString("Sig" + index + "Forename", null);
		addNameValueString("Sig" + index + "MiddleName", null);
		addNameValueString("Sig" + index + "Surname", null);
		
		addNameValueString("Sig" + index + "DOB", null);
		addNameValueString("Sig" + index + "Position", null);
		
		addNameValueString("Sig" + index + "HouseNoOrName", null);
		
		addNameValueString("Sig" + index + "Tel", SpoolFileUtils.stripAllSpaces(phone));
		addNameValueString("Sig" + index + "Postcode", SpoolFileUtils.stripAllSpaces(postCode)); 
	}
	
	/** Adds information for a single signatory to the spool output.
	 *  
	 *  If the passed Person instance is null, the fields will be inserted
	 *  with blank values.
	 *  */
	/*
	private void addSignatory(Person signatory, int index)
	throws IOException, DataException {
		
		String fullName = null;
		String phone	= null;
		String postCode	= null;
		
		if (signatory != null) {
			fullName 	= signatory.getFullName();
			phone		= signatory.getPhone();
			
			if (signatory.getAddress() != null)
				postCode	= signatory.getAddress().getPostCode();
		}
		
		addNameValueString("Sig" + index + "Fullname", fullName);
		addNameValueString("Sig" + index + "Title", null);
		addNameValueString("Sig" + index + "Forename", null);
		addNameValueString("Sig" + index + "MiddleName", null);
		addNameValueString("Sig" + index + "Surname", null);
		
		addNameValueString("Sig" + index + "DOB", null);
		addNameValueString("Sig" + index + "Position", null);
		
		addNameValueString("Sig" + index + "HouseNoOrName", null);
		
		if (index == 1) {
			// Insert page 6 report header in the middle of the first signatory
			addReportHeader(getReportId(6));
		}
		
		addNameValueString("Sig" + index + "Tel", SpoolFileUtils.stripAllSpaces(phone));
		addNameValueString("Sig" + index + "Postcode", SpoolFileUtils.stripAllSpaces(postCode)); 
	}
	*/
	
	/** Adds trustee fields to the form. No available data for these
	 *  yet, so the values are always blank.
	 * @throws IOException 
	 * @throws DataException 
	 *
	 **/
	private void addTrustees() throws DataException, IOException {
		
		for (int i=1; i<=MAX_LISTED_TRUSTEES; i++) {
			
			if (i == 5)
				addReportHeader(getReportId(9));
			
			addNameValueString("Trustee" + i + "Title", null);
			addNameValueString("Trustee" + i + "Forename", null);
			addNameValueString("Trustee" + i + "Surname", null);
			addNameValueString("Trustee" + i + "HouseNoOrName", null);
			addNameValueString("Trustee" + i + "Postcode", null);
			addNameValueString("Trustee" + i + "Email", null);
		}
	}
	
	/** Adds Trustees Authorisation fields to the form. No available data
	 *  for these yet, so the values are always blank.
	 *  
	 * @throws DataException
	 * @throws IOException
	 */
	private void addTrusteeAuthDetails() throws DataException, IOException {
		
		for (int i=1; i<=MAX_LISTED_AUTH_TRUSTEES; i++) {
			String prefix = "Trust" + i + "Auth";
			
			addNameValueString(prefix + "Title", null);
			addNameValueString(prefix + "Forename", null);
			addNameValueString(prefix + "MidName", null);
			addNameValueString("Trust" + i + "Surname", null);
			
			if (i == 1) {
				addReportHeader(getReportId(13));
			}
			
			addNameValueString(prefix + "DOB", null);
			addNameValueString(prefix + "Position", null);
			addNameValueString(prefix + "Tel", null);
			
			addNameValueString(prefix + "Address1", null);
			addNameValueString(prefix + "Address2", null);
			addNameValueString(prefix + "Address3", null);
			addNameValueString(prefix + "Postcode", null);
			addNameValueString(prefix + "DatMovToAdd", null);
			addNameValueString(prefix + "EmailAdd", null);
			
			if (i == 2) {
				addReportHeader(getReportId(14));
			}
			
			addNameValueString(prefix + "PrevAdd1", null);
			addNameValueString(prefix + "PrevAdd2", null);
			addNameValueString(prefix + "PrevPostCo", null);
			addNameValueString(prefix + "PrevDatMovTo", null);
		}
	}
	
	/** Adds income banking data from the passed bank account.
	 *  If the passed account is null, the banking fields are left
	 *  empty.
	 *  
	 *  TODO: refactor to work with new Account class.
	 *  
	 * @throws IOException 
	 * @throws Exception 
	 */
	private void addIncomeBankingDetails(Account account) 
	 throws DataException, IOException {
		/*
		if (account != null && account.hasIncomeBankingData()) {
			BankDetails bankDetails = account.getIncomeBankDetails();
			
			String bankName 	= null;
			String branchTitle	= null;
			
			if (bankDetails != null) {
				bankName 	= bankDetails.getBankName();
				branchTitle	= bankDetails.getBranchTitle();
			}
				
			addNameValueString("BankorBuildSocName", bankName);
			addNameValueString("BranchTitle", branchTitle);
			addNameValueString("AccountName", account.getBankAccountNameIncome());
			addNameValueString("Sortcode", 
			 SpoolFileUtils.formatSortCode(account.getBankSortCodeIncome()));
			
			String accountNum = account.getBankAccountNumberIncome();
			
			// Always trim the trailing digits from the account number, so the client
			// must complete them.
			if (!StringUtils.stringIsBlank(accountNum) && accountNum.length() > 4)
				accountNum = accountNum.substring(0, 4);
			
			addNameValueString("AccountNumber", accountNum);
			addNameValueString("BuildSocReference", null);
		} else {
			// Add null values if no banking data present. This prevents
			// adding zero values to the form, which will be present otherwise.
			addNameValueString("BankorBuildSocName", null);
			
			addNameValueString("BranchTitle", null);
			addNameValueString("AccountName", null);
			addNameValueString("Sortcode", null);
			
			addNameValueString("AccountNumber", null);
			addNameValueString("BuildSocReference", null);
		}
		*/
	}
	
	/** Adds income banking data from one of the passed accounts,
	 *  if it is present.
	 *  
	 *  TODO: refactor to work with new Account class.
	 *  
	 * @throws DataException
	 * @throws IOException
	 */
	private void addIncomeBankingDetails() 
	 throws DataException, IOException {
		
		/*
		Vector<CCLAAccount> accounts = null; //mandateForm.getAccounts();
		Vector<CCLAAccount> incomeAccounts = new Vector<CCLAAccount>();
		
		for (int i=0; i<accounts.size(); i++) {
			if (accounts.get(i).hasIncomeBankingData())
				incomeAccounts.add(accounts.get(i));
		}
		
		Log.debug("Found " + incomeAccounts.size() + " accounts with " +
		 "income banking data.");
		
		boolean singleIncomeData = true;
		
		for (int i=0; i<incomeAccounts.size(); i++) {
			CCLAAccount thisAccount = incomeAccounts.get(i);
			
			for (int j=0; j<incomeAccounts.size(); j++) {
				if (!thisAccount.incomeBankAccountMatches(incomeAccounts.get(j)))
					singleIncomeData = false;
			}
		}
		
		if (incomeAccounts.size() > 0) {
			if (!singleIncomeData)
				Log.debug("Income accounts did not have matching data. " +
				 "Using the first income account to pre-fill form.");
			
			CCLAAccount thisAccount = incomeAccounts.get(0);
			
			//addNameValueString("BankorBuildSocName", 
			// thisAccount.getBuildingSocietyNumberIncome());
			
			// TODO
			addNameValueString("BankorBuildSocName", 
			 null);
			
			addNameValueString("BranchTitle", null);
			addNameValueString("AccountName", thisAccount.getBankAccountNameIncome());
			addNameValueString("Sortcode", 
			 SpoolFileUtils.formatSortCode(thisAccount.getBankSortCodeIncome()));
			
			String accountNum = thisAccount.getBankAccountNumberIncome();
			
			// Always trim the trailing digits from the account number, so the client
			// must complete them.
			if (!StringUtils.stringIsBlank(accountNum) && accountNum.length() > 4)
				accountNum = accountNum.substring(0, 4);
			
			addNameValueString("AccountNumber", accountNum);
			addNameValueString("BuildSocReference", null);
		} else {
			// Add null values if no banking data present. This prevents
			// adding zero values to the form, which will be present otherwise.
			addNameValueString("BankorBuildSocName", null);
			
			addNameValueString("BranchTitle", null);
			addNameValueString("AccountName", null);
			addNameValueString("Sortcode", null);
			
			addNameValueString("AccountNumber", null);
			addNameValueString("BuildSocReference", null);
		}	
		*/
	}
	
	/** Adds withdrawal banking data from the passed account data,
	 *  if it is present.
	 *  
	 *  TODO: refactor to work with new Account class.
	 *  
	 * @throws Exception 
	 */
	private void addWithdrawalBankingDetails(Account account) 
	 throws DataException, IOException {
		
		/*
		if (account != null && account.hasWithdrawalBankingData()) {
			BankDetails bankDetails = account.getWithdrawalBankDetails();
			
			String bankName 	= null;
			String branchTitle	= null;
			
			if (bankDetails != null) {
				bankName 	= bankDetails.getBankName();
				branchTitle	= bankDetails.getBranchTitle();
			}
				
			addNameValueString("WDPaymentBankName", bankName);
			addNameValueString("WDPaymentBranchTitle", branchTitle);
			
			addNameValueString("WDPaymentAccName", 
			 account.getBankAccountNameWithdrawal());
			
			addNameValueString("WDSortCode", 
			 SpoolFileUtils.formatSortCode(account.getBankSortCodeWithdrawal()));
			
			String accountNum = account.getBankAccountNumberWithdrawal();
			
			// Always trim the trailing digits from the account number, so the client
			// must complete them.
			if (!StringUtils.stringIsBlank(accountNum) && accountNum.length() > 4)
				accountNum = accountNum.substring(0, 4);
			
			addNameValueString("WDAccountNumber", accountNum);
			addNameValueString("WDBuildingSocRef", null);
		} else {
			// Add null values if no banking data present. This prevents
			// adding zero values to the form, which will be present otherwise.
			addNameValueString("WDPaymentBankName", null);
			
			addNameValueString("WDPaymentBranchTitle", null);
			addNameValueString("WDPaymentAccName", null);
			
			addNameValueString("WDSortCode", null);

			addNameValueString("WDAccountNumber", null);
			addNameValueString("WDBuildingSocRef", null);
		}
		*/
	}
	
	/** Adds income banking data from one of the passed accounts,
	 *  if it is present.
	 *  
	 *  TODO: refactor to work with new Account class.
	 *  
	 * @throws DataException
	 * @throws IOException
	 */
	private void addWithdrawalBankingDetails() 
	 throws DataException, IOException {
		
		/*
		Vector<Account> accounts = mandateForm.getAccounts();
		Vector<CCLAAccount> withdrawalAccounts = new Vector<CCLAAccount>();
		
		for (int i=0; i<accounts.size(); i++) {
			//
			//if (accounts.get(i).hasWithdrawalBankingData())
			//	withdrawalAccounts.add(accounts.get(i));
		}
		
		Log.debug("Found " + withdrawalAccounts.size() + " accounts with " +
		 "withdrawal banking data.");
		
		boolean singleIncomeData = true;
		
		for (int i=0; i<withdrawalAccounts.size(); i++) {
			CCLAAccount thisAccount = withdrawalAccounts.get(i);
			
			for (int j=0; j<withdrawalAccounts.size(); j++) {
				if (!thisAccount.withdrawalBankAccountMatches(withdrawalAccounts.get(j)))
					singleIncomeData = false;
			}
		}
		
		if (!singleIncomeData)
			Log.debug("Withdrawal accounts did not have matching data. " +
			 "Using the first withdrawal account to pre-fill form.");
		
		if (withdrawalAccounts.size() > 0) {
			CCLAAccount  thisAccount = withdrawalAccounts.get(0);
			
			//addNameValueString("WDPaymentBankName", 
			// thisAccount.getBuildingSocietyNumberWithdrawal());
			
			// TODO
			addNameValueString("WDPaymentBankName", null);
			
			addNameValueString("WDPaymentBranchTitle", null);
			addNameValueString("WDPaymentAccName", 
			 thisAccount.getBankAccountNameWithdrawal());
			
			addNameValueString("WDSortCode", 
			 SpoolFileUtils.formatSortCode(thisAccount.getBankSortCodeWithdrawal()));
			
			String accountNum = thisAccount.getBankAccountNumberWithdrawal();
			
			// Always trim the trailing digits from the account number, so the client
			// must complete them.
			if (!StringUtils.stringIsBlank(accountNum) && accountNum.length() > 4)
				accountNum = accountNum.substring(0, 4);
			
			addNameValueString("WDAccountNumber", accountNum);
			addNameValueString("WDBuildingSocRef", null);
		} else {
			// Add null values if no banking data present. This prevents
			// adding zero values to the form, which will be present otherwise.
			addNameValueString("WDPaymentBankName", null);
			
			addNameValueString("WDPaymentBranchTitle", null);
			addNameValueString("WDPaymentAccName", null);
			
			addNameValueString("WDSortCode", null);

			addNameValueString("WDAccountNumber", null);
			addNameValueString("WDBuildingSocRef", null);
		}
		*/
	}
	
	/** Writes the SpoolHeader contents to the BufferedWriter instance.
	 * 
	 *  Overriden here to account for different padding size.
	 * 
	 * @throws SpoolFileException
	 * @throws DataException
	 * @throws IOException
	 */
	protected void writeHeader() throws SpoolFileException, DataException, 
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
		addNameValueString("TemplatePath", header.templatePath);

		addNameValueString("AnacompDocType", header.anacompDocType);
		addNameValueString("AnacompRetGrpNo", header.anacompRetGrpNo);
		
		addNameValueString("ReleaseVersion", header.releaseVersion);
	}
}