/**
 * ArrayOfUnitPrice.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class ArrayOfUnitPrice  implements java.io.Serializable {
    private java.lang.String dummy;

    private com.aurora.webservice.UnitPrice[] unitPrice;

    public ArrayOfUnitPrice() {
    }

    public ArrayOfUnitPrice(
           java.lang.String dummy,
           com.aurora.webservice.UnitPrice[] unitPrice) {
           this.dummy = dummy;
           this.unitPrice = unitPrice;
    }


    /**
     * Gets the dummy value for this ArrayOfUnitPrice.
     * 
     * @return dummy
     */
    public java.lang.String getDummy() {
        return dummy;
    }


    /**
     * Sets the dummy value for this ArrayOfUnitPrice.
     * 
     * @param dummy
     */
    public void setDummy(java.lang.String dummy) {
        this.dummy = dummy;
    }


    /**
     * Gets the unitPrice value for this ArrayOfUnitPrice.
     * 
     * @return unitPrice
     */
    public com.aurora.webservice.UnitPrice[] getUnitPrice() {
        return unitPrice;
    }


    /**
     * Sets the unitPrice value for this ArrayOfUnitPrice.
     * 
     * @param unitPrice
     */
    public void setUnitPrice(com.aurora.webservice.UnitPrice[] unitPrice) {
        this.unitPrice = unitPrice;
    }

    public com.aurora.webservice.UnitPrice getUnitPrice(int i) {
        return this.unitPrice[i];
    }

    public void setUnitPrice(int i, com.aurora.webservice.UnitPrice _value) {
        this.unitPrice[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfUnitPrice)) return false;
        ArrayOfUnitPrice other = (ArrayOfUnitPrice) obj;
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
            ((this.unitPrice==null && other.getUnitPrice()==null) || 
             (this.unitPrice!=null &&
              java.util.Arrays.equals(this.unitPrice, other.getUnitPrice())));
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
        if (getUnitPrice() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUnitPrice());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUnitPrice(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfUnitPrice.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ArrayOfUnitPrice"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dummy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "dummy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unitPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "UnitPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "UnitPrice"));
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
