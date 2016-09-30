package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundBankAccount implements Persistable {

	public static final class Cols {
		public static final String BANK_ACC_TYPE_ID = "BANK_ACC_TYPE_ID";
		public static final String BANK_ACCOUNT_ID	= "BANK_ACCOUNT_ID";
		public static final String FUND_CODE		= "FUND_CODE";
	}

	public static final class Queries {
		public static final String GET_ALL 					= "qCore_GetAllFundBankAccount";
		public static final String GET_ALL_BY_TYPE_AND_FUND = "qCore_GetFundBankAccountByTypeIdAndFund";
		public static final String GET_ALL_BY_ID_AND_TYPE_AND_FUND = "qCore_GetFundBankAccountByTypeIdAndFund";

		//public static final String ADD_FUND_BANK_ACCOUNT 	= "qCore_AddFundBankAccount";
	}
	
	//Attributes
	private int bankAccTypeId;
	private int bankAccountId;
	private String fundCode;
	
	/**************************** Constructors *******************************/
	public FundBankAccount() { }
	
	public FundBankAccount(int bankAccTypeId, int bankAccountId, String fundCode) {
		this.bankAccTypeId = bankAccTypeId;
		this.bankAccountId = bankAccountId;
		this.fundCode = fundCode;
	}
	
	/**************************** Methods *******************************/
	
	public int getBankAccTypeId() {
		return bankAccTypeId;
	}
	public void setBankAccTypeId(int bankAccTypeId) {
		this.bankAccTypeId = bankAccTypeId;
	}
	public int getBankAccountId() {
		return bankAccountId;
	}
	public void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}
	public String getFundCode() {
		return fundCode;
	}
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	
	public void setAttributes(DataBinder binder) throws DataException {
		this.setBankAccTypeId(CCLAUtils.getBinderIntegerValue(binder, Cols.BANK_ACC_TYPE_ID));		
		this.setBankAccountId(CCLAUtils.getBinderIntegerValue(binder, Cols.BANK_ACCOUNT_ID));
		this.setFundCode(binder.getLocal(Cols.FUND_CODE));
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.BANK_ACC_TYPE_ID, this.getBankAccTypeId());
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.BANK_ACCOUNT_ID, this.getBankAccountId());
		CCLAUtils.addQueryParamToBinder(binder, Cols.FUND_CODE, this.getFundCode());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		//TODO
	}

	public void validate(FWFacade facade) throws DataException {
		//TODO
	}

	
	/** Fetches all available InstructionStatus instances from the database.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<FundBankAccount> getAll(FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = facade.createResultSet
		 (Queries.GET_ALL, new DataBinder());
		
		Vector<FundBankAccount> allFundBankAcc = new Vector<FundBankAccount>();
		
		if (rs.first()) {
			do {
				allFundBankAcc.add(get(rs));
			} while (rs.next());
		}
		
		return allFundBankAcc;
	}
	
	
	public static FundBankAccount get(int bankAccTypeId, int bankAccountId, String fundCode, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(bankAccTypeId, bankAccountId, fundCode, facade);
		return get(rs);		
	}
	
	public static DataResultSet getData(int bankAccTypeId, int bankAccountId, String fundCode, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.BANK_ACC_TYPE_ID, bankAccTypeId);
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.BANK_ACCOUNT_ID, bankAccountId);
		CCLAUtils.addQueryParamToBinder(binder, Cols.FUND_CODE, fundCode);
		
		
		DataResultSet rs = facade.createResultSet(Queries.GET_ALL_BY_ID_AND_TYPE_AND_FUND, binder);
		return rs;
	}
	
	
	public static Vector<FundBankAccount> get(int bankAccTypeId, String fundCode, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(bankAccTypeId,  fundCode, facade);
		Vector<FundBankAccount> allFundBankAcc = new Vector<FundBankAccount>();
		
		if (rs.first()) {
			do {
				allFundBankAcc.add(get(rs));
			} while (rs.next());
		}	
		return allFundBankAcc;
	}
	
	public static DataResultSet getData(int bankAccTypeId, String fundCode, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.BANK_ACC_TYPE_ID, bankAccTypeId);
		CCLAUtils.addQueryParamToBinder(binder, Cols.FUND_CODE, fundCode);
		
		
		DataResultSet rs = facade.createResultSet(Queries.GET_ALL_BY_TYPE_AND_FUND, binder);
		return rs;
	}	
	
	
	public static FundBankAccount get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;

		return new FundBankAccount(
			CCLAUtils.getResultSetIntegerValue
			 (rs, Cols.BANK_ACC_TYPE_ID),
			CCLAUtils.getResultSetIntegerValue
			 (rs, Cols.BANK_ACCOUNT_ID),
			 CCLAUtils.getResultSetStringValue
			 (rs, Cols.FUND_CODE)
		);
	}	
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<String, FundBankAccount> getCache() {
		return CACHE;
	}
	
	/** FundBankAccountType cache implementor */
	public static class Cache extends Cachable<String, FundBankAccount> {

		public Cache() {
			super("Fund Bank Account Type");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<FundBankAccount> allFundBankAcc = FundBankAccount.getAll(facade);
			
			HashMap<String, FundBankAccount> newCache = 
			 new HashMap<String, FundBankAccount>();
			
			for (FundBankAccount fundBankAcc : allFundBankAcc) {
				String key = fundBankAcc.getBankAccTypeId()+ "|" +
								fundBankAcc.getBankAccountId()+ "|" +
									fundBankAcc.getFundCode();
				newCache.put(key, fundBankAcc);
			}
			
			this.CACHE_MAP = newCache;
		}
	}		
}
