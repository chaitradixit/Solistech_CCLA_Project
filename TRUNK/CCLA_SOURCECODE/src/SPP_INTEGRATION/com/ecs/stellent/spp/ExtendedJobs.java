/**
 * ExtendedJobs.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ExtendedJobs  implements java.io.Serializable {
    private com.ecs.stellent.spp.ExtendedMetaFieldDetails[] metaFieldDetails;

    private com.ecs.stellent.spp.ExtendedJobDetails[] jobDetails;

    public ExtendedJobs() {
    }

    public ExtendedJobs(
           com.ecs.stellent.spp.ExtendedMetaFieldDetails[] metaFieldDetails,
           com.ecs.stellent.spp.ExtendedJobDetails[] jobDetails) {
           this.metaFieldDetails = metaFieldDetails;
           this.jobDetails = jobDetails;
    }


    /**
     * Gets the metaFieldDetails value for this ExtendedJobs.
     * 
     * @return metaFieldDetails
     */
    public com.ecs.stellent.spp.ExtendedMetaFieldDetails[] getMetaFieldDetails() {
        return metaFieldDetails;
    }


    /**
     * Sets the metaFieldDetails value for this ExtendedJobs.
     * 
     * @param metaFieldDetails
     */
    public void setMetaFieldDetails(com.ecs.stellent.spp.ExtendedMetaFieldDetails[] metaFieldDetails) {
        this.metaFieldDetails = metaFieldDetails;
    }


    /**
     * Gets the jobDetails value for this ExtendedJobs.
     * 
     * @return jobDetails
     */
    public com.ecs.stellent.spp.ExtendedJobDetails[] getJobDetails() {
        return jobDetails;
    }


    /**
     * Sets the jobDetails value for this ExtendedJobs.
     * 
     * @param jobDetails
     */
    public void setJobDetails(com.ecs.stellent.spp.ExtendedJobDetails[] jobDetails) {
        this.jobDetails = jobDetails;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExtendedJobs)) return false;
        ExtendedJobs other = (ExtendedJobs) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.metaFieldDetails==null && other.getMetaFieldDetails()==null) || 
             (this.metaFieldDetails!=null &&
              java.util.Arrays.equals(this.metaFieldDetails, other.getMetaFieldDetails()))) &&
            ((this.jobDetails==null && other.getJobDetails()==null) || 
             (this.jobDetails!=null &&
              java.util.Arrays.equals(this.jobDetails, other.getJobDetails())));
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
        if (getMetaFieldDetails() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMetaFieldDetails());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMetaFieldDetails(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getJobDetails() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getJobDetails());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getJobDetails(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExtendedJobs.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobs"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("metaFieldDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MetaFieldDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedMetaFieldDetails"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedMetaFieldDetails"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "jobDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobDetails"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobDetails"));
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
