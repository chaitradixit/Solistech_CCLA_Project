package com.ecs.stellent.ccla;

import java.util.Vector;

import com.ecs.utils.Log;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSet;
import intradoc.data.Workspace;
import intradoc.provider.Provider;
import intradoc.provider.Providers;
import intradoc.server.Service;

/** All service methods relating to ODC reporting functions */
public class OdcReporting extends Service {
	
	// Returns the list of all file cabinet names and their
	// IDs
	private static final String qFileCabinets = 
	 "SELECT ecName, ecID FROM ecFileCabinets";
	
	// SQL query which returns a count of all batches for each
	// status, for a given filing cabinet.  
	// The filing cabinet name is parametized.
	private static final String qBatchSummary = 
	 "SELECT     COUNT(*) AS numBatches, ecStatus AS batchStatus " +
	 "FROM       ecBatches " +
	 "WHERE     (ecFileCabinetID IN " +
                "(SELECT     ecID " +
                "FROM        ecFileCabinets " +
                "WHERE      (ecName = ?))) " +
     "GROUP BY ecStatus";

	// Returns a list of all batches and their associated data, for a
	// given filing cabinet.
	private static final String qBatches = 
	 "SELECT 	* FROM ecBatches " +
	 "WHERE     (ecFileCabinetID IN " +
     			"(SELECT     ecID " +
     			"FROM        ecFileCabinets " +
     			"WHERE      (ecName = ?))) " +
     			"ORDER BY ecDateTime";
	
	public void getFileCabinets() throws ServiceException, DataException {
		Workspace odcWorkspace = getOdcWorkspace();
	
		ResultSet cabinets = odcWorkspace.createResultSetSQL(qFileCabinets);
		DataResultSet drs = new DataResultSet();
		drs.copy(cabinets);
		m_binder.addResultSet("rsFileCabinets", drs);
	}
	
	public void getBatchListing() throws ServiceException, DataException {
		Workspace odcWorkspace = getOdcWorkspace();
		
		String fileCabinet = m_binder.getLocal("fileCabinet");
		if (fileCabinet == null || fileCabinet.length() == 0)
			fileCabinet = "CCLA";
		
		// Add the filing cabinet name to the query
		String query = qBatches.replaceFirst("\\?", "'" + fileCabinet + "'");
		
		ResultSet batches = odcWorkspace.createResultSetSQL(query);
		DataResultSet drs = new DataResultSet();
		drs.copy(batches);
		m_binder.addResultSet("rsBatches", drs);
	}
	
	public void getBatchSummary() throws ServiceException, DataException {
		Workspace odcWorkspace = getOdcWorkspace();
			
		String fileCabinet = m_binder.getLocal("fileCabinet");
		if (fileCabinet == null || fileCabinet.length() == 0)
			fileCabinet = "CCLA";
		
		// Add the filing cabinet name to the query
		String query = qBatchSummary.replaceFirst("\\?", "'" + fileCabinet + "'");
		
		ResultSet batchSummary = odcWorkspace.createResultSetSQL(query);
		DataResultSet drs = new DataResultSet();
		drs.copy(batchSummary);
		m_binder.addResultSet("rsBatchSummary", drs);
	}
	
	/** Retrives a workspace instance for the 'ODC' Provider
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public static Workspace getOdcWorkspace() throws ServiceException {
		Workspace odcWorkspace = null;
		
		Provider odcProvider = Providers.getProvider("ODC");	
		if (odcProvider != null)
			odcWorkspace = (Workspace)odcProvider.getProvider();
		else {
			Log.error("Unable to retrieve ODC provider!");
			throw new ServiceException("Unable to retrieve ODC provider!");
		}
		
		if (odcWorkspace == null) {
			Log.error("Unable to retrieve ODC workspace instance!");
			throw new ServiceException("Unable to retrieve ODC workspace instance!");
		}
		
		return odcWorkspace;
	}
	
	public static void main (String[] args) {
		
		String query = qBatchSummary.replaceFirst("\\?", "\"" + "hello" + "\"");
		System.out.println(query);
		
	}
}
