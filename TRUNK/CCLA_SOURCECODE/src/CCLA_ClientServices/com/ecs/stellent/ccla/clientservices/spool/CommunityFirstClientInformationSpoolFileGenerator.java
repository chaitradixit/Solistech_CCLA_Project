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
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import com.aurora.webservice.BankDetails;
import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.form.ClientRegistrationForm;
import com.ecs.stellent.ccla.clientservices.form.PSDFRegistrationForm;
import com.ecs.stellent.ccla.clientservices.form.SegregatedClientRegistrationForm;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.data.*;
import com.ecs.ucm.ccla.data.campaign.ApplicableEnrolmentAttribute;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttributeApplied;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;


/** Generates a pre-filled Community First Client Information Form.
 * 
 *  This will be addressed to the nominated correspondent/address for the client, as
 *  specified by the CampaignEnrolment data.
 *  
 * @author Tom Marchant
 *
 */
public class CommunityFirstClientInformationSpoolFileGenerator extends SpoolFileGenerator {

	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	/** Minimum number of Director/Controller sections that will be generated. */
	private static int MIN_LISTED_AUTH_PERSONS = 0;
	/** Max number of Director/Controller entries. */
	private static int MAX_LISTED_AUTH_PERSONS = 10;
	
	/** Minimum number of Signatory sections that will be generated. */
	private static int MIN_LISTED_SIGNATORIES = 0;
	/** Max number of Signatory entries. */
	private static int MAX_LISTED_SIGNATORIES = 10;
	
	/** No. of pages required to output a single authorising person */
	private static final int PAGES_PER_AUTH_PERSON = 2;
	
	/** No. of pages required to output a single signatory */
	private static final int PAGES_PER_SIGNATORY = 2;
	
	/** No. of pages required to output a single main contact */
	private static final int PAGES_PER_MAIN_CONTACT = 2;
	
	/** Default no. of signature boxes to add on the final authorization page.
	 * 
	 *  If a number of signatures is specified in the enrolment attributes, this will
	 *  be used instead of the default value.
	 */
	private static final int DEFAULT_SIGNATURES = 2;
	
	// CreateForm report IDs.
	
	/** Prefix string for each CreateForm page template */
	private String PAGE_REPORT_ID_PREFIX = "COIFCIF";
	
	private static class ReportSections {
		static final String ORG_DETAILS 				= "S1";
		static final String BANKING_DETAILS 			= "S2";
		static final String DIRS_AND_CONTROLLERS 		= "S3";
		static final String MAIN_CONTACT 				= "S4";
		static final String SIGNATORIES					= "S5";
		static final String CHECKLIST					= "CL";
		static final String INFO						= "INFO";
	}
	
	/** Container for all the data objects required to generate the spool file, e.g.
	 *  Organisation, Person, Contact Details.
	 *  
	 */
	private ClientRegistrationForm regForm;
	
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
	
	public CommunityFirstClientInformationSpoolFileGenerator
	 (SpoolHeader spoolHeader, ClientRegistrationForm regForm) {
		
		super(spoolHeader, null, null);
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
		
		addNameValueString
		 ("Campaign", CommunityFirstClientEnrolmentHandler.SPOOL_FILE_CAMPAIGN_NAME);
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

		// Org Details (Section 1)
		// ---------------------------------
		// Displays correspondent name, postal address, some Org Identifiers and
		// attributes
		addReportHeader(getReportId(ReportSections.ORG_DETAILS, 1));
		
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
		
		//addNameValueString("LetterDate", EIGHT_CHAR_DATE_FORMAT.format(new Date()));
		addNameValueString("LetterDate", null);
		
		addNameValueString("AccountsToBeOpenedInFunds", null);
		
		addNameValueString("OrgID", regForm.getEntity().getOrgAccountCode());
		
		addNameValueString("OrgName", regForm.getEntity().getOrganisationName());
	
		String onsNumber = regForm.getOrgIdentifiers().get
		 (ElementIdentifier.ONS_NUMBER);
		String hmrcNumber = regForm.getOrgIdentifiers().get
		 (ElementIdentifier.HMRC_NUMBER);
		String charityRegNumber = regForm.getOrgIdentifiers().get
		 (ElementIdentifier.CHARITY_REF);
		String companyRegNumber = regForm.getOrgIdentifiers().get
		 (ElementIdentifier.COMPANY_REG_NUMBER);

		Boolean hasCharityRegNumber = 
		 StringUtils.stringIsBlank(charityRegNumber) ? null : true;
		
		addNameValueString("RegWithCharityCommOrOSCR", 
		 SpoolFileUtils.getTickboxValue(hasCharityRegNumber));
		addNameValueString("CharityRegNo", charityRegNumber);
		
		Boolean regWithHMRC = 
		 StringUtils.stringIsBlank(hmrcNumber) ? null : true;
		
		addNameValueString("RegWithHMRC", 
		 SpoolFileUtils.getTickboxValue(regWithHMRC));
		addNameValueString("HMRCNo", hmrcNumber);
		
		// Org Details, Page 2
		// -------------------
		// Lists Org Address, Financial Year End and Company Address
		addReportHeader(getReportId(ReportSections.ORG_DETAILS, 2));
		
		// Add the Organisation Address, if applicable, otherwise blank lines.
		Contact orgAddressContact = 
		 Contact.getDefaultContact(orgContacts, Contact.ADDRESS);
		
		Address orgAddress = null;

		if (orgAddressContact != null) {
			orgAddress = orgAddressContact.getAddress();
		}

		// Spread the normalized org address over a 26x4 grid (minus the postcode)
		Vector<SpoolFileLine> orgAddressLines = SpoolFileGenerator.getGridFormatAddress
		 (orgAddress, "OrgAddressLine", false, false, 26, 4);
		
		addLines(orgAddressLines, NAME_VALUE_PADDING);
			
		addNameValueString("OrgAddressPostcode", 
		 orgAddress != null 
		 ? CCLAUtils.stripSpaceCharacters(orgAddress.getPostCode()) : null);
		
		// Org Telephone and Website
		Contact orgTelephone = Contact.getDefaultContact(orgContacts, Contact.PHONE);
		Contact orgWebsite = Contact.getDefaultContact(orgContacts, Contact.WEB);
		
		String orgTelephoneStr = null;
		String orgWebsiteStr = null;
		
		if (orgTelephone != null)
			orgTelephoneStr = CCLAUtils.stripSpaceCharacters(orgTelephone.getValue());
		
		if (orgWebsite != null)
			orgWebsiteStr = orgWebsite.getValue();
		
		addNameValueString("OrgTelephone", orgTelephoneStr);
		addNameValueString("OrgURL", CCLAUtils.truncateWebUrl(orgWebsiteStr));
		
		addNameValueString("FinancialYearEndDate", getOrgAttributeAppliedValue
		 (ElementAttribute.OrganisationAttributes.FINANCIAL_YEAR_END));
		
		// Is Org registered as Limited Company?
		ElementAttributeApplied orgRegAsLtdCompanyAttr = regForm.getOrgAttributes().get
		 (ElementAttribute.OrganisationAttributes.REG_AS_LIMITED_COMPANY);
		
		if (orgRegAsLtdCompanyAttr != null)
			addNameValueString("IsOrgRegAsLimitedCompany", getOrgAttributeAppliedValue
			 (ElementAttribute.OrganisationAttributes.REG_AS_LIMITED_COMPANY));
		else
			addNameValueString("IsOrgRegAsLimitedCompany", null);
		
		// Last few fields on this page should be empty, if the previous attribute
		// has a false value. We'll just print out the values for now anyway.
		
		addNameValueString("CompanyRegNumber", companyRegNumber);
		
		addNameValueString("CompanyName", getOrgAttributeAppliedValue
		 (ElementAttribute.OrganisationAttributes.LIMITED_COMPANY_NAME));
		
		// Add the Organisation Company Address, if applicable, otherwise blank lines.
		Contact orgCompanyAddressContact = 
		 Contact.getFirstContactBySubMethod
		 (orgContacts, Contact.SUBMETHOD_ADDRESS_LIMITED_COMPANY);
		
		Address orgCompanyAddress = null;

		if (orgCompanyAddressContact != null) {
			orgCompanyAddress = orgCompanyAddressContact.getAddress();
		}
		
		// Spread the normalized org address over a 26x4 grid (minus the postcode)
		Vector<SpoolFileLine> companyAddressLines = 
		 SpoolFileGenerator.getGridFormatAddress
		 (orgCompanyAddress, "CompanyAddressLine", false, false, 26, 4);
		
		addLines(companyAddressLines, NAME_VALUE_PADDING);
			
		addNameValueString("CompanyAddressPostcode", 
		 orgCompanyAddress != null 
		 ? CCLAUtils.stripSpaceCharacters(orgCompanyAddress.getPostCode()) : null);
		
		/*
		addNameValueString("AccsSubjectToStatAudit", getOrgAttributeAppliedValue
		 (ElementAttribute.OrganisationAttributes.ACCOUNTS_SUBJECT_TO_AUDIT));
		
		String latestFinancialAccountsURL = getOrgAttributeAppliedValue
		 (ElementAttribute.OrganisationAttributes.LATEST_FINANCIAL_ACCOUNTS_URL);
		
		addNameValueString("LatestFinAccsAvailOnWeb", 
		 latestFinancialAccountsURL == null ? null : "Y");
		addNameValueString("LatestFinAccsURL", latestFinancialAccountsURL);
		
		addNameValueString("OrgType", 
		 regForm.getOrgCategoryTree() != null 
		 ? regForm.getOrgCategoryTree().get(0) : null);
		
		addNameValueString("BalSheetTotAtLeastE20m", getOrgAttributeAppliedValue
		 (ElementAttribute.OrganisationAttributes.BALANCE_SHEET_AT_LEAST_20M_EUROS));
		addNameValueString("NetTurnoverAtLeastE40m", getOrgAttributeAppliedValue
		 (ElementAttribute.OrganisationAttributes.NET_TURNOVER_AT_LEAST_40M_EUROS));
		addNameValueString("OwnFundsAtLeastE2m", getOrgAttributeAppliedValue
		 (ElementAttribute.OrganisationAttributes.FUND_OWNERSHIP_AT_LEAST_2M_EUROS));
		*/
		
		// Payment Details section (Section 2)
		// -----------------------
		// Contains bank account details and income distribution preferences
		addReportHeader(getReportId(ReportSections.BANKING_DETAILS, null));
		
		// TODO
		boolean allSectionsPrinted = true;
		
		addNameValueString("AllSectionsPrinted",
		 SpoolFileUtils.getTickboxValue(allSectionsPrinted));
		
		// Nominated bank account details
		BankAccount bankAccount = regForm.getBankAccount();
		
		SpoolFileUtils.addBankAccountDetails(this, bankAccount);
		
		addNameValueString("BuildingSocietyReference", 
		 bankAccount != null ? bankAccount.getBuildingSocietyNumber() : null);
		
		// Directors' and Controllers' Authorisation section (Section 3)
		// -------
		// Contains details for Directors/Controllers, i.e. auth persons for the org.
		
		// First page of section is a cover sheet and min. no. of dirs/controllers
		addReportHeader(getReportId(ReportSections.DIRS_AND_CONTROLLERS, 1));
		
		// TODO
		allSectionsPrinted = true;
		
		addNameValueString("AllSectionsPrinted",
		 SpoolFileUtils.getTickboxValue(allSectionsPrinted));
		
		// Leave both ticks blank for now.
		addNameValueString("ReqOneAuthSig",
		 SpoolFileUtils.getTickboxValue(false));
		
		addNameValueString("ReqTwoAuthSig",
		 SpoolFileUtils.getTickboxValue(false));
		
		// Add data for each auth person
		int numPrintedDirectorOrControllers = 
			Math.max(authPersons.size(), MIN_LISTED_AUTH_PERSONS);
		
		for (int i=1; i<=numPrintedDirectorOrControllers; i++) {
			Person thisAuthPerson = null;
			
			if (authPersons.size() >= i)
				thisAuthPerson = authPersons.get(i-1);
			
			addDirectorOrController(thisAuthPerson, i, numPrintedDirectorOrControllers);
		}
		
		// Main Contact details section (Section 4)
		// ----------------------------
		// Displays org correspondent details, if they weren't listed in the
		// previous section. If the org. corr. was previously listed, this section is
		// skipped.
		
		// Get Main Contact (i.e. Org Correspondent)
		if (mainContact != null && authPersons.contains(mainContact)) {
			// Skip this section if the assigned org correspondent was printed
			// in the Directors and Controllers section.
			
		} else {
			
			addReportHeader(getReportId(ReportSections.MAIN_CONTACT, 1));	
			
			printedPersons.add(mainContact);
			
			String linePrefix = "Cor";
			
			// Add name/position/DOB fields for main contact
			SpoolFileUtils.addPersonDetails(this, mainContact, linePrefix);
			
			// Add Tel/Email fields
			SpoolFileUtils.addPersonContactDetails(this, mainContact, linePrefix);

			// Add current corr address
			Contact corrAddressContact = 
			 mainContact == null ? null : Contact.getFirstContactBySubMethod
			 (mainContact.getContacts(), Contact.SUBMETHOD_ADDRESS_HOME);
			
			Address corrAddress = null;

			if (corrAddressContact != null) {
				corrAddress = corrAddressContact.getAddress();
			}
			
			// Spread the normalized corr address over a 26x3 grid (minus the postcode)
			Vector<SpoolFileLine> corrAddressLines = 
			 SpoolFileGenerator.getGridFormatAddress
			 (corrAddress, linePrefix + "AddressLine", false, false, 26, 3);
			
			addLines(corrAddressLines, NAME_VALUE_PADDING);
			
			addNameValueString(linePrefix + "AddressPostcode", 
			 corrAddress != null ?
			 CCLAUtils.stripSpaceCharacters(corrAddress.getPostCode()) : null);
			
			addNameValueString(linePrefix + "DateMovedToAddress", null);
			
			// Add previous corr address
			Contact corrPrevAddressContact = 
			 mainContact == null ? null : Contact.getFirstContactBySubMethod
			 (mainContact.getContacts(), Contact.SUBMETHOD_ADDRESS_PREVIOUS);
			
			Address corrPrevAddress = null;

			if (corrPrevAddressContact != null) {
				corrPrevAddress = corrPrevAddressContact.getAddress();
			}
			
			// Spread the normalized corr address over a 21x3 grid (minus the postcode)
			Vector<SpoolFileLine> corrPrevAddressLines = 
			 SpoolFileGenerator.getGridFormatAddress
			 (corrPrevAddress, linePrefix + "PrevAddressLine", false, false, 21, 3);
			
			addLines(corrPrevAddressLines, NAME_VALUE_PADDING);
			
			addNameValueString(linePrefix + "PrevAddressPostcode", 
			 corrPrevAddress != null ?
			 CCLAUtils.stripSpaceCharacters(corrPrevAddress.getPostCode()) : null);
			
			// TODO
			addNameValueString(linePrefix + "PrevAddrDateMovedTo", null);

			// Contains overflow stuff for main corr.
			// ----------------
			addReportHeader(getReportId(ReportSections.MAIN_CONTACT, 2));
			
			// TODO
			addNameValueString(linePrefix + "PassportNumber", null);
			addNameValueString(linePrefix + "PassportFullName", null);

			// TODO
			addNameValueString(linePrefix + "DrivingLicenceNumber", null);
			addNameValueString(linePrefix + "DrivingLicenceFullName", null);
			
			/*
			// Correspondence preference fields.
			Contact defaultCorrAddressContact = null;
			
			if (mainContact != null) {
				defaultCorrAddressContact = Contact.getDefaultContact
				 (mainContact.getContacts(), Contact.ADDRESS);
			} 
			
			SpoolFileUtils.addCorrespondencePref
			 (this, defaultCorrAddressContact, linePrefix);
			*/
			
			// Whether or not the correspondent is the main contact (well, yes, exactly
			// what they are!)
			// TODO confirm meaning.
			addNameValueString(linePrefix + "IsMainContact", 
			 SpoolFileUtils.getTickboxValue(isAuthSig(mainContact)));
			
			boolean dirDoNotContact = false;
			
			if (mainContact != null && mainContact.isDoNotContact())
				dirDoNotContact = true;
				
			addNameValueString(linePrefix + "DoNotContact", 
			 dirDoNotContact ? "Y" : null);
		}
		
		// Add remaining org signatories (i.e. Persons marked as Signatory to 
		// account but not Auth. Person/Correspondent, so they won't have been 
		// displayed in the Directors/Main Contact section)
		int numPrintedSignatories = Math.max(sigs.size(), MIN_LISTED_SIGNATORIES);
		
		for (int i=1; i<=numPrintedSignatories; i++) {
			
			Person sig = null;
			
			if (sigs.size() >= i)
				sig = sigs.get(i-1);
			
			addReportHeader(getReportId(ReportSections.SIGNATORIES, 1));
			addNameValueString("NumberOfSignatories", 
			 Integer.toString(numPrintedSignatories));
			addNameValueString("SignatoryNumber", Integer.toString(i));
			
			String linePrefix = "Sig";
			
			// Add name/position/DOB fields for signatory
			SpoolFileUtils.addPersonDetails(this, sig, linePrefix);
			
			// Add Tel/Email fields
			SpoolFileUtils.addPersonContactDetails(this, sig, linePrefix);
			
			// Add current sig address
			Contact sigAddressContact = (sig == null) 
			 ? null : Contact.getFirstContactBySubMethod
			 (sig.getContacts(), Contact.SUBMETHOD_ADDRESS_HOME);
			
			Address sigAddress = null;

			if (sigAddressContact != null) {
				sigAddress = sigAddressContact.getAddress();
			}
			
			// Spread the normalized corr address over a 21x3 grid (minus the postcode)
			Vector<SpoolFileLine> sigAddressLines = 
			 SpoolFileGenerator.getGridFormatAddress
			 (sigAddress, linePrefix + "AddressLine", false, false, 21, 3);
			
			addLines(sigAddressLines, NAME_VALUE_PADDING);
			
			addNameValueString(linePrefix + "AddressPostcode", 
			 sigAddress != null ?
			 CCLAUtils.stripSpaceCharacters(sigAddress.getPostCode()) : null);
			
			addNameValueString(linePrefix + "DateMovedToAddress", null); 
			
			// Add previous corr address
			Contact sigPrevAddressContact = (sig == null) 
			 ? null : Contact.getFirstContactBySubMethod
			 (sig.getContacts(), Contact.SUBMETHOD_ADDRESS_PREVIOUS);
			
			Address sigPrevAddress = null;

			if (sigPrevAddressContact != null) {
				sigPrevAddress = sigPrevAddressContact.getAddress();
			}
			
			// Spread the normalized corr address over a 21x3 grid (minus the postcode)
			Vector<SpoolFileLine> sigPrevAddressLines = 
			 SpoolFileGenerator.getGridFormatAddress
			 (sigPrevAddress, linePrefix + "PrevAddressLine", false, false, 21, 3);
			
			addLines(sigPrevAddressLines, NAME_VALUE_PADDING);
			
			addNameValueString(linePrefix + "PrevAddressPostcode", 
			 sigPrevAddress != null ?
			 CCLAUtils.stripSpaceCharacters(sigPrevAddress.getPostCode()) : null);
			
			// TODO
			addNameValueString(linePrefix + "PrevAddrDateMovedTo", null);
			
			// Second signatory page
			// -----------
			addReportHeader(getReportId(ReportSections.SIGNATORIES, 2));
			
			addNameValueString("NumberOfSignatories", Integer.toString(sigs.size()));
			addNameValueString("SignatoryNumber", Integer.toString(i));
			
			// TODO
			addNameValueString(linePrefix + "PassportNumber", null);
			addNameValueString(linePrefix + "PassportFullName", null);
			
			// TODO
			addNameValueString(linePrefix + "DrivingLicenceNumber", null);
			addNameValueString(linePrefix + "DrivingLicenceFullName", null);
			
			boolean sigDoNotContact = false;
			
			if (sig != null && sig.isDoNotContact())
				sigDoNotContact = true;
				
			addNameValueString(linePrefix + "DoNotContact", 
			 sigDoNotContact ? "Y" : null);
		}
		
		// Add Checklist page
		addReportHeader(getReportId(ReportSections.CHECKLIST, null));
		
		// Add Info page
		addReportHeader(getReportId(ReportSections.INFO, null));
		
		/*
		// Add Signature page at the end.
		addReportHeader(getReportId(ReportSections.SIGNATURES, null));
		
		int numSigs = DEFAULT_SIGNATURES;
		
		EnrolmentAttributeApplied numSigsAttrib = regForm.getEnrolmentAttributes().get
		 (ApplicableEnrolmentAttribute.Ids.SEG_CLIENT_NUM_SIGS);
		
		if (numSigsAttrib != null)
			numSigs = Integer.parseInt(numSigsAttrib.getValue());
		
		// TODO
		addNameValueString("AllSectionsPrinted", SpoolFileUtils.getTickboxValue(true));
		
		addNameValueString("NumberOfSignatories", Integer.toString(numSigs));
		
		for (int i=1; i<= numSigs; i++) {
			String linePrefix = "Sig" + i;
			addNameValueString(linePrefix + "Title", null);
			addNameValueString(linePrefix + "Forename", null);
			addNameValueString(linePrefix + "Surname", null);
			addNameValueString(linePrefix + "Date", null);
		}
		*/
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
		 regForm.getOrgAttributes().get(attributeId);
		
		if (elemAttrAppl != null) {
			if (attr.getDataType().equals(DataType.BOOL))
				return SpoolFileUtils.getTickboxValue(elemAttrAppl.getStatus());
			else
				return elemAttrAppl.getAttrValue();
		} else
			return null;
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
		// -2 pages for Org Details
		// -1 page for Payment Details
		// -1 page for Dirs/Controllers section first page
		// -1 page for Doc Checklist
		// -1 page for Info Page
		int testPageCount = 6;
		
		SystemConfigVar minAuthPersonsVar = SystemConfigVar.getCache().getCachedInstance
		 ("CommunityFirst_MinAuthPersons");
		
		SystemConfigVar minSigsVar = SystemConfigVar.getCache().getCachedInstance
		 ("CommunityFirst_MinSignatories");
		
		// Overwrite min. auth person/signatory vars
		EnrolmentAttributeApplied minAuthPersonsAttr = 
		 regForm.getEnrolmentAttributes().get
		 (ApplicableEnrolmentAttribute.Ids.COMM_FIRST_MIN_AUTH_PERSONS);
		
		if (minAuthPersonsAttr != null)
			MIN_LISTED_AUTH_PERSONS = Integer.parseInt(minAuthPersonsAttr.getValue());
		else if (minAuthPersonsVar != null && minAuthPersonsVar.getIntValue() != null)
			MIN_LISTED_AUTH_PERSONS = minAuthPersonsVar.getIntValue();
		
		EnrolmentAttributeApplied minSigsAttr = 
		 regForm.getEnrolmentAttributes().get
		 (ApplicableEnrolmentAttribute.Ids.COMM_FIRST_MIN_SIGNATORIES);
		
		if (minSigsAttr != null)
			MIN_LISTED_SIGNATORIES = Integer.parseInt(minSigsAttr.getValue());
		else if (minSigsVar != null && minSigsVar.getIntValue() != null)
			MIN_LISTED_SIGNATORIES = minSigsVar.getIntValue();
		
		authPersons 	= new Vector<Person>();
		sigs 			= new Vector<Person>();
		
		// Fetch all authorising persons for account. These will be printed in the
		// auth persons section and require 1 page each
		Vector<Person> testAuthPersons = regForm.getRelatedOrgPersons().get
		 (RelationName.getCache().getCachedInstance(
			RelationName.OrganisationPersonRelation.AUTH_PERSON)
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
		
		testPageCount += (numAuthPersons*PAGES_PER_AUTH_PERSON);
		
		/*
		Log.debug("Related Org persons:");
		
		for (Entry<RelationName, Vector<Person>> entry 
			 : regForm.getRelatedOrgPersons().entrySet()) {
			
			RelationName relName = entry.getKey();
			
			Log.debug("Listing all Persons with Relation: " + relName.getShortName());
			
			for (Person p : entry.getValue()) {
				Log.debug("Person ID " + p.getPersonId() + " Name: " + p.getFullName());
			}
		}
		*/
		
		Log.debug("Added " + authPersons.size() + 
		 " auth. persons, pageCount = " + testPageCount);
		
		// Fetch org correspondent. 
		mainContact = getCorrespondent();
		
		if (mainContact != null) {
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
				
				testPageCount += PAGES_PER_MAIN_CONTACT;
			}
			
		} else
			testPageCount += PAGES_PER_MAIN_CONTACT; // require empty Main Contact page
		
		Log.debug("Added correspondent, pageCount = " + testPageCount);
		
		// Fetch all signatories for org. These will be printed in the
		// auth persons section and require 1 page each
		Vector<Person> testSigs = getSigs();
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
		
		testPageCount += (numSigs * PAGES_PER_SIGNATORY);
		
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
	private void addDirectorOrController(Person person, int directorNumber, 
	 int totalDirectorOrControllers) throws DataException, IOException {
		
		addReportHeader(getReportId(ReportSections.DIRS_AND_CONTROLLERS, 2));
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
		 (personPrevHomeAddress, linePrefix + "PrevAddressLine", false, false, 21, 3);
		
		addLines(personPrevAddressLines, NAME_VALUE_PADDING);
	
		addNameValueString(linePrefix + "PrevAddressPostcode", 
		 personPrevHomeAddress != null 
		 ? CCLAUtils.stripSpaceCharacters(personPrevHomeAddress.getPostCode()) : null);

		addNameValueString(linePrefix + "PrevAddrDateMovedTo", null);
		
		// Second director page. Contains preference tickboxes
		// ---------------
		addReportHeader(getReportId(ReportSections.DIRS_AND_CONTROLLERS, 3));

		addNameValueString("NumberOfDirectors", Integer.toString(authPersons.size()));
		addNameValueString("DirectorNumber", Integer.toString(directorNumber));
		
		addNameValueString(linePrefix + "PassportNumber", null);
		addNameValueString(linePrefix + "PassportFullName", null);
		
		addNameValueString(linePrefix + "DrivingLicenceNumber", null);
		addNameValueString(linePrefix + "DrivingLicenceFullName", null);

		// Whether or not this auth. person is also marked as the correspondent.
		addNameValueString(linePrefix + "IsMainContact", 
		 person != null ?
		 SpoolFileUtils.getTickboxValue(person.equals(mainContact)) : null);
				
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

		Vector<Person> corrs = regForm.getRelatedOrgPersons().get(
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
		return regForm.getRelatedOrgPersons().get(
			RelationName.getCache().getCachedInstance(
			 RelationName.OrganisationPersonRelation.SIGNATORY)
		);
	}
	
	/** Returns the first correspondent found, or null if nobody is mapped to
	 *  the role.
	 *  
	 * @return
	 * @throws DataException
	 */
	private Person getCorrespondent() throws DataException {
		Vector<Person> corrs =  regForm.getRelatedOrgPersons().get(
			RelationName.getCache().getCachedInstance(
			 RelationName.OrganisationPersonRelation.CORRESPONDENT)
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