/**
 * ArrayOfUnitValuationPrice.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class ArrayOfUnitValuationPrice  implements java.io.Serializable {
    private java.lang.String dummy;

    private com.aurora.webservice.UnitValuationPrice[] unitValuationPrice;

    public ArrayOfUnitValuationPrice() {
    }

    public ArrayOfUnitValuationPrice(
           java.lang.String dummy,
           com.aurora.webservice.UnitValuationPrice[] unitValuationPrice) {
           this.dummy = dummy;
           this.unitValuationPrice = unitValuationPrice;
    }


    /**
     * Gets the dummy value for this ArrayOfUnitValuationPrice.
     * 
     * @return dummy
     */
    public java.lang.String getDummy() {
        return dummy;
    }


    /**
     * Sets the dummy value for this ArrayOfUnitValuationPrice.
     * 
     * @param dummy
     */
    public void setDummy(java.lang.String dummy) {
        this.dummy = dummy;
    }


    /**
     * Gets the unitValuationPrice value for this ArrayOfUnitValuationPrice.
     * 
     * @return unitValuationPrice
     */
    public com.aurora.webservice.UnitValuationPrice[] getUnitValuationPrice() {
        return unitValuationPrice;
    }


    /**
     * Sets the unitValuationPrice value for this ArrayOfUnitValuationPrice.
     * 
     * @param unitValuationPrice
     */
    public void setUnitValuationPrice(com.aurora.webservice.UnitValuationPrice[] unitValuationPrice) {
        this.unitValuationPrice = unitValuationPrice;
    }

    public com.aurora.webservice.UnitValuationPrice getUnitValuationPrice(int i) {
        return this.unitValuationPrice[i];
    }

    public void setUnitValuationPrice(int i, com.aurora.webservice.UnitValuationPrice _value) {
        this.unitValuationPrice[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfUnitValuationPrice)) return false;
        ArrayOfUnitValuationPrice other = (ArrayOfUnitValuationPrice) obj;
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
            ((this.unitValuationPrice==null && other.getUnitValuationPrice()==null) || 
             (this.unitValuationPrice!=null &&
              java.util.Arrays.equals(this.unitValuationPrice, other.getUnitValuationPrice())));
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
        if (getUnitValuationPrice() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUnitValuationPrice());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUnitValuationPrice(), i);
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
        new org.apache.axis.description.TypeDesc(ArrayOfUnitValuationPrice.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ArrayOfUnitValuationPrice"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dummy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "dummy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unitValuationPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "UnitValuationPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "UnitValuationPrice"));
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
