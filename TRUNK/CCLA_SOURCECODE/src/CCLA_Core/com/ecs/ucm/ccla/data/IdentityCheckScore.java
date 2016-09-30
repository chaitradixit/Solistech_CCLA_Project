package com.ecs.ucm.ccla.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the CCLA_IDENTITY_CHECK_LOOKUP table.
 * 
 *  These are all cached on server startup, see the CacheManager class.
 *  
 * @author Tom
 *
 */
public class IdentityCheckScore implements Persistable {

	/** The outcome of this Check Score. Can be null, this implies the
	 * score has no impact on Identity Check results.
	 */
	public enum Condition {
		PASS,
		FAIL,
		AMBER,
		NONE
	}
	
	public enum ScoreType {
		HAPPY_SCORE, /* Pass scores */
		UNHAPPY_SCORE /* Amber/fail scores */
	}
	
	private int outcomeId;
	
	private String description;
	private String experianRiskCode;
	private String experianDecisionCode;
	private String experianDecisionText;
	
	private Integer happyScore;
	private Integer unhappyScore;
	
	private boolean failCondition;
	private boolean passCondition;
	private boolean amberCondition;
	
	/** Element Attribute to add/remove against the associated Person record, when this
	 *  score is applied.
	 */
	private ElementAttribute elementAttribute;
	
	private Condition condition;
	
	public IdentityCheckScore(int outcomeId, String description,
			String experianRiskCode, String experianDecisionCode,
			String experianDecisionText, Integer happyScore, Integer unhappyScore,
			boolean failCondition, boolean passCondition, boolean amberCondition, 
			ElementAttribute elementAttribute) {
		this.outcomeId = outcomeId;
		this.description = description;
		this.experianRiskCode = experianRiskCode;
		this.experianDecisionCode = experianDecisionCode;
		this.experianDecisionText = experianDecisionText;
		this.happyScore = happyScore;
		this.unhappyScore = unhappyScore;
		this.failCondition = failCondition;
		this.passCondition = passCondition;
		this.amberCondition = amberCondition;
		this.setElementAttribute(elementAttribute);
		
		if (failCondition)
			this.condition = Condition.FAIL;
		else if (passCondition)
			this.condition = Condition.PASS;
		else if (amberCondition)
			this.condition = Condition.AMBER;
	}
	
	/** Fetches all Identity Check attributes from the database and converts
	 *  them to IdentityCheckAttribute instances.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<IdentityCheckScore> getAll(FWFacade facade)
	 throws DataException {
		Vector<IdentityCheckScore> attributes =
		 new Vector<IdentityCheckScore>();
		
		DataBinder binder = new DataBinder();
		
		DataResultSet rsAttributes = facade.createResultSet
		("qClientServices_GetAllAuthenticationConditions", binder);
		
		if (rsAttributes.first()) {
			do {
				attributes.add(get(rsAttributes));
			} while (rsAttributes.next());
		}
		
		return attributes;
	}
	
	public static IdentityCheckScore get(String ScoreType, int Score, FWFacade facade) throws DataException
	{
		DataResultSet rs = null;
		DataBinder binder = new DataBinder();
		
		if (ScoreType.equalsIgnoreCase("HAPPY"))
		{
			BinderUtils.addIntParamToBinder(binder, "HAPPY_SCORE", Score);
			rs = facade.createResultSet("qClientServices_GetIdentityCheckHappyScoreByValue", binder);
		} else
		{
			BinderUtils.addIntParamToBinder(binder, "UNHAPPY_SCORE", Score);
			rs = facade.createResultSet("qClientServices_GetIdentityCheckUnHappyScoreByValue", binder);			
		}
		
		if (rs == null || rs.isEmpty())
			return null;
		else
			return get(rs);
	}
	
	public static IdentityCheckScore get(DataResultSet rs) throws DataException {
		Integer elementAttrId = CCLAUtils.getResultSetIntegerValue
		 (rs, "ELEMENT_ATTRIBUTE_ID");
		
		return new IdentityCheckScore(
		 CCLAUtils.getResultSetIntegerValue(rs, "OUTCOME_ID"),
		 rs.getStringValueByName("DESCRIPTION"),
		 
		 rs.getStringValueByName("EXPERIAN_RISK_CODE"),
		 rs.getStringValueByName("EXPERIAN_DECISION_CODE"),
		 rs.getStringValueByName("EXPERIAN_DECISION_TEXT"),
		 
		 CCLAUtils.getResultSetIntegerValue(rs, "HAPPY_SCORE"),
		 CCLAUtils.getResultSetIntegerValue(rs, "UNHAPPY_SCORE"),
	
		 CCLAUtils.getResultSetBoolValue(rs, "FAIL_CONDITION"),
		 CCLAUtils.getResultSetBoolValue(rs, "PASS_CONDITION"),
		 CCLAUtils.getResultSetBoolValue(rs, "AMBER_CONDITION"),
		 
		 elementAttrId != null ? ElementAttribute.getCache()
		  .getCachedInstance(elementAttrId) : null 
		 );
	}

	/** First parameter is the full score, second is the value to check if the full score contains
	 * 
	 * @param checkScore
	 * @param containsScore
	 * @return
	 */
	public static boolean scoreContains(int checkScore, int containsScore)
	{
		boolean hasScore = false;
		
		if ((checkScore % containsScore) == 0)
			hasScore = true;
		
		return hasScore;
	}
	
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		this.validate(facade);
		
		throw new DataException("Not implemented");
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public int getOutcomeId() {
		return outcomeId;
	}

	public void setOutcomeId(int outcomeId) {
		this.outcomeId = outcomeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExperianRiskCode() {
		return experianRiskCode;
	}

	public void setExperianRiskCode(String experianRiskCode) {
		this.experianRiskCode = experianRiskCode;
	}

	public String getExperianDecisionCode() {
		return experianDecisionCode;
	}

	public void setExperianDecisionCode(String experianDecisionCode) {
		this.experianDecisionCode = experianDecisionCode;
	}

	public String getExperianDecisionText() {
		return experianDecisionText;
	}

	public void setExperianDecisionText(String experianDecisionText) {
		this.experianDecisionText = experianDecisionText;
	}

	public Integer getHappyScore() {
		return happyScore;
	}

	public void setHappyScore(Integer happyScore) {
		this.happyScore = happyScore;
	}

	public Integer getUnhappyScore() {
		return unhappyScore;
	}

	public void setUnhappyScore(Integer unhappyScore) {
		this.unhappyScore = unhappyScore;
	}

	public boolean isFailCondition() {
		return failCondition;
	}

	public void setFailCondition(boolean failCondition) {
		this.failCondition = failCondition;
	}

	public boolean isPassCondition() {
		return passCondition;
	}

	public void setPassCondition(boolean passCondition) {
		this.passCondition = passCondition;
	}

	public boolean isAmberCondition() {
		return amberCondition;
	}

	public void setAmberCondition(boolean amberCondition) {
		this.amberCondition = amberCondition;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, IdentityCheckScore> getCache() {
		return CACHE;
	}
	
	/** DataType cache implementor.
	 *  
	 *  Maps Outcome IDs to Identity Check Scores
	 *  
	 **/
	private static class Cache extends Cachable<Integer, IdentityCheckScore> {

		public Cache() {
			super("Outcome ID -> Identity Check Score");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			HashMap<Integer, IdentityCheckScore> newCache = 
			 new HashMap<Integer, IdentityCheckScore>();
			
			Vector<IdentityCheckScore> checkScores = IdentityCheckScore.getAll(facade);
			
			for (IdentityCheckScore checkScore : checkScores) {
				newCache.put(checkScore.getOutcomeId(), checkScore);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	
	private static ConditionCache CONDITION_CACHE = new ConditionCache();
	
	public static Cachable<IdentityCheckScore.Condition, Vector<IdentityCheckScore>>
	 getConditionCache() {
		return CONDITION_CACHE;
	}
	
	public void setElementAttribute(ElementAttribute elementAttribute) {
		this.elementAttribute = elementAttribute;
	}

	public ElementAttribute getElementAttribute() {
		return elementAttribute;
	}

	/** DataType cache implementor.
	 *  
	 *  Maps Conditions to Identity Check Scores. Dependant on Outcome ID cache above.
	 *  
	 **/
	private static class ConditionCache 
	 extends Cachable<IdentityCheckScore.Condition,  Vector<IdentityCheckScore>> {

		public ConditionCache() {
			super("Condition -> Identity Check Score");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			HashMap<IdentityCheckScore.Condition, Vector<IdentityCheckScore>> newCache = 
			 new HashMap<IdentityCheckScore.Condition, Vector<IdentityCheckScore>>();
			
			Collection<IdentityCheckScore> checkScores = CACHE.getCache().values();
			
			for (IdentityCheckScore checkScore : checkScores) {
				IdentityCheckScore.Condition condition = checkScore.getCondition();
				
				if (condition == null)
					condition = IdentityCheckScore.Condition.NONE;
				
				Vector<IdentityCheckScore> conditionCheckScores = 
				 newCache.get(condition);
				
				if (conditionCheckScores == null) {
					conditionCheckScores = new Vector<IdentityCheckScore>();
					newCache.put(condition, conditionCheckScores);
				}
				
				conditionCheckScores.add(checkScore);
			}
				
			this.CACHE_MAP = newCache;
		}
	}

}
