package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundBankAccountType implements Persistable {

	public static final class Cols {
		public static final String ID 			= "BANK_ACC_TYPE_ID";
		public static final String NAME 		= "BANK_ACC_TYPE_NAME";
		public static final String DESCRIPTION	= "BANK_ACC_TYPE_DESCRIPTION";
	}

	public static final class Queries {
		public static final String GET_ALL 		= "qCore_GetAllFundBankAccountType";
		public static final String GET_BY_ID 	= "qCore_GetFundBankAccountTypeById";
	}
	
	//Properties
	public static final int TYPE_CAPITAL_IN_ADVANCE = 1;
	public static final int TYPE_CURRENT = 2;
	
	//Attributes
	private int bankAccTypeId;
	private String bankAccTypeName;
	private String bankAccTypeDesc;
	
	/************************ Constructors ****************************/
	public FundBankAccountType() {}
	
	public FundBankAccountType(int bankAccTypeId, String bankAccTypeName, String bankAccTypeDesc) {
		this.bankAccTypeId = bankAccTypeId;
		this.bankAccTypeName = bankAccTypeName;
		this.bankAccTypeDesc = bankAccTypeDesc;
	}
	
	
	/*********************** Methods **********************************/
	public int getBankAccTypeId() {
		return bankAccTypeId;
	}

	public void setBankAccTypeId(int bankAccTypeId) {
		this.bankAccTypeId = bankAccTypeId;
	}

	public String getBankAccTypeName() {
		return bankAccTypeName;
	}

	public void setBankAccTypeName(String bankAccTypeName) {
		this.bankAccTypeName = bankAccTypeName;
	}

	public String getBankAccTypeDesc() {
		return bankAccTypeDesc;
	}

	public void setBankAccTypeDesc(String bankAccTypeDesc) {
		this.bankAccTypeDesc = bankAccTypeDesc;
	}

	
	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	
	/** Fetches all available InstructionStatus instances from the database.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<FundBankAccountType> getAll(FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = facade.createResultSet
		 (Queries.GET_ALL, new DataBinder());
		
		Vector<FundBankAccountType> allBankAccType = new Vector<FundBankAccountType>();
		
		if (rs.first()) {
			do {
				allBankAccType.add(get(rs));
			} while (rs.next());
		}
		
		return allBankAccType;
	}
	
	public static FundBankAccountType get(int bankAccTypeId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(bankAccTypeId, facade);
		return get(rs);
		
	}
	
	public static DataResultSet getData(int bankAccTypeId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, bankAccTypeId);
		DataResultSet rs = facade.createResultSet(Queries.GET_BY_ID, binder);
		return rs;
	}
	
	public static FundBankAccountType get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;

		return new FundBankAccountType(
			CCLAUtils.getResultSetIntegerValue
			 (rs, Cols.ID),
			CCLAUtils.getResultSetStringValue
			 (rs, Cols.NAME),
			 CCLAUtils.getResultSetStringValue
			 (rs, Cols.DESCRIPTION)
		);
	}	
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, FundBankAccountType> getCache() {
		return CACHE;
	}
	
	/** FundBankAccountType cache implementor */
	public static class Cache extends Cachable<Integer, FundBankAccountType> {

		public Cache() {
			super("Fund Bank Account Type");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<FundBankAccountType> allBankAccType = FundBankAccountType.getAll(facade);
			
			HashMap<Integer, FundBankAccountType> newCache = 
			 new HashMap<Integer, FundBankAccountType>();
			
			for (FundBankAccountType bankAccType : allBankAccType) {
				newCache.put(bankAccType.getBankAccTypeId(), bankAccType);
			}
			
			this.CACHE_MAP = newCache;
		}
	}	
	
}
