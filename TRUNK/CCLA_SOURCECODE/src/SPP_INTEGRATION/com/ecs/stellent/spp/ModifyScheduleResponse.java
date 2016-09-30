/**
 * ModifyScheduleResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ModifyScheduleResponse  implements java.io.Serializable {
    private java.util.Calendar modifyScheduleResult;

    public ModifyScheduleResponse() {
    }

    public ModifyScheduleResponse(
           java.util.Calendar modifyScheduleResult) {
           this.modifyScheduleResult = modifyScheduleResult;
    }


    /**
     * Gets the modifyScheduleResult value for this ModifyScheduleResponse.
     * 
     * @return modifyScheduleResult
     */
    public java.util.Calendar getModifyScheduleResult() {
        return modifyScheduleResult;
    }


    /**
     * Sets the modifyScheduleResult value for this ModifyScheduleResponse.
     * 
     * @param modifyScheduleResult
     */
    public void setModifyScheduleResult(java.util.Calendar modifyScheduleResult) {
        this.modifyScheduleResult = modifyScheduleResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ModifyScheduleResponse)) return false;
        ModifyScheduleResponse other = (ModifyScheduleResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.modifyScheduleResult==null && other.getModifyScheduleResult()==null) || 
             (this.modifyScheduleResult!=null &&
              this.modifyScheduleResult.equals(other.getModifyScheduleResult())));
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
        if (getModifyScheduleResult() != null) {
            _hashCode += getModifyScheduleResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ModifyScheduleResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ModifyScheduleResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modifyScheduleResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ModifyScheduleResult"));
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
