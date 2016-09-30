/**
 * GetNumberOfActivitiesUsingCriteriaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetNumberOfActivitiesUsingCriteriaResponse  implements java.io.Serializable {
    private int getNumberOfActivitiesUsingCriteriaResult;

    public GetNumberOfActivitiesUsingCriteriaResponse() {
    }

    public GetNumberOfActivitiesUsingCriteriaResponse(
           int getNumberOfActivitiesUsingCriteriaResult) {
           this.getNumberOfActivitiesUsingCriteriaResult = getNumberOfActivitiesUsingCriteriaResult;
    }


    /**
     * Gets the getNumberOfActivitiesUsingCriteriaResult value for this GetNumberOfActivitiesUsingCriteriaResponse.
     * 
     * @return getNumberOfActivitiesUsingCriteriaResult
     */
    public int getGetNumberOfActivitiesUsingCriteriaResult() {
        return getNumberOfActivitiesUsingCriteriaResult;
    }


    /**
     * Sets the getNumberOfActivitiesUsingCriteriaResult value for this GetNumberOfActivitiesUsingCriteriaResponse.
     * 
     * @param getNumberOfActivitiesUsingCriteriaResult
     */
    public void setGetNumberOfActivitiesUsingCriteriaResult(int getNumberOfActivitiesUsingCriteriaResult) {
        this.getNumberOfActivitiesUsingCriteriaResult = getNumberOfActivitiesUsingCriteriaResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetNumberOfActivitiesUsingCriteriaResponse)) return false;
        GetNumberOfActivitiesUsingCriteriaResponse other = (GetNumberOfActivitiesUsingCriteriaResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.getNumberOfActivitiesUsingCriteriaResult == other.getGetNumberOfActivitiesUsingCriteriaResult();
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
        _hashCode += getGetNumberOfActivitiesUsingCriteriaResult();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetNumberOfActivitiesUsingCriteriaResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNumberOfActivitiesUsingCriteriaResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getNumberOfActivitiesUsingCriteriaResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetNumberOfActivitiesUsingCriteriaResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
