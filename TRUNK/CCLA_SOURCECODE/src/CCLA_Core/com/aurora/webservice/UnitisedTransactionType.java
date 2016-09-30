/**
 * UnitisedTransactionType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class UnitisedTransactionType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected UnitisedTransactionType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Undefined = "Undefined";
    public static final java.lang.String _BUYDF = "BUYDF";
    public static final java.lang.String _BUYU = "BUYU";
    public static final java.lang.String _TRANSOUT = "TRANSOUT";
    public static final java.lang.String _BUYBNK = "BUYBNK";
    public static final java.lang.String _BUYSHX = "BUYSHX";
    public static final java.lang.String _BUYSHXSALE = "BUYSHXSALE";
    public static final java.lang.String _ORSALECHQ = "ORSALECHQ";
    public static final java.lang.String _ORSALEBACS = "ORSALEBACS";
    public static final java.lang.String _SALECHPS = "SALECHPS";
    public static final java.lang.String _INTERTRANS = "INTERTRANS";
    public static final java.lang.String _SALEBACS = "SALEBACS";
    public static final java.lang.String _SALECHQ = "SALECHQ";
    public static final java.lang.String _BUYCHQ = "BUYCHQ";
    public static final UnitisedTransactionType Undefined = new UnitisedTransactionType(_Undefined);
    public static final UnitisedTransactionType BUYDF = new UnitisedTransactionType(_BUYDF);
    public static final UnitisedTransactionType BUYU = new UnitisedTransactionType(_BUYU);
    public static final UnitisedTransactionType TRANSOUT = new UnitisedTransactionType(_TRANSOUT);
    public static final UnitisedTransactionType BUYBNK = new UnitisedTransactionType(_BUYBNK);
    public static final UnitisedTransactionType BUYSHX = new UnitisedTransactionType(_BUYSHX);
    public static final UnitisedTransactionType BUYSHXSALE = new UnitisedTransactionType(_BUYSHXSALE);
    public static final UnitisedTransactionType ORSALECHQ = new UnitisedTransactionType(_ORSALECHQ);
    public static final UnitisedTransactionType ORSALEBACS = new UnitisedTransactionType(_ORSALEBACS);
    public static final UnitisedTransactionType SALECHPS = new UnitisedTransactionType(_SALECHPS);
    public static final UnitisedTransactionType INTERTRANS = new UnitisedTransactionType(_INTERTRANS);
    public static final UnitisedTransactionType SALEBACS = new UnitisedTransactionType(_SALEBACS);
    public static final UnitisedTransactionType SALECHQ = new UnitisedTransactionType(_SALECHQ);
    public static final UnitisedTransactionType BUYCHQ = new UnitisedTransactionType(_BUYCHQ);
    public java.lang.String getValue() { return _value_;}
    public static UnitisedTransactionType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        UnitisedTransactionType enumeration = (UnitisedTransactionType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static UnitisedTransactionType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(UnitisedTransactionType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "UnitisedTransactionType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
