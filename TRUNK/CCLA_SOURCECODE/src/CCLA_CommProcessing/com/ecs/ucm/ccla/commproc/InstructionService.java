package com.ecs.ucm.ccla.commproc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.w3c.dom.Document;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.DataSource;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.comm.CommSource;
import com.ecs.ucm.ccla.data.comm.CommType;
import com.ecs.ucm.ccla.data.instruction.ApplicableInstructionData;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionAudit;
import com.ecs.ucm.ccla.data.instruction.InstructionAuditAction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.InstructionLock;
import com.ecs.ucm.ccla.data.instruction.InstructionProcessApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
//import com.ecs.ucm.ccla.utils.PriorityCalculationUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class InstructionService extends Service {
	
	/** Fetches various ResultSets relating to a single Instruction.
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getInstructionInfo() throws ServiceException, DataException {
		
		String instrIdStr = m_binder.getLocal(SharedCols.INSTRUCTION);
		
		if (StringUtils.stringIsBlank(instrIdStr)) {
			throw new ServiceException(SharedCols.INSTRUCTION + " missing");
		}
	
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		int instrId = Integer.parseInt(instrIdStr);
		
		DataResultSet rsInstruction = Instruction.getExtendedData(instrId, facade);
		m_binder.addResultSet("rsInstruction", rsInstruction);
		
		if (rsInstruction.isEmpty()) {
			throw new ServiceException("No Instruction found with ID: " + instrId);
		}
		
		Instruction instruction = Instruction.get(rsInstruction);
		
		m_binder.addResultSet("rsApplicableDataExt", 
		 ApplicableInstructionData.getDataByInstructionType
		 (instruction.getType(), true, facade));
		
		DataResultSet rsInstructionDataApplied = 
		 Instruction.getInstructionDataAppliedResultSet(instrId, facade);
		
		//Add DocName and DocType to the binder.
		String docGuid = instruction.getInstructionDocGuid();
		Log.info("docGuid: " + docGuid);

		if (docGuid!=null) {
			try {
				LWDocument lwDoc = CCLAUtils.getLatestLwdFromDocGuid(docGuid);
				if (lwDoc!=null) {
					String docname = lwDoc.getAttribute(Globals.UCMFieldNames.DocName);
					
					if (!StringUtils.stringIsBlank(docname))
						m_binder.putLocal("DOC_NAME", docname);
					
					String docIdStr = lwDoc.getId();
					
					if (!StringUtils.stringIsBlank(docIdStr))
						m_binder.putLocal("DOC_ID", docIdStr);
					
					String docType = lwDoc.getAttribute(Globals.UCMFieldNames.DocType);
					
					if (!StringUtils.stringIsBlank(docType))
						m_binder.putLocal("DOC_TYPE", docType);
				}
			} catch (Exception e) {
				Log.error(e.getMessage(), e);
			}
		}
		
		m_binder.addResultSet("rsInstructionDataApplied", rsInstructionDataApplied);
		
		DataResultSet rsInstructionLock = 
		 InstructionLock.getData(instrId, facade);
		m_binder.addResultSet("rsInstructionLock", rsInstructionLock);
		
		// Check for an active Process for this Instruction
		DataResultSet rsProcess = InstructionProcessApplied.getActiveProcessData
		 (instrId, facade);
		
		m_binder.addResultSet("rsInstructionProcess", rsProcess);
		
		InstructionProcessApplied instrProcessApplied = 
		 InstructionProcessApplied.get(rsProcess);
		
		if (instrProcessApplied != null) {
			DataResultSet rsProcessActions = 
			 instrProcessApplied.getProcess().getAvailableActionsData(facade);
			
			m_binder.addResultSet("rsProcessActions", rsProcessActions);
		}
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.INSTRUCTION, instrId);
		DataResultSet rsAudit =
		facade.createResultSet("qCommProc_GetInstructionAuditData", binder);
		
		m_binder.addResultSet("rsAudit", rsAudit);
		
		
		//Calculate Priority
		//load the data
//		instruction.loadDataApplied(facade);
//		int priority = PriorityCalculationUtils.getPriority(instruction);
//		CCLAUtils.addQueryIntParamToBinder(m_binder, "instrPriority", priority);
		
		
	}
	
	/** Called from Edit Instruction screen.
	 * 
	 *  Updates applied Instruction Data.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void updateInstruction() throws DataException, ServiceException {
		
		Integer instrId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.INSTRUCTION);
		
		if (instrId == null)
			throw new ServiceException(SharedCols.INSTRUCTION + " missing");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Instruction instr = Instruction.get(instrId, facade);
		
		if (instr == null)
			throw new ServiceException("Instruction with ID " + instrId + " not found");
		
		Vector<InstructionDataApplied> dataAppliedVec = 
			new Vector<InstructionDataApplied>();
		
		Vector<ApplicableInstructionData> applicableDataVec = 
		 instr.getType().getApplicableInstructionData();
		
		// Fetch the applied data from the Binder
		for (ApplicableInstructionData applicableData: applicableDataVec) {
			
			InstructionDataApplied dataApplied = 
			 InstructionDataApplied.getDataAppliedFromBinder
			 (instr, applicableData, m_binder);
				
			dataAppliedVec.add(dataApplied);
		}
		
		// Update the applied data.
		try {
			facade.beginTransaction();
			
			InstructionDataApplied.addOrUpdateAll
			 (dataAppliedVec, facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			Log.error("Failed to update applied instruction data", e);
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Called when a user manually updates the status of an Instruction.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void updateInstructionStatus() throws DataException, ServiceException {
		
		Integer instrId 	= CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.INSTRUCTION);

		if (instrId==null)
			throw new ServiceException("Instruction Id is null, cannot update");
		
		Integer instrStatusId 	= CCLAUtils.getBinderIntegerValue
		 (m_binder, InstructionStatus.Cols.ID);
		
		if (instrStatusId==null)
			throw new ServiceException("Instruction Status Id is null, cannot update");
		
		InstructionStatus status = InstructionStatus.getCache().
		 getCachedInstance(instrStatusId);
		
		boolean doAudit = CCLAUtils.getBinderBoolValue(m_binder, "DO_AUDIT");
		
		FWFacade facade			= CCLAUtils.getFacade(m_workspace, true);
		
		try {
			String username = m_userData.m_name;
			facade.beginTransaction();
			
			Instruction instruction = Instruction.get(instrId, facade);
			if (instruction==null)
				throw new ServiceException("Instruction Id is null, cannot update");
					
			instruction.setStatus(status);
			instruction.persist(facade, username);
			
			if (doAudit) {
				InstructionAudit instructionAudit = 
					new InstructionAudit(null, instruction.getInstructionId(), 
					 InstructionAuditAction.ACTION_STATUS_UPDATE, null, 
					 instruction.getStatus().getInstructionStatusId(), 
					 null, null, 0, username);
				
				InstructionAudit.add(instructionAudit, facade);
			}
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update instruction status: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	
	/**
	 * Removes the instruction lock
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void removeLock() throws DataException, ServiceException {
		Integer instrId 	= CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.INSTRUCTION);

		
		if (instrId==null)
			throw new ServiceException("Instruction Id is null, cannot update");
		
		FWFacade facade			= CCLAUtils.getFacade(m_workspace);
		InstructionLock lock = InstructionLock.get(instrId, facade);
		
		if (lock==null)
			return; //nothing to remove, just continue. 
		
		String userName = m_userData.m_name;
		
		InstructionLock.remove(instrId, facade, userName);		
	}
	
	/**
	 * Update the document and instruction with the reference number
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void updateTransactionReference() throws DataException, ServiceException {
		

		String pendingValStr = m_binder.getLocal("pendingValues");
		if (StringUtils.stringIsBlank(pendingValStr)) 
			throw new ServiceException("pendingValues is null");

		Log.debug("-- PendingTransStr = "+pendingValStr);
		
		String[] pendingValues = StringUtils.stringToArray(pendingValStr);
		InstructionEventGenerator ieg = InstructionEventGenerator.getInstance();
		FWFacade facade	= CCLAUtils.getFacade(m_workspace);
		
		for (int i=0; i<pendingValues.length; i++) {
			//expect the values in [transRef]|[DOC_ID]|[INSTRUCTION_ID]
			//transaction ref is usually the dDocname.
			String[] split = pendingValues[i].split("\\|");
			
			if (split.length!=3) {
				throw new ServiceException("unknown pendingValues");						
			}
			
			try {
				int docId = Integer.parseInt(split[1]);
				int instrId = Integer.parseInt(split[2]);				
				LWDocument lwDoc = new LWDocument(docId, true);
				lwDoc.setAttribute(Globals.UCMFieldNames.TransactionRef, split[0]);
				lwDoc.update();
				
				ieg.triggerTransactionRefEvent(instrId, Globals.Users.System, facade, split[0]);				
			} catch (Exception e) {
				Log.error("Cannot update transaction ref", e);
				throw new ServiceException("Cannot update transaction ref", e);
			}
		}
	}
	
	/**
	 * Gets the applicable instruction data
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getApplicableInstructionDataByType() throws DataException, ServiceException {
		Integer instrTypeId = CCLAUtils.getBinderIntegerValue(m_binder, "INSTRUCTION_TYPE_ID");

		if (instrTypeId==null) {
			Log.error("Cannot create Instruction, missing instruction type id");
			throw new DataException("Cannot create Instruction, missing instruction type id");
		}
		
		InstructionType instrType = InstructionType.getIdCache()
		 .getCachedInstance(instrTypeId);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
	
		m_binder.addResultSet("rsApplicableDataExt", 
		 ApplicableInstructionData.getDataByInstructionType(instrType, true, facade));
	}
	
	/**
	 * Create an instruction
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void createInstruction() throws DataException, ServiceException {

		Integer instrTypeId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "INSTRUCTION_TYPE_ID");
		Integer commTypeId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "COMM_TYPE_ID");
		Integer instrStatusId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Instruction.INSTRUCTION_STATUS_ID);
		
		if (instrTypeId==null) {
			Log.error("Cannot create Instruction, missing instruction type id");
			throw new DataException("Cannot create Instruction, missing instruction type id");
		}
		
		if (commTypeId==null) {
			Log.error("Cannot create Instruction, missing comm type id");
			throw new DataException("Cannot create Instruction, missing comm type id");
		}

		if (instrStatusId==null) {
			Log.debug("Instruction Status not specified, used default: INDEXED status");
			instrStatusId = InstructionStatus.StatusID.INDEXED;
		}
		
		InstructionType instrType = InstructionType.getIdCache().getCachedInstance(instrTypeId);
		
		if (instrType==null) {
			Log.error("Cannot find instructionType with id:"+instrTypeId);
			throw new DataException("Cannot find instructionType with id:"+instrTypeId);
		}
		
		CommType commType = CommType.getCache().getCachedInstance(commTypeId);
		if (commType==null) {
			Log.error("Cannot find commType with id:"+commTypeId);
			throw new DataException("Cannot find commType with id:"+commTypeId);
		}
		
		InstructionStatus instrStatus = InstructionStatus.getCache().getCachedInstance(instrStatusId);
		if (instrStatus==null) {
			Log.error("Cannot find instructionStatus with id:"+instrStatus);
			throw new DataException("Cannot find instructionStatus with id:"+instrStatus);
		}
		
		String userName = m_userData.m_name;
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			//1. Create CommGroup
			CommGroup commGroup = CommGroup.add(null, null, facade, userName);
			
			//2. Create Comm
			Comm comm = Comm.add(CommSource.USER, commType, 
					 null, null, new Date(), userName, 
					 null, null, commGroup.getCommGroupId(), 
					 facade, userName);
			
			//3. Create Instruction
			Instruction instr = 
				new Instruction(comm.getCommId(), instrType, 
						instrStatus, null, true, 
						userName, facade);
			
			Date processDate = new Date();
			instr.setProcessDate(processDate);
			instr.setOriginalProcessDate(processDate);
			
			//4. Create data
			Vector<InstructionDataApplied> dataAppliedVec = 
				new Vector<InstructionDataApplied>();
			
			Vector<ApplicableInstructionData> applicableDataVec = 
				instrType.getApplicableInstructionData();
			
			for (ApplicableInstructionData applicableData: applicableDataVec) {
				
				InstructionDataApplied dataApplied = 
				 InstructionDataApplied.getDataAppliedFromBinder
				 (instr, applicableData, m_binder);
					
				dataAppliedVec.add(dataApplied);
			}
			
			//add instruction and data
			Instruction.add(instr, userName, facade);
			InstructionDataApplied.addOrUpdateAll(dataAppliedVec, facade, userName);
			
			facade.commitTransaction();
			
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			
			if (!StringUtils.stringIsBlank(redirectUrl)) {
				redirectUrl += "&INSTRUCTION_ID="+instr.getInstructionId();
				
				m_binder.putLocal("RedirectUrl", redirectUrl);
			}
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			Log.error("Cannot create instruction: "+e.getMessage());
			throw new DataException("Cannot create instruction: "+e.getMessage(), e);
		}
	}
	
	/** Tests whether or not the pooled facade transaction block works correctly.
	 * 
	 * @throws DataException
	 */
	public void testTransactions() throws DataException {
		
		boolean useUCMDB = CCLAUtils.getBinderBoolValue(m_binder, "useUCMDB");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, !useUCMDB);
		
		if (useUCMDB)
			Log.debug("Built SystemDatabase facade");
		else
			Log.debug("Built OracleJDBCFacade");
		
		Log.debug("Testing single-line commit");

		Element testElem = Element.add(ElementType.PERSON, 
		 DataSource.getCache().getCachedInstance(2), facade, m_userData.m_name);
		
		Log.debug("Added new Element with ID: " + testElem.getElementId());
		
		Element fetchElem = Element.get(testElem.getElementId(), facade);
		
		Log.debug("Attempting to fetch Element from DB. Exists? " 
		 + (fetchElem != null));
		
		boolean doRollback = CCLAUtils.getBinderBoolValue(m_binder, "doRollback");
		
		Log.debug("Starting transaction block");
		facade.beginTransaction();
		Log.debug("Transaction block started");
		
		testElem = Element.add(ElementType.PERSON, 
		 DataSource.getCache().getCachedInstance(2), facade, m_userData.m_name);
		
		Log.debug("Added new Element with ID: " + testElem.getElementId());
		
		if (doRollback) {
			Log.debug("Rolling back transaction");
			facade.rollbackTransaction();
		} else {
			Log.debug("Committing transaction");
			facade.commitTransaction();
		}
		
		facade = CCLAUtils.getFacade(m_workspace, true);

		fetchElem = Element.get(testElem.getElementId(), facade);
		
		Log.debug("Attempting to fetch Element from DB. Exists? " 
		 + (fetchElem != null));
	}
}
