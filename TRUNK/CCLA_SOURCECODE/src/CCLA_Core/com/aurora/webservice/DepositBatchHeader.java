/**
 * DepositBatchHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class DepositBatchHeader  extends com.aurora.webservice.BatchHeader  implements java.io.Serializable {
    private int batchNumber;

    private java.lang.String username;

    private java.lang.String tradeDate;

    private java.lang.String dateCreated;

    private java.lang.String dateAuthorised;

    private com.aurora.webservice.DepositTransactionType transactionType;

    private boolean automaticEntryIndicator;

    private com.aurora.webservice.BatchStatus status;

    private boolean batchErrorIndicator;

    private boolean blindErrorIndicator;

    private java.lang.String fundCode;

    private com.aurora.webservice.FundTypeCodes fundTypeCode;

    private java.lang.String destinationCompany;

    private java.lang.String tradeTypeCode;

    private java.lang.String paymentMethodCode;

    private boolean paymentOverrideIndicator;

    private int numberOfItems;

    private double amount;

    private int lastItemNumber;

    private int lastBlindItemNumber;

    public DepositBatchHeader() {
    }

    public DepositBatchHeader(
           int batchNumber,
           java.lang.String username,
           java.lang.String tradeDate,
           java.lang.String dateCreated,
           java.lang.String dateAuthorised,
           com.aurora.webservice.DepositTransactionType transactionType,
           boolean automaticEntryIndicator,
           com.aurora.webservice.BatchStatus status,
           boolean batchErrorIndicator,
           boolean blindErrorIndicator,
           java.lang.String fundCode,
           com.aurora.webservice.FundTypeCodes fundTypeCode,
           java.lang.String destinationCompany,
           java.lang.String tradeTypeCode,
           java.lang.String paymentMethodCode,
           boolean paymentOverrideIndicator,
           int numberOfItems,
           double amount,
           int lastItemNumber,
           int lastBlindItemNumber) {
        this.batchNumber = batchNumber;
        this.username = username;
        this.tradeDate = tradeDate;
        this.dateCreated = dateCreated;
        this.dateAuthorised = dateAuthorised;
        this.transactionType = transactionType;
        this.automaticEntryIndicator = automaticEntryIndicator;
        this.status = status;
        this.batchErrorIndicator = batchErrorIndicator;
        this.blindErrorIndicator = blindErrorIndicator;
        this.fundCode = fundCode;
        this.fundTypeCode = fundTypeCode;
        this.destinationCompany = destinationCompany;
        this.tradeTypeCode = tradeTypeCode;
        this.paymentMethodCode = paymentMethodCode;
        this.paymentOverrideIndicator = paymentOverrideIndicator;
        this.numberOfItems = numberOfItems;
        this.amount = amount;
        this.lastItemNumber = lastItemNumber;
        this.lastBlindItemNumber = lastBlindItemNumber;
    }


    /**
     * Gets the batchNumber value for this DepositBatchHeader.
     * 
     * @return batchNumber
     */
    public int getBatchNumber() {
        return batchNumber;
    }


    /**
     * Sets the batchNumber value for this DepositBatchHeader.
     * 
     * @param batchNumber
     */
    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }


    /**
     * Gets the username value for this DepositBatchHeader.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this DepositBatchHeader.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the tradeDate value for this DepositBatchHeader.
     * 
     * @return tradeDate
     */
    public java.lang.String getTradeDate() {
        return tradeDate;
    }


    /**
     * Sets the tradeDate value for this DepositBatchHeader.
     * 
     * @param tradeDate
     */
    public void setTradeDate(java.lang.String tradeDate) {
        this.tradeDate = tradeDate;
    }


    /**
     * Gets the dateCreated value for this DepositBatchHeader.
     * 
     * @return dateCreated
     */
    public java.lang.String getDateCreated() {
        return dateCreated;
    }


    /**
     * Sets the dateCreated value for this DepositBatchHeader.
     * 
     * @param dateCreated
     */
    public void setDateCreated(java.lang.String dateCreated) {
        this.dateCreated = dateCreated;
    }


    /**
     * Gets the dateAuthorised value for this DepositBatchHeader.
     * 
     * @return dateAuthorised
     */
    public java.lang.String getDateAuthorised() {
        return dateAuthorised;
    }


    /**
     * Sets the dateAuthorised value for this DepositBatchHeader.
     * 
     * @param dateAuthorised
     */
    public void setDateAuthorised(java.lang.String dateAuthorised) {
        this.dateAuthorised = dateAuthorised;
    }


    /**
     * Gets the transactionType value for this DepositBatchHeader.
     * 
     * @return transactionType
     */
    public com.aurora.webservice.DepositTransactionType getTransactionType() {
        return transactionType;
    }


    /**
     * Sets the transactionType value for this DepositBatchHeader.
     * 
     * @param transactionType
     */
    public void setTransactionType(com.aurora.webservice.DepositTransactionType transactionType) {
        this.transactionType = transactionType;
    }


    /**
     * Gets the automaticEntryIndicator value for this DepositBatchHeader.
     * 
     * @return automaticEntryIndicator
     */
    public boolean isAutomaticEntryIndicator() {
        return automaticEntryIndicator;
    }


    /**
     * Sets the automaticEntryIndicator value for this DepositBatchHeader.
     * 
     * @param automaticEntryIndicator
     */
    public void setAutomaticEntryIndicator(boolean automaticEntryIndicator) {
        this.automaticEntryIndicator = automaticEntryIndicator;
    }


    /**
     * Gets the status value for this DepositBatchHeader.
     * 
     * @return status
     */
    public com.aurora.webservice.BatchStatus getStatus() {
        return status;
    }


    /**
     * Sets the status value for this DepositBatchHeader.
     * 
     * @param status
     */
    public void setStatus(com.aurora.webservice.BatchStatus status) {
        this.status = status;
    }


    /**
     * Gets the batchErrorIndicator value for this DepositBatchHeader.
     * 
     * @return batchErrorIndicator
     */
    public boolean isBatchErrorIndicator() {
        return batchErrorIndicator;
    }


    /**
     * Sets the batchErrorIndicator value for this DepositBatchHeader.
     * 
     * @param batchErrorIndicator
     */
    public void setBatchErrorIndicator(boolean batchErrorIndicator) {
        this.batchErrorIndicator = batchErrorIndicator;
    }


    /**
     * Gets the blindErrorIndicator value for this DepositBatchHeader.
     * 
     * @return blindErrorIndicator
     */
    public boolean isBlindErrorIndicator() {
        return blindErrorIndicator;
    }


    /**
     * Sets the blindErrorIndicator value for this DepositBatchHeader.
     * 
     * @param blindErrorIndicator
     */
    public void setBlindErrorIndicator(boolean blindErrorIndicator) {
        this.blindErrorIndicator = blindErrorIndicator;
    }


    /**
     * Gets the fundCode value for this DepositBatchHeader.
     * 
     * @return fundCode
     */
    public java.lang.String getFundCode() {
        return fundCode;
    }


    /**
     * Sets the fundCode value for this DepositBatchHeader.
     * 
     * @param fundCode
     */
    public void setFundCode(java.lang.String fundCode) {
        this.fundCode = fundCode;
    }


    /**
     * Gets the fundTypeCode value for this DepositBatchHeader.
     * 
     * @return fundTypeCode
     */
    public com.aurora.webservice.FundTypeCodes getFundTypeCode() {
        return fundTypeCode;
    }


    /**
     * Sets the fundTypeCode value for this DepositBatchHeader.
     * 
     * @param fundTypeCode
     */
    public void setFundTypeCode(com.aurora.webservice.FundTypeCodes fundTypeCode) {
        this.fundTypeCode = fundTypeCode;
    }


    /**
     * Gets the destinationCompany value for this DepositBatchHeader.
     * 
     * @return destinationCompany
     */
    public java.lang.String getDestinationCompany() {
        return destinationCompany;
    }


    /**
     * Sets the destinationCompany value for this DepositBatchHeader.
     * 
     * @param destinationCompany
     */
    public void setDestinationCompany(java.lang.String destinationCompany) {
        this.destinationCompany = destinationCompany;
    }


    /**
     * Gets the tradeTypeCode value for this DepositBatchHeader.
     * 
     * @return tradeTypeCode
     */
    public java.lang.String getTradeTypeCode() {
        return tradeTypeCode;
    }


    /**
     * Sets the tradeTypeCode value for this DepositBatchHeader.
     * 
     * @param tradeTypeCode
     */
    public void setTradeTypeCode(java.lang.String tradeTypeCode) {
        this.tradeTypeCode = tradeTypeCode;
    }


    /**
     * Gets the paymentMethodCode value for this DepositBatchHeader.
     * 
     * @return paymentMethodCode
     */
    public java.lang.String getPaymentMethodCode() {
        return paymentMethodCode;
    }


    /**
     * Sets the paymentMethodCode value for this DepositBatchHeader.
     * 
     * @param paymentMethodCode
     */
    public void setPaymentMethodCode(java.lang.String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }


    /**
     * Gets the paymentOverrideIndicator value for this DepositBatchHeader.
     * 
     * @return paymentOverrideIndicator
     */
    public boolean isPaymentOverrideIndicator() {
        return paymentOverrideIndicator;
    }


    /**
     * Sets the paymentOverrideIndicator value for this DepositBatchHeader.
     * 
     * @param paymentOverrideIndicator
     */
    public void setPaymentOverrideIndicator(boolean paymentOverrideIndicator) {
        this.paymentOverrideIndicator = paymentOverrideIndicator;
    }


    /**
     * Gets the numberOfItems value for this DepositBatchHeader.
     * 
     * @return numberOfItems
     */
    public int getNumberOfItems() {
        return numberOfItems;
    }


    /**
     * Sets the numberOfItems value for this DepositBatchHeader.
     * 
     * @param numberOfItems
     */
    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }


    /**
     * Gets the amount value for this DepositBatchHeader.
     * 
     * @return amount
     */
    public double getAmount() {
        return amount;
    }


    /**
     * Sets the amount value for this DepositBatchHeader.
     * 
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }


    /**
     * Gets the lastItemNumber value for this DepositBatchHeader.
     * 
     * @return lastItemNumber
     */
    public int getLastItemNumber() {
        return lastItemNumber;
    }


    /**
     * Sets the lastItemNumber value for this DepositBatchHeader.
     * 
     * @param lastItemNumber
     */
    public void setLastItemNumber(int lastItemNumber) {
        this.lastItemNumber = lastItemNumber;
    }


    /**
     * Gets the lastBlindItemNumber value for this DepositBatchHeader.
     * 
     * @return lastBlindItemNumber
     */
    public int getLastBlindItemNumber() {
        return lastBlindItemNumber;
    }


    /**
     * Sets the lastBlindItemNumber value for this DepositBatchHeader.
     * 
     * @param lastBlindItemNumber
     */
    public void setLastBlindItemNumber(int lastBlindItemNumber) {
        this.lastBlindItemNumber = lastBlindItemNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DepositBatchHeader)) return false;
        DepositBatchHeader other = (DepositBatchHeader) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            this.batchNumber == other.getBatchNumber() &&
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            ((this.tradeDate==null && other.getTradeDate()==null) || 
             (this.tradeDate!=null &&
              this.tradeDate.equals(other.getTradeDate()))) &&
            ((this.dateCreated==null && other.getDateCreated()==null) || 
             (this.dateCreated!=null &&
              this.dateCreated.equals(other.getDateCreated()))) &&
            ((this.dateAuthorised==null && other.getDateAuthorised()==null) || 
             (this.dateAuthorised!=null &&
              this.dateAuthorised.equals(other.getDateAuthorised()))) &&
            ((this.transactionType==null && other.getTransactionType()==null) || 
             (this.transactionType!=null &&
              this.transactionType.equals(other.getTransactionType()))) &&
            this.automaticEntryIndicator == other.isAutomaticEntryIndicator() &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            this.batchErrorIndicator == other.isBatchErrorIndicator() &&
            this.blindErrorIndicator == other.isBlindErrorIndicator() &&
            ((this.fundCode==null && other.getFundCode()==null) || 
             (this.fundCode!=null &&
              this.fundCode.equals(other.getFundCode()))) &&
            ((this.fundTypeCode==null && other.getFundTypeCode()==null) || 
             (this.fundTypeCode!=null &&
              this.fundTypeCode.equals(other.getFundTypeCode()))) &&
            ((this.destinationCompany==null && other.getDestinationCompany()==null) || 
             (this.destinationCompany!=null &&
              this.destinationCompany.equals(other.getDestinationCompany()))) &&
            ((this.tradeTypeCode==null && other.getTradeTypeCode()==null) || 
             (this.tradeTypeCode!=null &&
              this.tradeTypeCode.equals(other.getTradeTypeCode()))) &&
            ((this.paymentMethodCode==null && other.getPaymentMethodCode()==null) || 
             (this.paymentMethodCode!=null &&
              this.paymentMethodCode.equals(other.getPaymentMethodCode()))) &&
            this.paymentOverrideIndicator == other.isPaymentOverrideIndicator() &&
            this.numberOfItems == other.getNumberOfItems() &&
            this.amount == other.getAmount() &&
            this.lastItemNumber == other.getLastItemNumber() &&
            this.lastBlindItemNumber == other.getLastBlindItemNumber();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        _hashCode += getBatchNumber();
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getTradeDate() != null) {
            _hashCode += getTradeDate().hashCode();
        }
        if (getDateCreated() != null) {
            _hashCode += getDateCreated().hashCode();
        }
        if (getDateAuthorised() != null) {
            _hashCode += getDateAuthorised().hashCode();
        }
        if (getTransactionType() != null) {
            _hashCode += getTransactionType().hashCode();
        }
        _hashCode += (isAutomaticEntryIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        _hashCode += (isBatchErrorIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isBlindErrorIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getFundCode() != null) {
            _hashCode += getFundCode().hashCode();
        }
        if (getFundTypeCode() != null) {
            _hashCode += getFundTypeCode().hashCode();
        }
        if (getDestinationCompany() != null) {
            _hashCode += getDestinationCompany().hashCode();
        }
        if (getTradeTypeCode() != null) {
            _hashCode += getTradeTypeCode().hashCode();
        }
        if (getPaymentMethodCode() != null) {
            _hashCode += getPaymentMethodCode().hashCode();
        }
        _hashCode += (isPaymentOverrideIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getNumberOfItems();
        _hashCode += new Double(getAmount()).hashCode();
        _hashCode += getLastItemNumber();
        _hashCode += getLastBlindItemNumber();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DepositBatchHeader.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositBatchHeader"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BatchNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tradeDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "TradeDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateCreated");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DateCreated"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateAuthorised");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DateAuthorised"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "TransactionType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositTransactionType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("automaticEntryIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AutomaticEntryIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BatchStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchErrorIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BatchErrorIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("blindErrorIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BlindErrorIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FundCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundTypeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FundTypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FundTypeCodes"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationCompany");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DestinationCompany"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tradeTypeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "TradeTypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentMethodCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PaymentMethodCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentOverrideIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PaymentOverrideIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "NumberOfItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Amount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastItemNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "LastItemNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastBlindItemNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "LastBlindItemNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
