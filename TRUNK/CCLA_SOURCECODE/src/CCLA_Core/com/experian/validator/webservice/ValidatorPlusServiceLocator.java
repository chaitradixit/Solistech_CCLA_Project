/**
 * ValidatorPlusServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.validator.webservice;

public class ValidatorPlusServiceLocator extends org.apache.axis.client.Service implements com.experian.validator.webservice.ValidatorPlusService {

    public ValidatorPlusServiceLocator() {
    }


    public ValidatorPlusServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ValidatorPlusServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ValidatorPlusServiceSoap
    private java.lang.String ValidatorPlusServiceSoap_address = "http://ccla-vs-ap01/ValidatorPlusWS/ValidatorPlusService.asmx";

    public java.lang.String getValidatorPlusServiceSoapAddress() {
        return ValidatorPlusServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ValidatorPlusServiceSoapWSDDServiceName = "ValidatorPlusServiceSoap";

    public java.lang.String getValidatorPlusServiceSoapWSDDServiceName() {
        return ValidatorPlusServiceSoapWSDDServiceName;
    }

    public void setValidatorPlusServiceSoapWSDDServiceName(java.lang.String name) {
        ValidatorPlusServiceSoapWSDDServiceName = name;
    }

    public com.experian.validator.webservice.ValidatorPlusServiceSoap_PortType getValidatorPlusServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ValidatorPlusServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getValidatorPlusServiceSoap(endpoint);
    }

    public com.experian.validator.webservice.ValidatorPlusServiceSoap_PortType getValidatorPlusServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.experian.validator.webservice.ValidatorPlusServiceSoap_BindingStub _stub = new com.experian.validator.webservice.ValidatorPlusServiceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getValidatorPlusServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setValidatorPlusServiceSoapEndpointAddress(java.lang.String address) {
        ValidatorPlusServiceSoap_address = address;
    }


    // Use to get a proxy class for ValidatorPlusServiceSoap12
    private java.lang.String ValidatorPlusServiceSoap12_address = "http://ccla-vs-ap01/ValidatorPlusWS/ValidatorPlusService.asmx";

    public java.lang.String getValidatorPlusServiceSoap12Address() {
        return ValidatorPlusServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ValidatorPlusServiceSoap12WSDDServiceName = "ValidatorPlusServiceSoap12";

    public java.lang.String getValidatorPlusServiceSoap12WSDDServiceName() {
        return ValidatorPlusServiceSoap12WSDDServiceName;
    }

    public void setValidatorPlusServiceSoap12WSDDServiceName(java.lang.String name) {
        ValidatorPlusServiceSoap12WSDDServiceName = name;
    }

    public com.experian.validator.webservice.ValidatorPlusServiceSoap_PortType getValidatorPlusServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ValidatorPlusServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getValidatorPlusServiceSoap12(endpoint);
    }

    public com.experian.validator.webservice.ValidatorPlusServiceSoap_PortType getValidatorPlusServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.experian.validator.webservice.ValidatorPlusServiceSoap12Stub _stub = new com.experian.validator.webservice.ValidatorPlusServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getValidatorPlusServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setValidatorPlusServiceSoap12EndpointAddress(java.lang.String address) {
        ValidatorPlusServiceSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.experian.validator.webservice.ValidatorPlusServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.experian.validator.webservice.ValidatorPlusServiceSoap_BindingStub _stub = new com.experian.validator.webservice.ValidatorPlusServiceSoap_BindingStub(new java.net.URL(ValidatorPlusServiceSoap_address), this);
                _stub.setPortName(getValidatorPlusServiceSoapWSDDServiceName());
                return _stub;
            }
            if (com.experian.validator.webservice.ValidatorPlusServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.experian.validator.webservice.ValidatorPlusServiceSoap12Stub _stub = new com.experian.validator.webservice.ValidatorPlusServiceSoap12Stub(new java.net.URL(ValidatorPlusServiceSoap12_address), this);
                _stub.setPortName(getValidatorPlusServiceSoap12WSDDServiceName());
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
        if ("ValidatorPlusServiceSoap".equals(inputPortName)) {
            return getValidatorPlusServiceSoap();
        }
        else if ("ValidatorPlusServiceSoap12".equals(inputPortName)) {
            return getValidatorPlusServiceSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.qas.co.uk/", "ValidatorPlusService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.qas.co.uk/", "ValidatorPlusServiceSoap"));
            ports.add(new javax.xml.namespace.QName("http://www.qas.co.uk/", "ValidatorPlusServiceSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ValidatorPlusServiceSoap".equals(portName)) {
            setValidatorPlusServiceSoapEndpointAddress(address);
        }
        else 
if ("ValidatorPlusServiceSoap12".equals(portName)) {
            setValidatorPlusServiceSoap12EndpointAddress(address);
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
