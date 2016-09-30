/**
 * QAPicklistType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class QAPicklistType  implements java.io.Serializable {
    private java.lang.String fullPicklistMoniker;

    private java.lang.String refinementText;

    private java.lang.String displayText;

    private com.experian.webservice.PicklistEntryType[] picklistEntry;

    private java.lang.String prompt;

    private org.apache.axis.types.NonNegativeInteger total;

    private boolean autoFormatSafe;  // attribute

    private boolean autoFormatPastClose;  // attribute

    private boolean autoStepinSafe;  // attribute

    private boolean autoStepinPastClose;  // attribute

    private boolean largePotential;  // attribute

    private boolean maxMatches;  // attribute

    private boolean moreOtherMatches;  // attribute

    private boolean overThreshold;  // attribute

    private boolean timeout;  // attribute

    public QAPicklistType() {
    }

    public QAPicklistType(
           java.lang.String fullPicklistMoniker,
           java.lang.String refinementText,
           java.lang.String displayText,
           com.experian.webservice.PicklistEntryType[] picklistEntry,
           java.lang.String prompt,
           org.apache.axis.types.NonNegativeInteger total,
           boolean autoFormatSafe,
           boolean autoFormatPastClose,
           boolean autoStepinSafe,
           boolean autoStepinPastClose,
           boolean largePotential,
           boolean maxMatches,
           boolean moreOtherMatches,
           boolean overThreshold,
           boolean timeout) {
           this.fullPicklistMoniker = fullPicklistMoniker;
           this.refinementText = refinementText;
           this.displayText = displayText;
           this.picklistEntry = picklistEntry;
           this.prompt = prompt;
           this.total = total;
           this.autoFormatSafe = autoFormatSafe;
           this.autoFormatPastClose = autoFormatPastClose;
           this.autoStepinSafe = autoStepinSafe;
           this.autoStepinPastClose = autoStepinPastClose;
           this.largePotential = largePotential;
           this.maxMatches = maxMatches;
           this.moreOtherMatches = moreOtherMatches;
           this.overThreshold = overThreshold;
           this.timeout = timeout;
    }


    /**
     * Gets the fullPicklistMoniker value for this QAPicklistType.
     * 
     * @return fullPicklistMoniker
     */
    public java.lang.String getFullPicklistMoniker() {
        return fullPicklistMoniker;
    }


    /**
     * Sets the fullPicklistMoniker value for this QAPicklistType.
     * 
     * @param fullPicklistMoniker
     */
    public void setFullPicklistMoniker(java.lang.String fullPicklistMoniker) {
        this.fullPicklistMoniker = fullPicklistMoniker;
    }


    /**
     * Gets the refinementText value for this QAPicklistType.
     * 
     * @return refinementText
     */
    public java.lang.String getRefinementText() {
        return refinementText;
    }


    /**
     * Sets the refinementText value for this QAPicklistType.
     * 
     * @param refinementText
     */
    public void setRefinementText(java.lang.String refinementText) {
        this.refinementText = refinementText;
    }


    /**
     * Gets the displayText value for this QAPicklistType.
     * 
     * @return displayText
     */
    public java.lang.String getDisplayText() {
        return displayText;
    }


    /**
     * Sets the displayText value for this QAPicklistType.
     * 
     * @param displayText
     */
    public void setDisplayText(java.lang.String displayText) {
        this.displayText = displayText;
    }


    /**
     * Gets the picklistEntry value for this QAPicklistType.
     * 
     * @return picklistEntry
     */
    public com.experian.webservice.PicklistEntryType[] getPicklistEntry() {
        return picklistEntry;
    }


    /**
     * Sets the picklistEntry value for this QAPicklistType.
     * 
     * @param picklistEntry
     */
    public void setPicklistEntry(com.experian.webservice.PicklistEntryType[] picklistEntry) {
        this.picklistEntry = picklistEntry;
    }

    public com.experian.webservice.PicklistEntryType getPicklistEntry(int i) {
        return this.picklistEntry[i];
    }

    public void setPicklistEntry(int i, com.experian.webservice.PicklistEntryType _value) {
        this.picklistEntry[i] = _value;
    }


    /**
     * Gets the prompt value for this QAPicklistType.
     * 
     * @return prompt
     */
    public java.lang.String getPrompt() {
        return prompt;
    }


    /**
     * Sets the prompt value for this QAPicklistType.
     * 
     * @param prompt
     */
    public void setPrompt(java.lang.String prompt) {
        this.prompt = prompt;
    }


    /**
     * Gets the total value for this QAPicklistType.
     * 
     * @return total
     */
    public org.apache.axis.types.NonNegativeInteger getTotal() {
        return total;
    }


    /**
     * Sets the total value for this QAPicklistType.
     * 
     * @param total
     */
    public void setTotal(org.apache.axis.types.NonNegativeInteger total) {
        this.total = total;
    }


    /**
     * Gets the autoFormatSafe value for this QAPicklistType.
     * 
     * @return autoFormatSafe
     */
    public boolean isAutoFormatSafe() {
        return autoFormatSafe;
    }


    /**
     * Sets the autoFormatSafe value for this QAPicklistType.
     * 
     * @param autoFormatSafe
     */
    public void setAutoFormatSafe(boolean autoFormatSafe) {
        this.autoFormatSafe = autoFormatSafe;
    }


    /**
     * Gets the autoFormatPastClose value for this QAPicklistType.
     * 
     * @return autoFormatPastClose
     */
    public boolean isAutoFormatPastClose() {
        return autoFormatPastClose;
    }


    /**
     * Sets the autoFormatPastClose value for this QAPicklistType.
     * 
     * @param autoFormatPastClose
     */
    public void setAutoFormatPastClose(boolean autoFormatPastClose) {
        this.autoFormatPastClose = autoFormatPastClose;
    }


    /**
     * Gets the autoStepinSafe value for this QAPicklistType.
     * 
     * @return autoStepinSafe
     */
    public boolean isAutoStepinSafe() {
        return autoStepinSafe;
    }


    /**
     * Sets the autoStepinSafe value for this QAPicklistType.
     * 
     * @param autoStepinSafe
     */
    public void setAutoStepinSafe(boolean autoStepinSafe) {
        this.autoStepinSafe = autoStepinSafe;
    }


    /**
     * Gets the autoStepinPastClose value for this QAPicklistType.
     * 
     * @return autoStepinPastClose
     */
    public boolean isAutoStepinPastClose() {
        return autoStepinPastClose;
    }


    /**
     * Sets the autoStepinPastClose value for this QAPicklistType.
     * 
     * @param autoStepinPastClose
     */
    public void setAutoStepinPastClose(boolean autoStepinPastClose) {
        this.autoStepinPastClose = autoStepinPastClose;
    }


    /**
     * Gets the largePotential value for this QAPicklistType.
     * 
     * @return largePotential
     */
    public boolean isLargePotential() {
        return largePotential;
    }


    /**
     * Sets the largePotential value for this QAPicklistType.
     * 
     * @param largePotential
     */
    public void setLargePotential(boolean largePotential) {
        this.largePotential = largePotential;
    }


    /**
     * Gets the maxMatches value for this QAPicklistType.
     * 
     * @return maxMatches
     */
    public boolean isMaxMatches() {
        return maxMatches;
    }


    /**
     * Sets the maxMatches value for this QAPicklistType.
     * 
     * @param maxMatches
     */
    public void setMaxMatches(boolean maxMatches) {
        this.maxMatches = maxMatches;
    }


    /**
     * Gets the moreOtherMatches value for this QAPicklistType.
     * 
     * @return moreOtherMatches
     */
    public boolean isMoreOtherMatches() {
        return moreOtherMatches;
    }


    /**
     * Sets the moreOtherMatches value for this QAPicklistType.
     * 
     * @param moreOtherMatches
     */
    public void setMoreOtherMatches(boolean moreOtherMatches) {
        this.moreOtherMatches = moreOtherMatches;
    }


    /**
     * Gets the overThreshold value for this QAPicklistType.
     * 
     * @return overThreshold
     */
    public boolean isOverThreshold() {
        return overThreshold;
    }


    /**
     * Sets the overThreshold value for this QAPicklistType.
     * 
     * @param overThreshold
     */
    public void setOverThreshold(boolean overThreshold) {
        this.overThreshold = overThreshold;
    }


    /**
     * Gets the timeout value for this QAPicklistType.
     * 
     * @return timeout
     */
    public boolean isTimeout() {
        return timeout;
    }


    /**
     * Sets the timeout value for this QAPicklistType.
     * 
     * @param timeout
     */
    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QAPicklistType)) return false;
        QAPicklistType other = (QAPicklistType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fullPicklistMoniker==null && other.getFullPicklistMoniker()==null) || 
             (this.fullPicklistMoniker!=null &&
              this.fullPicklistMoniker.equals(other.getFullPicklistMoniker()))) &&
            ((this.refinementText==null && other.getRefinementText()==null) || 
             (this.refinementText!=null &&
              this.refinementText.equals(other.getRefinementText()))) &&
            ((this.displayText==null && other.getDisplayText()==null) || 
             (this.displayText!=null &&
              this.displayText.equals(other.getDisplayText()))) &&
            ((this.picklistEntry==null && other.getPicklistEntry()==null) || 
             (this.picklistEntry!=null &&
              java.util.Arrays.equals(this.picklistEntry, other.getPicklistEntry()))) &&
            ((this.prompt==null && other.getPrompt()==null) || 
             (this.prompt!=null &&
              this.prompt.equals(other.getPrompt()))) &&
            ((this.total==null && other.getTotal()==null) || 
             (this.total!=null &&
              this.total.equals(other.getTotal()))) &&
            this.autoFormatSafe == other.isAutoFormatSafe() &&
            this.autoFormatPastClose == other.isAutoFormatPastClose() &&
            this.autoStepinSafe == other.isAutoStepinSafe() &&
            this.autoStepinPastClose == other.isAutoStepinPastClose() &&
            this.largePotential == other.isLargePotential() &&
            this.maxMatches == other.isMaxMatches() &&
            this.moreOtherMatches == other.isMoreOtherMatches() &&
            this.overThreshold == other.isOverThreshold() &&
            this.timeout == other.isTimeout();
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
        if (getFullPicklistMoniker() != null) {
            _hashCode += getFullPicklistMoniker().hashCode();
        }
        if (getRefinementText() != null) {
            _hashCode += getRefinementText().hashCode();
        }
        if (getDisplayText() != null) {
            _hashCode += getDisplayText().hashCode();
        }
        if (getPicklistEntry() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPicklistEntry());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPicklistEntry(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getPrompt() != null) {
            _hashCode += getPrompt().hashCode();
        }
        if (getTotal() != null) {
            _hashCode += getTotal().hashCode();
        }
        _hashCode += (isAutoFormatSafe() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAutoFormatPastClose() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAutoStepinSafe() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAutoStepinPastClose() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isLargePotential() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMaxMatches() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMoreOtherMatches() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isOverThreshold() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isTimeout() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QAPicklistType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAPicklistType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("autoFormatSafe");
        attrField.setXmlName(new javax.xml.namespace.QName("", "AutoFormatSafe"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("autoFormatPastClose");
        attrField.setXmlName(new javax.xml.namespace.QName("", "AutoFormatPastClose"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("autoStepinSafe");
        attrField.setXmlName(new javax.xml.namespace.QName("", "AutoStepinSafe"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("autoStepinPastClose");
        attrField.setXmlName(new javax.xml.namespace.QName("", "AutoStepinPastClose"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("largePotential");
        attrField.setXmlName(new javax.xml.namespace.QName("", "LargePotential"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("maxMatches");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MaxMatches"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("moreOtherMatches");
        attrField.setXmlName(new javax.xml.namespace.QName("", "MoreOtherMatches"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("overThreshold");
        attrField.setXmlName(new javax.xml.namespace.QName("", "OverThreshold"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("timeout");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Timeout"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fullPicklistMoniker");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "FullPicklistMoniker"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refinementText");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "RefinementText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayText");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "DisplayText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("picklistEntry");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PicklistEntry"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PicklistEntryType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("prompt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Prompt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("total");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Total"));
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
