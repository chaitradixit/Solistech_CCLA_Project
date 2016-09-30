package com.ecs.ucm.ccla.data.instruction;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import java.util.Date;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;


/**
 * MODELS INSTRUCTION_AUDIT table
 * @author Cam
 *
 */

public class InstructionAudit implements Persistable {

	/* **************** Constants **************** */
	//BINDER AND DB COLUMN
	public static final String INSTRUCTION_AUDIT_ID 	= "INSTRUCTION_AUDIT_ID";
	public static final String INSTR_AUDIT_ACTION_ID 	= "INSTR_AUDIT_ACTION_ID";
	public static final String INSTRUCTION_ID 			= SharedCols.INSTRUCTION;
	public static final String MODULE_ID 				= "MODULE_ID";
	public static final String AUDIT_DATE 				= "AUDIT_DATE";
	public static final String TIME_ELAPSED 			= "TIME_ELAPSED";
	public static final String INSTRUCTION_STATUS_ID	= InstructionStatus.Cols.ID;
	public static final String INSTRUCTION_ACTION_ID	= "INSTRUCTION_ACTION_ID";
	public static final String USER						= SharedCols.USER;
	

	//QUERIES
	//private static final String GET_ALL_QUERY_NAME = "qCore_GetAllInstructionAudit";	
	private static final String ADD_QUERY_NAME = "qCore_AddInstructionAudit";	
	private static final String UPDATE_QUERY_NAME = "qCore_UpdateInstructionAudit";	
	private static final String GET_BY_ID_QUERY_NAME = "qCore_GetInstructionAuditById";	

	
	/** Properties **/
	private Integer instructionAuditId;
	private Integer instructionId;
	private Integer instructionAuditActionId;
	private Integer moduleId;
	private Integer instructionStatusId;
	private Integer instructionActionId;
	private Date auditDate;
	private Integer timeElapse;
	private String userName;
	
	public InstructionAudit(Integer instructionAuditId, Integer instructionId,
			Integer instructionAuditActionId, Integer moduleId,
			Integer instructionStatusId, Integer instructionActionId,
			Date auditDate, Integer timeElapse, String userName) {
		this.instructionAuditId = instructionAuditId;
		this.instructionId = instructionId;
		this.instructionAuditActionId = instructionAuditActionId;
		this.moduleId = moduleId;
		this.instructionStatusId = instructionStatusId;
		this.instructionActionId = instructionActionId;
		this.auditDate = auditDate;
		this.timeElapse = timeElapse;
		this.setUserName(userName);
	}
	
	
	public InstructionAudit(DataBinder binder) throws DataException {
			this.setAttributes(binder);
	}

	public Integer getTimeElapse() {
		return timeElapse;
	}

	public void setTimeElapse(Integer timeElapse) {
		this.timeElapse = timeElapse;
	}
	
	public void setInstructionAuditId(Integer instructionAuditId) {
		this.instructionAuditId = instructionAuditId;
	}
	
	public Integer getInstructionAuditId() {
		return instructionAuditId;
	}
	
	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}
	
	public Integer getInstructionId() {
		return instructionId;
	}
	public void setInstructionAuditActionId(Integer instructionAuditActionId) {
		this.instructionAuditActionId = instructionAuditActionId;
	}
	
	public Integer getInstructionAuditActionId() {
		return instructionAuditActionId;
	}
	
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	
	public Integer getModuleId() {
		return moduleId;
	}
	
	public void setInstructionStatusId(Integer instructionStatusId) {
		this.instructionStatusId = instructionStatusId;
	}
	
	public Integer getInstructionStatusId() {
		return instructionStatusId;
	}
	
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	
	public Date getAuditDate() {
		return auditDate;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserName() {
		return userName;
	}	
	
	public void setAttributes(DataBinder binder) throws DataException {
		this.setAuditDate(CCLAUtils.getBinderDateValue(binder, AUDIT_DATE));
		this.setInstructionAuditActionId(CCLAUtils.getBinderIntegerValue(binder, INSTR_AUDIT_ACTION_ID));
		this.setInstructionAuditId(CCLAUtils.getBinderIntegerValue(binder, INSTRUCTION_AUDIT_ID));
		this.setInstructionId(CCLAUtils.getBinderIntegerValue(binder, INSTRUCTION_ID));
		this.setInstructionStatusId(CCLAUtils.getBinderIntegerValue(binder, INSTRUCTION_STATUS_ID));
		this.setInstructionActionId(CCLAUtils.getBinderIntegerValue(binder, INSTRUCTION_ACTION_ID));
		this.setModuleId(CCLAUtils.getBinderIntegerValue(binder, MODULE_ID));
		this.setTimeElapse(CCLAUtils.getBinderIntegerValue(binder, TIME_ELAPSED));
		this.setUserName(binder.getLocal(USER));
	}


	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, INSTRUCTION_AUDIT_ID, this.getInstructionAuditId());
		CCLAUtils.addQueryIntParamToBinder(binder, INSTR_AUDIT_ACTION_ID, this.getInstructionAuditActionId());
		CCLAUtils.addQueryIntParamToBinder(binder, INSTRUCTION_ID, this.getInstructionId());
		CCLAUtils.addQueryIntParamToBinder(binder, INSTRUCTION_STATUS_ID, this.getInstructionStatusId());
		CCLAUtils.addQueryIntParamToBinder(binder, INSTRUCTION_ACTION_ID, this.getInstructionActionId());
		CCLAUtils.addQueryIntParamToBinder(binder, MODULE_ID, this.getModuleId());
		CCLAUtils.addQueryIntParamToBinder(binder, TIME_ELAPSED, this.getTimeElapse());
		CCLAUtils.addQueryDateParamToBinder(binder, AUDIT_DATE, this.getAuditDate());
		CCLAUtils.addQueryParamToBinder(binder, USER, this.getUserName());
	}


	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		this.setUserName(username);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		facade.execute(UPDATE_QUERY_NAME, binder);	
	}


	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}
	
	
	public static InstructionAudit add(DataBinder binder, String username, FWFacade facade) 
	throws DataException {
		InstructionAudit instructionAudit = new InstructionAudit(binder);
		return InstructionAudit.add(instructionAudit, facade);
	}
		
	public static InstructionAudit add(InstructionAudit instructionAudit, FWFacade facade) throws DataException 
	{
		if (instructionAudit.getInstructionAuditId()==null) {
			instructionAudit.setInstructionAuditId(
					Integer.parseInt(
							CCLAUtils.getNewKey("InstructionAudit", facade)));
		}
		

		instructionAudit.validate(facade);			
		DataBinder binder = new DataBinder();
		instructionAudit.addFieldsToBinder(binder);
		facade.execute(ADD_QUERY_NAME, binder);			
		
		return InstructionAudit.get(instructionAudit.getInstructionAuditId(), facade);
	}		
		
	public static InstructionAudit get(int instructionAuditId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(instructionAuditId, facade);
		return get(rs);
	}
	
	
	public static DataResultSet getData(int instructionAuditId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_AUDIT_ID, instructionAuditId);
		DataResultSet rsAudit = facade.createResultSet
		 (GET_BY_ID_QUERY_NAME, binder);
		
		return rsAudit;
	}		
	
	
	public static InstructionAudit get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		
		return new InstructionAudit(
			CCLAUtils.getResultSetIntegerValue(rs, INSTRUCTION_AUDIT_ID),
			CCLAUtils.getResultSetIntegerValue(rs, INSTRUCTION_ID),
			CCLAUtils.getResultSetIntegerValue(rs, INSTR_AUDIT_ACTION_ID),
			CCLAUtils.getResultSetIntegerValue(rs, INSTRUCTION_STATUS_ID),
			CCLAUtils.getResultSetIntegerValue(rs, INSTRUCTION_ACTION_ID),
			CCLAUtils.getResultSetIntegerValue(rs, MODULE_ID),
			rs.getDateValueByName(AUDIT_DATE),
			CCLAUtils.getResultSetIntegerValue(rs, TIME_ELAPSED),
			CCLAUtils.getResultSetStringValue(rs, USER)
		);
	}


	public void setInstructionActionId(Integer instructionActionId) {
		this.instructionActionId = instructionActionId;
	}

	public Integer getInstructionActionId() {
		return instructionActionId;
	}
}
	
