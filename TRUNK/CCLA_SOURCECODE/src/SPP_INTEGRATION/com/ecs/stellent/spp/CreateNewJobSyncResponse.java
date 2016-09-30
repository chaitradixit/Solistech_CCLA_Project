/**
 * CreateNewJobSyncResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobSyncResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.CreateNewJobSyncDetails createNewJobSyncResult;

    public CreateNewJobSyncResponse() {
    }

    public CreateNewJobSyncResponse(
           com.ecs.stellent.spp.CreateNewJobSyncDetails createNewJobSyncResult) {
           this.createNewJobSyncResult = createNewJobSyncResult;
    }


    /**
     * Gets the createNewJobSyncResult value for this CreateNewJobSyncResponse.
     * 
     * @return createNewJobSyncResult
     */
    public com.ecs.stellent.spp.CreateNewJobSyncDetails getCreateNewJobSyncResult() {
        return createNewJobSyncResult;
    }


    /**
     * Sets the createNewJobSyncResult value for this CreateNewJobSyncResponse.
     * 
     * @param createNewJobSyncResult
     */
    public void setCreateNewJobSyncResult(com.ecs.stellent.spp.CreateNewJobSyncDetails createNewJobSyncResult) {
        this.createNewJobSyncResult = createNewJobSyncResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobSyncResponse)) return false;
        CreateNewJobSyncResponse other = (CreateNewJobSyncResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createNewJobSyncResult==null && other.getCreateNewJobSyncResult()==null) || 
             (this.createNewJobSyncResult!=null &&
              this.createNewJobSyncResult.equals(other.getCreateNewJobSyncResult())));
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
        if (getCreateNewJobSyncResult() != null) {
            _hashCode += getCreateNewJobSyncResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewJobSyncResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobSyncResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobSyncResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobSyncResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobSyncDetails"));
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
