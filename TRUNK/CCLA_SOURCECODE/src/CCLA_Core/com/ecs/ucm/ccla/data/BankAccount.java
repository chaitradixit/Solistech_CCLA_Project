package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.aurora.webservice.BankDetails;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.aurora.AuroraWebServiceHandler;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class BankAccount extends Element implements Persistable {
	
	private int bankAccountId;
	
	private String accountNumber;
	private String sortCode;
	private String buildingSocietyNumber;
	
	private String accountName;
	private String shortName;
	
	private String IBAN;
	private String swiftCode;
	private String BICCode;
	
	private BankDetails bankDetails;
	
	/** Maximum character length for the Short Name field. */
	public static final int SHORT_NAME_MAX_LENGTH = 18;
	
	public static class Cols {
		public static final String ID = "BANK_ACCOUNT_ID";
		
		public static final String ACCOUNT_NO = "ACCOUNT_NO";
		public static final String SORT_CODE = "SORT_CODE";
		public static final String BUILDING_SOCIETY_ROLL_NUMBER = 
		 "BUILDING_SOCIETY_ROLL_NUMBER";
		
		public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
		public static final String ACCOUNT_SHORT_NAME = "ACCOUNT_SHORT_NAME";
		
		public static final String IBAN = "IBAN";
		public static final String SWIFT_CODE = "SWIFT_CODE";
		public static final String BIC_CODE = "BIC_CODE";
	}
	
	public BankAccount() {}
	
	public BankAccount(int bankAccountId, String accountNumber,
			String sortCode, String buildingSocietyNumber, 
			String accountName, String shortName,
			String IBAN, String swiftCode, String BICCode) {
		
		this.bankAccountId = bankAccountId;

		this.accountNumber = accountNumber;
		this.sortCode = sortCode;
		this.buildingSocietyNumber = buildingSocietyNumber;
		
		this.accountName = accountName;
		this.shortName = shortName;
		
		this.IBAN = IBAN;
		this.swiftCode = swiftCode;
		this.BICCode = BICCode;
	}
	
	public static BankAccount add(DataBinder binder, FWFacade facade, String username) 
	 throws DataException, ServiceException {
		
		BankAccount bankAccount = new BankAccount();
		bankAccount.setAttributes(binder);
		
		Element element = Element.add(ElementType.BANK_ACCOUNT, null, facade, username);
		int bankAccountId = element.getElementId();
		
		bankAccount.setBankAccountId(bankAccountId);
		bankAccount.addFieldsToBinder(binder);
		
		try {
			bankAccount.validate(facade);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		
		facade.execute("qClientServices_AddBankAccount", binder);
		
		// Add audit record
		DataResultSet newData = BankAccount.getData
		 (bankAccount.getBankAccountId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(bankAccount.getBankAccountId(), 
		ElementType.BANK_ACCOUNT.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.BANK_ACCOUNT.getName(), 
		 null, newData, auditRelations);
		
		return bankAccount;
	}
	
	public static BankAccount add(int entityId, 
	 String accountNumber, String sortCode, String buildingSocietyNumber, 
	 String accountName, String shortName,
	 String IBAN, String swiftCode, String bicCode,
	 FWFacade facade, String username) throws ServiceException, DataException {
				
		int bankAccountId = 
		 Integer.parseInt(
		 CCLAUtils.getNewKey("BankAccount", facade));
		
		BankAccount bankAccount = new BankAccount
		 (bankAccountId,
		  accountNumber, sortCode, buildingSocietyNumber, 
		  accountName, shortName,
		  IBAN, swiftCode, bicCode
		  );
		
		DataBinder binder = new DataBinder();
		bankAccount.addFieldsToBinder(binder);
		
		try {
			bankAccount.validate(facade);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		
		facade.execute("qClientServices_AddBankAccount", binder);
		
		// Add audit record
		DataResultSet newData = BankAccount.getData
		 (bankAccount.getBankAccountId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(bankAccount.getBankAccountId(), 
		ElementType.BANK_ACCOUNT.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.BANK_ACCOUNT.getName(), 
		 null, newData, auditRelations);
		
		return bankAccount;
	}
	
	public static BankAccount get(int bankAccountId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsBankAccount = getData(bankAccountId, facade);
		
		if (rsBankAccount.isEmpty()) {
			return null;
		} else {
			return get(rsBankAccount);
		}
	}
	
	/** Fetches a matching Bank Account by searching on its values.
	 *  
	 * @param accountNum
	 * @param sortCode
	 * @return null if no such bank account exists
	 * @throws DataException 
	 */
	public static BankAccount getByValues
	 (String accountNum, String sortCode, String buildingSocietyNumber,
	 FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryParamToBinder(binder, Cols.ACCOUNT_NO, accountNum);
		CCLAUtils.addQueryParamToBinder(binder, Cols.SORT_CODE, sortCode);
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.BUILDING_SOCIETY_ROLL_NUMBER, buildingSocietyNumber);
		
		DataResultSet rsBankAccount = facade.createResultSet
		 ("qClientServices_GetBankAccountByValues", binder);
		
		return get(rsBankAccount);
	}
	
	/** Fetches a matching Bank Account by searching on its values.
	 *  
	 * @param accountNum
	 * @param sortCode
	 * @return null if no such bank account exists
	 * @throws DataException 
	 */
	public static BankAccount getByValuesIncludingNull
	 (String accountNum, String sortCode, String buildingSocietyNumber,
	 FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		String query = "SELECT * FROM BANK_ACCOUNT WHERE ";
		
		if (!StringUtils.stringIsBlank(accountNum)) {
			query+=Cols.ACCOUNT_NO+"='"+accountNum+"' " ;
		} else {
			query+=Cols.ACCOUNT_NO+" IS NULL ";
		}

		query+="AND ";
		
		if (!StringUtils.stringIsBlank(sortCode)) {
			query+=Cols.SORT_CODE+"='"+sortCode+"' " ;
		} else {
			query+=Cols.SORT_CODE+" IS NULL ";
		}
		
		query+="AND ";
		
		if (!StringUtils.stringIsBlank(buildingSocietyNumber)) {
			query+=Cols.BUILDING_SOCIETY_ROLL_NUMBER+"='"+buildingSocietyNumber+"'" ;
		} else {
			query+=Cols.BUILDING_SOCIETY_ROLL_NUMBER+" IS NULL";
		}
		
		DataResultSet rsBankAccount = facade.createResultSetSQL(query);
		
		return get(rsBankAccount);
	}

	
	/** Fetch all bank accounts with the given sort code and account number.
	 * 
	 * @param accountNum
	 * @param sortCode
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<BankAccount> getAllBySortCodeAndAccountNumber
	 (String accountNum, String sortCode, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryParamToBinder(binder, Cols.ACCOUNT_NO, accountNum);
		CCLAUtils.addQueryParamToBinder(binder, Cols.SORT_CODE, sortCode);
		
		DataResultSet rsBankAccounts = facade.createResultSet
		 ("qClientServices_GetBankAccountsBySortCodeAndAccountNumber", binder);
		
		Vector<BankAccount> bankAccounts = new Vector<BankAccount>();
		
		if (rsBankAccounts.first()) {
			do {
				bankAccounts.add(get(rsBankAccounts));
			} while (rsBankAccounts.next());
		}
		
		return bankAccounts;
	}
	
	public static BankAccount get(DataResultSet rsBankAccount) 
	 throws DataException {
		
		if (rsBankAccount.isEmpty()) {
			return null;
		} else {
			
			return new BankAccount(
			 CCLAUtils.getResultSetIntegerValue
			  (rsBankAccount, Cols.ID),
			  
			 rsBankAccount.getStringValueByName(Cols.ACCOUNT_NO),
			 rsBankAccount.getStringValueByName(Cols.SORT_CODE),
			 rsBankAccount.getStringValueByName(Cols.BUILDING_SOCIETY_ROLL_NUMBER),
			 
			 rsBankAccount.getStringValueByName(Cols.ACCOUNT_NAME),
			 rsBankAccount.getStringValueByName(Cols.ACCOUNT_SHORT_NAME),

			 rsBankAccount.getStringValueByName(Cols.IBAN),
			 rsBankAccount.getStringValueByName(Cols.SWIFT_CODE),
			 rsBankAccount.getStringValueByName(Cols.BIC_CODE)
			);
		}
	}
	
	public static DataResultSet getData(int bankAccountId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "BANK_ACCOUNT_ID", bankAccountId);
		
		DataResultSet rsBankAccount = 
		 facade.createResultSet("qClientServices_GetBankAccount", binder);
		
		return rsBankAccount;
	}
	
	public void setAttributes(DataBinder binder) throws DataException {

		this.setAccountNumber(binder.getLocal(Cols.ACCOUNT_NO));
		this.setSortCode(binder.getLocal(Cols.SORT_CODE));
		this.setBuildingSocietyNumber(binder.getLocal(Cols.BUILDING_SOCIETY_ROLL_NUMBER));
		
		this.setAccountName(binder.getLocal(Cols.ACCOUNT_NAME));
		this.setShortName(binder.getLocal(Cols.ACCOUNT_SHORT_NAME));
		
		this.setIBAN(binder.getLocal(Cols.IBAN));
		this.setSwiftCode(binder.getLocal(Cols.SWIFT_CODE));
		this.setBICCode(binder.getLocal(Cols.BIC_CODE));
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getBankAccountId());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.ACCOUNT_NO, this.getAccountNumber());
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.SORT_CODE, this.getSortCode());
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.BUILDING_SOCIETY_ROLL_NUMBER, this.getBuildingSocietyNumber());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.ACCOUNT_NAME, this.getAccountName());
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.ACCOUNT_SHORT_NAME, this.getShortName());

		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.IBAN, this.getIBAN());
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.SWIFT_CODE, this.getSwiftCode());
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.BIC_CODE, this.getBICCode());
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		this.validate(facade);
		
		DataResultSet beforeData = BankAccount.getData
		 (this.getBankAccountId(), facade);
				
		facade.execute("qClientServices_UpdateBankAccount", binder);
		
		DataResultSet afterData = BankAccount.getData
		 (this.getBankAccountId(), facade);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getBankAccountId(), 
		ElementType.BANK_ACCOUNT.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.BANK_ACCOUNT.getName(), 
		 beforeData, afterData, auditRelations);
	}
	
	public void validate(FWFacade facade) throws DataException {
		
		if (StringUtils.stringIsBlank(this.getAccountNumber()))
			throw new DataException("Account Number is missing");
		
		if (StringUtils.stringIsBlank(this.getSortCode()))
			throw new DataException("Sort Code is missing");
		
		try {
			if (!AuroraWebServiceHandler.isValidBankAccount(
				this.getSortCode(), 
				this.getAccountNumber())) {
				throw new DataException("ADF Bank Finder failed to validate bank " +
				 "account details");
			}
		} catch (ServiceException se) {
			//log will have already been written out from the AuroraWebServiceHandler class.
			throw new DataException("Failed to instantiate Aurora WS "+se);
		} 
		
		if (this.getShortName() != null 
			&& 
			(this.getShortName().length() > SHORT_NAME_MAX_LENGTH)) {
			throw new DataException("Short Name is too long (exceeds " + 
			 SHORT_NAME_MAX_LENGTH + " characters)");
		}
		
		if (checkForDuplicate(this, facade))
			throw new DataException("Bank account number/sort code/building society " +
			 "reference already exists");
		
		// Ensure that the short name matches the building society number, if one was
		// specified
		if (!StringUtils.stringIsBlank(this.getBuildingSocietyNumber())
			&&
			(StringUtils.stringIsBlank(this.getShortName())
			||
			(!StringUtils.stringIsBlank(this.getShortName())
			&&
			!(this.getBuildingSocietyNumber().equals(this.getShortName()))))) {
			throw new DataException("Supplied Building Society Number (" + 
			 this.getBuildingSocietyNumber() + ") must match the Short Name");
		}
	}
	
	/** Checks if the bank account no/sort code/roll number already exists
	 *  in the table. This is also caught by a unique constraint on 
	 *  the table itself.
	 * 
	 * @param bankAccount
	 * @param facade
	 * @throws DataException 
	 */
	public static boolean checkForDuplicate
	 (BankAccount bankAccount, FWFacade facade) throws DataException {
		
		BankAccount dupeBankAccount = getByValues
		 (bankAccount.getAccountNumber(), bankAccount.getSortCode(), 
		 bankAccount.getBuildingSocietyNumber(), facade);
		
		if (dupeBankAccount == null)
			return false; // no existing bank account
		else {
			// Bank account exists, but it may be the one we are
			// comparing against.
			int bankAccountId = dupeBankAccount.getBankAccountId();
			return (bankAccountId != bankAccount.getBankAccountId());
		}
	}
	
	public int getBankAccountId() {
		return bankAccountId;
	}
	public void setBankAccountId(int bankingAccountId) {
		this.bankAccountId = bankingAccountId;
	}
	
	/** Returns the bank account number, doesn't guarantee padding to 8 characters 
	 *  though.
	 *  
	 *  Use getPaddedAccountNumber() for this.
	 *  
	 * @return
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	/** Returns the bank sort code, doesn't guarantee padding to 6 characters though.
	 *  Use getPaddedSortCode() for this.
	 *  
	 * @return
	 */
	public String getSortCode() {
		return sortCode;
	}
	
	public void setSortCode(String sortCode) {
		this.sortCode = sortCode;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getBICCode() {
		return BICCode;
	}

	public void setBICCode(String bICCode) {
		BICCode = bICCode;
	}
	
	/** If the bank details haven't been cached by a previous call to this method,
	 *  they will be fetched via Aurora web service call.
	 *  
	 *  Bear in mind this method will always return null in the test environments as
	 *  the bank lookup features are disabled (no internet access)
	 *  
	 * @return
	 * @throws ServiceException 
	 */
	public BankDetails getBankDetails() throws ServiceException {
		if (this.bankDetails != null)
			return bankDetails;
		
		BankDetails bankDetails = AuroraWebServiceHandler.getBankDetails
		 (CCLAUtils.padSortCode(this.getSortCode()));
		
		this.bankDetails = bankDetails;
		
		return bankDetails;
	}

	@Override
	public int getElementId() {
		return this.getBankAccountId();
	}
	
	@Override
	public ElementType getType() {
		return ElementType.BANK_ACCOUNT;
	}

	public void setBuildingSocietyNumber(String buildingSocietyNumber) {
		this.buildingSocietyNumber = buildingSocietyNumber;
	}

	public String getBuildingSocietyNumber() {
		return buildingSocietyNumber;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}
	
	public String toString() {
		return this.getAccountNumber() + "-" + this.getSortCode() + 
		 (this.getAccountName() != null ? " " + this.getAccountName() : "");
	}

	@Override
	public String getName() {
		return this.getSortCode() + "-" + this.getAccountNumber();
	}
}
