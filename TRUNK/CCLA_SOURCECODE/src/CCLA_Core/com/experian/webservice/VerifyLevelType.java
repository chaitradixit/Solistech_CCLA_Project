/**
 * VerifyLevelType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class VerifyLevelType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected VerifyLevelType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _None = "None";
    public static final java.lang.String _Verified = "Verified";
    public static final java.lang.String _InteractionRequired = "InteractionRequired";
    public static final java.lang.String _PremisesPartial = "PremisesPartial";
    public static final java.lang.String _StreetPartial = "StreetPartial";
    public static final java.lang.String _Multiple = "Multiple";
    public static final VerifyLevelType None = new VerifyLevelType(_None);
    public static final VerifyLevelType Verified = new VerifyLevelType(_Verified);
    public static final VerifyLevelType InteractionRequired = new VerifyLevelType(_InteractionRequired);
    public static final VerifyLevelType PremisesPartial = new VerifyLevelType(_PremisesPartial);
    public static final VerifyLevelType StreetPartial = new VerifyLevelType(_StreetPartial);
    public static final VerifyLevelType Multiple = new VerifyLevelType(_Multiple);
    public java.lang.String getValue() { return _value_;}
    public static VerifyLevelType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        VerifyLevelType enumeration = (VerifyLevelType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static VerifyLevelType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(VerifyLevelType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "VerifyLevelType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
