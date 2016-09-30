/**
 * SchedulePageInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class SchedulePageInfo  implements java.io.Serializable {
    private int lastScheduleId;

    private com.ecs.stellent.spp.SchedulePageDetails[] schedulePageDetails;

    public SchedulePageInfo() {
    }

    public SchedulePageInfo(
           int lastScheduleId,
           com.ecs.stellent.spp.SchedulePageDetails[] schedulePageDetails) {
           this.lastScheduleId = lastScheduleId;
           this.schedulePageDetails = schedulePageDetails;
    }


    /**
     * Gets the lastScheduleId value for this SchedulePageInfo.
     * 
     * @return lastScheduleId
     */
    public int getLastScheduleId() {
        return lastScheduleId;
    }


    /**
     * Sets the lastScheduleId value for this SchedulePageInfo.
     * 
     * @param lastScheduleId
     */
    public void setLastScheduleId(int lastScheduleId) {
        this.lastScheduleId = lastScheduleId;
    }


    /**
     * Gets the schedulePageDetails value for this SchedulePageInfo.
     * 
     * @return schedulePageDetails
     */
    public com.ecs.stellent.spp.SchedulePageDetails[] getSchedulePageDetails() {
        return schedulePageDetails;
    }


    /**
     * Sets the schedulePageDetails value for this SchedulePageInfo.
     * 
     * @param schedulePageDetails
     */
    public void setSchedulePageDetails(com.ecs.stellent.spp.SchedulePageDetails[] schedulePageDetails) {
        this.schedulePageDetails = schedulePageDetails;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SchedulePageInfo)) return false;
        SchedulePageInfo other = (SchedulePageInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.lastScheduleId == other.getLastScheduleId() &&
            ((this.schedulePageDetails==null && other.getSchedulePageDetails()==null) || 
             (this.schedulePageDetails!=null &&
              java.util.Arrays.equals(this.schedulePageDetails, other.getSchedulePageDetails())));
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
        _hashCode += getLastScheduleId();
        if (getSchedulePageDetails() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSchedulePageDetails());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSchedulePageDetails(), i);
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
        new org.apache.axis.description.TypeDesc(SchedulePageInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SchedulePageInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastScheduleId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastScheduleId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("schedulePageDetails");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "schedulePageDetails"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SchedulePageDetails"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SchedulePageDetails"));
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
