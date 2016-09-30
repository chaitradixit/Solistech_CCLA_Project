/**
 * GetJobProps4Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobProps4Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.JobProperties4 getJobProps4Result;

    public GetJobProps4Response() {
    }

    public GetJobProps4Response(
           com.ecs.stellent.spp.JobProperties4 getJobProps4Result) {
           this.getJobProps4Result = getJobProps4Result;
    }


    /**
     * Gets the getJobProps4Result value for this GetJobProps4Response.
     * 
     * @return getJobProps4Result
     */
    public com.ecs.stellent.spp.JobProperties4 getGetJobProps4Result() {
        return getJobProps4Result;
    }


    /**
     * Sets the getJobProps4Result value for this GetJobProps4Response.
     * 
     * @param getJobProps4Result
     */
    public void setGetJobProps4Result(com.ecs.stellent.spp.JobProperties4 getJobProps4Result) {
        this.getJobProps4Result = getJobProps4Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobProps4Response)) return false;
        GetJobProps4Response other = (GetJobProps4Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getJobProps4Result==null && other.getGetJobProps4Result()==null) || 
             (this.getJobProps4Result!=null &&
              this.getJobProps4Result.equals(other.getGetJobProps4Result())));
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
        if (getGetJobProps4Result() != null) {
            _hashCode += getGetJobProps4Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobProps4Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps4Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getJobProps4Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobProps4Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties4"));
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
