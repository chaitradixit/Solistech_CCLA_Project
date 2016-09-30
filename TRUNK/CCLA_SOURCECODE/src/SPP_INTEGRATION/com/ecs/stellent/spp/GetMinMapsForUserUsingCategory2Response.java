/**
 * GetMinMapsForUserUsingCategory2Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetMinMapsForUserUsingCategory2Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.ProcessMapData[] getMinMapsForUserUsingCategory2Result;

    public GetMinMapsForUserUsingCategory2Response() {
    }

    public GetMinMapsForUserUsingCategory2Response(
           com.ecs.stellent.spp.ProcessMapData[] getMinMapsForUserUsingCategory2Result) {
           this.getMinMapsForUserUsingCategory2Result = getMinMapsForUserUsingCategory2Result;
    }


    /**
     * Gets the getMinMapsForUserUsingCategory2Result value for this GetMinMapsForUserUsingCategory2Response.
     * 
     * @return getMinMapsForUserUsingCategory2Result
     */
    public com.ecs.stellent.spp.ProcessMapData[] getGetMinMapsForUserUsingCategory2Result() {
        return getMinMapsForUserUsingCategory2Result;
    }


    /**
     * Sets the getMinMapsForUserUsingCategory2Result value for this GetMinMapsForUserUsingCategory2Response.
     * 
     * @param getMinMapsForUserUsingCategory2Result
     */
    public void setGetMinMapsForUserUsingCategory2Result(com.ecs.stellent.spp.ProcessMapData[] getMinMapsForUserUsingCategory2Result) {
        this.getMinMapsForUserUsingCategory2Result = getMinMapsForUserUsingCategory2Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetMinMapsForUserUsingCategory2Response)) return false;
        GetMinMapsForUserUsingCategory2Response other = (GetMinMapsForUserUsingCategory2Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getMinMapsForUserUsingCategory2Result==null && other.getGetMinMapsForUserUsingCategory2Result()==null) || 
             (this.getMinMapsForUserUsingCategory2Result!=null &&
              java.util.Arrays.equals(this.getMinMapsForUserUsingCategory2Result, other.getGetMinMapsForUserUsingCategory2Result())));
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
        if (getGetMinMapsForUserUsingCategory2Result() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetMinMapsForUserUsingCategory2Result());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetMinMapsForUserUsingCategory2Result(), i);
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
        new org.apache.axis.description.TypeDesc(GetMinMapsForUserUsingCategory2Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMinMapsForUserUsingCategory2Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getMinMapsForUserUsingCategory2Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMinMapsForUserUsingCategory2Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapData"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapData"));
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
