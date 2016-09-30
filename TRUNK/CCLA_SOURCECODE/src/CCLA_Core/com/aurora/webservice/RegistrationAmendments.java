/**
 * RegistrationAmendments.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class RegistrationAmendments  implements java.io.Serializable {
    private int accountAmendmentsCount;

    private int clientAmendmentsCount;

    private int correspondentAmendmentsCount;

    public RegistrationAmendments() {
    }

    public RegistrationAmendments(
           int accountAmendmentsCount,
           int clientAmendmentsCount,
           int correspondentAmendmentsCount) {
           this.accountAmendmentsCount = accountAmendmentsCount;
           this.clientAmendmentsCount = clientAmendmentsCount;
           this.correspondentAmendmentsCount = correspondentAmendmentsCount;
    }


    /**
     * Gets the accountAmendmentsCount value for this RegistrationAmendments.
     * 
     * @return accountAmendmentsCount
     */
    public int getAccountAmendmentsCount() {
        return accountAmendmentsCount;
    }


    /**
     * Sets the accountAmendmentsCount value for this RegistrationAmendments.
     * 
     * @param accountAmendmentsCount
     */
    public void setAccountAmendmentsCount(int accountAmendmentsCount) {
        this.accountAmendmentsCount = accountAmendmentsCount;
    }


    /**
     * Gets the clientAmendmentsCount value for this RegistrationAmendments.
     * 
     * @return clientAmendmentsCount
     */
    public int getClientAmendmentsCount() {
        return clientAmendmentsCount;
    }


    /**
     * Sets the clientAmendmentsCount value for this RegistrationAmendments.
     * 
     * @param clientAmendmentsCount
     */
    public void setClientAmendmentsCount(int clientAmendmentsCount) {
        this.clientAmendmentsCount = clientAmendmentsCount;
    }


    /**
     * Gets the correspondentAmendmentsCount value for this RegistrationAmendments.
     * 
     * @return correspondentAmendmentsCount
     */
    public int getCorrespondentAmendmentsCount() {
        return correspondentAmendmentsCount;
    }


    /**
     * Sets the correspondentAmendmentsCount value for this RegistrationAmendments.
     * 
     * @param correspondentAmendmentsCount
     */
    public void setCorrespondentAmendmentsCount(int correspondentAmendmentsCount) {
        this.correspondentAmendmentsCount = correspondentAmendmentsCount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistrationAmendments)) return false;
        RegistrationAmendments other = (RegistrationAmendments) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.accountAmendmentsCount == other.getAccountAmendmentsCount() &&
            this.clientAmendmentsCount == other.getClientAmendmentsCount() &&
            this.correspondentAmendmentsCount == other.getCorrespondentAmendmentsCount();
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
        _hashCode += getAccountAmendmentsCount();
        _hashCode += getClientAmendmentsCount();
        _hashCode += getCorrespondentAmendmentsCount();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistrationAmendments.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "RegistrationAmendments"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountAmendmentsCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccountAmendmentsCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clientAmendmentsCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ClientAmendmentsCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correspondentAmendmentsCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "CorrespondentAmendmentsCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
