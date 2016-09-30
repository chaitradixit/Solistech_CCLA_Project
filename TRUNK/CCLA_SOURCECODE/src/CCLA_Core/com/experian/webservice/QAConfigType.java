/**
 * QAConfigType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class QAConfigType  implements java.io.Serializable {
    private java.lang.String iniFile;

    private java.lang.String iniSection;

    public QAConfigType() {
    }

    public QAConfigType(
           java.lang.String iniFile,
           java.lang.String iniSection) {
           this.iniFile = iniFile;
           this.iniSection = iniSection;
    }


    /**
     * Gets the iniFile value for this QAConfigType.
     * 
     * @return iniFile
     */
    public java.lang.String getIniFile() {
        return iniFile;
    }


    /**
     * Sets the iniFile value for this QAConfigType.
     * 
     * @param iniFile
     */
    public void setIniFile(java.lang.String iniFile) {
        this.iniFile = iniFile;
    }


    /**
     * Gets the iniSection value for this QAConfigType.
     * 
     * @return iniSection
     */
    public java.lang.String getIniSection() {
        return iniSection;
    }


    /**
     * Sets the iniSection value for this QAConfigType.
     * 
     * @param iniSection
     */
    public void setIniSection(java.lang.String iniSection) {
        this.iniSection = iniSection;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QAConfigType)) return false;
        QAConfigType other = (QAConfigType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.iniFile==null && other.getIniFile()==null) || 
             (this.iniFile!=null &&
              this.iniFile.equals(other.getIniFile()))) &&
            ((this.iniSection==null && other.getIniSection()==null) || 
             (this.iniSection!=null &&
              this.iniSection.equals(other.getIniSection())));
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
        if (getIniFile() != null) {
            _hashCode += getIniFile().hashCode();
        }
        if (getIniSection() != null) {
            _hashCode += getIniSection().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QAConfigType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAConfigType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("iniFile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "IniFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("iniSection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "IniSection"));
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
