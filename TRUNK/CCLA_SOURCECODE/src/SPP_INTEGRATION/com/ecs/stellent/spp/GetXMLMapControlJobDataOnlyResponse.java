/**
 * GetXMLMapControlJobDataOnlyResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetXMLMapControlJobDataOnlyResponse  implements java.io.Serializable {
    private java.lang.String getXMLMapControlJobDataOnlyResult;

    public GetXMLMapControlJobDataOnlyResponse() {
    }

    public GetXMLMapControlJobDataOnlyResponse(
           java.lang.String getXMLMapControlJobDataOnlyResult) {
           this.getXMLMapControlJobDataOnlyResult = getXMLMapControlJobDataOnlyResult;
    }


    /**
     * Gets the getXMLMapControlJobDataOnlyResult value for this GetXMLMapControlJobDataOnlyResponse.
     * 
     * @return getXMLMapControlJobDataOnlyResult
     */
    public java.lang.String getGetXMLMapControlJobDataOnlyResult() {
        return getXMLMapControlJobDataOnlyResult;
    }


    /**
     * Sets the getXMLMapControlJobDataOnlyResult value for this GetXMLMapControlJobDataOnlyResponse.
     * 
     * @param getXMLMapControlJobDataOnlyResult
     */
    public void setGetXMLMapControlJobDataOnlyResult(java.lang.String getXMLMapControlJobDataOnlyResult) {
        this.getXMLMapControlJobDataOnlyResult = getXMLMapControlJobDataOnlyResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetXMLMapControlJobDataOnlyResponse)) return false;
        GetXMLMapControlJobDataOnlyResponse other = (GetXMLMapControlJobDataOnlyResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getXMLMapControlJobDataOnlyResult==null && other.getGetXMLMapControlJobDataOnlyResult()==null) || 
             (this.getXMLMapControlJobDataOnlyResult!=null &&
              this.getXMLMapControlJobDataOnlyResult.equals(other.getGetXMLMapControlJobDataOnlyResult())));
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
        if (getGetXMLMapControlJobDataOnlyResult() != null) {
            _hashCode += getGetXMLMapControlJobDataOnlyResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetXMLMapControlJobDataOnlyResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetXMLMapControlJobDataOnlyResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getXMLMapControlJobDataOnlyResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetXMLMapControlJobDataOnlyResult"));
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
