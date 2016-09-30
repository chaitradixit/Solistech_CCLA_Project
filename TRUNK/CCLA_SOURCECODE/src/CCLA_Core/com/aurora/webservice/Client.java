/**
 * Client.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class Client  implements java.io.Serializable {
    
	

	private int contributorTypeCode;

    private int subDivisionCode;

    private int contributorCode;

    private int clientNumber;

    private java.lang.String name;

    private com.aurora.webservice.Address address;

    private com.aurora.webservice.ExemptionTypes exemptionType;

    private java.lang.String exemptionNumber;

    private boolean isSatisfactoryDocumentationPresent;

    private int defaultCorrespondentNumber;

    private int correspondentCodeBeneficialDefault;

    private boolean subTotalIndicator;

    private boolean incomeDistributionReportIndicator;

    private boolean incomeDistributionReportPaperIndicator;

    private boolean incomeDistributionReportDiskIndicator;

    private boolean incomeDistributionReportEmailIndicator;

    private int numberOfStatementsDeposit;

    private int numberOfStatementsUnitised;

    private int statementMonths1;

    private int statementMonths2;

    private int statementMonths3;

    private int statementMonths4;

    private boolean statementMonthsAllIndicator;

    private boolean depositStatementsIndicator;

    private boolean depositStatementsPaperIndicator;

    private boolean depositStatementsDiskIndicator;

    private boolean depositStatementsEmailIndicator;

    private boolean investmentStatementsPaperIndicator;

    private boolean investmentStatementsDiskIndicator;

    private boolean investmentStatementsEmailIndicator;

    private boolean bulkDepositStatementsIndicator;

    private boolean depositAndWithdrawalBooksIndicator;

    private java.lang.String lastStatementProduced;

    private boolean automaticStationaryOrderedIndicator;

    private com.aurora.webservice.EncryptionTypes encryptionMethod;

    private boolean externalUserAccessAllowedIndicator;

    private boolean quarterlyReportingClientIndicator;

    private java.lang.String quarterlyReportingManagerInitials;

    private boolean quarterlyUpdateIndicator;

    private int marketingGroup;

    private int marketingSubGroup;

    private int accountGroup;

    private boolean auditCertificateSendIndicator;

    private boolean auditCertificatePrintIndicator;

    private int auditCertificateDueDay;

    private int auditCertificateDueMonth;

    private boolean auditCertificateClientAuthority;

    private java.lang.String auditCertificateDestination;

    private java.lang.String auditPriceType;

    private java.lang.String authorityHeldDate;

    private java.lang.String auditorReference;

    private com.aurora.webservice.Address auditorAddress;

    private boolean multipleSignatoryIndicator;

    private java.lang.String multipleSignatoryInformation;

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

    private boolean dataProtectionMailingGroupIndicator;

    private java.lang.String dateOpened;

    private java.lang.String IVSCFID;

    private java.lang.String IVSReferenceNumber;

    public Client() {
    }

    public Client(
           int contributorTypeCode,
           int subDivisionCode,
           int contributorCode,
           int clientNumber,
           java.lang.String name,
           com.aurora.webservice.Address address,
           com.aurora.webservice.ExemptionTypes exemptionType,
           java.lang.String exemptionNumber,
           boolean isSatisfactoryDocumentationPresent,
           int defaultCorrespondentNumber,
           int correspondentCodeBeneficialDefault,
           boolean subTotalIndicator,
           boolean incomeDistributionReportIndicator,
           boolean incomeDistributionReportPaperIndicator,
           boolean incomeDistributionReportDiskIndicator,
           boolean incomeDistributionReportEmailIndicator,
           int numberOfStatementsDeposit,
           int numberOfStatementsUnitised,
           int statementMonths1,
           int statementMonths2,
           int statementMonths3,
           int statementMonths4,
           boolean statementMonthsAllIndicator,
           boolean depositStatementsIndicator,
           boolean depositStatementsPaperIndicator,
           boolean depositStatementsDiskIndicator,
           boolean depositStatementsEmailIndicator,
           boolean investmentStatementsPaperIndicator,
           boolean investmentStatementsDiskIndicator,
           boolean investmentStatementsEmailIndicator,
           boolean bulkDepositStatementsIndicator,
           boolean depositAndWithdrawalBooksIndicator,
           java.lang.String lastStatementProduced,
           boolean automaticStationaryOrderedIndicator,
           com.aurora.webservice.EncryptionTypes encryptionMethod,
           boolean externalUserAccessAllowedIndicator,
           boolean quarterlyReportingClientIndicator,
           java.lang.String quarterlyReportingManagerInitials,
           boolean quarterlyUpdateIndicator,
           int marketingGroup,
           int marketingSubGroup,
           int accountGroup,
           boolean auditCertificateSendIndicator,
           boolean auditCertificatePrintIndicator,
           int auditCertificateDueDay,
           int auditCertificateDueMonth,
           boolean auditCertificateClientAuthority,
           java.lang.String auditCertificateDestination,
           java.lang.String auditPriceType,
           java.lang.String authorityHeldDate,
           java.lang.String auditorReference,
           com.aurora.webservice.Address auditorAddress,
           boolean multipleSignatoryIndicator,
           java.lang.String multipleSignatoryInformation,
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
           boolean dataProtectionMailingGroupIndicator,
           java.lang.String dateOpened,
           java.lang.String IVSCFID,
           java.lang.String IVSReferenceNumber) {
           this.contributorTypeCode = contributorTypeCode;
           this.subDivisionCode = subDivisionCode;
           this.contributorCode = contributorCode;
           this.clientNumber = clientNumber;
           this.name = name;
           this.address = address;
           this.exemptionType = exemptionType;
           this.exemptionNumber = exemptionNumber;
           this.isSatisfactoryDocumentationPresent = isSatisfactoryDocumentationPresent;
           this.defaultCorrespondentNumber = defaultCorrespondentNumber;
           this.correspondentCodeBeneficialDefault = correspondentCodeBeneficialDefault;
           this.subTotalIndicator = subTotalIndicator;
           this.incomeDistributionReportIndicator = incomeDistributionReportIndicator;
           this.incomeDistributionReportPaperIndicator = incomeDistributionReportPaperIndicator;
           this.incomeDistributionReportDiskIndicator = incomeDistributionReportDiskIndicator;
           this.incomeDistributionReportEmailIndicator = incomeDistributionReportEmailIndicator;
           this.numberOfStatementsDeposit = numberOfStatementsDeposit;
           this.numberOfStatementsUnitised = numberOfStatementsUnitised;
           this.statementMonths1 = statementMonths1;
           this.statementMonths2 = statementMonths2;
           this.statementMonths3 = statementMonths3;
           this.statementMonths4 = statementMonths4;
           this.statementMonthsAllIndicator = statementMonthsAllIndicator;
           this.depositStatementsIndicator = depositStatementsIndicator;
           this.depositStatementsPaperIndicator = depositStatementsPaperIndicator;
           this.depositStatementsDiskIndicator = depositStatementsDiskIndicator;
           this.depositStatementsEmailIndicator = depositStatementsEmailIndicator;
           this.investmentStatementsPaperIndicator = investmentStatementsPaperIndicator;
           this.investmentStatementsDiskIndicator = investmentStatementsDiskIndicator;
           this.investmentStatementsEmailIndicator = investmentStatementsEmailIndicator;
           this.bulkDepositStatementsIndicator = bulkDepositStatementsIndicator;
           this.depositAndWithdrawalBooksIndicator = depositAndWithdrawalBooksIndicator;
           this.lastStatementProduced = lastStatementProduced;
           this.automaticStationaryOrderedIndicator = automaticStationaryOrderedIndicator;
           this.encryptionMethod = encryptionMethod;
           this.externalUserAccessAllowedIndicator = externalUserAccessAllowedIndicator;
           this.quarterlyReportingClientIndicator = quarterlyReportingClientIndicator;
           this.quarterlyReportingManagerInitials = quarterlyReportingManagerInitials;
           this.quarterlyUpdateIndicator = quarterlyUpdateIndicator;
           this.marketingGroup = marketingGroup;
           this.marketingSubGroup = marketingSubGroup;
           this.accountGroup = accountGroup;
           this.auditCertificateSendIndicator = auditCertificateSendIndicator;
           this.auditCertificatePrintIndicator = auditCertificatePrintIndicator;
           this.auditCertificateDueDay = auditCertificateDueDay;
           this.auditCertificateDueMonth = auditCertificateDueMonth;
           this.auditCertificateClientAuthority = auditCertificateClientAuthority;
           this.auditCertificateDestination = auditCertificateDestination;
           this.auditPriceType = auditPriceType;
           this.authorityHeldDate = authorityHeldDate;
           this.auditorReference = auditorReference;
           this.auditorAddress = auditorAddress;
           this.multipleSignatoryIndicator = multipleSignatoryIndicator;
           this.multipleSignatoryInformation = multipleSignatoryInformation;
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
           this.dataProtectionMailingGroupIndicator = dataProtectionMailingGroupIndicator;
           this.dateOpened = dateOpened;
           this.IVSCFID = IVSCFID;
           this.IVSReferenceNumber = IVSReferenceNumber;
    }


    /**
     * Gets the contributorTypeCode value for this Client.
     * 
     * @return contributorTypeCode
     */
    public int getContributorTypeCode() {
        return contributorTypeCode;
    }


    /**
     * Sets the contributorTypeCode value for this Client.
     * 
     * @param contributorTypeCode
     */
    public void setContributorTypeCode(int contributorTypeCode) {
        this.contributorTypeCode = contributorTypeCode;
    }


    /**
     * Gets the subDivisionCode value for this Client.
     * 
     * @return subDivisionCode
     */
    public int getSubDivisionCode() {
        return subDivisionCode;
    }


    /**
     * Sets the subDivisionCode value for this Client.
     * 
     * @param subDivisionCode
     */
    public void setSubDivisionCode(int subDivisionCode) {
        this.subDivisionCode = subDivisionCode;
    }


    /**
     * Gets the contributorCode value for this Client.
     * 
     * @return contributorCode
     */
    public int getContributorCode() {
        return contributorCode;
    }


    /**
     * Sets the contributorCode value for this Client.
     * 
     * @param contributorCode
     */
    public void setContributorCode(int contributorCode) {
        this.contributorCode = contributorCode;
    }


    /**
     * Gets the clientNumber value for this Client.
     * 
     * @return clientNumber
     */
    public int getClientNumber() {
        return clientNumber;
    }


    /**
     * Sets the clientNumber value for this Client.
     * 
     * @param clientNumber
     */
    public void setClientNumber(int clientNumber) {
        this.clientNumber = clientNumber;
    }


    /**
     * Gets the name value for this Client.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Client.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the address value for this Client.
     * 
     * @return address
     */
    public com.aurora.webservice.Address getAddress() {
        return address;
    }


    /**
     * Sets the address value for this Client.
     * 
     * @param address
     */
    public void setAddress(com.aurora.webservice.Address address) {
        this.address = address;
    }


    /**
     * Gets the exemptionType value for this Client.
     * 
     * @return exemptionType
     */
    public com.aurora.webservice.ExemptionTypes getExemptionType() {
        return exemptionType;
    }


    /**
     * Sets the exemptionType value for this Client.
     * 
     * @param exemptionType
     */
    public void setExemptionType(com.aurora.webservice.ExemptionTypes exemptionType) {
        this.exemptionType = exemptionType;
    }


    /**
     * Gets the exemptionNumber value for this Client.
     * 
     * @return exemptionNumber
     */
    public java.lang.String getExemptionNumber() {
        return exemptionNumber;
    }


    /**
     * Sets the exemptionNumber value for this Client.
     * 
     * @param exemptionNumber
     */
    public void setExemptionNumber(java.lang.String exemptionNumber) {
        this.exemptionNumber = exemptionNumber;
    }


    /**
     * Gets the isSatisfactoryDocumentationPresent value for this Client.
     * 
     * @return isSatisfactoryDocumentationPresent
     */
    public boolean isIsSatisfactoryDocumentationPresent() {
        return isSatisfactoryDocumentationPresent;
    }


    /**
     * Sets the isSatisfactoryDocumentationPresent value for this Client.
     * 
     * @param isSatisfactoryDocumentationPresent
     */
    public void setIsSatisfactoryDocumentationPresent(boolean isSatisfactoryDocumentationPresent) {
        this.isSatisfactoryDocumentationPresent = isSatisfactoryDocumentationPresent;
    }


    /**
     * Gets the defaultCorrespondentNumber value for this Client.
     * 
     * @return defaultCorrespondentNumber
     */
    public int getDefaultCorrespondentNumber() {
        return defaultCorrespondentNumber;
    }


    /**
     * Sets the defaultCorrespondentNumber value for this Client.
     * 
     * @param defaultCorrespondentNumber
     */
    public void setDefaultCorrespondentNumber(int defaultCorrespondentNumber) {
        this.defaultCorrespondentNumber = defaultCorrespondentNumber;
    }


    /**
     * Gets the correspondentCodeBeneficialDefault value for this Client.
     * 
     * @return correspondentCodeBeneficialDefault
     */
    public int getCorrespondentCodeBeneficialDefault() {
        return correspondentCodeBeneficialDefault;
    }


    /**
     * Sets the correspondentCodeBeneficialDefault value for this Client.
     * 
     * @param correspondentCodeBeneficialDefault
     */
    public void setCorrespondentCodeBeneficialDefault(int correspondentCodeBeneficialDefault) {
        this.correspondentCodeBeneficialDefault = correspondentCodeBeneficialDefault;
    }


    /**
     * Gets the subTotalIndicator value for this Client.
     * 
     * @return subTotalIndicator
     */
    public boolean isSubTotalIndicator() {
        return subTotalIndicator;
    }


    /**
     * Sets the subTotalIndicator value for this Client.
     * 
     * @param subTotalIndicator
     */
    public void setSubTotalIndicator(boolean subTotalIndicator) {
        this.subTotalIndicator = subTotalIndicator;
    }


    /**
     * Gets the incomeDistributionReportIndicator value for this Client.
     * 
     * @return incomeDistributionReportIndicator
     */
    public boolean isIncomeDistributionReportIndicator() {
        return incomeDistributionReportIndicator;
    }


    /**
     * Sets the incomeDistributionReportIndicator value for this Client.
     * 
     * @param incomeDistributionReportIndicator
     */
    public void setIncomeDistributionReportIndicator(boolean incomeDistributionReportIndicator) {
        this.incomeDistributionReportIndicator = incomeDistributionReportIndicator;
    }


    /**
     * Gets the incomeDistributionReportPaperIndicator value for this Client.
     * 
     * @return incomeDistributionReportPaperIndicator
     */
    public boolean isIncomeDistributionReportPaperIndicator() {
        return incomeDistributionReportPaperIndicator;
    }


    /**
     * Sets the incomeDistributionReportPaperIndicator value for this Client.
     * 
     * @param incomeDistributionReportPaperIndicator
     */
    public void setIncomeDistributionReportPaperIndicator(boolean incomeDistributionReportPaperIndicator) {
        this.incomeDistributionReportPaperIndicator = incomeDistributionReportPaperIndicator;
    }


    /**
     * Gets the incomeDistributionReportDiskIndicator value for this Client.
     * 
     * @return incomeDistributionReportDiskIndicator
     */
    public boolean isIncomeDistributionReportDiskIndicator() {
        return incomeDistributionReportDiskIndicator;
    }


    /**
     * Sets the incomeDistributionReportDiskIndicator value for this Client.
     * 
     * @param incomeDistributionReportDiskIndicator
     */
    public void setIncomeDistributionReportDiskIndicator(boolean incomeDistributionReportDiskIndicator) {
        this.incomeDistributionReportDiskIndicator = incomeDistributionReportDiskIndicator;
    }


    /**
     * Gets the incomeDistributionReportEmailIndicator value for this Client.
     * 
     * @return incomeDistributionReportEmailIndicator
     */
    public boolean isIncomeDistributionReportEmailIndicator() {
        return incomeDistributionReportEmailIndicator;
    }


    /**
     * Sets the incomeDistributionReportEmailIndicator value for this Client.
     * 
     * @param incomeDistributionReportEmailIndicator
     */
    public void setIncomeDistributionReportEmailIndicator(boolean incomeDistributionReportEmailIndicator) {
        this.incomeDistributionReportEmailIndicator = incomeDistributionReportEmailIndicator;
    }


    /**
     * Gets the numberOfStatementsDeposit value for this Client.
     * 
     * @return numberOfStatementsDeposit
     */
    public int getNumberOfStatementsDeposit() {
        return numberOfStatementsDeposit;
    }


    /**
     * Sets the numberOfStatementsDeposit value for this Client.
     * 
     * @param numberOfStatementsDeposit
     */
    public void setNumberOfStatementsDeposit(int numberOfStatementsDeposit) {
        this.numberOfStatementsDeposit = numberOfStatementsDeposit;
    }


    /**
     * Gets the numberOfStatementsUnitised value for this Client.
     * 
     * @return numberOfStatementsUnitised
     */
    public int getNumberOfStatementsUnitised() {
        return numberOfStatementsUnitised;
    }


    /**
     * Sets the numberOfStatementsUnitised value for this Client.
     * 
     * @param numberOfStatementsUnitised
     */
    public void setNumberOfStatementsUnitised(int numberOfStatementsUnitised) {
        this.numberOfStatementsUnitised = numberOfStatementsUnitised;
    }


    /**
     * Gets the statementMonths1 value for this Client.
     * 
     * @return statementMonths1
     */
    public int getStatementMonths1() {
        return statementMonths1;
    }


    /**
     * Sets the statementMonths1 value for this Client.
     * 
     * @param statementMonths1
     */
    public void setStatementMonths1(int statementMonths1) {
        this.statementMonths1 = statementMonths1;
    }


    /**
     * Gets the statementMonths2 value for this Client.
     * 
     * @return statementMonths2
     */
    public int getStatementMonths2() {
        return statementMonths2;
    }


    /**
     * Sets the statementMonths2 value for this Client.
     * 
     * @param statementMonths2
     */
    public void setStatementMonths2(int statementMonths2) {
        this.statementMonths2 = statementMonths2;
    }


    /**
     * Gets the statementMonths3 value for this Client.
     * 
     * @return statementMonths3
     */
    public int getStatementMonths3() {
        return statementMonths3;
    }


    /**
     * Sets the statementMonths3 value for this Client.
     * 
     * @param statementMonths3
     */
    public void setStatementMonths3(int statementMonths3) {
        this.statementMonths3 = statementMonths3;
    }


    /**
     * Gets the statementMonths4 value for this Client.
     * 
     * @return statementMonths4
     */
    public int getStatementMonths4() {
        return statementMonths4;
    }


    /**
     * Sets the statementMonths4 value for this Client.
     * 
     * @param statementMonths4
     */
    public void setStatementMonths4(int statementMonths4) {
        this.statementMonths4 = statementMonths4;
    }


    /**
     * Gets the statementMonthsAllIndicator value for this Client.
     * 
     * @return statementMonthsAllIndicator
     */
    public boolean isStatementMonthsAllIndicator() {
        return statementMonthsAllIndicator;
    }


    /**
     * Sets the statementMonthsAllIndicator value for this Client.
     * 
     * @param statementMonthsAllIndicator
     */
    public void setStatementMonthsAllIndicator(boolean statementMonthsAllIndicator) {
        this.statementMonthsAllIndicator = statementMonthsAllIndicator;
    }


    /**
     * Gets the depositStatementsIndicator value for this Client.
     * 
     * @return depositStatementsIndicator
     */
    public boolean isDepositStatementsIndicator() {
        return depositStatementsIndicator;
    }


    /**
     * Sets the depositStatementsIndicator value for this Client.
     * 
     * @param depositStatementsIndicator
     */
    public void setDepositStatementsIndicator(boolean depositStatementsIndicator) {
        this.depositStatementsIndicator = depositStatementsIndicator;
    }


    /**
     * Gets the depositStatementsPaperIndicator value for this Client.
     * 
     * @return depositStatementsPaperIndicator
     */
    public boolean isDepositStatementsPaperIndicator() {
        return depositStatementsPaperIndicator;
    }


    /**
     * Sets the depositStatementsPaperIndicator value for this Client.
     * 
     * @param depositStatementsPaperIndicator
     */
    public void setDepositStatementsPaperIndicator(boolean depositStatementsPaperIndicator) {
        this.depositStatementsPaperIndicator = depositStatementsPaperIndicator;
    }


    /**
     * Gets the depositStatementsDiskIndicator value for this Client.
     * 
     * @return depositStatementsDiskIndicator
     */
    public boolean isDepositStatementsDiskIndicator() {
        return depositStatementsDiskIndicator;
    }


    /**
     * Sets the depositStatementsDiskIndicator value for this Client.
     * 
     * @param depositStatementsDiskIndicator
     */
    public void setDepositStatementsDiskIndicator(boolean depositStatementsDiskIndicator) {
        this.depositStatementsDiskIndicator = depositStatementsDiskIndicator;
    }


    /**
     * Gets the depositStatementsEmailIndicator value for this Client.
     * 
     * @return depositStatementsEmailIndicator
     */
    public boolean isDepositStatementsEmailIndicator() {
        return depositStatementsEmailIndicator;
    }


    /**
     * Sets the depositStatementsEmailIndicator value for this Client.
     * 
     * @param depositStatementsEmailIndicator
     */
    public void setDepositStatementsEmailIndicator(boolean depositStatementsEmailIndicator) {
        this.depositStatementsEmailIndicator = depositStatementsEmailIndicator;
    }


    /**
     * Gets the investmentStatementsPaperIndicator value for this Client.
     * 
     * @return investmentStatementsPaperIndicator
     */
    public boolean isInvestmentStatementsPaperIndicator() {
        return investmentStatementsPaperIndicator;
    }


    /**
     * Sets the investmentStatementsPaperIndicator value for this Client.
     * 
     * @param investmentStatementsPaperIndicator
     */
    public void setInvestmentStatementsPaperIndicator(boolean investmentStatementsPaperIndicator) {
        this.investmentStatementsPaperIndicator = investmentStatementsPaperIndicator;
    }


    /**
     * Gets the investmentStatementsDiskIndicator value for this Client.
     * 
     * @return investmentStatementsDiskIndicator
     */
    public boolean isInvestmentStatementsDiskIndicator() {
        return investmentStatementsDiskIndicator;
    }


    /**
     * Sets the investmentStatementsDiskIndicator value for this Client.
     * 
     * @param investmentStatementsDiskIndicator
     */
    public void setInvestmentStatementsDiskIndicator(boolean investmentStatementsDiskIndicator) {
        this.investmentStatementsDiskIndicator = investmentStatementsDiskIndicator;
    }


    /**
     * Gets the investmentStatementsEmailIndicator value for this Client.
     * 
     * @return investmentStatementsEmailIndicator
     */
    public boolean isInvestmentStatementsEmailIndicator() {
        return investmentStatementsEmailIndicator;
    }


    /**
     * Sets the investmentStatementsEmailIndicator value for this Client.
     * 
     * @param investmentStatementsEmailIndicator
     */
    public void setInvestmentStatementsEmailIndicator(boolean investmentStatementsEmailIndicator) {
        this.investmentStatementsEmailIndicator = investmentStatementsEmailIndicator;
    }


    /**
     * Gets the bulkDepositStatementsIndicator value for this Client.
     * 
     * @return bulkDepositStatementsIndicator
     */
    public boolean isBulkDepositStatementsIndicator() {
        return bulkDepositStatementsIndicator;
    }


    /**
     * Sets the bulkDepositStatementsIndicator value for this Client.
     * 
     * @param bulkDepositStatementsIndicator
     */
    public void setBulkDepositStatementsIndicator(boolean bulkDepositStatementsIndicator) {
        this.bulkDepositStatementsIndicator = bulkDepositStatementsIndicator;
    }


    /**
     * Gets the depositAndWithdrawalBooksIndicator value for this Client.
     * 
     * @return depositAndWithdrawalBooksIndicator
     */
    public boolean isDepositAndWithdrawalBooksIndicator() {
        return depositAndWithdrawalBooksIndicator;
    }


    /**
     * Sets the depositAndWithdrawalBooksIndicator value for this Client.
     * 
     * @param depositAndWithdrawalBooksIndicator
     */
    public void setDepositAndWithdrawalBooksIndicator(boolean depositAndWithdrawalBooksIndicator) {
        this.depositAndWithdrawalBooksIndicator = depositAndWithdrawalBooksIndicator;
    }


    /**
     * Gets the lastStatementProduced value for this Client.
     * 
     * @return lastStatementProduced
     */
    public java.lang.String getLastStatementProduced() {
        return lastStatementProduced;
    }


    /**
     * Sets the lastStatementProduced value for this Client.
     * 
     * @param lastStatementProduced
     */
    public void setLastStatementProduced(java.lang.String lastStatementProduced) {
        this.lastStatementProduced = lastStatementProduced;
    }


    /**
     * Gets the automaticStationaryOrderedIndicator value for this Client.
     * 
     * @return automaticStationaryOrderedIndicator
     */
    public boolean isAutomaticStationaryOrderedIndicator() {
        return automaticStationaryOrderedIndicator;
    }


    /**
     * Sets the automaticStationaryOrderedIndicator value for this Client.
     * 
     * @param automaticStationaryOrderedIndicator
     */
    public void setAutomaticStationaryOrderedIndicator(boolean automaticStationaryOrderedIndicator) {
        this.automaticStationaryOrderedIndicator = automaticStationaryOrderedIndicator;
    }


    /**
     * Gets the encryptionMethod value for this Client.
     * 
     * @return encryptionMethod
     */
    public com.aurora.webservice.EncryptionTypes getEncryptionMethod() {
        return encryptionMethod;
    }


    /**
     * Sets the encryptionMethod value for this Client.
     * 
     * @param encryptionMethod
     */
    public void setEncryptionMethod(com.aurora.webservice.EncryptionTypes encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }


    /**
     * Gets the externalUserAccessAllowedIndicator value for this Client.
     * 
     * @return externalUserAccessAllowedIndicator
     */
    public boolean isExternalUserAccessAllowedIndicator() {
        return externalUserAccessAllowedIndicator;
    }


    /**
     * Sets the externalUserAccessAllowedIndicator value for this Client.
     * 
     * @param externalUserAccessAllowedIndicator
     */
    public void setExternalUserAccessAllowedIndicator(boolean externalUserAccessAllowedIndicator) {
        this.externalUserAccessAllowedIndicator = externalUserAccessAllowedIndicator;
    }


    /**
     * Gets the quarterlyReportingClientIndicator value for this Client.
     * 
     * @return quarterlyReportingClientIndicator
     */
    public boolean isQuarterlyReportingClientIndicator() {
        return quarterlyReportingClientIndicator;
    }


    /**
     * Sets the quarterlyReportingClientIndicator value for this Client.
     * 
     * @param quarterlyReportingClientIndicator
     */
    public void setQuarterlyReportingClientIndicator(boolean quarterlyReportingClientIndicator) {
        this.quarterlyReportingClientIndicator = quarterlyReportingClientIndicator;
    }


    /**
     * Gets the quarterlyReportingManagerInitials value for this Client.
     * 
     * @return quarterlyReportingManagerInitials
     */
    public java.lang.String getQuarterlyReportingManagerInitials() {
        return quarterlyReportingManagerInitials;
    }


    /**
     * Sets the quarterlyReportingManagerInitials value for this Client.
     * 
     * @param quarterlyReportingManagerInitials
     */
    public void setQuarterlyReportingManagerInitials(java.lang.String quarterlyReportingManagerInitials) {
        this.quarterlyReportingManagerInitials = quarterlyReportingManagerInitials;
    }


    /**
     * Gets the quarterlyUpdateIndicator value for this Client.
     * 
     * @return quarterlyUpdateIndicator
     */
    public boolean isQuarterlyUpdateIndicator() {
        return quarterlyUpdateIndicator;
    }


    /**
     * Sets the quarterlyUpdateIndicator value for this Client.
     * 
     * @param quarterlyUpdateIndicator
     */
    public void setQuarterlyUpdateIndicator(boolean quarterlyUpdateIndicator) {
        this.quarterlyUpdateIndicator = quarterlyUpdateIndicator;
    }


    /**
     * Gets the marketingGroup value for this Client.
     * 
     * @return marketingGroup
     */
    public int getMarketingGroup() {
        return marketingGroup;
    }


    /**
     * Sets the marketingGroup value for this Client.
     * 
     * @param marketingGroup
     */
    public void setMarketingGroup(int marketingGroup) {
        this.marketingGroup = marketingGroup;
    }


    /**
     * Gets the marketingSubGroup value for this Client.
     * 
     * @return marketingSubGroup
     */
    public int getMarketingSubGroup() {
        return marketingSubGroup;
    }


    /**
     * Sets the marketingSubGroup value for this Client.
     * 
     * @param marketingSubGroup
     */
    public void setMarketingSubGroup(int marketingSubGroup) {
        this.marketingSubGroup = marketingSubGroup;
    }


    /**
     * Gets the accountGroup value for this Client.
     * 
     * @return accountGroup
     */
    public int getAccountGroup() {
        return accountGroup;
    }


    /**
     * Sets the accountGroup value for this Client.
     * 
     * @param accountGroup
     */
    public void setAccountGroup(int accountGroup) {
        this.accountGroup = accountGroup;
    }


    /**
     * Gets the auditCertificateSendIndicator value for this Client.
     * 
     * @return auditCertificateSendIndicator
     */
    public boolean isAuditCertificateSendIndicator() {
        return auditCertificateSendIndicator;
    }


    /**
     * Sets the auditCertificateSendIndicator value for this Client.
     * 
     * @param auditCertificateSendIndicator
     */
    public void setAuditCertificateSendIndicator(boolean auditCertificateSendIndicator) {
        this.auditCertificateSendIndicator = auditCertificateSendIndicator;
    }


    /**
     * Gets the auditCertificatePrintIndicator value for this Client.
     * 
     * @return auditCertificatePrintIndicator
     */
    public boolean isAuditCertificatePrintIndicator() {
        return auditCertificatePrintIndicator;
    }


    /**
     * Sets the auditCertificatePrintIndicator value for this Client.
     * 
     * @param auditCertificatePrintIndicator
     */
    public void setAuditCertificatePrintIndicator(boolean auditCertificatePrintIndicator) {
        this.auditCertificatePrintIndicator = auditCertificatePrintIndicator;
    }


    /**
     * Gets the auditCertificateDueDay value for this Client.
     * 
     * @return auditCertificateDueDay
     */
    public int getAuditCertificateDueDay() {
        return auditCertificateDueDay;
    }


    /**
     * Sets the auditCertificateDueDay value for this Client.
     * 
     * @param auditCertificateDueDay
     */
    public void setAuditCertificateDueDay(int auditCertificateDueDay) {
        this.auditCertificateDueDay = auditCertificateDueDay;
    }


    /**
     * Gets the auditCertificateDueMonth value for this Client.
     * 
     * @return auditCertificateDueMonth
     */
    public int getAuditCertificateDueMonth() {
        return auditCertificateDueMonth;
    }


    /**
     * Sets the auditCertificateDueMonth value for this Client.
     * 
     * @param auditCertificateDueMonth
     */
    public void setAuditCertificateDueMonth(int auditCertificateDueMonth) {
        this.auditCertificateDueMonth = auditCertificateDueMonth;
    }


    /**
     * Gets the auditCertificateClientAuthority value for this Client.
     * 
     * @return auditCertificateClientAuthority
     */
    public boolean isAuditCertificateClientAuthority() {
        return auditCertificateClientAuthority;
    }


    /**
     * Sets the auditCertificateClientAuthority value for this Client.
     * 
     * @param auditCertificateClientAuthority
     */
    public void setAuditCertificateClientAuthority(boolean auditCertificateClientAuthority) {
        this.auditCertificateClientAuthority = auditCertificateClientAuthority;
    }


    /**
     * Gets the auditCertificateDestination value for this Client.
     * 
     * @return auditCertificateDestination
     */
    public java.lang.String getAuditCertificateDestination() {
        return auditCertificateDestination;
    }


    /**
     * Sets the auditCertificateDestination value for this Client.
     * 
     * @param auditCertificateDestination
     */
    public void setAuditCertificateDestination(java.lang.String auditCertificateDestination) {
        this.auditCertificateDestination = auditCertificateDestination;
    }


    /**
     * Gets the auditPriceType value for this Client.
     * 
     * @return auditPriceType
     */
    public java.lang.String getAuditPriceType() {
        return auditPriceType;
    }


    /**
     * Sets the auditPriceType value for this Client.
     * 
     * @param auditPriceType
     */
    public void setAuditPriceType(java.lang.String auditPriceType) {
        this.auditPriceType = auditPriceType;
    }


    /**
     * Gets the authorityHeldDate value for this Client.
     * 
     * @return authorityHeldDate
     */
    public java.lang.String getAuthorityHeldDate() {
        return authorityHeldDate;
    }


    /**
     * Sets the authorityHeldDate value for this Client.
     * 
     * @param authorityHeldDate
     */
    public void setAuthorityHeldDate(java.lang.String authorityHeldDate) {
        this.authorityHeldDate = authorityHeldDate;
    }


    /**
     * Gets the auditorReference value for this Client.
     * 
     * @return auditorReference
     */
    public java.lang.String getAuditorReference() {
        return auditorReference;
    }


    /**
     * Sets the auditorReference value for this Client.
     * 
     * @param auditorReference
     */
    public void setAuditorReference(java.lang.String auditorReference) {
        this.auditorReference = auditorReference;
    }


    /**
     * Gets the auditorAddress value for this Client.
     * 
     * @return auditorAddress
     */
    public com.aurora.webservice.Address getAuditorAddress() {
        return auditorAddress;
    }


    /**
     * Sets the auditorAddress value for this Client.
     * 
     * @param auditorAddress
     */
    public void setAuditorAddress(com.aurora.webservice.Address auditorAddress) {
        this.auditorAddress = auditorAddress;
    }


    /**
     * Gets the multipleSignatoryIndicator value for this Client.
     * 
     * @return multipleSignatoryIndicator
     */
    public boolean isMultipleSignatoryIndicator() {
        return multipleSignatoryIndicator;
    }


    /**
     * Sets the multipleSignatoryIndicator value for this Client.
     * 
     * @param multipleSignatoryIndicator
     */
    public void setMultipleSignatoryIndicator(boolean multipleSignatoryIndicator) {
        this.multipleSignatoryIndicator = multipleSignatoryIndicator;
    }


    /**
     * Gets the multipleSignatoryInformation value for this Client.
     * 
     * @return multipleSignatoryInformation
     */
    public java.lang.String getMultipleSignatoryInformation() {
        return multipleSignatoryInformation;
    }


    /**
     * Sets the multipleSignatoryInformation value for this Client.
     * 
     * @param multipleSignatoryInformation
     */
    public void setMultipleSignatoryInformation(java.lang.String multipleSignatoryInformation) {
        this.multipleSignatoryInformation = multipleSignatoryInformation;
    }


    /**
     * Gets the additionalSignatoryName1 value for this Client.
     * 
     * @return additionalSignatoryName1
     */
    public java.lang.String getAdditionalSignatoryName1() {
        return additionalSignatoryName1;
    }


    /**
     * Sets the additionalSignatoryName1 value for this Client.
     * 
     * @param additionalSignatoryName1
     */
    public void setAdditionalSignatoryName1(java.lang.String additionalSignatoryName1) {
        this.additionalSignatoryName1 = additionalSignatoryName1;
    }


    /**
     * Gets the additionalSignatoryTelephone1 value for this Client.
     * 
     * @return additionalSignatoryTelephone1
     */
    public java.lang.String getAdditionalSignatoryTelephone1() {
        return additionalSignatoryTelephone1;
    }


    /**
     * Sets the additionalSignatoryTelephone1 value for this Client.
     * 
     * @param additionalSignatoryTelephone1
     */
    public void setAdditionalSignatoryTelephone1(java.lang.String additionalSignatoryTelephone1) {
        this.additionalSignatoryTelephone1 = additionalSignatoryTelephone1;
    }


    /**
     * Gets the additionalSignatoryPostcode1 value for this Client.
     * 
     * @return additionalSignatoryPostcode1
     */
    public java.lang.String getAdditionalSignatoryPostcode1() {
        return additionalSignatoryPostcode1;
    }


    /**
     * Sets the additionalSignatoryPostcode1 value for this Client.
     * 
     * @param additionalSignatoryPostcode1
     */
    public void setAdditionalSignatoryPostcode1(java.lang.String additionalSignatoryPostcode1) {
        this.additionalSignatoryPostcode1 = additionalSignatoryPostcode1;
    }


    /**
     * Gets the additionalSignatoryName2 value for this Client.
     * 
     * @return additionalSignatoryName2
     */
    public java.lang.String getAdditionalSignatoryName2() {
        return additionalSignatoryName2;
    }


    /**
     * Sets the additionalSignatoryName2 value for this Client.
     * 
     * @param additionalSignatoryName2
     */
    public void setAdditionalSignatoryName2(java.lang.String additionalSignatoryName2) {
        this.additionalSignatoryName2 = additionalSignatoryName2;
    }


    /**
     * Gets the additionalSignatoryTelephone2 value for this Client.
     * 
     * @return additionalSignatoryTelephone2
     */
    public java.lang.String getAdditionalSignatoryTelephone2() {
        return additionalSignatoryTelephone2;
    }


    /**
     * Sets the additionalSignatoryTelephone2 value for this Client.
     * 
     * @param additionalSignatoryTelephone2
     */
    public void setAdditionalSignatoryTelephone2(java.lang.String additionalSignatoryTelephone2) {
        this.additionalSignatoryTelephone2 = additionalSignatoryTelephone2;
    }


    /**
     * Gets the additionalSignatoryPostcode2 value for this Client.
     * 
     * @return additionalSignatoryPostcode2
     */
    public java.lang.String getAdditionalSignatoryPostcode2() {
        return additionalSignatoryPostcode2;
    }


    /**
     * Sets the additionalSignatoryPostcode2 value for this Client.
     * 
     * @param additionalSignatoryPostcode2
     */
    public void setAdditionalSignatoryPostcode2(java.lang.String additionalSignatoryPostcode2) {
        this.additionalSignatoryPostcode2 = additionalSignatoryPostcode2;
    }


    /**
     * Gets the additionalSignatoryName3 value for this Client.
     * 
     * @return additionalSignatoryName3
     */
    public java.lang.String getAdditionalSignatoryName3() {
        return additionalSignatoryName3;
    }


    /**
     * Sets the additionalSignatoryName3 value for this Client.
     * 
     * @param additionalSignatoryName3
     */
    public void setAdditionalSignatoryName3(java.lang.String additionalSignatoryName3) {
        this.additionalSignatoryName3 = additionalSignatoryName3;
    }


    /**
     * Gets the additionalSignatoryTelephone3 value for this Client.
     * 
     * @return additionalSignatoryTelephone3
     */
    public java.lang.String getAdditionalSignatoryTelephone3() {
        return additionalSignatoryTelephone3;
    }


    /**
     * Sets the additionalSignatoryTelephone3 value for this Client.
     * 
     * @param additionalSignatoryTelephone3
     */
    public void setAdditionalSignatoryTelephone3(java.lang.String additionalSignatoryTelephone3) {
        this.additionalSignatoryTelephone3 = additionalSignatoryTelephone3;
    }


    /**
     * Gets the additionalSignatoryPostcode3 value for this Client.
     * 
     * @return additionalSignatoryPostcode3
     */
    public java.lang.String getAdditionalSignatoryPostcode3() {
        return additionalSignatoryPostcode3;
    }


    /**
     * Sets the additionalSignatoryPostcode3 value for this Client.
     * 
     * @param additionalSignatoryPostcode3
     */
    public void setAdditionalSignatoryPostcode3(java.lang.String additionalSignatoryPostcode3) {
        this.additionalSignatoryPostcode3 = additionalSignatoryPostcode3;
    }


    /**
     * Gets the additionalSignatoryName4 value for this Client.
     * 
     * @return additionalSignatoryName4
     */
    public java.lang.String getAdditionalSignatoryName4() {
        return additionalSignatoryName4;
    }


    /**
     * Sets the additionalSignatoryName4 value for this Client.
     * 
     * @param additionalSignatoryName4
     */
    public void setAdditionalSignatoryName4(java.lang.String additionalSignatoryName4) {
        this.additionalSignatoryName4 = additionalSignatoryName4;
    }


    /**
     * Gets the additionalSignatoryTelephone4 value for this Client.
     * 
     * @return additionalSignatoryTelephone4
     */
    public java.lang.String getAdditionalSignatoryTelephone4() {
        return additionalSignatoryTelephone4;
    }


    /**
     * Sets the additionalSignatoryTelephone4 value for this Client.
     * 
     * @param additionalSignatoryTelephone4
     */
    public void setAdditionalSignatoryTelephone4(java.lang.String additionalSignatoryTelephone4) {
        this.additionalSignatoryTelephone4 = additionalSignatoryTelephone4;
    }


    /**
     * Gets the additionalSignatoryPostcode4 value for this Client.
     * 
     * @return additionalSignatoryPostcode4
     */
    public java.lang.String getAdditionalSignatoryPostcode4() {
        return additionalSignatoryPostcode4;
    }


    /**
     * Sets the additionalSignatoryPostcode4 value for this Client.
     * 
     * @param additionalSignatoryPostcode4
     */
    public void setAdditionalSignatoryPostcode4(java.lang.String additionalSignatoryPostcode4) {
        this.additionalSignatoryPostcode4 = additionalSignatoryPostcode4;
    }


    /**
     * Gets the additionalSignatoryName5 value for this Client.
     * 
     * @return additionalSignatoryName5
     */
    public java.lang.String getAdditionalSignatoryName5() {
        return additionalSignatoryName5;
    }


    /**
     * Sets the additionalSignatoryName5 value for this Client.
     * 
     * @param additionalSignatoryName5
     */
    public void setAdditionalSignatoryName5(java.lang.String additionalSignatoryName5) {
        this.additionalSignatoryName5 = additionalSignatoryName5;
    }


    /**
     * Gets the additionalSignatoryTelephone5 value for this Client.
     * 
     * @return additionalSignatoryTelephone5
     */
    public java.lang.String getAdditionalSignatoryTelephone5() {
        return additionalSignatoryTelephone5;
    }


    /**
     * Sets the additionalSignatoryTelephone5 value for this Client.
     * 
     * @param additionalSignatoryTelephone5
     */
    public void setAdditionalSignatoryTelephone5(java.lang.String additionalSignatoryTelephone5) {
        this.additionalSignatoryTelephone5 = additionalSignatoryTelephone5;
    }


    /**
     * Gets the additionalSignatoryPostcode5 value for this Client.
     * 
     * @return additionalSignatoryPostcode5
     */
    public java.lang.String getAdditionalSignatoryPostcode5() {
        return additionalSignatoryPostcode5;
    }


    /**
     * Sets the additionalSignatoryPostcode5 value for this Client.
     * 
     * @param additionalSignatoryPostcode5
     */
    public void setAdditionalSignatoryPostcode5(java.lang.String additionalSignatoryPostcode5) {
        this.additionalSignatoryPostcode5 = additionalSignatoryPostcode5;
    }


    /**
     * Gets the additionalSignatoryName6 value for this Client.
     * 
     * @return additionalSignatoryName6
     */
    public java.lang.String getAdditionalSignatoryName6() {
        return additionalSignatoryName6;
    }


    /**
     * Sets the additionalSignatoryName6 value for this Client.
     * 
     * @param additionalSignatoryName6
     */
    public void setAdditionalSignatoryName6(java.lang.String additionalSignatoryName6) {
        this.additionalSignatoryName6 = additionalSignatoryName6;
    }


    /**
     * Gets the additionalSignatoryTelephone6 value for this Client.
     * 
     * @return additionalSignatoryTelephone6
     */
    public java.lang.String getAdditionalSignatoryTelephone6() {
        return additionalSignatoryTelephone6;
    }


    /**
     * Sets the additionalSignatoryTelephone6 value for this Client.
     * 
     * @param additionalSignatoryTelephone6
     */
    public void setAdditionalSignatoryTelephone6(java.lang.String additionalSignatoryTelephone6) {
        this.additionalSignatoryTelephone6 = additionalSignatoryTelephone6;
    }


    /**
     * Gets the additionalSignatoryPostcode6 value for this Client.
     * 
     * @return additionalSignatoryPostcode6
     */
    public java.lang.String getAdditionalSignatoryPostcode6() {
        return additionalSignatoryPostcode6;
    }


    /**
     * Sets the additionalSignatoryPostcode6 value for this Client.
     * 
     * @param additionalSignatoryPostcode6
     */
    public void setAdditionalSignatoryPostcode6(java.lang.String additionalSignatoryPostcode6) {
        this.additionalSignatoryPostcode6 = additionalSignatoryPostcode6;
    }


    /**
     * Gets the additionalSignatoryName7 value for this Client.
     * 
     * @return additionalSignatoryName7
     */
    public java.lang.String getAdditionalSignatoryName7() {
        return additionalSignatoryName7;
    }


    /**
     * Sets the additionalSignatoryName7 value for this Client.
     * 
     * @param additionalSignatoryName7
     */
    public void setAdditionalSignatoryName7(java.lang.String additionalSignatoryName7) {
        this.additionalSignatoryName7 = additionalSignatoryName7;
    }


    /**
     * Gets the additionalSignatoryTelephone7 value for this Client.
     * 
     * @return additionalSignatoryTelephone7
     */
    public java.lang.String getAdditionalSignatoryTelephone7() {
        return additionalSignatoryTelephone7;
    }


    /**
     * Sets the additionalSignatoryTelephone7 value for this Client.
     * 
     * @param additionalSignatoryTelephone7
     */
    public void setAdditionalSignatoryTelephone7(java.lang.String additionalSignatoryTelephone7) {
        this.additionalSignatoryTelephone7 = additionalSignatoryTelephone7;
    }


    /**
     * Gets the additionalSignatoryPostcode7 value for this Client.
     * 
     * @return additionalSignatoryPostcode7
     */
    public java.lang.String getAdditionalSignatoryPostcode7() {
        return additionalSignatoryPostcode7;
    }


    /**
     * Sets the additionalSignatoryPostcode7 value for this Client.
     * 
     * @param additionalSignatoryPostcode7
     */
    public void setAdditionalSignatoryPostcode7(java.lang.String additionalSignatoryPostcode7) {
        this.additionalSignatoryPostcode7 = additionalSignatoryPostcode7;
    }


    /**
     * Gets the additionalSignatoryName8 value for this Client.
     * 
     * @return additionalSignatoryName8
     */
    public java.lang.String getAdditionalSignatoryName8() {
        return additionalSignatoryName8;
    }


    /**
     * Sets the additionalSignatoryName8 value for this Client.
     * 
     * @param additionalSignatoryName8
     */
    public void setAdditionalSignatoryName8(java.lang.String additionalSignatoryName8) {
        this.additionalSignatoryName8 = additionalSignatoryName8;
    }


    /**
     * Gets the additionalSignatoryTelephone8 value for this Client.
     * 
     * @return additionalSignatoryTelephone8
     */
    public java.lang.String getAdditionalSignatoryTelephone8() {
        return additionalSignatoryTelephone8;
    }


    /**
     * Sets the additionalSignatoryTelephone8 value for this Client.
     * 
     * @param additionalSignatoryTelephone8
     */
    public void setAdditionalSignatoryTelephone8(java.lang.String additionalSignatoryTelephone8) {
        this.additionalSignatoryTelephone8 = additionalSignatoryTelephone8;
    }


    /**
     * Gets the additionalSignatoryPostcode8 value for this Client.
     * 
     * @return additionalSignatoryPostcode8
     */
    public java.lang.String getAdditionalSignatoryPostcode8() {
        return additionalSignatoryPostcode8;
    }


    /**
     * Sets the additionalSignatoryPostcode8 value for this Client.
     * 
     * @param additionalSignatoryPostcode8
     */
    public void setAdditionalSignatoryPostcode8(java.lang.String additionalSignatoryPostcode8) {
        this.additionalSignatoryPostcode8 = additionalSignatoryPostcode8;
    }


    /**
     * Gets the additionalSignatoryName9 value for this Client.
     * 
     * @return additionalSignatoryName9
     */
    public java.lang.String getAdditionalSignatoryName9() {
        return additionalSignatoryName9;
    }


    /**
     * Sets the additionalSignatoryName9 value for this Client.
     * 
     * @param additionalSignatoryName9
     */
    public void setAdditionalSignatoryName9(java.lang.String additionalSignatoryName9) {
        this.additionalSignatoryName9 = additionalSignatoryName9;
    }


    /**
     * Gets the additionalSignatoryTelephone9 value for this Client.
     * 
     * @return additionalSignatoryTelephone9
     */
    public java.lang.String getAdditionalSignatoryTelephone9() {
        return additionalSignatoryTelephone9;
    }


    /**
     * Sets the additionalSignatoryTelephone9 value for this Client.
     * 
     * @param additionalSignatoryTelephone9
     */
    public void setAdditionalSignatoryTelephone9(java.lang.String additionalSignatoryTelephone9) {
        this.additionalSignatoryTelephone9 = additionalSignatoryTelephone9;
    }


    /**
     * Gets the additionalSignatoryPostcode9 value for this Client.
     * 
     * @return additionalSignatoryPostcode9
     */
    public java.lang.String getAdditionalSignatoryPostcode9() {
        return additionalSignatoryPostcode9;
    }


    /**
     * Sets the additionalSignatoryPostcode9 value for this Client.
     * 
     * @param additionalSignatoryPostcode9
     */
    public void setAdditionalSignatoryPostcode9(java.lang.String additionalSignatoryPostcode9) {
        this.additionalSignatoryPostcode9 = additionalSignatoryPostcode9;
    }


    /**
     * Gets the additionalSignatoryName10 value for this Client.
     * 
     * @return additionalSignatoryName10
     */
    public java.lang.String getAdditionalSignatoryName10() {
        return additionalSignatoryName10;
    }


    /**
     * Sets the additionalSignatoryName10 value for this Client.
     * 
     * @param additionalSignatoryName10
     */
    public void setAdditionalSignatoryName10(java.lang.String additionalSignatoryName10) {
        this.additionalSignatoryName10 = additionalSignatoryName10;
    }


    /**
     * Gets the additionalSignatoryTelephone10 value for this Client.
     * 
     * @return additionalSignatoryTelephone10
     */
    public java.lang.String getAdditionalSignatoryTelephone10() {
        return additionalSignatoryTelephone10;
    }


    /**
     * Sets the additionalSignatoryTelephone10 value for this Client.
     * 
     * @param additionalSignatoryTelephone10
     */
    public void setAdditionalSignatoryTelephone10(java.lang.String additionalSignatoryTelephone10) {
        this.additionalSignatoryTelephone10 = additionalSignatoryTelephone10;
    }


    /**
     * Gets the additionalSignatoryPostcode10 value for this Client.
     * 
     * @return additionalSignatoryPostcode10
     */
    public java.lang.String getAdditionalSignatoryPostcode10() {
        return additionalSignatoryPostcode10;
    }


    /**
     * Sets the additionalSignatoryPostcode10 value for this Client.
     * 
     * @param additionalSignatoryPostcode10
     */
    public void setAdditionalSignatoryPostcode10(java.lang.String additionalSignatoryPostcode10) {
        this.additionalSignatoryPostcode10 = additionalSignatoryPostcode10;
    }


    /**
     * Gets the dataProtectionMailingGroupIndicator value for this Client.
     * 
     * @return dataProtectionMailingGroupIndicator
     */
    public boolean isDataProtectionMailingGroupIndicator() {
        return dataProtectionMailingGroupIndicator;
    }


    /**
     * Sets the dataProtectionMailingGroupIndicator value for this Client.
     * 
     * @param dataProtectionMailingGroupIndicator
     */
    public void setDataProtectionMailingGroupIndicator(boolean dataProtectionMailingGroupIndicator) {
        this.dataProtectionMailingGroupIndicator = dataProtectionMailingGroupIndicator;
    }


    /**
     * Gets the dateOpened value for this Client.
     * 
     * @return dateOpened
     */
    public java.lang.String getDateOpened() {
        return dateOpened;
    }


    /**
     * Sets the dateOpened value for this Client.
     * 
     * @param dateOpened
     */
    public void setDateOpened(java.lang.String dateOpened) {
        this.dateOpened = dateOpened;
    }


    /**
     * Gets the IVSCFID value for this Client.
     * 
     * @return IVSCFID
     */
    public java.lang.String getIVSCFID() {
        return IVSCFID;
    }


    /**
     * Sets the IVSCFID value for this Client.
     * 
     * @param IVSCFID
     */
    public void setIVSCFID(java.lang.String IVSCFID) {
        this.IVSCFID = IVSCFID;
    }


    /**
     * Gets the IVSReferenceNumber value for this Client.
     * 
     * @return IVSReferenceNumber
     */
    public java.lang.String getIVSReferenceNumber() {
        return IVSReferenceNumber;
    }


    /**
     * Sets the IVSReferenceNumber value for this Client.
     * 
     * @param IVSReferenceNumber
     */
    public void setIVSReferenceNumber(java.lang.String IVSReferenceNumber) {
        this.IVSReferenceNumber = IVSReferenceNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Client)) return false;
        Client other = (Client) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.contributorTypeCode == other.getContributorTypeCode() &&
            this.subDivisionCode == other.getSubDivisionCode() &&
            this.contributorCode == other.getContributorCode() &&
            this.clientNumber == other.getClientNumber() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            ((this.exemptionType==null && other.getExemptionType()==null) || 
             (this.exemptionType!=null &&
              this.exemptionType.equals(other.getExemptionType()))) &&
            ((this.exemptionNumber==null && other.getExemptionNumber()==null) || 
             (this.exemptionNumber!=null &&
              this.exemptionNumber.equals(other.getExemptionNumber()))) &&
            this.isSatisfactoryDocumentationPresent == other.isIsSatisfactoryDocumentationPresent() &&
            this.defaultCorrespondentNumber == other.getDefaultCorrespondentNumber() &&
            this.correspondentCodeBeneficialDefault == other.getCorrespondentCodeBeneficialDefault() &&
            this.subTotalIndicator == other.isSubTotalIndicator() &&
            this.incomeDistributionReportIndicator == other.isIncomeDistributionReportIndicator() &&
            this.incomeDistributionReportPaperIndicator == other.isIncomeDistributionReportPaperIndicator() &&
            this.incomeDistributionReportDiskIndicator == other.isIncomeDistributionReportDiskIndicator() &&
            this.incomeDistributionReportEmailIndicator == other.isIncomeDistributionReportEmailIndicator() &&
            this.numberOfStatementsDeposit == other.getNumberOfStatementsDeposit() &&
            this.numberOfStatementsUnitised == other.getNumberOfStatementsUnitised() &&
            this.statementMonths1 == other.getStatementMonths1() &&
            this.statementMonths2 == other.getStatementMonths2() &&
            this.statementMonths3 == other.getStatementMonths3() &&
            this.statementMonths4 == other.getStatementMonths4() &&
            this.statementMonthsAllIndicator == other.isStatementMonthsAllIndicator() &&
            this.depositStatementsIndicator == other.isDepositStatementsIndicator() &&
            this.depositStatementsPaperIndicator == other.isDepositStatementsPaperIndicator() &&
            this.depositStatementsDiskIndicator == other.isDepositStatementsDiskIndicator() &&
            this.depositStatementsEmailIndicator == other.isDepositStatementsEmailIndicator() &&
            this.investmentStatementsPaperIndicator == other.isInvestmentStatementsPaperIndicator() &&
            this.investmentStatementsDiskIndicator == other.isInvestmentStatementsDiskIndicator() &&
            this.investmentStatementsEmailIndicator == other.isInvestmentStatementsEmailIndicator() &&
            this.bulkDepositStatementsIndicator == other.isBulkDepositStatementsIndicator() &&
            this.depositAndWithdrawalBooksIndicator == other.isDepositAndWithdrawalBooksIndicator() &&
            ((this.lastStatementProduced==null && other.getLastStatementProduced()==null) || 
             (this.lastStatementProduced!=null &&
              this.lastStatementProduced.equals(other.getLastStatementProduced()))) &&
            this.automaticStationaryOrderedIndicator == other.isAutomaticStationaryOrderedIndicator() &&
            ((this.encryptionMethod==null && other.getEncryptionMethod()==null) || 
             (this.encryptionMethod!=null &&
              this.encryptionMethod.equals(other.getEncryptionMethod()))) &&
            this.externalUserAccessAllowedIndicator == other.isExternalUserAccessAllowedIndicator() &&
            this.quarterlyReportingClientIndicator == other.isQuarterlyReportingClientIndicator() &&
            ((this.quarterlyReportingManagerInitials==null && other.getQuarterlyReportingManagerInitials()==null) || 
             (this.quarterlyReportingManagerInitials!=null &&
              this.quarterlyReportingManagerInitials.equals(other.getQuarterlyReportingManagerInitials()))) &&
            this.quarterlyUpdateIndicator == other.isQuarterlyUpdateIndicator() &&
            this.marketingGroup == other.getMarketingGroup() &&
            this.marketingSubGroup == other.getMarketingSubGroup() &&
            this.accountGroup == other.getAccountGroup() &&
            this.auditCertificateSendIndicator == other.isAuditCertificateSendIndicator() &&
            this.auditCertificatePrintIndicator == other.isAuditCertificatePrintIndicator() &&
            this.auditCertificateDueDay == other.getAuditCertificateDueDay() &&
            this.auditCertificateDueMonth == other.getAuditCertificateDueMonth() &&
            this.auditCertificateClientAuthority == other.isAuditCertificateClientAuthority() &&
            ((this.auditCertificateDestination==null && other.getAuditCertificateDestination()==null) || 
             (this.auditCertificateDestination!=null &&
              this.auditCertificateDestination.equals(other.getAuditCertificateDestination()))) &&
            ((this.auditPriceType==null && other.getAuditPriceType()==null) || 
             (this.auditPriceType!=null &&
              this.auditPriceType.equals(other.getAuditPriceType()))) &&
            ((this.authorityHeldDate==null && other.getAuthorityHeldDate()==null) || 
             (this.authorityHeldDate!=null &&
              this.authorityHeldDate.equals(other.getAuthorityHeldDate()))) &&
            ((this.auditorReference==null && other.getAuditorReference()==null) || 
             (this.auditorReference!=null &&
              this.auditorReference.equals(other.getAuditorReference()))) &&
            ((this.auditorAddress==null && other.getAuditorAddress()==null) || 
             (this.auditorAddress!=null &&
              this.auditorAddress.equals(other.getAuditorAddress()))) &&
            this.multipleSignatoryIndicator == other.isMultipleSignatoryIndicator() &&
            ((this.multipleSignatoryInformation==null && other.getMultipleSignatoryInformation()==null) || 
             (this.multipleSignatoryInformation!=null &&
              this.multipleSignatoryInformation.equals(other.getMultipleSignatoryInformation()))) &&
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
            this.dataProtectionMailingGroupIndicator == other.isDataProtectionMailingGroupIndicator() &&
            ((this.dateOpened==null && other.getDateOpened()==null) || 
             (this.dateOpened!=null &&
              this.dateOpened.equals(other.getDateOpened()))) &&
            ((this.IVSCFID==null && other.getIVSCFID()==null) || 
             (this.IVSCFID!=null &&
              this.IVSCFID.equals(other.getIVSCFID()))) &&
            ((this.IVSReferenceNumber==null && other.getIVSReferenceNumber()==null) || 
             (this.IVSReferenceNumber!=null &&
              this.IVSReferenceNumber.equals(other.getIVSReferenceNumber())));
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
        _hashCode += getContributorTypeCode();
        _hashCode += getSubDivisionCode();
        _hashCode += getContributorCode();
        _hashCode += getClientNumber();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getExemptionType() != null) {
            _hashCode += getExemptionType().hashCode();
        }
        if (getExemptionNumber() != null) {
            _hashCode += getExemptionNumber().hashCode();
        }
        _hashCode += (isIsSatisfactoryDocumentationPresent() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getDefaultCorrespondentNumber();
        _hashCode += getCorrespondentCodeBeneficialDefault();
        _hashCode += (isSubTotalIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isIncomeDistributionReportIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isIncomeDistributionReportPaperIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isIncomeDistributionReportDiskIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isIncomeDistributionReportEmailIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getNumberOfStatementsDeposit();
        _hashCode += getNumberOfStatementsUnitised();
        _hashCode += getStatementMonths1();
        _hashCode += getStatementMonths2();
        _hashCode += getStatementMonths3();
        _hashCode += getStatementMonths4();
        _hashCode += (isStatementMonthsAllIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDepositStatementsIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDepositStatementsPaperIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDepositStatementsDiskIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDepositStatementsEmailIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInvestmentStatementsPaperIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInvestmentStatementsDiskIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInvestmentStatementsEmailIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isBulkDepositStatementsIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDepositAndWithdrawalBooksIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLastStatementProduced() != null) {
            _hashCode += getLastStatementProduced().hashCode();
        }
        _hashCode += (isAutomaticStationaryOrderedIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getEncryptionMethod() != null) {
            _hashCode += getEncryptionMethod().hashCode();
        }
        _hashCode += (isExternalUserAccessAllowedIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isQuarterlyReportingClientIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getQuarterlyReportingManagerInitials() != null) {
            _hashCode += getQuarterlyReportingManagerInitials().hashCode();
        }
        _hashCode += (isQuarterlyUpdateIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getMarketingGroup();
        _hashCode += getMarketingSubGroup();
        _hashCode += getAccountGroup();
        _hashCode += (isAuditCertificateSendIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAuditCertificatePrintIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getAuditCertificateDueDay();
        _hashCode += getAuditCertificateDueMonth();
        _hashCode += (isAuditCertificateClientAuthority() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getAuditCertificateDestination() != null) {
            _hashCode += getAuditCertificateDestination().hashCode();
        }
        if (getAuditPriceType() != null) {
            _hashCode += getAuditPriceType().hashCode();
        }
        if (getAuthorityHeldDate() != null) {
            _hashCode += getAuthorityHeldDate().hashCode();
        }
        if (getAuditorReference() != null) {
            _hashCode += getAuditorReference().hashCode();
        }
        if (getAuditorAddress() != null) {
            _hashCode += getAuditorAddress().hashCode();
        }
        _hashCode += (isMultipleSignatoryIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMultipleSignatoryInformation() != null) {
            _hashCode += getMultipleSignatoryInformation().hashCode();
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
        _hashCode += (isDataProtectionMailingGroupIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDateOpened() != null) {
            _hashCode += getDateOpened().hashCode();
        }
        if (getIVSCFID() != null) {
            _hashCode += getIVSCFID().hashCode();
        }
        if (getIVSReferenceNumber() != null) {
            _hashCode += getIVSReferenceNumber().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Client.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Client"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("clientNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ClientNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Address"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exemptionType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ExemptionType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ExemptionTypes"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exemptionNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ExemptionNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isSatisfactoryDocumentationPresent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IsSatisfactoryDocumentationPresent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("defaultCorrespondentNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DefaultCorrespondentNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correspondentCodeBeneficialDefault");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "CorrespondentCodeBeneficialDefault"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subTotalIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "SubTotalIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incomeDistributionReportIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeDistributionReportIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incomeDistributionReportPaperIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeDistributionReportPaperIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incomeDistributionReportDiskIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeDistributionReportDiskIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incomeDistributionReportEmailIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeDistributionReportEmailIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfStatementsDeposit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "NumberOfStatementsDeposit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfStatementsUnitised");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "NumberOfStatementsUnitised"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statementMonths1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "StatementMonths1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statementMonths2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "StatementMonths2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statementMonths3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "StatementMonths3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statementMonths4");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "StatementMonths4"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statementMonthsAllIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "StatementMonthsAllIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depositStatementsIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositStatementsIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depositStatementsPaperIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositStatementsPaperIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depositStatementsDiskIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositStatementsDiskIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depositStatementsEmailIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositStatementsEmailIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("investmentStatementsPaperIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "InvestmentStatementsPaperIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("investmentStatementsDiskIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "InvestmentStatementsDiskIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("investmentStatementsEmailIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "InvestmentStatementsEmailIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bulkDepositStatementsIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BulkDepositStatementsIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depositAndWithdrawalBooksIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositAndWithdrawalBooksIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastStatementProduced");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "LastStatementProduced"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("automaticStationaryOrderedIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AutomaticStationaryOrderedIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("encryptionMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "EncryptionMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "EncryptionTypes"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("externalUserAccessAllowedIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ExternalUserAccessAllowedIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quarterlyReportingClientIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "QuarterlyReportingClientIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quarterlyReportingManagerInitials");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "QuarterlyReportingManagerInitials"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quarterlyUpdateIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "QuarterlyUpdateIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("marketingGroup");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MarketingGroup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("marketingSubGroup");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MarketingSubGroup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountGroup");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccountGroup"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("auditCertificateSendIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuditCertificateSendIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("auditCertificatePrintIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuditCertificatePrintIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("auditCertificateDueDay");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuditCertificateDueDay"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("auditCertificateDueMonth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuditCertificateDueMonth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("auditCertificateClientAuthority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuditCertificateClientAuthority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("auditCertificateDestination");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuditCertificateDestination"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("auditPriceType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuditPriceType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authorityHeldDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuthorityHeldDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("auditorReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuditorReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("auditorAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuditorAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Address"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("multipleSignatoryIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MultipleSignatoryIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("multipleSignatoryInformation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MultipleSignatoryInformation"));
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
        elemField.setFieldName("dataProtectionMailingGroupIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DataProtectionMailingGroupIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateOpened");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DateOpened"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
		return "Client [contributorTypeCode=" + contributorTypeCode
				+ "\nsubDivisionCode=" + subDivisionCode + "\ncontributorCode="
				+ contributorCode + "\nclientNumber=" + clientNumber
				+ "\nname=" + name + "\naddress=" + address
				+ "\nexemptionType=" + exemptionType + "\nexemptionNumber="
				+ exemptionNumber + "\nisSatisfactoryDocumentationPresent="
				+ isSatisfactoryDocumentationPresent
				+ "\ndefaultCorrespondentNumber=" + defaultCorrespondentNumber
				+ "\ncorrespondentCodeBeneficialDefault="
				+ correspondentCodeBeneficialDefault + "\nsubTotalIndicator="
				+ subTotalIndicator + "\nincomeDistributionReportIndicator="
				+ incomeDistributionReportIndicator
				+ "\nincomeDistributionReportPaperIndicator="
				+ incomeDistributionReportPaperIndicator
				+ "\nincomeDistributionReportDiskIndicator="
				+ incomeDistributionReportDiskIndicator
				+ "\nincomeDistributionReportEmailIndicator="
				+ incomeDistributionReportEmailIndicator
				+ "\nnumberOfStatementsDeposit=" + numberOfStatementsDeposit
				+ "\nnumberOfStatementsUnitised=" + numberOfStatementsUnitised
				+ "\nstatementMonths1=" + statementMonths1
				+ "\nstatementMonths2=" + statementMonths2
				+ "\nstatementMonths3=" + statementMonths3
				+ "\nstatementMonths4=" + statementMonths4
				+ "\nstatementMonthsAllIndicator="
				+ statementMonthsAllIndicator + "\ndepositStatementsIndicator="
				+ depositStatementsIndicator
				+ "\ndepositStatementsPaperIndicator="
				+ depositStatementsPaperIndicator
				+ "\ndepositStatementsDiskIndicator="
				+ depositStatementsDiskIndicator
				+ "\ndepositStatementsEmailIndicator="
				+ depositStatementsEmailIndicator
				+ "\ninvestmentStatementsPaperIndicator="
				+ investmentStatementsPaperIndicator
				+ "\ninvestmentStatementsDiskIndicator="
				+ investmentStatementsDiskIndicator
				+ "\ninvestmentStatementsEmailIndicator="
				+ investmentStatementsEmailIndicator
				+ "\nbulkDepositStatementsIndicator="
				+ bulkDepositStatementsIndicator
				+ "\ndepositAndWithdrawalBooksIndicator="
				+ depositAndWithdrawalBooksIndicator
				+ "\nlastStatementProduced=" + lastStatementProduced
				+ "\nautomaticStationaryOrderedIndicator="
				+ automaticStationaryOrderedIndicator + "\nencryptionMethod="
				+ encryptionMethod + "\nexternalUserAccessAllowedIndicator="
				+ externalUserAccessAllowedIndicator
				+ "\nquarterlyReportingClientIndicator="
				+ quarterlyReportingClientIndicator
				+ "\nquarterlyReportingManagerInitials="
				+ quarterlyReportingManagerInitials
				+ "\nquarterlyUpdateIndicator=" + quarterlyUpdateIndicator
				+ "\nmarketingGroup=" + marketingGroup + "\nmarketingSubGroup="
				+ marketingSubGroup + "\naccountGroup=" + accountGroup
				+ "\nauditCertificateSendIndicator="
				+ auditCertificateSendIndicator
				+ "\nauditCertificatePrintIndicator="
				+ auditCertificatePrintIndicator + "\nauditCertificateDueDay="
				+ auditCertificateDueDay + "\nauditCertificateDueMonth="
				+ auditCertificateDueMonth
				+ "\nauditCertificateClientAuthority="
				+ auditCertificateClientAuthority
				+ "\nauditCertificateDestination="
				+ auditCertificateDestination + "\nauditPriceType="
				+ auditPriceType + "\nauthorityHeldDate=" + authorityHeldDate
				+ "\nauditorReference=" + auditorReference
				+ "\nauditorAddress=" + auditorAddress
				+ "\nmultipleSignatoryIndicator=" + multipleSignatoryIndicator
				+ "\nmultipleSignatoryInformation="
				+ multipleSignatoryInformation + "\nadditionalSignatoryName1="
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
				+ "\ndataProtectionMailingGroupIndicator="
				+ dataProtectionMailingGroupIndicator + "\ndateOpened="
				+ dateOpened + "\nIVSCFID=" + IVSCFID + "\nIVSReferenceNumber="
				+ IVSReferenceNumber + "\n__equalsCalc=" + __equalsCalc
				+ "\n__hashCodeCalc=" + __hashCodeCalc + "]";
	}

}
