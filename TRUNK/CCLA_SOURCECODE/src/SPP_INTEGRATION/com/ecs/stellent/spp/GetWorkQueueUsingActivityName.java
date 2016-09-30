/**
 * GetWorkQueueUsingActivityName.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetWorkQueueUsingActivityName  implements java.io.Serializable {
    private java.lang.String sessionId;

    private int numberOfActivities;

    private boolean useStartDueDate;

    private java.util.Calendar startDateDue;

    private short activityTypeFilter;

    private java.lang.String activityName;

    public GetWorkQueueUsingActivityName() {
    }

    public GetWorkQueueUsingActivityName(
           java.lang.String sessionId,
           int numberOfActivities,
           boolean useStartDueDate,
           java.util.Calendar startDateDue,
           short activityTypeFilter,
           java.lang.String activityName) {
           this.sessionId = sessionId;
           this.numberOfActivities = numberOfActivities;
           this.useStartDueDate = useStartDueDate;
           this.startDateDue = startDateDue;
           this.activityTypeFilter = activityTypeFilter;
           this.activityName = activityName;
    }


    /**
     * Gets the sessionId value for this GetWorkQueueUsingActivityName.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetWorkQueueUsingActivityName.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the numberOfActivities value for this GetWorkQueueUsingActivityName.
     * 
     * @return numberOfActivities
     */
    public int getNumberOfActivities() {
        return numberOfActivities;
    }


    /**
     * Sets the numberOfActivities value for this GetWorkQueueUsingActivityName.
     * 
     * @param numberOfActivities
     */
    public void setNumberOfActivities(int numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
    }


    /**
     * Gets the useStartDueDate value for this GetWorkQueueUsingActivityName.
     * 
     * @return useStartDueDate
     */
    public boolean isUseStartDueDate() {
        return useStartDueDate;
    }


    /**
     * Sets the useStartDueDate value for this GetWorkQueueUsingActivityName.
     * 
     * @param useStartDueDate
     */
    public void setUseStartDueDate(boolean useStartDueDate) {
        this.useStartDueDate = useStartDueDate;
    }


    /**
     * Gets the startDateDue value for this GetWorkQueueUsingActivityName.
     * 
     * @return startDateDue
     */
    public java.util.Calendar getStartDateDue() {
        return startDateDue;
    }


    /**
     * Sets the startDateDue value for this GetWorkQueueUsingActivityName.
     * 
     * @param startDateDue
     */
    public void setStartDateDue(java.util.Calendar startDateDue) {
        this.startDateDue = startDateDue;
    }


    /**
     * Gets the activityTypeFilter value for this GetWorkQueueUsingActivityName.
     * 
     * @return activityTypeFilter
     */
    public short getActivityTypeFilter() {
        return activityTypeFilter;
    }


    /**
     * Sets the activityTypeFilter value for this GetWorkQueueUsingActivityName.
     * 
     * @param activityTypeFilter
     */
    public void setActivityTypeFilter(short activityTypeFilter) {
        this.activityTypeFilter = activityTypeFilter;
    }


    /**
     * Gets the activityName value for this GetWorkQueueUsingActivityName.
     * 
     * @return activityName
     */
    public java.lang.String getActivityName() {
        return activityName;
    }


    /**
     * Sets the activityName value for this GetWorkQueueUsingActivityName.
     * 
     * @param activityName
     */
    public void setActivityName(java.lang.String activityName) {
        this.activityName = activityName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetWorkQueueUsingActivityName)) return false;
        GetWorkQueueUsingActivityName other = (GetWorkQueueUsingActivityName) obj;
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
              this.activityName.equals(other.getActivityName())));
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetWorkQueueUsingActivityName.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkQueueUsingActivityName"));
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
