/**
 * GetExtendedWorkQueueUsingActivityName.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetExtendedWorkQueueUsingActivityName  implements java.io.Serializable {
    private java.lang.String sessionId;

    private int numberOfActivities;

    private boolean useStartDueDate;

    private java.util.Calendar startDateDue;

    private short activityTypeFilter;

    private java.lang.String activityName;

    private java.lang.String wrkQueueDefnName;

    private com.ecs.stellent.spp.FieldFilterConfig fieldFilter;

    private boolean returnJobIds;

    private short usePriority;

    private short prioritiesToRetrieve;

    private boolean useLastDueDate;

    private java.util.Calendar lastDueDate;

    public GetExtendedWorkQueueUsingActivityName() {
    }

    public GetExtendedWorkQueueUsingActivityName(
           java.lang.String sessionId,
           int numberOfActivities,
           boolean useStartDueDate,
           java.util.Calendar startDateDue,
           short activityTypeFilter,
           java.lang.String activityName,
           java.lang.String wrkQueueDefnName,
           com.ecs.stellent.spp.FieldFilterConfig fieldFilter,
           boolean returnJobIds,
           short usePriority,
           short prioritiesToRetrieve,
           boolean useLastDueDate,
           java.util.Calendar lastDueDate) {
           this.sessionId = sessionId;
           this.numberOfActivities = numberOfActivities;
           this.useStartDueDate = useStartDueDate;
           this.startDateDue = startDateDue;
           this.activityTypeFilter = activityTypeFilter;
           this.activityName = activityName;
           this.wrkQueueDefnName = wrkQueueDefnName;
           this.fieldFilter = fieldFilter;
           this.returnJobIds = returnJobIds;
           this.usePriority = usePriority;
           this.prioritiesToRetrieve = prioritiesToRetrieve;
           this.useLastDueDate = useLastDueDate;
           this.lastDueDate = lastDueDate;
    }


    /**
     * Gets the sessionId value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the numberOfActivities value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return numberOfActivities
     */
    public int getNumberOfActivities() {
        return numberOfActivities;
    }


    /**
     * Sets the numberOfActivities value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param numberOfActivities
     */
    public void setNumberOfActivities(int numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
    }


    /**
     * Gets the useStartDueDate value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return useStartDueDate
     */
    public boolean isUseStartDueDate() {
        return useStartDueDate;
    }


    /**
     * Sets the useStartDueDate value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param useStartDueDate
     */
    public void setUseStartDueDate(boolean useStartDueDate) {
        this.useStartDueDate = useStartDueDate;
    }


    /**
     * Gets the startDateDue value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return startDateDue
     */
    public java.util.Calendar getStartDateDue() {
        return startDateDue;
    }


    /**
     * Sets the startDateDue value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param startDateDue
     */
    public void setStartDateDue(java.util.Calendar startDateDue) {
        this.startDateDue = startDateDue;
    }


    /**
     * Gets the activityTypeFilter value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return activityTypeFilter
     */
    public short getActivityTypeFilter() {
        return activityTypeFilter;
    }


    /**
     * Sets the activityTypeFilter value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param activityTypeFilter
     */
    public void setActivityTypeFilter(short activityTypeFilter) {
        this.activityTypeFilter = activityTypeFilter;
    }


    /**
     * Gets the activityName value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return activityName
     */
    public java.lang.String getActivityName() {
        return activityName;
    }


    /**
     * Sets the activityName value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param activityName
     */
    public void setActivityName(java.lang.String activityName) {
        this.activityName = activityName;
    }


    /**
     * Gets the wrkQueueDefnName value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return wrkQueueDefnName
     */
    public java.lang.String getWrkQueueDefnName() {
        return wrkQueueDefnName;
    }


    /**
     * Sets the wrkQueueDefnName value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param wrkQueueDefnName
     */
    public void setWrkQueueDefnName(java.lang.String wrkQueueDefnName) {
        this.wrkQueueDefnName = wrkQueueDefnName;
    }


    /**
     * Gets the fieldFilter value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return fieldFilter
     */
    public com.ecs.stellent.spp.FieldFilterConfig getFieldFilter() {
        return fieldFilter;
    }


    /**
     * Sets the fieldFilter value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param fieldFilter
     */
    public void setFieldFilter(com.ecs.stellent.spp.FieldFilterConfig fieldFilter) {
        this.fieldFilter = fieldFilter;
    }


    /**
     * Gets the returnJobIds value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return returnJobIds
     */
    public boolean isReturnJobIds() {
        return returnJobIds;
    }


    /**
     * Sets the returnJobIds value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param returnJobIds
     */
    public void setReturnJobIds(boolean returnJobIds) {
        this.returnJobIds = returnJobIds;
    }


    /**
     * Gets the usePriority value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return usePriority
     */
    public short getUsePriority() {
        return usePriority;
    }


    /**
     * Sets the usePriority value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param usePriority
     */
    public void setUsePriority(short usePriority) {
        this.usePriority = usePriority;
    }


    /**
     * Gets the prioritiesToRetrieve value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return prioritiesToRetrieve
     */
    public short getPrioritiesToRetrieve() {
        return prioritiesToRetrieve;
    }


    /**
     * Sets the prioritiesToRetrieve value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param prioritiesToRetrieve
     */
    public void setPrioritiesToRetrieve(short prioritiesToRetrieve) {
        this.prioritiesToRetrieve = prioritiesToRetrieve;
    }


    /**
     * Gets the useLastDueDate value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return useLastDueDate
     */
    public boolean isUseLastDueDate() {
        return useLastDueDate;
    }


    /**
     * Sets the useLastDueDate value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param useLastDueDate
     */
    public void setUseLastDueDate(boolean useLastDueDate) {
        this.useLastDueDate = useLastDueDate;
    }


    /**
     * Gets the lastDueDate value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @return lastDueDate
     */
    public java.util.Calendar getLastDueDate() {
        return lastDueDate;
    }


    /**
     * Sets the lastDueDate value for this GetExtendedWorkQueueUsingActivityName.
     * 
     * @param lastDueDate
     */
    public void setLastDueDate(java.util.Calendar lastDueDate) {
        this.lastDueDate = lastDueDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtendedWorkQueueUsingActivityName)) return false;
        GetExtendedWorkQueueUsingActivityName other = (GetExtendedWorkQueueUsingActivityName) obj;
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
            ((this.startDateDue==null && other.getStartDateDue()==null) || 
             (this.startDateDue!=null &&
              this.startDateDue.equals(other.getStartDateDue()))) &&
            this.activityTypeFilter == other.getActivityTypeFilter() &&
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
            this.prioritiesToRetrieve == other.getPrioritiesToRetrieve() &&
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
        _hashCode += getNumberOfActivities();
        _hashCode += (isUseStartDueDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStartDateDue() != null) {
            _hashCode += getStartDateDue().hashCode();
        }
        _hashCode += getActivityTypeFilter();
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
        _hashCode += getPrioritiesToRetrieve();
        _hashCode += (isUseLastDueDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLastDueDate() != null) {
            _hashCode += getLastDueDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtendedWorkQueueUsingActivityName.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueueUsingActivityName"));
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
        elemField.setFieldName("startDateDue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDateDue"));
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
        elemField.setFieldName("prioritiesToRetrieve");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrioritiesToRetrieve"));
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
