/**
 * MicronumberResults.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.validator.webservice;

public class MicronumberResults  implements java.io.Serializable {
    private boolean result;

    private boolean micronumberTest;

    private boolean historicMailsortTest;

    public MicronumberResults() {
    }

    public MicronumberResults(
           boolean result,
           boolean micronumberTest,
           boolean historicMailsortTest) {
           this.result = result;
           this.micronumberTest = micronumberTest;
           this.historicMailsortTest = historicMailsortTest;
    }


    /**
     * Gets the result value for this MicronumberResults.
     * 
     * @return result
     */
    public boolean isResult() {
        return result;
    }


    /**
     * Sets the result value for this MicronumberResults.
     * 
     * @param result
     */
    public void setResult(boolean result) {
        this.result = result;
    }


    /**
     * Gets the micronumberTest value for this MicronumberResults.
     * 
     * @return micronumberTest
     */
    public boolean isMicronumberTest() {
        return micronumberTest;
    }


    /**
     * Sets the micronumberTest value for this MicronumberResults.
     * 
     * @param micronumberTest
     */
    public void setMicronumberTest(boolean micronumberTest) {
        this.micronumberTest = micronumberTest;
    }


    /**
     * Gets the historicMailsortTest value for this MicronumberResults.
     * 
     * @return historicMailsortTest
     */
    public boolean isHistoricMailsortTest() {
        return historicMailsortTest;
    }


    /**
     * Sets the historicMailsortTest value for this MicronumberResults.
     * 
     * @param historicMailsortTest
     */
    public void setHistoricMailsortTest(boolean historicMailsortTest) {
        this.historicMailsortTest = historicMailsortTest;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MicronumberResults)) return false;
        MicronumberResults other = (MicronumberResults) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.result == other.isResult() &&
            this.micronumberTest == other.isMicronumberTest() &&
            this.historicMailsortTest == other.isHistoricMailsortTest();
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
        _hashCode += (isResult() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMicronumberTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isHistoricMailsortTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MicronumberResults.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.co.uk/", "MicronumberResults"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("micronumberTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "micronumberTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("historicMailsortTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "historicMailsortTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
