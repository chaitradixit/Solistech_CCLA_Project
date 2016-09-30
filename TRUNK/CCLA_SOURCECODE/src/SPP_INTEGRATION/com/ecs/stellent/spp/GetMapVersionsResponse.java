/**
 * GetMapVersionsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetMapVersionsResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.MapVersionDetails[] getMapVersionsResult;

    public GetMapVersionsResponse() {
    }

    public GetMapVersionsResponse(
           com.ecs.stellent.spp.MapVersionDetails[] getMapVersionsResult) {
           this.getMapVersionsResult = getMapVersionsResult;
    }


    /**
     * Gets the getMapVersionsResult value for this GetMapVersionsResponse.
     * 
     * @return getMapVersionsResult
     */
    public com.ecs.stellent.spp.MapVersionDetails[] getGetMapVersionsResult() {
        return getMapVersionsResult;
    }


    /**
     * Sets the getMapVersionsResult value for this GetMapVersionsResponse.
     * 
     * @param getMapVersionsResult
     */
    public void setGetMapVersionsResult(com.ecs.stellent.spp.MapVersionDetails[] getMapVersionsResult) {
        this.getMapVersionsResult = getMapVersionsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetMapVersionsResponse)) return false;
        GetMapVersionsResponse other = (GetMapVersionsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getMapVersionsResult==null && other.getGetMapVersionsResult()==null) || 
             (this.getMapVersionsResult!=null &&
              java.util.Arrays.equals(this.getMapVersionsResult, other.getGetMapVersionsResult())));
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
        if (getGetMapVersionsResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetMapVersionsResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetMapVersionsResult(), i);
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
        new org.apache.axis.description.TypeDesc(GetMapVersionsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMapVersionsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getMapVersionsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMapVersionsResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersionDetails"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersionDetails"));
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
