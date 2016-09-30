/**
 * UpdateJobOwners.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class UpdateJobOwners  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String[] jobIds;

    private java.lang.String jobOwnerID;

    public UpdateJobOwners() {
    }

    public UpdateJobOwners(
           java.lang.String sessionId,
           java.lang.String[] jobIds,
           java.lang.String jobOwnerID) {
           this.sessionId = sessionId;
           this.jobIds = jobIds;
           this.jobOwnerID = jobOwnerID;
    }


    /**
     * Gets the sessionId value for this UpdateJobOwners.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this UpdateJobOwners.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the jobIds value for this UpdateJobOwners.
     * 
     * @return jobIds
     */
    public java.lang.String[] getJobIds() {
        return jobIds;
    }


    /**
     * Sets the jobIds value for this UpdateJobOwners.
     * 
     * @param jobIds
     */
    public void setJobIds(java.lang.String[] jobIds) {
        this.jobIds = jobIds;
    }


    /**
     * Gets the jobOwnerID value for this UpdateJobOwners.
     * 
     * @return jobOwnerID
     */
    public java.lang.String getJobOwnerID() {
        return jobOwnerID;
    }


    /**
     * Sets the jobOwnerID value for this UpdateJobOwners.
     * 
     * @param jobOwnerID
     */
    public void setJobOwnerID(java.lang.String jobOwnerID) {
        this.jobOwnerID = jobOwnerID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UpdateJobOwners)) return false;
        UpdateJobOwners other = (UpdateJobOwners) obj;
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
            ((this.jobIds==null && other.getJobIds()==null) || 
             (this.jobIds!=null &&
              java.util.Arrays.equals(this.jobIds, other.getJobIds()))) &&
            ((this.jobOwnerID==null && other.getJobOwnerID()==null) || 
             (this.jobOwnerID!=null &&
              this.jobOwnerID.equals(other.getJobOwnerID())));
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
        if (getJobIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getJobIds());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getJobIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getJobOwnerID() != null) {
            _hashCode += getJobOwnerID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UpdateJobOwners.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateJobOwners"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobIds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobOwnerID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobOwnerID"));
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
