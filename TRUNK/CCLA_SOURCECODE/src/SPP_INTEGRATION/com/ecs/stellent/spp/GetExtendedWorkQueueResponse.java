/**
 * GetExtendedWorkQueueResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetExtendedWorkQueueResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.ExtendedWorkQueue getExtendedWorkQueueResult;

    public GetExtendedWorkQueueResponse() {
    }

    public GetExtendedWorkQueueResponse(
           com.ecs.stellent.spp.ExtendedWorkQueue getExtendedWorkQueueResult) {
           this.getExtendedWorkQueueResult = getExtendedWorkQueueResult;
    }


    /**
     * Gets the getExtendedWorkQueueResult value for this GetExtendedWorkQueueResponse.
     * 
     * @return getExtendedWorkQueueResult
     */
    public com.ecs.stellent.spp.ExtendedWorkQueue getGetExtendedWorkQueueResult() {
        return getExtendedWorkQueueResult;
    }


    /**
     * Sets the getExtendedWorkQueueResult value for this GetExtendedWorkQueueResponse.
     * 
     * @param getExtendedWorkQueueResult
     */
    public void setGetExtendedWorkQueueResult(com.ecs.stellent.spp.ExtendedWorkQueue getExtendedWorkQueueResult) {
        this.getExtendedWorkQueueResult = getExtendedWorkQueueResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtendedWorkQueueResponse)) return false;
        GetExtendedWorkQueueResponse other = (GetExtendedWorkQueueResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getExtendedWorkQueueResult==null && other.getGetExtendedWorkQueueResult()==null) || 
             (this.getExtendedWorkQueueResult!=null &&
              this.getExtendedWorkQueueResult.equals(other.getGetExtendedWorkQueueResult())));
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
        if (getGetExtendedWorkQueueResult() != null) {
            _hashCode += getGetExtendedWorkQueueResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtendedWorkQueueResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueueResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getExtendedWorkQueueResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueueResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue"));
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
