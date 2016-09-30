/**
 * ModifySchedule.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ModifySchedule  implements java.io.Serializable {
    private java.lang.String sessionId;

    private int scheduleId;

    private java.lang.String businessProcessId;

    private java.lang.String scheduleName;

    private int timeSpan;

    private short timeSpanPeriod;

    private short noOfOccurances;

    private short noOfJobsToCreate;

    private boolean scheduleActive;

    private boolean useStartDate;

    private java.util.Calendar startDate;

    private boolean useEndDate;

    private java.util.Calendar endDate;

    private com.ecs.stellent.spp.ScheduleVariable[] initialisationParams;

    private boolean monthly;

    private boolean useWeekDays;

    private short daysOfWeek;

    private boolean oneActiveAtTime;

    private boolean useBusinessCalendar;

    public ModifySchedule() {
    }

    public ModifySchedule(
           java.lang.String sessionId,
           int scheduleId,
           java.lang.String businessProcessId,
           java.lang.String scheduleName,
           int timeSpan,
           short timeSpanPeriod,
           short noOfOccurances,
           short noOfJobsToCreate,
           boolean scheduleActive,
           boolean useStartDate,
           java.util.Calendar startDate,
           boolean useEndDate,
           java.util.Calendar endDate,
           com.ecs.stellent.spp.ScheduleVariable[] initialisationParams,
           boolean monthly,
           boolean useWeekDays,
           short daysOfWeek,
           boolean oneActiveAtTime,
           boolean useBusinessCalendar) {
           this.sessionId = sessionId;
           this.scheduleId = scheduleId;
           this.businessProcessId = businessProcessId;
           this.scheduleName = scheduleName;
           this.timeSpan = timeSpan;
           this.timeSpanPeriod = timeSpanPeriod;
           this.noOfOccurances = noOfOccurances;
           this.noOfJobsToCreate = noOfJobsToCreate;
           this.scheduleActive = scheduleActive;
           this.useStartDate = useStartDate;
           this.startDate = startDate;
           this.useEndDate = useEndDate;
           this.endDate = endDate;
           this.initialisationParams = initialisationParams;
           this.monthly = monthly;
           this.useWeekDays = useWeekDays;
           this.daysOfWeek = daysOfWeek;
           this.oneActiveAtTime = oneActiveAtTime;
           this.useBusinessCalendar = useBusinessCalendar;
    }


    /**
     * Gets the sessionId value for this ModifySchedule.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this ModifySchedule.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the scheduleId value for this ModifySchedule.
     * 
     * @return scheduleId
     */
    public int getScheduleId() {
        return scheduleId;
    }


    /**
     * Sets the scheduleId value for this ModifySchedule.
     * 
     * @param scheduleId
     */
    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }


    /**
     * Gets the businessProcessId value for this ModifySchedule.
     * 
     * @return businessProcessId
     */
    public java.lang.String getBusinessProcessId() {
        return businessProcessId;
    }


    /**
     * Sets the businessProcessId value for this ModifySchedule.
     * 
     * @param businessProcessId
     */
    public void setBusinessProcessId(java.lang.String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }


    /**
     * Gets the scheduleName value for this ModifySchedule.
     * 
     * @return scheduleName
     */
    public java.lang.String getScheduleName() {
        return scheduleName;
    }


    /**
     * Sets the scheduleName value for this ModifySchedule.
     * 
     * @param scheduleName
     */
    public void setScheduleName(java.lang.String scheduleName) {
        this.scheduleName = scheduleName;
    }


    /**
     * Gets the timeSpan value for this ModifySchedule.
     * 
     * @return timeSpan
     */
    public int getTimeSpan() {
        return timeSpan;
    }


    /**
     * Sets the timeSpan value for this ModifySchedule.
     * 
     * @param timeSpan
     */
    public void setTimeSpan(int timeSpan) {
        this.timeSpan = timeSpan;
    }


    /**
     * Gets the timeSpanPeriod value for this ModifySchedule.
     * 
     * @return timeSpanPeriod
     */
    public short getTimeSpanPeriod() {
        return timeSpanPeriod;
    }


    /**
     * Sets the timeSpanPeriod value for this ModifySchedule.
     * 
     * @param timeSpanPeriod
     */
    public void setTimeSpanPeriod(short timeSpanPeriod) {
        this.timeSpanPeriod = timeSpanPeriod;
    }


    /**
     * Gets the noOfOccurances value for this ModifySchedule.
     * 
     * @return noOfOccurances
     */
    public short getNoOfOccurances() {
        return noOfOccurances;
    }


    /**
     * Sets the noOfOccurances value for this ModifySchedule.
     * 
     * @param noOfOccurances
     */
    public void setNoOfOccurances(short noOfOccurances) {
        this.noOfOccurances = noOfOccurances;
    }


    /**
     * Gets the noOfJobsToCreate value for this ModifySchedule.
     * 
     * @return noOfJobsToCreate
     */
    public short getNoOfJobsToCreate() {
        return noOfJobsToCreate;
    }


    /**
     * Sets the noOfJobsToCreate value for this ModifySchedule.
     * 
     * @param noOfJobsToCreate
     */
    public void setNoOfJobsToCreate(short noOfJobsToCreate) {
        this.noOfJobsToCreate = noOfJobsToCreate;
    }


    /**
     * Gets the scheduleActive value for this ModifySchedule.
     * 
     * @return scheduleActive
     */
    public boolean isScheduleActive() {
        return scheduleActive;
    }


    /**
     * Sets the scheduleActive value for this ModifySchedule.
     * 
     * @param scheduleActive
     */
    public void setScheduleActive(boolean scheduleActive) {
        this.scheduleActive = scheduleActive;
    }


    /**
     * Gets the useStartDate value for this ModifySchedule.
     * 
     * @return useStartDate
     */
    public boolean isUseStartDate() {
        return useStartDate;
    }


    /**
     * Sets the useStartDate value for this ModifySchedule.
     * 
     * @param useStartDate
     */
    public void setUseStartDate(boolean useStartDate) {
        this.useStartDate = useStartDate;
    }


    /**
     * Gets the startDate value for this ModifySchedule.
     * 
     * @return startDate
     */
    public java.util.Calendar getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this ModifySchedule.
     * 
     * @param startDate
     */
    public void setStartDate(java.util.Calendar startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the useEndDate value for this ModifySchedule.
     * 
     * @return useEndDate
     */
    public boolean isUseEndDate() {
        return useEndDate;
    }


    /**
     * Sets the useEndDate value for this ModifySchedule.
     * 
     * @param useEndDate
     */
    public void setUseEndDate(boolean useEndDate) {
        this.useEndDate = useEndDate;
    }


    /**
     * Gets the endDate value for this ModifySchedule.
     * 
     * @return endDate
     */
    public java.util.Calendar getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this ModifySchedule.
     * 
     * @param endDate
     */
    public void setEndDate(java.util.Calendar endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the initialisationParams value for this ModifySchedule.
     * 
     * @return initialisationParams
     */
    public com.ecs.stellent.spp.ScheduleVariable[] getInitialisationParams() {
        return initialisationParams;
    }


    /**
     * Sets the initialisationParams value for this ModifySchedule.
     * 
     * @param initialisationParams
     */
    public void setInitialisationParams(com.ecs.stellent.spp.ScheduleVariable[] initialisationParams) {
        this.initialisationParams = initialisationParams;
    }


    /**
     * Gets the monthly value for this ModifySchedule.
     * 
     * @return monthly
     */
    public boolean isMonthly() {
        return monthly;
    }


    /**
     * Sets the monthly value for this ModifySchedule.
     * 
     * @param monthly
     */
    public void setMonthly(boolean monthly) {
        this.monthly = monthly;
    }


    /**
     * Gets the useWeekDays value for this ModifySchedule.
     * 
     * @return useWeekDays
     */
    public boolean isUseWeekDays() {
        return useWeekDays;
    }


    /**
     * Sets the useWeekDays value for this ModifySchedule.
     * 
     * @param useWeekDays
     */
    public void setUseWeekDays(boolean useWeekDays) {
        this.useWeekDays = useWeekDays;
    }


    /**
     * Gets the daysOfWeek value for this ModifySchedule.
     * 
     * @return daysOfWeek
     */
    public short getDaysOfWeek() {
        return daysOfWeek;
    }


    /**
     * Sets the daysOfWeek value for this ModifySchedule.
     * 
     * @param daysOfWeek
     */
    public void setDaysOfWeek(short daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }


    /**
     * Gets the oneActiveAtTime value for this ModifySchedule.
     * 
     * @return oneActiveAtTime
     */
    public boolean isOneActiveAtTime() {
        return oneActiveAtTime;
    }


    /**
     * Sets the oneActiveAtTime value for this ModifySchedule.
     * 
     * @param oneActiveAtTime
     */
    public void setOneActiveAtTime(boolean oneActiveAtTime) {
        this.oneActiveAtTime = oneActiveAtTime;
    }


    /**
     * Gets the useBusinessCalendar value for this ModifySchedule.
     * 
     * @return useBusinessCalendar
     */
    public boolean isUseBusinessCalendar() {
        return useBusinessCalendar;
    }


    /**
     * Sets the useBusinessCalendar value for this ModifySchedule.
     * 
     * @param useBusinessCalendar
     */
    public void setUseBusinessCalendar(boolean useBusinessCalendar) {
        this.useBusinessCalendar = useBusinessCalendar;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ModifySchedule)) return false;
        ModifySchedule other = (ModifySchedule) obj;
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
            this.scheduleId == other.getScheduleId() &&
            ((this.businessProcessId==null && other.getBusinessProcessId()==null) || 
             (this.businessProcessId!=null &&
              this.businessProcessId.equals(other.getBusinessProcessId()))) &&
            ((this.scheduleName==null && other.getScheduleName()==null) || 
             (this.scheduleName!=null &&
              this.scheduleName.equals(other.getScheduleName()))) &&
            this.timeSpan == other.getTimeSpan() &&
            this.timeSpanPeriod == other.getTimeSpanPeriod() &&
            this.noOfOccurances == other.getNoOfOccurances() &&
            this.noOfJobsToCreate == other.getNoOfJobsToCreate() &&
            this.scheduleActive == other.isScheduleActive() &&
            this.useStartDate == other.isUseStartDate() &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            this.useEndDate == other.isUseEndDate() &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            ((this.initialisationParams==null && other.getInitialisationParams()==null) || 
             (this.initialisationParams!=null &&
              java.util.Arrays.equals(this.initialisationParams, other.getInitialisationParams()))) &&
            this.monthly == other.isMonthly() &&
            this.useWeekDays == other.isUseWeekDays() &&
            this.daysOfWeek == other.getDaysOfWeek() &&
            this.oneActiveAtTime == other.isOneActiveAtTime() &&
            this.useBusinessCalendar == other.isUseBusinessCalendar();
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
        _hashCode += getScheduleId();
        if (getBusinessProcessId() != null) {
            _hashCode += getBusinessProcessId().hashCode();
        }
        if (getScheduleName() != null) {
            _hashCode += getScheduleName().hashCode();
        }
        _hashCode += getTimeSpan();
        _hashCode += getTimeSpanPeriod();
        _hashCode += getNoOfOccurances();
        _hashCode += getNoOfJobsToCreate();
        _hashCode += (isScheduleActive() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUseStartDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        _hashCode += (isUseEndDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        if (getInitialisationParams() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInitialisationParams());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInitialisationParams(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isMonthly() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUseWeekDays() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getDaysOfWeek();
        _hashCode += (isOneActiveAtTime() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUseBusinessCalendar() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ModifySchedule.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ModifySchedule"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("scheduleName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleName"));
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
        elemField.setFieldName("timeSpanPeriod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeSpanPeriod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
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
        elemField.setFieldName("scheduleActive");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleActive"));
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
        elemField.setFieldName("initialisationParams");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitialisationParams"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleVariable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleVariable"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("monthly");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Monthly"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
