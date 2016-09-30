/**
 * TakePendingActivity4Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class TakePendingActivity4Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.PendingActivity takePendingActivity4Result;

    public TakePendingActivity4Response() {
    }

    public TakePendingActivity4Response(
           com.ecs.stellent.spp.PendingActivity takePendingActivity4Result) {
           this.takePendingActivity4Result = takePendingActivity4Result;
    }


    /**
     * Gets the takePendingActivity4Result value for this TakePendingActivity4Response.
     * 
     * @return takePendingActivity4Result
     */
    public com.ecs.stellent.spp.PendingActivity getTakePendingActivity4Result() {
        return takePendingActivity4Result;
    }


    /**
     * Sets the takePendingActivity4Result value for this TakePendingActivity4Response.
     * 
     * @param takePendingActivity4Result
     */
    public void setTakePendingActivity4Result(com.ecs.stellent.spp.PendingActivity takePendingActivity4Result) {
        this.takePendingActivity4Result = takePendingActivity4Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TakePendingActivity4Response)) return false;
        TakePendingActivity4Response other = (TakePendingActivity4Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.takePendingActivity4Result==null && other.getTakePendingActivity4Result()==null) || 
             (this.takePendingActivity4Result!=null &&
              this.takePendingActivity4Result.equals(other.getTakePendingActivity4Result())));
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
        if (getTakePendingActivity4Result() != null) {
            _hashCode += getTakePendingActivity4Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TakePendingActivity4Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakePendingActivity4Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("takePendingActivity4Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakePendingActivity4Result"));
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
