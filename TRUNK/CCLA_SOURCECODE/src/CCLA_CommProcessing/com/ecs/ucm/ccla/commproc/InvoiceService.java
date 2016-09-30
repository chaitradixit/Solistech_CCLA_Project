package com.ecs.ucm.ccla.commproc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMDocTypes;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

/** Helper service methods for capturing and processing invoices.
 * 
 * @author Tom
 *
 */
public class InvoiceService extends Service {
	
	public void addInvoiceReferenceDataToBinder() throws DataException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		addInvoiceReferenceDataToBinder(m_binder, facade);
	}
	
	/** Adds reference data ResultSets to DataBinder, required to display/index line
	 *  item data.
	 *  
	 *  Includes:
	 *  
	 *  -List of Cost Centres
	 *  -List of Nominal/GL Codes
	 *  -List of Dept/Pro Codes
	 * 
	 * @param binder
	 * @param facade
	 * @throws DataException 
	 */
	public static void addInvoiceReferenceDataToBinder
	 (DataBinder binder, FWFacade facade) throws DataException {
		
		binder.addResultSet("rsGeneralLedgers",
		 facade.createResultSet("qCore_GetAllGeneralLedgers", binder));
		
		binder.addResultSet("rsDeptProjectCodes",
		 facade.createResultSet("qCore_GetAllDeptProjectCodes", binder));
		
		binder.addResultSet("rsCostCentres",
		 facade.createResultSet("qCore_GetAllCostCentres", binder));
	}
	
	/** List of Line Item field names that are required to create an INV child document. 
	 * 
	 */
	public static final String[] REQUIRED_LINE_ITEM_FIELDS;
	
	/** List of Line Item field names, as used in the Add Invoice Lines pop-up form
	 *  in the indexing interface, mapped against their document metadata field names.
	 */
	public static HashMap<String, String> LINE_ITEM_DOCMETA_MAPPING =
	 new HashMap<String, String>();
	
	/** List of metadata fields that are copied from the parent item to child invoices
	 * 
	 */
	public static final String[] LINE_ITEM_INHERITED_DOCMETA;
	
	static {
		String reqLineItemFields = 
		 SharedObjects.getEnvironmentValue
		 ("CCLA_CommProc_Invoices_RequiredLineItemFields");
		
		REQUIRED_LINE_ITEM_FIELDS = reqLineItemFields.split(",");
	
		String[] lineItemFieldMap = 
		 SharedObjects.getEnvironmentValue
		 ("CCLA_CommProc_Invoices_LineItemFieldMap").split(",");
		
		for (String mapEntry : lineItemFieldMap) {
			int splitPos = mapEntry.indexOf(':');
			
			String lineItemField 	= mapEntry.substring(0, splitPos);
			String docMetaField 	= mapEntry.substring(splitPos+1);
			
			LINE_ITEM_DOCMETA_MAPPING.put(lineItemField, docMetaField);
		}
		
		String lineItemInheritedDocMeta = 
		 SharedObjects.getEnvironmentValue
		 ("CCLA_CommProc_Invoices_LineItemInheritedDocMeta");
			
		LINE_ITEM_INHERITED_DOCMETA = lineItemInheritedDocMeta.split(",");
	}
	
	/** Generates a series of INV child documents from the passed Line Item data in
	 *  the binder.
	 * @throws DataException 
	 * @throws Exception 
	 *  
	 */
	public void generateInvoiceChildDocs() throws ServiceException, DataException {
		
		// dDocName of the parent multi-doc.
		String parentDocName = m_binder.getLocal("parentDocName");
		
		if (parentDocName == null)
			throw new ServiceException("Parent DocName missing");

		Integer maxLineItems = CCLAUtils.getBinderIntegerValue
		 (m_binder, "maxLineItems");
		
		if (maxLineItems == null)
			throw new ServiceException("Max Line Items missing");
		
		Log.debug("Generating INV child documents using parent doc: " + parentDocName);
		Log.debug("Line Item Field -> Doc Meta mapping:");
		
		for (Map.Entry<String, String> entry : LINE_ITEM_DOCMETA_MAPPING.entrySet())
			Log.debug(entry.getKey() + "=" + entry.getValue());
		
		Set<String> lineItemFieldNames = LINE_ITEM_DOCMETA_MAPPING.keySet();
		
		String userName = m_userData.m_name;
		
		try {
			LWDocument parentDoc = new LWDocument(parentDocName, true);
			
			// Content dDocName reference
			String pdfDocName = SppIntegrationUtils.getPdfDocName(parentDoc);

			int docCheckinCount = 0;
			Vector<String> lineItemDocNames = new Vector<String>();
			
			/*For each available line item:
			 * 
			 * 1. Check for minimal required field values to create a line item doc.
			 * 2. If sufficient data, build the INV child document
			 * 3. If insufficient, move to the next line item
			 */
			for (int lineItem = 1; lineItem<=maxLineItems; lineItem++) {
				
				// Collect all line item values for this line item.
				HashMap<String, String> lineItemData = new HashMap<String, String>();
				
				for (String lineItemFieldName : lineItemFieldNames) {
					String lineItemFieldValue = m_binder.getLocal
					 (lineItemFieldName + "_" + lineItem);
					
					if (!StringUtils.stringIsBlank(lineItemFieldValue))
						lineItemData.put(lineItemFieldName, lineItemFieldValue);
				}
				
				boolean reqFieldsPresent = true;
				
				// Check for presence of required line item fields.
				for (String reqLineItemField : REQUIRED_LINE_ITEM_FIELDS) {
					if (!lineItemData.containsKey(reqLineItemField)) {
						reqFieldsPresent = false;
						break;
					}
				}
				
				if (!reqFieldsPresent)
					continue;
				
				Log.debug("Found minimal required invoice line data on row " 
				 + lineItem);
				
				// Create a new ChildDocument instance
				LWDocument lineItemDoc = new LWDocument();
				lineItemDoc.useDatabase();
				
				// Child Docs always have doc class INV.
				lineItemDoc.setAttribute
				 (UCMFieldNames.DocClass, DocumentClass.Classes.INV);
				
				// Build the child document title.
				String childDocTitle = parentDocName+"_InvoiceLineItem_"+lineItem;
				lineItemDoc.setAttribute(UCMFieldNames.DocTitle, childDocTitle);
				
				// Parent Doc/Content reference fields
				lineItemDoc.setAttribute(UCMFieldNames.ParentDocName, parentDocName);
				lineItemDoc.setAttribute(UCMFieldNames.PdfDocName, pdfDocName);
				
				// Core metadata fields
				lineItemDoc.setAttribute(UCMFieldNames.DocType, UCMDocTypes.ChildDocument);
				lineItemDoc.setAttribute(UCMFieldNames.DocAuthor, userName);
				lineItemDoc.setAttribute("createAlternateMetaFile", "false");
				lineItemDoc.setAttribute("createPrimaryMetaFile", "true");
				lineItemDoc.setAttribute("primaryFile", "");
				
				// Copy field values that the child documents inherit from the parent 
				// document.
				if (LINE_ITEM_INHERITED_DOCMETA != null) {
					for (String inheritedFieldName : LINE_ITEM_INHERITED_DOCMETA) {
						String parentValue = parentDoc.getAttribute(inheritedFieldName);

						if (!StringUtils.stringIsBlank(parentValue)) {
							lineItemDoc.setAttribute(inheritedFieldName, parentValue);
						}
					}	
				}
				
				// Set custom line item values.
				for (String lineItemFieldName : lineItemFieldNames) {
					String lineItemFieldValue = m_binder.getLocal(lineItemFieldName 
					 + "_" + lineItem);
					
					if (!StringUtils.stringIsBlank(lineItemFieldValue)) {
						String ucmFieldName = LINE_ITEM_DOCMETA_MAPPING.get
						 (lineItemFieldName);
						
						lineItemDoc.setAttribute(ucmFieldName, lineItemFieldValue);
					}
				}
				
				try {
					// Execute the checkin action on the LWDocument instance
					Log.debug("Checking in line item child doc from row: " + lineItem);
					Log.debug(lineItemDoc.dumpAttributes());

					String lineItemDocName = lineItemDoc.checkin(null, false);
					
					// Save a list of all new dDocName values, these are queried at the
					// end to ensure all child documents have completed check-in before
					// continuing.
					lineItemDocNames.add(lineItemDocName);
					
					Log.info("CCLA: Checked in invoice line item child doc: " 
					 + lineItemDocName);
					
					docCheckinCount++;

				} catch(Exception e) {
					String msg = "Unable to create line item: " + lineItem + ": "
					 + e.getMessage();
					
					Log.error(msg, e);
					throw new ServiceException(msg, e);
				}
			}
			
			Log.debug("Generated " + docCheckinCount + " line item documents");
			
			CCLAUtils.appendToBinderParam(m_binder, "RedirectUrl", "&numDocsGenerated=" 
			 + docCheckinCount);
			
			// Audit child doc checkins against bundle.
			Vector<String> params = new Vector<String>();
			params.add(Integer.toString(docCheckinCount));
			
			// Fetch data for parent batch item, for use in auditing
			String bundleRef = parentDoc.getAttribute(UCMFieldNames.BundleRef);
			
			FWFacade facade = CCLAUtils.getFacade(m_workspace);
			
			DataResultSet parentItem = 
			 BatchDocumentServices.getParentBatchItem(bundleRef, facade);
			
			AuditUtils.addAuditEntry("IRIS", "CHECKIN-BUNDLE-CHILD-DOCS", 
			 parentItem.getStringValueByName(UCMFieldNames.DocName), 
			 parentItem.getStringValueByName(UCMFieldNames.DocTitle), 
			 m_userData.m_name,
			 docCheckinCount + 
			 " line item documents created",
			 params);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/** Picks up UCM documents which have an xClientNumber field present, but no
	 *  xOrgAccountCode.
	 *  
	 *  The xOrgAccountCodes are then populated via a lookup on the Client Number.
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void populateMissingOrgAccountCodes() 
	 throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		Log.debug("Populating missing Org Account Codes...");
		
		Integer maxRows = CCLAUtils.getBinderIntegerValue(m_binder, "maxRows");
		boolean testMode = CCLAUtils.getBinderBoolValue(m_binder, "testMode");
		
		if (maxRows == null) {
			throw new ServiceException("Please ensure maxRows is specified");
		}
		
		Log.debug("maxRows = " + maxRows + ", testMode = " + testMode);
		
		DataResultSet rsInvoiceDocs = facade.createResultSet
		 ("qCommProc_GetInvoicesWithMissingOrgAccountCode", m_binder);
		
		Log.debug("Found " + rsInvoiceDocs.getNumRows() + " documents.");
		
		if (testMode)
			return;
		
		int successCount = 0, failCount = 0;
		
		if (rsInvoiceDocs.first()) {
			do {
				String docName = rsInvoiceDocs.getStringValueByName
				 (UCMFieldNames.DocName);
				String clientNumberStr = rsInvoiceDocs.getStringValueByName
				 (UCMFieldNames.ClientNumber);
				
				try {
					Integer clientNumber = Integer.parseInt(clientNumberStr);
					
					DataBinder binder = new DataBinder();
					CCLAUtils.addQueryIntParamToBinder
					 (binder, AuroraClient.Cols.CLIENT_NUMBER, clientNumber);
				
					DataResultSet rsEntities = facade.createResultSet
					 ("qCore_GetEntitiesByClientNumber", binder);
					
					if (rsEntities.isEmpty()) {
						throw new Exception("No mapped Organisation for Client Number");
					}
					
					AuroraClient auroraClient = AuroraClient.get(rsEntities);
					Entity org = Entity.get(auroraClient.getOrganisationId(), facade);
					
					LWDocument lwd = new LWDocument(docName, true);
					lwd.setAttribute
					 (UCMFieldNames.OrgAccountCode, org.getOrgAccountCode());
					
					lwd.update();
					
					successCount++;
				} catch (Exception e) {
					Log.error("Failed to populate Org Code for " + docName + 
					 ", Client Number: " + clientNumberStr, e);
					failCount++;
				}
				
			} while (rsInvoiceDocs.next());
		}
		
		Log.debug("Finished assigning Org Account Codes. Successes: "
		 + successCount + " Failures: " + failCount);
	}
}
