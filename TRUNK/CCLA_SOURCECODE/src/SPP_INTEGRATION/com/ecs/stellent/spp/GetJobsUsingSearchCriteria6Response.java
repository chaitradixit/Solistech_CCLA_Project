/**
 * GetJobsUsingSearchCriteria6Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobsUsingSearchCriteria6Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.ExtendedJobs2 getJobsUsingSearchCriteria6Result;

    public GetJobsUsingSearchCriteria6Response() {
    }

    public GetJobsUsingSearchCriteria6Response(
           com.ecs.stellent.spp.ExtendedJobs2 getJobsUsingSearchCriteria6Result) {
           this.getJobsUsingSearchCriteria6Result = getJobsUsingSearchCriteria6Result;
    }


    /**
     * Gets the getJobsUsingSearchCriteria6Result value for this GetJobsUsingSearchCriteria6Response.
     * 
     * @return getJobsUsingSearchCriteria6Result
     */
    public com.ecs.stellent.spp.ExtendedJobs2 getGetJobsUsingSearchCriteria6Result() {
        return getJobsUsingSearchCriteria6Result;
    }


    /**
     * Sets the getJobsUsingSearchCriteria6Result value for this GetJobsUsingSearchCriteria6Response.
     * 
     * @param getJobsUsingSearchCriteria6Result
     */
    public void setGetJobsUsingSearchCriteria6Result(com.ecs.stellent.spp.ExtendedJobs2 getJobsUsingSearchCriteria6Result) {
        this.getJobsUsingSearchCriteria6Result = getJobsUsingSearchCriteria6Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobsUsingSearchCriteria6Response)) return false;
        GetJobsUsingSearchCriteria6Response other = (GetJobsUsingSearchCriteria6Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getJobsUsingSearchCriteria6Result==null && other.getGetJobsUsingSearchCriteria6Result()==null) || 
             (this.getJobsUsingSearchCriteria6Result!=null &&
              this.getJobsUsingSearchCriteria6Result.equals(other.getGetJobsUsingSearchCriteria6Result())));
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
        if (getGetJobsUsingSearchCriteria6Result() != null) {
            _hashCode += getGetJobsUsingSearchCriteria6Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobsUsingSearchCriteria6Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria6Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getJobsUsingSearchCriteria6Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteria6Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobs2"));
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
