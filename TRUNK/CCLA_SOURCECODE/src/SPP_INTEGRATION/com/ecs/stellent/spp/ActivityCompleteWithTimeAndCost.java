/**
 * ActivityCompleteWithTimeAndCost.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ActivityCompleteWithTimeAndCost  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String jobId;

    private com.ecs.stellent.spp.Variable[] OPVars;

    private short embeddedProcessCount;

    private short nodeId;

    private java.util.Calendar startTime;

    private double cost;

    public ActivityCompleteWithTimeAndCost() {
    }

    public ActivityCompleteWithTimeAndCost(
           java.lang.String sessionId,
           java.lang.String jobId,
           com.ecs.stellent.spp.Variable[] OPVars,
           short embeddedProcessCount,
           short nodeId,
           java.util.Calendar startTime,
           double cost) {
           this.sessionId = sessionId;
           this.jobId = jobId;
           this.OPVars = OPVars;
           this.embeddedProcessCount = embeddedProcessCount;
           this.nodeId = nodeId;
           this.startTime = startTime;
           this.cost = cost;
    }


    /**
     * Gets the sessionId value for this ActivityCompleteWithTimeAndCost.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this ActivityCompleteWithTimeAndCost.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the jobId value for this ActivityCompleteWithTimeAndCost.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this ActivityCompleteWithTimeAndCost.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }


    /**
     * Gets the OPVars value for this ActivityCompleteWithTimeAndCost.
     * 
     * @return OPVars
     */
    public com.ecs.stellent.spp.Variable[] getOPVars() {
        return OPVars;
    }


    /**
     * Sets the OPVars value for this ActivityCompleteWithTimeAndCost.
     * 
     * @param OPVars
     */
    public void setOPVars(com.ecs.stellent.spp.Variable[] OPVars) {
        this.OPVars = OPVars;
    }


    /**
     * Gets the embeddedProcessCount value for this ActivityCompleteWithTimeAndCost.
     * 
     * @return embeddedProcessCount
     */
    public short getEmbeddedProcessCount() {
        return embeddedProcessCount;
    }


    /**
     * Sets the embeddedProcessCount value for this ActivityCompleteWithTimeAndCost.
     * 
     * @param embeddedProcessCount
     */
    public void setEmbeddedProcessCount(short embeddedProcessCount) {
        this.embeddedProcessCount = embeddedProcessCount;
    }


    /**
     * Gets the nodeId value for this ActivityCompleteWithTimeAndCost.
     * 
     * @return nodeId
     */
    public short getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this ActivityCompleteWithTimeAndCost.
     * 
     * @param nodeId
     */
    public void setNodeId(short nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the startTime value for this ActivityCompleteWithTimeAndCost.
     * 
     * @return startTime
     */
    public java.util.Calendar getStartTime() {
        return startTime;
    }


    /**
     * Sets the startTime value for this ActivityCompleteWithTimeAndCost.
     * 
     * @param startTime
     */
    public void setStartTime(java.util.Calendar startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the cost value for this ActivityCompleteWithTimeAndCost.
     * 
     * @return cost
     */
    public double getCost() {
        return cost;
    }


    /**
     * Sets the cost value for this ActivityCompleteWithTimeAndCost.
     * 
     * @param cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ActivityCompleteWithTimeAndCost)) return false;
        ActivityCompleteWithTimeAndCost other = (ActivityCompleteWithTimeAndCost) obj;
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
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId()))) &&
            ((this.OPVars==null && other.getOPVars()==null) || 
             (this.OPVars!=null &&
              java.util.Arrays.equals(this.OPVars, other.getOPVars()))) &&
            this.embeddedProcessCount == other.getEmbeddedProcessCount() &&
            this.nodeId == other.getNodeId() &&
            ((this.startTime==null && other.getStartTime()==null) || 
             (this.startTime!=null &&
              this.startTime.equals(other.getStartTime()))) &&
            this.cost == other.getCost();
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
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        if (getOPVars() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOPVars());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOPVars(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getEmbeddedProcessCount();
        _hashCode += getNodeId();
        if (getStartTime() != null) {
            _hashCode += getStartTime().hashCode();
        }
        _hashCode += new Double(getCost()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ActivityCompleteWithTimeAndCost.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ActivityCompleteWithTimeAndCost"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OPVars");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OPVars"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("embeddedProcessCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Cost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
