package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents a single entry from the REF_COMPANY table.
 *  
 *  All instances are cached at server startup. See CacheManager class
 *  
 * @author Tom
 *
 */
public class Company implements Persistable {

	private int companyId;
	private String code; /* 3/4 character company code, e.g. COIF */
	private String name; /* Full company name */
	
	private String orgCodePrefix; /* 	4-character code used when building
										Organisation Account Codes based on
	 									Aurora Client Numbers */	
	
	/* Minimum length of zero-padding to apply when Client Numbers relating to this 
	 * Company are displayed as a string
	 */
	private int clientNumberPadding;
	
	/* Minimum length of zero-padding to apply when Account Numbers relating to this 
	 * Company are displayed as a string
	 */
	private int accountNumberPadding;
	
	/* Hard-coded Company ID references, for easy access in code. Must match
	 *  to existing entries in REF_COMPANY table.
	 */
	public static final int UNKNOWN = 0;
	public static final int COIF 	= 1;
	public static final int CBF 	= 2;
	public static final int LAMIT	= 3;
	public static final int PSIC	= 4;
	public static final int CCLA	= 5;
	
	public static class Cols {
		public static final String ID = "COMPANY_ID";
		public static final String CODE = "COMPANY_CODE";
		public static final String NAME = "COMPANY_NAME";
	}
	
	public Company(int companyId, String code, String name, 
	 String orgCodePrefix, int clientNumberPadding, int accountNumberPadding) {
		
		this.companyId = companyId;
		this.code = code;
		this.name = name;
		this.orgCodePrefix = orgCodePrefix;
		this.clientNumberPadding = clientNumberPadding;
		this.accountNumberPadding = accountNumberPadding;
	}
	
	public static Vector<Company> getAll(FWFacade facade) throws DataException {
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllCompanies", new DataBinder());
		
		return getAll(rs);
	}
	
	public static Vector<Company> getAll(DataResultSet rs) throws DataException {
		Vector<Company> companies = new Vector<Company>();
		
		if (rs.first()) {
			do {
				companies.add(get(rs));
			} while (rs.next());
		}
		
		return companies;
	}
	
	public static Company get(DataResultSet rsCompany) throws DataException {
		if (rsCompany == null || rsCompany.isEmpty())
			return null;
		
		return new Company(
			CCLAUtils.getResultSetIntegerValue
			 (rsCompany, "COMPANY_ID"),
			 rsCompany.getStringValueByName("COMPANY_CODE"),
			 rsCompany.getStringValueByName("COMPANY_NAME"),
			 rsCompany.getStringValueByName("ORG_CODE_PREFIX"),
			 CCLAUtils.getResultSetIntegerValue(rsCompany, "CLIENT_NUMBER_PADDING"),
			 CCLAUtils.getResultSetIntegerValue(rsCompany, "ACCOUNTNUMBER_PADDING")
			 
		);
	}
	
	public static Company get(int companyId, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "COMPANY_ID", companyId);
		
		DataResultSet rsCompany = facade.createResultSet
		 ("qClientServices_GetCompanyById", binder);
		
		return get(rsCompany);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub

	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	
	/** Short code for the Company, e.g. COIF, CBF
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrgCodePrefix(String orgCodePrefix) {
		this.orgCodePrefix = orgCodePrefix;
	}

	public String getOrgCodePrefix() {
		return orgCodePrefix;
	}

	public boolean equals(Company company) {
		return (this.getCompanyId() == company.getCompanyId());
	}
	
	@Override
	public String toString() {
		return "Company [code=" + code + "\\n, companyId=" + companyId
				+ "\\n, name=" + name + "\\n, orgCodePrefix=" + orgCodePrefix
				+ "]";
	}

	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, Company> getCache() {
		return CACHE;
	}
	
	/** Company cache implementor.
	 *  
	 **/
	private static class Cache extends Cachable<Integer, Company> {

		public Cache() {
			super("Company");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<Company> objs = Company.getAll(facade);
			HashMap<Integer, Company> newCache = new HashMap<Integer, Company>();
			
			for (Company obj : objs) {
				newCache.put(obj.getCompanyId(), obj);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	
	
	/*
	 *  Caching stuff
	 */
	private static NameCache NameCACHE = new NameCache();
	
	public static Cachable<String, Company> getNameCache() {
		return NameCACHE;
	}
	
	public void setClientNumberPadding(int clientNumberPadding) {
		this.clientNumberPadding = clientNumberPadding;
	}

	public int getClientNumberPadding() {
		return clientNumberPadding;
	}

	public void setAccountNumberPadding(int accountNumberPadding) {
		this.accountNumberPadding = accountNumberPadding;
	}

	public int getAccountNumberPadding() {
		return accountNumberPadding;
	}

	/** Company cache implementor.
	 *  
	 **/
	private static class NameCache extends Cachable<String, Company> {

		public NameCache() {
			super("Company Name");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<Company> objs = Company.getAll(facade);
			HashMap<String, Company> newCache = new HashMap<String, Company>();
			
			for (Company obj : objs) {
				newCache.put(obj.getCode(), obj);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	
}
