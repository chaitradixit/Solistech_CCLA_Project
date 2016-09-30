/**
 * GetResourcesWithSkillAndSecurityResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetResourcesWithSkillAndSecurityResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.MTSResourceDetails[] getResourcesWithSkillAndSecurityResult;

    public GetResourcesWithSkillAndSecurityResponse() {
    }

    public GetResourcesWithSkillAndSecurityResponse(
           com.ecs.stellent.spp.MTSResourceDetails[] getResourcesWithSkillAndSecurityResult) {
           this.getResourcesWithSkillAndSecurityResult = getResourcesWithSkillAndSecurityResult;
    }


    /**
     * Gets the getResourcesWithSkillAndSecurityResult value for this GetResourcesWithSkillAndSecurityResponse.
     * 
     * @return getResourcesWithSkillAndSecurityResult
     */
    public com.ecs.stellent.spp.MTSResourceDetails[] getGetResourcesWithSkillAndSecurityResult() {
        return getResourcesWithSkillAndSecurityResult;
    }


    /**
     * Sets the getResourcesWithSkillAndSecurityResult value for this GetResourcesWithSkillAndSecurityResponse.
     * 
     * @param getResourcesWithSkillAndSecurityResult
     */
    public void setGetResourcesWithSkillAndSecurityResult(com.ecs.stellent.spp.MTSResourceDetails[] getResourcesWithSkillAndSecurityResult) {
        this.getResourcesWithSkillAndSecurityResult = getResourcesWithSkillAndSecurityResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetResourcesWithSkillAndSecurityResponse)) return false;
        GetResourcesWithSkillAndSecurityResponse other = (GetResourcesWithSkillAndSecurityResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getResourcesWithSkillAndSecurityResult==null && other.getGetResourcesWithSkillAndSecurityResult()==null) || 
             (this.getResourcesWithSkillAndSecurityResult!=null &&
              java.util.Arrays.equals(this.getResourcesWithSkillAndSecurityResult, other.getGetResourcesWithSkillAndSecurityResult())));
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
        if (getGetResourcesWithSkillAndSecurityResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetResourcesWithSkillAndSecurityResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetResourcesWithSkillAndSecurityResult(), i);
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
        new org.apache.axis.description.TypeDesc(GetResourcesWithSkillAndSecurityResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetResourcesWithSkillAndSecurityResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getResourcesWithSkillAndSecurityResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetResourcesWithSkillAndSecurityResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MTSResourceDetails"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MTSResourceDetails"));
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
