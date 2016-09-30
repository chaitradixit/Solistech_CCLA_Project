/**
 * GetWorkqueueOverviewForResource.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetWorkqueueOverviewForResource  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String resourceId;

    private boolean getIndividualWorkLoad;

    private boolean getCombinedWorkLoad;

    private boolean getWorkqueueDefinitionWorkLoads;

    private boolean useStartDate;

    private java.util.Calendar startDate;

    private boolean useEndDate;

    private java.util.Calendar endDate;

    public GetWorkqueueOverviewForResource() {
    }

    public GetWorkqueueOverviewForResource(
           java.lang.String sessionId,
           java.lang.String resourceId,
           boolean getIndividualWorkLoad,
           boolean getCombinedWorkLoad,
           boolean getWorkqueueDefinitionWorkLoads,
           boolean useStartDate,
           java.util.Calendar startDate,
           boolean useEndDate,
           java.util.Calendar endDate) {
           this.sessionId = sessionId;
           this.resourceId = resourceId;
           this.getIndividualWorkLoad = getIndividualWorkLoad;
           this.getCombinedWorkLoad = getCombinedWorkLoad;
           this.getWorkqueueDefinitionWorkLoads = getWorkqueueDefinitionWorkLoads;
           this.useStartDate = useStartDate;
           this.startDate = startDate;
           this.useEndDate = useEndDate;
           this.endDate = endDate;
    }


    /**
     * Gets the sessionId value for this GetWorkqueueOverviewForResource.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetWorkqueueOverviewForResource.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the resourceId value for this GetWorkqueueOverviewForResource.
     * 
     * @return resourceId
     */
    public java.lang.String getResourceId() {
        return resourceId;
    }


    /**
     * Sets the resourceId value for this GetWorkqueueOverviewForResource.
     * 
     * @param resourceId
     */
    public void setResourceId(java.lang.String resourceId) {
        this.resourceId = resourceId;
    }


    /**
     * Gets the getIndividualWorkLoad value for this GetWorkqueueOverviewForResource.
     * 
     * @return getIndividualWorkLoad
     */
    public boolean isGetIndividualWorkLoad() {
        return getIndividualWorkLoad;
    }


    /**
     * Sets the getIndividualWorkLoad value for this GetWorkqueueOverviewForResource.
     * 
     * @param getIndividualWorkLoad
     */
    public void setGetIndividualWorkLoad(boolean getIndividualWorkLoad) {
        this.getIndividualWorkLoad = getIndividualWorkLoad;
    }


    /**
     * Gets the getCombinedWorkLoad value for this GetWorkqueueOverviewForResource.
     * 
     * @return getCombinedWorkLoad
     */
    public boolean isGetCombinedWorkLoad() {
        return getCombinedWorkLoad;
    }


    /**
     * Sets the getCombinedWorkLoad value for this GetWorkqueueOverviewForResource.
     * 
     * @param getCombinedWorkLoad
     */
    public void setGetCombinedWorkLoad(boolean getCombinedWorkLoad) {
        this.getCombinedWorkLoad = getCombinedWorkLoad;
    }


    /**
     * Gets the getWorkqueueDefinitionWorkLoads value for this GetWorkqueueOverviewForResource.
     * 
     * @return getWorkqueueDefinitionWorkLoads
     */
    public boolean isGetWorkqueueDefinitionWorkLoads() {
        return getWorkqueueDefinitionWorkLoads;
    }


    /**
     * Sets the getWorkqueueDefinitionWorkLoads value for this GetWorkqueueOverviewForResource.
     * 
     * @param getWorkqueueDefinitionWorkLoads
     */
    public void setGetWorkqueueDefinitionWorkLoads(boolean getWorkqueueDefinitionWorkLoads) {
        this.getWorkqueueDefinitionWorkLoads = getWorkqueueDefinitionWorkLoads;
    }


    /**
     * Gets the useStartDate value for this GetWorkqueueOverviewForResource.
     * 
     * @return useStartDate
     */
    public boolean isUseStartDate() {
        return useStartDate;
    }


    /**
     * Sets the useStartDate value for this GetWorkqueueOverviewForResource.
     * 
     * @param useStartDate
     */
    public void setUseStartDate(boolean useStartDate) {
        this.useStartDate = useStartDate;
    }


    /**
     * Gets the startDate value for this GetWorkqueueOverviewForResource.
     * 
     * @return startDate
     */
    public java.util.Calendar getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this GetWorkqueueOverviewForResource.
     * 
     * @param startDate
     */
    public void setStartDate(java.util.Calendar startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the useEndDate value for this GetWorkqueueOverviewForResource.
     * 
     * @return useEndDate
     */
    public boolean isUseEndDate() {
        return useEndDate;
    }


    /**
     * Sets the useEndDate value for this GetWorkqueueOverviewForResource.
     * 
     * @param useEndDate
     */
    public void setUseEndDate(boolean useEndDate) {
        this.useEndDate = useEndDate;
    }


    /**
     * Gets the endDate value for this GetWorkqueueOverviewForResource.
     * 
     * @return endDate
     */
    public java.util.Calendar getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this GetWorkqueueOverviewForResource.
     * 
     * @param endDate
     */
    public void setEndDate(java.util.Calendar endDate) {
        this.endDate = endDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetWorkqueueOverviewForResource)) return false;
        GetWorkqueueOverviewForResource other = (GetWorkqueueOverviewForResource) obj;
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
            this.getIndividualWorkLoad == other.isGetIndividualWorkLoad() &&
            this.getCombinedWorkLoad == other.isGetCombinedWorkLoad() &&
            this.getWorkqueueDefinitionWorkLoads == other.isGetWorkqueueDefinitionWorkLoads() &&
            this.useStartDate == other.isUseStartDate() &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            this.useEndDate == other.isUseEndDate() &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate())));
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
        _hashCode += (isGetIndividualWorkLoad() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isGetCombinedWorkLoad() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isGetWorkqueueDefinitionWorkLoads() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUseStartDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        _hashCode += (isUseEndDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetWorkqueueOverviewForResource.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkqueueOverviewForResource"));
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
        elemField.setFieldName("getIndividualWorkLoad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetIndividualWorkLoad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getCombinedWorkLoad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetCombinedWorkLoad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getWorkqueueDefinitionWorkLoads");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkqueueDefinitionWorkLoads"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useStartDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useEndDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseEndDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EndDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
