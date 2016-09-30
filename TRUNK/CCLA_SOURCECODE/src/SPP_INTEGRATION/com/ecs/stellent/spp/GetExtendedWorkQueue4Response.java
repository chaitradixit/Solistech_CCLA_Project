/**
 * GetExtendedWorkQueue4Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetExtendedWorkQueue4Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.ExtendedWorkQueue4 getExtendedWorkQueue4Result;

    public GetExtendedWorkQueue4Response() {
    }

    public GetExtendedWorkQueue4Response(
           com.ecs.stellent.spp.ExtendedWorkQueue4 getExtendedWorkQueue4Result) {
           this.getExtendedWorkQueue4Result = getExtendedWorkQueue4Result;
    }


    /**
     * Gets the getExtendedWorkQueue4Result value for this GetExtendedWorkQueue4Response.
     * 
     * @return getExtendedWorkQueue4Result
     */
    public com.ecs.stellent.spp.ExtendedWorkQueue4 getGetExtendedWorkQueue4Result() {
        return getExtendedWorkQueue4Result;
    }


    /**
     * Sets the getExtendedWorkQueue4Result value for this GetExtendedWorkQueue4Response.
     * 
     * @param getExtendedWorkQueue4Result
     */
    public void setGetExtendedWorkQueue4Result(com.ecs.stellent.spp.ExtendedWorkQueue4 getExtendedWorkQueue4Result) {
        this.getExtendedWorkQueue4Result = getExtendedWorkQueue4Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtendedWorkQueue4Response)) return false;
        GetExtendedWorkQueue4Response other = (GetExtendedWorkQueue4Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getExtendedWorkQueue4Result==null && other.getGetExtendedWorkQueue4Result()==null) || 
             (this.getExtendedWorkQueue4Result!=null &&
              this.getExtendedWorkQueue4Result.equals(other.getGetExtendedWorkQueue4Result())));
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
        if (getGetExtendedWorkQueue4Result() != null) {
            _hashCode += getGetExtendedWorkQueue4Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtendedWorkQueue4Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue4Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getExtendedWorkQueue4Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueue4Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue4"));
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
