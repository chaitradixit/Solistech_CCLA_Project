/**
 * Account.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class Account  implements java.io.Serializable {
   

	private int clientNumber;

    private java.lang.String fundCode;

    private int contributorTypeCode;

    private int subDivisionCode;

    private int contributorCode;

    private int accountNumber;

    private java.lang.String accountNumberExternal;

    private java.lang.String subtitle;

    private com.aurora.webservice.AccountStatus status;

    private java.util.Calendar dateOpened;

    private java.util.Calendar lastAmendmentDate;

    private int correspondentCode;

    private int beneficialCorrespondentCode;

    private boolean dataProtectionMailingIndicator;

    private com.aurora.webservice.IncomeDistributionMethods incomeDistributionMethod;

    private int bankSortCodeIncome;

    private int bankAccountNumberIncome;

    private java.lang.String bankAccountNameIncome;

    private java.lang.String accountShortNameOrBuildingSocietyReferenceIncome;

    private int bankSortCodeWithdrawal;

    private int bankAccountNumberWithdrawal;

    private java.lang.String bankAccountNameWithdrawal;

    private java.lang.String accountShortNameOrBuildingSocietyReferenceWithdrawal;

    private java.lang.String mandatedCompany;

    private java.lang.String mandatedAccount;

    private boolean multipleSignaturesIndicator;

    private java.lang.String multipleSignaturesInformation;

    private java.lang.String additionalSignatoryName1;

    private java.lang.String additionalSignatoryTelephone1;

    private java.lang.String additionalSignatoryPostcode1;

    private java.lang.String additionalSignatoryName2;

    private java.lang.String additionalSignatoryTelephone2;

    private java.lang.String additionalSignatoryPostcode2;

    private java.lang.String additionalSignatoryName3;

    private java.lang.String additionalSignatoryTelephone3;

    private java.lang.String additionalSignatoryPostcode3;

    private java.lang.String additionalSignatoryName4;

    private java.lang.String additionalSignatoryTelephone4;

    private java.lang.String additionalSignatoryPostcode4;

    private java.lang.String additionalSignatoryName5;

    private java.lang.String additionalSignatoryTelephone5;

    private java.lang.String additionalSignatoryPostcode5;

    private java.lang.String additionalSignatoryName6;

    private java.lang.String additionalSignatoryTelephone6;

    private java.lang.String additionalSignatoryPostcode6;

    private java.lang.String additionalSignatoryName7;

    private java.lang.String additionalSignatoryTelephone7;

    private java.lang.String additionalSignatoryPostcode7;

    private java.lang.String additionalSignatoryName8;

    private java.lang.String additionalSignatoryTelephone8;

    private java.lang.String additionalSignatoryPostcode8;

    private java.lang.String additionalSignatoryName9;

    private java.lang.String additionalSignatoryTelephone9;

    private java.lang.String additionalSignatoryPostcode9;

    private java.lang.String additionalSignatoryName10;

    private java.lang.String additionalSignatoryTelephone10;

    private java.lang.String additionalSignatoryPostcode10;

    private boolean standingTransactionsIndicator;

    private double cash;

    private double units;

    private double accruedIncome;

    private double balance3;

    private double incomeForDistribution;

    private double balance5;

    private double interestDividendAccrued;

    private double managementExpenseAccrued;

    private double lastStatementCash;

    private double lastStatementUnits;

    private java.lang.String IVSCFID;

    private java.lang.String IVSReferenceNumber;

    private java.lang.String mandateLetterSentIndicator;

    private java.lang.String mandateLetterSentDate;

    private boolean isExternalAccount;

    private double interestDividendAccruedNonTiered;

    private int shareClassCode;

    public Account() {
    }

    public Account(
           int clientNumber,
           java.lang.String fundCode,
           int contributorTypeCode,
           int subDivisionCode,
           int contributorCode,
           int accountNumber,
           java.lang.String accountNumberExternal,
           java.lang.String subtitle,
           com.aurora.webservice.AccountStatus status,
           java.util.Calendar dateOpened,
           java.util.Calendar lastAmendmentDate,
           int correspondentCode,
           int beneficialCorrespondentCode,
           boolean dataProtectionMailingIndicator,
           com.aurora.webservice.IncomeDistributionMethods incomeDistributionMethod,
           int bankSortCodeIncome,
           int bankAccountNumberIncome,
           java.lang.String bankAccountNameIncome,
           java.lang.String accountShortNameOrBuildingSocietyReferenceIncome,
           int bankSortCodeWithdrawal,
           int bankAccountNumberWithdrawal,
           java.lang.String bankAccountNameWithdrawal,
           java.lang.String accountShortNameOrBuildingSocietyReferenceWithdrawal,
           java.lang.String mandatedCompany,
           java.lang.String mandatedAccount,
           boolean multipleSignaturesIndicator,
           java.lang.String multipleSignaturesInformation,
           java.lang.String additionalSignatoryName1,
           java.lang.String additionalSignatoryTelephone1,
           java.lang.String additionalSignatoryPostcode1,
           java.lang.String additionalSignatoryName2,
           java.lang.String additionalSignatoryTelephone2,
           java.lang.String additionalSignatoryPostcode2,
           java.lang.String additionalSignatoryName3,
           java.lang.String additionalSignatoryTelephone3,
           java.lang.String additionalSignatoryPostcode3,
           java.lang.String additionalSignatoryName4,
           java.lang.String additionalSignatoryTelephone4,
           java.lang.String additionalSignatoryPostcode4,
           java.lang.String additionalSignatoryName5,
           java.lang.String additionalSignatoryTelephone5,
           java.lang.String additionalSignatoryPostcode5,
           java.lang.String additionalSignatoryName6,
           java.lang.String additionalSignatoryTelephone6,
           java.lang.String additionalSignatoryPostcode6,
           java.lang.String additionalSignatoryName7,
           java.lang.String additionalSignatoryTelephone7,
           java.lang.String additionalSignatoryPostcode7,
           java.lang.String additionalSignatoryName8,
           java.lang.String additionalSignatoryTelephone8,
           java.lang.String additionalSignatoryPostcode8,
           java.lang.String additionalSignatoryName9,
           java.lang.String additionalSignatoryTelephone9,
           java.lang.String additionalSignatoryPostcode9,
           java.lang.String additionalSignatoryName10,
           java.lang.String additionalSignatoryTelephone10,
           java.lang.String additionalSignatoryPostcode10,
           boolean standingTransactionsIndicator,
           double cash,
           double units,
           double accruedIncome,
           double balance3,
           double incomeForDistribution,
           double balance5,
           double interestDividendAccrued,
           double managementExpenseAccrued,
           double lastStatementCash,
           double lastStatementUnits,
           java.lang.String IVSCFID,
           java.lang.String IVSReferenceNumber,
           java.lang.String mandateLetterSentIndicator,
           java.lang.String mandateLetterSentDate,
           boolean isExternalAccount,
           double interestDividendAccruedNonTiered,
           int shareClassCode) {
           this.clientNumber = clientNumber;
           this.fundCode = fundCode;
           this.contributorTypeCode = contributorTypeCode;
           this.subDivisionCode = subDivisionCode;
           this.contributorCode = contributorCode;
           this.accountNumber = accountNumber;
           this.accountNumberExternal = accountNumberExternal;
           this.subtitle = subtitle;
           this.status = status;
           this.dateOpened = dateOpened;
           this.lastAmendmentDate = lastAmendmentDate;
           this.correspondentCode = correspondentCode;
           this.beneficialCorrespondentCode = beneficialCorrespondentCode;
           this.dataProtectionMailingIndicator = dataProtectionMailingIndicator;
           this.incomeDistributionMethod = incomeDistributionMethod;
           this.bankSortCodeIncome = bankSortCodeIncome;
           this.bankAccountNumberIncome = bankAccountNumberIncome;
           this.bankAccountNameIncome = bankAccountNameIncome;
           this.accountShortNameOrBuildingSocietyReferenceIncome = accountShortNameOrBuildingSocietyReferenceIncome;
           this.bankSortCodeWithdrawal = bankSortCodeWithdrawal;
           this.bankAccountNumberWithdrawal = bankAccountNumberWithdrawal;
           this.bankAccountNameWithdrawal = bankAccountNameWithdrawal;
           this.accountShortNameOrBuildingSocietyReferenceWithdrawal = accountShortNameOrBuildingSocietyReferenceWithdrawal;
           this.mandatedCompany = mandatedCompany;
           this.mandatedAccount = mandatedAccount;
           this.multipleSignaturesIndicator = multipleSignaturesIndicator;
           this.multipleSignaturesInformation = multipleSignaturesInformation;
           this.additionalSignatoryName1 = additionalSignatoryName1;
           this.additionalSignatoryTelephone1 = additionalSignatoryTelephone1;
           this.additionalSignatoryPostcode1 = additionalSignatoryPostcode1;
           this.additionalSignatoryName2 = additionalSignatoryName2;
           this.additionalSignatoryTelephone2 = additionalSignatoryTelephone2;
           this.additionalSignatoryPostcode2 = additionalSignatoryPostcode2;
           this.additionalSignatoryName3 = additionalSignatoryName3;
           this.additionalSignatoryTelephone3 = additionalSignatoryTelephone3;
           this.additionalSignatoryPostcode3 = additionalSignatoryPostcode3;
           this.additionalSignatoryName4 = additionalSignatoryName4;
           this.additionalSignatoryTelephone4 = additionalSignatoryTelephone4;
           this.additionalSignatoryPostcode4 = additionalSignatoryPostcode4;
           this.additionalSignatoryName5 = additionalSignatoryName5;
           this.additionalSignatoryTelephone5 = additionalSignatoryTelephone5;
           this.additionalSignatoryPostcode5 = additionalSignatoryPostcode5;
           this.additionalSignatoryName6 = additionalSignatoryName6;
           this.additionalSignatoryTelephone6 = additionalSignatoryTelephone6;
           this.additionalSignatoryPostcode6 = additionalSignatoryPostcode6;
           this.additionalSignatoryName7 = additionalSignatoryName7;
           this.additionalSignatoryTelephone7 = additionalSignatoryTelephone7;
           this.additionalSignatoryPostcode7 = additionalSignatoryPostcode7;
           this.additionalSignatoryName8 = additionalSignatoryName8;
           this.additionalSignatoryTelephone8 = additionalSignatoryTelephone8;
           this.additionalSignatoryPostcode8 = additionalSignatoryPostcode8;
           this.additionalSignatoryName9 = additionalSignatoryName9;
           this.additionalSignatoryTelephone9 = additionalSignatoryTelephone9;
           this.additionalSignatoryPostcode9 = additionalSignatoryPostcode9;
           this.additionalSignatoryName10 = additionalSignatoryName10;
           this.additionalSignatoryTelephone10 = additionalSignatoryTelephone10;
           this.additionalSignatoryPostcode10 = additionalSignatoryPostcode10;
           this.standingTransactionsIndicator = standingTransactionsIndicator;
           this.cash = cash;
           this.units = units;
           this.accruedIncome = accruedIncome;
           this.balance3 = balance3;
           this.incomeForDistribution = incomeForDistribution;
           this.balance5 = balance5;
           this.interestDividendAccrued = interestDividendAccrued;
           this.managementExpenseAccrued = managementExpenseAccrued;
           this.lastStatementCash = lastStatementCash;
           this.lastStatementUnits = lastStatementUnits;
           this.IVSCFID = IVSCFID;
           this.IVSReferenceNumber = IVSReferenceNumber;
           this.mandateLetterSentIndicator = mandateLetterSentIndicator;
           this.mandateLetterSentDate = mandateLetterSentDate;
           this.isExternalAccount = isExternalAccount;
           this.interestDividendAccruedNonTiered = interestDividendAccruedNonTiered;
           this.shareClassCode = shareClassCode;
    }


    /**
     * Gets the clientNumber value for this Account.
     * 
     * @return clientNumber
     */
    public int getClientNumber() {
        return clientNumber;
    }


    /**
     * Sets the clientNumber value for this Account.
     * 
     * @param clientNumber
     */
    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }


    /**
     * Gets the fundCode value for this Account.
     * 
     * @return fundCode
     */
    public java.lang.String getFundCode() {
        return fundCode;
    }


    /**
     * Sets the fundCode value for this Account.
     * 
     * @param fundCode
     */
    public void setFundCode(java.lang.String fundCode) {
        this.fundCode = fundCode;
    }


    /**
     * Gets the contributorTypeCode value for this Account.
     * 
     * @return contributorTypeCode
     */
    public int getContributorTypeCode() {
        return contributorTypeCode;
    }


    /**
     * Sets the contributorTypeCode value for this Account.
     * 
     * @param contributorTypeCode
     */
    public void setContributorTypeCode(int contributorTypeCode) {
        this.contributorTypeCode = contributorTypeCode;
    }


    /**
     * Gets the subDivisionCode value for this Account.
     * 
     * @return subDivisionCode
     */
    public int getSubDivisionCode() {
        return subDivisionCode;
    }


    /**
     * Sets the subDivisionCode value for this Account.
     * 
     * @param subDivisionCode
     */
    public void setSubDivisionCode(int subDivisionCode) {
        this.subDivisionCode = subDivisionCode;
    }


    /**
     * Gets the contributorCode value for this Account.
     * 
     * @return contributorCode
     */
    public int getContributorCode() {
        return contributorCode;
    }


    /**
     * Sets the contributorCode value for this Account.
     * 
     * @param contributorCode
     */
    public void setContributorCode(int contributorCode) {
        this.contributorCode = contributorCode;
    }


    /**
     * Gets the accountNumber value for this Account.
     * 
     * @return accountNumber
     */
    public int getAccountNumber() {
        return accountNumber;
    }


    /**
     * Sets the accountNumber value for this Account.
     * 
     * @param accountNumber
     */
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }


    /**
     * Gets the accountNumberExternal value for this Account.
     * 
     * @return accountNumberExternal
     */
    public java.lang.String getAccountNumberExternal() {
        return accountNumberExternal;
    }


    /**
     * Sets the accountNumberExternal value for this Account.
     * 
     * @param accountNumberExternal
     */
    public void setAccountNumberExternal(java.lang.String accountNumberExternal) {
        this.accountNumberExternal = accountNumberExternal;
    }


    /**
     * Gets the subtitle value for this Account.
     * 
     * @return subtitle
     */
    public java.lang.String getSubtitle() {
        return subtitle;
    }


    /**
     * Sets the subtitle value for this Account.
     * 
     * @param subtitle
     */
    public void setSubtitle(java.lang.String subtitle) {
        this.subtitle = subtitle;
    }


    /**
     * Gets the status value for this Account.
     * 
     * @return status
     */
    public com.aurora.webservice.AccountStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Account.
     * 
     * @param status
     */
    public void setStatus(com.aurora.webservice.AccountStatus status) {
        this.status = status;
    }


    /**
     * Gets the dateOpened value for this Account.
     * 
     * @return dateOpened
     */
    public java.util.Calendar getDateOpened() {
        return dateOpened;
    }


    /**
     * Sets the dateOpened value for this Account.
     * 
     * @param dateOpened
     */
    public void setDateOpened(java.util.Calendar dateOpened) {
        this.dateOpened = dateOpened;
    }


    /**
     * Gets the lastAmendmentDate value for this Account.
     * 
     * @return lastAmendmentDate
     */
    public java.util.Calendar getLastAmendmentDate() {
        return lastAmendmentDate;
    }


    /**
     * Sets the lastAmendmentDate value for this Account.
     * 
     * @param lastAmendmentDate
     */
    public void setLastAmendmentDate(java.util.Calendar lastAmendmentDate) {
        this.lastAmendmentDate = lastAmendmentDate;
    }


    /**
     * Gets the correspondentCode value for this Account.
     * 
     * @return correspondentCode
     */
    public int getCorrespondentCode() {
        return correspondentCode;
    }


    /**
     * Sets the correspondentCode value for this Account.
     * 
     * @param correspondentCode
     */
    public void setCorrespondentCode(int correspondentCode) {
        this.correspondentCode = correspondentCode;
    }


    /**
     * Gets the beneficialCorrespondentCode value for this Account.
     * 
     * @return beneficialCorrespondentCode
     */
    public int getBeneficialCorrespondentCode() {
        return beneficialCorrespondentCode;
    }


    /**
     * Sets the beneficialCorrespondentCode value for this Account.
     * 
     * @param beneficialCorrespondentCode
     */
    public void setBeneficialCorrespondentCode(int beneficialCorrespondentCode) {
        this.beneficialCorrespondentCode = beneficialCorrespondentCode;
    }


    /**
     * Gets the dataProtectionMailingIndicator value for this Account.
     * 
     * @return dataProtectionMailingIndicator
     */
    public boolean isDataProtectionMailingIndicator() {
        return dataProtectionMailingIndicator;
    }


    /**
     * Sets the dataProtectionMailingIndicator value for this Account.
     * 
     * @param dataProtectionMailingIndicator
     */
    public void setDataProtectionMailingIndicator(boolean dataProtectionMailingIndicator) {
        this.dataProtectionMailingIndicator = dataProtectionMailingIndicator;
    }


    /**
     * Gets the incomeDistributionMethod value for this Account.
     * 
     * @return incomeDistributionMethod
     */
    public com.aurora.webservice.IncomeDistributionMethods getIncomeDistributionMethod() {
        return incomeDistributionMethod;
    }


    /**
     * Sets the incomeDistributionMethod value for this Account.
     * 
     * @param incomeDistributionMethod
     */
    public void setIncomeDistributionMethod(com.aurora.webservice.IncomeDistributionMethods incomeDistributionMethod) {
        this.incomeDistributionMethod = incomeDistributionMethod;
    }


    /**
     * Gets the bankSortCodeIncome value for this Account.
     * 
     * @return bankSortCodeIncome
     */
    public int getBankSortCodeIncome() {
        return bankSortCodeIncome;
    }


    /**
     * Sets the bankSortCodeIncome value for this Account.
     * 
     * @param bankSortCodeIncome
     */
    public void setBankSortCodeIncome(int bankSortCodeIncome) {
        this.bankSortCodeIncome = bankSortCodeIncome;
    }


    /**
     * Gets the bankAccountNumberIncome value for this Account.
     * 
     * @return bankAccountNumberIncome
     */
    public int getBankAccountNumberIncome() {
        return bankAccountNumberIncome;
    }


    /**
     * Sets the bankAccountNumberIncome value for this Account.
     * 
     * @param bankAccountNumberIncome
     */
    public void setBankAccountNumberIncome(int bankAccountNumberIncome) {
        this.bankAccountNumberIncome = bankAccountNumberIncome;
    }


    /**
     * Gets the bankAccountNameIncome value for this Account.
     * 
     * @return bankAccountNameIncome
     */
    public java.lang.String getBankAccountNameIncome() {
        return bankAccountNameIncome;
    }


    /**
     * Sets the bankAccountNameIncome value for this Account.
     * 
     * @param bankAccountNameIncome
     */
    public void setBankAccountNameIncome(java.lang.String bankAccountNameIncome) {
        this.bankAccountNameIncome = bankAccountNameIncome;
    }


    /**
     * Gets the accountShortNameOrBuildingSocietyReferenceIncome value for this Account.
     * 
     * @return accountShortNameOrBuildingSocietyReferenceIncome
     */
    public java.lang.String getAccountShortNameOrBuildingSocietyReferenceIncome() {
        return accountShortNameOrBuildingSocietyReferenceIncome;
    }


    /**
     * Sets the accountShortNameOrBuildingSocietyReferenceIncome value for this Account.
     * 
     * @param accountShortNameOrBuildingSocietyReferenceIncome
     */
    public void setAccountShortNameOrBuildingSocietyReferenceIncome(java.lang.String accountShortNameOrBuildingSocietyReferenceIncome) {
        this.accountShortNameOrBuildingSocietyReferenceIncome = accountShortNameOrBuildingSocietyReferenceIncome;
    }


    /**
     * Gets the bankSortCodeWithdrawal value for this Account.
     * 
     * @return bankSortCodeWithdrawal
     */
    public int getBankSortCodeWithdrawal() {
        return bankSortCodeWithdrawal;
    }


    /**
     * Sets the bankSortCodeWithdrawal value for this Account.
     * 
     * @param bankSortCodeWithdrawal
     */
    public void setBankSortCodeWithdrawal(int bankSortCodeWithdrawal) {
        this.bankSortCodeWithdrawal = bankSortCodeWithdrawal;
    }


    /**
     * Gets the bankAccountNumberWithdrawal value for this Account.
     * 
     * @return bankAccountNumberWithdrawal
     */
    public int getBankAccountNumberWithdrawal() {
        return bankAccountNumberWithdrawal;
    }


    /**
     * Sets the bankAccountNumberWithdrawal value for this Account.
     * 
     * @param bankAccountNumberWithdrawal
     */
    public void setBankAccountNumberWithdrawal(int bankAccountNumberWithdrawal) {
        this.bankAccountNumberWithdrawal = bankAccountNumberWithdrawal;
    }


    /**
     * Gets the bankAccountNameWithdrawal value for this Account.
     * 
     * @return bankAccountNameWithdrawal
     */
    public java.lang.String getBankAccountNameWithdrawal() {
        return bankAccountNameWithdrawal;
    }


    /**
     * Sets the bankAccountNameWithdrawal value for this Account.
     * 
     * @param bankAccountNameWithdrawal
     */
    public void setBankAccountNameWithdrawal(java.lang.String bankAccountNameWithdrawal) {
        this.bankAccountNameWithdrawal = bankAccountNameWithdrawal;
    }


    /**
     * Gets the accountShortNameOrBuildingSocietyReferenceWithdrawal value for this Account.
     * 
     * @return accountShortNameOrBuildingSocietyReferenceWithdrawal
     */
    public java.lang.String getAccountShortNameOrBuildingSocietyReferenceWithdrawal() {
        return accountShortNameOrBuildingSocietyReferenceWithdrawal;
    }


    /**
     * Sets the accountShortNameOrBuildingSocietyReferenceWithdrawal value for this Account.
     * 
     * @param accountShortNameOrBuildingSocietyReferenceWithdrawal
     */
    public void setAccountShortNameOrBuildingSocietyReferenceWithdrawal(java.lang.String accountShortNameOrBuildingSocietyReferenceWithdrawal) {
        this.accountShortNameOrBuildingSocietyReferenceWithdrawal = accountShortNameOrBuildingSocietyReferenceWithdrawal;
    }


    /**
     * Gets the mandatedCompany value for this Account.
     * 
     * @return mandatedCompany
     */
    public java.lang.String getMandatedCompany() {
        return mandatedCompany;
    }


    /**
     * Sets the mandatedCompany value for this Account.
     * 
     * @param mandatedCompany
     */
    public void setMandatedCompany(java.lang.String mandatedCompany) {
        this.mandatedCompany = mandatedCompany;
    }


    /**
     * Gets the mandatedAccount value for this Account.
     * 
     * @return mandatedAccount
     */
    public java.lang.String getMandatedAccount() {
        return mandatedAccount;
    }


    /**
     * Sets the mandatedAccount value for this Account.
     * 
     * @param mandatedAccount
     */
    public void setMandatedAccount(java.lang.String mandatedAccount) {
        this.mandatedAccount = mandatedAccount;
    }


    /**
     * Gets the multipleSignaturesIndicator value for this Account.
     * 
     * @return multipleSignaturesIndicator
     */
    public boolean isMultipleSignaturesIndicator() {
        return multipleSignaturesIndicator;
    }


    /**
     * Sets the multipleSignaturesIndicator value for this Account.
     * 
     * @param multipleSignaturesIndicator
     */
    public void setMultipleSignaturesIndicator(boolean multipleSignaturesIndicator) {
        this.multipleSignaturesIndicator = multipleSignaturesIndicator;
    }


    /**
     * Gets the multipleSignaturesInformation value for this Account.
     * 
     * @return multipleSignaturesInformation
     */
    public java.lang.String getMultipleSignaturesInformation() {
        return multipleSignaturesInformation;
    }


    /**
     * Sets the multipleSignaturesInformation value for this Account.
     * 
     * @param multipleSignaturesInformation
     */
    public void setMultipleSignaturesInformation(java.lang.String multipleSignaturesInformation) {
        this.multipleSignaturesInformation = multipleSignaturesInformation;
    }


    /**
     * Gets the additionalSignatoryName1 value for this Account.
     * 
     * @return additionalSignatoryName1
     */
    public java.lang.String getAdditionalSignatoryName1() {
        return additionalSignatoryName1;
    }


    /**
     * Sets the additionalSignatoryName1 value for this Account.
     * 
     * @param additionalSignatoryName1
     */
    public void setAdditionalSignatoryName1(java.lang.String additionalSignatoryName1) {
        this.additionalSignatoryName1 = additionalSignatoryName1;
    }


    /**
     * Gets the additionalSignatoryTelephone1 value for this Account.
     * 
     * @return additionalSignatoryTelephone1
     */
    public java.lang.String getAdditionalSignatoryTelephone1() {
        return additionalSignatoryTelephone1;
    }


    /**
     * Sets the additionalSignatoryTelephone1 value for this Account.
     * 
     * @param additionalSignatoryTelephone1
     */
    public void setAdditionalSignatoryTelephone1(java.lang.String additionalSignatoryTelephone1) {
        this.additionalSignatoryTelephone1 = additionalSignatoryTelephone1;
    }


    /**
     * Gets the additionalSignatoryPostcode1 value for this Account.
     * 
     * @return additionalSignatoryPostcode1
     */
    public java.lang.String getAdditionalSignatoryPostcode1() {
        return additionalSignatoryPostcode1;
    }


    /**
     * Sets the additionalSignatoryPostcode1 value for this Account.
     * 
     * @param additionalSignatoryPostcode1
     */
    public void setAdditionalSignatoryPostcode1(java.lang.String additionalSignatoryPostcode1) {
        this.additionalSignatoryPostcode1 = additionalSignatoryPostcode1;
    }


    /**
     * Gets the additionalSignatoryName2 value for this Account.
     * 
     * @return additionalSignatoryName2
     */
    public java.lang.String getAdditionalSignatoryName2() {
        return additionalSignatoryName2;
    }


    /**
     * Sets the additionalSignatoryName2 value for this Account.
     * 
     * @param additionalSignatoryName2
     */
    public void setAdditionalSignatoryName2(java.lang.String additionalSignatoryName2) {
        this.additionalSignatoryName2 = additionalSignatoryName2;
    }


    /**
     * Gets the additionalSignatoryTelephone2 value for this Account.
     * 
     * @return additionalSignatoryTelephone2
     */
    public java.lang.String getAdditionalSignatoryTelephone2() {
        return additionalSignatoryTelephone2;
    }


    /**
     * Sets the additionalSignatoryTelephone2 value for this Account.
     * 
     * @param additionalSignatoryTelephone2
     */
    public void setAdditionalSignatoryTelephone2(java.lang.String additionalSignatoryTelephone2) {
        this.additionalSignatoryTelephone2 = additionalSignatoryTelephone2;
    }


    /**
     * Gets the additionalSignatoryPostcode2 value for this Account.
     * 
     * @return additionalSignatoryPostcode2
     */
    public java.lang.String getAdditionalSignatoryPostcode2() {
        return additionalSignatoryPostcode2;
    }


    /**
     * Sets the additionalSignatoryPostcode2 value for this Account.
     * 
     * @param additionalSignatoryPostcode2
     */
    public void setAdditionalSignatoryPostcode2(java.lang.String additionalSignatoryPostcode2) {
        this.additionalSignatoryPostcode2 = additionalSignatoryPostcode2;
    }


    /**
     * Gets the additionalSignatoryName3 value for this Account.
     * 
     * @return additionalSignatoryName3
     */
    public java.lang.String getAdditionalSignatoryName3() {
        return additionalSignatoryName3;
    }


    /**
     * Sets the additionalSignatoryName3 value for this Account.
     * 
     * @param additionalSignatoryName3
     */
    public void setAdditionalSignatoryName3(java.lang.String additionalSignatoryName3) {
        this.additionalSignatoryName3 = additionalSignatoryName3;
    }


    /**
     * Gets the additionalSignatoryTelephone3 value for this Account.
     * 
     * @return additionalSignatoryTelephone3
     */
    public java.lang.String getAdditionalSignatoryTelephone3() {
        return additionalSignatoryTelephone3;
    }


    /**
     * Sets the additionalSignatoryTelephone3 value for this Account.
     * 
     * @param additionalSignatoryTelephone3
     */
    public void setAdditionalSignatoryTelephone3(java.lang.String additionalSignatoryTelephone3) {
        this.additionalSignatoryTelephone3 = additionalSignatoryTelephone3;
    }


    /**
     * Gets the additionalSignatoryPostcode3 value for this Account.
     * 
     * @return additionalSignatoryPostcode3
     */
    public java.lang.String getAdditionalSignatoryPostcode3() {
        return additionalSignatoryPostcode3;
    }


    /**
     * Sets the additionalSignatoryPostcode3 value for this Account.
     * 
     * @param additionalSignatoryPostcode3
     */
    public void setAdditionalSignatoryPostcode3(java.lang.String additionalSignatoryPostcode3) {
        this.additionalSignatoryPostcode3 = additionalSignatoryPostcode3;
    }


    /**
     * Gets the additionalSignatoryName4 value for this Account.
     * 
     * @return additionalSignatoryName4
     */
    public java.lang.String getAdditionalSignatoryName4() {
        return additionalSignatoryName4;
    }


    /**
     * Sets the additionalSignatoryName4 value for this Account.
     * 
     * @param additionalSignatoryName4
     */
    public void setAdditionalSignatoryName4(java.lang.String additionalSignatoryName4) {
        this.additionalSignatoryName4 = additionalSignatoryName4;
    }


    /**
     * Gets the additionalSignatoryTelephone4 value for this Account.
     * 
     * @return additionalSignatoryTelephone4
     */
    public java.lang.String getAdditionalSignatoryTelephone4() {
        return additionalSignatoryTelephone4;
    }


    /**
     * Sets the additionalSignatoryTelephone4 value for this Account.
     * 
     * @param additionalSignatoryTelephone4
     */
    public void setAdditionalSignatoryTelephone4(java.lang.String additionalSignatoryTelephone4) {
        this.additionalSignatoryTelephone4 = additionalSignatoryTelephone4;
    }


    /**
     * Gets the additionalSignatoryPostcode4 value for this Account.
     * 
     * @return additionalSignatoryPostcode4
     */
    public java.lang.String getAdditionalSignatoryPostcode4() {
        return additionalSignatoryPostcode4;
    }


    /**
     * Sets the additionalSignatoryPostcode4 value for this Account.
     * 
     * @param additionalSignatoryPostcode4
     */
    public void setAdditionalSignatoryPostcode4(java.lang.String additionalSignatoryPostcode4) {
        this.additionalSignatoryPostcode4 = additionalSignatoryPostcode4;
    }


    /**
     * Gets the additionalSignatoryName5 value for this Account.
     * 
     * @return additionalSignatoryName5
     */
    public java.lang.String getAdditionalSignatoryName5() {
        return additionalSignatoryName5;
    }


    /**
     * Sets the additionalSignatoryName5 value for this Account.
     * 
     * @param additionalSignatoryName5
     */
    public void setAdditionalSignatoryName5(java.lang.String additionalSignatoryName5) {
        this.additionalSignatoryName5 = additionalSignatoryName5;
    }


    /**
     * Gets the additionalSignatoryTelephone5 value for this Account.
     * 
     * @return additionalSignatoryTelephone5
     */
    public java.lang.String getAdditionalSignatoryTelephone5() {
        return additionalSignatoryTelephone5;
    }


    /**
     * Sets the additionalSignatoryTelephone5 value for this Account.
     * 
     * @param additionalSignatoryTelephone5
     */
    public void setAdditionalSignatoryTelephone5(java.lang.String additionalSignatoryTelephone5) {
        this.additionalSignatoryTelephone5 = additionalSignatoryTelephone5;
    }


    /**
     * Gets the additionalSignatoryPostcode5 value for this Account.
     * 
     * @return additionalSignatoryPostcode5
     */
    public java.lang.String getAdditionalSignatoryPostcode5() {
        return additionalSignatoryPostcode5;
    }


    /**
     * Sets the additionalSignatoryPostcode5 value for this Account.
     * 
     * @param additionalSignatoryPostcode5
     */
    public void setAdditionalSignatoryPostcode5(java.lang.String additionalSignatoryPostcode5) {
        this.additionalSignatoryPostcode5 = additionalSignatoryPostcode5;
    }


    /**
     * Gets the additionalSignatoryName6 value for this Account.
     * 
     * @return additionalSignatoryName6
     */
    public java.lang.String getAdditionalSignatoryName6() {
        return additionalSignatoryName6;
    }


    /**
     * Sets the additionalSignatoryName6 value for this Account.
     * 
     * @param additionalSignatoryName6
     */
    public void setAdditionalSignatoryName6(java.lang.String additionalSignatoryName6) {
        this.additionalSignatoryName6 = additionalSignatoryName6;
    }


    /**
     * Gets the additionalSignatoryTelephone6 value for this Account.
     * 
     * @return additionalSignatoryTelephone6
     */
    public java.lang.String getAdditionalSignatoryTelephone6() {
        return additionalSignatoryTelephone6;
    }


    /**
     * Sets the additionalSignatoryTelephone6 value for this Account.
     * 
     * @param additionalSignatoryTelephone6
     */
    public void setAdditionalSignatoryTelephone6(java.lang.String additionalSignatoryTelephone6) {
        this.additionalSignatoryTelephone6 = additionalSignatoryTelephone6;
    }


    /**
     * Gets the additionalSignatoryPostcode6 value for this Account.
     * 
     * @return additionalSignatoryPostcode6
     */
    public java.lang.String getAdditionalSignatoryPostcode6() {
        return additionalSignatoryPostcode6;
    }


    /**
     * Sets the additionalSignatoryPostcode6 value for this Account.
     * 
     * @param additionalSignatoryPostcode6
     */
    public void setAdditionalSignatoryPostcode6(java.lang.String additionalSignatoryPostcode6) {
        this.additionalSignatoryPostcode6 = additionalSignatoryPostcode6;
    }


    /**
     * Gets the additionalSignatoryName7 value for this Account.
     * 
     * @return additionalSignatoryName7
     */
    public java.lang.String getAdditionalSignatoryName7() {
        return additionalSignatoryName7;
    }


    /**
     * Sets the additionalSignatoryName7 value for this Account.
     * 
     * @param additionalSignatoryName7
     */
    public void setAdditionalSignatoryName7(java.lang.String additionalSignatoryName7) {
        this.additionalSignatoryName7 = additionalSignatoryName7;
    }


    /**
     * Gets the additionalSignatoryTelephone7 value for this Account.
     * 
     * @return additionalSignatoryTelephone7
     */
    public java.lang.String getAdditionalSignatoryTelephone7() {
        return additionalSignatoryTelephone7;
    }


    /**
     * Sets the additionalSignatoryTelephone7 value for this Account.
     * 
     * @param additionalSignatoryTelephone7
     */
    public void setAdditionalSignatoryTelephone7(java.lang.String additionalSignatoryTelephone7) {
        this.additionalSignatoryTelephone7 = additionalSignatoryTelephone7;
    }


    /**
     * Gets the additionalSignatoryPostcode7 value for this Account.
     * 
     * @return additionalSignatoryPostcode7
     */
    public java.lang.String getAdditionalSignatoryPostcode7() {
        return additionalSignatoryPostcode7;
    }


    /**
     * Sets the additionalSignatoryPostcode7 value for this Account.
     * 
     * @param additionalSignatoryPostcode7
     */
    public void setAdditionalSignatoryPostcode7(java.lang.String additionalSignatoryPostcode7) {
        this.additionalSignatoryPostcode7 = additionalSignatoryPostcode7;
    }


    /**
     * Gets the additionalSignatoryName8 value for this Account.
     * 
     * @return additionalSignatoryName8
     */
    public java.lang.String getAdditionalSignatoryName8() {
        return additionalSignatoryName8;
    }


    /**
     * Sets the additionalSignatoryName8 value for this Account.
     * 
     * @param additionalSignatoryName8
     */
    public void setAdditionalSignatoryName8(java.lang.String additionalSignatoryName8) {
        this.additionalSignatoryName8 = additionalSignatoryName8;
    }


    /**
     * Gets the additionalSignatoryTelephone8 value for this Account.
     * 
     * @return additionalSignatoryTelephone8
     */
    public java.lang.String getAdditionalSignatoryTelephone8() {
        return additionalSignatoryTelephone8;
    }


    /**
     * Sets the additionalSignatoryTelephone8 value for this Account.
     * 
     * @param additionalSignatoryTelephone8
     */
    public void setAdditionalSignatoryTelephone8(java.lang.String additionalSignatoryTelephone8) {
        this.additionalSignatoryTelephone8 = additionalSignatoryTelephone8;
    }


    /**
     * Gets the additionalSignatoryPostcode8 value for this Account.
     * 
     * @return additionalSignatoryPostcode8
     */
    public java.lang.String getAdditionalSignatoryPostcode8() {
        return additionalSignatoryPostcode8;
    }


    /**
     * Sets the additionalSignatoryPostcode8 value for this Account.
     * 
     * @param additionalSignatoryPostcode8
     */
    public void setAdditionalSignatoryPostcode8(java.lang.String additionalSignatoryPostcode8) {
        this.additionalSignatoryPostcode8 = additionalSignatoryPostcode8;
    }


    /**
     * Gets the additionalSignatoryName9 value for this Account.
     * 
     * @return additionalSignatoryName9
     */
    public java.lang.String getAdditionalSignatoryName9() {
        return additionalSignatoryName9;
    }


    /**
     * Sets the additionalSignatoryName9 value for this Account.
     * 
     * @param additionalSignatoryName9
     */
    public void setAdditionalSignatoryName9(java.lang.String additionalSignatoryName9) {
        this.additionalSignatoryName9 = additionalSignatoryName9;
    }


    /**
     * Gets the additionalSignatoryTelephone9 value for this Account.
     * 
     * @return additionalSignatoryTelephone9
     */
    public java.lang.String getAdditionalSignatoryTelephone9() {
        return additionalSignatoryTelephone9;
    }


    /**
     * Sets the additionalSignatoryTelephone9 value for this Account.
     * 
     * @param additionalSignatoryTelephone9
     */
    public void setAdditionalSignatoryTelephone9(java.lang.String additionalSignatoryTelephone9) {
        this.additionalSignatoryTelephone9 = additionalSignatoryTelephone9;
    }


    /**
     * Gets the additionalSignatoryPostcode9 value for this Account.
     * 
     * @return additionalSignatoryPostcode9
     */
    public java.lang.String getAdditionalSignatoryPostcode9() {
        return additionalSignatoryPostcode9;
    }


    /**
     * Sets the additionalSignatoryPostcode9 value for this Account.
     * 
     * @param additionalSignatoryPostcode9
     */
    public void setAdditionalSignatoryPostcode9(java.lang.String additionalSignatoryPostcode9) {
        this.additionalSignatoryPostcode9 = additionalSignatoryPostcode9;
    }


    /**
     * Gets the additionalSignatoryName10 value for this Account.
     * 
     * @return additionalSignatoryName10
     */
    public java.lang.String getAdditionalSignatoryName10() {
        return additionalSignatoryName10;
    }


    /**
     * Sets the additionalSignatoryName10 value for this Account.
     * 
     * @param additionalSignatoryName10
     */
    public void setAdditionalSignatoryName10(java.lang.String additionalSignatoryName10) {
        this.additionalSignatoryName10 = additionalSignatoryName10;
    }


    /**
     * Gets the additionalSignatoryTelephone10 value for this Account.
     * 
     * @return additionalSignatoryTelephone10
     */
    public java.lang.String getAdditionalSignatoryTelephone10() {
        return additionalSignatoryTelephone10;
    }


    /**
     * Sets the additionalSignatoryTelephone10 value for this Account.
     * 
     * @param additionalSignatoryTelephone10
     */
    public void setAdditionalSignatoryTelephone10(java.lang.String additionalSignatoryTelephone10) {
        this.additionalSignatoryTelephone10 = additionalSignatoryTelephone10;
    }


    /**
     * Gets the additionalSignatoryPostcode10 value for this Account.
     * 
     * @return additionalSignatoryPostcode10
     */
    public java.lang.String getAdditionalSignatoryPostcode10() {
        return additionalSignatoryPostcode10;
    }


    /**
     * Sets the additionalSignatoryPostcode10 value for this Account.
     * 
     * @param additionalSignatoryPostcode10
     */
    public void setAdditionalSignatoryPostcode10(java.lang.String additionalSignatoryPostcode10) {
        this.additionalSignatoryPostcode10 = additionalSignatoryPostcode10;
    }


    /**
     * Gets the standingTransactionsIndicator value for this Account.
     * 
     * @return standingTransactionsIndicator
     */
    public boolean isStandingTransactionsIndicator() {
        return standingTransactionsIndicator;
    }


    /**
     * Sets the standingTransactionsIndicator value for this Account.
     * 
     * @param standingTransactionsIndicator
     */
    public void setStandingTransactionsIndicator(boolean standingTransactionsIndicator) {
        this.standingTransactionsIndicator = standingTransactionsIndicator;
    }


    /**
     * Gets the cash value for this Account.
     * 
     * @return cash
     */
    public double getCash() {
        return cash;
    }


    /**
     * Sets the cash value for this Account.
     * 
     * @param cash
     */
    public void setCash(double cash) {
        this.cash = cash;
    }


    /**
     * Gets the units value for this Account.
     * 
     * @return units
     */
    public double getUnits() {
        return units;
    }


    /**
     * Sets the units value for this Account.
     * 
     * @param units
     */
    public void setUnits(double units) {
        this.units = units;
    }


    /**
     * Gets the accruedIncome value for this Account.
     * 
     * @return accruedIncome
     */
    public double getAccruedIncome() {
        return accruedIncome;
    }


    /**
     * Sets the accruedIncome value for this Account.
     * 
     * @param accruedIncome
     */
    public void setAccruedIncome(double accruedIncome) {
        this.accruedIncome = accruedIncome;
    }


    /**
     * Gets the balance3 value for this Account.
     * 
     * @return balance3
     */
    public double getBalance3() {
        return balance3;
    }


    /**
     * Sets the balance3 value for this Account.
     * 
     * @param balance3
     */
    public void setBalance3(double balance3) {
        this.balance3 = balance3;
    }


    /**
     * Gets the incomeForDistribution value for this Account.
     * 
     * @return incomeForDistribution
     */
    public double getIncomeForDistribution() {
        return incomeForDistribution;
    }


    /**
     * Sets the incomeForDistribution value for this Account.
     * 
     * @param incomeForDistribution
     */
    public void setIncomeForDistribution(double incomeForDistribution) {
        this.incomeForDistribution = incomeForDistribution;
    }


    /**
     * Gets the balance5 value for this Account.
     * 
     * @return balance5
     */
    public double getBalance5() {
        return balance5;
    }


    /**
     * Sets the balance5 value for this Account.
     * 
     * @param balance5
     */
    public void setBalance5(double balance5) {
        this.balance5 = balance5;
    }


    /**
     * Gets the interestDividendAccrued value for this Account.
     * 
     * @return interestDividendAccrued
     */
    public double getInterestDividendAccrued() {
        return interestDividendAccrued;
    }


    /**
     * Sets the interestDividendAccrued value for this Account.
     * 
     * @param interestDividendAccrued
     */
    public void setInterestDividendAccrued(double interestDividendAccrued) {
        this.interestDividendAccrued = interestDividendAccrued;
    }


    /**
     * Gets the managementExpenseAccrued value for this Account.
     * 
     * @return managementExpenseAccrued
     */
    public double getManagementExpenseAccrued() {
        return managementExpenseAccrued;
    }


    /**
     * Sets the managementExpenseAccrued value for this Account.
     * 
     * @param managementExpenseAccrued
     */
    public void setManagementExpenseAccrued(double managementExpenseAccrued) {
        this.managementExpenseAccrued = managementExpenseAccrued;
    }


    /**
     * Gets the lastStatementCash value for this Account.
     * 
     * @return lastStatementCash
     */
    public double getLastStatementCash() {
        return lastStatementCash;
    }


    /**
     * Sets the lastStatementCash value for this Account.
     * 
     * @param lastStatementCash
     */
    public void setLastStatementCash(double lastStatementCash) {
        this.lastStatementCash = lastStatementCash;
    }


    /**
     * Gets the lastStatementUnits value for this Account.
     * 
     * @return lastStatementUnits
     */
    public double getLastStatementUnits() {
        return lastStatementUnits;
    }


    /**
     * Sets the lastStatementUnits value for this Account.
     * 
     * @param lastStatementUnits
     */
    public void setLastStatementUnits(double lastStatementUnits) {
        this.lastStatementUnits = lastStatementUnits;
    }


    /**
     * Gets the IVSCFID value for this Account.
     * 
     * @return IVSCFID
     */
    public java.lang.String getIVSCFID() {
        return IVSCFID;
    }


    /**
     * Sets the IVSCFID value for this Account.
     * 
     * @param IVSCFID
     */
    public void setIVSCFID(java.lang.String IVSCFID) {
        this.IVSCFID = IVSCFID;
    }


    /**
     * Gets the IVSReferenceNumber value for this Account.
     * 
     * @return IVSReferenceNumber
     */
    public java.lang.String getIVSReferenceNumber() {
        return IVSReferenceNumber;
    }


    /**
     * Sets the IVSReferenceNumber value for this Account.
     * 
     * @param IVSReferenceNumber
     */
    public void setIVSReferenceNumber(java.lang.String IVSReferenceNumber) {
        this.IVSReferenceNumber = IVSReferenceNumber;
    }


    /**
     * Gets the mandateLetterSentIndicator value for this Account.
     * 
     * @return mandateLetterSentIndicator
     */
    public java.lang.String getMandateLetterSentIndicator() {
        return mandateLetterSentIndicator;
    }


    /**
     * Sets the mandateLetterSentIndicator value for this Account.
     * 
     * @param mandateLetterSentIndicator
     */
    public void setMandateLetterSentIndicator(java.lang.String mandateLetterSentIndicator) {
        this.mandateLetterSentIndicator = mandateLetterSentIndicator;
    }


    /**
     * Gets the mandateLetterSentDate value for this Account.
     * 
     * @return mandateLetterSentDate
     */
    public java.lang.String getMandateLetterSentDate() {
        return mandateLetterSentDate;
    }


    /**
     * Sets the mandateLetterSentDate value for this Account.
     * 
     * @param mandateLetterSentDate
     */
    public void setMandateLetterSentDate(java.lang.String mandateLetterSentDate) {
        this.mandateLetterSentDate = mandateLetterSentDate;
    }


    /**
     * Gets the isExternalAccount value for this Account.
     * 
     * @return isExternalAccount
     */
    public boolean isIsExternalAccount() {
        return isExternalAccount;
    }


    /**
     * Sets the isExternalAccount value for this Account.
     * 
     * @param isExternalAccount
     */
    public void setIsExternalAccount(boolean isExternalAccount) {
        this.isExternalAccount = isExternalAccount;
    }


    /**
     * Gets the interestDividendAccruedNonTiered value for this Account.
     * 
     * @return interestDividendAccruedNonTiered
     */
    public double getInterestDividendAccruedNonTiered() {
        return interestDividendAccruedNonTiered;
    }


    /**
     * Sets the interestDividendAccruedNonTiered value for this Account.
     * 
     * @param interestDividendAccruedNonTiered
     */
    public void setInterestDividendAccruedNonTiered(double interestDividendAccruedNonTiered) {
        this.interestDividendAccruedNonTiered = interestDividendAccruedNonTiered;
    }


    /**
     * Gets the shareClassCode value for this Account.
     * 
     * @return shareClassCode
     */
    public int getShareClassCode() {
        return shareClassCode;
    }


    /**
     * Sets the shareClassCode value for this Account.
     * 
     * @param shareClassCode
     */
    public void setShareClassCode(int shareClassCode) {
        this.shareClassCode = shareClassCode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Account)) return false;
        Account other = (Account) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.clientNumber == other.getClientNumber() &&
            ((this.fundCode==null && other.getFundCode()==null) || 
             (this.fundCode!=null &&
              this.fundCode.equals(other.getFundCode()))) &&
            this.contributorTypeCode == other.getContributorTypeCode() &&
            this.subDivisionCode == other.getSubDivisionCode() &&
            this.contributorCode == other.getContributorCode() &&
            this.accountNumber == other.getAccountNumber() &&
            ((this.accountNumberExternal==null && other.getAccountNumberExternal()==null) || 
             (this.accountNumberExternal!=null &&
              this.accountNumberExternal.equals(other.getAccountNumberExternal()))) &&
            ((this.subtitle==null && other.getSubtitle()==null) || 
             (this.subtitle!=null &&
              this.subtitle.equals(other.getSubtitle()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.dateOpened==null && other.getDateOpened()==null) || 
             (this.dateOpened!=null &&
              this.dateOpened.equals(other.getDateOpened()))) &&
            ((this.lastAmendmentDate==null && other.getLastAmendmentDate()==null) || 
             (this.lastAmendmentDate!=null &&
              this.lastAmendmentDate.equals(other.getLastAmendmentDate()))) &&
            this.correspondentCode == other.getCorrespondentCode() &&
            this.beneficialCorrespondentCode == other.getBeneficialCorrespondentCode() &&
            this.dataProtectionMailingIndicator == other.isDataProtectionMailingIndicator() &&
            ((this.incomeDistributionMethod==null && other.getIncomeDistributionMethod()==null) || 
             (this.incomeDistributionMethod!=null &&
              this.incomeDistributionMethod.equals(other.getIncomeDistributionMethod()))) &&
            this.bankSortCodeIncome == other.getBankSortCodeIncome() &&
            this.bankAccountNumberIncome == other.getBankAccountNumberIncome() &&
            ((this.bankAccountNameIncome==null && other.getBankAccountNameIncome()==null) || 
             (this.bankAccountNameIncome!=null &&
              this.bankAccountNameIncome.equals(other.getBankAccountNameIncome()))) &&
            ((this.accountShortNameOrBuildingSocietyReferenceIncome==null && other.getAccountShortNameOrBuildingSocietyReferenceIncome()==null) || 
             (this.accountShortNameOrBuildingSocietyReferenceIncome!=null &&
              this.accountShortNameOrBuildingSocietyReferenceIncome.equals(other.getAccountShortNameOrBuildingSocietyReferenceIncome()))) &&
            this.bankSortCodeWithdrawal == other.getBankSortCodeWithdrawal() &&
            this.bankAccountNumberWithdrawal == other.getBankAccountNumberWithdrawal() &&
            ((this.bankAccountNameWithdrawal==null && other.getBankAccountNameWithdrawal()==null) || 
             (this.bankAccountNameWithdrawal!=null &&
              this.bankAccountNameWithdrawal.equals(other.getBankAccountNameWithdrawal()))) &&
            ((this.accountShortNameOrBuildingSocietyReferenceWithdrawal==null && other.getAccountShortNameOrBuildingSocietyReferenceWithdrawal()==null) || 
             (this.accountShortNameOrBuildingSocietyReferenceWithdrawal!=null &&
              this.accountShortNameOrBuildingSocietyReferenceWithdrawal.equals(other.getAccountShortNameOrBuildingSocietyReferenceWithdrawal()))) &&
            ((this.mandatedCompany==null && other.getMandatedCompany()==null) || 
             (this.mandatedCompany!=null &&
              this.mandatedCompany.equals(other.getMandatedCompany()))) &&
            ((this.mandatedAccount==null && other.getMandatedAccount()==null) || 
             (this.mandatedAccount!=null &&
              this.mandatedAccount.equals(other.getMandatedAccount()))) &&
            this.multipleSignaturesIndicator == other.isMultipleSignaturesIndicator() &&
            ((this.multipleSignaturesInformation==null && other.getMultipleSignaturesInformation()==null) || 
             (this.multipleSignaturesInformation!=null &&
              this.multipleSignaturesInformation.equals(other.getMultipleSignaturesInformation()))) &&
            ((this.additionalSignatoryName1==null && other.getAdditionalSignatoryName1()==null) || 
             (this.additionalSignatoryName1!=null &&
              this.additionalSignatoryName1.equals(other.getAdditionalSignatoryName1()))) &&
            ((this.additionalSignatoryTelephone1==null && other.getAdditionalSignatoryTelephone1()==null) || 
             (this.additionalSignatoryTelephone1!=null &&
              this.additionalSignatoryTelephone1.equals(other.getAdditionalSignatoryTelephone1()))) &&
            ((this.additionalSignatoryPostcode1==null && other.getAdditionalSignatoryPostcode1()==null) || 
             (this.additionalSignatoryPostcode1!=null &&
              this.additionalSignatoryPostcode1.equals(other.getAdditionalSignatoryPostcode1()))) &&
            ((this.additionalSignatoryName2==null && other.getAdditionalSignatoryName2()==null) || 
             (this.additionalSignatoryName2!=null &&
              this.additionalSignatoryName2.equals(other.getAdditionalSignatoryName2()))) &&
            ((this.additionalSignatoryTelephone2==null && other.getAdditionalSignatoryTelephone2()==null) || 
             (this.additionalSignatoryTelephone2!=null &&
              this.additionalSignatoryTelephone2.equals(other.getAdditionalSignatoryTelephone2()))) &&
            ((this.additionalSignatoryPostcode2==null && other.getAdditionalSignatoryPostcode2()==null) || 
             (this.additionalSignatoryPostcode2!=null &&
              this.additionalSignatoryPostcode2.equals(other.getAdditionalSignatoryPostcode2()))) &&
            ((this.additionalSignatoryName3==null && other.getAdditionalSignatoryName3()==null) || 
             (this.additionalSignatoryName3!=null &&
              this.additionalSignatoryName3.equals(other.getAdditionalSignatoryName3()))) &&
            ((this.additionalSignatoryTelephone3==null && other.getAdditionalSignatoryTelephone3()==null) || 
             (this.additionalSignatoryTelephone3!=null &&
              this.additionalSignatoryTelephone3.equals(other.getAdditionalSignatoryTelephone3()))) &&
            ((this.additionalSignatoryPostcode3==null && other.getAdditionalSignatoryPostcode3()==null) || 
             (this.additionalSignatoryPostcode3!=null &&
              this.additionalSignatoryPostcode3.equals(other.getAdditionalSignatoryPostcode3()))) &&
            ((this.additionalSignatoryName4==null && other.getAdditionalSignatoryName4()==null) || 
             (this.additionalSignatoryName4!=null &&
              this.additionalSignatoryName4.equals(other.getAdditionalSignatoryName4()))) &&
            ((this.additionalSignatoryTelephone4==null && other.getAdditionalSignatoryTelephone4()==null) || 
             (this.additionalSignatoryTelephone4!=null &&
              this.additionalSignatoryTelephone4.equals(other.getAdditionalSignatoryTelephone4()))) &&
            ((this.additionalSignatoryPostcode4==null && other.getAdditionalSignatoryPostcode4()==null) || 
             (this.additionalSignatoryPostcode4!=null &&
              this.additionalSignatoryPostcode4.equals(other.getAdditionalSignatoryPostcode4()))) &&
            ((this.additionalSignatoryName5==null && other.getAdditionalSignatoryName5()==null) || 
             (this.additionalSignatoryName5!=null &&
              this.additionalSignatoryName5.equals(other.getAdditionalSignatoryName5()))) &&
            ((this.additionalSignatoryTelephone5==null && other.getAdditionalSignatoryTelephone5()==null) || 
             (this.additionalSignatoryTelephone5!=null &&
              this.additionalSignatoryTelephone5.equals(other.getAdditionalSignatoryTelephone5()))) &&
            ((this.additionalSignatoryPostcode5==null && other.getAdditionalSignatoryPostcode5()==null) || 
             (this.additionalSignatoryPostcode5!=null &&
              this.additionalSignatoryPostcode5.equals(other.getAdditionalSignatoryPostcode5()))) &&
            ((this.additionalSignatoryName6==null && other.getAdditionalSignatoryName6()==null) || 
             (this.additionalSignatoryName6!=null &&
              this.additionalSignatoryName6.equals(other.getAdditionalSignatoryName6()))) &&
            ((this.additionalSignatoryTelephone6==null && other.getAdditionalSignatoryTelephone6()==null) || 
             (this.additionalSignatoryTelephone6!=null &&
              this.additionalSignatoryTelephone6.equals(other.getAdditionalSignatoryTelephone6()))) &&
            ((this.additionalSignatoryPostcode6==null && other.getAdditionalSignatoryPostcode6()==null) || 
             (this.additionalSignatoryPostcode6!=null &&
              this.additionalSignatoryPostcode6.equals(other.getAdditionalSignatoryPostcode6()))) &&
            ((this.additionalSignatoryName7==null && other.getAdditionalSignatoryName7()==null) || 
             (this.additionalSignatoryName7!=null &&
              this.additionalSignatoryName7.equals(other.getAdditionalSignatoryName7()))) &&
            ((this.additionalSignatoryTelephone7==null && other.getAdditionalSignatoryTelephone7()==null) || 
             (this.additionalSignatoryTelephone7!=null &&
              this.additionalSignatoryTelephone7.equals(other.getAdditionalSignatoryTelephone7()))) &&
            ((this.additionalSignatoryPostcode7==null && other.getAdditionalSignatoryPostcode7()==null) || 
             (this.additionalSignatoryPostcode7!=null &&
              this.additionalSignatoryPostcode7.equals(other.getAdditionalSignatoryPostcode7()))) &&
            ((this.additionalSignatoryName8==null && other.getAdditionalSignatoryName8()==null) || 
             (this.additionalSignatoryName8!=null &&
              this.additionalSignatoryName8.equals(other.getAdditionalSignatoryName8()))) &&
            ((this.additionalSignatoryTelephone8==null && other.getAdditionalSignatoryTelephone8()==null) || 
             (this.additionalSignatoryTelephone8!=null &&
              this.additionalSignatoryTelephone8.equals(other.getAdditionalSignatoryTelephone8()))) &&
            ((this.additionalSignatoryPostcode8==null && other.getAdditionalSignatoryPostcode8()==null) || 
             (this.additionalSignatoryPostcode8!=null &&
              this.additionalSignatoryPostcode8.equals(other.getAdditionalSignatoryPostcode8()))) &&
            ((this.additionalSignatoryName9==null && other.getAdditionalSignatoryName9()==null) || 
             (this.additionalSignatoryName9!=null &&
              this.additionalSignatoryName9.equals(other.getAdditionalSignatoryName9()))) &&
            ((this.additionalSignatoryTelephone9==null && other.getAdditionalSignatoryTelephone9()==null) || 
             (this.additionalSignatoryTelephone9!=null &&
              this.additionalSignatoryTelephone9.equals(other.getAdditionalSignatoryTelephone9()))) &&
            ((this.additionalSignatoryPostcode9==null && other.getAdditionalSignatoryPostcode9()==null) || 
             (this.additionalSignatoryPostcode9!=null &&
              this.additionalSignatoryPostcode9.equals(other.getAdditionalSignatoryPostcode9()))) &&
            ((this.additionalSignatoryName10==null && other.getAdditionalSignatoryName10()==null) || 
             (this.additionalSignatoryName10!=null &&
              this.additionalSignatoryName10.equals(other.getAdditionalSignatoryName10()))) &&
            ((this.additionalSignatoryTelephone10==null && other.getAdditionalSignatoryTelephone10()==null) || 
             (this.additionalSignatoryTelephone10!=null &&
              this.additionalSignatoryTelephone10.equals(other.getAdditionalSignatoryTelephone10()))) &&
            ((this.additionalSignatoryPostcode10==null && other.getAdditionalSignatoryPostcode10()==null) || 
             (this.additionalSignatoryPostcode10!=null &&
              this.additionalSignatoryPostcode10.equals(other.getAdditionalSignatoryPostcode10()))) &&
            this.standingTransactionsIndicator == other.isStandingTransactionsIndicator() &&
            this.cash == other.getCash() &&
            this.units == other.getUnits() &&
            this.accruedIncome == other.getAccruedIncome() &&
            this.balance3 == other.getBalance3() &&
            this.incomeForDistribution == other.getIncomeForDistribution() &&
            this.balance5 == other.getBalance5() &&
            this.interestDividendAccrued == other.getInterestDividendAccrued() &&
            this.managementExpenseAccrued == other.getManagementExpenseAccrued() &&
            this.lastStatementCash == other.getLastStatementCash() &&
            this.lastStatementUnits == other.getLastStatementUnits() &&
            ((this.IVSCFID==null && other.getIVSCFID()==null) || 
             (this.IVSCFID!=null &&
              this.IVSCFID.equals(other.getIVSCFID()))) &&
            ((this.IVSReferenceNumber==null && other.getIVSReferenceNumber()==null) || 
             (this.IVSReferenceNumber!=null &&
              this.IVSReferenceNumber.equals(other.getIVSReferenceNumber()))) &&
            ((this.mandateLetterSentIndicator==null && other.getMandateLetterSentIndicator()==null) || 
             (this.mandateLetterSentIndicator!=null &&
              this.mandateLetterSentIndicator.equals(other.getMandateLetterSentIndicator()))) &&
            ((this.mandateLetterSentDate==null && other.getMandateLetterSentDate()==null) || 
             (this.mandateLetterSentDate!=null &&
              this.mandateLetterSentDate.equals(other.getMandateLetterSentDate()))) &&
            this.isExternalAccount == other.isIsExternalAccount() &&
            this.interestDividendAccruedNonTiered == other.getInterestDividendAccruedNonTiered() &&
            this.shareClassCode == other.getShareClassCode();
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
        _hashCode += getClientNumber();
        if (getFundCode() != null) {
            _hashCode += getFundCode().hashCode();
        }
        _hashCode += getContributorTypeCode();
        _hashCode += getSubDivisionCode();
        _hashCode += getContributorCode();
        _hashCode += getAccountNumber();
        if (getAccountNumberExternal() != null) {
            _hashCode += getAccountNumberExternal().hashCode();
        }
        if (getSubtitle() != null) {
            _hashCode += getSubtitle().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getDateOpened() != null) {
            _hashCode += getDateOpened().hashCode();
        }
        if (getLastAmendmentDate() != null) {
            _hashCode += getLastAmendmentDate().hashCode();
        }
        _hashCode += getCorrespondentCode();
        _hashCode += getBeneficialCorrespondentCode();
        _hashCode += (isDataProtectionMailingIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIncomeDistributionMethod() != null) {
            _hashCode += getIncomeDistributionMethod().hashCode();
        }
        _hashCode += getBankSortCodeIncome();
        _hashCode += getBankAccountNumberIncome();
        if (getBankAccountNameIncome() != null) {
            _hashCode += getBankAccountNameIncome().hashCode();
        }
        if (getAccountShortNameOrBuildingSocietyReferenceIncome() != null) {
            _hashCode += getAccountShortNameOrBuildingSocietyReferenceIncome().hashCode();
        }
        _hashCode += getBankSortCodeWithdrawal();
        _hashCode += getBankAccountNumberWithdrawal();
        if (getBankAccountNameWithdrawal() != null) {
            _hashCode += getBankAccountNameWithdrawal().hashCode();
        }
        if (getAccountShortNameOrBuildingSocietyReferenceWithdrawal() != null) {
            _hashCode += getAccountShortNameOrBuildingSocietyReferenceWithdrawal().hashCode();
        }
        if (getMandatedCompany() != null) {
            _hashCode += getMandatedCompany().hashCode();
        }
        if (getMandatedAccount() != null) {
            _hashCode += getMandatedAccount().hashCode();
        }
        _hashCode += (isMultipleSignaturesIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMultipleSignaturesInformation() != null) {
            _hashCode += getMultipleSignaturesInformation().hashCode();
        }
        if (getAdditionalSignatoryName1() != null) {
            _hashCode += getAdditionalSignatoryName1().hashCode();
        }
        if (getAdditionalSignatoryTelephone1() != null) {
            _hashCode += getAdditionalSignatoryTelephone1().hashCode();
        }
        if (getAdditionalSignatoryPostcode1() != null) {
            _hashCode += getAdditionalSignatoryPostcode1().hashCode();
        }
        if (getAdditionalSignatoryName2() != null) {
            _hashCode += getAdditionalSignatoryName2().hashCode();
        }
        if (getAdditionalSignatoryTelephone2() != null) {
            _hashCode += getAdditionalSignatoryTelephone2().hashCode();
        }
        if (getAdditionalSignatoryPostcode2() != null) {
            _hashCode += getAdditionalSignatoryPostcode2().hashCode();
        }
        if (getAdditionalSignatoryName3() != null) {
            _hashCode += getAdditionalSignatoryName3().hashCode();
        }
        if (getAdditionalSignatoryTelephone3() != null) {
            _hashCode += getAdditionalSignatoryTelephone3().hashCode();
        }
        if (getAdditionalSignatoryPostcode3() != null) {
            _hashCode += getAdditionalSignatoryPostcode3().hashCode();
        }
        if (getAdditionalSignatoryName4() != null) {
            _hashCode += getAdditionalSignatoryName4().hashCode();
        }
        if (getAdditionalSignatoryTelephone4() != null) {
            _hashCode += getAdditionalSignatoryTelephone4().hashCode();
        }
        if (getAdditionalSignatoryPostcode4() != null) {
            _hashCode += getAdditionalSignatoryPostcode4().hashCode();
        }
        if (getAdditionalSignatoryName5() != null) {
            _hashCode += getAdditionalSignatoryName5().hashCode();
        }
        if (getAdditionalSignatoryTelephone5() != null) {
            _hashCode += getAdditionalSignatoryTelephone5().hashCode();
        }
        if (getAdditionalSignatoryPostcode5() != null) {
            _hashCode += getAdditionalSignatoryPostcode5().hashCode();
        }
        if (getAdditionalSignatoryName6() != null) {
            _hashCode += getAdditionalSignatoryName6().hashCode();
        }
        if (getAdditionalSignatoryTelephone6() != null) {
            _hashCode += getAdditionalSignatoryTelephone6().hashCode();
        }
        if (getAdditionalSignatoryPostcode6() != null) {
            _hashCode += getAdditionalSignatoryPostcode6().hashCode();
        }
        if (getAdditionalSignatoryName7() != null) {
            _hashCode += getAdditionalSignatoryName7().hashCode();
        }
        if (getAdditionalSignatoryTelephone7() != null) {
            _hashCode += getAdditionalSignatoryTelephone7().hashCode();
        }
        if (getAdditionalSignatoryPostcode7() != null) {
            _hashCode += getAdditionalSignatoryPostcode7().hashCode();
        }
        if (getAdditionalSignatoryName8() != null) {
            _hashCode += getAdditionalSignatoryName8().hashCode();
        }
        if (getAdditionalSignatoryTelephone8() != null) {
            _hashCode += getAdditionalSignatoryTelephone8().hashCode();
        }
        if (getAdditionalSignatoryPostcode8() != null) {
            _hashCode += getAdditionalSignatoryPostcode8().hashCode();
        }
        if (getAdditionalSignatoryName9() != null) {
            _hashCode += getAdditionalSignatoryName9().hashCode();
        }
        if (getAdditionalSignatoryTelephone9() != null) {
            _hashCode += getAdditionalSignatoryTelephone9().hashCode();
        }
        if (getAdditionalSignatoryPostcode9() != null) {
            _hashCode += getAdditionalSignatoryPostcode9().hashCode();
        }
        if (getAdditionalSignatoryName10() != null) {
            _hashCode += getAdditionalSignatoryName10().hashCode();
        }
        if (getAdditionalSignatoryTelephone10() != null) {
            _hashCode += getAdditionalSignatoryTelephone10().hashCode();
        }
        if (getAdditionalSignatoryPostcode10() != null) {
            _hashCode += getAdditionalSignatoryPostcode10().hashCode();
        }
        _hashCode += (isStandingTransactionsIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += new Double(getCash()).hashCode();
        _hashCode += new Double(getUnits()).hashCode();
        _hashCode += new Double(getAccruedIncome()).hashCode();
        _hashCode += new Double(getBalance3()).hashCode();
        _hashCode += new Double(getIncomeForDistribution()).hashCode();
        _hashCode += new Double(getBalance5()).hashCode();
        _hashCode += new Double(getInterestDividendAccrued()).hashCode();
        _hashCode += new Double(getManagementExpenseAccrued()).hashCode();
        _hashCode += new Double(getLastStatementCash()).hashCode();
        _hashCode += new Double(getLastStatementUnits()).hashCode();
        if (getIVSCFID() != null) {
            _hashCode += getIVSCFID().hashCode();
        }
        if (getIVSReferenceNumber() != null) {
            _hashCode += getIVSReferenceNumber().hashCode();
        }
        if (getMandateLetterSentIndicator() != null) {
            _hashCode += getMandateLetterSentIndicator().hashCode();
        }
        if (getMandateLetterSentDate() != null) {
            _hashCode += getMandateLetterSentDate().hashCode();
        }
        _hashCode += (isIsExternalAccount() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += new Double(getInterestDividendAccruedNonTiered()).hashCode();
        _hashCode += getShareClassCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Account.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Account"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clientNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ClientNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FundCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contributorTypeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ContributorTypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subDivisionCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "SubDivisionCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contributorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ContributorCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccountNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountNumberExternal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccountNumberExternal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subtitle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Subtitle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccountStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateOpened");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DateOpened"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastAmendmentDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "LastAmendmentDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correspondentCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "CorrespondentCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("beneficialCorrespondentCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BeneficialCorrespondentCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataProtectionMailingIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DataProtectionMailingIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incomeDistributionMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeDistributionMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeDistributionMethods"));
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
        elemField.setFieldName("bankAccountNameIncome");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankAccountNameIncome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountShortNameOrBuildingSocietyReferenceIncome");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccountShortNameOrBuildingSocietyReferenceIncome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankSortCodeWithdrawal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankSortCodeWithdrawal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankAccountNumberWithdrawal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankAccountNumberWithdrawal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankAccountNameWithdrawal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankAccountNameWithdrawal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountShortNameOrBuildingSocietyReferenceWithdrawal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccountShortNameOrBuildingSocietyReferenceWithdrawal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mandatedCompany");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MandatedCompany"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mandatedAccount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MandatedAccount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("multipleSignaturesIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MultipleSignaturesIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("multipleSignaturesInformation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MultipleSignaturesInformation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryName1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryName1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryTelephone1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryTelephone1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryPostcode1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryPostcode1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryName2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryName2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryTelephone2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryTelephone2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryPostcode2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryPostcode2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryName3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryName3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryTelephone3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryTelephone3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryPostcode3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryPostcode3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryName4");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryName4"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryTelephone4");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryTelephone4"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryPostcode4");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryPostcode4"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryName5");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryName5"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryTelephone5");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryTelephone5"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryPostcode5");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryPostcode5"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryName6");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryName6"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryTelephone6");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryTelephone6"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryPostcode6");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryPostcode6"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryName7");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryName7"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryTelephone7");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryTelephone7"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryPostcode7");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryPostcode7"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryName8");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryName8"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryTelephone8");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryTelephone8"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryPostcode8");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryPostcode8"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryName9");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryName9"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryTelephone9");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryTelephone9"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryPostcode9");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryPostcode9"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryName10");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryName10"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryTelephone10");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryTelephone10"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalSignatoryPostcode10");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AdditionalSignatoryPostcode10"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("standingTransactionsIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "StandingTransactionsIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("interestDividendAccrued");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "InterestDividendAccrued"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("managementExpenseAccrued");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ManagementExpenseAccrued"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastStatementCash");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "LastStatementCash"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastStatementUnits");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "LastStatementUnits"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IVSCFID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IVSCFID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("IVSReferenceNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IVSReferenceNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mandateLetterSentIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MandateLetterSentIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mandateLetterSentDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MandateLetterSentDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isExternalAccount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IsExternalAccount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interestDividendAccruedNonTiered");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "InterestDividendAccruedNonTiered"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shareClassCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClassCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

	@Override
	public String toString() {
		return "Account [clientNumber=" + clientNumber + "\nfundCode="
				+ fundCode + "\ncontributorTypeCode=" + contributorTypeCode
				+ "\nsubDivisionCode=" + subDivisionCode + "\ncontributorCode="
				+ contributorCode + "\naccountNumber=" + accountNumber
				+ "\naccountNumberExternal=" + accountNumberExternal
				+ "\nsubtitle=" + subtitle + "\nstatus=" + status
				+ "\ndateOpened=" + dateOpened + "\nlastAmendmentDate="
				+ lastAmendmentDate + "\ncorrespondentCode="
				+ correspondentCode + "\nbeneficialCorrespondentCode="
				+ beneficialCorrespondentCode
				+ "\ndataProtectionMailingIndicator="
				+ dataProtectionMailingIndicator
				+ "\nincomeDistributionMethod=" + incomeDistributionMethod
				+ "\nbankSortCodeIncome=" + bankSortCodeIncome
				+ "\nbankAccountNumberIncome=" + bankAccountNumberIncome
				+ "\nbankAccountNameIncome=" + bankAccountNameIncome
				+ "\naccountShortNameOrBuildingSocietyReferenceIncome="
				+ accountShortNameOrBuildingSocietyReferenceIncome
				+ "\nbankSortCodeWithdrawal=" + bankSortCodeWithdrawal
				+ "\nbankAccountNumberWithdrawal="
				+ bankAccountNumberWithdrawal + "\nbankAccountNameWithdrawal="
				+ bankAccountNameWithdrawal
				+ "\naccountShortNameOrBuildingSocietyReferenceWithdrawal="
				+ accountShortNameOrBuildingSocietyReferenceWithdrawal
				+ "\nmandatedCompany=" + mandatedCompany + "\nmandatedAccount="
				+ mandatedAccount + "\nmultipleSignaturesIndicator="
				+ multipleSignaturesIndicator
				+ "\nmultipleSignaturesInformation="
				+ multipleSignaturesInformation + "\nadditionalSignatoryName1="
				+ additionalSignatoryName1 + "\nadditionalSignatoryTelephone1="
				+ additionalSignatoryTelephone1
				+ "\nadditionalSignatoryPostcode1="
				+ additionalSignatoryPostcode1 + "\nadditionalSignatoryName2="
				+ additionalSignatoryName2 + "\nadditionalSignatoryTelephone2="
				+ additionalSignatoryTelephone2
				+ "\nadditionalSignatoryPostcode2="
				+ additionalSignatoryPostcode2 + "\nadditionalSignatoryName3="
				+ additionalSignatoryName3 + "\nadditionalSignatoryTelephone3="
				+ additionalSignatoryTelephone3
				+ "\nadditionalSignatoryPostcode3="
				+ additionalSignatoryPostcode3 + "\nadditionalSignatoryName4="
				+ additionalSignatoryName4 + "\nadditionalSignatoryTelephone4="
				+ additionalSignatoryTelephone4
				+ "\nadditionalSignatoryPostcode4="
				+ additionalSignatoryPostcode4 + "\nadditionalSignatoryName5="
				+ additionalSignatoryName5 + "\nadditionalSignatoryTelephone5="
				+ additionalSignatoryTelephone5
				+ "\nadditionalSignatoryPostcode5="
				+ additionalSignatoryPostcode5 + "\nadditionalSignatoryName6="
				+ additionalSignatoryName6 + "\nadditionalSignatoryTelephone6="
				+ additionalSignatoryTelephone6
				+ "\nadditionalSignatoryPostcode6="
				+ additionalSignatoryPostcode6 + "\nadditionalSignatoryName7="
				+ additionalSignatoryName7 + "\nadditionalSignatoryTelephone7="
				+ additionalSignatoryTelephone7
				+ "\nadditionalSignatoryPostcode7="
				+ additionalSignatoryPostcode7 + "\nadditionalSignatoryName8="
				+ additionalSignatoryName8 + "\nadditionalSignatoryTelephone8="
				+ additionalSignatoryTelephone8
				+ "\nadditionalSignatoryPostcode8="
				+ additionalSignatoryPostcode8 + "\nadditionalSignatoryName9="
				+ additionalSignatoryName9 + "\nadditionalSignatoryTelephone9="
				+ additionalSignatoryTelephone9
				+ "\nadditionalSignatoryPostcode9="
				+ additionalSignatoryPostcode9 + "\nadditionalSignatoryName10="
				+ additionalSignatoryName10
				+ "\nadditionalSignatoryTelephone10="
				+ additionalSignatoryTelephone10
				+ "\nadditionalSignatoryPostcode10="
				+ additionalSignatoryPostcode10
				+ "\nstandingTransactionsIndicator="
				+ standingTransactionsIndicator + "\ncash=" + cash + "\nunits="
				+ units + "\naccruedIncome=" + accruedIncome + "\nbalance3="
				+ balance3 + "\nincomeForDistribution=" + incomeForDistribution
				+ "\nbalance5=" + balance5 + "\ninterestDividendAccrued="
				+ interestDividendAccrued + "\nmanagementExpenseAccrued="
				+ managementExpenseAccrued + "\nlastStatementCash="
				+ lastStatementCash + "\nlastStatementUnits="
				+ lastStatementUnits + "\nIVSCFID=" + IVSCFID
				+ "\nIVSReferenceNumber=" + IVSReferenceNumber
				+ "\nmandateLetterSentIndicator=" + mandateLetterSentIndicator
				+ "\nmandateLetterSentDate=" + mandateLetterSentDate
				+ "\nisExternalAccount=" + isExternalAccount
				+ "\ninterestDividendAccruedNonTiered="
				+ interestDividendAccruedNonTiered + "\nshareClassCode="
				+ shareClassCode + "\n__equalsCalc=" + __equalsCalc
				+ "\n__hashCodeCalc=" + __hashCodeCalc + "]";
	}

}
