/**
 * CreateNewJobUsingStartDateUsingVersionResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobUsingStartDateUsingVersionResponse  implements java.io.Serializable {
    private java.lang.String createNewJobUsingStartDateUsingVersionResult;

    public CreateNewJobUsingStartDateUsingVersionResponse() {
    }

    public CreateNewJobUsingStartDateUsingVersionResponse(
           java.lang.String createNewJobUsingStartDateUsingVersionResult) {
           this.createNewJobUsingStartDateUsingVersionResult = createNewJobUsingStartDateUsingVersionResult;
    }


    /**
     * Gets the createNewJobUsingStartDateUsingVersionResult value for this CreateNewJobUsingStartDateUsingVersionResponse.
     * 
     * @return createNewJobUsingStartDateUsingVersionResult
     */
    public java.lang.String getCreateNewJobUsingStartDateUsingVersionResult() {
        return createNewJobUsingStartDateUsingVersionResult;
    }


    /**
     * Sets the createNewJobUsingStartDateUsingVersionResult value for this CreateNewJobUsingStartDateUsingVersionResponse.
     * 
     * @param createNewJobUsingStartDateUsingVersionResult
     */
    public void setCreateNewJobUsingStartDateUsingVersionResult(java.lang.String createNewJobUsingStartDateUsingVersionResult) {
        this.createNewJobUsingStartDateUsingVersionResult = createNewJobUsingStartDateUsingVersionResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobUsingStartDateUsingVersionResponse)) return false;
        CreateNewJobUsingStartDateUsingVersionResponse other = (CreateNewJobUsingStartDateUsingVersionResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createNewJobUsingStartDateUsingVersionResult==null && other.getCreateNewJobUsingStartDateUsingVersionResult()==null) || 
             (this.createNewJobUsingStartDateUsingVersionResult!=null &&
              this.createNewJobUsingStartDateUsingVersionResult.equals(other.getCreateNewJobUsingStartDateUsingVersionResult())));
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
        if (getCreateNewJobUsingStartDateUsingVersionResult() != null) {
            _hashCode += getCreateNewJobUsingStartDateUsingVersionResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewJobUsingStartDateUsingVersionResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingStartDateUsingVersionResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobUsingStartDateUsingVersionResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingStartDateUsingVersionResult"));
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
