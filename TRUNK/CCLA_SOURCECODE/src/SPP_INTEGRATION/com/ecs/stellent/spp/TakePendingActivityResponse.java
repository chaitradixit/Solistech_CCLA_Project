/**
 * TakePendingActivityResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class TakePendingActivityResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.PendingActivity takePendingActivityResult;

    public TakePendingActivityResponse() {
    }

    public TakePendingActivityResponse(
           com.ecs.stellent.spp.PendingActivity takePendingActivityResult) {
           this.takePendingActivityResult = takePendingActivityResult;
    }


    /**
     * Gets the takePendingActivityResult value for this TakePendingActivityResponse.
     * 
     * @return takePendingActivityResult
     */
    public com.ecs.stellent.spp.PendingActivity getTakePendingActivityResult() {
        return takePendingActivityResult;
    }


    /**
     * Sets the takePendingActivityResult value for this TakePendingActivityResponse.
     * 
     * @param takePendingActivityResult
     */
    public void setTakePendingActivityResult(com.ecs.stellent.spp.PendingActivity takePendingActivityResult) {
        this.takePendingActivityResult = takePendingActivityResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TakePendingActivityResponse)) return false;
        TakePendingActivityResponse other = (TakePendingActivityResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.takePendingActivityResult==null && other.getTakePendingActivityResult()==null) || 
             (this.takePendingActivityResult!=null &&
              this.takePendingActivityResult.equals(other.getTakePendingActivityResult())));
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
        if (getTakePendingActivityResult() != null) {
            _hashCode += getTakePendingActivityResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TakePendingActivityResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakePendingActivityResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("takePendingActivityResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakePendingActivityResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PendingActivity"));
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
