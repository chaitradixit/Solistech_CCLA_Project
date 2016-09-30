package com.ecs.ucm.ccla;

import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.ServiceHandler;

/** Extension service methods to the core Service class.
 * 
 * @author Tom
 *
 */
public class CoreServiceHandler extends ServiceHandler {
	
	/**
	 *  Creates a ResultSet via the pooled Oracle DB connection.
	 *  
	 *  Required service arguments:
	 *  0: query name
	 *  1: ResultSet name
	 *  
	 * @throws DataException 
	 *  
	 */
	public void createResultSetFromPooledConnection() throws DataException {
		if (m_currentAction.getNumParams() < 2) {
			 throw new DataException ("not enough parameters to create ResultSet.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		//get query name param
		String queryName = null;
		try{
			queryName = (String)m_currentAction.m_params.get(0);
		}catch(Exception e){
			throw new DataException ("Unable to create queryName parameter." , e);
		}
		//execute query to build result set
		DataResultSet rs = facade.createResultSet
		 (queryName, m_binder);
		
		//add result set to binder under supplied name
		String resultSetName = null;
		try{
			resultSetName = (String)m_currentAction.m_params.get(1);
		}catch(Exception e){
			throw new DataException ("Unable to create resultSetName parameter." , e);
		}
		m_binder.addResultSet(resultSetName, rs);
	}
	
	/** 
	 *  Executes a query via the pooled Oracle DB connection.
	 *  
	 *  Required service arguments:
	 *  0: query name
	 *  
	 * @throws DataException 
	 *  
	 */
	public void executeQueryFromPooledConnection() throws DataException {
		if (m_currentAction.getNumParams() < 1) {
			throw new DataException ("not enough parameters to execute query.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		//get query name param
		String queryName = null;
		try{
			queryName = (String)m_currentAction.m_params.get(0);
		}catch(Exception e){
			throw new DataException ("Unable to create queryName parameter." , e);
		}
		
		facade.execute(queryName, m_binder);
	}
}
