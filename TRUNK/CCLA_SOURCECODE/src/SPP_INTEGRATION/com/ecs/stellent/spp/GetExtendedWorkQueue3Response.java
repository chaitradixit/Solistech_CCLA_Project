/**
 * GetExtendedWorkQueue3Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetExtendedWorkQueue3Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.ExtendedWorkQueue3 getExtendedWorkQueue3Result;

    public GetExtendedWorkQueue3Response() {
    }

    public GetExtendedWorkQueue3Response(
           com.ecs.stellent.spp.ExtendedWorkQueue3 getExtendedWorkQueue3Result) {
           this.getExtendedWorkQueue3Result = getExtendedWorkQueue3Result;
    }


    /**
     * Gets the getExtendedWorkQueue3Result value for this GetExtendedWorkQueue3Response.
     * 
     * @return getExtendedWorkQueue3Result
     */
    public com.ecs.stellent.spp.ExtendedWorkQueue3 getGetExtendedWorkQueue3Result() {
        return getExtendedWorkQueue3Result;
    }


    /**
     * Sets the getExtendedWorkQueue3Result value for this GetExtendedWorkQueue3Response.
     * 
     * @param getExtendedWorkQueue3Result
     */
    public void setGetExtendedWorkQueue3Result(com.ecs.stellent.spp.ExtendedWorkQueue3 getExtendedWorkQueue3Result) {
        this.getExtendedWorkQueue3Result = getExtendedWorkQueue3Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtendedWorkQueue3Response)) return false;
        GetExtendedWorkQueue3Response other = (GetExtendedWorkQueue3Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getExtendedWorkQueue3Result==null && other.getGetExtendedWorkQueue3Result()==null) || 
             (this.getExtendedWorkQueue3Result!=null &&
              this.getExtendedWorkQueue3Result.equals(other.getGetExtendedWorkQueue3Result())));
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
        if (getGetExtendedWorkQueue3Result() != null) {
            _hashCode += getGetExtendedWorkQueue3Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtendedWorkQueue3Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue3Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getExtendedWorkQueue3Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueue3Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue3"));
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
