package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.aurora.webservice.BankDetails;
import com.ecs.stellent.ccla.clientservices.form.PSDFRegistrationForm;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.data.*;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;


/** Generates a pre-filled PSDF Registration Form spool file.
 * 
 *  This will be addressed to the nominated correspondent/address for the client, as
 *  specified by the CampaignEnrolment data.
 *  
 *  A single client may receive multiple copies of this form if they choose to open
 *  more than 1 PSDF account.
 *  
 * @author Tom Marchant
 *
 */
public class PSDFRegistrationSpoolFileGenerator extends SpoolFileGenerator {

	/** Determines whether the form will print differently based on the LA/Non-LA form
	 *  types. Setting to true will ignore the differences and print the newer, generic
	 *  version of the form regardless of the passed form type.
	 */
	private static final boolean IGNORE_LA_FORM_TYPES = true;
	
	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	/** Minimum number of Director/Controller sections that will be generated. */
	private static final int MIN_LISTED_AUTH_PERSONS = 4;
	/** Max number of Director/Controller entries. */
	private static final int MAX_LISTED_AUTH_PERSONS = 6;
	
	/** Minimum number of Signatory sections that will be generated. */
	private static final int MIN_LISTED_SIGNATORIES = 2;
	/** Max number of Signatory entries. */
	private static final int MAX_LISTED_SIGNATORIES = 5;
	
	// CreateForm report IDs.
	
	/** Prefix string for each CreateForm page template */
	private String PAGE_REPORT_ID_PREFIX;
	
	private static class ReportSections {
		static final String INVESTOR_DETAILS 			= "S1";
		static final String PAYMENT_DETAILS 			= "S2";
		static final String DIRECTORS_AND_CONTROLLERS 	= "S3";
		static final String MAIN_CONTACT_DETAILS 		= "S4";
		static final String SIGNATORY_DETAILS 			= "S5";
		static final String DOC_CHECKLIST				= "CL";
	}
	
	/** Container for all the data objects required to generate the spool file, e.g.
	 *  Organisation, Person, Contact Details.
	 *  
	 */
	private PSDFRegistrationForm regForm;
	
	/** After a person has their details output in one section, they are added to this
	 *  HashSet to prevent their details being reprinted in a later section.
	 */
	private HashSet<Person> printedPersons = new HashSet<Person>();
	
	/** Page counters, used for printing 'Page x of y' captions on each report page.
	 * 
	 */
	int currentPage 	= 1;
	int totalPages 		= 10;
	
	// These vars are mapped during the initial form creation process.
	private Vector<Person> authPersons 	= null;
	private Person mainContact			= null;
	private Vector<Person> sigs 		= null;
	
	/** Counter for unmapped entries in the spool file.
	 * 
	 */
	int missingLineCount = 1;
	
	public PSDFRegistrationSpoolFileGenerator(SpoolHeader spoolHeader, String docName,
	 Integer enrolmentId, PSDFRegistrationForm regForm) {
		
		super(spoolHeader, docName, enrolmentId);
		
		this.regForm	= regForm;
		
		if (IGNORE_LA_FORM_TYPES) {
			// New generic report prefix - supersedes the LA/Non-LA form types.
			PAGE_REPORT_ID_PREFIX = "PSICAPP";
		} else {
			if (regForm.isLocalAuthority())
				PAGE_REPORT_ID_PREFIX	= "PSICLA";
			else
				PAGE_REPORT_ID_PREFIX	= "PSICNLA";
		}
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
	private void appendFormData()
	 throws DataException, IOException, ServiceException {
		
		// Calculate person mapping and total page count first.
		preparePersonMapping();
		
		Vector<Contact> orgContacts = regForm.getEntity().getContacts();
		
		// Investor Details section (page 1)
		// ---------------------------------
		// Displays correspondent name, postal address and basic Org details
		addReportHeader(getReportId(ReportSections.INVESTOR_DETAILS, 1));
		
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
		
		addNameValueString("LetterDate", EIGHT_CHAR_DATE_FORMAT.format(new Date()));
		addNameValueString("OrgName", regForm.getEntity().getOrganisationName());
		
		addNameValueString("AccountName", regForm.getAccount().getSubtitle());
		addNameValueString("OrgType", regForm.getOrgCategoryTree().get(0));
		
		if (!IGNORE_LA_FORM_TYPES) {
			if (regForm.isLocalAuthority()) {
				addNameValueString("ONSNumber", regForm.getOrgIdentifiers().get
				 (ElementIdentifier.ONS_NUMBER));
				
				addNameValueString("AuthConfirmation", null);
			}
			
			if (!regForm.isLocalAuthority())
				addNameValueString("OrgDescription", null);
		}
		
		// Investor Details section (Section 1)
		// ---------------------------------
		// Displays some Org Identifiers and the Org Address.
		addReportHeader(getReportId(ReportSections.INVESTOR_DETAILS, 2));
		
		addNameValueString("GoverningAuth", null);
		addNameValueString("EligibleToReceiveGross", null);
		addNameValueString("AuthorisedToInvest", null);
		
		String isRegWithCharityComm = "";
		
		if (regForm.getOrgIdentifiers().containsKey(ElementIdentifier.CHARITY_REF))
			isRegWithCharityComm = "Y";
			
		addNameValueString("RegWithCharityCommOrOSCR", isRegWithCharityComm);
		
		addNameValueString("HMRCNo", regForm.getOrgIdentifiers().get
		 (ElementIdentifier.HMRC_NUMBER));
		addNameValueString("CharityRegNo", regForm.getOrgIdentifiers().get
		 (ElementIdentifier.CHARITY_REF));
		
		// Add the Organisation Address, if applicable, otherwise blank lines.
		Contact orgAddressContact = 
		 Contact.getDefaultContact(orgContacts, Contact.ADDRESS);
		
		Address orgAddress = null;

		if (orgAddressContact != null) {
			orgAddress = orgAddressContact.getAddress();
		}
		
		// Spread the normalized org address over a 21x4 grid (minus the postcode)
		Vector<SpoolFileLine> orgAddressLines = SpoolFileGenerator.getGridFormatAddress
		 (orgAddress, "OrgAddressLine", false, false, 26, 4);
		
		addLines(orgAddressLines, NAME_VALUE_PADDING);
			
		addNameValueString("OrgAddressPostcode", 
		 orgAddress != null 
		 ? CCLAUtils.stripSpaceCharacters(orgAddress.getPostCode()) : null);
		
		Contact orgTelephone = Contact.getDefaultContact(orgContacts, Contact.PHONE);
		Contact orgWebsite = Contact.getDefaultContact(orgContacts, Contact.WEB);
		
		String orgTelephoneStr = null;
		String orgWebsiteStr = null;
		
		if (orgTelephone != null)
			orgTelephoneStr = CCLAUtils.stripSpaceCharacters(orgTelephone.getValue());
		
		if (orgWebsite != null)
			orgWebsiteStr = orgWebsite.getValue();
		
		addNameValueString("OrgTelephone", orgTelephoneStr);
		addNameValueString("OrgWebsite", CCLAUtils.truncateWebUrl(orgWebsiteStr));
		
		// Payment Details section (Section 2)
		// -----------------------
		// Contains bank account details and income distribution preferences
		addReportHeader(getReportId(ReportSections.PAYMENT_DETAILS, null));
		
		// Nominated bank account details
		BankAccount bankAccount = regForm.getBankAccount();
		
		SpoolFileUtils.addBankAccountDetails(this, bankAccount);
		
		// Fetch income dist method from the mandated PI account.
		String incDistMethod = regForm.getMandatedAccount().getIncomeDistMethod();

		boolean reinvestDividend = incDistMethod.equals
		 (Account.IncomeDistMethod.REIN.toString());
		
		addNameValueString("ReinvestDividend", reinvestDividend ? "Y" : null);
		
		if (reinvestDividend) {
			// If account is set for re-investment, add blank pay-away preferences
			addNameValueString("PayToNominatedAccount", null);
			addNameValueString("PayToOtherAccount", null);

			SpoolFileUtils.addBankAccountDetails(this, null);
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
			
			SpoolFileUtils.addBankAccountDetails(this, payAwayBankAccount);
		}

		// Directors' and Controllers' Authorisation section (Section 3)
		// -------
		// Contains details for up to 4 Directors/Controllers. This will correspond
		// to the first 4 Authorising Persons linked to the account.
		
		// The first section page contains a few basic preference fields.
		addReportHeader(getReportId(ReportSections.DIRECTORS_AND_CONTROLLERS, 1));
			
		// Add tick to box if at least 1 auth person is attached to the account
		addNameValueString("ListOfDirsAndControllers",
		 authPersons != null && !authPersons.isEmpty() ? "Y" : null);
		
		// Number of required signatures preference.
		addNameValueString("ReqOneAuthSig", 
		 regForm.getAccount().getRequiredSignatures() == 1 ? "Y" : null);
		addNameValueString("ReqTwoAuthSig", 
		 regForm.getAccount().getRequiredSignatures() == 2 ? "Y" : null);
		
		// Print out summary information for the first 4 auth. persons on the cover page.
		for (int i = 1; i <= MIN_LISTED_AUTH_PERSONS; i++) {
			
			Person authPerson = null;
			
			if (authPersons.size() > (i-1))
				authPerson = authPersons.get(i-1);
		
			addNameValueString("Dir" + i + "Name", 
			 authPerson != null ? authPerson.getFullName() : null);
			//addNameValueString("Dir" + i + "Signature", null);
			//addNameValueString("Dir" + i + "Date", null);
		}
		
		// Add data for up to five auth. persons (1 per page)
		for (int i = 1; i <= MAX_LISTED_AUTH_PERSONS; i++) {
			if (i > MIN_LISTED_AUTH_PERSONS 
				&&
				i > authPersons.size())
				break;
			
			Person authPerson = null;
			
			if (authPersons.size() > (i-1))
				authPerson = authPersons.get(i-1);
		
			addDirectorOrController(authPerson, i);
		}
		
		// Main Contact details section (Section 4)
		// ----------------------------
		// Displays account correspondent details, if they weren't listed in the
		// previous section. If the acc. corr. was previously listed, this section is
		// skipped.
		
		// Get Main Contact (i.e. Account Correspondent)
		if (mainContact != null && authPersons.contains(mainContact)) {
			// Skip this section if the assigned account correspondent was printed
			// in the Directors and Controllers section.
			
		} else {
			if (IGNORE_LA_FORM_TYPES) {
				// Just one Main Contact Details page on the generic form.
				addReportHeader(getReportId(ReportSections.MAIN_CONTACT_DETAILS, null));
				
			} else {
				if (!regForm.isLocalAuthority()) {
					// Section 4, Page 1 for the Non-LA form.
					addReportHeader(getReportId(ReportSections.MAIN_CONTACT_DETAILS, 1));
				} else {
					// Section 4 for LA form (only 1 page)
					addReportHeader(getReportId(ReportSections.MAIN_CONTACT_DETAILS, null));	
				}
			}
			
			printedPersons.add(mainContact);
			
			String linePrefix = "Corr";
			
			// Add name/position/DOB fields for main contact
			SpoolFileUtils.addPersonDetails(this, mainContact, linePrefix);
			
			// Add Tel/Email fields
			SpoolFileUtils.addPersonContactDetails(this, mainContact, linePrefix);
			
			if (!IGNORE_LA_FORM_TYPES) {
				if (!regForm.isLocalAuthority()) {
					SpoolFileUtils.addPersonShortAddress(this, mainContact, linePrefix);
				}
				
				if (!regForm.isLocalAuthority()) {
					
					// Add previous corr address on Non-LA form.
					Contact corrPrevAddressContact = 
					 Contact.getFirstContactBySubMethod
					 (mainContact.getContacts(), Contact.SUBMETHOD_ADDRESS_PREVIOUS);
					
					Address corrPrevAddress = null;
	
					if (corrPrevAddressContact != null) {
						corrPrevAddress = corrPrevAddressContact.getAddress();
					}
					
					// Spread the normalized org address over a 21x3 grid (minus the postcode)
					Vector<SpoolFileLine> corrPrevAddressLines = 
					 SpoolFileGenerator.getGridFormatAddress
					 (corrPrevAddress, "CorrPrevAddressLine", false, false, 26, 3);
					
					addLines(corrPrevAddressLines, NAME_VALUE_PADDING);
					
					addNameValueString("CorrPrevAddressPostcode", corrPrevAddress != null ?
					 CCLAUtils.stripSpaceCharacters(corrPrevAddress.getPostCode()) : null);
					
					// TODO
					addNameValueString("CorrPrevAddrDateMovedTo", null);
	
					// Section 4, Page 2 for the Non-LA form.
					addReportHeader(getReportId(ReportSections.MAIN_CONTACT_DETAILS, 2));
					
					// Correspondence preference fields.
					Contact defaultCorrAddressContact = null;
					
					if (mainContact != null) {
						defaultCorrAddressContact = Contact.getDefaultContact
						 (mainContact.getContacts(), Contact.ADDRESS);
					} 
					
					SpoolFileUtils.addCorrespondencePref
					 (this, defaultCorrAddressContact, linePrefix);
				}
			}
				
			// Whether or not the correspondent is also marked as an account signatory.
			addNameValueString(linePrefix + "IsAuthSig", 
			 SpoolFileUtils.getTickboxValue(isAuthSig(mainContact)));
			
			boolean dirDoNotContact = false;
			
			if (mainContact != null && mainContact.isDoNotContact())
				dirDoNotContact = true;
				
			addNameValueString(linePrefix + "DoNotContact", 
			 dirDoNotContact ? "Y" : null);
			
			if (IGNORE_LA_FORM_TYPES) {
				// Require additional sigs flag
				addNameValueString("ReqAdditionalSigs", !sigs.isEmpty() ? "Y" : "N");
			}
		}
		
		// Add remaining account signatories (i.e. Persons marked as Signatory to 
		// account but not Auth. Person/Correspondent, so they won't have been 
		// displayed in the Directors/Main Contact section)
		for (int i=1; i<=MAX_LISTED_SIGNATORIES; i++) {
			if (i > MIN_LISTED_SIGNATORIES 
				&&
				i > sigs.size())
				break;
			
			Person sig = null;
		
			if (sigs.size() > (i-1))
				sig = sigs.get(i-1);
		
			addReportHeader(getReportId(ReportSections.SIGNATORY_DETAILS, null));
			addNameValueString("SignatoryNumber", Integer.toString(i));
			
			String linePrefix = "Sig";
			
			// Add name/position/DOB fields for signatory
			SpoolFileUtils.addPersonDetails(this, sig, linePrefix);
			
			// Add Tel/Email fields
			SpoolFileUtils.addPersonContactDetails(this, sig, linePrefix);
			
			if (!IGNORE_LA_FORM_TYPES) {
				if (!regForm.isLocalAuthority()) {
					// Output basic address details for Non-LA signatories
					SpoolFileUtils.addPersonShortAddress(this, sig, linePrefix);
				}
			}
			
			boolean dirDoNotContact = false;
			
			if (sig != null && sig.isDoNotContact())
				dirDoNotContact = true;
				
			addNameValueString(linePrefix + "DoNotContact", 
			 dirDoNotContact ? "Y" : null);
		}

		// Add Checklist at the end.
		addReportHeader(getReportId(ReportSections.DOC_CHECKLIST, null));
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
	private void preparePersonMapping() throws DataException {
		
		// Min. no. of pages before considering person data. Assumes no dirs/
		// controllers, main contact or signatory data will be output.
		// -2 pages for Investor Details
		// -1 page for Payment Details
		// -1 page for Dirs/Controllers section first page
		// -1 page for Doc Checklist
		int testPageCount = 5;
		
		authPersons 	= new Vector<Person>();
		sigs 			= new Vector<Person>();
		
		// Fetch all authorising persons for account. These will be printed in the
		// auth persons section and require 1 page each
		Vector<Person> testAuthPersons = regForm.getRelatedAccountPersons().get
		 (RelationName.getCache().getCachedInstance(
			RelationName.PersonAccountRelation.AUTH_PERSON)
		  );
		
		int numAuthPersons = 0;
		
		if (testAuthPersons != null) {
			for (Person p : testAuthPersons) {
				printedPersons.add(p);
				authPersons.add(p);
				numAuthPersons++;
				
				if (numAuthPersons >= MAX_LISTED_AUTH_PERSONS)
					break;
			}	
		}

		if (numAuthPersons < MIN_LISTED_AUTH_PERSONS)
			numAuthPersons = MIN_LISTED_AUTH_PERSONS;
		
		testPageCount += numAuthPersons;
		
		Log.debug("Added " + authPersons.size() + 
		 " auth. persons, pageCount = " + testPageCount);
		
		// Fetch account correspondent. 
		mainContact = getCorrespondent();
		
		if (mainContact != null) {
			if (!printedPersons.contains(mainContact)) {
				// Correspondent not listed in prev. section, so they need to be
				// printed in Main Contact section
				printedPersons.add(mainContact);
				
				testPageCount++;
				
				if (!IGNORE_LA_FORM_TYPES) {
					if (!regForm.isLocalAuthority())
						testPageCount++; // Non-LA form has 2 pages for Main Contact.
				}
			}
		} else
			testPageCount++; // require empty Main Contact page
		
		Log.debug("Added correspondent, pageCount = " + testPageCount);
		
		// Fetch all signatories for account. These will be printed in the
		// auth persons section and require 1 page each
		Vector<Person> testSigs = getSigs();
		int numSigs = 0;
		
		if (testSigs != null) {
			for (Person p : testSigs) {
				if (!printedPersons.contains(p)) {
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
		
		testPageCount += numSigs;
		
		Log.debug("Added " + sigs.size() + 
		 " sigs, final pageCount = " + testPageCount);
		
		totalPages = testPageCount;
		
		// Empty out printed persons list again
		printedPersons.clear();
	}

	private String getMissingOutput() {
		NumberFormat fmt = NumberFormat.getInstance();
		fmt.setMinimumIntegerDigits(2);
		
		return fmt.format(missingLineCount++) + 
		 "-aBcDeFgHiJkLmNoPqRsTuVwXyZaBcDeFgHiJkLmNoPqRsTuVwXyZ";
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
	private void addDirectorOrController(Person person, int directorNumber) 
	 throws DataException, IOException {
		
		addReportHeader(getReportId(ReportSections.DIRECTORS_AND_CONTROLLERS, 2));
		addNameValueString("DirectorNumber", Integer.toString(directorNumber));
		
		String linePrefix = "Dir";
		
		if (person != null)
			printedPersons.add(person);
		
		// Add name/DOB/position fields
		SpoolFileUtils.addPersonDetails(this, person, linePrefix);

		// Add Telephone/Email fields
		SpoolFileUtils.addPersonContactDetails(this, person, linePrefix);

		Vector<Contact> personContacts = null;
		Address personHomeAddress = null;
		
		if (person != null)
			personContacts = person.getContacts();
		
		// Fetch person's home address
		Contact personHomeAddressContact = 
		 personContacts == null ? null : 
		 Contact.getFirstContactBySubMethod
		 (personContacts, Contact.SUBMETHOD_ADDRESS_HOME);
		
		if (personHomeAddressContact != null)
			personHomeAddress = personHomeAddressContact.getAddress();
		
		// Spread the normalized home address over a 21x3 grid (minus the postcode)
		Vector<SpoolFileLine> homeAddressLines = SpoolFileGenerator.getGridFormatAddress
		 (personHomeAddress, linePrefix + "AddressLine", false, false, 26, 3);
		
		// Add home address lines
		// ----------------------
		addLines(homeAddressLines, NAME_VALUE_PADDING);
		
		addNameValueString(linePrefix + "AddressPostcode", 
		 personHomeAddress != null 
		 ? CCLAUtils.stripSpaceCharacters(personHomeAddress.getPostCode()) : null);
		
		// TODO
		addNameValueString(linePrefix + "DateMovedToAddress", null);
		
		// Fetch person's previous home address
		Address personPrevHomeAddress = null;
		
		Contact personPrevHomeAddressContact = 
		 personContacts == null ? null :
		 Contact.getFirstContactBySubMethod
		 (personContacts, Contact.SUBMETHOD_ADDRESS_PREVIOUS);
		
		if (personPrevHomeAddressContact != null)
			personPrevHomeAddress = personPrevHomeAddressContact.getAddress();
		
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
		
		// Whether or not this auth. person is also marked as an account correspondent.
		addNameValueString(linePrefix + "IsMainContact", 
		 SpoolFileUtils.getTickboxValue(isAccountCorrespondent(person)));
				
		// Correspondence preference fields.
		Contact defaultDirAddressContact = null;
		
		if (person != null) {
			defaultDirAddressContact = Contact.getDefaultContact
			 (person.getContacts(), Contact.ADDRESS);
		} 
		
		SpoolFileUtils.addCorrespondencePref
		 (this,defaultDirAddressContact, linePrefix);
		
		// Whether or not this auth. person is also marked as an account signatory.
		addNameValueString(linePrefix + "IsAuthSig", 
		 SpoolFileUtils.getTickboxValue(isAuthSig(person)));
		
		boolean dirDoNotContact = false;
		
		if (person != null && person.isDoNotContact())
			dirDoNotContact = true;
			
		addNameValueString(linePrefix + "DoNotContact", 
		 dirDoNotContact ? "Y" : null);
	}
	
	/** Adds 3 lines to the file in the following format:
	 *  
	 *  <xxTitle>		Mr
	 *	<xxLastName>	Jameson
	 *	<xxDate>		
	 * 
	 *  Where xx is the passed linePrefix and Date is the current date.
	 * @throws IOException 
	 * @throws DataException 
	 * 
	 */
	private void addBasicPersonDetails(Person person, String linePrefix)
	 throws DataException, IOException {
	
		PersonTitle title = null;
		
		if (person != null && person.getTitleId() != null)
			title = PersonTitle.getCache().getCachedInstance(person.getTitleId());
		
		addNameValueString(linePrefix + "Title",
		 title != null ? title.getTitle() : null);

		addNameValueString(linePrefix + "LastName",
		 person != null ? person.getLastName() : null);
		
		addNameValueString(linePrefix + "Date", getMissingOutput());
	}
	
	/** Determines whether the passed person has a Signatory relationship to the 
	 *  account.
	 *  
	 * @param person
	 * @return null if the passed Person is null.
	 * @throws DataException 
	 */
	private Boolean isAuthSig(Person person) throws DataException {
		
		if (person == null)
			return null;

		// Fetch all account signatories
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
	 *  account.
	 *  
	 * @param person
	 * @return null if the passed Person is null.
	 * @throws DataException 
	 */
	private Boolean isAccountCorrespondent(Person person) throws DataException {
		
		if (person == null)
			return null;

		Vector<Person> corrs = regForm.getRelatedAccountPersons().get(
				RelationName.getCache().getCachedInstance(
				 RelationName.PersonAccountRelation.CORRESPONDENT)
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
		return regForm.getRelatedAccountPersons().get(
			RelationName.getCache().getCachedInstance(
			 RelationName.PersonAccountRelation.SIGNATORY)
		);
	}
	
	/** Returns the first account correspondent found, or null if nobody is mapped to
	 *  the role.
	 *  
	 * @return
	 * @throws DataException
	 */
	private Person getCorrespondent() throws DataException {
		Vector<Person> corrs =  regForm.getRelatedAccountPersons().get(
			RelationName.getCache().getCachedInstance(
			 RelationName.PersonAccountRelation.CORRESPONDENT)
		);
		
		if (corrs != null && corrs.size() > 0)
			return corrs.get(0);
		else
			return null;
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