/**
 * RestartJobUsingNodeName.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class RestartJobUsingNodeName  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String jobId;

    private java.lang.String nodeName;

    private com.ecs.stellent.spp.EmbeddedProcessList[] EPList;

    private short restartType;

    public RestartJobUsingNodeName() {
    }

    public RestartJobUsingNodeName(
           java.lang.String sessionId,
           java.lang.String jobId,
           java.lang.String nodeName,
           com.ecs.stellent.spp.EmbeddedProcessList[] EPList,
           short restartType) {
           this.sessionId = sessionId;
           this.jobId = jobId;
           this.nodeName = nodeName;
           this.EPList = EPList;
           this.restartType = restartType;
    }


    /**
     * Gets the sessionId value for this RestartJobUsingNodeName.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this RestartJobUsingNodeName.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the jobId value for this RestartJobUsingNodeName.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this RestartJobUsingNodeName.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }


    /**
     * Gets the nodeName value for this RestartJobUsingNodeName.
     * 
     * @return nodeName
     */
    public java.lang.String getNodeName() {
        return nodeName;
    }


    /**
     * Sets the nodeName value for this RestartJobUsingNodeName.
     * 
     * @param nodeName
     */
    public void setNodeName(java.lang.String nodeName) {
        this.nodeName = nodeName;
    }


    /**
     * Gets the EPList value for this RestartJobUsingNodeName.
     * 
     * @return EPList
     */
    public com.ecs.stellent.spp.EmbeddedProcessList[] getEPList() {
        return EPList;
    }


    /**
     * Sets the EPList value for this RestartJobUsingNodeName.
     * 
     * @param EPList
     */
    public void setEPList(com.ecs.stellent.spp.EmbeddedProcessList[] EPList) {
        this.EPList = EPList;
    }


    /**
     * Gets the restartType value for this RestartJobUsingNodeName.
     * 
     * @return restartType
     */
    public short getRestartType() {
        return restartType;
    }


    /**
     * Sets the restartType value for this RestartJobUsingNodeName.
     * 
     * @param restartType
     */
    public void setRestartType(short restartType) {
        this.restartType = restartType;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RestartJobUsingNodeName)) return false;
        RestartJobUsingNodeName other = (RestartJobUsingNodeName) obj;
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
            ((this.nodeName==null && other.getNodeName()==null) || 
             (this.nodeName!=null &&
              this.nodeName.equals(other.getNodeName()))) &&
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
        if (getNodeName() != null) {
            _hashCode += getNodeName().hashCode();
        }
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
        new org.apache.axis.description.TypeDesc(RestartJobUsingNodeName.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RestartJobUsingNodeName"));
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
        elemField.setFieldName("nodeName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
