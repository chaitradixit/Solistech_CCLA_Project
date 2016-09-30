/**
 * GetMinJobDetailsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetMinJobDetailsResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.MinJobDetails getMinJobDetailsResult;

    public GetMinJobDetailsResponse() {
    }

    public GetMinJobDetailsResponse(
           com.ecs.stellent.spp.MinJobDetails getMinJobDetailsResult) {
           this.getMinJobDetailsResult = getMinJobDetailsResult;
    }


    /**
     * Gets the getMinJobDetailsResult value for this GetMinJobDetailsResponse.
     * 
     * @return getMinJobDetailsResult
     */
    public com.ecs.stellent.spp.MinJobDetails getGetMinJobDetailsResult() {
        return getMinJobDetailsResult;
    }


    /**
     * Sets the getMinJobDetailsResult value for this GetMinJobDetailsResponse.
     * 
     * @param getMinJobDetailsResult
     */
    public void setGetMinJobDetailsResult(com.ecs.stellent.spp.MinJobDetails getMinJobDetailsResult) {
        this.getMinJobDetailsResult = getMinJobDetailsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetMinJobDetailsResponse)) return false;
        GetMinJobDetailsResponse other = (GetMinJobDetailsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getMinJobDetailsResult==null && other.getGetMinJobDetailsResult()==null) || 
             (this.getMinJobDetailsResult!=null &&
              this.getMinJobDetailsResult.equals(other.getGetMinJobDetailsResult())));
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
        if (getGetMinJobDetailsResult() != null) {
            _hashCode += getGetMinJobDetailsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetMinJobDetailsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMinJobDetailsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getMinJobDetailsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMinJobDetailsResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MinJobDetails"));
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
