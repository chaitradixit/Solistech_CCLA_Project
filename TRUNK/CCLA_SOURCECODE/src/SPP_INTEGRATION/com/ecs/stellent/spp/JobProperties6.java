/**
 * JobProperties6.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class JobProperties6  implements java.io.Serializable {
    private java.lang.String primProcessId;

    private java.lang.String primProcessName;

    private short primProcessType;

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

    private com.ecs.stellent.spp.ActivityHistory2[] history;

    private com.ecs.stellent.spp.Notes[] notes;

    private int workingTimeSpentInSecs;

    private java.lang.String jobOwnerID;

    private java.lang.String holdTime;

    private java.lang.String activationTime;

    private java.util.Calendar expectedFinishDate;

    private java.lang.String jobState;

    private short jobPercent;

    private java.lang.String associatedCaseID;

    private double budget;

    private com.ecs.stellent.spp.Milestones[] milestones;

    private double jobSpend;

    private com.ecs.stellent.spp.Events[] events;

    private com.ecs.stellent.spp.RoleInfo[] roles;

    private short jobType;

    private java.lang.String caseRef;

    private java.lang.String jobId;

    public JobProperties6() {
    }

    public JobProperties6(
           java.lang.String primProcessId,
           java.lang.String primProcessName,
           short primProcessType,
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
           com.ecs.stellent.spp.ActivityHistory2[] history,
           com.ecs.stellent.spp.Notes[] notes,
           int workingTimeSpentInSecs,
           java.lang.String jobOwnerID,
           java.lang.String holdTime,
           java.lang.String activationTime,
           java.util.Calendar expectedFinishDate,
           java.lang.String jobState,
           short jobPercent,
           java.lang.String associatedCaseID,
           double budget,
           com.ecs.stellent.spp.Milestones[] milestones,
           double jobSpend,
           com.ecs.stellent.spp.Events[] events,
           com.ecs.stellent.spp.RoleInfo[] roles,
           short jobType,
           java.lang.String caseRef,
           java.lang.String jobId) {
           this.primProcessId = primProcessId;
           this.primProcessName = primProcessName;
           this.primProcessType = primProcessType;
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
           this.jobOwnerID = jobOwnerID;
           this.holdTime = holdTime;
           this.activationTime = activationTime;
           this.expectedFinishDate = expectedFinishDate;
           this.jobState = jobState;
           this.jobPercent = jobPercent;
           this.associatedCaseID = associatedCaseID;
           this.budget = budget;
           this.milestones = milestones;
           this.jobSpend = jobSpend;
           this.events = events;
           this.roles = roles;
           this.jobType = jobType;
           this.caseRef = caseRef;
           this.jobId = jobId;
    }


    /**
     * Gets the primProcessId value for this JobProperties6.
     * 
     * @return primProcessId
     */
    public java.lang.String getPrimProcessId() {
        return primProcessId;
    }


    /**
     * Sets the primProcessId value for this JobProperties6.
     * 
     * @param primProcessId
     */
    public void setPrimProcessId(java.lang.String primProcessId) {
        this.primProcessId = primProcessId;
    }


    /**
     * Gets the primProcessName value for this JobProperties6.
     * 
     * @return primProcessName
     */
    public java.lang.String getPrimProcessName() {
        return primProcessName;
    }


    /**
     * Sets the primProcessName value for this JobProperties6.
     * 
     * @param primProcessName
     */
    public void setPrimProcessName(java.lang.String primProcessName) {
        this.primProcessName = primProcessName;
    }


    /**
     * Gets the primProcessType value for this JobProperties6.
     * 
     * @return primProcessType
     */
    public short getPrimProcessType() {
        return primProcessType;
    }


    /**
     * Sets the primProcessType value for this JobProperties6.
     * 
     * @param primProcessType
     */
    public void setPrimProcessType(short primProcessType) {
        this.primProcessType = primProcessType;
    }


    /**
     * Gets the version value for this JobProperties6.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this JobProperties6.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the creatorId value for this JobProperties6.
     * 
     * @return creatorId
     */
    public java.lang.String getCreatorId() {
        return creatorId;
    }


    /**
     * Sets the creatorId value for this JobProperties6.
     * 
     * @param creatorId
     */
    public void setCreatorId(java.lang.String creatorId) {
        this.creatorId = creatorId;
    }


    /**
     * Gets the creatorName value for this JobProperties6.
     * 
     * @return creatorName
     */
    public java.lang.String getCreatorName() {
        return creatorName;
    }


    /**
     * Sets the creatorName value for this JobProperties6.
     * 
     * @param creatorName
     */
    public void setCreatorName(java.lang.String creatorName) {
        this.creatorName = creatorName;
    }


    /**
     * Gets the createDate value for this JobProperties6.
     * 
     * @return createDate
     */
    public java.util.Calendar getCreateDate() {
        return createDate;
    }


    /**
     * Sets the createDate value for this JobProperties6.
     * 
     * @param createDate
     */
    public void setCreateDate(java.util.Calendar createDate) {
        this.createDate = createDate;
    }


    /**
     * Gets the timeSpentInSecs value for this JobProperties6.
     * 
     * @return timeSpentInSecs
     */
    public int getTimeSpentInSecs() {
        return timeSpentInSecs;
    }


    /**
     * Sets the timeSpentInSecs value for this JobProperties6.
     * 
     * @param timeSpentInSecs
     */
    public void setTimeSpentInSecs(int timeSpentInSecs) {
        this.timeSpentInSecs = timeSpentInSecs;
    }


    /**
     * Gets the finishDate value for this JobProperties6.
     * 
     * @return finishDate
     */
    public java.util.Calendar getFinishDate() {
        return finishDate;
    }


    /**
     * Sets the finishDate value for this JobProperties6.
     * 
     * @param finishDate
     */
    public void setFinishDate(java.util.Calendar finishDate) {
        this.finishDate = finishDate;
    }


    /**
     * Gets the cost value for this JobProperties6.
     * 
     * @return cost
     */
    public double getCost() {
        return cost;
    }


    /**
     * Sets the cost value for this JobProperties6.
     * 
     * @param cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }


    /**
     * Gets the jobStatus value for this JobProperties6.
     * 
     * @return jobStatus
     */
    public short getJobStatus() {
        return jobStatus;
    }


    /**
     * Sets the jobStatus value for this JobProperties6.
     * 
     * @param jobStatus
     */
    public void setJobStatus(short jobStatus) {
        this.jobStatus = jobStatus;
    }


    /**
     * Gets the expectedDuration value for this JobProperties6.
     * 
     * @return expectedDuration
     */
    public int getExpectedDuration() {
        return expectedDuration;
    }


    /**
     * Sets the expectedDuration value for this JobProperties6.
     * 
     * @param expectedDuration
     */
    public void setExpectedDuration(int expectedDuration) {
        this.expectedDuration = expectedDuration;
    }


    /**
     * Gets the expectedCost value for this JobProperties6.
     * 
     * @return expectedCost
     */
    public double getExpectedCost() {
        return expectedCost;
    }


    /**
     * Sets the expectedCost value for this JobProperties6.
     * 
     * @param expectedCost
     */
    public void setExpectedCost(double expectedCost) {
        this.expectedCost = expectedCost;
    }


    /**
     * Gets the vars value for this JobProperties6.
     * 
     * @return vars
     */
    public com.ecs.stellent.spp.Variable[] getVars() {
        return vars;
    }


    /**
     * Sets the vars value for this JobProperties6.
     * 
     * @param vars
     */
    public void setVars(com.ecs.stellent.spp.Variable[] vars) {
        this.vars = vars;
    }


    /**
     * Gets the history value for this JobProperties6.
     * 
     * @return history
     */
    public com.ecs.stellent.spp.ActivityHistory2[] getHistory() {
        return history;
    }


    /**
     * Sets the history value for this JobProperties6.
     * 
     * @param history
     */
    public void setHistory(com.ecs.stellent.spp.ActivityHistory2[] history) {
        this.history = history;
    }


    /**
     * Gets the notes value for this JobProperties6.
     * 
     * @return notes
     */
    public com.ecs.stellent.spp.Notes[] getNotes() {
        return notes;
    }


    /**
     * Sets the notes value for this JobProperties6.
     * 
     * @param notes
     */
    public void setNotes(com.ecs.stellent.spp.Notes[] notes) {
        this.notes = notes;
    }


    /**
     * Gets the workingTimeSpentInSecs value for this JobProperties6.
     * 
     * @return workingTimeSpentInSecs
     */
    public int getWorkingTimeSpentInSecs() {
        return workingTimeSpentInSecs;
    }


    /**
     * Sets the workingTimeSpentInSecs value for this JobProperties6.
     * 
     * @param workingTimeSpentInSecs
     */
    public void setWorkingTimeSpentInSecs(int workingTimeSpentInSecs) {
        this.workingTimeSpentInSecs = workingTimeSpentInSecs;
    }


    /**
     * Gets the jobOwnerID value for this JobProperties6.
     * 
     * @return jobOwnerID
     */
    public java.lang.String getJobOwnerID() {
        return jobOwnerID;
    }


    /**
     * Sets the jobOwnerID value for this JobProperties6.
     * 
     * @param jobOwnerID
     */
    public void setJobOwnerID(java.lang.String jobOwnerID) {
        this.jobOwnerID = jobOwnerID;
    }


    /**
     * Gets the holdTime value for this JobProperties6.
     * 
     * @return holdTime
     */
    public java.lang.String getHoldTime() {
        return holdTime;
    }


    /**
     * Sets the holdTime value for this JobProperties6.
     * 
     * @param holdTime
     */
    public void setHoldTime(java.lang.String holdTime) {
        this.holdTime = holdTime;
    }


    /**
     * Gets the activationTime value for this JobProperties6.
     * 
     * @return activationTime
     */
    public java.lang.String getActivationTime() {
        return activationTime;
    }


    /**
     * Sets the activationTime value for this JobProperties6.
     * 
     * @param activationTime
     */
    public void setActivationTime(java.lang.String activationTime) {
        this.activationTime = activationTime;
    }


    /**
     * Gets the expectedFinishDate value for this JobProperties6.
     * 
     * @return expectedFinishDate
     */
    public java.util.Calendar getExpectedFinishDate() {
        return expectedFinishDate;
    }


    /**
     * Sets the expectedFinishDate value for this JobProperties6.
     * 
     * @param expectedFinishDate
     */
    public void setExpectedFinishDate(java.util.Calendar expectedFinishDate) {
        this.expectedFinishDate = expectedFinishDate;
    }


    /**
     * Gets the jobState value for this JobProperties6.
     * 
     * @return jobState
     */
    public java.lang.String getJobState() {
        return jobState;
    }


    /**
     * Sets the jobState value for this JobProperties6.
     * 
     * @param jobState
     */
    public void setJobState(java.lang.String jobState) {
        this.jobState = jobState;
    }


    /**
     * Gets the jobPercent value for this JobProperties6.
     * 
     * @return jobPercent
     */
    public short getJobPercent() {
        return jobPercent;
    }


    /**
     * Sets the jobPercent value for this JobProperties6.
     * 
     * @param jobPercent
     */
    public void setJobPercent(short jobPercent) {
        this.jobPercent = jobPercent;
    }


    /**
     * Gets the associatedCaseID value for this JobProperties6.
     * 
     * @return associatedCaseID
     */
    public java.lang.String getAssociatedCaseID() {
        return associatedCaseID;
    }


    /**
     * Sets the associatedCaseID value for this JobProperties6.
     * 
     * @param associatedCaseID
     */
    public void setAssociatedCaseID(java.lang.String associatedCaseID) {
        this.associatedCaseID = associatedCaseID;
    }


    /**
     * Gets the budget value for this JobProperties6.
     * 
     * @return budget
     */
    public double getBudget() {
        return budget;
    }


    /**
     * Sets the budget value for this JobProperties6.
     * 
     * @param budget
     */
    public void setBudget(double budget) {
        this.budget = budget;
    }


    /**
     * Gets the milestones value for this JobProperties6.
     * 
     * @return milestones
     */
    public com.ecs.stellent.spp.Milestones[] getMilestones() {
        return milestones;
    }


    /**
     * Sets the milestones value for this JobProperties6.
     * 
     * @param milestones
     */
    public void setMilestones(com.ecs.stellent.spp.Milestones[] milestones) {
        this.milestones = milestones;
    }


    /**
     * Gets the jobSpend value for this JobProperties6.
     * 
     * @return jobSpend
     */
    public double getJobSpend() {
        return jobSpend;
    }


    /**
     * Sets the jobSpend value for this JobProperties6.
     * 
     * @param jobSpend
     */
    public void setJobSpend(double jobSpend) {
        this.jobSpend = jobSpend;
    }


    /**
     * Gets the events value for this JobProperties6.
     * 
     * @return events
     */
    public com.ecs.stellent.spp.Events[] getEvents() {
        return events;
    }


    /**
     * Sets the events value for this JobProperties6.
     * 
     * @param events
     */
    public void setEvents(com.ecs.stellent.spp.Events[] events) {
        this.events = events;
    }


    /**
     * Gets the roles value for this JobProperties6.
     * 
     * @return roles
     */
    public com.ecs.stellent.spp.RoleInfo[] getRoles() {
        return roles;
    }


    /**
     * Sets the roles value for this JobProperties6.
     * 
     * @param roles
     */
    public void setRoles(com.ecs.stellent.spp.RoleInfo[] roles) {
        this.roles = roles;
    }


    /**
     * Gets the jobType value for this JobProperties6.
     * 
     * @return jobType
     */
    public short getJobType() {
        return jobType;
    }


    /**
     * Sets the jobType value for this JobProperties6.
     * 
     * @param jobType
     */
    public void setJobType(short jobType) {
        this.jobType = jobType;
    }


    /**
     * Gets the caseRef value for this JobProperties6.
     * 
     * @return caseRef
     */
    public java.lang.String getCaseRef() {
        return caseRef;
    }


    /**
     * Sets the caseRef value for this JobProperties6.
     * 
     * @param caseRef
     */
    public void setCaseRef(java.lang.String caseRef) {
        this.caseRef = caseRef;
    }


    /**
     * Gets the jobId value for this JobProperties6.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this JobProperties6.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JobProperties6)) return false;
        JobProperties6 other = (JobProperties6) obj;
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
            this.primProcessType == other.getPrimProcessType() &&
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
            this.workingTimeSpentInSecs == other.getWorkingTimeSpentInSecs() &&
            ((this.jobOwnerID==null && other.getJobOwnerID()==null) || 
             (this.jobOwnerID!=null &&
              this.jobOwnerID.equals(other.getJobOwnerID()))) &&
            ((this.holdTime==null && other.getHoldTime()==null) || 
             (this.holdTime!=null &&
              this.holdTime.equals(other.getHoldTime()))) &&
            ((this.activationTime==null && other.getActivationTime()==null) || 
             (this.activationTime!=null &&
              this.activationTime.equals(other.getActivationTime()))) &&
            ((this.expectedFinishDate==null && other.getExpectedFinishDate()==null) || 
             (this.expectedFinishDate!=null &&
              this.expectedFinishDate.equals(other.getExpectedFinishDate()))) &&
            ((this.jobState==null && other.getJobState()==null) || 
             (this.jobState!=null &&
              this.jobState.equals(other.getJobState()))) &&
            this.jobPercent == other.getJobPercent() &&
            ((this.associatedCaseID==null && other.getAssociatedCaseID()==null) || 
             (this.associatedCaseID!=null &&
              this.associatedCaseID.equals(other.getAssociatedCaseID()))) &&
            this.budget == other.getBudget() &&
            ((this.milestones==null && other.getMilestones()==null) || 
             (this.milestones!=null &&
              java.util.Arrays.equals(this.milestones, other.getMilestones()))) &&
            this.jobSpend == other.getJobSpend() &&
            ((this.events==null && other.getEvents()==null) || 
             (this.events!=null &&
              java.util.Arrays.equals(this.events, other.getEvents()))) &&
            ((this.roles==null && other.getRoles()==null) || 
             (this.roles!=null &&
              java.util.Arrays.equals(this.roles, other.getRoles()))) &&
            this.jobType == other.getJobType() &&
            ((this.caseRef==null && other.getCaseRef()==null) || 
             (this.caseRef!=null &&
              this.caseRef.equals(other.getCaseRef()))) &&
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId())));
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
        _hashCode += getPrimProcessType();
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
        if (getJobOwnerID() != null) {
            _hashCode += getJobOwnerID().hashCode();
        }
        if (getHoldTime() != null) {
            _hashCode += getHoldTime().hashCode();
        }
        if (getActivationTime() != null) {
            _hashCode += getActivationTime().hashCode();
        }
        if (getExpectedFinishDate() != null) {
            _hashCode += getExpectedFinishDate().hashCode();
        }
        if (getJobState() != null) {
            _hashCode += getJobState().hashCode();
        }
        _hashCode += getJobPercent();
        if (getAssociatedCaseID() != null) {
            _hashCode += getAssociatedCaseID().hashCode();
        }
        _hashCode += new Double(getBudget()).hashCode();
        if (getMilestones() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getMilestones());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getMilestones(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += new Double(getJobSpend()).hashCode();
        if (getEvents() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEvents());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEvents(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getRoles() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRoles());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRoles(), i);
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
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JobProperties6.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties6"));
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
        elemField.setFieldName("primProcessType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrimProcessType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory2"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory2"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobOwnerID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobOwnerID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("holdTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "HoldTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activationTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivationTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expectedFinishDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExpectedFinishDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
        elemField.setFieldName("jobPercent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobPercent"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("associatedCaseID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AssociatedCaseID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("budget");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Budget"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("milestones");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "milestones"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Milestones"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Milestones"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobSpend");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobSpend"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("events");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "events"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Events"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Events"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("roles");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "roles"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleInfo"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"));
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
