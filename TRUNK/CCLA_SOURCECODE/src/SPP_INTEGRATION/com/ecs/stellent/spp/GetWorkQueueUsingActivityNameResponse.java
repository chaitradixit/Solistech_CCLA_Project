/**
 * GetWorkQueueUsingActivityNameResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetWorkQueueUsingActivityNameResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.WorkQueue getWorkQueueUsingActivityNameResult;

    public GetWorkQueueUsingActivityNameResponse() {
    }

    public GetWorkQueueUsingActivityNameResponse(
           com.ecs.stellent.spp.WorkQueue getWorkQueueUsingActivityNameResult) {
           this.getWorkQueueUsingActivityNameResult = getWorkQueueUsingActivityNameResult;
    }


    /**
     * Gets the getWorkQueueUsingActivityNameResult value for this GetWorkQueueUsingActivityNameResponse.
     * 
     * @return getWorkQueueUsingActivityNameResult
     */
    public com.ecs.stellent.spp.WorkQueue getGetWorkQueueUsingActivityNameResult() {
        return getWorkQueueUsingActivityNameResult;
    }


    /**
     * Sets the getWorkQueueUsingActivityNameResult value for this GetWorkQueueUsingActivityNameResponse.
     * 
     * @param getWorkQueueUsingActivityNameResult
     */
    public void setGetWorkQueueUsingActivityNameResult(com.ecs.stellent.spp.WorkQueue getWorkQueueUsingActivityNameResult) {
        this.getWorkQueueUsingActivityNameResult = getWorkQueueUsingActivityNameResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetWorkQueueUsingActivityNameResponse)) return false;
        GetWorkQueueUsingActivityNameResponse other = (GetWorkQueueUsingActivityNameResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getWorkQueueUsingActivityNameResult==null && other.getGetWorkQueueUsingActivityNameResult()==null) || 
             (this.getWorkQueueUsingActivityNameResult!=null &&
              this.getWorkQueueUsingActivityNameResult.equals(other.getGetWorkQueueUsingActivityNameResult())));
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
        if (getGetWorkQueueUsingActivityNameResult() != null) {
            _hashCode += getGetWorkQueueUsingActivityNameResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetWorkQueueUsingActivityNameResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkQueueUsingActivityNameResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getWorkQueueUsingActivityNameResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkQueueUsingActivityNameResult"));
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
