/**
 * QAGetPromptSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class QAGetPromptSet  implements java.io.Serializable {
    private java.lang.String country;

    private com.experian.webservice.EngineType engine;

    private com.experian.webservice.PromptSetType promptSet;

    private com.experian.webservice.QAConfigType QAConfig;

    public QAGetPromptSet() {
    }

    public QAGetPromptSet(
           java.lang.String country,
           com.experian.webservice.EngineType engine,
           com.experian.webservice.PromptSetType promptSet,
           com.experian.webservice.QAConfigType QAConfig) {
           this.country = country;
           this.engine = engine;
           this.promptSet = promptSet;
           this.QAConfig = QAConfig;
    }


    /**
     * Gets the country value for this QAGetPromptSet.
     * 
     * @return country
     */
    public java.lang.String getCountry() {
        return country;
    }


    /**
     * Sets the country value for this QAGetPromptSet.
     * 
     * @param country
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }


    /**
     * Gets the engine value for this QAGetPromptSet.
     * 
     * @return engine
     */
    public com.experian.webservice.EngineType getEngine() {
        return engine;
    }


    /**
     * Sets the engine value for this QAGetPromptSet.
     * 
     * @param engine
     */
    public void setEngine(com.experian.webservice.EngineType engine) {
        this.engine = engine;
    }


    /**
     * Gets the promptSet value for this QAGetPromptSet.
     * 
     * @return promptSet
     */
    public com.experian.webservice.PromptSetType getPromptSet() {
        return promptSet;
    }


    /**
     * Sets the promptSet value for this QAGetPromptSet.
     * 
     * @param promptSet
     */
    public void setPromptSet(com.experian.webservice.PromptSetType promptSet) {
        this.promptSet = promptSet;
    }


    /**
     * Gets the QAConfig value for this QAGetPromptSet.
     * 
     * @return QAConfig
     */
    public com.experian.webservice.QAConfigType getQAConfig() {
        return QAConfig;
    }


    /**
     * Sets the QAConfig value for this QAGetPromptSet.
     * 
     * @param QAConfig
     */
    public void setQAConfig(com.experian.webservice.QAConfigType QAConfig) {
        this.QAConfig = QAConfig;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QAGetPromptSet)) return false;
        QAGetPromptSet other = (QAGetPromptSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.country==null && other.getCountry()==null) || 
             (this.country!=null &&
              this.country.equals(other.getCountry()))) &&
            ((this.engine==null && other.getEngine()==null) || 
             (this.engine!=null &&
              this.engine.equals(other.getEngine()))) &&
            ((this.promptSet==null && other.getPromptSet()==null) || 
             (this.promptSet!=null &&
              this.promptSet.equals(other.getPromptSet()))) &&
            ((this.QAConfig==null && other.getQAConfig()==null) || 
             (this.QAConfig!=null &&
              this.QAConfig.equals(other.getQAConfig())));
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
        if (getCountry() != null) {
            _hashCode += getCountry().hashCode();
        }
        if (getEngine() != null) {
            _hashCode += getEngine().hashCode();
        }
        if (getPromptSet() != null) {
            _hashCode += getPromptSet().hashCode();
        }
        if (getQAConfig() != null) {
            _hashCode += getQAConfig().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QAGetPromptSet.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetPromptSet"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("country");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Country"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("engine");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Engine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "EngineType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("promptSet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PromptSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PromptSetType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("QAConfig");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAConfig"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAConfigType"));
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
