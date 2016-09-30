/**
 * PassportNumberResults.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.validator.webservice;

public class PassportNumberResults  implements java.io.Serializable {
    private boolean result;

    private boolean documentNumberDigitTest;

    private boolean dobDigitTest;

    private boolean expiryDateDigitTest;

    private boolean dobTest;

    private boolean genderTest;

    private boolean isoTest;

    private boolean personalNumberDigitTest;

    private boolean finalDigitTest;

    public PassportNumberResults() {
    }

    public PassportNumberResults(
           boolean result,
           boolean documentNumberDigitTest,
           boolean dobDigitTest,
           boolean expiryDateDigitTest,
           boolean dobTest,
           boolean genderTest,
           boolean isoTest,
           boolean personalNumberDigitTest,
           boolean finalDigitTest) {
           this.result = result;
           this.documentNumberDigitTest = documentNumberDigitTest;
           this.dobDigitTest = dobDigitTest;
           this.expiryDateDigitTest = expiryDateDigitTest;
           this.dobTest = dobTest;
           this.genderTest = genderTest;
           this.isoTest = isoTest;
           this.personalNumberDigitTest = personalNumberDigitTest;
           this.finalDigitTest = finalDigitTest;
    }


    /**
     * Gets the result value for this PassportNumberResults.
     * 
     * @return result
     */
    public boolean isResult() {
        return result;
    }


    /**
     * Sets the result value for this PassportNumberResults.
     * 
     * @param result
     */
    public void setResult(boolean result) {
        this.result = result;
    }


    /**
     * Gets the documentNumberDigitTest value for this PassportNumberResults.
     * 
     * @return documentNumberDigitTest
     */
    public boolean isDocumentNumberDigitTest() {
        return documentNumberDigitTest;
    }


    /**
     * Sets the documentNumberDigitTest value for this PassportNumberResults.
     * 
     * @param documentNumberDigitTest
     */
    public void setDocumentNumberDigitTest(boolean documentNumberDigitTest) {
        this.documentNumberDigitTest = documentNumberDigitTest;
    }


    /**
     * Gets the dobDigitTest value for this PassportNumberResults.
     * 
     * @return dobDigitTest
     */
    public boolean isDobDigitTest() {
        return dobDigitTest;
    }


    /**
     * Sets the dobDigitTest value for this PassportNumberResults.
     * 
     * @param dobDigitTest
     */
    public void setDobDigitTest(boolean dobDigitTest) {
        this.dobDigitTest = dobDigitTest;
    }


    /**
     * Gets the expiryDateDigitTest value for this PassportNumberResults.
     * 
     * @return expiryDateDigitTest
     */
    public boolean isExpiryDateDigitTest() {
        return expiryDateDigitTest;
    }


    /**
     * Sets the expiryDateDigitTest value for this PassportNumberResults.
     * 
     * @param expiryDateDigitTest
     */
    public void setExpiryDateDigitTest(boolean expiryDateDigitTest) {
        this.expiryDateDigitTest = expiryDateDigitTest;
    }


    /**
     * Gets the dobTest value for this PassportNumberResults.
     * 
     * @return dobTest
     */
    public boolean isDobTest() {
        return dobTest;
    }


    /**
     * Sets the dobTest value for this PassportNumberResults.
     * 
     * @param dobTest
     */
    public void setDobTest(boolean dobTest) {
        this.dobTest = dobTest;
    }


    /**
     * Gets the genderTest value for this PassportNumberResults.
     * 
     * @return genderTest
     */
    public boolean isGenderTest() {
        return genderTest;
    }


    /**
     * Sets the genderTest value for this PassportNumberResults.
     * 
     * @param genderTest
     */
    public void setGenderTest(boolean genderTest) {
        this.genderTest = genderTest;
    }


    /**
     * Gets the isoTest value for this PassportNumberResults.
     * 
     * @return isoTest
     */
    public boolean isIsoTest() {
        return isoTest;
    }


    /**
     * Sets the isoTest value for this PassportNumberResults.
     * 
     * @param isoTest
     */
    public void setIsoTest(boolean isoTest) {
        this.isoTest = isoTest;
    }


    /**
     * Gets the personalNumberDigitTest value for this PassportNumberResults.
     * 
     * @return personalNumberDigitTest
     */
    public boolean isPersonalNumberDigitTest() {
        return personalNumberDigitTest;
    }


    /**
     * Sets the personalNumberDigitTest value for this PassportNumberResults.
     * 
     * @param personalNumberDigitTest
     */
    public void setPersonalNumberDigitTest(boolean personalNumberDigitTest) {
        this.personalNumberDigitTest = personalNumberDigitTest;
    }


    /**
     * Gets the finalDigitTest value for this PassportNumberResults.
     * 
     * @return finalDigitTest
     */
    public boolean isFinalDigitTest() {
        return finalDigitTest;
    }


    /**
     * Sets the finalDigitTest value for this PassportNumberResults.
     * 
     * @param finalDigitTest
     */
    public void setFinalDigitTest(boolean finalDigitTest) {
        this.finalDigitTest = finalDigitTest;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PassportNumberResults)) return false;
        PassportNumberResults other = (PassportNumberResults) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.result == other.isResult() &&
            this.documentNumberDigitTest == other.isDocumentNumberDigitTest() &&
            this.dobDigitTest == other.isDobDigitTest() &&
            this.expiryDateDigitTest == other.isExpiryDateDigitTest() &&
            this.dobTest == other.isDobTest() &&
            this.genderTest == other.isGenderTest() &&
            this.isoTest == other.isIsoTest() &&
            this.personalNumberDigitTest == other.isPersonalNumberDigitTest() &&
            this.finalDigitTest == other.isFinalDigitTest();
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
        _hashCode += (isDocumentNumberDigitTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDobDigitTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isExpiryDateDigitTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDobTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isGenderTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isIsoTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPersonalNumberDigitTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isFinalDigitTest() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PassportNumberResults.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.co.uk/", "PassportNumberResults"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documentNumberDigitTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "documentNumberDigitTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dobDigitTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "dobDigitTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expiryDateDigitTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "expiryDateDigitTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dobTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "dobTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("genderTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "genderTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isoTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "isoTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("personalNumberDigitTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "personalNumberDigitTest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("finalDigitTest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.co.uk/", "finalDigitTest"));
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
