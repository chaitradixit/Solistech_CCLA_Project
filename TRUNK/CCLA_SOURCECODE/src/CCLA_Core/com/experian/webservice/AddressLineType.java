/**
 * AddressLineType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class AddressLineType  implements java.io.Serializable {
    private java.lang.String label;

    private java.lang.String line;

    private java.lang.String elementCode;

    private com.experian.webservice.LineContentType lineContent;  // attribute

    private boolean overflow;  // attribute

    private boolean truncated;  // attribute

    public AddressLineType() {
    }

    public AddressLineType(
           java.lang.String label,
           java.lang.String line,
           java.lang.String elementCode,
           com.experian.webservice.LineContentType lineContent,
           boolean overflow,
           boolean truncated) {
           this.label = label;
           this.line = line;
           this.elementCode = elementCode;
           this.lineContent = lineContent;
           this.overflow = overflow;
           this.truncated = truncated;
    }


    /**
     * Gets the label value for this AddressLineType.
     * 
     * @return label
     */
    public java.lang.String getLabel() {
        return label;
    }


    /**
     * Sets the label value for this AddressLineType.
     * 
     * @param label
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }


    /**
     * Gets the line value for this AddressLineType.
     * 
     * @return line
     */
    public java.lang.String getLine() {
        return line;
    }


    /**
     * Sets the line value for this AddressLineType.
     * 
     * @param line
     */
    public void setLine(java.lang.String line) {
        this.line = line;
    }


    /**
     * Gets the elementCode value for this AddressLineType.
     * 
     * @return elementCode
     */
    public java.lang.String getElementCode() {
        return elementCode;
    }


    /**
     * Sets the elementCode value for this AddressLineType.
     * 
     * @param elementCode
     */
    public void setElementCode(java.lang.String elementCode) {
        this.elementCode = elementCode;
    }


    /**
     * Gets the lineContent value for this AddressLineType.
     * 
     * @return lineContent
     */
    public com.experian.webservice.LineContentType getLineContent() {
        return lineContent;
    }


    /**
     * Sets the lineContent value for this AddressLineType.
     * 
     * @param lineContent
     */
    public void setLineContent(com.experian.webservice.LineContentType lineContent) {
        this.lineContent = lineContent;
    }


    /**
     * Gets the overflow value for this AddressLineType.
     * 
     * @return overflow
     */
    public boolean isOverflow() {
        return overflow;
    }


    /**
     * Sets the overflow value for this AddressLineType.
     * 
     * @param overflow
     */
    public void setOverflow(boolean overflow) {
        this.overflow = overflow;
    }


    /**
     * Gets the truncated value for this AddressLineType.
     * 
     * @return truncated
     */
    public boolean isTruncated() {
        return truncated;
    }


    /**
     * Sets the truncated value for this AddressLineType.
     * 
     * @param truncated
     */
    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddressLineType)) return false;
        AddressLineType other = (AddressLineType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.label==null && other.getLabel()==null) || 
             (this.label!=null &&
              this.label.equals(other.getLabel()))) &&
            ((this.line==null && other.getLine()==null) || 
             (this.line!=null &&
              this.line.equals(other.getLine()))) &&
            ((this.elementCode==null && other.getElementCode()==null) || 
             (this.elementCode!=null &&
              this.elementCode.equals(other.getElementCode()))) &&
            ((this.lineContent==null && other.getLineContent()==null) || 
             (this.lineContent!=null &&
              this.lineContent.equals(other.getLineContent()))) &&
            this.overflow == other.isOverflow() &&
            this.truncated == other.isTruncated();
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
        if (getLabel() != null) {
            _hashCode += getLabel().hashCode();
        }
        if (getLine() != null) {
            _hashCode += getLine().hashCode();
        }
        if (getElementCode() != null) {
            _hashCode += getElementCode().hashCode();
        }
        if (getLineContent() != null) {
            _hashCode += getLineContent().hashCode();
        }
        _hashCode += (isOverflow() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isTruncated() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AddressLineType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "AddressLineType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("lineContent");
        attrField.setXmlName(new javax.xml.namespace.QName("", "LineContent"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "LineContentType"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("overflow");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Overflow"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("truncated");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Truncated"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("label");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Label"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("line");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Line"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("elementCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "ElementCode"));
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
