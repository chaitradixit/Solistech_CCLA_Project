/**
 * DepositTransactionType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class DepositTransactionType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected DepositTransactionType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Undefined = "Undefined";
    public static final java.lang.String _DEPCHQ = "DEPCHQ";
    public static final java.lang.String _DEPBNK = "DEPBNK";
    public static final java.lang.String _DEPCHPS = "DEPCHPS";
    public static final java.lang.String _WTHCHQ = "WTHCHQ";
    public static final java.lang.String _WTHBACS = "WTHBACS";
    public static final java.lang.String _WTHCHPS = "WTHCHPS";
    public static final java.lang.String _ORWTHCHQ = "ORWTHCHQ";
    public static final java.lang.String _ORWTHBACS = "ORWTHBACS";
    public static final java.lang.String _REJWTH = "REJWTH";
    public static final java.lang.String _REJINT = "REJINT";
    public static final java.lang.String _TRANSOUT = "TRANSOUT";
    public static final DepositTransactionType Undefined = new DepositTransactionType(_Undefined);
    public static final DepositTransactionType DEPCHQ = new DepositTransactionType(_DEPCHQ);
    public static final DepositTransactionType DEPBNK = new DepositTransactionType(_DEPBNK);
    public static final DepositTransactionType DEPCHPS = new DepositTransactionType(_DEPCHPS);
    public static final DepositTransactionType WTHCHQ = new DepositTransactionType(_WTHCHQ);
    public static final DepositTransactionType WTHBACS = new DepositTransactionType(_WTHBACS);
    public static final DepositTransactionType WTHCHPS = new DepositTransactionType(_WTHCHPS);
    public static final DepositTransactionType ORWTHCHQ = new DepositTransactionType(_ORWTHCHQ);
    public static final DepositTransactionType ORWTHBACS = new DepositTransactionType(_ORWTHBACS);
    public static final DepositTransactionType REJWTH = new DepositTransactionType(_REJWTH);
    public static final DepositTransactionType REJINT = new DepositTransactionType(_REJINT);
    public static final DepositTransactionType TRANSOUT = new DepositTransactionType(_TRANSOUT);
    public java.lang.String getValue() { return _value_;}
    public static DepositTransactionType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        DepositTransactionType enumeration = (DepositTransactionType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static DepositTransactionType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(DepositTransactionType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositTransactionType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
