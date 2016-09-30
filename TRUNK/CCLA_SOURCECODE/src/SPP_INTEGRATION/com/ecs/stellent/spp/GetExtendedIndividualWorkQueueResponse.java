/**
 * GetExtendedIndividualWorkQueueResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetExtendedIndividualWorkQueueResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.ExtendedWorkQueue getExtendedIndividualWorkQueueResult;

    public GetExtendedIndividualWorkQueueResponse() {
    }

    public GetExtendedIndividualWorkQueueResponse(
           com.ecs.stellent.spp.ExtendedWorkQueue getExtendedIndividualWorkQueueResult) {
           this.getExtendedIndividualWorkQueueResult = getExtendedIndividualWorkQueueResult;
    }


    /**
     * Gets the getExtendedIndividualWorkQueueResult value for this GetExtendedIndividualWorkQueueResponse.
     * 
     * @return getExtendedIndividualWorkQueueResult
     */
    public com.ecs.stellent.spp.ExtendedWorkQueue getGetExtendedIndividualWorkQueueResult() {
        return getExtendedIndividualWorkQueueResult;
    }


    /**
     * Sets the getExtendedIndividualWorkQueueResult value for this GetExtendedIndividualWorkQueueResponse.
     * 
     * @param getExtendedIndividualWorkQueueResult
     */
    public void setGetExtendedIndividualWorkQueueResult(com.ecs.stellent.spp.ExtendedWorkQueue getExtendedIndividualWorkQueueResult) {
        this.getExtendedIndividualWorkQueueResult = getExtendedIndividualWorkQueueResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetExtendedIndividualWorkQueueResponse)) return false;
        GetExtendedIndividualWorkQueueResponse other = (GetExtendedIndividualWorkQueueResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getExtendedIndividualWorkQueueResult==null && other.getGetExtendedIndividualWorkQueueResult()==null) || 
             (this.getExtendedIndividualWorkQueueResult!=null &&
              this.getExtendedIndividualWorkQueueResult.equals(other.getGetExtendedIndividualWorkQueueResult())));
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
        if (getGetExtendedIndividualWorkQueueResult() != null) {
            _hashCode += getGetExtendedIndividualWorkQueueResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetExtendedIndividualWorkQueueResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedIndividualWorkQueueResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getExtendedIndividualWorkQueueResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedIndividualWorkQueueResult"));
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
