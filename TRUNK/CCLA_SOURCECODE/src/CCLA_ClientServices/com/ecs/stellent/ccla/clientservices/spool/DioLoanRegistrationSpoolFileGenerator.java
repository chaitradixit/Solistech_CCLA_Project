package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.AccountRegistrationForm;
import com.ecs.stellent.ccla.clientservices.form.DioLoanRegistrationForm;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;

/** Generates spool files for Diocesan Loan application forms.
 * 
 * @author Tom
 *
 */
public class DioLoanRegistrationSpoolFileGenerator extends SpoolFileGenerator {

	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	/** Minimum number of Director/Controller sections that will be generated. */
	private static final int MIN_LISTED_AUTH_PERSONS = 2;
	/** Max number of Director/Controller entries. */
	private static final int MAX_LISTED_AUTH_PERSONS = 10;
	
	/** Minimum number of Signatory sections that will be generated. */
	private static final int MIN_LISTED_SIGNATORIES = 2;
	/** Max number of Signatory entries. */
	private static final int MAX_LISTED_SIGNATORIES = 5;
	
	// CreateForm report IDs.
	
	/** Prefix string for each CreateForm page template */
	private String PAGE_REPORT_ID_PREFIX = "CBFDLA";
	
	private static class ReportSections {
		static final String BORROWER_DETAILS 			= "S1";
		static final String PAYMENT_DETAILS 			= "S2";
		static final String AUTHORISING_PERSONS 		= "S3";
		static final String CORRESPONDENT_DETAILS 		= "S4";
		static final String SIGNATORY_DETAILS 			= "S5";
		static final String DOC_CHECKLIST				= "ACL";
		static final String INFO						= "INFO";
	}
	
	/** Container for all the data objects required to generate the spool file, e.g.
	 *  Organisation, Person, Contact Details.
	 *  
	 */
	private DioLoanRegistrationForm regForm;
	
	/** After a person has their details output in one section, they are added to this
	 *  HashSet to prevent their details being reprinted in a later section.
	 */
	private HashSet<Person> printedPersons = new HashSet<Person>();
	
	/** Page counters, used for printing 'Page x of y' captions on each report page.
	 * 
	 *  The 'totalPages' var gets set in the preparePersonMapping method.
	 * 
	 */
	int currentPage 	= 1;
	int totalPages 		= 1;
	
	// These vars are mapped during the initial form creation process.
	private Vector<Person> authPersons 	= null;
	private Person mainContact			= null;
	private Vector<Person> sigs 		= null;
	
	public DioLoanRegistrationSpoolFileGenerator
	 (SpoolHeader header, DioLoanRegistrationForm regForm) {
		super(header);
		
		this.regForm = regForm;
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
	
	@Override
	public ByteArrayOutputStream createSpoolFile() throws IOException,
			DataException, ServiceException {
		
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

		// Indicate whether client has requested Email Indemnity authorisation
		boolean emailIndemnityRequested = false;

		ElementAttributeApplied emailIndemnityReceivedAttr = 
		 regForm.getOrgAttributes().get
		 (ElementAttribute.OrganisationAttributes.EMAIL_INDEMNITY_RECEIVED);
		
		// Indicate whether an Email Indemnity has already been received for the
		// client
		boolean emailIndemnityReceived = (emailIndemnityReceivedAttr != null) &&
		 emailIndemnityReceivedAttr.getStatus();
		
		ElementAttributeApplied emailIndemnityReqAttr = 
		 regForm.getOrgAttributes().get
		 (ElementAttribute.OrganisationAttributes.EMAIL_INDEMNITY_REQUESTED);
		
		if (emailIndemnityReqAttr != null && emailIndemnityReqAttr.getStatus())
			emailIndemnityRequested = true;
		
		// Borrower Details section (page 1)
		// ---------------------------------
		// Displays correspondent name, postal address and basic Org details
		addReportHeader(getReportId(ReportSections.BORROWER_DETAILS, 1));
		
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
		
		addNameValueString("OrgID", this.regForm.getEntity().getOrgAccountCode());
		
		addNameValueString("LetterDate", EIGHT_CHAR_DATE_FORMAT.format(new Date()));
		addNameValueString("OrgName", regForm.getEntity().getOrganisationName());
		
		//addNameValueString("AccountName", regForm.getAccount().getSubtitle());

		// Add the Diocese address.
		Contact dioAddressContact = Contact.getDefaultContact
		 (regForm.getEntity().getContacts(), Contact.ADDRESS);
		
		Address dioAddress = dioAddressContact != null ?
		 dioAddressContact.getAddress() : null;
		
		Vector<SpoolFileLine> dioAddressLines = SpoolFileGenerator.getGridFormatAddress
		 (dioAddress, "AddressLine", false, false, 21, 4);
		
		// Add home address lines
		// ----------------------
		addLines(dioAddressLines, NAME_VALUE_PADDING); 
		
		/*
		Vector<String> dioAddressLines = 
		 dioAddress != null ? dioAddress.getPrintableAddress(false, false) : null;
		 
		// Print the Diocese address over 4 lines, postcode separate at the bottom.
		for (int j=0; j<4; j++) {
			String addressPiece = null;
			
			if (dioAddress != null && dioAddressLines.size() > j)
				addressPiece = dioAddressLines.get(j);
			
			addNameValueString(
			 "AddressLine" + (j+1), addressPiece);
		}
		*/
		
		addNameValueString("AddressPostcode", 
		 dioAddress != null ? 
		 CCLAUtils.stripSpaceCharacters(dioAddress.getPostCode()) : null);
		
		Contact orgPhone 	= Contact.getDefaultContact(orgContacts, Contact.PHONE);
		//Contact orgWebsite 	= Contact.getDefaultContact(orgContacts, Contact.WEB);
		
		addNameValueString("OrgTelephone", 
		 orgPhone != null ? CCLAUtils.stripSpaceCharacters(orgPhone.getValue()) : null);
		
		/*
		addNameValueString("OrgWebsite", 
		 orgWebsite != null ? orgWebsite.getValue() : null);
		*/
		
		addNameValueString("EmailIndemnityReceived", SpoolFileUtils.getTickboxValue(
		 emailIndemnityReceived));		
		
		// Payment Details section (Section 2)
		// -----------------------
		// Contains bank account details
		addReportHeader(getReportId(ReportSections.PAYMENT_DETAILS, null));
		
		// Nominated bank account details
		BankAccount bankAccount = regForm.getBankAccount();
		
		SpoolFileUtils.addBankAccountDetails(this, bankAccount);

		addNameValueString("IsKnownBankAccount", 
		 SpoolFileUtils.getTickboxValue(!regForm.isRequireBankStatement()));
		
		// Authorising Signatories section (Section 3)
		// -------
		// Contains details for up to 3 Auth Persons. This will correspond
		// to the first 3 Authorising Persons linked to the account.
		
		// The first section page contains a few basic preference fields.
		addReportHeader(getReportId(ReportSections.AUTHORISING_PERSONS, 1));
		
//		<SigsKnown>                Y
//		<SigsSupplied>  Y
//		<EmailIndemnityReceived>   Y
//		<EmailIndemnityRequired>   Y
		
		ElementAttributeApplied useExistingSigsAttr = 
		 regForm.getAccountAttributes().get
		 (ElementAttribute.AccountAttributes.USE_CURRENT_SIG_LIST);
		
		boolean useExistingSigs = 
		 (useExistingSigsAttr != null) && useExistingSigsAttr.getStatus();
		
		boolean sigsSupplied = !useExistingSigs;
		
		addNameValueString("SigsKnown", 
		 SpoolFileUtils.getTickboxValue(useExistingSigs));
		addNameValueString("SigsSupplied", 
		 SpoolFileUtils.getTickboxValue(sigsSupplied));
		
		addNameValueString("EmailIndemnityReceived", SpoolFileUtils.getTickboxValue(
		 emailIndemnityReceived));
		
		addNameValueString("EmailIndemnityRequired", SpoolFileUtils.getTickboxValue(
		 !emailIndemnityReceived && emailIndemnityRequested));
	
		// Add data for auth. persons.
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
		// Displays account correspondent details.
		addReportHeader(getReportId(ReportSections.CORRESPONDENT_DETAILS, null));	
		
		printedPersons.add(mainContact);
		
		String linePrefix = "Corr";
		
		// Add name/position/DOB fields for main contact
		SpoolFileUtils.addPersonDetails(this, mainContact, linePrefix, false, true);
		
		// Add Tel/Email fields
		SpoolFileUtils.addPersonContactDetails(this, mainContact, linePrefix);
		
		boolean corrDoNotContact = false;
		
		if (mainContact != null && mainContact.isDoNotContact())
			corrDoNotContact = true;
			
		addNameValueString(linePrefix + "DoNotContact", 
		 corrDoNotContact ? "Y" : null);
		
		// Whether or not the correspondent is also marked as an account signatory.
		addNameValueString(linePrefix + "IsAuthSig", 
		 SpoolFileUtils.getTickboxValue(isAuthSig(mainContact)));
		
		// Whether or not the next Signatory section will be output.
		addNameValueString(linePrefix + "OtherSigsRequired", 
		 SpoolFileUtils.getTickboxValue(!useExistingSigs));
	
		if (!useExistingSigs) {
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
				
				String siglinePrefix = "Sig";
				
				// Add name/position/DOB fields for signatory
				SpoolFileUtils.addPersonDetails(this, sig, siglinePrefix, false, true);
				
				// Add Tel/Email fields
				SpoolFileUtils.addPersonContactDetails(this, sig, siglinePrefix);
				
				// Output basic address details
				//SpoolFileUtils.addPersonShortAddress(this, sig, linePrefix);
				
				boolean sigDoNotContact = false;
				
				if (sig != null && sig.isDoNotContact())
					sigDoNotContact = true;
					
				addNameValueString(siglinePrefix + "DoNotContact", 
				 sigDoNotContact ? "Y" : null);
			}
		}

		// Add Checklist
		addReportHeader(getReportId(ReportSections.DOC_CHECKLIST, null));
		
//		<ImportantInfoPageNumber>  10
//		<EncloseBankStatement>     Y (or N)
//		<EncloseListOfAuthSigs>    Y (or N)
//		<EncloseEmailIndemnity>    Y (or N)
		
		//addNameValueString("ImportantInfoPageNumber", Integer.toString(totalPages));
	
		addNameValueString("EncloseBankStatement", 
		 SpoolFileUtils.getTickboxValue(regForm.isRequireBankStatement()));
		addNameValueString("EncloseListOfAuthSigs", 
		 SpoolFileUtils.getTickboxValue(!useExistingSigs));
		addNameValueString("EncloseEmailIndemnity", 
		 SpoolFileUtils.getTickboxValue
		 (!emailIndemnityReceived && emailIndemnityRequested));
		
		// Add Info Page
		addReportHeader(getReportId(ReportSections.INFO, null));
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
		
		addReportHeader(getReportId(ReportSections.AUTHORISING_PERSONS, 2));
		addNameValueString("DirectorNumber", Integer.toString(directorNumber));
		
		String linePrefix = "Dir";
		
		if (person != null)
			printedPersons.add(person);
		
		// Add name/DOB/position fields
		SpoolFileUtils.addPersonDetails(this, person, linePrefix);

		// Add Telephone/Email fields
		SpoolFileUtils.addPersonContactDetails(this, person, linePrefix);

		/*
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
		 (personHomeAddress, linePrefix + "AddressLine", false, false, 21, 3);
		
		// Add home address lines
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
		 (personPrevHomeAddress, linePrefix + "PrevAddressLine", false, false, 21, 3);
		
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
		
		*/
		
		boolean dirDoNotContact = false;
		
		if (person != null && person.isDoNotContact())
			dirDoNotContact = true;
			
		addNameValueString(linePrefix + "DoNotContact", 
		 dirDoNotContact ? "Y" : null);
		
		addNameValueString("ImportantInfoPageNumber", Integer.toString(totalPages));
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
		// controllers or signatory data will be output.
		// -1 pages for Borrower Details
		// -1 page for Payment Details
		// -1 page for Auth Persons section first page
		// -1 page for Main Contact
		// -1 page for Doc Checklist
		// -1 page for Important Info
		int testPageCount = 6;
		
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
				
				//testPageCount++;
			}
		}
		
		Log.debug("Added correspondent, pageCount = " + testPageCount);
		
		// We may skip displaying any signatories, if the account has the 'use existing
		// sig list' attribute set.
		ElementAttributeApplied useExistingSigsAttr = 
		 regForm.getAccountAttributes().get
		 (ElementAttribute.AccountAttributes.USE_CURRENT_SIG_LIST);
		
		boolean useExistingSigs = 
		 (useExistingSigsAttr != null) && useExistingSigsAttr.getStatus();
		
		// Fetch all signatories for account. These will be printed in the
		// auth persons section and require 1 page each
		if (!useExistingSigs) {
			Vector<Person> testSigs = getSigs();
			int numSigs = 0;
			
			if (testSigs != null) {
				for (Person p : testSigs) {
					if (!printedPersons.contains(p)) {
						// Signatory not listed in Dirs/Controllers or Main Contact
						// section, so they need to be printed in this section
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
		} else {
			Log.debug("Skipping over signatory section - using current sig. list");
		}
		
		totalPages = testPageCount;
		
		// Empty out printed persons list again
		printedPersons.clear();
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
