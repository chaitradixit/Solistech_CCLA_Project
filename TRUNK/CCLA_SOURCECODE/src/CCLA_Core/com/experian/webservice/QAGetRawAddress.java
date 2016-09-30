/**
 * QAGetRawAddress.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class QAGetRawAddress  implements java.io.Serializable {
    private java.lang.String moniker;

    public QAGetRawAddress() {
    }

    public QAGetRawAddress(
           java.lang.String moniker) {
           this.moniker = moniker;
    }


    /**
     * Gets the moniker value for this QAGetRawAddress.
     * 
     * @return moniker
     */
    public java.lang.String getMoniker() {
        return moniker;
    }


    /**
     * Sets the moniker value for this QAGetRawAddress.
     * 
     * @param moniker
     */
    public void setMoniker(java.lang.String moniker) {
        this.moniker = moniker;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QAGetRawAddress)) return false;
        QAGetRawAddress other = (QAGetRawAddress) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.moniker==null && other.getMoniker()==null) || 
             (this.moniker!=null &&
              this.moniker.equals(other.getMoniker())));
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
        if (getMoniker() != null) {
            _hashCode += getMoniker().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QAGetRawAddress.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetRawAddress"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("moniker");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Moniker"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
