/**
 * GetWorkQueueResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetWorkQueueResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.WorkQueue getWorkQueueResult;

    public GetWorkQueueResponse() {
    }

    public GetWorkQueueResponse(
           com.ecs.stellent.spp.WorkQueue getWorkQueueResult) {
           this.getWorkQueueResult = getWorkQueueResult;
    }


    /**
     * Gets the getWorkQueueResult value for this GetWorkQueueResponse.
     * 
     * @return getWorkQueueResult
     */
    public com.ecs.stellent.spp.WorkQueue getGetWorkQueueResult() {
        return getWorkQueueResult;
    }


    /**
     * Sets the getWorkQueueResult value for this GetWorkQueueResponse.
     * 
     * @param getWorkQueueResult
     */
    public void setGetWorkQueueResult(com.ecs.stellent.spp.WorkQueue getWorkQueueResult) {
        this.getWorkQueueResult = getWorkQueueResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetWorkQueueResponse)) return false;
        GetWorkQueueResponse other = (GetWorkQueueResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getWorkQueueResult==null && other.getGetWorkQueueResult()==null) || 
             (this.getWorkQueueResult!=null &&
              this.getWorkQueueResult.equals(other.getGetWorkQueueResult())));
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
        if (getGetWorkQueueResult() != null) {
            _hashCode += getGetWorkQueueResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetWorkQueueResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkQueueResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getWorkQueueResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkQueueResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueue"));
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
