package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.IOException;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.PSDFRegistrationForm;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;

/** Generates a pre-filled PSDF Additional Bank Account Form spool file. Used to allow
 *  the client to add a new withdrawal bank account, potentionally replacing the
 *  current nominated withdrawal account.
 * 
 *  This will be addressed to the nominated correspondent/address for the client, as
 *  specified by the CampaignEnrolment data.
 *  
 *  It is expected that this form will only be sent to previously-enrolled PSDF clients,
 *  so it doesn't require as much data as the Registration Form.
 *  
 *  Each form is tied to a single pre-existing PC account belonging to the client, and
 *  optionally a specific Bank Account which the client wishes to authorise for use.
 *  
 * @author Tom Marchant
 *
 */
public class PSDFAdditionalBankAccountSpoolFileGenerator extends
		PSDFAdditionalAccountSpoolFileGenerator {

	/** Prefix string for each CreateForm page template */
	protected static String PAGE_REPORT_ID_PREFIX = "PSICAWTH";
	
	protected static class ReportSections {
		static final String ACCOUNT_DETAILS 			= "S1";
		static final String NEW_BANK_ACCOUNT_DETAILS 	= "S2";
		static final String AUTHORISING_SIGS 			= "S3";
	}
	
	public PSDFAdditionalBankAccountSpoolFileGenerator(SpoolHeader spoolHeader,
			String docName, Integer enrolmentId, PSDFRegistrationForm regForm) {
		super(spoolHeader, docName, enrolmentId, regForm);
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
		
		// New Bank Account Details section
		// --------------------------------
		// Contains bank account details and income distribution preferences
		addReportHeader(getReportId(ReportSections.NEW_BANK_ACCOUNT_DETAILS, null));
		
		// If there are repeating bank account sections, this must be incremented
		// for each one. Hard-coded to 1 for now.
		addNameValueString("AdditionalAccountNumber", "1");
		
		// Add empty fields for new bank account details (specified by client)
		addBankAccountDetails(null);
		
		// Add empty preference fields for new bank account
		addNameValueString("MakeNominatedAccount", null);
		addNameValueString("RemoveNominatedAccount", null);
		
		// Current nominated bank account details
		BankAccount bankAccount = regForm.getBankAccount();

		addBankAccountDetails(bankAccount);

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
}
