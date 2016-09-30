/**
 * CreateNewJobUsingVersionResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobUsingVersionResponse  implements java.io.Serializable {
    private java.lang.String createNewJobUsingVersionResult;

    public CreateNewJobUsingVersionResponse() {
    }

    public CreateNewJobUsingVersionResponse(
           java.lang.String createNewJobUsingVersionResult) {
           this.createNewJobUsingVersionResult = createNewJobUsingVersionResult;
    }


    /**
     * Gets the createNewJobUsingVersionResult value for this CreateNewJobUsingVersionResponse.
     * 
     * @return createNewJobUsingVersionResult
     */
    public java.lang.String getCreateNewJobUsingVersionResult() {
        return createNewJobUsingVersionResult;
    }


    /**
     * Sets the createNewJobUsingVersionResult value for this CreateNewJobUsingVersionResponse.
     * 
     * @param createNewJobUsingVersionResult
     */
    public void setCreateNewJobUsingVersionResult(java.lang.String createNewJobUsingVersionResult) {
        this.createNewJobUsingVersionResult = createNewJobUsingVersionResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobUsingVersionResponse)) return false;
        CreateNewJobUsingVersionResponse other = (CreateNewJobUsingVersionResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createNewJobUsingVersionResult==null && other.getCreateNewJobUsingVersionResult()==null) || 
             (this.createNewJobUsingVersionResult!=null &&
              this.createNewJobUsingVersionResult.equals(other.getCreateNewJobUsingVersionResult())));
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
        if (getCreateNewJobUsingVersionResult() != null) {
            _hashCode += getCreateNewJobUsingVersionResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewJobUsingVersionResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingVersionResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobUsingVersionResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingVersionResult"));
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
