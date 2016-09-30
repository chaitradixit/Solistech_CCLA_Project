/**
 * CreateNewJobAndProgressUsingNameResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobAndProgressUsingNameResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressUsingNameResult;

    public CreateNewJobAndProgressUsingNameResponse() {
    }

    public CreateNewJobAndProgressUsingNameResponse(
           com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressUsingNameResult) {
           this.createNewJobAndProgressUsingNameResult = createNewJobAndProgressUsingNameResult;
    }


    /**
     * Gets the createNewJobAndProgressUsingNameResult value for this CreateNewJobAndProgressUsingNameResponse.
     * 
     * @return createNewJobAndProgressUsingNameResult
     */
    public com.ecs.stellent.spp.ProgressDetails getCreateNewJobAndProgressUsingNameResult() {
        return createNewJobAndProgressUsingNameResult;
    }


    /**
     * Sets the createNewJobAndProgressUsingNameResult value for this CreateNewJobAndProgressUsingNameResponse.
     * 
     * @param createNewJobAndProgressUsingNameResult
     */
    public void setCreateNewJobAndProgressUsingNameResult(com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressUsingNameResult) {
        this.createNewJobAndProgressUsingNameResult = createNewJobAndProgressUsingNameResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobAndProgressUsingNameResponse)) return false;
        CreateNewJobAndProgressUsingNameResponse other = (CreateNewJobAndProgressUsingNameResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createNewJobAndProgressUsingNameResult==null && other.getCreateNewJobAndProgressUsingNameResult()==null) || 
             (this.createNewJobAndProgressUsingNameResult!=null &&
              this.createNewJobAndProgressUsingNameResult.equals(other.getCreateNewJobAndProgressUsingNameResult())));
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
        if (getCreateNewJobAndProgressUsingNameResult() != null) {
            _hashCode += getCreateNewJobAndProgressUsingNameResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewJobAndProgressUsingNameResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgressUsingNameResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobAndProgressUsingNameResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgressUsingNameResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProgressDetails"));
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
