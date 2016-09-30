/**
 * GetInitVarList3Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetInitVarList3Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.VariableDisplayName[] getInitVarList3Result;

    public GetInitVarList3Response() {
    }

    public GetInitVarList3Response(
           com.ecs.stellent.spp.VariableDisplayName[] getInitVarList3Result) {
           this.getInitVarList3Result = getInitVarList3Result;
    }


    /**
     * Gets the getInitVarList3Result value for this GetInitVarList3Response.
     * 
     * @return getInitVarList3Result
     */
    public com.ecs.stellent.spp.VariableDisplayName[] getGetInitVarList3Result() {
        return getInitVarList3Result;
    }


    /**
     * Sets the getInitVarList3Result value for this GetInitVarList3Response.
     * 
     * @param getInitVarList3Result
     */
    public void setGetInitVarList3Result(com.ecs.stellent.spp.VariableDisplayName[] getInitVarList3Result) {
        this.getInitVarList3Result = getInitVarList3Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetInitVarList3Response)) return false;
        GetInitVarList3Response other = (GetInitVarList3Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getInitVarList3Result==null && other.getGetInitVarList3Result()==null) || 
             (this.getInitVarList3Result!=null &&
              java.util.Arrays.equals(this.getInitVarList3Result, other.getGetInitVarList3Result())));
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
        if (getGetInitVarList3Result() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetInitVarList3Result());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetInitVarList3Result(), i);
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
        new org.apache.axis.description.TypeDesc(GetInitVarList3Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetInitVarList3Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getInitVarList3Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetInitVarList3Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableDisplayName"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableDisplayName"));
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
