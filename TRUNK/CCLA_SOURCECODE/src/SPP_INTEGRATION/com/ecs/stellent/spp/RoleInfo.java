/**
 * RoleInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class RoleInfo  implements java.io.Serializable {
    private short roleType;

    private java.lang.String roleName;

    private java.lang.String roleID;

    private com.ecs.stellent.spp.RoleMember[] roleMembers;

    public RoleInfo() {
    }

    public RoleInfo(
           short roleType,
           java.lang.String roleName,
           java.lang.String roleID,
           com.ecs.stellent.spp.RoleMember[] roleMembers) {
           this.roleType = roleType;
           this.roleName = roleName;
           this.roleID = roleID;
           this.roleMembers = roleMembers;
    }


    /**
     * Gets the roleType value for this RoleInfo.
     * 
     * @return roleType
     */
    public short getRoleType() {
        return roleType;
    }


    /**
     * Sets the roleType value for this RoleInfo.
     * 
     * @param roleType
     */
    public void setRoleType(short roleType) {
        this.roleType = roleType;
    }


    /**
     * Gets the roleName value for this RoleInfo.
     * 
     * @return roleName
     */
    public java.lang.String getRoleName() {
        return roleName;
    }


    /**
     * Sets the roleName value for this RoleInfo.
     * 
     * @param roleName
     */
    public void setRoleName(java.lang.String roleName) {
        this.roleName = roleName;
    }


    /**
     * Gets the roleID value for this RoleInfo.
     * 
     * @return roleID
     */
    public java.lang.String getRoleID() {
        return roleID;
    }


    /**
     * Sets the roleID value for this RoleInfo.
     * 
     * @param roleID
     */
    public void setRoleID(java.lang.String roleID) {
        this.roleID = roleID;
    }


    /**
     * Gets the roleMembers value for this RoleInfo.
     * 
     * @return roleMembers
     */
    public com.ecs.stellent.spp.RoleMember[] getRoleMembers() {
        return roleMembers;
    }


    /**
     * Sets the roleMembers value for this RoleInfo.
     * 
     * @param roleMembers
     */
    public void setRoleMembers(com.ecs.stellent.spp.RoleMember[] roleMembers) {
        this.roleMembers = roleMembers;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RoleInfo)) return false;
        RoleInfo other = (RoleInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.roleType == other.getRoleType() &&
            ((this.roleName==null && other.getRoleName()==null) || 
             (this.roleName!=null &&
              this.roleName.equals(other.getRoleName()))) &&
            ((this.roleID==null && other.getRoleID()==null) || 
             (this.roleID!=null &&
              this.roleID.equals(other.getRoleID()))) &&
            ((this.roleMembers==null && other.getRoleMembers()==null) || 
             (this.roleMembers!=null &&
              java.util.Arrays.equals(this.roleMembers, other.getRoleMembers())));
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
        _hashCode += getRoleType();
        if (getRoleName() != null) {
            _hashCode += getRoleName().hashCode();
        }
        if (getRoleID() != null) {
            _hashCode += getRoleID().hashCode();
        }
        if (getRoleMembers() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRoleMembers());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRoleMembers(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RoleInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roleType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roleName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roleID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roleMembers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleMembers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleMember"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleMember"));
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
