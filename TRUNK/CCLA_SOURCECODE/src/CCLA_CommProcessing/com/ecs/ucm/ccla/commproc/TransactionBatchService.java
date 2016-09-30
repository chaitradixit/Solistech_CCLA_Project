package com.ecs.ucm.ccla.commproc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.commproc.TransactionBatchTranslator.TransBatchError;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.TransactionBatch;
import com.ecs.ucm.ccla.data.TransactionBatchStatus;
import com.ecs.ucm.ccla.data.instruction.ApplicableInstructionData;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;
//import com.ecs.utils.ucm.ContentItem;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

public class TransactionBatchService extends Service {


	private static final boolean debug = 
		SharedObjects.getEnvValueAsBoolean("CCLA_CommProc_EnableDebugOnTransactionBatch", false);
	/**
	 * For unprocessed transaction batches, update the status
	 * So if the document cannot be processed, the user can set this to invalid etc..
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void updateTransactionBatchDocument() throws DataException, ServiceException {
		
		Integer docId = CCLAUtils.getBinderIntegerValue(m_binder, Globals.UCMFieldNames.DocID);
		if (docId==null)
			throw new DataException("Cannot Process Document, document Id is null");
		
		String status = m_binder.getLocal(Globals.UCMFieldNames.Status);
		if (StringUtils.stringIsBlank(status))
			throw new DataException("Cannot Process Document, status is null");

		String comments = m_binder.getLocal(Globals.UCMFieldNames.Comments);

		
		try {
			LWDocument contentItem = new LWDocument(docId);
			contentItem.setAttribute(Globals.UCMFieldNames.Status, status);
			
			//Update the comments if it is not null.
			if (!StringUtils.stringIsBlank(comments))
				contentItem.setAttribute(Globals.UCMFieldNames.Comments, comments);
				
			contentItem.update();
		} catch (Exception e) {
			Log.error("Error updating unprocessed Transaction Batch status: "+e.getMessage(), e);
			throw new ServiceException("Error updating unprocessed Transaction Batch status: "+e.getMessage(), e);
		}
	}
	
	/**
	 * Creates a transaction batch plus instructions from the csv file in UCM
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void createTransactionBatch() throws DataException, ServiceException 
	{
		
		if (debug)
			Log.debug("Start *** Creating Transaction Batch and Instructions.");
		
		Integer docId = CCLAUtils.getBinderIntegerValue(m_binder, Globals.UCMFieldNames.DocID);
		if (docId==null)
			throw new ServiceException("Cannot Process Document, document Id is null");
	
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		boolean persist = CCLAUtils.getBinderBoolValue(m_binder, "persist");
		String userName = this.m_userData.m_name;
		TransactionBatch transBatch = null;
		
		try {
			LWDocument contentItem = new LWDocument(docId);
			//String fund = contentItem.getAttribute(Globals.UCMFieldNames.Fund);
			
			String bnkAccNo = contentItem.getAttribute(Globals.UCMFieldNames.BankAccountNumber);
			String sortCode = contentItem.getAttribute(Globals.UCMFieldNames.SortCode);
			String buildingSocietyNo = null;
			
			String seqStr = contentItem.getAttribute(Globals.UCMFieldNames.Amount);
			String dateStr = contentItem.getAttribute(Globals.UCMFieldNames.ProcessDate);
			

			if (StringUtils.stringIsBlank(bnkAccNo) ||
					StringUtils.stringIsBlank(sortCode)	||	
					StringUtils.stringIsBlank(seqStr) ||
					StringUtils.stringIsBlank(dateStr)) {
				throw new Exception("TransactionBatch is missing metadata fields: bank account No, sort code, amount, sequence or processDate, Please Check the file");
			}

			BankAccount bankAccount = BankAccount.getByValuesIncludingNull(bnkAccNo, sortCode, buildingSocietyNo, facade);
			if (bankAccount==null) {
				throw new Exception("Error Processing File, Cannot find BankAccount with BankAccNo:"+bnkAccNo+", SortCode:"+sortCode+", BuildingSocietyNo:NULL");			
			}
			
			Date transDate = DateFormatter.getSystemSimpleDateFormat().parse(dateStr);
			Integer seq = Integer.parseInt(seqStr);
			
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryIntParamToBinder(binder, TransactionBatch.Cols.SEQUENCE, seq);
//			CCLAUtils.addQueryParamToBinder(binder, TransactionBatch.Cols.FUND_CODE, fund);
//			CCLAUtils.addQueryParamToBinder(binder, TransactionBatch.Cols.BANK_ACCOUNT_NO, bnkAccNo );
//			CCLAUtils.addQueryParamToBinder(binder, TransactionBatch.Cols.SORT_CODE, sortCode);
			CCLAUtils.addQueryIntParamToBinder(binder, TransactionBatch.Cols.BANK_ACCOUNT_ID, bankAccount.getBankAccountId());

			CCLAUtils.addQueryDateParamToBinder(binder, "START_TRANSACTION_DATE", DateUtils.roundToBeginningOfDay(transDate));
			CCLAUtils.addQueryDateParamToBinder(binder, "END_TRANSACTION_DATE", DateUtils.roundToEndOfDay(transDate));
			
			DataResultSet rs = facade.createResultSet(TransactionBatch.GET_TRANSACTION_BATCH_BY_PARAMS_QUERY, binder);
			
			//Found result, this must be a duplicate
			if (rs!=null && !rs.isEmpty()) {
				
				Log.debug("Num of duplicate rows :"+rs.getNumRows());
				String msg = "Duplicate of "+CCLAUtils.getResultSetIntegerValue(rs, SharedCols.DOC);				
				//Log.error(msg);
				
				if (persist) {
					contentItem.setAttribute(Globals.UCMFieldNames.Status, "Duplicate");
					contentItem.setAttribute(Globals.UCMFieldNames.Comments, msg);
					contentItem.update();
				}
				throw new Exception(msg);			
			}
		} catch (Exception e) {
			Log.error(e.getMessage(),e);
			throw new DataException(e.getMessage());			
		}
		
		//Not a duplicate, so try and convert instructions.
		TransactionBatchTranslator translator = null;
		try {
			facade.beginTransaction();

//			if (docId==1 && doDummy) {
//				transBatch = generateDummyTransactions(facade, userName, 0F, 5000F, "F");
//			} else {
			translator = new TransactionBatchTranslator(docId, persist, userName, facade);
			//Translates the instruction and verify the transaction amounts.
			transBatch = translator.generateTransactionBatchAndInstructions();
			
//			}
			if (persist)
				facade.commitTransaction();
			else 
				facade.rollbackTransaction();
			
		} catch (Exception e) {
			String msg = "Unable to import Transaction Batch: " + e.getMessage();
			//Finally set the flag to true
			
			CCLAUtils.addQueryBooleanParamToBinder(m_binder, "SUCCESS", false);

			facade.rollbackTransaction();		
			
			//Populate the m_binder with the error messages
			if (translator!=null && translator.getErrorList()!=null && !translator.getErrorList().isEmpty()) {
				
				DataResultSet rsError = new DataResultSet(new String[] {
						"LINE_NO",
						"TYPE", 
						"MESSAGE"});
				
				for (TransBatchError error : translator.getErrorList()) {
					
					Vector<String> rowData = new Vector<String>();
					rowData.add(String.valueOf(error.getLineNumber()));
					rowData.add(StringUtils.stringIsBlank(error.getType())?"N/A":error.getType());
					rowData.add(error.getErrorMessage());
					
					rsError.addRow(rowData);
				}
				m_binder.addResultSet("rsError", rsError);
				
				if (debug)
					Log.debug("Instruction Creation Errors...");
				
				return;
			}
			
			if (persist) 
				throw new DataException(msg, e);	
			
		}
		//Update Content
		if (persist && transBatch!=null) {
			try {
				LWDocument contentItem = new LWDocument(docId);
				contentItem.setAttribute(Globals.UCMFieldNames.Status, "Processed");
				contentItem.update(); 
			} catch (Exception e) {
				Log.error("Error Updating ContentItem with docId:"+docId+" "+e.getMessage());
				
			}
		}
		
		// Append new transactionBatchId to RedirectUrl
		String redirectUrl = m_binder.getLocal("RedirectUrl");
		
		if (!StringUtils.stringIsBlank(redirectUrl)) {
			redirectUrl += "&"+TransactionBatch.Cols.TRANS_BATCH_ID+"="+transBatch.getTransactionBatchId();
			
			m_binder.putLocal("RedirectUrl", redirectUrl);
			CCLAUtils.addQueryIntParamToBinder(m_binder,TransactionBatch.Cols.TRANS_BATCH_ID, transBatch.getTransactionBatchId());
		}

		//Finally set the flag to true
		CCLAUtils.addQueryBooleanParamToBinder(m_binder, "SUCCESS", true);
		
		if (debug)
			Log.debug("Finished Creating Transaction Batch and Instructions");
	}
	
	/**
	 * User for displaying information about a transaction batch, 
	 * also gets all the unmatched instructions for use.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getTransactionBatchInformation() throws DataException, ServiceException 
	{
		if (debug) 
			Log.debug("Start getTransactionBatchInformation");
		
		Integer transBatchId = 
			CCLAUtils.getBinderIntegerValue(m_binder, TransactionBatch.Cols.TRANS_BATCH_ID);
		
		if (transBatchId==null) {
			throw new ServiceException("Cannot get TransactionBatch information, Id is null"); 
		}
		
		//Use oracle facade!
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsTransBatch = TransactionBatch.getData(transBatchId, facade);
		
		if (rsTransBatch==null || rsTransBatch.isEmpty()) {
			throw new ServiceException("Cannot find TransactionBatch information with TransactionBatchId:"+transBatchId); 
		}
		
		TransactionBatch transBatch = TransactionBatch.get(rsTransBatch);
		
		if (transBatch.getCommId()==0) {
			throw new ServiceException("TransactionBatch doesn't contain a commId"); 			
		}
		
		boolean previousBatchIsProcessed = TransactionBatchUtils.calculatePreviousUnprocessedTransactionBatch(transBatch, facade);
		
		//get all instructions with this transaction batch. 
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.COMM, transBatch.getCommId());
		CCLAUtils.addQueryDateParamToBinder(binder, "START_TRANSACTION_DATE", DateUtils.roundToBeginningOfDay(transBatch.getTransactionDate()));
		CCLAUtils.addQueryDateParamToBinder(binder, "END_TRANSACTION_DATE", DateUtils.roundToEndOfDay(transBatch.getTransactionDate()));

		BankAccount bankAcc = BankAccount.get(transBatch.getBankAccountId(), facade);
		
		CCLAUtils.addQueryParamToBinder(binder, "DEST_BANK_ACCOUNT_NUMBER", (bankAcc!=null)?bankAcc.getAccountNumber():"");
		CCLAUtils.addQueryParamToBinder(binder, "DEST_SORT_CODE", (bankAcc!=null)?bankAcc.getSortCode():"");
//		
		//get all transaction batch instructions
		DataResultSet rsTranBatchInstr = facade.createResultSet("qCore_GetAllTransactionBatchInstructions", binder);
		
		//loop through and get all matching instructions
		DataResultSet rsMatchedInstr = new DataResultSet(new String[] {
				"INSTRUCTION_ID",
				"INSTRUCTION_TYPE_NAME", 
				"INSTRUCTION_TYPE_ID",
				"CASH",
				"NARRATIVE",
				"DICONDIN_MATCHED",
				"DOC_DATE",
				"TRANS_BATCH_MATCH"});
		
		do {
			Integer dicondinInstrId = CCLAUtils.getResultSetIntegerValue(rsTranBatchInstr, "DICONDIN_REF");
			
			InstructionDataApplied matchDataApplied;
			
			if (dicondinInstrId!=null) {
				Instruction matchedDicondin = Instruction.get(dicondinInstrId, facade);
				
				if (matchedDicondin!=null) 
				{					
					Vector<String> matchInstrVec = new Vector<String>();
					
					matchInstrVec.add(String.valueOf(matchedDicondin.getInstructionId()));
					matchInstrVec.add(String.valueOf(matchedDicondin.getType().getName()));
					matchInstrVec.add(String.valueOf(matchedDicondin.getType().getInstructionTypeId()));
					
					//Cash Value
					matchDataApplied = InstructionDataApplied.getDataApplied(
							matchedDicondin.getInstructionId(), 
							InstructionData.Fields.CASH, 
							facade);

					
					if (matchDataApplied!=null && !matchDataApplied.getDataFieldValue().isEmpty()) {
						matchInstrVec.add(matchDataApplied.getDataFieldValue().getBigDecimalValue().toString());
					} else {
						matchInstrVec.add("");
					}
					
					//Narrative Value
					matchDataApplied = InstructionDataApplied.getDataApplied(
							matchedDicondin.getInstructionId(), 
							InstructionData.Fields.NARRATIVE, 
							facade);
					
					if (matchDataApplied!=null && !matchDataApplied.getDataFieldValue().isEmpty()) {
						matchInstrVec.add(matchDataApplied.getDataFieldValue().getStringValue());
					} else {
						matchInstrVec.add("");
					}
					
					//dicondin match Value
					matchDataApplied = InstructionDataApplied.getDataApplied(
							matchedDicondin.getInstructionId(), 
							InstructionData.Fields.DICONDIN_MATCHED, 
							facade);
					
					if (matchDataApplied!=null && !matchDataApplied.getDataFieldValue().isEmpty()) {
						matchInstrVec.add(matchDataApplied.getDataFieldValue().getBoolValue().toString());
					} else {
						matchInstrVec.add("");
					}
					
					//Doc date Value
					matchDataApplied = InstructionDataApplied.getDataApplied(
							matchedDicondin.getInstructionId(), 
							InstructionData.Fields.DOC_DATE, 
							facade);
					
					if (matchDataApplied!=null && !matchDataApplied.getDataFieldValue().isEmpty()) {
						matchInstrVec.add(DateFormatter.getTimeStamp(matchDataApplied.getDataFieldValue().getDateValue()));
					} else {
						matchInstrVec.add("");
					}
					
					//trans batch match
					matchDataApplied = InstructionDataApplied.getDataApplied(
							matchedDicondin.getInstructionId(), 
							InstructionData.Fields.TRANS_BATCH_REFERENCE, 
							facade);
					
					if (matchDataApplied!=null && !matchDataApplied.getDataFieldValue().isEmpty()) {
						matchInstrVec.add(String.valueOf(matchDataApplied.getDataFieldValue().getIntValue()));
					} 
					else {
						matchInstrVec.add("");
					}
					
					rsMatchedInstr.addRow(matchInstrVec);
				}
			} 
		} while (rsTranBatchInstr.next());
		
		//reset the result set.
		rsTranBatchInstr.first();
		
		//get all pending instructions of type DICONDINS 
		//which doesn't contain a match
		//DataResultSet rsPendingInstr = facade.createResultSet("qCore_GetAllPendingDicondinInstructions",binder);
		
		//Gets all pending dicondins instructions that has the 
		//same destination bank account number and sort code as the transaction batch. 
		
		CCLAUtils.addQueryIntParamToBinder(binder, "INSTRUCTION_TYPE_ID", InstructionType.Ids.DICONDIN);
		
		DataResultSet rsPendingInstr = 
			facade.createResultSet("qCore_GetAllPendingInstructionsByBankAccount",binder);

		CCLAUtils.addQueryIntParamToBinder(binder, "INSTRUCTION_TYPE_ID", InstructionType.Ids.DEPCHQ);
		
		DataResultSet rsPendingDepChqInstr = 
			facade.createResultSet("qCore_GetAllPendingInstructions",binder);
		
		CCLAUtils.addQueryIntParamToBinder(binder, "INSTRUCTION_TYPE_ID", InstructionType.Ids.DEPCHQTP);
		
		DataResultSet rsPendingDepChqTPInstr = 
			facade.createResultSet("qCore_GetAllPendingInstructions",binder);
		
				
		
		//get suggested Instructions for this transaction batch
		BigDecimal expectedAmount = TransactionBatchUtils.calculateDifferenceInClosingBalance(transBatch, facade);
		BigDecimal changeInAmount = expectedAmount;
		Vector<Instruction> instrVec = Instruction.getByCommId(transBatch.getCommId(), facade);
		List<Integer> matchedInstrId = 
			TransactionBatchUtils.getSuggestedMatchForTransactionBatch(instrVec, expectedAmount, facade);
		
		DataResultSet rsSuggestedMatches = new DataResultSet(new String[]{ SharedCols.INSTRUCTION});
		for (Integer instrId: matchedInstrId) {
			Vector<String> vec = new Vector<String>();
			vec.add(instrId.toString());
			rsSuggestedMatches.addRow(vec);
		}

		//get change in amount
		//BigDecimal changeInAmount = TransactionBatchUtils.calculateDifferenceInClosingBalance(transBatch, facade);
		
		//calculate best matches for instructions
		boolean matchAll = true;
		DataResultSet rsCalculatedMatches = new DataResultSet(new String[] {"DEPBNK_ID", "DICONDIN_ID"});
		
		if (rsPendingInstr!=null && !rsPendingInstr.isEmpty()) {
			do {
				int currInstrId = CCLAUtils.getResultSetIntegerValue(rsTranBatchInstr, "INSTRUCTION_ID");
				
				if (!matchedInstrId.contains(currInstrId)) {
					float currAmount = CCLAUtils.getResultSetFloatValue(rsTranBatchInstr, "CASH");
					boolean found = false;
					Integer matchId = 0;	
					rsPendingInstr.first();
					
					do { 
						float amount = CCLAUtils.getResultSetFloatValue(rsPendingInstr, "CASH");
						int pendingInstrId = CCLAUtils.getResultSetIntegerValue(rsPendingInstr, "INSTRUCTION_ID"); 
					
						if (debug)
							Log.debug("Comparing Match:- dicondin:"+pendingInstrId+":"+amount+",  transInstr:"+currInstrId+":"+currAmount);
						
						if (amount==currAmount) {
							if (found) {
								//Ambiguous match, resetting.
								if (debug)
									Log.debug("Ambiguous Match:- dicondin:"+pendingInstrId+":"+amount+",  transInstr:"+currInstrId+":"+currAmount);
								
								found = false;
								matchId = null; 
								break;
							} else {
								//first match
								if (debug)
									Log.debug("First Match:- dicondin:"+pendingInstrId+":"+amount+",  transInstr:"+currInstrId+":"+currAmount);
								
								found = true;
								matchId = pendingInstrId;
							}
						} 
					} while (rsPendingInstr.next());
					
					if (found && matchId!=null) {
						Vector<String> row = new Vector<String>();
						row.add(String.valueOf(currInstrId));
						row.add(String.valueOf(matchId));
						rsCalculatedMatches.addRow(row);
					} else 
						matchAll = false;
				}
			} while (rsTranBatchInstr.next());
			
		}
		//Add results to binder for displaying.
		CCLAUtils.addQueryBigDecimalParamToBinder(m_binder, "ChangeInAmount", changeInAmount);
		m_binder.addResultSet("rsTransBatch", rsTransBatch);
		m_binder.addResultSet("rsTransBatchInstr", rsTranBatchInstr);
		m_binder.addResultSet("rsPendingInstr", rsPendingInstr);
		m_binder.addResultSet("rsPendingDepChqInstr", rsPendingDepChqInstr);
		m_binder.addResultSet("rsPendingDepChqTPInstr", rsPendingDepChqTPInstr);
		m_binder.addResultSet("rsSuggestedMatches", rsSuggestedMatches);
		m_binder.addResultSet("rsMatchedInstr", rsMatchedInstr);
		m_binder.addResultSet("rsCalculatedMatches", rsCalculatedMatches);
		CCLAUtils.addQueryBooleanParamToBinder(m_binder, "matchAll", matchAll);
		CCLAUtils.addQueryBooleanParamToBinder(m_binder, "previousBatchIsProcessed", previousBatchIsProcessed);
	}
	
	/**
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void doTransactionBatchLanding() throws DataException, ServiceException 
	{	
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		Date transactionDate = CCLAUtils.getBinderDateValue(m_binder, TransactionBatch.Cols.TRANSACTION_DATE);
		if (transactionDate==null)
			transactionDate = DateUtils.getNow();
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryDateParamToBinder(binder, 
				TransactionBatch.Cols.TRANSACTION_DATE, 
				transactionDate);
		
		CCLAUtils.addQueryDateParamToBinder(binder,"START_TRANSACTION_DATE", 
				DateUtils.roundToBeginningOfDay(transactionDate));
		
		CCLAUtils.addQueryDateParamToBinder(binder,"END_TRANSACTION_DATE", 
				DateUtils.roundToEndOfDay(transactionDate));
		
		CCLAUtils.addQueryIntParamToBinder(binder, 
				TransactionBatch.Cols.TRANS_BATCH_STATUS_ID, 
				TransactionBatchStatus.Statuses.PENDING_STATUS);
		
		
		DataResultSet rsUnprocessed = facade.createResultSet("qCore_GetUnprocessedTransactionBatch", binder);
		DataResultSet rsTransBatch = facade.createResultSet("qCore_GetTransactionBatchByStatusId", binder);
		
//		if ((rsUnprocessed==null || rsUnprocessed.isEmpty()) && doDummy) {
//			rsUnprocessed = new DataResultSet(new String[] {"dID", "dDocName", "xFund", "xAmount", "xProcessDate"});
//			
//			Vector<String> vec = new Vector<String>();
//			
//			vec.add("1");
//			vec.add("dummyTest");
//			vec.add("C");
//			vec.add("1");
//			vec.add(DateUtils.formatddsMMsyyyyspHHcmm(DateUtils.getNow()));
//			
//			rsUnprocessed.addRow(vec);
//		}
		
		m_binder.addResultSet("rsUnprocessed", rsUnprocessed);
		m_binder.addResultSet("rsTransBatch", rsTransBatch);
	}
	
	
	/**
	 * Updates instructions with matches.
	 * If all matches are performed the transaction batch is considered completed
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void processTransactionBatch() throws DataException, ServiceException {
		
		Log.debug("--- start process transaction batch");
		
		String userName = m_userData.m_name;
		
		Integer transBatchId = CCLAUtils.getBinderIntegerValue(m_binder, TransactionBatch.Cols.TRANS_BATCH_ID);
		if (transBatchId==null)
			throw new ServiceException("Cannot process transaction batch, id is null");
		
		boolean persist = CCLAUtils.getBinderBoolValue(m_binder, "persist");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		//get list of dicondins match
		String instrMatchesString = m_binder.getLocal("instrMatches");
		String[] instrMatches = null;
		if (!StringUtils.stringIsBlank(instrMatchesString))
			instrMatches = instrMatchesString.split(",");
		
		String transBatchMatchesString = m_binder.getLocal("transBatchMatches");
		String[] transBatchMatches = null;
		if (!StringUtils.stringIsBlank(transBatchMatchesString))
			transBatchMatches = transBatchMatchesString.split(",");

		String[] match;
		int instrId;
		Integer refId;
		BigDecimal amount = new BigDecimal("0");
		boolean allInstructionMatch = true;
		Map<Integer, BigDecimal> instrTotalsMap = new HashMap<Integer,BigDecimal>();
		
		try {
			facade.beginTransaction();

			TransactionBatch transBatch = TransactionBatch.get(transBatchId, facade);
			if (transBatch==null)
				throw new DataException("TransactionBatch cannot be found with id:"+transBatchId);
			
			if (transBatchMatches!=null) {
				for (String batchMatchesId: transBatchMatches) {
					instrId = Integer.parseInt(batchMatchesId);
					Instruction instr = Instruction.get(instrId, facade);
					
					if (instr==null)
						throw new DataException("Instruction not found for id:"+instrId);
					
					InstructionDataApplied dataApplied = 
						InstructionDataApplied.getDataApplied(instrId, InstructionData.Fields.TRANS_BATCH_REFERENCE, facade);
					if (dataApplied!=null) {
						InstructionDataFieldValue fieldValue = dataApplied.getDataFieldValue();
						fieldValue.setIntValue(transBatch.getTransactionBatchId());
						dataApplied.setDataFieldValue(fieldValue);
					} else {
						InstructionData instrData = 
							InstructionData.getCache().getCachedInstance(InstructionData.Fields.TRANS_BATCH_REFERENCE);
						
						ApplicableInstructionData applicableData = 
							instr.getType().getApplicableInstructionData(instrData);
						
						InstructionDataFieldValue fieldValue = new InstructionDataFieldValue(instrData.getDataType());
						fieldValue.setIntValue(transBatch.getTransactionBatchId());
						
						dataApplied = new InstructionDataApplied(
								instr.getInstructionId(), applicableData, fieldValue);	
						
					}
					
					if (persist)
						InstructionDataApplied.addOrUpdate(dataApplied, facade, userName);
				}
			}
			
			if (instrMatches!=null) {
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
							amount= amount.add(instrTotalsMap.get(refId));
						}

						instrTotalsMap.put(refId, amount);

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
						
						if (allInstructionMatch) {
							transBatch.setStatusId(TransactionBatchStatus.Statuses.PROCESSED_STATUS);
							transBatch.setAppliedDate(DateUtils.getNow());
						} else {
							transBatch.setStatusId(TransactionBatchStatus.Statuses.PENDING_STATUS);
							transBatch.setAppliedDate(null);							
						}
						
						if (persist)
							transBatch.persist(facade, userName);
					}
				}
			} else {
				transBatch.setStatusId(TransactionBatchStatus.Statuses.PROCESSED_STATUS);
				transBatch.setAppliedDate(DateUtils.getNow());
				
				if (persist)
					transBatch.persist(facade, userName);
			}
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			Log.error("Error processing transaction batch",e);
			throw new ServiceException("Error processing transaction batch",e);
		}

		Log.debug("--- end process transaction batch");
	}

	/**
	 * Gets any outstanding instructions for the outstanding DICONDIN and list any instructions to match.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getPendingDicondinInfo() throws DataException, ServiceException 
	{	
		Date startTransactionDate = CCLAUtils.getBinderDateValue(m_binder, "startTransactionDate");
		Date endTransactionDate = CCLAUtils.getBinderDateValue(m_binder, "endTransactionDate");
		Date transactionDate = null;
		
		if (startTransactionDate!=null) {
			startTransactionDate = DateUtils.roundToBeginningOfDay(startTransactionDate);
			
			if (endTransactionDate!=null)
				endTransactionDate = DateUtils.roundToEndOfDay(endTransactionDate);
			else 
				endTransactionDate = DateUtils.roundToEndOfDay(startTransactionDate);
		}
		
		if (startTransactionDate==null || endTransactionDate==null) {
			transactionDate = CCLAUtils.getBinderDateValue(m_binder, "transactionDate");
			
			if (transactionDate==null)
				transactionDate = DateUtils.getDateFromNow(-1);
			startTransactionDate = DateUtils.roundToBeginningOfDay(transactionDate);
			endTransactionDate = DateUtils.roundToEndOfDay(transactionDate);
		}
		
		String fundCode = m_binder.getLocal(SharedCols.FUND);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryDateParamToBinder(binder, 
				"START_TRANSACTION_DATE", startTransactionDate);
		
		CCLAUtils.addQueryDateParamToBinder(binder, 
				"END_TRANSACTION_DATE", endTransactionDate);
		
		CCLAUtils.addQueryIntParamToBinder(binder, 
				Instruction.INSTRUCTION_TYPE_ID, 
				InstructionType.Ids.PREADVICE);
		
		
		//SQL Query to use
		String query = "qCore_GetAllPendingTransBatchInstructionsByType";
		
		if (!StringUtils.stringIsBlank(fundCode)) {
			CCLAUtils.addQueryParamToBinder(binder, SharedCols.FUND, fundCode);
			query = "qCore_GetAllPendingTransBatchInstructionsByTypeAndFund";
		} 
		
		DataResultSet rsPendingPreadvice = 
			facade.createResultSet(query, binder);
		
		DataResultSet rsChildPreadvices = getChildPreadvices(rsPendingPreadvice);
		
		CCLAUtils.addQueryIntParamToBinder(binder, 
				Instruction.INSTRUCTION_TYPE_ID, 
				InstructionType.Ids.DICONDIN);
		
		DataResultSet rsPendingDiCondins = 
			facade.createResultSet(query, binder);
		
		CCLAUtils.addQueryIntParamToBinder(binder, 
				Instruction.INSTRUCTION_TYPE_ID, 
				InstructionType.Ids.BNKCONDIN);
		
		DataResultSet rsPendingBnkCondins = 
			facade.createResultSet(query, binder);
		
		DataResultSet rsDates = new DataResultSet(new String[] {"TransactionDate"});
		
		for (int i=0; i>=-5;i--)
		{
			Vector<String> dates = new Vector<String>();
			dates.add(DateFormatter.getTimeStamp(DateUtils.getDateFromNow(i)));
			rsDates.addRow(dates);
		}
		
		m_binder.addResultSet("rsPendingDiCondins", rsPendingDiCondins);
		m_binder.addResultSet("rsPendingPreadvice", rsPendingPreadvice);
		m_binder.addResultSet("rsPendingBnkCondins", rsPendingBnkCondins);
		m_binder.addResultSet("rsChildPreadvices", rsChildPreadvices);
		m_binder.addResultSet("rsDates", rsDates);
		if (!StringUtils.stringIsBlank(fundCode))
			m_binder.putLocal("currentFund", fundCode);
		CCLAUtils.addQueryDateParamToBinder(m_binder, "userTransactionDate", transactionDate);
	}
	
	/**
	 * Processes pending dicondins.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void processPendingDicondin() throws DataException, ServiceException {
		
		String userName = m_userData.m_name;
		//get list of dicondins match
		String instrMatchesString = m_binder.getLocal("instrMatches");
		String allPendingDicondin = m_binder.getLocal("pendingDicondinList");
		
		boolean persist = CCLAUtils.getBinderBoolValue(m_binder, "persist");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		
		try {
			facade.beginTransaction();
			Map<Integer, BigDecimal> instrTotalsMap = new HashMap<Integer, BigDecimal>(); 			
			
			//Process Preadvice and Dicondin matches
			if (!StringUtils.stringIsBlank(instrMatchesString)) 
			{
				String[] match;
				int instrId;
				Integer refId;
				
				Vector<Instruction> preadviceVec = new Vector<Instruction>();
				String[] instrMatches = instrMatchesString.split(",");
				
				BigDecimal amount;

				for (int i=0; i<instrMatches.length; i++) 
				{
					match = instrMatches[i].split("\\|");
					
					instrId = Integer.parseInt(match[0]);
					amount = new BigDecimal("0");
					
					if (match.length<2) {
						refId = null;
					} else {
						if (!StringUtils.stringIsBlank(match[1]))
							refId = Integer.parseInt(match[1]);
						else
							refId = null;
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
							amount=amount.add(instrTotalsMap.get(refId));
						}
	
						instrTotalsMap.put(refId, amount);
						
						//Check if this dicondins is matched against a transaction i.e. DEPBNK, BUYBNK
						//If it is don't generate depbnk or buybnk instructions.
						if (!TransactionBatchUtils.isDicondinMatchedToTransactions(refId, facade))
							preadviceVec.add(transInstr);					
					} else {
						//dicondin ref is empty, remove the previous entry if it exist.
						if (dicondinRefApplied!=null) {
							if (persist)
								InstructionDataApplied.remove(dicondinRefApplied, facade, userName);
						}
					}
				}
				
				//Generate buybnk or depbnk for preadvices.
				Vector<Instruction> preadviceTransactionVec = 
					TransactionBatchUtils.generateInstrForPreadvice(preadviceVec, userName, persist, facade);
				
				if (persist) {
					for (Instruction instr: preadviceTransactionVec) {
						
						Vector<InstructionDataApplied> dataApplied = instr.getDataApplied();
						
						Instruction.add(instr, userName, facade);
						
						InstructionDataApplied.addOrUpdateAll(dataApplied, facade, userName);
					}
				}
				
				//Now check the totals and update the dicondin instructions and transaction batch
				if (!instrTotalsMap.isEmpty()) 
				{
					BigDecimal diCondinAmount;
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
			}
			
			//Convert remaining dicondin to bnkcondins
			if (!StringUtils.stringIsBlank(allPendingDicondin)) 
			{
				String[] matches = allPendingDicondin.split(",");
				
				for (int i=0; i<matches.length;i++) 
				{
					if (!StringUtils.stringIsBlank(matches[i])) 
					{
						int pendDicondinId = Integer.parseInt(matches[i]);
						
						if (!instrTotalsMap.containsKey(pendDicondinId)) 
						{
							if (debug)
								Log.debug("converting dicondin to bnkcondin with id:"+pendDicondinId);
							TransactionBatchTranslator.convertDicondinToBnkcondin(pendDicondinId, persist, userName, facade);
						} else {
							if (debug)
								Log.debug("not converting dicondin to bnkcondin with id:"+pendDicondinId);
						}
					}
				}
			}
			
			//Finally commit the transaction!
			facade.commitTransaction();
			
		} catch (Exception e) {
			Log.debug("Rolling back transaction :"+facade.isInTransaction());
			facade.rollbackTransaction();
			Log.error("Error processing transaction batch",e);
			throw new ServiceException("Error processing transaction batch",e);
		}
	}
	
	/**
	 * Converts an dicondin into a bnkcondin if the dicondin is older than 24 hours
	 * @param persist
	 * @param userName
	 * @param facade
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static void checkAndConvertExpiredDicondin(boolean persist, String userName, FWFacade facade) 
	throws DataException, ServiceException {
		
		try {
			facade.beginTransaction();
			
			Date previousTime = DateUtils.getDateFromNow(-1);
			
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryDateParamToBinder(binder, "END_DATE_TIME", previousTime);
			
			DataResultSet rsExpiredDicondin = facade.createResultSet("", binder);
			
			do {
				int dicondinId = CCLAUtils.getResultSetIntegerValue(rsExpiredDicondin, "");
				TransactionBatchTranslator.convertDicondinToBnkcondin(dicondinId, persist, userName, facade);
				
			} while (rsExpiredDicondin.next());
			
			facade.commitTransaction();
		
		} catch (Exception e) {
			Log.error("Failed checkAndConvertExpiredDicondin "+e.getMessage(), e);
			facade.rollbackTransaction();
			throw new ServiceException("Failed checkAndConvertExpiredDicondin "+e.getMessage(),e);
		}
	}
	
	/**
	 * 
	 * @param facade
	 * @param userName
	 * @param openingAmount
	 * @param closingAmount
	 * @param fundCode
	 * @return
	 * @throws DataException
	 */
//	private TransactionBatch generateDummyTransactions(FWFacade facade, String userName, 
//			Float openingAmount, Float closingAmount, String bankAccNo, String sortCode) throws DataException {
//		Log.debug("--- generateDummyTransactions");
//		
//		TransactionBatch transBatch = null;
//		
//		try {
//			
//			Log.debug("--- create CommGroup");
//			//CommGroup
//			CommGroup commGroup = CommGroup.add(null, null, facade, userName);
//			Log.debug("--- finished creating CommGroup");			
//			
//			//Comm
//			Log.debug("--- create Comm");
//			Comm comm = Comm.add(CommSource.USER, CommType.TRANSACTION_BATCH, 
//					null, null, DateUtils.getNow(), userName, null, 
//					null, commGroup.getCommGroupId(), facade, userName);
//			Log.debug("--- finished creating CommGroup");
//			//TransactionBatch			
//			
//			Log.debug("--- create Transaction Batch");
//			transBatch = new TransactionBatch(0, bankAccNo, sortCode, 
//					DateUtils.roundToBeginningOfDay(DateUtils.getNow()),
//					DateUtils.getNow(), openingAmount, closingAmount, 
//					TransactionBatchStatus.Statuses.PENDING_STATUS, userName,
//					comm.getCommId(), null, 1);
//			
//			transBatch = TransactionBatch.add(transBatch, facade);
//			Log.debug("--- finished creating Transaction Batch");
//			
//			//BUYBNK and DEPBNK
//			Log.debug("--- Creating Instructions");
//			
//			Log.debug("--- instr 1");
//			
//			Instruction instr = 
//				new Instruction
//				 (comm.getCommId(), 
//						 InstructionType.getIdCache().getCachedInstance(InstructionType.Ids.DICONDIN), 
//						 InstructionStatus.getCache().getCachedInstance(InstructionStatus.StatusID.PENDING_MATCH), 
//						 null, false, userName, facade);
//			instr.setProcessDate(DateUtils.roundToBeginningOfDay(DateUtils.getNow()));
//			instr.setOriginalProcessDate(DateUtils.roundToBeginningOfDay(DateUtils.getNow()));
//			
//			instr = Instruction.add(instr, userName, facade);
//					
//			
//			Vector<InstructionDataApplied> dataVec = 
//				genDummyInstrData(instr, "1000.00", "2000000","12333", "NarrDiCondin"+instr.getInstructionId(), "BAC", transBatch.getTransactionDate());
//			InstructionDataApplied.addOrUpdateAll(dataVec, facade, userName);
//			
//			Log.debug("--- instr 2");
//			
//			instr = 
//				new Instruction
//				 (comm.getCommId(), 
//						 InstructionType.getIdCache().getCachedInstance(InstructionType.Ids.DICONDIN), 
//						 InstructionStatus.getCache().getCachedInstance(InstructionStatus.StatusID.PENDING_MATCH), 
//						 null, false, userName, facade);
//			instr.setProcessDate(DateUtils.roundToBeginningOfDay(DateUtils.getNow()));
//			instr.setOriginalProcessDate(DateUtils.roundToBeginningOfDay(DateUtils.getNow()));
//			
//			instr = Instruction.add(instr, userName, facade);
//			
//			dataVec = genDummyInstrData(instr, "2000.00", "2000000","12333", "NarrDiCondin"+instr.getInstructionId(), "BAC", transBatch.getTransactionDate());
//			InstructionDataApplied.addOrUpdateAll(dataVec, facade, userName);
//			
//			Log.debug("--- instr 3");
//			
//			instr = 
//				new Instruction
//				 (comm.getCommId(), 
//						 InstructionType.getIdCache().getCachedInstance(InstructionType.Ids.BUYBNK), 
//						 InstructionStatus.getCache().getCachedInstance(InstructionStatus.StatusID.READY_FOR_SPP_RELEASE), 
//						 null, false, userName, facade);
//			instr.setProcessDate(DateUtils.roundToBeginningOfDay(DateUtils.getNow()));
//			instr.setOriginalProcessDate(DateUtils.roundToBeginningOfDay(DateUtils.getNow()));
//			
//			instr = Instruction.add(instr, userName, facade);
//			dataVec = genDummyInstrData(instr, "2000.00", "2000000","12333", "NarrBuyBnk"+instr.getInstructionId(), "BAC", transBatch.getTransactionDate());
//			InstructionDataApplied.addOrUpdateAll(dataVec, facade, userName);
//
//			Log.debug("--- instr 4");
//			
//			instr = 
//				new Instruction
//				 (comm.getCommId(), 
//						 InstructionType.getIdCache().getCachedInstance(InstructionType.Ids.DEPBNK), 
//						 InstructionStatus.getCache().getCachedInstance(InstructionStatus.StatusID.READY_FOR_SPP_RELEASE), 
//						 null, false, userName, facade);
//			instr.setProcessDate(DateUtils.roundToBeginningOfDay(DateUtils.getNow()));
//			instr.setOriginalProcessDate(DateUtils.roundToBeginningOfDay(DateUtils.getNow()));
//			
//			instr = Instruction.add(instr, userName, facade);
//			dataVec = genDummyInstrData(instr, "2000.00", "2000000","12333", "NarrDepBnk"+instr.getInstructionId(), "BAC", transBatch.getTransactionDate());
//			InstructionDataApplied.addOrUpdateAll(dataVec, facade, userName);
//			
//			Log.debug("--- finished generateDummyTransactions");
//			
//			
//		} catch (Exception e) {
//			Log.error(e.getMessage(), e);
//			throw new DataException("Cannot create dummy transactions", e);
//		}
//		return transBatch;
//	}		
	
	/**
	 * Generates dummy instruction data
	 * @param instr
	 * @param amount
	 * @param orgId
	 * @param sourceAccId
	 * @param narrative
	 * @param bankTransType
	 * @param transactionDate
	 * @return
	 * @throws DataException
	 */
	private Vector<InstructionDataApplied> genDummyInstrData(
			Instruction instr, String amount, String orgId, String sourceAccId, 
			String narrative, String bankTransType, Date transactionDate) throws DataException 
	{
		InstructionType instrType = instr.getType();
		
		// Fetch the applicable fields for this Instruction Type
		Vector<ApplicableInstructionData> applicableFields = 
		 instrType.getApplicableInstructionData();
		
		
		Vector<InstructionDataApplied> instrDataApplied = 
			 new Vector<InstructionDataApplied>();

		for (ApplicableInstructionData applicableField: applicableFields) {
			
			InstructionDataFieldValue fieldValue = null;
			
			switch (applicableField.getInstructionData().getInstructionDataId()) {
			
			case InstructionData.Fields.SOURCE_ACCOUNT_ID:
				fieldValue = new InstructionDataFieldValue(applicableField.getInstructionData().getDataType());
				fieldValue.setValue(sourceAccId);
				break;
			case InstructionData.Fields.ORGANISATION_ID:
				fieldValue = new InstructionDataFieldValue(applicableField.getInstructionData().getDataType());
				fieldValue.setValue(orgId);
				break;
			case InstructionData.Fields.DOC_DATE:
				fieldValue = new InstructionDataFieldValue(applicableField.getInstructionData().getDataType());
				fieldValue.setDateValue(transactionDate);
				break;
			case InstructionData.Fields.NARRATIVE:
				fieldValue = new InstructionDataFieldValue(applicableField.getInstructionData().getDataType());
				fieldValue.setStringValue(narrative);
				break;
			case InstructionData.Fields.BANK_TRANS_TYPE:
				fieldValue = new InstructionDataFieldValue(applicableField.getInstructionData().getDataType());
				fieldValue.setStringValue(bankTransType);
				break;
			case InstructionData.Fields.CASH:							
				fieldValue = new InstructionDataFieldValue(applicableField.getInstructionData().getDataType());
				fieldValue.setValue(amount);
				break;
			
			default:
				//do nothing
				break;
			}
			
			if (applicableField!=null && fieldValue!=null) {
				InstructionDataApplied instrData = new InstructionDataApplied
				(instr.getInstructionId(), applicableField, fieldValue);
				
				instrDataApplied.add(instrData);
			}
		}
		return instrDataApplied;
	}
	

	
	/**
	 * Webservice call from SPP to create a UCM Retfund instruction
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void generateRETFUNDS() throws DataException, ServiceException 
	{	
		Integer bnkcondinId = CCLAUtils.getBinderIntegerValue(m_binder, "bnkcondinId");
		
		if (bnkcondinId==null || bnkcondinId==0) 
			throw new DataException("Cannot create RETFUNDS as bnkcondinId is empty.");
		
		String userName = m_userData.m_name;
		boolean persist = CCLAUtils.getBinderBoolValue(m_binder, "persist");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			TransactionBatchUtils.generateRETFUNDSForIds(bnkcondinId, persist, userName, facade);
			facade.commitTransaction();			
		} catch (Exception e) {
			facade.rollbackTransaction();
			throw new ServiceException(e.getMessage(), e);
		}
		// Append new transactionBatchId to RedirectUrl
		String redirectUrl = m_binder.getLocal("RedirectUrl");
		
		if (!StringUtils.stringIsBlank(redirectUrl)) {
			redirectUrl += "&bnkcondinId="+bnkcondinId+"&generated=1";
			m_binder.putLocal("RedirectUrl", redirectUrl);
		}
	}
	
	
	/**
	 * Webservice call from SPP to create a UCM Retfund instruction
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void generateRETFUNDSFromList() throws DataException, ServiceException 
	{	
		String bnkcondinIdList = m_binder.getLocal("retfundsList");
		
		if (StringUtils.stringIsBlank(bnkcondinIdList)) 
			throw new DataException("Cannot create RETFUNDS as bnkcondinId is empty.");
		
		String userName = m_userData.m_name;
		boolean persist = CCLAUtils.getBinderBoolValue(m_binder, "persist");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		StringTokenizer st = new StringTokenizer(bnkcondinIdList,",");

		try {
			facade.beginTransaction();
			
			while(st.hasMoreTokens()) {					
				int bnkcondinId = Integer.parseInt(st.nextToken());
				TransactionBatchUtils.generateRETFUNDSForIds(bnkcondinId, persist, userName, facade);
			}
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			throw new ServiceException(e.getMessage(), e);
		}
	
		// Append new transactionBatchId to RedirectUrl
		String redirectUrl = m_binder.getLocal("RedirectUrl");
		
		if (!StringUtils.stringIsBlank(redirectUrl)) {
			redirectUrl += "&generatedRetfund=1";
			m_binder.putLocal("RedirectUrl", redirectUrl);
		}
	}
	
	
	
	
	
	
	/**
	 * Performs IAT Sweep
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void performIATSweep() throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		String fundCode = m_binder.getLocal(SharedCols.FUND);
//		if (StringUtils.stringIsBlank(fundCode)) {
//			throw new DataException("Cannot perform IAT Sweep, Fund code is blank");
//		}
		
		boolean persist = CCLAUtils.getBinderBoolValue(m_binder, "persist");
		boolean ignoreIncompleteInstrError = CCLAUtils.getBinderBoolValue(m_binder, "ignoreIncompleteInstrError");
		
		String userName = m_userData.m_name;
		try {
			facade.beginTransaction();
			
			DataResultSet rsMatchedTransactions = null;
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryParamToBinder(binder, "DEST_FUND_CODE", fundCode);			
			rsMatchedTransactions = facade.createResultSet("qCore_GetInstructionsPendingIATSweep", binder);
			
			if (rsMatchedTransactions!=null && !rsMatchedTransactions.isEmpty()) {
				InstructionData iatIdData = InstructionData.getCache().getCachedInstance(InstructionData.Fields.IAT_ID);
				InstructionDataFieldValue fieldValue;

				//Generate the retfund instruction 
				Instruction iatInstr = TransactionBatchUtils.generateIATInstruction(persist, userName, facade);
				
				Float totalAmount = 0F;
				Integer bankAccountId = null;

				do {
					Float cashAmount = CCLAUtils.getResultSetFloatValue(rsMatchedTransactions, "CASH_AMOUNT");
					Integer instrTypeId = CCLAUtils.getResultSetIntegerValue(rsMatchedTransactions, "INSTRUCTION_TYPE_ID");
					String destFundCode = rsMatchedTransactions.getStringValueByName("DEST_FUND_CODE");
					Integer destBankAccountId = CCLAUtils.getResultSetIntegerValue(rsMatchedTransactions, "DEST_BANK_ACCOUNT_ID");
					Integer instrId = CCLAUtils.getResultSetIntegerValue(rsMatchedTransactions, SharedCols.INSTRUCTION);
					
					if (debug)
						Log.debug("CashAmount:"+(cashAmount!=null?cashAmount:"null")+
								", DestFundCode:"+(destFundCode!=null?destFundCode:"null")+
								", DestBnkAccId:"+(destBankAccountId!=null?destBankAccountId:"null")
						);
					
					Instruction instr = Instruction.get(instrId, facade);
					if (instr==null)
						throw new DataException("Cannot find instruction with id:"+instrId);
					
					if (instrId==null ) {
						throw new ServiceException("Results contain an null instruction id");
					}
					
					if (destBankAccountId!=null && cashAmount!=null && destFundCode!=null) {
						
						if (!destFundCode.equalsIgnoreCase(fundCode)) {
							throw new DataException("Instruction "+instrId+" contains different fundCode, expecting:"+fundCode+", actual:"+destFundCode);	
						} 
						
						if (bankAccountId==null)
							bankAccountId = destBankAccountId; 
						
						totalAmount+=cashAmount;
						 
					} else {
						if (ignoreIncompleteInstrError) {
							Log.warn("IATSweep, **skipping** Either destBankAccountId, cashAmount or destFundCode are null for instrId:"+instrId);
							continue;
						} else {
							throw new DataException("Either destBankAccountId, cashAmount or destFundCode are null for instrId:"+instrId);
						}
					}
	
					//Set the IAT instruction reference
					InstructionDataApplied dataApplied  = InstructionDataApplied.getDataApplied(
																instr.getInstructionId(), 
																InstructionData.Fields.IAT_ID, 
																facade);
					
					if (dataApplied==null) {
						ApplicableInstructionData applicableData = 
							ApplicableInstructionData.getApplicableInstructionDataByName(
									instrTypeId, 
									iatIdData.getName(),
									facade);
						fieldValue = new InstructionDataFieldValue(iatIdData.getDataType());
						fieldValue.setIntValue(iatInstr.getInstructionId());
						
						dataApplied = new InstructionDataApplied(instrId, applicableData, fieldValue);
					} else {
						fieldValue = dataApplied.getDataFieldValue();
						fieldValue.setIntValue(iatInstr.getInstructionId());
						dataApplied.setDataFieldValue(fieldValue);
					}
					
					if (persist) {
						InstructionDataApplied.addOrUpdate(dataApplied, facade, userName);
					}
				} while (rsMatchedTransactions.next());
			
				//Update IAT instruction data
				Map<Integer, String> dataMap = new HashMap<Integer, String>();
				dataMap.put(InstructionData.Fields.SOURCE_BANK_ACCOUNT_ID, bankAccountId.toString());
				dataMap.put(InstructionData.Fields.CASH, totalAmount.toString());
				dataMap.put(InstructionData.Fields.FUND_CODE, fundCode);
				
				Vector<InstructionDataApplied> appliedVec = 
					TransactionBatchTranslator.generateInstructionDataFromMap(iatInstr, dataMap, facade);	
				
				if (persist)
					InstructionDataApplied.addOrUpdateAll(appliedVec, facade, userName);
				
			} else {
				//TODO
				//Display a message or something indicating that there is no instructions.... 
			}
			
			facade.commitTransaction();

		} catch (Exception e) {
			Log.error("Error Performing IAT Sweep :"+e.getMessage(), e);
			facade.rollbackTransaction();
			throw new ServiceException("Error Performing IAT Sweep :"+e.getMessage(),e);
		}
	}

	
	/**
	 * Get the iat sweep info.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getIATSweepInfo() throws DataException, ServiceException 
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Integer iatId = CCLAUtils.getBinderIntegerValue(m_binder, "IAT_ID");
		
		if (iatId==null)
			throw new DataException("Cannot get IAT info, IAT ID is null");
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "IAT_ID", iatId);
		
		DataResultSet rsInstructions = facade.createResultSet("qCore_GetInstructionsForIATSweepId", binder);
		m_binder.addResultSet("rsInstructions", rsInstructions);
		
		DataResultSet rsIATInstruction = facade.createResultSet("qCore_GetIATInstruction", binder);
		m_binder.addResultSet("rsIATInstruction", rsIATInstruction);
		
	}
	
	/**
	 * Landing page information
	 * @throws DataExcpetion
	 * @throws ServiceExcpetion
	 */
	public void doIATSweepLanding() throws DataException, ServiceException 
	{		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataBinder binder = new DataBinder();
		//DataResultSet rsFunds = facade.createResultSet("qClientServices_GetFunds", binder);
		DataResultSet rsPreviousIATSweep = facade.createResultSet("qCore_GetAllIATSweep", binder);
		DataResultSet rsPossibleIATSweep = facade.createResultSet("qCore_PossibleIATSweep", binder);
		
		
		//m_binder.addResultSet("rsFunds", rsFunds);
		m_binder.addResultSet("rsPreviousIATSweep", rsPreviousIATSweep);
		m_binder.addResultSet("rsPossibleIATSweep", rsPossibleIATSweep);
	}


	/**
	 * Private method to get child matching preadvices.
	 * @param rsPendingPreadvice
	 * @return
	 * @throws DataException
	 */
	private static DataResultSet getChildPreadvices(DataResultSet rsPendingPreadvice) 
	throws DataException{
		DataResultSet rsChildPreadvices = new DataResultSet(new String[] { SharedCols.COMM});
		Vector<Integer> vec = new Vector<Integer>();
		//Map<Integer,Float> amountMap = new HashMap<Integer, Float>();
		Integer previousCommId = null;
		
		if (rsPendingPreadvice!=null && !rsPendingPreadvice.isEmpty()) {
			do {
				Integer currCommId = CCLAUtils.getResultSetIntegerValue(rsPendingPreadvice, SharedCols.COMM);
				if (currCommId!=null) {
					if (previousCommId==null) {
						previousCommId = currCommId;
					} else {
						if (previousCommId.intValue()==currCommId.intValue()) {
							if (!vec.contains(currCommId)) {
								vec.add(currCommId);
							}
						} else {
							previousCommId = currCommId;
						}
					}
				}
			} while (rsPendingPreadvice.next());
			
			//Populate the rsChildPreadvices
			for (Integer commId: vec) {
				Vector<String> commIdVec = new Vector<String>();
				commIdVec.add(commId.toString());
				rsChildPreadvices.addRow(commIdVec);
			}
			
			//reset to the first entry.
			rsPendingPreadvice.first();
		}
		return rsChildPreadvices;
	}	
}