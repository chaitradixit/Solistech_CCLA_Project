/**
 * GetResourcesWithSkillAndSecurityUsingGroupName.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetResourcesWithSkillAndSecurityUsingGroupName  implements java.io.Serializable {
    private java.lang.String sessionId;

    private boolean useSkillLevel;

    private short skillLevel;

    private boolean useSecurityLevel;

    private short securityLevel;

    private java.lang.String groupResourceName;

    public GetResourcesWithSkillAndSecurityUsingGroupName() {
    }

    public GetResourcesWithSkillAndSecurityUsingGroupName(
           java.lang.String sessionId,
           boolean useSkillLevel,
           short skillLevel,
           boolean useSecurityLevel,
           short securityLevel,
           java.lang.String groupResourceName) {
           this.sessionId = sessionId;
           this.useSkillLevel = useSkillLevel;
           this.skillLevel = skillLevel;
           this.useSecurityLevel = useSecurityLevel;
           this.securityLevel = securityLevel;
           this.groupResourceName = groupResourceName;
    }


    /**
     * Gets the sessionId value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the useSkillLevel value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @return useSkillLevel
     */
    public boolean isUseSkillLevel() {
        return useSkillLevel;
    }


    /**
     * Sets the useSkillLevel value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @param useSkillLevel
     */
    public void setUseSkillLevel(boolean useSkillLevel) {
        this.useSkillLevel = useSkillLevel;
    }


    /**
     * Gets the skillLevel value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @return skillLevel
     */
    public short getSkillLevel() {
        return skillLevel;
    }


    /**
     * Sets the skillLevel value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @param skillLevel
     */
    public void setSkillLevel(short skillLevel) {
        this.skillLevel = skillLevel;
    }


    /**
     * Gets the useSecurityLevel value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @return useSecurityLevel
     */
    public boolean isUseSecurityLevel() {
        return useSecurityLevel;
    }


    /**
     * Sets the useSecurityLevel value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @param useSecurityLevel
     */
    public void setUseSecurityLevel(boolean useSecurityLevel) {
        this.useSecurityLevel = useSecurityLevel;
    }


    /**
     * Gets the securityLevel value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @return securityLevel
     */
    public short getSecurityLevel() {
        return securityLevel;
    }


    /**
     * Sets the securityLevel value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @param securityLevel
     */
    public void setSecurityLevel(short securityLevel) {
        this.securityLevel = securityLevel;
    }


    /**
     * Gets the groupResourceName value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @return groupResourceName
     */
    public java.lang.String getGroupResourceName() {
        return groupResourceName;
    }


    /**
     * Sets the groupResourceName value for this GetResourcesWithSkillAndSecurityUsingGroupName.
     * 
     * @param groupResourceName
     */
    public void setGroupResourceName(java.lang.String groupResourceName) {
        this.groupResourceName = groupResourceName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetResourcesWithSkillAndSecurityUsingGroupName)) return false;
        GetResourcesWithSkillAndSecurityUsingGroupName other = (GetResourcesWithSkillAndSecurityUsingGroupName) obj;
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
            ((this.groupResourceName==null && other.getGroupResourceName()==null) || 
             (this.groupResourceName!=null &&
              this.groupResourceName.equals(other.getGroupResourceName())));
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
        if (getGroupResourceName() != null) {
            _hashCode += getGroupResourceName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetResourcesWithSkillAndSecurityUsingGroupName.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetResourcesWithSkillAndSecurityUsingGroupName"));
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
        elemField.setFieldName("groupResourceName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GroupResourceName"));
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
