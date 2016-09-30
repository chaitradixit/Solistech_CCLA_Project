/**
 * FieldFilterInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class FieldFilterInfo  implements java.io.Serializable {
    private java.lang.String name;

    private java.lang.String operator;

    private java.lang.String value;

    private java.lang.String field_Name;

    private java.lang.String field_Operator;

    private java.lang.String field_Value;

    public FieldFilterInfo() {
    }

    public FieldFilterInfo(
           java.lang.String name,
           java.lang.String operator,
           java.lang.String value,
           java.lang.String field_Name,
           java.lang.String field_Operator,
           java.lang.String field_Value) {
           this.name = name;
           this.operator = operator;
           this.value = value;
           this.field_Name = field_Name;
           this.field_Operator = field_Operator;
           this.field_Value = field_Value;
    }


    /**
     * Gets the name value for this FieldFilterInfo.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this FieldFilterInfo.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the operator value for this FieldFilterInfo.
     * 
     * @return operator
     */
    public java.lang.String getOperator() {
        return operator;
    }


    /**
     * Sets the operator value for this FieldFilterInfo.
     * 
     * @param operator
     */
    public void setOperator(java.lang.String operator) {
        this.operator = operator;
    }


    /**
     * Gets the value value for this FieldFilterInfo.
     * 
     * @return value
     */
    public java.lang.String getValue() {
        return value;
    }


    /**
     * Sets the value value for this FieldFilterInfo.
     * 
     * @param value
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }


    /**
     * Gets the field_Name value for this FieldFilterInfo.
     * 
     * @return field_Name
     */
    public java.lang.String getField_Name() {
        return field_Name;
    }


    /**
     * Sets the field_Name value for this FieldFilterInfo.
     * 
     * @param field_Name
     */
    public void setField_Name(java.lang.String field_Name) {
        this.field_Name = field_Name;
    }


    /**
     * Gets the field_Operator value for this FieldFilterInfo.
     * 
     * @return field_Operator
     */
    public java.lang.String getField_Operator() {
        return field_Operator;
    }


    /**
     * Sets the field_Operator value for this FieldFilterInfo.
     * 
     * @param field_Operator
     */
    public void setField_Operator(java.lang.String field_Operator) {
        this.field_Operator = field_Operator;
    }


    /**
     * Gets the field_Value value for this FieldFilterInfo.
     * 
     * @return field_Value
     */
    public java.lang.String getField_Value() {
        return field_Value;
    }


    /**
     * Sets the field_Value value for this FieldFilterInfo.
     * 
     * @param field_Value
     */
    public void setField_Value(java.lang.String field_Value) {
        this.field_Value = field_Value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FieldFilterInfo)) return false;
        FieldFilterInfo other = (FieldFilterInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.operator==null && other.getOperator()==null) || 
             (this.operator!=null &&
              this.operator.equals(other.getOperator()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue()))) &&
            ((this.field_Name==null && other.getField_Name()==null) || 
             (this.field_Name!=null &&
              this.field_Name.equals(other.getField_Name()))) &&
            ((this.field_Operator==null && other.getField_Operator()==null) || 
             (this.field_Operator!=null &&
              this.field_Operator.equals(other.getField_Operator()))) &&
            ((this.field_Value==null && other.getField_Value()==null) || 
             (this.field_Value!=null &&
              this.field_Value.equals(other.getField_Value())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getOperator() != null) {
            _hashCode += getOperator().hashCode();
        }
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        if (getField_Name() != null) {
            _hashCode += getField_Name().hashCode();
        }
        if (getField_Operator() != null) {
            _hashCode += getField_Operator().hashCode();
        }
        if (getField_Value() != null) {
            _hashCode += getField_Value().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FieldFilterInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Operator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_Name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "field_Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_Operator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "field_Operator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field_Value");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "field_Value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
