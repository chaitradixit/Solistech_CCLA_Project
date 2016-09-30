/**
 * ArrayOfCorrespondent.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class ArrayOfCorrespondent  implements java.io.Serializable {
    private java.lang.String dummy;

    private com.aurora.webservice.Correspondent[] correspondent;

    public ArrayOfCorrespondent() {
    }

    public ArrayOfCorrespondent(
           java.lang.String dummy,
           com.aurora.webservice.Correspondent[] correspondent) {
           this.dummy = dummy;
           this.correspondent = correspondent;
    }


    /**
     * Gets the dummy value for this ArrayOfCorrespondent.
     * 
     * @return dummy
     */
    public java.lang.String getDummy() {
        return dummy;
    }


    /**
     * Sets the dummy value for this ArrayOfCorrespondent.
     * 
     * @param dummy
     */
    public void setDummy(java.lang.String dummy) {
        this.dummy = dummy;
    }


    /**
     * Gets the correspondent value for this ArrayOfCorrespondent.
     * 
     * @return correspondent
     */
    public com.aurora.webservice.Correspondent[] getCorrespondent() {
        return correspondent;
    }


    /**
     * Sets the correspondent value for this ArrayOfCorrespondent.
     * 
     * @param correspondent
     */
    public void setCorrespondent(com.aurora.webservice.Correspondent[] correspondent) {
        this.correspondent = correspondent;
    }

    public com.aurora.webservice.Correspondent getCorrespondent(int i) {
        return this.correspondent[i];
    }

    public void setCorrespondent(int i, com.aurora.webservice.Correspondent _value) {
        this.correspondent[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfCorrespondent)) return false;
        ArrayOfCorrespondent other = (ArrayOfCorrespondent) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dummy==null && other.getDummy()==null) || 
             (this.dummy!=null &&
              this.dummy.equals(other.getDummy()))) &&
            ((this.correspondent==null && other.getCorrespondent()==null) || 
             (this.correspondent!=null &&
              java.util.Arrays.equals(this.correspondent, other.getCorrespondent())));
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
        if (getDummy() != null) {
            _hashCode += getDummy().hashCode();
        }
        if (getCorrespondent() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCorrespondent());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCorrespondent(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArrayOfCorrespondent.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ArrayOfCorrespondent"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dummy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "dummy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correspondent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Correspondent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Correspondent"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
