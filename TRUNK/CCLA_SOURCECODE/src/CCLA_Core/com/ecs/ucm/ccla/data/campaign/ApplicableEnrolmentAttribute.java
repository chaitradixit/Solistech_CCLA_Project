package com.ecs.ucm.ccla.data.campaign;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the APPLICABLE_ENROLMENT_ATTR table.
 * 
 * @author Tom
 *
 */
public class ApplicableEnrolmentAttribute {

	public static class Ids {
		/** Seg. Client campaign attribs */
		public static final int SEG_CLIENT_BANK_ACCOUNT = 1;
		public static final int SEG_CLIENT_NUM_SIGS = 2;
		public static final int SEG_CLIENT_MIN_AUTH_PERSONS = 3;
		public static final int SEG_CLIENT_MIN_SIGNATORIES = 4;
		
		/** Community First campaign attribs */
		public static final int COMM_FIRST_BANK_ACCOUNT = 5;
		public static final int COMM_FIRST_MIN_AUTH_PERSONS = 6;
		public static final int COMM_FIRST_MIN_SIGNATORIES = 7;
		
		/** CBF Client Confirmation attribs */
		public static final int CBF_CLIENT_CONF_ACCOUNT = 8;
	}
	
	public static class Cols {
		public static final String ID 				= "APPLENROLATTR_ID";
		public static final String ATTRIBUTE_ID 	= "ENROLMENT_ATTRIBUTE_ID";
		public static final String CAMPAIGN_ID 		= "CAMPAIGN_ID";
	}
	
	private int id;
	private EnrolmentAttribute attribute;
	private int campaignId;
	
	public ApplicableEnrolmentAttribute
	 (int id, EnrolmentAttribute attribute, int campaignId) {
		this.id = id;
		this.setAttribute(attribute);
		this.campaignId = campaignId;
	}
	
	public static DataResultSet getAllData(FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		return facade.createResultSet
		 ("qCore_GetAllApplicableEnrolmentAttributes", binder);
	}
	
	public static DataResultSet getDataByCampaignId(int campaignId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.CAMPAIGN_ID, campaignId);
		
		return facade.createResultSet
		 ("qCore_GetApplicableEnrolmentAttributesByCampaignId", binder);
	}
	
	public static Vector<ApplicableEnrolmentAttribute> getAll(FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getAllData(facade);
		Vector<ApplicableEnrolmentAttribute> v = 
		 new Vector<ApplicableEnrolmentAttribute>();
		
		if (rs.first()) {
			do {
				v.add(get(rs));
			} while (rs.next());
		}
		
		return v;
	}
	
	public static ApplicableEnrolmentAttribute get(DataResultSet rs) 
	 throws DataException {
		
		if (rs == null) {
			return null;
		} else {
			return new ApplicableEnrolmentAttribute(
				CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
				EnrolmentAttribute.getCache().getCachedInstance(
				 CCLAUtils.getResultSetIntegerValue(rs, Cols.ATTRIBUTE_ID)),
				CCLAUtils.getResultSetIntegerValue(rs, Cols.CAMPAIGN_ID)
			);
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}
	public void setAttribute(EnrolmentAttribute attribute) {
		this.attribute = attribute;
	}
	public EnrolmentAttribute getAttribute() {
		return attribute;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	/** Cache of ApplicableEnrolmentAttributes.
	 * 
	 * @return
	 */
	public static Cachable<Integer, ApplicableEnrolmentAttribute> getCache() {
		return CACHE;
	}
	
	private static CampaignCache CAMPAIGN_CACHE = new CampaignCache();
	
	/** Cache of Campaign IDs to Vectors of ApplicableEnrolmentAttributes.
	 * 
	 * @return
	 */
	public static Cachable<Integer, Vector<ApplicableEnrolmentAttribute>> 
	 getCampaignCache() {
		return CAMPAIGN_CACHE;
	}

	private static class Cache extends Cachable
	 <Integer, ApplicableEnrolmentAttribute> {

		public Cache() {
			super("Applicable Enrolment Attributes");
		}
		
		public void doRebuild(FWFacade facade) throws DataException {
			Vector<ApplicableEnrolmentAttribute> attribs = 
			 getAll(facade);
			
			HashMap<Integer, ApplicableEnrolmentAttribute> newCache = 
			 new HashMap<Integer, ApplicableEnrolmentAttribute>();
			
			for (ApplicableEnrolmentAttribute attrib : attribs) {
				newCache.put(attrib.getId(), attrib);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	
	private static class CampaignCache extends Cachable
	 <Integer, Vector<ApplicableEnrolmentAttribute>> {

		public CampaignCache() {
			super("Campaign ID -> Applicable Enrolment Attributes");
		}
		
		public void doRebuild(FWFacade facade) throws DataException {
			Vector<ApplicableEnrolmentAttribute> attribs = 
			 getAll(facade);
			
			HashMap<Integer, Vector<ApplicableEnrolmentAttribute>> newCache = 
			 new HashMap<Integer, Vector<ApplicableEnrolmentAttribute>>();
			
			for (ApplicableEnrolmentAttribute attrib : attribs) {
				Vector<ApplicableEnrolmentAttribute> campaignAttribs = 
				 newCache.get(attrib.getCampaignId());
				
				if (campaignAttribs == null) {
					campaignAttribs = new Vector<ApplicableEnrolmentAttribute>();
					newCache.put(attrib.getCampaignId(), campaignAttribs);
				}
				
				campaignAttribs.add(attrib);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
