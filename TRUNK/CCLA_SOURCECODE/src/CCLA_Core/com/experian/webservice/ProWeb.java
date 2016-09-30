/**
 * ProWeb.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public interface ProWeb extends javax.xml.rpc.Service {
    public java.lang.String getQAPortTypeAddress();

    public com.experian.webservice.QAPortType_PortType getQAPortType() throws javax.xml.rpc.ServiceException;

    public com.experian.webservice.QAPortType_PortType getQAPortType(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
