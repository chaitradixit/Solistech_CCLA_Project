/**
 * CreateJobInCaseUsingRefResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateJobInCaseUsingRefResponse  implements java.io.Serializable {
    private java.lang.String createJobInCaseUsingRefResult;

    public CreateJobInCaseUsingRefResponse() {
    }

    public CreateJobInCaseUsingRefResponse(
           java.lang.String createJobInCaseUsingRefResult) {
           this.createJobInCaseUsingRefResult = createJobInCaseUsingRefResult;
    }


    /**
     * Gets the createJobInCaseUsingRefResult value for this CreateJobInCaseUsingRefResponse.
     * 
     * @return createJobInCaseUsingRefResult
     */
    public java.lang.String getCreateJobInCaseUsingRefResult() {
        return createJobInCaseUsingRefResult;
    }


    /**
     * Sets the createJobInCaseUsingRefResult value for this CreateJobInCaseUsingRefResponse.
     * 
     * @param createJobInCaseUsingRefResult
     */
    public void setCreateJobInCaseUsingRefResult(java.lang.String createJobInCaseUsingRefResult) {
        this.createJobInCaseUsingRefResult = createJobInCaseUsingRefResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateJobInCaseUsingRefResponse)) return false;
        CreateJobInCaseUsingRefResponse other = (CreateJobInCaseUsingRefResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createJobInCaseUsingRefResult==null && other.getCreateJobInCaseUsingRefResult()==null) || 
             (this.createJobInCaseUsingRefResult!=null &&
              this.createJobInCaseUsingRefResult.equals(other.getCreateJobInCaseUsingRefResult())));
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
        if (getCreateJobInCaseUsingRefResult() != null) {
            _hashCode += getCreateJobInCaseUsingRefResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateJobInCaseUsingRefResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateJobInCaseUsingRefResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createJobInCaseUsingRefResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateJobInCaseUsingRefResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
