/**
 * IncomeDistributionMethods.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class IncomeDistributionMethods implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected IncomeDistributionMethods(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Undefined = "Undefined";
    public static final java.lang.String _PAYA = "PAYA";
    public static final java.lang.String _REIN = "REIN";
    public static final java.lang.String _RETN = "RETN";
    public static final java.lang.String _TXRI = "TXRI";
    public static final IncomeDistributionMethods Undefined = new IncomeDistributionMethods(_Undefined);
    public static final IncomeDistributionMethods PAYA = new IncomeDistributionMethods(_PAYA);
    public static final IncomeDistributionMethods REIN = new IncomeDistributionMethods(_REIN);
    public static final IncomeDistributionMethods RETN = new IncomeDistributionMethods(_RETN);
    public static final IncomeDistributionMethods TXRI = new IncomeDistributionMethods(_TXRI);
    public java.lang.String getValue() { return _value_;}
    public static IncomeDistributionMethods fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        IncomeDistributionMethods enumeration = (IncomeDistributionMethods)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static IncomeDistributionMethods fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(IncomeDistributionMethods.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeDistributionMethods"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
