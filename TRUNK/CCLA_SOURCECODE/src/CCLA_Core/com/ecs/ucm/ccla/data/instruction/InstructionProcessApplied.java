package com.ecs.ucm.ccla.data.instruction;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the INSTRUCTION_PROCESS_APPLIED table.
 * 
 * @author Tom
 *
 */
public class InstructionProcessApplied implements Persistable {

	private int instructionProcessAppliedId;
	
	private int instructionId;
	private InstructionProcess process;
	
	private String description;
	
	private Date startDate;
	private Date endDate;
	
	private Integer instructionActionId;
	private String userName;
	
	public InstructionProcessApplied(int instructionProcessAppliedId,
			int instructionId, InstructionProcess process, String description,
			Date startDate, Date endDate, 
			Integer instructionActionId, String userName) {
		this.instructionProcessAppliedId = instructionProcessAppliedId;
		this.instructionId = instructionId;
		this.setProcess(process);
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.setInstructionActionId(instructionActionId);
		this.userName = userName;
	}
	
	public static InstructionProcessApplied add
	 (int instructionId, InstructionProcess process, String description, 
	 FWFacade facade) throws DataException {
		
		if (hasActiveProcess(instructionId, facade))
			throw new DataException("Unable to add new Instruction Process, " +
			 "one is still active for this instruction");
		
		int id = Integer.parseInt(
		 CCLAUtils.getNewKey("InstructionProcessApplied", facade)
		);
		
		InstructionProcessApplied instrProcApplied = new InstructionProcessApplied(
			id, instructionId, process, description, new Date(), null, null, null
		);
		
		DataBinder binder = new DataBinder();
		instrProcApplied.addFieldsToBinder(binder);
		
		facade.execute("qCommProc_AddInstructionProcessApplied", binder);
		return instrProcApplied;
	}
	
	
	
	public static boolean hasActiveProcess(int instructionId, FWFacade facade) 
	 throws DataException {
		return !getActiveProcessData(instructionId, facade).isEmpty();
	}
	
	/** Fetches the active Instruction Process for the given instruction ID.
	 * 
	 * @param instructionId
	 * @param facade
	 * @return null, if there is no active Instruction Process for the Instruction.
	 * @throws DataException 
	 */
	public static InstructionProcessApplied getActiveProcess
	 (int instructionId, FWFacade facade) throws DataException {
		
		DataResultSet rs = getActiveProcessData(instructionId, facade);
		return get(rs);
	}
	
	public static InstructionProcessApplied get(DataResultSet rs) 
	 throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new InstructionProcessApplied(
			DataResultSetUtils.getResultSetIntegerValue(rs, "INSTR_PROCESS_APPLIED_ID"),
			
			DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_ID"),
			
			InstructionProcess.getCache().getCachedInstance(
			 DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_PROCESS_ID")
			),
			
			DataResultSetUtils.getResultSetStringValue(rs, "DESCRIPTION"),
			
			rs.getDateValueByName("PROCESS_START_DATE"),
			rs.getDateValueByName("PROCESS_END_DATE"),
			
			DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_ACTION_ID"),
			
			DataResultSetUtils.getResultSetStringValue(rs, "PROCESS_USER")
		);
	}

	public static DataResultSet getActiveProcessData
	 (int instructionId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "INSTRUCTION_ID", instructionId);
		
		return facade.createResultSet("qCommProc_GetActiveInstructionProcess", binder);
	}
	
	public static InstructionProcessApplied get
	 (int instrProcessAppliedId, FWFacade facade) throws DataException {
		return get(getData(instrProcessAppliedId, facade));
	}
	
	public static DataResultSet getData(int instrProcessAppliedId, FWFacade facade)
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder
		 (binder, "INSTR_PROCESS_APPLIED_ID", instrProcessAppliedId);
		
		return facade.createResultSet("qCommProc_GetInstructionProcessApplied", binder);
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {

		BinderUtils.addIntParamToBinder
		 (binder, "INSTR_PROCESS_APPLIED_ID", this.getInstructionProcessAppliedId());
		
		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_ID", this.getInstructionId());
		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_PROCESS_ID", this.getProcess().getInstructionProcessId());
		
		BinderUtils.addParamToBinder(binder, "DESCRIPTION", this.getDescription());
		
		BinderUtils.addDateParamToBinder
		 (binder, "PROCESS_START_DATE", this.getStartDate());
		BinderUtils.addDateParamToBinder
		 (binder, "PROCESS_END_DATE", this.getEndDate());
		
		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_ACTION_ID", this.getInstructionActionId());
		
		BinderUtils.addParamToBinder(binder, "PROCESS_USER", this.getUserName());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		
		DataBinder binder = new DataBinder();	
		this.addFieldsToBinder(binder);
		
		facade.execute("qCommProc_UpdateInstructionProcessApplied", binder);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}
	
	public boolean isActive() {
		return (this.getEndDate() == null);
	}

	public int getInstructionProcessAppliedId() {
		return instructionProcessAppliedId;
	}

	public void setInstructionProcessAppliedId(int instructionProcessAppliedId) {
		this.instructionProcessAppliedId = instructionProcessAppliedId;
	}

	public int getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(int instructionId) {
		this.instructionId = instructionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setProcess(InstructionProcess process) {
		this.process = process;
	}

	public InstructionProcess getProcess() {
		return process;
	}

	public void setInstructionActionId(Integer instructionActionId) {
		this.instructionActionId = instructionActionId;
	}

	public Integer getInstructionActionId() {
		return instructionActionId;
	}
	
	/** Fetches all instances for the given Instruction Process ID and Routing Module
	 *  ID.
	 * @param instructionActionId2
	 * @param routingModuleId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<InstructionProcessApplied> getByProcessIdAndModuleId(
			Integer instructionProcessId, Integer routingModuleId,
			FWFacade facade) throws DataException {

		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, InstructionProcess.Cols.ID, instructionProcessId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, RoutingModule.Cols.ID, routingModuleId);
		
		DataResultSet rs = facade.createResultSet
		 ("qCommProc_GetInstructionProcessAppliedByProcessIdAndModuleId", binder);
		
		Vector<InstructionProcessApplied> instrProcsApplied = 
		 new Vector<InstructionProcessApplied>();
	
		if (rs.first()) {
			do {
				instrProcsApplied.add(get(rs));
			} while (rs.next());
		}
		
		return instrProcsApplied;
	}

}
