/**
 * ExtendedJobDetails2.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ExtendedJobDetails2  implements java.io.Serializable {
    private java.lang.String jobId;

    private java.lang.String creator;

    private java.util.Calendar creationTime;

    private java.util.Calendar finishTime;

    private java.lang.String primProcess;

    private short completed;

    private java.lang.String serverName;

    private double version;

    private short priority;

    private java.lang.String[] workQueueDefnField;

    private short jobType;

    private java.lang.String caseRef;

    public ExtendedJobDetails2() {
    }

    public ExtendedJobDetails2(
           java.lang.String jobId,
           java.lang.String creator,
           java.util.Calendar creationTime,
           java.util.Calendar finishTime,
           java.lang.String primProcess,
           short completed,
           java.lang.String serverName,
           double version,
           short priority,
           java.lang.String[] workQueueDefnField,
           short jobType,
           java.lang.String caseRef) {
           this.jobId = jobId;
           this.creator = creator;
           this.creationTime = creationTime;
           this.finishTime = finishTime;
           this.primProcess = primProcess;
           this.completed = completed;
           this.serverName = serverName;
           this.version = version;
           this.priority = priority;
           this.workQueueDefnField = workQueueDefnField;
           this.jobType = jobType;
           this.caseRef = caseRef;
    }


    /**
     * Gets the jobId value for this ExtendedJobDetails2.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this ExtendedJobDetails2.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }


    /**
     * Gets the creator value for this ExtendedJobDetails2.
     * 
     * @return creator
     */
    public java.lang.String getCreator() {
        return creator;
    }


    /**
     * Sets the creator value for this ExtendedJobDetails2.
     * 
     * @param creator
     */
    public void setCreator(java.lang.String creator) {
        this.creator = creator;
    }


    /**
     * Gets the creationTime value for this ExtendedJobDetails2.
     * 
     * @return creationTime
     */
    public java.util.Calendar getCreationTime() {
        return creationTime;
    }


    /**
     * Sets the creationTime value for this ExtendedJobDetails2.
     * 
     * @param creationTime
     */
    public void setCreationTime(java.util.Calendar creationTime) {
        this.creationTime = creationTime;
    }


    /**
     * Gets the finishTime value for this ExtendedJobDetails2.
     * 
     * @return finishTime
     */
    public java.util.Calendar getFinishTime() {
        return finishTime;
    }


    /**
     * Sets the finishTime value for this ExtendedJobDetails2.
     * 
     * @param finishTime
     */
    public void setFinishTime(java.util.Calendar finishTime) {
        this.finishTime = finishTime;
    }


    /**
     * Gets the primProcess value for this ExtendedJobDetails2.
     * 
     * @return primProcess
     */
    public java.lang.String getPrimProcess() {
        return primProcess;
    }


    /**
     * Sets the primProcess value for this ExtendedJobDetails2.
     * 
     * @param primProcess
     */
    public void setPrimProcess(java.lang.String primProcess) {
        this.primProcess = primProcess;
    }


    /**
     * Gets the completed value for this ExtendedJobDetails2.
     * 
     * @return completed
     */
    public short getCompleted() {
        return completed;
    }


    /**
     * Sets the completed value for this ExtendedJobDetails2.
     * 
     * @param completed
     */
    public void setCompleted(short completed) {
        this.completed = completed;
    }


    /**
     * Gets the serverName value for this ExtendedJobDetails2.
     * 
     * @return serverName
     */
    public java.lang.String getServerName() {
        return serverName;
    }


    /**
     * Sets the serverName value for this ExtendedJobDetails2.
     * 
     * @param serverName
     */
    public void setServerName(java.lang.String serverName) {
        this.serverName = serverName;
    }


    /**
     * Gets the version value for this ExtendedJobDetails2.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this ExtendedJobDetails2.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the priority value for this ExtendedJobDetails2.
     * 
     * @return priority
     */
    public short getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this ExtendedJobDetails2.
     * 
     * @param priority
     */
    public void setPriority(short priority) {
        this.priority = priority;
    }


    /**
     * Gets the workQueueDefnField value for this ExtendedJobDetails2.
     * 
     * @return workQueueDefnField
     */
    public java.lang.String[] getWorkQueueDefnField() {
        return workQueueDefnField;
    }


    /**
     * Sets the workQueueDefnField value for this ExtendedJobDetails2.
     * 
     * @param workQueueDefnField
     */
    public void setWorkQueueDefnField(java.lang.String[] workQueueDefnField) {
        this.workQueueDefnField = workQueueDefnField;
    }


    /**
     * Gets the jobType value for this ExtendedJobDetails2.
     * 
     * @return jobType
     */
    public short getJobType() {
        return jobType;
    }


    /**
     * Sets the jobType value for this ExtendedJobDetails2.
     * 
     * @param jobType
     */
    public void setJobType(short jobType) {
        this.jobType = jobType;
    }


    /**
     * Gets the caseRef value for this ExtendedJobDetails2.
     * 
     * @return caseRef
     */
    public java.lang.String getCaseRef() {
        return caseRef;
    }


    /**
     * Sets the caseRef value for this ExtendedJobDetails2.
     * 
     * @param caseRef
     */
    public void setCaseRef(java.lang.String caseRef) {
        this.caseRef = caseRef;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExtendedJobDetails2)) return false;
        ExtendedJobDetails2 other = (ExtendedJobDetails2) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId()))) &&
            ((this.creator==null && other.getCreator()==null) || 
             (this.creator!=null &&
              this.creator.equals(other.getCreator()))) &&
            ((this.creationTime==null && other.getCreationTime()==null) || 
             (this.creationTime!=null &&
              this.creationTime.equals(other.getCreationTime()))) &&
            ((this.finishTime==null && other.getFinishTime()==null) || 
             (this.finishTime!=null &&
              this.finishTime.equals(other.getFinishTime()))) &&
            ((this.primProcess==null && other.getPrimProcess()==null) || 
             (this.primProcess!=null &&
              this.primProcess.equals(other.getPrimProcess()))) &&
            this.completed == other.getCompleted() &&
            ((this.serverName==null && other.getServerName()==null) || 
             (this.serverName!=null &&
              this.serverName.equals(other.getServerName()))) &&
            this.version == other.getVersion() &&
            this.priority == other.getPriority() &&
            ((this.workQueueDefnField==null && other.getWorkQueueDefnField()==null) || 
             (this.workQueueDefnField!=null &&
              java.util.Arrays.equals(this.workQueueDefnField, other.getWorkQueueDefnField()))) &&
            this.jobType == other.getJobType() &&
            ((this.caseRef==null && other.getCaseRef()==null) || 
             (this.caseRef!=null &&
              this.caseRef.equals(other.getCaseRef())));
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
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        if (getCreator() != null) {
            _hashCode += getCreator().hashCode();
        }
        if (getCreationTime() != null) {
            _hashCode += getCreationTime().hashCode();
        }
        if (getFinishTime() != null) {
            _hashCode += getFinishTime().hashCode();
        }
        if (getPrimProcess() != null) {
            _hashCode += getPrimProcess().hashCode();
        }
        _hashCode += getCompleted();
        if (getServerName() != null) {
            _hashCode += getServerName().hashCode();
        }
        _hashCode += new Double(getVersion()).hashCode();
        _hashCode += getPriority();
        if (getWorkQueueDefnField() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWorkQueueDefnField());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getWorkQueueDefnField(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getJobType();
        if (getCaseRef() != null) {
            _hashCode += getCaseRef().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExtendedJobDetails2.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobDetails2"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Creator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("finishTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("primProcess");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrimProcess"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("completed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Completed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serverName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ServerName"));
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
        elemField.setFieldName("priority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Priority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workQueueDefnField");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueueDefnField"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("caseRef");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CaseRef"));
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
