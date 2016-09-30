/**
 * GetSubJobDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetSubJobDetails  implements java.io.Serializable {
    private java.lang.String sessionID;

    private java.lang.String jobID;

    private java.lang.String subJobID;

    private java.lang.String subProcessID;

    private double subJobVersion;

    private short jobDatabase;

    public GetSubJobDetails() {
    }

    public GetSubJobDetails(
           java.lang.String sessionID,
           java.lang.String jobID,
           java.lang.String subJobID,
           java.lang.String subProcessID,
           double subJobVersion,
           short jobDatabase) {
           this.sessionID = sessionID;
           this.jobID = jobID;
           this.subJobID = subJobID;
           this.subProcessID = subProcessID;
           this.subJobVersion = subJobVersion;
           this.jobDatabase = jobDatabase;
    }


    /**
     * Gets the sessionID value for this GetSubJobDetails.
     * 
     * @return sessionID
     */
    public java.lang.String getSessionID() {
        return sessionID;
    }


    /**
     * Sets the sessionID value for this GetSubJobDetails.
     * 
     * @param sessionID
     */
    public void setSessionID(java.lang.String sessionID) {
        this.sessionID = sessionID;
    }


    /**
     * Gets the jobID value for this GetSubJobDetails.
     * 
     * @return jobID
     */
    public java.lang.String getJobID() {
        return jobID;
    }


    /**
     * Sets the jobID value for this GetSubJobDetails.
     * 
     * @param jobID
     */
    public void setJobID(java.lang.String jobID) {
        this.jobID = jobID;
    }


    /**
     * Gets the subJobID value for this GetSubJobDetails.
     * 
     * @return subJobID
     */
    public java.lang.String getSubJobID() {
        return subJobID;
    }


    /**
     * Sets the subJobID value for this GetSubJobDetails.
     * 
     * @param subJobID
     */
    public void setSubJobID(java.lang.String subJobID) {
        this.subJobID = subJobID;
    }


    /**
     * Gets the subProcessID value for this GetSubJobDetails.
     * 
     * @return subProcessID
     */
    public java.lang.String getSubProcessID() {
        return subProcessID;
    }


    /**
     * Sets the subProcessID value for this GetSubJobDetails.
     * 
     * @param subProcessID
     */
    public void setSubProcessID(java.lang.String subProcessID) {
        this.subProcessID = subProcessID;
    }


    /**
     * Gets the subJobVersion value for this GetSubJobDetails.
     * 
     * @return subJobVersion
     */
    public double getSubJobVersion() {
        return subJobVersion;
    }


    /**
     * Sets the subJobVersion value for this GetSubJobDetails.
     * 
     * @param subJobVersion
     */
    public void setSubJobVersion(double subJobVersion) {
        this.subJobVersion = subJobVersion;
    }


    /**
     * Gets the jobDatabase value for this GetSubJobDetails.
     * 
     * @return jobDatabase
     */
    public short getJobDatabase() {
        return jobDatabase;
    }


    /**
     * Sets the jobDatabase value for this GetSubJobDetails.
     * 
     * @param jobDatabase
     */
    public void setJobDatabase(short jobDatabase) {
        this.jobDatabase = jobDatabase;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSubJobDetails)) return false;
        GetSubJobDetails other = (GetSubJobDetails) obj;
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
            ((this.subJobID==null && other.getSubJobID()==null) || 
             (this.subJobID!=null &&
              this.subJobID.equals(other.getSubJobID()))) &&
            ((this.subProcessID==null && other.getSubProcessID()==null) || 
             (this.subProcessID!=null &&
              this.subProcessID.equals(other.getSubProcessID()))) &&
            this.subJobVersion == other.getSubJobVersion() &&
            this.jobDatabase == other.getJobDatabase();
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
        if (getSubJobID() != null) {
            _hashCode += getSubJobID().hashCode();
        }
        if (getSubProcessID() != null) {
            _hashCode += getSubProcessID().hashCode();
        }
        _hashCode += new Double(getSubJobVersion()).hashCode();
        _hashCode += getJobDatabase();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSubJobDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetSubJobDetails"));
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
        elemField.setFieldName("subJobID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subProcessID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubProcessID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subJobVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobDatabase");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDatabase"));
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
