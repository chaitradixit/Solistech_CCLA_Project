/**
 * BatchQueueJob.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class BatchQueueJob  implements java.io.Serializable {
    private int entry;

    private java.lang.String entryDate;

    private java.lang.String username;

    private java.lang.String workstation;

    private java.lang.String printerName;

    private com.aurora.webservice.RequestType request;

    private java.lang.String parameters;

    private java.lang.String scheduledDate;

    private int priority;

    private com.aurora.webservice.RequestStatus status;

    private java.lang.String startDate;

    private java.lang.String finishDate;

    public BatchQueueJob() {
    }

    public BatchQueueJob(
           int entry,
           java.lang.String entryDate,
           java.lang.String username,
           java.lang.String workstation,
           java.lang.String printerName,
           com.aurora.webservice.RequestType request,
           java.lang.String parameters,
           java.lang.String scheduledDate,
           int priority,
           com.aurora.webservice.RequestStatus status,
           java.lang.String startDate,
           java.lang.String finishDate) {
           this.entry = entry;
           this.entryDate = entryDate;
           this.username = username;
           this.workstation = workstation;
           this.printerName = printerName;
           this.request = request;
           this.parameters = parameters;
           this.scheduledDate = scheduledDate;
           this.priority = priority;
           this.status = status;
           this.startDate = startDate;
           this.finishDate = finishDate;
    }


    /**
     * Gets the entry value for this BatchQueueJob.
     * 
     * @return entry
     */
    public int getEntry() {
        return entry;
    }


    /**
     * Sets the entry value for this BatchQueueJob.
     * 
     * @param entry
     */
    public void setEntry(int entry) {
        this.entry = entry;
    }


    /**
     * Gets the entryDate value for this BatchQueueJob.
     * 
     * @return entryDate
     */
    public java.lang.String getEntryDate() {
        return entryDate;
    }


    /**
     * Sets the entryDate value for this BatchQueueJob.
     * 
     * @param entryDate
     */
    public void setEntryDate(java.lang.String entryDate) {
        this.entryDate = entryDate;
    }


    /**
     * Gets the username value for this BatchQueueJob.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this BatchQueueJob.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the workstation value for this BatchQueueJob.
     * 
     * @return workstation
     */
    public java.lang.String getWorkstation() {
        return workstation;
    }


    /**
     * Sets the workstation value for this BatchQueueJob.
     * 
     * @param workstation
     */
    public void setWorkstation(java.lang.String workstation) {
        this.workstation = workstation;
    }


    /**
     * Gets the printerName value for this BatchQueueJob.
     * 
     * @return printerName
     */
    public java.lang.String getPrinterName() {
        return printerName;
    }


    /**
     * Sets the printerName value for this BatchQueueJob.
     * 
     * @param printerName
     */
    public void setPrinterName(java.lang.String printerName) {
        this.printerName = printerName;
    }


    /**
     * Gets the request value for this BatchQueueJob.
     * 
     * @return request
     */
    public com.aurora.webservice.RequestType getRequest() {
        return request;
    }


    /**
     * Sets the request value for this BatchQueueJob.
     * 
     * @param request
     */
    public void setRequest(com.aurora.webservice.RequestType request) {
        this.request = request;
    }


    /**
     * Gets the parameters value for this BatchQueueJob.
     * 
     * @return parameters
     */
    public java.lang.String getParameters() {
        return parameters;
    }


    /**
     * Sets the parameters value for this BatchQueueJob.
     * 
     * @param parameters
     */
    public void setParameters(java.lang.String parameters) {
        this.parameters = parameters;
    }


    /**
     * Gets the scheduledDate value for this BatchQueueJob.
     * 
     * @return scheduledDate
     */
    public java.lang.String getScheduledDate() {
        return scheduledDate;
    }


    /**
     * Sets the scheduledDate value for this BatchQueueJob.
     * 
     * @param scheduledDate
     */
    public void setScheduledDate(java.lang.String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }


    /**
     * Gets the priority value for this BatchQueueJob.
     * 
     * @return priority
     */
    public int getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this BatchQueueJob.
     * 
     * @param priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }


    /**
     * Gets the status value for this BatchQueueJob.
     * 
     * @return status
     */
    public com.aurora.webservice.RequestStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this BatchQueueJob.
     * 
     * @param status
     */
    public void setStatus(com.aurora.webservice.RequestStatus status) {
        this.status = status;
    }


    /**
     * Gets the startDate value for this BatchQueueJob.
     * 
     * @return startDate
     */
    public java.lang.String getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this BatchQueueJob.
     * 
     * @param startDate
     */
    public void setStartDate(java.lang.String startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the finishDate value for this BatchQueueJob.
     * 
     * @return finishDate
     */
    public java.lang.String getFinishDate() {
        return finishDate;
    }


    /**
     * Sets the finishDate value for this BatchQueueJob.
     * 
     * @param finishDate
     */
    public void setFinishDate(java.lang.String finishDate) {
        this.finishDate = finishDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BatchQueueJob)) return false;
        BatchQueueJob other = (BatchQueueJob) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.entry == other.getEntry() &&
            ((this.entryDate==null && other.getEntryDate()==null) || 
             (this.entryDate!=null &&
              this.entryDate.equals(other.getEntryDate()))) &&
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            ((this.workstation==null && other.getWorkstation()==null) || 
             (this.workstation!=null &&
              this.workstation.equals(other.getWorkstation()))) &&
            ((this.printerName==null && other.getPrinterName()==null) || 
             (this.printerName!=null &&
              this.printerName.equals(other.getPrinterName()))) &&
            ((this.request==null && other.getRequest()==null) || 
             (this.request!=null &&
              this.request.equals(other.getRequest()))) &&
            ((this.parameters==null && other.getParameters()==null) || 
             (this.parameters!=null &&
              this.parameters.equals(other.getParameters()))) &&
            ((this.scheduledDate==null && other.getScheduledDate()==null) || 
             (this.scheduledDate!=null &&
              this.scheduledDate.equals(other.getScheduledDate()))) &&
            this.priority == other.getPriority() &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            ((this.finishDate==null && other.getFinishDate()==null) || 
             (this.finishDate!=null &&
              this.finishDate.equals(other.getFinishDate())));
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
        _hashCode += getEntry();
        if (getEntryDate() != null) {
            _hashCode += getEntryDate().hashCode();
        }
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getWorkstation() != null) {
            _hashCode += getWorkstation().hashCode();
        }
        if (getPrinterName() != null) {
            _hashCode += getPrinterName().hashCode();
        }
        if (getRequest() != null) {
            _hashCode += getRequest().hashCode();
        }
        if (getParameters() != null) {
            _hashCode += getParameters().hashCode();
        }
        if (getScheduledDate() != null) {
            _hashCode += getScheduledDate().hashCode();
        }
        _hashCode += getPriority();
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        if (getFinishDate() != null) {
            _hashCode += getFinishDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BatchQueueJob.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BatchQueueJob"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entry");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Entry"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entryDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "EntryDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workstation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Workstation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("printerName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PrinterName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("request");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Request"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "RequestType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameters");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Parameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduledDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ScheduledDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("priority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Priority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "RequestStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "StartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("finishDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FinishDate"));
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
