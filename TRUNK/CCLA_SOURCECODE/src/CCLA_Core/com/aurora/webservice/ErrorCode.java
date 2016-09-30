/**
 * ErrorCode.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class ErrorCode implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ErrorCode(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _System = "System";
    public static final java.lang.String _InvalidData = "InvalidData";
    public static final java.lang.String _NotADealingDay = "NotADealingDay";
    public static final java.lang.String _NotFound = "NotFound";
    public static final java.lang.String _NullValue = "NullValue";
    public static final java.lang.String _DuplicateRecordsFound = "DuplicateRecordsFound";
    public static final java.lang.String _DataMarshalFailed = "DataMarshalFailed";
    public static final java.lang.String _DatabaseUpdateFailed = "DatabaseUpdateFailed";
    public static final java.lang.String _BusinessLogicException = "BusinessLogicException";
    public static final ErrorCode System = new ErrorCode(_System);
    public static final ErrorCode InvalidData = new ErrorCode(_InvalidData);
    public static final ErrorCode NotADealingDay = new ErrorCode(_NotADealingDay);
    public static final ErrorCode NotFound = new ErrorCode(_NotFound);
    public static final ErrorCode NullValue = new ErrorCode(_NullValue);
    public static final ErrorCode DuplicateRecordsFound = new ErrorCode(_DuplicateRecordsFound);
    public static final ErrorCode DataMarshalFailed = new ErrorCode(_DataMarshalFailed);
    public static final ErrorCode DatabaseUpdateFailed = new ErrorCode(_DatabaseUpdateFailed);
    public static final ErrorCode BusinessLogicException = new ErrorCode(_BusinessLogicException);
    public java.lang.String getValue() { return _value_;}
    public static ErrorCode fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ErrorCode enumeration = (ErrorCode)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ErrorCode fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(ErrorCode.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ErrorCode"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
