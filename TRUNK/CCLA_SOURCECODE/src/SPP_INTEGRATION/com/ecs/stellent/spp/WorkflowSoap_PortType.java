/**
 * WorkflowSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public interface WorkflowSoap_PortType extends java.rmi.Remote {
    public java.lang.String getJobID(java.lang.String ucmJobID) throws java.rmi.RemoteException;
    public java.lang.String getComplaintCategories() throws java.rmi.RemoteException;
    public java.lang.String getComplaintSubCategories(int categoryID) throws java.rmi.RemoteException;
    public java.lang.String getLookupData(short lookupCategoryID) throws java.rmi.RemoteException;
    public java.lang.String getCascadingLookupData(short cascadeID) throws java.rmi.RemoteException;
}
