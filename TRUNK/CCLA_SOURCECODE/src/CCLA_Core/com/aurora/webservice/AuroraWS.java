/**
 * AuroraWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public interface AuroraWS extends javax.xml.rpc.Service {
    public java.lang.String getAuroraWSSoapAddress();

    public com.aurora.webservice.AuroraWSSoap_PortType getAuroraWSSoap() throws javax.xml.rpc.ServiceException;

    public com.aurora.webservice.AuroraWSSoap_PortType getAuroraWSSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
