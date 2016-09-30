/**
 * MTSResourceDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class MTSResourceDetails  implements java.io.Serializable {
    private java.lang.String resourceId;

    private java.lang.String resourceName;

    private short skillLevelMin;

    private short skillLevelMax;

    private short securityLevel;

    private boolean isGroupResource;

    public MTSResourceDetails() {
    }

    public MTSResourceDetails(
           java.lang.String resourceId,
           java.lang.String resourceName,
           short skillLevelMin,
           short skillLevelMax,
           short securityLevel,
           boolean isGroupResource) {
           this.resourceId = resourceId;
           this.resourceName = resourceName;
           this.skillLevelMin = skillLevelMin;
           this.skillLevelMax = skillLevelMax;
           this.securityLevel = securityLevel;
           this.isGroupResource = isGroupResource;
    }


    /**
     * Gets the resourceId value for this MTSResourceDetails.
     * 
     * @return resourceId
     */
    public java.lang.String getResourceId() {
        return resourceId;
    }


    /**
     * Sets the resourceId value for this MTSResourceDetails.
     * 
     * @param resourceId
     */
    public void setResourceId(java.lang.String resourceId) {
        this.resourceId = resourceId;
    }


    /**
     * Gets the resourceName value for this MTSResourceDetails.
     * 
     * @return resourceName
     */
    public java.lang.String getResourceName() {
        return resourceName;
    }


    /**
     * Sets the resourceName value for this MTSResourceDetails.
     * 
     * @param resourceName
     */
    public void setResourceName(java.lang.String resourceName) {
        this.resourceName = resourceName;
    }


    /**
     * Gets the skillLevelMin value for this MTSResourceDetails.
     * 
     * @return skillLevelMin
     */
    public short getSkillLevelMin() {
        return skillLevelMin;
    }


    /**
     * Sets the skillLevelMin value for this MTSResourceDetails.
     * 
     * @param skillLevelMin
     */
    public void setSkillLevelMin(short skillLevelMin) {
        this.skillLevelMin = skillLevelMin;
    }


    /**
     * Gets the skillLevelMax value for this MTSResourceDetails.
     * 
     * @return skillLevelMax
     */
    public short getSkillLevelMax() {
        return skillLevelMax;
    }


    /**
     * Sets the skillLevelMax value for this MTSResourceDetails.
     * 
     * @param skillLevelMax
     */
    public void setSkillLevelMax(short skillLevelMax) {
        this.skillLevelMax = skillLevelMax;
    }


    /**
     * Gets the securityLevel value for this MTSResourceDetails.
     * 
     * @return securityLevel
     */
    public short getSecurityLevel() {
        return securityLevel;
    }


    /**
     * Sets the securityLevel value for this MTSResourceDetails.
     * 
     * @param securityLevel
     */
    public void setSecurityLevel(short securityLevel) {
        this.securityLevel = securityLevel;
    }


    /**
     * Gets the isGroupResource value for this MTSResourceDetails.
     * 
     * @return isGroupResource
     */
    public boolean isIsGroupResource() {
        return isGroupResource;
    }


    /**
     * Sets the isGroupResource value for this MTSResourceDetails.
     * 
     * @param isGroupResource
     */
    public void setIsGroupResource(boolean isGroupResource) {
        this.isGroupResource = isGroupResource;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MTSResourceDetails)) return false;
        MTSResourceDetails other = (MTSResourceDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.resourceId==null && other.getResourceId()==null) || 
             (this.resourceId!=null &&
              this.resourceId.equals(other.getResourceId()))) &&
            ((this.resourceName==null && other.getResourceName()==null) || 
             (this.resourceName!=null &&
              this.resourceName.equals(other.getResourceName()))) &&
            this.skillLevelMin == other.getSkillLevelMin() &&
            this.skillLevelMax == other.getSkillLevelMax() &&
            this.securityLevel == other.getSecurityLevel() &&
            this.isGroupResource == other.isIsGroupResource();
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
        if (getResourceId() != null) {
            _hashCode += getResourceId().hashCode();
        }
        if (getResourceName() != null) {
            _hashCode += getResourceName().hashCode();
        }
        _hashCode += getSkillLevelMin();
        _hashCode += getSkillLevelMax();
        _hashCode += getSecurityLevel();
        _hashCode += (isIsGroupResource() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MTSResourceDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MTSResourceDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourceId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourceName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skillLevelMin");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SkillLevelMin"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skillLevelMax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SkillLevelMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SecurityLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isGroupResource");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "IsGroupResource"));
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
