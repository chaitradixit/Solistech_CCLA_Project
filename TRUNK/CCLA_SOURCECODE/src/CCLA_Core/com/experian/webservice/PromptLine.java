/**
 * PromptLine.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class PromptLine  implements java.io.Serializable {
    private java.lang.String prompt;

    private org.apache.axis.types.NonNegativeInteger suggestedInputLength;

    private java.lang.String example;

    private java.lang.String key;

    private org.apache.axis.types.NonNegativeInteger index;

    public PromptLine() {
    }

    public PromptLine(
           java.lang.String prompt,
           org.apache.axis.types.NonNegativeInteger suggestedInputLength,
           java.lang.String example,
           java.lang.String key,
           org.apache.axis.types.NonNegativeInteger index) {
           this.prompt = prompt;
           this.suggestedInputLength = suggestedInputLength;
           this.example = example;
           this.key = key;
           this.index = index;
    }


    /**
     * Gets the prompt value for this PromptLine.
     * 
     * @return prompt
     */
    public java.lang.String getPrompt() {
        return prompt;
    }


    /**
     * Sets the prompt value for this PromptLine.
     * 
     * @param prompt
     */
    public void setPrompt(java.lang.String prompt) {
        this.prompt = prompt;
    }


    /**
     * Gets the suggestedInputLength value for this PromptLine.
     * 
     * @return suggestedInputLength
     */
    public org.apache.axis.types.NonNegativeInteger getSuggestedInputLength() {
        return suggestedInputLength;
    }


    /**
     * Sets the suggestedInputLength value for this PromptLine.
     * 
     * @param suggestedInputLength
     */
    public void setSuggestedInputLength(org.apache.axis.types.NonNegativeInteger suggestedInputLength) {
        this.suggestedInputLength = suggestedInputLength;
    }


    /**
     * Gets the example value for this PromptLine.
     * 
     * @return example
     */
    public java.lang.String getExample() {
        return example;
    }


    /**
     * Sets the example value for this PromptLine.
     * 
     * @param example
     */
    public void setExample(java.lang.String example) {
        this.example = example;
    }


    /**
     * Gets the key value for this PromptLine.
     * 
     * @return key
     */
    public java.lang.String getKey() {
        return key;
    }


    /**
     * Sets the key value for this PromptLine.
     * 
     * @param key
     */
    public void setKey(java.lang.String key) {
        this.key = key;
    }


    /**
     * Gets the index value for this PromptLine.
     * 
     * @return index
     */
    public org.apache.axis.types.NonNegativeInteger getIndex() {
        return index;
    }


    /**
     * Sets the index value for this PromptLine.
     * 
     * @param index
     */
    public void setIndex(org.apache.axis.types.NonNegativeInteger index) {
        this.index = index;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PromptLine)) return false;
        PromptLine other = (PromptLine) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.prompt==null && other.getPrompt()==null) || 
             (this.prompt!=null &&
              this.prompt.equals(other.getPrompt()))) &&
            ((this.suggestedInputLength==null && other.getSuggestedInputLength()==null) || 
             (this.suggestedInputLength!=null &&
              this.suggestedInputLength.equals(other.getSuggestedInputLength()))) &&
            ((this.example==null && other.getExample()==null) || 
             (this.example!=null &&
              this.example.equals(other.getExample()))) &&
            ((this.key==null && other.getKey()==null) || 
             (this.key!=null &&
              this.key.equals(other.getKey()))) &&
            ((this.index==null && other.getIndex()==null) || 
             (this.index!=null &&
              this.index.equals(other.getIndex())));
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
        if (getPrompt() != null) {
            _hashCode += getPrompt().hashCode();
        }
        if (getSuggestedInputLength() != null) {
            _hashCode += getSuggestedInputLength().hashCode();
        }
        if (getExample() != null) {
            _hashCode += getExample().hashCode();
        }
        if (getKey() != null) {
            _hashCode += getKey().hashCode();
        }
        if (getIndex() != null) {
            _hashCode += getIndex().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PromptLine.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PromptLine"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prompt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Prompt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("suggestedInputLength");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "SuggestedInputLength"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "nonNegativeInteger"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("example");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Example"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("index");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Index"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "nonNegativeInteger"));
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
