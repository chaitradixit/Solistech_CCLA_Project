/**
 * ExtendedWorkQActivity2.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ExtendedWorkQActivity2  implements java.io.Serializable {
    private java.lang.String nodeName;

    private java.lang.String description;

    private java.util.Calendar dueDate;

    private short priority;

    private short nodeId;

    private short embeddedProcessCount;

    private java.lang.String jobId;

    private java.lang.String processId;

    private double version;

    private java.lang.String processName;

    private java.lang.String resourceName;

    private java.lang.String associatedFile;

    private java.lang.String[] extendedField;

    private short jobRAGStatus;

    private java.lang.String jobRAGStatusImage;

    private java.lang.String jobState;

    private short activityStatus;

    public ExtendedWorkQActivity2() {
    }

    public ExtendedWorkQActivity2(
           java.lang.String nodeName,
           java.lang.String description,
           java.util.Calendar dueDate,
           short priority,
           short nodeId,
           short embeddedProcessCount,
           java.lang.String jobId,
           java.lang.String processId,
           double version,
           java.lang.String processName,
           java.lang.String resourceName,
           java.lang.String associatedFile,
           java.lang.String[] extendedField,
           short jobRAGStatus,
           java.lang.String jobRAGStatusImage,
           java.lang.String jobState,
           short activityStatus) {
           this.nodeName = nodeName;
           this.description = description;
           this.dueDate = dueDate;
           this.priority = priority;
           this.nodeId = nodeId;
           this.embeddedProcessCount = embeddedProcessCount;
           this.jobId = jobId;
           this.processId = processId;
           this.version = version;
           this.processName = processName;
           this.resourceName = resourceName;
           this.associatedFile = associatedFile;
           this.extendedField = extendedField;
           this.jobRAGStatus = jobRAGStatus;
           this.jobRAGStatusImage = jobRAGStatusImage;
           this.jobState = jobState;
           this.activityStatus = activityStatus;
    }


    /**
     * Gets the nodeName value for this ExtendedWorkQActivity2.
     * 
     * @return nodeName
     */
    public java.lang.String getNodeName() {
        return nodeName;
    }


    /**
     * Sets the nodeName value for this ExtendedWorkQActivity2.
     * 
     * @param nodeName
     */
    public void setNodeName(java.lang.String nodeName) {
        this.nodeName = nodeName;
    }


    /**
     * Gets the description value for this ExtendedWorkQActivity2.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ExtendedWorkQActivity2.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the dueDate value for this ExtendedWorkQActivity2.
     * 
     * @return dueDate
     */
    public java.util.Calendar getDueDate() {
        return dueDate;
    }


    /**
     * Sets the dueDate value for this ExtendedWorkQActivity2.
     * 
     * @param dueDate
     */
    public void setDueDate(java.util.Calendar dueDate) {
        this.dueDate = dueDate;
    }


    /**
     * Gets the priority value for this ExtendedWorkQActivity2.
     * 
     * @return priority
     */
    public short getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this ExtendedWorkQActivity2.
     * 
     * @param priority
     */
    public void setPriority(short priority) {
        this.priority = priority;
    }


    /**
     * Gets the nodeId value for this ExtendedWorkQActivity2.
     * 
     * @return nodeId
     */
    public short getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this ExtendedWorkQActivity2.
     * 
     * @param nodeId
     */
    public void setNodeId(short nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the embeddedProcessCount value for this ExtendedWorkQActivity2.
     * 
     * @return embeddedProcessCount
     */
    public short getEmbeddedProcessCount() {
        return embeddedProcessCount;
    }


    /**
     * Sets the embeddedProcessCount value for this ExtendedWorkQActivity2.
     * 
     * @param embeddedProcessCount
     */
    public void setEmbeddedProcessCount(short embeddedProcessCount) {
        this.embeddedProcessCount = embeddedProcessCount;
    }


    /**
     * Gets the jobId value for this ExtendedWorkQActivity2.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this ExtendedWorkQActivity2.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }


    /**
     * Gets the processId value for this ExtendedWorkQActivity2.
     * 
     * @return processId
     */
    public java.lang.String getProcessId() {
        return processId;
    }


    /**
     * Sets the processId value for this ExtendedWorkQActivity2.
     * 
     * @param processId
     */
    public void setProcessId(java.lang.String processId) {
        this.processId = processId;
    }


    /**
     * Gets the version value for this ExtendedWorkQActivity2.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this ExtendedWorkQActivity2.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the processName value for this ExtendedWorkQActivity2.
     * 
     * @return processName
     */
    public java.lang.String getProcessName() {
        return processName;
    }


    /**
     * Sets the processName value for this ExtendedWorkQActivity2.
     * 
     * @param processName
     */
    public void setProcessName(java.lang.String processName) {
        this.processName = processName;
    }


    /**
     * Gets the resourceName value for this ExtendedWorkQActivity2.
     * 
     * @return resourceName
     */
    public java.lang.String getResourceName() {
        return resourceName;
    }


    /**
     * Sets the resourceName value for this ExtendedWorkQActivity2.
     * 
     * @param resourceName
     */
    public void setResourceName(java.lang.String resourceName) {
        this.resourceName = resourceName;
    }


    /**
     * Gets the associatedFile value for this ExtendedWorkQActivity2.
     * 
     * @return associatedFile
     */
    public java.lang.String getAssociatedFile() {
        return associatedFile;
    }


    /**
     * Sets the associatedFile value for this ExtendedWorkQActivity2.
     * 
     * @param associatedFile
     */
    public void setAssociatedFile(java.lang.String associatedFile) {
        this.associatedFile = associatedFile;
    }


    /**
     * Gets the extendedField value for this ExtendedWorkQActivity2.
     * 
     * @return extendedField
     */
    public java.lang.String[] getExtendedField() {
        return extendedField;
    }


    /**
     * Sets the extendedField value for this ExtendedWorkQActivity2.
     * 
     * @param extendedField
     */
    public void setExtendedField(java.lang.String[] extendedField) {
        this.extendedField = extendedField;
    }


    /**
     * Gets the jobRAGStatus value for this ExtendedWorkQActivity2.
     * 
     * @return jobRAGStatus
     */
    public short getJobRAGStatus() {
        return jobRAGStatus;
    }


    /**
     * Sets the jobRAGStatus value for this ExtendedWorkQActivity2.
     * 
     * @param jobRAGStatus
     */
    public void setJobRAGStatus(short jobRAGStatus) {
        this.jobRAGStatus = jobRAGStatus;
    }


    /**
     * Gets the jobRAGStatusImage value for this ExtendedWorkQActivity2.
     * 
     * @return jobRAGStatusImage
     */
    public java.lang.String getJobRAGStatusImage() {
        return jobRAGStatusImage;
    }


    /**
     * Sets the jobRAGStatusImage value for this ExtendedWorkQActivity2.
     * 
     * @param jobRAGStatusImage
     */
    public void setJobRAGStatusImage(java.lang.String jobRAGStatusImage) {
        this.jobRAGStatusImage = jobRAGStatusImage;
    }


    /**
     * Gets the jobState value for this ExtendedWorkQActivity2.
     * 
     * @return jobState
     */
    public java.lang.String getJobState() {
        return jobState;
    }


    /**
     * Sets the jobState value for this ExtendedWorkQActivity2.
     * 
     * @param jobState
     */
    public void setJobState(java.lang.String jobState) {
        this.jobState = jobState;
    }


    /**
     * Gets the activityStatus value for this ExtendedWorkQActivity2.
     * 
     * @return activityStatus
     */
    public short getActivityStatus() {
        return activityStatus;
    }


    /**
     * Sets the activityStatus value for this ExtendedWorkQActivity2.
     * 
     * @param activityStatus
     */
    public void setActivityStatus(short activityStatus) {
        this.activityStatus = activityStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExtendedWorkQActivity2)) return false;
        ExtendedWorkQActivity2 other = (ExtendedWorkQActivity2) obj;
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
            ((this.processName==null && other.getProcessName()==null) || 
             (this.processName!=null &&
              this.processName.equals(other.getProcessName()))) &&
            ((this.resourceName==null && other.getResourceName()==null) || 
             (this.resourceName!=null &&
              this.resourceName.equals(other.getResourceName()))) &&
            ((this.associatedFile==null && other.getAssociatedFile()==null) || 
             (this.associatedFile!=null &&
              this.associatedFile.equals(other.getAssociatedFile()))) &&
            ((this.extendedField==null && other.getExtendedField()==null) || 
             (this.extendedField!=null &&
              java.util.Arrays.equals(this.extendedField, other.getExtendedField()))) &&
            this.jobRAGStatus == other.getJobRAGStatus() &&
            ((this.jobRAGStatusImage==null && other.getJobRAGStatusImage()==null) || 
             (this.jobRAGStatusImage!=null &&
              this.jobRAGStatusImage.equals(other.getJobRAGStatusImage()))) &&
            ((this.jobState==null && other.getJobState()==null) || 
             (this.jobState!=null &&
              this.jobState.equals(other.getJobState()))) &&
            this.activityStatus == other.getActivityStatus();
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
        if (getProcessName() != null) {
            _hashCode += getProcessName().hashCode();
        }
        if (getResourceName() != null) {
            _hashCode += getResourceName().hashCode();
        }
        if (getAssociatedFile() != null) {
            _hashCode += getAssociatedFile().hashCode();
        }
        if (getExtendedField() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExtendedField());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExtendedField(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getJobRAGStatus();
        if (getJobRAGStatusImage() != null) {
            _hashCode += getJobRAGStatusImage().hashCode();
        }
        if (getJobState() != null) {
            _hashCode += getJobState().hashCode();
        }
        _hashCode += getActivityStatus();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExtendedWorkQActivity2.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity2"));
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
        elemField.setFieldName("processName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"));
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
        elemField.setFieldName("associatedFile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AssociatedFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extendedField");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedField"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobRAGStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobRAGStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobRAGStatusImage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobRAGStatusImage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activityStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityStatus"));
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
