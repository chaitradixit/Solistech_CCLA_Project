package com.ecs.ucm.ccla.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class TransactionBatch implements Persistable {

	/** variables **/
	public static final class Cols {
		public static final String TRANS_BATCH_ID 			= "TRANSACTION_BATCH_ID";
//		public static final String FUND_CODE				= "FUND_CODE";		
//		public static final String BANK_ACCOUNT_NO			= "BANK_ACCOUNT_NO";
//		public static final String SORT_CODE				= "SORT_CODE";
		public static final String BANK_ACCOUNT_ID 			= "BANK_ACCOUNT_ID";
		
		public static final String TRANSACTION_DATE 		= "TRANSACTION_DATE";
		public static final String DATE_ADDED				= "DATE_ADDED";
		public static final String OPENING_BALANCE			= "OPENING_BALANCE";
		public static final String CLOSING_BALANCE			= "CLOSING_BALANCE";
		public static final String TRANS_BATCH_STATUS_ID 	= "TRANS_BATCH_STATUS_ID";
		public static final String COMM_ID					= "COMM_ID";
		public static final String USER_ID					= "USER_ID";
		public static final String APPLIED_DATE				= "APPLIED_DATE";
		public static final String SEQUENCE					= "TRANS_BATCH_SEQUENCE";
	}
	
	public static final String ADD_QUERY = "qCore_AddTransactionBatch";
	public static final String UPDATE_QUERY = "qCore_UpdateTransactionBatch";
	public static final String GET_ALL_QUERY = "qCore_GetAllTransactionBatch";
	public static final String GET_BY_ID_QUERY = "qCore_GetTransactionBatchById";
	public static final String GET_BY_STATUS_ID_QUERY = "qCore_GetTransactionBatchByStatusId";
	public static final String GET_BY_EXTENDED_ID_QUERY = "qCore_GetExtendedTransactionBatchById";
	public static final String GET_BY_EXTENDED_STATUS_ID_QUERY = "qCore_GetExtendedTransactionBatchByStatusId";
	public static final String GET_TRANSACTION_BATCH_BY_PARAMS_QUERY = "qCore_GetTransactionBatchByParams";
	
	/** Properties **/
	private int transactionBatchId;
//	private String fundCode;
	private Date transactionDate;
	private Date dateAdded;
	private BigDecimal openingBalance;
	private BigDecimal closingBalance;
	private int statusId;
	private String userId;
	private int commId;
	private Date appliedDate;
	private int sequence;
	private int bankAccountId;
//	private String sortCode;
//	private String bankAccountNumber;
	
	/* ********************* Constructor ************************* */
	public TransactionBatch(int transactionBatchId, int bankAccountId, Date transactionDate,
			Date dateAdded, BigDecimal openingBalance, BigDecimal closingBalance, int statusId, String userId,
			int commId, Date appliedDate, int sequence) {
		this.transactionBatchId = transactionBatchId;
//		this.bankAccountNumber = bankAccountNumber;
//		this.sortCode = sortCode;
		this.bankAccountId = bankAccountId;
		this.transactionDate = transactionDate;
		this.dateAdded = dateAdded;
		this.openingBalance = openingBalance;
		this.closingBalance = closingBalance;
		this.statusId = statusId;
		this.userId = userId;
		this.commId = commId;
		this.appliedDate = appliedDate;
		this.sequence = sequence;
	}
	
	public TransactionBatch(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	/* ************************** Methods *************************** */
	
	public int getTransactionBatchId() {
		return transactionBatchId;
	}

	public void setTransactionBatchId(int transactionBatchId) {
		this.transactionBatchId = transactionBatchId;
	}

//	public String getFundCode() {
//		return fundCode;
//	}
//
//	public void setFundCode(String fundCode) {
//		this.fundCode = fundCode;
//	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance = openingBalance;
	}

	public BigDecimal getClosingBalance() {
		return closingBalance;
	}

	public void setClosingBalance(BigDecimal closingBalance) {
		this.closingBalance = closingBalance;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCommId() {
		return commId;
	}

	public void setCommId(int commId) {
		this.commId = commId;
	}

	public Date getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(Date appliedDate) {
		this.appliedDate = appliedDate;
	}	
	
	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}	
	
	public int getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}	
//	public String getSortCode() {
//		return sortCode;
//	}
//
//	public void setSortCode(String sortCode) {
//		this.sortCode = sortCode;
//	}
//
//	public String getBankAccountNumber() {
//		return bankAccountNumber;
//	}
//
//	public void setBankAccountNumber(String bankAccountNumber) {
//		this.bankAccountNumber = bankAccountNumber;
//	}


	
	public static TransactionBatch add(DataBinder binder, FWFacade facade) 
	throws DataException {
		TransactionBatch transBatch = new TransactionBatch(binder);
		return TransactionBatch.add(transBatch, facade);
	}
	
	public static TransactionBatch add(TransactionBatch transBatch, FWFacade facade) 
	throws DataException {
		
		if (transBatch.getTransactionBatchId()==0) {
			transBatch.setTransactionBatchId(
					Integer.parseInt(
							CCLAUtils.getNewKey("TransactionBatch", facade)));
		}
		
		transBatch.validate(facade);
		DataBinder binder = new DataBinder();
		transBatch.addFieldsToBinder(binder);
		facade.execute(ADD_QUERY, binder);
		return transBatch;
	}
	
	
	public static TransactionBatch get(int transBatchId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(transBatchId, facade);
		return get(rs);
	}
	
	public static Vector<TransactionBatch> getByStatusId(int transBatchStatusId, FWFacade facade) 
	 throws DataException {
		Vector<TransactionBatch> TransactionBatchVec = new Vector<TransactionBatch>();
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.TRANS_BATCH_STATUS_ID, transBatchStatusId);
		DataResultSet rs = facade.createResultSet
		 (GET_BY_STATUS_ID_QUERY, binder);
		
		if (rs.first()) {
			do {
				TransactionBatchVec.add(get(rs));
			} while (rs.next());
		}	
		return TransactionBatchVec;
	}
	
	public static DataResultSet getData(int transBatchId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, Cols.TRANS_BATCH_ID, transBatchId);
		DataResultSet rs = facade.createResultSet
		 (GET_BY_ID_QUERY, binder);
		
		return rs;
	}	

	public static DataResultSet getStatusData(int transBatchStatusId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, Cols.TRANS_BATCH_STATUS_ID, transBatchStatusId);
		DataResultSet rs = facade.createResultSet
		 (GET_BY_STATUS_ID_QUERY, binder);
		
		return rs;
	}	
	
	public static DataResultSet getExtendedData(int transBatchId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, Cols.TRANS_BATCH_ID, transBatchId);
		DataResultSet rs = facade.createResultSet
		 (GET_BY_EXTENDED_ID_QUERY, binder);
		
		return rs;
	}	

	public static DataResultSet getExtendedStatusData(int transBatchStatusId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, Cols.TRANS_BATCH_STATUS_ID, transBatchStatusId);
		DataResultSet rs = facade.createResultSet
		 (GET_BY_EXTENDED_STATUS_ID_QUERY, binder);
		
		return rs;
	}	

	
	public static TransactionBatch get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
	
		return new TransactionBatch(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.TRANS_BATCH_ID),
//			CCLAUtils.getResultSetStringValue(rs, Cols.FUND_CODE),
//			CCLAUtils.getResultSetStringValue(rs, Cols.BANK_ACCOUNT_NO),
//			CCLAUtils.getResultSetStringValue(rs, Cols.SORT_CODE),
			
			CCLAUtils.getResultSetIntegerValue(rs, Cols.BANK_ACCOUNT_ID),
			
			rs.getDateValueByName(Cols.TRANSACTION_DATE),
			rs.getDateValueByName(Cols.DATE_ADDED),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.OPENING_BALANCE),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.CLOSING_BALANCE),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.TRANS_BATCH_STATUS_ID),
			CCLAUtils.getResultSetStringValue(rs, Cols.USER_ID),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.COMM_ID),
			rs.getDateValueByName(Cols.APPLIED_DATE),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.SEQUENCE)
			
		);
	}
	
	public static Vector<TransactionBatch> getAll(FWFacade facade) throws DataException 
	{
		Vector<TransactionBatch> TransactionBatchVec = new Vector<TransactionBatch>();
		
		DataResultSet rs = facade.createResultSet
		 (GET_ALL_QUERY, new DataBinder());
		
		if (rs.first()) {
			do {
				TransactionBatchVec.add(get(rs));
			} while (rs.next());
		}	
		return TransactionBatchVec;
	}
	
	/* *************** Persistable Interface **************** */
	
	public void setAttributes(DataBinder binder) throws DataException 
	{
		this.setTransactionBatchId(CCLAUtils.getBinderIntegerValue(binder, Cols.TRANS_BATCH_ID));
//		this.setFundCode(binder.getLocal(Cols.FUND_CODE));
//		this.setBankAccountNumber(binder.getLocal(Cols.BANK_ACCOUNT_NO));
//		this.setSortCode(binder.getLocal(Cols.SORT_CODE));
		this.setBankAccountId(CCLAUtils.getBinderIntegerValue(binder, Cols.BANK_ACCOUNT_ID));
		this.setUserId(binder.getLocal(Cols.USER_ID));
		this.setTransactionDate(CCLAUtils.getBinderDateValue(binder, Cols.TRANSACTION_DATE));
		this.setDateAdded(CCLAUtils.getBinderDateValue(binder, Cols.DATE_ADDED));
		this.setOpeningBalance(CCLAUtils.getBinderBigDecimalValue(binder, Cols.OPENING_BALANCE));
		this.setClosingBalance(CCLAUtils.getBinderBigDecimalValue(binder, Cols.CLOSING_BALANCE));
		this.setStatusId(CCLAUtils.getBinderIntegerValue(binder, Cols.TRANS_BATCH_STATUS_ID));
		this.setUserId(binder.getLocal(Cols.USER_ID));
		this.setAppliedDate(CCLAUtils.getBinderDateValue(binder, Cols.APPLIED_DATE));
		this.setCommId(CCLAUtils.getBinderIntegerValue(binder, Cols.COMM_ID));
		this.setSequence(CCLAUtils.getBinderIntegerValue(binder, Cols.SEQUENCE));
		
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException 
	{
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.TRANS_BATCH_ID, this.getTransactionBatchId());
//		CCLAUtils.addQueryParamToBinder(binder, Cols.FUND_CODE, this.getFundCode());
//		CCLAUtils.addQueryParamToBinder(binder, Cols.BANK_ACCOUNT_NO, this.getBankAccountNumber());
//		CCLAUtils.addQueryParamToBinder(binder, Cols.SORT_CODE, this.getSortCode());
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.BANK_ACCOUNT_ID, this.getBankAccountId());		
		CCLAUtils.addQueryDateParamToBinder(binder, Cols.TRANSACTION_DATE, this.getTransactionDate());
		CCLAUtils.addQueryDateParamToBinder(binder, Cols.DATE_ADDED, this.getDateAdded());
		CCLAUtils.addQueryBigDecimalParamToBinder(binder, Cols.OPENING_BALANCE, this.getOpeningBalance());
		CCLAUtils.addQueryBigDecimalParamToBinder(binder, Cols.CLOSING_BALANCE, this.getClosingBalance());
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.TRANS_BATCH_STATUS_ID, this.getStatusId());
		CCLAUtils.addQueryParamToBinder(binder, Cols.USER_ID, this.getUserId());
		CCLAUtils.addQueryDateParamToBinder(binder, Cols.APPLIED_DATE, this.getAppliedDate());
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.COMM_ID, this.getCommId());
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.SEQUENCE, this.getSequence());

	}

	public void persist(FWFacade facade, String username) throws DataException 
	{
		this.validate(facade);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);		
		facade.execute(UPDATE_QUERY, binder);	
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

}
