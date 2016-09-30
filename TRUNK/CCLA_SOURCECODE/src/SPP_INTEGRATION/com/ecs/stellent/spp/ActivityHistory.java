/**
 * ActivityHistory.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ActivityHistory  implements java.io.Serializable {
    private java.lang.String processName;

    private double version;

    private java.lang.String nodeName;

    private java.lang.String resName;

    private java.util.Calendar when;

    private short status;

    private double cost;

    private int timeSpentInSeconds;

    private short nodeId;

    private short nodeIdAfterDecision;

    private java.lang.String serverName;

    private java.lang.String processId;

    private int workingTimeSpent;

    private java.lang.String subJobId;

    private java.lang.String startedSubJobId;

    private short startedEPC;

    public ActivityHistory() {
    }

    public ActivityHistory(
           java.lang.String processName,
           double version,
           java.lang.String nodeName,
           java.lang.String resName,
           java.util.Calendar when,
           short status,
           double cost,
           int timeSpentInSeconds,
           short nodeId,
           short nodeIdAfterDecision,
           java.lang.String serverName,
           java.lang.String processId,
           int workingTimeSpent,
           java.lang.String subJobId,
           java.lang.String startedSubJobId,
           short startedEPC) {
           this.processName = processName;
           this.version = version;
           this.nodeName = nodeName;
           this.resName = resName;
           this.when = when;
           this.status = status;
           this.cost = cost;
           this.timeSpentInSeconds = timeSpentInSeconds;
           this.nodeId = nodeId;
           this.nodeIdAfterDecision = nodeIdAfterDecision;
           this.serverName = serverName;
           this.processId = processId;
           this.workingTimeSpent = workingTimeSpent;
           this.subJobId = subJobId;
           this.startedSubJobId = startedSubJobId;
           this.startedEPC = startedEPC;
    }


    /**
     * Gets the processName value for this ActivityHistory.
     * 
     * @return processName
     */
    public java.lang.String getProcessName() {
        return processName;
    }


    /**
     * Sets the processName value for this ActivityHistory.
     * 
     * @param processName
     */
    public void setProcessName(java.lang.String processName) {
        this.processName = processName;
    }


    /**
     * Gets the version value for this ActivityHistory.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this ActivityHistory.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the nodeName value for this ActivityHistory.
     * 
     * @return nodeName
     */
    public java.lang.String getNodeName() {
        return nodeName;
    }


    /**
     * Sets the nodeName value for this ActivityHistory.
     * 
     * @param nodeName
     */
    public void setNodeName(java.lang.String nodeName) {
        this.nodeName = nodeName;
    }


    /**
     * Gets the resName value for this ActivityHistory.
     * 
     * @return resName
     */
    public java.lang.String getResName() {
        return resName;
    }


    /**
     * Sets the resName value for this ActivityHistory.
     * 
     * @param resName
     */
    public void setResName(java.lang.String resName) {
        this.resName = resName;
    }


    /**
     * Gets the when value for this ActivityHistory.
     * 
     * @return when
     */
    public java.util.Calendar getWhen() {
        return when;
    }


    /**
     * Sets the when value for this ActivityHistory.
     * 
     * @param when
     */
    public void setWhen(java.util.Calendar when) {
        this.when = when;
    }


    /**
     * Gets the status value for this ActivityHistory.
     * 
     * @return status
     */
    public short getStatus() {
        return status;
    }


    /**
     * Sets the status value for this ActivityHistory.
     * 
     * @param status
     */
    public void setStatus(short status) {
        this.status = status;
    }


    /**
     * Gets the cost value for this ActivityHistory.
     * 
     * @return cost
     */
    public double getCost() {
        return cost;
    }


    /**
     * Sets the cost value for this ActivityHistory.
     * 
     * @param cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }


    /**
     * Gets the timeSpentInSeconds value for this ActivityHistory.
     * 
     * @return timeSpentInSeconds
     */
    public int getTimeSpentInSeconds() {
        return timeSpentInSeconds;
    }


    /**
     * Sets the timeSpentInSeconds value for this ActivityHistory.
     * 
     * @param timeSpentInSeconds
     */
    public void setTimeSpentInSeconds(int timeSpentInSeconds) {
        this.timeSpentInSeconds = timeSpentInSeconds;
    }


    /**
     * Gets the nodeId value for this ActivityHistory.
     * 
     * @return nodeId
     */
    public short getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this ActivityHistory.
     * 
     * @param nodeId
     */
    public void setNodeId(short nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the nodeIdAfterDecision value for this ActivityHistory.
     * 
     * @return nodeIdAfterDecision
     */
    public short getNodeIdAfterDecision() {
        return nodeIdAfterDecision;
    }


    /**
     * Sets the nodeIdAfterDecision value for this ActivityHistory.
     * 
     * @param nodeIdAfterDecision
     */
    public void setNodeIdAfterDecision(short nodeIdAfterDecision) {
        this.nodeIdAfterDecision = nodeIdAfterDecision;
    }


    /**
     * Gets the serverName value for this ActivityHistory.
     * 
     * @return serverName
     */
    public java.lang.String getServerName() {
        return serverName;
    }


    /**
     * Sets the serverName value for this ActivityHistory.
     * 
     * @param serverName
     */
    public void setServerName(java.lang.String serverName) {
        this.serverName = serverName;
    }


    /**
     * Gets the processId value for this ActivityHistory.
     * 
     * @return processId
     */
    public java.lang.String getProcessId() {
        return processId;
    }


    /**
     * Sets the processId value for this ActivityHistory.
     * 
     * @param processId
     */
    public void setProcessId(java.lang.String processId) {
        this.processId = processId;
    }


    /**
     * Gets the workingTimeSpent value for this ActivityHistory.
     * 
     * @return workingTimeSpent
     */
    public int getWorkingTimeSpent() {
        return workingTimeSpent;
    }


    /**
     * Sets the workingTimeSpent value for this ActivityHistory.
     * 
     * @param workingTimeSpent
     */
    public void setWorkingTimeSpent(int workingTimeSpent) {
        this.workingTimeSpent = workingTimeSpent;
    }


    /**
     * Gets the subJobId value for this ActivityHistory.
     * 
     * @return subJobId
     */
    public java.lang.String getSubJobId() {
        return subJobId;
    }


    /**
     * Sets the subJobId value for this ActivityHistory.
     * 
     * @param subJobId
     */
    public void setSubJobId(java.lang.String subJobId) {
        this.subJobId = subJobId;
    }


    /**
     * Gets the startedSubJobId value for this ActivityHistory.
     * 
     * @return startedSubJobId
     */
    public java.lang.String getStartedSubJobId() {
        return startedSubJobId;
    }


    /**
     * Sets the startedSubJobId value for this ActivityHistory.
     * 
     * @param startedSubJobId
     */
    public void setStartedSubJobId(java.lang.String startedSubJobId) {
        this.startedSubJobId = startedSubJobId;
    }


    /**
     * Gets the startedEPC value for this ActivityHistory.
     * 
     * @return startedEPC
     */
    public short getStartedEPC() {
        return startedEPC;
    }


    /**
     * Sets the startedEPC value for this ActivityHistory.
     * 
     * @param startedEPC
     */
    public void setStartedEPC(short startedEPC) {
        this.startedEPC = startedEPC;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ActivityHistory)) return false;
        ActivityHistory other = (ActivityHistory) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.processName==null && other.getProcessName()==null) || 
             (this.processName!=null &&
              this.processName.equals(other.getProcessName()))) &&
            this.version == other.getVersion() &&
            ((this.nodeName==null && other.getNodeName()==null) || 
             (this.nodeName!=null &&
              this.nodeName.equals(other.getNodeName()))) &&
            ((this.resName==null && other.getResName()==null) || 
             (this.resName!=null &&
              this.resName.equals(other.getResName()))) &&
            ((this.when==null && other.getWhen()==null) || 
             (this.when!=null &&
              this.when.equals(other.getWhen()))) &&
            this.status == other.getStatus() &&
            this.cost == other.getCost() &&
            this.timeSpentInSeconds == other.getTimeSpentInSeconds() &&
            this.nodeId == other.getNodeId() &&
            this.nodeIdAfterDecision == other.getNodeIdAfterDecision() &&
            ((this.serverName==null && other.getServerName()==null) || 
             (this.serverName!=null &&
              this.serverName.equals(other.getServerName()))) &&
            ((this.processId==null && other.getProcessId()==null) || 
             (this.processId!=null &&
              this.processId.equals(other.getProcessId()))) &&
            this.workingTimeSpent == other.getWorkingTimeSpent() &&
            ((this.subJobId==null && other.getSubJobId()==null) || 
             (this.subJobId!=null &&
              this.subJobId.equals(other.getSubJobId()))) &&
            ((this.startedSubJobId==null && other.getStartedSubJobId()==null) || 
             (this.startedSubJobId!=null &&
              this.startedSubJobId.equals(other.getStartedSubJobId()))) &&
            this.startedEPC == other.getStartedEPC();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getProcessName() != null) {
            _hashCode += getProcessName().hashCode();
        }
        _hashCode += new Double(getVersion()).hashCode();
        if (getNodeName() != null) {
            _hashCode += getNodeName().hashCode();
        }
        if (getResName() != null) {
            _hashCode += getResName().hashCode();
        }
        if (getWhen() != null) {
            _hashCode += getWhen().hashCode();
        }
        _hashCode += getStatus();
        _hashCode += new Double(getCost()).hashCode();
        _hashCode += getTimeSpentInSeconds();
        _hashCode += getNodeId();
        _hashCode += getNodeIdAfterDecision();
        if (getServerName() != null) {
            _hashCode += getServerName().hashCode();
        }
        if (getProcessId() != null) {
            _hashCode += getProcessId().hashCode();
        }
        _hashCode += getWorkingTimeSpent();
        if (getSubJobId() != null) {
            _hashCode += getSubJobId().hashCode();
        }
        if (getStartedSubJobId() != null) {
            _hashCode += getStartedSubJobId().hashCode();
        }
        _hashCode += getStartedEPC();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ActivityHistory.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("when");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "When"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Cost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeSpentInSeconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeSpentInSeconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeIdAfterDecision");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeIdAfterDecision"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serverName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ServerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workingTimeSpent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkingTimeSpent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subJobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startedSubJobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartedSubJobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startedEPC");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartedEPC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
