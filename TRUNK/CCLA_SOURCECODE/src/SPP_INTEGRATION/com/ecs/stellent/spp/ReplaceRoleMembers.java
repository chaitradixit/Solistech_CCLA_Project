/**
 * ReplaceRoleMembers.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ReplaceRoleMembers  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String jobID;

    private java.lang.String roleID;

    private java.lang.String[] resourceIDs;

    public ReplaceRoleMembers() {
    }

    public ReplaceRoleMembers(
           java.lang.String sessionId,
           java.lang.String jobID,
           java.lang.String roleID,
           java.lang.String[] resourceIDs) {
           this.sessionId = sessionId;
           this.jobID = jobID;
           this.roleID = roleID;
           this.resourceIDs = resourceIDs;
    }


    /**
     * Gets the sessionId value for this ReplaceRoleMembers.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this ReplaceRoleMembers.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the jobID value for this ReplaceRoleMembers.
     * 
     * @return jobID
     */
    public java.lang.String getJobID() {
        return jobID;
    }


    /**
     * Sets the jobID value for this ReplaceRoleMembers.
     * 
     * @param jobID
     */
    public void setJobID(java.lang.String jobID) {
        this.jobID = jobID;
    }


    /**
     * Gets the roleID value for this ReplaceRoleMembers.
     * 
     * @return roleID
     */
    public java.lang.String getRoleID() {
        return roleID;
    }


    /**
     * Sets the roleID value for this ReplaceRoleMembers.
     * 
     * @param roleID
     */
    public void setRoleID(java.lang.String roleID) {
        this.roleID = roleID;
    }


    /**
     * Gets the resourceIDs value for this ReplaceRoleMembers.
     * 
     * @return resourceIDs
     */
    public java.lang.String[] getResourceIDs() {
        return resourceIDs;
    }


    /**
     * Sets the resourceIDs value for this ReplaceRoleMembers.
     * 
     * @param resourceIDs
     */
    public void setResourceIDs(java.lang.String[] resourceIDs) {
        this.resourceIDs = resourceIDs;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReplaceRoleMembers)) return false;
        ReplaceRoleMembers other = (ReplaceRoleMembers) obj;
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
            ((this.jobID==null && other.getJobID()==null) || 
             (this.jobID!=null &&
              this.jobID.equals(other.getJobID()))) &&
            ((this.roleID==null && other.getRoleID()==null) || 
             (this.roleID!=null &&
              this.roleID.equals(other.getRoleID()))) &&
            ((this.resourceIDs==null && other.getResourceIDs()==null) || 
             (this.resourceIDs!=null &&
              java.util.Arrays.equals(this.resourceIDs, other.getResourceIDs())));
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
        if (getJobID() != null) {
            _hashCode += getJobID().hashCode();
        }
        if (getRoleID() != null) {
            _hashCode += getRoleID().hashCode();
        }
        if (getResourceIDs() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getResourceIDs());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResourceIDs(), i);
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
        new org.apache.axis.description.TypeDesc(ReplaceRoleMembers.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ReplaceRoleMembers"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"));
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
        elemField.setFieldName("resourceIDs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceIDs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
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
