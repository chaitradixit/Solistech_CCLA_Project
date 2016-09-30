/**
 * ProWebLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

import com.ecs.utils.Log;

import intradoc.shared.SharedObjects;

/** TM: Customized this to externalise the Server Name and Port Number.
 * 
 * @author Tom
 *
 */
public class ProWebLocator extends org.apache.axis.client.Service implements com.experian.webservice.ProWeb {
	
	public static int PORT_NUMBER;
	public static String SERVER_NAME;
	
	
	static {
		PORT_NUMBER = Integer.parseInt(
		 SharedObjects.getEnvironmentValue("EXPERIAN_PortNumber"));
		
		SERVER_NAME = SharedObjects.getEnvironmentValue("EXPERIAN_ServerName");
	}
	
	// Use to get a proxy class for QAPortType
    private java.lang.String QAPortType_address =  
     "http://" + SERVER_NAME + ":" + PORT_NUMBER + "/";
	
    public ProWebLocator() {}

    public ProWebLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ProWebLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    public java.lang.String getQAPortTypeAddress() {
        return QAPortType_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String QAPortTypeWSDDServiceName = "QAPortType";

    public java.lang.String getQAPortTypeWSDDServiceName() {
        return QAPortTypeWSDDServiceName;
    }

    public void setQAPortTypeWSDDServiceName(java.lang.String name) {
        QAPortTypeWSDDServiceName = name;
    }

    public com.experian.webservice.QAPortType_PortType getQAPortType() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
    		Log.debug("USING QAS PORT NUMBER:" + Integer.toString(PORT_NUMBER));
            endpoint = new java.net.URL(QAPortType_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getQAPortType(endpoint);
    }

    public com.experian.webservice.QAPortType_PortType getQAPortType(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.experian.webservice.QASoapBindingStub _stub = new com.experian.webservice.QASoapBindingStub(portAddress, this);
            _stub.setPortName(getQAPortTypeWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setQAPortTypeEndpointAddress(java.lang.String address) {
		Log.debug("USING QAS PORT NUMBER:" + address);
        QAPortType_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.experian.webservice.QAPortType_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.experian.webservice.QASoapBindingStub _stub = new com.experian.webservice.QASoapBindingStub(new java.net.URL(QAPortType_address), this);
                _stub.setPortName(getQAPortTypeWSDDServiceName());
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
    	Log.debug("in getPort" );
    	if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        
        if ("QAPortType".equals(inputPortName)) {
            return getQAPortType();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
    	Log.debug("in getServiceName" );
        return new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "ProWeb");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAPortType"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("QAPortType".equals(portName)) {
            setQAPortTypeEndpointAddress(address);
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
