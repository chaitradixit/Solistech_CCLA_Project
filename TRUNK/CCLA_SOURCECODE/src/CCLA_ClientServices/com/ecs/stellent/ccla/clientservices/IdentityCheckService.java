package com.ecs.stellent.ccla.clientservices;

import java.util.Collection;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementAttributeType;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.IdentityCheck;
import com.ecs.ucm.ccla.data.IdentityCheckScore;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.ElementAttribute.SelectionType;
import com.ecs.ucm.ccla.data.Person.PersonIdentityCheck;
import com.ecs.ucm.ccla.experian.AuthenticationScoreUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class IdentityCheckService extends Service {
	
	/** Fetches full Identity Checking information for the given Person ID.
	 *  
	 *  This is designed for use on the Identity Check Details popup.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void getIdentityCheck() throws DataException, ServiceException {
		
		String personIdStr = m_binder.getLocal("PERSON_ID");
		
		if (StringUtils.stringIsBlank(personIdStr))
			throw new ServiceException("Unable to fetch Identity Check " +
			 "details, no Person ID found");

		int personId = Integer.parseInt(personIdStr);
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Log.debug("Fetching Identity Check info for Person ID " + personId);
		
		// Add the single corresponding entry from the Identity Check table
		DataResultSet rsIdentityCheck = IdentityCheck.getData(personId, facade);
		m_binder.addResultSet("rsIdentityCheck", rsIdentityCheck);
		
		IdentityCheck identityCheck = null;
		
		if (rsIdentityCheck.first())
			identityCheck = IdentityCheck.get(rsIdentityCheck);
		
		if (identityCheck != null) {
			DataResultSet rsScoreDescriptions = getScoreDescriptions
			 (identityCheck, facade);
			
			m_binder.addResultSet
			 ("rsScoreDescriptions", rsScoreDescriptions);
		}
		
		PersonIdentityCheck idCheck = 
		 Person.getIdentityCheckFromAttributes(ElementAttributeApplied.getAll
		 (personId, ElementAttributeType.getCache().getCachedInstance
		 (ElementAttributeType.PERSON_IVS_CHECKING), false, facade));
		
		Log.debug(idCheck.toString());
		
		DataResultSet rsElementAttributes = ElementAttribute.getElementAttributesData
		 (ElementType.PERSON, SelectionType.ALL, facade);
		
		m_binder.addResultSet("rsElementAttributes", rsElementAttributes);
	
		// Add any relevant applied attributes
		DataResultSet rsPersonIVSAttributes = ElementAttributeApplied.getAllData
		 (personId, ElementAttributeType.getCache().getCachedInstance
		 (ElementAttributeType.PERSON_IVS_CHECKING), false, facade);
		
		m_binder.addResultSet("rsElementAttributesApplied", rsPersonIVSAttributes);
	}
	
	/** Returns a ResultSet of the user's broken-down Happy/Unhappy score 
	 *  values. The ResultSet has 2 columns:
	 *  
	 *  ScoreType: 		either 'Positive' or 'Negative'
	 *  Description:	corresponding score description
	 *  
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getScoreDescriptions 
	 (IdentityCheck identityCheck, FWFacade facade) throws DataException {
		
		Log.debug("gettting score descriptions");
		
		String[] myCols = new String[] {"ScoreType","Description"};
		DataResultSet rsScoreDescriptions = new DataResultSet(myCols);
		
		Integer personHappyScore 	= identityCheck.getHappyScore();
		Integer personUnhappyScore	= identityCheck.getUnhappyScore();
		
		// Fetch all cached scores.
		Collection<IdentityCheckScore> scores = 
		 IdentityCheckScore.getCache().getCache().values();
		
		for (IdentityCheckScore checkScore : scores) {
			Vector<String> rsValues = new Vector<String>();
			
			if (personHappyScore != null 
				&& 
				checkScore.getHappyScore() != null) {
				
				if (AuthenticationScoreUtils.getScoreContains
					(checkScore.getHappyScore(), personHappyScore)) {
					rsValues.add("Positive");
					rsValues.add(checkScore.getDescription());
				}
			} else if (personUnhappyScore != null
					   &&
					   checkScore.getUnhappyScore() != null) {
				
				if (AuthenticationScoreUtils.getScoreContains
					(checkScore.getUnhappyScore(), personUnhappyScore)) {
					rsValues.add("Negative");
					rsValues.add(checkScore.getDescription());
				}
			}
			
			// Check if we've found a matching Positive/Negative score for this 
			// Person.
			if (rsValues.size() > 0)
				rsScoreDescriptions.addRow(rsValues);
		}
		
		return rsScoreDescriptions;
	}
	
	/** Service method for recalculating all ID check scores for records requiring
	 *  recalculation.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void recalculateAll() throws DataException, ServiceException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		AuthenticationScoreUtils.recalculateAll(facade);
	}
	
	/** Service method for forcing recalculation on a single Person Identity Check,
	 *  regardless of their current recalculation date.
	 * @throws DataException 
	 *  
	 */
	public void forceRecalculate() throws DataException {
		
		int personId = CCLAUtils.getBinderIntegerValue(m_binder, SharedCols.PERSON);
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		IdentityCheck identityCheck = IdentityCheck.get(personId, facade);
		
		if (identityCheck == null)
			identityCheck = IdentityCheck.add(personId, facade);
		
		AuthenticationScoreUtils.recalculateCheckResult(identityCheck, facade);
	}
}
