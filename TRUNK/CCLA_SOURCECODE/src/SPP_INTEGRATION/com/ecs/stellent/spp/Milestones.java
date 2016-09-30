/**
 * Milestones.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class Milestones  implements java.io.Serializable {
    private java.lang.String milestone;

    private java.util.Calendar targetTime;

    private java.lang.Object actualTime;

    public Milestones() {
    }

    public Milestones(
           java.lang.String milestone,
           java.util.Calendar targetTime,
           java.lang.Object actualTime) {
           this.milestone = milestone;
           this.targetTime = targetTime;
           this.actualTime = actualTime;
    }


    /**
     * Gets the milestone value for this Milestones.
     * 
     * @return milestone
     */
    public java.lang.String getMilestone() {
        return milestone;
    }


    /**
     * Sets the milestone value for this Milestones.
     * 
     * @param milestone
     */
    public void setMilestone(java.lang.String milestone) {
        this.milestone = milestone;
    }


    /**
     * Gets the targetTime value for this Milestones.
     * 
     * @return targetTime
     */
    public java.util.Calendar getTargetTime() {
        return targetTime;
    }


    /**
     * Sets the targetTime value for this Milestones.
     * 
     * @param targetTime
     */
    public void setTargetTime(java.util.Calendar targetTime) {
        this.targetTime = targetTime;
    }


    /**
     * Gets the actualTime value for this Milestones.
     * 
     * @return actualTime
     */
    public java.lang.Object getActualTime() {
        return actualTime;
    }


    /**
     * Sets the actualTime value for this Milestones.
     * 
     * @param actualTime
     */
    public void setActualTime(java.lang.Object actualTime) {
        this.actualTime = actualTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Milestones)) return false;
        Milestones other = (Milestones) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.milestone==null && other.getMilestone()==null) || 
             (this.milestone!=null &&
              this.milestone.equals(other.getMilestone()))) &&
            ((this.targetTime==null && other.getTargetTime()==null) || 
             (this.targetTime!=null &&
              this.targetTime.equals(other.getTargetTime()))) &&
            ((this.actualTime==null && other.getActualTime()==null) || 
             (this.actualTime!=null &&
              this.actualTime.equals(other.getActualTime())));
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
        if (getMilestone() != null) {
            _hashCode += getMilestone().hashCode();
        }
        if (getTargetTime() != null) {
            _hashCode += getTargetTime().hashCode();
        }
        if (getActualTime() != null) {
            _hashCode += getActualTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Milestones.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Milestones"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("milestone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Milestone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TargetTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActualTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
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
