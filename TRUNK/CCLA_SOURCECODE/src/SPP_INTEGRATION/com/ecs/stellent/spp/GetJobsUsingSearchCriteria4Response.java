/**
 * GetJobsUsingSearchCriteria4Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobsUsingSearchCriteria4Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.ExtendedJobs getJobsUsingSearchCriteria4Result;

    public GetJobsUsingSearchCriteria4Response() {
    }

    public GetJobsUsingSearchCriteria4Response(
           com.ecs.stellent.spp.ExtendedJobs getJobsUsingSearchCriteria4Result) {
           this.getJobsUsingSearchCriteria4Result = getJobsUsingSearchCriteria4Result;
    }


    /**
     * Gets the getJobsUsingSearchCriteria4Result value for this GetJobsUsingSearchCriteria4Response.
     * 
     * @return getJobsUsingSearchCriteria4Result
     */
    public com.ecs.stellent.spp.ExtendedJobs getGetJobsUsingSearchCriteria4Result() {
        return getJobsUsingSearchCriteria4Result;
    }


    /**
     * Sets the getJobsUsingSearchCriteria4Result value for this GetJobsUsingSearchCriteria4Response.
     * 
     * @param getJobsUsingSearchCriteria4Result
     */
    public void setGetJobsUsingSearchCriteria4Result(com.ecs.stellent.spp.ExtendedJobs getJobsUsingSearchCriteria4Result) {
        this.getJobsUsingSearchCriteria4Result = getJobsUsingSearchCriteria4Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobsUsingSearchCriteria4Response)) return false;
        GetJobsUsingSearchCriteria4Response other = (GetJobsUsingSearchCriteria4Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getJobsUsingSearchCriteria4Result==null && other.getGetJobsUsingSearchCriteria4Result()==null) || 
             (this.getJobsUsingSearchCriteria4Result!=null &&
              this.getJobsUsingSearchCriteria4Result.equals(other.getGetJobsUsingSearchCriteria4Result())));
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
        if (getGetJobsUsingSearchCriteria4Result() != null) {
            _hashCode += getGetJobsUsingSearchCriteria4Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobsUsingSearchCriteria4Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria4Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getJobsUsingSearchCriteria4Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteria4Result"));
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
