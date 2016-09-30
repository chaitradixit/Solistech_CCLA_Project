/**
 * SchedulePageDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class SchedulePageDetails  implements java.io.Serializable {
    private int scheduleId;

    private java.lang.String businessProcessId;

    private int timeSpan;

    private short noOfOccurances;

    private short noOfJobsToCreate;

    private java.util.Calendar nextDueDate;

    private short noOfOccurancesProcessed;

    private boolean monthly;

    private boolean useStartDate;

    private java.util.Calendar startDate;

    private boolean useEndDate;

    private java.util.Calendar endDate;

    private java.lang.String scheduleName;

    private boolean useWeekDays;

    private short daysOfWeek;

    private boolean scheduleActive;

    private boolean oneActiveAtTime;

    private boolean useBusinessCalendar;

    private java.util.Calendar lastModifiedDate;

    public SchedulePageDetails() {
    }

    public SchedulePageDetails(
           int scheduleId,
           java.lang.String businessProcessId,
           int timeSpan,
           short noOfOccurances,
           short noOfJobsToCreate,
           java.util.Calendar nextDueDate,
           short noOfOccurancesProcessed,
           boolean monthly,
           boolean useStartDate,
           java.util.Calendar startDate,
           boolean useEndDate,
           java.util.Calendar endDate,
           java.lang.String scheduleName,
           boolean useWeekDays,
           short daysOfWeek,
           boolean scheduleActive,
           boolean oneActiveAtTime,
           boolean useBusinessCalendar,
           java.util.Calendar lastModifiedDate) {
           this.scheduleId = scheduleId;
           this.businessProcessId = businessProcessId;
           this.timeSpan = timeSpan;
           this.noOfOccurances = noOfOccurances;
           this.noOfJobsToCreate = noOfJobsToCreate;
           this.nextDueDate = nextDueDate;
           this.noOfOccurancesProcessed = noOfOccurancesProcessed;
           this.monthly = monthly;
           this.useStartDate = useStartDate;
           this.startDate = startDate;
           this.useEndDate = useEndDate;
           this.endDate = endDate;
           this.scheduleName = scheduleName;
           this.useWeekDays = useWeekDays;
           this.daysOfWeek = daysOfWeek;
           this.scheduleActive = scheduleActive;
           this.oneActiveAtTime = oneActiveAtTime;
           this.useBusinessCalendar = useBusinessCalendar;
           this.lastModifiedDate = lastModifiedDate;
    }


    /**
     * Gets the scheduleId value for this SchedulePageDetails.
     * 
     * @return scheduleId
     */
    public int getScheduleId() {
        return scheduleId;
    }


    /**
     * Sets the scheduleId value for this SchedulePageDetails.
     * 
     * @param scheduleId
     */
    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }


    /**
     * Gets the businessProcessId value for this SchedulePageDetails.
     * 
     * @return businessProcessId
     */
    public java.lang.String getBusinessProcessId() {
        return businessProcessId;
    }


    /**
     * Sets the businessProcessId value for this SchedulePageDetails.
     * 
     * @param businessProcessId
     */
    public void setBusinessProcessId(java.lang.String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }


    /**
     * Gets the timeSpan value for this SchedulePageDetails.
     * 
     * @return timeSpan
     */
    public int getTimeSpan() {
        return timeSpan;
    }


    /**
     * Sets the timeSpan value for this SchedulePageDetails.
     * 
     * @param timeSpan
     */
    public void setTimeSpan(int timeSpan) {
        this.timeSpan = timeSpan;
    }


    /**
     * Gets the noOfOccurances value for this SchedulePageDetails.
     * 
     * @return noOfOccurances
     */
    public short getNoOfOccurances() {
        return noOfOccurances;
    }


    /**
     * Sets the noOfOccurances value for this SchedulePageDetails.
     * 
     * @param noOfOccurances
     */
    public void setNoOfOccurances(short noOfOccurances) {
        this.noOfOccurances = noOfOccurances;
    }


    /**
     * Gets the noOfJobsToCreate value for this SchedulePageDetails.
     * 
     * @return noOfJobsToCreate
     */
    public short getNoOfJobsToCreate() {
        return noOfJobsToCreate;
    }


    /**
     * Sets the noOfJobsToCreate value for this SchedulePageDetails.
     * 
     * @param noOfJobsToCreate
     */
    public void setNoOfJobsToCreate(short noOfJobsToCreate) {
        this.noOfJobsToCreate = noOfJobsToCreate;
    }


    /**
     * Gets the nextDueDate value for this SchedulePageDetails.
     * 
     * @return nextDueDate
     */
    public java.util.Calendar getNextDueDate() {
        return nextDueDate;
    }


    /**
     * Sets the nextDueDate value for this SchedulePageDetails.
     * 
     * @param nextDueDate
     */
    public void setNextDueDate(java.util.Calendar nextDueDate) {
        this.nextDueDate = nextDueDate;
    }


    /**
     * Gets the noOfOccurancesProcessed value for this SchedulePageDetails.
     * 
     * @return noOfOccurancesProcessed
     */
    public short getNoOfOccurancesProcessed() {
        return noOfOccurancesProcessed;
    }


    /**
     * Sets the noOfOccurancesProcessed value for this SchedulePageDetails.
     * 
     * @param noOfOccurancesProcessed
     */
    public void setNoOfOccurancesProcessed(short noOfOccurancesProcessed) {
        this.noOfOccurancesProcessed = noOfOccurancesProcessed;
    }


    /**
     * Gets the monthly value for this SchedulePageDetails.
     * 
     * @return monthly
     */
    public boolean isMonthly() {
        return monthly;
    }


    /**
     * Sets the monthly value for this SchedulePageDetails.
     * 
     * @param monthly
     */
    public void setMonthly(boolean monthly) {
        this.monthly = monthly;
    }


    /**
     * Gets the useStartDate value for this SchedulePageDetails.
     * 
     * @return useStartDate
     */
    public boolean isUseStartDate() {
        return useStartDate;
    }


    /**
     * Sets the useStartDate value for this SchedulePageDetails.
     * 
     * @param useStartDate
     */
    public void setUseStartDate(boolean useStartDate) {
        this.useStartDate = useStartDate;
    }


    /**
     * Gets the startDate value for this SchedulePageDetails.
     * 
     * @return startDate
     */
    public java.util.Calendar getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this SchedulePageDetails.
     * 
     * @param startDate
     */
    public void setStartDate(java.util.Calendar startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the useEndDate value for this SchedulePageDetails.
     * 
     * @return useEndDate
     */
    public boolean isUseEndDate() {
        return useEndDate;
    }


    /**
     * Sets the useEndDate value for this SchedulePageDetails.
     * 
     * @param useEndDate
     */
    public void setUseEndDate(boolean useEndDate) {
        this.useEndDate = useEndDate;
    }


    /**
     * Gets the endDate value for this SchedulePageDetails.
     * 
     * @return endDate
     */
    public java.util.Calendar getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this SchedulePageDetails.
     * 
     * @param endDate
     */
    public void setEndDate(java.util.Calendar endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the scheduleName value for this SchedulePageDetails.
     * 
     * @return scheduleName
     */
    public java.lang.String getScheduleName() {
        return scheduleName;
    }


    /**
     * Sets the scheduleName value for this SchedulePageDetails.
     * 
     * @param scheduleName
     */
    public void setScheduleName(java.lang.String scheduleName) {
        this.scheduleName = scheduleName;
    }


    /**
     * Gets the useWeekDays value for this SchedulePageDetails.
     * 
     * @return useWeekDays
     */
    public boolean isUseWeekDays() {
        return useWeekDays;
    }


    /**
     * Sets the useWeekDays value for this SchedulePageDetails.
     * 
     * @param useWeekDays
     */
    public void setUseWeekDays(boolean useWeekDays) {
        this.useWeekDays = useWeekDays;
    }


    /**
     * Gets the daysOfWeek value for this SchedulePageDetails.
     * 
     * @return daysOfWeek
     */
    public short getDaysOfWeek() {
        return daysOfWeek;
    }


    /**
     * Sets the daysOfWeek value for this SchedulePageDetails.
     * 
     * @param daysOfWeek
     */
    public void setDaysOfWeek(short daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }


    /**
     * Gets the scheduleActive value for this SchedulePageDetails.
     * 
     * @return scheduleActive
     */
    public boolean isScheduleActive() {
        return scheduleActive;
    }


    /**
     * Sets the scheduleActive value for this SchedulePageDetails.
     * 
     * @param scheduleActive
     */
    public void setScheduleActive(boolean scheduleActive) {
        this.scheduleActive = scheduleActive;
    }


    /**
     * Gets the oneActiveAtTime value for this SchedulePageDetails.
     * 
     * @return oneActiveAtTime
     */
    public boolean isOneActiveAtTime() {
        return oneActiveAtTime;
    }


    /**
     * Sets the oneActiveAtTime value for this SchedulePageDetails.
     * 
     * @param oneActiveAtTime
     */
    public void setOneActiveAtTime(boolean oneActiveAtTime) {
        this.oneActiveAtTime = oneActiveAtTime;
    }


    /**
     * Gets the useBusinessCalendar value for this SchedulePageDetails.
     * 
     * @return useBusinessCalendar
     */
    public boolean isUseBusinessCalendar() {
        return useBusinessCalendar;
    }


    /**
     * Sets the useBusinessCalendar value for this SchedulePageDetails.
     * 
     * @param useBusinessCalendar
     */
    public void setUseBusinessCalendar(boolean useBusinessCalendar) {
        this.useBusinessCalendar = useBusinessCalendar;
    }


    /**
     * Gets the lastModifiedDate value for this SchedulePageDetails.
     * 
     * @return lastModifiedDate
     */
    public java.util.Calendar getLastModifiedDate() {
        return lastModifiedDate;
    }


    /**
     * Sets the lastModifiedDate value for this SchedulePageDetails.
     * 
     * @param lastModifiedDate
     */
    public void setLastModifiedDate(java.util.Calendar lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulePageDetails)) return false;
        SchedulePageDetails other = (SchedulePageDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.scheduleId == other.getScheduleId() &&
            ((this.businessProcessId==null && other.getBusinessProcessId()==null) || 
             (this.businessProcessId!=null &&
              this.businessProcessId.equals(other.getBusinessProcessId()))) &&
            this.timeSpan == other.getTimeSpan() &&
            this.noOfOccurances == other.getNoOfOccurances() &&
            this.noOfJobsToCreate == other.getNoOfJobsToCreate() &&
            ((this.nextDueDate==null && other.getNextDueDate()==null) || 
             (this.nextDueDate!=null &&
              this.nextDueDate.equals(other.getNextDueDate()))) &&
            this.noOfOccurancesProcessed == other.getNoOfOccurancesProcessed() &&
            this.monthly == other.isMonthly() &&
            this.useStartDate == other.isUseStartDate() &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            this.useEndDate == other.isUseEndDate() &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            ((this.scheduleName==null && other.getScheduleName()==null) || 
             (this.scheduleName!=null &&
              this.scheduleName.equals(other.getScheduleName()))) &&
            this.useWeekDays == other.isUseWeekDays() &&
            this.daysOfWeek == other.getDaysOfWeek() &&
            this.scheduleActive == other.isScheduleActive() &&
            this.oneActiveAtTime == other.isOneActiveAtTime() &&
            this.useBusinessCalendar == other.isUseBusinessCalendar() &&
            ((this.lastModifiedDate==null && other.getLastModifiedDate()==null) || 
             (this.lastModifiedDate!=null &&
              this.lastModifiedDate.equals(other.getLastModifiedDate())));
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
        _hashCode += getScheduleId();
        if (getBusinessProcessId() != null) {
            _hashCode += getBusinessProcessId().hashCode();
        }
        _hashCode += getTimeSpan();
        _hashCode += getNoOfOccurances();
        _hashCode += getNoOfJobsToCreate();
        if (getNextDueDate() != null) {
            _hashCode += getNextDueDate().hashCode();
        }
        _hashCode += getNoOfOccurancesProcessed();
        _hashCode += (isMonthly() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUseStartDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        _hashCode += (isUseEndDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        if (getScheduleName() != null) {
            _hashCode += getScheduleName().hashCode();
        }
        _hashCode += (isUseWeekDays() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getDaysOfWeek();
        _hashCode += (isScheduleActive() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isOneActiveAtTime() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUseBusinessCalendar() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getLastModifiedDate() != null) {
            _hashCode += getLastModifiedDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SchedulePageDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SchedulePageDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduleId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessProcessId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeSpan");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeSpan"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noOfOccurances");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NoOfOccurances"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noOfJobsToCreate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NoOfJobsToCreate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nextDueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NextDueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("noOfOccurancesProcessed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NoOfOccurancesProcessed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("monthly");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Monthly"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useStartDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useEndDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseEndDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EndDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduleName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useWeekDays");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseWeekDays"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("daysOfWeek");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DaysOfWeek"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduleActive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleActive"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oneActiveAtTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OneActiveAtTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useBusinessCalendar");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseBusinessCalendar"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastModifiedDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastModifiedDate"));
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
