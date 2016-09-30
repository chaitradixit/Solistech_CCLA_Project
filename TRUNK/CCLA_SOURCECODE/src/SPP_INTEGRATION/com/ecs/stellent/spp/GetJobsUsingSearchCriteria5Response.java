/**
 * GetJobsUsingSearchCriteria5Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobsUsingSearchCriteria5Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.ExtendedJobs getJobsUsingSearchCriteria5Result;

    public GetJobsUsingSearchCriteria5Response() {
    }

    public GetJobsUsingSearchCriteria5Response(
           com.ecs.stellent.spp.ExtendedJobs getJobsUsingSearchCriteria5Result) {
           this.getJobsUsingSearchCriteria5Result = getJobsUsingSearchCriteria5Result;
    }


    /**
     * Gets the getJobsUsingSearchCriteria5Result value for this GetJobsUsingSearchCriteria5Response.
     * 
     * @return getJobsUsingSearchCriteria5Result
     */
    public com.ecs.stellent.spp.ExtendedJobs getGetJobsUsingSearchCriteria5Result() {
        return getJobsUsingSearchCriteria5Result;
    }


    /**
     * Sets the getJobsUsingSearchCriteria5Result value for this GetJobsUsingSearchCriteria5Response.
     * 
     * @param getJobsUsingSearchCriteria5Result
     */
    public void setGetJobsUsingSearchCriteria5Result(com.ecs.stellent.spp.ExtendedJobs getJobsUsingSearchCriteria5Result) {
        this.getJobsUsingSearchCriteria5Result = getJobsUsingSearchCriteria5Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobsUsingSearchCriteria5Response)) return false;
        GetJobsUsingSearchCriteria5Response other = (GetJobsUsingSearchCriteria5Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getJobsUsingSearchCriteria5Result==null && other.getGetJobsUsingSearchCriteria5Result()==null) || 
             (this.getJobsUsingSearchCriteria5Result!=null &&
              this.getJobsUsingSearchCriteria5Result.equals(other.getGetJobsUsingSearchCriteria5Result())));
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
        if (getGetJobsUsingSearchCriteria5Result() != null) {
            _hashCode += getGetJobsUsingSearchCriteria5Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobsUsingSearchCriteria5Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria5Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getJobsUsingSearchCriteria5Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteria5Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobs"));
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
