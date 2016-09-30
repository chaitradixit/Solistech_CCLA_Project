package com.ecs.stellent.ccla.clientservices;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.utils.stellent.embedded.FWFacade;

import com.ecs.utils.Log;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

/** Used for testing Keith's new audit stuff.
 * 
 * @author Tom
 *
 */
public class AuditTestService extends Service {
    
	HashMap<Integer, String> relations = new HashMap<Integer, String>();
    	

	
	public void testAudit() throws DataException {
		
	    // http://ccla-ap14/ucm/idcplg?IdcService=CCLA_CS_TEST_AUDIT&eventName=TEST&auditName="SDAUDIT"
	    
	    
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		String auditName = m_binder.getLocal("auditName");
		
		
		
		String[] testCols = new String[] {
			"NAME", "AGE", "PHONE_NUMBER"
		};
		
	    	

	    	relations.put(1234567, "PERSONID");
	    	relations.put(1234568, "ACCOUNTID");		
		
		try {
		    test1(facade, auditName, testCols);
		    Log.debug("Test #1 successful");
		} catch (Exception e) {
		    Log.error("Test #1 failed : " + e.getMessage(),e);
		    
		   
		}
		
		try {
		    test2(facade, auditName, testCols);
		    Log.debug("Test #2 successful");
		} catch (Exception e) {
		    Log.error("Test #2 failed : " + e.getMessage(),e);
		   
		}
			
		try {
		    test3(facade, auditName, testCols);
		    Log.debug("Test #3 successful");
		} catch (Exception e) {
		    Log.error("Test #3 failed : " + e.getMessage(),e);
		   
		}
		
		try {
		    test4(facade, auditName, testCols);
		    Log.debug("Test #4 successful");
		} catch (Exception e) {
		    Log.error("Test #4 failed : " + e.getMessage(),e);
		   
		}
		
		try {
		    test4(facade, auditName, testCols);
		    Log.debug("Test #5 successful");
		} catch (Exception e) {
		    Log.error("Test #5 failed : " + e.getMessage(),e);
		   
		}
	}

	private void test1(FWFacade facade, String auditName, String[] testCols) throws DataException {
	    // Create the before/after ResultSets
	    DataResultSet beforeDataTest1 = new DataResultSet(testCols);
	    DataResultSet afterDataTest2 = new DataResultSet(testCols);
	    
	    // Create test data for the 'before' ResultSet
	    Vector<String> beforeDataValues = new Vector<String>();
	    
	    beforeDataValues.add("Tom");
	    beforeDataValues.add("26");
	    beforeDataValues.add("07725012792");
	    
	    beforeDataTest1.addRow(beforeDataValues);
	    
	    // Create test data for the 'after' ResultSet
	    Vector<String> afterDataValues = new Vector<String>();
	    
	    afterDataValues.add("Tom");
	    afterDataValues.add("27");
	    afterDataValues.add("07725012792");
	    
	    afterDataTest2.addRow(afterDataValues);
	    
	    Log.debug("Starting Test #1: Basic Update Audit");
	    
	    
	    

	    	
	    	// Test 1:
	    	Audit.addAudit(facade, auditName, m_userData.m_name, "UPDATE", 
	    	 "EDITPERSON", beforeDataTest1, afterDataTest2, relations);	    	
	    	
	}
	
	private void test2(FWFacade facade, String auditName, String[] testCols) throws DataException {
	    // Create the before/after ResultSets
	    DataResultSet beforeDataTest1 = new DataResultSet(testCols);
	    DataResultSet afterDataTest2 = new DataResultSet(testCols);
	    
	    // Create test data for the 'before' ResultSet
	    Vector<String> beforeDataValues = new Vector<String>();
	    
	    beforeDataValues.add(null);
	    beforeDataValues.add(null);
	    beforeDataValues.add(null);
	    
	    beforeDataTest1.addRow(beforeDataValues);
	    
	    // Create test data for the 'after' ResultSet
	    Vector<String> afterDataValues = new Vector<String>();
	    
	    afterDataValues.add("Tom");
	    afterDataValues.add("27");
	    afterDataValues.add("07725012792");
	    
	    afterDataTest2.addRow(afterDataValues);
	    
	    Log.debug("Starting Test #2: Basic Insert Audit");
	   	
	    	// Test 1:
	    	Audit.addAudit(facade, auditName, m_userData.m_name, "INSERT", 
	    	 "EDITPERSON", beforeDataTest1, afterDataTest2, relations);	    	
	    	
	}
	
	private void test3(FWFacade facade, String auditName, String[] testCols) throws DataException {
	    // Create the before/after ResultSets
	    DataResultSet beforeDataTest1 = new DataResultSet(testCols);
	    DataResultSet afterDataTest2 = new DataResultSet(testCols);
	    
	    // Create test data for the 'before' ResultSet
	    Vector<String> beforeDataValues = new Vector<String>();
	    
	    beforeDataValues.add("Tom");
	    beforeDataValues.add("26");
	    beforeDataValues.add("07725012792");
	    
	    beforeDataTest1.addRow(beforeDataValues);
	    
	    // Create test data for the 'after' ResultSet
	    Vector<String> afterDataValues = new Vector<String>();
	    
	    afterDataValues.add(null);
	    afterDataValues.add(null);
	    afterDataValues.add(null);
	    
	    afterDataTest2.addRow(afterDataValues);
	    
	    Log.debug("Starting Test #3: Basic Delete Audit");
	   
	    	
		// Test 1:
	    	Audit.addAudit(facade, auditName, m_userData.m_name, "DELETE", 
	    	 "EDITPERSON", beforeDataTest1, afterDataTest2, relations );	    	
	    	
	}
	
	private void test4(FWFacade facade, String auditName, String[] testCols) throws DataException {
	    // Create the before/after ResultSets
	    DataResultSet beforeDataTest1 = new DataResultSet(testCols);
	    DataResultSet afterDataTest2 = null;
	    
	    // Create test data for the 'before' ResultSet
	    Vector<String> beforeDataValues = new Vector<String>();
	    
	    beforeDataValues.add("Tom");
	    beforeDataValues.add("26");
	    beforeDataValues.add("07725012792");
	    
	    beforeDataTest1.addRow(beforeDataValues);
	    
	    
	    Log.debug("Starting Test #4: Alternate Delete Audit (setting null dataresultset)");
	    
	    
	    

	    	
	    	// Test 1:
	    	Audit.addAudit(facade, auditName, m_userData.m_name, "DELETE", 
	    	 "EDITPERSON", beforeDataTest1, afterDataTest2, relations);	    	
	    	
	}
	
	private void test5(FWFacade facade, String auditName, String[] testCols) throws DataException {
	    // Create the before/after ResultSets
	    DataResultSet beforeDataTest1 = null;
	    DataResultSet afterDataTest2 = new DataResultSet(testCols);
	    
	    
	    // Create test data for the 'after' ResultSet
	    Vector<String> afterDataValues = new Vector<String>();
	    
	    afterDataValues.add("Tom");
	    afterDataValues.add("27");
	    afterDataValues.add("07725012792");
	    
	    afterDataTest2.addRow(afterDataValues);
	    
	    Log.debug("Starting Test #6: Alternate Insert Audit (using null dataresultset)");
	   	
	    	// Test 1:
	    	Audit.addAudit(facade, auditName, m_userData.m_name, "INSERT", 
	    	 "EDITPERSON", beforeDataTest1, afterDataTest2, relations);	    	
	    	
	}
	
	
}
