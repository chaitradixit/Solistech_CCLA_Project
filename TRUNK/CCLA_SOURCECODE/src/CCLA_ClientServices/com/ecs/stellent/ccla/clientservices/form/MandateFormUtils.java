package com.ecs.stellent.ccla.clientservices.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.AMLUtils;
import com.ecs.stellent.ccla.clientservices.spool.MandateSpoolFileGenerator;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.AuroraCorrespondent;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.UCMForm;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.Signatory;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

public class MandateFormUtils {
	
	/** 
	 *  @deprecated uses old campaign schema.
	 *  
	 *  Generates pre-filled Mandate spool files and places them in a temporary
	 *  directory. The spool file paths are then passed to the Aurora Forms
	 *  printer.
	 *  
	 *  New entries are added to the CCLA_FORM table, including the base dDocName.
	 *  This will be used to identify the generated PDF once it is consumed from
	 *  the Aurora Form Printer output folder and checked into UCM (the check-in
	 *  part doesn't happen yet!)
	 *  
	 * @param processId
	 * @param facade
	 * @param userName
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static void createAndPrintMandateForms
	 (ClientProcess process, FWFacade facade, String userName) 
	 throws ServiceException, DataException {
		
		if (process == null)
			throw new ServiceException("Unable to generate Mandate form(s): " +
			 "passed process was null");
		
		int processId			= process.getProcessId();
		Integer entityId		= process.getOrganisationId();
		
		Log.debug("Generating Mandate form(s) for process: " + processId + 
		 ", entity: " + entityId);
		
		// Fetch entity, with associated contact data
		Entity entity = Entity.get(process.getOrganisationId(), true, facade);
		
		if (entity == null)
			throw new ServiceException("Unable to generate form: " +
			 "could not find associated entity");
		
		// Fetch AuroraClient mapping for this entity
		// Will leave this as-is, method is old now.
		AuroraClient auroraClient = 
		 Entity.getAuroraClient(entityId, facade);

		// Fetch all accounts for placement on AML form(s). These will be active
		// accounts opened before Dec 15th, 2007.
		DataResultSet rsAmlAccounts =
		 AMLUtils.getAMLAccounts(entityId, facade);
		
		Log.debug("Found " + rsAmlAccounts.getNumRows() + 
		 " accounts requiring AML validation.");
		
		if (rsAmlAccounts.isEmpty())
			throw new ServiceException("No eligible accounts for this client.");
		
		Vector<Account> amlAccounts = Account.getAccounts(rsAmlAccounts);
		
		// Create mapping of correspondents -> client accounts requiring AML check.
		// This is used to determine how many Mandate forms to create.
		HashMap<Integer, Vector<Account>> corrAccountMap = 
		 Account.getCorrespondentAccountMap(amlAccounts, facade);
		
		int numCorrespondents = corrAccountMap.keySet().size();
		
		Log.debug("AML accounts are spread between " 
		 + numCorrespondents + " correspondents. Requires creation of " +
		 + numCorrespondents + " Mandate forms.");
		
		Iterator<Integer> i = corrAccountMap.keySet().iterator();
		
		// Ensure no account mapping exceeds 20 accounts (the maximum
		// number of accounts which can be displayed on a single form)
		while (i.hasNext()) {
			Integer personId = i.next();
			Vector<Account> corrAmlAccounts = corrAccountMap.get(personId);
			
			if (corrAmlAccounts.size() > MandateForm.ACCOUNT_LIST_LIMIT) {
				String msg = "Unable to generate form for entity: " + entityId 
				 + ", person: " + personId + ". Too many accounts.";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}
		
		i = corrAccountMap.keySet().iterator();
		
		// Loop through each correspondent, generating a Mandate form for
		// each.
		while (i.hasNext()) {
			Integer personId = i.next();
			Vector<Account> corrAmlAccounts = corrAccountMap.get(personId);
			
			boolean matchingDetails = checkForMatchingBankingDetails(corrAmlAccounts);
			
			if (!matchingDetails) {
				String msg = "Unable to display banking information for elient: " + entityId 
				 + ", person: " + personId + ". Banking details did not match " +
				 "across accounts.";
				
				Log.warn(msg);
			}
			
			ByteArrayOutputStream outputStream = null;
			
			
			
			try {
				// Use the Aurora Correspondent ID for now when assigning
				// dDocNames. Future form types should use the Person ID
				// though.
				Vector<AuroraCorrespondent> correspondents =
				 AuroraCorrespondent.getCorrespondentsByPersonId(personId, facade);
				
				if (correspondents.size() == 0) {
					throw new ServiceException("Unable to generate form: " +
					 "no Aurora Correspondent ID for Person " + personId);
				} else if (correspondents.size() > 1) {
					throw new ServiceException("Unable to generate form: " +
					 "multiple Aurora Correspondent IDs for Person " + personId);
				}
	
				int corrId = correspondents.get(0).getCorrId();
				
				String formDocName = "FORM-" + processId + "-" + corrId;
				
				// Determine form sub-type
				String subType; 
				
				if (corrAmlAccounts.size() == 1)
					subType = MandateSpoolFileGenerator.FORM_SUBTYPE_SINGLE_ACCOUNT;
				else if (matchingDetails)
					subType = MandateSpoolFileGenerator.FORM_SUBTYPE_MULTI_ACCOUNTS;
				else
					subType = MandateSpoolFileGenerator.FORM_SUBTYPE_MULTI_MIXED_ACCOUNTS;
				
				// Create/fetch Form ID for this mandate
				UCMForm form = UCMForm.add(
				 processId, 
				 MandateSpoolFileGenerator.FORM_TYPE,
				 subType,
				 entityId,
				 auroraClient,
				 personId, 
				 formDocName, null, 
				 facade);
				
				outputStream = createMandateSpoolFile
				 (processId, form, entity, auroraClient, personId, 
				  corrAmlAccounts, matchingDetails, userName, facade);
				
				// Generate a new file in the temporary location
				File spoolFile = FormUtils.createTempSpoolFile
				 (formDocName, null, outputStream);
				
				Log.debug("Generated temp spool file: " + spoolFile.getName() + 
				 ". Dispatching for print.");
				
				FormUtils.printForm(spoolFile.getAbsolutePath());
				
				// Update form data to indicate it was printed
				form.setPrintDate(new Date());
				form.setPrinted(true);
				form.setStatus("Printed");
				
				form.persist(facade, null);
				
				// Add activity log to Client Services process, indicating form
				// was generated.
				
				process.addActivity(userName, personId, "Form Generation",
				 "Mandate form " + form.getFormId() + " created and dispatched for print", 
				 null, false, facade);
				
			} catch (Exception e) {
				Log.error("Failed to generate/print Mandate form", e);
				
				// Add activity log to Client Services process, indicating form
				// was generated.
				
				process.addActivity(userName, personId, "Form Generation", 
				 "Failed to generate/print Mandate form", 
				 null, true, facade);
				
				throw new ServiceException(e);
			}
		}
	}
	
	/** Creates and checks in a pre-filled Mandate form. This requires creation
	 *  of the spool file and associated Create!Form PDF - these are checked
	 *  in as the primary/web renditions respectively. Currently the 
	 *  Create!Form PDF rendition is unavailable.
	 *  
	 *  If a form already exists with the derived content ID, it will be up-
	 *  revisioned.
	 *  
	 *  A processId must be present in the binder, this is assumed to relate
	 *  to a Client Services process ID. The associated process will be looked
	 *  up to determine the company/correspondent ID combo required to build
	 *  the form.
	 *  
	 * @throws ServiceException
	 */
	public static void checkinMandateForms
	 (int processId, FWFacade facade, String userName) 
	 throws ServiceException, DataException {
		
		ClientProcess process 	= ClientProcess.get(processId, facade);
		
		if (process == null)
			throw new ServiceException("Unable to generate Mandate form(s): " +
			 "no associated process found for ID: " + processId);
		
		Integer entityId		= process.getOrganisationId();

		Log.debug("Checking in Mandate form(s) for process: " + processId + 
		 ", entity: " + entityId);
		
		// Fetch entity, with associated contact data
		Entity entity = Entity.get(entityId, true, facade);
		
		if (entity == null)
			throw new ServiceException("Unable to generate form: " +
			 "could not find associated entity.");
		
		// Now fetch Aurora Client mapping
		Vector<AuroraClient> auroraClients = 
		 Entity.getAuroraClientMapping(entityId, facade);

		if (auroraClients.size() == 0)
			throw new ServiceException("Unable to generate form: " +
			 "could not find associated Client Number/Company for entity.");
		
		// TODO: Handle multiple aurora client maps?
		AuroraClient auroraClient = auroraClients.get(0);
		
		// Fetch all accounts for placement on AML form(s). These will be active
		// accounts opened before Dec 15th, 2007.
		DataResultSet rsAmlAccounts =
		 AMLUtils.getAMLAccounts(entityId, facade);
		
		Log.debug("Found " + rsAmlAccounts.getNumRows() + 
		 " accounts requiring AML validation.");
		
		if (rsAmlAccounts.isEmpty())
			throw new ServiceException("No eligible accounts for this client.");
		
		Vector<Account> amlAccounts = Account.getAccounts(rsAmlAccounts);
		
		// Create mapping of correspondent Person IDs -> client accounts 
		// requiring AML check.
		//
		// This is used to determine how many Mandate forms to create.
		HashMap<Integer, Vector<Account>> corrAccountMap = 
		 Account.getCorrespondentAccountMap(amlAccounts, facade);
		
		int numCorrespondents = corrAccountMap.keySet().size();
		
		Log.debug("AML accounts are spread between " 
		 + numCorrespondents + " correspondents. Requires creation of " +
		 + numCorrespondents + " Mandate forms.");
		
		Iterator<Integer> i = corrAccountMap.keySet().iterator();
		
		// Loop through each correspondent, generating a Mandate form for
		// each.
		while (i.hasNext()) {
			Integer personId = i.next();
			Vector<Account> corrAmlAccounts = corrAccountMap.get(personId);
			
			ByteArrayOutputStream outputStream = null;
			
			try {
				// Use the Aurora Correspondent ID for now when assigning
				// dDocNames. Future form types should use the Person ID
				// though.
				Vector<AuroraCorrespondent> correspondents =
				 AuroraCorrespondent.getCorrespondentsByPersonId(personId, facade);
				
				if (correspondents.size() == 0) {
					throw new ServiceException("Unable to generate form: " +
					 "no Aurora Correspondent ID for Person " + personId);
				} else if (correspondents.size() > 1) {
					throw new ServiceException("Unable to generate form: " +
					 "multiple Aurora Correspondent IDs for Person " + personId);
				}
	
				int corrId = correspondents.get(0).getCorrId();
				
				String formDocName = "FORM-" + processId + "-" + corrId;
				
				// Create new Form record for this mandate
				UCMForm form = UCMForm.add(
				 processId, 
				 MandateSpoolFileGenerator.FORM_TYPE, 
				 MandateSpoolFileGenerator.FORM_SUBTYPE_SINGLE_ACCOUNT,
				 entityId,
				 auroraClient,
				 personId, 
				 formDocName, null, 
				 facade);
				
				outputStream = createMandateSpoolFile
				 (processId, form, entity, auroraClient, 
				  personId, corrAmlAccounts, true, userName, facade);
				
				String fileName = formDocName + ".txt";
				String docTitle = 
				 "Mandate form - entity ID: " + entityId + ", person ID: " + personId;
				
				Hashtable<String, String> attributes = new Hashtable<String, String>();
				
				attributes.put("xDocumentClass", "AMLCHASER");
				attributes.put("xCompany", auroraClient.getCompany().getCode());
				attributes.put("xClientNumber", 
				 Integer.toString(auroraClient.getClientNumber()));
				attributes.put("xSource", "Form Generator");
				
				int dID = FormUtils.checkinForm(form, docTitle, fileName, userName, 
				 attributes, outputStream, facade);
				
				// Add activity log to Client Services process, indicating form
				// was generated.
				if (process != null) {
					process.addActivity(userName, personId, "Form Generation",
					 "Mandate form " + form.getFormId() + " created @DOCNAME:" 
					 + formDocName + "@", 
					 null, false, facade);
				}
				
			} catch (Exception e) {
				Log.error("Failed to create/checkin Mandate form", e);
				
				if (process != null) {
					// Add activity log to Client Services process, indicating form
					// was generated.
					
					process.addActivity(userName, personId, "Form Generation",
					"Failed to generate Mandate form",
					 null, true, facade);
				}
				
				throw new ServiceException(e);
			}
		}
	}
	
	/** Generates the Create!Form spool file for a Mandate form, mapped
	 *  to a single client/correspondent and a set of associated accounts. 
	 *  This is returned as a ByteArrayOutputStream.
	 *  
	 *  This requires several operations:
	 *  
	 *  1. Fetch client/correspondent data.
	 *  2. Build spool file header wrapper object
	 *  3. Fetch accounts for AML verification 
	 *  6. Create the spool file
	 *  
	 */
	public static ByteArrayOutputStream createMandateSpoolFile(
	 int processId, UCMForm form, Entity entity, AuroraClient auroraClient,
	 Integer corrId, Vector<Account> corrAmlAccounts, 
	 boolean displayBankingDetails,
	 String userName, FWFacade facade) 
	
	 throws ServiceException, DataException, IOException {
		
		Log.debug("Creating Mandate Form");
		
		int clientId 	= auroraClient.getClientNumber();
		String company	= auroraClient.getCompany().getCode();
		
		Log.debug("Client: " + clientId + ", Company: " + company 
		 + ", Correspondent: " + corrId);
		
		Log.debug("Searching for account signatories");
		
		/**
		 * Proper method used to fetch signatories, using account relationships table.
		 */
		Vector<Person> signatories = new Vector<Person>();
		
		for (int i=0; i<corrAmlAccounts.size(); i++) {
			Account account = corrAmlAccounts.get(i);
			
			// Fetch all persons related to this account
			HashMap<RelationName, Vector<Person>> relatedPersons = 
			 account.getRelatedPersons(facade);
		
			for (RelationName relationName : relatedPersons.keySet()) {
				
				if (relationName.getRelationNameId() 
					== 
					RelationName.PersonAccountRelation.SIGNATORY) {
						
					signatories.addAll(relatedPersons.get(relationName));
					
					Log.debug("Found " + relatedPersons.get(relationName).size() +
					 " registered authorized signatories for account: " + 
					 account.getAccNumExt());
				}
			}
		}
		
		Log.debug("Total signatories fetched: "  + signatories.size());
		
		for (int i=0; i<signatories.size(); i++) {
			Person p = signatories.get(i);
			Log.debug(p.toString());
		}
		
		//Vector<Signatory> sigs = getSignatories(signatories);
		
		// Fetch data for this account correspondent
		Person correspondent = Person.get(corrId, true, facade);
			
		if (correspondent == null)
			throw new ServiceException("Unable to generate form: " +
			 "could not find data for associated correspondent Person ID: " +
			 corrId);
		
		String docName 	= form.getBaseDocName();
		int formId		= form.getFormId();
		
		MandateForm mandateForm = new MandateForm
		 (formId, entity, correspondent, corrAmlAccounts, 
		  displayBankingDetails, null, facade);
		
		// Add form-account mapping
		FormUtils.setFormAccountMapping(formId, corrAmlAccounts, facade);
		
		String templatePath			= docName + ".txt";
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (company, null, userName, 1, null, null, templatePath);

		MandateSpoolFileGenerator spoolFileGenerator = 
		 new MandateSpoolFileGenerator(
		  spoolHeader, docName, processId, entity, auroraClient, mandateForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
	
	/** Generates a list of Signatory instances from the passed Person instances.
	 * 
	 *  Signatories originally extracted from Aurora will not have address records.
	 *  Instead, if they have a postcode/phone number set in Aurora, these would
	 *  have been copied into the Notes field for their Person entry. This method
	 *  extracts these fields, if they exist.
	 *  
	 *  @deprecated
	 *  
	 * @param persons
	 * @return
	 */
	private static Vector<Signatory> getSignatories(Vector<Person> persons) {
		Vector<Signatory> sigs = new Vector<Signatory>();
		
		/*
		for (int i=0; i<persons.size(); i++) {
			String fullName = persons.get(i).getFullName();
			String tel 		= null;
			String postCode	= null;
			
			if (persons.get(i).getAddress() != null) {
				// Extract telephone and post code from Address instance
				tel 		= persons.get(i).getAddress().getTelephone();
				postCode 	= persons.get(i).getAddress().getPostCode();
			} else {
				// Attempt to extract tel and post code from notes field
				String notes = persons.get(i).getNotes();
				
				if (!StringUtils.stringIsBlank(notes)) {
					String[] elems = notes.split(":");
					
					if (elems.length == 3) {
						tel 		= elems[1];
						postCode	= elems[2];
					}
				}
			}
			
			Signatory sig = new Signatory(fullName, tel, postCode);
			sigs.add(sig);
		}
		*/
		
		return sigs;
	}
	
	/** Checks the given list of accounts to see if their payment
	 *  preferences match.
	 *  
	 *  TODO: update this!!
	 *  
	 * @param accounts
	 * @return
	 * @throws DataException 
	 */
	private static boolean checkForMatchingBankingDetails
	 (Vector<Account> accounts) throws DataException {
		
		Log.debug("Checking for matching details across " 
		 + accounts.size() + " accounts. Hardcode return false");
		
		/*
		for (int i=0; i<accounts.size(); i++) {
			CCLAAccount account = accounts.get(i);
			CCLAFund fund			= account.getFund();
			
			for (int j=0; j<accounts.size(); j++) {
				
				if (i != j) {
					
					CCLAAccount checkAccount 		= accounts.get(j);
					CCLAFund checkFund				= checkAccount.getFund();
					
					if ((fund.getTypeCode().equals(checkFund.getTypeCode()))
						&&
						(fund.getIncomeTypeCode().equals(checkFund.getIncomeTypeCode()))
						&&
						(account.getPaymentPref().equals(checkAccount.getPaymentPref()))
						&&
						(account.hasIncomeBankingData() == checkAccount.hasIncomeBankingData())
						&&
						(account.hasWithdrawalBankingData() == checkAccount.hasWithdrawalBankingData())) {
						
						// Check for matching income bank details, if applicable
						if (account.hasIncomeBankingData()) {
							if (!account.getBankAccountNumberIncome().
								 equals(checkAccount.getBankAccountNumberIncome())) {
								
								Log.debug("Failed account match: income banking details " +
								 "differed between " + account.getAccountNumberExt() + ", " 
								 + checkAccount.getAccountNumberExt());
								 
								return false;
							}
						}
						
						// Check for matching withdrawal bank details, if applicable
						if (account.hasWithdrawalBankingData()) {
							if (!account.getBankAccountNumberWithdrawal().
								 equals(checkAccount.getBankAccountNumberWithdrawal())) {
								
								Log.debug("Failed account match: withdrawal banking details " +
								 "differed between " + account.getAccountNumberExt() + ", " 
								 + checkAccount.getAccountNumberExt());
								
								return false;
							}
						}
					} else {
						Log.debug("Failed account match: fund types or general preferences " +
						 "differed between " + account.getAccountNumberExt() + ", " 
						 + checkAccount.getAccountNumberExt());
						
						return false;
					}
					
					Log.debug("Account " + account.getAccountNumberExt() + " matches "
					 + checkAccount.getAccountNumberExt());
				}
			}
		}
		*/
		
		return false;
	}
}
