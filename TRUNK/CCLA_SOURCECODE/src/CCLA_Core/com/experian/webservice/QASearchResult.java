/**
 * QASearchResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class QASearchResult  implements java.io.Serializable {
    private com.experian.webservice.QAPicklistType QAPicklist;

    private com.experian.webservice.AddressLineType[] QAAddress;

    private com.experian.webservice.VerifyLevelType verifyLevel;  // attribute

    public QASearchResult() {
    }

    public QASearchResult(
           com.experian.webservice.QAPicklistType QAPicklist,
           com.experian.webservice.AddressLineType[] QAAddress,
           com.experian.webservice.VerifyLevelType verifyLevel) {
           this.QAPicklist = QAPicklist;
           this.QAAddress = QAAddress;
           this.verifyLevel = verifyLevel;
    }


    /**
     * Gets the QAPicklist value for this QASearchResult.
     * 
     * @return QAPicklist
     */
    public com.experian.webservice.QAPicklistType getQAPicklist() {
        return QAPicklist;
    }


    /**
     * Sets the QAPicklist value for this QASearchResult.
     * 
     * @param QAPicklist
     */
    public void setQAPicklist(com.experian.webservice.QAPicklistType QAPicklist) {
        this.QAPicklist = QAPicklist;
    }


    /**
     * Gets the QAAddress value for this QASearchResult.
     * 
     * @return QAAddress
     */
    public com.experian.webservice.AddressLineType[] getQAAddress() {
        return QAAddress;
    }


    /**
     * Sets the QAAddress value for this QASearchResult.
     * 
     * @param QAAddress
     */
    public void setQAAddress(com.experian.webservice.AddressLineType[] QAAddress) {
        this.QAAddress = QAAddress;
    }


    /**
     * Gets the verifyLevel value for this QASearchResult.
     * 
     * @return verifyLevel
     */
    public com.experian.webservice.VerifyLevelType getVerifyLevel() {
        return verifyLevel;
    }


    /**
     * Sets the verifyLevel value for this QASearchResult.
     * 
     * @param verifyLevel
     */
    public void setVerifyLevel(com.experian.webservice.VerifyLevelType verifyLevel) {
        this.verifyLevel = verifyLevel;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QASearchResult)) return false;
        QASearchResult other = (QASearchResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.QAPicklist==null && other.getQAPicklist()==null) || 
             (this.QAPicklist!=null &&
              this.QAPicklist.equals(other.getQAPicklist()))) &&
            ((this.QAAddress==null && other.getQAAddress()==null) || 
             (this.QAAddress!=null &&
              java.util.Arrays.equals(this.QAAddress, other.getQAAddress()))) &&
            ((this.verifyLevel==null && other.getVerifyLevel()==null) || 
             (this.verifyLevel!=null &&
              this.verifyLevel.equals(other.getVerifyLevel())));
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
        if (getQAPicklist() != null) {
            _hashCode += getQAPicklist().hashCode();
        }
        if (getQAAddress() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getQAAddress());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getQAAddress(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getVerifyLevel() != null) {
            _hashCode += getVerifyLevel().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QASearchResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QASearchResult"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("verifyLevel");
        attrField.setXmlName(new javax.xml.namespace.QName("", "VerifyLevel"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "VerifyLevelType"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("QAPicklist");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAPicklist"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAPicklistType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("QAAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "AddressLineType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "AddressLine"));
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
