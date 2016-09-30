/**
 * GetActivitiesInJobWithStatus2Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetActivitiesInJobWithStatus2Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.ActivitiesForStatus2[] getActivitiesInJobWithStatus2Result;

    public GetActivitiesInJobWithStatus2Response() {
    }

    public GetActivitiesInJobWithStatus2Response(
           com.ecs.stellent.spp.ActivitiesForStatus2[] getActivitiesInJobWithStatus2Result) {
           this.getActivitiesInJobWithStatus2Result = getActivitiesInJobWithStatus2Result;
    }


    /**
     * Gets the getActivitiesInJobWithStatus2Result value for this GetActivitiesInJobWithStatus2Response.
     * 
     * @return getActivitiesInJobWithStatus2Result
     */
    public com.ecs.stellent.spp.ActivitiesForStatus2[] getGetActivitiesInJobWithStatus2Result() {
        return getActivitiesInJobWithStatus2Result;
    }


    /**
     * Sets the getActivitiesInJobWithStatus2Result value for this GetActivitiesInJobWithStatus2Response.
     * 
     * @param getActivitiesInJobWithStatus2Result
     */
    public void setGetActivitiesInJobWithStatus2Result(com.ecs.stellent.spp.ActivitiesForStatus2[] getActivitiesInJobWithStatus2Result) {
        this.getActivitiesInJobWithStatus2Result = getActivitiesInJobWithStatus2Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetActivitiesInJobWithStatus2Response)) return false;
        GetActivitiesInJobWithStatus2Response other = (GetActivitiesInJobWithStatus2Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getActivitiesInJobWithStatus2Result==null && other.getGetActivitiesInJobWithStatus2Result()==null) || 
             (this.getActivitiesInJobWithStatus2Result!=null &&
              java.util.Arrays.equals(this.getActivitiesInJobWithStatus2Result, other.getGetActivitiesInJobWithStatus2Result())));
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
        if (getGetActivitiesInJobWithStatus2Result() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getGetActivitiesInJobWithStatus2Result());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getGetActivitiesInJobWithStatus2Result(), i);
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
        new org.apache.axis.description.TypeDesc(GetActivitiesInJobWithStatus2Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetActivitiesInJobWithStatus2Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getActivitiesInJobWithStatus2Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetActivitiesInJobWithStatus2Result"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus2"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus2"));
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
