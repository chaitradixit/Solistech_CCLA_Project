package com.ecs.stellent.spp.data;

import java.util.StringTokenizer;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** Models single entries from the JOB_PRIORITY_RULES table.
 * 
 * @author Tom
 *
 */
public class JobPriorityRule {
	
	public enum RuleType {
		BundleData, ItemData
	}
	
	private int ruleId;
	private String name;
	
	private String fieldName;
	private RuleType ruleType;
	
	private int order;
	private boolean isAppendPriority = false; //default behaviour 
	private boolean enabled;
	private String appliesToBundleStatusStr;
	private Vector<String> appliesToBundleStatusVec;
	
	/** Stores a list of values associated with this Job Rule */
	private Vector<JobPriorityRuleValue> ruleValues;

	public JobPriorityRule(int ruleId, String name, String fieldName,
			RuleType ruleType, int order, boolean enabled, String appliesToBundleStatusStr, 
			boolean isAppendPriority) {
		this.ruleId = ruleId;
		this.name = name;
		this.fieldName = fieldName;
		this.ruleType = ruleType;
		this.order = order;
		this.enabled = enabled;
		this.setAppliesToBundleValuesString(appliesToBundleStatusStr);
		this.isAppendPriority = isAppendPriority;
	}
	
	public static DataResultSet getAllJobPriorityRulesData(FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		return facade.createResultSet("QGetJobPriorityRules", binder);
	}
	
	public static JobPriorityRule get(DataResultSet rs) {
		
		String isEnabledStr = rs.getStringValueByName("IS_ENABLED");
		boolean enabled = 
		 (isEnabledStr != null && isEnabledStr.equals("1"));
		
		RuleType ruleType = null;
		
		String ruleTypeStr	= rs.getStringValueByName("RULE_TYPE");
		if (ruleTypeStr != null)
			ruleType = RuleType.valueOf(ruleTypeStr);
			
	
		return new JobPriorityRule(
			Integer.parseInt(rs.getStringValueByName("RULE_ID")),
			rs.getStringValueByName("RULE_NAME"),
			rs.getStringValueByName("RULE_FIELD"),
			ruleType,
			Integer.parseInt(rs.getStringValueByName("RULE_ORDER")),
			enabled,
			rs.getStringValueByName("APPLIES_TO"),
			CCLAUtils.getResultSetBoolValue(rs, "IS_APPEND_PRIORITY")
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		
		String isEnabledStr = "0";
		
		if (enabled)
			isEnabledStr = "1";
		
		binder.putLocal("RULE_ID", Integer.toString(ruleId));
		binder.putLocal("RULE_NAME", name);
		binder.putLocal("RULE_FIELD", fieldName);
		binder.putLocal("RULE_TYPE", ruleType.toString());
		binder.putLocal("RULE_ORDER", Integer.toString(order));
		binder.putLocal("IS_ENABLED", isEnabledStr);
		binder.putLocal("APPLIES_TO", appliesToBundleStatusStr);
		CCLAUtils.addQueryBooleanParamToBinder(binder, "IS_APPEND_PRIORITY", isAppendPriority);
	}
	
	public void setAttributes(DataBinder binder) {
		
		boolean enabled 	= false;
		String enabledStr	= binder.getLocal("IS_ENABLED");
		String appliesToBundleStatusStr = binder.getLocal("APPLIES_TO");
			
		if (!StringUtils.stringIsBlank(enabledStr)
			&&
			!enabledStr.equals("0")) {
			enabled = true;
		}
		
		this.setName(binder.getLocal("RULE_NAME"));
		this.setFieldName(binder.getLocal("RULE_FIELD"));
		
		this.setRuleType(RuleType.valueOf(binder.getLocal("RULE_TYPE")));
		this.setOrder(Integer.parseInt(binder.getLocal("RULE_ORDER")));
		this.setEnabled(enabled);
		this.setAppliesToBundleValuesString(appliesToBundleStatusStr);
		this.setAppendPriority(CCLAUtils.getBinderBoolValue(binder,"IS_APPEND_PRIORITY"));
	}
	
	public void persist(FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		addFieldsToBinder(binder);
		
		facade.execute("QUpdateJobPriorityRule", binder);
	}
	
	/** Adds a new Job Priority Rule. The primary key RULE_ID
	 *  is derived using a database trigger on row insert.
	 *  
	 *  The rule is disabled by default.
	 *  
	 * @param ruleId
	 * @param fieldValue
	 * @param priority
	 * @param facade
	 * @throws DataException
	 */
	public static void add(String name, String field, RuleType ruleType,
	 Integer order, boolean isAppendPriority, FWFacade facade) throws DataException {
		
		//Default Values
		String isEnabled = "0";
		String appliesToBundleStatusStr = null;
		
		DataBinder binder = new DataBinder();
		
		binder.putLocal("RULE_NAME", name);
		binder.putLocal("RULE_FIELD", field);
		binder.putLocal("RULE_TYPE", ruleType.toString());
		binder.putLocal("RULE_ORDER", Integer.toString(order));
		binder.putLocal("IS_ENABLED", isEnabled);
		CCLAUtils.addQueryParamToBinder(binder, "APPLIES_TO", appliesToBundleStatusStr);
		CCLAUtils.addQueryBooleanParamToBinder(binder, "IS_APPEND_PRIORITY", isAppendPriority);
		facade.execute("QAddJobPriorityRule", binder);
	}

	
	/** Adds a new Job Priority Rule. The primary key RULE_ID
	 *  is derived using a database trigger on row insert.
	 *  
	 *  The rule is disabled by default.
	 *  
	 * @param ruleId
	 * @param fieldValue
	 * @param priority
	 * @param facade
	 * @throws DataException
	 */
	public static void add(String name, String field, RuleType ruleType,
	 Integer order, String isEnabled, String appliesToBundleStatusStr, 
	 boolean isAppendPriority, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		binder.putLocal("RULE_NAME", name);
		binder.putLocal("RULE_FIELD", field);
		binder.putLocal("RULE_TYPE", ruleType.toString());
		binder.putLocal("RULE_ORDER", Integer.toString(order));
		binder.putLocal("IS_ENABLED", isEnabled);
		CCLAUtils.addQueryParamToBinder(binder, "APPLIES_TO", appliesToBundleStatusStr);
		CCLAUtils.addQueryBooleanParamToBinder(binder, "IS_APPEND_PRIORITY", isAppendPriority);

		facade.execute("QAddJobPriorityRule", binder);
	}

	
	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setRuleValues(Vector<JobPriorityRuleValue> ruleValues) {
		this.ruleValues = ruleValues;
	}

	public Vector<JobPriorityRuleValue> getRuleValues() {
		return ruleValues;
	}

	public void setAppliesToBundleValuesString(String appliesToBundleStatusStr) {
		this.appliesToBundleStatusStr = appliesToBundleStatusStr;		
		
		if (appliesToBundleStatusStr!=null && !appliesToBundleStatusStr.equals("")) 
		{
			if (appliesToBundleStatusVec==null) {
				appliesToBundleStatusVec = new Vector<String>(2);
			} else if (!appliesToBundleStatusVec.isEmpty()) {
					appliesToBundleStatusVec.clear();
			}
		
			StringTokenizer st = new StringTokenizer(appliesToBundleStatusStr,",");
			
			while (st.hasMoreTokens()) 
			{
				String str = st.nextToken().trim();
				if (str!=null && !str.equals(""))
					appliesToBundleStatusVec.add(str);
			}
		}
		else {
			if (appliesToBundleStatusVec!=null && !appliesToBundleStatusVec.isEmpty()) {
					appliesToBundleStatusVec.clear();
			}
		}
	}

	public String getAppliesToBundleValuesString() {
		return appliesToBundleStatusStr;
	}

	public boolean containsAppliesToStatus(String status) {
		//Default behaviour, always return true		
		if (appliesToBundleStatusVec==null || appliesToBundleStatusVec.isEmpty()) 
			return true;
		else
			return appliesToBundleStatusVec.contains(status);
	}

	public boolean isAppendPriority() {
		return isAppendPriority;
	}

	public void setAppendPriority(boolean isAppendPriority) {
		this.isAppendPriority = isAppendPriority;
	}
	
}
