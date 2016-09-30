/**
 * WorkflowLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class WorkflowLocator extends org.apache.axis.client.Service implements com.ecs.stellent.spp.Workflow {

    public WorkflowLocator() {
    }


    public WorkflowLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WorkflowLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WorkflowSoap
    private java.lang.String WorkflowSoap_address = "http://ccla-ap12:81/Workflow/WebService/Workflow.asmx";
    
    public java.lang.String getWorkflowSoapAddress() {
        return WorkflowSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WorkflowSoapWSDDServiceName = "WorkflowSoap";

    public java.lang.String getWorkflowSoapWSDDServiceName() {
        return WorkflowSoapWSDDServiceName;
    }

    public void setWorkflowSoapWSDDServiceName(java.lang.String name) {
        WorkflowSoapWSDDServiceName = name;
    }

    public com.ecs.stellent.spp.WorkflowSoap_PortType getWorkflowSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WorkflowSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWorkflowSoap(endpoint);
    }

    public com.ecs.stellent.spp.WorkflowSoap_PortType getWorkflowSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ecs.stellent.spp.WorkflowSoap_BindingStub _stub = new com.ecs.stellent.spp.WorkflowSoap_BindingStub(portAddress, this);
            _stub.setPortName(getWorkflowSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWorkflowSoapEndpointAddress(java.lang.String address) {
        WorkflowSoap_address = address;
    }


    // Use to get a proxy class for WorkflowSoap12
    private java.lang.String WorkflowSoap12_address = "http://ccla-ap12:81/Workflow/WebService/Workflow.asmx";

    public java.lang.String getWorkflowSoap12Address() {
        return WorkflowSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WorkflowSoap12WSDDServiceName = "WorkflowSoap12";

    public java.lang.String getWorkflowSoap12WSDDServiceName() {
        return WorkflowSoap12WSDDServiceName;
    }

    public void setWorkflowSoap12WSDDServiceName(java.lang.String name) {
        WorkflowSoap12WSDDServiceName = name;
    }

    public com.ecs.stellent.spp.WorkflowSoap_PortType getWorkflowSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WorkflowSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWorkflowSoap12(endpoint);
    }

    public com.ecs.stellent.spp.WorkflowSoap_PortType getWorkflowSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ecs.stellent.spp.WorkflowSoap12Stub _stub = new com.ecs.stellent.spp.WorkflowSoap12Stub(portAddress, this);
            _stub.setPortName(getWorkflowSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWorkflowSoap12EndpointAddress(java.lang.String address) {
        WorkflowSoap12_address = address;
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
            if (com.ecs.stellent.spp.WorkflowSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ecs.stellent.spp.WorkflowSoap_BindingStub _stub = new com.ecs.stellent.spp.WorkflowSoap_BindingStub(new java.net.URL(WorkflowSoap_address), this);
                _stub.setPortName(getWorkflowSoapWSDDServiceName());
                return _stub;
            }
            if (com.ecs.stellent.spp.WorkflowSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ecs.stellent.spp.WorkflowSoap12Stub _stub = new com.ecs.stellent.spp.WorkflowSoap12Stub(new java.net.URL(WorkflowSoap12_address), this);
                _stub.setPortName(getWorkflowSoap12WSDDServiceName());
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
        if ("WorkflowSoap".equals(inputPortName)) {
            return getWorkflowSoap();
        }
        else if ("WorkflowSoap12".equals(inputPortName)) {
            return getWorkflowSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ccla-ap12.ccla.local/Workflow/WebService", "Workflow");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ccla-ap12.ccla.local/Workflow/WebService", "WorkflowSoap"));
            ports.add(new javax.xml.namespace.QName("http://ccla-ap12.ccla.local/Workflow/WebService", "WorkflowSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WorkflowSoap".equals(portName)) {
            setWorkflowSoapEndpointAddress(address);
        }
        else 
if ("WorkflowSoap12".equals(portName)) {
            setWorkflowSoap12EndpointAddress(address);
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
    
    /** TM: Added this to override default transport settings */
    protected org.apache.axis.EngineConfiguration getEngineConfiguration() {
    	
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        sb.append("<deployment name=\"defaultClientConfig\"\r\n");
        sb.append("xmlns=\"http://xml.apache.org/axis/wsdd/\"\r\n");
        sb.append("xmlns:java=\"http://xml.apache.org/axis/wsdd/providers/java\">\r\n");
        sb.append("<transport name=\"http\" pivot=\"java:org.apache.axis.transport.http.CommonsHTTPSender\" />\r\n");
        sb.append("<transport name=\"local\" pivot=\"java:org.apache.axis.transport.local.LocalSender\" />\r\n");
        sb.append("<transport name=\"java\" pivot=\"java:org.apache.axis.transport.java.JavaSender\" />\r\n");
        sb.append("</deployment>\r\n");
        
        org.apache.axis.configuration.XMLStringProvider config = 
            new org.apache.axis.configuration.XMLStringProvider(sb.toString());
        return config;
    }
}
