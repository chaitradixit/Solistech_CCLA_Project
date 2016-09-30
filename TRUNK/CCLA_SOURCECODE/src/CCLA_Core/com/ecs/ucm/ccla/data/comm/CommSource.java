package com.ecs.ucm.ccla.data.comm;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_COMM_SOURCE table.
 * 
 * @author Tom
 *
 */
public class CommSource implements Persistable {
	
	// Document Sources
	public static final CommSource POST = new CommSource(1, "Post");
	public static final CommSource FAX  = new CommSource(2, "Fax");
	public static final CommSource EMAIL = new CommSource(3, "Email");
	public static final CommSource BANK = new CommSource(4, "Bank");
	public static final CommSource USER = new CommSource(5, "User");

	// Interaction Sources
	public static final CommSource INCOMING_CALL = new CommSource(6, "Incoming Call");
	public static final CommSource OUTGOING_CALL = new CommSource(7, "Outgoing Call");
	
	// Auto generated sources
	public static final CommSource SYSTEM = new CommSource(8, "System generated");
	
	
	/** List of available metadata values for the xSource field.
	 *  
	 *  These are all mapped to an equivalent CommSource instance in the docSouceMap
	 *  HashMap.
	 *  
	 * @author Tom
	 *
	 */
	public static final class UCMDocSources {
		public static final String SCANNED_FOR_IRIS = "Scanned for Iris";
		public static final String FAX = "Fax";
		public static final String EMAIL = "Email";
		public static final String BANK = "Bank";
		public static final String USER_UPLOAD = "User Upload";
	}
	
	private static HashMap<String, CommSource> docSourceMap;
	
	static {
		docSourceMap = new HashMap<String, CommSource>();
		
		docSourceMap.put(UCMDocSources.SCANNED_FOR_IRIS, POST);
		docSourceMap.put(UCMDocSources.FAX, FAX);
		docSourceMap.put(UCMDocSources.EMAIL, EMAIL);
		docSourceMap.put(UCMDocSources.BANK, BANK);
		docSourceMap.put(UCMDocSources.USER_UPLOAD, USER);
	}
	
	private int commSourceId;
	private String name;
	
	public CommSource(int commSourceId, String name) {
		this.commSourceId = commSourceId;
		this.name = name;
	}

	public static Vector<CommSource> getAll(FWFacade facade) throws DataException {
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllCommSources", new DataBinder());
		
		Vector<CommSource> commSources = new Vector<CommSource>();
		
		if (rs.first()) {
			do {
				commSources.add(get(rs));
			} while (rs.next());
		}
		
		return commSources;
	}
	
	public static CommSource get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new CommSource(
			DataResultSetUtils.getResultSetIntegerValue(rs, "COMM_SOURCE_ID"),
			DataResultSetUtils.getResultSetStringValue(rs, "COMM_SOURCE_NAME")
		);
	}
	
	public static CommSource getByDocumentSource(String xSource) {
		return docSourceMap.get(xSource);
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

	public void setCommSourceId(int commSourceId) {
		this.commSourceId = commSourceId;
	}

	public int getCommSourceId() {
		return commSourceId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public boolean equals(CommSource source) {
		return (this.getCommSourceId() == source.getCommSourceId());
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, CommSource> getCache() {
		return CACHE;
	}
	
	/** CommSource cache implementor */
	private static class Cache extends Cachable<Integer, CommSource> {

		public Cache() {
			super("Comm Source");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<CommSource> commSources = CommSource.getAll(facade);
			
			HashMap<Integer, CommSource> newCache = 
			 new HashMap<Integer, CommSource>();
			
			for (CommSource source : commSources) {
				newCache.put(source.getCommSourceId(), source);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
