package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import com.aurora.webservice.BankDetails;
import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.form.AccountRegistrationForm;
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
public class CommunityFirstOnBoardingCoveringLetterSpoolFileGenerator extends SpoolFileGenerator {

	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	/** Max number of signatory entries. */
	private static int MAX_LISTED_SIGNATORIES = 20;
	
	/** CreateForm page template ID */
	private String PAGE_REPORT_ID = "CFEMCOBLTR";
	
	/** Container for all the data objects required to generate the spool file, e.g.
	 *  Organisation, Person, Contact Details.
	 *  
	 */
	private AccountRegistrationForm regForm;
	
	/** Page counters, used for printing 'Page x of y' captions on each report page.
	 * 
	 */
	int currentPage 	= 1;
	int totalPages 		= 1;
	
	/** Counter for unmapped entries in the spool file.
	 * 
	 */
	int missingLineCount = 1;
	
	public CommunityFirstOnBoardingCoveringLetterSpoolFileGenerator
	 (SpoolHeader spoolHeader, AccountRegistrationForm regForm) {
		
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
		//addNameValueString("FormID", 
		// CCLAUtils.padString(
		// Integer.toString(this.regForm.getFormId()), '0', 9));
		
		//addNameValueString
		// ("PageNumber", "Page " + (currentPage++) + " of " + totalPages);
		
		//addNameValueString
		// ("Campaign", CommunityFirstClientEnrolmentHandler.SPOOL_FILE_CAMPAIGN_NAME);
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
		return PAGE_REPORT_ID + sectionRef + 
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
		
		// Page 1 - the only page.
		// ---------------------------------
		// Displays correspondent name, postal address, Org Account Code, Org Name,
		// list of Auth Persons
		addReportHeader(getReportId("", null));

		addNameValueString("LetterDate", COVER_LETTER_DATE_FORMAT.format(new Date()));
		
		// Add postal address with correspondent name at the top
		addNameValueString("Correspondent", 
		 regForm.getCorrespondent().getCompleteName());
		
		// Output postal address
		Address postalAddress = regForm.getContactAddress().getAddress();
		
		Vector<String> postalAddressLines = 
		 postalAddress.getPrintableAddress(true, false);
		
		// Print the Org Name on the first address line.
		addNameValueString("AddressLine1", regForm.getEntity().getOrganisationName());
		
		// Print the postal address over the 7 remaining lines.
		for (int j=2; j<=8; j++) {
			String addressPiece = null;
			
			if (postalAddressLines.size() > (j-2))
				addressPiece = postalAddressLines.get(j-2);
			
			addNameValueString(
			 "AddressLine" + (j), addressPiece);
		}
		
		addNameValueString("Salutation", regForm.getCorrespondent().getSalutation());
		
		addNameValueString("OrgID", regForm.getEntity().getOrgAccountCode());
		addNameValueString("OrgName", regForm.getEntity().getOrganisationName());
		
		// Nominated bank account details
		BankAccount bankAccount = regForm.getBankAccount();
		
		SpoolFileUtils.addBankAccountDetails(this, bankAccount);
		
		addNameValueString("BuildingSocietyReference", 
		 bankAccount != null ? bankAccount.getBuildingSocietyNumber() : null);
		
		addNameValueString("NumberOfAuthSigsRequired", 
		 Integer.toString(regForm.getAccount().getRequiredSignatures()));
				
		// List of Account signatories.
		RelationName sigRel = RelationName.getCache().getCachedInstance(
			RelationName.PersonAccountRelation.SIGNATORY
		);
		
		Vector<Person> authPersons = regForm.getRelatedAccountPersons().get(sigRel);
		String authPersonPrefix = "AuthSigAndSignInstruct";
		
		for (int i=1; i<=MAX_LISTED_SIGNATORIES; i++) {
			Person thisPerson = null;
			
			if (authPersons != null && (authPersons.size() >= i))
				thisPerson = authPersons.get(i-1);
			
			addNameValueString(authPersonPrefix + i, 
			 thisPerson != null ? thisPerson.getFullName() : null);
		}
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