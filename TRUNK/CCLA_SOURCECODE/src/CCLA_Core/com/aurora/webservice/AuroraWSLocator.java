/**
 * AuroraWSLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

import com.ecs.utils.Log;

public class AuroraWSLocator extends org.apache.axis.client.Service implements com.aurora.webservice.AuroraWS {

    public AuroraWSLocator() {
    }

    public AuroraWSLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AuroraWSLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AuroraWSSoap
    private java.lang.String AuroraWSSoap_address = "http://aurora/AuroraWS/AuroraWS.asmx";

    public java.lang.String getAuroraWSSoapAddress() {
        return AuroraWSSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AuroraWSSoapWSDDServiceName = "AuroraWSSoap";

    public java.lang.String getAuroraWSSoapWSDDServiceName() {
        return AuroraWSSoapWSDDServiceName;
    }

    public void setAuroraWSSoapWSDDServiceName(java.lang.String name) {
        AuroraWSSoapWSDDServiceName = name;
    }

    public com.aurora.webservice.AuroraWSSoap_PortType getAuroraWSSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AuroraWSSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAuroraWSSoap(endpoint);
    }

    public com.aurora.webservice.AuroraWSSoap_PortType getAuroraWSSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.aurora.webservice.AuroraWSSoap_BindingStub _stub = new com.aurora.webservice.AuroraWSSoap_BindingStub(portAddress, this);
            _stub.setPortName(getAuroraWSSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAuroraWSSoapEndpointAddress(java.lang.String address) {
        AuroraWSSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.aurora.webservice.AuroraWSSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.aurora.webservice.AuroraWSSoap_BindingStub _stub = new com.aurora.webservice.AuroraWSSoap_BindingStub(new java.net.URL(AuroraWSSoap_address), this);
                _stub.setPortName(getAuroraWSSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("AuroraWSSoap".equals(inputPortName)) {
            return getAuroraWSSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuroraWS");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AuroraWSSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AuroraWSSoap".equals(portName)) {
            setAuroraWSSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }
}
