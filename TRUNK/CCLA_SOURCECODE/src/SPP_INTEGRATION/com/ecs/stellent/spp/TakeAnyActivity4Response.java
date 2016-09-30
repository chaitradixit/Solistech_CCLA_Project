/**
 * TakeAnyActivity4Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class TakeAnyActivity4Response  implements java.io.Serializable {
    private com.ecs.stellent.spp.ActivityInfo takeAnyActivity4Result;

    public TakeAnyActivity4Response() {
    }

    public TakeAnyActivity4Response(
           com.ecs.stellent.spp.ActivityInfo takeAnyActivity4Result) {
           this.takeAnyActivity4Result = takeAnyActivity4Result;
    }


    /**
     * Gets the takeAnyActivity4Result value for this TakeAnyActivity4Response.
     * 
     * @return takeAnyActivity4Result
     */
    public com.ecs.stellent.spp.ActivityInfo getTakeAnyActivity4Result() {
        return takeAnyActivity4Result;
    }


    /**
     * Sets the takeAnyActivity4Result value for this TakeAnyActivity4Response.
     * 
     * @param takeAnyActivity4Result
     */
    public void setTakeAnyActivity4Result(com.ecs.stellent.spp.ActivityInfo takeAnyActivity4Result) {
        this.takeAnyActivity4Result = takeAnyActivity4Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TakeAnyActivity4Response)) return false;
        TakeAnyActivity4Response other = (TakeAnyActivity4Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.takeAnyActivity4Result==null && other.getTakeAnyActivity4Result()==null) || 
             (this.takeAnyActivity4Result!=null &&
              this.takeAnyActivity4Result.equals(other.getTakeAnyActivity4Result())));
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
        if (getTakeAnyActivity4Result() != null) {
            _hashCode += getTakeAnyActivity4Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TakeAnyActivity4Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeAnyActivity4Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("takeAnyActivity4Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeAnyActivity4Result"));
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
