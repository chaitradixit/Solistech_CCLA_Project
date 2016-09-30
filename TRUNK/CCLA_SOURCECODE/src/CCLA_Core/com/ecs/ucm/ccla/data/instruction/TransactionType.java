package com.ecs.ucm.ccla.data.instruction;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus.Cache;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_TRANSACTION_TYPE table.
 *  
 *  There are so few of these, that they are hard-coded at the top of the file.
 *  
 * @author Tom
 *
 */
public class TransactionType implements Persistable {
	
	/** Deposit/buy transaction */
	public static TransactionType BUY = new TransactionType(1, "BUY");
	/** Withdrawal/sell transaction */
	public static TransactionType SELL = new TransactionType(2, "SELL");
	/** Transfer transaction */
	public static TransactionType TRANSFER = new TransactionType(3, "TRANSFER");
	
	private int transactionTypeId;
	private String name;
	
	public TransactionType(int transactionTypeId, String transactionSubTypeName)
	{
		this.transactionTypeId = transactionTypeId;
		this.name = transactionSubTypeName;
	}
	
	public static Vector<TransactionType> getAll(FWFacade facade) throws DataException {
		Vector<TransactionType> transTypes = new Vector<TransactionType>();
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllTransactionTypes", new DataBinder());
		
		if (rs.first()) {
			do {
				transTypes.add(get(rs));
			} while (rs.next());
		}
		
		return transTypes;
	}
	
	public static TransactionType get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;

		return new TransactionType(
			DataResultSetUtils.getResultSetIntegerValue(rs, "TRANSACTION_TYPE_ID"),
			DataResultSetUtils.getResultSetStringValue(rs, "TRANSACTION_TYPE_NAME")
		);
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

	public int getTransactionTypeId() {
		return transactionTypeId;
	}

	public void setTransactionId(int transactionSubTypeId) {
		this.transactionTypeId = transactionSubTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String transactionSubTypeName) {
		this.name = transactionSubTypeName;
	}
	
	public boolean equals(TransactionType type) {
		return (this.getTransactionTypeId() == type.getTransactionTypeId());
	}

	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, TransactionType> getCache() {
		return CACHE;
	}
	
	/** TransactionType cache implementor */
	public static class Cache extends Cachable<Integer, TransactionType> {

		public Cache() {
			super("Transaction Type");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<TransactionType> types = TransactionType.getAll(facade);
			
			HashMap<Integer, TransactionType> newCache = 
			 new HashMap<Integer, TransactionType>();
			
			for (TransactionType type : types) {
				newCache.put(type.getTransactionTypeId(), type);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
