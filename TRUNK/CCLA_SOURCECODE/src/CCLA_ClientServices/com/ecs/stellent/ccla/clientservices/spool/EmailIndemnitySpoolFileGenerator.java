package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.EmailIndemnityForm;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;

public class EmailIndemnitySpoolFileGenerator extends SpoolFileGenerator {

	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	private EmailIndemnityForm emailIndemnityForm;
	
	/** Prefix string for each CreateForm page template */
	private String PAGE_REPORT_ID_PREFIX = "EMAIL_INDEM";
	
	private static class ReportSections {
		static final String COVER_PAGE 				= "1";
		static final String SIGNATURES 				= "2";
		static final String EMAIL_ADDRESSES 		= "3";
	}
	
	/** Page counters, used for printing 'Page x of y' captions on each report page.
	 * 
	 * 
	 */
	int currentPage 	= 1;
	int totalPages 		= 3;
	
	public EmailIndemnitySpoolFileGenerator(SpoolHeader header) {
		super(header);
	}
	
	public EmailIndemnitySpoolFileGenerator
	 (SpoolHeader header, EmailIndemnityForm form) {
		super(header);
		
		this.emailIndemnityForm = form;
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
	
	/** Adds a name-value line to the BufferedWriter. */
	public void addNameValueString(String name, String value) 
	 throws DataException, IOException {
		addNameValueString(name, value, NAME_VALUE_PADDING);
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
		 Integer.toString(this.emailIndemnityForm.getFormId()), '0', 9));
		
		addNameValueString
		 ("PageNumber", "Page " + (currentPage++) + " of " + totalPages);
		
		if (emailIndemnityForm.getCampaign() != null)
			addNameValueString("Campaign", emailIndemnityForm.getCampaign());
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

		if (emailIndemnityForm == null)
			throw new SpoolFileException(
			 "RegForm instance was empty or not initialized");
	
		ByteArrayOutputStream outputStream 	= new ByteArrayOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		this.header.setRecordCount(1);
		
		this.writeNewHeader();
		
		// Add required report elements for the form instance
		appendFormData();

		// EOF
		writer.write("#");
		
		writer.flush();
		writer.close();
		
		return outputStream;
	}

	private void appendFormData() throws IOException, DataException {
		
		// Cover Sheet
		// -----------
		// Displays Client Name and Aurora Client Number
		addReportHeader(getReportId(ReportSections.COVER_PAGE, null));
		
//		<OrgID>                    ABC12345678901
//		<OrgName>                  aBcDeFgHiJkLmNoPq
//		<NumberOfClients>          2
//		<ClientNumber1>            123456
//		<ClientNumber2>            876543
		
		addNameValueString("OrgAccountCode", 
		 emailIndemnityForm.getOrganisation().getOrgAccountCode());
		
		addNameValueString("OrgName", 
		 emailIndemnityForm.getOrganisation().getOrganisationName());
		
		// The form is capable of printing up to 2 Client Numbers but we won't enforce
		// this. Each form maps to a single Organisation and Aurora Client Number
		addNameValueString("NumberOfClients", "1");
		
		addNameValueString("ClientNumber1", 
		 emailIndemnityForm.getAuroraClient().getPaddedClientNumber());
		
		// Authorising signatures
		// ----------------------
		// Displays up to 3 blank authorising signature sections
		addReportHeader(getReportId(ReportSections.SIGNATURES, null));
		
//		<NumberOfSignatories>      3
//		<Sig1Title>                ABCDEF
//		<Sig1Forename>             ABCDEFGHIJKLM
//		<Sig1Surname>              ABCDEFGHIJK
//		<Sig1Date>                 12345678
//		<Sig2Title>                abcdef
//		<Sig2Forename>             abcdefghijklm
//		<Sig2Surname>              abcdefghijk
		
		addNameValueString("NumberOfSignatories", 
		 Integer.toString(emailIndemnityForm.getNumSignatories()));
		
		for (int i=0; i<emailIndemnityForm.getNumSignatories(); i++) {
			SpoolFileUtils.addPersonDetails(this, null, "Sig" + (i+1), false, false);
			addNameValueString("Sig" + (i+1) + "Date", null);
		}
		
		// Email Addresses
		// ---------------
		// Displays up to three email address boxes. If the passed list of email
		// addresses is null/empty, 3 empty boxes are printed. Otherwise up to three
		// addresses are populated using the contact list data.
		addReportHeader(getReportId(ReportSections.EMAIL_ADDRESSES, null));

//		<NumberOfEmails>           3
//		<NominatedEmail1>          01aBcDeFgHiJkLmNoPqRsTuVwXyZaBcDeFgHiJkLmN
//		<NominatedEmail2>          02aBcDeFgHiJkLmNoPqRsTuVwXyZaBcDeFgHiJkLmN
//		<NominatedEmail3>          03aBcDeFgHiJkLmNoPqRsTuVwXyZaBcDeFgHiJkLmN
		
		int numberOfEmails = 3;
		
		Vector<Contact> emailAddresses = emailIndemnityForm.getEmailAddresses();
		
		if (emailAddresses != null && emailAddresses.size() > 0) {
			// Email addresses were supplied.
			numberOfEmails = Math.min(numberOfEmails, emailAddresses.size());
		}
		
		addNameValueString("NumberOfEmails", Integer.toString(numberOfEmails));
		
		for (int i=0; i<numberOfEmails; i++) {
			String emailAddress = null;
			
			if (emailAddresses != null && emailAddresses.size() > i)
				emailAddress = emailAddresses.get(i).getValue();
			
			addNameValueString("NominatedEmail" + (i+1), emailAddress);
		}
	}

}
