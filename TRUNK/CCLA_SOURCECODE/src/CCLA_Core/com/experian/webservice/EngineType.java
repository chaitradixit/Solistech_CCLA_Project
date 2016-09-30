/**
 * EngineType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class EngineType  extends com.experian.webservice.EngineEnumType  implements java.io.Serializable {
    private boolean flatten;  // attribute

    private com.experian.webservice.EngineIntensityType intensity;  // attribute

    private com.experian.webservice.PromptSetType promptSet;  // attribute

    private org.apache.axis.types.PositiveInteger threshold;  // attribute

    private org.apache.axis.types.NonNegativeInteger timeout;  // attribute

    // Simple Types must have a String constructor
    public EngineType(java.lang.String _value) {
        super(_value);
    }


    /**
     * Gets the flatten value for this EngineType.
     * 
     * @return flatten
     */
    public boolean isFlatten() {
        return flatten;
    }


    /**
     * Sets the flatten value for this EngineType.
     * 
     * @param flatten
     */
    public void setFlatten(boolean flatten) {
        this.flatten = flatten;
    }


    /**
     * Gets the intensity value for this EngineType.
     * 
     * @return intensity
     */
    public com.experian.webservice.EngineIntensityType getIntensity() {
        return intensity;
    }


    /**
     * Sets the intensity value for this EngineType.
     * 
     * @param intensity
     */
    public void setIntensity(com.experian.webservice.EngineIntensityType intensity) {
        this.intensity = intensity;
    }


    /**
     * Gets the promptSet value for this EngineType.
     * 
     * @return promptSet
     */
    public com.experian.webservice.PromptSetType getPromptSet() {
        return promptSet;
    }


    /**
     * Sets the promptSet value for this EngineType.
     * 
     * @param promptSet
     */
    public void setPromptSet(com.experian.webservice.PromptSetType promptSet) {
        this.promptSet = promptSet;
    }


    /**
     * Gets the threshold value for this EngineType.
     * 
     * @return threshold
     */
    public org.apache.axis.types.PositiveInteger getThreshold() {
        return threshold;
    }


    /**
     * Sets the threshold value for this EngineType.
     * 
     * @param threshold
     */
    public void setThreshold(org.apache.axis.types.PositiveInteger threshold) {
        this.threshold = threshold;
    }


    /**
     * Gets the timeout value for this EngineType.
     * 
     * @return timeout
     */
    public org.apache.axis.types.NonNegativeInteger getTimeout() {
        return timeout;
    }


    /**
     * Sets the timeout value for this EngineType.
     * 
     * @param timeout
     */
    public void setTimeout(org.apache.axis.types.NonNegativeInteger timeout) {
        this.timeout = timeout;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EngineType)) return false;
        EngineType other = (EngineType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.flatten == other.isFlatten() &&
            ((this.intensity==null && other.getIntensity()==null) || 
             (this.intensity!=null &&
              this.intensity.equals(other.getIntensity()))) &&
            ((this.promptSet==null && other.getPromptSet()==null) || 
             (this.promptSet!=null &&
              this.promptSet.equals(other.getPromptSet()))) &&
            ((this.threshold==null && other.getThreshold()==null) || 
             (this.threshold!=null &&
              this.threshold.equals(other.getThreshold()))) &&
            ((this.timeout==null && other.getTimeout()==null) || 
             (this.timeout!=null &&
              this.timeout.equals(other.getTimeout())));
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
        _hashCode += (isFlatten() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getIntensity() != null) {
            _hashCode += getIntensity().hashCode();
        }
        if (getPromptSet() != null) {
            _hashCode += getPromptSet().hashCode();
        }
        if (getThreshold() != null) {
            _hashCode += getThreshold().hashCode();
        }
        if (getTimeout() != null) {
            _hashCode += getTimeout().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EngineType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "EngineType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("flatten");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Flatten"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("intensity");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Intensity"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "EngineIntensityType"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("promptSet");
        attrField.setXmlName(new javax.xml.namespace.QName("", "PromptSet"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PromptSetType"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("threshold");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Threshold"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "ThresholdType"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("timeout");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Timeout"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "TimeoutType"));
        typeDesc.addFieldDesc(attrField);
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
          new  org.apache.axis.encoding.ser.SimpleSerializer(
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
          new  org.apache.axis.encoding.ser.SimpleDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
