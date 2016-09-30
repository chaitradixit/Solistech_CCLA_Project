/**
 * GetExtendedWorkQueue2Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetExtendedWorkQueue2Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.ExtendedWorkQueue2 getExtendedWorkQueue2Result;

    public GetExtendedWorkQueue2Response() {
    }

    public GetExtendedWorkQueue2Response(
           com.ecs.stellent.spp.ExtendedWorkQueue2 getExtendedWorkQueue2Result) {
           this.getExtendedWorkQueue2Result = getExtendedWorkQueue2Result;
    }


    /**
     * Gets the getExtendedWorkQueue2Result value for this GetExtendedWorkQueue2Response.
     * 
     * @return getExtendedWorkQueue2Result
     */
    public com.ecs.stellent.spp.ExtendedWorkQueue2 getGetExtendedWorkQueue2Result() {
        return getExtendedWorkQueue2Result;
    }


    /**
     * Sets the getExtendedWorkQueue2Result value for this GetExtendedWorkQueue2Response.
     * 
     * @param getExtendedWorkQueue2Result
     */
    public void setGetExtendedWorkQueue2Result(com.ecs.stellent.spp.ExtendedWorkQueue2 getExtendedWorkQueue2Result) {
        this.getExtendedWorkQueue2Result = getExtendedWorkQueue2Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtendedWorkQueue2Response)) return false;
        GetExtendedWorkQueue2Response other = (GetExtendedWorkQueue2Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getExtendedWorkQueue2Result==null && other.getGetExtendedWorkQueue2Result()==null) || 
             (this.getExtendedWorkQueue2Result!=null &&
              this.getExtendedWorkQueue2Result.equals(other.getGetExtendedWorkQueue2Result())));
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
        if (getGetExtendedWorkQueue2Result() != null) {
            _hashCode += getGetExtendedWorkQueue2Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtendedWorkQueue2Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue2Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getExtendedWorkQueue2Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueue2Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue2"));
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
