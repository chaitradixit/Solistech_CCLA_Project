/**
 * GetMapsForUserBasedOnType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetMapsForUserBasedOnType  implements java.io.Serializable {
    private java.lang.String sessionId;

    private short accessType;

    private short processType;

    private boolean allVersions;

    public GetMapsForUserBasedOnType() {
    }

    public GetMapsForUserBasedOnType(
           java.lang.String sessionId,
           short accessType,
           short processType,
           boolean allVersions) {
           this.sessionId = sessionId;
           this.accessType = accessType;
           this.processType = processType;
           this.allVersions = allVersions;
    }


    /**
     * Gets the sessionId value for this GetMapsForUserBasedOnType.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetMapsForUserBasedOnType.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the accessType value for this GetMapsForUserBasedOnType.
     * 
     * @return accessType
     */
    public short getAccessType() {
        return accessType;
    }


    /**
     * Sets the accessType value for this GetMapsForUserBasedOnType.
     * 
     * @param accessType
     */
    public void setAccessType(short accessType) {
        this.accessType = accessType;
    }


    /**
     * Gets the processType value for this GetMapsForUserBasedOnType.
     * 
     * @return processType
     */
    public short getProcessType() {
        return processType;
    }


    /**
     * Sets the processType value for this GetMapsForUserBasedOnType.
     * 
     * @param processType
     */
    public void setProcessType(short processType) {
        this.processType = processType;
    }


    /**
     * Gets the allVersions value for this GetMapsForUserBasedOnType.
     * 
     * @return allVersions
     */
    public boolean isAllVersions() {
        return allVersions;
    }


    /**
     * Sets the allVersions value for this GetMapsForUserBasedOnType.
     * 
     * @param allVersions
     */
    public void setAllVersions(boolean allVersions) {
        this.allVersions = allVersions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetMapsForUserBasedOnType)) return false;
        GetMapsForUserBasedOnType other = (GetMapsForUserBasedOnType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sessionId==null && other.getSessionId()==null) || 
             (this.sessionId!=null &&
              this.sessionId.equals(other.getSessionId()))) &&
            this.accessType == other.getAccessType() &&
            this.processType == other.getProcessType() &&
            this.allVersions == other.isAllVersions();
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
        if (getSessionId() != null) {
            _hashCode += getSessionId().hashCode();
        }
        _hashCode += getAccessType();
        _hashCode += getProcessType();
        _hashCode += (isAllVersions() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetMapsForUserBasedOnType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMapsForUserBasedOnType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accessType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AccessType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allVersions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AllVersions"));
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
