/**
 * LogonUsingPasswordResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class LogonUsingPasswordResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.LogonDetails logonUsingPasswordResult;

    public LogonUsingPasswordResponse() {
    }

    public LogonUsingPasswordResponse(
           com.ecs.stellent.spp.LogonDetails logonUsingPasswordResult) {
           this.logonUsingPasswordResult = logonUsingPasswordResult;
    }


    /**
     * Gets the logonUsingPasswordResult value for this LogonUsingPasswordResponse.
     * 
     * @return logonUsingPasswordResult
     */
    public com.ecs.stellent.spp.LogonDetails getLogonUsingPasswordResult() {
        return logonUsingPasswordResult;
    }


    /**
     * Sets the logonUsingPasswordResult value for this LogonUsingPasswordResponse.
     * 
     * @param logonUsingPasswordResult
     */
    public void setLogonUsingPasswordResult(com.ecs.stellent.spp.LogonDetails logonUsingPasswordResult) {
        this.logonUsingPasswordResult = logonUsingPasswordResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LogonUsingPasswordResponse)) return false;
        LogonUsingPasswordResponse other = (LogonUsingPasswordResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.logonUsingPasswordResult==null && other.getLogonUsingPasswordResult()==null) || 
             (this.logonUsingPasswordResult!=null &&
              this.logonUsingPasswordResult.equals(other.getLogonUsingPasswordResult())));
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
        if (getLogonUsingPasswordResult() != null) {
            _hashCode += getLogonUsingPasswordResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LogonUsingPasswordResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">LogonUsingPasswordResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logonUsingPasswordResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonUsingPasswordResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonDetails"));
        elemField.setMinOccurs(0);
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
