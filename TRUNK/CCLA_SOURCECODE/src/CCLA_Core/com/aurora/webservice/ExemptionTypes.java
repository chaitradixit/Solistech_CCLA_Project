/**
 * ExemptionTypes.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class ExemptionTypes implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ExemptionTypes(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _UNSET = "UNSET";
    public static final java.lang.String _CHARITYREGISTRATION = "CHARITYREGISTRATION";
    public static final java.lang.String _INLANDREVENUETAXREFERENCE = "INLANDREVENUETAXREFERENCE";
    public static final java.lang.String _OTHER = "OTHER";
    public static final ExemptionTypes UNSET = new ExemptionTypes(_UNSET);
    public static final ExemptionTypes CHARITYREGISTRATION = new ExemptionTypes(_CHARITYREGISTRATION);
    public static final ExemptionTypes INLANDREVENUETAXREFERENCE = new ExemptionTypes(_INLANDREVENUETAXREFERENCE);
    public static final ExemptionTypes OTHER = new ExemptionTypes(_OTHER);
    public java.lang.String getValue() { return _value_;}
    public static ExemptionTypes fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ExemptionTypes enumeration = (ExemptionTypes)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ExemptionTypes fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExemptionTypes.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ExemptionTypes"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
