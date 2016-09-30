/**
 * GetMilestonesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetMilestonesResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.Milestones[] getMilestonesResult;

    public GetMilestonesResponse() {
    }

    public GetMilestonesResponse(
           com.ecs.stellent.spp.Milestones[] getMilestonesResult) {
           this.getMilestonesResult = getMilestonesResult;
    }


    /**
     * Gets the getMilestonesResult value for this GetMilestonesResponse.
     * 
     * @return getMilestonesResult
     */
    public com.ecs.stellent.spp.Milestones[] getGetMilestonesResult() {
        return getMilestonesResult;
    }


    /**
     * Sets the getMilestonesResult value for this GetMilestonesResponse.
     * 
     * @param getMilestonesResult
     */
    public void setGetMilestonesResult(com.ecs.stellent.spp.Milestones[] getMilestonesResult) {
        this.getMilestonesResult = getMilestonesResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetMilestonesResponse)) return false;
        GetMilestonesResponse other = (GetMilestonesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getMilestonesResult==null && other.getGetMilestonesResult()==null) || 
             (this.getMilestonesResult!=null &&
              java.util.Arrays.equals(this.getMilestonesResult, other.getGetMilestonesResult())));
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
        if (getGetMilestonesResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetMilestonesResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetMilestonesResult(), i);
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
        new org.apache.axis.description.TypeDesc(GetMilestonesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMilestonesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getMilestonesResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMilestonesResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Milestones"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Milestones"));
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
