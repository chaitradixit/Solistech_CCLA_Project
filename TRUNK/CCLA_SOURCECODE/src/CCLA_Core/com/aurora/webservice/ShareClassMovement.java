/**
 * ShareClassMovement.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class ShareClassMovement  implements java.io.Serializable {
    private java.lang.String fundCode;

    private java.lang.String accountNumberExternal;

    private int shareClassCodeSource;

    private int shareClassCodeDestination;

    private java.util.Calendar movementRequestDate;

    private java.lang.String status;

    private java.lang.String userName;

    public ShareClassMovement() {
    }

    public ShareClassMovement(
           java.lang.String fundCode,
           java.lang.String accountNumberExternal,
           int shareClassCodeSource,
           int shareClassCodeDestination,
           java.util.Calendar movementRequestDate,
           java.lang.String status,
           java.lang.String userName) {
           this.fundCode = fundCode;
           this.accountNumberExternal = accountNumberExternal;
           this.shareClassCodeSource = shareClassCodeSource;
           this.shareClassCodeDestination = shareClassCodeDestination;
           this.movementRequestDate = movementRequestDate;
           this.status = status;
           this.userName = userName;
    }


    /**
     * Gets the fundCode value for this ShareClassMovement.
     * 
     * @return fundCode
     */
    public java.lang.String getFundCode() {
        return fundCode;
    }


    /**
     * Sets the fundCode value for this ShareClassMovement.
     * 
     * @param fundCode
     */
    public void setFundCode(java.lang.String fundCode) {
        this.fundCode = fundCode;
    }


    /**
     * Gets the accountNumberExternal value for this ShareClassMovement.
     * 
     * @return accountNumberExternal
     */
    public java.lang.String getAccountNumberExternal() {
        return accountNumberExternal;
    }


    /**
     * Sets the accountNumberExternal value for this ShareClassMovement.
     * 
     * @param accountNumberExternal
     */
    public void setAccountNumberExternal(java.lang.String accountNumberExternal) {
        this.accountNumberExternal = accountNumberExternal;
    }


    /**
     * Gets the shareClassCodeSource value for this ShareClassMovement.
     * 
     * @return shareClassCodeSource
     */
    public int getShareClassCodeSource() {
        return shareClassCodeSource;
    }


    /**
     * Sets the shareClassCodeSource value for this ShareClassMovement.
     * 
     * @param shareClassCodeSource
     */
    public void setShareClassCodeSource(int shareClassCodeSource) {
        this.shareClassCodeSource = shareClassCodeSource;
    }


    /**
     * Gets the shareClassCodeDestination value for this ShareClassMovement.
     * 
     * @return shareClassCodeDestination
     */
    public int getShareClassCodeDestination() {
        return shareClassCodeDestination;
    }


    /**
     * Sets the shareClassCodeDestination value for this ShareClassMovement.
     * 
     * @param shareClassCodeDestination
     */
    public void setShareClassCodeDestination(int shareClassCodeDestination) {
        this.shareClassCodeDestination = shareClassCodeDestination;
    }


    /**
     * Gets the movementRequestDate value for this ShareClassMovement.
     * 
     * @return movementRequestDate
     */
    public java.util.Calendar getMovementRequestDate() {
        return movementRequestDate;
    }


    /**
     * Sets the movementRequestDate value for this ShareClassMovement.
     * 
     * @param movementRequestDate
     */
    public void setMovementRequestDate(java.util.Calendar movementRequestDate) {
        this.movementRequestDate = movementRequestDate;
    }


    /**
     * Gets the status value for this ShareClassMovement.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this ShareClassMovement.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the userName value for this ShareClassMovement.
     * 
     * @return userName
     */
    public java.lang.String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this ShareClassMovement.
     * 
     * @param userName
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ShareClassMovement)) return false;
        ShareClassMovement other = (ShareClassMovement) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fundCode==null && other.getFundCode()==null) || 
             (this.fundCode!=null &&
              this.fundCode.equals(other.getFundCode()))) &&
            ((this.accountNumberExternal==null && other.getAccountNumberExternal()==null) || 
             (this.accountNumberExternal!=null &&
              this.accountNumberExternal.equals(other.getAccountNumberExternal()))) &&
            this.shareClassCodeSource == other.getShareClassCodeSource() &&
            this.shareClassCodeDestination == other.getShareClassCodeDestination() &&
            ((this.movementRequestDate==null && other.getMovementRequestDate()==null) || 
             (this.movementRequestDate!=null &&
              this.movementRequestDate.equals(other.getMovementRequestDate()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.userName==null && other.getUserName()==null) || 
             (this.userName!=null &&
              this.userName.equals(other.getUserName())));
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
        if (getFundCode() != null) {
            _hashCode += getFundCode().hashCode();
        }
        if (getAccountNumberExternal() != null) {
            _hashCode += getAccountNumberExternal().hashCode();
        }
        _hashCode += getShareClassCodeSource();
        _hashCode += getShareClassCodeDestination();
        if (getMovementRequestDate() != null) {
            _hashCode += getMovementRequestDate().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getUserName() != null) {
            _hashCode += getUserName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ShareClassMovement.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClassMovement"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FundCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accountNumberExternal");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccountNumberExternal"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shareClassCodeSource");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClassCodeSource"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shareClassCodeDestination");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClassCodeDestination"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("movementRequestDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MovementRequestDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "UserName"));
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
