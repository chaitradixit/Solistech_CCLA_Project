/**
 * PlaceJobOnHold.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class PlaceJobOnHold  implements java.io.Serializable {
    private java.lang.String sessionID;

    private java.lang.String jobID;

    private short holdType;

    private java.util.Calendar activationDate;

    private short activationPeriodWeeks;

    private short activationPeriodDays;

    private short activationPeriodHours;

    private java.lang.String reasonForHold;

    public PlaceJobOnHold() {
    }

    public PlaceJobOnHold(
           java.lang.String sessionID,
           java.lang.String jobID,
           short holdType,
           java.util.Calendar activationDate,
           short activationPeriodWeeks,
           short activationPeriodDays,
           short activationPeriodHours,
           java.lang.String reasonForHold) {
           this.sessionID = sessionID;
           this.jobID = jobID;
           this.holdType = holdType;
           this.activationDate = activationDate;
           this.activationPeriodWeeks = activationPeriodWeeks;
           this.activationPeriodDays = activationPeriodDays;
           this.activationPeriodHours = activationPeriodHours;
           this.reasonForHold = reasonForHold;
    }


    /**
     * Gets the sessionID value for this PlaceJobOnHold.
     * 
     * @return sessionID
     */
    public java.lang.String getSessionID() {
        return sessionID;
    }


    /**
     * Sets the sessionID value for this PlaceJobOnHold.
     * 
     * @param sessionID
     */
    public void setSessionID(java.lang.String sessionID) {
        this.sessionID = sessionID;
    }


    /**
     * Gets the jobID value for this PlaceJobOnHold.
     * 
     * @return jobID
     */
    public java.lang.String getJobID() {
        return jobID;
    }


    /**
     * Sets the jobID value for this PlaceJobOnHold.
     * 
     * @param jobID
     */
    public void setJobID(java.lang.String jobID) {
        this.jobID = jobID;
    }


    /**
     * Gets the holdType value for this PlaceJobOnHold.
     * 
     * @return holdType
     */
    public short getHoldType() {
        return holdType;
    }


    /**
     * Sets the holdType value for this PlaceJobOnHold.
     * 
     * @param holdType
     */
    public void setHoldType(short holdType) {
        this.holdType = holdType;
    }


    /**
     * Gets the activationDate value for this PlaceJobOnHold.
     * 
     * @return activationDate
     */
    public java.util.Calendar getActivationDate() {
        return activationDate;
    }


    /**
     * Sets the activationDate value for this PlaceJobOnHold.
     * 
     * @param activationDate
     */
    public void setActivationDate(java.util.Calendar activationDate) {
        this.activationDate = activationDate;
    }


    /**
     * Gets the activationPeriodWeeks value for this PlaceJobOnHold.
     * 
     * @return activationPeriodWeeks
     */
    public short getActivationPeriodWeeks() {
        return activationPeriodWeeks;
    }


    /**
     * Sets the activationPeriodWeeks value for this PlaceJobOnHold.
     * 
     * @param activationPeriodWeeks
     */
    public void setActivationPeriodWeeks(short activationPeriodWeeks) {
        this.activationPeriodWeeks = activationPeriodWeeks;
    }


    /**
     * Gets the activationPeriodDays value for this PlaceJobOnHold.
     * 
     * @return activationPeriodDays
     */
    public short getActivationPeriodDays() {
        return activationPeriodDays;
    }


    /**
     * Sets the activationPeriodDays value for this PlaceJobOnHold.
     * 
     * @param activationPeriodDays
     */
    public void setActivationPeriodDays(short activationPeriodDays) {
        this.activationPeriodDays = activationPeriodDays;
    }


    /**
     * Gets the activationPeriodHours value for this PlaceJobOnHold.
     * 
     * @return activationPeriodHours
     */
    public short getActivationPeriodHours() {
        return activationPeriodHours;
    }


    /**
     * Sets the activationPeriodHours value for this PlaceJobOnHold.
     * 
     * @param activationPeriodHours
     */
    public void setActivationPeriodHours(short activationPeriodHours) {
        this.activationPeriodHours = activationPeriodHours;
    }


    /**
     * Gets the reasonForHold value for this PlaceJobOnHold.
     * 
     * @return reasonForHold
     */
    public java.lang.String getReasonForHold() {
        return reasonForHold;
    }


    /**
     * Sets the reasonForHold value for this PlaceJobOnHold.
     * 
     * @param reasonForHold
     */
    public void setReasonForHold(java.lang.String reasonForHold) {
        this.reasonForHold = reasonForHold;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PlaceJobOnHold)) return false;
        PlaceJobOnHold other = (PlaceJobOnHold) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sessionID==null && other.getSessionID()==null) || 
             (this.sessionID!=null &&
              this.sessionID.equals(other.getSessionID()))) &&
            ((this.jobID==null && other.getJobID()==null) || 
             (this.jobID!=null &&
              this.jobID.equals(other.getJobID()))) &&
            this.holdType == other.getHoldType() &&
            ((this.activationDate==null && other.getActivationDate()==null) || 
             (this.activationDate!=null &&
              this.activationDate.equals(other.getActivationDate()))) &&
            this.activationPeriodWeeks == other.getActivationPeriodWeeks() &&
            this.activationPeriodDays == other.getActivationPeriodDays() &&
            this.activationPeriodHours == other.getActivationPeriodHours() &&
            ((this.reasonForHold==null && other.getReasonForHold()==null) || 
             (this.reasonForHold!=null &&
              this.reasonForHold.equals(other.getReasonForHold())));
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
        if (getSessionID() != null) {
            _hashCode += getSessionID().hashCode();
        }
        if (getJobID() != null) {
            _hashCode += getJobID().hashCode();
        }
        _hashCode += getHoldType();
        if (getActivationDate() != null) {
            _hashCode += getActivationDate().hashCode();
        }
        _hashCode += getActivationPeriodWeeks();
        _hashCode += getActivationPeriodDays();
        _hashCode += getActivationPeriodHours();
        if (getReasonForHold() != null) {
            _hashCode += getReasonForHold().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PlaceJobOnHold.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PlaceJobOnHold"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("holdType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "HoldType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activationPeriodWeeks");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivationPeriodWeeks"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activationPeriodDays");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivationPeriodDays"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activationPeriodHours");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivationPeriodHours"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reasonForHold");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReasonForHold"));
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
