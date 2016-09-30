package com.ecs.ucm.ccla.commproc;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;

import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class ArchiveService extends Service {

	public void doArchiveDocs() throws ServiceException, DataException {
		Date startDate = CCLAUtils.getBinderDateValue(m_binder, "startDate");
		Date endDate = CCLAUtils.getBinderDateValue(m_binder, "endDate");
		
		if (startDate==null || endDate==null) {
			throw new DataException("Cannot archive, either start/end Date is null");
		}
		
		Log.debug("startDate: "+startDate.toString());
		Log.debug("endDate: "+endDate.toString());
		
		
		String query = "select * from docmeta dm "+
		"inner join revisions r on (dm.did=r.did) "+
		"where r.drevrank=0 and r.dindate between "+
		"to_date('"+DateUtils.formatddsMMsyyyy(startDate)+"','dd/MM/yyyy') and "+
		"to_date('"+DateUtils.formatddsMMsyyyy(endDate)+"','dd/MM/yyyy') and "+
		"((dm.xStorageRule is null and dm.xFolderName is null) or dm.xStorageRule <> '"+ Globals.STORAGE_RULE_NAME+"') ";
		
		String docType = m_binder.getLocal("docType");
		if (!StringUtils.stringIsBlank(docType)) {
			query+=" and r.dDocType='"+docType+"'";
		} else {
			query+=" and r.dDocType<>'IrisProfile'";
		}		
		
		Log.debug("ArchiveQuery: "+query);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);				
		DataResultSet rs =facade.createResultSetSQL(query);
		
		if (rs!=null && !rs.isEmpty()) 
		{
			if (rs.getNumRows()>20000) {
				m_binder.putLocal("processMessage", "Found "+rs.getNumRows()+", please limit the search size");
			} else {
				int count=0;
				do {
					try {
						int dId = CCLAUtils.getResultSetIntegerValue(rs,UCMFieldNames.DocID);
						Date dInDate = rs.getDateValueByName(UCMFieldNames.DocInDate);
						
						Log.debug("Archiving: did:"+dId+", dindate:"+dInDate.toString()+", folder:"+DateUtils.formatyyyyMM(dInDate));
						
						LWDocument lwDoc = new LWDocument(dId, true);
						lwDoc.setAttribute(UCMFieldNames.FolderName, DateUtils.formatyyyyMM(dInDate));
						lwDoc.setAttribute(UCMFieldNames.StorageRule, Globals.STORAGE_RULE_NAME);
						lwDoc.update();
						count++;
					} catch (Exception e) {
						throw new ServiceException("Cannot archive document: "+e.getMessage(), e);
					}
				} while (rs.next());
				m_binder.putLocal("processMessage", "Processed "+count+" documents");				
			}
		} else {
			m_binder.putLocal("processMessage", "No results found!");
		}
		
		String message = m_binder.getLocal("processMessage");
		Log.debug("Process Message: "+message);
		CCLAUtils.addQueryDateParamToBinder(m_binder, "startDate", startDate);
		CCLAUtils.addQueryDateParamToBinder(m_binder, "endDate", endDate);
		CCLAUtils.addQueryParamToBinder(m_binder, "docType", docType);
	}
}
