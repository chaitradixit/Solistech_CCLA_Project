/**
 * GetJaggedArrayResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJaggedArrayResponse  implements java.io.Serializable {
    private java.lang.Object[][] getJaggedArrayResult;

    public GetJaggedArrayResponse() {
    }

    public GetJaggedArrayResponse(
           java.lang.Object[][] getJaggedArrayResult) {
           this.getJaggedArrayResult = getJaggedArrayResult;
    }


    /**
     * Gets the getJaggedArrayResult value for this GetJaggedArrayResponse.
     * 
     * @return getJaggedArrayResult
     */
    public java.lang.Object[][] getGetJaggedArrayResult() {
        return getJaggedArrayResult;
    }


    /**
     * Sets the getJaggedArrayResult value for this GetJaggedArrayResponse.
     * 
     * @param getJaggedArrayResult
     */
    public void setGetJaggedArrayResult(java.lang.Object[][] getJaggedArrayResult) {
        this.getJaggedArrayResult = getJaggedArrayResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJaggedArrayResponse)) return false;
        GetJaggedArrayResponse other = (GetJaggedArrayResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getJaggedArrayResult==null && other.getGetJaggedArrayResult()==null) || 
             (this.getJaggedArrayResult!=null &&
              java.util.Arrays.equals(this.getJaggedArrayResult, other.getGetJaggedArrayResult())));
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
        if (getGetJaggedArrayResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetJaggedArrayResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetJaggedArrayResult(), i);
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
        new org.apache.axis.description.TypeDesc(GetJaggedArrayResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJaggedArrayResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getJaggedArrayResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJaggedArrayResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfAnyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfAnyType"));
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
