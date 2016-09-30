/**
 * RaiseEventUpdVars.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class RaiseEventUpdVars  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String jobid;

    private java.lang.String event;

    private java.lang.String eventSource;

    private com.ecs.stellent.spp.RaiseEventVars[] vVariables;

    public RaiseEventUpdVars() {
    }

    public RaiseEventUpdVars(
           java.lang.String sessionId,
           java.lang.String jobid,
           java.lang.String event,
           java.lang.String eventSource,
           com.ecs.stellent.spp.RaiseEventVars[] vVariables) {
           this.sessionId = sessionId;
           this.jobid = jobid;
           this.event = event;
           this.eventSource = eventSource;
           this.vVariables = vVariables;
    }


    /**
     * Gets the sessionId value for this RaiseEventUpdVars.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this RaiseEventUpdVars.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the jobid value for this RaiseEventUpdVars.
     * 
     * @return jobid
     */
    public java.lang.String getJobid() {
        return jobid;
    }


    /**
     * Sets the jobid value for this RaiseEventUpdVars.
     * 
     * @param jobid
     */
    public void setJobid(java.lang.String jobid) {
        this.jobid = jobid;
    }


    /**
     * Gets the event value for this RaiseEventUpdVars.
     * 
     * @return event
     */
    public java.lang.String getEvent() {
        return event;
    }


    /**
     * Sets the event value for this RaiseEventUpdVars.
     * 
     * @param event
     */
    public void setEvent(java.lang.String event) {
        this.event = event;
    }


    /**
     * Gets the eventSource value for this RaiseEventUpdVars.
     * 
     * @return eventSource
     */
    public java.lang.String getEventSource() {
        return eventSource;
    }


    /**
     * Sets the eventSource value for this RaiseEventUpdVars.
     * 
     * @param eventSource
     */
    public void setEventSource(java.lang.String eventSource) {
        this.eventSource = eventSource;
    }


    /**
     * Gets the vVariables value for this RaiseEventUpdVars.
     * 
     * @return vVariables
     */
    public com.ecs.stellent.spp.RaiseEventVars[] getVVariables() {
        return vVariables;
    }


    /**
     * Sets the vVariables value for this RaiseEventUpdVars.
     * 
     * @param vVariables
     */
    public void setVVariables(com.ecs.stellent.spp.RaiseEventVars[] vVariables) {
        this.vVariables = vVariables;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RaiseEventUpdVars)) return false;
        RaiseEventUpdVars other = (RaiseEventUpdVars) obj;
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
            ((this.jobid==null && other.getJobid()==null) || 
             (this.jobid!=null &&
              this.jobid.equals(other.getJobid()))) &&
            ((this.event==null && other.getEvent()==null) || 
             (this.event!=null &&
              this.event.equals(other.getEvent()))) &&
            ((this.eventSource==null && other.getEventSource()==null) || 
             (this.eventSource!=null &&
              this.eventSource.equals(other.getEventSource()))) &&
            ((this.vVariables==null && other.getVVariables()==null) || 
             (this.vVariables!=null &&
              java.util.Arrays.equals(this.vVariables, other.getVVariables())));
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
        if (getJobid() != null) {
            _hashCode += getJobid().hashCode();
        }
        if (getEvent() != null) {
            _hashCode += getEvent().hashCode();
        }
        if (getEventSource() != null) {
            _hashCode += getEventSource().hashCode();
        }
        if (getVVariables() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getVVariables());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getVVariables(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RaiseEventUpdVars.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RaiseEventUpdVars"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "jobid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("event");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Event"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventSource");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EventSource"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VVariables");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "vVariables"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RaiseEventVars"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RaiseEventVars"));
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
