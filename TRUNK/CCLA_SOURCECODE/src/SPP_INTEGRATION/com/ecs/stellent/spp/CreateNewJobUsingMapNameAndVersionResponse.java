/**
 * CreateNewJobUsingMapNameAndVersionResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobUsingMapNameAndVersionResponse  implements java.io.Serializable {
    private java.lang.String createNewJobUsingMapNameAndVersionResult;

    public CreateNewJobUsingMapNameAndVersionResponse() {
    }

    public CreateNewJobUsingMapNameAndVersionResponse(
           java.lang.String createNewJobUsingMapNameAndVersionResult) {
           this.createNewJobUsingMapNameAndVersionResult = createNewJobUsingMapNameAndVersionResult;
    }


    /**
     * Gets the createNewJobUsingMapNameAndVersionResult value for this CreateNewJobUsingMapNameAndVersionResponse.
     * 
     * @return createNewJobUsingMapNameAndVersionResult
     */
    public java.lang.String getCreateNewJobUsingMapNameAndVersionResult() {
        return createNewJobUsingMapNameAndVersionResult;
    }


    /**
     * Sets the createNewJobUsingMapNameAndVersionResult value for this CreateNewJobUsingMapNameAndVersionResponse.
     * 
     * @param createNewJobUsingMapNameAndVersionResult
     */
    public void setCreateNewJobUsingMapNameAndVersionResult(java.lang.String createNewJobUsingMapNameAndVersionResult) {
        this.createNewJobUsingMapNameAndVersionResult = createNewJobUsingMapNameAndVersionResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobUsingMapNameAndVersionResponse)) return false;
        CreateNewJobUsingMapNameAndVersionResponse other = (CreateNewJobUsingMapNameAndVersionResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createNewJobUsingMapNameAndVersionResult==null && other.getCreateNewJobUsingMapNameAndVersionResult()==null) || 
             (this.createNewJobUsingMapNameAndVersionResult!=null &&
              this.createNewJobUsingMapNameAndVersionResult.equals(other.getCreateNewJobUsingMapNameAndVersionResult())));
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
        if (getCreateNewJobUsingMapNameAndVersionResult() != null) {
            _hashCode += getCreateNewJobUsingMapNameAndVersionResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewJobUsingMapNameAndVersionResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingMapNameAndVersionResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobUsingMapNameAndVersionResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingMapNameAndVersionResult"));
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
