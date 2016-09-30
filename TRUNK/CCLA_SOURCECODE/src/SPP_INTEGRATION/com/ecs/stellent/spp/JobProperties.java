/**
 * JobProperties.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class JobProperties  implements java.io.Serializable {
    private java.lang.String primProcessId;

    private java.lang.String primProcessName;

    private double version;

    private java.lang.String creatorId;

    private java.lang.String creatorName;

    private java.util.Calendar createDate;

    private int timeSpentInSecs;

    private java.util.Calendar finishDate;

    private double cost;

    private short jobStatus;

    private int expectedDuration;

    private double expectedCost;

    private com.ecs.stellent.spp.Variable[] vars;

    private com.ecs.stellent.spp.ActivityHistory[] history;

    private com.ecs.stellent.spp.Notes[] notes;

    private int workingTimeSpentInSecs;

    public JobProperties() {
    }

    public JobProperties(
           java.lang.String primProcessId,
           java.lang.String primProcessName,
           double version,
           java.lang.String creatorId,
           java.lang.String creatorName,
           java.util.Calendar createDate,
           int timeSpentInSecs,
           java.util.Calendar finishDate,
           double cost,
           short jobStatus,
           int expectedDuration,
           double expectedCost,
           com.ecs.stellent.spp.Variable[] vars,
           com.ecs.stellent.spp.ActivityHistory[] history,
           com.ecs.stellent.spp.Notes[] notes,
           int workingTimeSpentInSecs) {
           this.primProcessId = primProcessId;
           this.primProcessName = primProcessName;
           this.version = version;
           this.creatorId = creatorId;
           this.creatorName = creatorName;
           this.createDate = createDate;
           this.timeSpentInSecs = timeSpentInSecs;
           this.finishDate = finishDate;
           this.cost = cost;
           this.jobStatus = jobStatus;
           this.expectedDuration = expectedDuration;
           this.expectedCost = expectedCost;
           this.vars = vars;
           this.history = history;
           this.notes = notes;
           this.workingTimeSpentInSecs = workingTimeSpentInSecs;
    }


    /**
     * Gets the primProcessId value for this JobProperties.
     * 
     * @return primProcessId
     */
    public java.lang.String getPrimProcessId() {
        return primProcessId;
    }


    /**
     * Sets the primProcessId value for this JobProperties.
     * 
     * @param primProcessId
     */
    public void setPrimProcessId(java.lang.String primProcessId) {
        this.primProcessId = primProcessId;
    }


    /**
     * Gets the primProcessName value for this JobProperties.
     * 
     * @return primProcessName
     */
    public java.lang.String getPrimProcessName() {
        return primProcessName;
    }


    /**
     * Sets the primProcessName value for this JobProperties.
     * 
     * @param primProcessName
     */
    public void setPrimProcessName(java.lang.String primProcessName) {
        this.primProcessName = primProcessName;
    }


    /**
     * Gets the version value for this JobProperties.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this JobProperties.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the creatorId value for this JobProperties.
     * 
     * @return creatorId
     */
    public java.lang.String getCreatorId() {
        return creatorId;
    }


    /**
     * Sets the creatorId value for this JobProperties.
     * 
     * @param creatorId
     */
    public void setCreatorId(java.lang.String creatorId) {
        this.creatorId = creatorId;
    }


    /**
     * Gets the creatorName value for this JobProperties.
     * 
     * @return creatorName
     */
    public java.lang.String getCreatorName() {
        return creatorName;
    }


    /**
     * Sets the creatorName value for this JobProperties.
     * 
     * @param creatorName
     */
    public void setCreatorName(java.lang.String creatorName) {
        this.creatorName = creatorName;
    }


    /**
     * Gets the createDate value for this JobProperties.
     * 
     * @return createDate
     */
    public java.util.Calendar getCreateDate() {
        return createDate;
    }


    /**
     * Sets the createDate value for this JobProperties.
     * 
     * @param createDate
     */
    public void setCreateDate(java.util.Calendar createDate) {
        this.createDate = createDate;
    }


    /**
     * Gets the timeSpentInSecs value for this JobProperties.
     * 
     * @return timeSpentInSecs
     */
    public int getTimeSpentInSecs() {
        return timeSpentInSecs;
    }


    /**
     * Sets the timeSpentInSecs value for this JobProperties.
     * 
     * @param timeSpentInSecs
     */
    public void setTimeSpentInSecs(int timeSpentInSecs) {
        this.timeSpentInSecs = timeSpentInSecs;
    }


    /**
     * Gets the finishDate value for this JobProperties.
     * 
     * @return finishDate
     */
    public java.util.Calendar getFinishDate() {
        return finishDate;
    }


    /**
     * Sets the finishDate value for this JobProperties.
     * 
     * @param finishDate
     */
    public void setFinishDate(java.util.Calendar finishDate) {
        this.finishDate = finishDate;
    }


    /**
     * Gets the cost value for this JobProperties.
     * 
     * @return cost
     */
    public double getCost() {
        return cost;
    }


    /**
     * Sets the cost value for this JobProperties.
     * 
     * @param cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }


    /**
     * Gets the jobStatus value for this JobProperties.
     * 
     * @return jobStatus
     */
    public short getJobStatus() {
        return jobStatus;
    }


    /**
     * Sets the jobStatus value for this JobProperties.
     * 
     * @param jobStatus
     */
    public void setJobStatus(short jobStatus) {
        this.jobStatus = jobStatus;
    }


    /**
     * Gets the expectedDuration value for this JobProperties.
     * 
     * @return expectedDuration
     */
    public int getExpectedDuration() {
        return expectedDuration;
    }


    /**
     * Sets the expectedDuration value for this JobProperties.
     * 
     * @param expectedDuration
     */
    public void setExpectedDuration(int expectedDuration) {
        this.expectedDuration = expectedDuration;
    }


    /**
     * Gets the expectedCost value for this JobProperties.
     * 
     * @return expectedCost
     */
    public double getExpectedCost() {
        return expectedCost;
    }


    /**
     * Sets the expectedCost value for this JobProperties.
     * 
     * @param expectedCost
     */
    public void setExpectedCost(double expectedCost) {
        this.expectedCost = expectedCost;
    }


    /**
     * Gets the vars value for this JobProperties.
     * 
     * @return vars
     */
    public com.ecs.stellent.spp.Variable[] getVars() {
        return vars;
    }


    /**
     * Sets the vars value for this JobProperties.
     * 
     * @param vars
     */
    public void setVars(com.ecs.stellent.spp.Variable[] vars) {
        this.vars = vars;
    }


    /**
     * Gets the history value for this JobProperties.
     * 
     * @return history
     */
    public com.ecs.stellent.spp.ActivityHistory[] getHistory() {
        return history;
    }


    /**
     * Sets the history value for this JobProperties.
     * 
     * @param history
     */
    public void setHistory(com.ecs.stellent.spp.ActivityHistory[] history) {
        this.history = history;
    }


    /**
     * Gets the notes value for this JobProperties.
     * 
     * @return notes
     */
    public com.ecs.stellent.spp.Notes[] getNotes() {
        return notes;
    }


    /**
     * Sets the notes value for this JobProperties.
     * 
     * @param notes
     */
    public void setNotes(com.ecs.stellent.spp.Notes[] notes) {
        this.notes = notes;
    }


    /**
     * Gets the workingTimeSpentInSecs value for this JobProperties.
     * 
     * @return workingTimeSpentInSecs
     */
    public int getWorkingTimeSpentInSecs() {
        return workingTimeSpentInSecs;
    }


    /**
     * Sets the workingTimeSpentInSecs value for this JobProperties.
     * 
     * @param workingTimeSpentInSecs
     */
    public void setWorkingTimeSpentInSecs(int workingTimeSpentInSecs) {
        this.workingTimeSpentInSecs = workingTimeSpentInSecs;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JobProperties)) return false;
        JobProperties other = (JobProperties) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.primProcessId==null && other.getPrimProcessId()==null) || 
             (this.primProcessId!=null &&
              this.primProcessId.equals(other.getPrimProcessId()))) &&
            ((this.primProcessName==null && other.getPrimProcessName()==null) || 
             (this.primProcessName!=null &&
              this.primProcessName.equals(other.getPrimProcessName()))) &&
            this.version == other.getVersion() &&
            ((this.creatorId==null && other.getCreatorId()==null) || 
             (this.creatorId!=null &&
              this.creatorId.equals(other.getCreatorId()))) &&
            ((this.creatorName==null && other.getCreatorName()==null) || 
             (this.creatorName!=null &&
              this.creatorName.equals(other.getCreatorName()))) &&
            ((this.createDate==null && other.getCreateDate()==null) || 
             (this.createDate!=null &&
              this.createDate.equals(other.getCreateDate()))) &&
            this.timeSpentInSecs == other.getTimeSpentInSecs() &&
            ((this.finishDate==null && other.getFinishDate()==null) || 
             (this.finishDate!=null &&
              this.finishDate.equals(other.getFinishDate()))) &&
            this.cost == other.getCost() &&
            this.jobStatus == other.getJobStatus() &&
            this.expectedDuration == other.getExpectedDuration() &&
            this.expectedCost == other.getExpectedCost() &&
            ((this.vars==null && other.getVars()==null) || 
             (this.vars!=null &&
              java.util.Arrays.equals(this.vars, other.getVars()))) &&
            ((this.history==null && other.getHistory()==null) || 
             (this.history!=null &&
              java.util.Arrays.equals(this.history, other.getHistory()))) &&
            ((this.notes==null && other.getNotes()==null) || 
             (this.notes!=null &&
              java.util.Arrays.equals(this.notes, other.getNotes()))) &&
            this.workingTimeSpentInSecs == other.getWorkingTimeSpentInSecs();
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
        if (getPrimProcessId() != null) {
            _hashCode += getPrimProcessId().hashCode();
        }
        if (getPrimProcessName() != null) {
            _hashCode += getPrimProcessName().hashCode();
        }
        _hashCode += new Double(getVersion()).hashCode();
        if (getCreatorId() != null) {
            _hashCode += getCreatorId().hashCode();
        }
        if (getCreatorName() != null) {
            _hashCode += getCreatorName().hashCode();
        }
        if (getCreateDate() != null) {
            _hashCode += getCreateDate().hashCode();
        }
        _hashCode += getTimeSpentInSecs();
        if (getFinishDate() != null) {
            _hashCode += getFinishDate().hashCode();
        }
        _hashCode += new Double(getCost()).hashCode();
        _hashCode += getJobStatus();
        _hashCode += getExpectedDuration();
        _hashCode += new Double(getExpectedCost()).hashCode();
        if (getVars() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getVars());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getVars(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getHistory() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getHistory());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getHistory(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNotes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNotes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNotes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getWorkingTimeSpentInSecs();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JobProperties.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("primProcessId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrimProcessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("primProcessName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrimProcessName"));
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
        elemField.setFieldName("creatorId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreatorId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creatorName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreatorName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeSpentInSecs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeSpentInSecs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("finishDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Cost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expectedDuration");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExpectedDuration"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expectedCost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExpectedCost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vars");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "vars"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("history");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "history"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "notes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Notes"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Notes"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workingTimeSpentInSecs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkingTimeSpentInSecs"));
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
