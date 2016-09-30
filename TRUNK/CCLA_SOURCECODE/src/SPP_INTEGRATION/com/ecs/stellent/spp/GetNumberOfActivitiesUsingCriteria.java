/**
 * GetNumberOfActivitiesUsingCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetNumberOfActivitiesUsingCriteria  implements java.io.Serializable {
    private java.lang.String sessionId;

    private boolean usingJobId;

    private java.lang.String jobId;

    private boolean usingStatus;

    private short status;

    public GetNumberOfActivitiesUsingCriteria() {
    }

    public GetNumberOfActivitiesUsingCriteria(
           java.lang.String sessionId,
           boolean usingJobId,
           java.lang.String jobId,
           boolean usingStatus,
           short status) {
           this.sessionId = sessionId;
           this.usingJobId = usingJobId;
           this.jobId = jobId;
           this.usingStatus = usingStatus;
           this.status = status;
    }


    /**
     * Gets the sessionId value for this GetNumberOfActivitiesUsingCriteria.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetNumberOfActivitiesUsingCriteria.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the usingJobId value for this GetNumberOfActivitiesUsingCriteria.
     * 
     * @return usingJobId
     */
    public boolean isUsingJobId() {
        return usingJobId;
    }


    /**
     * Sets the usingJobId value for this GetNumberOfActivitiesUsingCriteria.
     * 
     * @param usingJobId
     */
    public void setUsingJobId(boolean usingJobId) {
        this.usingJobId = usingJobId;
    }


    /**
     * Gets the jobId value for this GetNumberOfActivitiesUsingCriteria.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this GetNumberOfActivitiesUsingCriteria.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }


    /**
     * Gets the usingStatus value for this GetNumberOfActivitiesUsingCriteria.
     * 
     * @return usingStatus
     */
    public boolean isUsingStatus() {
        return usingStatus;
    }


    /**
     * Sets the usingStatus value for this GetNumberOfActivitiesUsingCriteria.
     * 
     * @param usingStatus
     */
    public void setUsingStatus(boolean usingStatus) {
        this.usingStatus = usingStatus;
    }


    /**
     * Gets the status value for this GetNumberOfActivitiesUsingCriteria.
     * 
     * @return status
     */
    public short getStatus() {
        return status;
    }


    /**
     * Sets the status value for this GetNumberOfActivitiesUsingCriteria.
     * 
     * @param status
     */
    public void setStatus(short status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetNumberOfActivitiesUsingCriteria)) return false;
        GetNumberOfActivitiesUsingCriteria other = (GetNumberOfActivitiesUsingCriteria) obj;
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
            this.usingJobId == other.isUsingJobId() &&
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId()))) &&
            this.usingStatus == other.isUsingStatus() &&
            this.status == other.getStatus();
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
        _hashCode += (isUsingJobId() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        _hashCode += (isUsingStatus() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getStatus();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetNumberOfActivitiesUsingCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNumberOfActivitiesUsingCriteria"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usingJobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsingJobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("usingStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsingStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"));
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
