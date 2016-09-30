package com.ecs.stellent.spp.dependantdocs;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSet;
import intradoc.data.Workspace;

import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;

public class DependantDocUtils{
	
	public final static String TABLE_NAME = "TDEPENDANTDOCS";
	
	/**
	 * Sets a number of dependant docs (csv) for a single root document.
	 * 
	 * Updates TDEPENDANTDOCS table.
	 * 
	 * @param workspace
	 * @param rootDocName
	 * @param csvDependantDocs
	 * @param isSupporting
	 * @throws ServiceException
	 */
	public void setDependantDocsForRootDoc(Workspace workspace, String rootDocName, 
			String[] csvDependantDocs, boolean isSupporting) 
		throws ServiceException{
		
		Log.debug("setDependantDocsForRootDoc >>");
		long startTime = System.currentTimeMillis();
		
		//Determine the Supporting string as Y or N
		String isSupportingStr = "0"; 
		
		if(isSupporting)
			isSupportingStr = "1";
			
		int successCounter = 0;
		
		DataBinder binder = new DataBinder();
		binder.putLocal("rootDocName", rootDocName);
		binder.putLocal("isSupporting", isSupportingStr);
		
		for (int i=0; i<csvDependantDocs.length; i++) {
			
			/*
			String insertSql = "INSERT INTO " + TABLE_NAME + " VALUES (" +
					"'" + rootDocName + "'," +
					"'" + csvDependantDocs[i] + "'," + 
					"'" + isSupportingStr + "'" +
					")";
			
			Log.debug("Executing SQL: " + insertSql);
			*/
			
			binder.putLocal("depDocName", csvDependantDocs[i]);
			
			try {
				workspace.execute("qInsertDependantDoc", binder);
				successCounter++;
			} catch (DataException de) {
				//The row already exists, try updating it instead
				
				Log.warn("Cannot perform INSERT (row probably exists already)  (" 
				 + de.getMessage() + ")");
			}
		}
		
		Log.debug("Successfully executed " + successCounter + " of " + 
		 csvDependantDocs.length + " sql INSERTs");
		
		Log.debug("setDependantDocsForRootDoc << docName: " + rootDocName + 
		 ", time taken: " + ((System.currentTimeMillis() - startTime)/1000D) + " seconds");
	}
	
	/**
	 * Allows a single dependant doc to be marked as a dependant document of the
	 * passed root document.
	 *  
	 * @param workspace
	 * @param rootDocName
	 * @param dependantDocName
	 * @param isSupporting
	 * @throws ServiceException
	 */
	public void setDependantDocForRootDoc(Workspace workspace, String rootDocName, 
			String dependantDocName, boolean isSupporting) throws ServiceException{
		
		String[] csvDependantDocs = StringUtils.stringToArray(dependantDocName);
		
		setDependantDocsForRootDoc(workspace, rootDocName, csvDependantDocs, isSupporting);
	}
	
	/**
	 * Allows one dependant document to have its root document set to many documents (csv).
	 * 
	 * @param workspace
	 * @param csvRootDocNames
	 * @param dependantDocName
	 * @param isSupporting
	 * @throws ServiceException
	 */
	public void setDependantDocForRootDocs(Workspace workspace, String[] csvRootDocNames, 
			String dependantDocName,  boolean isSupporting) 
		throws ServiceException{
		
		Log.debug("setDependantDocForRootDocs >>");
		
		//Determine the Supporting string as Y or N
		String isSupportingStr = "0"; 
		
		if(isSupporting)
			isSupportingStr = "1";
		
		int successCounter = 0;
		
		for(int i=0; i<csvRootDocNames.length; i++){
			String insertSql = "INSERT INTO " + TABLE_NAME + " VALUES (" +
					"'" + csvRootDocNames[i] + "'," +
					"'" + dependantDocName + "'," + 
					"'" + isSupportingStr + "'" +
					")";
			
			Log.debug("Executing SQL: " + insertSql);
			
			try {
				workspace.executeSQL(insertSql);
				successCounter++;
			} catch (DataException de) {
				//The row already exists, try updating it instead
				
				Log.error("Cannot perform INSERT (row probably exists already) (" 
						+ de.getMessage() + ")");
			}
		}
		
		Log.debug("Successfully executed " + successCounter + " of " 
				+ csvRootDocNames.length + " sql INSERTs");
		
		Log.debug("setDependantDocForRootDocs <<");
	}
	
	/**
	 * Returns a result set containing the dependant documents doc names (column name: 
	 * DEP_DOCNAME)for the passed root document.
	 * 
	 * @param workspace
	 * @param rootDocName
	 * @param isSupporting
	 * @return
	 * @throws ServiceException
	 */
	public DataResultSet getDependantDocsForRootDoc(Workspace workspace, String rootDocName, 
			boolean isSupporting) throws ServiceException{
		
		//Determine the Supporting string as Y or N
		String isSupportingStr = "0"; 
		
		if(isSupporting)
			isSupportingStr = "1";
		
		Log.debug("getDependantDocsForRootDoc >>");
		
		try{
			DataBinder binder = new DataBinder();
			binder.putLocal("rootDocName", rootDocName);
			binder.putLocal("isSupporting", isSupportingStr);
			
			ResultSet rs = workspace.createResultSet(
			 "qDependantDocs_GetDependantDocsForRootDoc", binder);
			
			Log.debug("getDependantDocsForRootDoc <<");
			
			DataResultSet drs = new DataResultSet();
			drs.copy(rs);
			return drs;
			
		}catch(DataException de){
			Log.error("Unable to execute SQL: " + de.getMessage(), de);
			throw new ServiceException(de);
		}
	}
	
	/**
	 * Returns a result set containing the root documents doc names (coumn name: ROOT_DOCNAME) 
	 * for the passed dependant document.
	 * 
	 * @param workspace
	 * @param dependantDocName
	 * @param isSupporting
	 * @return
	 * @throws ServiceException
	 */
	public DataResultSet getRootDocsForDependantDoc(Workspace workspace, String dependantDocName, 
			boolean isSupporting)throws ServiceException{
		
		//Determine the Supporting string as Y or N
		String isSupportingStr = "0"; 
		
		if(isSupporting)
			isSupportingStr = "1";
		
		Log.debug("getRootDocsForDependantDoc >>");
		
		try{
			DataBinder binder = new DataBinder();
			binder.putLocal("dependantDocName", dependantDocName);
			binder.putLocal("isSupporting", isSupportingStr);
			
			ResultSet rs = workspace.createResultSet(
					"qDependantDocs_GetRootDocsForDependantDoc", binder);
			
			Log.debug("getRootDocsForDependantDoc <<");
			
			DataResultSet drs = new DataResultSet();
			drs.copy(rs);
			return drs;
			
		}catch(DataException de){
			Log.error("Unable to execute SQL: " + de.getMessage(), de);
			throw new ServiceException(de);
		}
	}
	
	/**
	 * Will remove a link between a root document and a dependant document by
	 * deleting the relevant row from the TDEPENDANTDOCS table
	 * 
	 * @param workspace
	 * @param rootDocName
	 * @param dependantDocName
	 * @param isSupporting
	 * @throws ServiceException
	 */
	public void deleteDependantDocForRootDoc(Workspace workspace,String rootDocName,  
			String dependantDocName, boolean isSupporting) throws ServiceException{
		
		Log.debug("deleteDependantDocForRootDoc >>");
		
		//Determine the Supporting string as Y or N
		String isSupportingStr = "0"; 
		
		if(isSupporting)
			isSupportingStr = "1";
		
		DataBinder binder = new DataBinder();
		binder.putLocal("rootDocName", rootDocName);
		binder.putLocal("dependantDocName", dependantDocName);
		binder.putLocal("isSupporting", isSupportingStr);
		
		try {
			workspace.createResultSet(
					"qDependantDocs_DeleteDependantDocForRootDoc", binder);
		} catch (DataException de) {				
			Log.error("Cannot perform DELETE (" 
					+ de.getMessage() + ")");
			throw new ServiceException(de);
		}
		
		Log.debug("deleteDependantDocForRootDoc <<");

	}
	
}
