/**
 * ActivitiesForStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ActivitiesForStatus  implements java.io.Serializable {
    private java.lang.String nodeName;

    private java.lang.String description;

    private java.util.Calendar dueDate;

    private short priority;

    private short nodeId;

    private short embeddedProcessCount;

    private java.lang.String jobId;

    private java.lang.String processId;

    private double version;

    private short status;

    private java.util.Calendar timeTaken;

    private java.util.Calendar timeOffered;

    private java.lang.String resourcePerformingActivity;

    private short skill;

    private short security;

    private java.lang.String offeredResource;

    private int expectedDuration;

    private int liveActivityType;

    private java.util.Calendar monitoringDueDate;

    private short library;

    public ActivitiesForStatus() {
    }

    public ActivitiesForStatus(
           java.lang.String nodeName,
           java.lang.String description,
           java.util.Calendar dueDate,
           short priority,
           short nodeId,
           short embeddedProcessCount,
           java.lang.String jobId,
           java.lang.String processId,
           double version,
           short status,
           java.util.Calendar timeTaken,
           java.util.Calendar timeOffered,
           java.lang.String resourcePerformingActivity,
           short skill,
           short security,
           java.lang.String offeredResource,
           int expectedDuration,
           int liveActivityType,
           java.util.Calendar monitoringDueDate,
           short library) {
           this.nodeName = nodeName;
           this.description = description;
           this.dueDate = dueDate;
           this.priority = priority;
           this.nodeId = nodeId;
           this.embeddedProcessCount = embeddedProcessCount;
           this.jobId = jobId;
           this.processId = processId;
           this.version = version;
           this.status = status;
           this.timeTaken = timeTaken;
           this.timeOffered = timeOffered;
           this.resourcePerformingActivity = resourcePerformingActivity;
           this.skill = skill;
           this.security = security;
           this.offeredResource = offeredResource;
           this.expectedDuration = expectedDuration;
           this.liveActivityType = liveActivityType;
           this.monitoringDueDate = monitoringDueDate;
           this.library = library;
    }


    /**
     * Gets the nodeName value for this ActivitiesForStatus.
     * 
     * @return nodeName
     */
    public java.lang.String getNodeName() {
        return nodeName;
    }


    /**
     * Sets the nodeName value for this ActivitiesForStatus.
     * 
     * @param nodeName
     */
    public void setNodeName(java.lang.String nodeName) {
        this.nodeName = nodeName;
    }


    /**
     * Gets the description value for this ActivitiesForStatus.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ActivitiesForStatus.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the dueDate value for this ActivitiesForStatus.
     * 
     * @return dueDate
     */
    public java.util.Calendar getDueDate() {
        return dueDate;
    }


    /**
     * Sets the dueDate value for this ActivitiesForStatus.
     * 
     * @param dueDate
     */
    public void setDueDate(java.util.Calendar dueDate) {
        this.dueDate = dueDate;
    }


    /**
     * Gets the priority value for this ActivitiesForStatus.
     * 
     * @return priority
     */
    public short getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this ActivitiesForStatus.
     * 
     * @param priority
     */
    public void setPriority(short priority) {
        this.priority = priority;
    }


    /**
     * Gets the nodeId value for this ActivitiesForStatus.
     * 
     * @return nodeId
     */
    public short getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this ActivitiesForStatus.
     * 
     * @param nodeId
     */
    public void setNodeId(short nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the embeddedProcessCount value for this ActivitiesForStatus.
     * 
     * @return embeddedProcessCount
     */
    public short getEmbeddedProcessCount() {
        return embeddedProcessCount;
    }


    /**
     * Sets the embeddedProcessCount value for this ActivitiesForStatus.
     * 
     * @param embeddedProcessCount
     */
    public void setEmbeddedProcessCount(short embeddedProcessCount) {
        this.embeddedProcessCount = embeddedProcessCount;
    }


    /**
     * Gets the jobId value for this ActivitiesForStatus.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this ActivitiesForStatus.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }


    /**
     * Gets the processId value for this ActivitiesForStatus.
     * 
     * @return processId
     */
    public java.lang.String getProcessId() {
        return processId;
    }


    /**
     * Sets the processId value for this ActivitiesForStatus.
     * 
     * @param processId
     */
    public void setProcessId(java.lang.String processId) {
        this.processId = processId;
    }


    /**
     * Gets the version value for this ActivitiesForStatus.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this ActivitiesForStatus.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the status value for this ActivitiesForStatus.
     * 
     * @return status
     */
    public short getStatus() {
        return status;
    }


    /**
     * Sets the status value for this ActivitiesForStatus.
     * 
     * @param status
     */
    public void setStatus(short status) {
        this.status = status;
    }


    /**
     * Gets the timeTaken value for this ActivitiesForStatus.
     * 
     * @return timeTaken
     */
    public java.util.Calendar getTimeTaken() {
        return timeTaken;
    }


    /**
     * Sets the timeTaken value for this ActivitiesForStatus.
     * 
     * @param timeTaken
     */
    public void setTimeTaken(java.util.Calendar timeTaken) {
        this.timeTaken = timeTaken;
    }


    /**
     * Gets the timeOffered value for this ActivitiesForStatus.
     * 
     * @return timeOffered
     */
    public java.util.Calendar getTimeOffered() {
        return timeOffered;
    }


    /**
     * Sets the timeOffered value for this ActivitiesForStatus.
     * 
     * @param timeOffered
     */
    public void setTimeOffered(java.util.Calendar timeOffered) {
        this.timeOffered = timeOffered;
    }


    /**
     * Gets the resourcePerformingActivity value for this ActivitiesForStatus.
     * 
     * @return resourcePerformingActivity
     */
    public java.lang.String getResourcePerformingActivity() {
        return resourcePerformingActivity;
    }


    /**
     * Sets the resourcePerformingActivity value for this ActivitiesForStatus.
     * 
     * @param resourcePerformingActivity
     */
    public void setResourcePerformingActivity(java.lang.String resourcePerformingActivity) {
        this.resourcePerformingActivity = resourcePerformingActivity;
    }


    /**
     * Gets the skill value for this ActivitiesForStatus.
     * 
     * @return skill
     */
    public short getSkill() {
        return skill;
    }


    /**
     * Sets the skill value for this ActivitiesForStatus.
     * 
     * @param skill
     */
    public void setSkill(short skill) {
        this.skill = skill;
    }


    /**
     * Gets the security value for this ActivitiesForStatus.
     * 
     * @return security
     */
    public short getSecurity() {
        return security;
    }


    /**
     * Sets the security value for this ActivitiesForStatus.
     * 
     * @param security
     */
    public void setSecurity(short security) {
        this.security = security;
    }


    /**
     * Gets the offeredResource value for this ActivitiesForStatus.
     * 
     * @return offeredResource
     */
    public java.lang.String getOfferedResource() {
        return offeredResource;
    }


    /**
     * Sets the offeredResource value for this ActivitiesForStatus.
     * 
     * @param offeredResource
     */
    public void setOfferedResource(java.lang.String offeredResource) {
        this.offeredResource = offeredResource;
    }


    /**
     * Gets the expectedDuration value for this ActivitiesForStatus.
     * 
     * @return expectedDuration
     */
    public int getExpectedDuration() {
        return expectedDuration;
    }


    /**
     * Sets the expectedDuration value for this ActivitiesForStatus.
     * 
     * @param expectedDuration
     */
    public void setExpectedDuration(int expectedDuration) {
        this.expectedDuration = expectedDuration;
    }


    /**
     * Gets the liveActivityType value for this ActivitiesForStatus.
     * 
     * @return liveActivityType
     */
    public int getLiveActivityType() {
        return liveActivityType;
    }


    /**
     * Sets the liveActivityType value for this ActivitiesForStatus.
     * 
     * @param liveActivityType
     */
    public void setLiveActivityType(int liveActivityType) {
        this.liveActivityType = liveActivityType;
    }


    /**
     * Gets the monitoringDueDate value for this ActivitiesForStatus.
     * 
     * @return monitoringDueDate
     */
    public java.util.Calendar getMonitoringDueDate() {
        return monitoringDueDate;
    }


    /**
     * Sets the monitoringDueDate value for this ActivitiesForStatus.
     * 
     * @param monitoringDueDate
     */
    public void setMonitoringDueDate(java.util.Calendar monitoringDueDate) {
        this.monitoringDueDate = monitoringDueDate;
    }


    /**
     * Gets the library value for this ActivitiesForStatus.
     * 
     * @return library
     */
    public short getLibrary() {
        return library;
    }


    /**
     * Sets the library value for this ActivitiesForStatus.
     * 
     * @param library
     */
    public void setLibrary(short library) {
        this.library = library;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ActivitiesForStatus)) return false;
        ActivitiesForStatus other = (ActivitiesForStatus) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nodeName==null && other.getNodeName()==null) || 
             (this.nodeName!=null &&
              this.nodeName.equals(other.getNodeName()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.dueDate==null && other.getDueDate()==null) || 
             (this.dueDate!=null &&
              this.dueDate.equals(other.getDueDate()))) &&
            this.priority == other.getPriority() &&
            this.nodeId == other.getNodeId() &&
            this.embeddedProcessCount == other.getEmbeddedProcessCount() &&
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId()))) &&
            ((this.processId==null && other.getProcessId()==null) || 
             (this.processId!=null &&
              this.processId.equals(other.getProcessId()))) &&
            this.version == other.getVersion() &&
            this.status == other.getStatus() &&
            ((this.timeTaken==null && other.getTimeTaken()==null) || 
             (this.timeTaken!=null &&
              this.timeTaken.equals(other.getTimeTaken()))) &&
            ((this.timeOffered==null && other.getTimeOffered()==null) || 
             (this.timeOffered!=null &&
              this.timeOffered.equals(other.getTimeOffered()))) &&
            ((this.resourcePerformingActivity==null && other.getResourcePerformingActivity()==null) || 
             (this.resourcePerformingActivity!=null &&
              this.resourcePerformingActivity.equals(other.getResourcePerformingActivity()))) &&
            this.skill == other.getSkill() &&
            this.security == other.getSecurity() &&
            ((this.offeredResource==null && other.getOfferedResource()==null) || 
             (this.offeredResource!=null &&
              this.offeredResource.equals(other.getOfferedResource()))) &&
            this.expectedDuration == other.getExpectedDuration() &&
            this.liveActivityType == other.getLiveActivityType() &&
            ((this.monitoringDueDate==null && other.getMonitoringDueDate()==null) || 
             (this.monitoringDueDate!=null &&
              this.monitoringDueDate.equals(other.getMonitoringDueDate()))) &&
            this.library == other.getLibrary();
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
        if (getNodeName() != null) {
            _hashCode += getNodeName().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getDueDate() != null) {
            _hashCode += getDueDate().hashCode();
        }
        _hashCode += getPriority();
        _hashCode += getNodeId();
        _hashCode += getEmbeddedProcessCount();
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        if (getProcessId() != null) {
            _hashCode += getProcessId().hashCode();
        }
        _hashCode += new Double(getVersion()).hashCode();
        _hashCode += getStatus();
        if (getTimeTaken() != null) {
            _hashCode += getTimeTaken().hashCode();
        }
        if (getTimeOffered() != null) {
            _hashCode += getTimeOffered().hashCode();
        }
        if (getResourcePerformingActivity() != null) {
            _hashCode += getResourcePerformingActivity().hashCode();
        }
        _hashCode += getSkill();
        _hashCode += getSecurity();
        if (getOfferedResource() != null) {
            _hashCode += getOfferedResource().hashCode();
        }
        _hashCode += getExpectedDuration();
        _hashCode += getLiveActivityType();
        if (getMonitoringDueDate() != null) {
            _hashCode += getMonitoringDueDate().hashCode();
        }
        _hashCode += getLibrary();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ActivitiesForStatus.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("priority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Priority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("embeddedProcessCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeTaken");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeTaken"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeOffered");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeOffered"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourcePerformingActivity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourcePerformingActivity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("skill");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Skill"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("security");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Security"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offeredResource");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OfferedResource"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expectedDuration");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExpectedDuration"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("liveActivityType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LiveActivityType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("monitoringDueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MonitoringDueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("library");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Library"));
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
