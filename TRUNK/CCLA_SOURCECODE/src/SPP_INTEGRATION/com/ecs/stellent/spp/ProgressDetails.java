/**
 * ProgressDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ProgressDetails  implements java.io.Serializable {
    private com.ecs.stellent.spp.CNJActivityDetails[] cnjActivityDetails;

    private short jobStatus;

    private java.lang.String jobId;

    public ProgressDetails() {
    }

    public ProgressDetails(
           com.ecs.stellent.spp.CNJActivityDetails[] cnjActivityDetails,
           short jobStatus,
           java.lang.String jobId) {
           this.cnjActivityDetails = cnjActivityDetails;
           this.jobStatus = jobStatus;
           this.jobId = jobId;
    }


    /**
     * Gets the cnjActivityDetails value for this ProgressDetails.
     * 
     * @return cnjActivityDetails
     */
    public com.ecs.stellent.spp.CNJActivityDetails[] getCnjActivityDetails() {
        return cnjActivityDetails;
    }


    /**
     * Sets the cnjActivityDetails value for this ProgressDetails.
     * 
     * @param cnjActivityDetails
     */
    public void setCnjActivityDetails(com.ecs.stellent.spp.CNJActivityDetails[] cnjActivityDetails) {
        this.cnjActivityDetails = cnjActivityDetails;
    }


    /**
     * Gets the jobStatus value for this ProgressDetails.
     * 
     * @return jobStatus
     */
    public short getJobStatus() {
        return jobStatus;
    }


    /**
     * Sets the jobStatus value for this ProgressDetails.
     * 
     * @param jobStatus
     */
    public void setJobStatus(short jobStatus) {
        this.jobStatus = jobStatus;
    }


    /**
     * Gets the jobId value for this ProgressDetails.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this ProgressDetails.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProgressDetails)) return false;
        ProgressDetails other = (ProgressDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cnjActivityDetails==null && other.getCnjActivityDetails()==null) || 
             (this.cnjActivityDetails!=null &&
              java.util.Arrays.equals(this.cnjActivityDetails, other.getCnjActivityDetails()))) &&
            this.jobStatus == other.getJobStatus() &&
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId())));
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
        if (getCnjActivityDetails() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCnjActivityDetails());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCnjActivityDetails(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getJobStatus();
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProgressDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProgressDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cnjActivityDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "cnjActivityDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CNJActivityDetails"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CNJActivityDetails"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
