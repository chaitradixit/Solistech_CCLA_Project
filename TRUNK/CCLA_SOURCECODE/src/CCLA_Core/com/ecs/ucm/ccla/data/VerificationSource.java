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

/** Models entries in the REF_VERIFICATION_SOURCE table.
 * 
 *  These are cached at server startup - see the CacheManager class
 *  
 * @author Tom
 *
 */
public class VerificationSource implements Persistable {
	
	private int verificationSourceId;
	private String sourceName;
	private String description;
	
	private boolean supportingDocRequired;
	private boolean override;
	
	public static class SourceIds {
		/** Authorisation for Dio Loan transactions */
		public static final int DIO_LOAN_RESOLUTION = 19;
	}
	
	public VerificationSource(int verificationSourceId, String sourceName,
			String description, boolean supportingDocRequired, boolean override) {
		this.verificationSourceId = verificationSourceId;
		this.sourceName = sourceName;
		this.description = description;
		this.supportingDocRequired = supportingDocRequired;
		this.override = override;
	}
	
	public static Vector<VerificationSource> getAll(FWFacade facade) 
	 throws DataException {
		Vector<VerificationSource> verSources = new Vector<VerificationSource>();
		
		DataResultSet rs = facade.createResultSet
		 ("qClientServices_GetVerificationSources", new DataBinder());
		
		if (rs.first()) {
			do {
				verSources.add(get(rs));
			} while (rs.next());
		}
		
		return verSources;
	}
	
	public static VerificationSource get(DataResultSet rs) throws DataException {
		return new VerificationSource(
			CCLAUtils.getResultSetIntegerValue(rs, "VERIFICATION_SOURCE_ID"),
			rs.getStringValueByName("VER_SOURCE_NAME"),
			rs.getStringValueByName("VER_SOURCE_DESCRIPTION"),
			CCLAUtils.getResultSetBoolValue(rs, "SUPPORTING_DOC_REQUIRED"),
			CCLAUtils.getResultSetBoolValue(rs, "IS_OVERRIDE")
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}

	public int getVerificationSourceId() {
		return verificationSourceId;
	}

	public void setVerificationSourceId(int verificationSourceId) {
		this.verificationSourceId = verificationSourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSupportingDocRequired() {
		return supportingDocRequired;
	}

	public void setSupportingDocRequired(boolean supportingDocRequired) {
		this.supportingDocRequired = supportingDocRequired;
	}

	public boolean isOverride() {
		return override;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, VerificationSource> getCache() {
		return CACHE;
	}
	
	/** Verification Source cache implementor.
	 *  
	 *  Maps Verification Source IDs to Verification Sources
	 *  
	 **/
	private static class Cache extends Cachable<Integer, VerificationSource> {

		public Cache() {
			super("Verification Source");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			HashMap<Integer, VerificationSource> newCache = 
			 new HashMap<Integer, VerificationSource>();
			
			Vector<VerificationSource> verSources = VerificationSource.getAll(facade);
			
			for (VerificationSource verSource : verSources) {
				newCache.put(verSource.getVerificationSourceId(), verSource);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
