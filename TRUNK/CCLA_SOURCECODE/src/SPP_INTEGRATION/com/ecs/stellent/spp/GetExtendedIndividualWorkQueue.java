/**
 * GetExtendedIndividualWorkQueue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetExtendedIndividualWorkQueue  implements java.io.Serializable {
    private java.lang.String sessionId;

    private int numberOfActivitiesRq;

    private boolean useStartDueDate;

    private java.util.Calendar startDueDate;

    private short activityTypeFilter;

    private boolean useActivityName;

    private java.lang.String activityName;

    private java.lang.String wrkQueueDefnName;

    private com.ecs.stellent.spp.FieldFilterConfig fieldFilter;

    private boolean returnJobIds;

    private short usePriority;

    private short priorityToRetrieve;

    private boolean useLastDueDate;

    private java.util.Calendar lastDueDate;

    public GetExtendedIndividualWorkQueue() {
    }

    public GetExtendedIndividualWorkQueue(
           java.lang.String sessionId,
           int numberOfActivitiesRq,
           boolean useStartDueDate,
           java.util.Calendar startDueDate,
           short activityTypeFilter,
           boolean useActivityName,
           java.lang.String activityName,
           java.lang.String wrkQueueDefnName,
           com.ecs.stellent.spp.FieldFilterConfig fieldFilter,
           boolean returnJobIds,
           short usePriority,
           short priorityToRetrieve,
           boolean useLastDueDate,
           java.util.Calendar lastDueDate) {
           this.sessionId = sessionId;
           this.numberOfActivitiesRq = numberOfActivitiesRq;
           this.useStartDueDate = useStartDueDate;
           this.startDueDate = startDueDate;
           this.activityTypeFilter = activityTypeFilter;
           this.useActivityName = useActivityName;
           this.activityName = activityName;
           this.wrkQueueDefnName = wrkQueueDefnName;
           this.fieldFilter = fieldFilter;
           this.returnJobIds = returnJobIds;
           this.usePriority = usePriority;
           this.priorityToRetrieve = priorityToRetrieve;
           this.useLastDueDate = useLastDueDate;
           this.lastDueDate = lastDueDate;
    }


    /**
     * Gets the sessionId value for this GetExtendedIndividualWorkQueue.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetExtendedIndividualWorkQueue.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the numberOfActivitiesRq value for this GetExtendedIndividualWorkQueue.
     * 
     * @return numberOfActivitiesRq
     */
    public int getNumberOfActivitiesRq() {
        return numberOfActivitiesRq;
    }


    /**
     * Sets the numberOfActivitiesRq value for this GetExtendedIndividualWorkQueue.
     * 
     * @param numberOfActivitiesRq
     */
    public void setNumberOfActivitiesRq(int numberOfActivitiesRq) {
        this.numberOfActivitiesRq = numberOfActivitiesRq;
    }


    /**
     * Gets the useStartDueDate value for this GetExtendedIndividualWorkQueue.
     * 
     * @return useStartDueDate
     */
    public boolean isUseStartDueDate() {
        return useStartDueDate;
    }


    /**
     * Sets the useStartDueDate value for this GetExtendedIndividualWorkQueue.
     * 
     * @param useStartDueDate
     */
    public void setUseStartDueDate(boolean useStartDueDate) {
        this.useStartDueDate = useStartDueDate;
    }


    /**
     * Gets the startDueDate value for this GetExtendedIndividualWorkQueue.
     * 
     * @return startDueDate
     */
    public java.util.Calendar getStartDueDate() {
        return startDueDate;
    }


    /**
     * Sets the startDueDate value for this GetExtendedIndividualWorkQueue.
     * 
     * @param startDueDate
     */
    public void setStartDueDate(java.util.Calendar startDueDate) {
        this.startDueDate = startDueDate;
    }


    /**
     * Gets the activityTypeFilter value for this GetExtendedIndividualWorkQueue.
     * 
     * @return activityTypeFilter
     */
    public short getActivityTypeFilter() {
        return activityTypeFilter;
    }


    /**
     * Sets the activityTypeFilter value for this GetExtendedIndividualWorkQueue.
     * 
     * @param activityTypeFilter
     */
    public void setActivityTypeFilter(short activityTypeFilter) {
        this.activityTypeFilter = activityTypeFilter;
    }


    /**
     * Gets the useActivityName value for this GetExtendedIndividualWorkQueue.
     * 
     * @return useActivityName
     */
    public boolean isUseActivityName() {
        return useActivityName;
    }


    /**
     * Sets the useActivityName value for this GetExtendedIndividualWorkQueue.
     * 
     * @param useActivityName
     */
    public void setUseActivityName(boolean useActivityName) {
        this.useActivityName = useActivityName;
    }


    /**
     * Gets the activityName value for this GetExtendedIndividualWorkQueue.
     * 
     * @return activityName
     */
    public java.lang.String getActivityName() {
        return activityName;
    }


    /**
     * Sets the activityName value for this GetExtendedIndividualWorkQueue.
     * 
     * @param activityName
     */
    public void setActivityName(java.lang.String activityName) {
        this.activityName = activityName;
    }


    /**
     * Gets the wrkQueueDefnName value for this GetExtendedIndividualWorkQueue.
     * 
     * @return wrkQueueDefnName
     */
    public java.lang.String getWrkQueueDefnName() {
        return wrkQueueDefnName;
    }


    /**
     * Sets the wrkQueueDefnName value for this GetExtendedIndividualWorkQueue.
     * 
     * @param wrkQueueDefnName
     */
    public void setWrkQueueDefnName(java.lang.String wrkQueueDefnName) {
        this.wrkQueueDefnName = wrkQueueDefnName;
    }


    /**
     * Gets the fieldFilter value for this GetExtendedIndividualWorkQueue.
     * 
     * @return fieldFilter
     */
    public com.ecs.stellent.spp.FieldFilterConfig getFieldFilter() {
        return fieldFilter;
    }


    /**
     * Sets the fieldFilter value for this GetExtendedIndividualWorkQueue.
     * 
     * @param fieldFilter
     */
    public void setFieldFilter(com.ecs.stellent.spp.FieldFilterConfig fieldFilter) {
        this.fieldFilter = fieldFilter;
    }


    /**
     * Gets the returnJobIds value for this GetExtendedIndividualWorkQueue.
     * 
     * @return returnJobIds
     */
    public boolean isReturnJobIds() {
        return returnJobIds;
    }


    /**
     * Sets the returnJobIds value for this GetExtendedIndividualWorkQueue.
     * 
     * @param returnJobIds
     */
    public void setReturnJobIds(boolean returnJobIds) {
        this.returnJobIds = returnJobIds;
    }


    /**
     * Gets the usePriority value for this GetExtendedIndividualWorkQueue.
     * 
     * @return usePriority
     */
    public short getUsePriority() {
        return usePriority;
    }


    /**
     * Sets the usePriority value for this GetExtendedIndividualWorkQueue.
     * 
     * @param usePriority
     */
    public void setUsePriority(short usePriority) {
        this.usePriority = usePriority;
    }


    /**
     * Gets the priorityToRetrieve value for this GetExtendedIndividualWorkQueue.
     * 
     * @return priorityToRetrieve
     */
    public short getPriorityToRetrieve() {
        return priorityToRetrieve;
    }


    /**
     * Sets the priorityToRetrieve value for this GetExtendedIndividualWorkQueue.
     * 
     * @param priorityToRetrieve
     */
    public void setPriorityToRetrieve(short priorityToRetrieve) {
        this.priorityToRetrieve = priorityToRetrieve;
    }


    /**
     * Gets the useLastDueDate value for this GetExtendedIndividualWorkQueue.
     * 
     * @return useLastDueDate
     */
    public boolean isUseLastDueDate() {
        return useLastDueDate;
    }


    /**
     * Sets the useLastDueDate value for this GetExtendedIndividualWorkQueue.
     * 
     * @param useLastDueDate
     */
    public void setUseLastDueDate(boolean useLastDueDate) {
        this.useLastDueDate = useLastDueDate;
    }


    /**
     * Gets the lastDueDate value for this GetExtendedIndividualWorkQueue.
     * 
     * @return lastDueDate
     */
    public java.util.Calendar getLastDueDate() {
        return lastDueDate;
    }


    /**
     * Sets the lastDueDate value for this GetExtendedIndividualWorkQueue.
     * 
     * @param lastDueDate
     */
    public void setLastDueDate(java.util.Calendar lastDueDate) {
        this.lastDueDate = lastDueDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtendedIndividualWorkQueue)) return false;
        GetExtendedIndividualWorkQueue other = (GetExtendedIndividualWorkQueue) obj;
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
            this.numberOfActivitiesRq == other.getNumberOfActivitiesRq() &&
            this.useStartDueDate == other.isUseStartDueDate() &&
            ((this.startDueDate==null && other.getStartDueDate()==null) || 
             (this.startDueDate!=null &&
              this.startDueDate.equals(other.getStartDueDate()))) &&
            this.activityTypeFilter == other.getActivityTypeFilter() &&
            this.useActivityName == other.isUseActivityName() &&
            ((this.activityName==null && other.getActivityName()==null) || 
             (this.activityName!=null &&
              this.activityName.equals(other.getActivityName()))) &&
            ((this.wrkQueueDefnName==null && other.getWrkQueueDefnName()==null) || 
             (this.wrkQueueDefnName!=null &&
              this.wrkQueueDefnName.equals(other.getWrkQueueDefnName()))) &&
            ((this.fieldFilter==null && other.getFieldFilter()==null) || 
             (this.fieldFilter!=null &&
              this.fieldFilter.equals(other.getFieldFilter()))) &&
            this.returnJobIds == other.isReturnJobIds() &&
            this.usePriority == other.getUsePriority() &&
            this.priorityToRetrieve == other.getPriorityToRetrieve() &&
            this.useLastDueDate == other.isUseLastDueDate() &&
            ((this.lastDueDate==null && other.getLastDueDate()==null) || 
             (this.lastDueDate!=null &&
              this.lastDueDate.equals(other.getLastDueDate())));
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
        _hashCode += getNumberOfActivitiesRq();
        _hashCode += (isUseStartDueDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStartDueDate() != null) {
            _hashCode += getStartDueDate().hashCode();
        }
        _hashCode += getActivityTypeFilter();
        _hashCode += (isUseActivityName() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getActivityName() != null) {
            _hashCode += getActivityName().hashCode();
        }
        if (getWrkQueueDefnName() != null) {
            _hashCode += getWrkQueueDefnName().hashCode();
        }
        if (getFieldFilter() != null) {
            _hashCode += getFieldFilter().hashCode();
        }
        _hashCode += (isReturnJobIds() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getUsePriority();
        _hashCode += getPriorityToRetrieve();
        _hashCode += (isUseLastDueDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLastDueDate() != null) {
            _hashCode += getLastDueDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtendedIndividualWorkQueue.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedIndividualWorkQueue"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfActivitiesRq");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivitiesRq"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useActivityName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseActivityName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
        elemField.setFieldName("priorityToRetrieve");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PriorityToRetrieve"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useLastDueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseLastDueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastDueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastDueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
