package com.ecs.ucm.ccla.transactions.services;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/**This is a temporary class to enable testing of the eod service by manualy adding/editing
 * transactions.  Not to be used in future!
 * 
 * 
 * 
 * @author katie
 *
 */

public class TransactionService extends Service {
	
	
	public void getTransaction() throws DataException, ServiceException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			int instructionId = CCLAUtils.getBinderIntegerValue(m_binder, "INSTRUCTION_ID");
			Instruction instruction = Instruction.get(instructionId, facade);
			instruction.addFieldsToBinder(m_binder);
			String cash = InstructionDataApplied.getDataValueByName(instructionId, TransactionGlobals.CASH_VALUE, facade);
			String units =  InstructionDataApplied.getDataValueByName(instructionId, TransactionGlobals.UNIT_VALUE, facade);
			String sourceAcc = InstructionDataApplied.getDataValueByName(instructionId, TransactionGlobals.SOURCE_ACCOUNT, facade);
			String destAcc = InstructionDataApplied.getDataValueByName(instructionId, TransactionGlobals.DEST_ACCOUNT, facade);
			if (cash!=null)
				CCLAUtils.addQueryParamToBinder(m_binder, "CASH", cash);
			if (units!=null)
				CCLAUtils.addQueryParamToBinder(m_binder, "UNITS", units);
			if (sourceAcc!=null)
				CCLAUtils.addQueryParamToBinder(m_binder, "SOURCE_ACCOUNT_ID", sourceAcc);
			if (destAcc!=null)
				CCLAUtils.addQueryParamToBinder(m_binder, "DEST_ACCOUNT_ID", destAcc);
			
			facade.commitTransaction();
		} catch (DataException e) {
			String msg = "Unable to create instruction " + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}
	}

	public void addTransaction() throws DataException, ServiceException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String username = m_userData.m_name;
		
		try {
			facade.beginTransaction();
			Instruction instruction = Instruction.add(m_binder, facade, username);
			int instructionId = instruction.getInstructionId();
			
			String cash = CCLAUtils.getBinderStringValue(m_binder, TransactionGlobals.CASH_VALUE);
			String units = CCLAUtils.getBinderStringValue(m_binder, TransactionGlobals.UNIT_VALUE);
			String destAccount = CCLAUtils.getBinderStringValue(m_binder, TransactionGlobals.DEST_ACCOUNT);
			String sourceAccount = CCLAUtils.getBinderStringValue(m_binder, TransactionGlobals.SOURCE_ACCOUNT);
			
			if (cash != null && !Instruction.isTransfer(instructionId, facade))
				InstructionDataApplied.updateDataValueByName(instructionId, TransactionGlobals.CASH_VALUE, cash, username, facade);
			if (units != null && Instruction.isTransfer(instructionId, facade))
				InstructionDataApplied.updateDataValueByName(instructionId, TransactionGlobals.UNIT_VALUE, units, username, facade);
			if (sourceAccount != null)
				InstructionDataApplied.updateDataValueByName(instructionId, TransactionGlobals.SOURCE_ACCOUNT, sourceAccount, username, facade);
			if (destAccount != null && Instruction.isTransfer(instructionId, facade))
				InstructionDataApplied.updateDataValueByName(instructionId, TransactionGlobals.DEST_ACCOUNT, destAccount, username, facade);
			
			Log.debug("added instruction with id:" + instructionId);
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			redirectUrl = redirectUrl + instructionId;
			m_binder.putLocal("RedirectUrl", redirectUrl);			
			facade.commitTransaction();
		} catch (DataException e) {
			String msg = "Unable to create instruction " + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}
		
	}

	
	public void updateTransaction() throws DataException, ServiceException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String username = m_userData.m_name;
		
		try {
			facade.beginTransaction();
			int instructionId = CCLAUtils.getBinderIntegerValue(m_binder, "INSTRUCTION_ID");
			Instruction instruction = Instruction.get(instructionId, facade);
			instruction.setAttributes(m_binder);
			instruction.setLastUpdatedBy(username);
			instruction.persist(facade, username);
			
			String cash = CCLAUtils.getBinderStringValue(m_binder, TransactionGlobals.CASH_VALUE);
			String units = CCLAUtils.getBinderStringValue(m_binder, TransactionGlobals.UNIT_VALUE);
			String destAccount = CCLAUtils.getBinderStringValue(m_binder, TransactionGlobals.DEST_ACCOUNT);
			String sourceAccount = CCLAUtils.getBinderStringValue(m_binder, TransactionGlobals.SOURCE_ACCOUNT);
			
			if (cash != null)
				InstructionDataApplied.updateDataValueByName(instructionId, TransactionGlobals.CASH_VALUE, cash, username, facade);
			if (units != null)
				InstructionDataApplied.updateDataValueByName(instructionId, "UNITS", units, username, facade);
			if (sourceAccount != null)
				InstructionDataApplied.updateDataValueByName(instructionId, TransactionGlobals.SOURCE_ACCOUNT, sourceAccount, username, facade);
			if (destAccount != null)
				InstructionDataApplied.updateDataValueByName(instructionId,TransactionGlobals.DEST_ACCOUNT, destAccount, username, facade);
			
			Log.debug("added instruction with id:" + instructionId);
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			redirectUrl = redirectUrl + instructionId;
			m_binder.putLocal("RedirectUrl", redirectUrl);			
			facade.commitTransaction();
		} catch (DataException e) {
			String msg = "Unable to create instruction " + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}
	}

	public void getAllCompletedTransactionsForEOD() throws DataException, ServiceException 
	{	
		Integer eodId = CCLAUtils.getBinderIntegerValue(m_binder, "EOD_ID");
		if (eodId==null) 
			throw new ServiceException("EOD_ID is null, cannot display the transactions");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		if (facade==null)
			throw new ServiceException("Cannot create facade for EOD Transactions");	
			
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "EOD_ID", eodId);
		DataResultSet rsInstructions = facade.createResultSet("qTransactions_GetPSDFTransactionsForEODId", binder);
		
		m_binder.addResultSet("rsInstructions", rsInstructions);
	}

	public void getAllCurrentTransactionsForEOD() throws DataException, ServiceException 
	{	
		Integer statusId = CCLAUtils.getBinderIntegerValue(m_binder, "INSTRUCTION_STATUS_ID");
		if (statusId==null) 
			throw new ServiceException("INSTRUCTION_STATUS_ID is null, cannot display the transactions");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		if (facade==null)
			throw new ServiceException("Cannot create facade for Transactions");	
			
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "INSTRUCTION_STATUS_ID", statusId);
		DataResultSet rsInstructions = facade.createResultSet("qTransactions_GetPSDFTransactionsForStatusId", binder);
		
		m_binder.addResultSet("rsInstructions", rsInstructions);
	}
}
