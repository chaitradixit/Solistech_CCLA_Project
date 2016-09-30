package com.ecs.ucm.ccla.commproc;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.csv.CSVParser;


import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.commproc.translation.AccountIdFieldHandler;
import com.ecs.ucm.ccla.commproc.translation.OrganisationIdFieldHandler;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Fund;
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
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class TransactionBatchTranslator {
	
	// Flag to enable extra logging 
	private static final boolean debug = 
		SharedObjects.getEnvValueAsBoolean("CCLA_CommProc_EnableDebugOnTransactionBatch", false);
	
	// Flag to enable extra logging 
	private static final boolean CHECK_PREVIOUS_DAY_CLOSING_BLANCE = 
		SharedObjects.getEnvValueAsBoolean("CCLA_CommProc_CheckPrevDayClosingBalOnTransBatch", false);
	
	
	// Columns in xls that are currency amounts, i.e. will be formatted to 2dp
	private static final List<Integer> currenyColsList = 
		 CCLAUtils.getIntegerList(SharedObjects.getEnvironmentValue("CCLA_CommProc_TransactionBatchAmountCols"));  
	
	//Error Type codes.
	public static final class ErrorType {
		public static final int NONE							= 0;
		public static final int GENERAL_ERROR 					= 1;
		public static final int INSTRUCTION_GENERATION_ERROR 	= 2;
		public static final int AMOUNT_MISMATCH_ERROR			= 3;	
	}
	
	//class csv fields
	public static final class CsvFieldName {
		public static final String TIMESTAMP = "TIMESTAMP";
		public static final String DEALING_DATE = "DEALING_DATE";
		public static final String SORT_CODE = "SORT_CODE";
		public static final String BANK_ACCOUNT_NUMBER = "BANK_ACCOUNT_NUMBER";
		public static final String FUND_CODE = "FUND_CODE";
		public static final String FUND_SEQ = "FUND_SEQ";
		public static final String OPENING_ACCOUNT_BALANCE = "OPENING_ACCOUNT_BALANCE";
		public static final String CLOSING_ACCOUNT_BALANCE = "CLOSING_ACCOUNT_BALANCE";

		public static final String INSTRUCTION_TYPE = "INSTRUCTION_TYPE";
		public static final String AMOUNT = "AMOUNT";
		public static final String NARRATIVE = "NARRATIVE";
		public static final String TRANSACTION_TYPE = "TRANSACTION_TYPE";
		public static final String CLIENT_NUMBER = "CLIENT_NUMBER";
		public static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
	}
	
	private FWFacade facade;
	private boolean persist;
	private int docId;
	private String userName;
	
	private int errorType = ErrorType.NONE;
	private TransactionBatch transBatch;
	private List<TransBatchError> errorList = new ArrayList<TransBatchError>(); 
	
	
	/**
	 * 
	 * @param docId
	 * @param persist
	 * @param userName
	 * @param facade
	 */
	public TransactionBatchTranslator(int docId, boolean persist, 
			String userName, FWFacade facade) 
	{
		this.docId = docId;
		this.persist = persist;
		this.facade = facade;
		this.userName = userName;
	}
	
	
	/**
	 * Gets the error type
	 * @return
	 */
	public int getErrorType() {
		return this.errorType;
	}
	
	
	/**
	 * Gets the error list.
	 * @return
	 */
	public List<TransBatchError> getErrorList() {
		return errorList;
	}
	
	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public TransactionBatch generateTransactionBatchAndInstructions() throws DataException 
	{
		String docGuid = null;
		try{
			docGuid = CCLAUtils.getDocGuidFromDid(docId);
        } catch (Exception e) {
        	String errMsg = "Unable to getDocGuidFromDid() for docId: " + docId + ", " + e.getMessage();
        	Log.error(errMsg);
        	throw new DataException(errMsg, e);
        }	
        
		DataResultSet commRs = Comm.getDataByDocGuid(docGuid, facade);
		if (commRs!=null && !commRs.isEmpty())
			throw new DataException("Not generating Comm already exist for document "+docId);	
		
		CommGroup commGroup = null;
		Comm comm = null;
		CommSource commSource = null;
		Vector<Instruction> instructions = null;
		Map<String,String> headerMap = new HashMap<String, String>();
		Vector<Map<String,String>> instructionVec = new Vector<Map<String,String>>();

		try
        {
			//1. get variables from the contentItem
			LWDocument contentItem = new LWDocument(docId);
			ByteArrayOutputStream outStream = contentItem.getLatestContent();
			byte[] bytes = outStream.toByteArray();
			
			//2. Create CommGroup
			if (persist) {
				commGroup = CommGroup.add(null, null, facade, userName);
			} else {
				commGroup = new CommGroup(); // create temp CommGroup instead
			}
			
			//3. Create Comm
			commSource = CommSource.getByDocumentSource(
							contentItem.getAttribute(Globals.UCMFieldNames.Source));					
			
			if (commSource==null)
				commSource = CommSource.BANK;
			
			if (persist) {
				comm = Comm.add(commSource, CommType.TRANSACTION_BATCH, 
					null, null, DateUtils.getNow(), userName, docGuid, 
					null, commGroup.getCommGroupId(), facade, userName);
			} else {
				comm = new Comm(0, commSource, CommType.TRANSACTION_BATCH, 
						 Comm.DEFAULT_COMM_STATUS, null, 
						 null, null, userName, docGuid, 
						 null, commGroup.getCommGroupId());
			}
			
			//4. Create TransactionBatch & Instructions
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			InputStreamReader ir = new InputStreamReader(bais);
			CSVParser parser = new CSVParser(ir);
			String[][] values = parser.getAllValues();

			if (debug) {
				for (Integer a: currenyColsList) {
					Log.debug("CurrencyList Value is :"+ a);
				}
			}
			
			for (int i=1; i<values.length; i++){
				
				Map<String,String> instructionMap = new HashMap<String, String>();
				
			    for (int j=0; j<values[i].length; j++){    	

		    		String mapName = (values[0][j]).trim();
		    		String mapValue = (values[i][j]).trim();
		    		
		    		if (currenyColsList.contains(j)) {
		    			mapValue = CCLAUtils.convertToDecimal(mapValue);
		    			
			    		if (debug)
			    			Log.debug("Converting currency amount column from "+(values[i][j]).trim()+" to "+mapValue);
		    		}
		    		
			    	//Transaction Batch Header
			    	if (j<=7 && i==1) {
			    		headerMap.put(mapName, mapValue);
		    		}
			    	
		    		//Instructions
			    	if (j>7) {
			    		instructionMap.put(mapName, mapValue);
		    		}
		    	}
			    
			    if (!instructionMap.isEmpty())
			    	instructionVec.add(instructionMap);
			}
			
			transBatch = generateTransactionBatch(comm, headerMap);
			
			// if enabled in the config.
			//check opening balance is equals to the previous closing balance.
			if (CHECK_PREVIOUS_DAY_CLOSING_BLANCE) {
				if (!TransactionBatchUtils.validatePrevDayClosingBalanceMatch(transBatch, facade)) {	
					
					String msg = "Opening balance doesn't equal previous day's closing balance. Please check file."; 
					Log.error(msg);
					throw new DataException(msg);
				}
			}
			if (persist)
				TransactionBatch.add(transBatch, facade);

        } catch (Exception e) {
        	this.errorType = ErrorType.GENERAL_ERROR;
            //Log.error("Error generating TransactionBatch and Instruction :"+e.getMessage());
            throw new DataException("Error generating TransactionBatch and Instruction :"+e.getMessage(), e);
        }						

		//5. Create Instructions and Instruction Data
		try {
			instructions = generateInstructions(commGroup, comm, transBatch, instructionVec, headerMap);
			
			for (Instruction instr: instructions) 
			{
				Vector<InstructionDataApplied> allDataApplied = instr.getDataApplied();
				
				if (persist) {					
					Instruction.add(instr, userName, facade);
					InstructionDataApplied.addOrUpdateAll(allDataApplied, facade, userName);
				} else {
					instr.validate(facade);
					for (InstructionDataApplied dataApplied: allDataApplied) {
						dataApplied.validate(facade);
					}
				}
			}
		} catch (DataException de) {
			this.errorType = ErrorType.INSTRUCTION_GENERATION_ERROR;
			throw de;
		}

		//6. Verify the amounts
		boolean isValidAmount = false;
		BigDecimal expectedAmount = new BigDecimal("0");
		try {
			expectedAmount = 
				TransactionBatchUtils.calculateDifferenceInClosingBalance(
						transBatch, 
						facade);
			
			isValidAmount = 
				TransactionBatchUtils.checkTransactionBatchInstructions(
						instructions, 
						expectedAmount, 
						facade);
			
		} catch (DataException de) {
			this.errorType = ErrorType.GENERAL_ERROR;
			throw de;
		}
		
		if (!isValidAmount) {
			this.errorType = ErrorType.AMOUNT_MISMATCH_ERROR;
			throw new DataException("TransactionBatch Instruction Amounts invalid, cannot get "+expectedAmount+" from instructions in csv file");
		}
		
        return transBatch;
	}
	
	/**
	 * Generate Instructions with InstructionData 
	 * @return
	 */
	private Vector<Instruction> generateInstructions(CommGroup commGroup, Comm comm, 
	TransactionBatch transBatch, Vector<Map<String,String>> instructionVec, Map<String,String> header) throws DataException 
	{
		
		boolean hasError = false;
		
		Vector<Instruction> instrVec = new Vector<Instruction>();
		int count = 1;
		Instruction instr = null;
		
		for (Map<String,String> dataMap: instructionVec) {
			try {
				instr = generateInstruction(comm, dataMap);
    		
				if (instr!=null) {
					Vector<InstructionDataApplied> dataApplied = 
						generateInstructionDataFromCSV(instr, dataMap, header, facade);
					instr.setDataApplied(dataApplied);
    		
					instrVec.add(instr);
				}
				count++;
			} catch (Exception e) {
				Log.error("Error in line "+count+", "+e.getMessage());
				
				//if in persist mode throw the error straight away
				if (persist)
					throw new DataException("Error in line "+count+", "+e.getMessage() ,e);
				else {
					hasError = true;
					TransBatchError error = new TransBatchError();
					error.setLineNumber(count);
					error.setErrorMessage(e.getMessage());
					
					if (instr!=null && instr.getType()!=null)
						error.setType(instr.getType().getName());
					
					errorList.add(error);
				}
	        }
		}
		
		if (hasError) {
			throw new DataException("Error generating instructions.");
		}
		return instrVec;
	}
	
	/**
	 * Generate instruction
	 * @param comm
	 * @param data
	 * @param header
	 * @return
	 * @throws DataException
	 */
	private Instruction generateInstruction(Comm comm, Map<String,String> dataMap) throws DataException {
		
		String docClass = dataMap.get(CsvFieldName.INSTRUCTION_TYPE);
		InstructionType instrType = InstructionType.getNameCache().getCachedInstance(docClass);
		
		if (instrType==null) {
			throw new DataException("Cannot find Instruction Type for docClass:"+docClass);
		}
		
		InstructionStatus instrStatus = null;
		
		if (instrType.getInstructionTypeId()==InstructionType.Ids.DICONDIN)
			instrStatus = InstructionStatus.getCache().getCachedInstance(InstructionStatus.StatusID.PENDING_MATCH);
		else
			instrStatus = InstructionStatus.getCache().getCachedInstance(InstructionStatus.StatusID.READY_FOR_SPP_RELEASE);
		
		// Create new instruction, but don't add it to the DB yet.
		Instruction instr = new Instruction
		 (comm.getCommId(), instrType, instrStatus, null, persist, userName, facade);
		
		instr.setProcessDate(DateUtils.getNow());
		instr.setOriginalProcessDate(DateUtils.getNow());
		
		try {
			instr.validate(facade);
		} catch (DataException e) {
			throw new DataException("Failed to generate Instruction: " + e.getMessage());
		}
		
		return instr;
	}

	
	private TransactionBatch generateTransactionBatch(Comm comm, Map<String,String> headerMap) 
	throws DataException 
	{
		//String fundCode = headerMap.get(CsvFieldName.FUND_CODE);
		
		String bnkAccNo = headerMap.get(CsvFieldName.BANK_ACCOUNT_NUMBER);
		String sortCode = headerMap.get(CsvFieldName.SORT_CODE);
		String buildingSocietyNo = null;
		
		BankAccount bankAcc = BankAccount.getByValuesIncludingNull(bnkAccNo, sortCode, buildingSocietyNo, facade);
		
		if (bankAcc==null) {
			Log.error("Cannot find BankAccount with BankAccNo:"+
					bnkAccNo+", SortCode:"+sortCode+", BuildingSocietyNo:NULL");
			throw new DataException("Cannot find BankAccount with BankAccNo:"+
					bnkAccNo+", SortCode:"+sortCode+", BuildingSocietyNo:NULL");			

		}
		
		Date transactionDate = DateUtils.parseddsMMsyyyy(headerMap.get(CsvFieldName.DEALING_DATE));
		
		if (transactionDate==null)
			throw new DataException("Transaction Date is null");
		
		BigDecimal bdOpening = null;
		BigDecimal bdClosing = null; 
		int sequence = 0;	
		
		try {
			bdOpening = new BigDecimal(headerMap.get(CsvFieldName.OPENING_ACCOUNT_BALANCE));
			bdClosing = new BigDecimal(headerMap.get(CsvFieldName.CLOSING_ACCOUNT_BALANCE));
			
			if (debug) {
				Log.debug("openingBalance from csv:"+headerMap.get(CsvFieldName.OPENING_ACCOUNT_BALANCE));
				Log.debug("closingBalance from csv:"+headerMap.get(CsvFieldName.CLOSING_ACCOUNT_BALANCE));
				
			}
			
			sequence = Integer.parseInt(headerMap.get(CsvFieldName.FUND_SEQ));
		} catch (NumberFormatException nfe) {
			Log.error("Cannot parse opening/closing or sequence "+nfe.getMessage());
			throw new DataException("Cannot parse opening/closing or sequence "+nfe.getMessage(),nfe);
		}
		
		if (debug) {
			Log.debug("Generating TransactionBatch with OpeningBal:"+bdOpening.toPlainString()+", ClosingBal:"+bdClosing.toPlainString()+
					", Seq:"+sequence+", BankAccountNo:"+bnkAccNo+", sortCode:"+sortCode+", TransactionDate:"+
					transactionDate);
		}
		
		return new TransactionBatch(0, bankAcc.getBankAccountId(), transactionDate,
				DateUtils.getNow(), bdOpening, bdClosing, 
				TransactionBatchStatus.Statuses.PENDING_STATUS, userName,
				comm.getCommId(), null, sequence);
		
	}
	
	
	public static Vector<InstructionDataApplied> generateInstructionDataFromCSV(Instruction instr, Map<String,String> dataMap, Map<String,String> headerMap, FWFacade facade) 
	throws DataException, ServiceException 
	{
		InstructionType instrType = instr.getType();
		
		// Fetch the applicable fields for this Instruction Type
		Vector<ApplicableInstructionData> applicableFields = 
		 instrType.getApplicableInstructionData();
		
		// Step through each applicable Instruction Data field for this Instruction
		// Type and gather up field values.
		Vector<InstructionDataApplied> instrDataAppliedVec = 
		 new Vector<InstructionDataApplied>();
		
		
		if (applicableFields == null || applicableFields.isEmpty()) {
			// No applicable instruction data fields for this instruction type.
			String msg = "Instruction type " + instrType.getName() +
			 " has no applicable data fields set.";
			
			throw new DataException(msg);
		}
		
		
		for (ApplicableInstructionData applicableField : applicableFields) 
		{
			InstructionDataFieldValue fieldValue = null;
			String clientNumStr = null;
			String fundCode = null;
			InstructionData instrData = applicableField.getInstructionData();
			BankAccount bankAccount = null;
			
			fieldValue = new InstructionDataFieldValue(instrData.getDataType());
			
			switch (instrData.getInstructionDataId()) {
				case InstructionData.Fields.DEST_FUND_CODE:
					fieldValue.setStringValue(headerMap.get(CsvFieldName.FUND_CODE));
					break;
				case InstructionData.Fields.DEST_BANK_ACCOUNT_NUMBER:
					String destBnkAccNum = headerMap.get(CsvFieldName.BANK_ACCOUNT_NUMBER);
					if (!StringUtils.stringIsBlank(destBnkAccNum)) {
						fieldValue.setStringValue(destBnkAccNum);
					}						
					break;
				case InstructionData.Fields.DEST_BANK_ACCOUNT_ID:
					String destBA = headerMap.get(CsvFieldName.BANK_ACCOUNT_NUMBER);
					String destSC = headerMap.get(CsvFieldName.SORT_CODE);
					if (!StringUtils.stringIsBlank(destBA) 
							&& !StringUtils.stringIsBlank(destSC)) {
						try {
							bankAccount = BankAccount.getByValuesIncludingNull(destBA, destSC, null, facade);
							if (bankAccount!=null) {
								fieldValue.setIntValue(bankAccount.getBankAccountId());
							} else {
								Log.error("Cannot find bankaccount with bankAccNum:"+destBA+", sortCode:"+destSC);								
							}
						} catch (DataException de) {
							Log.error("Cannot find bankaccount "+de.getMessage(),de);
						}
					}						
					break;				
				case InstructionData.Fields.DEST_SORT_CODE:
					String destSortCode = headerMap.get(CsvFieldName.SORT_CODE);
					if (!StringUtils.stringIsBlank(destSortCode)) {
						fieldValue.setStringValue(destSortCode);
					}						
					break;
				case InstructionData.Fields.FUND_CODE:
					if (!StringUtils.stringIsBlank(dataMap.get(CsvFieldName.FUND_CODE)))
						fieldValue.setStringValue(dataMap.get(CsvFieldName.FUND_CODE));
					break;
				case InstructionData.Fields.NARRATIVE:
					fieldValue.setStringValue(dataMap.get(CsvFieldName.NARRATIVE));
					break;	
				case InstructionData.Fields.BANK_TRANS_TYPE:
					fieldValue.setStringValue(dataMap.get(CsvFieldName.TRANSACTION_TYPE));
					break;	
				case InstructionData.Fields.DOC_DATE:
					fieldValue.setDateValue(DateUtils.parseddsMMsyyyy(headerMap.get(CsvFieldName.DEALING_DATE)));
					break;	
					
				case InstructionData.Fields.SOURCE_ACCOUNT_ID:
					clientNumStr = dataMap.get(CsvFieldName.CLIENT_NUMBER);
					String accountNumStr = dataMap.get(CsvFieldName.ACCOUNT_NUMBER);	
					fundCode = dataMap.get(CsvFieldName.FUND_CODE);
					
					if (!StringUtils.stringIsBlank(clientNumStr) && !StringUtils.stringIsBlank(accountNumStr) &&
							!StringUtils.stringIsBlank(fundCode)) {
						try {
							fieldValue = AccountIdFieldHandler.getAccountId(clientNumStr, accountNumStr+fundCode, facade);
						} catch (DataException de) {
							Log.error(de.getMessage(), de);
						}
					}
					break;	
				case InstructionData.Fields.SOURCE_BANK_ACCOUNT_NUMBER:
					String sourceBnkAccNum = dataMap.get(CsvFieldName.BANK_ACCOUNT_NUMBER);
					if (!StringUtils.stringIsBlank(sourceBnkAccNum)) {
						fieldValue.setStringValue(sourceBnkAccNum);
					}						
					break;
				case InstructionData.Fields.SOURCE_SORT_CODE:
					String sourceSortCode = dataMap.get(CsvFieldName.SORT_CODE);
					if (!StringUtils.stringIsBlank(sourceSortCode)) {
						fieldValue.setStringValue(sourceSortCode);
					}						
					break;
				case InstructionData.Fields.SOURCE_BANK_ACCOUNT_ID:
					String sourceBA = dataMap.get(CsvFieldName.BANK_ACCOUNT_NUMBER);
					String sourceSC = dataMap.get(CsvFieldName.SORT_CODE);
					if (!StringUtils.stringIsBlank(sourceBA) 
							&& !StringUtils.stringIsBlank(sourceSC)) {
						try {
							bankAccount = BankAccount.getByValues(sourceBA, sourceSC, null, facade);
							if (bankAccount!=null) {
								fieldValue.setIntValue(bankAccount.getBankAccountId());
							} else {
								Log.error("Cannot find bankaccount with bankAccNum:"+sourceBA+", sortCode:"+sourceSC);								
							}
						} catch (DataException de) {
							Log.error("Cannot find bankaccount "+de.getMessage(),de);
						}
					}						
					break;
				case InstructionData.Fields.ORGANISATION_ID:
					clientNumStr = dataMap.get(CsvFieldName.CLIENT_NUMBER);
					fundCode = dataMap.get(CsvFieldName.FUND_CODE);
					String companyStr = null;
					Fund fund = Fund.getCache().getCachedInstance(fundCode);
					
					if (fund==null) {
						Log.warn("Cannot find fund with fundCode: "+fundCode+", not using company to find org id!");
					} else {
						companyStr = fund.getCompany().getCode();
					}	
					if (!StringUtils.stringIsBlank(clientNumStr)) {
						fieldValue =  OrganisationIdFieldHandler.getOrganisationId
						 (null, clientNumStr, companyStr, facade);					
					}
					break;										
				case InstructionData.Fields.CASH:
					BigDecimal amount = new BigDecimal("0");
					try {
						String amountStr = dataMap.get(CsvFieldName.AMOUNT);
						if (!StringUtils.stringIsBlank(amountStr)) {
							amount = new BigDecimal(amountStr);
						} 
					} catch (NumberFormatException nfe) {
						throw new DataException("Cannot convert amount from csv", nfe);
					}
					Log.debug("Setting amount to be: "+amount);
					fieldValue.setBigDecimalValue(amount);
					break;	
				
				default:
					break;	
			}
			
			if (fieldValue.isEmpty() && !applicableField.isOptional()) {
				// Required field value is missing.
				String msg = "Field '" + instrData.getLabel() + 
				 "' is required for instruction type " + instrType.getName() +
				 ". Ensure this field has a value set, or change the " +
				 "instruction type.";
				Log.error(msg);
				throw new DataException(msg);
			}

			InstructionDataApplied instrDataApplied = new InstructionDataApplied
			(instr.getInstructionId(), applicableField, fieldValue);
			
			Log.debug(instrDataApplied.toString());
		
			instrDataAppliedVec.add(instrDataApplied);
		}
		
		return instrDataAppliedVec;
	}		
	
	/**
	 * Generate instruction data from map using instruction_data_id as key
	 * @param instr
	 * @param dataMap
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static Vector<InstructionDataApplied> generateInstructionDataFromMap(Instruction instr, Map<Integer,String> dataMap, FWFacade facade) 
	throws DataException, ServiceException 
	{
		InstructionType instrType = instr.getType();
		
		// Fetch the applicable fields for this Instruction Type
		Vector<ApplicableInstructionData> applicableFields = 
		 instrType.getApplicableInstructionData();
		
		// Step through each applicable Instruction Data field for this Instruction
		// Type and gather up field values.
		Vector<InstructionDataApplied> instrDataAppliedVec = 
		 new Vector<InstructionDataApplied>();
		
		
		if (applicableFields == null || applicableFields.isEmpty()) {
			// No applicable instruction data fields for this instruction type.
			String msg = "Instruction type " + instrType.getName() +
			 " has no applicable data fields set.";
			
			throw new DataException(msg);
		}
		
		
		for (ApplicableInstructionData applicableField : applicableFields) 
		{
			InstructionDataFieldValue fieldValue = null;
			InstructionData instrData = applicableField.getInstructionData();
			fieldValue = new InstructionDataFieldValue(instrData.getDataType());
			
			fieldValue.setValue(dataMap.get(instrData.getInstructionDataId()));
			
			if (fieldValue.isEmpty() && !applicableField.isOptional()) {
				// Required field value is missing.
				String msg = "Field '" + instrData.getLabel() + 
				 "' is required for instruction type " + instrType.getName() +
				 ". Ensure this field has a value set, or change the " +
				 "instruction type.";
				Log.error(msg);
				throw new DataException(msg);
			}

			InstructionDataApplied instrDataApplied = new InstructionDataApplied
			(instr.getInstructionId(), applicableField, fieldValue);
			
			Log.debug(instrDataApplied.toString());
		
			instrDataAppliedVec.add(instrDataApplied);
		}
		
		return instrDataAppliedVec;
	}		
	

	/**
	 * Converts a dicondins to an bnkcondin
	 * @param dicondinId
	 * @param persist
	 * @param userName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Instruction convertDicondinToBnkcondin(int dicondinId, boolean persist, String userName, FWFacade facade) 
	throws DataException {
		
		Instruction pendDicondinInstr = Instruction.get(dicondinId, facade);

		if (pendDicondinInstr==null) {
			throw new DataException("Cannot find dicondin instruction with id"+dicondinId);
		}
		
		//loads the instruction data
		pendDicondinInstr.loadDataApplied(facade);
//		pendDicondinInstr.getDataApplied();
		
		Vector<InstructionDataApplied> bnkCondinDataAppliedVec = new Vector<InstructionDataApplied>(); 
		
		InstructionType bnkcondinType = 
			InstructionType.getIdCache().getCachedInstance(InstructionType.Ids.BNKCONDIN);
		
		for (ApplicableInstructionData applicableData: bnkcondinType.getApplicableInstructionData()) {
			
			InstructionData instrData = applicableData.getInstructionData();
			
			InstructionDataFieldValue fieldValue = 
				pendDicondinInstr.getDataApplied(instrData.getInstructionDataId());
			
			//legacy old instructions, 
			//field value might not have been set for optional data
			if (fieldValue==null) {
				
				DataType currDataType = instrData.getDataType();
				fieldValue = new InstructionDataFieldValue(currDataType);
				
				if (currDataType.equals(DataType.FLOAT)) {
					fieldValue.setBigDecimalValue(new BigDecimal("0"));
				} else if (currDataType.equals(DataType.INT)) {
					fieldValue.setIntValue(0);
				}
			}
			
			if (fieldValue.isEmpty() && !applicableData.isOptional()) {
				// Required field value is missing.
				String msg = "Field '" + instrData.getLabel() + 
				 "' is required for instruction type " + bnkcondinType.getName() +
				 ". Ensure this field has a value set, or change the " +
				 "instruction type.";
				Log.error(msg);
				throw new DataException(msg);
			}
			
			InstructionDataApplied instrDataApplied = new InstructionDataApplied
			(pendDicondinInstr.getInstructionId(), applicableData, fieldValue);
			
			bnkCondinDataAppliedVec.add(instrDataApplied);
		}
		
		//remove the dicondins applied data
		if (persist) {
			for (InstructionDataApplied dataApplied: pendDicondinInstr.getDataApplied())
				InstructionDataApplied.remove(dataApplied, facade, userName);
		}
		
		//set type to bnkcondins
		pendDicondinInstr.setType(bnkcondinType);
		InstructionStatus status = InstructionStatus.getCache().getCachedInstance(InstructionStatus.StatusID.READY_FOR_SPP_RELEASE);
		pendDicondinInstr.setStatus(status);
		
		if (persist)
			pendDicondinInstr.persist(facade, userName);
		
		//add bnkcondins applied data
		if (persist)
			InstructionDataApplied.addOrUpdateAll(bnkCondinDataAppliedVec, facade, userName);
		
		return pendDicondinInstr;
	}
	
	public static void main(String[] args) {

	}
	
	
	/**
	 * Class for storing error details during the instruction creation from the csv file.
	 * 
	 */
	public class TransBatchError {
		
		private int lineNumber = 0;
		private String errorMessage;
		private String type;
		
		
		public TransBatchError() { }
		
		public TransBatchError(int lineNumber, String errorMessage, String type) {
			this.lineNumber = lineNumber;
			this.errorMessage = errorMessage;
			this.type = type;
		}

		public int getLineNumber() {
			return lineNumber;
		}

		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
		
		
	}
}
