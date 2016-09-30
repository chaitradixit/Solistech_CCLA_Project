package com.ecs.stellent.ccla;

import java.util.HashSet;
import java.util.Properties;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.ServiceUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import idcbean.data.LWDataBinder;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSet;
import intradoc.server.Service;

public class DocumentClassFixer extends Service {
	
	public void fixDocumentClasses() throws ServiceException, DataException, Exception {
		
		Log.debug("fixDocumentClasses - Begin");
		Log.debug("-----------------------------");
		
		ResultSet rs = m_workspace.createResultSet("qScannedDocs", m_binder);
	
		DataResultSet scannedDocs = new DataResultSet();
		scannedDocs.copy(rs);
		
		int totalItems = scannedDocs.getNumRows();
		
		int brokenItems = 0;
		int fixedItems = 0;
		
		scannedDocs.first();
		
		do {
			Properties thisDoc = scannedDocs.getCurrentRowProps();
			String thisDocClass = (String)thisDoc.get("xDocumentClass");
		
			if (!StringUtils.stringIsBlank(thisDocClass)) {

				int hyphenPos = thisDocClass.indexOf(" -");				
				String fixedDocClass = null;
				
				if (hyphenPos > -1) {
					brokenItems++;
					
					fixedDocClass = thisDocClass.substring(0, hyphenPos);
					
					String docName = thisDoc.getProperty("dDocName");
					
					try {
						LWDocument lwDoc = new LWDocument(docName,true);
						lwDoc.setAttribute("xDocumentClass", fixedDocClass);
						
						lwDoc.update();
						
						Thread.sleep(200);
						
						fixedItems++;
						
						Log.debug("Fixed doc class on item " + docName + " to '" 
						 + fixedDocClass + "' (orig value: '" + thisDocClass + "')");
						
					} catch (Exception e) {
						Log.error("Error attempting to load/update document " + docName 
						 + " (docClass = " + thisDocClass + "): " + e.toString());
					}
				}
			}
			
		} while (scannedDocs.next());
		
		Log.debug("-----------------------------");
		Log.debug("fixDocumentClasses - Summary");
		Log.debug("total items checked - " + totalItems);
		Log.debug("total broken items found - " + brokenItems);
		Log.debug("total items fixed - " + fixedItems);
	}
	
	public void removeDuplicateApps() throws Exception {
		
		String sql = "SELECT * " +
		"FROM Revisions r INNER JOIN DocMeta dm ON (r.dID=dm.dID) " +

		"WHERE r.dRevRank = 0 AND r.dStatus = 'RELEASED' " +
		"AND dm.xSource = 'Swordfish' " +
		"AND dm.xdocumentclass = 'APP'";

		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		DataResultSet rsApps = facade.createResultSetSQL(sql);
		Log.debug("Found " + rsApps.getNumRows() + " Swordfish APPs");
		
		HashSet docSet = new HashSet(4000);
		int numDupes = 0;
		
		do {
			String title = rsApps.getStringValueByName("dDocTitle");
			String clientId = rsApps.getStringValueByName("xClientNumber");
			
			String appId = clientId + "-" + title;
			
			if (docSet.contains(appId)) {
				Log.debug("Found a duplicate APP: " + clientId + ", " + title);
				
				DataBinder binder = new DataBinder();
				
				binder.putLocal("IdcService", "DELETE_REV");
				binder.putLocal("dID", rsApps.getStringValueByName("dID"));
				numDupes++;
				
				try {
					ServiceUtils.callService(binder);
				} catch (Exception e) {
					
				}
					
			} else {
				docSet.add(appId);
			}
		} while (rsApps.next());
		
		Log.debug("Finished duplicate removal: found " + numDupes + " duplicates");
		
	}
	
}
