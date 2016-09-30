/**
 * GetActivitiesInJobWithStatus3Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetActivitiesInJobWithStatus3Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.ActivitiesForStatus3[] getActivitiesInJobWithStatus3Result;

    public GetActivitiesInJobWithStatus3Response() {
    }

    public GetActivitiesInJobWithStatus3Response(
           com.ecs.stellent.spp.ActivitiesForStatus3[] getActivitiesInJobWithStatus3Result) {
           this.getActivitiesInJobWithStatus3Result = getActivitiesInJobWithStatus3Result;
    }


    /**
     * Gets the getActivitiesInJobWithStatus3Result value for this GetActivitiesInJobWithStatus3Response.
     * 
     * @return getActivitiesInJobWithStatus3Result
     */
    public com.ecs.stellent.spp.ActivitiesForStatus3[] getGetActivitiesInJobWithStatus3Result() {
        return getActivitiesInJobWithStatus3Result;
    }


    /**
     * Sets the getActivitiesInJobWithStatus3Result value for this GetActivitiesInJobWithStatus3Response.
     * 
     * @param getActivitiesInJobWithStatus3Result
     */
    public void setGetActivitiesInJobWithStatus3Result(com.ecs.stellent.spp.ActivitiesForStatus3[] getActivitiesInJobWithStatus3Result) {
        this.getActivitiesInJobWithStatus3Result = getActivitiesInJobWithStatus3Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetActivitiesInJobWithStatus3Response)) return false;
        GetActivitiesInJobWithStatus3Response other = (GetActivitiesInJobWithStatus3Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getActivitiesInJobWithStatus3Result==null && other.getGetActivitiesInJobWithStatus3Result()==null) || 
             (this.getActivitiesInJobWithStatus3Result!=null &&
              java.util.Arrays.equals(this.getActivitiesInJobWithStatus3Result, other.getGetActivitiesInJobWithStatus3Result())));
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
        if (getGetActivitiesInJobWithStatus3Result() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetActivitiesInJobWithStatus3Result());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetActivitiesInJobWithStatus3Result(), i);
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
        new org.apache.axis.description.TypeDesc(GetActivitiesInJobWithStatus3Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetActivitiesInJobWithStatus3Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getActivitiesInJobWithStatus3Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetActivitiesInJobWithStatus3Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus3"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus3"));
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
