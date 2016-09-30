package com.ecs.ucm.ccla.experian;

import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.IdentityCheck;
import com.ecs.ucm.ccla.data.IdentityCheckScore;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;


public class AuthenticationScoreUtils {
	
	/** Determines whether the Legacy AML Tracker Checked attribute will play a part
	 *  in the person's Identity Check status.
	 * 
	 * 
	 */
	public static final boolean AML_TRACKER_CHECK_ENABLED =
	 SharedObjects.getEnvValueAsBoolean
	 ("CCLA_CS_AMLTrackerIdentityCheckEnabled", false);
	
	/** Performs a check to see if one number is a product of another
	 *  
	 * @return true if number is a product of the other number, false otherwise
	 *  
	 * @throws
	 */
	public static boolean getScoreContains(int containsValue, int Score)
	{
		return (Score%containsValue == 0);

	}
	
	/** Checks to see if one number is already contained in the other and if not
	 * multiplies the two number together
	 *  
	 * @return either the product of the numbers or the original score
	 *  
	 * @throws
	 */
	public static int addScore(int addValue, int Score)
	{
		int returnScore = Score;
		if (!getScoreContains(addValue, Score))
			returnScore = addValue*Score;
		
		return returnScore;
	}	
	
	/** Checks to see if one number is already contained in the other and if it does 
	 * then divides the containing number by the other
	 *  
	 * @return either the result of division of the numbers or the original score
	 *  
	 * @throws
	 */
	public static int removeScore(int removeValue, int Score)
	{
		int returnScore = Score;
		if (getScoreContains(removeValue, Score))
			returnScore = Score/removeValue;
		
		return returnScore;
	}	
	
	/** Gets a resultset of the validation results table for a particular person
	 *  
	 * @return a results set containing the  validation valdiation results row for a person
	 *  
	 * @throws DataException
	 */
	public static DataResultSet getValidationResultSetForPerson
	(FWFacade fw, DataBinder binder, int Person_ID)
	throws DataException
	{
		binder.putLocal("PERSON_ID", Integer.toString(Person_ID));	
		DataResultSet rs = fw.createResultSet("qClientServices_GetValidationRecord", binder);
		return rs;
	}
	
	/** Gets a resultset of the validation results table
	 *  
	 * @return a results set containing the whole validation valdiation results table
	 *  
	 * @throws DataException
	 */
	public static DataResultSet getValidationResultSet(FWFacade fw, DataBinder binder)
	throws DataException
	{
			
		DataResultSet rs = fw.createResultSet("qClientServices_GetAllValidationRecords", binder);
		return rs;
	}
		
	/** Gets a resultset of the risk codes from the authentication lookup table
	 *  
	 * @return a results set containing the risk codes for authentication
	 *  
	 * @throws DataException
	 */
	public static DataResultSet getAuthenticationRiskCodes(FWFacade fw, DataBinder binder)
	throws DataException
	{	
		DataResultSet rs = fw.createResultSet("qClientServices_GetAuthenticationRiskCodes", binder);	
		return rs;
	}


	/** Gets a resultset of the validation results table for a particular person
	 *  
	 * @return a results set containing the  validation valdiation results row for a person
	 *  
	 * @throws DataException
	 */
	public static DataResultSet getAuthenticationResultSetForPerson
	(FWFacade fw, DataBinder binder, int Person_ID)
	throws DataException
	{
		binder.putLocal("PERSON_ID", Integer.toString(Person_ID));	
		DataResultSet rs = fw.createResultSet("qClientServices_GetAuthenticateRecord", binder);
		return rs;
	}
	
	
	public static void updateIdentityCheck(int Person_ID, FWFacade fw) throws DataException
	{
		IdentityCheck idcheck = IdentityCheck.get(Person_ID, fw);
		if (idcheck == null)
		{
			idcheck = IdentityCheck.add(Person_ID, fw);
		}	
		
		recalculateCheckResult(idcheck,fw);
	}
	
	/** Calculates the Happy/Unhappy scores for a person from scratch and updates 
	 *  both values in the passed IdentityCheck instance.
	 *  
	 * @throws DataException
	 **/
	
	private static void calculateScoreForPerson(IdentityCheck idcheck, FWFacade fw)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		int happyScore = 1;
		int unhappyScore = 1;
		int personId = idcheck.getPersonId();
		
		Log.debug("Recalculating score for Person ID " + personId);
		
		/* Check the Validation Record first, if one exists for this person. 
		 * 
		 * This includes the outcome of driving license, passport, mailsort code and
		 * Crockfords validation checks.
		 * 
		 **/
		DataResultSet rs = getValidationResultSetForPerson(fw, binder, personId);
		
		// Set to true if at least one validation check was successful.
		boolean passedValidation = false;
		
		if (rs.first()) 
		{
			Log.debug("Validation Record found");
			
			if (!StringUtils.stringIsBlank
				(rs.getStringValueByName("DRIVING_LICENCE_VALID")))
			{
				Boolean drivingLicenseValid = CCLAUtils.getResultSetBoolValueAllowNull
				 (rs, "DRIVING_LICENCE_VALID");
				
				Log.debug("Driving License check result: " + drivingLicenseValid);
				
				if (drivingLicenseValid != null) {
					if (!drivingLicenseValid) {
						// failed
						unhappyScore = unhappyScore*IdentityCheckGlobals.DRIVING_LICENCE_VALIDATION_FAILED_SCORE;
					} else {
						// passed
						happyScore = happyScore*IdentityCheckGlobals.DRIVING_LICENCE_VALIDATION_PASSED_SCORE;
						passedValidation = true;
					}
				}
			}
			
			if (!StringUtils.stringIsBlank
				(rs.getStringValueByName("PASSPORT_VALID")))
			{
				
				Boolean passportValid = CCLAUtils.getResultSetBoolValueAllowNull
				 (rs, "PASSPORT_VALID");
				
				Log.debug("Passport check result: " + passportValid);
				
				if (passportValid != null) {
					if (!passportValid) {
						// failed
						unhappyScore = unhappyScore*IdentityCheckGlobals.PASSPORT_VALIDATION_FAILED_SCORE;
					} else {
						// passed
						happyScore = happyScore*IdentityCheckGlobals.PASSPORT_VALIDATION_PASSED_SCORE;
						passedValidation = true;
					}
				}
			}

			if (!StringUtils.stringIsBlank
				(rs.getStringValueByName("MAILSORT_VALID")))
			{
				
				Boolean mailsortValid = CCLAUtils.getResultSetBoolValueAllowNull
				 (rs, "MAILSORT_VALID");
				
				Log.debug("Mail Sort Code check result: " + mailsortValid);
				
				if (!mailsortValid) {
					// failed
					unhappyScore = unhappyScore*IdentityCheckGlobals.UTILITY_MAILSORT_VALIDATION_FAILED_SCORE;
				} else {
					// passed
					happyScore = happyScore*IdentityCheckGlobals.UTILITY_MAILSORT_VALIDATION_PASSED_SCORE;
					passedValidation = true;
				}
			}
			
			if (!StringUtils.stringIsBlank
				(rs.getStringValueByName("CROCKFORDS_VALID")))
			{
				
				Boolean crockfordsValid = CCLAUtils.getResultSetBoolValueAllowNull
				 (rs, "CROCKFORDS_VALID");

				Log.debug("Crockfords check result: " + crockfordsValid);
				
				if (!crockfordsValid) {
					// failed
					unhappyScore = unhappyScore*IdentityCheckGlobals.CROCKFORDS_VALIDATION_FAILED_SCORE;
				} else {
					// passed
					happyScore = happyScore*IdentityCheckGlobals.CROCKFORDS_VALIDATION_PASSED_SCORE;
					passedValidation = true;
				}
			}
			
			Log.debug("Post-Validation Record checks: " +
			 ":) " + happyScore + " :( " + unhappyScore);
		}

		boolean legacyAMLChecked = false;
		
		if (AML_TRACKER_CHECK_ENABLED) {
			/* Check for existence of a Legacy AML Tracker element attribute.
			 * 
			 *  This indicates they were IVS checked during the previous AML Tracker 
			 *  exercise
			 **/
			ElementAttributeApplied amlChecked = ElementAttributeApplied.get
			 (personId, ElementAttribute.PersonAttributes.AML_TRACKER_CHECKED, fw);

			if (amlChecked != null && amlChecked.getStatus()) 
			{
				// add manual pep override to score
				Log.debug("Found AML Tracker Checked attribute");
				happyScore = happyScore*IdentityCheckGlobals.LEGACY_AML_TRACKER_CHECKED;
				
				legacyAMLChecked = true;
			}
		}
		
		/* Check the contents of the Authentication Record for the person, if one 
		 * exists.
		 * 
		 * This includes details of the last Experian Authentication check run against
		 * the Person's record.
		 * 
		 */
		DataResultSet authRS = getAuthenticationResultSetForPerson
		 (fw, binder, personId);
		
		if (authRS.first())
		{
			Log.debug("Identity Check record found");
			
			// Special flag that gets set if the PEP risk flag was returned from
			// Experian.
			boolean isInPEPList = false;
						
			int experianCheckOutcome 		= IdentityCheck.UNCHECKED;
			
			// Flag field that is set to true/false following an Experian check.
			// Null value indicates no check has been run.
			Boolean isExperianAuthenticated = CCLAUtils.getResultSetBoolValueAllowNull
			 (authRS, "IS_EXPERIAN_AUTHENTICATED");
			
			// Experian Decision return code, e.g. AU01, NA01
			// Only present if an experian check has been run.
			String experianAuthDecision = authRS.getStringValueByName("DECISION");
			
			Log.debug("Is Experian Authenticated? " + isExperianAuthenticated + 
			 ", Experian Decision Code: " + experianAuthDecision);
			
			// These 2 should never get set, so we won't try and read them.
			// String legacyS = authRS.getStringValueByName("LEGACY_CHECKED");
			// String manualOverride = authRS.getStringValueByName("MANUAL_OVERRIDE");
			
			// Expiry date for the authentication result.
			Date expiryDate = authRS.getDateValueByName("EXPIRY_DATE");
			
			// Set to true if core data that could affect the result of an Experian 
			// check has been changed (e.g. change of name/address)
			boolean coreDataChanged = CCLAUtils.getResultSetBoolValue
			 (authRS, "CORE_DATA_CHANGED");
			
			/* Check for Amber states e.g. expired record, core data change.
			 * 
			 * These will overrule a passed check outcome.
			 *  
			 */
			
			// Has the expiry date been reached?
			if (expiryDate.before(new Date()))
			{	
				Log.debug("Expiry Date has been reached");
				experianCheckOutcome = IdentityCheck.UNCHECKED;;
				unhappyScore=unhappyScore*IdentityCheckGlobals.IDENTITY_CHECK_EXPIRED;
			}
			// Has the core Person record data been changed since?
			else if (coreDataChanged)
			{
				Log.debug("Core Data has been updated");
				experianCheckOutcome = IdentityCheck.UNCHECKED;;
				unhappyScore=unhappyScore*IdentityCheckGlobals.CORE_DATA_CHANGED;
			}
			else 
			{
				if (isExperianAuthenticated!= null)
				{	
					if (isExperianAuthenticated)
					{
						experianCheckOutcome = IdentityCheck.PASSED;	
					} else if (!isExperianAuthenticated) {
						experianCheckOutcome = IdentityCheck.FAILED;							
					}						
				} else {
					// Apply the 'Awaiting Authentication' score, if none of the 
					// previous checks had passed.
					if (!passedValidation && !legacyAMLChecked)
						unhappyScore=unhappyScore*IdentityCheckGlobals.AWAITING_EXPERIAN_AUTHENTICATED_SCORE;			
				}
			}

			if (experianCheckOutcome == IdentityCheck.PASSED)
			{
				// Passed Experian Auth check
				happyScore=happyScore*IdentityCheckGlobals.EXPERIAN_AUTHENTICATED_SCORE;	
				
			} else if (experianCheckOutcome == IdentityCheck.FAILED)
			{
				// Failed Experian Auth check
				unhappyScore=unhappyScore*IdentityCheckGlobals.EXPERIAN_FAILED_AUTHENTICATION_SCORE;	
			} 
				
			/* Check for presence of Experian Risk Codes
			 * 
			 */
			if (!StringUtils.stringIsBlank(experianAuthDecision) 
				&& experianAuthDecision.equalsIgnoreCase(ExperianGlobals.REFER_CODE))
			{
				// Risk code(s) present.
				
				String riskConditions = authRS.getStringValueByName("RISK_CONDITIONS");
				Log.debug("Experian Risk Conditions found:" + riskConditions);
				
				DataResultSet riskRS = getAuthenticationRiskCodes(fw, binder);

				if (riskRS.first())
				{
					String riskCode = riskRS.getStringValueByName("EXPERIAN_RISK_CODE");
					String strRiskScore = riskRS.getStringValueByName("UNHAPPY_SCORE");		
					
					int riskScore = Integer.parseInt(strRiskScore);
					StringTokenizer st = new StringTokenizer(riskConditions, "|");
					
					if (riskCode!=null)
					{
						while (st.hasMoreTokens())
						{
							String token = st.nextToken();
							if (riskCode.equalsIgnoreCase(token))
							{
								Log.debug("Found risk code match:" + token);
								unhappyScore=unhappyScore*riskScore;
								
								// Check for PEP Risk condition (this one needs special
								// treatment)
								if (token.equalsIgnoreCase
									(IdentityCheck.RISK_CODE_NAME_ON_BASIC_PEP_FILE)) {
									isInPEPList = true;
								}
							}							
						}
					}	
				}
			} 			
			
			//Only add the attributes if this has been through Experian checking.
			Log.debug("isInPEPList="+isInPEPList+", hasPassed="+experianCheckOutcome);
			
			// Add/remove PEP check attribute, depending on whether Experian check was
			// executed, and if so whether PEP risk was returned.
			
			if (isExperianAuthenticated != null) {
				// Add PEP check attribute, with appropriate status value
				ElementAttributeApplied.addOrUpdateSingle(idcheck.getPersonId(), 
				 ElementAttribute.PersonAttributes.PEP_CHECKED, !isInPEPList, 
				 null, fw);
				
			} else {
				// Remove PEP check attribute, if it exists.
				ElementAttributeApplied.addOrRemoveSingle(idcheck.getPersonId(), 
				 ElementAttribute.PersonAttributes.PEP_CHECKED, false, fw);	
				
			}

		} else {
			// add awaiting authentication score
			if (!passedValidation && !legacyAMLChecked)
				unhappyScore=unhappyScore*IdentityCheckGlobals.AWAITING_EXPERIAN_AUTHENTICATED_SCORE;			
		}
		
		Log.debug("Post-Authentication Record checks: " +
		 ":) " + happyScore + " :( " + unhappyScore);
	
		/* Fetch the Person's address record which has been marked for use in Experian
		 * checks.
		 */
		DataResultSet rsAddress = Authenticate.getPersonExperianAddress
		 (personId, fw, binder);
		
		if (!rsAddress.first())
		{
			// No Experian-nominated address on file.
			Log.debug("No Experian-nominated address linked to person");
		} else 
		{
			boolean QASValid = CCLAUtils.getResultSetBoolValue(rsAddress, "QAS_VALID");
			
			// Check if the address data came directly from QAS Lookup, without
			// manual modifications
			if (QASValid) {
				Log.debug("Experian-nominated address came from QAS");
				happyScore = happyScore*IdentityCheckGlobals.QAS_VALID_SCORE;
			} else {
				Log.debug("Experian-nominated address was manually added/amended");
				unhappyScore = unhappyScore*IdentityCheckGlobals.MANUAL_ADDRESS_SCORE;
			}
			
			// Check if the address was marked as dubious
			boolean dubious = CCLAUtils.getResultSetBoolValue(rsAddress, "IS_DUBIOUS");
			if (dubious) {
				Log.debug("Experian-nominated address has been marked as dubious");
				unhappyScore = unhappyScore*IdentityCheckGlobals.ADDRESS_IS_DUBIOUS;
			}
		}
		
		/* Now check applied Element Attributes
		 * 
		 */
		// PEP check override is held in element attribute of person
		ElementAttributeApplied pepOverride  = ElementAttributeApplied.get
		 (personId, ElementAttribute.PersonAttributes.PEP_CHECK_OVERRIDE, fw);
		
		if (pepOverride != null && pepOverride.getStatus())
		{
			// add manual pep override to score
			Log.debug("Found PEP override element attribute");
			happyScore = happyScore*IdentityCheckGlobals.MANUAL_PEP_OVERRIDE;
		}
		
		// Person account override is held in element attribute of person
		ElementAttributeApplied personOverride  = ElementAttributeApplied.get
		 (personId, ElementAttribute.PersonAttributes.ACCOUNT_IVS_OVERRIDE, fw);
		if (personOverride != null && personOverride.getStatus())
		{
			// add manual pep override to score
			Log.debug("Found Account check override element attribute");
			happyScore = happyScore*IdentityCheckGlobals.MANUAL_PERSON_ACCOUNT_OVERRIDE;
		}		
		
		Log.debug("Final scores are :) " + happyScore + " :( " + unhappyScore);
		
		// Set the scores
		idcheck.setHappyScore(happyScore);
		idcheck.setUnhappyScore(unhappyScore);		
	}
	
	/** Recalculates an existing Identity Check score/result. This is designed
	 *  to be executed when: 
	 *  
	 *  <br/>
	 *   -The Identity Check result is inspected and the recalculate date has 
	 *    passed.
	 *  <br/>
	 *  -An update is made to the Identity Check scoring, i.e. when the Person
	 *   record is updated.
	 *  <br/>
	 *  Updates to the Identity Check record will be applied here:
	 *  <br/>
	 *   -At a minimum, the recalculate date is reset.
	 *   -The result may also be updated if the scoring rules have changed.
	 *   -If the check result indicates a pass, the expiry date is checked. If
	 *    the score/result has expired, the Identity Check must be marked as
	 *    expired and will move back to 'Amber' status.
	 *  
	 * @param identityCheck
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static int recalculateCheckResult
	 (IdentityCheck identityCheck, FWFacade facade) throws DataException {
		
		Log.debug("Recalculating check result for Person ID: " 
		 + identityCheck.getPersonId());

		Log.debug("Current authentication result: " + 
		 identityCheck.getAuthenticated());
		
		Log.debug("Current Happy Score: " + identityCheck.getHappyScore() + 
		 ", Unhappy Score: " + identityCheck.getUnhappyScore());
		
		calculateScoreForPerson(identityCheck,facade);

		boolean failed		= false;
		boolean amber		= false;
		boolean passed		= false;

		// Fetch fail conditions.
		Vector<IdentityCheckScore> failScores = 
		 IdentityCheckScore.getConditionCache().getCachedInstance
		 (IdentityCheckScore.Condition.FAIL);
	
		for (IdentityCheckScore score: failScores) {
			if (score.getUnhappyScore() != null)
				failed = AuthenticationScoreUtils.getScoreContains
				 (score.getUnhappyScore(), identityCheck.getUnhappyScore());
			
			// Break out the loop as soon as we encounter a single Fail score.
			if (failed) {
				Log.debug("Found a Fail score: " + score.getDescription());
				break;
			}
		}
		
		// Fetch amber conditions.
		Vector<IdentityCheckScore> amberScores = 
		 IdentityCheckScore.getConditionCache().getCachedInstance
		 (IdentityCheckScore.Condition.AMBER);
		
		for (IdentityCheckScore score: amberScores) {
			if (score.getUnhappyScore() != null)
				amber = AuthenticationScoreUtils.getScoreContains
				 (score.getUnhappyScore(), identityCheck.getUnhappyScore());
			
			// Break out the loop as soon as we encounter a single Amber score.
			if (amber) {
				Log.debug("Found an Amber score: " + score.getDescription());
				break;
			}
		}	
		
		// Stores any applied pass scores, if applicable
		Vector<IdentityCheckScore> appliedPassScores = new Vector<IdentityCheckScore>();
		
		// Check for pass conditions, but only if person doesn't have an 
		// amber status.
		if (!amber) {
			Log.debug("Checking for Pass scores");
			
			// Fetch pass conditions.
			Vector<IdentityCheckScore> passScores = 
			 IdentityCheckScore.getConditionCache().getCachedInstance
			 (IdentityCheckScore.Condition.PASS);
			
			int countNum = IdentityCheckGlobals.PASS_CONDITION_THRESHOLD;
			
			int passCount = 0;
			for (IdentityCheckScore score: passScores) {

				if (score.getHappyScore() != null) {
					// Increment happyCount for each matching Pass score
					if (AuthenticationScoreUtils.getScoreContains
						 (score.getHappyScore(), identityCheck.getHappyScore())) {
						Log.debug("Found a Pass score: " + score.getDescription());
						passCount = passCount + 1;
						
						appliedPassScores.add(score);
					}
				}

				//if number of Pass conditions greater or equal to threshold value then 
				// it counts as pass
				if (passCount >= countNum) {
					Log.debug("No. of Pass scores has met required threshold (" 
					 + countNum + ")");
					
					passed = true;
					break;
				}
			}
		}
		
		if (passed) {
			// Check if the previously passed result has now expired.
			// Should never execute, will already have Amber state.
			
			if (identityCheck.isExpired()) {
				Log.debug("Check Result has passed, but now expired.");
				
				Log.debug("SHOULDN'T BE HERE,, HALP");
				
				/*
				// Result has expired, ensure it has been marked as such.
				if (!getScoreContains(IdentityCheckGlobals.EXPERIAN_AUTHENTICATION_EXPIRED, 
				 identityCheck.getUnhappyScore())) {
					Log.debug("Marking Identity Check as 'Expired'");
					
					identityCheck.setUnhappyScore(
					addScore(IdentityCheckGlobals.EXPERIAN_AUTHENTICATION_EXPIRED, 
					 identityCheck.getUnhappyScore()));
				}
				
				// Demote Pass result to Amber
				amber = true;
				*/
			}
			
		}
		
		// Check if outcome result is different to stored result. If there was a 
		// change, the new result must be persisted against the Identity Check
		// record.
		int newResult = IdentityCheck.FAILED;
		
		if (failed & !passed) // If failed and not passed, then count as a Fail
			newResult = IdentityCheck.FAILED;
		else if (amber) // If amber return then count as Unchecked
			newResult = IdentityCheck.UNCHECKED;
		else // Otherwise return as Passed.
			newResult = IdentityCheck.PASSED;
		
		Log.debug("New authentication result: " + newResult);
		
		// Apply new authentication result, scores and recalcuate date.
		identityCheck.setAuthenticated(newResult);
		identityCheck.setRecalcuateDate(getNewRecalcuateDate());
		identityCheck.persist(facade, Globals.Users.System);
		
		// Add/remove/update Element Attributes that are set based on the outcome of
		// an Identity Check
		updateElementAttributesByCheckResult(identityCheck.getPersonId(), 
		 appliedPassScores, newResult, facade);
			
		return newResult;
	}

	/** Fetches a fresh recalcuation date, relative to the current date.
	 * 
	 * @return
	 */
	public static Date getNewRecalcuateDate() {
		Date date = new Date();
		
		Integer daysResultValid = null;
		
		try {
			daysResultValid = SystemConfigVar.getCache().getCachedInstance(
					ExperianGlobals.ConfigVarNames.
					IdentityCheck_DaysResultValid.toString()).getIntValue();
		} catch (DataException de) {
			Log.error("Error Getting IdentityCheck_DaysResultValid", de);
			daysResultValid = 1;
		}
		
		date.setTime(date.getTime() + 
		 (long)((long)(daysResultValid * ExperianGlobals.MILLIS_PER_DAY)));
		
		return date;
	}
	
	/** Fetches a fresh expiry date, relative to the current date.
	 * 
	 * @return
	 */
	public static Date getNewExpiryDate() {
		Date date = new Date();
		Integer daysCheckValid = null;
		Date fixedExpiryDate = null;
		
		try {
			daysCheckValid = SystemConfigVar.getCache().getCachedInstance(
					ExperianGlobals.ConfigVarNames.
					IdentityCheck_DaysCheckValid.toString()).getIntValue();
		} catch (DataException de) {
			Log.error("Error getting DaysCheckValid, defaulting to 1 year.", de);
			daysCheckValid = 1;
		}
		
		try {
			fixedExpiryDate =
				SystemConfigVar.getCache().getCachedInstance(
						ExperianGlobals.ConfigVarNames.
						IdentityCheck_FixedExpiryDate.toString()).getDateValue();
		} catch (DataException de) {
			Log.error("Error getting FixedExpiryDate.", de);
			
		}
		
		if (fixedExpiryDate != null) 
			return fixedExpiryDate;
		else
			date.setTime(date.getTime() + 
			 (long)((long)(daysCheckValid * ExperianGlobals.MILLIS_PER_DAY)));
		
		return date;
	}
	
	public static void main (String[] args) {
		
		final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
		
		Date date = new Date();
		
		date.setTime(date.getTime() + 
		 (long)((long)730 * MILLIS_PER_DAY));
		
		System.out.println(date);
	}
	
	/** Returns the Authentication result for the given Person ID.
	 *  <br/>
	 *  If there is an existing score for this person, it will be checked first
	 *  to see if it needs to be recalcuated. If so, it must be recalcuated before the 
	 *  result is returned.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static int getIdentityCheckResult
	 (int personId, FWFacade facade) throws DataException {
		IdentityCheck identityCheck = null;
		 identityCheck = IdentityCheck.get(personId, facade);
		 if (identityCheck == null)
			 identityCheck = IdentityCheck.add(personId, facade);

		return refreshIdentityCheckResult(identityCheck, facade);
	}
	
	public static int refreshIdentityCheckResult
	 (IdentityCheck identityCheck, FWFacade facade) throws DataException {
		
		if (identityCheck == null) {
			// No existing check.
			return IdentityCheck.UNCHECKED;
		}
			
		if (identityCheck.needsRecalculation()) {
			// Recalculate result
			return recalculateCheckResult(identityCheck, facade);
		} else {
			// Return current result
			return identityCheck.getAuthenticated();
		}
	}
	
	/** Searches through all Person records which require recalculation and recalculates
	 *  their scores.
	 *  
	 * @param facade
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void recalculateAll(FWFacade facade) 
	 throws DataException, ServiceException {
		
		DataBinder binder = new DataBinder();
		
		DataResultSet rsPersonsRequiringRecalc = facade.createResultSet
		 ("qClientServices_GetIdentityChecksRequiringRecalculation", binder);
		
		int count = 0;
		
		if (rsPersonsRequiringRecalc.first()) {
			Log.debug("Found " + rsPersonsRequiringRecalc.getNumRows() 
			 + " person with Identity Checks requiring recalculation");
				
			do {
				int personId = CCLAUtils.getResultSetIntegerValue
				 (rsPersonsRequiringRecalc, SharedCols.PERSON);
				
				try {
					facade.beginTransaction();
					
					IdentityCheck identityCheck = IdentityCheck.get(personId, facade);
					recalculateCheckResult(identityCheck, facade);
					
					facade.commitTransaction();
					
					Log.debug(++count + "/" + rsPersonsRequiringRecalc.getNumRows() +
					 " recalculated");
					
				} catch (Exception e) {
					facade.rollbackTransaction();
					
					String msg = "Failed to recalculate score for Person ID " 
					 + personId;
					
					Log.error(msg, e);
					throw new ServiceException(msg, e);
				}
				
			} while (rsPersonsRequiringRecalc.next());
		} else {
			Log.debug("Found no Person records requiring recalculation");
		}
	}
	
	/** Checks the Identity Check results for all Persons in the passed
	 *  ResultSet (requires a non-null PERSON_ID column).
	 *  
	 *  The ResultSet should contain all columns from the CCLA_IDENTITY_CHECK
	 *  table, although the values can be blank.
	 *  
	 *  Designed for use when displaying lists of Persons, e.g. those related
	 *  to a particular account. This method ensures that the displayed
	 *  Identity Check results are guaranteed to be up-to-date.
	 *  
	 * @param rsPersons
	 * @throws DataException 
	 */
	public static void refreshIdentityCheckResults
	 (DataResultSet rsPersons, FWFacade facade) throws DataException {
		
		Log.debug("Refreshing Identity Check results for " + 
		 rsPersons.getNumRows() + " listed persons");
		
		if (!rsPersons.first())
			return;
		
		// Determine position of the IS_AUTHENTICATED column in the ResultSet.
		// This is required if any Identity Check results change during refresh
		int checkResultCol = -1;
		
		for (int i=0; i<rsPersons.getNumFields() && checkResultCol == -1; i++) {
			if (rsPersons.getFieldName(i).equals("IS_AUTHENTICATED"))
				checkResultCol = i;
		}
		
		if (checkResultCol == -1)
			throw new DataException("Unable to refresh Identity Check scores, " +
			 "no Check Result column in passed ResultSet");
		
		do {
			String currentCheckResultStr = 
			 rsPersons.getStringValue(checkResultCol);
			
			// Non-null check result value in ResultSet. This means a check
			// has been carried out for this Person previously, and we can
			// construct an IdentityCheck instance from the row values.
			//
			// Refresh the IdentityCheck istance to ensure it is still valid.
			if (!StringUtils.stringIsBlank(currentCheckResultStr)) {
				int currentCheckResult = Integer.parseInt(currentCheckResultStr);
				
				IdentityCheck identityCheck = IdentityCheck.get(rsPersons);	
				int checkResult = refreshIdentityCheckResult(identityCheck, facade);
				
				if (currentCheckResult != checkResult) {
					// Identity check score has changed since the refresh.
					// Update the entry in the passed ResultSet.
					Log.debug("Updating IS_AUTHENTICATED column value for " +
					 "Person ID " + identityCheck.getPersonId() + " to " +
					 checkResult);
					 
					rsPersons.setCurrentValue
					 (checkResultCol, Integer.toString(checkResult));
				}
			}
			
		} while (rsPersons.next());
	
		rsPersons.first();
	}
	
	/**
	 * Add Applied Element Attribute to person, depending on outcome of IdentityCheck
	 * 
	 * @param personId
	 * @param attrID
	 * @param result
	 * @param facade
	 * @throws DataException
	 */
	private static void updateElementAttributesByCheckResult
	 (int personId, Vector<IdentityCheckScore> appliedPassScores, 
	 int newResult, FWFacade facade) throws DataException {
		
		switch(newResult) {
			case IdentityCheck.UNCHECKED: {
				// No checks have been performed, or the previous check has expired.
				// Remove the ID Checked attribute, if it currently exists.
				
				/*
				ElementAttributeApplied.addOrRemoveSingle
				 (personId, ElementAttribute.PersonAttributes.IDENTITY_CHECKED, 
				 false, facade);
				*/
				
				break;
			}
			case IdentityCheck.PASSED: {
				// Identity Check was resolved as 'Passed'. Iterate through applied
				// pass scores and add their corresponding element attributes to the
				// Person.
				for (IdentityCheckScore passScore : appliedPassScores) {
					if (passScore.getElementAttribute() != null) {

						ElementAttributeApplied.addOrUpdateSingle
						 (personId, passScore.getElementAttribute().getAttributeId(), 
						 true, null, facade);	
					}
				}
				
				break;
			} 

			case IdentityCheck.FAILED: {
				// Mark any relevant ID check attributes as Failed
				ElementAttributeApplied.addOrUpdateSingle
				 (personId, ElementAttribute.PersonAttributes.IDENTITY_CHECKED, 
				 false, null, facade);				
				break;
			} default: {
				Log.error("Unknown Identity Check result status "+newResult
				 + ", leaving ElementAttributes alone for Person ID: "+personId);
				break;
			}
		}
	}

	/** Sets the 'Core Data Changed' flag, wipes the Experian Auth flag on the passed 
	 *  Identity Check instance and re-runs the score evaluation.
	 *  
	 *  This should be called when peripheral data belonging to a person used in
	 *  Identity checking is updated. This includes the nominated Experian address
	 *  and name/date of birth fields.
	 *  
	 * @param idCheck
	 * @param facade
	 * @throws DataException 
	 */
	public static void setCoreDataChanged
	 (IdentityCheck idCheck, FWFacade facade, String userName) throws DataException {
		
		Log.debug("Setting 'Core Data Changed' flag for Person ID " 
		 + idCheck.getPersonId());
		
		idCheck.setCoreDataChanged(true);
		idCheck.setExperianAuthenticated(null);
		
		/** TODO: remove validation check outcomes */
		
		idCheck.persist(facade, userName);
		
		AuthenticationScoreUtils.recalculateCheckResult
		 (idCheck, facade);
		
		AuthenticationScoreUtils.updateIdentityCheck
		 (idCheck.getPersonId(),facade);
	}
}
