/**
 * GetInitVarListResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetInitVarListResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.Variable[] getInitVarListResult;

    public GetInitVarListResponse() {
    }

    public GetInitVarListResponse(
           com.ecs.stellent.spp.Variable[] getInitVarListResult) {
           this.getInitVarListResult = getInitVarListResult;
    }


    /**
     * Gets the getInitVarListResult value for this GetInitVarListResponse.
     * 
     * @return getInitVarListResult
     */
    public com.ecs.stellent.spp.Variable[] getGetInitVarListResult() {
        return getInitVarListResult;
    }


    /**
     * Sets the getInitVarListResult value for this GetInitVarListResponse.
     * 
     * @param getInitVarListResult
     */
    public void setGetInitVarListResult(com.ecs.stellent.spp.Variable[] getInitVarListResult) {
        this.getInitVarListResult = getInitVarListResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetInitVarListResponse)) return false;
        GetInitVarListResponse other = (GetInitVarListResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getInitVarListResult==null && other.getGetInitVarListResult()==null) || 
             (this.getInitVarListResult!=null &&
              java.util.Arrays.equals(this.getInitVarListResult, other.getGetInitVarListResult())));
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
        if (getGetInitVarListResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetInitVarListResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetInitVarListResult(), i);
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
        new org.apache.axis.description.TypeDesc(GetInitVarListResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetInitVarListResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getInitVarListResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetInitVarListResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
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
