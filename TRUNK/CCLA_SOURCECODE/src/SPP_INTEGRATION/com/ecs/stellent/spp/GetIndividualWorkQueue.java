/**
 * GetIndividualWorkQueue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetIndividualWorkQueue  implements java.io.Serializable {
    private java.lang.String sessionId;

    private int numberOfActivitiesRq;

    private boolean useStartDueDate;

    private java.util.Calendar startDueDate;

    private short activityTypeFilter;

    private boolean useActivityName;

    private java.lang.String activityName;

    public GetIndividualWorkQueue() {
    }

    public GetIndividualWorkQueue(
           java.lang.String sessionId,
           int numberOfActivitiesRq,
           boolean useStartDueDate,
           java.util.Calendar startDueDate,
           short activityTypeFilter,
           boolean useActivityName,
           java.lang.String activityName) {
           this.sessionId = sessionId;
           this.numberOfActivitiesRq = numberOfActivitiesRq;
           this.useStartDueDate = useStartDueDate;
           this.startDueDate = startDueDate;
           this.activityTypeFilter = activityTypeFilter;
           this.useActivityName = useActivityName;
           this.activityName = activityName;
    }


    /**
     * Gets the sessionId value for this GetIndividualWorkQueue.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetIndividualWorkQueue.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the numberOfActivitiesRq value for this GetIndividualWorkQueue.
     * 
     * @return numberOfActivitiesRq
     */
    public int getNumberOfActivitiesRq() {
        return numberOfActivitiesRq;
    }


    /**
     * Sets the numberOfActivitiesRq value for this GetIndividualWorkQueue.
     * 
     * @param numberOfActivitiesRq
     */
    public void setNumberOfActivitiesRq(int numberOfActivitiesRq) {
        this.numberOfActivitiesRq = numberOfActivitiesRq;
    }


    /**
     * Gets the useStartDueDate value for this GetIndividualWorkQueue.
     * 
     * @return useStartDueDate
     */
    public boolean isUseStartDueDate() {
        return useStartDueDate;
    }


    /**
     * Sets the useStartDueDate value for this GetIndividualWorkQueue.
     * 
     * @param useStartDueDate
     */
    public void setUseStartDueDate(boolean useStartDueDate) {
        this.useStartDueDate = useStartDueDate;
    }


    /**
     * Gets the startDueDate value for this GetIndividualWorkQueue.
     * 
     * @return startDueDate
     */
    public java.util.Calendar getStartDueDate() {
        return startDueDate;
    }


    /**
     * Sets the startDueDate value for this GetIndividualWorkQueue.
     * 
     * @param startDueDate
     */
    public void setStartDueDate(java.util.Calendar startDueDate) {
        this.startDueDate = startDueDate;
    }


    /**
     * Gets the activityTypeFilter value for this GetIndividualWorkQueue.
     * 
     * @return activityTypeFilter
     */
    public short getActivityTypeFilter() {
        return activityTypeFilter;
    }


    /**
     * Sets the activityTypeFilter value for this GetIndividualWorkQueue.
     * 
     * @param activityTypeFilter
     */
    public void setActivityTypeFilter(short activityTypeFilter) {
        this.activityTypeFilter = activityTypeFilter;
    }


    /**
     * Gets the useActivityName value for this GetIndividualWorkQueue.
     * 
     * @return useActivityName
     */
    public boolean isUseActivityName() {
        return useActivityName;
    }


    /**
     * Sets the useActivityName value for this GetIndividualWorkQueue.
     * 
     * @param useActivityName
     */
    public void setUseActivityName(boolean useActivityName) {
        this.useActivityName = useActivityName;
    }


    /**
     * Gets the activityName value for this GetIndividualWorkQueue.
     * 
     * @return activityName
     */
    public java.lang.String getActivityName() {
        return activityName;
    }


    /**
     * Sets the activityName value for this GetIndividualWorkQueue.
     * 
     * @param activityName
     */
    public void setActivityName(java.lang.String activityName) {
        this.activityName = activityName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetIndividualWorkQueue)) return false;
        GetIndividualWorkQueue other = (GetIndividualWorkQueue) obj;
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetIndividualWorkQueue.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetIndividualWorkQueue"));
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
