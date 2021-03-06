/**
 * GetAllProcessMapStatesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetAllProcessMapStatesResponse  implements java.io.Serializable {
    private java.lang.String[] getAllProcessMapStatesResult;

    public GetAllProcessMapStatesResponse() {
    }

    public GetAllProcessMapStatesResponse(
           java.lang.String[] getAllProcessMapStatesResult) {
           this.getAllProcessMapStatesResult = getAllProcessMapStatesResult;
    }


    /**
     * Gets the getAllProcessMapStatesResult value for this GetAllProcessMapStatesResponse.
     * 
     * @return getAllProcessMapStatesResult
     */
    public java.lang.String[] getGetAllProcessMapStatesResult() {
        return getAllProcessMapStatesResult;
    }


    /**
     * Sets the getAllProcessMapStatesResult value for this GetAllProcessMapStatesResponse.
     * 
     * @param getAllProcessMapStatesResult
     */
    public void setGetAllProcessMapStatesResult(java.lang.String[] getAllProcessMapStatesResult) {
        this.getAllProcessMapStatesResult = getAllProcessMapStatesResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAllProcessMapStatesResponse)) return false;
        GetAllProcessMapStatesResponse other = (GetAllProcessMapStatesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getAllProcessMapStatesResult==null && other.getGetAllProcessMapStatesResult()==null) || 
             (this.getAllProcessMapStatesResult!=null &&
              java.util.Arrays.equals(this.getAllProcessMapStatesResult, other.getGetAllProcessMapStatesResult())));
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
        if (getGetAllProcessMapStatesResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetAllProcessMapStatesResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetAllProcessMapStatesResult(), i);
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
        new org.apache.axis.description.TypeDesc(GetAllProcessMapStatesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetAllProcessMapStatesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getAllProcessMapStatesResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetAllProcessMapStatesResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
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
