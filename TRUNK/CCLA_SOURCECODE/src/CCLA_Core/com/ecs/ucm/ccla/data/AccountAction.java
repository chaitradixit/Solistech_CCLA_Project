package com.ecs.ucm.ccla.data;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.Vector;

import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** 
 * @deprecated will be replaced by new Instruction Register functionality
 * 
 * Wrapper class for entries in the CCLA_ACCOUNT_ACTIONS table. */
public class AccountAction {
	
	private int actionId;
	
	// Source account fields
	private String sourceAccountNumExt;
	private String sourceAccountCompany;
	private Integer sourceAccountClientId;
	private Integer sourceAccountNum;
	private String  sourceAccountFund;
	
	private String sourceAccountSubtitle;
	
	private Float sourceAccountCash;
	private Float sourceAccountUnits;
	
	private Integer sourceAccountCorrId;
	private String sourceAccountIncomeDistMethod;
	private boolean sourceAccountExternal;
	private String sourceAccountShareClass;
	
	// Destination account fields
	private String destAccountNumExt;
	private String destAccountCompany;
	private Integer destAccountClientId;
	private Integer destAccountNum;
	private String  destAccountFund;
	
	private String destAccountSubtitle;
	
	private Float destAccountCash;
	private Float destAccountUnits;
	
	private Integer destAccountCorrId;
	private String destAccountIncomeDistMethod;
	private boolean destAccountExternal;
	private String destAccountShareClass;
	
	// Banking details
	private BankingDetails bankingDetails;
	
	// Action fields
	private String userId;
	private String action;
	private String status;
	
	// Related document
	private String docName;
	
	private boolean suspended;
	private boolean completed;
	
	private String jobId;
	
	private boolean submittedForAuth;
	private boolean batchErrors;
	private Integer auroraBatchId;
	private String authStatus;
	
	private Date dateAdded;
	private Date lastUpdated;
	
	public AccountAction(int actionId, 
	 String sourceAccountNumExt, String sourceAccountCompany,
	 Integer sourceAccountClientId,
	 Integer sourceAccountNum, String sourceAccountFund,
	 String sourceAccountSubtitle,
	 Float sourceAccountCash, Float sourceAccountUnits,
	 Integer sourceAccountCorrId, String sourceAccountIncomeDistMethod,
	 boolean sourceAccountExternal, String sourceAccountShareClass,
	
	 String destAccountNumExt, String destAccountCompany, 
	 Integer destAccountClientId, 
	 Integer destAccountNum, String destAccountFund,
	 String destAccountSubtitle,
	 Float destAccountCash, Float destAccountUnits, 
	 Integer destAccountCorrId, String destAccountIncomeDistMethod,
	 boolean destAccountExternal, String destAccountShareClass,
	 
	 BankingDetails bankingDetails,
	
	 String userId, String action,
	 String status, String docName, 
	
	 boolean suspended, boolean completed, 
	
	 String jobId, boolean submittedForAuth, boolean batchErrors,
	
	Integer auroraBatchId, String authStatus,
	Date dateAdded, Date lastUpdated) {
		this.actionId = actionId;
		this.sourceAccountNumExt = sourceAccountNumExt;
		this.sourceAccountCompany = sourceAccountCompany;
		this.sourceAccountClientId = sourceAccountClientId;
		this.sourceAccountNum = sourceAccountNum;
		this.sourceAccountFund = sourceAccountFund;
		this.sourceAccountSubtitle = sourceAccountSubtitle;
		this.sourceAccountCash = sourceAccountCash;
		this.sourceAccountUnits = sourceAccountUnits;
		this.sourceAccountCorrId = sourceAccountCorrId;
		this.sourceAccountIncomeDistMethod = sourceAccountIncomeDistMethod;
		this.sourceAccountExternal = sourceAccountExternal;
		this.sourceAccountShareClass = sourceAccountShareClass;
		
		this.destAccountNumExt = destAccountNumExt;
		this.destAccountCompany = destAccountCompany;
		this.destAccountClientId = destAccountClientId;
		this.destAccountNum = destAccountNum;
		this.destAccountFund = destAccountFund;
		this.destAccountSubtitle = destAccountSubtitle;
		this.destAccountCash = destAccountCash;
		this.destAccountUnits = destAccountUnits;
		this.destAccountCorrId = destAccountCorrId;
		this.destAccountIncomeDistMethod = destAccountIncomeDistMethod;
		this.destAccountExternal = destAccountExternal;
		this.destAccountShareClass = destAccountShareClass;
		
		this.bankingDetails = bankingDetails;
		
		this.userId = userId;
		this.action = action;
		this.status = status;
		this.docName = docName;
		
		this.suspended = suspended;
		this.completed = completed;
		
		this.jobId = jobId;
		this.submittedForAuth = submittedForAuth;
		this.batchErrors = batchErrors;
		this.auroraBatchId = auroraBatchId;
		this.authStatus = authStatus;
		
		this.dateAdded = dateAdded;
		this.lastUpdated = lastUpdated;
	}
	
	/** Adds a new Account Action audit message, tied to this Account Action
	 *  instance.
	 *  
	 * @param type
	 * @param description
	 * @param userId
	 * @param facade
	 * @throws DataException
	 */
	public void addAudit(AccountActionAudit.ActionType type, 
	 String description, String userId, FWFacade facade) 
	 throws DataException {
		
		AccountActionAudit.add(this.getActionId(), type, 
		 description, userId, facade);
	}
	
	public static AccountAction get(int actionId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsAccountAction = getData(actionId, facade);
		
		if (rsAccountAction == null || rsAccountAction.isEmpty())
			return null;
		else
			return get(rsAccountAction);
	}
	
	public static Vector<AccountAction> getAccountActionsByBatchId
	 (String batchId, FWFacade facade) throws DataException {
		
		DataResultSet rsAccountActions = 
		 getAccountActionsDataByBatchId(batchId, facade);
		
		Vector<AccountAction> accountActions =
		 new Vector<AccountAction>();
		
		if (rsAccountActions.first()) {
			do {
				accountActions.add(get(rsAccountActions));
			} while (rsAccountActions.next());
		}
		
		return accountActions;
	}
	
	/** Fetches a DataResult of all matched entries from the 
	 *  CCLA_ACCOUNT_ACTIONS table which have the corresponding 
	 *  AURORA_BATCH_ID value.
	 *  
	 * @param batchId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAccountActionsDataByBatchId
	 (String batchId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("batchId", batchId);
		
		return facade.createResultSet
		 ("qClientServices_GetAccountActionsByBatchId", binder);
	}
	
	public static AccountAction get(DataResultSet rsAccountAction) 
	 throws DataException {
		
		int actionId = Integer.parseInt
		 (rsAccountAction.getStringValueByName("ACCOUNT_ACTION_ID"));
		
		// Generate new BankingDetails instance, if applicable
		BankingDetails bankingDetails = BankingDetails.get(rsAccountAction);
		AccountAction action = new AccountAction(
			actionId,
			
			rsAccountAction.getStringValueByName("SOURCE_ACC_NUM_EXT"),
			rsAccountAction.getStringValueByName("SOURCE_ACC_COMPANY"),
			CCLAUtils.getResultSetIntegerValue(rsAccountAction, "SOURCE_ACC_CLIENT_ID"),
			CCLAUtils.getResultSetIntegerValue(rsAccountAction, "SOURCE_ACC_NUM"),
			rsAccountAction.getStringValueByName("SOURCE_ACC_FUND"),
			rsAccountAction.getStringValueByName("SOURCE_ACC_SUBTITLE"),
			CCLAUtils.getResultSetFloatValue(rsAccountAction, "SOURCE_ACC_CASH"),
			CCLAUtils.getResultSetFloatValue(rsAccountAction, "SOURCE_ACC_UNITS"),
			CCLAUtils.getResultSetIntegerValue(rsAccountAction, "SOURCE_ACC_CORR_ID"),
			rsAccountAction.getStringValueByName("SOURCE_ACC_INCOME_DIST_METHOD"),
			CCLAUtils.getResultSetBoolValue(rsAccountAction, "SOURCE_ACC_IS_EXTERNAL"),	
			rsAccountAction.getStringValueByName("SOURCE_ACC_SHARE_CLASS"),
			rsAccountAction.getStringValueByName("DEST_ACC_NUM_EXT"),
			rsAccountAction.getStringValueByName("DEST_ACC_COMPANY"),
			CCLAUtils.getResultSetIntegerValue(rsAccountAction, "DEST_ACC_CLIENT_ID"),
			CCLAUtils.getResultSetIntegerValue(rsAccountAction, "DEST_ACC_NUM"),
			rsAccountAction.getStringValueByName("DEST_ACC_FUND"),
			rsAccountAction.getStringValueByName("DEST_ACC_SUBTITLE"),
			CCLAUtils.getResultSetFloatValue(rsAccountAction, "DEST_ACC_CASH"),
			CCLAUtils.getResultSetFloatValue(rsAccountAction, "DEST_ACC_UNITS"),
			CCLAUtils.getResultSetIntegerValue(rsAccountAction, "DEST_ACC_CORR_ID"),
			rsAccountAction.getStringValueByName("DEST_ACC_INCOME_DIST_METHOD"),
			CCLAUtils.getResultSetBoolValue(rsAccountAction, "DEST_ACC_IS_EXTERNAL"),	
			rsAccountAction.getStringValueByName("DEST_ACC_SHARE_CLASS"),
			bankingDetails,
			
			rsAccountAction.getStringValueByName("USER_ID"),
			rsAccountAction.getStringValueByName("ACTION"),
			rsAccountAction.getStringValueByName("STATUS"),
			rsAccountAction.getStringValueByName("DOCNAME"),
			
			CCLAUtils.getResultSetBoolValue(rsAccountAction, "SUSPENDED"),
			CCLAUtils.getResultSetBoolValue(rsAccountAction, "COMPLETED"),
			
			rsAccountAction.getStringValueByName("JOB_ID"),
			CCLAUtils.getResultSetBoolValue(rsAccountAction, "SUBMITTED_FOR_AUTH"),
			CCLAUtils.getResultSetBoolValue(rsAccountAction, "BATCH_ERRORS"),
			CCLAUtils.getResultSetIntegerValue(rsAccountAction, "AURORA_BATCH_ID"),
			rsAccountAction.getStringValueByName("AUTH_STATUS"),
			
			rsAccountAction.getDateValueByName("DATE_ADDED"),
			rsAccountAction.getDateValueByName("LAST_UPDATED")
		);
		
		return action;
	}
	
	public static AccountAction add(DataBinder binder, FWFacade facade) 
	 throws DataException, ServiceException {
		
		facade.beginTransaction();
		
		try {
		
			String idStr = CCLAUtils.getNewKey("AccountAction", facade);
			int id = Integer.parseInt(idStr);
			
			boolean hasBankingDetails = !StringUtils.stringIsBlank(
			 binder.getLocal("hasBankingDetails"));
			BankingDetails bankingDetails = null;
			
			if (hasBankingDetails) {
				// Create new BankingDetails instance first
				bankingDetails = BankingDetails.add(binder, facade);
			}
			
			Date curDate = new Date();
			
			AccountAction action = new AccountAction(
				id,
				
				binder.getLocal("sourceAccNumExt"),
				binder.getLocal("sourceAccCompany"),
				CCLAUtils.getBinderIntegerValue(binder, "sourceAccClientId"),
				CCLAUtils.getBinderIntegerValue(binder, "sourceAccAccountNum"),
				binder.getLocal("sourceAccFund"),
				
				binder.getLocal("sourceAccSubtitle"),
				CCLAUtils.getBinderFloatValue(binder, "sourceAccCash"),
				CCLAUtils.getBinderFloatValue(binder, "sourceAccUnits"),
				CCLAUtils.getBinderIntegerValue(binder, "sourceAccCorrId"),
				binder.getLocal("sourceAccIncomeDistMethod"),
				CCLAUtils.getBinderBoolValue(binder, "sourceAccIsExternal"),
				binder.getLocal("sourceAccShareClass"),
				binder.getLocal("destAccNumExt"),
				binder.getLocal("destAccCompany"),
				CCLAUtils.getBinderIntegerValue(binder, "destAccClientId"),
				CCLAUtils.getBinderIntegerValue(binder, "destAccAccountNum"),
				binder.getLocal("destAccFund"),
				
				binder.getLocal("destAccSubtitle"),
				CCLAUtils.getBinderFloatValue(binder, "destAccCash"),
				CCLAUtils.getBinderFloatValue(binder, "destAccUnits"),
				CCLAUtils.getBinderIntegerValue(binder, "destAccCorrId"),
				binder.getLocal("destAccIncomeDistMethod"),
				CCLAUtils.getBinderBoolValue(binder, "destAccIsExternal"),
				binder.getLocal("DEST_ACC_SHARE_CLASS"),
				bankingDetails,
				
				binder.getLocal("userId"),
				binder.getLocal("action"),
				binder.getLocal("status"),
				binder.getLocal("docName"),
				
				false, false,
				
				binder.getLocal("jobId"),
				CCLAUtils.getBinderBoolValue(binder, "submittedForAuth"),
				CCLAUtils.getBinderBoolValue(binder, "batchErrors"),
				CCLAUtils.getBinderIntegerValue(binder, "auroraBatchId"),
				binder.getLocal("authStatus"),
				
				curDate, curDate
			);
			
			// Ensure all fields have blank string values using this method
			action.addFieldsToBinder(binder);
			facade.execute("qClientServices_AddAccountAction", binder);
			facade.commitTransaction();
			
			return action;
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			Log.error("Failed to add new account action", e);
			throw new ServiceException("Failed to add new account action", e);
		}
	}
	
	public void persist(FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateAccountAction", binder);
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		
		Integer bankingDetailsId = null;

		if (this.getBankingDetails() != null)
			bankingDetailsId = bankingDetails.getBankingDetailsId();
		
		binder.putLocal("actionId", 
		 Integer.toString(this.getActionId()));
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "sourceAccNumExt", this.getSourceAccountNumExt());
		CCLAUtils.addQueryParamToBinder
		 (binder, "sourceAccCompany", this.getSourceAccountCompany());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "sourceAccClientId", this.getSourceAccountClientId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "sourceAccAccountNum", this.getSourceAccountNum());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "sourceAccFund", this.getSourceAccountFund());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "sourceAccSubtitle", this.getSourceAccountSubtitle());
		
		CCLAUtils.addQueryFloatParamToBinder
		 (binder, "sourceAccCash", this.getSourceAccountCash());
		CCLAUtils.addQueryFloatParamToBinder
		 (binder, "sourceAccUnits", this.getSourceAccountUnits());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "sourceAccCorrId", this.getSourceAccountCorrId());
		CCLAUtils.addQueryParamToBinder
		 (binder, "sourceAccIncomeDistMethod", this.getSourceAccountIncomeDistMethod());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "sourceAccIsExternal", this.isSourceAccountExternal());
		CCLAUtils.addQueryParamToBinder
			(binder, "SOURCE_ACC_SHARE_CLASS", this.getSourceShareClass());
		CCLAUtils.addQueryParamToBinder
		 (binder, "destAccNumExt", this.getDestAccountNumExt());
		CCLAUtils.addQueryParamToBinder
		 (binder, "destAccCompany", this.getDestAccountCompany());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "destAccClientId", this.getDestAccountClientId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "destAccAccountNum", this.getDestAccountNum());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "destAccFund", this.getDestAccountFund());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "destAccSubtitle", this.getDestAccountSubtitle());
		
		CCLAUtils.addQueryFloatParamToBinder
		 (binder, "destAccCash", this.getDestAccountCash());
		CCLAUtils.addQueryFloatParamToBinder
		 (binder, "destAccUnits", this.getDestAccountUnits());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "destAccCorrId", this.getDestAccountCorrId());
		CCLAUtils.addQueryParamToBinder
		 (binder, "destAccIncomeDistMethod", this.getDestAccountIncomeDistMethod());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "destAccIsExternal", this.isDestAccountExternal());
		CCLAUtils.addQueryParamToBinder
		 (binder, "destAccShareClass", this.getDestAccountShareClass());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "bankingDetailsId", bankingDetailsId);
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "docName", this.getDocName());
		CCLAUtils.addQueryParamToBinder
		 (binder, "userId", this.getUserId());
		CCLAUtils.addQueryParamToBinder
		 (binder, "action", this.getAction());
		CCLAUtils.addQueryParamToBinder
		 (binder, "status", this.getStatus());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "suspended", this.isSuspended());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "completed", this.isSuspended());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "jobId", this.getJobId());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "submittedForAuth", this.isSubmittedForAuth());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "batchErrors", this.isBatchErrors());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "auroraBatchId", this.getAuroraBatchId());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "authStatus", this.getAuthStatus());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "dateAdded", this.getDateAdded());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "lastUpdated", this.getLastUpdated());
	}
	
	public static DataResultSet getData(int actionId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("actionId", Integer.toString(actionId));
		
		return facade.createResultSet
		 ("qClientServices_GetAccountAction", binder);
	}

	public Integer getSourceAccountClientId() {
		return sourceAccountClientId;
	}

	public void setSourceAccountClientId(Integer sourceAccountClientId) {
		this.sourceAccountClientId = sourceAccountClientId;
	}

	public Integer getSourceAccountNum() {
		return sourceAccountNum;
	}

	public void setSourceAccountNum(Integer sourceAccountNum) {
		this.sourceAccountNum = sourceAccountNum;
	}

	public String getSourceAccountFund() {
		return sourceAccountFund;
	}

	public void setSourceAccountFund(String sourceAccountFund) {
		this.sourceAccountFund = sourceAccountFund;
	}

	public Float getSourceAccountCash() {
		return sourceAccountCash;
	}

	public void setSourceAccountCash(Float sourceAccountCash) {
		this.sourceAccountCash = sourceAccountCash;
	}

	public Float getSourceAccountUnits() {
		return sourceAccountUnits;
	}

	public void setSourceAccountUnits(Float sourceAccountUnits) {
		this.sourceAccountUnits = sourceAccountUnits;
	}

	public Integer getDestAccountClientId() {
		return destAccountClientId;
	}

	public void setDestAccountClientId(Integer destAccountClientId) {
		this.destAccountClientId = destAccountClientId;
	}

	public Integer getDestAccountNum() {
		return destAccountNum;
	}

	public void setDestAccountNum(Integer destAccountNum) {
		this.destAccountNum = destAccountNum;
	}

	public String getDestAccountFund() {
		return destAccountFund;
	}

	public void setDestAccountFund(String destAccountFund) {
		this.destAccountFund = destAccountFund;
	}

	public Float getDestAccountCash() {
		return destAccountCash;
	}

	public void setDestAccountCash(Float destAccountCash) {
		this.destAccountCash = destAccountCash;
	}

	public Float getDestAccountUnits() {
		return destAccountUnits;
	}

	public void setDestAccountUnits(Float destAccountUnits) {
		this.destAccountUnits = destAccountUnits;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public boolean isSubmittedForAuth() {
		return submittedForAuth;
	}

	public void setSubmittedForAuth(boolean submittedForAuth) {
		this.submittedForAuth = submittedForAuth;
	}

	public boolean isBatchErrors() {
		return batchErrors;
	}

	public void setBatchErrors(boolean batchErrors) {
		this.batchErrors = batchErrors;
	}

	public Integer getAuroraBatchId() {
		return auroraBatchId;
	}

	public void setAuroraBatchId(Integer auroraBatchId) {
		this.auroraBatchId = auroraBatchId;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public int getActionId() {
		return actionId;
	}

	public Integer getSourceAccountCorrId() {
		return sourceAccountCorrId;
	}

	public void setSourceAccountCorrId(Integer sourceAccountCorrId) {
		this.sourceAccountCorrId = sourceAccountCorrId;
	}

	public boolean isSourceAccountExternal() {
		return sourceAccountExternal;
	}

	public void setSourceAccountExternal(boolean sourceAccountIsExternal) {
		this.sourceAccountExternal = sourceAccountIsExternal;
	}

	public Integer getDestAccountCorrId() {
		return destAccountCorrId;
	}

	public void setDestAccountCorrId(Integer destAccountCorrId) {
		this.destAccountCorrId = destAccountCorrId;
	}

	public boolean isDestAccountExternal() {
		return destAccountExternal;
	}

	public void setDestAccountExternal(boolean destAccountIsExternal) {
		this.destAccountExternal = destAccountIsExternal;
	}

	public BankingDetails getBankingDetails() {
		return bankingDetails;
	}

	public void setBankingDetails(BankingDetails bankingDetails) {
		this.bankingDetails = bankingDetails;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public String getSourceAccountCompany() {
		return sourceAccountCompany;
	}

	public void setSourceAccountCompany(String sourceAccountCompany) {
		this.sourceAccountCompany = sourceAccountCompany;
	}

	public String getDestAccountCompany() {
		return destAccountCompany;
	}

	public void setDestAccountCompany(String destAccountCompany) {
		this.destAccountCompany = destAccountCompany;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getSourceAccountIncomeDistMethod() {
		return sourceAccountIncomeDistMethod;
	}

	public void setSourceAccountIncomeDistMethod(
			String sourceAccountIncomeDistMethod) {
		this.sourceAccountIncomeDistMethod = sourceAccountIncomeDistMethod;
	}

	public String getDestAccountIncomeDistMethod() {
		return destAccountIncomeDistMethod;
	}

	public void setDestAccountIncomeDistMethod(String destAccountIncomeDistMethod) {
		this.destAccountIncomeDistMethod = destAccountIncomeDistMethod;
	}

	public String getSourceAccountSubtitle() {
		return sourceAccountSubtitle;
	}

	public void setSourceAccountSubtitle(String sourceAccountSubtitle) {
		this.sourceAccountSubtitle = sourceAccountSubtitle;
	}

	public String getDestAccountSubtitle() {
		return destAccountSubtitle;
	}

	public void setDestAccountSubtitle(String destAccountSubtitle) {
		this.destAccountSubtitle = destAccountSubtitle;
	}

	public String getSourceAccountNumExt() {
		return sourceAccountNumExt;
	}

	public void setSourceAccountNumExt(String sourceAccountNumExt) {
		this.sourceAccountNumExt = sourceAccountNumExt;
	}

	public String getDestAccountNumExt() {
		return destAccountNumExt;
	}

	public void setDestAccountNumExt(String destAccountNumExt) {
		this.destAccountNumExt = destAccountNumExt;
	}
	
	public String getSourceShareClass() {
		return sourceAccountShareClass;
	}

	public void setSourceAccountShareClass(String sourceShareClass) {
		this.sourceAccountShareClass = sourceShareClass;
	}	
	
	public String getDestAccountShareClass() {
		return destAccountShareClass;
	}

	public void setDestAccountShareClass(String destShareClass) {
		this.destAccountShareClass = destShareClass;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getAuthStatus() {
		return authStatus;
	}	
}
