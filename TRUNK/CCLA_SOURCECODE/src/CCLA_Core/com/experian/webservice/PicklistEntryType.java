/**
 * PicklistEntryType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class PicklistEntryType  implements java.io.Serializable {
    private java.lang.String moniker;

    private java.lang.String partialAddress;

    private java.lang.String picklist;

    private java.lang.String postcode;

    private org.apache.axis.types.NonNegativeInteger score;

    private boolean fullAddress;  // attribute

    private boolean multiples;  // attribute

    private boolean canStep;  // attribute

    private boolean aliasMatch;  // attribute

    private boolean postcodeRecoded;  // attribute

    private boolean crossBorderMatch;  // attribute

    private boolean dummyPOBox;  // attribute

    private boolean name;  // attribute

    private boolean information;  // attribute

    private boolean warnInformation;  // attribute

    private boolean incompleteAddr;  // attribute

    private boolean unresolvableRange;  // attribute

    private boolean phantomPrimaryPoint;  // attribute

    public PicklistEntryType() {
    }

    public PicklistEntryType(
           java.lang.String moniker,
           java.lang.String partialAddress,
           java.lang.String picklist,
           java.lang.String postcode,
           org.apache.axis.types.NonNegativeInteger score,
           boolean fullAddress,
           boolean multiples,
           boolean canStep,
           boolean aliasMatch,
           boolean postcodeRecoded,
           boolean crossBorderMatch,
           boolean dummyPOBox,
           boolean name,
           boolean information,
           boolean warnInformation,
           boolean incompleteAddr,
           boolean unresolvableRange,
           boolean phantomPrimaryPoint) {
           this.moniker = moniker;
           this.partialAddress = partialAddress;
           this.picklist = picklist;
           this.postcode = postcode;
           this.score = score;
           this.fullAddress = fullAddress;
           this.multiples = multiples;
           this.canStep = canStep;
           this.aliasMatch = aliasMatch;
           this.postcodeRecoded = postcodeRecoded;
           this.crossBorderMatch = crossBorderMatch;
           this.dummyPOBox = dummyPOBox;
           this.name = name;
           this.information = information;
           this.warnInformation = warnInformation;
           this.incompleteAddr = incompleteAddr;
           this.unresolvableRange = unresolvableRange;
           this.phantomPrimaryPoint = phantomPrimaryPoint;
    }


    /**
     * Gets the moniker value for this PicklistEntryType.
     * 
     * @return moniker
     */
    public java.lang.String getMoniker() {
        return moniker;
    }


    /**
     * Sets the moniker value for this PicklistEntryType.
     * 
     * @param moniker
     */
    public void setMoniker(java.lang.String moniker) {
        this.moniker = moniker;
    }


    /**
     * Gets the partialAddress value for this PicklistEntryType.
     * 
     * @return partialAddress
     */
    public java.lang.String getPartialAddress() {
        return partialAddress;
    }


    /**
     * Sets the partialAddress value for this PicklistEntryType.
     * 
     * @param partialAddress
     */
    public void setPartialAddress(java.lang.String partialAddress) {
        this.partialAddress = partialAddress;
    }


    /**
     * Gets the picklist value for this PicklistEntryType.
     * 
     * @return picklist
     */
    public java.lang.String getPicklist() {
        return picklist;
    }


    /**
     * Sets the picklist value for this PicklistEntryType.
     * 
     * @param picklist
     */
    public void setPicklist(java.lang.String picklist) {
        this.picklist = picklist;
    }


    /**
     * Gets the postcode value for this PicklistEntryType.
     * 
     * @return postcode
     */
    public java.lang.String getPostcode() {
        return postcode;
    }


    /**
     * Sets the postcode value for this PicklistEntryType.
     * 
     * @param postcode
     */
    public void setPostcode(java.lang.String postcode) {
        this.postcode = postcode;
    }


    /**
     * Gets the score value for this PicklistEntryType.
     * 
     * @return score
     */
    public org.apache.axis.types.NonNegativeInteger getScore() {
        return score;
    }


    /**
     * Sets the score value for this PicklistEntryType.
     * 
     * @param score
     */
    public void setScore(org.apache.axis.types.NonNegativeInteger score) {
        this.score = score;
    }


    /**
     * Gets the fullAddress value for this PicklistEntryType.
     * 
     * @return fullAddress
     */
    public boolean isFullAddress() {
        return fullAddress;
    }


    /**
     * Sets the fullAddress value for this PicklistEntryType.
     * 
     * @param fullAddress
     */
    public void setFullAddress(boolean fullAddress) {
        this.fullAddress = fullAddress;
    }


    /**
     * Gets the multiples value for this PicklistEntryType.
     * 
     * @return multiples
     */
    public boolean isMultiples() {
        return multiples;
    }


    /**
     * Sets the multiples value for this PicklistEntryType.
     * 
     * @param multiples
     */
    public void setMultiples(boolean multiples) {
        this.multiples = multiples;
    }


    /**
     * Gets the canStep value for this PicklistEntryType.
     * 
     * @return canStep
     */
    public boolean isCanStep() {
        return canStep;
    }


    /**
     * Sets the canStep value for this PicklistEntryType.
     * 
     * @param canStep
     */
    public void setCanStep(boolean canStep) {
        this.canStep = canStep;
    }


    /**
     * Gets the aliasMatch value for this PicklistEntryType.
     * 
     * @return aliasMatch
     */
    public boolean isAliasMatch() {
        return aliasMatch;
    }


    /**
     * Sets the aliasMatch value for this PicklistEntryType.
     * 
     * @param aliasMatch
     */
    public void setAliasMatch(boolean aliasMatch) {
        this.aliasMatch = aliasMatch;
    }


    /**
     * Gets the postcodeRecoded value for this PicklistEntryType.
     * 
     * @return postcodeRecoded
     */
    public boolean isPostcodeRecoded() {
        return postcodeRecoded;
    }


    /**
     * Sets the postcodeRecoded value for this PicklistEntryType.
     * 
     * @param postcodeRecoded
     */
    public void setPostcodeRecoded(boolean postcodeRecoded) {
        this.postcodeRecoded = postcodeRecoded;
    }


    /**
     * Gets the crossBorderMatch value for this PicklistEntryType.
     * 
     * @return crossBorderMatch
     */
    public boolean isCrossBorderMatch() {
        return crossBorderMatch;
    }


    /**
     * Sets the crossBorderMatch value for this PicklistEntryType.
     * 
     * @param crossBorderMatch
     */
    public void setCrossBorderMatch(boolean crossBorderMatch) {
        this.crossBorderMatch = crossBorderMatch;
    }


    /**
     * Gets the dummyPOBox value for this PicklistEntryType.
     * 
     * @return dummyPOBox
     */
    public boolean isDummyPOBox() {
        return dummyPOBox;
    }


    /**
     * Sets the dummyPOBox value for this PicklistEntryType.
     * 
     * @param dummyPOBox
     */
    public void setDummyPOBox(boolean dummyPOBox) {
        this.dummyPOBox = dummyPOBox;
    }


    /**
     * Gets the name value for this PicklistEntryType.
     * 
     * @return name
     */
    public boolean isName() {
        return name;
    }


    /**
     * Sets the name value for this PicklistEntryType.
     * 
     * @param name
     */
    public void setName(boolean name) {
        this.name = name;
    }


    /**
     * Gets the information value for this PicklistEntryType.
     * 
     * @return information
     */
    public boolean isInformation() {
        return information;
    }


    /**
     * Sets the information value for this PicklistEntryType.
     * 
     * @param information
     */
    public void setInformation(boolean information) {
        this.information = information;
    }


    /**
     * Gets the warnInformation value for this PicklistEntryType.
     * 
     * @return warnInformation
     */
    public boolean isWarnInformation() {
        return warnInformation;
    }


    /**
     * Sets the warnInformation value for this PicklistEntryType.
     * 
     * @param warnInformation
     */
    public void setWarnInformation(boolean warnInformation) {
        this.warnInformation = warnInformation;
    }


    /**
     * Gets the incompleteAddr value for this PicklistEntryType.
     * 
     * @return incompleteAddr
     */
    public boolean isIncompleteAddr() {
        return incompleteAddr;
    }


    /**
     * Sets the incompleteAddr value for this PicklistEntryType.
     * 
     * @param incompleteAddr
     */
    public void setIncompleteAddr(boolean incompleteAddr) {
        this.incompleteAddr = incompleteAddr;
    }


    /**
     * Gets the unresolvableRange value for this PicklistEntryType.
     * 
     * @return unresolvableRange
     */
    public boolean isUnresolvableRange() {
        return unresolvableRange;
    }


    /**
     * Sets the unresolvableRange value for this PicklistEntryType.
     * 
     * @param unresolvableRange
     */
    public void setUnresolvableRange(boolean unresolvableRange) {
        this.unresolvableRange = unresolvableRange;
    }


    /**
     * Gets the phantomPrimaryPoint value for this PicklistEntryType.
     * 
     * @return phantomPrimaryPoint
     */
    public boolean isPhantomPrimaryPoint() {
        return phantomPrimaryPoint;
    }


    /**
     * Sets the phantomPrimaryPoint value for this PicklistEntryType.
     * 
     * @param phantomPrimaryPoint
     */
    public void setPhantomPrimaryPoint(boolean phantomPrimaryPoint) {
        this.phantomPrimaryPoint = phantomPrimaryPoint;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PicklistEntryType)) return false;
        PicklistEntryType other = (PicklistEntryType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.moniker==null && other.getMoniker()==null) || 
             (this.moniker!=null &&
              this.moniker.equals(other.getMoniker()))) &&
            ((this.partialAddress==null && other.getPartialAddress()==null) || 
             (this.partialAddress!=null &&
              this.partialAddress.equals(other.getPartialAddress()))) &&
            ((this.picklist==null && other.getPicklist()==null) || 
             (this.picklist!=null &&
              this.picklist.equals(other.getPicklist()))) &&
            ((this.postcode==null && other.getPostcode()==null) || 
             (this.postcode!=null &&
              this.postcode.equals(other.getPostcode()))) &&
            ((this.score==null && other.getScore()==null) || 
             (this.score!=null &&
              this.score.equals(other.getScore()))) &&
            this.fullAddress == other.isFullAddress() &&
            this.multiples == other.isMultiples() &&
            this.canStep == other.isCanStep() &&
            this.aliasMatch == other.isAliasMatch() &&
            this.postcodeRecoded == other.isPostcodeRecoded() &&
            this.crossBorderMatch == other.isCrossBorderMatch() &&
            this.dummyPOBox == other.isDummyPOBox() &&
            this.name == other.isName() &&
            this.information == other.isInformation() &&
            this.warnInformation == other.isWarnInformation() &&
            this.incompleteAddr == other.isIncompleteAddr() &&
            this.unresolvableRange == other.isUnresolvableRange() &&
            this.phantomPrimaryPoint == other.isPhantomPrimaryPoint();
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
        if (getMoniker() != null) {
            _hashCode += getMoniker().hashCode();
        }
        if (getPartialAddress() != null) {
            _hashCode += getPartialAddress().hashCode();
        }
        if (getPicklist() != null) {
            _hashCode += getPicklist().hashCode();
        }
        if (getPostcode() != null) {
            _hashCode += getPostcode().hashCode();
        }
        if (getScore() != null) {
            _hashCode += getScore().hashCode();
        }
        _hashCode += (isFullAddress() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMultiples() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCanStep() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isAliasMatch() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPostcodeRecoded() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCrossBorderMatch() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDummyPOBox() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isName() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isInformation() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isWarnInformation() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isIncompleteAddr() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isUnresolvableRange() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isPhantomPrimaryPoint() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PicklistEntryType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PicklistEntryType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("fullAddress");
        attrField.setXmlName(new javax.xml.namespace.QName("", "FullAddress"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("multiples");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Multiples"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("canStep");
        attrField.setXmlName(new javax.xml.namespace.QName("", "CanStep"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("aliasMatch");
        attrField.setXmlName(new javax.xml.namespace.QName("", "AliasMatch"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("postcodeRecoded");
        attrField.setXmlName(new javax.xml.namespace.QName("", "PostcodeRecoded"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("crossBorderMatch");
        attrField.setXmlName(new javax.xml.namespace.QName("", "CrossBorderMatch"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("dummyPOBox");
        attrField.setXmlName(new javax.xml.namespace.QName("", "DummyPOBox"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("information");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Information"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("warnInformation");
        attrField.setXmlName(new javax.xml.namespace.QName("", "WarnInformation"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("incompleteAddr");
        attrField.setXmlName(new javax.xml.namespace.QName("", "IncompleteAddr"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("unresolvableRange");
        attrField.setXmlName(new javax.xml.namespace.QName("", "UnresolvableRange"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("phantomPrimaryPoint");
        attrField.setXmlName(new javax.xml.namespace.QName("", "PhantomPrimaryPoint"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("moniker");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Moniker"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("partialAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PartialAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("picklist");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Picklist"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postcode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Postcode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("score");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Score"));
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
