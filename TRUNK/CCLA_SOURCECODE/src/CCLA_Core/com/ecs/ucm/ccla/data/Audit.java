package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/**
 * Provides auditing facility
 * 
 * @author kib
 * 
 */
public class Audit {
	
	public static boolean DEBUG_LOGGING = false;
	
	/** Truncate any audit values which exceed this length. */
	public static final int MAX_AUDIT_VALUE_LENGTH = 256;
	
	public static void debug(String msg) {
		if (DEBUG_LOGGING)
			Log.debug(msg);
	}
	
    /**
     * add an Audit Event to the audit table.
     * 
     * Note: Throws exceptions. Make sure you rollback any current transaction
     * if Data Exception is thrown here.
     * 
     * @param fw
     *            FWFacade
     * @param auditName
     *            Name of the audit tables in use
     * @param userid
     *            UserID of person being audited
     * @param actionName
     *            Current Action
     * @param eventName
     *            Current Event
     * @param beforeData
     *            DataResultSet containing data before change
     * @param afterData
     *            DataResultSet containing data after change
     * @param subjectId
     *            Primary Key of the thing being audited (eg event id)
     * @param relations
     *            Hashmap of ids
     * 
     * @throws DataException
     */

    public static void addAudit(FWFacade fw, String auditName, 
    	String userid, String actionName, String eventName,
	    DataResultSet beforeData, 
	    DataResultSet afterData, 
	    HashMap<Integer, String> relations) throws DataException {

	if (auditName == null) {
	    throw new DataException("ERROR: Null Audit Name passed");
	}

	if (beforeData != null && beforeData.getNumRows() > 1) {
	    Log.error("Audit: cannot audit more than one row");
	    throw new DataException("Audit: cannot audit more than one row");
	}

	if (afterData != null && afterData.getNumRows() > 1) {
	    Log.error("Audit: cannot audit more than one row");
	    throw new DataException("Audit: cannot audit more than one row");
	}

	int auditId = getNextAuditId(fw, auditName);

	StringBuffer masterQuery = new StringBuffer();

	masterQuery.append("INSERT INTO " + auditName);
	masterQuery.append(" ( ");
	masterQuery.append("AUDITID,");
	masterQuery.append("USERID,");
	masterQuery.append("AUDIT_DATE,");
	masterQuery.append("EVENT,");
	masterQuery.append("ACTION");
	masterQuery.append(" ) ");
	masterQuery.append("VALUES");
	masterQuery.append(" ( ");
	masterQuery.append("'").append(auditId).append("',");
	masterQuery.append("'").append(CCLAUtils.escapeForOracle(userid)).append("',");
	masterQuery.append("SYSDATE,");
	masterQuery.append("'").append(eventName).append("',");
	masterQuery.append("'").append(actionName).append("'");
	masterQuery.append(" ) ");

	debug("Auditing Event " + eventName + " with action " + actionName + " for user " + userid);
	debug("Master audit query :" + masterQuery.toString());
	fw.executeSQL(masterQuery.toString());

	insertAuditData(fw, auditName, auditId, beforeData, afterData);

	insertAuditRelations(fw, auditName, auditId, relations);

    }

    private static void insertAuditData(FWFacade fw, String auditName, int auditId, DataResultSet beforeData,
	    DataResultSet afterData) throws DataException {

	HashMap<String, String> hmAfterdata = getHashMapFromResultSetRow(afterData);
	HashMap<String, String> hmBeforedata = getHashMapFromResultSetRow(beforeData);

	HashMap<String, AuditBeforeAfterPair> auditData = null;

	auditData = createComparisonDataSet(hmBeforedata, hmAfterdata);

	if (auditData != null) {
	    Iterator<String> it = auditData.keySet().iterator();
	    debug("Auditing Data Changes");
	    while (it.hasNext()) {
		String colName = it.next();

		AuditBeforeAfterPair beforeAfter = auditData.get(colName);

		String beforeVal = beforeAfter.getValueBefore();
		String afterVal = beforeAfter.getValueAfter();
		

		// If both values are blank - no need to record the data.
		// Probably a blank field.
		if (!(beforeVal == null && afterVal == null)) {
		    debug("Auditing change to " + colName + ". Before Value = " + beforeVal + " After Value = " + afterVal);
		    //One of these fields has something in it. Audit it !
		    insertAuditDataRow(fw, auditName, colName, beforeVal, afterVal, auditId);
		}

	    }
	    debug("Finished Auditing Data Changes");
	} else {
	    throw new DataException("Cannot create an audit event with no data!");
	}

    }

    /**
     * Returns a hashmap of comparison results, given a DataResultset for before
     * and after data
     * 
     * @param hmBeforeData
     * @param hmAfterData
     * @return hashmap of differences.
     * @throws DataException
     */
    private static HashMap<String, AuditBeforeAfterPair> createComparisonDataSet(HashMap<String, String> hmBeforeData,
	    HashMap<String, String> hmAfterData) throws DataException {
	HashMap<String, AuditBeforeAfterPair> result = new HashMap<String, AuditBeforeAfterPair>();

	if (hmBeforeData == null || hmBeforeData.isEmpty()) {
	    debug("Audit: Before Data is Null - This is an insert");
	    // We're talking about an insert here.
	    if (hmAfterData == null || hmAfterData.isEmpty()) {
		// but this is just plain silly
		Log.error("Audit ERROR: After Data is Null also - This is just silly");
		result = null;
		return null;
	    } else {

		Iterator<String> it = hmAfterData.keySet().iterator();

		while (it.hasNext()) {
		    AuditBeforeAfterPair resultPair = new AuditBeforeAfterPair();

		    String key = it.next();

		    String value = hmAfterData.get(key);

		    resultPair.setKey(key);
		    resultPair.setValueBefore(null);
		    resultPair.setValueAfter(value);

		    debug("Audit: Adding to result map (insert) : key=" + key + ", AfterValue=" + value);

		    result.put(key, resultPair);
		}
	    }

	} else {
	    debug("Audit: Before data has values");
	}

	if (hmAfterData == null) {
	    debug("Audit: After data is null. Must be a delete.");

	    
	    Iterator<String> it = hmBeforeData.keySet().iterator();

	    while (it.hasNext()) {
		AuditBeforeAfterPair resultPair = new AuditBeforeAfterPair();

		String key = it.next();

		String value = hmBeforeData.get(key);

		resultPair.setKey(key);
		resultPair.setValueBefore(value);
		resultPair.setValueAfter(null);
		debug("Adding to result map (delete): key=" + key + ", BeforeValue=" + value);
		result.put(key, resultPair);
	    }
	}

	if (hmBeforeData != null && hmAfterData != null && !hmBeforeData.isEmpty() && !hmAfterData.isEmpty()) {
	    debug("Audit: Before and after data have values. Must be an update");
	    // This must be an update !
	    Iterator<String> it = hmBeforeData.keySet().iterator();

	    while (it.hasNext()) {
		String key = it.next();

		if (hmAfterData != null && hmAfterData.containsKey(key)) {

		    // Reset null data to empty strings.
		    String afterData = hmAfterData.get(key) == null ? "" : hmAfterData.get(key);
		    String beforeData = hmBeforeData.get(key) == null ? "" : hmBeforeData.get(key);

		    // Compare before and after
		    if (!afterData.equals(beforeData)) {
			AuditBeforeAfterPair resultPair = new AuditBeforeAfterPair();

			resultPair.setKey(key); // hmm this setter is a bit
			// redundant.
			resultPair.setValueBefore(beforeData);
			resultPair.setValueAfter(afterData);

			debug("Adding to result map (update) : key=" + key + ",BeforeValue=" + beforeData + ",AfterValue="
				+ afterData);
			result.put(key, resultPair);
		    }

		} else {
		    throw new DataException("Error, mismatch in datasets for update audit");
		}
	    }

	}

	return result;
    }

    /**
     * Convert a single row DataResultSet into a hashmap.
     * 
     * @param resultRow
     * @return Hashmap containing one row of data as name value pairs. Returns
     *         null if resultRow is null.
     * @throws DataException
     */
    private static HashMap<String, String> getHashMapFromResultSetRow(DataResultSet resultRow) throws DataException {
	HashMap<String, String> resultMap = null;

	if (resultRow != null) {
	    resultMap = new HashMap<String, String>();
	    int numCols = resultRow.getNumFields();

	    if (resultRow == null || resultRow.getNumRows() < 1) {
		return null;
	    }

	    if (resultRow != null && resultRow.getNumRows() > 1) {
		Log.error("getHashMapFromResultSetRow cannot handle more than one row");
		throw new DataException("Audit: cannot handle more than one row");
	    }
	    for (int i = 0; i < numCols; i++) {
		String fieldName = resultRow.getFieldName(i);
		String value = resultRow.getStringValue(i);
		resultMap.put(fieldName, value);
	    }
	}
	return resultMap;
    }

    /**
     * 
     * @param fw
     * @param auditID
     *            - Audit ID Key
     * @param relations
     *            - Hashmap of key / value pairs. Eg element id, element table
     *            name
     * @throws DataException
     */
    private static void insertAuditRelations(FWFacade fw, String auditName, int auditId, HashMap<Integer, String> relations)
	    throws DataException {

	if (relations != null && !relations.isEmpty()) {

	    Iterator<Integer> it = relations.keySet().iterator();

	    while (it.hasNext()) {

		Integer relationId = it.next();
		String relationType = relations.get(relationId);

		debug("Auditing Relation Type" + relationType + " : " + relationId.toString());

		StringBuffer dataQuery = new StringBuffer();
		dataQuery.append("INSERT INTO " + auditName + "_RELATIONS");
		dataQuery.append(" ( ");
		dataQuery.append("AUDITID,");
		dataQuery.append("RELATIONID,");
		dataQuery.append("RELATIONTYPE");
		dataQuery.append(" ) ");
		dataQuery.append("VALUES");
		dataQuery.append(" ( ");
		dataQuery.append(Integer.toString(auditId)).append(",");
		dataQuery.append(relationId.toString()).append(",");
		dataQuery.append("'").append(relationType).append("'");
		dataQuery.append(" ) ");

		debug("Audit Relations Query : " + dataQuery.toString());

		fw.executeSQL(dataQuery.toString());

	    }
	}
    }

    private static void insertAuditDataRow
     (FWFacade fw, String auditName, String attrName, 
      String beforeVal, String afterVal, int auditId) throws DataException {
		StringBuffer dataQuery = new StringBuffer();
	
		String beforeString = null;
		String afterString = null;
	
		if (beforeVal == null) {
		    beforeString = "NULL";
		} else {
			if (beforeVal.length() > MAX_AUDIT_VALUE_LENGTH)
				beforeVal = beforeVal.substring(0, MAX_AUDIT_VALUE_LENGTH);
			
		    beforeString = "'" + CCLAUtils.escapeForOracle(beforeVal) + "'";
		}
	
		if (afterVal == null) {
		    afterString = "NULL";
		} else {
			if (afterVal.length() > MAX_AUDIT_VALUE_LENGTH)
				afterVal = afterVal.substring(0, MAX_AUDIT_VALUE_LENGTH);
			
		    afterString = "'" + CCLAUtils.escapeForOracle(afterVal) + "'";
		}
	
		dataQuery.append("INSERT INTO " + auditName + "_DATA");
		dataQuery.append(" ( ");
		dataQuery.append("AUDITID,");
		dataQuery.append("ATTR_NAME,");
		dataQuery.append("ATTR_VAL_BEFORE,");
		dataQuery.append("ATTR_VAL_AFTER");
		dataQuery.append(" ) ");
		dataQuery.append("VALUES");
		dataQuery.append(" ( ");
		dataQuery.append(Integer.toString(auditId)).append(",");
		dataQuery.append("'").append(attrName).append("',");
		dataQuery.append(beforeString).append(",");
		dataQuery.append(afterString);
		dataQuery.append(" ) ");
	
		debug("Audit Data Query : " + dataQuery.toString());
	
		fw.executeSQL(dataQuery.toString());
    }

    public static void initAudit(FWFacade fw, String auditName) throws DataException {
	// TODO Auto-generated method stub
	initialiseAuditTables(fw, auditName);

    }

    private static void initialiseAuditTables(FWFacade fw, String auditName) throws DataException {
	// TODO Auto-generated method stub

	Hashtable<String, String> auditCols = new Hashtable<String, String>();

	auditCols.put("AUDITID", "NUMBER(15)");
	auditCols.put("USERID", "VARCHAR2(32)");
	auditCols.put("AUDIT_DATE", "TIMESTAMP(6)");
	auditCols.put("EVENT", "VARCHAR2(32)");
	auditCols.put("ACTION", "VARCHAR2(32)");

	Hashtable<String, String> auditDataCols = new Hashtable<String, String>();

	auditDataCols.put("AUDITID", "NUMBER(15)");
	auditDataCols.put("ATTR_NAME", "VARCHAR2(32)");
	auditDataCols.put("ATTR_VAL_BEFORE", "VARCHAR2(256)");
	auditDataCols.put("ATTR_VAL_AFTER", "VARCHAR2(256)");

	Hashtable<String, String> auditRelationCols = new Hashtable<String, String>();

	auditRelationCols.put("AUDITID", "NUMBER(15)");
	auditRelationCols.put("RELATIONID", "NUMBER(15)");
	auditRelationCols.put("RELATIONTYPE", "VARCHAR2(32)");

	try {
	    createTableIfNotExists(fw, auditName, auditCols);
	    createTableIfNotExists(fw, auditName + "_DATA", auditDataCols);
	    createTableIfNotExists(fw, auditName + "_RELATIONS", auditRelationCols);
	    createSequenceIfNotExists(fw, auditName);
	} catch (Exception e) {
	    throw new DataException("Unable to initialise audit tables: " + auditName, e);
	}

    }

    private static int getNextAuditId(FWFacade fw, String auditName) throws DataException {
	String sequenceName = "SEQ_" + auditName;

	String query = "SELECT " + sequenceName + ".NEXTVAL FROM dual";

	DataResultSet results = fw.createResultSetSQL(query);

	String nextVal = results.getStringValue(0);

	return Integer.parseInt(nextVal);

    }

    private static void createSequenceIfNotExists(FWFacade fw, String auditName) throws DataException {
	String sequenceName = "SEQ_" + auditName;
	try {
	    if (!sequenceExists(fw, sequenceName)) {
		fw.createSequence(sequenceName);
	    }
	} catch (Exception e) {
	    throw new DataException("Unable to init sequence " + sequenceName, e);
	}

    }

    private static void createTableIfNotExists(FWFacade fw, String tableName, Hashtable<String, String> tableCols) throws Exception {

	if (!tableExists(fw, tableName)) {
	    StringBuffer query = new StringBuffer();

	    query.append("CREATE TABLE ").append(tableName);
	    query.append("(");

	    Set<String> cols = tableCols.keySet();

	    Iterator<String> it = cols.iterator();

	    while (it.hasNext()) {
		String colName = it.next();

		String dataType = tableCols.get(colName);
		query.append(colName);
		query.append(" ");
		query.append(dataType);
		if (it.hasNext())
		    query.append(",");
	    }

	    query.append(")");

	    debug("Create Table query = " + query.toString());

	    fw.executeSQL(query.toString());

	}

    }
    
    /** Adapted method from FWFacade class. 
     *  
     *  The FWFacade class has a bug where it returns false if there is more than 1 table
     *  in the entire DB instance with the passed name. 
     * 
     * @param tableName
     * @return
     * @throws DataException 
     */
    private static boolean tableExists(FWFacade facade, String tableName) 
     throws Exception {
    	boolean exists = false;
		String sql = null;
		if (facade.isOracle()) {
			sql = "select table_name from all_tables where table_name = '"
					+ tableName + "'";
		} else {
			throw new DataException(
					"tableExists method only supports Oracle databases");
		}

		try {
			DataResultSet d = facade.createResultSetSQL(sql);
			if (d != null && d.getNumRows() > 0) {
				exists = true;
			}
		} catch (Exception e) {
			Log.warn("Table exists check returning false for table '"
					+ tableName + "' [" + e.getMessage() + "]");
			throw e;
		}
		return exists;
    }
    
	/**
	 * Returns a flag indicating whether a sequence exists.
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static boolean sequenceExists(FWFacade facade, String key) throws Exception {
		boolean exists = false;
		String sql = null;
		if (facade.isOracle()) {
			sql = "select sequence_name from all_sequences "
					+ "where sequence_name = '" + key + "'";
		} else {
			throw new Exception(
					"sequenceExists method only supports Oracle databases");
		}

		try {
			DataResultSet d = facade.createResultSetSQL(sql);
			if (d != null && d.getNumRows() > 0) {
				exists = true;
			}
		} catch (Exception e) {
			Log.warn("Sequence exists check returning false for sequence '"
					+ key + "' [" + e.getMessage() + "]");
			throw e;
		}
		return exists;
	}
    
    /** Returns a set of all SDAUDIT entries for the given RELATIONID.
     * 
     * @param relationId
     * @param facade
     * @return
     * @throws DataException
     */
    public static DataResultSet getAuditsByRelationId
     (int relationId, FWFacade facade) throws DataException {
    	
    	DataBinder binder = new DataBinder();
    	CCLAUtils.addQueryIntParamToBinder(binder, "RELATIONID", relationId);
    	
    	return facade.createResultSet
    	 ("qClientServices_GetSDAuditsByRelationId", binder);
    }

}
