/**
 * GetJobProps3Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobProps3Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.JobProperties3 getJobProps3Result;

    public GetJobProps3Response() {
    }

    public GetJobProps3Response(
           com.ecs.stellent.spp.JobProperties3 getJobProps3Result) {
           this.getJobProps3Result = getJobProps3Result;
    }


    /**
     * Gets the getJobProps3Result value for this GetJobProps3Response.
     * 
     * @return getJobProps3Result
     */
    public com.ecs.stellent.spp.JobProperties3 getGetJobProps3Result() {
        return getJobProps3Result;
    }


    /**
     * Sets the getJobProps3Result value for this GetJobProps3Response.
     * 
     * @param getJobProps3Result
     */
    public void setGetJobProps3Result(com.ecs.stellent.spp.JobProperties3 getJobProps3Result) {
        this.getJobProps3Result = getJobProps3Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobProps3Response)) return false;
        GetJobProps3Response other = (GetJobProps3Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getJobProps3Result==null && other.getGetJobProps3Result()==null) || 
             (this.getJobProps3Result!=null &&
              this.getJobProps3Result.equals(other.getGetJobProps3Result())));
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
        if (getGetJobProps3Result() != null) {
            _hashCode += getGetJobProps3Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobProps3Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps3Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getJobProps3Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobProps3Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties3"));
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
