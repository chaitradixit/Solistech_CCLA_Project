package com.ecs.ucm.ccla.utils;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class DocumentUtils {
	
	/** Forces generated doc URLs to lowercase strings. Fixes issue with URLs generated 
	 * from DocumentUtils.generateDocUrl not resolving on Linux environments, due to 
	 * upper-case dDocName in filename and Public folder.
	 */
	public static final boolean FORCE_GENERATED_DOC_URLS_TO_LOWERCASE = 
	 !StringUtils.stringIsBlank
	 (SharedObjects.getEnvironmentValue("CCLA_forceGeneratedDocURLsToLowerCase"));
	
	/** Stores document metadata field values which determine how the content is
	 *  stored on disk.
	 *  
	 * @author Tom
	 *
	 */
	public static class DocStorageAttributes {
		private String partitionId;
		private String storageRule;
		private String folderName;
		
		public String getPartitionId() {
			return partitionId;
		}
		public String getStorageRule() {
			return storageRule;
		}
		public String getFolderName() {
			return folderName;
		}
		
		public DocStorageAttributes(String partitionId, String storageRule,
		 String folderName) {
			this.partitionId = 
			 StringUtils.stringIsBlank(partitionId) ? null : partitionId;
			this.storageRule = 
			 StringUtils.stringIsBlank(storageRule) ? null : storageRule;
			this.folderName = 
			 StringUtils.stringIsBlank(folderName) ? null : folderName;
		}
	}
	
	/** Constructs the direct URL which points to the web-viewable rendition
	 *  for the given content item.
	 *  
	 * @param dSecurityGroup must always be supplied
	 * @param dDocAccount optional
	 * @param dDocType must always be supplied
	 * @param dDocName must always be supplied
	 * @param parentDocName must be supplied if dDocType='ChildDocument'
	 * @return
	 * @throws DataException
	 */
	public static String generateDocUrl(String dSecurityGroup, String dDocAccount, 
			String dDocType, String dDocName, String parentDocName, String pdfDocName, 
			String folderName, FWFacade ucmFacade) 
		throws DataException {
		
		//If an xPdfDocName field is set on the parent, use that rather than the direct
		//parent's doc name for the pdf
		if(!StringUtils.stringIsBlank(pdfDocName)){
			parentDocName = pdfDocName;
		}
		
		//Create URL to web viewable doc
		String docAccount = "";

		if(!StringUtils.stringIsBlank(dDocAccount)){
			docAccount ="/@" + dDocAccount;
		}

		String folderNameStr = "";
		
		if (!StringUtils.stringIsBlank(folderName)) {
			folderNameStr = folderName + "/";
		}
		
		String httpServerAddress = 
		 SharedObjects.getEnvironmentValue("HttpServerAddress");
		
		String docUrl = "";
		
		if (dDocType.equals("ChildDocument")) {
			// Resolve parent PDF docname
			String actualPdfDocName = !StringUtils.stringIsBlank(pdfDocName) ? 
			 pdfDocName : parentDocName;
			
			if (StringUtils.stringIsBlank(actualPdfDocName))
				throw new DataException("Failed to generate ChildDocument URL: " +
				 "parent document reference was missing");
			
			// Fetch storage attribs for the parent document
			DocStorageAttributes docStorageAttribs = getDocStorageAttributes
			 (actualPdfDocName, ucmFacade);
			
			String parentFolderName = docStorageAttribs.getFolderName();
			
			if (!StringUtils.stringIsBlank(parentFolderName)) {
				String parentFolderNameStr = parentFolderName + "/";
				
				if (!parentFolderNameStr.equalsIgnoreCase(folderNameStr))
					folderNameStr = parentFolderNameStr;
			} else {
				folderNameStr = "";
			}
			
			docUrl = "http://" + httpServerAddress + "/ucm" + "/groups/" + 
			 dSecurityGroup + docAccount + "/documents/"+ folderNameStr + 
			 "document/" + actualPdfDocName + ".pdf";
			
		} else {
			docUrl = "http://" + httpServerAddress + "/ucm" + "/groups/" + 
			 dSecurityGroup + docAccount + "/documents/" + folderNameStr + 
			 dDocType + "/" +  dDocName + ".pdf";
		}
		
		if (FORCE_GENERATED_DOC_URLS_TO_LOWERCASE)
			return docUrl.toLowerCase();
		else
			return docUrl;
	}
	
	/** Fetches the Doc Storage attributes for the given dDocName.
	 * 
	 * @param docName
	 * @param facade UCM DB facade
	 * @return null if the dDocName doesn't exist
	 * @throws DataException
	 */
	public static DocStorageAttributes getDocStorageAttributes
	 (String docName, FWFacade ucmFacade) throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal(UCMFieldNames.DocName, docName);
		
		DataResultSet rs = ucmFacade.createResultSet("qCore_GetDocStorageAttributes",
 		 binder);
		
		if (!rs.first())
			return null;
		
		return new DocStorageAttributes(
			CCLAUtils.getResultSetStringValue(rs, UCMFieldNames.PartitionId),
			CCLAUtils.getResultSetStringValue(rs, UCMFieldNames.StorageRule),
			CCLAUtils.getResultSetStringValue(rs, UCMFieldNames.FolderName)
		);
	}
}
