/**
 * GetJobProps3.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobProps3  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String jobId;

    private short jobDatabase;

    public GetJobProps3() {
    }

    public GetJobProps3(
           java.lang.String sessionId,
           java.lang.String jobId,
           short jobDatabase) {
           this.sessionId = sessionId;
           this.jobId = jobId;
           this.jobDatabase = jobDatabase;
    }


    /**
     * Gets the sessionId value for this GetJobProps3.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetJobProps3.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the jobId value for this GetJobProps3.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this GetJobProps3.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }


    /**
     * Gets the jobDatabase value for this GetJobProps3.
     * 
     * @return jobDatabase
     */
    public short getJobDatabase() {
        return jobDatabase;
    }


    /**
     * Sets the jobDatabase value for this GetJobProps3.
     * 
     * @param jobDatabase
     */
    public void setJobDatabase(short jobDatabase) {
        this.jobDatabase = jobDatabase;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobProps3)) return false;
        GetJobProps3 other = (GetJobProps3) obj;
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
            this.jobDatabase == other.getJobDatabase();
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
        _hashCode += getJobDatabase();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobProps3.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps3"));
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
        elemField.setFieldName("jobDatabase");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDatabase"));
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
