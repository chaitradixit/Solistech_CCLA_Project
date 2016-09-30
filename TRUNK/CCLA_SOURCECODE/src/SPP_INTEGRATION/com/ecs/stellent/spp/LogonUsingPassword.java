/**
 * LogonUsingPassword.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class LogonUsingPassword  implements java.io.Serializable {
    private java.lang.String logonName;

    private java.lang.String logonPassword;

    private short logonType;

    private boolean unConditional;

    public LogonUsingPassword() {
    }

    public LogonUsingPassword(
           java.lang.String logonName,
           java.lang.String logonPassword,
           short logonType,
           boolean unConditional) {
           this.logonName = logonName;
           this.logonPassword = logonPassword;
           this.logonType = logonType;
           this.unConditional = unConditional;
    }


    /**
     * Gets the logonName value for this LogonUsingPassword.
     * 
     * @return logonName
     */
    public java.lang.String getLogonName() {
        return logonName;
    }


    /**
     * Sets the logonName value for this LogonUsingPassword.
     * 
     * @param logonName
     */
    public void setLogonName(java.lang.String logonName) {
        this.logonName = logonName;
    }


    /**
     * Gets the logonPassword value for this LogonUsingPassword.
     * 
     * @return logonPassword
     */
    public java.lang.String getLogonPassword() {
        return logonPassword;
    }


    /**
     * Sets the logonPassword value for this LogonUsingPassword.
     * 
     * @param logonPassword
     */
    public void setLogonPassword(java.lang.String logonPassword) {
        this.logonPassword = logonPassword;
    }


    /**
     * Gets the logonType value for this LogonUsingPassword.
     * 
     * @return logonType
     */
    public short getLogonType() {
        return logonType;
    }


    /**
     * Sets the logonType value for this LogonUsingPassword.
     * 
     * @param logonType
     */
    public void setLogonType(short logonType) {
        this.logonType = logonType;
    }


    /**
     * Gets the unConditional value for this LogonUsingPassword.
     * 
     * @return unConditional
     */
    public boolean isUnConditional() {
        return unConditional;
    }


    /**
     * Sets the unConditional value for this LogonUsingPassword.
     * 
     * @param unConditional
     */
    public void setUnConditional(boolean unConditional) {
        this.unConditional = unConditional;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LogonUsingPassword)) return false;
        LogonUsingPassword other = (LogonUsingPassword) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.logonName==null && other.getLogonName()==null) || 
             (this.logonName!=null &&
              this.logonName.equals(other.getLogonName()))) &&
            ((this.logonPassword==null && other.getLogonPassword()==null) || 
             (this.logonPassword!=null &&
              this.logonPassword.equals(other.getLogonPassword()))) &&
            this.logonType == other.getLogonType() &&
            this.unConditional == other.isUnConditional();
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
        if (getLogonName() != null) {
            _hashCode += getLogonName().hashCode();
        }
        if (getLogonPassword() != null) {
            _hashCode += getLogonPassword().hashCode();
        }
        _hashCode += getLogonType();
        _hashCode += (isUnConditional() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LogonUsingPassword.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">LogonUsingPassword"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logonName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logonPassword");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonPassword"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logonType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unConditional");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UnConditional"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
