/**
 * CreateNewJobAndProgressResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobAndProgressResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressResult;

    public CreateNewJobAndProgressResponse() {
    }

    public CreateNewJobAndProgressResponse(
           com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressResult) {
           this.createNewJobAndProgressResult = createNewJobAndProgressResult;
    }


    /**
     * Gets the createNewJobAndProgressResult value for this CreateNewJobAndProgressResponse.
     * 
     * @return createNewJobAndProgressResult
     */
    public com.ecs.stellent.spp.ProgressDetails getCreateNewJobAndProgressResult() {
        return createNewJobAndProgressResult;
    }


    /**
     * Sets the createNewJobAndProgressResult value for this CreateNewJobAndProgressResponse.
     * 
     * @param createNewJobAndProgressResult
     */
    public void setCreateNewJobAndProgressResult(com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressResult) {
        this.createNewJobAndProgressResult = createNewJobAndProgressResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobAndProgressResponse)) return false;
        CreateNewJobAndProgressResponse other = (CreateNewJobAndProgressResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createNewJobAndProgressResult==null && other.getCreateNewJobAndProgressResult()==null) || 
             (this.createNewJobAndProgressResult!=null &&
              this.createNewJobAndProgressResult.equals(other.getCreateNewJobAndProgressResult())));
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
        if (getCreateNewJobAndProgressResult() != null) {
            _hashCode += getCreateNewJobAndProgressResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewJobAndProgressResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgressResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobAndProgressResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgressResult"));
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
