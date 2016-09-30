package com.ecs.ucm.ccla.data;

import java.util.Date;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.experian.AuthenticationScoreUtils;
import com.ecs.ucm.ccla.experian.ExperianGlobals;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class IdentityCheck implements Persistable {

	// Overall authentication result, stored in IS_AUTHENTICATED field.
	public static final int FAILED = 0;
	public static final int PASSED = 1;
	public static final int UNCHECKED = 2;
	
	//Risk Codes
	public static final String RISK_CODE_UNKNOWN_ADDRESS 				= "U000";
	public static final String RISK_CODE_NAME_ON_MORTALITY_FILE 		= "U001";
	public static final String RISK_CODE_NAME_ON_BASIC_SANCTIONS_FILE 	= "U131";
	public static final String RISK_CODE_NAME_ON_BASIC_PEP_FILE 		= "U133";
	public static final String RISK_CODE_ADDRESS_ON_ACCOMMODATION_FILE 	= "U004";
	public static final String RISK_CODE_POTENTIAL_DEVELOPED_IDENTITY 	= "U007";
	public static final String RISK_CODE_ADDRESS_ON_REDIRECTION_FILE 	= "U013";
	public static final String RISK_CODE_NAME_ON_SANCTIONS_OR_PEP_FILE 	= "U015";
	public static final String RISK_CODE_INCONSISTENACIES_IN_APP_DATA 	= "U018";
	public static final String RISK_CODE_APPLICANT_UNDER_AGE 			= "U019";
	
	
	private int personId;
	
	/** Cached check result, from 0 - 2 */
	private int authenticated;
	/** Can be true/false or null, if no Experian Authentication check has
	 * 	been attempted.
	 */
	private Boolean experianAuthenticated;
	
	/** Experian score, from 0 - 90. Will be null 
	 *  if no Experian check has taken place. */
	private Integer authenticationIndex;
	
	private String decision;
	private String decisionText;
	
	private Date checkDate;
	private Date expiryDate;
	
	private Date recalcuateDate;
	
	private String riskConditions;
	private String riskConditionsText;
	
	/** Experian check reference number */
	private String refNo;
	
	private Integer happyScore;
	private Integer unhappyScore;
	
	private boolean legacyChecked;
	private boolean manualOverride;
	private boolean coreDataChanged;
	
	public IdentityCheck(int personId, int authenticated,
			Boolean experianAuthenticated, Integer authenticationIndex,
			String decision, String decisionText, Date checkDate,
			Date expiryDate, Date recalcuateDate, String riskConditions,
			String riskConditionsText, String refNo, Integer happyScore,
			Integer unhappyScore, boolean legacyChecked, boolean manualOverride, boolean coreDataChanged) {

		this.personId = personId;
		this.authenticated = authenticated;
		this.experianAuthenticated = experianAuthenticated;
		this.authenticationIndex = authenticationIndex;
		this.decision = decision;
		this.decisionText = decisionText;
		this.checkDate = checkDate;
		this.expiryDate = expiryDate;
		this.recalcuateDate = recalcuateDate;
		this.riskConditions = riskConditions;
		this.riskConditionsText = riskConditionsText;
		this.refNo = refNo;
		this.happyScore = happyScore;
		this.unhappyScore = unhappyScore;
		this.legacyChecked = legacyChecked;
		this.manualOverride = manualOverride;
		this.coreDataChanged = coreDataChanged;
	}
	
	public static DataResultSet getData(int personId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
		
		DataResultSet rsIdentityCheck = facade.createResultSet
		 ("qClientServices_GetIdentityCheck", binder);
		
		return rsIdentityCheck;
	}
	
	/** Adds an empty Identity Check record for the given Person with a status of 
	 *  'Unchecked'
	 *  
	 * @param personId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static IdentityCheck add(int personId, FWFacade facade)
	 throws DataException {
		DataBinder binder = new DataBinder();
		binder.putLocal(SharedCols.PERSON, Integer.toString(personId));
		
		Date expiryDate = AuthenticationScoreUtils.getNewExpiryDate();
		Date checkDate = new Date();
		Date recalcDate = new Date();
		
		// Add default values required for 'unchecked' Identity Check record.
		IdentityCheck idCheck = new IdentityCheck(personId, IdentityCheck.UNCHECKED, 
		 null, 0, null, null, checkDate, expiryDate, recalcDate, null, null, 
		 null, 1, 1, false, false, false);
		
		idCheck.addFieldsToBinder(binder);

		facade.execute("qClientServices_AddIdentityCheck", binder);
		
		IdentityCheck idcheck = IdentityCheck.get(personId, facade);
		return idcheck;
	}
	
	public static IdentityCheck get(int personId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsIdentityCheck = getData(personId, facade);
		
		if (rsIdentityCheck.isEmpty())
			return null;
		
		return get(rsIdentityCheck);
	}

	public static IdentityCheck get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		String isExperianAuthenticatedStr = 
		 rs.getStringValueByName("IS_EXPERIAN_AUTHENTICATED");
		
		Boolean isExperianAuthenticated = null;
		
		if (!StringUtils.stringIsBlank(isExperianAuthenticatedStr))
			isExperianAuthenticated = 
			 CCLAUtils.getResultSetBoolValue
			  (rs, "IS_EXPERIAN_AUTHENTICATED");
		
		int isAuthenticated = IdentityCheck.UNCHECKED;
		
		if (!StringUtils.stringIsBlank(rs.getStringValueByName("IS_AUTHENTICATED")))
			isAuthenticated = CCLAUtils.getResultSetIntegerValue(rs, "IS_AUTHENTICATED");
		
		return new IdentityCheck(
			CCLAUtils.getResultSetIntegerValue(rs, "PERSON_ID"),
			
			isAuthenticated,
			isExperianAuthenticated,
			
			CCLAUtils.getResultSetIntegerValue(rs, "AUTHENTICATION_INDEX"),
			
			rs.getStringValueByName("DECISION"),
			rs.getStringValueByName("DECISION_TEXT"),
			
			rs.getDateValueByName("CHECK_DATE"),
			rs.getDateValueByName("EXPIRY_DATE"),
			rs.getDateValueByName("RECALCULATE_DATE"),
			
			rs.getStringValueByName("RISK_CONDITIONS"),
			rs.getStringValueByName("RISK_CONDITIONS_TEXT"),
			
			rs.getStringValueByName("REF_NO"),
			
			CCLAUtils.getResultSetIntegerValue(rs, "TOTAL_HAPPY_SCORE"),
			CCLAUtils.getResultSetIntegerValue(rs, "TOTAL_UNHAPPY_SCORE"),
			
			CCLAUtils.getResultSetBoolValue(rs, "LEGACY_CHECKED"),
			CCLAUtils.getResultSetBoolValue(rs, "MANUAL_OVERRIDE"),
			CCLAUtils.getResultSetBoolValue(rs, "CORE_DATA_CHANGED")
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PERSON_ID", this.getPersonId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "IS_AUTHENTICATED", this.getAuthenticated());
		
		if (this.isExperianAuthenticated() != null)
			CCLAUtils.addQueryBooleanParamToBinder
			 (binder, "IS_EXPERIAN_AUTHENTICATED", this.isExperianAuthenticated());
		else
			CCLAUtils.addQueryParamToBinder
			 (binder, "IS_EXPERIAN_AUTHENTICATED", null);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "AUTHENTICATION_INDEX", this.getAuthenticationIndex());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "DECISION", this.getDecision());
		CCLAUtils.addQueryParamToBinder
		 (binder, "DECISION_TEXT", this.getDecisionText());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "CHECK_DATE", this.getCheckDate());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "EXPIRY_DATE", this.getExpiryDate());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "RECALCULATE_DATE", this.getRecalcuateDate());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "RISK_CONDITIONS", this.getRiskConditions());
		CCLAUtils.addQueryParamToBinder
		 (binder, "RISK_CONDITIONS_TEXT", this.getRiskConditionsText());
		CCLAUtils.addQueryParamToBinder
		 (binder, "REF_NO", this.getRefNo());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "TOTAL_HAPPY_SCORE", this.getHappyScore());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "TOTAL_UNHAPPY_SCORE", this.getUnhappyScore());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "LEGACY_CHECKED", this.isLegacyChecked());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "MANUAL_OVERRIDE", this.isManualOverride());
		CCLAUtils.addQueryBooleanParamToBinder
		(binder, "CORE_DATA_CHANGED", this.isCoreDataChanged());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateIdentityCheck", binder);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented!");
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
	}

	/**  
	 * Determines whether the check scores/result needs to be recalculated,
	 * i.e. the Recalcuate Date has passed.
	 * 
	 * If the date is null, the method will return false.
	 * 
	 * @return
	 */
	public boolean needsRecalculation() {
		return (this.getRecalcuateDate() == null
				||
				this.getRecalcuateDate().before(new Date()));
	}
	
	/** 
	 * Determines whether the check scores/result has expired, i.e. the Expiry
	 * Date has passed.
	 * 
	 * @return
	 */
	public boolean isExpired() {
		return (this.getExpiryDate() != null
				&&
				this.getExpiryDate().before(new Date()));
	}
	
	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}
	
	/** Returns a number indicating the overall authentication result.
	 *  0: FAILED
	 *  1: PASSED
	 *  2: UNCHECKED/EXPIRED
	 * 
	 * @return
	 */
	public int getAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(int authenticated) {
		this.authenticated = authenticated;
	}

	public Boolean isExperianAuthenticated() {
		return experianAuthenticated;
	}

	public void setExperianAuthenticated(Boolean experianAuthenticated) {
		this.experianAuthenticated = experianAuthenticated;
	}

	public Integer getAuthenticationIndex() {
		return authenticationIndex;
	}

	public void setAuthenticationIndex(Integer authenticationIndex) {
		this.authenticationIndex = authenticationIndex;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getDecisionText() {
		return decisionText;
	}

	public void setDecisionText(String decisionText) {
		this.decisionText = decisionText;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getRecalcuateDate() {
		return recalcuateDate;
	}

	public void setRecalcuateDate(Date recalcuateDate) {
		this.recalcuateDate = recalcuateDate;
	}

	public String getRiskConditions() {
		return riskConditions;
	}

	public void setRiskConditions(String riskConditions) {
		this.riskConditions = riskConditions;
	}

	public String getRiskConditionsText() {
		return riskConditionsText;
	}

	public void setRiskConditionsText(String riskConditionsText) {
		this.riskConditionsText = riskConditionsText;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
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

	public boolean isLegacyChecked() {
		return legacyChecked;
	}

	public void setLegacyChecked(boolean legacyChecked) {
		this.legacyChecked = legacyChecked;
	}

	public boolean isManualOverride() {
		return manualOverride;
	}

	public void setManualOverride(boolean manualOverride) {
		this.manualOverride = manualOverride;
	}
	public boolean isCoreDataChanged() {
		return coreDataChanged;
	}

	public void setCoreDataChanged(boolean coreDataChanged) {
		this.coreDataChanged = coreDataChanged;
	}	
}
