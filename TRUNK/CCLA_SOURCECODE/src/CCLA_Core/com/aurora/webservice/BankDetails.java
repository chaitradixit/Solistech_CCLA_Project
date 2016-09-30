/**
 * BankDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class BankDetails  implements java.io.Serializable {
    private java.lang.String bankName;

    private java.lang.String branchTitle;

    private java.lang.String branchAddress;

    public BankDetails() {
    }

    public BankDetails(
           java.lang.String bankName,
           java.lang.String branchTitle,
           java.lang.String branchAddress) {
           this.bankName = bankName;
           this.branchTitle = branchTitle;
           this.branchAddress = branchAddress;
    }


    /**
     * Gets the bankName value for this BankDetails.
     * 
     * @return bankName
     */
    public java.lang.String getBankName() {
        return bankName;
    }


    /**
     * Sets the bankName value for this BankDetails.
     * 
     * @param bankName
     */
    public void setBankName(java.lang.String bankName) {
        this.bankName = bankName;
    }


    /**
     * Gets the branchTitle value for this BankDetails.
     * 
     * @return branchTitle
     */
    public java.lang.String getBranchTitle() {
        return branchTitle;
    }


    /**
     * Sets the branchTitle value for this BankDetails.
     * 
     * @param branchTitle
     */
    public void setBranchTitle(java.lang.String branchTitle) {
        this.branchTitle = branchTitle;
    }


    /**
     * Gets the branchAddress value for this BankDetails.
     * 
     * @return branchAddress
     */
    public java.lang.String getBranchAddress() {
        return branchAddress;
    }


    /**
     * Sets the branchAddress value for this BankDetails.
     * 
     * @param branchAddress
     */
    public void setBranchAddress(java.lang.String branchAddress) {
        this.branchAddress = branchAddress;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BankDetails)) return false;
        BankDetails other = (BankDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.bankName==null && other.getBankName()==null) || 
             (this.bankName!=null &&
              this.bankName.equals(other.getBankName()))) &&
            ((this.branchTitle==null && other.getBranchTitle()==null) || 
             (this.branchTitle!=null &&
              this.branchTitle.equals(other.getBranchTitle()))) &&
            ((this.branchAddress==null && other.getBranchAddress()==null) || 
             (this.branchAddress!=null &&
              this.branchAddress.equals(other.getBranchAddress())));
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
        if (getBankName() != null) {
            _hashCode += getBankName().hashCode();
        }
        if (getBranchTitle() != null) {
            _hashCode += getBranchTitle().hashCode();
        }
        if (getBranchAddress() != null) {
            _hashCode += getBranchAddress().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BankDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("branchTitle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BranchTitle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("branchAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BranchAddress"));
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
