/**
 * GetEngineListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetEngineListResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.EngineDetails[] getEngineListResult;

    public GetEngineListResponse() {
    }

    public GetEngineListResponse(
           com.ecs.stellent.spp.EngineDetails[] getEngineListResult) {
           this.getEngineListResult = getEngineListResult;
    }


    /**
     * Gets the getEngineListResult value for this GetEngineListResponse.
     * 
     * @return getEngineListResult
     */
    public com.ecs.stellent.spp.EngineDetails[] getGetEngineListResult() {
        return getEngineListResult;
    }


    /**
     * Sets the getEngineListResult value for this GetEngineListResponse.
     * 
     * @param getEngineListResult
     */
    public void setGetEngineListResult(com.ecs.stellent.spp.EngineDetails[] getEngineListResult) {
        this.getEngineListResult = getEngineListResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetEngineListResponse)) return false;
        GetEngineListResponse other = (GetEngineListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getEngineListResult==null && other.getGetEngineListResult()==null) || 
             (this.getEngineListResult!=null &&
              java.util.Arrays.equals(this.getEngineListResult, other.getGetEngineListResult())));
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
        if (getGetEngineListResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetEngineListResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetEngineListResult(), i);
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
        new org.apache.axis.description.TypeDesc(GetEngineListResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetEngineListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getEngineListResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetEngineListResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EngineDetails"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EngineDetails"));
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
