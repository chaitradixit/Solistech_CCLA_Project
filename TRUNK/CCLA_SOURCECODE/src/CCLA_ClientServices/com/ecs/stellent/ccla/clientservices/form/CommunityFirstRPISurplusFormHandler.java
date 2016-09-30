package com.ecs.stellent.ccla.clientservices.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import intradoc.common.ParseStringException;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.stellent.ccla.clientservices.SubscriptionUtils;
import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.spool.CommunityFirstClientInformationSpoolFileGenerator;
import com.ecs.stellent.ccla.clientservices.spool.CommunityFirstRPISurplusSpoolFileGenerator;
import com.ecs.stellent.ccla.signature.data.InstructionSignature;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormElementApplied;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.ucm.ccla.data.subscription.RpiDrawdown;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CommunityFirstRPISurplusFormHandler extends AccountFormHandler {
	
	/** Date format used when passing date parameters to external ADF page
	 * 
	 */
	public static SimpleDateFormat ADF_PAGE_DATE_FORMAT
	 = new SimpleDateFormat("yyyy-MM-dd");
	
	public enum RpiDate {
		DEADLINE_DATE	("CommunityFirst_RPI_DeadlineDate"),
		REPORT_DATE		("CommunityFirst_RPI_ReportDate"),
		CALCULATION_DATE("CommunityFirst_RPI_CalcDate"),
		SALE_DATE		("CommunityFirst_RPI_SaleDate");
		
		private String configVarName;
		
		private RpiDate(String configVarName) {
			this.configVarName = configVarName;
		}
		
		public String getConfigVarName() {
			return configVarName;
		}
	}
	
	@Override
	public void doPostCheckinActions(int docId) throws Exception {
		// TODO Auto-generated method stub
		super.doPostCheckinActions(docId);
	}

	@Override
	public Hashtable<String, String> getDocMetaMapping() throws DataException {
		Hashtable<String, String> map = super.getDocMetaMapping();
		
		if (form.getCalculationDate() == null) {
			// Calculation Date MUST be present on RPI surplus forms. Fail
			throw new DataException("Calculation Date on RPI Surplus Form ID " + 
			 this.form.getFormId() + " missing! Unable to process.");
		}
		
		// Parse the date to obtain a truncated string for log messages.
		String truncCalcDate = null;
		
		try {
			truncCalcDate = DateFormatter.getTimeStamp
			 ("dd/MM/yyyy", form.getCalculationDate());
		} catch (ParseStringException e) {
			throw new DataException("Unable to parse form Calculation Date: " +
			 form.getCalculationDate().toString());
		}
		
		// Check if this is the latest RPI Surplus form for the Calculation Date
		// If there were multiple form instances generated, only the most recent one
		// is considered to be valid.
		Form latestRPISurplusForm = getLatestRPISurplusForm();
		
		if (latestRPISurplusForm != null) {
			if (latestRPISurplusForm.getFormId() != this.form.getFormId()) {
				// Older form has been returned! Abort!!
				Log.warn("Older form for Org ID " + form.getOrganisationId() + 
				 ", Type ID: " + form.getFormType() + ", Calculation Date: " + 
				 truncCalcDate +
				 " detected! Form ID: " + form.getFormId() + ". Latest form has ID " 
				 + latestRPISurplusForm.getFormId() 
				 + ". Indexing as CONDINS instead");
				
				map.put(UCMFieldNames.DocClass, DocumentClass.Classes.CONDINS);
				// Add comment
				String msgHeader = "RPI Surplus Form invalid";
				String msgBody = "Newer form exists for Calculation Date " + truncCalcDate + 
				 " with Form ID " + latestRPISurplusForm.getFormId();
				
				map.put(UCMFieldNames.Comments, msgHeader + ". " + msgBody);
				
				FormMessage message = new FormMessage(
					MessageType.ERROR,
					msgHeader,
					msgBody
				);
				enqueueFormMessage(message);
			}
		} else {
			// Weird - no latest form found with Org ID/Calc Date. There must have been
			// at least one (i.e. the current form in context) so this is a fatal
			// state.
			throw new DataException("Unable to determine latest RPI Surplus Form ID for "
			 + "Calculation Date " + truncCalcDate);
		}
		
		// Add Comm First account reference.
		/*
		try {
			Account lcfAccount = Account.getAccountByIndexingValues
			 (form.getOrganisationId(), "1CF", false, facade);
			
			if (lcfAccount != null)
				addAccountDocMeta(map, lcfAccount);
			else
				throw new DataException("CF Account not found");
			
		} catch (Exception e) {
			throw new DataException("Unable to retrieve Comm First account info for "+
			 "Organisation ID " + form.getOrganisationId() + ": " + e.getMessage());
		}
		*/
		
		return map;
	}
	
	private Form getLatestRPISurplusForm() throws DataException {
		
		return
				 Form.getLatestFormByTypeOrgAndCalculationDate
				 (form.getFormType().getFormTypeId(), form.getOrganisationId(), 
				 form.getCalculationDate(), facade);
	}

	public CommunityFirstRPISurplusFormHandler(Form form, String userId,
			FWFacade facade) {
		super(form, userId, facade);
	}

	public CommunityFirstRPISurplusFormHandler(String userId, FWFacade facade) {
		super(userId, facade);
	}

	@Override
	public Form generateForm(FormType formType, Element element,
			Integer investmentId) throws ServiceException, DataException {
				
		// Check Organisation
		Entity org = Entity.get(element.getElementId(), true, facade);
		
		if (org == null)
			throw new DataException("Unable to generate " + formType.getName() + 
			 ": no org found with ID " + element.getElementId());
		
		Log.debug("Generating " + formType.getName() + " form for Org ID: " 
		 + org.getOrganisationId());
	
		Person correspondent = Entity.getNominatedCorrespondent
			(org.getOrganisationId(), true, facade);
		
		if (correspondent==null)
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 "No nominated correspondent set for " + org.getOrganisationName());
	
		// Check Contact Details
		Contact nomAddressContact = Contact.getDefaultContact
			(correspondent.getContacts(), Contact.ADDRESS);

		boolean isAddressValid = Address.addressDataValid
		 (nomAddressContact.getAddressId(), facade);
		
		if (!isAddressValid)
			throw new ServiceException("Correspondent postal address is not valid, " +
			 "ensure it has a number/street/postcode set");
		
		return createDrawdownForm(
			org, 
			correspondent, 
			nomAddressContact.getAddress(),
			formType
		);
	}
	
	private Form createDrawdownForm(
		Entity org, 
		Person correspondent, 
		Address corrAddress,
		FormType formType) throws DataException, ServiceException {
		
		Log.debug("Creating RPI Drawdown form for LCF " + org.getOrganisationName() 
		 + ", code: " + org.getOrgAccountCode());
	
		// Fetch special dates that are displayed on the form (deadlines etc.)
		Map<RpiDate, Date> rpiDateValues = new EnumMap<RpiDate, Date>(RpiDate.class);
		
		for (RpiDate rpiDate : RpiDate.values()) {
			SystemConfigVar dateVar = SystemConfigVar.getCache().getCachedInstance
				(rpiDate.getConfigVarName());
			
			if (dateVar == null || dateVar.getDateValue() == null) {
				throw new DataException("System Config Var " + rpiDate.getConfigVarName()
				 + " date value missing");
			}
			rpiDateValues.put(rpiDate, dateVar.getDateValue());
		}
		
		// Fetch LCF Campaign Enrolment
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (CommunityFirstClientEnrolmentHandler.CAMPAIGN_ID, org.getOrganisationId(), facade);
		
		if (enrolment == null)
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 org.getOrganisationName() + " is not enrolled to Community First campaign");
		
		// Fetch Aurora COIF Client
		Company coifCompany = Company.getCache().getCachedInstance(Company.COIF);
		
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), coifCompany, facade);
		
		// Fetch all Donors for this LCF
		Vector<Integer> orgPersonRelationNameIds = new Vector<Integer>();
		orgPersonRelationNameIds.add(RelationName.OrganisationPersonRelation.DONOR);

		List<Person> donorPersons = Relation.getRelatedPersons
		 (org.getOrganisationId(), ElementType.ORGANISATION, orgPersonRelationNameIds, facade);

		Vector<Integer> orgOrgRelationNameIds = new Vector<Integer>();
		orgOrgRelationNameIds.add(RelationName.OrganisationOrganisationRelation.DONOR);
		
		List<Entity> donorOrgs = Relation.getRelatedOrgs
		 (null, org.getOrganisationId(), orgOrgRelationNameIds, facade);
		
		Log.debug("Found " + donorPersons.size() + " Person donors and " 
		 + donorOrgs.size() + " Org donors");
				
		List<Element> allDonors = new ArrayList<Element>();
		
		allDonors.addAll(donorPersons);
		allDonors.addAll(donorOrgs);
		
		// Sort them all by name.
		Collections.sort(allDonors, new Comparator<Element>() {
			public int compare(Element e1, Element e2) {
				return e1.getName().compareTo(e2.getName());
			}
		});
				
		// Fetch all RPI Drawdown details for this LCF
		List<RpiDrawdown> drawdowns = RpiDrawdown.getAll(
			RpiDrawdown.getAllDataByCampaignAndDateAndOwner(
				CommunityFirstClientEnrolmentHandler.CAMPAIGN_ID, 
				rpiDateValues.get(RpiDate.CALCULATION_DATE), 
				org.getOrganisationId(), 
				facade
			)
		);
		
		if (drawdowns.isEmpty()) {
			Log.warn("No drawdown entries found for LCF " + org.getOrganisationName() 
			 + ", code: " + org.getOrgAccountCode());
		}
		
		Map<Integer, RpiDrawdown> donorDrawdowns = new HashMap<Integer, RpiDrawdown>();
		
		for (RpiDrawdown drawdown : drawdowns)
			donorDrawdowns.put(drawdown.getDonorId(), drawdown);
		
		Map<Integer, Account> donorAccounts = new HashMap<Integer, Account>();
		
		Fund depositFund = Fund.getCache().getCachedInstance
		 (CommunityFirstClientEnrolmentHandler.CDF_DEPOSIT_FUNDCODE.toString());
		
		// Fetch Donor deposit accounts
		for (Element donorElement : allDonors) {
			Account donorAccount = SubscriptionUtils.getDonorAccount(
				org.getOrganisationId(), 
				donorElement.getElementId(), 
				depositFund, 
				facade
			);
			
			donorAccounts.put(donorElement.getElementId(), donorAccount);
		}
		
		RpiSurplusDetails surplusDetails = new RpiSurplusDetails(
			org, 
			allDonors, 
			donorDrawdowns, 
			donorAccounts, 
			correspondent, 
			corrAddress
		);
		
		surplusDetails.setCalcDate(rpiDateValues.get(RpiDate.CALCULATION_DATE));
		surplusDetails.setReportDate(rpiDateValues.get(RpiDate.REPORT_DATE));
		surplusDetails.setDeadlineDate(rpiDateValues.get(RpiDate.DEADLINE_DATE));
		surplusDetails.setSaleDate(rpiDateValues.get(RpiDate.SALE_DATE));

		try {
			// Generate a new Form entry in the DB.
			form = Form.add(
				formType,
				enrolment.getCampaignEnrolmentId(),
				null,
				correspondent.getPersonId(),
				null,
				org.getOrganisationId(),
				null,
				facade,
				userId	
			);
			
			form.setCalculationDate(rpiDateValues.get(RpiDate.CALCULATION_DATE));
			form.persist(facade, userId);
			
			// Add reference to CF Account
			Account cfAccount = CommunityFirstClientEnrolmentHandler.getDonationAccount
			 (org.getOrganisationId(), facade);
			FormElementApplied.add(form.getFormId(), cfAccount.getAccountId(), facade);
			
			String fileName = FormUtils.getSpoolFileName
			 (form, org, auroraClient);

			String clientIdStr = auroraClient.getPaddedClientNumber();
			
			String templatePath			= fileName;
			
			SpoolHeader spoolHeader 	= new SpoolHeader
			 (coifCompany.getCode(), clientIdStr, 
			 userId, 1, form.getFormId(), org.getOrganisationId(), templatePath);

			// Generate the spool file
			CommunityFirstRPISurplusSpoolFileGenerator spoolGen = 
				new CommunityFirstRPISurplusSpoolFileGenerator(spoolHeader, surplusDetails);
					
			ByteArrayOutputStream outputStream = spoolGen.createSpoolFile();
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, coifCompany.getCode(), outputStream);
			
			form.setDateGenerated(new Date());
			
			Log.debug("Generated temp spool file: " + spoolFile.getName() + 
			 ". Dispatching for print.");
			
			boolean print = true;
			
			if (print) {
				FormUtils.printForm(spoolFile.getAbsolutePath());
				
				// Update form data to indicate it was printed
				form.setFormStatusId(Form.FormStatus.PRINTED.id);
				form.setDatePrinted(new Date());
			} else {
				// Set generated status
				form.setFormStatusId(Form.FormStatus.GENERATED.id);
			}
			
			form.persist(facade, userId);
			
			// Add activity log to campaign enrolment, indicating form
			// was generated.
			enrolment.addActivity(correspondent.getPersonId(), 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 formType.getName() + " form " + form.getFormId() + 
			 " created and dispatched for print", null, facade, userId); 

		} catch (Exception e) {
			Log.error("Failed to generate/print " + formType.getName() + " form", e);
			
			// Add activity log to Client Services process, indicating form
			// failed to generate.
			enrolment.addActivity(correspondent.getPersonId(), 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 "Failed to generate " + formType.getName() + " form", 
			 null, facade, userId); 
			
			throw new ServiceException(e);
		}
		
		Log.debug("RPI Drawdown form generated successfully");
		return form;
	}

	/** Adds the bits required to build the link to the external RPI surplus withdrawal
	 *  value entry screen.
	 */
	@Override
	public void addExtraBinderParams(DataBinder binder) {
		super.addExtraBinderParams(binder);
		
		Log.debug(this.getClass().getName() + "::addExtraBinderParams");
		
		try {
			// First, check that the associated doc has been signature checked.
			String docName = binder.getLocal("docName");
			boolean sigsChecked = false;
			
			if (!StringUtils.stringIsBlank(docName)) {
				Log.debug("Checking no. of approved signatures against request document "
				 + docName);
				
				LWDocument lwDoc = new LWDocument(docName);
				
				String docGUID = CCLAUtils.getDocGuidFromLwd(lwDoc);
				Vector<InstructionSignature> approvedSigs = 
				 InstructionSignature.getAllByDocGuid
				 (docGUID, this.facade);
				
				int numApprovedSigs = approvedSigs.size();
				Log.debug("Found " + numApprovedSigs + " approved signatures");
				
				// Fetch mapped accounts.
				Vector<Account> accounts = FormElementApplied.getFormAccounts
				 (form.getFormId(), ElementType.ACCOUNT, facade);
				
				// Add data for single mapped account
				if (!accounts.isEmpty()) {
					Account account = accounts.get(0);
					
					if (account.getRequiredSignatures() != null) {
						Log.error("Account ID: " + account.getAccountId() + 
						 " requires " + account.getRequiredSignatures() + " signatures");

						if (numApprovedSigs >= account.getRequiredSignatures()) {
							Log.error("RPI doc has valid number of signatures verified");
							sigsChecked = true;
						}
						
					} else {
						Log.error("Unable to determine no. of required signatures for " +
						 "Account ID: " + account.getAccountId());
					}
					
				} else {
					Log.error("Unable to determine no. of required signatures, no " +
					 "linked account found");
				}
			}
			
			if (!sigsChecked) {
				// Don't show the ADF page link in this case.
				FormMessage message = new FormMessage(
					MessageType.WARNING,
					"Sig Check Required",
					"The RPI form must be signature checked before entry of " +
					"drawdown amounts"
				);
				enqueueFormMessage(message);
				return;
			}
			
			// Signatures are checked - build the ADF page link.
			SystemConfigVar urlVar = SystemConfigVar.getCache().getCachedInstance
			 ("CommunityFirst_RPISurplusDrawdown_ADFPageURL");
			
			if (urlVar == null)
				throw new DataException("URL not found in Sys Config Vars");
			
			StringBuffer url = new StringBuffer(urlVar.getStringValue());
			
			Entity org = Entity.get(this.form.getOrganisationId(), this.facade);
			url.append("?orgCode=" + org.getOrgAccountCode());
			url.append("&stCode=rpi_dividend");
			url.append("&endDate=" + 
			 ADF_PAGE_DATE_FORMAT.format(this.form.getCalculationDate()));
			
			// Attempt to figure out which indexing step the returned doc is at, by
			// checking its Status value.
			String userRole = "index";
			
			if (this.form.getFormStatusId() == Form.FormStatus.INDEXED.id) {
				userRole = "validate";
			} else if (this.form.getFormStatusId() == Form.FormStatus.VALIDATED.id
					||
					this.form.getFormStatusId() == Form.FormStatus.INVALIDATED.id) {
				userRole = "admin";
			}
			
			url.append("&userRole=" + userRole);
			url.append("&userName=" + this.userId);
			
			/*
			if (this.form.getRetDocGuid() != null) {
				LWDocument lwDoc = 
				 CCLAUtils.getLatestLwdFromDocGuid(this.form.getRetDocGuid());
				
				if (lwDoc != null) {
					String bundleRef = lwDoc.getAttribute(UCMFieldNames.BundleRef);
				}
			}
			*/

			binder.putLocal(FormLinkBinderStrings.URL, url.toString());
		
			binder.putLocal
			 (FormLinkBinderStrings.LABEL, "Enter RPI drawdown amounts...");
			
		} catch (Exception e) {
			Log.error("Unable to build ADF screen link for form ID "
			 + this.form.getFormId() + ": " + e.getMessage(), e);
			return;
		}
	}
}
