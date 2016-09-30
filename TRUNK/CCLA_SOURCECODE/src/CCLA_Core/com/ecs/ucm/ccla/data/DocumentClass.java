package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the UCMADMIN.DOCUMENT_CLASSES table.
 * 
 * @author Tom
 *
 */
public class DocumentClass {
	
	/** Internal SCHPRIMARYKEY column */
	private int id;
	
	private String name;
	private String description;
	
	private boolean submitToSpp;
	private boolean supporting;
	private boolean multiDoc;
	private boolean requireAccountData;
	private boolean transaction;
	private boolean standingOrder;
	private boolean signaturesRequired;
	private boolean mandate;
	
	public static class Cols {
		public static final String ID = "SCHPRIMARYKEY";
		public static final String NAME = "DOC_CLASS";
		public static final String DESCRIPTION = "DESCRIPTION";
		
		public static final String SUBMIT_TO_SPP = "SUBMIT_TO_SPP";
		public static final String IS_SUPPORTING = "IS_SUPPORTING";
		public static final String IS_MULTIDOC = "IS_MULTIDOC";
		public static final String REQUIRE_ACCOUNT_DATA = "REQUIRE_ACCOUNT_DATA";
		public static final String IS_TRANSACTION = "IS_TRANSACTION";
		public static final String IS_STANDING_ORDER = "IS_STANDING_ORDER";
		public static final String SIGNATURES_REQUIRED = "SIGNATURES_REQUIRED";
		public static final String IS_MANDATE = "IS_MANDATE";
	}
	
	public static class Classes {
		// Transactions (Deposit)
		public static final String DEPBNK = "DEPBNK";
		public static final String DEPCHQ = "DEPCHQ";
		
		// Transactions (Transfer)
		public static final String BUYDF = "BUYDF"; 
		
		// Specials
		public static final String PREADVICE = "PREADVICE";
		
		// Multi-instruction container class
		public static final String MULTIDOC = "MULTIDOC";
		// Invalid form/instruction
		public static final String CONDINS = "CONDINS";
		
		// Multi-invoice container class
		public static final String MULTIINV = "MULTIINV";
		
		public static final String INV = "INV";

		public static final String AUTOMAND = "AUTOMAND";
	}
	
	public DocumentClass(String name, String description,
			boolean submitToSpp, boolean supporting, boolean multiDoc,
			boolean requireAccountData, boolean transaction,
			boolean standingOrder, boolean signaturesRequired, boolean mandate) {
		//this.id = id;
		this.name = name;
		this.description = description;
		this.submitToSpp = submitToSpp;
		this.supporting = supporting;
		this.multiDoc = multiDoc;
		this.requireAccountData = requireAccountData;
		this.transaction = transaction;
		this.standingOrder = standingOrder;
		this.signaturesRequired = signaturesRequired;
		this.mandate = mandate;
	}
	
	public static DocumentClass get(DataResultSet rs) throws DataException {
		return new DocumentClass(
			//CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getStringValueByName(Cols.NAME),
			rs.getStringValueByName(Cols.DESCRIPTION),
			CCLAUtils.getResultSetBoolValue(rs, Cols.SUBMIT_TO_SPP),
			CCLAUtils.getResultSetBoolValue(rs, Cols.IS_SUPPORTING),
			CCLAUtils.getResultSetBoolValue(rs, Cols.IS_MULTIDOC),
			CCLAUtils.getResultSetBoolValue(rs, Cols.REQUIRE_ACCOUNT_DATA),
			CCLAUtils.getResultSetBoolValue(rs, Cols.IS_TRANSACTION),
			CCLAUtils.getResultSetBoolValue(rs, Cols.IS_STANDING_ORDER),
			CCLAUtils.getResultSetBoolValue(rs, Cols.SIGNATURES_REQUIRED),
			CCLAUtils.getResultSetBoolValue(rs, Cols.IS_MANDATE)
		);
	}
	
	public static Vector<DocumentClass> getAll(FWFacade facade) throws DataException {
		DataResultSet rs = getAllData(facade);
		Vector<DocumentClass> v = new Vector<DocumentClass>();
		
		if (rs.first()) {
			do {
				v.add(get(rs));
			} while (rs.next());
		}
		
		return v;
	}
	
	public static DataResultSet getAllData(FWFacade facade) throws DataException {
		return facade.createResultSet("QDocClasses", new DataBinder());
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isSubmitToSpp() {
		return submitToSpp;
	}

	public boolean isSupporting() {
		return supporting;
	}

	public boolean isMultiDoc() {
		return multiDoc;
	}

	public boolean isRequireAccountData() {
		return requireAccountData;
	}

	public boolean isTransaction() {
		return transaction;
	}

	public boolean isStandingOrder() {
		return standingOrder;
	}

	public boolean isSignaturesRequired() {
		return signaturesRequired;
	}

	public boolean isMandate() {
		return mandate;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<String, DocumentClass> getCache() {
		return CACHE;
	}
	
	/** DocumentClass cache implementor.
	 *  
	 **/
	private static class Cache extends Cachable<String, DocumentClass> {

		public Cache() {
			super("Document Class");
			this.setConnectionName(null);
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<DocumentClass> v = getAll(facade);
			
			HashMap<String, DocumentClass> newCache = 
			 new HashMap<String, DocumentClass>();
			
			for (DocumentClass docClass : v)
				newCache.put(docClass.getName(), docClass);
			
			this.CACHE_MAP = newCache;
		}
	}
}
