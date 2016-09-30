/**
 * StateHistory.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class StateHistory  implements java.io.Serializable {
    private java.lang.String sState;

    private java.lang.String sResourceName;

    private java.util.Calendar sPerformedTime;

    public StateHistory() {
    }

    public StateHistory(
           java.lang.String sState,
           java.lang.String sResourceName,
           java.util.Calendar sPerformedTime) {
           this.sState = sState;
           this.sResourceName = sResourceName;
           this.sPerformedTime = sPerformedTime;
    }


    /**
     * Gets the sState value for this StateHistory.
     * 
     * @return sState
     */
    public java.lang.String getSState() {
        return sState;
    }


    /**
     * Sets the sState value for this StateHistory.
     * 
     * @param sState
     */
    public void setSState(java.lang.String sState) {
        this.sState = sState;
    }


    /**
     * Gets the sResourceName value for this StateHistory.
     * 
     * @return sResourceName
     */
    public java.lang.String getSResourceName() {
        return sResourceName;
    }


    /**
     * Sets the sResourceName value for this StateHistory.
     * 
     * @param sResourceName
     */
    public void setSResourceName(java.lang.String sResourceName) {
        this.sResourceName = sResourceName;
    }


    /**
     * Gets the sPerformedTime value for this StateHistory.
     * 
     * @return sPerformedTime
     */
    public java.util.Calendar getSPerformedTime() {
        return sPerformedTime;
    }


    /**
     * Sets the sPerformedTime value for this StateHistory.
     * 
     * @param sPerformedTime
     */
    public void setSPerformedTime(java.util.Calendar sPerformedTime) {
        this.sPerformedTime = sPerformedTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StateHistory)) return false;
        StateHistory other = (StateHistory) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sState==null && other.getSState()==null) || 
             (this.sState!=null &&
              this.sState.equals(other.getSState()))) &&
            ((this.sResourceName==null && other.getSResourceName()==null) || 
             (this.sResourceName!=null &&
              this.sResourceName.equals(other.getSResourceName()))) &&
            ((this.sPerformedTime==null && other.getSPerformedTime()==null) || 
             (this.sPerformedTime!=null &&
              this.sPerformedTime.equals(other.getSPerformedTime())));
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
        if (getSState() != null) {
            _hashCode += getSState().hashCode();
        }
        if (getSResourceName() != null) {
            _hashCode += getSResourceName().hashCode();
        }
        if (getSPerformedTime() != null) {
            _hashCode += getSPerformedTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StateHistory.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StateHistory"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "sState"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SResourceName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "sResourceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SPerformedTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "sPerformedTime"));
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
