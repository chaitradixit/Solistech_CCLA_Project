/**
 * TakeActivityFromJobResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class TakeActivityFromJobResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.ActivityInfo takeActivityFromJobResult;

    public TakeActivityFromJobResponse() {
    }

    public TakeActivityFromJobResponse(
           com.ecs.stellent.spp.ActivityInfo takeActivityFromJobResult) {
           this.takeActivityFromJobResult = takeActivityFromJobResult;
    }


    /**
     * Gets the takeActivityFromJobResult value for this TakeActivityFromJobResponse.
     * 
     * @return takeActivityFromJobResult
     */
    public com.ecs.stellent.spp.ActivityInfo getTakeActivityFromJobResult() {
        return takeActivityFromJobResult;
    }


    /**
     * Sets the takeActivityFromJobResult value for this TakeActivityFromJobResponse.
     * 
     * @param takeActivityFromJobResult
     */
    public void setTakeActivityFromJobResult(com.ecs.stellent.spp.ActivityInfo takeActivityFromJobResult) {
        this.takeActivityFromJobResult = takeActivityFromJobResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TakeActivityFromJobResponse)) return false;
        TakeActivityFromJobResponse other = (TakeActivityFromJobResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.takeActivityFromJobResult==null && other.getTakeActivityFromJobResult()==null) || 
             (this.takeActivityFromJobResult!=null &&
              this.takeActivityFromJobResult.equals(other.getTakeActivityFromJobResult())));
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
        if (getTakeActivityFromJobResult() != null) {
            _hashCode += getTakeActivityFromJobResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TakeActivityFromJobResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityFromJobResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("takeActivityFromJobResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityFromJobResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityInfo"));
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
