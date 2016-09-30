/**
 * AuroraWSSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public interface AuroraWSSoap_PortType extends java.rmi.Remote {
    public void dummy(com.aurora.webservice.ErrorCode dummyErrorCodeEnum, com.aurora.webservice.FaultCode dummyFaultCodeEnum) throws java.rmi.RemoteException;
    public com.aurora.webservice.Client getClientByClientNumber(java.lang.String company, int clientNumber) throws java.rmi.RemoteException;
    public com.aurora.webservice.Client getClientByAccountNumberExternal(java.lang.String company, java.lang.String accountNumberExternal) throws java.rmi.RemoteException;
    public java.lang.String getAccountNumberExternalByClientNumberAndAccountNumber(java.lang.String company, java.lang.String fundCode, int clientNumber, int accountNumber) throws java.rmi.RemoteException;
    public java.lang.String getPendingAccountNumberExternalByClientNumberAndAccountNumber(java.lang.String company, java.lang.String fundCode, int clientNumber, int accountNumber) throws java.rmi.RemoteException;
    public com.aurora.webservice.Account getAccountByAccountNumberExternal(java.lang.String company, java.lang.String accountNumberExternal) throws java.rmi.RemoteException;
    public com.aurora.webservice.Address getClientCorrespondentAddress(java.lang.String company, int clientNumber) throws java.rmi.RemoteException;
    public com.aurora.webservice.Address getAccountCorrespondentAddress(java.lang.String company, java.lang.String accountNumberExternal) throws java.rmi.RemoteException;
    public boolean isValidBankAccount(java.lang.String accountSortCode, java.lang.String accountNumber) throws java.rmi.RemoteException;
    public int createClient(java.lang.String company, com.aurora.webservice.Client client, boolean autogenerateClientNumber) throws java.rmi.RemoteException;
    public boolean amendClient(java.lang.String company, com.aurora.webservice.Client client) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfClient getClientsByCorrespondentCode(java.lang.String company, int correspondentCode) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfAccount getAccountsByCorrespondentCode(java.lang.String company, int correspondentCode) throws java.rmi.RemoteException;
    public int createCorrespondent(java.lang.String company, com.aurora.webservice.Correspondent correspondent, boolean autogenerateCorrespondentNumber) throws java.rmi.RemoteException;
    public boolean amendCorrespondent(java.lang.String company, com.aurora.webservice.Correspondent correspondent) throws java.rmi.RemoteException;
    public com.aurora.webservice.Correspondent getCorrespondentByCorrespondentCode(java.lang.String company, int correspondentCode) throws java.rmi.RemoteException;
    public com.aurora.webservice.Correspondent getCorrespondentByClientNumber(java.lang.String company, int clientNumber) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfUnitPrice getUnitValuationPricesByValuationPoint(java.lang.String company, java.util.Calendar valuationPoint) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfUnitPrice getLatestUnitValuationPrices(java.lang.String company) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfUnitPrice getUnitPricesByValuationPoint(java.lang.String company, java.util.Calendar valuationPoint) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfUnitPrice getLatestUnitPrices(java.lang.String company) throws java.rmi.RemoteException;
    public boolean createUnitPrices(java.lang.String company, com.aurora.webservice.ArrayOfUnitPrice unitPrice) throws java.rmi.RemoteException;
    public boolean authoriseUnitPrices(java.lang.String company, com.aurora.webservice.ArrayOfUnitPrice unitPrice) throws java.rmi.RemoteException;
    public boolean createUnitValuationPrices(java.lang.String company, com.aurora.webservice.ArrayOfUnitPrice unitValuationPrice) throws java.rmi.RemoteException;
    public boolean authoriseUnitValuationPrices(java.lang.String company, com.aurora.webservice.ArrayOfUnitPrice unitValuationPrice) throws java.rmi.RemoteException;
    public boolean createIncomeAccrualDistribution(java.lang.String company, java.lang.String fundCode, com.aurora.webservice.ArrayOfIncomeAccrual incomeAccrual, com.aurora.webservice.ArrayOfIncomeDistribution incomeDistribution) throws java.rmi.RemoteException;
    public boolean submitIncomeAccrualDistributionAuthorisation(java.lang.String company, java.lang.String fundCode, com.aurora.webservice.ArrayOfIncomeAccrual incomeAccrual, com.aurora.webservice.ArrayOfIncomeDistribution incomeDistribution) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfDistribution getDistributionsByFundCode(java.lang.String company, java.lang.String fundCode, java.util.Calendar distributionDate) throws java.rmi.RemoteException;
    public com.aurora.webservice.InterFundTransferType getInterFundTransfer(java.lang.String companySource, java.lang.String fundCodeSource, int shareClassCodeSource, java.lang.String companyDestination, java.lang.String fundCodeDestination, int shareClassCodeDestination) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfFund getFunds(java.lang.String company) throws java.rmi.RemoteException;
    public com.aurora.webservice.Fund getFundByFundCode(java.lang.String company, java.lang.String fundCode) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfCorrespondent getAccountsCorrespondentsByClientNumber(java.lang.String company, int clientNumber) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfShareClass getShareClassByFund(java.lang.String company, java.lang.String fundCode) throws java.rmi.RemoteException;
    public com.aurora.webservice.RegistrationAmendments generateRegistrationAmendmentsReport(java.lang.String company) throws java.rmi.RemoteException;
    public com.aurora.webservice.RegistrationAmendments generateRegistrationAmendmentLetters(java.lang.String company) throws java.rmi.RemoteException;
    public int createUnitisedBatch(java.lang.String company, com.aurora.webservice.UnitisedBatchHeader unitisedBatchHeader, com.aurora.webservice.ArrayOfUnitisedBatchItem unitisedBatchItems) throws java.rmi.RemoteException;
    public com.aurora.webservice.UnitisedBatchHeader enquireUnitisedBatchHeader(java.lang.String company, int batchNumber) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfUnitisedBatchItem enquireUnitisedBatchItems(java.lang.String company, int batchNumber) throws java.rmi.RemoteException;
    public void amendUnitisedBatch(java.lang.String company, com.aurora.webservice.UnitisedBatchHeader unitisedBatchHeader, com.aurora.webservice.ArrayOfUnitisedBatchItem unitisedBatchItems) throws java.rmi.RemoteException;
    public void cancelUnitisedBatch(java.lang.String company, int batchNumber) throws java.rmi.RemoteException;
    public void submitUnitisedBatchAuthorisation(java.lang.String company, int batchNumber) throws java.rmi.RemoteException;
    public void submitUnitisedDealsApplication(java.lang.String company, java.lang.String tradeDate) throws java.rmi.RemoteException;
    public int createDepositBatch(java.lang.String company, com.aurora.webservice.DepositBatchHeader depositBatchHeader, com.aurora.webservice.ArrayOfDepositBatchItem depositBatchItems) throws java.rmi.RemoteException;
    public com.aurora.webservice.DepositBatchHeader enquireDepositBatchHeader(java.lang.String company, int batchNumber) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfDepositBatchItem enquireDepositBatchItems(java.lang.String company, int batchNumber) throws java.rmi.RemoteException;
    public void amendDepositBatch(java.lang.String company, com.aurora.webservice.DepositBatchHeader depositBatchHeader, com.aurora.webservice.ArrayOfDepositBatchItem depositBatchItems) throws java.rmi.RemoteException;
    public void cancelDepositBatch(java.lang.String company, int batchNumber) throws java.rmi.RemoteException;
    public void cancelDepositDeal(java.lang.String company, int batchNumber, java.lang.String chequeValue, java.lang.String bankSortCode, java.lang.String bankAccountNumber) throws java.rmi.RemoteException;
    public void submitDepositBatchAuthorisation(java.lang.String company, int batchNumber) throws java.rmi.RemoteException;
    public void submitDepositDealsApplication(java.lang.String company, int batchNumber) throws java.rmi.RemoteException;
    public java.lang.String createAccount(java.lang.String company, com.aurora.webservice.Account account, boolean autogenerateAccountNumber) throws java.rmi.RemoteException;
    public boolean amendAccount(java.lang.String company, com.aurora.webservice.Account account) throws java.rmi.RemoteException;
    public void changeAccountStatus(java.lang.String company, java.lang.String accountNumberExt, com.aurora.webservice.AccountStatus status) throws java.rmi.RemoteException;
    public boolean createAccountPendingShareClassMovement(java.lang.String company, java.lang.String accountNumberExt, int destinationShareClassCode) throws java.rmi.RemoteException;
    public com.aurora.webservice.ArrayOfShareClassMovement getPendingShareClassMovementByAccountNumber(java.lang.String company, java.lang.String accountNumberExt, java.util.Calendar movementRequestDate) throws java.rmi.RemoteException;
    public com.aurora.webservice.BatchQueueJob getBatchQueueJobByTypeAndParameter(java.lang.String company, com.aurora.webservice.RequestType requestType, java.lang.String parameter) throws java.rmi.RemoteException;
    public com.aurora.webservice.BankDetails getBankDetails(java.lang.String sortCode) throws java.rmi.RemoteException;
}
