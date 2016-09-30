package com.ecs.stellent.spp.service;

import intradoc.data.DataException;

import java.util.Date;

import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CashForecastingUtils {

	//name of system config var that we are interested in.
	private static final String BANKING_CUTOFF_TIME_VAR_NAME = "BankingDate_CutOffTime"; 
	
	
	private static final String INSTR_DATA_NAME_BANKING_DATE 	= "BANKING_DATE";
	private static final String INSTR_DATA_NAME_SETTLEMENT_DATE = "SETTLEMENT_DATE";
	
	/**
	 * Gets the banking date for the instruction
	 * @param instr
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Date getBankingDate(Instruction instr, FWFacade facade) throws DataException
	{
		if (instr==null)
			throw new DataException("Instruction is null, cannot calculate banking date");
		
		//TODO to determine which instruction will require a banking/settlement date
		
		String bankingDateStr = InstructionDataApplied.getDataValueByName(instr.getInstructionId(), INSTR_DATA_NAME_BANKING_DATE, facade);
		Date bankingDate = DateUtils.parseddsMMsyyyyspHHcmm(bankingDateStr);
			
		if (bankingDate!=null) {
			Log.debug("Banking Date already defined for instructionId:"+
					instr.getInstructionId()+", bankingDate:"+bankingDate.toString());
			return bankingDate;
		}
		
		//Need to get the dindate (i.e comm dateAdded field) 		
		Comm comm = Comm.get(instr.getCommId(), facade);
		if (comm==null) {
			Log.error("No Comm found for instructionId:"+instr.getInstructionId()+", CommId:"+instr.getCommId());
			throw new DataException("Cannot find comm for instructionId:"+instr.getInstructionId());
		}
		
		return calculateBankingDate(comm.getDatedAdded());
	}
	

	/**
	 * Gets the settlement date for the instruction
	 * @param instr
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Date getSettlementDate(Instruction instr, FWFacade facade) throws DataException {
		
		if (instr==null) {
			Log.error("Instruction is null, cannot calculate settlement date");
			throw new DataException("Instruction is null, cannot calculate settlement date");
		}

		//TODO to determine which instruction will require a banking/settlement date
		
		if (instr.getProcessDate()==null) {		
			Log.error("processDate is null for instrId:"+instr.getInstructionId()+", cannot calculate settlement date");
			throw new DataException("processDate is null for instrId:"+instr.getInstructionId()+", cannot calculate settlement date");
		}

		//banking date String in the format [??????]
		String bankingDateStr = InstructionDataApplied.getDataValueByName(instr.getInstructionId(), INSTR_DATA_NAME_BANKING_DATE, facade);
		Date bankingDate = DateUtils.parseddsMMsyyyyspHHcmm(bankingDateStr);
		if (bankingDate==null) {
			Log.error("bankingDate is null for instrId:"+instr.getInstructionId()+", cannot calculate settlement date");
			throw new DataException("bankingDate is null for instrId:"+instr.getInstructionId()+", cannot calculate settlement date");
		}
		
		return calculateSettlementDate(instr.getType().getSettlementOffset(), bankingDate, instr.getProcessDate());
	}
	
	
	/**
	 * Calculates the Banking date based on the date that was past in.
	 * @param dIndate
	 * @return
	 * @throws DataException
	 */
	public static Date calculateBankingDate(Date dIndate) throws DataException {
		Date bankingCutoffTime = null; 
		Date bankingDate = null;
		
		SystemConfigVar systemConfigVar = 
			SystemConfigVar.getCache().getCachedInstance(BANKING_CUTOFF_TIME_VAR_NAME);
		
		if (systemConfigVar==null || systemConfigVar.getDateValue()==null) {
			Log.warn("No banking cut-off defined, using 3pm as default");	
			bankingCutoffTime = DateUtils.getDate(1970, 1, 1, 15, 00, 00);
		} else {
			bankingCutoffTime = systemConfigVar.getDateValue();
		}
		
		if ((bankingDate=dIndate)==null) {
			Log.error("No dIndate specified");
			throw new DataException("No dInDate specified");			
		}
		
		if (DateUtils.compareDateTimes(bankingDate, bankingCutoffTime)<0) {
			//Before cut-off time, hence assume comm.getDateAdded (dInDate) as banking date.
			bankingDate = DateUtils.getWorkingDay(bankingDate);
		} else {
			bankingDate = DateUtils.getWorkingDay(bankingDate, 1, false);
		}		
		return bankingDate;		
		
	}

	/**
	 * Calculates the settlement date for a known settlement offset, banking and process date
	 * @param offset
	 * @param bankingDate
	 * @param processDate
	 * @return
	 */
	public static Date calculateSettlementDate(int offset, Date bankingDate, Date processDate) 
	{
		Date calcSettlementDate = null;		
		calcSettlementDate = DateUtils.getWorkingDay(bankingDate, offset, false);
		
		//InstructionDataApplied.getDataValueByName(instructionId, TransactionGlobals.SOURCE_ACCOUNT, facade);
		if (DateUtils.compareDates(calcSettlementDate, processDate)<0) {
			//processDate is greater, hence settlementDate=processDate;
			return processDate;
		} else {
			//calcSettlementDate is greater, hence settlementDate=calcSettlementDate 
			return calcSettlementDate;
		}
	}
}
