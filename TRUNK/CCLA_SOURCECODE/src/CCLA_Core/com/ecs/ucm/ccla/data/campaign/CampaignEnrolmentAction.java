package com.ecs.ucm.ccla.data.campaign;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** 
 * Models entries from the REF_CAMPAIGN_ENROLMENT_ACTION table.
 * @author Cam
 */
public class CampaignEnrolmentAction {

	
	
	/* **************** Constants **************** */
	//BINDER AND DB COLUMN
	public static final String ID = "ENROLMENT_ACTION_ID";
	public static final String NAME = "ENROLMENT_ACTION_NAME";
	public static final String DESCRIPTION = "ENROLMENT_ACTION_DESCRIPTION";
	
	//Actions, this should mirror the ENROLMENT_ACTION_ID 
	//Important Add to this list and also to the array.
	public static final int ENROL_ACTION						= 1;
	public static final int EXCLUDE_ACTION						= 2;
	public static final int MARK_INTEREST_ACTION				= 3;
	public static final int MARK_NOT_INTEREST_ACTION			= 4;
	public static final int MARK_UNDECIDED_ACTION				= 5;
	public static final int MARK_NO_RESPONSE_ACTION				= 6;
	public static final int MARK_AWAIT_ELIGIBILITY_ACTION		= 7;	
	public static final int MARK_INELIGIBLE						= 8;
	public static final int MARK_INTENTIONS_CONFIRMED			= 9;
	public static final int GENERATE_FORM_ACTION				= 10;
	public static final int PRINT_FORM_ACTION					= 11;
	public static final int GENERATE_AND_PRINT_FORM_ACTION		= 12;
	public static final int REGENERATE_FORM_ACTION				= 13;
	public static final int REPRINT_FORM_ACTION					= 14;
	public static final int REGENERATE_AND_PRINT_FORM_ACTION	= 15;
	public static final int FORM_DISPATCHED_ACTION				= 16;
	public static final int FORM_RETURNED_ACTION				= 17;
	public static final int MARK_COMPLETED_ACTION				= 18;
	public static final int ISSUE_RETURNED_FORMED_ACTION		= 19;
	public static final int AWAIT_MORE_INFO_ACTION				= 20;
	public static final int INTENTIONS_NOT_CONFIRMED_ACTION		= 21;
	public static final int GENERATE_LA_FORM_ACTION				= 22;
	public static final int GENERATE_NON_LA_FORM_ACTION			= 23;
	public static final int REGENERATE_LA_FORM_ACTION			= 24;
	public static final int REGENERATE_NON_LA_FORM_ACTION		= 25;
	public static final int GENERATE_ONBOARDING_COVERING_LETTER = 26;
	public static final int GENERATE_INFORMATION_FORM 			= 27;
	public static final int GENERATE_CONFIRMATION_FORM 			= 28;
	
	
	public static final int[] AVAILABLE_ACTIONS = 
	{
		ENROL_ACTION, EXCLUDE_ACTION, MARK_INTEREST_ACTION,
		MARK_NOT_INTEREST_ACTION, MARK_UNDECIDED_ACTION, MARK_NO_RESPONSE_ACTION,
		MARK_AWAIT_ELIGIBILITY_ACTION, MARK_INELIGIBLE, MARK_INTENTIONS_CONFIRMED,
		GENERATE_FORM_ACTION, PRINT_FORM_ACTION, GENERATE_AND_PRINT_FORM_ACTION, 
		REGENERATE_FORM_ACTION, REPRINT_FORM_ACTION, REGENERATE_AND_PRINT_FORM_ACTION, 
		FORM_DISPATCHED_ACTION, FORM_RETURNED_ACTION, MARK_COMPLETED_ACTION, 
		ISSUE_RETURNED_FORMED_ACTION, AWAIT_MORE_INFO_ACTION, INTENTIONS_NOT_CONFIRMED_ACTION,
		GENERATE_LA_FORM_ACTION, GENERATE_NON_LA_FORM_ACTION,
		REGENERATE_LA_FORM_ACTION, REGENERATE_NON_LA_FORM_ACTION, 
		GENERATE_ONBOARDING_COVERING_LETTER, GENERATE_INFORMATION_FORM, 
		GENERATE_CONFIRMATION_FORM
	};
	
	//QUERIES
	private static final String GET_ALL_QUERY_NAME = "qCore_GetAllCampaignEnrolmentAction";
	
	/* **************** Properties **************** */
	private int actionId;
	private String name;
	private String description;
	
	/* ********************** Constructor ************************* */
	/**
	 * Constructor
	 * @param statusId
	 * @param name
	 * @param description
	 */
	public CampaignEnrolmentAction(int actionId, String name, String description) {
		this.actionId = actionId;
		this.name = name;
		this.description = description;
	}
	
	/* ************************* Methods ************************* */
	public int getActionId() { return actionId; }
	public void setActionId(int actionId) { this.actionId = actionId; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	/**
	 * Gets a Vector of CampaignEnrolmentAction.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<CampaignEnrolmentAction> getAll(FWFacade facade) throws DataException {
		Vector<CampaignEnrolmentAction> statuses = new Vector<CampaignEnrolmentAction>();
		
		DataResultSet rs = facade.createResultSet
		 (GET_ALL_QUERY_NAME, new DataBinder());
		
		if (rs.first()) {
			do {
				statuses.add(get(rs));
			} while (rs.next());
		}
		return statuses;
	}
	
	/**
	 * Returns a CampaignEnrolmentAction object from the DataResultSet or null if empty
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static CampaignEnrolmentAction get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new CampaignEnrolmentAction(
			DataResultSetUtils.getResultSetIntegerValue(rs, ID),
			DataResultSetUtils.getResultSetStringValue(rs, NAME),
			DataResultSetUtils.getResultSetStringValue(rs, DESCRIPTION)
		);
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, CampaignEnrolmentAction> getCache() {
		return CACHE;
	}
	
	/** CampaignEnrolmentAction cache implementor */
	private static class Cache extends Cachable<Integer, CampaignEnrolmentAction> {

		public Cache() {
			super("Campaign Enrolment Action");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<CampaignEnrolmentAction> actions = CampaignEnrolmentAction.getAll(facade);
			
			HashMap<Integer, CampaignEnrolmentAction> newCache = 
			 new HashMap<Integer, CampaignEnrolmentAction>();
			
			for (CampaignEnrolmentAction action : actions) {
				newCache.put(action.getActionId(), action);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
