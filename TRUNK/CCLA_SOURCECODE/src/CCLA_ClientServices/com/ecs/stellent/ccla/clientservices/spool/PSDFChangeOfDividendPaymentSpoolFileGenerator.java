package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.IOException;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.PSDFRegistrationForm;
import com.ecs.stellent.ccla.clientservices.spool.PSDFAdditionalAccountSpoolFileGenerator.ReportSections;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;

/** Generates a pre-filled PSDF Change of Dividend Payment Form spool file.
 * 
 *  This will be addressed to the nominated correspondent/address for the client, as
 *  specified by the CampaignEnrolment data.
 *  
 *  It is expected that this form will only be sent to previously-enrolled PSDF clients,
 *  so it doesn't require as much data as the Registration Form.
 *  
 *  Each form is tied to a single pre-existing PC account belonging to the client, and
 *  optionally a specific Bank Account which the client wishes to authorise for use.
 */
public class PSDFChangeOfDividendPaymentSpoolFileGenerator extends
		PSDFAdditionalAccountSpoolFileGenerator {

	/** Prefix string for each CreateForm page template */
	protected static String PAGE_REPORT_ID_PREFIX = "PSICCDPM";
	
	public PSDFChangeOfDividendPaymentSpoolFileGenerator
	 (SpoolHeader spoolHeader,
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
		
		// Payment Details section
		// -----------------------
		// Contains dividend preferences and dividend bank account details.
		// This sections is intentionally LEFT BLANK on this form, so the client can
		// specifiy new details as they see fit.
		addReportHeader(getReportId(ReportSections.PAYMENT_DETAILS, null));
		
		addNameValueString("ReinvestDividend", null);
		addNameValueString("PayToNominatedAccount", null);
		
		addNameValueString("PayToOtherAccount", null);
		
		addBankAccountDetails(null);
		
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
