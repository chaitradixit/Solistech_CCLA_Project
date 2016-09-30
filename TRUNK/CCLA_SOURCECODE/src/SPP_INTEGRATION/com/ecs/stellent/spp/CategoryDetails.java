/**
 * CategoryDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CategoryDetails  implements java.io.Serializable {
    private java.lang.String sCategoryId;

    private java.lang.String sCategoryName;

    private java.lang.String sDescription;

    public CategoryDetails() {
    }

    public CategoryDetails(
           java.lang.String sCategoryId,
           java.lang.String sCategoryName,
           java.lang.String sDescription) {
           this.sCategoryId = sCategoryId;
           this.sCategoryName = sCategoryName;
           this.sDescription = sDescription;
    }


    /**
     * Gets the sCategoryId value for this CategoryDetails.
     * 
     * @return sCategoryId
     */
    public java.lang.String getSCategoryId() {
        return sCategoryId;
    }


    /**
     * Sets the sCategoryId value for this CategoryDetails.
     * 
     * @param sCategoryId
     */
    public void setSCategoryId(java.lang.String sCategoryId) {
        this.sCategoryId = sCategoryId;
    }


    /**
     * Gets the sCategoryName value for this CategoryDetails.
     * 
     * @return sCategoryName
     */
    public java.lang.String getSCategoryName() {
        return sCategoryName;
    }


    /**
     * Sets the sCategoryName value for this CategoryDetails.
     * 
     * @param sCategoryName
     */
    public void setSCategoryName(java.lang.String sCategoryName) {
        this.sCategoryName = sCategoryName;
    }


    /**
     * Gets the sDescription value for this CategoryDetails.
     * 
     * @return sDescription
     */
    public java.lang.String getSDescription() {
        return sDescription;
    }


    /**
     * Sets the sDescription value for this CategoryDetails.
     * 
     * @param sDescription
     */
    public void setSDescription(java.lang.String sDescription) {
        this.sDescription = sDescription;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CategoryDetails)) return false;
        CategoryDetails other = (CategoryDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sCategoryId==null && other.getSCategoryId()==null) || 
             (this.sCategoryId!=null &&
              this.sCategoryId.equals(other.getSCategoryId()))) &&
            ((this.sCategoryName==null && other.getSCategoryName()==null) || 
             (this.sCategoryName!=null &&
              this.sCategoryName.equals(other.getSCategoryName()))) &&
            ((this.sDescription==null && other.getSDescription()==null) || 
             (this.sDescription!=null &&
              this.sDescription.equals(other.getSDescription())));
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
        if (getSCategoryId() != null) {
            _hashCode += getSCategoryId().hashCode();
        }
        if (getSCategoryName() != null) {
            _hashCode += getSCategoryName().hashCode();
        }
        if (getSDescription() != null) {
            _hashCode += getSDescription().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CategoryDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SCategoryId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "sCategoryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SCategoryName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "sCategoryName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SDescription");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "sDescription"));
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
