/**
 * GetWorkqueueOverviewForResourceResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetWorkqueueOverviewForResourceResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.WorkqueueOverview getWorkqueueOverviewForResourceResult;

    public GetWorkqueueOverviewForResourceResponse() {
    }

    public GetWorkqueueOverviewForResourceResponse(
           com.ecs.stellent.spp.WorkqueueOverview getWorkqueueOverviewForResourceResult) {
           this.getWorkqueueOverviewForResourceResult = getWorkqueueOverviewForResourceResult;
    }


    /**
     * Gets the getWorkqueueOverviewForResourceResult value for this GetWorkqueueOverviewForResourceResponse.
     * 
     * @return getWorkqueueOverviewForResourceResult
     */
    public com.ecs.stellent.spp.WorkqueueOverview getGetWorkqueueOverviewForResourceResult() {
        return getWorkqueueOverviewForResourceResult;
    }


    /**
     * Sets the getWorkqueueOverviewForResourceResult value for this GetWorkqueueOverviewForResourceResponse.
     * 
     * @param getWorkqueueOverviewForResourceResult
     */
    public void setGetWorkqueueOverviewForResourceResult(com.ecs.stellent.spp.WorkqueueOverview getWorkqueueOverviewForResourceResult) {
        this.getWorkqueueOverviewForResourceResult = getWorkqueueOverviewForResourceResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetWorkqueueOverviewForResourceResponse)) return false;
        GetWorkqueueOverviewForResourceResponse other = (GetWorkqueueOverviewForResourceResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getWorkqueueOverviewForResourceResult==null && other.getGetWorkqueueOverviewForResourceResult()==null) || 
             (this.getWorkqueueOverviewForResourceResult!=null &&
              this.getWorkqueueOverviewForResourceResult.equals(other.getGetWorkqueueOverviewForResourceResult())));
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
        if (getGetWorkqueueOverviewForResourceResult() != null) {
            _hashCode += getGetWorkqueueOverviewForResourceResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetWorkqueueOverviewForResourceResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkqueueOverviewForResourceResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getWorkqueueOverviewForResourceResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkqueueOverviewForResourceResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkqueueOverview"));
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
