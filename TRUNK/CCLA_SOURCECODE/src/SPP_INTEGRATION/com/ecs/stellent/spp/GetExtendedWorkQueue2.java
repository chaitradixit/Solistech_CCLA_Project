/**
 * GetExtendedWorkQueue2.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetExtendedWorkQueue2  implements java.io.Serializable {
    private java.lang.String sessionId;

    private int numberOfActivities;

    private boolean useStartDueDate;

    private java.util.Calendar startDueDate;

    private short activityTypeFilter;

    private java.lang.String wrkQueueDefnName;

    private com.ecs.stellent.spp.FieldFilterConfig fieldFilter;

    private boolean returnJobIds;

    private short usePriority;

    private short prioritiesToRetrieve;

    private java.lang.String activityName;

    private java.lang.String resourceID;

    private boolean combinedWorkqueue;

    private short pageDirection;

    public GetExtendedWorkQueue2() {
    }

    public GetExtendedWorkQueue2(
           java.lang.String sessionId,
           int numberOfActivities,
           boolean useStartDueDate,
           java.util.Calendar startDueDate,
           short activityTypeFilter,
           java.lang.String wrkQueueDefnName,
           com.ecs.stellent.spp.FieldFilterConfig fieldFilter,
           boolean returnJobIds,
           short usePriority,
           short prioritiesToRetrieve,
           java.lang.String activityName,
           java.lang.String resourceID,
           boolean combinedWorkqueue,
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
           this.pageDirection = pageDirection;
    }


    /**
     * Gets the sessionId value for this GetExtendedWorkQueue2.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetExtendedWorkQueue2.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the numberOfActivities value for this GetExtendedWorkQueue2.
     * 
     * @return numberOfActivities
     */
    public int getNumberOfActivities() {
        return numberOfActivities;
    }


    /**
     * Sets the numberOfActivities value for this GetExtendedWorkQueue2.
     * 
     * @param numberOfActivities
     */
    public void setNumberOfActivities(int numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
    }


    /**
     * Gets the useStartDueDate value for this GetExtendedWorkQueue2.
     * 
     * @return useStartDueDate
     */
    public boolean isUseStartDueDate() {
        return useStartDueDate;
    }


    /**
     * Sets the useStartDueDate value for this GetExtendedWorkQueue2.
     * 
     * @param useStartDueDate
     */
    public void setUseStartDueDate(boolean useStartDueDate) {
        this.useStartDueDate = useStartDueDate;
    }


    /**
     * Gets the startDueDate value for this GetExtendedWorkQueue2.
     * 
     * @return startDueDate
     */
    public java.util.Calendar getStartDueDate() {
        return startDueDate;
    }


    /**
     * Sets the startDueDate value for this GetExtendedWorkQueue2.
     * 
     * @param startDueDate
     */
    public void setStartDueDate(java.util.Calendar startDueDate) {
        this.startDueDate = startDueDate;
    }


    /**
     * Gets the activityTypeFilter value for this GetExtendedWorkQueue2.
     * 
     * @return activityTypeFilter
     */
    public short getActivityTypeFilter() {
        return activityTypeFilter;
    }


    /**
     * Sets the activityTypeFilter value for this GetExtendedWorkQueue2.
     * 
     * @param activityTypeFilter
     */
    public void setActivityTypeFilter(short activityTypeFilter) {
        this.activityTypeFilter = activityTypeFilter;
    }


    /**
     * Gets the wrkQueueDefnName value for this GetExtendedWorkQueue2.
     * 
     * @return wrkQueueDefnName
     */
    public java.lang.String getWrkQueueDefnName() {
        return wrkQueueDefnName;
    }


    /**
     * Sets the wrkQueueDefnName value for this GetExtendedWorkQueue2.
     * 
     * @param wrkQueueDefnName
     */
    public void setWrkQueueDefnName(java.lang.String wrkQueueDefnName) {
        this.wrkQueueDefnName = wrkQueueDefnName;
    }


    /**
     * Gets the fieldFilter value for this GetExtendedWorkQueue2.
     * 
     * @return fieldFilter
     */
    public com.ecs.stellent.spp.FieldFilterConfig getFieldFilter() {
        return fieldFilter;
    }


    /**
     * Sets the fieldFilter value for this GetExtendedWorkQueue2.
     * 
     * @param fieldFilter
     */
    public void setFieldFilter(com.ecs.stellent.spp.FieldFilterConfig fieldFilter) {
        this.fieldFilter = fieldFilter;
    }


    /**
     * Gets the returnJobIds value for this GetExtendedWorkQueue2.
     * 
     * @return returnJobIds
     */
    public boolean isReturnJobIds() {
        return returnJobIds;
    }


    /**
     * Sets the returnJobIds value for this GetExtendedWorkQueue2.
     * 
     * @param returnJobIds
     */
    public void setReturnJobIds(boolean returnJobIds) {
        this.returnJobIds = returnJobIds;
    }


    /**
     * Gets the usePriority value for this GetExtendedWorkQueue2.
     * 
     * @return usePriority
     */
    public short getUsePriority() {
        return usePriority;
    }


    /**
     * Sets the usePriority value for this GetExtendedWorkQueue2.
     * 
     * @param usePriority
     */
    public void setUsePriority(short usePriority) {
        this.usePriority = usePriority;
    }


    /**
     * Gets the prioritiesToRetrieve value for this GetExtendedWorkQueue2.
     * 
     * @return prioritiesToRetrieve
     */
    public short getPrioritiesToRetrieve() {
        return prioritiesToRetrieve;
    }


    /**
     * Sets the prioritiesToRetrieve value for this GetExtendedWorkQueue2.
     * 
     * @param prioritiesToRetrieve
     */
    public void setPrioritiesToRetrieve(short prioritiesToRetrieve) {
        this.prioritiesToRetrieve = prioritiesToRetrieve;
    }


    /**
     * Gets the activityName value for this GetExtendedWorkQueue2.
     * 
     * @return activityName
     */
    public java.lang.String getActivityName() {
        return activityName;
    }


    /**
     * Sets the activityName value for this GetExtendedWorkQueue2.
     * 
     * @param activityName
     */
    public void setActivityName(java.lang.String activityName) {
        this.activityName = activityName;
    }


    /**
     * Gets the resourceID value for this GetExtendedWorkQueue2.
     * 
     * @return resourceID
     */
    public java.lang.String getResourceID() {
        return resourceID;
    }


    /**
     * Sets the resourceID value for this GetExtendedWorkQueue2.
     * 
     * @param resourceID
     */
    public void setResourceID(java.lang.String resourceID) {
        this.resourceID = resourceID;
    }


    /**
     * Gets the combinedWorkqueue value for this GetExtendedWorkQueue2.
     * 
     * @return combinedWorkqueue
     */
    public boolean isCombinedWorkqueue() {
        return combinedWorkqueue;
    }


    /**
     * Sets the combinedWorkqueue value for this GetExtendedWorkQueue2.
     * 
     * @param combinedWorkqueue
     */
    public void setCombinedWorkqueue(boolean combinedWorkqueue) {
        this.combinedWorkqueue = combinedWorkqueue;
    }


    /**
     * Gets the pageDirection value for this GetExtendedWorkQueue2.
     * 
     * @return pageDirection
     */
    public short getPageDirection() {
        return pageDirection;
    }


    /**
     * Sets the pageDirection value for this GetExtendedWorkQueue2.
     * 
     * @param pageDirection
     */
    public void setPageDirection(short pageDirection) {
        this.pageDirection = pageDirection;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtendedWorkQueue2)) return false;
        GetExtendedWorkQueue2 other = (GetExtendedWorkQueue2) obj;
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
        _hashCode += getActivityTypeFilter();
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
        _hashCode += getPageDirection();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtendedWorkQueue2.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue2"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
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
