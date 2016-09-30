package com.ecs.stellent.ccla.clientservices;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.HelpMapping;
import com.ecs.ucm.ccla.utils.DocumentUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class HelpService extends Service {
	
	/**
	 * Landing page data.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void helpAdminLanding() throws DataException, ServiceException {
		
		FWFacade cdbFacade = CCLAUtils.getFacade(m_workspace, true);
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace, false);
		
		DataBinder binder = new DataBinder();
		DataResultSet rsDocClasses = ucmFacade.createResultSet("QDocClasses", binder);
		DataResultSet rsHelpDocs = ucmFacade.createResultSet("QSystemDocument", binder);
		DataResultSet rsHelp = HelpMapping.getAllData(cdbFacade);
		
		m_binder.addResultSet("rsDocClasses", rsDocClasses);
		m_binder.addResultSet("rsHelp", rsHelp);
		m_binder.addResultSet("rsHelpDocs", rsHelpDocs);
	}
	
	/**
	 * Update help reference
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void updateHelpReference() throws DataException, ServiceException {
		String helpCode = m_binder.getLocal(HelpMapping.Cols.HELP_CODE);		
		Integer pageNumber = CCLAUtils.getBinderIntegerValue(m_binder,HelpMapping.Cols.PAGE_NUMBER);
		String docname = m_binder.getLocal(HelpMapping.Cols.DOCNAME);
		
		if (StringUtils.stringIsBlank(docname) || StringUtils.stringIsBlank(helpCode)) {
			Log.error("Cannot update HelpMapping, missing docName or helpCode");
			throw new ServiceException("Cannot update HelpMapping, missing docName or helpCode");			
		}
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			HelpMapping mapping = HelpMapping.get(helpCode, facade);
			
			if (mapping==null) {
				Log.error("Cannot update, cannot find helpMapping with helpCode:"+helpCode);
				throw new ServiceException("Cannot update, cannot find helpMapping with helpCode:"+helpCode);
			}
			
			mapping.setDocname(docname);
			mapping.setPageNumber(pageNumber);
			mapping.persist(facade, m_userData.m_name);
			
			//refresh the cache
			HelpMapping.getCache().buildCache(facade);
			
		} catch (Exception de) {
			Log.error("Error updating helpMapping, "+de.getMessage(), de);
			throw new ServiceException("Error updating helpMapping, "+de.getMessage(), de);
		}
	}
	
	/**
	 * Remove a help reference
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void removeHelpReference() throws DataException, ServiceException {
		String helpCode = m_binder.getLocal(HelpMapping.Cols.HELP_CODE);		
		
		if (StringUtils.stringIsBlank(helpCode)) {
			Log.error("Cannot delete HelpMapping, missing helpCode");
			throw new ServiceException("Cannot delete HelpMapping, missing helpCode");			
		}
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			HelpMapping mapping = HelpMapping.get(helpCode, facade);
			
			if (mapping!=null) {
				HelpMapping.delete(helpCode, facade);
			}
			
			//refresh the cache
			HelpMapping.getCache().buildCache(facade);
			
		} catch (Exception de) {
			Log.error("Error removing helpMapping, "+de.getMessage(), de);
			throw new ServiceException("Error removing helpMapping, "+de.getMessage(), de);
		}
	}
	
	/**
	 * Add a help reference
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addHelpReference() throws DataException, ServiceException {
		String helpCode = m_binder.getLocal(HelpMapping.Cols.HELP_CODE);		
		Integer pageNumber = CCLAUtils.getBinderIntegerValue(m_binder,HelpMapping.Cols.PAGE_NUMBER);
		String docname = m_binder.getLocal(HelpMapping.Cols.DOCNAME);
		
		if (StringUtils.stringIsBlank(docname) || StringUtils.stringIsBlank(helpCode)) {
			Log.error("Cannot add HelpMapping, missing docName or helpCode");
			throw new ServiceException("Cannot add HelpMapping, missing docName or helpCode");			
		}
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			HelpMapping mapping = HelpMapping.get(helpCode, facade);
			
			if (mapping!=null) {
				Log.error("Cannot add, helpMapping already exist with helpCode:"+helpCode);
				throw new ServiceException("Cannot add, helpMapping already exist with helpCode:"+helpCode);
			} else {
				mapping = new HelpMapping(helpCode, docname, pageNumber);
				HelpMapping.add(mapping, facade);
			}
			
			//refresh the cache
			HelpMapping.getCache().buildCache(facade);
			
		} catch (Exception de) {
			Log.error("Error adding helpMapping, "+de.getMessage(), de);
			throw new ServiceException("Error adding helpMapping, "+de.getMessage(), de);
		}
	}
	
	/**
	 * Constructs the url 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getHelpUrl() throws DataException, ServiceException {
		
		String helpCode = m_binder.getLocal(HelpMapping.Cols.HELP_CODE);
		
		if (!StringUtils.stringIsBlank(helpCode)) {
			HelpMapping helpMapping = HelpMapping.getCache().getCachedInstance(helpCode);
			if (helpMapping!=null) {
				try {
					LWDocument lwDoc = new LWDocument(helpMapping.getDocname());
					String dSecurityGroup = lwDoc.getAttribute(Globals.UCMFieldNames.SecurityGroup);
					String dDocAccount = lwDoc.getAttribute(Globals.UCMFieldNames.DocAccount);
					String dDocType = lwDoc.getAttribute(Globals.UCMFieldNames.DocType);
					String dDocName = lwDoc.getAttribute(Globals.UCMFieldNames.DocName);
					String parentDocName = lwDoc.getAttribute(Globals.UCMFieldNames.ParentDocName);
					String pdfDocName = lwDoc.getAttribute(Globals.UCMFieldNames.PdfDocName);
					String folderName = lwDoc.getAttribute(Globals.UCMFieldNames.FolderName);
					
					FWFacade facade = CCLAUtils.getFacade(m_workspace, false);
					String helpDocUrl = DocumentUtils.generateDocUrl(dSecurityGroup, dDocAccount, dDocType, dDocName, parentDocName, pdfDocName, folderName, facade);
					if (helpMapping.getPageNumber()!=null) {
						helpDocUrl+="#Page="+helpMapping.getPageNumber();
					}
					
					Log.debug("HelpDocumentUrl: "+helpDocUrl);
					m_binder.putLocal("helpDocUrl", helpDocUrl);
					
				} catch (Exception e) {
					Log.error("Cannot getHelpDocuemntUrl, "+e.getMessage(),e);
					throw new ServiceException("Cannot getHelpDocuemntUrl, "+e.getMessage(),e);
				}
			} else {
				Log.debug("Cannot get HelpDocumentUrl, helpMapping is null for "+helpCode);	
			}
		} else {
			Log.debug("Cannot get HelpDocumentUrl, helpCode is null");	
		}
	}
}
