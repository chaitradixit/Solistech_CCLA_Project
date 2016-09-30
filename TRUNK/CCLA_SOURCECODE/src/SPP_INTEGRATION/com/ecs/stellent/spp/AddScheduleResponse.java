/**
 * AddScheduleResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class AddScheduleResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.JobScheduleDetails addScheduleResult;

    public AddScheduleResponse() {
    }

    public AddScheduleResponse(
           com.ecs.stellent.spp.JobScheduleDetails addScheduleResult) {
           this.addScheduleResult = addScheduleResult;
    }


    /**
     * Gets the addScheduleResult value for this AddScheduleResponse.
     * 
     * @return addScheduleResult
     */
    public com.ecs.stellent.spp.JobScheduleDetails getAddScheduleResult() {
        return addScheduleResult;
    }


    /**
     * Sets the addScheduleResult value for this AddScheduleResponse.
     * 
     * @param addScheduleResult
     */
    public void setAddScheduleResult(com.ecs.stellent.spp.JobScheduleDetails addScheduleResult) {
        this.addScheduleResult = addScheduleResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddScheduleResponse)) return false;
        AddScheduleResponse other = (AddScheduleResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.addScheduleResult==null && other.getAddScheduleResult()==null) || 
             (this.addScheduleResult!=null &&
              this.addScheduleResult.equals(other.getAddScheduleResult())));
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
        if (getAddScheduleResult() != null) {
            _hashCode += getAddScheduleResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AddScheduleResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AddScheduleResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addScheduleResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AddScheduleResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobScheduleDetails"));
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
