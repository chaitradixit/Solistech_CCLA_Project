/**
 * QARefine.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class QARefine  implements java.io.Serializable {
    private java.lang.String moniker;

    private java.lang.String refinement;

    private com.experian.webservice.QAConfigType QAConfig;

    private org.apache.axis.types.PositiveInteger threshold;  // attribute

    private org.apache.axis.types.NonNegativeInteger timeout;  // attribute

    public QARefine() {
    }

    public QARefine(
           java.lang.String moniker,
           java.lang.String refinement,
           com.experian.webservice.QAConfigType QAConfig,
           org.apache.axis.types.PositiveInteger threshold,
           org.apache.axis.types.NonNegativeInteger timeout) {
           this.moniker = moniker;
           this.refinement = refinement;
           this.QAConfig = QAConfig;
           this.threshold = threshold;
           this.timeout = timeout;
    }


    /**
     * Gets the moniker value for this QARefine.
     * 
     * @return moniker
     */
    public java.lang.String getMoniker() {
        return moniker;
    }


    /**
     * Sets the moniker value for this QARefine.
     * 
     * @param moniker
     */
    public void setMoniker(java.lang.String moniker) {
        this.moniker = moniker;
    }


    /**
     * Gets the refinement value for this QARefine.
     * 
     * @return refinement
     */
    public java.lang.String getRefinement() {
        return refinement;
    }


    /**
     * Sets the refinement value for this QARefine.
     * 
     * @param refinement
     */
    public void setRefinement(java.lang.String refinement) {
        this.refinement = refinement;
    }


    /**
     * Gets the QAConfig value for this QARefine.
     * 
     * @return QAConfig
     */
    public com.experian.webservice.QAConfigType getQAConfig() {
        return QAConfig;
    }


    /**
     * Sets the QAConfig value for this QARefine.
     * 
     * @param QAConfig
     */
    public void setQAConfig(com.experian.webservice.QAConfigType QAConfig) {
        this.QAConfig = QAConfig;
    }


    /**
     * Gets the threshold value for this QARefine.
     * 
     * @return threshold
     */
    public org.apache.axis.types.PositiveInteger getThreshold() {
        return threshold;
    }


    /**
     * Sets the threshold value for this QARefine.
     * 
     * @param threshold
     */
    public void setThreshold(org.apache.axis.types.PositiveInteger threshold) {
        this.threshold = threshold;
    }


    /**
     * Gets the timeout value for this QARefine.
     * 
     * @return timeout
     */
    public org.apache.axis.types.NonNegativeInteger getTimeout() {
        return timeout;
    }


    /**
     * Sets the timeout value for this QARefine.
     * 
     * @param timeout
     */
    public void setTimeout(org.apache.axis.types.NonNegativeInteger timeout) {
        this.timeout = timeout;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QARefine)) return false;
        QARefine other = (QARefine) obj;
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
            ((this.refinement==null && other.getRefinement()==null) || 
             (this.refinement!=null &&
              this.refinement.equals(other.getRefinement()))) &&
            ((this.QAConfig==null && other.getQAConfig()==null) || 
             (this.QAConfig!=null &&
              this.QAConfig.equals(other.getQAConfig()))) &&
            ((this.threshold==null && other.getThreshold()==null) || 
             (this.threshold!=null &&
              this.threshold.equals(other.getThreshold()))) &&
            ((this.timeout==null && other.getTimeout()==null) || 
             (this.timeout!=null &&
              this.timeout.equals(other.getTimeout())));
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
        if (getRefinement() != null) {
            _hashCode += getRefinement().hashCode();
        }
        if (getQAConfig() != null) {
            _hashCode += getQAConfig().hashCode();
        }
        if (getThreshold() != null) {
            _hashCode += getThreshold().hashCode();
        }
        if (getTimeout() != null) {
            _hashCode += getTimeout().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QARefine.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QARefine"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("threshold");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Threshold"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "ThresholdType"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("timeout");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Timeout"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "TimeoutType"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("moniker");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Moniker"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refinement");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Refinement"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("QAConfig");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAConfig"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAConfigType"));
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
