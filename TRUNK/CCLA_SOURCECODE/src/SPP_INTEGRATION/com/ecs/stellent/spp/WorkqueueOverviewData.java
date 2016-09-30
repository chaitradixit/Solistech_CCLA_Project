/**
 * WorkqueueOverviewData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class WorkqueueOverviewData  implements java.io.Serializable {
    private java.lang.String workqueueDefinitionName;

    private int individualWorkLoadTotalSeconds;

    private com.ecs.stellent.spp.TimePeriod individualWorkLoad;

    private int individualActivityCount;

    private int combinedWorkLoadTotalSeconds;

    private com.ecs.stellent.spp.TimePeriod combinedWorkLoad;

    private int combinedActivityCount;

    public WorkqueueOverviewData() {
    }

    public WorkqueueOverviewData(
           java.lang.String workqueueDefinitionName,
           int individualWorkLoadTotalSeconds,
           com.ecs.stellent.spp.TimePeriod individualWorkLoad,
           int individualActivityCount,
           int combinedWorkLoadTotalSeconds,
           com.ecs.stellent.spp.TimePeriod combinedWorkLoad,
           int combinedActivityCount) {
           this.workqueueDefinitionName = workqueueDefinitionName;
           this.individualWorkLoadTotalSeconds = individualWorkLoadTotalSeconds;
           this.individualWorkLoad = individualWorkLoad;
           this.individualActivityCount = individualActivityCount;
           this.combinedWorkLoadTotalSeconds = combinedWorkLoadTotalSeconds;
           this.combinedWorkLoad = combinedWorkLoad;
           this.combinedActivityCount = combinedActivityCount;
    }


    /**
     * Gets the workqueueDefinitionName value for this WorkqueueOverviewData.
     * 
     * @return workqueueDefinitionName
     */
    public java.lang.String getWorkqueueDefinitionName() {
        return workqueueDefinitionName;
    }


    /**
     * Sets the workqueueDefinitionName value for this WorkqueueOverviewData.
     * 
     * @param workqueueDefinitionName
     */
    public void setWorkqueueDefinitionName(java.lang.String workqueueDefinitionName) {
        this.workqueueDefinitionName = workqueueDefinitionName;
    }


    /**
     * Gets the individualWorkLoadTotalSeconds value for this WorkqueueOverviewData.
     * 
     * @return individualWorkLoadTotalSeconds
     */
    public int getIndividualWorkLoadTotalSeconds() {
        return individualWorkLoadTotalSeconds;
    }


    /**
     * Sets the individualWorkLoadTotalSeconds value for this WorkqueueOverviewData.
     * 
     * @param individualWorkLoadTotalSeconds
     */
    public void setIndividualWorkLoadTotalSeconds(int individualWorkLoadTotalSeconds) {
        this.individualWorkLoadTotalSeconds = individualWorkLoadTotalSeconds;
    }


    /**
     * Gets the individualWorkLoad value for this WorkqueueOverviewData.
     * 
     * @return individualWorkLoad
     */
    public com.ecs.stellent.spp.TimePeriod getIndividualWorkLoad() {
        return individualWorkLoad;
    }


    /**
     * Sets the individualWorkLoad value for this WorkqueueOverviewData.
     * 
     * @param individualWorkLoad
     */
    public void setIndividualWorkLoad(com.ecs.stellent.spp.TimePeriod individualWorkLoad) {
        this.individualWorkLoad = individualWorkLoad;
    }


    /**
     * Gets the individualActivityCount value for this WorkqueueOverviewData.
     * 
     * @return individualActivityCount
     */
    public int getIndividualActivityCount() {
        return individualActivityCount;
    }


    /**
     * Sets the individualActivityCount value for this WorkqueueOverviewData.
     * 
     * @param individualActivityCount
     */
    public void setIndividualActivityCount(int individualActivityCount) {
        this.individualActivityCount = individualActivityCount;
    }


    /**
     * Gets the combinedWorkLoadTotalSeconds value for this WorkqueueOverviewData.
     * 
     * @return combinedWorkLoadTotalSeconds
     */
    public int getCombinedWorkLoadTotalSeconds() {
        return combinedWorkLoadTotalSeconds;
    }


    /**
     * Sets the combinedWorkLoadTotalSeconds value for this WorkqueueOverviewData.
     * 
     * @param combinedWorkLoadTotalSeconds
     */
    public void setCombinedWorkLoadTotalSeconds(int combinedWorkLoadTotalSeconds) {
        this.combinedWorkLoadTotalSeconds = combinedWorkLoadTotalSeconds;
    }


    /**
     * Gets the combinedWorkLoad value for this WorkqueueOverviewData.
     * 
     * @return combinedWorkLoad
     */
    public com.ecs.stellent.spp.TimePeriod getCombinedWorkLoad() {
        return combinedWorkLoad;
    }


    /**
     * Sets the combinedWorkLoad value for this WorkqueueOverviewData.
     * 
     * @param combinedWorkLoad
     */
    public void setCombinedWorkLoad(com.ecs.stellent.spp.TimePeriod combinedWorkLoad) {
        this.combinedWorkLoad = combinedWorkLoad;
    }


    /**
     * Gets the combinedActivityCount value for this WorkqueueOverviewData.
     * 
     * @return combinedActivityCount
     */
    public int getCombinedActivityCount() {
        return combinedActivityCount;
    }


    /**
     * Sets the combinedActivityCount value for this WorkqueueOverviewData.
     * 
     * @param combinedActivityCount
     */
    public void setCombinedActivityCount(int combinedActivityCount) {
        this.combinedActivityCount = combinedActivityCount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WorkqueueOverviewData)) return false;
        WorkqueueOverviewData other = (WorkqueueOverviewData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.workqueueDefinitionName==null && other.getWorkqueueDefinitionName()==null) || 
             (this.workqueueDefinitionName!=null &&
              this.workqueueDefinitionName.equals(other.getWorkqueueDefinitionName()))) &&
            this.individualWorkLoadTotalSeconds == other.getIndividualWorkLoadTotalSeconds() &&
            ((this.individualWorkLoad==null && other.getIndividualWorkLoad()==null) || 
             (this.individualWorkLoad!=null &&
              this.individualWorkLoad.equals(other.getIndividualWorkLoad()))) &&
            this.individualActivityCount == other.getIndividualActivityCount() &&
            this.combinedWorkLoadTotalSeconds == other.getCombinedWorkLoadTotalSeconds() &&
            ((this.combinedWorkLoad==null && other.getCombinedWorkLoad()==null) || 
             (this.combinedWorkLoad!=null &&
              this.combinedWorkLoad.equals(other.getCombinedWorkLoad()))) &&
            this.combinedActivityCount == other.getCombinedActivityCount();
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
        if (getWorkqueueDefinitionName() != null) {
            _hashCode += getWorkqueueDefinitionName().hashCode();
        }
        _hashCode += getIndividualWorkLoadTotalSeconds();
        if (getIndividualWorkLoad() != null) {
            _hashCode += getIndividualWorkLoad().hashCode();
        }
        _hashCode += getIndividualActivityCount();
        _hashCode += getCombinedWorkLoadTotalSeconds();
        if (getCombinedWorkLoad() != null) {
            _hashCode += getCombinedWorkLoad().hashCode();
        }
        _hashCode += getCombinedActivityCount();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WorkqueueOverviewData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkqueueOverviewData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workqueueDefinitionName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkqueueDefinitionName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("individualWorkLoadTotalSeconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "IndividualWorkLoadTotalSeconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("individualWorkLoad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "IndividualWorkLoad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimePeriod"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("individualActivityCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "IndividualActivityCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("combinedWorkLoadTotalSeconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CombinedWorkLoadTotalSeconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("combinedWorkLoad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CombinedWorkLoad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimePeriod"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("combinedActivityCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CombinedActivityCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
