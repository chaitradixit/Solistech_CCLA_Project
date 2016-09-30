/**
 * QAGetExampleAddresses.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class QAGetExampleAddresses  implements java.io.Serializable {
    private java.lang.String country;

    private java.lang.String layout;

    private com.experian.webservice.QAConfigType QAConfig;

    public QAGetExampleAddresses() {
    }

    public QAGetExampleAddresses(
           java.lang.String country,
           java.lang.String layout,
           com.experian.webservice.QAConfigType QAConfig) {
           this.country = country;
           this.layout = layout;
           this.QAConfig = QAConfig;
    }


    /**
     * Gets the country value for this QAGetExampleAddresses.
     * 
     * @return country
     */
    public java.lang.String getCountry() {
        return country;
    }


    /**
     * Sets the country value for this QAGetExampleAddresses.
     * 
     * @param country
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }


    /**
     * Gets the layout value for this QAGetExampleAddresses.
     * 
     * @return layout
     */
    public java.lang.String getLayout() {
        return layout;
    }


    /**
     * Sets the layout value for this QAGetExampleAddresses.
     * 
     * @param layout
     */
    public void setLayout(java.lang.String layout) {
        this.layout = layout;
    }


    /**
     * Gets the QAConfig value for this QAGetExampleAddresses.
     * 
     * @return QAConfig
     */
    public com.experian.webservice.QAConfigType getQAConfig() {
        return QAConfig;
    }


    /**
     * Sets the QAConfig value for this QAGetExampleAddresses.
     * 
     * @param QAConfig
     */
    public void setQAConfig(com.experian.webservice.QAConfigType QAConfig) {
        this.QAConfig = QAConfig;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QAGetExampleAddresses)) return false;
        QAGetExampleAddresses other = (QAGetExampleAddresses) obj;
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
            ((this.layout==null && other.getLayout()==null) || 
             (this.layout!=null &&
              this.layout.equals(other.getLayout()))) &&
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
        if (getLayout() != null) {
            _hashCode += getLayout().hashCode();
        }
        if (getQAConfig() != null) {
            _hashCode += getQAConfig().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QAGetExampleAddresses.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetExampleAddresses"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("country");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Country"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("layout");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Layout"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
