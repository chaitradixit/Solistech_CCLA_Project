/**
 * CNJActivityDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CNJActivityDetails  implements java.io.Serializable {
    private java.lang.String nodeName;

    private java.lang.String description;

    private java.util.Calendar dueDate;

    private short priority;

    private short nodeId;

    private short embeddedProcessCount;

    private java.lang.String processId;

    private double version;

    private java.lang.String processName;

    private java.lang.String resourceName;

    private java.lang.String associatedFile;

    private java.lang.String resourceId;

    private boolean multipleResources;

    public CNJActivityDetails() {
    }

    public CNJActivityDetails(
           java.lang.String nodeName,
           java.lang.String description,
           java.util.Calendar dueDate,
           short priority,
           short nodeId,
           short embeddedProcessCount,
           java.lang.String processId,
           double version,
           java.lang.String processName,
           java.lang.String resourceName,
           java.lang.String associatedFile,
           java.lang.String resourceId,
           boolean multipleResources) {
           this.nodeName = nodeName;
           this.description = description;
           this.dueDate = dueDate;
           this.priority = priority;
           this.nodeId = nodeId;
           this.embeddedProcessCount = embeddedProcessCount;
           this.processId = processId;
           this.version = version;
           this.processName = processName;
           this.resourceName = resourceName;
           this.associatedFile = associatedFile;
           this.resourceId = resourceId;
           this.multipleResources = multipleResources;
    }


    /**
     * Gets the nodeName value for this CNJActivityDetails.
     * 
     * @return nodeName
     */
    public java.lang.String getNodeName() {
        return nodeName;
    }


    /**
     * Sets the nodeName value for this CNJActivityDetails.
     * 
     * @param nodeName
     */
    public void setNodeName(java.lang.String nodeName) {
        this.nodeName = nodeName;
    }


    /**
     * Gets the description value for this CNJActivityDetails.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this CNJActivityDetails.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the dueDate value for this CNJActivityDetails.
     * 
     * @return dueDate
     */
    public java.util.Calendar getDueDate() {
        return dueDate;
    }


    /**
     * Sets the dueDate value for this CNJActivityDetails.
     * 
     * @param dueDate
     */
    public void setDueDate(java.util.Calendar dueDate) {
        this.dueDate = dueDate;
    }


    /**
     * Gets the priority value for this CNJActivityDetails.
     * 
     * @return priority
     */
    public short getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this CNJActivityDetails.
     * 
     * @param priority
     */
    public void setPriority(short priority) {
        this.priority = priority;
    }


    /**
     * Gets the nodeId value for this CNJActivityDetails.
     * 
     * @return nodeId
     */
    public short getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this CNJActivityDetails.
     * 
     * @param nodeId
     */
    public void setNodeId(short nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the embeddedProcessCount value for this CNJActivityDetails.
     * 
     * @return embeddedProcessCount
     */
    public short getEmbeddedProcessCount() {
        return embeddedProcessCount;
    }


    /**
     * Sets the embeddedProcessCount value for this CNJActivityDetails.
     * 
     * @param embeddedProcessCount
     */
    public void setEmbeddedProcessCount(short embeddedProcessCount) {
        this.embeddedProcessCount = embeddedProcessCount;
    }


    /**
     * Gets the processId value for this CNJActivityDetails.
     * 
     * @return processId
     */
    public java.lang.String getProcessId() {
        return processId;
    }


    /**
     * Sets the processId value for this CNJActivityDetails.
     * 
     * @param processId
     */
    public void setProcessId(java.lang.String processId) {
        this.processId = processId;
    }


    /**
     * Gets the version value for this CNJActivityDetails.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this CNJActivityDetails.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the processName value for this CNJActivityDetails.
     * 
     * @return processName
     */
    public java.lang.String getProcessName() {
        return processName;
    }


    /**
     * Sets the processName value for this CNJActivityDetails.
     * 
     * @param processName
     */
    public void setProcessName(java.lang.String processName) {
        this.processName = processName;
    }


    /**
     * Gets the resourceName value for this CNJActivityDetails.
     * 
     * @return resourceName
     */
    public java.lang.String getResourceName() {
        return resourceName;
    }


    /**
     * Sets the resourceName value for this CNJActivityDetails.
     * 
     * @param resourceName
     */
    public void setResourceName(java.lang.String resourceName) {
        this.resourceName = resourceName;
    }


    /**
     * Gets the associatedFile value for this CNJActivityDetails.
     * 
     * @return associatedFile
     */
    public java.lang.String getAssociatedFile() {
        return associatedFile;
    }


    /**
     * Sets the associatedFile value for this CNJActivityDetails.
     * 
     * @param associatedFile
     */
    public void setAssociatedFile(java.lang.String associatedFile) {
        this.associatedFile = associatedFile;
    }


    /**
     * Gets the resourceId value for this CNJActivityDetails.
     * 
     * @return resourceId
     */
    public java.lang.String getResourceId() {
        return resourceId;
    }


    /**
     * Sets the resourceId value for this CNJActivityDetails.
     * 
     * @param resourceId
     */
    public void setResourceId(java.lang.String resourceId) {
        this.resourceId = resourceId;
    }


    /**
     * Gets the multipleResources value for this CNJActivityDetails.
     * 
     * @return multipleResources
     */
    public boolean isMultipleResources() {
        return multipleResources;
    }


    /**
     * Sets the multipleResources value for this CNJActivityDetails.
     * 
     * @param multipleResources
     */
    public void setMultipleResources(boolean multipleResources) {
        this.multipleResources = multipleResources;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CNJActivityDetails)) return false;
        CNJActivityDetails other = (CNJActivityDetails) obj;
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
            ((this.resourceId==null && other.getResourceId()==null) || 
             (this.resourceId!=null &&
              this.resourceId.equals(other.getResourceId()))) &&
            this.multipleResources == other.isMultipleResources();
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
        if (getResourceId() != null) {
            _hashCode += getResourceId().hashCode();
        }
        _hashCode += (isMultipleResources() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CNJActivityDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CNJActivityDetails"));
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
        elemField.setFieldName("resourceId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("multipleResources");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MultipleResources"));
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
