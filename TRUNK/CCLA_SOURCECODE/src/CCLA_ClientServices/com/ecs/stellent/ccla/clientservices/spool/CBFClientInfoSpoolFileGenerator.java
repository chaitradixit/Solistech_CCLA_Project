package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import com.aurora.webservice.BankDetails;
import com.ecs.stellent.ccla.clientservices.campaign.CBFClientConfirmationEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.form.ClientInfoForm;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementIdentifier;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.PersonTitle;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.campaign.ApplicableEnrolmentAttribute;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttributeApplied;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;

public class CBFClientInfoSpoolFileGenerator extends SpoolFileGenerator {

	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	/** Minimum number of Director/Controller sections that will be generated. */
	private static int MIN_LISTED_AUTH_PERSONS = 2;
	/** Max number of Director/Controller entries. */
	private static int MAX_LISTED_AUTH_PERSONS = 2;
	
	/** Minimum number of Signatory sections that will be generated. */
	private static int MIN_LISTED_SIGNATORIES = 3;
	/** Max number of Signatory entries. */
	private static int MAX_LISTED_SIGNATORIES = 10;
	
	/** No. of pages required to output a single authorising person */
	private static final int PAGES_PER_AUTH_PERSON = 2;
	
	/** No. of pages required to output a single signatory */
	private static final int PAGES_PER_SIGNATORY = 1;
	
	/** No. of pages required to output a single main contact */
	private static final int PAGES_PER_MAIN_CONTACT = 2;
	
	/** Default no. of signature boxes to add on the final authorization page.
	 * 
	 *  If a number of signatures is specified in the enrolment attributes, this will
	 *  be used instead of the default value.
	 */
	private static final int DEFAULT_SIGNATURES = 2;
	
	/** Determines whether this is the 'confirmation' version of the form or not.
	 *  
	 *  If true, doesn't change the form content, but a 'C' is suffixed to every Report
	 *  ID which changes the appearance of the printed form.
	 */
	private boolean isConfirmation = true;
	
	/** Format used to display the form deadline date on the first page */
	private static SimpleDateFormat DEADLINE_DATE_FORMAT = 
	 new SimpleDateFormat("dd MMMM yyyy");
	
	/** Max number of accounts that can be displayed on the first Account listing page.
	 * 
	 */
	private static final int MAX_ACCOUNTS_FIRST_PAGE = 12;
	
	/** Max number of accounts that can be displayed on an overflow Account listing 
	 *  page.
	 */
	private static final int MAX_ACCOUNTS_OVERFLOW_PAGE = 44;
	
	// CreateForm report IDs.
	
	/** Prefix string for each CreateForm page template */
	private String PAGE_REPORT_ID_PREFIX = "CBFCIF";
	
	private static class ReportSections {
		static final String COVERING_PAGE				= "LTR";
		static final String ACCOUNT_LISTING 			= "S1P1";
		static final String ACCOUNT_LISTING_OVERFLOW 	= "S1P2";
		static final String ORG_INFO 					= "S1P3";
		static final String BANKING_DETAILS 			= "S2";
		static final String DIRS_COVERING_PAGE 			= "S3P1";
		static final String DIR_INFO_P1 				= "S3P2";
		static final String DIR_INFO_P2 				= "S3P3";
		static final String MAIN_CONTACT_P1 			= "S4P1";
		static final String MAIN_CONTACT_P2 			= "S4P2";
		static final String SIGNATORY_INFO				= "S5P1";
		static final String CONFIRMATION				= "CLINFO";
	}
	
	/** Keys for extra attribs accessible via regForm.getExtraAttributes()
	 * 
	 * @author Tom
	 *
	 */
	public static class Attributes {
		/** Deadline date printed on the covering letter. */
		public static final String DEADLINE_DATE  		= "DEADLINE_DATE";
		/** Determines whether this is the confirmation version of the form or not */
		public static final String IS_CONFIRMATION 		= "IS_CONFIRMATION";
	}
	
	/** Container for all the data objects required to generate the spool file, e.g.
	 *  Organisation, Person, Contact Details.
	 *  
	 */
	private ClientInfoForm infoForm;
	
	/** After a person has their details output in one section, they are added to this
	 *  HashSet to prevent their details being reprinted in a later section.
	 */
	private HashSet<Person> printedPersons = new HashSet<Person>();

	// Below variables are populated in prepareFormContents method
	// -------------------
	
	/** Page counters, used for printing 'Page x of y' captions on each report page.
	 *  
	 *  Total pages is calculated via the prepareFormContents method.
	 */
	int currentPage 	= 1;
	int totalPages 		= 10;
	
	// These vars are mapped during the initial form creation process.
	private Vector<Person> authPersons 	= null;
	private Person mainContact			= null;
	private Vector<Person> sigs 		= null;
	
	// Determines whether or not the Main Contact section is displayed or not.
	private boolean displayMainContactSection = true;
	
	// Number of director/controller/auth person sections to output on the form.
	private Integer numAuthPersonSections;
	// Number of signatory sections to output on the form.
	private Integer numSignatorySections;
	
	// -------------------
	
	public CBFClientInfoSpoolFileGenerator
	 (SpoolHeader spoolHeader, ClientInfoForm infoForm) {
		
		super(spoolHeader, null, null);
		this.infoForm	= infoForm;
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
		
		addReportHeader(reportId, true);
	}
	
	/** Adds a report header to the BufferedWriter.
	 * 
	 *  Includes FormID and PageNumber label.
	 *  
	 **/
	protected void addReportHeader(String reportId, boolean printPageCounter) 
	 throws IOException, DataException {
		
		writer.write(SpoolFileUtils.REPORT_DELIMITER);
		writer.newLine();
		
		addNameValueString("ReportID", reportId + (isConfirmation ? "C" : ""));

		// Add Form ID and PageNumber under every page delimiter.
		addNameValueString("FormID", 
		 CCLAUtils.padString(
		 Integer.toString(this.infoForm.getFormId()), '0', 9));
		
		if (printPageCounter)
			addNameValueString
			 ("PageNumber", "Page " + (currentPage++) + " of " + totalPages);
		
		addNameValueString
		 ("Campaign", CBFClientConfirmationEnrolmentHandler.SPOOL_FILE_CAMPAIGN_NAME);
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
		
		if (infoForm == null)
			throw new SpoolFileException(
			 "RegForm instance was empty or not initialized");
	
		ByteArrayOutputStream outputStream 	= new ByteArrayOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		// Fetch isConfirmation flag.
		Boolean attrib =  null;
		
		if (infoForm.getExtraAttributes().containsKey(Attributes.IS_CONFIRMATION))
			attrib = (Boolean)
			 infoForm.getExtraAttributes().get(Attributes.IS_CONFIRMATION);
		
		if (attrib != null) {
			this.isConfirmation = attrib.booleanValue();
		}
		
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
	 * @param infoForm
	 * @throws IOException 
	 * @throws ServiceException 
	 */
	private void appendFormData()
	 throws DataException, IOException, ServiceException {
		
		// Calculate person mapping and total page count first.
		prepareFormContents();

		// Covering Page
		// ---------------------------------
		// Displays correspondent address, name plus some blurb relating to Client Info
		// campaign. No page counter in the heaer!
		addReportHeader(getReportId(ReportSections.COVERING_PAGE, null), false);
		
		addNameValueString("LetterDate", "Date as Postmark");
		
		// Add postal address with correspondent name at the top
		addNameValueString("Correspondent", 
		 infoForm.getCorrespondent().getCompleteName());
		
		// Output postal address
		Address postalAddress = infoForm.getContactAddress().getAddress();
		
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
		
		addNameValueString("Salutation", infoForm.getCorrespondent().getSalutation());
		addNameValueString("OrgID", infoForm.getEntity().getOrgAccountCode());
		addNameValueString("OrgName", infoForm.getEntity().getOrganisationName());
		
		// Fetch deadline date.
		Date deadlineDate = (Date)infoForm.getExtraAttributes().get
		 (Attributes.DEADLINE_DATE);
		String deadlineDateStr = DEADLINE_DATE_FORMAT.format(deadlineDate);
		
		addNameValueString("FormReturnDate", deadlineDateStr);
		
		// Account Listing (first page)
		// -------------------
		// Lists Correspondent info again, plus the first 12 accounts.
		addReportHeader(getReportId(ReportSections.ACCOUNT_LISTING, null));
		
		// Add postal address with correspondent name at the top
		addNameValueString("Correspondent", 
		 infoForm.getCorrespondent().getCompleteName());
		
		// Print the postal address over 8 lines.
		for (int j=0; j<8; j++) {
			String addressPiece = null;
			
			if (postalAddressLines.size() > j)
				addressPiece = postalAddressLines.get(j);
			
			addNameValueString(
			 "AddressLine" + (j+1), addressPiece);
		}

		addNameValueString("OrgID", infoForm.getEntity().getOrgAccountCode());
		addNameValueString("LetterDate", null);
		addNameValueString("OrgName", infoForm.getEntity().getOrganisationName());
		
		// Sort the accounts in ascending order first.
		Collections.sort(infoForm.getAccounts());
		
		// Output up to the first 12 accounts.
		for (int i=0; i<MAX_ACCOUNTS_FIRST_PAGE; i++) {
			String accountStr = null;
			
			if (infoForm.getAccounts().size() > i)
				accountStr = infoForm.getAccounts().get(i).getAccNumExt().trim();
			
			addNameValueString(
			 "AccountNum" + (i+1), accountStr);
		}

		// Determine number of account listing overflow pages required, if any.
		int numOverflowAccountPages = getNumRequiredOverflowAccountPages();
		
		for (int i=0; i<numOverflowAccountPages; i++) {
			// Account Listing (overflow page)
			// -------------------
			// If there are more than 12 accounts to be listed on the form, this page 
			// will be repeated for all remaining sets of accounts, in groups of 44 per 
			// page.
			addReportHeader(getReportId
			 (ReportSections.ACCOUNT_LISTING_OVERFLOW, null));
			
			// Tracks the current account to display.
			int accountIndex = MAX_ACCOUNTS_FIRST_PAGE + 
			 (i*MAX_ACCOUNTS_OVERFLOW_PAGE);
			
			for (int j=0; j<MAX_ACCOUNTS_OVERFLOW_PAGE; j++) {
				String accountStr = null;

				if (infoForm.getAccounts().size() > accountIndex)
					accountStr = infoForm.getAccounts().get(accountIndex)
					 .getAccNumExt().trim();
				
				addNameValueString(
				 "AccountNum" + (j+1), accountStr);
				
				accountIndex++;
			}
		}
		
		// Org Details
		// -------------------
		// Lists various Org KYC flags
		addReportHeader(getReportId(ReportSections.ORG_INFO, null));
		
		// Only populate this section if the sys config var is set.
		SystemConfigVar populateOrgDetailsVar = SystemConfigVar.getCache()
		 .getCachedInstance("CBFClientInfo_PopulateOrgDetails");
		
		String hmrcNumber = null, charityRegNumber = null;
		
		Boolean hasCharityRegNumber = null, isParochialChurchCouncil = null;
		
		if (populateOrgDetailsVar != null 
			&& populateOrgDetailsVar.getBoolValue() != null
			&& populateOrgDetailsVar.getBoolValue()) {
			
			hmrcNumber = infoForm.getOrgIdentifiers().get
			 (ElementIdentifier.HMRC_NUMBER);
			charityRegNumber = infoForm.getOrgIdentifiers().get
			 (ElementIdentifier.CHARITY_REF);
	
			hasCharityRegNumber = 
			 StringUtils.stringIsBlank(charityRegNumber) ? null : true;
			
			// Determine whether the client is parochial church council by checking the
			// Contributor Type Code.
			Integer contributorTypeCode = 
			 infoForm.getAuroraClient().getContributorTypeCode();
			
			isParochialChurchCouncil = 
			 contributorTypeCode != null
			 && contributorTypeCode.equals
			 (AuroraClient.ContributorTypeCodes.PAROCHIAL_AUTHORITY);
		}
		
		addNameValueString("PCC", 
		 SpoolFileUtils.getTickboxValue(isParochialChurchCouncil));
		addNameValueString("RegWithCharityComm", 
		 SpoolFileUtils.getTickboxValue(hasCharityRegNumber));
		addNameValueString("CharityRegNo", charityRegNumber);
		addNameValueString("HMRCNo", hmrcNumber);
		
		// Banking Details
		// -------------------
		// Lists associated Bank Accounts
		
		// Determine number of 'Bank Account' sections to print on the form.
		// 2 Bank Accounts can fit per page. Even if there are no Bank Accounts stored
		// against the client accounts, we'll always print a single Bank Account page
		// with space for 2 Bank Accounts.
		int numBankAccounts = 2;
		
		if (infoForm.getBankAccounts() != null 
			&& infoForm.getBankAccounts().size() > numBankAccounts)
			numBankAccounts = infoForm.getBankAccounts().size();
		
		for (int i=0; i<numBankAccounts; i++) {
			// Print 2 bank accounts per page
			
			if (i % 2 == 0) {
				// New page!
				addReportHeader(getReportId(ReportSections.BANKING_DETAILS, null));

				addNameValueString("NumberOfBankAccounts", 
				 Integer.toString(numBankAccounts));
			}
			
			addNameValueString("BankAccountNumber", Integer.toString(i+1));
			
			BankAccount bankAccount = null;
			
			if (infoForm.getBankAccounts() != null
				&& infoForm.getBankAccounts().size() > i) {
				// Print bank account details, if we have some.
				bankAccount = infoForm.getBankAccounts().get(i);		
			}
			
			addBankAccountDetails(this, bankAccount);
		}
		
		// Trustee/Dir Auth cover page
		// -------------------
		// Lists Org Address, Financial Year End and Company Address
		addReportHeader(getReportId(ReportSections.DIRS_COVERING_PAGE, null));
		
		Boolean allSectionsPrinted = true;
		
		addNameValueString("AllSectionsPrinted",
		 SpoolFileUtils.getTickboxValue(allSectionsPrinted));
		
		// Trustee/Dir Auth display
		// -------
		// Contains details for Directors/Controllers, i.e. auth persons for the org.
		// 2 pages per auth person.
		for (int i=1; i<=numAuthPersonSections; i++) {
			Person thisAuthPerson = null;
			
			if (authPersons != null && authPersons.size() >= i)
				thisAuthPerson = authPersons.get(i-1);
			
			addDirectorOrController(thisAuthPerson, i, numAuthPersonSections);
		}
		
		// Main Contact details section (Section 4)
		// ----------------------------
		// Displays org correspondent details, if they weren't listed in the
		// previous section. If the org. corr. was previously listed, this section is
		// skipped.
		
		// Get Main Contact (i.e. Org Correspondent)
		if (displayMainContactSection) {
			addReportHeader(getReportId(ReportSections.MAIN_CONTACT_P1, null));	
			
			if (mainContact != null)
				printedPersons.add(mainContact);
			
			String linePrefix = "Cor";
			
			// Add name/position/DOB fields for main contact
			SpoolFileUtils.addPersonDetails(this, mainContact, linePrefix);
			
			// Add Tel/Email fields
			SpoolFileUtils.addPersonContactDetails(this, mainContact, linePrefix);

			// Search for correspondent's home address, i.e. their 'Experian' flagged
			// address.
			Contact corrHomeAddressContact = null;
			Address corrHomeAddress = null;
			
			if (mainContact != null) {
				corrHomeAddressContact = Contact.getExperianContact
				 (mainContact.getContacts(), Contact.ADDRESS);
				
				// If we didn't find an experian flagged address, just search for the
				// first one marked as 'Home Address' instead..
				if (corrHomeAddressContact == null)
					corrHomeAddressContact = Contact.getFirstContactBySubMethod
					(mainContact.getContacts(), Contact.SUBMETHOD_ADDRESS_HOME);

				if (corrHomeAddressContact != null)
					corrHomeAddress = corrHomeAddressContact.getAddress();
			}
			
			// Spread the normalized corr address over a 26x3 grid (minus the postcode)
			Vector<SpoolFileLine> corrHomeAddressLines = 
			 SpoolFileGenerator.getGridFormatAddress
			 (corrHomeAddress, linePrefix + "AddressLine", false, false, 26, 3);
			
			addLines(corrHomeAddressLines, NAME_VALUE_PADDING);
			
			addNameValueString(linePrefix + "AddressPostcode", 
			 corrHomeAddress != null ?
			 CCLAUtils.stripSpaceCharacters(corrHomeAddress.getPostCode()) : null);

			// Second page for Main Contact details.
			// -----------------------
			addReportHeader(getReportId(ReportSections.MAIN_CONTACT_P2, null));	
			
			// Whether or not the correspondent is also marked as a signatory.
			addNameValueString(linePrefix + "IsAuthSig", 
			 SpoolFileUtils.getTickboxValue(isAuthSig(mainContact)));
		
			// Display separate correspondence address, if the person's 
			// default/nominated address differs from the previously-displayed 
			// (Experian) address.
			Contact corrNominatedAddressContact = null;
			Address corrNominatedAddress = null;
			
			if (mainContact != null) {
				corrNominatedAddressContact =
				 Contact.getDefaultContact
				(mainContact.getContacts(), Contact.ADDRESS);

				if (corrNominatedAddressContact != null
					&&
					(
						(corrHomeAddress == null)
						||
						(corrNominatedAddressContact.getAddress().getAddressId() 
						!= corrHomeAddress.getAddressId())
					)) {
					corrNominatedAddress = corrNominatedAddressContact.getAddress();
				}
			}
			
			// Whether or not the correspondent is also marked as a signatory.
			addNameValueString(linePrefix + "PrefOtherAddress", 
			 SpoolFileUtils.getTickboxValue(corrNominatedAddress != null));
			
			// Spread the normalized corr address over a 26x3 grid (minus the postcode)
			Vector<SpoolFileLine> corrNominatedAddressLines = 
			 SpoolFileGenerator.getGridFormatAddress
			 (corrNominatedAddress, linePrefix + "OtherAddressLine", 
			 false, false, 26, 3);
			
			addLines(corrNominatedAddressLines, NAME_VALUE_PADDING);
			
			addNameValueString(linePrefix + "OtherAddressPostcode", 
			 corrNominatedAddress != null ?
			 CCLAUtils.stripSpaceCharacters(corrNominatedAddress.getPostCode()) : null);
			
			boolean dirDoNotContact = false;
			
			if (mainContact != null && mainContact.isDoNotContact())
				dirDoNotContact = true;
				
			addNameValueString(linePrefix + "DoNotContact", 
			 dirDoNotContact ? "Y" : null);
		}
		
		// Add remaining org signatories (i.e. Persons marked as Signatory to 
		// account but not Auth. Person/Correspondent, so they won't have been 
		// displayed in the Directors/Main Contact section)
		for (int i=1; i<=numSignatorySections; i++) {
			
			Person sig = null;
			
			if (sigs != null && sigs.size() >= i)
				sig = sigs.get(i-1);
			
			addReportHeader(getReportId(ReportSections.SIGNATORY_INFO, null));
			
			addNameValueString("NumberOfSignatories", 
			 Integer.toString(numSignatorySections));
			addNameValueString("SignatoryNumber", Integer.toString(i));
			
			String linePrefix = "Sig";
			
			// Add name/position/DOB fields for signatory
			SpoolFileUtils.addPersonDetails(this, sig, linePrefix);
			
			// Add Telephone
			Contact telephone 				= null;

			if (sig != null)
				telephone = Contact.getDefaultContact(sig.getContacts(), Contact.PHONE);
			
			addNameValueString(linePrefix +  "Telephone", telephone != null 
			 ? CCLAUtils.stripSpaceCharacters(telephone.getValue()) : null);
			
			boolean sigDoNotContact = false;
			
			if (sig != null && sig.isDoNotContact())
				sigDoNotContact = true;
				
			addNameValueString(linePrefix + "DoNotContact", 
			 sigDoNotContact ? "Y" : null);
		}
		
		// Add Checklist page
		addReportHeader(getReportId(ReportSections.CONFIRMATION, null));
	}
	
	/** Adds Bank Account data fields for the passed BankAccount instance.
	 *  
	 *  The passed instance can be null - this will output blank lines instead.
	 *  
	 * @param bankAccount
	 * @throws IOException 
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void addBankAccountDetails
	 (SpoolFileGenerator generator, BankAccount bankAccount) 
	 throws DataException, IOException, ServiceException {
		
		BankDetails bankDetails = null;
		
		if (bankAccount != null)
			bankDetails = bankAccount.getBankDetails();
		
		generator.addNameValueString("BankName", 
		 bankDetails != null ? bankDetails.getBankName() : null);
		generator.addNameValueString("BranchName",
		 bankDetails != null ? bankDetails.getBranchTitle() : null);
		
		generator.addNameValueString("AccountName", 
		 bankAccount != null ? bankAccount.getAccountName() : null);
		generator.addNameValueString("SortCode", 
		 bankAccount != null ? bankAccount.getSortCode() : null);
		generator.addNameValueString("AccountNumber", 
		 bankAccount != null ? bankAccount.getAccountNumber() : null);
		
		generator.addNameValueString("BuildingSocietyRef", 
		 bankAccount != null ? bankAccount.getBuildingSocietyNumber() : null);
	}
	
	/** Returns the mapped org attribute value. Returns null if the mapped attribute
	 *  isn't present.
	 *  
	 *  If the attribute has data type BOOL, either a Y or N is returned, based on the
	 *  status parameter of the applied attribute.
	 *  
	 * @param attributeId
	 * @return
	 * @throws DataException 
	 */
	private String getOrgAttributeAppliedValue(int attributeId) throws DataException {
		
		// Determine data type
		ElementAttribute attr = ElementAttribute.getCache().getCachedInstance
		 (attributeId);
		
		ElementAttributeApplied elemAttrAppl = 
		 infoForm.getOrgAttributes().get(attributeId);
		
		if (elemAttrAppl != null) {
			if (attr.getDataType().equals(DataType.BOOL))
				return SpoolFileUtils.getTickboxValue(elemAttrAppl.getStatus());
			else
				return elemAttrAppl.getAttrValue();
		} else
			return null;
	}
	
	/** Determines how many account overflow pages must be printed (could be zero)
	 * 
	 * @return
	 */
	private int getNumRequiredOverflowAccountPages() {
		
		// Determine number of account listing overflow pages required.
		int numOverflowAccountPages = 0;
		
		if (infoForm.getAccounts().size() > MAX_ACCOUNTS_FIRST_PAGE) {
			numOverflowAccountPages = 
			 (infoForm.getAccounts().size() - MAX_ACCOUNTS_FIRST_PAGE) 
			 / MAX_ACCOUNTS_OVERFLOW_PAGE;
			
			if (numOverflowAccountPages == 0)
				numOverflowAccountPages = 1; // always be at least 1 overflow page!
			else if ((infoForm.getAccounts().size() % MAX_ACCOUNTS_OVERFLOW_PAGE) > 0) {
				// There wasn't an exact fit on the overflow pages.
				// Add one more page.
				numOverflowAccountPages++;
			}
		}
		
		return numOverflowAccountPages;
	}
	
	/** 
	 * Decides which Person records will be displayed on the form, and in which
	 * sections.
	 * 
	 * Also determines the total page count for the form.
	 * 
	 * @throws DataException 
	 * 
	 */
	private void prepareFormContents() throws DataException {
		
		// Min. no. of pages before considering person data. Assumes no dirs/
		// controllers, bank accounts, main contact or signatory data will be output.
		// -1 page for Account Listing (up to 12 accounts)
		// -1 page for Org Details
		// -1 page for Trustees/Dir Auth header
		// -1 page for Confirmation
		int testPageCount = 4;
		
		// Determine number of account listing overflow pages required.
		int numOverflowAccountPages = getNumRequiredOverflowAccountPages();
		
		testPageCount += numOverflowAccountPages;
		
		Log.debug("Require " + numOverflowAccountPages + " account overflow pages from " 
		 + infoForm.getAccounts().size() + " listed accounts, page count = " 
		 + testPageCount);
		
		// Determine no. of Bank Account pages to display on form. 
		int numBankAccountPages = infoForm.getBankAccounts().size() / 2;
		
		if (numBankAccountPages == 0)
			numBankAccountPages = 1;
		else if ((infoForm.getBankAccounts().size() % 2) > 0)
			numBankAccountPages++;
		
		testPageCount += numBankAccountPages;
		
		Log.debug("Added " + infoForm.getBankAccounts().size() + 
		 " bank accounts, pageCount = " + testPageCount);
		
		// Determine no. of Directors/Controllers and Signatories to display on form
		
		SystemConfigVar minAuthPersonsVar = SystemConfigVar.getCache().getCachedInstance
		 ("CBFClientInfo_MinAuthPersons");
		
		SystemConfigVar minSigsVar = SystemConfigVar.getCache().getCachedInstance
		 ("CBFClientInfo_MinSignatories");
		
		SystemConfigVar maxAuthPersonsVar = SystemConfigVar.getCache().getCachedInstance
		 ("CBFClientInfo_MaxAuthPersons");
		
		SystemConfigVar maxSigsVar = SystemConfigVar.getCache().getCachedInstance
		 ("CBFClientInfo_MaxSignatories");
		
		// Overwrite min. auth person/signatory vars
		EnrolmentAttributeApplied minAuthPersonsAttr = 
		 infoForm.getEnrolmentAttributes().get
		 (ApplicableEnrolmentAttribute.Ids.COMM_FIRST_MIN_AUTH_PERSONS);
		
		if (minAuthPersonsAttr != null)
			MIN_LISTED_AUTH_PERSONS = Integer.parseInt(minAuthPersonsAttr.getValue());
		else if (minAuthPersonsVar != null && minAuthPersonsVar.getIntValue() != null)
			MIN_LISTED_AUTH_PERSONS = minAuthPersonsVar.getIntValue();
		
		EnrolmentAttributeApplied minSigsAttr = 
		 infoForm.getEnrolmentAttributes().get
		 (ApplicableEnrolmentAttribute.Ids.COMM_FIRST_MIN_SIGNATORIES);
		
		if (minSigsAttr != null)
			MIN_LISTED_SIGNATORIES = Integer.parseInt(minSigsAttr.getValue());
		else if (minSigsVar != null && minSigsVar.getIntValue() != null)
			MIN_LISTED_SIGNATORIES = minSigsVar.getIntValue();
		
		// Overwrite max listed auth persons
		if (maxAuthPersonsVar != null && maxAuthPersonsVar.getIntValue() != null)
			MAX_LISTED_AUTH_PERSONS = maxAuthPersonsVar.getIntValue();
		
		// Overwrite max listed sigs
		if (maxSigsVar != null && maxSigsVar.getIntValue() != null)
			MAX_LISTED_SIGNATORIES = maxSigsVar.getIntValue();
		
		authPersons 	= new Vector<Person>();
		sigs 			= new Vector<Person>();
		
		int numAuthPersons = 0;
		
		boolean populatePersonSections = false;
		
		SystemConfigVar populatePersonSectionsVar = SystemConfigVar.getCache()
		 .getCachedInstance("CBFClientInfo_PopulatePersonSections");
		
		if (populatePersonSectionsVar != null 
			&& populatePersonSectionsVar.getBoolValue() != null) {
			populatePersonSections = populatePersonSectionsVar.getBoolValue();
		}
		
		if (populatePersonSections) {
			Log.debug("Determining persons who will be printed on the form...");
			
			// Fetch all authorising persons to display. These will be printed in the
			// auth persons section and require 2 pages each
			Vector<Person> testAuthPersons = infoForm.getRelatedOrgPersons().get
			 (RelationName.getCache().getCachedInstance(
				RelationName.PersonAccountRelation.AUTH_PERSON)
			  );
			
			Log.debug("Found " + ((testAuthPersons == null) ?
			 "no" : testAuthPersons.size()) + " candidate auth persons");
			
			if (testAuthPersons != null) {
				for (Person p : testAuthPersons) {
					printedPersons.add(p);
					authPersons.add(p);
					numAuthPersons++;
					
					if (numAuthPersons >= MAX_LISTED_AUTH_PERSONS)
						break;
				}	
			}
		}
		
		if (numAuthPersons < MIN_LISTED_AUTH_PERSONS)
			numAuthPersons = MIN_LISTED_AUTH_PERSONS;
		
		if (numAuthPersons > MAX_LISTED_AUTH_PERSONS)
			numAuthPersons = MAX_LISTED_AUTH_PERSONS;
		
		numAuthPersonSections = numAuthPersons;
		testPageCount += (numAuthPersonSections*PAGES_PER_AUTH_PERSON);

		Log.debug("Added " + authPersons.size() + 
		 " auth. persons, " + numAuthPersonSections + 
		 " auth person sections, pageCount = " + testPageCount);
		
		// Fetch account correspondent.
		
		if (populatePersonSections)
			mainContact = getCorrespondent();
		else
			mainContact = null;
		
		if (mainContact != null) {
			// Main Contact person details are available. Determine whether they were
			// already output in a previous section of the form.
			boolean alreadyPrinted = false;
			
			// Have to loop through all entries as conatins() doesn't seem to work
			// with person instances.
			for (Person printedPerson : printedPersons) {
				if (printedPerson.getPersonId() == mainContact.getPersonId())
					alreadyPrinted = true;
			}
			
			if (!alreadyPrinted) {
				// Correspondent not listed in prev. section, so they need to be
				// printed in Main Contact section
				printedPersons.add(mainContact);
				
				displayMainContactSection = true;
			} else {
				// Main Contact already printed in previous section.
				displayMainContactSection = false;
			}
			
		} else {
			// Display an empty Main Contact section.
			displayMainContactSection = true;
		}
		
		if (displayMainContactSection) {
			testPageCount += PAGES_PER_MAIN_CONTACT; // require empty Main Contact page
		}
			
		Log.debug("Checked main contact data, person data available? " 
		 + (mainContact!=null) + ", display section? " + displayMainContactSection + 
		 ", pageCount = " + testPageCount);
		
		// Determine how many signatories to display.
		Vector<Person> testSigs = null;
		
		if (populatePersonSections)
			testSigs = getSigs();

		int numSigs = 0;
		
		if (testSigs != null) {
			for (Person p : testSigs) {
				boolean alreadyPrinted = false;
				
				// Have to loop through all entries as conatins() doesn't seem to work
				// with person instances.
				for (Person printedPerson : printedPersons) {
					if (printedPerson.getPersonId() == p.getPersonId())
						alreadyPrinted = true;
				}
				
				if (!alreadyPrinted) {
					// Signatory not listed in Dirs/Controllers or Main Contact section,
					// so they need to be printed in this section
					sigs.add(p);
					numSigs++;
				}
				
				if (numSigs >= MAX_LISTED_SIGNATORIES)
					break;
			}
		}
		
		if (numSigs < MIN_LISTED_SIGNATORIES)
			numSigs = MIN_LISTED_SIGNATORIES;
		
		if (numSigs > MAX_LISTED_SIGNATORIES)
			numSigs = MAX_LISTED_SIGNATORIES;
		
		numSignatorySections = numSigs;
		testPageCount += (numSignatorySections * PAGES_PER_SIGNATORY);
		
		Log.debug("Adding " + ((sigs==null) ? "no" : sigs.size()) + 
		 " sigs, " + numSignatorySections + " signatory sections," + 
		 " final pageCount = " + testPageCount);
		
		totalPages = testPageCount;
		
		// Empty out printed persons list again
		printedPersons.clear();
	}

	/** Appends the Report template for a single Director/Controller.
	 * 
	 *  The passed Person instance can be null, this will add empty fields instead.
	 *  
	 * @param person
	 * @param linePrefix
	 * @throws IOException 
	 * @throws DataException 
	 */
	private void addDirectorOrController(Person person, int directorNumber, 
	 int totalDirectorOrControllers) throws DataException, IOException {
		
		// First director page
		// ------
		// Name, contact details
		addReportHeader(getReportId(ReportSections.DIR_INFO_P1, null));
		addNameValueString("NumberOfDirectors", 
		 Integer.toString(totalDirectorOrControllers));
		addNameValueString("DirectorNumber", Integer.toString(directorNumber));
		
		String linePrefix = "Dir";
		
		if (person != null)
			printedPersons.add(person);
		
		// Add name/DOB/position fields
		SpoolFileUtils.addPersonDetails(this, person, linePrefix);

		// Add Telephone/Email fields
		SpoolFileUtils.addPersonContactDetails(this, person, linePrefix);

		Contact personHomeAddressContact = null;
		Address personHomeAddress = null;
		
		// Fetch person's Experian/Home address
		if (person != null) {
			personHomeAddressContact = Contact.getExperianContact
			 (person.getContacts(), Contact.ADDRESS);
			
			if (personHomeAddressContact == null)
			personHomeAddressContact = Contact.getFirstContactBySubMethod
			 (person.getContacts(), Contact.SUBMETHOD_ADDRESS_HOME);
		}
		
		if (personHomeAddressContact != null)
			personHomeAddress = personHomeAddressContact.getAddress();
		
		// Spread the normalized home address over a 21x3 grid (minus the postcode)
		Vector<SpoolFileLine> homeAddressLines = SpoolFileGenerator.getGridFormatAddress
		 (personHomeAddress, linePrefix + "AddressLine", false, false, 26, 3);
		
		// Add home address lines
		addLines(homeAddressLines, NAME_VALUE_PADDING);
		
		addNameValueString(linePrefix + "AddressPostcode", 
		 personHomeAddress != null 
		 ? CCLAUtils.stripSpaceCharacters(personHomeAddress.getPostCode()) : null);
		
		// TODO
		addNameValueString(linePrefix + "DateMovedToAddress", null);
		
		// Fetch person's previous home address
		Contact personPrevHomeAddressContact = null;
		Address personPrevHomeAddress = null;
		
		if (person != null)
			personPrevHomeAddressContact = Contact.getFirstContactBySubMethod
			 (person.getContacts(), Contact.SUBMETHOD_ADDRESS_PREVIOUS);
			
		if (personPrevHomeAddressContact != null)
			personPrevHomeAddress = personPrevHomeAddressContact.getAddress();
		
		// If the previous address matches their actual home address, don't bother
		// printing it.
		if (personHomeAddress != null
			&&
			personPrevHomeAddress != null
			&&
			personHomeAddress.getAddressId() == personPrevHomeAddress.getAddressId())
			personPrevHomeAddress = null;
		
		// Print the normalized sig prev home address over the 21x3 grid, followed 
		// by the postcode.
		Vector<SpoolFileLine> personPrevAddressLines = 
		 SpoolFileGenerator.getGridFormatAddress
		 (personPrevHomeAddress, linePrefix + "PrevAddressLine", false, false, 26, 3);
		
		addLines(personPrevAddressLines, NAME_VALUE_PADDING);
	
		addNameValueString(linePrefix + "PrevAddressPostcode", 
		 personPrevHomeAddress != null 
		 ? CCLAUtils.stripSpaceCharacters(personPrevHomeAddress.getPostCode()) : null);

		addNameValueString(linePrefix + "PrevAddrDateMovedTo", null);
		
		// Second director page. Contains preference tickboxes
		// ---------------
		addReportHeader(getReportId(ReportSections.DIR_INFO_P2, null));

		addNameValueString("NumberOfDirectors", 
		 Integer.toString(totalDirectorOrControllers));
		addNameValueString("DirectorNumber", Integer.toString(directorNumber));
		
		addNameValueString(linePrefix + "PassportNumber", null);
		addNameValueString(linePrefix + "PassportFullName", null);
		
		addNameValueString(linePrefix + "DrivingLicenceNumber", null);
		addNameValueString(linePrefix + "DrivingLicenceFullName", null);

		// Whether or not this auth. person is also marked as the correspondent.
		addNameValueString(linePrefix + "IsMainContact", 
		 person != null ?
		 SpoolFileUtils.getTickboxValue(person.equals(mainContact)) : null);
		
		// Special address section - 'Address for correspondence' 
		// This is only populated if:
		// -this director is also the Main Contact
		// -his nominated/default address DOES NOT match the printed home address
		//  on previous page
		Address corrAddress = null;
		
		if (person != null && person.equals(mainContact)) {
			Contact corrAddressContact = Contact.getDefaultContact
			 (person.getContacts(), Contact.ADDRESS);
			
			if ((personHomeAddressContact == null) 
				||
				(corrAddressContact != null 
				&& personHomeAddressContact.getContactId() 
				!= corrAddressContact.getContactId())) {
				corrAddress = corrAddressContact.getAddress();
			}
		}
		
		addNameValueString(linePrefix + "PrefOtherAddress", 
		 SpoolFileUtils.getTickboxValue(corrAddress != null));
		
		// Print the normalized sig prev home address over the 26x3 grid, followed 
		// by the postcode.
		Vector<SpoolFileLine> corrAddressLines = 
		 SpoolFileGenerator.getGridFormatAddress
		 (corrAddress, linePrefix + "AddressLine", false, false, 26, 3);
		
		addLines(corrAddressLines, NAME_VALUE_PADDING);
		
		addNameValueString(linePrefix + "AddressPostcode", 
		 corrAddress != null 
		 ? CCLAUtils.stripSpaceCharacters(corrAddress.getPostCode()) : null);
		
		// Correspondence preference fields.
		Contact defaultDirAddressContact = null;
		
		if (person != null) {
			defaultDirAddressContact = Contact.getDefaultContact
			 (person.getContacts(), Contact.ADDRESS);
		} 
		
		SpoolFileUtils.addCorrespondencePref
		 (this,defaultDirAddressContact, linePrefix);
		
		// Whether or not this auth. person is also marked as a signatory.
		addNameValueString(linePrefix + "IsAuthSig", 
		 SpoolFileUtils.getTickboxValue(isAuthSig(person)));
		
		boolean dirDoNotContact = false;
		
		if (person != null && person.isDoNotContact())
			dirDoNotContact = true;
			
		addNameValueString(linePrefix + "DoNotContact", 
		 dirDoNotContact ? "Y" : null);
	}
	
	/** Determines whether the passed person has a Signatory relationship to the org
	 *  
	 * @param person
	 * @return null if the passed Person is null.
	 * @throws DataException 
	 */
	private Boolean isAuthSig(Person person) throws DataException {
		
		if (person == null)
			return null;

		// Fetch all signatories
		Vector<Person> sigs = getSigs();
		
		if (person != null && sigs != null) {
			for (Person sig : sigs) {
				if (sig.equals(person)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/** Determines whether the passed person has a Correspondent relationship to the 
	 *  org.
	 *  
	 * @param person
	 * @return null if the passed Person is null.
	 * @throws DataException 
	 */
	private Boolean isOrgCorrespondent(Person person) throws DataException {
		
		if (person == null)
			return null;

		Vector<Person> corrs = infoForm.getRelatedOrgPersons().get(
				RelationName.getCache().getCachedInstance(
				 RelationName.OrganisationPersonRelation.CORRESPONDENT)
			);
		
		if (corrs == null)
			return false;
		
		for (Person p : corrs) {
			if (p.equals(person))
				return true;
		}
		
		return false;
	}
	
	private Vector<Person> getSigs() throws DataException {
		return infoForm.getRelatedOrgPersons().get(
			RelationName.getCache().getCachedInstance(
			 RelationName.PersonAccountRelation.SIGNATORY)
		);
	}
	
	/** Returns the designated form correspondent, i.e. the nominated correspondent
	 *  for all the listed accounts on the form.
	 *  
	 * @return
	 * @throws DataException
	 */
	private Person getCorrespondent() throws DataException {
		return infoForm.getCorrespondent();
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
