/**
 * Workflow.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public interface Workflow extends javax.xml.rpc.Service {
    public java.lang.String getWorkflowSoapAddress();

    public com.ecs.stellent.spp.WorkflowSoap_PortType getWorkflowSoap() throws javax.xml.rpc.ServiceException;

    public com.ecs.stellent.spp.WorkflowSoap_PortType getWorkflowSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getWorkflowSoap12Address();

    public com.ecs.stellent.spp.WorkflowSoap_PortType getWorkflowSoap12() throws javax.xml.rpc.ServiceException;

    public com.ecs.stellent.spp.WorkflowSoap_PortType getWorkflowSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
