/**
 * Events.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class Events  implements java.io.Serializable {
    private java.lang.String event;

    private java.util.Calendar raiseDateTime;

    private java.lang.String eventSource;

    public Events() {
    }

    public Events(
           java.lang.String event,
           java.util.Calendar raiseDateTime,
           java.lang.String eventSource) {
           this.event = event;
           this.raiseDateTime = raiseDateTime;
           this.eventSource = eventSource;
    }


    /**
     * Gets the event value for this Events.
     * 
     * @return event
     */
    public java.lang.String getEvent() {
        return event;
    }


    /**
     * Sets the event value for this Events.
     * 
     * @param event
     */
    public void setEvent(java.lang.String event) {
        this.event = event;
    }


    /**
     * Gets the raiseDateTime value for this Events.
     * 
     * @return raiseDateTime
     */
    public java.util.Calendar getRaiseDateTime() {
        return raiseDateTime;
    }


    /**
     * Sets the raiseDateTime value for this Events.
     * 
     * @param raiseDateTime
     */
    public void setRaiseDateTime(java.util.Calendar raiseDateTime) {
        this.raiseDateTime = raiseDateTime;
    }


    /**
     * Gets the eventSource value for this Events.
     * 
     * @return eventSource
     */
    public java.lang.String getEventSource() {
        return eventSource;
    }


    /**
     * Sets the eventSource value for this Events.
     * 
     * @param eventSource
     */
    public void setEventSource(java.lang.String eventSource) {
        this.eventSource = eventSource;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Events)) return false;
        Events other = (Events) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.event==null && other.getEvent()==null) || 
             (this.event!=null &&
              this.event.equals(other.getEvent()))) &&
            ((this.raiseDateTime==null && other.getRaiseDateTime()==null) || 
             (this.raiseDateTime!=null &&
              this.raiseDateTime.equals(other.getRaiseDateTime()))) &&
            ((this.eventSource==null && other.getEventSource()==null) || 
             (this.eventSource!=null &&
              this.eventSource.equals(other.getEventSource())));
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
        if (getEvent() != null) {
            _hashCode += getEvent().hashCode();
        }
        if (getRaiseDateTime() != null) {
            _hashCode += getRaiseDateTime().hashCode();
        }
        if (getEventSource() != null) {
            _hashCode += getEventSource().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Events.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Events"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("event");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Event"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("raiseDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RaiseDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("eventSource");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EventSource"));
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
