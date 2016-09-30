/**
 * GetJobVariableHistory.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobVariableHistory  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String jobID;

    private short nodeID;

    private short EPC;

    private boolean usePerformDate;

    private java.util.Calendar performDate;

    public GetJobVariableHistory() {
    }

    public GetJobVariableHistory(
           java.lang.String sessionId,
           java.lang.String jobID,
           short nodeID,
           short EPC,
           boolean usePerformDate,
           java.util.Calendar performDate) {
           this.sessionId = sessionId;
           this.jobID = jobID;
           this.nodeID = nodeID;
           this.EPC = EPC;
           this.usePerformDate = usePerformDate;
           this.performDate = performDate;
    }


    /**
     * Gets the sessionId value for this GetJobVariableHistory.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this GetJobVariableHistory.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the jobID value for this GetJobVariableHistory.
     * 
     * @return jobID
     */
    public java.lang.String getJobID() {
        return jobID;
    }


    /**
     * Sets the jobID value for this GetJobVariableHistory.
     * 
     * @param jobID
     */
    public void setJobID(java.lang.String jobID) {
        this.jobID = jobID;
    }


    /**
     * Gets the nodeID value for this GetJobVariableHistory.
     * 
     * @return nodeID
     */
    public short getNodeID() {
        return nodeID;
    }


    /**
     * Sets the nodeID value for this GetJobVariableHistory.
     * 
     * @param nodeID
     */
    public void setNodeID(short nodeID) {
        this.nodeID = nodeID;
    }


    /**
     * Gets the EPC value for this GetJobVariableHistory.
     * 
     * @return EPC
     */
    public short getEPC() {
        return EPC;
    }


    /**
     * Sets the EPC value for this GetJobVariableHistory.
     * 
     * @param EPC
     */
    public void setEPC(short EPC) {
        this.EPC = EPC;
    }


    /**
     * Gets the usePerformDate value for this GetJobVariableHistory.
     * 
     * @return usePerformDate
     */
    public boolean isUsePerformDate() {
        return usePerformDate;
    }


    /**
     * Sets the usePerformDate value for this GetJobVariableHistory.
     * 
     * @param usePerformDate
     */
    public void setUsePerformDate(boolean usePerformDate) {
        this.usePerformDate = usePerformDate;
    }


    /**
     * Gets the performDate value for this GetJobVariableHistory.
     * 
     * @return performDate
     */
    public java.util.Calendar getPerformDate() {
        return performDate;
    }


    /**
     * Sets the performDate value for this GetJobVariableHistory.
     * 
     * @param performDate
     */
    public void setPerformDate(java.util.Calendar performDate) {
        this.performDate = performDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobVariableHistory)) return false;
        GetJobVariableHistory other = (GetJobVariableHistory) obj;
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
            ((this.jobID==null && other.getJobID()==null) || 
             (this.jobID!=null &&
              this.jobID.equals(other.getJobID()))) &&
            this.nodeID == other.getNodeID() &&
            this.EPC == other.getEPC() &&
            this.usePerformDate == other.isUsePerformDate() &&
            ((this.performDate==null && other.getPerformDate()==null) || 
             (this.performDate!=null &&
              this.performDate.equals(other.getPerformDate())));
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
        if (getJobID() != null) {
            _hashCode += getJobID().hashCode();
        }
        _hashCode += getNodeID();
        _hashCode += getEPC();
        _hashCode += (isUsePerformDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getPerformDate() != null) {
            _hashCode += getPerformDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobVariableHistory.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobVariableHistory"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
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
        elemField.setFieldName("nodeID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EPC");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EPC"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usePerformDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePerformDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("performDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PerformDate"));
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
