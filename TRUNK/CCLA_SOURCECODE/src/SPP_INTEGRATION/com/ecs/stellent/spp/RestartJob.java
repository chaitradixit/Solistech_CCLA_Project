/**
 * RestartJob.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class RestartJob  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String jobId;

    private short nodeId;

    private com.ecs.stellent.spp.EmbeddedProcessList[] EPList;

    private short restartType;

    public RestartJob() {
    }

    public RestartJob(
           java.lang.String sessionId,
           java.lang.String jobId,
           short nodeId,
           com.ecs.stellent.spp.EmbeddedProcessList[] EPList,
           short restartType) {
           this.sessionId = sessionId;
           this.jobId = jobId;
           this.nodeId = nodeId;
           this.EPList = EPList;
           this.restartType = restartType;
    }


    /**
     * Gets the sessionId value for this RestartJob.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this RestartJob.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the jobId value for this RestartJob.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this RestartJob.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }


    /**
     * Gets the nodeId value for this RestartJob.
     * 
     * @return nodeId
     */
    public short getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this RestartJob.
     * 
     * @param nodeId
     */
    public void setNodeId(short nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the EPList value for this RestartJob.
     * 
     * @return EPList
     */
    public com.ecs.stellent.spp.EmbeddedProcessList[] getEPList() {
        return EPList;
    }


    /**
     * Sets the EPList value for this RestartJob.
     * 
     * @param EPList
     */
    public void setEPList(com.ecs.stellent.spp.EmbeddedProcessList[] EPList) {
        this.EPList = EPList;
    }


    /**
     * Gets the restartType value for this RestartJob.
     * 
     * @return restartType
     */
    public short getRestartType() {
        return restartType;
    }


    /**
     * Sets the restartType value for this RestartJob.
     * 
     * @param restartType
     */
    public void setRestartType(short restartType) {
        this.restartType = restartType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RestartJob)) return false;
        RestartJob other = (RestartJob) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sessionId==null && other.getSessionId()==null) || 
             (this.sessionId!=null &&
              this.sessionId.equals(other.getSessionId()))) &&
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId()))) &&
            this.nodeId == other.getNodeId() &&
            ((this.EPList==null && other.getEPList()==null) || 
             (this.EPList!=null &&
              java.util.Arrays.equals(this.EPList, other.getEPList()))) &&
            this.restartType == other.getRestartType();
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
        if (getSessionId() != null) {
            _hashCode += getSessionId().hashCode();
        }
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        _hashCode += getNodeId();
        if (getEPList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEPList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEPList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getRestartType();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RestartJob.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RestartJob"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EPList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EPList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessList"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessList"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("restartType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RestartType"));
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
