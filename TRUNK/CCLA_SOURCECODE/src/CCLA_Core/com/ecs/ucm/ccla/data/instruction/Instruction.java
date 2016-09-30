package com.ecs.ucm.ccla.data.instruction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.SharedCols;

import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the INSTRUCTION table.
 *  
 * @author Tom
 *
 */
public class Instruction implements Persistable {
	
	public static final String AUDIT_LABEL = "Instruction";
	
	//DB columns
	public static final String INSTRUCTION_STATUS_ID = "INSTRUCTION_STATUS_ID";
	public static final String INSTRUCTION_TYPE_ID = "INSTRUCTION_TYPE_ID";
	public static final String ORIGINAL_PROCESS_DATE = "ORIGINAL_PROCESS_DATE";
	public static final String PROCESS_DATE = "PROCESS_DATE";
	public static final String ACTUAL_PROCESS_DATE = "ACTUAL_PROCESS_DATE";
	public static final String SPP_JOB_ID = "SPP_JOB_ID";
	public static final String DEPENDENT_INSTRUCTION_ID = "DEPENDENT_INSTRUCTION_ID";
	//public static final String INSTRUCTION_DOC_ID = "INSTRUCTION_DOC_ID";
	public static final String INSTRUCTION_DOC_GUID = "INSTRUCTION_DOC_GUID";
	public static final String PRIORITY = "PRIORITY";
	
	// Values used for instruction priority.
	public static final int DEFAULT_PRIORITY = 0;
	public static final int HIGH_PRIORITY = 10;
	
	private int instructionId;
	private int commId;
	
	private InstructionType type;
	private InstructionStatus status;
	
	private int priority;
	
	private Date origProcessDate;
	private Date processDate;
	private Date actualProcessDate;
	
	private String sppJobId;
	private String instructionDocGuid;
	private Integer dependentInstructionId;
	
	private Date lastUpdated;
	private Date dateAdded;
	private String lastUpdatedBy;

	private Vector<InstructionDataApplied> dataApplied;
	
	public Instruction(int instructionId, int commId, InstructionType type,
	 InstructionStatus status, int priority, Date origProcessDate, Date processDate,
	 Date actualProcessDate, String sppJobId, String instructionDocGuid,
	 Integer dependentInstructionId, Date lastUpdated, Date dateAdded,
	 String lastUpdatedBy) {
		this.instructionId = instructionId;
		this.commId = commId;
		this.setType(type);
		this.setStatus(status);
		this.priority = priority;
		this.setOriginalProcessDate(origProcessDate);
		this.processDate = processDate;
		this.actualProcessDate = actualProcessDate;
		this.sppJobId = sppJobId;
		this.setInstructionDocGuid(instructionDocGuid);
		this.dependentInstructionId = dependentInstructionId;
		this.lastUpdated = lastUpdated;
		this.dateAdded = dateAdded;
		this.lastUpdatedBy = lastUpdatedBy;
		
		this.dataApplied = null;
	}
	
	/** Creates a new Instruction instance using field values from the passed 
	 *  DataBinder, but doesn't add it to the database.
	 *  
	 * @param binder
	 * @throws DataException
	 */
	public Instruction(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	/** Creates a new Instruction instance using the passed field values, and assigns
	 *  it a new INSTRUCTION_ID, but doesn't add it to the database.
	 *  
	 *  Should be used when creating a set of linked instructions which need to 
	 *  reference each other.
	 * 
	 * @param assignId whether or not a new Instruction ID is fetched. This should be
	 * 					set to false when creating 'test' instructions.
	 * @param binder
	 * @throws DataException
	 */
	public Instruction(int commId, InstructionType type, InstructionStatus status,
	 String instructionDocGuid, boolean assignId, String userName, FWFacade facade)
	 throws DataException {
	
		this(0, commId, type, 
		 (status != null) ? status : InstructionStatus.DEFAULT_INSTRUCTION_STATUS, 
		 0, null, null, null, null,
		 instructionDocGuid, null, null, null, userName);
		
		if (assignId) {
			int instructionId = Integer.parseInt(
			 CCLAUtils.getNewKey("Instruction", facade));
			
			this.setInstructionId(instructionId);
		}
	}
	
	public static Instruction add(int commId, InstructionStatus status, InstructionType type, 
	String instructionDocGuid, Integer dependantInstructionId, Date processDate, 
	String userName, FWFacade facade) 
	 throws DataException {
		
		Instruction instr = new Instruction(0, commId, type, 
		 status != null ? status : InstructionStatus.DEFAULT_INSTRUCTION_STATUS, 
		 0, processDate, processDate, null, null,
		 instructionDocGuid, dependantInstructionId, null, null, userName);
		
		return add(instr, userName, facade);
	}
	
	/** Adds a new Instruction via fields in the passed DataBinder.
	 * 
	 * @param binder
	 * @param facade
	 * @param username
	 * @return
	 * @throws NumberFormatException
	 * @throws DataException
	 */
	public static Instruction add(DataBinder binder, FWFacade facade, String username) 
	 throws NumberFormatException, DataException {

		Instruction instruction = new Instruction(binder);
		add(instruction, username, facade);
		
		return instruction;
	}
	
	/** Adds the given Instruction instance to the database.
	 *  
	 *  Creates a new PK value and assigns it before executing the query.
	 *  
	 *  All add-instruction methods must ultimately call this one.
	 *  
	 * @param instr
	 * @param userName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Instruction add(Instruction instr, String userName, FWFacade facade) 
	 throws DataException {
		
		int instructionId = 0;
		
		// Get new ID value if required
		if (instr.getInstructionId() == 0) {
			instructionId = Integer.parseInt(
			 CCLAUtils.getNewKey("Instruction", facade));
			instr.setInstructionId(instructionId);
		}
		instr.setLastUpdatedBy(userName);
		instr.validate(facade);
		
		DataBinder binder = new DataBinder();
		instr.addFieldsToBinder(binder);
		facade.execute("qCore_AddInstruction", binder);
		
		//Add Audit Data
		DataResultSet newData = Instruction.getData(
				instr.getInstructionId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(
				instr.getInstructionId(), ElementType.SecondaryElementType.Instruction.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.Instruction.toString(), 
		 null, newData, auditRelations);
		
		// Add a special 'Created' Instruction Audit
		InstructionAudit instrAudit = new InstructionAudit
		 (null,instr.getInstructionId(),
		 InstructionAuditAction.ACTION_CREATED, null, 
		 instr.getStatus().getInstructionStatusId(), 
		 null, null, 0, userName);
		
		InstructionAudit.add(instrAudit, facade);
		
		return Instruction.get(newData);
	}
	
	public static Instruction get(int instructionId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(instructionId, facade);
		return get(rs);
	}
	
	public static Instruction getByDocGuid(String instructionDocGuid, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getDataByInstructionDocGuid(instructionDocGuid, facade);
		return get(rs);
	}
	
	public static Vector<Instruction> getByCommId(int commId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getDataByCommId(commId, facade);
		return getVector(rs);
	}
	
	
	
	
	public static Vector<Instruction> getVector(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		Vector<Instruction> instrVec = new Vector<Instruction>();
		
		do {
			Instruction instr = get(rs);
			instrVec.add(instr);
		} while (rs.next());
		
		return instrVec;
	}
	
	
	public static Instruction get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;

		return new Instruction(
			DataResultSetUtils.getResultSetIntegerValue(rs, SharedCols.INSTRUCTION),
			DataResultSetUtils.getResultSetIntegerValue(rs, SharedCols.COMM),
			
			InstructionType.getIdCache().getCachedInstance(
			 DataResultSetUtils.getResultSetIntegerValue(rs, INSTRUCTION_TYPE_ID)
			),
			InstructionStatus.getCache().getCachedInstance(
			 DataResultSetUtils.getResultSetIntegerValue(rs, INSTRUCTION_STATUS_ID)
			),
			
			DataResultSetUtils.getResultSetIntegerValue(rs, PRIORITY),
			
			rs.getDateValueByName(ORIGINAL_PROCESS_DATE),
			rs.getDateValueByName(PROCESS_DATE),
			rs.getDateValueByName(ACTUAL_PROCESS_DATE),
			
			DataResultSetUtils.getResultSetStringValue(rs, SPP_JOB_ID),
			DataResultSetUtils.getResultSetStringValue(rs, INSTRUCTION_DOC_GUID),
			DataResultSetUtils.getResultSetIntegerValue(rs, DEPENDENT_INSTRUCTION_ID),
			
			rs.getDateValueByName(SharedCols.LAST_UPDATED),
			rs.getDateValueByName(SharedCols.DATE_ADDED),
			DataResultSetUtils.getResultSetStringValue(rs, SharedCols.LAST_UPDATED_BY)
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		BinderUtils.addIntParamToBinder
		 (binder, SharedCols.INSTRUCTION, this.getInstructionId());
		
		BinderUtils.addIntParamToBinder
		 (binder, INSTRUCTION_TYPE_ID, this.getType().getInstructionTypeId());
		BinderUtils.addIntParamToBinder
		 (binder, INSTRUCTION_STATUS_ID, this.getStatus().getInstructionStatusId());

		BinderUtils.addDateParamToBinder
		 (binder, ORIGINAL_PROCESS_DATE, this.getOriginalProcessDate());
		BinderUtils.addDateParamToBinder
		 (binder, PROCESS_DATE, this.getProcessDate());
		BinderUtils.addDateParamToBinder
		 (binder, ACTUAL_PROCESS_DATE, this.getActualProcessDate());		
		
		BinderUtils.addParamToBinder
		 (binder, SPP_JOB_ID, this.getSppJobId());
		
		BinderUtils.addIntParamToBinder
		 (binder, DEPENDENT_INSTRUCTION_ID, this.getDependentInstructionId());
		BinderUtils.addIntParamToBinder
		 (binder, SharedCols.COMM, this.getCommId());

		BinderUtils.addParamToBinder
		 (binder, INSTRUCTION_DOC_GUID, this.getInstructionDocGuid());
		
		BinderUtils.addIntParamToBinder
		 (binder, PRIORITY, this.getPriority());
		
		BinderUtils.addDateParamToBinder
		 (binder, SharedCols.LAST_UPDATED, this.getLastUpdated());
		BinderUtils.addDateParamToBinder
		 (binder, SharedCols.DATE_ADDED, this.getDateAdded());
		BinderUtils.addParamToBinder
		 (binder, SharedCols.LAST_UPDATED_BY, this.getLastUpdatedBy());
	}

	
	/** Fetches ResultSet for a single Instruction.
	 * 
	 * @param instructionId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int instructionId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, SharedCols.INSTRUCTION, instructionId);
		DataResultSet rsInstruction = facade.createResultSet
		 ("qCore_GetInstruction", binder);
		
		return rsInstruction;
	}
	
	/** Fetches ResultSet for a single Instruction, using the extended view. This will
	 *  expand the Instruction Type Name, Status Name etc.
	 * 
	 * @param instructionId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getExtendedData(int instructionId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, SharedCols.INSTRUCTION, instructionId);
		DataResultSet rsInstruction = facade.createResultSet
		 ("qCore_GetInstructionExtended", binder);
		
		return rsInstruction;
	}
	
	/** Fetches a list of Instructions belonging to a given Organisation ID, ordered
	 *  by descending Instruction ID.
	 *  
	 *  The numRows argument puts a limit on the total no. of returned rows.
	 *  
	 * @param orgId
	 * @param numRows
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getExtendedDataByOrg
	 (int orgId, int numRows, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, Entity.Cols.ID, orgId);
		BinderUtils.addIntParamToBinder(binder, "numRows", numRows);
		
		DataResultSet rsInstruction = facade.createResultSet
		 ("qCore_GetInstructionsExtendedByOrgId", binder);
		
		return rsInstruction;
	}
	
	/** Fetches a list of Instructions belonging to a given Account ID, ordered
	 *  by descending Instruction ID.
	 *  
	 *  The numRows argument puts a limit on the total no. of returned rows.
	 *  
	 * @param orgId
	 * @param numRows
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getExtendedDataByAccount
	 (int orgId, int numRows, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, Account.Cols.ID, orgId);
		BinderUtils.addIntParamToBinder(binder, "numRows", numRows);
		
		DataResultSet rsInstruction = facade.createResultSet
		 ("qCore_GetInstructionsExtendedByAccountId", binder);
		
		return rsInstruction;
	}
	
	/**
	 * Get DataResultSet of all instructions with a specific commId
	 * @param commId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getDataByCommId(int commId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, SharedCols.COMM, commId);
		DataResultSet rsInstruction = facade.createResultSet
		 ("qCore_GetInstructionByCommId", binder);
		
		return rsInstruction;
	}
	
	/** Fetches an instruction by looking for a matching INSTRUCTION_DOC_GUID field.
	 *  
	 *  All Instructions that were sourced from UCM documents will have this value
	 *  set.
	 *  
	 * @param docGuid
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getDataByInstructionDocGuid(String instructionDocGuid, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addParamToBinder(binder, INSTRUCTION_DOC_GUID, instructionDocGuid);
		DataResultSet rsInstruction = facade.createResultSet
		 ("qCore_GetInstructionByDocGuid", binder);
		
		return rsInstruction;
	}

	/** Returns a set of mapped Instruction Data from the database.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public Vector<InstructionDataApplied> getInstructionDataApplied(FWFacade facade) 
	 throws DataException {
		return getInstructionDataApplied(this.getInstructionId(), facade);
	}
	
	/**
	 * Returns a set of INSTRUCTION DATA APPLIED objects
	 * for a particular instruction - if a particular data object has no value
	 * then null is returned for that value but it will still be present in the results
	 * 
	 * @return Vector<InstructionDataApplied> or null if no objects found
     * @throws DataException
	 */	
	public static Vector<InstructionDataApplied> getInstructionDataApplied
	 (int instructionId, FWFacade facade) throws DataException {
		Vector<InstructionDataApplied>  dataApplied = 
		 new Vector<InstructionDataApplied>();
		
		DataResultSet rs = getInstructionDataAppliedResultSet(instructionId, facade);
		
		if (rs.first()) {
			do {
				InstructionDataApplied ida = InstructionDataApplied.get(rs);
				dataApplied.add(ida);
			} while (rs.next());
		}
		
		return dataApplied;
	}
	
	public static DataResultSet getInstructionDataAppliedResultSet
	 (int instructionId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, SharedCols.INSTRUCTION, instructionId);
		return facade.createResultSet("qCore_GetInstructionValues", binder);
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		this.setLastUpdatedBy(username);
		this.validate(facade);
		
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = Instruction.getData(this.getInstructionId(), facade);
		
		facade.execute("qCore_UpdateInstruction", binder);	
		
		// Update: audit
		DataResultSet newData = Instruction.getData(this.getInstructionId(), facade);
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(
				instructionId, ElementType.SecondaryElementType.Instruction.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.Instruction.toString(), 
		 beforeData, newData, auditRelations);
		
	}

	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setCommId(BinderUtils.getBinderIntegerValue(binder, SharedCols.COMM));
		
		Integer instrTypeId = 
		 BinderUtils.getBinderIntegerValue(binder, INSTRUCTION_TYPE_ID);
		this.setType(InstructionType.getIdCache().getCachedInstance(instrTypeId));
		
		Integer statusId = BinderUtils.getBinderIntegerValue
		 (binder, INSTRUCTION_STATUS_ID);
		this.setStatus(InstructionStatus.getCache().getCachedInstance(statusId));
		
		this.setOriginalProcessDate
		 (BinderUtils.getBinderDateValue(binder, ORIGINAL_PROCESS_DATE));
		
		this.setProcessDate
		 (BinderUtils.getBinderDateValue(binder, PROCESS_DATE));
		this.setActualProcessDate
		(BinderUtils.getBinderDateValue(binder, ACTUAL_PROCESS_DATE));
		
		this.setSppJobId(BinderUtils.getBinderStringValue(binder, SPP_JOB_ID));

		this.setDependentInstructionId(
		 BinderUtils.getBinderIntegerValue(binder, DEPENDENT_INSTRUCTION_ID));

		
		this.setLastUpdated(BinderUtils.getBinderDateValue(binder, SharedCols.LAST_UPDATED));
		this.setDateAdded(BinderUtils.getBinderDateValue(binder, SharedCols.DATE_ADDED));
		this.setLastUpdatedBy(BinderUtils.getBinderStringValue
		 (binder, SharedCols.LAST_UPDATED_BY));
	}

	public void validate(FWFacade facade) throws DataException {

		if (this.getStatus() == null)
			throw new DataException("Instruction Status missing");
		
		if (this.getType() == null)
			throw new DataException("Instruction Type missing");
		
		/*
		if (this.getProcessDate() == null)
			throw new DataException("Process Date missing");
			*/
	}

	/** Sets the instruction status for the given set of Instructions.
	 * 
	 * @param vIns
	 * @param newStatusId
	 * @param username
	 * @param facade
	 * @throws DataException
	 */
	public static void updateBulkInstructionStatus(Vector<Instruction> vIns, 
	 int newStatusId, String username, FWFacade facade) throws DataException {
		for (Instruction instruction : vIns) {
				instruction.setStatusById(newStatusId);
				instruction.persist(facade, username);
		}
	}

	public boolean isBuy() {
		TransactionType tranType = this.getType().getTransactionType();
		return (tranType != null && tranType.equals(TransactionType.BUY));
	}
	
	public boolean isSell() {
		TransactionType tranType = this.getType().getTransactionType();
		return (tranType != null && tranType.equals(TransactionType.SELL));
	}
	
	public boolean isTransfer() {
		TransactionType tranType = this.getType().getTransactionType();
		return (tranType != null && tranType.equals(TransactionType.TRANSFER));
	}
	
	/** Returns a boolean indicating if transaction is a buy
	 *
	 * 
	 * @return boolean true if is a buy, false otherwise
	 * @throws DataException 
	 */	
	public static boolean isBuy(int transactionId, FWFacade facade) throws DataException
	{
		boolean isBuy = false;
		Instruction transaction = Instruction.get(transactionId, facade);
		InstructionType insType = transaction.getType();
		TransactionType transType = insType.getTransactionType();
		if (transType.getTransactionTypeId()==InstructionGlobals.TRANSACTION_TYPE_BUY_ID)
			isBuy = true;
		return isBuy;
	}

	/** Returns a boolean indicating if transaction is a sell
	 *
	 * 
	 * @return boolean true if is a sell, false otherwise
	 * @throws DataException 
	 */	
	public static boolean isSell(int transactionId, FWFacade facade) throws DataException
	{
		boolean isSell = false;
		Instruction transaction = Instruction.get(transactionId, facade);
		InstructionType insType = transaction.getType();
		TransactionType transType = insType.getTransactionType();
		if (transType.getTransactionTypeId()==InstructionGlobals.TRANSACTION_TYPE_SELL_ID)
			isSell = true;
		return isSell;
	}
	
	/** Returns a boolean indicating if transaction is a transfer
	 *
	 * 
	 * @return boolean true if is a transfer, false otherwise
	 * @throws DataException 
	 */	
	public static boolean isTransfer(int transactionId, FWFacade facade) throws DataException
	{
		boolean isTransfer = false;
		Instruction transaction = Instruction.get(transactionId, facade);
		InstructionType insType = transaction.getType();
		TransactionType transType = insType.getTransactionType();
		if (transType.getTransactionTypeId()==InstructionGlobals.TRANSACTION_TYPE_TRANSFER_ID)
			isTransfer = true;
		return isTransfer;
	}

	/** Returns a vector of instructions at a particular status for 
	 * a fund code and with process date less than or equal to passed process
	 * date
	 *
	 * 
	 * @return Vector<Transaction> or null if no objects found
	 * @throws DataException 
    * @throws DataException
	 */	
	public static Vector<Instruction> getTransactionsToProcess(Date processDate, int statusId, String fundCode, FWFacade facade)
	throws DataException
	{
		Vector<Instruction> transaction = new Vector<Instruction>();
		Vector<Integer> currentIdsVec = new Vector<Integer>();
		DataBinder binder = new DataBinder();
		BinderUtils.addDateParamToBinder(binder, PROCESS_DATE, processDate);
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_STATUS_ID, statusId);
		BinderUtils.addParamToBinder(binder, SharedCols.FUND, fundCode);
		DataResultSet rsTransactions = facade.createResultSet("qCore_GetTransactionsByStatusProcessDateFund", binder);
		
		if (!rsTransactions.isEmpty())
		{
			do {
				Instruction trans = get(rsTransactions);
				Log.debug("Adding SourceAcc Instruction for processing:"+trans.getInstructionId());
				transaction.add(trans);
				currentIdsVec.add(trans.getInstructionId());
			} while (rsTransactions.next());
		}
		// look for transactions where pc account is in destination field
		DataResultSet rsDestTransactions = facade.createResultSet("qCore_GetDestTransactionsByStatusProcessDateFund", binder);
		if (!rsDestTransactions.isEmpty())
		{
			do {
				Instruction trans = get(rsDestTransactions);
				if (!currentIdsVec.contains(trans.getInstructionId())) {
					Log.debug("Adding DestAcc Instruction for processing:"+trans.getInstructionId());					
					transaction.add(trans);
					currentIdsVec.add(trans.getInstructionId());
				} else {
					Log.debug("Not Adding DestAcc Instruction for processing:"+trans.getInstructionId());

				}
			} while (rsDestTransactions.next());
		}
		if (transaction.size()>0)
			return transaction;
		else
			return null;
	}	

	/** Returns a vector of instructions at a particular status for 
	 * a fund code
	 *
	 * 
	 * @return Vector<Instruction> or null if no objects found
	 * @throws DataException 
    * @throws DataException
	 */	
	public static Vector<Instruction> getTransactionsByStatus(int statusId, String fundCode, FWFacade facade)
	throws DataException
	{
		Vector<Instruction> transaction = new Vector<Instruction>();
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_STATUS_ID, statusId);
		BinderUtils.addParamToBinder(binder, SharedCols.FUND, fundCode);
		DataResultSet rsTransactions = facade.createResultSet("qCore_GetTransactionsByStatusFund", binder);
		if (rsTransactions.isEmpty())
			return null;
		else
		{
			do {
				Instruction trans = get(rsTransactions);
				transaction.add(trans);
			} while (rsTransactions.next());
			return transaction;
		}
	}		
	
	public int getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(int instructionId) {
		this.instructionId = instructionId;
	}

	public int getCommId() {
		return commId;
	}

	public void setCommId(int commId) {
		this.commId = commId;
	}

	public InstructionType getType() {
		return type;
	}

	public void setType(InstructionType type) {
		this.type = type;
	}

	public InstructionStatus getStatus() {
		return status;
	}

	public void setStatus(InstructionStatus status) {
		this.status = status;
	}
	
	public void setStatusById(int instructionStatusId) throws DataException {
		InstructionStatus newStatus = InstructionStatus.getCache().
		 getCachedInstance(instructionStatusId);
		
		if (newStatus == null)
			throw new DataException("Invalid Instruction Status ID: " 
			+ instructionStatusId);
		
		this.setStatus(newStatus);
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
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

	public String getSppJobId() {
		return sppJobId;
	}

	public void setSppJobId(String sppJobId) {
		this.sppJobId = sppJobId;
	}

	public String getInstructionDocGuid() {
		return instructionDocGuid;
	}

	public void setInstructionDocGuid(String instructionDocGuid) {
		this.instructionDocGuid = instructionDocGuid;
	}

	public Integer getDependentInstructionId() {
		return dependentInstructionId;
	}

	public void setDependentInstructionId(Integer dependentInstructionId) {
		this.dependentInstructionId = dependentInstructionId;
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
	
	public void setOriginalProcessDate(Date origProcessDate) {
		this.origProcessDate = origProcessDate;
	}

	public Date getOriginalProcessDate() {
		return origProcessDate;
	}
	/** Returns a two-column ResultSet representing the passed Instruction list.
	 *  
	 *  The fields are INSTRUCTION_TYPE_NAME and COUNT.
	 *  
	 * @param instrs
	 * @return
	 */
	public static DataResultSet getSimpleResultSet(Vector<Instruction> instrs) {
		
		DataResultSet rs = new DataResultSet(
			new String[] { "INSTRUCTION_TYPE_NAME", "COUNT" }
		);

		HashMap<String, Integer> counts = new HashMap<String, Integer>();
		
		for (Instruction instr : instrs) {
			String thisName = instr.getType().getName();
			
			if (counts.containsKey(thisName)) {
				int thisCount = counts.get(thisName);
				thisCount++;
				
				counts.put(thisName, thisCount);
			} else {
				counts.put(thisName, 1);
			}
		}
		
		for (String instrName : counts.keySet()) {
			Vector<String> v = new Vector<String>();
			v.add(instrName);
			v.add(counts.get(instrName).toString());
			
			rs.addRow(v);
		}
		
		return rs;
	}
	
	@Override
	public String toString() {
		return "Instruction [instructionId=" + instructionId 
				+ ", commId=" + commId
				+ ", type=" + type.toString()
				+ ", instructionDocGuid=" + instructionDocGuid				
				+ ", status=" + status.toString() 
				+ ", process date=" + processDate + "]";
	}
	
	/** Stores the list of applied instruction data fields against the instance.
	 * 
	 * @param facade
	 * @throws DataException 
	 */
	public void loadDataApplied(FWFacade facade) throws DataException {
		this.dataApplied = getInstructionDataApplied(this.getInstructionId(), facade);
	}
	
	/**
	 * Temporary set the data to the instruction, 
	 * use for when the instruction is not being persist
	 * This will be overridden when you call loadDataApplied.
	 * @param dataApplied
	 */
	public void setDataApplied(Vector<InstructionDataApplied> dataApplied) {
		this.dataApplied = dataApplied;
	}

	
	public Vector<InstructionDataApplied> getDataApplied() {
		return dataApplied;
	}
	
	/** Returns the corresponding field value for the given Instruction Data ID.
	 *  
	 * @param instructionDataId
	 * @return
	 * @throws DataException if the applied data wasn't already loaded into the
	 * 							Instruction instance
	 */
	public InstructionDataFieldValue getDataApplied(int instructionDataId) 
	 throws DataException {
		
		if (dataApplied == null) {
			Log.error("Applied Instruction Data not loaded, ensure " +
					 "that the loadDataApplied() method is called first");
			throw new DataException("Applied Instruction Data not loaded, ensure " +
			 "that the loadDataApplied() method is called first");
		}
		return InstructionDataApplied.getFieldValue(dataApplied, instructionDataId);
	}

	public boolean equals(Instruction instr) {
		return (this.getInstructionId() == instr.getInstructionId());
	}

	public static Vector<Instruction> getAll(DataResultSet rs) throws DataException {
		
		Vector<Instruction> instrs = new Vector<Instruction>();
			
		if (rs.first()) {
			do {
				instrs.add(get(rs));
			} while (rs.next());
		}
		
		return instrs;
	}
	
	/** Fetches instructions with a matching applied numeric data value.
	 * 
	 * @param cash
	 * @param thisCashAmount
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Instruction> getInstructionsByAppliedNumValue
	 (int instructionDataId, BigDecimal value, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, InstructionData.Cols.ID, instructionDataId);
		
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, InstructionDataApplied.Cols.INSTRUCTION_NUM_VALUE, value);
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetInstructionsByAppliedNumValue", binder);
		
		return getAll(rs);
	}
	
	/** Fetches instructions with a matching applied string data value.
	 * 
	 * @param cash
	 * @param thisCashAmount
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Instruction> getInstructionsByAppliedStringValue
	 (int instructionDataId, String value, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, InstructionData.Cols.ID, instructionDataId);
		
		CCLAUtils.addQueryParamToBinder
		 (binder, InstructionDataApplied.Cols.INSTRUCTION_STRING_VALUE, value);
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetInstructionsByAppliedStringValue", binder);
		
		return getAll(rs);
	}
	
	/** Fetches instructions with a matching applied Date data value.
	 * 
	 * @param cash
	 * @param thisCashAmount
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Instruction> getInstructionsByAppliedDateValue
	 (int instructionDataId, Date value, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, InstructionData.Cols.ID, instructionDataId);
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, InstructionDataApplied.Cols.INSTRUCTION_DATE_VALUE, value);
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetInstructionsByAppliedDateValue", binder);
		
		return getAll(rs);
	}
	
	
	/** Fetches instructions with a matching applied Date data value that falls between
	 *  the two passed dates
	 * 
	 * @param cash
	 * @param thisCashAmount
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Instruction> getInstructionsByAppliedDateValueRange
	 (int instructionDataId, Date startDate, Date endDate, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, InstructionData.Cols.ID, instructionDataId);
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, InstructionDataApplied.Cols.INSTRUCTION_DATE_VALUE + "_START", 
		 startDate);
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, InstructionDataApplied.Cols.INSTRUCTION_DATE_VALUE + "_END", 
		 endDate);
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetInstructionsByAppliedDateValueRange", binder);
		
		return getAll(rs);
	}
}
