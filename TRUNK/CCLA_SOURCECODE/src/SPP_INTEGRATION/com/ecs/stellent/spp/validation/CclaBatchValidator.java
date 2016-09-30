package com.ecs.stellent.spp.validation;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.Workspace;

import java.util.Vector;

import com.ecs.stellent.iris.DocumentValidator;
import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.stellent.iris.batch.BatchValidator;
import com.ecs.stellent.iris.definition.FieldSpec;
import com.ecs.stellent.iris.definition.IrisProfile;
import com.ecs.stellent.iris.definition.XMLProfileReader;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CclaBatchValidator implements DocumentValidator {

	// Special error flag if the bundle has a status of 'Flagged'
	protected static final String BUNDLE_FLAGGED = "bundleFlagged";
	
	// Special error flag if the bundle has a document with missing account
	// data. This check isn't performed on all documents.
	protected static final String 
	 REQUIRED_BATCH_ITEM_ACCOUNT_FIELDS_MISSING = 
	 "requiredBatchItemAccountFieldsMissing";
	
	// Special error flag if the bundle has a document with mismatched form validity
	// flag and doc class set. Only relevant for forms with Form ID set.
	protected static final String BATCH_ITEM_FORM_STATE_MISMATCH = 
	 "batchItemFormStateMismatch";

	public boolean validate(String docName, DataBinder binder,
	 Workspace workspace) {
		
		// First check for the workflow action in the binder. If this is a Reject
		// action, no validation is required, so return true.
		String action = binder.getLocal("action");
		if (!StringUtils.stringIsBlank(action) && action.equals("Reject"))
			return true;
		
		DataResultSet batchItems;
		
		try {
			LWDocument batchDoc = new LWDocument(docName, true);
			
			String batchRef = batchDoc.getAttribute(UCMFieldNames.BundleRef);
			Log.debug("Validating CCLA bundle: " + batchRef);
			
			// Check if the bundle is currently 'Flagged' - 
			// prevent the submission in this case.
			String status		= batchDoc.getAttribute("xStatus");
			
			if (!StringUtils.stringIsBlank(status)) {
				if (status.equals("Flagged")) {
					binder.putLocal(BUNDLE_FLAGGED, "1");
					
					binder.putLocal(VALIDATION_FAILED, "1");
					return false;
				}
			}
			
			// Fetch all batch items.
			
			// Ensure the returned ResultSet uses the latest pending
			// Dual Index data where applicable.
			FWFacade ucmFacade = CCLAUtils.getFacade(workspace,false);
			batchItems = BatchDocumentServices.getBatchItems
			 (batchRef, ucmFacade, false, true);
			
			FWFacade cdbFacade = CCLAUtils.getFacade(true);
			
			// Generate ResultSet of batch items with required
			// fields missing.
			DataResultSet rsBatchItemsMissingReqFields = 
			 new DataResultSet(new String[] {
			  UCMFieldNames.DocName, 
			  UCMFieldNames.DocClass
			 }
			);
			
			// Check for:
			// -missing required fields
			if (batchItems.first()) {

				IrisProfile irisProfile = XMLProfileReader.loadProfile
				 ("CCLA", binder, workspace);
				
				do {
					String itemDocName =
					 batchItems.getStringValueByName(UCMFieldNames.DocName);

					String itemDocClass = 
					 batchItems.getStringValueByName(UCMFieldNames.DocClass);
					
					// Each doc in the bundle may map to a different field specification 
					// in the profile, depending on its metadata.
					FieldSpec fieldSpec = XMLProfileReader.resolveFieldSpecForDocument
					 (batchItems, irisProfile);
					
					String[] reqFields = fieldSpec.getRequired().split(",");
					boolean missingReqField = false;
					
					for (String reqField : reqFields) {
						if (StringUtils.stringIsBlank
							(batchItems.getStringValueByName(reqField))) {
							// Missing req. field found.
							missingReqField = true;
							break;
						}
					}
					
					if (missingReqField) {
						Vector<String> v = new Vector<String>();
						
						v.add(itemDocName);
						v.add(itemDocClass);
						
						rsBatchItemsMissingReqFields.addRow(v);
					}
					
					
				} while (batchItems.next());
			}
			
			binder.addResultSet("rsBatchItemsMissingReqFields",
			 rsBatchItemsMissingReqFields);
			
			boolean reqFieldsMissing = false;
			
			if (!rsBatchItemsMissingReqFields.isEmpty())
				reqFieldsMissing = true;
			
			if (reqFieldsMissing) {
				binder.putLocal(
				 BatchValidator.REQUIRED_BATCH_ITEM_FIELDS_MISSING, "1");
				
				binder.putLocal(VALIDATION_FAILED, "1");
				return false;
			}
			
			// Generate ResultSet of batch items with mismatched form state.
			DataResultSet rsBatchItemsMismatchedFormState = 
			 new DataResultSet(new String[] {
			  UCMFieldNames.DocName, 
			  UCMFieldNames.DocClass
			 }
			);
			
			// Check for items with Form ID set.
			if (batchItems.first()) {
				do {
					String formIdStr =
					 batchItems.getStringValueByName(UCMFieldNames.FormID);

					if (!StringUtils.stringIsBlank(formIdStr)) {
						Log.debug("Found bundle item with Form ID: " + formIdStr);
						Form frm = null;
						
						try {
							Integer formId = Integer.parseInt(formIdStr);
							frm = Form.get(formId, cdbFacade);
						} catch (NumberFormatException e) {
							throw new DataException("Expected numeric Form ID, found "
							 + formIdStr);
						}
						
						if (frm == null) {
							throw new DataException("No record found for Form ID "
							 + formIdStr);
						}
						
						// Can't really check the validity of a non-singleton form.
						if (!frm.getFormType().isSingleton()) {
							Log.debug("Not a singleton form type - ignoring");
							continue;
						}
						
						boolean mismatchedState = false;
						
						Boolean isValid = frm.isValid();
						
						String itemDocClass = 
						 batchItems.getStringValueByName(UCMFieldNames.DocClass);
						
						if (isValid != null 
							&& !isValid
							&& !itemDocClass.equals(DocumentClass.Classes.CONDINS)) {
							// Found a form explicitly marked as invalid, but not set
							// as CONDINS.
							Log.debug("Form is marked invalid but not CONDINS");
							mismatchedState = true;
						} else if ((isValid == null || isValid)
							&&
							itemDocClass.equals(DocumentClass.Classes.CONDINS)) {
							// Found a form NOT marked as invalid, but set as CONDINS.
							mismatchedState = true;
							Log.debug("Form is marked valid but set as CONDINS");
						}
						
						if (mismatchedState) {
							Vector<String> v = new Vector<String>();
							
							String itemDocName =
							 batchItems.getStringValueByName(UCMFieldNames.DocName);

							v.add(itemDocName);
							v.add(itemDocClass);
							
							rsBatchItemsMismatchedFormState.addRow(v);
						}
					}
				} while (batchItems.next());
			}
			
			binder.addResultSet("rsBatchItemsMismatchedFormState",
			 rsBatchItemsMismatchedFormState);
		
			if (!rsBatchItemsMismatchedFormState.isEmpty()) {
				binder.putLocal(
				 CclaBatchValidator.BATCH_ITEM_FORM_STATE_MISMATCH, "1");
				
				binder.putLocal(VALIDATION_FAILED, "1");
				return false;
			}
			
			// All checks completed.
			Log.debug("Validation passed.");
			
			binder.putLocal(VALIDATION_PASSED, "1");
			return true;
			
		} catch (Exception e) {
			Log.error("Error during batch validation - bundle item:" + docName, e);
			
			binder.putLocal(VALIDATION_FAILED, "1");
			binder.putLocal(VALIDATION_ERROR_MESSAGE, e.getMessage());
			return false;
		}
	}

}
