/**
 * TakeActivityUsingActivityNameAndProcessResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class TakeActivityUsingActivityNameAndProcessResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.ActivityInfo takeActivityUsingActivityNameAndProcessResult;

    public TakeActivityUsingActivityNameAndProcessResponse() {
    }

    public TakeActivityUsingActivityNameAndProcessResponse(
           com.ecs.stellent.spp.ActivityInfo takeActivityUsingActivityNameAndProcessResult) {
           this.takeActivityUsingActivityNameAndProcessResult = takeActivityUsingActivityNameAndProcessResult;
    }


    /**
     * Gets the takeActivityUsingActivityNameAndProcessResult value for this TakeActivityUsingActivityNameAndProcessResponse.
     * 
     * @return takeActivityUsingActivityNameAndProcessResult
     */
    public com.ecs.stellent.spp.ActivityInfo getTakeActivityUsingActivityNameAndProcessResult() {
        return takeActivityUsingActivityNameAndProcessResult;
    }


    /**
     * Sets the takeActivityUsingActivityNameAndProcessResult value for this TakeActivityUsingActivityNameAndProcessResponse.
     * 
     * @param takeActivityUsingActivityNameAndProcessResult
     */
    public void setTakeActivityUsingActivityNameAndProcessResult(com.ecs.stellent.spp.ActivityInfo takeActivityUsingActivityNameAndProcessResult) {
        this.takeActivityUsingActivityNameAndProcessResult = takeActivityUsingActivityNameAndProcessResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TakeActivityUsingActivityNameAndProcessResponse)) return false;
        TakeActivityUsingActivityNameAndProcessResponse other = (TakeActivityUsingActivityNameAndProcessResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.takeActivityUsingActivityNameAndProcessResult==null && other.getTakeActivityUsingActivityNameAndProcessResult()==null) || 
             (this.takeActivityUsingActivityNameAndProcessResult!=null &&
              this.takeActivityUsingActivityNameAndProcessResult.equals(other.getTakeActivityUsingActivityNameAndProcessResult())));
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
        if (getTakeActivityUsingActivityNameAndProcessResult() != null) {
            _hashCode += getTakeActivityUsingActivityNameAndProcessResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TakeActivityUsingActivityNameAndProcessResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityUsingActivityNameAndProcessResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("takeActivityUsingActivityNameAndProcessResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityUsingActivityNameAndProcessResult"));
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
