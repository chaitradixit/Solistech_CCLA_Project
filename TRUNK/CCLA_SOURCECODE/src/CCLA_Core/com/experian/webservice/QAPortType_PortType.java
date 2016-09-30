/**
 * QAPortType_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public interface QAPortType_PortType extends java.rmi.Remote {
    public com.experian.webservice.QASearchResult doSearch(com.experian.webservice.QASearch body) throws java.rmi.RemoteException, com.experian.webservice.QAFault;
    public com.experian.webservice.Picklist doRefine(com.experian.webservice.QARefine body) throws java.rmi.RemoteException, com.experian.webservice.QAFault;
    public com.experian.webservice.Address doGetAddress(com.experian.webservice.QAGetAddress body) throws java.rmi.RemoteException, com.experian.webservice.QAFault;
    public com.experian.webservice.RawAddressLine[] doGetRawAddress(com.experian.webservice.QAGetRawAddress body) throws java.rmi.RemoteException, com.experian.webservice.QAFault;
    public com.experian.webservice.QADataSet[] doGetData() throws java.rmi.RemoteException, com.experian.webservice.QAFault;
    public com.experian.webservice.QALicenceInfo doGetLicenseInfo() throws java.rmi.RemoteException, com.experian.webservice.QAFault;
    public java.lang.String[] doGetSystemInfo() throws java.rmi.RemoteException, com.experian.webservice.QAFault;
    public com.experian.webservice.QAExampleAddress[] doGetExampleAddresses(com.experian.webservice.QAGetExampleAddresses body) throws java.rmi.RemoteException, com.experian.webservice.QAFault;
    public com.experian.webservice.QALayout[] doGetLayouts(com.experian.webservice.QAGetLayouts body) throws java.rmi.RemoteException, com.experian.webservice.QAFault;
    public com.experian.webservice.PromptLine[] doGetPromptSet(com.experian.webservice.QAGetPromptSet body) throws java.rmi.RemoteException, com.experian.webservice.QAFault;
    public com.experian.webservice.QASearchOk doCanSearch(com.experian.webservice.QACanSearch body) throws java.rmi.RemoteException, com.experian.webservice.QAFault;
}
