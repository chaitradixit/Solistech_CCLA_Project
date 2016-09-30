/**
 * GetJobsUsingSearchCriteria4.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobsUsingSearchCriteria4  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.util.Calendar startTimeFrom;

    private boolean useStartTimeFrom;

    private java.util.Calendar startTimeTo;

    private boolean useStartTimeTo;

    private java.util.Calendar finishTimeFrom;

    private boolean useFinishTimeFrom;

    private java.util.Calendar finishTimeTo;

    private boolean useFinishTimeTo;

    private java.lang.String creatorId;

    private boolean useCreatorId;

    private boolean workerResource;

    private short status;

    private java.lang.String processId;

    private boolean useProcessId;

    private double version;

    private boolean useVersion;

    private java.lang.String originServerId;

    private boolean useOriginServerId;

    private int maxNumToRetrieve;

    private java.lang.String categoryId;

    private boolean useCategoryId;

    private short priority;

    private boolean usePriority;

    private java.util.Calendar dueDateFrom;

    private boolean useDueDateFrom;

    private java.util.Calendar dueDateTo;

    private boolean useDueDateTo;

    private java.lang.String workQueueDefnName;

    private com.ecs.stellent.spp.FieldFilterConfig fieldFilter;

    private boolean useJobOwner;

    private java.lang.String jobOwnerID;

    private boolean useJobState;

    private java.lang.String jobStateName;

    public GetJobsUsingSearchCriteria4() {
    }

    public GetJobsUsingSearchCriteria4(
           java.lang.String sessionId,
           java.util.Calendar startTimeFrom,
           boolean useStartTimeFrom,
           java.util.Calendar startTimeTo,
           boolean useStartTimeTo,
           java.util.Calendar finishTimeFrom,
           boolean useFinishTimeFrom,
           java.util.Calendar finishTimeTo,
           boolean useFinishTimeTo,
           java.lang.String creatorId,
           boolean useCreatorId,
           boolean workerResource,
           short status,
           java.lang.String processId,
           boolean useProcessId,
           double version,
           boolean useVersion,
           java.lang.String originServerId,
           boolean useOriginServerId,
           int maxNumToRetrieve,
           java.lang.String categoryId,
           boolean useCategoryId,
           short priority,
           boolean usePriority,
           java.util.Calendar dueDateFrom,
           boolean useDueDateFrom,
           java.util.Calendar dueDateTo,
           boolean useDueDateTo,
           java.lang.String workQueueDefnName,
           com.ecs.stellent.spp.FieldFilterConfig fieldFilter,
           boolean useJobOwner,
           java.lang.String jobOwnerID,
           boolean useJobState,
           java.lang.String jobStateName) {
           this.sessionId = sessionId;
           this.startTimeFrom = startTimeFrom;
           this.useStartTimeFrom = useStartTimeFrom;
           this.startTimeTo = startTimeTo;
           this.useStartTimeTo = useStartTimeTo;
           this.finishTimeFrom = finishTimeFrom;
           this.useFinishTimeFrom = useFinishTimeFrom;
           this.finishTimeTo = finishTimeTo;
           this.useFinishTimeTo = useFinishTimeTo;
           this.creatorId = creatorId;
           this.useCreatorId = useCreatorId;
           this.workerResource = workerResource;
           this.status = status;
           this.processId = processId;
           this.useProcessId = useProcessId;
           this.version = version;
           this.useVersion = useVersion;
           this.originServerId = originServerId;
           this.useOriginServerId = useOriginServerId;
           this.maxNumToRetrieve = maxNumToRetrieve;
           this.categoryId = categoryId;
           this.useCategoryId = useCategoryId;
           this.priority = priority;
           this.usePriority = usePriority;
           this.dueDateFrom = dueDateFrom;
           this.useDueDateFrom = useDueDateFrom;
           this.dueDateTo = dueDateTo;
           this.useDueDateTo = useDueDateTo;
           this.workQueueDefnName = workQueueDefnName;
           this.fieldFilter = fieldFilter;
           this.useJobOwner = useJobOwner;
           this.jobOwnerID = jobOwnerID;
           this.useJobState = useJobState;
           this.jobStateName = jobStateName;
    }


    /**
     * Gets the sessionId value for this GetJobsUsingSearchCriteria4.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetJobsUsingSearchCriteria4.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the startTimeFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @return startTimeFrom
     */
    public java.util.Calendar getStartTimeFrom() {
        return startTimeFrom;
    }


    /**
     * Sets the startTimeFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @param startTimeFrom
     */
    public void setStartTimeFrom(java.util.Calendar startTimeFrom) {
        this.startTimeFrom = startTimeFrom;
    }


    /**
     * Gets the useStartTimeFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useStartTimeFrom
     */
    public boolean isUseStartTimeFrom() {
        return useStartTimeFrom;
    }


    /**
     * Sets the useStartTimeFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useStartTimeFrom
     */
    public void setUseStartTimeFrom(boolean useStartTimeFrom) {
        this.useStartTimeFrom = useStartTimeFrom;
    }


    /**
     * Gets the startTimeTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @return startTimeTo
     */
    public java.util.Calendar getStartTimeTo() {
        return startTimeTo;
    }


    /**
     * Sets the startTimeTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @param startTimeTo
     */
    public void setStartTimeTo(java.util.Calendar startTimeTo) {
        this.startTimeTo = startTimeTo;
    }


    /**
     * Gets the useStartTimeTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useStartTimeTo
     */
    public boolean isUseStartTimeTo() {
        return useStartTimeTo;
    }


    /**
     * Sets the useStartTimeTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useStartTimeTo
     */
    public void setUseStartTimeTo(boolean useStartTimeTo) {
        this.useStartTimeTo = useStartTimeTo;
    }


    /**
     * Gets the finishTimeFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @return finishTimeFrom
     */
    public java.util.Calendar getFinishTimeFrom() {
        return finishTimeFrom;
    }


    /**
     * Sets the finishTimeFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @param finishTimeFrom
     */
    public void setFinishTimeFrom(java.util.Calendar finishTimeFrom) {
        this.finishTimeFrom = finishTimeFrom;
    }


    /**
     * Gets the useFinishTimeFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useFinishTimeFrom
     */
    public boolean isUseFinishTimeFrom() {
        return useFinishTimeFrom;
    }


    /**
     * Sets the useFinishTimeFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useFinishTimeFrom
     */
    public void setUseFinishTimeFrom(boolean useFinishTimeFrom) {
        this.useFinishTimeFrom = useFinishTimeFrom;
    }


    /**
     * Gets the finishTimeTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @return finishTimeTo
     */
    public java.util.Calendar getFinishTimeTo() {
        return finishTimeTo;
    }


    /**
     * Sets the finishTimeTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @param finishTimeTo
     */
    public void setFinishTimeTo(java.util.Calendar finishTimeTo) {
        this.finishTimeTo = finishTimeTo;
    }


    /**
     * Gets the useFinishTimeTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useFinishTimeTo
     */
    public boolean isUseFinishTimeTo() {
        return useFinishTimeTo;
    }


    /**
     * Sets the useFinishTimeTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useFinishTimeTo
     */
    public void setUseFinishTimeTo(boolean useFinishTimeTo) {
        this.useFinishTimeTo = useFinishTimeTo;
    }


    /**
     * Gets the creatorId value for this GetJobsUsingSearchCriteria4.
     * 
     * @return creatorId
     */
    public java.lang.String getCreatorId() {
        return creatorId;
    }


    /**
     * Sets the creatorId value for this GetJobsUsingSearchCriteria4.
     * 
     * @param creatorId
     */
    public void setCreatorId(java.lang.String creatorId) {
        this.creatorId = creatorId;
    }


    /**
     * Gets the useCreatorId value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useCreatorId
     */
    public boolean isUseCreatorId() {
        return useCreatorId;
    }


    /**
     * Sets the useCreatorId value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useCreatorId
     */
    public void setUseCreatorId(boolean useCreatorId) {
        this.useCreatorId = useCreatorId;
    }


    /**
     * Gets the workerResource value for this GetJobsUsingSearchCriteria4.
     * 
     * @return workerResource
     */
    public boolean isWorkerResource() {
        return workerResource;
    }


    /**
     * Sets the workerResource value for this GetJobsUsingSearchCriteria4.
     * 
     * @param workerResource
     */
    public void setWorkerResource(boolean workerResource) {
        this.workerResource = workerResource;
    }


    /**
     * Gets the status value for this GetJobsUsingSearchCriteria4.
     * 
     * @return status
     */
    public short getStatus() {
        return status;
    }


    /**
     * Sets the status value for this GetJobsUsingSearchCriteria4.
     * 
     * @param status
     */
    public void setStatus(short status) {
        this.status = status;
    }


    /**
     * Gets the processId value for this GetJobsUsingSearchCriteria4.
     * 
     * @return processId
     */
    public java.lang.String getProcessId() {
        return processId;
    }


    /**
     * Sets the processId value for this GetJobsUsingSearchCriteria4.
     * 
     * @param processId
     */
    public void setProcessId(java.lang.String processId) {
        this.processId = processId;
    }


    /**
     * Gets the useProcessId value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useProcessId
     */
    public boolean isUseProcessId() {
        return useProcessId;
    }


    /**
     * Sets the useProcessId value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useProcessId
     */
    public void setUseProcessId(boolean useProcessId) {
        this.useProcessId = useProcessId;
    }


    /**
     * Gets the version value for this GetJobsUsingSearchCriteria4.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this GetJobsUsingSearchCriteria4.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the useVersion value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useVersion
     */
    public boolean isUseVersion() {
        return useVersion;
    }


    /**
     * Sets the useVersion value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useVersion
     */
    public void setUseVersion(boolean useVersion) {
        this.useVersion = useVersion;
    }


    /**
     * Gets the originServerId value for this GetJobsUsingSearchCriteria4.
     * 
     * @return originServerId
     */
    public java.lang.String getOriginServerId() {
        return originServerId;
    }


    /**
     * Sets the originServerId value for this GetJobsUsingSearchCriteria4.
     * 
     * @param originServerId
     */
    public void setOriginServerId(java.lang.String originServerId) {
        this.originServerId = originServerId;
    }


    /**
     * Gets the useOriginServerId value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useOriginServerId
     */
    public boolean isUseOriginServerId() {
        return useOriginServerId;
    }


    /**
     * Sets the useOriginServerId value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useOriginServerId
     */
    public void setUseOriginServerId(boolean useOriginServerId) {
        this.useOriginServerId = useOriginServerId;
    }


    /**
     * Gets the maxNumToRetrieve value for this GetJobsUsingSearchCriteria4.
     * 
     * @return maxNumToRetrieve
     */
    public int getMaxNumToRetrieve() {
        return maxNumToRetrieve;
    }


    /**
     * Sets the maxNumToRetrieve value for this GetJobsUsingSearchCriteria4.
     * 
     * @param maxNumToRetrieve
     */
    public void setMaxNumToRetrieve(int maxNumToRetrieve) {
        this.maxNumToRetrieve = maxNumToRetrieve;
    }


    /**
     * Gets the categoryId value for this GetJobsUsingSearchCriteria4.
     * 
     * @return categoryId
     */
    public java.lang.String getCategoryId() {
        return categoryId;
    }


    /**
     * Sets the categoryId value for this GetJobsUsingSearchCriteria4.
     * 
     * @param categoryId
     */
    public void setCategoryId(java.lang.String categoryId) {
        this.categoryId = categoryId;
    }


    /**
     * Gets the useCategoryId value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useCategoryId
     */
    public boolean isUseCategoryId() {
        return useCategoryId;
    }


    /**
     * Sets the useCategoryId value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useCategoryId
     */
    public void setUseCategoryId(boolean useCategoryId) {
        this.useCategoryId = useCategoryId;
    }


    /**
     * Gets the priority value for this GetJobsUsingSearchCriteria4.
     * 
     * @return priority
     */
    public short getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this GetJobsUsingSearchCriteria4.
     * 
     * @param priority
     */
    public void setPriority(short priority) {
        this.priority = priority;
    }


    /**
     * Gets the usePriority value for this GetJobsUsingSearchCriteria4.
     * 
     * @return usePriority
     */
    public boolean isUsePriority() {
        return usePriority;
    }


    /**
     * Sets the usePriority value for this GetJobsUsingSearchCriteria4.
     * 
     * @param usePriority
     */
    public void setUsePriority(boolean usePriority) {
        this.usePriority = usePriority;
    }


    /**
     * Gets the dueDateFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @return dueDateFrom
     */
    public java.util.Calendar getDueDateFrom() {
        return dueDateFrom;
    }


    /**
     * Sets the dueDateFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @param dueDateFrom
     */
    public void setDueDateFrom(java.util.Calendar dueDateFrom) {
        this.dueDateFrom = dueDateFrom;
    }


    /**
     * Gets the useDueDateFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useDueDateFrom
     */
    public boolean isUseDueDateFrom() {
        return useDueDateFrom;
    }


    /**
     * Sets the useDueDateFrom value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useDueDateFrom
     */
    public void setUseDueDateFrom(boolean useDueDateFrom) {
        this.useDueDateFrom = useDueDateFrom;
    }


    /**
     * Gets the dueDateTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @return dueDateTo
     */
    public java.util.Calendar getDueDateTo() {
        return dueDateTo;
    }


    /**
     * Sets the dueDateTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @param dueDateTo
     */
    public void setDueDateTo(java.util.Calendar dueDateTo) {
        this.dueDateTo = dueDateTo;
    }


    /**
     * Gets the useDueDateTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useDueDateTo
     */
    public boolean isUseDueDateTo() {
        return useDueDateTo;
    }


    /**
     * Sets the useDueDateTo value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useDueDateTo
     */
    public void setUseDueDateTo(boolean useDueDateTo) {
        this.useDueDateTo = useDueDateTo;
    }


    /**
     * Gets the workQueueDefnName value for this GetJobsUsingSearchCriteria4.
     * 
     * @return workQueueDefnName
     */
    public java.lang.String getWorkQueueDefnName() {
        return workQueueDefnName;
    }


    /**
     * Sets the workQueueDefnName value for this GetJobsUsingSearchCriteria4.
     * 
     * @param workQueueDefnName
     */
    public void setWorkQueueDefnName(java.lang.String workQueueDefnName) {
        this.workQueueDefnName = workQueueDefnName;
    }


    /**
     * Gets the fieldFilter value for this GetJobsUsingSearchCriteria4.
     * 
     * @return fieldFilter
     */
    public com.ecs.stellent.spp.FieldFilterConfig getFieldFilter() {
        return fieldFilter;
    }


    /**
     * Sets the fieldFilter value for this GetJobsUsingSearchCriteria4.
     * 
     * @param fieldFilter
     */
    public void setFieldFilter(com.ecs.stellent.spp.FieldFilterConfig fieldFilter) {
        this.fieldFilter = fieldFilter;
    }


    /**
     * Gets the useJobOwner value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useJobOwner
     */
    public boolean isUseJobOwner() {
        return useJobOwner;
    }


    /**
     * Sets the useJobOwner value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useJobOwner
     */
    public void setUseJobOwner(boolean useJobOwner) {
        this.useJobOwner = useJobOwner;
    }


    /**
     * Gets the jobOwnerID value for this GetJobsUsingSearchCriteria4.
     * 
     * @return jobOwnerID
     */
    public java.lang.String getJobOwnerID() {
        return jobOwnerID;
    }


    /**
     * Sets the jobOwnerID value for this GetJobsUsingSearchCriteria4.
     * 
     * @param jobOwnerID
     */
    public void setJobOwnerID(java.lang.String jobOwnerID) {
        this.jobOwnerID = jobOwnerID;
    }


    /**
     * Gets the useJobState value for this GetJobsUsingSearchCriteria4.
     * 
     * @return useJobState
     */
    public boolean isUseJobState() {
        return useJobState;
    }


    /**
     * Sets the useJobState value for this GetJobsUsingSearchCriteria4.
     * 
     * @param useJobState
     */
    public void setUseJobState(boolean useJobState) {
        this.useJobState = useJobState;
    }


    /**
     * Gets the jobStateName value for this GetJobsUsingSearchCriteria4.
     * 
     * @return jobStateName
     */
    public java.lang.String getJobStateName() {
        return jobStateName;
    }


    /**
     * Sets the jobStateName value for this GetJobsUsingSearchCriteria4.
     * 
     * @param jobStateName
     */
    public void setJobStateName(java.lang.String jobStateName) {
        this.jobStateName = jobStateName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobsUsingSearchCriteria4)) return false;
        GetJobsUsingSearchCriteria4 other = (GetJobsUsingSearchCriteria4) obj;
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
            ((this.startTimeFrom==null && other.getStartTimeFrom()==null) || 
             (this.startTimeFrom!=null &&
              this.startTimeFrom.equals(other.getStartTimeFrom()))) &&
            this.useStartTimeFrom == other.isUseStartTimeFrom() &&
            ((this.startTimeTo==null && other.getStartTimeTo()==null) || 
             (this.startTimeTo!=null &&
              this.startTimeTo.equals(other.getStartTimeTo()))) &&
            this.useStartTimeTo == other.isUseStartTimeTo() &&
            ((this.finishTimeFrom==null && other.getFinishTimeFrom()==null) || 
             (this.finishTimeFrom!=null &&
              this.finishTimeFrom.equals(other.getFinishTimeFrom()))) &&
            this.useFinishTimeFrom == other.isUseFinishTimeFrom() &&
            ((this.finishTimeTo==null && other.getFinishTimeTo()==null) || 
             (this.finishTimeTo!=null &&
              this.finishTimeTo.equals(other.getFinishTimeTo()))) &&
            this.useFinishTimeTo == other.isUseFinishTimeTo() &&
            ((this.creatorId==null && other.getCreatorId()==null) || 
             (this.creatorId!=null &&
              this.creatorId.equals(other.getCreatorId()))) &&
            this.useCreatorId == other.isUseCreatorId() &&
            this.workerResource == other.isWorkerResource() &&
            this.status == other.getStatus() &&
            ((this.processId==null && other.getProcessId()==null) || 
             (this.processId!=null &&
              this.processId.equals(other.getProcessId()))) &&
            this.useProcessId == other.isUseProcessId() &&
            this.version == other.getVersion() &&
            this.useVersion == other.isUseVersion() &&
            ((this.originServerId==null && other.getOriginServerId()==null) || 
             (this.originServerId!=null &&
              this.originServerId.equals(other.getOriginServerId()))) &&
            this.useOriginServerId == other.isUseOriginServerId() &&
            this.maxNumToRetrieve == other.getMaxNumToRetrieve() &&
            ((this.categoryId==null && other.getCategoryId()==null) || 
             (this.categoryId!=null &&
              this.categoryId.equals(other.getCategoryId()))) &&
            this.useCategoryId == other.isUseCategoryId() &&
            this.priority == other.getPriority() &&
            this.usePriority == other.isUsePriority() &&
            ((this.dueDateFrom==null && other.getDueDateFrom()==null) || 
             (this.dueDateFrom!=null &&
              this.dueDateFrom.equals(other.getDueDateFrom()))) &&
            this.useDueDateFrom == other.isUseDueDateFrom() &&
            ((this.dueDateTo==null && other.getDueDateTo()==null) || 
             (this.dueDateTo!=null &&
              this.dueDateTo.equals(other.getDueDateTo()))) &&
            this.useDueDateTo == other.isUseDueDateTo() &&
            ((this.workQueueDefnName==null && other.getWorkQueueDefnName()==null) || 
             (this.workQueueDefnName!=null &&
              this.workQueueDefnName.equals(other.getWorkQueueDefnName()))) &&
            ((this.fieldFilter==null && other.getFieldFilter()==null) || 
             (this.fieldFilter!=null &&
              this.fieldFilter.equals(other.getFieldFilter()))) &&
            this.useJobOwner == other.isUseJobOwner() &&
            ((this.jobOwnerID==null && other.getJobOwnerID()==null) || 
             (this.jobOwnerID!=null &&
              this.jobOwnerID.equals(other.getJobOwnerID()))) &&
            this.useJobState == other.isUseJobState() &&
            ((this.jobStateName==null && other.getJobStateName()==null) || 
             (this.jobStateName!=null &&
              this.jobStateName.equals(other.getJobStateName())));
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
        if (getStartTimeFrom() != null) {
            _hashCode += getStartTimeFrom().hashCode();
        }
        _hashCode += (isUseStartTimeFrom() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStartTimeTo() != null) {
            _hashCode += getStartTimeTo().hashCode();
        }
        _hashCode += (isUseStartTimeTo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getFinishTimeFrom() != null) {
            _hashCode += getFinishTimeFrom().hashCode();
        }
        _hashCode += (isUseFinishTimeFrom() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getFinishTimeTo() != null) {
            _hashCode += getFinishTimeTo().hashCode();
        }
        _hashCode += (isUseFinishTimeTo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getCreatorId() != null) {
            _hashCode += getCreatorId().hashCode();
        }
        _hashCode += (isUseCreatorId() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isWorkerResource() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getStatus();
        if (getProcessId() != null) {
            _hashCode += getProcessId().hashCode();
        }
        _hashCode += (isUseProcessId() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += new Double(getVersion()).hashCode();
        _hashCode += (isUseVersion() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOriginServerId() != null) {
            _hashCode += getOriginServerId().hashCode();
        }
        _hashCode += (isUseOriginServerId() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getMaxNumToRetrieve();
        if (getCategoryId() != null) {
            _hashCode += getCategoryId().hashCode();
        }
        _hashCode += (isUseCategoryId() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getPriority();
        _hashCode += (isUsePriority() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDueDateFrom() != null) {
            _hashCode += getDueDateFrom().hashCode();
        }
        _hashCode += (isUseDueDateFrom() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getDueDateTo() != null) {
            _hashCode += getDueDateTo().hashCode();
        }
        _hashCode += (isUseDueDateTo() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getWorkQueueDefnName() != null) {
            _hashCode += getWorkQueueDefnName().hashCode();
        }
        if (getFieldFilter() != null) {
            _hashCode += getFieldFilter().hashCode();
        }
        _hashCode += (isUseJobOwner() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getJobOwnerID() != null) {
            _hashCode += getJobOwnerID().hashCode();
        }
        _hashCode += (isUseJobState() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getJobStateName() != null) {
            _hashCode += getJobStateName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobsUsingSearchCriteria4.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria4"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startTimeFrom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeFrom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useStartTimeFrom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeFrom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startTimeTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useStartTimeTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("finishTimeFrom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeFrom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useFinishTimeFrom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeFrom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("finishTimeTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useFinishTimeTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("useCreatorId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCreatorId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workerResource");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkerResource"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"));
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
        elemField.setFieldName("useProcessId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseProcessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originServerId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OriginServerId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useOriginServerId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseOriginServerId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxNumToRetrieve");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MaxNumToRetrieve"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoryId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useCategoryId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCategoryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("priority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Priority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usePriority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dueDateFrom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateFrom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useDueDateFrom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateFrom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dueDateTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useDueDateTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workQueueDefnName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueueDefnName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldFilter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useJobOwner");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobOwner"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("useJobState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobStateName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobStateName"));
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
