package com.ecs.ucm.ccla.data.instruction;

import java.util.Date;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class InstructionOld implements Persistable {
	
	private int instructionId;
	private int instructionStatusId;
	private Date processDate;
	private Date actualProcessDate;
	private int instructionTypeId;
	private String sppJobId;
	private Date lastUpdated;
	private Date dateAdded;
	private String lastUpdatedBy;
	private String dependentInstructionId;
	private int commId;
	private String instructionTypeName;
	private String instructionDescription;
	private boolean isFinancialTransaction;
	private boolean SubmitToSpp;
	private String instructionStatusName;
	private String instructionDocId;
	private int priority;

	public InstructionOld(int instructionId, int instructionStatusId, Date processDate, Date actualProcessDate,
			int instructionTypeId, String sppJobId, Date lastUpdated, Date dateAdded, String lastUpdatedBy,
			String dependentInstructionId, int commId, String instructionTypeName, String instructionDescription,
			boolean isFinancialTransaction, boolean SubmitToSpp, String instructionStatusName, String instructionDocId, int priority)
	{
		this.instructionId = instructionId;
		this.instructionStatusId = instructionStatusId;
		this.processDate = processDate;
		this.actualProcessDate = actualProcessDate;
		this.instructionTypeId = instructionTypeId;
		this.sppJobId = sppJobId;
		this.lastUpdated = lastUpdated;
		this.dateAdded = dateAdded;
		this.lastUpdatedBy = lastUpdatedBy;
		this.dependentInstructionId = dependentInstructionId;
		this.commId = commId;
		this.instructionTypeName = instructionTypeName;
		this.instructionDescription = instructionDescription;
		this.isFinancialTransaction = isFinancialTransaction;
		this.SubmitToSpp = SubmitToSpp;
		this.instructionStatusName = instructionStatusName;
		this.instructionDocId = instructionDocId;
		this.priority = priority;
	}

	public static InstructionOld get(int instructionId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(instructionId, facade);
		return get(rs);
		
	}
	
	public static InstructionOld get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new InstructionOld(
					DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_STATUS_ID"),
					rs.getDateValueByName("PROCESS_DATE"),
					rs.getDateValueByName("ACTUAL_PROCESS_DATE"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_TYPE_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "SPP_JOB_ID"),
					rs.getDateValueByName("LAST_UPDATED"),
					rs.getDateValueByName("DATE_ADDED"),
					DataResultSetUtils.getResultSetStringValue(rs, "LAST_UPDATED_BY"),
					DataResultSetUtils.getResultSetStringValue(rs, "DEPENDENT_INSTRUCTION_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "COMM_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "INSTRUCTION_TYPE_NAME"),
					DataResultSetUtils.getResultSetStringValue(rs, "INSTRUCTION_DESCRIPTION"),
					DataResultSetUtils.getResultSetBoolValue(rs, "IS_FINANCIAL_TRANSACTION"),
					DataResultSetUtils.getResultSetBoolValue(rs, "SUBMIT_TO_SPP"),
					DataResultSetUtils.getResultSetStringValue(rs, "INSTRUCTION_STATUS_NAME"),
					DataResultSetUtils.getResultSetStringValue(rs, "INSTRUCTION_DOC_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "PRIORITY")
					);
						}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		BinderUtils.addIntParamToBinder(binder, "INSTRUCTION_ID", this.getInstructionId());
		BinderUtils.addIntParamToBinder(binder, "INSTRUCTION_STATUS_ID", this.getInstructionStatusId());
		BinderUtils.addDateParamToBinder(binder, "PROCESS_DATE", this.getProcessDate());
		BinderUtils.addDateParamToBinder(binder, "ACTUAL_PROCESS_DATE", this.getActualProcessDate());		
		BinderUtils.addIntParamToBinder(binder, "INSTRUCTION_TYPE_ID", this.getInstructionTypeId());
		BinderUtils.addParamToBinder(binder, "SPP_JOB_ID", this.getSppJobId());
		BinderUtils.addDateParamToBinder(binder, "LAST_UPDATED", this.getLastUpdated());
		BinderUtils.addDateParamToBinder(binder, "DATE_ADDED", this.getDateAdded());
		BinderUtils.addParamToBinder(binder, "LAST_UPDATED_BY", this.getLastUpdatedBy());
		BinderUtils.addParamToBinder(binder, "DEPENDENT_INSTRUCTION_ID", this.getDependentInstructionId());
		BinderUtils.addIntParamToBinder(binder, "COMM_ID", this.getCommId());
		BinderUtils.addParamToBinder(binder, "INSTRUCTION_TYPE_NAME", this.getInstructionTypeName());
		BinderUtils.addParamToBinder(binder, "INSTRUCTION_DESCRIPTION", this.getInstructionDescription());
		BinderUtils.addBooleanParamToBinder(binder, "IS_FINANCIAL_TRANSACTION", this.isFinancialTransaction());
		BinderUtils.addBooleanParamToBinder(binder, "SUBMIT_TO_SPP", this.isSubmitToSpp());
		BinderUtils.addParamToBinder(binder, "INSTRUCTION_STATUS_NAME", this.instructionStatusName);
		BinderUtils.addParamToBinder(binder, "INSTRUCTION_DOC_ID", this.getInstructionDocId());
		BinderUtils.addIntParamToBinder(binder, "PRIORITY", this.getPriority());

	}

	
	public static DataResultSet getData(int instructionId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "INSTRUCTION_ID", instructionId);
		DataResultSet rsComm = facade.createResultSet("qCore_GetInstruction", binder);
		return rsComm;
	}
	
	
	/**
	 *  gets vector of applicable instruction data objects
	 * for a particular instruction - the data objects will depend on the
	 * instruction type
	 * 
	 * @return Vector<ApplicableInstructionData> or null if no objects found
     * @throws DataException
	 */
	public static Vector<ApplicableInstructionData> getApplicableInstructionData(int instructionId, FWFacade facade) 
	throws DataException
	{
		Vector<ApplicableInstructionData>  insData = new Vector<ApplicableInstructionData>();
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "INSTRUCTION_ID", instructionId);
		DataResultSet rs = facade.createResultSet("qCore_GetApplicableInstructionData", binder);
		if (rs.isEmpty())
			return null;
		else
		{
			do {
			ApplicableInstructionData aid = ApplicableInstructionData.get(rs);
			insData.add(aid);				
			} while (rs.next());
			return insData;
		}		
	}

	/**
	 *  gets vector of INSTRUCTUTION DATA APPLIED objects
	 * for a particular instruction - if a particular data object has no value
	 * then null is returned for that value but it will still be present in the results
	 * 
	 * @return Vector<InstructionDataApplied> or null if no objects found
     * @throws DataException
	 */	
	public static Vector<InstructionDataApplied> getInstructionDataApplied(int instructionId, FWFacade facade) throws DataException
	{
		Vector<InstructionDataApplied>  dataApplied = new Vector<InstructionDataApplied>();
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "INSTRUCTION_ID", instructionId);
		DataResultSet rs = facade.createResultSet("qCore_GetInstructionValues", binder);
		if (rs.isEmpty())
			return null;
		else
		{
			do {
				InstructionDataApplied ida = InstructionDataApplied.get(rs);
				dataApplied.add(ida);
			} while (rs.next());
			return dataApplied;
		}
	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		facade.execute("qCore_UpdateInstruction", binder);	
	}


	public void setAttributes(DataBinder binder) throws DataException {
		this.setInstructionStatusId(BinderUtils.getBinderIntegerValue(binder, "INSTRUCTION_STATUS_ID"));
		this.setProcessDate(BinderUtils.getBinderDateValue(binder, "PROCESS_DATE"));
		this.setActualProcessDate(BinderUtils.getBinderDateValue(binder, "ACTUAL_PROCESS_DATE"));
		this.setInstructionTypeId(BinderUtils.getBinderIntegerValue(binder, "INSTRUCTION_TYPE_ID"));
		this.setSppJobId(BinderUtils.getBinderStringValue(binder, "SPP_JOB_ID"));
		this.setLastUpdated(BinderUtils.getBinderDateValue(binder, "LAST_UPDATED"));
		this.setDateAdded(BinderUtils.getBinderDateValue(binder, "DATE_ADDED"));
		this.setLastUpdatedBy(BinderUtils.getBinderStringValue(binder, "LAST_UPDATED_BY"));
		this.setDependentInstructionId(BinderUtils.getBinderStringValue(binder, "DEPENDENT_INSTRUCTION_ID"));
		this.setCommId(BinderUtils.getBinderIntegerValue(binder, "COMM_ID"));
	}

	public InstructionOld(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static InstructionOld add(DataBinder binder, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		// get new id value
		int instructionId = Integer.parseInt(
				 CCLAUtils.getNewKey("Instruction", facade));
		InstructionOld instruction = new InstructionOld(binder);
		instruction.setInstructionId(instructionId);
		instruction.setLastUpdatedBy(username);
		instruction.addFieldsToBinder(binder);
		facade.execute("qCore_addInstruction", binder);
		return instruction;
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public int getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(int instructionId) {
		this.instructionId = instructionId;
	}

	public int getInstructionStatusId() {
		return instructionStatusId;
	}

	public void setInstructionStatusId(int instructionStatusId) {
		this.instructionStatusId = instructionStatusId;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}

	public Date getActualProcessDate() {
		return actualProcessDate;
	}

	public void setActualProcessDate(Date actualProcessDate) {
		this.actualProcessDate = actualProcessDate;
	}

	public int getInstructionTypeId() {
		return instructionTypeId;
	}

	public void setInstructionTypeId(int instructionTypeId) {
		this.instructionTypeId = instructionTypeId;
	}

	public String getSppJobId() {
		return sppJobId;
	}

	public void setSppJobId(String sppJobId) {
		this.sppJobId = sppJobId;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getDependentInstructionId() {
		return dependentInstructionId;
	}

	public void setDependentInstructionId(String dependentInstructionId) {
		this.dependentInstructionId = dependentInstructionId;
	}

	public int getCommId() {
		return commId;
	}

	public void setCommId(int commId) {
		this.commId = commId;
	}

	public String getInstructionTypeName() {
		return instructionTypeName;
	}

	public void setInstructionTypeName(String instructionTypeName) {
		// this is set by using the instruction type id
		// this.instructionTypeName = instructionTypeName;
	}

	public String getInstructionDescription() {
		return instructionDescription;
	}

	public void setInstructionDescription(String instructionDescription) {
		// this is set by changing the instruction type
		// this.instructionDescription = instructionDescription;
	}

	public boolean isFinancialTransaction() {
		return isFinancialTransaction;
	}

	public void setFinancialTransaction(boolean isFinancialTransaction) {
		// this is set by changing the instruction type
		// this.isFinancialTransaction = isFinancialTransaction;
	}

	public boolean isSubmitToSpp() {
		return SubmitToSpp;
	}

	public void setSubmitToSpp(boolean submitToSpp) {
		// this is set by changing the instruction type
		// SubmitToSpp = submitToSpp;
	}

	public String getInstructionStatusName() {
		return instructionStatusName;
	}

	public void setInstructionStatusName(String instructionStatusName) {
		// this is set using status id
		// this.instructionStatusName = instructionStatusName;
	}

	public String getInstructionDocId() {
		return instructionDocId;
	}

	public void setInstructionDocId(String instructionDocId) {
		this.instructionDocId = instructionDocId;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}


}
