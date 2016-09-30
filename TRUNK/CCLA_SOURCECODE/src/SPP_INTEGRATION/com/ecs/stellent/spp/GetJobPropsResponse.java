/**
 * GetJobPropsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class GetJobPropsResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.JobProperties getJobPropsResult;

    public GetJobPropsResponse() {
    }

    public GetJobPropsResponse(
           com.ecs.stellent.spp.JobProperties getJobPropsResult) {
           this.getJobPropsResult = getJobPropsResult;
    }


    /**
     * Gets the getJobPropsResult value for this GetJobPropsResponse.
     * 
     * @return getJobPropsResult
     */
    public com.ecs.stellent.spp.JobProperties getGetJobPropsResult() {
        return getJobPropsResult;
    }


    /**
     * Sets the getJobPropsResult value for this GetJobPropsResponse.
     * 
     * @param getJobPropsResult
     */
    public void setGetJobPropsResult(com.ecs.stellent.spp.JobProperties getJobPropsResult) {
        this.getJobPropsResult = getJobPropsResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetJobPropsResponse)) return false;
        GetJobPropsResponse other = (GetJobPropsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.getJobPropsResult==null && other.getGetJobPropsResult()==null) || 
             (this.getJobPropsResult!=null &&
              this.getJobPropsResult.equals(other.getGetJobPropsResult())));
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
        if (getGetJobPropsResult() != null) {
            _hashCode += getGetJobPropsResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetJobPropsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobPropsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("getJobPropsResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobPropsResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
