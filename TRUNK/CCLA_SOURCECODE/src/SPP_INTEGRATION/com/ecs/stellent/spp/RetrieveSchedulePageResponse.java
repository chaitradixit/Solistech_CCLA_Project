/**
 * RetrieveSchedulePageResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class RetrieveSchedulePageResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.SchedulePageInfo retrieveSchedulePageResult;

    public RetrieveSchedulePageResponse() {
    }

    public RetrieveSchedulePageResponse(
           com.ecs.stellent.spp.SchedulePageInfo retrieveSchedulePageResult) {
           this.retrieveSchedulePageResult = retrieveSchedulePageResult;
    }


    /**
     * Gets the retrieveSchedulePageResult value for this RetrieveSchedulePageResponse.
     * 
     * @return retrieveSchedulePageResult
     */
    public com.ecs.stellent.spp.SchedulePageInfo getRetrieveSchedulePageResult() {
        return retrieveSchedulePageResult;
    }


    /**
     * Sets the retrieveSchedulePageResult value for this RetrieveSchedulePageResponse.
     * 
     * @param retrieveSchedulePageResult
     */
    public void setRetrieveSchedulePageResult(com.ecs.stellent.spp.SchedulePageInfo retrieveSchedulePageResult) {
        this.retrieveSchedulePageResult = retrieveSchedulePageResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetrieveSchedulePageResponse)) return false;
        RetrieveSchedulePageResponse other = (RetrieveSchedulePageResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.retrieveSchedulePageResult==null && other.getRetrieveSchedulePageResult()==null) || 
             (this.retrieveSchedulePageResult!=null &&
              this.retrieveSchedulePageResult.equals(other.getRetrieveSchedulePageResult())));
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
        if (getRetrieveSchedulePageResult() != null) {
            _hashCode += getRetrieveSchedulePageResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RetrieveSchedulePageResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RetrieveSchedulePageResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retrieveSchedulePageResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RetrieveSchedulePageResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SchedulePageInfo"));
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
