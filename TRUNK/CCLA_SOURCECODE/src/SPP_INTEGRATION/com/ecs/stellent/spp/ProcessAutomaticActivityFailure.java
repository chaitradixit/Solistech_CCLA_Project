/**
 * ProcessAutomaticActivityFailure.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ProcessAutomaticActivityFailure  implements java.io.Serializable {
    private java.lang.String exceptionCode;

    private java.lang.String jobId;

    private short nodeId;

    private short embeddedProcessCount;

    private short autoActivityType;

    private boolean cancelActivity;

    public ProcessAutomaticActivityFailure() {
    }

    public ProcessAutomaticActivityFailure(
           java.lang.String exceptionCode,
           java.lang.String jobId,
           short nodeId,
           short embeddedProcessCount,
           short autoActivityType,
           boolean cancelActivity) {
           this.exceptionCode = exceptionCode;
           this.jobId = jobId;
           this.nodeId = nodeId;
           this.embeddedProcessCount = embeddedProcessCount;
           this.autoActivityType = autoActivityType;
           this.cancelActivity = cancelActivity;
    }


    /**
     * Gets the exceptionCode value for this ProcessAutomaticActivityFailure.
     * 
     * @return exceptionCode
     */
    public java.lang.String getExceptionCode() {
        return exceptionCode;
    }


    /**
     * Sets the exceptionCode value for this ProcessAutomaticActivityFailure.
     * 
     * @param exceptionCode
     */
    public void setExceptionCode(java.lang.String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }


    /**
     * Gets the jobId value for this ProcessAutomaticActivityFailure.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this ProcessAutomaticActivityFailure.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }


    /**
     * Gets the nodeId value for this ProcessAutomaticActivityFailure.
     * 
     * @return nodeId
     */
    public short getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this ProcessAutomaticActivityFailure.
     * 
     * @param nodeId
     */
    public void setNodeId(short nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the embeddedProcessCount value for this ProcessAutomaticActivityFailure.
     * 
     * @return embeddedProcessCount
     */
    public short getEmbeddedProcessCount() {
        return embeddedProcessCount;
    }


    /**
     * Sets the embeddedProcessCount value for this ProcessAutomaticActivityFailure.
     * 
     * @param embeddedProcessCount
     */
    public void setEmbeddedProcessCount(short embeddedProcessCount) {
        this.embeddedProcessCount = embeddedProcessCount;
    }


    /**
     * Gets the autoActivityType value for this ProcessAutomaticActivityFailure.
     * 
     * @return autoActivityType
     */
    public short getAutoActivityType() {
        return autoActivityType;
    }


    /**
     * Sets the autoActivityType value for this ProcessAutomaticActivityFailure.
     * 
     * @param autoActivityType
     */
    public void setAutoActivityType(short autoActivityType) {
        this.autoActivityType = autoActivityType;
    }


    /**
     * Gets the cancelActivity value for this ProcessAutomaticActivityFailure.
     * 
     * @return cancelActivity
     */
    public boolean isCancelActivity() {
        return cancelActivity;
    }


    /**
     * Sets the cancelActivity value for this ProcessAutomaticActivityFailure.
     * 
     * @param cancelActivity
     */
    public void setCancelActivity(boolean cancelActivity) {
        this.cancelActivity = cancelActivity;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProcessAutomaticActivityFailure)) return false;
        ProcessAutomaticActivityFailure other = (ProcessAutomaticActivityFailure) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.exceptionCode==null && other.getExceptionCode()==null) || 
             (this.exceptionCode!=null &&
              this.exceptionCode.equals(other.getExceptionCode()))) &&
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId()))) &&
            this.nodeId == other.getNodeId() &&
            this.embeddedProcessCount == other.getEmbeddedProcessCount() &&
            this.autoActivityType == other.getAutoActivityType() &&
            this.cancelActivity == other.isCancelActivity();
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
        if (getExceptionCode() != null) {
            _hashCode += getExceptionCode().hashCode();
        }
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        _hashCode += getNodeId();
        _hashCode += getEmbeddedProcessCount();
        _hashCode += getAutoActivityType();
        _hashCode += (isCancelActivity() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProcessAutomaticActivityFailure.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ProcessAutomaticActivityFailure"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exceptionCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExceptionCode"));
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
        elemField.setFieldName("embeddedProcessCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("autoActivityType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AutoActivityType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cancelActivity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CancelActivity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
