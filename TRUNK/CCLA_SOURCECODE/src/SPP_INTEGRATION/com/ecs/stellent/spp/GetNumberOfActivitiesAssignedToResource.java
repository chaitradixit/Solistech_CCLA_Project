/**
 * GetNumberOfActivitiesAssignedToResource.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetNumberOfActivitiesAssignedToResource  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String resourceId;

    private boolean useActivityName;

    private java.lang.String activityName;

    private boolean useProcessId;

    private java.lang.String processId;

    private short resourcesIncluded;

    public GetNumberOfActivitiesAssignedToResource() {
    }

    public GetNumberOfActivitiesAssignedToResource(
           java.lang.String sessionId,
           java.lang.String resourceId,
           boolean useActivityName,
           java.lang.String activityName,
           boolean useProcessId,
           java.lang.String processId,
           short resourcesIncluded) {
           this.sessionId = sessionId;
           this.resourceId = resourceId;
           this.useActivityName = useActivityName;
           this.activityName = activityName;
           this.useProcessId = useProcessId;
           this.processId = processId;
           this.resourcesIncluded = resourcesIncluded;
    }


    /**
     * Gets the sessionId value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the resourceId value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @return resourceId
     */
    public java.lang.String getResourceId() {
        return resourceId;
    }


    /**
     * Sets the resourceId value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @param resourceId
     */
    public void setResourceId(java.lang.String resourceId) {
        this.resourceId = resourceId;
    }


    /**
     * Gets the useActivityName value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @return useActivityName
     */
    public boolean isUseActivityName() {
        return useActivityName;
    }


    /**
     * Sets the useActivityName value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @param useActivityName
     */
    public void setUseActivityName(boolean useActivityName) {
        this.useActivityName = useActivityName;
    }


    /**
     * Gets the activityName value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @return activityName
     */
    public java.lang.String getActivityName() {
        return activityName;
    }


    /**
     * Sets the activityName value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @param activityName
     */
    public void setActivityName(java.lang.String activityName) {
        this.activityName = activityName;
    }


    /**
     * Gets the useProcessId value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @return useProcessId
     */
    public boolean isUseProcessId() {
        return useProcessId;
    }


    /**
     * Sets the useProcessId value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @param useProcessId
     */
    public void setUseProcessId(boolean useProcessId) {
        this.useProcessId = useProcessId;
    }


    /**
     * Gets the processId value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @return processId
     */
    public java.lang.String getProcessId() {
        return processId;
    }


    /**
     * Sets the processId value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @param processId
     */
    public void setProcessId(java.lang.String processId) {
        this.processId = processId;
    }


    /**
     * Gets the resourcesIncluded value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @return resourcesIncluded
     */
    public short getResourcesIncluded() {
        return resourcesIncluded;
    }


    /**
     * Sets the resourcesIncluded value for this GetNumberOfActivitiesAssignedToResource.
     * 
     * @param resourcesIncluded
     */
    public void setResourcesIncluded(short resourcesIncluded) {
        this.resourcesIncluded = resourcesIncluded;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetNumberOfActivitiesAssignedToResource)) return false;
        GetNumberOfActivitiesAssignedToResource other = (GetNumberOfActivitiesAssignedToResource) obj;
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
            ((this.resourceId==null && other.getResourceId()==null) || 
             (this.resourceId!=null &&
              this.resourceId.equals(other.getResourceId()))) &&
            this.useActivityName == other.isUseActivityName() &&
            ((this.activityName==null && other.getActivityName()==null) || 
             (this.activityName!=null &&
              this.activityName.equals(other.getActivityName()))) &&
            this.useProcessId == other.isUseProcessId() &&
            ((this.processId==null && other.getProcessId()==null) || 
             (this.processId!=null &&
              this.processId.equals(other.getProcessId()))) &&
            this.resourcesIncluded == other.getResourcesIncluded();
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
        if (getResourceId() != null) {
            _hashCode += getResourceId().hashCode();
        }
        _hashCode += (isUseActivityName() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getActivityName() != null) {
            _hashCode += getActivityName().hashCode();
        }
        _hashCode += (isUseProcessId() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getProcessId() != null) {
            _hashCode += getProcessId().hashCode();
        }
        _hashCode += getResourcesIncluded();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetNumberOfActivitiesAssignedToResource.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNumberOfActivitiesAssignedToResource"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourceId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useActivityName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseActivityName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activityName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useProcessId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseProcessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourcesIncluded");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourcesIncluded"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
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
