package com.ecs.ucm.ccla.data.audit;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class SDAuditData {

	public static class Cols {
		public static final String ATTR_NAME = "ATTR_NAME";
		public static final String ATTR_VAL_BEFORE = "ATTR_VAL_BEFORE";
		public static final String ATTR_VAL_AFTER = "ATTR_VAL_AFTER";
	}
	
	/** Parameterized DB query used for fetching a result that requires a list of
	 *  strings as a clause.
	 */
	private static final String GET_LATEST_DATA_FIELD_GROUP_UPDATE_SQL = 
	 "SELECT aud.AUDITID, aud.AUDIT_DATE FROM SDAUDIT aud " +
	 "WHERE aud.AUDITID = (" +
	  "SELECT MAX(aud.AUDITID) FROM SDAUDIT aud " +
	  "INNER JOIN SDAUDIT_RELATIONS rel ON (aud.AUDITID = rel.AUDITID) " +
	  "INNER JOIN SDAUDIT_DATA audData ON (aud.AUDITID = audData.AUDITID) " +
	  "WHERE aud.EVENT = '%1$s' AND rel.RELATIONTYPE = '%2$s' " +
	  "AND rel.relationid = %3$d " +
	  "AND auddata.attr_name IN (%4$s) " +
	 ")";
	
	/** Returns the last time the given field name (must correspond to the DB column name)
	 *  for the given entity ID/type was updated.
	 *  
	 *  Returns null if there is no record of the field being updated
	 *  
	 * @param relationId	primary key of entity
	 * @param eventName		entity name e.g. 'Person'
	 * @param fieldName		field name e.g. 'FULL_NAME'
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Date getLatestDataFieldUpdateDate
	 (int relationId, String eventName, String fieldName, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SDAuditRelation.Cols.RELATIONID, relationId);
		CCLAUtils.addQueryParamToBinder
		 (binder, SDAudit.Cols.EVENT, eventName);
		CCLAUtils.addQueryParamToBinder
		 (binder, SDAuditRelation.Cols.RELATIONTYPE, eventName);
		CCLAUtils.addQueryParamToBinder
		 (binder, SDAuditData.Cols.ATTR_NAME, fieldName);
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetLatestAuditDataFieldUpdate", binder);
		
		if (rs.isEmpty())
			return null;
		else
			return rs.getDateValueByName(SDAudit.Cols.AUDIT_DATE);
	}
	
	/** Returns the last time that any of the given field names (must correspond to the 
	 *  DB column name) for the given entity ID/type was updated.
	 *  
	 *  Returns null if there is no record of the field being updated
	 *  
	 * @param relationId	primary key of entity
	 * @param eventName		entity name e.g. 'Person'
	 * @param fieldName		list of field names e.g. 'FULL_NAME'
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Date getLatestDataFieldGroupUpdateDate
	 (int relationId, String eventName, Vector<String> fieldNames, FWFacade facade) 
	 throws DataException {
		
		String sql = String.format
		 (GET_LATEST_DATA_FIELD_GROUP_UPDATE_SQL, 
		 eventName, eventName, relationId, 
		 CCLAUtils.getCSVFromStringList(fieldNames, false, true));
		
		DataResultSet rs = facade.createResultSetSQL
		 (sql);
		
		if (rs.isEmpty())
			return null;
		else
			return rs.getDateValueByName(SDAudit.Cols.AUDIT_DATE);
	}
	
	public static void main (String[] args) {
		Vector<String> fieldNames = new Vector<String>();
		fieldNames.add("fieldName1");
		fieldNames.add("fieldName2");
		fieldNames.add("fieldName3");
		fieldNames.add("f");
		fieldNames.add(null);
		
		System.out.println(String.format
		 (GET_LATEST_DATA_FIELD_GROUP_UPDATE_SQL, 
		 "EventName", "RelName", 1234, 
		 CCLAUtils.getCSVFromStringList(fieldNames, false, true)));
	}
}
