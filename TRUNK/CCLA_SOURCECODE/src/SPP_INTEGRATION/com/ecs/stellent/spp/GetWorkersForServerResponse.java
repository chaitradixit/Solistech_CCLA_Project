/**
 * GetWorkersForServerResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetWorkersForServerResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.WorkersInfo[] getWorkersForServerResult;

    public GetWorkersForServerResponse() {
    }

    public GetWorkersForServerResponse(
           com.ecs.stellent.spp.WorkersInfo[] getWorkersForServerResult) {
           this.getWorkersForServerResult = getWorkersForServerResult;
    }


    /**
     * Gets the getWorkersForServerResult value for this GetWorkersForServerResponse.
     * 
     * @return getWorkersForServerResult
     */
    public com.ecs.stellent.spp.WorkersInfo[] getGetWorkersForServerResult() {
        return getWorkersForServerResult;
    }


    /**
     * Sets the getWorkersForServerResult value for this GetWorkersForServerResponse.
     * 
     * @param getWorkersForServerResult
     */
    public void setGetWorkersForServerResult(com.ecs.stellent.spp.WorkersInfo[] getWorkersForServerResult) {
        this.getWorkersForServerResult = getWorkersForServerResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetWorkersForServerResponse)) return false;
        GetWorkersForServerResponse other = (GetWorkersForServerResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getWorkersForServerResult==null && other.getGetWorkersForServerResult()==null) || 
             (this.getWorkersForServerResult!=null &&
              java.util.Arrays.equals(this.getWorkersForServerResult, other.getGetWorkersForServerResult())));
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
        if (getGetWorkersForServerResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetWorkersForServerResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetWorkersForServerResult(), i);
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
        new org.apache.axis.description.TypeDesc(GetWorkersForServerResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkersForServerResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getWorkersForServerResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkersForServerResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkersInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkersInfo"));
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
