/**
 * VariableValueInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class VariableValueInfo  implements java.io.Serializable {
    private java.lang.Object variableValue;

    private boolean isFound;

    public VariableValueInfo() {
    }

    public VariableValueInfo(
           java.lang.Object variableValue,
           boolean isFound) {
           this.variableValue = variableValue;
           this.isFound = isFound;
    }


    /**
     * Gets the variableValue value for this VariableValueInfo.
     * 
     * @return variableValue
     */
    public java.lang.Object getVariableValue() {
        return variableValue;
    }


    /**
     * Sets the variableValue value for this VariableValueInfo.
     * 
     * @param variableValue
     */
    public void setVariableValue(java.lang.Object variableValue) {
        this.variableValue = variableValue;
    }


    /**
     * Gets the isFound value for this VariableValueInfo.
     * 
     * @return isFound
     */
    public boolean isIsFound() {
        return isFound;
    }


    /**
     * Sets the isFound value for this VariableValueInfo.
     * 
     * @param isFound
     */
    public void setIsFound(boolean isFound) {
        this.isFound = isFound;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VariableValueInfo)) return false;
        VariableValueInfo other = (VariableValueInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.variableValue==null && other.getVariableValue()==null) || 
             (this.variableValue!=null &&
              this.variableValue.equals(other.getVariableValue()))) &&
            this.isFound == other.isIsFound();
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
        if (getVariableValue() != null) {
            _hashCode += getVariableValue().hashCode();
        }
        _hashCode += (isIsFound() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VariableValueInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableValueInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("variableValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isFound");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "IsFound"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
