/**
 * RoleMember.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class RoleMember  implements java.io.Serializable {
    private java.lang.String roleMemberId;

    private java.lang.String roleMemberName;

    public RoleMember() {
    }

    public RoleMember(
           java.lang.String roleMemberId,
           java.lang.String roleMemberName) {
           this.roleMemberId = roleMemberId;
           this.roleMemberName = roleMemberName;
    }


    /**
     * Gets the roleMemberId value for this RoleMember.
     * 
     * @return roleMemberId
     */
    public java.lang.String getRoleMemberId() {
        return roleMemberId;
    }


    /**
     * Sets the roleMemberId value for this RoleMember.
     * 
     * @param roleMemberId
     */
    public void setRoleMemberId(java.lang.String roleMemberId) {
        this.roleMemberId = roleMemberId;
    }


    /**
     * Gets the roleMemberName value for this RoleMember.
     * 
     * @return roleMemberName
     */
    public java.lang.String getRoleMemberName() {
        return roleMemberName;
    }


    /**
     * Sets the roleMemberName value for this RoleMember.
     * 
     * @param roleMemberName
     */
    public void setRoleMemberName(java.lang.String roleMemberName) {
        this.roleMemberName = roleMemberName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoleMember)) return false;
        RoleMember other = (RoleMember) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.roleMemberId==null && other.getRoleMemberId()==null) || 
             (this.roleMemberId!=null &&
              this.roleMemberId.equals(other.getRoleMemberId()))) &&
            ((this.roleMemberName==null && other.getRoleMemberName()==null) || 
             (this.roleMemberName!=null &&
              this.roleMemberName.equals(other.getRoleMemberName())));
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
        if (getRoleMemberId() != null) {
            _hashCode += getRoleMemberId().hashCode();
        }
        if (getRoleMemberName() != null) {
            _hashCode += getRoleMemberName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoleMember.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleMember"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roleMemberId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleMemberId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roleMemberName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleMemberName"));
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
