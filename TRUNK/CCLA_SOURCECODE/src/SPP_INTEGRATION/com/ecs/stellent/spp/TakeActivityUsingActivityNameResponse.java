/**
 * TakeActivityUsingActivityNameResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class TakeActivityUsingActivityNameResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.ActivityInfo takeActivityUsingActivityNameResult;

    public TakeActivityUsingActivityNameResponse() {
    }

    public TakeActivityUsingActivityNameResponse(
           com.ecs.stellent.spp.ActivityInfo takeActivityUsingActivityNameResult) {
           this.takeActivityUsingActivityNameResult = takeActivityUsingActivityNameResult;
    }


    /**
     * Gets the takeActivityUsingActivityNameResult value for this TakeActivityUsingActivityNameResponse.
     * 
     * @return takeActivityUsingActivityNameResult
     */
    public com.ecs.stellent.spp.ActivityInfo getTakeActivityUsingActivityNameResult() {
        return takeActivityUsingActivityNameResult;
    }


    /**
     * Sets the takeActivityUsingActivityNameResult value for this TakeActivityUsingActivityNameResponse.
     * 
     * @param takeActivityUsingActivityNameResult
     */
    public void setTakeActivityUsingActivityNameResult(com.ecs.stellent.spp.ActivityInfo takeActivityUsingActivityNameResult) {
        this.takeActivityUsingActivityNameResult = takeActivityUsingActivityNameResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TakeActivityUsingActivityNameResponse)) return false;
        TakeActivityUsingActivityNameResponse other = (TakeActivityUsingActivityNameResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.takeActivityUsingActivityNameResult==null && other.getTakeActivityUsingActivityNameResult()==null) || 
             (this.takeActivityUsingActivityNameResult!=null &&
              this.takeActivityUsingActivityNameResult.equals(other.getTakeActivityUsingActivityNameResult())));
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
        if (getTakeActivityUsingActivityNameResult() != null) {
            _hashCode += getTakeActivityUsingActivityNameResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TakeActivityUsingActivityNameResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityUsingActivityNameResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("takeActivityUsingActivityNameResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityUsingActivityNameResult"));
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
