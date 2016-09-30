package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.stellent.embedded.FWFacade;

/**
 * Models the CCLA.HELP_MAPPING Table
 * @author Cam
 *
 */
public class HelpMapping  implements Persistable {

	/* **************** Constants **************** */
	//BINDER AND DB COLUMN
	public static final class Cols {
		public static final String HELP_CODE 	= "HELP_CODE";
		public static final String DOCNAME 		= "DOCNAME";
		public static final String PAGE_NUMBER 	= "PAGE_NUMBER";
	}
	
	//QUERIES
	private static final String GET_ALL_QUERY = "qCore_GetAllHelpMapping";
	private static final String UPDATE_QUERY = "qCore_UpdateHelpMapping";
	private static final String ADD_QUERY = "qCore_AddHelpMapping";
	private static final String DELETE_QUERY = "qCore_DeleteHelpMapping";
	private static final String GET_BY_HELP_CODE_QUERY = "qCore_GetHelpMappingByHelpCode";
	
	
	/* **************** Properties **************** */
	private String helpCode;
	private String docname;
	private Integer pageNumber;
	
	/* ********************** Constructor ************************* */
	/**
	 * Constructor
	 * @param helpCode
	 * @param docName
	 * @param pageNumber
	 */
	public HelpMapping(String helpCode, String docname, Integer pageNumber) {
		this.helpCode = helpCode;
		this.docname = docname;
		this.pageNumber = pageNumber;
	}
	
	/**
	 * Constructor
	 * @param binder
	 * @throws DataException
	 */
	public HelpMapping(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	/* ************************* Methods ************************* */

	public String getHelpCode() {
		return helpCode;
	}

	public void setHelpCode(String helpCode) {
		this.helpCode = helpCode;
	}

	public String getDocname() {
		return docname;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	/************************ static Methods *********************************/
	
	/**
	 * Adds a helpMapping to the database
	 * 
	 * @param helpMapping
	 * @param facade
	 * @throws DataException
	 */
	public static HelpMapping add(HelpMapping helpMapping, FWFacade facade) 
	throws DataException {
		
		helpMapping.validate(facade);
		DataBinder binder = new DataBinder();
		helpMapping.addFieldsToBinder(binder);
		facade.execute(ADD_QUERY, binder);
		
		return helpMapping;
	}
	
	/**
	 * Add a helpMapping to the database
	 * @param binder
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static HelpMapping add(DataBinder binder, FWFacade facade) throws DataException {
		HelpMapping mapping = new HelpMapping(binder);
		return HelpMapping.add(mapping, facade);
	}
	
	/**
	 * Delete Help Mapping by helpCode
	 * @param helpCode
	 * @param facade
	 * @throws DataException
	 */
	public static void delete(String helpCode, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(binder, Cols.HELP_CODE, helpCode);
		facade.execute(DELETE_QUERY, binder);
	}

	/** 
	 * Fetches ResultSet for a single entry by helpCode.
	 * @param helpCode
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(String helpCode, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(binder, Cols.HELP_CODE , helpCode);
		DataResultSet rsHelp = facade.createResultSet
		 (GET_BY_HELP_CODE_QUERY, binder);
		
		return rsHelp;
	}	

	/**
	 * Gets a help mapping by help code
	 * @param helpCode
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static HelpMapping get(String helpCode, FWFacade facade) throws DataException {
		return get(getData(helpCode,facade));
	}
	
	/**
	 * Gets a Vector of HelpMapping.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<HelpMapping> getAll(FWFacade facade) throws DataException {
		Vector<HelpMapping> mappings = new Vector<HelpMapping>();
		
		DataResultSet rs = getAllData(facade);
		if (rs.first()) {
			do {
				mappings.add(get(rs));
			} while (rs.next());
		}
		return mappings;
	}

	/**
	 * 
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getAllData(FWFacade facade) throws DataException {
		return facade.createResultSet
		 (GET_ALL_QUERY, new DataBinder());
	}
	
	/**
	 * Returns a HelpMapping object from the DataResultSet or null if empty
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static HelpMapping get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new HelpMapping(
			CCLAUtils.getResultSetStringValue(rs, Cols.HELP_CODE),
			CCLAUtils.getResultSetStringValue(rs, Cols.DOCNAME),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.PAGE_NUMBER)	
		);
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<String, HelpMapping> getCache() {
		return CACHE;
	}
	
	/** HelpMapping cache implementor */
	private static class Cache extends Cachable<String, HelpMapping> {

		public Cache() {
			super("Help Mapping");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<HelpMapping> helpMappings = HelpMapping.getAll(facade);
			
			HashMap<String, HelpMapping> newCache = 
			 new HashMap<String, HelpMapping>();
			
			for (HelpMapping helpMapping : helpMappings) {
				newCache.put(helpMapping.getHelpCode(), helpMapping);
			}
			this.CACHE_MAP = newCache;
		}
	}

	/* ******************** Persistable Interface ************************ */
	
	public void setAttributes(DataBinder binder) throws DataException {
		this.setPageNumber(CCLAUtils.getBinderIntegerValue(binder, Cols.PAGE_NUMBER));
		this.setDocname(binder.getLocal(Cols.DOCNAME));
		this.setHelpCode(binder.getLocal(Cols.HELP_CODE));
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.PAGE_NUMBER, this.getPageNumber());
		CCLAUtils.addQueryParamToBinder(binder, Cols.DOCNAME, this.getDocname());
		CCLAUtils.addQueryParamToBinder(binder, Cols.HELP_CODE, this.getHelpCode());
	}

	public void persist(FWFacade facade, String userName) throws DataException {
		this.validate(facade);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		facade.execute(UPDATE_QUERY, binder);	
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}
}
