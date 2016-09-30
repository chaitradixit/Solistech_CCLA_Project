/**
 * MailsortResults.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.validator.webservice;

public class MailsortResults  implements java.io.Serializable {
    private boolean result;

    private boolean mailsortRangeTest;

    private boolean fullMatchTest;

    public MailsortResults() {
    }

    public MailsortResults(
           boolean result,
           boolean mailsortRangeTest,
           boolean fullMatchTest) {
           this.result = result;
           this.mailsortRangeTest = mailsortRangeTest;
           this.fullMatchTest = fullMatchTest;
    }


    /**
     * Gets the result value for this MailsortResults.
     * 
     * @return result
     */
    public boolean isResult() {
        return result;
    }


    /**
     * Sets the result value for this MailsortResults.
     * 
     * @param result
     */
    public void setResult(boolean result) {
        this.result = result;
    }


    /**
     * Gets the mailsortRangeTest value for this MailsortResults.
     * 
     * @return mailsortRangeTest
     */
    public boolean isMailsortRangeTest() {
        return mailsortRangeTest;
    }


    /**
     * Sets the mailsortRangeTest value for this MailsortResults.
     * 
     * @param mailsortRangeTest
     */
    public void setMailsortRangeTest(boolean mailsortRangeTest) {
        this.mailsortRangeTest = mailsortRangeTest;
    }


    /**
     * Gets the fullMatchTest value for this MailsortResults.
     * 
     * @return fullMatchTest
     */
    public boolean isFullMatchTest() {
        return fullMatchTest;
    }


    /**
     * Sets the fullMatchTest value for this MailsortResults.
     * 
     * @param fullMatchTest
     */
    public void setFullMatchTest(boolean fullMatchTest) {
        this.fullMatchTest = fullMatchTest;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MailsortResults)) return false;
        MailsortResults other = (MailsortResults) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.result == other.isResult() &&
            this.mailsortRangeTest == other.isMailsortRangeTest() &&
            this.fullMatchTest == other.isFullMatchTest();
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
        _hashCode += (isMailsortRangeTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isFullMatchTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MailsortResults.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.co.uk/", "MailsortResults"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mailsortRangeTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "mailsortRangeTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fullMatchTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "fullMatchTest"));
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
