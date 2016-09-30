/**
 * Fund.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class Fund  implements java.io.Serializable {
    private java.lang.String fundCode;

    private java.lang.String fundName;

    private java.lang.String status;

    private java.util.Calendar openingDate;

    private java.lang.String typeCode;

    private java.lang.String incomeTypeCode;

    private java.lang.String numberOfDecimalPlaceRounding;

    private boolean incomeGrossIndicator;

    private boolean managementExpensesIndicator;

    private boolean taxExemptIndicator;

    private boolean taxReliefIndicator;

    private boolean interestBACsIndicator;

    private boolean interestRetainedIndicator;

    private boolean interestTrandferedIndicator;

    private boolean dividendBACIndicator;

    private boolean dividendTransferDepositIndicator;

    private boolean dividendReinvestIndicator;

    private boolean dividendReinvestAccountIndicator;

    private java.lang.String BACSUserNumberCapital;

    private java.lang.String BACSUserNameCapital;

    private int bankSortCodeCapital;

    private int bankAccountNumCapital;

    private java.lang.String BACSUserNumberIncome;

    private java.lang.String BACSUserNameIncome;

    private int bankSortCodeIncome;

    private int bankAccountNumberIncome;

    private java.lang.String depositCompany;

    private java.lang.String depositAccountNumberExternal;

    private double minimumIncomePaymentAmount;

    private int duplicateDealSearchPeriod;

    private int withdrawlSearchPeriod;

    private double incrementRate1;

    private double incrementRate2;

    private double threshold1;

    private double threshold2;

    private boolean accrualJanIndicator;

    private boolean accrualFebIndicator;

    private boolean accrualMarIndicator;

    private boolean accrualAprIndicator;

    private boolean accrualMayIndicator;

    private boolean accrualJunIndicator;

    private boolean accrualJulIndicator;

    private boolean accrualAugIndicator;

    private boolean accrualSepIndicator;

    private boolean accrualOctIndicator;

    private boolean accrualNovIndicator;

    private boolean accrualDecIndicator;

    private java.util.Calendar currentAccrualDate;

    private java.util.Calendar nextAccrualDate;

    private boolean distributionJanIndicator;

    private boolean distributionFebIndicator;

    private boolean distributionMarIndicator;

    private boolean distributionAprIndicator;

    private boolean distributionMayIndicator;

    private boolean distributionJunIndicator;

    private boolean distributionJulIndicator;

    private boolean distributionAugIndicator;

    private boolean distributionSepIndicator;

    private boolean distributionOctIndicator;

    private boolean distributionNovIndicator;

    private boolean distributionDecIndicator;

    private java.util.Calendar currentDistributionDate;

    private java.util.Calendar nextDistributionDate;

    private java.lang.String dealingFrequency;

    private java.lang.String monthlyDealingDay;

    private java.lang.String dailyDealingDay;

    private java.util.Calendar currentDealingDate;

    private java.util.Calendar nextDealingDate;

    private double cash;

    private double units;

    private double accruedIncome;

    private double balance3;

    private double incomeForDistribution;

    private double balance5;

    private double balance6;

    private double balance7;

    private double balance8;

    private double managementExpenses;

    private double VATManagementExpenses;

    private double positiveRoundingDifference;

    private double negativeRoundingDifference;

    private int reportingOrder;

    private java.lang.String displayName;

    private java.lang.String CHAPSCode;

    private int fundGroupId;

    private int dividendPaidMonthsPostXD;

    private boolean accrualDailyIndicator;

    private boolean distributionDailyIndicator;

    private boolean dividendReinvestOtherFundsIndicator;

    private boolean dividendReinvestCapitalBalanceIndicator;

    private boolean dividendReinvestCapitalBalancePayBasicPriceIndicator;

    private boolean shareClassIndicator;

    public Fund() {
    }

    public Fund(
           java.lang.String fundCode,
           java.lang.String fundName,
           java.lang.String status,
           java.util.Calendar openingDate,
           java.lang.String typeCode,
           java.lang.String incomeTypeCode,
           java.lang.String numberOfDecimalPlaceRounding,
           boolean incomeGrossIndicator,
           boolean managementExpensesIndicator,
           boolean taxExemptIndicator,
           boolean taxReliefIndicator,
           boolean interestBACsIndicator,
           boolean interestRetainedIndicator,
           boolean interestTrandferedIndicator,
           boolean dividendBACIndicator,
           boolean dividendTransferDepositIndicator,
           boolean dividendReinvestIndicator,
           boolean dividendReinvestAccountIndicator,
           java.lang.String BACSUserNumberCapital,
           java.lang.String BACSUserNameCapital,
           int bankSortCodeCapital,
           int bankAccountNumCapital,
           java.lang.String BACSUserNumberIncome,
           java.lang.String BACSUserNameIncome,
           int bankSortCodeIncome,
           int bankAccountNumberIncome,
           java.lang.String depositCompany,
           java.lang.String depositAccountNumberExternal,
           double minimumIncomePaymentAmount,
           int duplicateDealSearchPeriod,
           int withdrawlSearchPeriod,
           double incrementRate1,
           double incrementRate2,
           double threshold1,
           double threshold2,
           boolean accrualJanIndicator,
           boolean accrualFebIndicator,
           boolean accrualMarIndicator,
           boolean accrualAprIndicator,
           boolean accrualMayIndicator,
           boolean accrualJunIndicator,
           boolean accrualJulIndicator,
           boolean accrualAugIndicator,
           boolean accrualSepIndicator,
           boolean accrualOctIndicator,
           boolean accrualNovIndicator,
           boolean accrualDecIndicator,
           java.util.Calendar currentAccrualDate,
           java.util.Calendar nextAccrualDate,
           boolean distributionJanIndicator,
           boolean distributionFebIndicator,
           boolean distributionMarIndicator,
           boolean distributionAprIndicator,
           boolean distributionMayIndicator,
           boolean distributionJunIndicator,
           boolean distributionJulIndicator,
           boolean distributionAugIndicator,
           boolean distributionSepIndicator,
           boolean distributionOctIndicator,
           boolean distributionNovIndicator,
           boolean distributionDecIndicator,
           java.util.Calendar currentDistributionDate,
           java.util.Calendar nextDistributionDate,
           java.lang.String dealingFrequency,
           java.lang.String monthlyDealingDay,
           java.lang.String dailyDealingDay,
           java.util.Calendar currentDealingDate,
           java.util.Calendar nextDealingDate,
           double cash,
           double units,
           double accruedIncome,
           double balance3,
           double incomeForDistribution,
           double balance5,
           double balance6,
           double balance7,
           double balance8,
           double managementExpenses,
           double VATManagementExpenses,
           double positiveRoundingDifference,
           double negativeRoundingDifference,
           int reportingOrder,
           java.lang.String displayName,
           java.lang.String CHAPSCode,
           int fundGroupId,
           int dividendPaidMonthsPostXD,
           boolean accrualDailyIndicator,
           boolean distributionDailyIndicator,
           boolean dividendReinvestOtherFundsIndicator,
           boolean dividendReinvestCapitalBalanceIndicator,
           boolean dividendReinvestCapitalBalancePayBasicPriceIndicator,
           boolean shareClassIndicator) {
           this.fundCode = fundCode;
           this.fundName = fundName;
           this.status = status;
           this.openingDate = openingDate;
           this.typeCode = typeCode;
           this.incomeTypeCode = incomeTypeCode;
           this.numberOfDecimalPlaceRounding = numberOfDecimalPlaceRounding;
           this.incomeGrossIndicator = incomeGrossIndicator;
           this.managementExpensesIndicator = managementExpensesIndicator;
           this.taxExemptIndicator = taxExemptIndicator;
           this.taxReliefIndicator = taxReliefIndicator;
           this.interestBACsIndicator = interestBACsIndicator;
           this.interestRetainedIndicator = interestRetainedIndicator;
           this.interestTrandferedIndicator = interestTrandferedIndicator;
           this.dividendBACIndicator = dividendBACIndicator;
           this.dividendTransferDepositIndicator = dividendTransferDepositIndicator;
           this.dividendReinvestIndicator = dividendReinvestIndicator;
           this.dividendReinvestAccountIndicator = dividendReinvestAccountIndicator;
           this.BACSUserNumberCapital = BACSUserNumberCapital;
           this.BACSUserNameCapital = BACSUserNameCapital;
           this.bankSortCodeCapital = bankSortCodeCapital;
           this.bankAccountNumCapital = bankAccountNumCapital;
           this.BACSUserNumberIncome = BACSUserNumberIncome;
           this.BACSUserNameIncome = BACSUserNameIncome;
           this.bankSortCodeIncome = bankSortCodeIncome;
           this.bankAccountNumberIncome = bankAccountNumberIncome;
           this.depositCompany = depositCompany;
           this.depositAccountNumberExternal = depositAccountNumberExternal;
           this.minimumIncomePaymentAmount = minimumIncomePaymentAmount;
           this.duplicateDealSearchPeriod = duplicateDealSearchPeriod;
           this.withdrawlSearchPeriod = withdrawlSearchPeriod;
           this.incrementRate1 = incrementRate1;
           this.incrementRate2 = incrementRate2;
           this.threshold1 = threshold1;
           this.threshold2 = threshold2;
           this.accrualJanIndicator = accrualJanIndicator;
           this.accrualFebIndicator = accrualFebIndicator;
           this.accrualMarIndicator = accrualMarIndicator;
           this.accrualAprIndicator = accrualAprIndicator;
           this.accrualMayIndicator = accrualMayIndicator;
           this.accrualJunIndicator = accrualJunIndicator;
           this.accrualJulIndicator = accrualJulIndicator;
           this.accrualAugIndicator = accrualAugIndicator;
           this.accrualSepIndicator = accrualSepIndicator;
           this.accrualOctIndicator = accrualOctIndicator;
           this.accrualNovIndicator = accrualNovIndicator;
           this.accrualDecIndicator = accrualDecIndicator;
           this.currentAccrualDate = currentAccrualDate;
           this.nextAccrualDate = nextAccrualDate;
           this.distributionJanIndicator = distributionJanIndicator;
           this.distributionFebIndicator = distributionFebIndicator;
           this.distributionMarIndicator = distributionMarIndicator;
           this.distributionAprIndicator = distributionAprIndicator;
           this.distributionMayIndicator = distributionMayIndicator;
           this.distributionJunIndicator = distributionJunIndicator;
           this.distributionJulIndicator = distributionJulIndicator;
           this.distributionAugIndicator = distributionAugIndicator;
           this.distributionSepIndicator = distributionSepIndicator;
           this.distributionOctIndicator = distributionOctIndicator;
           this.distributionNovIndicator = distributionNovIndicator;
           this.distributionDecIndicator = distributionDecIndicator;
           this.currentDistributionDate = currentDistributionDate;
           this.nextDistributionDate = nextDistributionDate;
           this.dealingFrequency = dealingFrequency;
           this.monthlyDealingDay = monthlyDealingDay;
           this.dailyDealingDay = dailyDealingDay;
           this.currentDealingDate = currentDealingDate;
           this.nextDealingDate = nextDealingDate;
           this.cash = cash;
           this.units = units;
           this.accruedIncome = accruedIncome;
           this.balance3 = balance3;
           this.incomeForDistribution = incomeForDistribution;
           this.balance5 = balance5;
           this.balance6 = balance6;
           this.balance7 = balance7;
           this.balance8 = balance8;
           this.managementExpenses = managementExpenses;
           this.VATManagementExpenses = VATManagementExpenses;
           this.positiveRoundingDifference = positiveRoundingDifference;
           this.negativeRoundingDifference = negativeRoundingDifference;
           this.reportingOrder = reportingOrder;
           this.displayName = displayName;
           this.CHAPSCode = CHAPSCode;
           this.fundGroupId = fundGroupId;
           this.dividendPaidMonthsPostXD = dividendPaidMonthsPostXD;
           this.accrualDailyIndicator = accrualDailyIndicator;
           this.distributionDailyIndicator = distributionDailyIndicator;
           this.dividendReinvestOtherFundsIndicator = dividendReinvestOtherFundsIndicator;
           this.dividendReinvestCapitalBalanceIndicator = dividendReinvestCapitalBalanceIndicator;
           this.dividendReinvestCapitalBalancePayBasicPriceIndicator = dividendReinvestCapitalBalancePayBasicPriceIndicator;
           this.shareClassIndicator = shareClassIndicator;
    }


    /**
     * Gets the fundCode value for this Fund.
     * 
     * @return fundCode
     */
    public java.lang.String getFundCode() {
        return fundCode;
    }


    /**
     * Sets the fundCode value for this Fund.
     * 
     * @param fundCode
     */
    public void setFundCode(java.lang.String fundCode) {
        this.fundCode = fundCode;
    }


    /**
     * Gets the fundName value for this Fund.
     * 
     * @return fundName
     */
    public java.lang.String getFundName() {
        return fundName;
    }


    /**
     * Sets the fundName value for this Fund.
     * 
     * @param fundName
     */
    public void setFundName(java.lang.String fundName) {
        this.fundName = fundName;
    }


    /**
     * Gets the status value for this Fund.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Fund.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the openingDate value for this Fund.
     * 
     * @return openingDate
     */
    public java.util.Calendar getOpeningDate() {
        return openingDate;
    }


    /**
     * Sets the openingDate value for this Fund.
     * 
     * @param openingDate
     */
    public void setOpeningDate(java.util.Calendar openingDate) {
        this.openingDate = openingDate;
    }


    /**
     * Gets the typeCode value for this Fund.
     * 
     * @return typeCode
     */
    public java.lang.String getTypeCode() {
        return typeCode;
    }


    /**
     * Sets the typeCode value for this Fund.
     * 
     * @param typeCode
     */
    public void setTypeCode(java.lang.String typeCode) {
        this.typeCode = typeCode;
    }


    /**
     * Gets the incomeTypeCode value for this Fund.
     * 
     * @return incomeTypeCode
     */
    public java.lang.String getIncomeTypeCode() {
        return incomeTypeCode;
    }


    /**
     * Sets the incomeTypeCode value for this Fund.
     * 
     * @param incomeTypeCode
     */
    public void setIncomeTypeCode(java.lang.String incomeTypeCode) {
        this.incomeTypeCode = incomeTypeCode;
    }


    /**
     * Gets the numberOfDecimalPlaceRounding value for this Fund.
     * 
     * @return numberOfDecimalPlaceRounding
     */
    public java.lang.String getNumberOfDecimalPlaceRounding() {
        return numberOfDecimalPlaceRounding;
    }


    /**
     * Sets the numberOfDecimalPlaceRounding value for this Fund.
     * 
     * @param numberOfDecimalPlaceRounding
     */
    public void setNumberOfDecimalPlaceRounding(java.lang.String numberOfDecimalPlaceRounding) {
        this.numberOfDecimalPlaceRounding = numberOfDecimalPlaceRounding;
    }


    /**
     * Gets the incomeGrossIndicator value for this Fund.
     * 
     * @return incomeGrossIndicator
     */
    public boolean isIncomeGrossIndicator() {
        return incomeGrossIndicator;
    }


    /**
     * Sets the incomeGrossIndicator value for this Fund.
     * 
     * @param incomeGrossIndicator
     */
    public void setIncomeGrossIndicator(boolean incomeGrossIndicator) {
        this.incomeGrossIndicator = incomeGrossIndicator;
    }


    /**
     * Gets the managementExpensesIndicator value for this Fund.
     * 
     * @return managementExpensesIndicator
     */
    public boolean isManagementExpensesIndicator() {
        return managementExpensesIndicator;
    }


    /**
     * Sets the managementExpensesIndicator value for this Fund.
     * 
     * @param managementExpensesIndicator
     */
    public void setManagementExpensesIndicator(boolean managementExpensesIndicator) {
        this.managementExpensesIndicator = managementExpensesIndicator;
    }


    /**
     * Gets the taxExemptIndicator value for this Fund.
     * 
     * @return taxExemptIndicator
     */
    public boolean isTaxExemptIndicator() {
        return taxExemptIndicator;
    }


    /**
     * Sets the taxExemptIndicator value for this Fund.
     * 
     * @param taxExemptIndicator
     */
    public void setTaxExemptIndicator(boolean taxExemptIndicator) {
        this.taxExemptIndicator = taxExemptIndicator;
    }


    /**
     * Gets the taxReliefIndicator value for this Fund.
     * 
     * @return taxReliefIndicator
     */
    public boolean isTaxReliefIndicator() {
        return taxReliefIndicator;
    }


    /**
     * Sets the taxReliefIndicator value for this Fund.
     * 
     * @param taxReliefIndicator
     */
    public void setTaxReliefIndicator(boolean taxReliefIndicator) {
        this.taxReliefIndicator = taxReliefIndicator;
    }


    /**
     * Gets the interestBACsIndicator value for this Fund.
     * 
     * @return interestBACsIndicator
     */
    public boolean isInterestBACsIndicator() {
        return interestBACsIndicator;
    }


    /**
     * Sets the interestBACsIndicator value for this Fund.
     * 
     * @param interestBACsIndicator
     */
    public void setInterestBACsIndicator(boolean interestBACsIndicator) {
        this.interestBACsIndicator = interestBACsIndicator;
    }


    /**
     * Gets the interestRetainedIndicator value for this Fund.
     * 
     * @return interestRetainedIndicator
     */
    public boolean isInterestRetainedIndicator() {
        return interestRetainedIndicator;
    }


    /**
     * Sets the interestRetainedIndicator value for this Fund.
     * 
     * @param interestRetainedIndicator
     */
    public void setInterestRetainedIndicator(boolean interestRetainedIndicator) {
        this.interestRetainedIndicator = interestRetainedIndicator;
    }


    /**
     * Gets the interestTrandferedIndicator value for this Fund.
     * 
     * @return interestTrandferedIndicator
     */
    public boolean isInterestTrandferedIndicator() {
        return interestTrandferedIndicator;
    }


    /**
     * Sets the interestTrandferedIndicator value for this Fund.
     * 
     * @param interestTrandferedIndicator
     */
    public void setInterestTrandferedIndicator(boolean interestTrandferedIndicator) {
        this.interestTrandferedIndicator = interestTrandferedIndicator;
    }


    /**
     * Gets the dividendBACIndicator value for this Fund.
     * 
     * @return dividendBACIndicator
     */
    public boolean isDividendBACIndicator() {
        return dividendBACIndicator;
    }


    /**
     * Sets the dividendBACIndicator value for this Fund.
     * 
     * @param dividendBACIndicator
     */
    public void setDividendBACIndicator(boolean dividendBACIndicator) {
        this.dividendBACIndicator = dividendBACIndicator;
    }


    /**
     * Gets the dividendTransferDepositIndicator value for this Fund.
     * 
     * @return dividendTransferDepositIndicator
     */
    public boolean isDividendTransferDepositIndicator() {
        return dividendTransferDepositIndicator;
    }


    /**
     * Sets the dividendTransferDepositIndicator value for this Fund.
     * 
     * @param dividendTransferDepositIndicator
     */
    public void setDividendTransferDepositIndicator(boolean dividendTransferDepositIndicator) {
        this.dividendTransferDepositIndicator = dividendTransferDepositIndicator;
    }


    /**
     * Gets the dividendReinvestIndicator value for this Fund.
     * 
     * @return dividendReinvestIndicator
     */
    public boolean isDividendReinvestIndicator() {
        return dividendReinvestIndicator;
    }


    /**
     * Sets the dividendReinvestIndicator value for this Fund.
     * 
     * @param dividendReinvestIndicator
     */
    public void setDividendReinvestIndicator(boolean dividendReinvestIndicator) {
        this.dividendReinvestIndicator = dividendReinvestIndicator;
    }


    /**
     * Gets the dividendReinvestAccountIndicator value for this Fund.
     * 
     * @return dividendReinvestAccountIndicator
     */
    public boolean isDividendReinvestAccountIndicator() {
        return dividendReinvestAccountIndicator;
    }


    /**
     * Sets the dividendReinvestAccountIndicator value for this Fund.
     * 
     * @param dividendReinvestAccountIndicator
     */
    public void setDividendReinvestAccountIndicator(boolean dividendReinvestAccountIndicator) {
        this.dividendReinvestAccountIndicator = dividendReinvestAccountIndicator;
    }


    /**
     * Gets the BACSUserNumberCapital value for this Fund.
     * 
     * @return BACSUserNumberCapital
     */
    public java.lang.String getBACSUserNumberCapital() {
        return BACSUserNumberCapital;
    }


    /**
     * Sets the BACSUserNumberCapital value for this Fund.
     * 
     * @param BACSUserNumberCapital
     */
    public void setBACSUserNumberCapital(java.lang.String BACSUserNumberCapital) {
        this.BACSUserNumberCapital = BACSUserNumberCapital;
    }


    /**
     * Gets the BACSUserNameCapital value for this Fund.
     * 
     * @return BACSUserNameCapital
     */
    public java.lang.String getBACSUserNameCapital() {
        return BACSUserNameCapital;
    }


    /**
     * Sets the BACSUserNameCapital value for this Fund.
     * 
     * @param BACSUserNameCapital
     */
    public void setBACSUserNameCapital(java.lang.String BACSUserNameCapital) {
        this.BACSUserNameCapital = BACSUserNameCapital;
    }


    /**
     * Gets the bankSortCodeCapital value for this Fund.
     * 
     * @return bankSortCodeCapital
     */
    public int getBankSortCodeCapital() {
        return bankSortCodeCapital;
    }


    /**
     * Sets the bankSortCodeCapital value for this Fund.
     * 
     * @param bankSortCodeCapital
     */
    public void setBankSortCodeCapital(int bankSortCodeCapital) {
        this.bankSortCodeCapital = bankSortCodeCapital;
    }


    /**
     * Gets the bankAccountNumCapital value for this Fund.
     * 
     * @return bankAccountNumCapital
     */
    public int getBankAccountNumCapital() {
        return bankAccountNumCapital;
    }


    /**
     * Sets the bankAccountNumCapital value for this Fund.
     * 
     * @param bankAccountNumCapital
     */
    public void setBankAccountNumCapital(int bankAccountNumCapital) {
        this.bankAccountNumCapital = bankAccountNumCapital;
    }


    /**
     * Gets the BACSUserNumberIncome value for this Fund.
     * 
     * @return BACSUserNumberIncome
     */
    public java.lang.String getBACSUserNumberIncome() {
        return BACSUserNumberIncome;
    }


    /**
     * Sets the BACSUserNumberIncome value for this Fund.
     * 
     * @param BACSUserNumberIncome
     */
    public void setBACSUserNumberIncome(java.lang.String BACSUserNumberIncome) {
        this.BACSUserNumberIncome = BACSUserNumberIncome;
    }


    /**
     * Gets the BACSUserNameIncome value for this Fund.
     * 
     * @return BACSUserNameIncome
     */
    public java.lang.String getBACSUserNameIncome() {
        return BACSUserNameIncome;
    }


    /**
     * Sets the BACSUserNameIncome value for this Fund.
     * 
     * @param BACSUserNameIncome
     */
    public void setBACSUserNameIncome(java.lang.String BACSUserNameIncome) {
        this.BACSUserNameIncome = BACSUserNameIncome;
    }


    /**
     * Gets the bankSortCodeIncome value for this Fund.
     * 
     * @return bankSortCodeIncome
     */
    public int getBankSortCodeIncome() {
        return bankSortCodeIncome;
    }


    /**
     * Sets the bankSortCodeIncome value for this Fund.
     * 
     * @param bankSortCodeIncome
     */
    public void setBankSortCodeIncome(int bankSortCodeIncome) {
        this.bankSortCodeIncome = bankSortCodeIncome;
    }


    /**
     * Gets the bankAccountNumberIncome value for this Fund.
     * 
     * @return bankAccountNumberIncome
     */
    public int getBankAccountNumberIncome() {
        return bankAccountNumberIncome;
    }


    /**
     * Sets the bankAccountNumberIncome value for this Fund.
     * 
     * @param bankAccountNumberIncome
     */
    public void setBankAccountNumberIncome(int bankAccountNumberIncome) {
        this.bankAccountNumberIncome = bankAccountNumberIncome;
    }


    /**
     * Gets the depositCompany value for this Fund.
     * 
     * @return depositCompany
     */
    public java.lang.String getDepositCompany() {
        return depositCompany;
    }


    /**
     * Sets the depositCompany value for this Fund.
     * 
     * @param depositCompany
     */
    public void setDepositCompany(java.lang.String depositCompany) {
        this.depositCompany = depositCompany;
    }


    /**
     * Gets the depositAccountNumberExternal value for this Fund.
     * 
     * @return depositAccountNumberExternal
     */
    public java.lang.String getDepositAccountNumberExternal() {
        return depositAccountNumberExternal;
    }


    /**
     * Sets the depositAccountNumberExternal value for this Fund.
     * 
     * @param depositAccountNumberExternal
     */
    public void setDepositAccountNumberExternal(java.lang.String depositAccountNumberExternal) {
        this.depositAccountNumberExternal = depositAccountNumberExternal;
    }


    /**
     * Gets the minimumIncomePaymentAmount value for this Fund.
     * 
     * @return minimumIncomePaymentAmount
     */
    public double getMinimumIncomePaymentAmount() {
        return minimumIncomePaymentAmount;
    }


    /**
     * Sets the minimumIncomePaymentAmount value for this Fund.
     * 
     * @param minimumIncomePaymentAmount
     */
    public void setMinimumIncomePaymentAmount(double minimumIncomePaymentAmount) {
        this.minimumIncomePaymentAmount = minimumIncomePaymentAmount;
    }


    /**
     * Gets the duplicateDealSearchPeriod value for this Fund.
     * 
     * @return duplicateDealSearchPeriod
     */
    public int getDuplicateDealSearchPeriod() {
        return duplicateDealSearchPeriod;
    }


    /**
     * Sets the duplicateDealSearchPeriod value for this Fund.
     * 
     * @param duplicateDealSearchPeriod
     */
    public void setDuplicateDealSearchPeriod(int duplicateDealSearchPeriod) {
        this.duplicateDealSearchPeriod = duplicateDealSearchPeriod;
    }


    /**
     * Gets the withdrawlSearchPeriod value for this Fund.
     * 
     * @return withdrawlSearchPeriod
     */
    public int getWithdrawlSearchPeriod() {
        return withdrawlSearchPeriod;
    }


    /**
     * Sets the withdrawlSearchPeriod value for this Fund.
     * 
     * @param withdrawlSearchPeriod
     */
    public void setWithdrawlSearchPeriod(int withdrawlSearchPeriod) {
        this.withdrawlSearchPeriod = withdrawlSearchPeriod;
    }


    /**
     * Gets the incrementRate1 value for this Fund.
     * 
     * @return incrementRate1
     */
    public double getIncrementRate1() {
        return incrementRate1;
    }


    /**
     * Sets the incrementRate1 value for this Fund.
     * 
     * @param incrementRate1
     */
    public void setIncrementRate1(double incrementRate1) {
        this.incrementRate1 = incrementRate1;
    }


    /**
     * Gets the incrementRate2 value for this Fund.
     * 
     * @return incrementRate2
     */
    public double getIncrementRate2() {
        return incrementRate2;
    }


    /**
     * Sets the incrementRate2 value for this Fund.
     * 
     * @param incrementRate2
     */
    public void setIncrementRate2(double incrementRate2) {
        this.incrementRate2 = incrementRate2;
    }


    /**
     * Gets the threshold1 value for this Fund.
     * 
     * @return threshold1
     */
    public double getThreshold1() {
        return threshold1;
    }


    /**
     * Sets the threshold1 value for this Fund.
     * 
     * @param threshold1
     */
    public void setThreshold1(double threshold1) {
        this.threshold1 = threshold1;
    }


    /**
     * Gets the threshold2 value for this Fund.
     * 
     * @return threshold2
     */
    public double getThreshold2() {
        return threshold2;
    }


    /**
     * Sets the threshold2 value for this Fund.
     * 
     * @param threshold2
     */
    public void setThreshold2(double threshold2) {
        this.threshold2 = threshold2;
    }


    /**
     * Gets the accrualJanIndicator value for this Fund.
     * 
     * @return accrualJanIndicator
     */
    public boolean isAccrualJanIndicator() {
        return accrualJanIndicator;
    }


    /**
     * Sets the accrualJanIndicator value for this Fund.
     * 
     * @param accrualJanIndicator
     */
    public void setAccrualJanIndicator(boolean accrualJanIndicator) {
        this.accrualJanIndicator = accrualJanIndicator;
    }


    /**
     * Gets the accrualFebIndicator value for this Fund.
     * 
     * @return accrualFebIndicator
     */
    public boolean isAccrualFebIndicator() {
        return accrualFebIndicator;
    }


    /**
     * Sets the accrualFebIndicator value for this Fund.
     * 
     * @param accrualFebIndicator
     */
    public void setAccrualFebIndicator(boolean accrualFebIndicator) {
        this.accrualFebIndicator = accrualFebIndicator;
    }


    /**
     * Gets the accrualMarIndicator value for this Fund.
     * 
     * @return accrualMarIndicator
     */
    public boolean isAccrualMarIndicator() {
        return accrualMarIndicator;
    }


    /**
     * Sets the accrualMarIndicator value for this Fund.
     * 
     * @param accrualMarIndicator
     */
    public void setAccrualMarIndicator(boolean accrualMarIndicator) {
        this.accrualMarIndicator = accrualMarIndicator;
    }


    /**
     * Gets the accrualAprIndicator value for this Fund.
     * 
     * @return accrualAprIndicator
     */
    public boolean isAccrualAprIndicator() {
        return accrualAprIndicator;
    }


    /**
     * Sets the accrualAprIndicator value for this Fund.
     * 
     * @param accrualAprIndicator
     */
    public void setAccrualAprIndicator(boolean accrualAprIndicator) {
        this.accrualAprIndicator = accrualAprIndicator;
    }


    /**
     * Gets the accrualMayIndicator value for this Fund.
     * 
     * @return accrualMayIndicator
     */
    public boolean isAccrualMayIndicator() {
        return accrualMayIndicator;
    }


    /**
     * Sets the accrualMayIndicator value for this Fund.
     * 
     * @param accrualMayIndicator
     */
    public void setAccrualMayIndicator(boolean accrualMayIndicator) {
        this.accrualMayIndicator = accrualMayIndicator;
    }


    /**
     * Gets the accrualJunIndicator value for this Fund.
     * 
     * @return accrualJunIndicator
     */
    public boolean isAccrualJunIndicator() {
        return accrualJunIndicator;
    }


    /**
     * Sets the accrualJunIndicator value for this Fund.
     * 
     * @param accrualJunIndicator
     */
    public void setAccrualJunIndicator(boolean accrualJunIndicator) {
        this.accrualJunIndicator = accrualJunIndicator;
    }


    /**
     * Gets the accrualJulIndicator value for this Fund.
     * 
     * @return accrualJulIndicator
     */
    public boolean isAccrualJulIndicator() {
        return accrualJulIndicator;
    }


    /**
     * Sets the accrualJulIndicator value for this Fund.
     * 
     * @param accrualJulIndicator
     */
    public void setAccrualJulIndicator(boolean accrualJulIndicator) {
        this.accrualJulIndicator = accrualJulIndicator;
    }


    /**
     * Gets the accrualAugIndicator value for this Fund.
     * 
     * @return accrualAugIndicator
     */
    public boolean isAccrualAugIndicator() {
        return accrualAugIndicator;
    }


    /**
     * Sets the accrualAugIndicator value for this Fund.
     * 
     * @param accrualAugIndicator
     */
    public void setAccrualAugIndicator(boolean accrualAugIndicator) {
        this.accrualAugIndicator = accrualAugIndicator;
    }


    /**
     * Gets the accrualSepIndicator value for this Fund.
     * 
     * @return accrualSepIndicator
     */
    public boolean isAccrualSepIndicator() {
        return accrualSepIndicator;
    }


    /**
     * Sets the accrualSepIndicator value for this Fund.
     * 
     * @param accrualSepIndicator
     */
    public void setAccrualSepIndicator(boolean accrualSepIndicator) {
        this.accrualSepIndicator = accrualSepIndicator;
    }


    /**
     * Gets the accrualOctIndicator value for this Fund.
     * 
     * @return accrualOctIndicator
     */
    public boolean isAccrualOctIndicator() {
        return accrualOctIndicator;
    }


    /**
     * Sets the accrualOctIndicator value for this Fund.
     * 
     * @param accrualOctIndicator
     */
    public void setAccrualOctIndicator(boolean accrualOctIndicator) {
        this.accrualOctIndicator = accrualOctIndicator;
    }


    /**
     * Gets the accrualNovIndicator value for this Fund.
     * 
     * @return accrualNovIndicator
     */
    public boolean isAccrualNovIndicator() {
        return accrualNovIndicator;
    }


    /**
     * Sets the accrualNovIndicator value for this Fund.
     * 
     * @param accrualNovIndicator
     */
    public void setAccrualNovIndicator(boolean accrualNovIndicator) {
        this.accrualNovIndicator = accrualNovIndicator;
    }


    /**
     * Gets the accrualDecIndicator value for this Fund.
     * 
     * @return accrualDecIndicator
     */
    public boolean isAccrualDecIndicator() {
        return accrualDecIndicator;
    }


    /**
     * Sets the accrualDecIndicator value for this Fund.
     * 
     * @param accrualDecIndicator
     */
    public void setAccrualDecIndicator(boolean accrualDecIndicator) {
        this.accrualDecIndicator = accrualDecIndicator;
    }


    /**
     * Gets the currentAccrualDate value for this Fund.
     * 
     * @return currentAccrualDate
     */
    public java.util.Calendar getCurrentAccrualDate() {
        return currentAccrualDate;
    }


    /**
     * Sets the currentAccrualDate value for this Fund.
     * 
     * @param currentAccrualDate
     */
    public void setCurrentAccrualDate(java.util.Calendar currentAccrualDate) {
        this.currentAccrualDate = currentAccrualDate;
    }


    /**
     * Gets the nextAccrualDate value for this Fund.
     * 
     * @return nextAccrualDate
     */
    public java.util.Calendar getNextAccrualDate() {
        return nextAccrualDate;
    }


    /**
     * Sets the nextAccrualDate value for this Fund.
     * 
     * @param nextAccrualDate
     */
    public void setNextAccrualDate(java.util.Calendar nextAccrualDate) {
        this.nextAccrualDate = nextAccrualDate;
    }


    /**
     * Gets the distributionJanIndicator value for this Fund.
     * 
     * @return distributionJanIndicator
     */
    public boolean isDistributionJanIndicator() {
        return distributionJanIndicator;
    }


    /**
     * Sets the distributionJanIndicator value for this Fund.
     * 
     * @param distributionJanIndicator
     */
    public void setDistributionJanIndicator(boolean distributionJanIndicator) {
        this.distributionJanIndicator = distributionJanIndicator;
    }


    /**
     * Gets the distributionFebIndicator value for this Fund.
     * 
     * @return distributionFebIndicator
     */
    public boolean isDistributionFebIndicator() {
        return distributionFebIndicator;
    }


    /**
     * Sets the distributionFebIndicator value for this Fund.
     * 
     * @param distributionFebIndicator
     */
    public void setDistributionFebIndicator(boolean distributionFebIndicator) {
        this.distributionFebIndicator = distributionFebIndicator;
    }


    /**
     * Gets the distributionMarIndicator value for this Fund.
     * 
     * @return distributionMarIndicator
     */
    public boolean isDistributionMarIndicator() {
        return distributionMarIndicator;
    }


    /**
     * Sets the distributionMarIndicator value for this Fund.
     * 
     * @param distributionMarIndicator
     */
    public void setDistributionMarIndicator(boolean distributionMarIndicator) {
        this.distributionMarIndicator = distributionMarIndicator;
    }


    /**
     * Gets the distributionAprIndicator value for this Fund.
     * 
     * @return distributionAprIndicator
     */
    public boolean isDistributionAprIndicator() {
        return distributionAprIndicator;
    }


    /**
     * Sets the distributionAprIndicator value for this Fund.
     * 
     * @param distributionAprIndicator
     */
    public void setDistributionAprIndicator(boolean distributionAprIndicator) {
        this.distributionAprIndicator = distributionAprIndicator;
    }


    /**
     * Gets the distributionMayIndicator value for this Fund.
     * 
     * @return distributionMayIndicator
     */
    public boolean isDistributionMayIndicator() {
        return distributionMayIndicator;
    }


    /**
     * Sets the distributionMayIndicator value for this Fund.
     * 
     * @param distributionMayIndicator
     */
    public void setDistributionMayIndicator(boolean distributionMayIndicator) {
        this.distributionMayIndicator = distributionMayIndicator;
    }


    /**
     * Gets the distributionJunIndicator value for this Fund.
     * 
     * @return distributionJunIndicator
     */
    public boolean isDistributionJunIndicator() {
        return distributionJunIndicator;
    }


    /**
     * Sets the distributionJunIndicator value for this Fund.
     * 
     * @param distributionJunIndicator
     */
    public void setDistributionJunIndicator(boolean distributionJunIndicator) {
        this.distributionJunIndicator = distributionJunIndicator;
    }


    /**
     * Gets the distributionJulIndicator value for this Fund.
     * 
     * @return distributionJulIndicator
     */
    public boolean isDistributionJulIndicator() {
        return distributionJulIndicator;
    }


    /**
     * Sets the distributionJulIndicator value for this Fund.
     * 
     * @param distributionJulIndicator
     */
    public void setDistributionJulIndicator(boolean distributionJulIndicator) {
        this.distributionJulIndicator = distributionJulIndicator;
    }


    /**
     * Gets the distributionAugIndicator value for this Fund.
     * 
     * @return distributionAugIndicator
     */
    public boolean isDistributionAugIndicator() {
        return distributionAugIndicator;
    }


    /**
     * Sets the distributionAugIndicator value for this Fund.
     * 
     * @param distributionAugIndicator
     */
    public void setDistributionAugIndicator(boolean distributionAugIndicator) {
        this.distributionAugIndicator = distributionAugIndicator;
    }


    /**
     * Gets the distributionSepIndicator value for this Fund.
     * 
     * @return distributionSepIndicator
     */
    public boolean isDistributionSepIndicator() {
        return distributionSepIndicator;
    }


    /**
     * Sets the distributionSepIndicator value for this Fund.
     * 
     * @param distributionSepIndicator
     */
    public void setDistributionSepIndicator(boolean distributionSepIndicator) {
        this.distributionSepIndicator = distributionSepIndicator;
    }


    /**
     * Gets the distributionOctIndicator value for this Fund.
     * 
     * @return distributionOctIndicator
     */
    public boolean isDistributionOctIndicator() {
        return distributionOctIndicator;
    }


    /**
     * Sets the distributionOctIndicator value for this Fund.
     * 
     * @param distributionOctIndicator
     */
    public void setDistributionOctIndicator(boolean distributionOctIndicator) {
        this.distributionOctIndicator = distributionOctIndicator;
    }


    /**
     * Gets the distributionNovIndicator value for this Fund.
     * 
     * @return distributionNovIndicator
     */
    public boolean isDistributionNovIndicator() {
        return distributionNovIndicator;
    }


    /**
     * Sets the distributionNovIndicator value for this Fund.
     * 
     * @param distributionNovIndicator
     */
    public void setDistributionNovIndicator(boolean distributionNovIndicator) {
        this.distributionNovIndicator = distributionNovIndicator;
    }


    /**
     * Gets the distributionDecIndicator value for this Fund.
     * 
     * @return distributionDecIndicator
     */
    public boolean isDistributionDecIndicator() {
        return distributionDecIndicator;
    }


    /**
     * Sets the distributionDecIndicator value for this Fund.
     * 
     * @param distributionDecIndicator
     */
    public void setDistributionDecIndicator(boolean distributionDecIndicator) {
        this.distributionDecIndicator = distributionDecIndicator;
    }


    /**
     * Gets the currentDistributionDate value for this Fund.
     * 
     * @return currentDistributionDate
     */
    public java.util.Calendar getCurrentDistributionDate() {
        return currentDistributionDate;
    }


    /**
     * Sets the currentDistributionDate value for this Fund.
     * 
     * @param currentDistributionDate
     */
    public void setCurrentDistributionDate(java.util.Calendar currentDistributionDate) {
        this.currentDistributionDate = currentDistributionDate;
    }


    /**
     * Gets the nextDistributionDate value for this Fund.
     * 
     * @return nextDistributionDate
     */
    public java.util.Calendar getNextDistributionDate() {
        return nextDistributionDate;
    }


    /**
     * Sets the nextDistributionDate value for this Fund.
     * 
     * @param nextDistributionDate
     */
    public void setNextDistributionDate(java.util.Calendar nextDistributionDate) {
        this.nextDistributionDate = nextDistributionDate;
    }


    /**
     * Gets the dealingFrequency value for this Fund.
     * 
     * @return dealingFrequency
     */
    public java.lang.String getDealingFrequency() {
        return dealingFrequency;
    }


    /**
     * Sets the dealingFrequency value for this Fund.
     * 
     * @param dealingFrequency
     */
    public void setDealingFrequency(java.lang.String dealingFrequency) {
        this.dealingFrequency = dealingFrequency;
    }


    /**
     * Gets the monthlyDealingDay value for this Fund.
     * 
     * @return monthlyDealingDay
     */
    public java.lang.String getMonthlyDealingDay() {
        return monthlyDealingDay;
    }


    /**
     * Sets the monthlyDealingDay value for this Fund.
     * 
     * @param monthlyDealingDay
     */
    public void setMonthlyDealingDay(java.lang.String monthlyDealingDay) {
        this.monthlyDealingDay = monthlyDealingDay;
    }


    /**
     * Gets the dailyDealingDay value for this Fund.
     * 
     * @return dailyDealingDay
     */
    public java.lang.String getDailyDealingDay() {
        return dailyDealingDay;
    }


    /**
     * Sets the dailyDealingDay value for this Fund.
     * 
     * @param dailyDealingDay
     */
    public void setDailyDealingDay(java.lang.String dailyDealingDay) {
        this.dailyDealingDay = dailyDealingDay;
    }


    /**
     * Gets the currentDealingDate value for this Fund.
     * 
     * @return currentDealingDate
     */
    public java.util.Calendar getCurrentDealingDate() {
        return currentDealingDate;
    }


    /**
     * Sets the currentDealingDate value for this Fund.
     * 
     * @param currentDealingDate
     */
    public void setCurrentDealingDate(java.util.Calendar currentDealingDate) {
        this.currentDealingDate = currentDealingDate;
    }


    /**
     * Gets the nextDealingDate value for this Fund.
     * 
     * @return nextDealingDate
     */
    public java.util.Calendar getNextDealingDate() {
        return nextDealingDate;
    }


    /**
     * Sets the nextDealingDate value for this Fund.
     * 
     * @param nextDealingDate
     */
    public void setNextDealingDate(java.util.Calendar nextDealingDate) {
        this.nextDealingDate = nextDealingDate;
    }


    /**
     * Gets the cash value for this Fund.
     * 
     * @return cash
     */
    public double getCash() {
        return cash;
    }


    /**
     * Sets the cash value for this Fund.
     * 
     * @param cash
     */
    public void setCash(double cash) {
        this.cash = cash;
    }


    /**
     * Gets the units value for this Fund.
     * 
     * @return units
     */
    public double getUnits() {
        return units;
    }


    /**
     * Sets the units value for this Fund.
     * 
     * @param units
     */
    public void setUnits(double units) {
        this.units = units;
    }


    /**
     * Gets the accruedIncome value for this Fund.
     * 
     * @return accruedIncome
     */
    public double getAccruedIncome() {
        return accruedIncome;
    }


    /**
     * Sets the accruedIncome value for this Fund.
     * 
     * @param accruedIncome
     */
    public void setAccruedIncome(double accruedIncome) {
        this.accruedIncome = accruedIncome;
    }


    /**
     * Gets the balance3 value for this Fund.
     * 
     * @return balance3
     */
    public double getBalance3() {
        return balance3;
    }


    /**
     * Sets the balance3 value for this Fund.
     * 
     * @param balance3
     */
    public void setBalance3(double balance3) {
        this.balance3 = balance3;
    }


    /**
     * Gets the incomeForDistribution value for this Fund.
     * 
     * @return incomeForDistribution
     */
    public double getIncomeForDistribution() {
        return incomeForDistribution;
    }


    /**
     * Sets the incomeForDistribution value for this Fund.
     * 
     * @param incomeForDistribution
     */
    public void setIncomeForDistribution(double incomeForDistribution) {
        this.incomeForDistribution = incomeForDistribution;
    }


    /**
     * Gets the balance5 value for this Fund.
     * 
     * @return balance5
     */
    public double getBalance5() {
        return balance5;
    }


    /**
     * Sets the balance5 value for this Fund.
     * 
     * @param balance5
     */
    public void setBalance5(double balance5) {
        this.balance5 = balance5;
    }


    /**
     * Gets the balance6 value for this Fund.
     * 
     * @return balance6
     */
    public double getBalance6() {
        return balance6;
    }


    /**
     * Sets the balance6 value for this Fund.
     * 
     * @param balance6
     */
    public void setBalance6(double balance6) {
        this.balance6 = balance6;
    }


    /**
     * Gets the balance7 value for this Fund.
     * 
     * @return balance7
     */
    public double getBalance7() {
        return balance7;
    }


    /**
     * Sets the balance7 value for this Fund.
     * 
     * @param balance7
     */
    public void setBalance7(double balance7) {
        this.balance7 = balance7;
    }


    /**
     * Gets the balance8 value for this Fund.
     * 
     * @return balance8
     */
    public double getBalance8() {
        return balance8;
    }


    /**
     * Sets the balance8 value for this Fund.
     * 
     * @param balance8
     */
    public void setBalance8(double balance8) {
        this.balance8 = balance8;
    }


    /**
     * Gets the managementExpenses value for this Fund.
     * 
     * @return managementExpenses
     */
    public double getManagementExpenses() {
        return managementExpenses;
    }


    /**
     * Sets the managementExpenses value for this Fund.
     * 
     * @param managementExpenses
     */
    public void setManagementExpenses(double managementExpenses) {
        this.managementExpenses = managementExpenses;
    }


    /**
     * Gets the VATManagementExpenses value for this Fund.
     * 
     * @return VATManagementExpenses
     */
    public double getVATManagementExpenses() {
        return VATManagementExpenses;
    }


    /**
     * Sets the VATManagementExpenses value for this Fund.
     * 
     * @param VATManagementExpenses
     */
    public void setVATManagementExpenses(double VATManagementExpenses) {
        this.VATManagementExpenses = VATManagementExpenses;
    }


    /**
     * Gets the positiveRoundingDifference value for this Fund.
     * 
     * @return positiveRoundingDifference
     */
    public double getPositiveRoundingDifference() {
        return positiveRoundingDifference;
    }


    /**
     * Sets the positiveRoundingDifference value for this Fund.
     * 
     * @param positiveRoundingDifference
     */
    public void setPositiveRoundingDifference(double positiveRoundingDifference) {
        this.positiveRoundingDifference = positiveRoundingDifference;
    }


    /**
     * Gets the negativeRoundingDifference value for this Fund.
     * 
     * @return negativeRoundingDifference
     */
    public double getNegativeRoundingDifference() {
        return negativeRoundingDifference;
    }


    /**
     * Sets the negativeRoundingDifference value for this Fund.
     * 
     * @param negativeRoundingDifference
     */
    public void setNegativeRoundingDifference(double negativeRoundingDifference) {
        this.negativeRoundingDifference = negativeRoundingDifference;
    }


    /**
     * Gets the reportingOrder value for this Fund.
     * 
     * @return reportingOrder
     */
    public int getReportingOrder() {
        return reportingOrder;
    }


    /**
     * Sets the reportingOrder value for this Fund.
     * 
     * @param reportingOrder
     */
    public void setReportingOrder(int reportingOrder) {
        this.reportingOrder = reportingOrder;
    }


    /**
     * Gets the displayName value for this Fund.
     * 
     * @return displayName
     */
    public java.lang.String getDisplayName() {
        return displayName;
    }


    /**
     * Sets the displayName value for this Fund.
     * 
     * @param displayName
     */
    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }


    /**
     * Gets the CHAPSCode value for this Fund.
     * 
     * @return CHAPSCode
     */
    public java.lang.String getCHAPSCode() {
        return CHAPSCode;
    }


    /**
     * Sets the CHAPSCode value for this Fund.
     * 
     * @param CHAPSCode
     */
    public void setCHAPSCode(java.lang.String CHAPSCode) {
        this.CHAPSCode = CHAPSCode;
    }


    /**
     * Gets the fundGroupId value for this Fund.
     * 
     * @return fundGroupId
     */
    public int getFundGroupId() {
        return fundGroupId;
    }


    /**
     * Sets the fundGroupId value for this Fund.
     * 
     * @param fundGroupId
     */
    public void setFundGroupId(int fundGroupId) {
        this.fundGroupId = fundGroupId;
    }


    /**
     * Gets the dividendPaidMonthsPostXD value for this Fund.
     * 
     * @return dividendPaidMonthsPostXD
     */
    public int getDividendPaidMonthsPostXD() {
        return dividendPaidMonthsPostXD;
    }


    /**
     * Sets the dividendPaidMonthsPostXD value for this Fund.
     * 
     * @param dividendPaidMonthsPostXD
     */
    public void setDividendPaidMonthsPostXD(int dividendPaidMonthsPostXD) {
        this.dividendPaidMonthsPostXD = dividendPaidMonthsPostXD;
    }


    /**
     * Gets the accrualDailyIndicator value for this Fund.
     * 
     * @return accrualDailyIndicator
     */
    public boolean isAccrualDailyIndicator() {
        return accrualDailyIndicator;
    }


    /**
     * Sets the accrualDailyIndicator value for this Fund.
     * 
     * @param accrualDailyIndicator
     */
    public void setAccrualDailyIndicator(boolean accrualDailyIndicator) {
        this.accrualDailyIndicator = accrualDailyIndicator;
    }


    /**
     * Gets the distributionDailyIndicator value for this Fund.
     * 
     * @return distributionDailyIndicator
     */
    public boolean isDistributionDailyIndicator() {
        return distributionDailyIndicator;
    }


    /**
     * Sets the distributionDailyIndicator value for this Fund.
     * 
     * @param distributionDailyIndicator
     */
    public void setDistributionDailyIndicator(boolean distributionDailyIndicator) {
        this.distributionDailyIndicator = distributionDailyIndicator;
    }


    /**
     * Gets the dividendReinvestOtherFundsIndicator value for this Fund.
     * 
     * @return dividendReinvestOtherFundsIndicator
     */
    public boolean isDividendReinvestOtherFundsIndicator() {
        return dividendReinvestOtherFundsIndicator;
    }


    /**
     * Sets the dividendReinvestOtherFundsIndicator value for this Fund.
     * 
     * @param dividendReinvestOtherFundsIndicator
     */
    public void setDividendReinvestOtherFundsIndicator(boolean dividendReinvestOtherFundsIndicator) {
        this.dividendReinvestOtherFundsIndicator = dividendReinvestOtherFundsIndicator;
    }


    /**
     * Gets the dividendReinvestCapitalBalanceIndicator value for this Fund.
     * 
     * @return dividendReinvestCapitalBalanceIndicator
     */
    public boolean isDividendReinvestCapitalBalanceIndicator() {
        return dividendReinvestCapitalBalanceIndicator;
    }


    /**
     * Sets the dividendReinvestCapitalBalanceIndicator value for this Fund.
     * 
     * @param dividendReinvestCapitalBalanceIndicator
     */
    public void setDividendReinvestCapitalBalanceIndicator(boolean dividendReinvestCapitalBalanceIndicator) {
        this.dividendReinvestCapitalBalanceIndicator = dividendReinvestCapitalBalanceIndicator;
    }


    /**
     * Gets the dividendReinvestCapitalBalancePayBasicPriceIndicator value for this Fund.
     * 
     * @return dividendReinvestCapitalBalancePayBasicPriceIndicator
     */
    public boolean isDividendReinvestCapitalBalancePayBasicPriceIndicator() {
        return dividendReinvestCapitalBalancePayBasicPriceIndicator;
    }


    /**
     * Sets the dividendReinvestCapitalBalancePayBasicPriceIndicator value for this Fund.
     * 
     * @param dividendReinvestCapitalBalancePayBasicPriceIndicator
     */
    public void setDividendReinvestCapitalBalancePayBasicPriceIndicator(boolean dividendReinvestCapitalBalancePayBasicPriceIndicator) {
        this.dividendReinvestCapitalBalancePayBasicPriceIndicator = dividendReinvestCapitalBalancePayBasicPriceIndicator;
    }


    /**
     * Gets the shareClassIndicator value for this Fund.
     * 
     * @return shareClassIndicator
     */
    public boolean isShareClassIndicator() {
        return shareClassIndicator;
    }


    /**
     * Sets the shareClassIndicator value for this Fund.
     * 
     * @param shareClassIndicator
     */
    public void setShareClassIndicator(boolean shareClassIndicator) {
        this.shareClassIndicator = shareClassIndicator;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Fund)) return false;
        Fund other = (Fund) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fundCode==null && other.getFundCode()==null) || 
             (this.fundCode!=null &&
              this.fundCode.equals(other.getFundCode()))) &&
            ((this.fundName==null && other.getFundName()==null) || 
             (this.fundName!=null &&
              this.fundName.equals(other.getFundName()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.openingDate==null && other.getOpeningDate()==null) || 
             (this.openingDate!=null &&
              this.openingDate.equals(other.getOpeningDate()))) &&
            ((this.typeCode==null && other.getTypeCode()==null) || 
             (this.typeCode!=null &&
              this.typeCode.equals(other.getTypeCode()))) &&
            ((this.incomeTypeCode==null && other.getIncomeTypeCode()==null) || 
             (this.incomeTypeCode!=null &&
              this.incomeTypeCode.equals(other.getIncomeTypeCode()))) &&
            ((this.numberOfDecimalPlaceRounding==null && other.getNumberOfDecimalPlaceRounding()==null) || 
             (this.numberOfDecimalPlaceRounding!=null &&
              this.numberOfDecimalPlaceRounding.equals(other.getNumberOfDecimalPlaceRounding()))) &&
            this.incomeGrossIndicator == other.isIncomeGrossIndicator() &&
            this.managementExpensesIndicator == other.isManagementExpensesIndicator() &&
            this.taxExemptIndicator == other.isTaxExemptIndicator() &&
            this.taxReliefIndicator == other.isTaxReliefIndicator() &&
            this.interestBACsIndicator == other.isInterestBACsIndicator() &&
            this.interestRetainedIndicator == other.isInterestRetainedIndicator() &&
            this.interestTrandferedIndicator == other.isInterestTrandferedIndicator() &&
            this.dividendBACIndicator == other.isDividendBACIndicator() &&
            this.dividendTransferDepositIndicator == other.isDividendTransferDepositIndicator() &&
            this.dividendReinvestIndicator == other.isDividendReinvestIndicator() &&
            this.dividendReinvestAccountIndicator == other.isDividendReinvestAccountIndicator() &&
            ((this.BACSUserNumberCapital==null && other.getBACSUserNumberCapital()==null) || 
             (this.BACSUserNumberCapital!=null &&
              this.BACSUserNumberCapital.equals(other.getBACSUserNumberCapital()))) &&
            ((this.BACSUserNameCapital==null && other.getBACSUserNameCapital()==null) || 
             (this.BACSUserNameCapital!=null &&
              this.BACSUserNameCapital.equals(other.getBACSUserNameCapital()))) &&
            this.bankSortCodeCapital == other.getBankSortCodeCapital() &&
            this.bankAccountNumCapital == other.getBankAccountNumCapital() &&
            ((this.BACSUserNumberIncome==null && other.getBACSUserNumberIncome()==null) || 
             (this.BACSUserNumberIncome!=null &&
              this.BACSUserNumberIncome.equals(other.getBACSUserNumberIncome()))) &&
            ((this.BACSUserNameIncome==null && other.getBACSUserNameIncome()==null) || 
             (this.BACSUserNameIncome!=null &&
              this.BACSUserNameIncome.equals(other.getBACSUserNameIncome()))) &&
            this.bankSortCodeIncome == other.getBankSortCodeIncome() &&
            this.bankAccountNumberIncome == other.getBankAccountNumberIncome() &&
            ((this.depositCompany==null && other.getDepositCompany()==null) || 
             (this.depositCompany!=null &&
              this.depositCompany.equals(other.getDepositCompany()))) &&
            ((this.depositAccountNumberExternal==null && other.getDepositAccountNumberExternal()==null) || 
             (this.depositAccountNumberExternal!=null &&
              this.depositAccountNumberExternal.equals(other.getDepositAccountNumberExternal()))) &&
            this.minimumIncomePaymentAmount == other.getMinimumIncomePaymentAmount() &&
            this.duplicateDealSearchPeriod == other.getDuplicateDealSearchPeriod() &&
            this.withdrawlSearchPeriod == other.getWithdrawlSearchPeriod() &&
            this.incrementRate1 == other.getIncrementRate1() &&
            this.incrementRate2 == other.getIncrementRate2() &&
            this.threshold1 == other.getThreshold1() &&
            this.threshold2 == other.getThreshold2() &&
            this.accrualJanIndicator == other.isAccrualJanIndicator() &&
            this.accrualFebIndicator == other.isAccrualFebIndicator() &&
            this.accrualMarIndicator == other.isAccrualMarIndicator() &&
            this.accrualAprIndicator == other.isAccrualAprIndicator() &&
            this.accrualMayIndicator == other.isAccrualMayIndicator() &&
            this.accrualJunIndicator == other.isAccrualJunIndicator() &&
            this.accrualJulIndicator == other.isAccrualJulIndicator() &&
            this.accrualAugIndicator == other.isAccrualAugIndicator() &&
            this.accrualSepIndicator == other.isAccrualSepIndicator() &&
            this.accrualOctIndicator == other.isAccrualOctIndicator() &&
            this.accrualNovIndicator == other.isAccrualNovIndicator() &&
            this.accrualDecIndicator == other.isAccrualDecIndicator() &&
            ((this.currentAccrualDate==null && other.getCurrentAccrualDate()==null) || 
             (this.currentAccrualDate!=null &&
              this.currentAccrualDate.equals(other.getCurrentAccrualDate()))) &&
            ((this.nextAccrualDate==null && other.getNextAccrualDate()==null) || 
             (this.nextAccrualDate!=null &&
              this.nextAccrualDate.equals(other.getNextAccrualDate()))) &&
            this.distributionJanIndicator == other.isDistributionJanIndicator() &&
            this.distributionFebIndicator == other.isDistributionFebIndicator() &&
            this.distributionMarIndicator == other.isDistributionMarIndicator() &&
            this.distributionAprIndicator == other.isDistributionAprIndicator() &&
            this.distributionMayIndicator == other.isDistributionMayIndicator() &&
            this.distributionJunIndicator == other.isDistributionJunIndicator() &&
            this.distributionJulIndicator == other.isDistributionJulIndicator() &&
            this.distributionAugIndicator == other.isDistributionAugIndicator() &&
            this.distributionSepIndicator == other.isDistributionSepIndicator() &&
            this.distributionOctIndicator == other.isDistributionOctIndicator() &&
            this.distributionNovIndicator == other.isDistributionNovIndicator() &&
            this.distributionDecIndicator == other.isDistributionDecIndicator() &&
            ((this.currentDistributionDate==null && other.getCurrentDistributionDate()==null) || 
             (this.currentDistributionDate!=null &&
              this.currentDistributionDate.equals(other.getCurrentDistributionDate()))) &&
            ((this.nextDistributionDate==null && other.getNextDistributionDate()==null) || 
             (this.nextDistributionDate!=null &&
              this.nextDistributionDate.equals(other.getNextDistributionDate()))) &&
            ((this.dealingFrequency==null && other.getDealingFrequency()==null) || 
             (this.dealingFrequency!=null &&
              this.dealingFrequency.equals(other.getDealingFrequency()))) &&
            ((this.monthlyDealingDay==null && other.getMonthlyDealingDay()==null) || 
             (this.monthlyDealingDay!=null &&
              this.monthlyDealingDay.equals(other.getMonthlyDealingDay()))) &&
            ((this.dailyDealingDay==null && other.getDailyDealingDay()==null) || 
             (this.dailyDealingDay!=null &&
              this.dailyDealingDay.equals(other.getDailyDealingDay()))) &&
            ((this.currentDealingDate==null && other.getCurrentDealingDate()==null) || 
             (this.currentDealingDate!=null &&
              this.currentDealingDate.equals(other.getCurrentDealingDate()))) &&
            ((this.nextDealingDate==null && other.getNextDealingDate()==null) || 
             (this.nextDealingDate!=null &&
              this.nextDealingDate.equals(other.getNextDealingDate()))) &&
            this.cash == other.getCash() &&
            this.units == other.getUnits() &&
            this.accruedIncome == other.getAccruedIncome() &&
            this.balance3 == other.getBalance3() &&
            this.incomeForDistribution == other.getIncomeForDistribution() &&
            this.balance5 == other.getBalance5() &&
            this.balance6 == other.getBalance6() &&
            this.balance7 == other.getBalance7() &&
            this.balance8 == other.getBalance8() &&
            this.managementExpenses == other.getManagementExpenses() &&
            this.VATManagementExpenses == other.getVATManagementExpenses() &&
            this.positiveRoundingDifference == other.getPositiveRoundingDifference() &&
            this.negativeRoundingDifference == other.getNegativeRoundingDifference() &&
            this.reportingOrder == other.getReportingOrder() &&
            ((this.displayName==null && other.getDisplayName()==null) || 
             (this.displayName!=null &&
              this.displayName.equals(other.getDisplayName()))) &&
            ((this.CHAPSCode==null && other.getCHAPSCode()==null) || 
             (this.CHAPSCode!=null &&
              this.CHAPSCode.equals(other.getCHAPSCode()))) &&
            this.fundGroupId == other.getFundGroupId() &&
            this.dividendPaidMonthsPostXD == other.getDividendPaidMonthsPostXD() &&
            this.accrualDailyIndicator == other.isAccrualDailyIndicator() &&
            this.distributionDailyIndicator == other.isDistributionDailyIndicator() &&
            this.dividendReinvestOtherFundsIndicator == other.isDividendReinvestOtherFundsIndicator() &&
            this.dividendReinvestCapitalBalanceIndicator == other.isDividendReinvestCapitalBalanceIndicator() &&
            this.dividendReinvestCapitalBalancePayBasicPriceIndicator == other.isDividendReinvestCapitalBalancePayBasicPriceIndicator() &&
            this.shareClassIndicator == other.isShareClassIndicator();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getFundCode() != null) {
            _hashCode += getFundCode().hashCode();
        }
        if (getFundName() != null) {
            _hashCode += getFundName().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getOpeningDate() != null) {
            _hashCode += getOpeningDate().hashCode();
        }
        if (getTypeCode() != null) {
            _hashCode += getTypeCode().hashCode();
        }
        if (getIncomeTypeCode() != null) {
            _hashCode += getIncomeTypeCode().hashCode();
        }
        if (getNumberOfDecimalPlaceRounding() != null) {
            _hashCode += getNumberOfDecimalPlaceRounding().hashCode();
        }
        _hashCode += (isIncomeGrossIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isManagementExpensesIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isTaxExemptIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isTaxReliefIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInterestBACsIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInterestRetainedIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInterestTrandferedIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDividendBACIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDividendTransferDepositIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDividendReinvestIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDividendReinvestAccountIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getBACSUserNumberCapital() != null) {
            _hashCode += getBACSUserNumberCapital().hashCode();
        }
        if (getBACSUserNameCapital() != null) {
            _hashCode += getBACSUserNameCapital().hashCode();
        }
        _hashCode += getBankSortCodeCapital();
        _hashCode += getBankAccountNumCapital();
        if (getBACSUserNumberIncome() != null) {
            _hashCode += getBACSUserNumberIncome().hashCode();
        }
        if (getBACSUserNameIncome() != null) {
            _hashCode += getBACSUserNameIncome().hashCode();
        }
        _hashCode += getBankSortCodeIncome();
        _hashCode += getBankAccountNumberIncome();
        if (getDepositCompany() != null) {
            _hashCode += getDepositCompany().hashCode();
        }
        if (getDepositAccountNumberExternal() != null) {
            _hashCode += getDepositAccountNumberExternal().hashCode();
        }
        _hashCode += new Double(getMinimumIncomePaymentAmount()).hashCode();
        _hashCode += getDuplicateDealSearchPeriod();
        _hashCode += getWithdrawlSearchPeriod();
        _hashCode += new Double(getIncrementRate1()).hashCode();
        _hashCode += new Double(getIncrementRate2()).hashCode();
        _hashCode += new Double(getThreshold1()).hashCode();
        _hashCode += new Double(getThreshold2()).hashCode();
        _hashCode += (isAccrualJanIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualFebIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualMarIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualAprIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualMayIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualJunIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualJulIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualAugIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualSepIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualOctIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualNovIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAccrualDecIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getCurrentAccrualDate() != null) {
            _hashCode += getCurrentAccrualDate().hashCode();
        }
        if (getNextAccrualDate() != null) {
            _hashCode += getNextAccrualDate().hashCode();
        }
        _hashCode += (isDistributionJanIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionFebIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionMarIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionAprIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionMayIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionJunIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionJulIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionAugIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionSepIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionOctIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionNovIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionDecIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getCurrentDistributionDate() != null) {
            _hashCode += getCurrentDistributionDate().hashCode();
        }
        if (getNextDistributionDate() != null) {
            _hashCode += getNextDistributionDate().hashCode();
        }
        if (getDealingFrequency() != null) {
            _hashCode += getDealingFrequency().hashCode();
        }
        if (getMonthlyDealingDay() != null) {
            _hashCode += getMonthlyDealingDay().hashCode();
        }
        if (getDailyDealingDay() != null) {
            _hashCode += getDailyDealingDay().hashCode();
        }
        if (getCurrentDealingDate() != null) {
            _hashCode += getCurrentDealingDate().hashCode();
        }
        if (getNextDealingDate() != null) {
            _hashCode += getNextDealingDate().hashCode();
        }
        _hashCode += new Double(getCash()).hashCode();
        _hashCode += new Double(getUnits()).hashCode();
        _hashCode += new Double(getAccruedIncome()).hashCode();
        _hashCode += new Double(getBalance3()).hashCode();
        _hashCode += new Double(getIncomeForDistribution()).hashCode();
        _hashCode += new Double(getBalance5()).hashCode();
        _hashCode += new Double(getBalance6()).hashCode();
        _hashCode += new Double(getBalance7()).hashCode();
        _hashCode += new Double(getBalance8()).hashCode();
        _hashCode += new Double(getManagementExpenses()).hashCode();
        _hashCode += new Double(getVATManagementExpenses()).hashCode();
        _hashCode += new Double(getPositiveRoundingDifference()).hashCode();
        _hashCode += new Double(getNegativeRoundingDifference()).hashCode();
        _hashCode += getReportingOrder();
        if (getDisplayName() != null) {
            _hashCode += getDisplayName().hashCode();
        }
        if (getCHAPSCode() != null) {
            _hashCode += getCHAPSCode().hashCode();
        }
        _hashCode += getFundGroupId();
        _hashCode += getDividendPaidMonthsPostXD();
        _hashCode += (isAccrualDailyIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDistributionDailyIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDividendReinvestOtherFundsIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDividendReinvestCapitalBalanceIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDividendReinvestCapitalBalancePayBasicPriceIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isShareClassIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Fund.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Fund"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FundCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FundName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("openingDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "OpeningDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "TypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incomeTypeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeTypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfDecimalPlaceRounding");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "NumberOfDecimalPlaceRounding"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incomeGrossIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeGrossIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("managementExpensesIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ManagementExpensesIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxExemptIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "TaxExemptIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxReliefIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "TaxReliefIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interestBACsIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "InterestBACsIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interestRetainedIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "InterestRetainedIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interestTrandferedIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "InterestTrandferedIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dividendBACIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DividendBACIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dividendTransferDepositIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DividendTransferDepositIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dividendReinvestIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DividendReinvestIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dividendReinvestAccountIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DividendReinvestAccountIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BACSUserNumberCapital");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BACSUserNumberCapital"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BACSUserNameCapital");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BACSUserNameCapital"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankSortCodeCapital");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankSortCodeCapital"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankAccountNumCapital");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankAccountNumCapital"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BACSUserNumberIncome");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BACSUserNumberIncome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BACSUserNameIncome");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BACSUserNameIncome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankSortCodeIncome");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankSortCodeIncome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankAccountNumberIncome");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankAccountNumberIncome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depositCompany");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositCompany"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depositAccountNumberExternal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositAccountNumberExternal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minimumIncomePaymentAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MinimumIncomePaymentAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("duplicateDealSearchPeriod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DuplicateDealSearchPeriod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("withdrawlSearchPeriod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "WithdrawlSearchPeriod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incrementRate1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncrementRate1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incrementRate2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncrementRate2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("threshold1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Threshold1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("threshold2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Threshold2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualJanIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualJanIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualFebIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualFebIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualMarIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualMarIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualAprIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualAprIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualMayIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualMayIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualJunIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualJunIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualJulIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualJulIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualAugIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualAugIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualSepIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualSepIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualOctIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualOctIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualNovIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualNovIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualDecIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualDecIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentAccrualDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "CurrentAccrualDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nextAccrualDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "NextAccrualDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionJanIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionJanIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionFebIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionFebIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionMarIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionMarIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionAprIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionAprIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionMayIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionMayIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionJunIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionJunIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionJulIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionJulIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionAugIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionAugIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionSepIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionSepIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionOctIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionOctIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionNovIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionNovIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionDecIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionDecIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentDistributionDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "CurrentDistributionDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nextDistributionDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "NextDistributionDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dealingFrequency");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DealingFrequency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("monthlyDealingDay");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MonthlyDealingDay"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dailyDealingDay");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DailyDealingDay"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentDealingDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "CurrentDealingDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nextDealingDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "NextDealingDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cash");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Cash"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("units");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Units"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accruedIncome");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccruedIncome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Balance3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incomeForDistribution");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeForDistribution"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance5");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Balance5"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance6");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Balance6"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance7");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Balance7"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance8");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Balance8"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("managementExpenses");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ManagementExpenses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VATManagementExpenses");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "VATManagementExpenses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("positiveRoundingDifference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PositiveRoundingDifference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("negativeRoundingDifference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "NegativeRoundingDifference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportingOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ReportingOrder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DisplayName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CHAPSCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "CHAPSCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundGroupId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FundGroupId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dividendPaidMonthsPostXD");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DividendPaidMonthsPostXD"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accrualDailyIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualDailyIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionDailyIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionDailyIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dividendReinvestOtherFundsIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DividendReinvestOtherFundsIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dividendReinvestCapitalBalanceIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DividendReinvestCapitalBalanceIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dividendReinvestCapitalBalancePayBasicPriceIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DividendReinvestCapitalBalancePayBasicPriceIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shareClassIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClassIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
