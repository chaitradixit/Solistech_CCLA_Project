/**
 * GetEmbeddedXMLMapControlDataResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetEmbeddedXMLMapControlDataResponse  implements java.io.Serializable {
    private java.lang.String getEmbeddedXMLMapControlDataResult;

    public GetEmbeddedXMLMapControlDataResponse() {
    }

    public GetEmbeddedXMLMapControlDataResponse(
           java.lang.String getEmbeddedXMLMapControlDataResult) {
           this.getEmbeddedXMLMapControlDataResult = getEmbeddedXMLMapControlDataResult;
    }


    /**
     * Gets the getEmbeddedXMLMapControlDataResult value for this GetEmbeddedXMLMapControlDataResponse.
     * 
     * @return getEmbeddedXMLMapControlDataResult
     */
    public java.lang.String getGetEmbeddedXMLMapControlDataResult() {
        return getEmbeddedXMLMapControlDataResult;
    }


    /**
     * Sets the getEmbeddedXMLMapControlDataResult value for this GetEmbeddedXMLMapControlDataResponse.
     * 
     * @param getEmbeddedXMLMapControlDataResult
     */
    public void setGetEmbeddedXMLMapControlDataResult(java.lang.String getEmbeddedXMLMapControlDataResult) {
        this.getEmbeddedXMLMapControlDataResult = getEmbeddedXMLMapControlDataResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetEmbeddedXMLMapControlDataResponse)) return false;
        GetEmbeddedXMLMapControlDataResponse other = (GetEmbeddedXMLMapControlDataResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getEmbeddedXMLMapControlDataResult==null && other.getGetEmbeddedXMLMapControlDataResult()==null) || 
             (this.getEmbeddedXMLMapControlDataResult!=null &&
              this.getEmbeddedXMLMapControlDataResult.equals(other.getGetEmbeddedXMLMapControlDataResult())));
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
        if (getGetEmbeddedXMLMapControlDataResult() != null) {
            _hashCode += getGetEmbeddedXMLMapControlDataResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetEmbeddedXMLMapControlDataResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetEmbeddedXMLMapControlDataResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getEmbeddedXMLMapControlDataResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetEmbeddedXMLMapControlDataResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
