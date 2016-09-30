/**
 * CreateNewJobUsingMapNameResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobUsingMapNameResponse  implements java.io.Serializable {
    private java.lang.String createNewJobUsingMapNameResult;

    public CreateNewJobUsingMapNameResponse() {
    }

    public CreateNewJobUsingMapNameResponse(
           java.lang.String createNewJobUsingMapNameResult) {
           this.createNewJobUsingMapNameResult = createNewJobUsingMapNameResult;
    }


    /**
     * Gets the createNewJobUsingMapNameResult value for this CreateNewJobUsingMapNameResponse.
     * 
     * @return createNewJobUsingMapNameResult
     */
    public java.lang.String getCreateNewJobUsingMapNameResult() {
        return createNewJobUsingMapNameResult;
    }


    /**
     * Sets the createNewJobUsingMapNameResult value for this CreateNewJobUsingMapNameResponse.
     * 
     * @param createNewJobUsingMapNameResult
     */
    public void setCreateNewJobUsingMapNameResult(java.lang.String createNewJobUsingMapNameResult) {
        this.createNewJobUsingMapNameResult = createNewJobUsingMapNameResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobUsingMapNameResponse)) return false;
        CreateNewJobUsingMapNameResponse other = (CreateNewJobUsingMapNameResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createNewJobUsingMapNameResult==null && other.getCreateNewJobUsingMapNameResult()==null) || 
             (this.createNewJobUsingMapNameResult!=null &&
              this.createNewJobUsingMapNameResult.equals(other.getCreateNewJobUsingMapNameResult())));
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
        if (getCreateNewJobUsingMapNameResult() != null) {
            _hashCode += getCreateNewJobUsingMapNameResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewJobUsingMapNameResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingMapNameResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobUsingMapNameResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingMapNameResult"));
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
