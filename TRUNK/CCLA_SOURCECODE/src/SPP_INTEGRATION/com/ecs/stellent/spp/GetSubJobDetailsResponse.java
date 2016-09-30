/**
 * GetSubJobDetailsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetSubJobDetailsResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.SubJobDetails getSubJobDetailsResult;

    public GetSubJobDetailsResponse() {
    }

    public GetSubJobDetailsResponse(
           com.ecs.stellent.spp.SubJobDetails getSubJobDetailsResult) {
           this.getSubJobDetailsResult = getSubJobDetailsResult;
    }


    /**
     * Gets the getSubJobDetailsResult value for this GetSubJobDetailsResponse.
     * 
     * @return getSubJobDetailsResult
     */
    public com.ecs.stellent.spp.SubJobDetails getGetSubJobDetailsResult() {
        return getSubJobDetailsResult;
    }


    /**
     * Sets the getSubJobDetailsResult value for this GetSubJobDetailsResponse.
     * 
     * @param getSubJobDetailsResult
     */
    public void setGetSubJobDetailsResult(com.ecs.stellent.spp.SubJobDetails getSubJobDetailsResult) {
        this.getSubJobDetailsResult = getSubJobDetailsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSubJobDetailsResponse)) return false;
        GetSubJobDetailsResponse other = (GetSubJobDetailsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getSubJobDetailsResult==null && other.getGetSubJobDetailsResult()==null) || 
             (this.getSubJobDetailsResult!=null &&
              this.getSubJobDetailsResult.equals(other.getGetSubJobDetailsResult())));
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
        if (getGetSubJobDetailsResult() != null) {
            _hashCode += getGetSubJobDetailsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSubJobDetailsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetSubJobDetailsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getSubJobDetailsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetSubJobDetailsResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobDetails"));
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
