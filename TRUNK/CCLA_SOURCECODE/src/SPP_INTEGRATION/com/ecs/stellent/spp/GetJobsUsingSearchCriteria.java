/**
 * GetJobsUsingSearchCriteria.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobsUsingSearchCriteria  implements java.io.Serializable {
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

    private com.ecs.stellent.spp.Variable[] procTemplateVariables;

    private java.lang.String categoryId;

    private boolean useCategoryId;

    public GetJobsUsingSearchCriteria() {
    }

    public GetJobsUsingSearchCriteria(
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
           com.ecs.stellent.spp.Variable[] procTemplateVariables,
           java.lang.String categoryId,
           boolean useCategoryId) {
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
           this.procTemplateVariables = procTemplateVariables;
           this.categoryId = categoryId;
           this.useCategoryId = useCategoryId;
    }


    /**
     * Gets the sessionId value for this GetJobsUsingSearchCriteria.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetJobsUsingSearchCriteria.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the startTimeFrom value for this GetJobsUsingSearchCriteria.
     * 
     * @return startTimeFrom
     */
    public java.util.Calendar getStartTimeFrom() {
        return startTimeFrom;
    }


    /**
     * Sets the startTimeFrom value for this GetJobsUsingSearchCriteria.
     * 
     * @param startTimeFrom
     */
    public void setStartTimeFrom(java.util.Calendar startTimeFrom) {
        this.startTimeFrom = startTimeFrom;
    }


    /**
     * Gets the useStartTimeFrom value for this GetJobsUsingSearchCriteria.
     * 
     * @return useStartTimeFrom
     */
    public boolean isUseStartTimeFrom() {
        return useStartTimeFrom;
    }


    /**
     * Sets the useStartTimeFrom value for this GetJobsUsingSearchCriteria.
     * 
     * @param useStartTimeFrom
     */
    public void setUseStartTimeFrom(boolean useStartTimeFrom) {
        this.useStartTimeFrom = useStartTimeFrom;
    }


    /**
     * Gets the startTimeTo value for this GetJobsUsingSearchCriteria.
     * 
     * @return startTimeTo
     */
    public java.util.Calendar getStartTimeTo() {
        return startTimeTo;
    }


    /**
     * Sets the startTimeTo value for this GetJobsUsingSearchCriteria.
     * 
     * @param startTimeTo
     */
    public void setStartTimeTo(java.util.Calendar startTimeTo) {
        this.startTimeTo = startTimeTo;
    }


    /**
     * Gets the useStartTimeTo value for this GetJobsUsingSearchCriteria.
     * 
     * @return useStartTimeTo
     */
    public boolean isUseStartTimeTo() {
        return useStartTimeTo;
    }


    /**
     * Sets the useStartTimeTo value for this GetJobsUsingSearchCriteria.
     * 
     * @param useStartTimeTo
     */
    public void setUseStartTimeTo(boolean useStartTimeTo) {
        this.useStartTimeTo = useStartTimeTo;
    }


    /**
     * Gets the finishTimeFrom value for this GetJobsUsingSearchCriteria.
     * 
     * @return finishTimeFrom
     */
    public java.util.Calendar getFinishTimeFrom() {
        return finishTimeFrom;
    }


    /**
     * Sets the finishTimeFrom value for this GetJobsUsingSearchCriteria.
     * 
     * @param finishTimeFrom
     */
    public void setFinishTimeFrom(java.util.Calendar finishTimeFrom) {
        this.finishTimeFrom = finishTimeFrom;
    }


    /**
     * Gets the useFinishTimeFrom value for this GetJobsUsingSearchCriteria.
     * 
     * @return useFinishTimeFrom
     */
    public boolean isUseFinishTimeFrom() {
        return useFinishTimeFrom;
    }


    /**
     * Sets the useFinishTimeFrom value for this GetJobsUsingSearchCriteria.
     * 
     * @param useFinishTimeFrom
     */
    public void setUseFinishTimeFrom(boolean useFinishTimeFrom) {
        this.useFinishTimeFrom = useFinishTimeFrom;
    }


    /**
     * Gets the finishTimeTo value for this GetJobsUsingSearchCriteria.
     * 
     * @return finishTimeTo
     */
    public java.util.Calendar getFinishTimeTo() {
        return finishTimeTo;
    }


    /**
     * Sets the finishTimeTo value for this GetJobsUsingSearchCriteria.
     * 
     * @param finishTimeTo
     */
    public void setFinishTimeTo(java.util.Calendar finishTimeTo) {
        this.finishTimeTo = finishTimeTo;
    }


    /**
     * Gets the useFinishTimeTo value for this GetJobsUsingSearchCriteria.
     * 
     * @return useFinishTimeTo
     */
    public boolean isUseFinishTimeTo() {
        return useFinishTimeTo;
    }


    /**
     * Sets the useFinishTimeTo value for this GetJobsUsingSearchCriteria.
     * 
     * @param useFinishTimeTo
     */
    public void setUseFinishTimeTo(boolean useFinishTimeTo) {
        this.useFinishTimeTo = useFinishTimeTo;
    }


    /**
     * Gets the creatorId value for this GetJobsUsingSearchCriteria.
     * 
     * @return creatorId
     */
    public java.lang.String getCreatorId() {
        return creatorId;
    }


    /**
     * Sets the creatorId value for this GetJobsUsingSearchCriteria.
     * 
     * @param creatorId
     */
    public void setCreatorId(java.lang.String creatorId) {
        this.creatorId = creatorId;
    }


    /**
     * Gets the useCreatorId value for this GetJobsUsingSearchCriteria.
     * 
     * @return useCreatorId
     */
    public boolean isUseCreatorId() {
        return useCreatorId;
    }


    /**
     * Sets the useCreatorId value for this GetJobsUsingSearchCriteria.
     * 
     * @param useCreatorId
     */
    public void setUseCreatorId(boolean useCreatorId) {
        this.useCreatorId = useCreatorId;
    }


    /**
     * Gets the workerResource value for this GetJobsUsingSearchCriteria.
     * 
     * @return workerResource
     */
    public boolean isWorkerResource() {
        return workerResource;
    }


    /**
     * Sets the workerResource value for this GetJobsUsingSearchCriteria.
     * 
     * @param workerResource
     */
    public void setWorkerResource(boolean workerResource) {
        this.workerResource = workerResource;
    }


    /**
     * Gets the status value for this GetJobsUsingSearchCriteria.
     * 
     * @return status
     */
    public short getStatus() {
        return status;
    }


    /**
     * Sets the status value for this GetJobsUsingSearchCriteria.
     * 
     * @param status
     */
    public void setStatus(short status) {
        this.status = status;
    }


    /**
     * Gets the processId value for this GetJobsUsingSearchCriteria.
     * 
     * @return processId
     */
    public java.lang.String getProcessId() {
        return processId;
    }


    /**
     * Sets the processId value for this GetJobsUsingSearchCriteria.
     * 
     * @param processId
     */
    public void setProcessId(java.lang.String processId) {
        this.processId = processId;
    }


    /**
     * Gets the useProcessId value for this GetJobsUsingSearchCriteria.
     * 
     * @return useProcessId
     */
    public boolean isUseProcessId() {
        return useProcessId;
    }


    /**
     * Sets the useProcessId value for this GetJobsUsingSearchCriteria.
     * 
     * @param useProcessId
     */
    public void setUseProcessId(boolean useProcessId) {
        this.useProcessId = useProcessId;
    }


    /**
     * Gets the version value for this GetJobsUsingSearchCriteria.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this GetJobsUsingSearchCriteria.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the useVersion value for this GetJobsUsingSearchCriteria.
     * 
     * @return useVersion
     */
    public boolean isUseVersion() {
        return useVersion;
    }


    /**
     * Sets the useVersion value for this GetJobsUsingSearchCriteria.
     * 
     * @param useVersion
     */
    public void setUseVersion(boolean useVersion) {
        this.useVersion = useVersion;
    }


    /**
     * Gets the originServerId value for this GetJobsUsingSearchCriteria.
     * 
     * @return originServerId
     */
    public java.lang.String getOriginServerId() {
        return originServerId;
    }


    /**
     * Sets the originServerId value for this GetJobsUsingSearchCriteria.
     * 
     * @param originServerId
     */
    public void setOriginServerId(java.lang.String originServerId) {
        this.originServerId = originServerId;
    }


    /**
     * Gets the useOriginServerId value for this GetJobsUsingSearchCriteria.
     * 
     * @return useOriginServerId
     */
    public boolean isUseOriginServerId() {
        return useOriginServerId;
    }


    /**
     * Sets the useOriginServerId value for this GetJobsUsingSearchCriteria.
     * 
     * @param useOriginServerId
     */
    public void setUseOriginServerId(boolean useOriginServerId) {
        this.useOriginServerId = useOriginServerId;
    }


    /**
     * Gets the maxNumToRetrieve value for this GetJobsUsingSearchCriteria.
     * 
     * @return maxNumToRetrieve
     */
    public int getMaxNumToRetrieve() {
        return maxNumToRetrieve;
    }


    /**
     * Sets the maxNumToRetrieve value for this GetJobsUsingSearchCriteria.
     * 
     * @param maxNumToRetrieve
     */
    public void setMaxNumToRetrieve(int maxNumToRetrieve) {
        this.maxNumToRetrieve = maxNumToRetrieve;
    }


    /**
     * Gets the procTemplateVariables value for this GetJobsUsingSearchCriteria.
     * 
     * @return procTemplateVariables
     */
    public com.ecs.stellent.spp.Variable[] getProcTemplateVariables() {
        return procTemplateVariables;
    }


    /**
     * Sets the procTemplateVariables value for this GetJobsUsingSearchCriteria.
     * 
     * @param procTemplateVariables
     */
    public void setProcTemplateVariables(com.ecs.stellent.spp.Variable[] procTemplateVariables) {
        this.procTemplateVariables = procTemplateVariables;
    }


    /**
     * Gets the categoryId value for this GetJobsUsingSearchCriteria.
     * 
     * @return categoryId
     */
    public java.lang.String getCategoryId() {
        return categoryId;
    }


    /**
     * Sets the categoryId value for this GetJobsUsingSearchCriteria.
     * 
     * @param categoryId
     */
    public void setCategoryId(java.lang.String categoryId) {
        this.categoryId = categoryId;
    }


    /**
     * Gets the useCategoryId value for this GetJobsUsingSearchCriteria.
     * 
     * @return useCategoryId
     */
    public boolean isUseCategoryId() {
        return useCategoryId;
    }


    /**
     * Sets the useCategoryId value for this GetJobsUsingSearchCriteria.
     * 
     * @param useCategoryId
     */
    public void setUseCategoryId(boolean useCategoryId) {
        this.useCategoryId = useCategoryId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobsUsingSearchCriteria)) return false;
        GetJobsUsingSearchCriteria other = (GetJobsUsingSearchCriteria) obj;
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
            ((this.procTemplateVariables==null && other.getProcTemplateVariables()==null) || 
             (this.procTemplateVariables!=null &&
              java.util.Arrays.equals(this.procTemplateVariables, other.getProcTemplateVariables()))) &&
            ((this.categoryId==null && other.getCategoryId()==null) || 
             (this.categoryId!=null &&
              this.categoryId.equals(other.getCategoryId()))) &&
            this.useCategoryId == other.isUseCategoryId();
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
        if (getProcTemplateVariables() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProcTemplateVariables());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProcTemplateVariables(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCategoryId() != null) {
            _hashCode += getCategoryId().hashCode();
        }
        _hashCode += (isUseCategoryId() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobsUsingSearchCriteria.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria"));
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
        elemField.setFieldName("procTemplateVariables");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcTemplateVariables"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
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
