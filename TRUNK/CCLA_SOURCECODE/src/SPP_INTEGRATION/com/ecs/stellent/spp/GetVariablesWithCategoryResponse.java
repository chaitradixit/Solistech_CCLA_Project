/**
 * GetVariablesWithCategoryResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetVariablesWithCategoryResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.VariableWithCategory[] getVariablesWithCategoryResult;

    public GetVariablesWithCategoryResponse() {
    }

    public GetVariablesWithCategoryResponse(
           com.ecs.stellent.spp.VariableWithCategory[] getVariablesWithCategoryResult) {
           this.getVariablesWithCategoryResult = getVariablesWithCategoryResult;
    }


    /**
     * Gets the getVariablesWithCategoryResult value for this GetVariablesWithCategoryResponse.
     * 
     * @return getVariablesWithCategoryResult
     */
    public com.ecs.stellent.spp.VariableWithCategory[] getGetVariablesWithCategoryResult() {
        return getVariablesWithCategoryResult;
    }


    /**
     * Sets the getVariablesWithCategoryResult value for this GetVariablesWithCategoryResponse.
     * 
     * @param getVariablesWithCategoryResult
     */
    public void setGetVariablesWithCategoryResult(com.ecs.stellent.spp.VariableWithCategory[] getVariablesWithCategoryResult) {
        this.getVariablesWithCategoryResult = getVariablesWithCategoryResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetVariablesWithCategoryResponse)) return false;
        GetVariablesWithCategoryResponse other = (GetVariablesWithCategoryResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getVariablesWithCategoryResult==null && other.getGetVariablesWithCategoryResult()==null) || 
             (this.getVariablesWithCategoryResult!=null &&
              java.util.Arrays.equals(this.getVariablesWithCategoryResult, other.getGetVariablesWithCategoryResult())));
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
        if (getGetVariablesWithCategoryResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetVariablesWithCategoryResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetVariablesWithCategoryResult(), i);
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
        new org.apache.axis.description.TypeDesc(GetVariablesWithCategoryResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetVariablesWithCategoryResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getVariablesWithCategoryResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetVariablesWithCategoryResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableWithCategory"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableWithCategory"));
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
