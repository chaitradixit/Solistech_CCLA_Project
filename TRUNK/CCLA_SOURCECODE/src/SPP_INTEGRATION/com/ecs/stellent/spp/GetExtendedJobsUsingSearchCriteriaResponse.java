/**
 * GetExtendedJobsUsingSearchCriteriaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetExtendedJobsUsingSearchCriteriaResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.ExtendedJobs getExtendedJobsUsingSearchCriteriaResult;

    public GetExtendedJobsUsingSearchCriteriaResponse() {
    }

    public GetExtendedJobsUsingSearchCriteriaResponse(
           com.ecs.stellent.spp.ExtendedJobs getExtendedJobsUsingSearchCriteriaResult) {
           this.getExtendedJobsUsingSearchCriteriaResult = getExtendedJobsUsingSearchCriteriaResult;
    }


    /**
     * Gets the getExtendedJobsUsingSearchCriteriaResult value for this GetExtendedJobsUsingSearchCriteriaResponse.
     * 
     * @return getExtendedJobsUsingSearchCriteriaResult
     */
    public com.ecs.stellent.spp.ExtendedJobs getGetExtendedJobsUsingSearchCriteriaResult() {
        return getExtendedJobsUsingSearchCriteriaResult;
    }


    /**
     * Sets the getExtendedJobsUsingSearchCriteriaResult value for this GetExtendedJobsUsingSearchCriteriaResponse.
     * 
     * @param getExtendedJobsUsingSearchCriteriaResult
     */
    public void setGetExtendedJobsUsingSearchCriteriaResult(com.ecs.stellent.spp.ExtendedJobs getExtendedJobsUsingSearchCriteriaResult) {
        this.getExtendedJobsUsingSearchCriteriaResult = getExtendedJobsUsingSearchCriteriaResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtendedJobsUsingSearchCriteriaResponse)) return false;
        GetExtendedJobsUsingSearchCriteriaResponse other = (GetExtendedJobsUsingSearchCriteriaResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getExtendedJobsUsingSearchCriteriaResult==null && other.getGetExtendedJobsUsingSearchCriteriaResult()==null) || 
             (this.getExtendedJobsUsingSearchCriteriaResult!=null &&
              this.getExtendedJobsUsingSearchCriteriaResult.equals(other.getGetExtendedJobsUsingSearchCriteriaResult())));
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
        if (getGetExtendedJobsUsingSearchCriteriaResult() != null) {
            _hashCode += getGetExtendedJobsUsingSearchCriteriaResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtendedJobsUsingSearchCriteriaResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedJobsUsingSearchCriteriaResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getExtendedJobsUsingSearchCriteriaResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedJobsUsingSearchCriteriaResult"));
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
