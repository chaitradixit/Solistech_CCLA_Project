package com.ecs.ucm.ccla.commproc;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.FundTypeCode;
import com.ecs.ucm.ccla.data.TransactionBatch;
import com.ecs.ucm.ccla.data.TransactionBatchStatus;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.comm.CommSource;
import com.ecs.ucm.ccla.data.comm.CommType;
import com.ecs.ucm.ccla.data.instruction.ApplicableInstructionData;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;


public class TransactionBatchUtils {

	// Flag to enable extra logging 
	private static final boolean debug = 
		SharedObjects.getEnvValueAsBoolean("CCLA_CommProc_EnableDebugOnTransactionBatch", false);
	
	/**
	 * Gets the next increase by comparing the closing balance of the previous transaction batch 
	 * closing balance or the difference if it is a new transaction batch for the day.
	 * 
	 * @param transBatch
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static BigDecimal calculateDifferenceInClosingBalance(TransactionBatch transBatch, FWFacade facade) 
	throws DataException {
		if (transBatch.getSequence()>1) {
			//get the previous sequence and compare the closing balance 
			//to work out the difference.
			DataBinder binder = new DataBinder();
			transBatch.addFieldsToBinder(binder);
			CCLAUtils.addQueryIntParamToBinder(binder, TransactionBatch.Cols.SEQUENCE, transBatch.getSequence()-1);
			CCLAUtils.addQueryIntParamToBinder(binder, TransactionBatch.Cols.BANK_ACCOUNT_ID, transBatch.getBankAccountId());
//			CCLAUtils.addQueryParamToBinder(binder, TransactionBatch.Cols.BANK_ACCOUNT_NO, transBatch.getBankAccountNumber());
//			CCLAUtils.addQueryParamToBinder(binder, TransactionBatch.Cols.SORT_CODE, transBatch.getSortCode());			
//			CCLAUtils.addQueryParamToBinder(binder, TransactionBatch.Cols.FUND_CODE, transBatch.getFundCode());
			CCLAUtils.addQueryDateParamToBinder(binder, "START_TRANSACTION_DATE", DateUtils.roundToBeginningOfDay(transBatch.getTransactionDate()));
			CCLAUtils.addQueryDateParamToBinder(binder, "END_TRANSACTION_DATE", DateUtils.roundToEndOfDay(transBatch.getTransactionDate()));
			
			DataResultSet rs = facade.createResultSet(TransactionBatch.GET_TRANSACTION_BATCH_BY_PARAMS_QUERY, binder);
			if (rs!=null && !rs.isEmpty()) {
				BigDecimal previousClosingBal = CCLAUtils.getResultSetBigDecimalValue(rs, TransactionBatch.Cols.CLOSING_BALANCE);
				if (previousClosingBal==null) {
					int transBatchId = CCLAUtils.getResultSetIntegerValue(rs, TransactionBatch.Cols.TRANS_BATCH_ID);
					throw new DataException("Previous closing balance is null "+transBatchId);
				} else {
					if (debug)
						Log.debug("ClosingBalance:"+transBatch.getClosingBalance()+", PreviousClosingBalance:"+previousClosingBal);
					return transBatch.getClosingBalance().subtract(previousClosingBal);
				}
			} else {
				throw new DataException("Cannot find previous Transaction Batch with " +
				 "BnkAccId " + transBatch.getBankAccountId()+ " and sequence no. " + 
				 (transBatch.getSequence()-1));
			}
		} else {
			//First sequence
			if (debug) {
				Log.debug("ClosingBalance:"+transBatch.getClosingBalance()+", OpeningBalance:"+transBatch.getOpeningBalance());

				BigDecimal bdOpening = transBatch.getOpeningBalance();
				BigDecimal bdClosing = transBatch.getClosingBalance();
				
				Log.debug("return BigDecimal :"+bdClosing.subtract(bdOpening));
			}
			return transBatch.getClosingBalance().subtract(transBatch.getOpeningBalance());
		}
	}
	
	/**
	 * Gets all pending transaction batches
	 * 
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<TransactionBatch> getPendingTransactionBatch(FWFacade facade) 
	throws DataException
	{
		return TransactionBatch.getByStatusId(
					TransactionBatchStatus.Statuses.PENDING_STATUS, 
					facade);
	}
	
	/**
	 * Checks if a dicondin will 
	 * @param dicondinId
	 * @return
	 */
	public static boolean isDicondinMatchedToTransactions(int dicondinId, FWFacade facade)
	throws DataException 
	{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "DICONDIN_REF", dicondinId);	
		DataResultSet rs = facade.createResultSet("qCore_GetMatchedTransactionsForDicondin", binder);
		
		return (rs!=null && !rs.isEmpty());
	}
	
	/**
	 * 
	 * @param instrVec
	 * @param expectedAmount
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static List<Integer> getSuggestedMatchForTransactionBatch(Vector<Instruction> instrVec, BigDecimal expectedAmount, FWFacade facade) 
	throws DataException {
		
		
		List<Instruction> remainingInstrList = new ArrayList<Instruction>();
		List<Integer> matchInstrIdList = new ArrayList<Integer>();
		
		BigDecimal diCondinsAmount = new BigDecimal("0");
		InstructionDataApplied dataApplied = null;
		for (Instruction instr: instrVec) {
			if (instr.getType().getInstructionTypeId()==InstructionType.Ids.DICONDIN) {
				
				dataApplied = InstructionDataApplied.getDataApplied(instr.getInstructionId(), InstructionData.Fields.CASH, facade);
				
				if (dataApplied == null || dataApplied.getDataFieldValue().isEmpty()) { 

					throw new DataException("DICondins cash value is empty for instrId:"+instr.getInstructionId());
				} else {
					diCondinsAmount = diCondinsAmount.add(new BigDecimal(dataApplied.getDataFieldValue().getRawValue()));
					matchInstrIdList.add(instr.getInstructionId());
				}					
			} else {
				remainingInstrList.add(instr);
			}
		}
		
		String msg = "";
		int compareInt = diCondinsAmount.compareTo(expectedAmount);
		
		if (compareInt>0) {
			//Cannot match
			msg = "diCondins amount is greater than expected amount "+diCondinsAmount+" : "+expectedAmount;
			throw new DataException(msg);
		} else if (compareInt==0) {
			//do nothing and just return the list
		} else if (compareInt<0){
			//amounts are less, build up a list of transactions.
			BigDecimal remainingAmount = expectedAmount.subtract(diCondinsAmount);
			Map<BigDecimal,List<List<Instruction>>> map = calculateCombinations(remainingInstrList, facade);
			
			if (map.containsKey(remainingAmount)) {
				
				//just get the first one.
				List<Instruction> instrList = (map.get(remainingAmount)).get(0);
				for (Instruction instr: instrList) {
					matchInstrIdList.add(instr.getInstructionId());
				}
			} else {
				msg = "Cannot match instructions to expected value";
				throw new DataException(msg);
			}
		}
		return matchInstrIdList;
	}	
		
	
	
	/**
	 * Checks whether the transaction batch instructions are valid, i.e. the 
	 * @param instrVec
	 * @param expectedAmount
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static boolean checkTransactionBatchInstructions(Vector<Instruction> instrVec, BigDecimal expectedAmount, FWFacade facade) 
	throws DataException {
		
		Log.debug("checkTransactionBatchInstructions...expectedAmount:"+expectedAmount+", no. of instruction:"+instrVec.size());
		boolean isValid = false;
		
		List<Instruction> remainingInstrList = new ArrayList<Instruction>();
		BigDecimal diCondinsAmount = new BigDecimal("0");
		
		InstructionDataFieldValue dataValue = null;
		
		for (Instruction instr: instrVec) {
			if (instr.getType().getInstructionTypeId()==InstructionType.Ids.DICONDIN) {
				dataValue = instr.getDataApplied(InstructionData.Fields.CASH);
				if (dataValue==null || dataValue.isEmpty()) {
					throw new DataException("DICondins cash value is empty for instrId:"+instr.getInstructionId());
				} else {
					if (debug)
						Log.debug("-- DiCondins Amount:"+dataValue.getRawValue());
					
					diCondinsAmount = diCondinsAmount.add(new BigDecimal(dataValue.getRawValue()));
					
					if (debug)
						Log.debug("-- appended DiCondins Amount:"+diCondinsAmount);
					
				}					
			} else {
				remainingInstrList.add(instr);
			}
		}
		
		Log.debug("calculated total diCondin Amount:"+diCondinsAmount);
		
		String msg = "";
		
		int compareAmountInt = diCondinsAmount.compareTo(expectedAmount);
		
		if (compareAmountInt>0) {
			msg = "diCondins amount is greater than expected amount "+diCondinsAmount+" : "+expectedAmount;
			throw new DataException(msg);
		} else if (compareAmountInt==0) {
			isValid = true;
		} else if (compareAmountInt<0){
			BigDecimal remainingAmount = expectedAmount.subtract(diCondinsAmount);
			
			if (debug)
				Log.debug("remainingAmount: "+remainingAmount+
						" = ("+expectedAmount+"-"+diCondinsAmount+")");
			
			Map<BigDecimal,List<List<Instruction>>> map = calculateCombinations(remainingInstrList, facade);
			
			
			isValid = map.containsKey(remainingAmount);
		
			
			if (debug) {
				for (BigDecimal mapAmount: map.keySet()) {
					Log.debug("map amount: "+mapAmount);
				}
			}
		}
		return isValid;
	}	
	
	/**
	 * Checks the previous day closing balance
	 * @param transBatch
	 * @return
	 * @throws DataException
	 */
	public static boolean validatePrevDayClosingBalanceMatch(TransactionBatch transBatch, FWFacade facade) throws DataException {
		
		if (transBatch==null) 
			return false;
		
		if (debug) {
			Log.debug("Using TransBatch "+transBatch.getTransactionBatchId()+", OpeningBalance:"+transBatch.getOpeningBalance().toString());
		}
		
		BigDecimal openingBalance = transBatch.getOpeningBalance();
		Date transDate = DateUtils.roundToBeginningOfDay(transBatch.getTransactionDate());
		
		DataBinder binder = new DataBinder();
//		CCLAUtils.addQueryIntParamToBinder(binder, TransactionBatch.Cols.BANK_ACCOUNT_NO, transBatch.getBankAccountNumber());
//		CCLAUtils.addQueryParamToBinder(binder, TransactionBatch.Cols.SORT_CODE, transBatch.getSortCode());			
		CCLAUtils.addQueryIntParamToBinder(binder, TransactionBatch.Cols.BANK_ACCOUNT_ID, transBatch.getBankAccountId());
		CCLAUtils.addQueryDateParamToBinder(binder, "TRANSACTION_DATE", transDate);
		
		TransactionBatch prevTransBatch = 
			TransactionBatch.get(facade.createResultSet("qCore_GetLastTransactionBatchByParams", binder));
		
		if (prevTransBatch==null) 
			return true;
		
		if (debug) {
			Log.debug("Using previousTransBatch "+prevTransBatch.getTransactionBatchId()+
					", ClosingBalance:"+prevTransBatch.getClosingBalance().toString());
		}

		return prevTransBatch.getClosingBalance().compareTo(openingBalance)==0;
	}
	
	/**
	 * 
	 * @param list
	 * @param facade
	 * @return
	 * @throws DataException
	 */
    public static Map<BigDecimal,List<List<Instruction>>> calculateCombinations(List<Instruction> list, FWFacade facade) 
    throws DataException { 
    	Map<BigDecimal,List<List<Instruction>>> map = new HashMap<BigDecimal,List<List<Instruction>>>();
    	generateCombinations(null, new BigDecimal("0"), list, map, facade);
		return map;
	}
	
    /**
     * 
     * @param currentInstrList
     * @param amount
     * @param remaininglist
     * @param map
     * @param facade
     * @throws DataException
     */
    private static void generateCombinations(List<Instruction> currentInstrList, BigDecimal amount, List<Instruction> remaininglist, Map<BigDecimal,List<List<Instruction>>> map, FWFacade facade) 
	throws DataException
    {
        if (currentInstrList!=null && !currentInstrList.isEmpty()) {
        	
        	List<List<Instruction>> currentValueList = null;
        	
        	if (map.containsKey(amount)) {
        		currentValueList = map.get(amount);
        	} else {
        		currentValueList = new ArrayList<List<Instruction>>();
        	}
    		currentValueList.add(currentInstrList);
    		//Log.debug("adding to map key:"+amount+", currentValueList.size:"+currentValueList.size());
    		map.put(amount, currentValueList);
    	}
    	
        for (int i = 0; i < remaininglist.size(); i++) {
        	List<Instruction> tempInstrList = remaininglist.subList(i+1, remaininglist.size());
        	
        	Instruction instr = remaininglist.get(i);
    		BigDecimal additionalAmount = new BigDecimal("0");        	
        	if (instr.getDataApplied()!=null) {
        		InstructionDataFieldValue fieldValue = instr.getDataApplied(InstructionData.Fields.CASH);
        		if (!fieldValue.isEmpty())
        			additionalAmount = fieldValue.getBigDecimalValue();
        	} else {
        		InstructionDataApplied dataApplied = InstructionDataApplied.getDataApplied(
        			instr.getInstructionId(), 
        			InstructionData.Fields.CASH, facade);

            	if (dataApplied!=null && !dataApplied.getDataFieldValue().isEmpty()) {
            		additionalAmount=dataApplied.getDataFieldValue().getBigDecimalValue();
            	} 
        	}
        	
        	
        	List<Instruction> currentInstrList2 = new ArrayList<Instruction>();
        	if (currentInstrList!=null) {
        		currentInstrList2.addAll(currentInstrList);
        	}
        	currentInstrList2.add(instr);
        	
        	generateCombinations(currentInstrList2, amount.add(additionalAmount), tempInstrList, map, facade);
        }
    }		
	
	/**
	 * Generates buybnk or depbnk for the preadvices.
	 * @param preAdvices
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<Instruction> generateInstrForPreadvice(
			Vector<Instruction> preAdvices, String userName, boolean persist, 
			FWFacade facade) 
	throws DataException, ServiceException {
		
		Vector<Instruction> instrVec = new Vector<Instruction>();
		
		for (Instruction instr: preAdvices) {
			
			String fundCode = null;
			BigDecimal cash = null;
			Account account = null;
			Integer dicondinRef = null;
			Entity organisation = null;
			Date docDate = null;
			
			Map<Integer, String> map = new HashMap<Integer,String>();
			
			//Fund Code
			InstructionDataApplied dataApplied = 
				InstructionDataApplied.getDataApplied(
						instr.getInstructionId(), 
						InstructionData.Fields.FUND_CODE, 
						facade);
			
			if (dataApplied!=null && !dataApplied.getDataFieldValue().isEmpty()) {
				fundCode = dataApplied.getDataFieldValue().getStringValue();
				if (!StringUtils.stringIsBlank(fundCode))
						map.put(dataApplied.getInstructionData().getInstructionDataId(), fundCode);
			}

			//Dicondins Ref
			dataApplied = 
				InstructionDataApplied.getDataApplied(
						instr.getInstructionId(), 
						InstructionData.Fields.DICONDIN_REF, 
						facade);
			
			if (dataApplied!=null && !dataApplied.getDataFieldValue().isEmpty()) {
				dicondinRef = dataApplied.getDataFieldValue().getIntValue();
				
				Instruction dicondinInstr = Instruction.get(dicondinRef, facade);
				dicondinInstr.getProcessDate();
				map.put(dataApplied.getInstructionData().getInstructionDataId(), String.valueOf(dicondinRef));
				
				
				
			}			
			//Cash Amount
			dataApplied = 
				InstructionDataApplied.getDataApplied(
						instr.getInstructionId(), 
						InstructionData.Fields.CASH, 
						facade);
			
			if (dataApplied!=null && !dataApplied.getDataFieldValue().isEmpty()) {
				cash = dataApplied.getDataFieldValue().getBigDecimalValue();
				map.put(dataApplied.getInstructionData().getInstructionDataId(), String.valueOf(cash));
			}
			
			//account id 
			dataApplied = 
				InstructionDataApplied.getDataApplied(
						instr.getInstructionId(), 
						InstructionData.Fields.SOURCE_ACCOUNT_ID, 
						facade);
			
			if (dataApplied!=null && !dataApplied.getDataFieldValue().isEmpty()) {
				account = Account.get(dataApplied.getDataFieldValue().getIntValue(), facade);
				if (account==null)
					throw new DataException("Cannot find account with accountId:"+dataApplied.getDataFieldValue().getIntValue()); 
				map.put(dataApplied.getInstructionData().getInstructionDataId(), String.valueOf(account.getAccountId()));
			
			}
			
			//organisation id
			dataApplied = 
				InstructionDataApplied.getDataApplied(
						instr.getInstructionId(), 
						InstructionData.Fields.ORGANISATION_ID, 
						facade);
			
			if (dataApplied!=null && !dataApplied.getDataFieldValue().isEmpty()) {
				organisation = Entity.get(dataApplied.getDataFieldValue().getIntValue(), facade);
				if (organisation==null) 
					throw new DataException("Cannot find organisation with orgId:"+dataApplied.getDataFieldValue().getIntValue()); 
				map.put(dataApplied.getInstructionData().getInstructionDataId(), String.valueOf(organisation.getOrganisationId()));				
			}
			
			//document date
			dataApplied = 
				InstructionDataApplied.getDataApplied(
						instr.getInstructionId(), 
						InstructionData.Fields.DOC_DATE, 
						facade);
			
			if (dataApplied!=null && !dataApplied.getDataFieldValue().isEmpty()) {
				docDate = dataApplied.getDataFieldValue().getDateValue();
				map.put(dataApplied.getInstructionData().getInstructionDataId(), DateFormatter.getTimeStamp(docDate));				
			}
			
			//Create new depbank or buybank.
			
			Fund fund = Fund.get(account.getFundCode(), facade);
			InstructionType instrType = null;
			if (fund.getTypeCode().getFundTypeCodeId()==FundTypeCode.FUND_TYPECODE_ID_DEPOSIT) {
				instrType = InstructionType.getIdCache().getCachedInstance(InstructionType.Ids.DEPBNK);
			} else {
				instrType = InstructionType.getIdCache().getCachedInstance(InstructionType.Ids.BUYBNK);
			}
			
			InstructionStatus instrStatus = InstructionStatus.getCache().getCachedInstance(InstructionStatus.StatusID.READY_FOR_SPP_RELEASE);
			
			Instruction instruction = 
				new Instruction(instr.getCommId(), instrType, instrStatus, null, persist, userName, facade);
			
			Date processDate = DateUtils.getNow();
			instruction.setProcessDate(processDate);
			instruction.setOriginalProcessDate(processDate);
			
			Vector<InstructionDataApplied> instrDataAppliedVec = 
				TransactionBatchTranslator.generateInstructionDataFromMap(instruction, map, facade);
			
			instruction.setDataApplied(instrDataAppliedVec);
			instrVec.add(instruction);
		}
		return instrVec;
	}
	
	
	public static boolean calculatePreviousUnprocessedTransactionBatch(TransactionBatch transBatch, FWFacade facade) 
	throws DataException {
		if (transBatch.getSequence()>1) {
			//get the previous sequence and compare the closing balance 
			//to work out the difference.
			DataBinder binder = new DataBinder();
			transBatch.addFieldsToBinder(binder);
			CCLAUtils.addQueryIntParamToBinder(binder, TransactionBatch.Cols.SEQUENCE, transBatch.getSequence()-1);
//			CCLAUtils.addQueryParamToBinder(binder, TransactionBatch.Cols.FUND_CODE, transBatch.getFundCode());
//			CCLAUtils.addQueryParamToBinder(binder, TransactionBatch.Cols.BANK_ACCOUNT_NO, transBatch.getBankAccountNumber());
//			CCLAUtils.addQueryParamToBinder(binder, TransactionBatch.Cols.SORT_CODE, transBatch.getSortCode());		
			CCLAUtils.addQueryIntParamToBinder(binder, TransactionBatch.Cols.BANK_ACCOUNT_ID, transBatch.getBankAccountId());
			CCLAUtils.addQueryDateParamToBinder(binder, "START_TRANSACTION_DATE", DateUtils.roundToBeginningOfDay(transBatch.getTransactionDate()));
			CCLAUtils.addQueryDateParamToBinder(binder, "END_TRANSACTION_DATE", DateUtils.roundToEndOfDay(transBatch.getTransactionDate()));
			
			DataResultSet rs = facade.createResultSet(TransactionBatch.GET_TRANSACTION_BATCH_BY_PARAMS_QUERY, binder);
			if (rs!=null && !rs.isEmpty()) {
				Integer statusId = CCLAUtils.getResultSetIntegerValue(rs, TransactionBatch.Cols.TRANS_BATCH_STATUS_ID);
				
				if (statusId==null)
					throw new DataException("Unknown statusId for previous transaction batch!");
				else {
					if (statusId==TransactionBatchStatus.Statuses.PENDING_STATUS ||
							statusId==TransactionBatchStatus.Statuses.ERROR_STATUS) {
						return false;
					} else 
						return true;
				}
			} else {
				throw new DataException("Cannot find previous TransactionBatch for "+transBatch.getTransactionBatchId());
			}
		} else {
			return true;
		}
	}	
	
	
	public static Instruction generateIATInstruction(boolean persist, String userName, FWFacade facade) throws DataException {
		
		CommGroup commGroup = null;
		Comm comm = null;
		CommSource commSource = null;
		
		if (persist) {
			commGroup = CommGroup.add(null, null, facade, userName);
		} else {
			commGroup = new CommGroup(); // create temp CommGroup instead
		}
		
		//3. Create Comm
		commSource =CommSource.BANK;					
		
		if (persist) {
			comm = Comm.add(commSource, CommType.TRANSACTION_BATCH, 
				null, null, DateUtils.getNow(), userName, null, 
				null, commGroup.getCommGroupId(), facade, userName);
		} else {
			comm = new Comm(0, commSource, CommType.TRANSACTION_BATCH, 
					 Comm.DEFAULT_COMM_STATUS, null, 
					 null, null, userName, null, 
					 null, commGroup.getCommGroupId());
		}
		
		InstructionType instrType = InstructionType.getIdCache().getCachedInstance(InstructionType.Ids.IAT);
		InstructionStatus instrStatus = InstructionStatus.getCache().getCachedInstance(InstructionStatus.StatusID.COMPLETED_IAT_SWEEP);
		
		
		Instruction iatInstr =  new Instruction
		 (comm.getCommId(), instrType, instrStatus, null, persist, userName, facade);
		
		Date processDate = DateUtils.getNow();
		iatInstr.setProcessDate(processDate);
		iatInstr.setOriginalProcessDate(processDate);
		
		if (persist)
			iatInstr = Instruction.add(iatInstr, userName, facade);
		
		return iatInstr;
	}
	
	
	public static Instruction generateRETFUNDSInstruction(boolean persist, String userName, FWFacade facade) throws DataException {
		
		CommGroup commGroup = null;
		Comm comm = null;
		CommSource commSource = null;
		
		if (persist) {
			commGroup = CommGroup.add(null, null, facade, userName);
		} else {
			commGroup = new CommGroup(); // create temp CommGroup instead
		}
		
		//3. Create Comm
		commSource =CommSource.BANK;					
		
		if (persist) {
			comm = Comm.add(commSource, CommType.TRANSACTION_BATCH, 
				null, null, DateUtils.getNow(), userName, null, 
				null, commGroup.getCommGroupId(), facade, userName);
		} else {
			comm = new Comm(0, commSource, CommType.TRANSACTION_BATCH, 
					 Comm.DEFAULT_COMM_STATUS, null, 
					 null, null, userName, null, 
					 null, commGroup.getCommGroupId());
		}
		
		InstructionType instrType = InstructionType.getIdCache().getCachedInstance(InstructionType.Ids.RETFUNDS);
		InstructionStatus instrStatus = InstructionStatus.getCache().getCachedInstance(InstructionStatus.StatusID.READY_FOR_SPP_RELEASE);
		
		Instruction retfundsInstr =  new Instruction
		 (comm.getCommId(), instrType, instrStatus, null, persist, userName, facade);
		
		Date processDate = DateUtils.getNow();
		retfundsInstr.setProcessDate(processDate);
		retfundsInstr.setOriginalProcessDate(processDate);
		
		if (persist)
			retfundsInstr = Instruction.add(retfundsInstr, userName, facade);
		
		return retfundsInstr;
	}
	
	
	/**
	 * Updates all instructions with the dicondin reference.
	 * Takes an array of string object in the format [instruction_id]|[matching_dicondin_id]
	 * 
	 * @param instrMatches
	 * @param userName
	 * @param persist
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static DicondinRefResult addOrRemoveDicondinRef(String[] instrMatches, 
											  	String userName, 
												boolean persist, 
												FWFacade facade) 
	throws DataException, ServiceException {	
		String[] match;
		int instrId;
		Integer refId;
		BigDecimal amount = new BigDecimal("0");
		boolean allInstructionMatch = true;
		DicondinRefResult result = new DicondinRefResult();
		
		Map<Integer, BigDecimal> instrTotalsMap = new HashMap<Integer,BigDecimal>();
		Vector<Instruction> preadviceVec = new Vector<Instruction>();
		
		for (int i=0; i<instrMatches.length; i++) 
		{
			match = instrMatches[i].split("\\|");
			instrId = Integer.parseInt(match[0]);
			amount = new BigDecimal("0");
			
			if (match.length<2) {
				allInstructionMatch = false;
				refId = null;
			} else {
				refId = Integer.parseInt(match[1]);
			}
			
			Instruction transInstr = Instruction.get(instrId, facade);
			
			if (transInstr==null)
				throw new ServiceException("Instruction not found with id:"+instrId);

			InstructionDataApplied dicondinRefApplied = 
				InstructionDataApplied.get(instrId, InstructionData.Fields.DICONDIN_REF, facade);
			
			if (refId!=null) 
			{
				InstructionDataFieldValue fieldValue;
				
				if (dicondinRefApplied!=null) {
					fieldValue = dicondinRefApplied.getDataFieldValue();
					fieldValue.setIntValue(refId);
					dicondinRefApplied.setDataFieldValue(fieldValue);
				} else {
					InstructionData instrData = 
						InstructionData.getCache().getCachedInstance(InstructionData.Fields.DICONDIN_REF);
					
					ApplicableInstructionData applicableData = 
						transInstr.getType().getApplicableInstructionData(instrData);
					
					fieldValue = new InstructionDataFieldValue(instrData.getDataType());
					fieldValue.setIntValue(refId);
					
					dicondinRefApplied = new InstructionDataApplied(
							transInstr.getInstructionId(), applicableData, fieldValue);
				}
				
				if (persist)
					InstructionDataApplied.addOrUpdate(dicondinRefApplied, facade, userName);
			
				InstructionDataApplied cashApplied = 
					InstructionDataApplied.get(instrId, InstructionData.Fields.CASH, facade);
				
				if (cashApplied!=null && !cashApplied.getDataFieldValue().isEmpty()) {
					amount = cashApplied.getDataFieldValue().getBigDecimalValue();
				}
				
				if (instrTotalsMap.containsKey(refId)) {
					amount = amount.add(instrTotalsMap.get(refId));
				}

				instrTotalsMap.put(refId, amount);

				//Check if this dicondins is matched against a transaction i.e. DEPBNK, BUYBNK
				//If it is don't generate depbnk or buybnk instructions.
				if (transInstr.getType().getInstructionTypeId() == InstructionType.Ids.PREADVICE
						&& !TransactionBatchUtils.isDicondinMatchedToTransactions(refId, facade)) {
					preadviceVec.add(transInstr);	
				}
				
			} else {
				//dicondin ref is empty, remove the previous entry if it exist.
				if (dicondinRefApplied!=null) {
					if (persist)
						InstructionDataApplied.remove(dicondinRefApplied, facade, userName);
				}
			}
		}
		
		//Now check the totals and update the dicondin instructions and transaction batch
		if (!instrTotalsMap.isEmpty()) 
		{
			BigDecimal diCondinAmount = null;
			for (Integer dicondinId: instrTotalsMap.keySet()) 
			{
				diCondinAmount = new BigDecimal("0");
				
				Instruction dicondinInstr = Instruction.get(dicondinId, facade);
				
				if (dicondinInstr==null)
					throw new ServiceException("Instruction not found with id:"+dicondinId);
				
				InstructionDataApplied cashApplied = 
					InstructionDataApplied.get(dicondinId, InstructionData.Fields.CASH, facade);
				
				if (cashApplied!=null && !cashApplied.getDataFieldValue().isEmpty())
					diCondinAmount = cashApplied.getDataFieldValue().getBigDecimalValue();

				InstructionDataApplied matchApplied = 
					InstructionDataApplied.get(dicondinId, InstructionData.Fields.DICONDIN_MATCHED, facade);	
				
				InstructionDataFieldValue fieldValue;

				if (diCondinAmount==instrTotalsMap.get(dicondinId)) {
					if (matchApplied!=null) {
						fieldValue = matchApplied.getDataFieldValue();
						fieldValue.setBoolValue(true);
						matchApplied.setDataFieldValue(fieldValue);
					} else {
						InstructionData instrData = 
							InstructionData.getCache().getCachedInstance(InstructionData.Fields.DICONDIN_MATCHED);
						
						ApplicableInstructionData applicableData = 
							dicondinInstr.getType().getApplicableInstructionData(instrData);
						
						fieldValue = new InstructionDataFieldValue(instrData.getDataType());
						fieldValue.setBoolValue(true);
						
						matchApplied = new InstructionDataApplied(
								dicondinInstr.getInstructionId(), applicableData, fieldValue);
					}
					
					if (persist) 
						InstructionDataApplied.addOrUpdate(matchApplied, facade, userName);

				} else {
					if (matchApplied!=null) {
						if (persist)
							InstructionDataApplied.remove(matchApplied, facade, userName);
					}
				}					
			}
		}
		
		result.setAllInstructionMatch(allInstructionMatch);
		result.setInstrTotalsMap(instrTotalsMap);
		result.setPreadviceVec(preadviceVec);
		return result;
	}		
	
	
	/**
	 * Utility method to generateRetfund for the given bnkcodninIds
	 * @param bnkcondinId
	 * @param persist
	 * @param userName
	 * @param facade
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static void generateRETFUNDSForIds(int bnkcondinId, boolean persist, String userName, FWFacade facade) 
	throws DataException, ServiceException 
	{
		Instruction bnkcondinInstr = Instruction.get(bnkcondinId, facade); 
		if (bnkcondinInstr==null || bnkcondinInstr.getType().getInstructionTypeId()!=InstructionType.Ids.BNKCONDIN) 
			throw new DataException("Cannot find BNKCONDIN with Id:"+bnkcondinId);
	
		Instruction retfundInstr = TransactionBatchUtils.generateRETFUNDSInstruction(persist, userName, facade);
		
		Vector<InstructionDataApplied> appliedInstructionData = new Vector<InstructionDataApplied>();
		
		//Loop through the retfund applicable data and check if the data exist in the bnkcondin and use it.
		//In theory the bnkcondin should contain all the information
		//Retfunds requires: 
		for (ApplicableInstructionData applicableData: retfundInstr.getType().getApplicableInstructionData()) {
			
			InstructionData instrData = applicableData.getInstructionData();
			InstructionDataFieldValue fieldValue = new InstructionDataFieldValue(instrData.getDataType());
			
			InstructionDataApplied dataApplied = 
				InstructionDataApplied.getDataApplied(bnkcondinId, instrData.getInstructionDataId(), facade);
			
			if (instrData.getInstructionDataId()==InstructionData.Fields.DICONDIN_REF) {
				fieldValue.setIntValue(bnkcondinId);
			} else {
				if (dataApplied!=null && !dataApplied.getDataFieldValue().isEmpty()) {
					fieldValue.setValue(dataApplied.getDataFieldValue().getRawValue()); 
				} 
			}
			
			if (!applicableData.isOptional() && fieldValue.isEmpty()) {
				// Required field value is missing.
				String msg = "Field '" + instrData.getLabel() + 
				 "' is required for instruction type " + retfundInstr.getType().getName() +
				 ". Ensure this field has a value set, or change the " +
				 "instruction type.";
				Log.error(msg);
				throw new DataException(msg);
			}
			
			InstructionDataApplied newAppliedData = 
				new InstructionDataApplied(
						retfundInstr.getInstructionId(), 
						applicableData, 
						fieldValue);
			appliedInstructionData.add(newAppliedData);	
		}
		
		//add this to the data base.
		if (persist)
			InstructionDataApplied.addOrUpdateAll(appliedInstructionData, facade, userName);
		
		//set the bnkcondins as matched
		InstructionDataApplied dataApplied = InstructionDataApplied.getDataApplied(bnkcondinInstr.getInstructionId(), InstructionData.Fields.DICONDIN_MATCHED, facade);
		dataApplied.getDataFieldValue().setBoolValue(true);
		if (persist)
			InstructionDataApplied.addOrUpdate(dataApplied, facade, userName);
		
		//Set the bank condins are returned
		bnkcondinInstr.setStatus(
				InstructionStatus.getCache().getCachedInstance(
						InstructionStatus.StatusID.RETURNED_FUND));
		if (persist)
			bnkcondinInstr.persist(facade, userName);
	}
	
	
	/**
	 * Inner Class to store results of the dicondins reference update.
	 * @author Cam
	 *
	 */
	public static class DicondinRefResult {
		
		private boolean allInstructionMatch = false;
		private Map<Integer, BigDecimal> instrTotalsMap = null;
		private Vector<Instruction> preadviceVec = null;
		
		public boolean isAllInstructionMatch() {
			return allInstructionMatch;
		}

		public Map<Integer, BigDecimal> getInstrTotalsMap() {
			return instrTotalsMap;
		}

		public void setInstrTotalsMap(Map<Integer, BigDecimal> instrTotalsMap) {
			this.instrTotalsMap = instrTotalsMap;
		}

		public Vector<Instruction> getPreadviceVec() {
			return preadviceVec;
		}

		public void setPreadviceVec(Vector<Instruction> preadviceVec) {
			this.preadviceVec = preadviceVec;
		}

		public void setAllInstructionMatch(boolean allInstructionMatch) {
			this.allInstructionMatch = allInstructionMatch;
		}
		
		public void addPreadviceInstr(Instruction preadviceInstr) 
		{
			if (preadviceVec==null)
				preadviceVec = new Vector<Instruction>();
			
			preadviceVec.add(preadviceInstr);	
		}
		
		public void addInstrTotal(Integer instrId, BigDecimal amount) 
		{
			if (instrTotalsMap!=null) {
				if (instrTotalsMap.containsKey(instrId)) {
					amount = amount.add(instrTotalsMap.get(instrId));
				} 
			} else {
				instrTotalsMap = new HashMap<Integer,BigDecimal>();
			}
			
			instrTotalsMap.put(instrId, amount);	
		}		
	}
}