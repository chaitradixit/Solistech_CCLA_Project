/**
 * GetResourcesWithSkillAndSecurity.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetResourcesWithSkillAndSecurity  implements java.io.Serializable {
    private java.lang.String sessionId;

    private boolean useSkillLevel;

    private short skillLevel;

    private boolean useSecurityLevel;

    private short securityLevel;

    private boolean useGroupResourceId;

    private java.lang.String groupResourceId;

    public GetResourcesWithSkillAndSecurity() {
    }

    public GetResourcesWithSkillAndSecurity(
           java.lang.String sessionId,
           boolean useSkillLevel,
           short skillLevel,
           boolean useSecurityLevel,
           short securityLevel,
           boolean useGroupResourceId,
           java.lang.String groupResourceId) {
           this.sessionId = sessionId;
           this.useSkillLevel = useSkillLevel;
           this.skillLevel = skillLevel;
           this.useSecurityLevel = useSecurityLevel;
           this.securityLevel = securityLevel;
           this.useGroupResourceId = useGroupResourceId;
           this.groupResourceId = groupResourceId;
    }


    /**
     * Gets the sessionId value for this GetResourcesWithSkillAndSecurity.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetResourcesWithSkillAndSecurity.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the useSkillLevel value for this GetResourcesWithSkillAndSecurity.
     * 
     * @return useSkillLevel
     */
    public boolean isUseSkillLevel() {
        return useSkillLevel;
    }


    /**
     * Sets the useSkillLevel value for this GetResourcesWithSkillAndSecurity.
     * 
     * @param useSkillLevel
     */
    public void setUseSkillLevel(boolean useSkillLevel) {
        this.useSkillLevel = useSkillLevel;
    }


    /**
     * Gets the skillLevel value for this GetResourcesWithSkillAndSecurity.
     * 
     * @return skillLevel
     */
    public short getSkillLevel() {
        return skillLevel;
    }


    /**
     * Sets the skillLevel value for this GetResourcesWithSkillAndSecurity.
     * 
     * @param skillLevel
     */
    public void setSkillLevel(short skillLevel) {
        this.skillLevel = skillLevel;
    }


    /**
     * Gets the useSecurityLevel value for this GetResourcesWithSkillAndSecurity.
     * 
     * @return useSecurityLevel
     */
    public boolean isUseSecurityLevel() {
        return useSecurityLevel;
    }


    /**
     * Sets the useSecurityLevel value for this GetResourcesWithSkillAndSecurity.
     * 
     * @param useSecurityLevel
     */
    public void setUseSecurityLevel(boolean useSecurityLevel) {
        this.useSecurityLevel = useSecurityLevel;
    }


    /**
     * Gets the securityLevel value for this GetResourcesWithSkillAndSecurity.
     * 
     * @return securityLevel
     */
    public short getSecurityLevel() {
        return securityLevel;
    }


    /**
     * Sets the securityLevel value for this GetResourcesWithSkillAndSecurity.
     * 
     * @param securityLevel
     */
    public void setSecurityLevel(short securityLevel) {
        this.securityLevel = securityLevel;
    }


    /**
     * Gets the useGroupResourceId value for this GetResourcesWithSkillAndSecurity.
     * 
     * @return useGroupResourceId
     */
    public boolean isUseGroupResourceId() {
        return useGroupResourceId;
    }


    /**
     * Sets the useGroupResourceId value for this GetResourcesWithSkillAndSecurity.
     * 
     * @param useGroupResourceId
     */
    public void setUseGroupResourceId(boolean useGroupResourceId) {
        this.useGroupResourceId = useGroupResourceId;
    }


    /**
     * Gets the groupResourceId value for this GetResourcesWithSkillAndSecurity.
     * 
     * @return groupResourceId
     */
    public java.lang.String getGroupResourceId() {
        return groupResourceId;
    }


    /**
     * Sets the groupResourceId value for this GetResourcesWithSkillAndSecurity.
     * 
     * @param groupResourceId
     */
    public void setGroupResourceId(java.lang.String groupResourceId) {
        this.groupResourceId = groupResourceId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetResourcesWithSkillAndSecurity)) return false;
        GetResourcesWithSkillAndSecurity other = (GetResourcesWithSkillAndSecurity) obj;
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
            this.useSkillLevel == other.isUseSkillLevel() &&
            this.skillLevel == other.getSkillLevel() &&
            this.useSecurityLevel == other.isUseSecurityLevel() &&
            this.securityLevel == other.getSecurityLevel() &&
            this.useGroupResourceId == other.isUseGroupResourceId() &&
            ((this.groupResourceId==null && other.getGroupResourceId()==null) || 
             (this.groupResourceId!=null &&
              this.groupResourceId.equals(other.getGroupResourceId())));
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
        _hashCode += (isUseSkillLevel() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getSkillLevel();
        _hashCode += (isUseSecurityLevel() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getSecurityLevel();
        _hashCode += (isUseGroupResourceId() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getGroupResourceId() != null) {
            _hashCode += getGroupResourceId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetResourcesWithSkillAndSecurity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetResourcesWithSkillAndSecurity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useSkillLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseSkillLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skillLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SkillLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useSecurityLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseSecurityLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SecurityLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useGroupResourceId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseGroupResourceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupResourceId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GroupResourceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
