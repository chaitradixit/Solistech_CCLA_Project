/**
 * CreateNewJobAndProgressUsingVersionResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobAndProgressUsingVersionResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressUsingVersionResult;

    public CreateNewJobAndProgressUsingVersionResponse() {
    }

    public CreateNewJobAndProgressUsingVersionResponse(
           com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressUsingVersionResult) {
           this.createNewJobAndProgressUsingVersionResult = createNewJobAndProgressUsingVersionResult;
    }


    /**
     * Gets the createNewJobAndProgressUsingVersionResult value for this CreateNewJobAndProgressUsingVersionResponse.
     * 
     * @return createNewJobAndProgressUsingVersionResult
     */
    public com.ecs.stellent.spp.ProgressDetails getCreateNewJobAndProgressUsingVersionResult() {
        return createNewJobAndProgressUsingVersionResult;
    }


    /**
     * Sets the createNewJobAndProgressUsingVersionResult value for this CreateNewJobAndProgressUsingVersionResponse.
     * 
     * @param createNewJobAndProgressUsingVersionResult
     */
    public void setCreateNewJobAndProgressUsingVersionResult(com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressUsingVersionResult) {
        this.createNewJobAndProgressUsingVersionResult = createNewJobAndProgressUsingVersionResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobAndProgressUsingVersionResponse)) return false;
        CreateNewJobAndProgressUsingVersionResponse other = (CreateNewJobAndProgressUsingVersionResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createNewJobAndProgressUsingVersionResult==null && other.getCreateNewJobAndProgressUsingVersionResult()==null) || 
             (this.createNewJobAndProgressUsingVersionResult!=null &&
              this.createNewJobAndProgressUsingVersionResult.equals(other.getCreateNewJobAndProgressUsingVersionResult())));
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
        if (getCreateNewJobAndProgressUsingVersionResult() != null) {
            _hashCode += getCreateNewJobAndProgressUsingVersionResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewJobAndProgressUsingVersionResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgressUsingVersionResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobAndProgressUsingVersionResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgressUsingVersionResult"));
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
