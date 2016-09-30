/**
 * GetJobsUsingSearchCriteriaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobsUsingSearchCriteriaResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.JobDetails[] getJobsUsingSearchCriteriaResult;

    public GetJobsUsingSearchCriteriaResponse() {
    }

    public GetJobsUsingSearchCriteriaResponse(
           com.ecs.stellent.spp.JobDetails[] getJobsUsingSearchCriteriaResult) {
           this.getJobsUsingSearchCriteriaResult = getJobsUsingSearchCriteriaResult;
    }


    /**
     * Gets the getJobsUsingSearchCriteriaResult value for this GetJobsUsingSearchCriteriaResponse.
     * 
     * @return getJobsUsingSearchCriteriaResult
     */
    public com.ecs.stellent.spp.JobDetails[] getGetJobsUsingSearchCriteriaResult() {
        return getJobsUsingSearchCriteriaResult;
    }


    /**
     * Sets the getJobsUsingSearchCriteriaResult value for this GetJobsUsingSearchCriteriaResponse.
     * 
     * @param getJobsUsingSearchCriteriaResult
     */
    public void setGetJobsUsingSearchCriteriaResult(com.ecs.stellent.spp.JobDetails[] getJobsUsingSearchCriteriaResult) {
        this.getJobsUsingSearchCriteriaResult = getJobsUsingSearchCriteriaResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobsUsingSearchCriteriaResponse)) return false;
        GetJobsUsingSearchCriteriaResponse other = (GetJobsUsingSearchCriteriaResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getJobsUsingSearchCriteriaResult==null && other.getGetJobsUsingSearchCriteriaResult()==null) || 
             (this.getJobsUsingSearchCriteriaResult!=null &&
              java.util.Arrays.equals(this.getJobsUsingSearchCriteriaResult, other.getGetJobsUsingSearchCriteriaResult())));
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
        if (getGetJobsUsingSearchCriteriaResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetJobsUsingSearchCriteriaResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetJobsUsingSearchCriteriaResult(), i);
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
        new org.apache.axis.description.TypeDesc(GetJobsUsingSearchCriteriaResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteriaResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getJobsUsingSearchCriteriaResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteriaResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDetails"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDetails"));
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
