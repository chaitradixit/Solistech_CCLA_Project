package com.ecs.ucm.ccla.data;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** Represents an entry in the CCLA_ACCOUNT_BANKING_DETAILS table,
 *  i.e. a set of income/withdrawal banking details.
 * 
 *  Only used in conjunction with the Account Actions table - not
 *  used to store static Bank Account data. See BankAccount class
 *  for this!
 * 
 **/
public class BankingDetails {
	
	private int bankingDetailsId;
	
	private Integer incomeAccountNumber;
	private Integer incomeSortCode;
	private String incomeAccountName;
	
	private Integer withdrawalAccountNumber;
	private Integer withdrawalSortCode;
	private String withdrawalAccountName;
	
	private String mandatedCompany;
	private String mandatedAccNumExt;
	
	public BankingDetails(int bankingDetailsId, Integer incomeAccountNumber,
			Integer incomeSortCode, String incomeAccountName,
			Integer withdrawalAccountNumber, Integer withdrawalSortCode,
			String withdrawalAccountName,
			String mandatedCompany, String mandatedAccNumExt) {
		super();
		this.bankingDetailsId = bankingDetailsId;
		this.incomeAccountNumber = incomeAccountNumber;
		this.incomeSortCode = incomeSortCode;
		this.incomeAccountName = incomeAccountName;
		this.withdrawalAccountNumber = withdrawalAccountNumber;
		this.withdrawalSortCode = withdrawalSortCode;
		this.withdrawalAccountName = withdrawalAccountName;
		this.mandatedCompany = mandatedCompany;
		this.mandatedAccNumExt = mandatedAccNumExt;
	}
	
	/** Instantiates a new BankingDetails instance, using values
	 *  from the passed ResultSet. The column names are expected to
	 *  match those used in the CCLA_ACCOUNT_BANKING_DETAILS table.
	 *  
	 *  If a BANKING_DETAILS_ID value is not present, the method
	 *  returns null.
	 *  
	 * @param rsBankingDetails
	 * @return
	 * @throws DataException
	 */
	public static BankingDetails get(DataResultSet rsBankingDetails) 
	 throws DataException {
		
		Integer id = CCLAUtils.getResultSetIntegerValue
		 (rsBankingDetails, "BANKING_DETAILS_ID");
		
		if (id == null)
			return null;
		
		BankingDetails bankingDetails = new BankingDetails(
			id.intValue(),
			
			CCLAUtils.getResultSetIntegerValue
			 (rsBankingDetails, "INC_ACC_NUMBER"),
			 CCLAUtils.getResultSetIntegerValue
			 (rsBankingDetails, "INC_SORT_CODE"),
			rsBankingDetails.getStringValueByName("INC_ACC_NAME"),
			
			CCLAUtils.getResultSetIntegerValue
			 (rsBankingDetails, "WITH_ACC_NUMBER"),
			 CCLAUtils.getResultSetIntegerValue
			 (rsBankingDetails, "WITH_SORT_CODE"),
			rsBankingDetails.getStringValueByName("WITH_ACC_NAME"),
			
			rsBankingDetails.getStringValueByName("MANDATED_COMPANY"),
			rsBankingDetails.getStringValueByName("MANDATED_ACCNUMEXT")
		);
		
		return bankingDetails;
	}
	
	/** Adds a new BankingDetails instance, used values specified in the
	 *  passed DataBinder.
	 *  
	 * @param binder
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static BankingDetails add(DataBinder binder, FWFacade facade) 
	 throws DataException {
		
		return add(
		 CCLAUtils.getBinderIntegerValue(binder, "incAccNumber"),
		 CCLAUtils.getBinderIntegerValue(binder, "incSortCode"),
		 binder.getLocal("incAccName"),
		 CCLAUtils.getBinderIntegerValue(binder, "withAccNumber"),
		 CCLAUtils.getBinderIntegerValue(binder, "withSortCode"),
		 binder.getLocal("withAccName"),
		 binder.getLocal("mandatedCompany"),
		 binder.getLocal("mandatedAccNumExt"),
		 facade
		);
		
	}
	
	/** Adds a new BankingDetails instance.
	 *  
	 * @param binder
	 * @param facade
	 * @return
	 */
	public static BankingDetails add(
	 Integer incomeAccountNumber, Integer incomeSortCode, 
	 String incomeAccountName,
	 Integer withdrawalAccountNumber, Integer withdrawalSortCode, 
	 String withdrawalAccountName, 
	 String mandatedCompany, String mandatedAccNumExt, 
	 FWFacade facade) throws DataException {
		
		BankingDetails bankingDetails =
		 new BankingDetails(0, 
		 incomeAccountNumber, incomeSortCode, incomeAccountName,
		 withdrawalAccountNumber, withdrawalSortCode, withdrawalAccountName,
		 mandatedCompany, mandatedAccNumExt);
		
		return add(bankingDetails, facade);
	}
	
	private static BankingDetails add(BankingDetails details, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		// Fetch new BankingDetails primary key value
		String idStr 	= CCLAUtils.getNewKey("BankingDetails", facade);
		int id			= Integer.parseInt(idStr);
		details.setBankingDetailsId(id);
		
		details.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddBankingDetails", binder);
		
		return details;
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		
		CCLAUtils.addQueryParamToBinder
		(binder, "bankingDetailsId", Integer.toString(this.getBankingDetailsId()));
		
		CCLAUtils.addQueryIntParamToBinder
		(binder, "incAccNumber", this.getIncomeAccountNumber());
		CCLAUtils.addQueryIntParamToBinder
		(binder, "incSortCode", this.getIncomeSortCode());
		CCLAUtils.addQueryParamToBinder
		(binder, "incAccName", this.getIncomeAccountName());
		
		CCLAUtils.addQueryIntParamToBinder
		(binder, "withAccNumber", this.getWithdrawalAccountNumber());
		CCLAUtils.addQueryIntParamToBinder
		(binder, "withSortCode", this.getWithdrawalSortCode());
		CCLAUtils.addQueryParamToBinder
		(binder, "withAccName", this.getWithdrawalAccountName());
		
		CCLAUtils.addQueryParamToBinder
		(binder, "mandatedCompany", this.getMandatedCompany());
		CCLAUtils.addQueryParamToBinder
		(binder, "mandatedAccNumExt", this.getMandatedAccNumExt());
	}

	public Integer getIncomeAccountNumber() {
		return incomeAccountNumber;
	}

	public void setIncomeAccountNumber(Integer incomeAccountNumber) {
		this.incomeAccountNumber = incomeAccountNumber;
	}

	public Integer getIncomeSortCode() {
		return incomeSortCode;
	}

	public void setIncomeSortCode(Integer incomeSortCode) {
		this.incomeSortCode = incomeSortCode;
	}

	public String getIncomeAccountName() {
		return incomeAccountName;
	}

	public void setIncomeAccountName(String incomeAccountName) {
		this.incomeAccountName = incomeAccountName;
	}

	public Integer getWithdrawalAccountNumber() {
		return withdrawalAccountNumber;
	}

	public void setWithdrawalAccountNumber(Integer withdrawalAccountNumber) {
		this.withdrawalAccountNumber = withdrawalAccountNumber;
	}

	public Integer getWithdrawalSortCode() {
		return withdrawalSortCode;
	}

	public void setWithdrawalSortCode(Integer withdrawalSortCode) {
		this.withdrawalSortCode = withdrawalSortCode;
	}

	public String getWithdrawalAccountName() {
		return withdrawalAccountName;
	}

	public void setWithdrawalAccountName(String withdrawalAccountName) {
		this.withdrawalAccountName = withdrawalAccountName;
	}

	public int getBankingDetailsId() {
		return bankingDetailsId;
	}
	
	public void setBankingDetailsId(int id) {
		this.bankingDetailsId = id;
	}

	public String getMandatedCompany() {
		return mandatedCompany;
	}

	public void setMandatedCompany(String mandatedCompany) {
		this.mandatedCompany = mandatedCompany;
	}

	public String getMandatedAccNumExt() {
		return mandatedAccNumExt;
	}

	public void setMandatedAccNumExt(String mandatedAccNumExt) {
		this.mandatedAccNumExt = mandatedAccNumExt;
	}
}
