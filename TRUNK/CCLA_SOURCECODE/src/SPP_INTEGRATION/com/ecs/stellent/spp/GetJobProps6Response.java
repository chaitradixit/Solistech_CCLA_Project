/**
 * GetJobProps6Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobProps6Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.JobProperties6 getJobProps6Result;

    public GetJobProps6Response() {
    }

    public GetJobProps6Response(
           com.ecs.stellent.spp.JobProperties6 getJobProps6Result) {
           this.getJobProps6Result = getJobProps6Result;
    }


    /**
     * Gets the getJobProps6Result value for this GetJobProps6Response.
     * 
     * @return getJobProps6Result
     */
    public com.ecs.stellent.spp.JobProperties6 getGetJobProps6Result() {
        return getJobProps6Result;
    }


    /**
     * Sets the getJobProps6Result value for this GetJobProps6Response.
     * 
     * @param getJobProps6Result
     */
    public void setGetJobProps6Result(com.ecs.stellent.spp.JobProperties6 getJobProps6Result) {
        this.getJobProps6Result = getJobProps6Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobProps6Response)) return false;
        GetJobProps6Response other = (GetJobProps6Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getJobProps6Result==null && other.getGetJobProps6Result()==null) || 
             (this.getJobProps6Result!=null &&
              this.getJobProps6Result.equals(other.getGetJobProps6Result())));
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
        if (getGetJobProps6Result() != null) {
            _hashCode += getGetJobProps6Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobProps6Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps6Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getJobProps6Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobProps6Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties6"));
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
