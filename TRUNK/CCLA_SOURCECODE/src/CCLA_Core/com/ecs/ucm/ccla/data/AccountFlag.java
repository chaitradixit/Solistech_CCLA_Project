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


/** Models entries from the REF_ACCOUNT_FLAG table.
 * 
 * @author Tom
 *
 */
public class AccountFlag {
	
	/** Hard-coded Account Flag IDs, for easy reference */
	public static class Ids {
		public static final int NO_ISSUE = 1;
		public static final int GENERATE_CS_LETTER = 10;
	}
	
	private int accountFlagId;
	private String name;
	private boolean addToWatchList;
	
	public AccountFlag(int accountFlagId, String name, boolean addToWatchList) {
		this.accountFlagId = accountFlagId;
		this.name = name;
		this.addToWatchList = addToWatchList;
	}
	
	public static class Cols {
		public static final String ID 	= "ACCOUNT_FLAG_ID";
		public static final String NAME = "ACCOUNT_FLAG_NAME";
		public static final String ADD_TO_WATCHLIST = "ADD_TO_WATCHLIST";
	}

	public static AccountFlag get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new AccountFlag(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getStringValueByName(Cols.NAME),
			CCLAUtils.getResultSetBoolValue(rs, Cols.ADD_TO_WATCHLIST)
		);
	}
	
	public static Vector<AccountFlag> getAll(DataResultSet rs) throws DataException {
		Vector<AccountFlag> flags = new Vector<AccountFlag>();
		
		if (rs.first()) {
			do {
				flags.add(get(rs));
			} while (rs.next());
		}
		
		return flags;
	}
	
	public static DataResultSet getAll(FWFacade facade) throws DataException {
		
		return facade.createResultSet
		 ("qClientServices_GetAllAccountFlags", new DataBinder());
	}

	public int getAccountFlagId() {
		return accountFlagId;
	}

	public String getName() {
		return name;
	}

	public boolean isAddToWatchList() {
		return addToWatchList;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, AccountFlag> getCache() {
		return CACHE;
	}
	
	/** DataType cache implementor.
	 *  
	 *  Maps DataType names against DataType instances
	 *  
	 **/
	private static class Cache extends Cachable<Integer, AccountFlag> {

		public Cache() {
			super("Account Flag");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {	
			Vector<AccountFlag> accountFlags = getAll(getAll(facade));
			
			HashMap<Integer, AccountFlag> newCache = 
			 new HashMap<Integer, AccountFlag>();
			
			for (AccountFlag accountFlag : accountFlags) {
				newCache.put(accountFlag.getAccountFlagId(), accountFlag);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
