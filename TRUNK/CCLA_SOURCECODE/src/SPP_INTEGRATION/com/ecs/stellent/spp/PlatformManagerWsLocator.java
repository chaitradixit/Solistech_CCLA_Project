/**
 * PlatformManagerWsLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class PlatformManagerWsLocator extends org.apache.axis.client.Service implements com.ecs.stellent.spp.PlatformManagerWs {

    public PlatformManagerWsLocator() {
    }


    public PlatformManagerWsLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PlatformManagerWsLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PlatformManagerWsSoap
    private java.lang.String PlatformManagerWsSoap_address = "http://10.15.4.109/PlatformManagerWs/PlatformManagerWs.asmx";

    public java.lang.String getPlatformManagerWsSoapAddress() {
        return PlatformManagerWsSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PlatformManagerWsSoapWSDDServiceName = "PlatformManagerWsSoap";

    public java.lang.String getPlatformManagerWsSoapWSDDServiceName() {
        return PlatformManagerWsSoapWSDDServiceName;
    }

    public void setPlatformManagerWsSoapWSDDServiceName(java.lang.String name) {
        PlatformManagerWsSoapWSDDServiceName = name;
    }

    public com.ecs.stellent.spp.PlatformManagerWsSoap_PortType getPlatformManagerWsSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PlatformManagerWsSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPlatformManagerWsSoap(endpoint);
    }

    public com.ecs.stellent.spp.PlatformManagerWsSoap_PortType getPlatformManagerWsSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ecs.stellent.spp.PlatformManagerWsSoap_BindingStub _stub = new com.ecs.stellent.spp.PlatformManagerWsSoap_BindingStub(portAddress, this);
            _stub.setPortName(getPlatformManagerWsSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPlatformManagerWsSoapEndpointAddress(java.lang.String address) {
    	PlatformManagerWsSoap_address = address;
    }


    // Use to get a proxy class for PlatformManagerWsSoap12
    private java.lang.String PlatformManagerWsSoap12_address = "http://10.15.4.109/PlatformManagerWs/PlatformManagerWs.asmx";

    public java.lang.String getPlatformManagerWsSoap12Address() {
        return PlatformManagerWsSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PlatformManagerWsSoap12WSDDServiceName = "PlatformManagerWsSoap12";

    public java.lang.String getPlatformManagerWsSoap12WSDDServiceName() {
        return PlatformManagerWsSoap12WSDDServiceName;
    }

    public void setPlatformManagerWsSoap12WSDDServiceName(java.lang.String name) {
        PlatformManagerWsSoap12WSDDServiceName = name;
    }

    public com.ecs.stellent.spp.PlatformManagerWsSoap_PortType getPlatformManagerWsSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PlatformManagerWsSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPlatformManagerWsSoap12(endpoint);
    }

    public com.ecs.stellent.spp.PlatformManagerWsSoap_PortType getPlatformManagerWsSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ecs.stellent.spp.PlatformManagerWsSoap12Stub _stub = new com.ecs.stellent.spp.PlatformManagerWsSoap12Stub(portAddress, this);
            _stub.setPortName(getPlatformManagerWsSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPlatformManagerWsSoap12EndpointAddress(java.lang.String address) {
        PlatformManagerWsSoap12_address = address;
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
            if (com.ecs.stellent.spp.PlatformManagerWsSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ecs.stellent.spp.PlatformManagerWsSoap_BindingStub _stub = new com.ecs.stellent.spp.PlatformManagerWsSoap_BindingStub(new java.net.URL(PlatformManagerWsSoap_address), this);
                _stub.setPortName(getPlatformManagerWsSoapWSDDServiceName());
                return _stub;
            }
            if (com.ecs.stellent.spp.PlatformManagerWsSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ecs.stellent.spp.PlatformManagerWsSoap12Stub _stub = new com.ecs.stellent.spp.PlatformManagerWsSoap12Stub(new java.net.URL(PlatformManagerWsSoap12_address), this);
                _stub.setPortName(getPlatformManagerWsSoap12WSDDServiceName());
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
        if ("PlatformManagerWsSoap".equals(inputPortName)) {
            return getPlatformManagerWsSoap();
        }
        else if ("PlatformManagerWsSoap12".equals(inputPortName)) {
            return getPlatformManagerWsSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PlatformManagerWs");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PlatformManagerWsSoap"));
            ports.add(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PlatformManagerWsSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PlatformManagerWsSoap".equals(portName)) {
            setPlatformManagerWsSoapEndpointAddress(address);
        }
        else 
if ("PlatformManagerWsSoap12".equals(portName)) {
            setPlatformManagerWsSoap12EndpointAddress(address);
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
