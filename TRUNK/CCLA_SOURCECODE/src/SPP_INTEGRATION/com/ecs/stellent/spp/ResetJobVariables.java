/**
 * ResetJobVariables.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ResetJobVariables  implements java.io.Serializable {
    private java.lang.String sessionID;

    private java.lang.String jobID;

    private java.lang.String subJobID;

    private short nodeID;

    private short embeddedProcessCount;

    private boolean useLatestHistory;

    private java.util.Calendar setTime;

    private short setTimeMilliSecs;

    public ResetJobVariables() {
    }

    public ResetJobVariables(
           java.lang.String sessionID,
           java.lang.String jobID,
           java.lang.String subJobID,
           short nodeID,
           short embeddedProcessCount,
           boolean useLatestHistory,
           java.util.Calendar setTime,
           short setTimeMilliSecs) {
           this.sessionID = sessionID;
           this.jobID = jobID;
           this.subJobID = subJobID;
           this.nodeID = nodeID;
           this.embeddedProcessCount = embeddedProcessCount;
           this.useLatestHistory = useLatestHistory;
           this.setTime = setTime;
           this.setTimeMilliSecs = setTimeMilliSecs;
    }


    /**
     * Gets the sessionID value for this ResetJobVariables.
     * 
     * @return sessionID
     */
    public java.lang.String getSessionID() {
        return sessionID;
    }


    /**
     * Sets the sessionID value for this ResetJobVariables.
     * 
     * @param sessionID
     */
    public void setSessionID(java.lang.String sessionID) {
        this.sessionID = sessionID;
    }


    /**
     * Gets the jobID value for this ResetJobVariables.
     * 
     * @return jobID
     */
    public java.lang.String getJobID() {
        return jobID;
    }


    /**
     * Sets the jobID value for this ResetJobVariables.
     * 
     * @param jobID
     */
    public void setJobID(java.lang.String jobID) {
        this.jobID = jobID;
    }


    /**
     * Gets the subJobID value for this ResetJobVariables.
     * 
     * @return subJobID
     */
    public java.lang.String getSubJobID() {
        return subJobID;
    }


    /**
     * Sets the subJobID value for this ResetJobVariables.
     * 
     * @param subJobID
     */
    public void setSubJobID(java.lang.String subJobID) {
        this.subJobID = subJobID;
    }


    /**
     * Gets the nodeID value for this ResetJobVariables.
     * 
     * @return nodeID
     */
    public short getNodeID() {
        return nodeID;
    }


    /**
     * Sets the nodeID value for this ResetJobVariables.
     * 
     * @param nodeID
     */
    public void setNodeID(short nodeID) {
        this.nodeID = nodeID;
    }


    /**
     * Gets the embeddedProcessCount value for this ResetJobVariables.
     * 
     * @return embeddedProcessCount
     */
    public short getEmbeddedProcessCount() {
        return embeddedProcessCount;
    }


    /**
     * Sets the embeddedProcessCount value for this ResetJobVariables.
     * 
     * @param embeddedProcessCount
     */
    public void setEmbeddedProcessCount(short embeddedProcessCount) {
        this.embeddedProcessCount = embeddedProcessCount;
    }


    /**
     * Gets the useLatestHistory value for this ResetJobVariables.
     * 
     * @return useLatestHistory
     */
    public boolean isUseLatestHistory() {
        return useLatestHistory;
    }


    /**
     * Sets the useLatestHistory value for this ResetJobVariables.
     * 
     * @param useLatestHistory
     */
    public void setUseLatestHistory(boolean useLatestHistory) {
        this.useLatestHistory = useLatestHistory;
    }


    /**
     * Gets the setTime value for this ResetJobVariables.
     * 
     * @return setTime
     */
    public java.util.Calendar getSetTime() {
        return setTime;
    }


    /**
     * Sets the setTime value for this ResetJobVariables.
     * 
     * @param setTime
     */
    public void setSetTime(java.util.Calendar setTime) {
        this.setTime = setTime;
    }


    /**
     * Gets the setTimeMilliSecs value for this ResetJobVariables.
     * 
     * @return setTimeMilliSecs
     */
    public short getSetTimeMilliSecs() {
        return setTimeMilliSecs;
    }


    /**
     * Sets the setTimeMilliSecs value for this ResetJobVariables.
     * 
     * @param setTimeMilliSecs
     */
    public void setSetTimeMilliSecs(short setTimeMilliSecs) {
        this.setTimeMilliSecs = setTimeMilliSecs;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResetJobVariables)) return false;
        ResetJobVariables other = (ResetJobVariables) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sessionID==null && other.getSessionID()==null) || 
             (this.sessionID!=null &&
              this.sessionID.equals(other.getSessionID()))) &&
            ((this.jobID==null && other.getJobID()==null) || 
             (this.jobID!=null &&
              this.jobID.equals(other.getJobID()))) &&
            ((this.subJobID==null && other.getSubJobID()==null) || 
             (this.subJobID!=null &&
              this.subJobID.equals(other.getSubJobID()))) &&
            this.nodeID == other.getNodeID() &&
            this.embeddedProcessCount == other.getEmbeddedProcessCount() &&
            this.useLatestHistory == other.isUseLatestHistory() &&
            ((this.setTime==null && other.getSetTime()==null) || 
             (this.setTime!=null &&
              this.setTime.equals(other.getSetTime()))) &&
            this.setTimeMilliSecs == other.getSetTimeMilliSecs();
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
        if (getSessionID() != null) {
            _hashCode += getSessionID().hashCode();
        }
        if (getJobID() != null) {
            _hashCode += getJobID().hashCode();
        }
        if (getSubJobID() != null) {
            _hashCode += getSubJobID().hashCode();
        }
        _hashCode += getNodeID();
        _hashCode += getEmbeddedProcessCount();
        _hashCode += (isUseLatestHistory() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getSetTime() != null) {
            _hashCode += getSetTime().hashCode();
        }
        _hashCode += getSetTimeMilliSecs();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResetJobVariables.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ResetJobVariables"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subJobID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("embeddedProcessCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useLatestHistory");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseLatestHistory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("setTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SetTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("setTimeMilliSecs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SetTimeMilliSecs"));
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
