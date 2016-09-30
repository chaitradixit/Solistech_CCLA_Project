package com.ecs.ucm.ccla.commproc;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.iris.ItemLockUtils;
import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.stellent.iris.batch.dualindex.DualIndexUtils;
import com.ecs.stellent.spp.data.SPPJobProfile;
import com.ecs.stellent.spp.fundprocessdetails.FundProcessDetailsManager;
import com.ecs.stellent.spp.service.BundlePriorityUtils;
import com.ecs.stellent.spp.service.SppIntegrationServices;
import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.Globals.Users;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Property;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationPropertyApplied;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.UserWorkGroup;
import com.ecs.ucm.ccla.data.WorkGroup;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.staticdataupdate.StaticDataUpdateApplied;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class DocumentBundleService extends Service {

	/** Determines whether or not the Process Date field is calculated and
	 *  applied to bundle items at the end of the EnterDetails/Validation steps.
	 *  
	 */
	public static final boolean APPLY_DOCUMENT_PROCESS_DATE =
	 !StringUtils.stringIsBlank
	 (SharedObjects.getEnvironmentValue("SPP_ApplyDocumentProcessDate"));
	
	/** Determines whether bundles are immediately locked after being
	 *  returned by the getNextPendingBundle() method.
	 * 
	 */
	public static final boolean PREEMPTIVE_AUTOFETCH_LOCK =
	 !StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue
	 ("CCLA_preemptiveAutoFetchLock"));
	
	/** Determines whether all AML Tracker attributes/properties/overrides are stripped
	 *  from accounts linked to Mandate instructions post-EnterDetails.
	 *  
	 */
	public static final boolean REMOVE_AML_TRACKER_ATTRIBUTES_POST_ENTERDETAILS =
	 !StringUtils.stringIsBlank
	 (SharedObjects.getEnvironmentValue
	  ("CCLA_CommProc_RemoveAMLTrackerAttributesPostEnterDetails"));
	
	/** Available values for the xStatus field on parent bundle items. 
	 * 
	 * @author Tom
	 *
	 */
	public static enum BundleStatus {
		EnterDetails,
		Validation,
		Completed
	}
	
	/**
	 * Fetches the next pending bundle item for the given user, referring to their
	 * assigned WorkGroup if one exists. If the user has a mapped WorkGroup, this will
	 * determine the fetch query used. If not, the default WorkGroup is used instead.
	 * <br/>
	 * If a pending bundle is found, the dDocName/xBundleRef values are dropped into 
	 * the binder, so the new bundle can be displayed.
	 * <br/>
	 * Designed to be called via AJAX with IsJson=1 parameter in the URL, so the 
	 * response parameters can be easily parsed.
	 * 
	 * @throws DataException 
	 */
	public void getNextPendingBundle() throws DataException {
		
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace);
		//FWFacade oraFacade = CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsPendingBundle = null;

		do {
			// Attempt to fetch a pending bundle for this user.
			rsPendingBundle = WorkGroup.getNextPendingBundleForUser
			 (ucmFacade, m_userData.m_name);
			
			if (!rsPendingBundle.isEmpty()) {
				String docName 		= rsPendingBundle.getStringValue(0);
				String bundleRef	= rsPendingBundle.getStringValue(1);
				
				m_binder.putLocal("docName", docName);
				m_binder.putLocal("bundleRef", bundleRef);
				
				if (PREEMPTIVE_AUTOFETCH_LOCK) {
					// Attempt to add a bundle lock before it is loaded.
					//
					// This should prevent the same bundle being returned in a
					// subsequent fetch query.
					Log.debug("Found pending bundle " + docName + " (" + bundleRef + 
					 "), attempting pre-emptive lock");
			
					DataResultSet rsItemLock = 
					 ItemLockUtils.getItemLock(docName, ucmFacade);
					
					if (rsItemLock == null) {
						// The bundle is unlocked - lock it now.
						ItemLockUtils.addItemLock(docName, m_userData.m_name, ucmFacade);
						return;
					} else {
						Log.debug("Unable to pre-emptively lock bundle, already " +
						 "locked to " + rsItemLock.getStringValueByName("LOCKUSER") +
						 ". Attempting to fetch another...");
						
						try {
							Thread.sleep(100); 	// wee sleep to stop the service 
												// hammering the DB with fetch queries
						} catch (InterruptedException e) {}
					}
				} else {
					// No attempt to pre-emptively lock the bundle. Return regardless
					// of lock state
					return;
				}
			}
			
		} while (!rsPendingBundle.isEmpty());
	
		// No pending bundles left!
		m_binder.putLocal("noPendingBundles", "1");
	}
	
	/** Drops the WorkGroup ID and WorkGroup Name from the WorkGroup mapped to the 
	 *  current user into the DataBinder.
	 *  <br/>
	 *  If the user doesn't have a WorkGroup mapped, the default WorkGroup details are
	 *  dropped in instead.
	 * @throws DataException 
	 */
	public void getUserWorkGroup() throws DataException {
		WorkGroup workGroup = null;
		
		UserWorkGroup userWorkGroup = 
		 UserWorkGroup.getCache().getCachedInstance(m_userData.m_name);
		
		if (userWorkGroup == null) {
			workGroup = WorkGroup.getCache().getCachedInstance
			 (WorkGroup.DEFAULT_WORK_GROUP_ID);
		} else {
			workGroup = userWorkGroup.getWorkGroup();
		}
		
		CCLAUtils.addQueryIntParamToBinder
		 (m_binder, WorkGroup.Cols.ID, workGroup.getWorkGroupId());
		
		m_binder.putLocal(WorkGroup.Cols.NAME, workGroup.getName());
	}
	
	/** 
	 *  Sub-service method called after the custom CCLA_WF_APPROVE service,
	 *  used for bundles in the CCLA_MailHandling workflow.
	 *  
	 *  This will be executed when the user clicks the 'Submit Bundle' button
	 *  in Iris.
	 *  
	 *  The xStatus value of the Bundle item in question must be checked to
	 *  determine the next course of action for the items in the Bundle.
	 *  
	 *  The following status values are supported:
	 *  
	 *  xStatus=Validation
	 *  ------------------
	 *  The bundle has been through the EnterDetails step and should now be in
	 *  the Validation step. 
	 *  
	 *  All bundle documents must have their xStatus values set to 'Validation'
	 *  
	 *  Process dates will also be applied to any documents where the date
	 *  isn't already set.
	 *  
	 *  xStatus=Completed
	 *  -----------------
	 *  The bundle has been through the Validation step and should have exited
	 *  the workflow. Dispatch the bundle for job creation/archival
	 *  (as per old behaviour).
	 *  
	 *  Process dates will also be applied to any documents where the date
	 *  isn't already set.
	 *  
	 * @return
	 * @throws Exception 
	 */
	public void applyPostWorkflowAction() throws Exception {
		
		String bundleRef 		= m_binder.getLocal("bundleRef");
		String bundleStatus		= m_binder.getLocal("xStatus");
		String bundleDocName 	= m_binder.getLocal("dDocName");
		
		if (StringUtils.stringIsBlank(bundleRef)) {
			throw new ServiceException("No bundleRef found - unable to " +
			 "process bundle items");
		}
		
		if (StringUtils.stringIsBlank(bundleStatus)) {
			throw new ServiceException("No bundle status found - unable to " +
			 "process bundle items");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace);
		
		Log.debug("Applying post-workflow action for bundle " + bundleRef);
		
		DataResultSet rsBatchItems = 
		 BatchDocumentServices.getBatchItems(bundleRef , ucmFacade, false);
		
		Log.debug("Found " + rsBatchItems.getNumRows() + " items " +
		 "in bundle "+bundleRef);
			
		if (bundleStatus.equals(BundleStatus.EnterDetails.toString())) {
			// Bundle at EnterDetails status.
			Log.debug("Bundle "+bundleRef+" in EnterDetails step. No action will be taken.");
			
		} else if (bundleStatus.equals(BundleStatus.Validation.toString())) {
			// Bundle in Validation status.
			Log.debug("Bundle "+bundleRef+" in Validation step.");
			
			// Re-evaluate priority.
			LWDocument bundleDoc = new LWDocument
			 (bundleDocName, true);
			
			int priority = 
			 BundlePriorityUtils.getPriority(bundleDoc, rsBatchItems);
			
			Log.debug("Updating bundle "+bundleRef+" priority to " + priority);
			
			bundleDoc.setAttribute("xPriority", Integer.toString(priority));
			bundleDoc.update();
			
			// Create Dual Index Item entries for documents that require
			// dual indexing.
			Vector<String> dualIndexDocs = DualIndexUtils.addBundleDualIndexItems
			 (bundleDoc, rsBatchItems, true, false, ucmFacade);
			
			// Update all bundle document xStatus values to 'Validation',
			// except when the document was previously marked as a duplicate.
			//
			// Set the Process Date on items where it isn't already set.
			if (rsBatchItems.first()) {
				do {
					boolean updateItem = false;
					
					String itemDocName = 
					 rsBatchItems.getStringValueByName(UCMFieldNames.DocName);
					
					String status = 
					 rsBatchItems.getStringValueByName(UCMFieldNames.Status);
					
					LWDocument lwDoc = new LWDocument(itemDocName, true);
					
					if (status.equals("Duplicate")) {
						Log.debug("Skipping bundle document status update " +
						 "for " + itemDocName + ": marked as Duplicate in bundle "+bundleRef);
					} else {
						Log.debug("Updating bundle document status: " +
						 itemDocName + " to 'Validation' in bundle "+bundleRef); 
						
						updateItem = true;
					}
					
					if (updateItem)
						lwDoc.update();
					
					String docClassStr = lwDoc.getAttribute(UCMFieldNames.DocClass);
					DocumentClass docClass = DocumentClass.getCache().getCachedInstance
					 (docClassStr);
					
					// If this is a mandate document class, remove all AML Tracker
					// KYC attributes from the associated account.
					if (docClass.isMandate()) {
						
						String clientNumStr = rsBatchItems.getStringValueByName
						 (UCMFieldNames.ClientNumber);
						String accountNumStr = rsBatchItems.getStringValueByName
						 (UCMFieldNames.AccountNumber);
						
						Account account = Account.get(
						 Account.getAccountByIndexingValues(
						 clientNumStr, 
						 accountNumStr,
						 false,
						 facade)
						);
						
						if (account!=null) {
							if (REMOVE_AML_TRACKER_ATTRIBUTES_POST_ENTERDETAILS)
								removeAMLTrackerAttributes
								 (account, Globals.Users.System, facade);
							
						} else {
							Log.warn("Cannot find account by indexing values: clientNum="+
									clientNumStr+", accountNum="+accountNumStr);
						}
					}
					
				} while (rsBatchItems.next());
			}
			
			// Apply post-index actions to any documents with Form IDs set.
			if (rsBatchItems.first()) {
				do {
					Integer formId = CCLAUtils.getResultSetIntegerValue
					 (rsBatchItems, UCMFieldNames.FormID);
					
					if (formId != null) {
						Form form = Form.get(formId, facade);
						
						if (form != null) {
							FormHandler formHandler = form.getHandlerInstance
							 (Users.System, facade);
							
							formHandler.doPostIndexActions();
						}
					}
			
				} while (rsBatchItems.next());
			}
			
		} else if (bundleStatus.equals(BundleStatus.Completed.toString())) {
			// Completed bundle. Apply post-validation/pre-instruction register actions
			Log.debug("Bundle "+bundleRef+" in Completed step.");
			
			if (rsBatchItems.first()) {
				do {	
					if (APPLY_DOCUMENT_PROCESS_DATE) {
						
						String origProcessDateStr = null;
						Date originalDealingDate = null;
						
						if (StringUtils.stringIsBlank(origProcessDateStr))	{
							originalDealingDate = 
								FundProcessDetailsManager.getDealingDate
								 (rsBatchItems, false, null, true, facade);
						}
						
						//Calculate the process date (if required)
						Date dealingDate = 
							FundProcessDetailsManager.getDealingDate
							 (rsBatchItems, false, null, false, facade);
					
						String itemDocName = 
						 rsBatchItems.getStringValueByName("dDocName");
							
						if (dealingDate != null || originalDealingDate !=null) {
							
							if (dealingDate!=null) {
								Log.debug("Applying dealing date of "  + dealingDate +
								" to item " + itemDocName + " (fund code: " + 
								rsBatchItems.getStringValueByName("xFund") + 
								") in bundle "+bundleRef);
							}
							
							if (originalDealingDate!=null) {
								Log.debug("Applying original dealing date of "  + 
								 originalDealingDate +
								 " to item " + itemDocName + " (fund code: " + 
								 rsBatchItems.getStringValueByName("xFund") + ") in bundle "+bundleRef);
							}
							
							LWDocument lwDoc = new LWDocument(itemDocName, true);
				
							if (dealingDate!=null) {
								lwDoc.setAttribute("xProcessDate", 
								 DateFormatter.getTimeStamp(dealingDate));
							}
							
							if (originalDealingDate!=null) {
								lwDoc.setAttribute("xOriginalProcessDate", 
								 DateFormatter.getTimeStamp(originalDealingDate));
							}
							
							lwDoc.update();
							
						} else {
							Log.debug("No dealing date or original dealing date will " +
							 "be applied to " + itemDocName + " (already set) in bundle "+bundleRef);
						}
					}
					
				} while (rsBatchItems.next());
			}
			
			// Force any failed Dual Index items to Passed state.
			// Create Dual Index Item entries for documents that require
			// dual indexing.
			Log.debug("forceFailedDualIndexItemsToPassed for bundle "+bundleRef);
			DualIndexUtils.forceFailedDualIndexItemsToPassed
			 (bundleDocName, m_userData.m_name, ucmFacade);
			
			// Apply DependantDocName values to any transaction docs which are marked
			// as 'With Instruction' but don't have a DependantDocName set.
			Log.debug("setDependantDocnames for bundle "+bundleRef);
			setDependantDocNames(bundleRef, ucmFacade);
			
			// Apply post-validation actions to any documents with Form IDs set.
			if (rsBatchItems.first()) {
				do {
					Integer formId = CCLAUtils.getResultSetIntegerValue
					 (rsBatchItems, UCMFieldNames.FormID);
					
					if (formId != null) {
						Form form = Form.get(formId, facade);
						
						if (form != null) {
							FormHandler formHandler = form.getHandlerInstance
							 (Users.System, facade);
							
							formHandler.doPostValidateActions();
						}
					}
			
				} while (rsBatchItems.next());
			}
			
			if (CommProcGlobals.ENABLE_DOC_BUNDLE_CONVERSION) {
				Log.debug("DOC_BUNDLE_CONVERSION enabled for bundle "+bundleRef);
				// Convert the UCM document data into equivalent 
				// CommGroup/Comm/Instruction objects
				facade.beginTransaction();
				
				try {
					// First check if this bundle has already gone through conversion.
					// If so, run the translation in 'append' mode.
					CommGroup commGroup = CommGroup.getByUCMBatchRef(bundleRef, facade);
					boolean append = (commGroup != null);
					
					CommUtils.addDocumentBundle
					 (bundleRef, true, append, facade, m_userData.m_name);
					facade.commitTransaction();
					
				} catch (Exception e) {
					facade.rollbackTransaction();
					
					// TODO: Add audit message to bundle indicating failure reason
					
					Log.error(e.getMessage(), e);
					throw new ServiceException(e.getMessage(), e);
				}
			} else {
				Log.debug("DOC_BUNDLE_CONVERSION not enabled for bundle "+bundleRef);
			}
			
		} else {
			Log.debug("Bundle has unmapped status: " + bundleStatus + ". " +
			 "No action will be taken for bundle "+bundleRef);
		}
	}
	
	/** Removes all AML Tracker KYC attributes linked to the given Account. Includes
	 *  AML Tracker attributes on:
	 *  
	 *  -the owning Organisation
	 *  -related Person IVS check
	 *  -Org-Person relations
	 *  -Account Flags (TODO)
	 * 
	 * @param account
	 * @param userName
	 * @param facade
	 * @throws DataException 
	 */
	public static void removeAMLTrackerAttributes
	 (Account account, String userName, FWFacade facade) throws DataException {
		Log.debug("Removing AML Tracker KYC attributes against Account ID: " 
		 + account.getAccountId());
		
		// Search for 'Verified by AML Tracker' org attribute.
		Integer orgId = account.getOwnerOrganisationId(facade);
		
		ElementAttributeApplied orgVerifiedByAMLTracker = 
		 ElementAttributeApplied.get(
		  orgId, 
		  ElementAttribute.OrganisationAttributes.VERIFIED_BY_AML_TRACKER, 
		  facade
		);
		
		if (orgVerifiedByAMLTracker != null) {
			Log.debug("Removing 'Verified by AML Tracker' org attribute");
			orgVerifiedByAMLTracker.remove
			 (userName, facade);
		}
		
		// Search for IVS override person attributes.
		
		// Grab all the related Account Persons.
		Vector<Person> relAccountPersons = Relation.getRelatedPersons
		 (account.getAccountId(), ElementType.PERSON, null, facade);
		
		for (Person person : relAccountPersons) {
			ElementAttributeApplied personVerifiedByAMLTracker = 
			 ElementAttributeApplied.get(
			  person.getPersonId(), 
			  ElementAttribute.PersonAttributes.AML_TRACKER_CHECKED, 
			  facade
			 );
			
			if (personVerifiedByAMLTracker != null) {
				Log.debug("Removing 'Verified by AML Tracker' person " +
				 "attribute against " + person.getFullName());
				
				personVerifiedByAMLTracker.remove
				 (userName, facade);
			}
		}
		
		// Remove all AML Tracker relation properties against Org-Person relations
		Vector<Person> relOrgPersons = Relation.getRelatedPersons
		 (orgId, ElementType.PERSON, null, facade);
		
		Vector<Element> relOrgPersonElements = new Vector<Element>();
		
		for (Person person : relOrgPersons) {
			relOrgPersonElements.add(person);
		}
		
		Vector<RelationPropertyApplied> orgPersonRelProps = 
		 RelationPropertyApplied.getAll(
		  RelationPropertyApplied.getByRelationsData(
			 null, 
			 relOrgPersonElements,
			 RelationType.getCache().getCachedInstance(RelationType.ORG_PERSON), 
			 null, 
			 facade
		 ));
		
		for (RelationPropertyApplied relProp : orgPersonRelProps) {
			if (relProp.getRelationProperty().getProperty().getPropertyId() ==
				Property.Ids.VERIFIED_BY_AML_TRACKER) {
				Log.debug("Removing 'Verified by AML Tracker' org-person " +
				 "relation attribute against Relation ID " + relProp.getRelationId());
				
				RelationPropertyApplied.remove(relProp, facade, userName);
			}
		}
		
		// TODO Account flags!
	}
	
	/** Sets xDependantDocName value on documents, where values were not explicitly
	 *  set by users during the indexing process.
	 *  
	 *  The list of bundle items is first swept for occurences of mandate/app items. 
	 *  The dDocName of the last one to be detected is stored for the second pass.
	 *  
	 *  In the second pass, any non-mandate/app items which satisfy the following
	 *  critera have their xDependantDocName value set to the mandate/app dDocName:
	 *  -xDependantDocName is empty
	 *  -xWithInstruction = 'Yes'
	 *  
	 * @param bundleRef
	 * @param ucmFacade
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void setDependantDocNames(String bundleRef, FWFacade ucmFacade) 
	 throws DataException, ServiceException {
		
		Log.debug("Setting dependant docNames for bundle " + bundleRef);
		
		String mandateDocName = null;
		
		DataResultSet rsBatchItems = BatchDocumentServices.getBatchItems
		 (bundleRef, ucmFacade);
		
		// First pass of bundle items.
		// Determine if bundle contains a mandates and if so, grab their dDocNames
		// Bear in mind 'mandate' includes MAND, APP, MANDSHORT etc.
		if (rsBatchItems.first()) {
			do {
				String docClassStr = rsBatchItems.getStringValueByName
				 (Globals.UCMFieldNames.DocClass);
				
				if (!StringUtils.stringIsBlank(docClassStr)) {
				
					DocumentClass docClass = 
					 DocumentClass.getCache().getCachedInstance(docClassStr);
					
					if (docClass.isMandate()) {
						mandateDocName = rsBatchItems.getStringValueByName
						 (Globals.UCMFieldNames.DocName);
						Log.debug("Found a mandate/app (doc class: " 
								 + docClass.getName() + ", docName: " 
								 + mandateDocName + ")");
					}
				}
				
			} while (rsBatchItems.next());
		}
		
		// Second pass.
		// If a mandate/app was found in the bundle, set their dDocName against items
		// marked as 'With Instruction' and don't have an xDependantDocName explicitly
		// set.
		if (mandateDocName != null) {
			if (rsBatchItems.first()) {
				do {
					String docClassStr = rsBatchItems.getStringValueByName
					 (Globals.UCMFieldNames.DocClass);
					
					DocumentClass docClass = 
						 DocumentClass.getCache().getCachedInstance(docClassStr);
					
					// Ignore Mandates and Apps
					if (docClass.isMandate())
						continue;
					
					String depDocName = rsBatchItems.getStringValueByName
					 (Globals.UCMFieldNames.DependantDocName);
					
					String withInstrStr = rsBatchItems.getStringValueByName
					 (Globals.UCMFieldNames.WithInstruction);
					
					boolean withInstruction = 
					 (withInstrStr != null && withInstrStr.equals("Yes"));
					
					if (StringUtils.stringIsBlank(depDocName) && withInstruction) {
						String docName = rsBatchItems.getStringValueByName
						 (Globals.UCMFieldNames.DocName);
						
						try {
							LWDocument lwDoc = new LWDocument(docName, true);
							
							if (mandateDocName != null) {
								Log.debug("Setting Dependant DocName value on " + docName 
								 + " to mandate/app: " + mandateDocName); 
								
								lwDoc.setAttribute
								 (Globals.UCMFieldNames.DependantDocName, mandateDocName);
							}
							
							lwDoc.update();
							
						} catch (Exception e) {
							String msg = "Failed to instantiate/update " + docName + 
							 " when setting dependant docName";
							
							Log.error(msg, e);
							throw new ServiceException(msg, e);
						}
					}
				} while (rsBatchItems.next());
			}
		}
	}
	
	/** Checks the passed set of documents for presence of Static Data Update items, 
	 *  e.g. Apps, Mandates.
	 *  
	 *  For each one found, check the xNominatedContactPoint field value - this must
	 *  correspond to the CONTACT_ID of the nominated account correspondent's default
	 *  address contact.
	 *  
	 *  So if a Mandate doc is indexed to Account X, which has nominated correspondent 
	 *  Y, who has default address Z, the xNominatedContactPoint field value must match
	 *  to the CONTACT_ID of address Z.
	 *  
	 * @return Vector of dDocNames for items with a mismatched contact point. An empty
	 * 			Vector indicates no errors.
	 * @throws DataException 
	 * @throws ServiceException 
	 * @throws NumberFormatException 
	 */
	public static Vector<String> verifyNominatedContactPoints
	 (DataResultSet rsBatchItems, FWFacade cdbFacade) 
	 throws DataException, ServiceException {
		
		Vector<String> failedDocNames = new Vector<String>();
		
		if (rsBatchItems.first()) {
			// Cache of Account ID -> Nominated/Default address Contact ID for the 
			// account correspondent
			HashMap<Integer, Integer> accountNominatedContactPoints = 
			 new HashMap<Integer, Integer>();

			do {
				String docName = rsBatchItems.getStringValueByName
				 (UCMFieldNames.DocName);
				String docClass = rsBatchItems.getStringValueByName
				 (UCMFieldNames.DocClass);
				
				// Check for mismatched contact points
				InstructionType instrType = 
				 InstructionType.getNameCache().getCachedInstance(docClass);
					
				if (instrType != null &&
					StaticDataUpdateApplied
					.isInstructionGeneratingStaticDataInstructions(instrType)) {
					// The document must be checked for a matching Contact Point.
					
					// Resolve the account.
					String clientNumStr = rsBatchItems.getStringValueByName
					 (UCMFieldNames.ClientNumber);
					String accountNumStr = rsBatchItems.getStringValueByName
					 (UCMFieldNames.AccountNumber);
								
					if (StringUtils.stringIsBlank(clientNumStr) 
						||
						StringUtils.stringIsBlank(accountNumStr)) {
						// No account data to check against.
						
					} else {
						Account account = Account.get(
						 Account.getAccountByIndexingValues
						 (clientNumStr, accountNumStr, false, cdbFacade));

						if (account != null) {
							Log.debug("Checking account ID " + account.getAccountId() +
							 " nominated corr. address contact ID matches metadata " +
							 "on " + docName);
							
							Integer accountContactPointId = null;
							
							// Check if we cached the contact ID for this 
							// account already!
							if (accountNominatedContactPoints.containsKey
								(account.getAccountId())) {
								
								// Already in cache.
								accountContactPointId = 
								 accountNominatedContactPoints.get
								 (account.getAccountId());
								
								Log.debug("Found nominated contact ID in cache (" +
								 accountContactPointId + ")");
								
							} else {
								Person corr = Account.getNominatedCorrespondent
								 (account.getAccountId(), true, cdbFacade);
								
								Contact defaultAddressContact = null;
								
								if (corr != null) {
									// Determine the default address contact. Must
									// be explicitly marked as the default address.
									defaultAddressContact = 
									 Contact.getDefaultContact(corr.getContacts(),
									 Contact.ADDRESS, true);
								}
								
								if (defaultAddressContact != null) {
									accountContactPointId = 
									 defaultAddressContact.getContactId();
								
									Log.debug("Found nominated contact ID (" +
									 accountContactPointId + ")");
								} else {
									Log.debug
									 ("Unable to find nominated contact ID");
								}
								
								// Add to cache.
								accountNominatedContactPoints.put
								 (account.getAccountId(), accountContactPointId);
							}
							
							// Fetch the selected contact point on the document
							Integer selContactPointId = CCLAUtils
							 .getResultSetIntegerValue(rsBatchItems, 
							 UCMFieldNames.NominatedContactPoint);
							
							Log.debug("Selected contact point ID from doc meta: " +
							 selContactPointId);
									
							if ((selContactPointId == null
								&& accountContactPointId != null)
								||
								(selContactPointId != null
								&& accountContactPointId == null)
								|| 
								(!accountContactPointId.equals(selContactPointId))) {
								// Found a mismatched Contact Point.						
								Log.debug("Contact point IDs did not match!");
								
								failedDocNames.add(docName);
							}
						}
					}
				}
				
			} while (rsBatchItems.next());
		}
		
		return failedDocNames;
	}
	
	/** Service method which checks the passed bundle for invalid contact point data.
	 * 
	 *  Only executes the check for bundles at Validation step.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void verifyNominatedContactPoints() throws DataException, ServiceException {
		String bundleRef = m_binder.getLocal("bundleRef");
	
		if (StringUtils.stringIsBlank(bundleRef))
			throw new ServiceException("No bundle ref present");
		
		Log.debug("Verifying nominated contact points for bundle " + bundleRef);
		
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace);
		
		try {
			// Check the bundle is at Validation step before applying the check.
			LWDocument lwd = BatchDocumentServices.getParentBatchDoc
			 (bundleRef, ucmFacade);
			
			String wfStepName = SppIntegrationUtils.getDocumentWorkflowStepName
			 (lwd.getDocName(), ucmFacade);
			
			if (StringUtils.stringIsBlank(wfStepName) 
				|| !wfStepName.equals("Validation")) {
				Log.debug("Skipping verification - bundle is not at Validation step");
				return;
			}
			
		} catch (Exception e) {
			Log.error("Failed to load bundle doc/WF step name: " + e);
			throw new ServiceException(e);
		}
		
		DataResultSet rsBatchItems = BatchDocumentServices.getBatchItems
		 (bundleRef, ucmFacade);
		
		Vector<String> failedDocNames = 
		 verifyNominatedContactPoints(rsBatchItems, CCLAUtils.getFacade(true));
	
		Log.debug("Number of mismatched contact points: " + failedDocNames.size());
		
		if (failedDocNames.size() > 0) {
			// Just put the first failed item docname on the binder for now.
			m_binder.putLocal("failedDocName", failedDocNames.get(0));
		}
	}
	
	/** Called by SPP via UCM web service.
	 * 
	 *  The only required parameter is GUID. This should be a reference to a DOC_GUID,
	 *  i.e. 'CCLA_0001234:1', although a dDocName value on its own will also be
	 *  accepted i.e. 'CCLA_0001234'
	 *  
	 *  The corresponding document is loaded and all attributes are copied into a 
	 *  new ChildDocument content item.
	 *  
	 *  The envelope bundle is reset via a checkout/checkin action to force the newly-
	 *  generated document through indexing/validation.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	@SuppressWarnings("unchecked")
	public void cloneDocumentAndResetBundle() throws ServiceException, DataException {
		
		String guid = CCLAUtils.getBinderStringValue(m_binder, "GUID");
		String docName = null;
		
		if (!StringUtils.stringIsBlank(guid)) {	
			String[] values = guid.split(":");
			if (values.length==1) {
				// No colon char, so we'll assume this is a dDocName value
				docName = values[0];
			} else if (values.length>1) {
				// Assume we've been passed a fully-qualified Doc GUID.
				docName = values[0];
				String revisionStr = values[1];
				try {
					Integer revisionId = Integer.parseInt(revisionStr);
				} catch (NumberFormatException nfe) {
					throw new DataException("RevisionId is not known");
				}
			}
		} else {
			throw new ServiceException("Unable to clone and submit document to SPP: " +
			 "no Document Identifier specified (GUID)");
		}
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
			
			// Load an instance of the document to be cloned, and pull out all its
			// attributes.
			LWDocument lwDoc = null;
			
			try {
				lwDoc = new LWDocument(docName, true);
			} catch (Exception e) {
				String msg = "failed to instantiate content item with dDocName: "
				 + docName;
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}
			
			String bundleRef = lwDoc.getAttribute(UCMFieldNames.BundleRef);
			String pdfDocName = lwDoc.getDocName();
			
			if (StringUtils.stringIsBlank(bundleRef)) {
				// Item to clone doesn't have an Iris bundle ref.
				// For now, throw an error in this case and prevent the cloning taking
				// place.
				String msg = "no Bundle/Envelope Ref set";
				
				Log.error(msg);
				throw new ServiceException(msg);
				
				// TODO: assign a new bundleRef to the original item, using its Source
				// value. Below code should do it.
				
				//String source = lwDoc.getAttribute(UCMFieldNames.Source);
				//bundleRef = BundleServices.getNewBundleRef(source, facade)
				//lwDoc.setAttribute(UCMFieldNames.BundleRef, bundleRef)
				//lwDoc.update();
			}
			
			// Load an instance of the parent bundle item, which must be reset after
			// the cloned item is checked in.
			LWDocument bundleDoc = BatchDocumentServices.getParentBatchDoc
			 (bundleRef, facade);
			
			if (lwDoc.getAttribute(UCMFieldNames.DocType)
				.equals(Globals.UCMDocTypes.ChildDocument)) {
				// Original is a child document - attempt to resolve parent PDF
				// content.
				String origPdfDocName = lwDoc.getAttribute(UCMFieldNames.PdfDocName);
				
				if (StringUtils.stringIsBlank(origPdfDocName)) {
					pdfDocName = lwDoc.getAttribute(UCMFieldNames.ParentDocName);
				} else {
					pdfDocName = origPdfDocName;
				}
			}
			
			Hashtable<String, String> attribs = lwDoc.getAttributes();
			
			// Remove attributes which may break/invalidate the new checkin
			String[] stripFields;
			
			Log.debug("Preparing to clone document " + lwDoc.getDocName());

			if (SppIntegrationServices.KEEP_ORIGINAL_DATES_FOR_CLONE_DOCUMENTS) {
				stripFields = new String[] {
					"dID", "dDocName", "dRevLabel", "dStatus", 
					"xWorkflowDate", "xJobId",
					"xStatus", 
					"xProcessDate", "xOriginalProcessDate",
					"xMultiDoc",
					"xSignaturesValid"
				};				
			} else {
				stripFields = new String[] {
					"dID", "dDocName", "dRevLabel", "dStatus", 
					"dReleaseDate", "dInDate", "dCreateDate",
					"xWorkflowDate", "xJobId", 
					"xStatus", 
					"xProcessDate", "xOriginalProcessDate",
					"xMultiDoc",
					"xSignaturesValid"
				};
			}
			
			for (String stripField : stripFields) {
				if (attribs.containsKey(stripField))
					attribs.remove(stripField);
			}
			
			LWDocument clonedDoc = new LWDocument();
			clonedDoc.setAttributes(attribs);
			
			SPPJobProfile wfProfile = SPPJobProfile.JOB_PROFILE;
			
			for (int i=0; i<wfProfile.getSppVarNames().length; i++) {
				String wfVarName = wfProfile.getSppVarNames()[i];
				String wfVarValue = m_binder.getLocal(wfVarName);
				
				if (wfVarValue != null) {
					String ucmVarName = wfProfile.getUcmVarNames()[i];
					
					Log.debug("Found a passed SPP var " + wfVarName + " with value '" +
					 wfVarValue + "'. Converting to UCM var: " + ucmVarName);
					
					clonedDoc.setAttribute(ucmVarName, wfVarValue);
				}
			}
			
			// Cloned documents are ALWAYS ChildDocuments. They don't have their
			// own content.
			clonedDoc.setDocType("ChildDocument");
			
			// Set parent/PDF doc attributes
			clonedDoc.setAttribute(UCMFieldNames.ParentDocName, lwDoc.getDocName());
			clonedDoc.setAttribute(UCMFieldNames.PdfDocName, pdfDocName);
			clonedDoc.setAttribute(UCMFieldNames.Status, "Pending");

			String newDocName = clonedDoc.checkin(null, true);
			
			Log.debug("Checked in clone of document " + lwDoc.getDocName() + 
			 " as " + newDocName);

			// Add audit note to bundle.
			if (!StringUtils.stringIsBlank(bundleRef)) {
				// Add audit notes to bundle item
				Vector<String> params = new Vector<String>();
				params.add(lwDoc.getDocName());
				params.add(newDocName);
				
				AuditUtils.addAuditEntry("IRIS", "SPP-CLONE-DOC", 
				 bundleDoc.getDocName(), 
				 bundleDoc.getTitle(), 
				 m_userData.m_name,
				 "Document " + lwDoc.getDocName() + " cloned to " + newDocName,
				 params);
			}
			
			// Reset the previous bundle, via a checkout-checkin, if it isn't in
			// the CCLA_MailHandling workflow.
			if (!StringUtils.stringIsBlank(bundleRef)) {
				// Check the bundle has completed the CCLA_MailHandling workflow, or
				// at the Validation step - this will require a check-out/check-in to
				// reset the bundle back to EnterDetails step.
				String wfStepName = SppIntegrationUtils.getDocumentWorkflowStepName
				 (bundleDoc.getDocName(), facade);
				
				if (wfStepName == null) {
					Log.debug("Checking out/checking in parent bundle item " 
					 + bundleRef + ", docName: " + bundleDoc.getDocName());
					
					bundleDoc.checkout();
					
					// Use special check-in method which prevents release
					// date being updated.
					bundleDoc.checkin(null, null, true, false);
					
					Log.debug("Checkout/checkin complete.");
				} else {
					Log.debug("Skipping checkout/checkin of parent bundle item, still "+
					 "in workflow");
				}
				
				// Recalculate bundle priority
				BundlePriorityUtils.updateBundlePriority(bundleDoc, 
				 BatchDocumentServices.getBatchItems(bundleRef, facade));
			}

		} catch (Exception e) {
			throw new ServiceException("Unable to clone document " + docName + ": " +
			 e.getMessage(), e);
		}
	}
	
	/** Performs a metadata update on the content items matched by the passed query 
	 *  string.
	 *  
	 *  No field values are explicitly updated, but update hooks/filters will be triggered
	 *  for all matched items.
	 *  
	 *  Requires a non-null dDocName attribute in every returned row.
	 * @throws DataException 
	 * 
	 */
	public void tapDocs() throws DataException {
		String sql = m_binder.getLocal("queryText");
		
		if (StringUtils.stringIsBlank(sql))
			throw new DataException("queryText is missing");
		
		Log.debug("Tapping docs matching queryText: " + sql);
		
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace, false);
		DataResultSet rsDocs = ucmFacade.createResultSetSQL(sql);
		
		Log.debug("Query returned " + rsDocs.getNumRows() + " rows");
		int counter = 0, success = 0;
		
		if (rsDocs.first()) {
			do {
				String docName = rsDocs.getStringValueByName(UCMFieldNames.DocName);
				counter++;
				
				Log.debug("Document " + counter + "/" + rsDocs.getNumRows() + ": " 
				 + docName);
				
				try {
					LWDocument lwDoc = new LWDocument(docName, true);
					lwDoc.update();
					
					Log.debug("Document updated");
					
					success++;
					
				} catch (Exception e) {
					Log.error("Failed update on item: " + docName + ", "
					 + e.getMessage(), e);
				}
				
			} while (rsDocs.next());
		}
		
		Log.debug("Successfully updated " + success + "/" 
		 + rsDocs.getNumRows() + " documents");
	}
	
	/** Checks out/checks any content items matched by the passed query string.
	 * @throws DataException 
	 * 
	 */
	public void checkOutCheckinDocs() throws DataException {
		
		String sql = m_binder.getLocal("queryText");
		
		if (StringUtils.stringIsBlank(sql))
			throw new DataException("queryText is missing");
		
		Log.debug("Checking out/checking in docs matching queryText: " + sql);
		
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace, false);
		DataResultSet rsDocs = ucmFacade.createResultSetSQL(sql);
		
		Log.debug("Query returned " + rsDocs.getNumRows() + " rows");
		int counter = 0, success = 0;
		
		if (rsDocs.first()) {
			do {
				String docName = rsDocs.getStringValueByName(UCMFieldNames.DocName);
				counter++;
				
				Log.debug("Document " + counter + "/" + rsDocs.getNumRows() + ": " 
				 + docName);
				
				try {
					LWDocument lwDoc = new LWDocument(docName, true);
					
					lwDoc.checkout();
					
					lwDoc.setAttribute("createPrimaryMetaFile", "1");
					lwDoc.checkin(null);
					
					Log.debug("Success");
					success++;
					
					// Add audit note.
					Vector<String> params = new Vector<String>();
					params.add(docName);
					
					AuditUtils.addAuditEntry("IRIS", "CHECKOUT-CHECKIN", 
					 docName, 
					 lwDoc.getTitle(), 
					 m_userData.m_name,
					 "Document checked out/checked in",
					 params);
					
				} catch (Exception e) {
					Log.error("Failed checkout/checkin on item: " + docName + ", "
					 + e.getMessage(), e);
				}
				
			} while (rsDocs.next());
		}
		
		Log.debug("Successfully checked out/checked in " + success + "/" 
		 + rsDocs.getNumRows() + " documents");
	}
	
	/** Used for testing handling of dates via LWFacade/LWDocument instances.
	 * @throws Exception 
	 * 
	 */
	public void testDateHandling() throws Exception {
		
		String docName = m_binder.getLocal(UCMFieldNames.DocName);
		LWDocument lwDoc = new LWDocument(docName, true);
		
		String dateField = m_binder.getLocal("dateField");
		String dateValue = m_binder.getLocal("dateStr");
		
		Log.debug("Instantiated LWDocument instance for " + lwDoc.getDocName());
		Log.debug("Params: dateField=" + dateField, ", dateValue=" + dateValue);
		
		if (!StringUtils.stringIsBlank(dateField)) {
			String dateFieldValue = lwDoc.getAttribute(dateField);
			Log.debug("Current date field (raw) value: " + dateFieldValue);
			
			Date date = DateFormatter.getSystemSimpleDateFormat().parse(dateFieldValue);
			Log.debug("Current date field Date value: " + date.toString());
			
			if (dateValue != null) {
				Log.debug("Updating date field Date value to " + dateValue);
				lwDoc.setAttribute(dateField, dateValue);
			}
			
			Log.debug("Running update call.");
			
			lwDoc.update();
			
			// Reload 'just to be sure'
			lwDoc = new LWDocument(docName, true);
			
			dateFieldValue = lwDoc.getAttribute(dateField);
			Log.debug("Post-update date field (raw) value: " + dateFieldValue);
			
			date = DateFormatter.getSystemSimpleDateFormat().parse(dateFieldValue);
			Log.debug("Post-update date field Date value: " + date.toString());
		}
	}
	
	/** Used for testing concurrent document updates. Allows up to 10 concurrent updates
	 *  to be dispatched on separate documents, with differing configurations.
	 * 
	 * @throws DataException 
	 * 
	 */
	public void testDocUpdates() throws DataException {
		
		Vector<DocUpdateThread> docUpdateThreads = new Vector<DocUpdateThread>();
		
		for (int i=0; i<10; i++) {
			String docName = m_binder.getLocal("docName" + i);
			
			if (StringUtils.stringIsBlank(docName))
				continue;
			
			Integer delay = CCLAUtils.getBinderIntegerValue
			 (m_binder, "docName" + i + "_delay");
			
			boolean noTransaction = CCLAUtils.getBinderBoolValue
			 (m_binder, "docName" + i + "_noTransaction");
			
			DocUpdateThread docThread = 
			 new DocUpdateThread(docName, delay, noTransaction);
			
			docUpdateThreads.add(docThread);
		}
		
		Log.debug("Testing doc updates. Collected info for " + docUpdateThreads.size() +
		 " doc updates");
		
		for (DocUpdateThread updateThread : docUpdateThreads) {
			updateThread.start();
		}
	}
	
	/** Thread subclass used for testing document updates.
	 * 
	 * @author Tom
	 *
	 */
	public static class DocUpdateThread extends Thread {
		
		private String docName;
		private Integer delayPeriod;
		private boolean noTransaction;
		
		public DocUpdateThread
		 (String docName, Integer delayPeriod, boolean noTransaction) {
			this.setName(docName + "-UpdateThread");
			
			this.docName = docName;
			this.delayPeriod = delayPeriod;
			this.noTransaction = noTransaction;
		}
		
		public void run() {

			try {
				Log.debug("Starting update thread for document " + docName + ". Delay: " 
				 + this.delayPeriod + ", No Transaction: " + noTransaction);
				
				LWDocument lwDoc = new LWDocument(docName, true);
				
				if (noTransaction)
					lwDoc.setUseTransaction(false);
				
				if (delayPeriod != null) {
					lwDoc.setAttribute("updateDelayMillis", delayPeriod.toString());
				}
				
				lwDoc.update();
				
				Log.debug("Completed update thread for document " + docName);
				
			} catch (Exception e) {
				Log.error("Failed to update document " + docName + ": " 
				 + e.getMessage(), e);
			}
		} 
	}
}
