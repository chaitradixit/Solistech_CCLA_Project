/**
 * GetExtendedWorkQueue3.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetExtendedWorkQueue3  implements java.io.Serializable {
    private java.lang.String sessionId;

    private int numberOfActivities;

    private boolean useStartDueDate;

    private java.util.Calendar startDueDate;

    private long activityTypeFilter;

    private java.lang.String wrkQueueDefnName;

    private com.ecs.stellent.spp.FieldFilterConfig fieldFilter;

    private boolean returnJobIds;

    private short usePriority;

    private short prioritiesToRetrieve;

    private java.lang.String activityName;

    private java.lang.String resourceID;

    private boolean combinedWorkqueue;

    private boolean bUseJobRAGStatus;

    private short jobRAGStatus;

    private boolean bUseJobState;

    private java.lang.String jobState;

    private short pageDirection;

    public GetExtendedWorkQueue3() {
    }

    public GetExtendedWorkQueue3(
           java.lang.String sessionId,
           int numberOfActivities,
           boolean useStartDueDate,
           java.util.Calendar startDueDate,
           long activityTypeFilter,
           java.lang.String wrkQueueDefnName,
           com.ecs.stellent.spp.FieldFilterConfig fieldFilter,
           boolean returnJobIds,
           short usePriority,
           short prioritiesToRetrieve,
           java.lang.String activityName,
           java.lang.String resourceID,
           boolean combinedWorkqueue,
           boolean bUseJobRAGStatus,
           short jobRAGStatus,
           boolean bUseJobState,
           java.lang.String jobState,
           short pageDirection) {
           this.sessionId = sessionId;
           this.numberOfActivities = numberOfActivities;
           this.useStartDueDate = useStartDueDate;
           this.startDueDate = startDueDate;
           this.activityTypeFilter = activityTypeFilter;
           this.wrkQueueDefnName = wrkQueueDefnName;
           this.fieldFilter = fieldFilter;
           this.returnJobIds = returnJobIds;
           this.usePriority = usePriority;
           this.prioritiesToRetrieve = prioritiesToRetrieve;
           this.activityName = activityName;
           this.resourceID = resourceID;
           this.combinedWorkqueue = combinedWorkqueue;
           this.bUseJobRAGStatus = bUseJobRAGStatus;
           this.jobRAGStatus = jobRAGStatus;
           this.bUseJobState = bUseJobState;
           this.jobState = jobState;
           this.pageDirection = pageDirection;
    }


    /**
     * Gets the sessionId value for this GetExtendedWorkQueue3.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetExtendedWorkQueue3.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the numberOfActivities value for this GetExtendedWorkQueue3.
     * 
     * @return numberOfActivities
     */
    public int getNumberOfActivities() {
        return numberOfActivities;
    }


    /**
     * Sets the numberOfActivities value for this GetExtendedWorkQueue3.
     * 
     * @param numberOfActivities
     */
    public void setNumberOfActivities(int numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
    }


    /**
     * Gets the useStartDueDate value for this GetExtendedWorkQueue3.
     * 
     * @return useStartDueDate
     */
    public boolean isUseStartDueDate() {
        return useStartDueDate;
    }


    /**
     * Sets the useStartDueDate value for this GetExtendedWorkQueue3.
     * 
     * @param useStartDueDate
     */
    public void setUseStartDueDate(boolean useStartDueDate) {
        this.useStartDueDate = useStartDueDate;
    }


    /**
     * Gets the startDueDate value for this GetExtendedWorkQueue3.
     * 
     * @return startDueDate
     */
    public java.util.Calendar getStartDueDate() {
        return startDueDate;
    }


    /**
     * Sets the startDueDate value for this GetExtendedWorkQueue3.
     * 
     * @param startDueDate
     */
    public void setStartDueDate(java.util.Calendar startDueDate) {
        this.startDueDate = startDueDate;
    }


    /**
     * Gets the activityTypeFilter value for this GetExtendedWorkQueue3.
     * 
     * @return activityTypeFilter
     */
    public long getActivityTypeFilter() {
        return activityTypeFilter;
    }


    /**
     * Sets the activityTypeFilter value for this GetExtendedWorkQueue3.
     * 
     * @param activityTypeFilter
     */
    public void setActivityTypeFilter(long activityTypeFilter) {
        this.activityTypeFilter = activityTypeFilter;
    }


    /**
     * Gets the wrkQueueDefnName value for this GetExtendedWorkQueue3.
     * 
     * @return wrkQueueDefnName
     */
    public java.lang.String getWrkQueueDefnName() {
        return wrkQueueDefnName;
    }


    /**
     * Sets the wrkQueueDefnName value for this GetExtendedWorkQueue3.
     * 
     * @param wrkQueueDefnName
     */
    public void setWrkQueueDefnName(java.lang.String wrkQueueDefnName) {
        this.wrkQueueDefnName = wrkQueueDefnName;
    }


    /**
     * Gets the fieldFilter value for this GetExtendedWorkQueue3.
     * 
     * @return fieldFilter
     */
    public com.ecs.stellent.spp.FieldFilterConfig getFieldFilter() {
        return fieldFilter;
    }


    /**
     * Sets the fieldFilter value for this GetExtendedWorkQueue3.
     * 
     * @param fieldFilter
     */
    public void setFieldFilter(com.ecs.stellent.spp.FieldFilterConfig fieldFilter) {
        this.fieldFilter = fieldFilter;
    }


    /**
     * Gets the returnJobIds value for this GetExtendedWorkQueue3.
     * 
     * @return returnJobIds
     */
    public boolean isReturnJobIds() {
        return returnJobIds;
    }


    /**
     * Sets the returnJobIds value for this GetExtendedWorkQueue3.
     * 
     * @param returnJobIds
     */
    public void setReturnJobIds(boolean returnJobIds) {
        this.returnJobIds = returnJobIds;
    }


    /**
     * Gets the usePriority value for this GetExtendedWorkQueue3.
     * 
     * @return usePriority
     */
    public short getUsePriority() {
        return usePriority;
    }


    /**
     * Sets the usePriority value for this GetExtendedWorkQueue3.
     * 
     * @param usePriority
     */
    public void setUsePriority(short usePriority) {
        this.usePriority = usePriority;
    }


    /**
     * Gets the prioritiesToRetrieve value for this GetExtendedWorkQueue3.
     * 
     * @return prioritiesToRetrieve
     */
    public short getPrioritiesToRetrieve() {
        return prioritiesToRetrieve;
    }


    /**
     * Sets the prioritiesToRetrieve value for this GetExtendedWorkQueue3.
     * 
     * @param prioritiesToRetrieve
     */
    public void setPrioritiesToRetrieve(short prioritiesToRetrieve) {
        this.prioritiesToRetrieve = prioritiesToRetrieve;
    }


    /**
     * Gets the activityName value for this GetExtendedWorkQueue3.
     * 
     * @return activityName
     */
    public java.lang.String getActivityName() {
        return activityName;
    }


    /**
     * Sets the activityName value for this GetExtendedWorkQueue3.
     * 
     * @param activityName
     */
    public void setActivityName(java.lang.String activityName) {
        this.activityName = activityName;
    }


    /**
     * Gets the resourceID value for this GetExtendedWorkQueue3.
     * 
     * @return resourceID
     */
    public java.lang.String getResourceID() {
        return resourceID;
    }


    /**
     * Sets the resourceID value for this GetExtendedWorkQueue3.
     * 
     * @param resourceID
     */
    public void setResourceID(java.lang.String resourceID) {
        this.resourceID = resourceID;
    }


    /**
     * Gets the combinedWorkqueue value for this GetExtendedWorkQueue3.
     * 
     * @return combinedWorkqueue
     */
    public boolean isCombinedWorkqueue() {
        return combinedWorkqueue;
    }


    /**
     * Sets the combinedWorkqueue value for this GetExtendedWorkQueue3.
     * 
     * @param combinedWorkqueue
     */
    public void setCombinedWorkqueue(boolean combinedWorkqueue) {
        this.combinedWorkqueue = combinedWorkqueue;
    }


    /**
     * Gets the bUseJobRAGStatus value for this GetExtendedWorkQueue3.
     * 
     * @return bUseJobRAGStatus
     */
    public boolean isBUseJobRAGStatus() {
        return bUseJobRAGStatus;
    }


    /**
     * Sets the bUseJobRAGStatus value for this GetExtendedWorkQueue3.
     * 
     * @param bUseJobRAGStatus
     */
    public void setBUseJobRAGStatus(boolean bUseJobRAGStatus) {
        this.bUseJobRAGStatus = bUseJobRAGStatus;
    }


    /**
     * Gets the jobRAGStatus value for this GetExtendedWorkQueue3.
     * 
     * @return jobRAGStatus
     */
    public short getJobRAGStatus() {
        return jobRAGStatus;
    }


    /**
     * Sets the jobRAGStatus value for this GetExtendedWorkQueue3.
     * 
     * @param jobRAGStatus
     */
    public void setJobRAGStatus(short jobRAGStatus) {
        this.jobRAGStatus = jobRAGStatus;
    }


    /**
     * Gets the bUseJobState value for this GetExtendedWorkQueue3.
     * 
     * @return bUseJobState
     */
    public boolean isBUseJobState() {
        return bUseJobState;
    }


    /**
     * Sets the bUseJobState value for this GetExtendedWorkQueue3.
     * 
     * @param bUseJobState
     */
    public void setBUseJobState(boolean bUseJobState) {
        this.bUseJobState = bUseJobState;
    }


    /**
     * Gets the jobState value for this GetExtendedWorkQueue3.
     * 
     * @return jobState
     */
    public java.lang.String getJobState() {
        return jobState;
    }


    /**
     * Sets the jobState value for this GetExtendedWorkQueue3.
     * 
     * @param jobState
     */
    public void setJobState(java.lang.String jobState) {
        this.jobState = jobState;
    }


    /**
     * Gets the pageDirection value for this GetExtendedWorkQueue3.
     * 
     * @return pageDirection
     */
    public short getPageDirection() {
        return pageDirection;
    }


    /**
     * Sets the pageDirection value for this GetExtendedWorkQueue3.
     * 
     * @param pageDirection
     */
    public void setPageDirection(short pageDirection) {
        this.pageDirection = pageDirection;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtendedWorkQueue3)) return false;
        GetExtendedWorkQueue3 other = (GetExtendedWorkQueue3) obj;
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
            this.numberOfActivities == other.getNumberOfActivities() &&
            this.useStartDueDate == other.isUseStartDueDate() &&
            ((this.startDueDate==null && other.getStartDueDate()==null) || 
             (this.startDueDate!=null &&
              this.startDueDate.equals(other.getStartDueDate()))) &&
            this.activityTypeFilter == other.getActivityTypeFilter() &&
            ((this.wrkQueueDefnName==null && other.getWrkQueueDefnName()==null) || 
             (this.wrkQueueDefnName!=null &&
              this.wrkQueueDefnName.equals(other.getWrkQueueDefnName()))) &&
            ((this.fieldFilter==null && other.getFieldFilter()==null) || 
             (this.fieldFilter!=null &&
              this.fieldFilter.equals(other.getFieldFilter()))) &&
            this.returnJobIds == other.isReturnJobIds() &&
            this.usePriority == other.getUsePriority() &&
            this.prioritiesToRetrieve == other.getPrioritiesToRetrieve() &&
            ((this.activityName==null && other.getActivityName()==null) || 
             (this.activityName!=null &&
              this.activityName.equals(other.getActivityName()))) &&
            ((this.resourceID==null && other.getResourceID()==null) || 
             (this.resourceID!=null &&
              this.resourceID.equals(other.getResourceID()))) &&
            this.combinedWorkqueue == other.isCombinedWorkqueue() &&
            this.bUseJobRAGStatus == other.isBUseJobRAGStatus() &&
            this.jobRAGStatus == other.getJobRAGStatus() &&
            this.bUseJobState == other.isBUseJobState() &&
            ((this.jobState==null && other.getJobState()==null) || 
             (this.jobState!=null &&
              this.jobState.equals(other.getJobState()))) &&
            this.pageDirection == other.getPageDirection();
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
        _hashCode += getNumberOfActivities();
        _hashCode += (isUseStartDueDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStartDueDate() != null) {
            _hashCode += getStartDueDate().hashCode();
        }
        _hashCode += new Long(getActivityTypeFilter()).hashCode();
        if (getWrkQueueDefnName() != null) {
            _hashCode += getWrkQueueDefnName().hashCode();
        }
        if (getFieldFilter() != null) {
            _hashCode += getFieldFilter().hashCode();
        }
        _hashCode += (isReturnJobIds() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getUsePriority();
        _hashCode += getPrioritiesToRetrieve();
        if (getActivityName() != null) {
            _hashCode += getActivityName().hashCode();
        }
        if (getResourceID() != null) {
            _hashCode += getResourceID().hashCode();
        }
        _hashCode += (isCombinedWorkqueue() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isBUseJobRAGStatus() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getJobRAGStatus();
        _hashCode += (isBUseJobState() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getJobState() != null) {
            _hashCode += getJobState().hashCode();
        }
        _hashCode += getPageDirection();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtendedWorkQueue3.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue3"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfActivities");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivities"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useStartDueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activityTypeFilter");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityTypeFilter"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("wrkQueueDefnName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WrkQueueDefnName"));
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
        elemField.setFieldName("returnJobIds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReturnJobIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usePriority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prioritiesToRetrieve");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrioritiesToRetrieve"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activityName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourceID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("combinedWorkqueue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CombinedWorkqueue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BUseJobRAGStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "bUseJobRAGStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobRAGStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobRAGStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BUseJobState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "bUseJobState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("pageDirection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PageDirection"));
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
