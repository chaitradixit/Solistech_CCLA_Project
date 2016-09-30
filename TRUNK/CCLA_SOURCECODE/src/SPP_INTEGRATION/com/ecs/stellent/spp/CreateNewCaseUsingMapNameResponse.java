/**
 * CreateNewCaseUsingMapNameResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewCaseUsingMapNameResponse  implements java.io.Serializable {
    private java.lang.String createNewCaseUsingMapNameResult;

    public CreateNewCaseUsingMapNameResponse() {
    }

    public CreateNewCaseUsingMapNameResponse(
           java.lang.String createNewCaseUsingMapNameResult) {
           this.createNewCaseUsingMapNameResult = createNewCaseUsingMapNameResult;
    }


    /**
     * Gets the createNewCaseUsingMapNameResult value for this CreateNewCaseUsingMapNameResponse.
     * 
     * @return createNewCaseUsingMapNameResult
     */
    public java.lang.String getCreateNewCaseUsingMapNameResult() {
        return createNewCaseUsingMapNameResult;
    }


    /**
     * Sets the createNewCaseUsingMapNameResult value for this CreateNewCaseUsingMapNameResponse.
     * 
     * @param createNewCaseUsingMapNameResult
     */
    public void setCreateNewCaseUsingMapNameResult(java.lang.String createNewCaseUsingMapNameResult) {
        this.createNewCaseUsingMapNameResult = createNewCaseUsingMapNameResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewCaseUsingMapNameResponse)) return false;
        CreateNewCaseUsingMapNameResponse other = (CreateNewCaseUsingMapNameResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createNewCaseUsingMapNameResult==null && other.getCreateNewCaseUsingMapNameResult()==null) || 
             (this.createNewCaseUsingMapNameResult!=null &&
              this.createNewCaseUsingMapNameResult.equals(other.getCreateNewCaseUsingMapNameResult())));
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
        if (getCreateNewCaseUsingMapNameResult() != null) {
            _hashCode += getCreateNewCaseUsingMapNameResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewCaseUsingMapNameResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewCaseUsingMapNameResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewCaseUsingMapNameResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewCaseUsingMapNameResult"));
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
