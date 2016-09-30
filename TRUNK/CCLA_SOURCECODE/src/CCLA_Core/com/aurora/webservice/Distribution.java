/**
 * Distribution.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class Distribution  implements java.io.Serializable {
    private java.lang.String fundCode;

    private int shareClassCode;

    private int distributionNumber;

    private java.util.Calendar periodStartDate;

    private java.util.Calendar periodEndDate;

    private double incomeTaxRate;

    private double VATRate;

    private java.util.Calendar reinvestmentDate;

    private java.util.Calendar paymentDate;

    private java.util.Calendar depositFundTransferDate;

    private java.lang.String status;

    private java.lang.String username;

    private java.util.Calendar unitisedFundTransferDate;

    private double distributionAmount;

    private double taxReliefRate;

    private int paymentHeaderNumber;

    private boolean paymentsExecutedIndicator;

    private boolean retainedExecutedIndicator;

    private boolean transfersExecutedIndicator;

    private boolean reinvestmentsExecutedIndicator;

    private boolean revised;

    public Distribution() {
    }

    public Distribution(
           java.lang.String fundCode,
           int shareClassCode,
           int distributionNumber,
           java.util.Calendar periodStartDate,
           java.util.Calendar periodEndDate,
           double incomeTaxRate,
           double VATRate,
           java.util.Calendar reinvestmentDate,
           java.util.Calendar paymentDate,
           java.util.Calendar depositFundTransferDate,
           java.lang.String status,
           java.lang.String username,
           java.util.Calendar unitisedFundTransferDate,
           double distributionAmount,
           double taxReliefRate,
           int paymentHeaderNumber,
           boolean paymentsExecutedIndicator,
           boolean retainedExecutedIndicator,
           boolean transfersExecutedIndicator,
           boolean reinvestmentsExecutedIndicator,
           boolean revised) {
           this.fundCode = fundCode;
           this.shareClassCode = shareClassCode;
           this.distributionNumber = distributionNumber;
           this.periodStartDate = periodStartDate;
           this.periodEndDate = periodEndDate;
           this.incomeTaxRate = incomeTaxRate;
           this.VATRate = VATRate;
           this.reinvestmentDate = reinvestmentDate;
           this.paymentDate = paymentDate;
           this.depositFundTransferDate = depositFundTransferDate;
           this.status = status;
           this.username = username;
           this.unitisedFundTransferDate = unitisedFundTransferDate;
           this.distributionAmount = distributionAmount;
           this.taxReliefRate = taxReliefRate;
           this.paymentHeaderNumber = paymentHeaderNumber;
           this.paymentsExecutedIndicator = paymentsExecutedIndicator;
           this.retainedExecutedIndicator = retainedExecutedIndicator;
           this.transfersExecutedIndicator = transfersExecutedIndicator;
           this.reinvestmentsExecutedIndicator = reinvestmentsExecutedIndicator;
           this.revised = revised;
    }


    /**
     * Gets the fundCode value for this Distribution.
     * 
     * @return fundCode
     */
    public java.lang.String getFundCode() {
        return fundCode;
    }


    /**
     * Sets the fundCode value for this Distribution.
     * 
     * @param fundCode
     */
    public void setFundCode(java.lang.String fundCode) {
        this.fundCode = fundCode;
    }


    /**
     * Gets the shareClassCode value for this Distribution.
     * 
     * @return shareClassCode
     */
    public int getShareClassCode() {
        return shareClassCode;
    }


    /**
     * Sets the shareClassCode value for this Distribution.
     * 
     * @param shareClassCode
     */
    public void setShareClassCode(int shareClassCode) {
        this.shareClassCode = shareClassCode;
    }


    /**
     * Gets the distributionNumber value for this Distribution.
     * 
     * @return distributionNumber
     */
    public int getDistributionNumber() {
        return distributionNumber;
    }


    /**
     * Sets the distributionNumber value for this Distribution.
     * 
     * @param distributionNumber
     */
    public void setDistributionNumber(int distributionNumber) {
        this.distributionNumber = distributionNumber;
    }


    /**
     * Gets the periodStartDate value for this Distribution.
     * 
     * @return periodStartDate
     */
    public java.util.Calendar getPeriodStartDate() {
        return periodStartDate;
    }


    /**
     * Sets the periodStartDate value for this Distribution.
     * 
     * @param periodStartDate
     */
    public void setPeriodStartDate(java.util.Calendar periodStartDate) {
        this.periodStartDate = periodStartDate;
    }


    /**
     * Gets the periodEndDate value for this Distribution.
     * 
     * @return periodEndDate
     */
    public java.util.Calendar getPeriodEndDate() {
        return periodEndDate;
    }


    /**
     * Sets the periodEndDate value for this Distribution.
     * 
     * @param periodEndDate
     */
    public void setPeriodEndDate(java.util.Calendar periodEndDate) {
        this.periodEndDate = periodEndDate;
    }


    /**
     * Gets the incomeTaxRate value for this Distribution.
     * 
     * @return incomeTaxRate
     */
    public double getIncomeTaxRate() {
        return incomeTaxRate;
    }


    /**
     * Sets the incomeTaxRate value for this Distribution.
     * 
     * @param incomeTaxRate
     */
    public void setIncomeTaxRate(double incomeTaxRate) {
        this.incomeTaxRate = incomeTaxRate;
    }


    /**
     * Gets the VATRate value for this Distribution.
     * 
     * @return VATRate
     */
    public double getVATRate() {
        return VATRate;
    }


    /**
     * Sets the VATRate value for this Distribution.
     * 
     * @param VATRate
     */
    public void setVATRate(double VATRate) {
        this.VATRate = VATRate;
    }


    /**
     * Gets the reinvestmentDate value for this Distribution.
     * 
     * @return reinvestmentDate
     */
    public java.util.Calendar getReinvestmentDate() {
        return reinvestmentDate;
    }


    /**
     * Sets the reinvestmentDate value for this Distribution.
     * 
     * @param reinvestmentDate
     */
    public void setReinvestmentDate(java.util.Calendar reinvestmentDate) {
        this.reinvestmentDate = reinvestmentDate;
    }


    /**
     * Gets the paymentDate value for this Distribution.
     * 
     * @return paymentDate
     */
    public java.util.Calendar getPaymentDate() {
        return paymentDate;
    }


    /**
     * Sets the paymentDate value for this Distribution.
     * 
     * @param paymentDate
     */
    public void setPaymentDate(java.util.Calendar paymentDate) {
        this.paymentDate = paymentDate;
    }


    /**
     * Gets the depositFundTransferDate value for this Distribution.
     * 
     * @return depositFundTransferDate
     */
    public java.util.Calendar getDepositFundTransferDate() {
        return depositFundTransferDate;
    }


    /**
     * Sets the depositFundTransferDate value for this Distribution.
     * 
     * @param depositFundTransferDate
     */
    public void setDepositFundTransferDate(java.util.Calendar depositFundTransferDate) {
        this.depositFundTransferDate = depositFundTransferDate;
    }


    /**
     * Gets the status value for this Distribution.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this Distribution.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the username value for this Distribution.
     * 
     * @return username
     */
    public java.lang.String getUsername() {
        return username;
    }


    /**
     * Sets the username value for this Distribution.
     * 
     * @param username
     */
    public void setUsername(java.lang.String username) {
        this.username = username;
    }


    /**
     * Gets the unitisedFundTransferDate value for this Distribution.
     * 
     * @return unitisedFundTransferDate
     */
    public java.util.Calendar getUnitisedFundTransferDate() {
        return unitisedFundTransferDate;
    }


    /**
     * Sets the unitisedFundTransferDate value for this Distribution.
     * 
     * @param unitisedFundTransferDate
     */
    public void setUnitisedFundTransferDate(java.util.Calendar unitisedFundTransferDate) {
        this.unitisedFundTransferDate = unitisedFundTransferDate;
    }


    /**
     * Gets the distributionAmount value for this Distribution.
     * 
     * @return distributionAmount
     */
    public double getDistributionAmount() {
        return distributionAmount;
    }


    /**
     * Sets the distributionAmount value for this Distribution.
     * 
     * @param distributionAmount
     */
    public void setDistributionAmount(double distributionAmount) {
        this.distributionAmount = distributionAmount;
    }


    /**
     * Gets the taxReliefRate value for this Distribution.
     * 
     * @return taxReliefRate
     */
    public double getTaxReliefRate() {
        return taxReliefRate;
    }


    /**
     * Sets the taxReliefRate value for this Distribution.
     * 
     * @param taxReliefRate
     */
    public void setTaxReliefRate(double taxReliefRate) {
        this.taxReliefRate = taxReliefRate;
    }


    /**
     * Gets the paymentHeaderNumber value for this Distribution.
     * 
     * @return paymentHeaderNumber
     */
    public int getPaymentHeaderNumber() {
        return paymentHeaderNumber;
    }


    /**
     * Sets the paymentHeaderNumber value for this Distribution.
     * 
     * @param paymentHeaderNumber
     */
    public void setPaymentHeaderNumber(int paymentHeaderNumber) {
        this.paymentHeaderNumber = paymentHeaderNumber;
    }


    /**
     * Gets the paymentsExecutedIndicator value for this Distribution.
     * 
     * @return paymentsExecutedIndicator
     */
    public boolean isPaymentsExecutedIndicator() {
        return paymentsExecutedIndicator;
    }


    /**
     * Sets the paymentsExecutedIndicator value for this Distribution.
     * 
     * @param paymentsExecutedIndicator
     */
    public void setPaymentsExecutedIndicator(boolean paymentsExecutedIndicator) {
        this.paymentsExecutedIndicator = paymentsExecutedIndicator;
    }


    /**
     * Gets the retainedExecutedIndicator value for this Distribution.
     * 
     * @return retainedExecutedIndicator
     */
    public boolean isRetainedExecutedIndicator() {
        return retainedExecutedIndicator;
    }


    /**
     * Sets the retainedExecutedIndicator value for this Distribution.
     * 
     * @param retainedExecutedIndicator
     */
    public void setRetainedExecutedIndicator(boolean retainedExecutedIndicator) {
        this.retainedExecutedIndicator = retainedExecutedIndicator;
    }


    /**
     * Gets the transfersExecutedIndicator value for this Distribution.
     * 
     * @return transfersExecutedIndicator
     */
    public boolean isTransfersExecutedIndicator() {
        return transfersExecutedIndicator;
    }


    /**
     * Sets the transfersExecutedIndicator value for this Distribution.
     * 
     * @param transfersExecutedIndicator
     */
    public void setTransfersExecutedIndicator(boolean transfersExecutedIndicator) {
        this.transfersExecutedIndicator = transfersExecutedIndicator;
    }


    /**
     * Gets the reinvestmentsExecutedIndicator value for this Distribution.
     * 
     * @return reinvestmentsExecutedIndicator
     */
    public boolean isReinvestmentsExecutedIndicator() {
        return reinvestmentsExecutedIndicator;
    }


    /**
     * Sets the reinvestmentsExecutedIndicator value for this Distribution.
     * 
     * @param reinvestmentsExecutedIndicator
     */
    public void setReinvestmentsExecutedIndicator(boolean reinvestmentsExecutedIndicator) {
        this.reinvestmentsExecutedIndicator = reinvestmentsExecutedIndicator;
    }


    /**
     * Gets the revised value for this Distribution.
     * 
     * @return revised
     */
    public boolean isRevised() {
        return revised;
    }


    /**
     * Sets the revised value for this Distribution.
     * 
     * @param revised
     */
    public void setRevised(boolean revised) {
        this.revised = revised;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Distribution)) return false;
        Distribution other = (Distribution) obj;
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
            this.shareClassCode == other.getShareClassCode() &&
            this.distributionNumber == other.getDistributionNumber() &&
            ((this.periodStartDate==null && other.getPeriodStartDate()==null) || 
             (this.periodStartDate!=null &&
              this.periodStartDate.equals(other.getPeriodStartDate()))) &&
            ((this.periodEndDate==null && other.getPeriodEndDate()==null) || 
             (this.periodEndDate!=null &&
              this.periodEndDate.equals(other.getPeriodEndDate()))) &&
            this.incomeTaxRate == other.getIncomeTaxRate() &&
            this.VATRate == other.getVATRate() &&
            ((this.reinvestmentDate==null && other.getReinvestmentDate()==null) || 
             (this.reinvestmentDate!=null &&
              this.reinvestmentDate.equals(other.getReinvestmentDate()))) &&
            ((this.paymentDate==null && other.getPaymentDate()==null) || 
             (this.paymentDate!=null &&
              this.paymentDate.equals(other.getPaymentDate()))) &&
            ((this.depositFundTransferDate==null && other.getDepositFundTransferDate()==null) || 
             (this.depositFundTransferDate!=null &&
              this.depositFundTransferDate.equals(other.getDepositFundTransferDate()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.username==null && other.getUsername()==null) || 
             (this.username!=null &&
              this.username.equals(other.getUsername()))) &&
            ((this.unitisedFundTransferDate==null && other.getUnitisedFundTransferDate()==null) || 
             (this.unitisedFundTransferDate!=null &&
              this.unitisedFundTransferDate.equals(other.getUnitisedFundTransferDate()))) &&
            this.distributionAmount == other.getDistributionAmount() &&
            this.taxReliefRate == other.getTaxReliefRate() &&
            this.paymentHeaderNumber == other.getPaymentHeaderNumber() &&
            this.paymentsExecutedIndicator == other.isPaymentsExecutedIndicator() &&
            this.retainedExecutedIndicator == other.isRetainedExecutedIndicator() &&
            this.transfersExecutedIndicator == other.isTransfersExecutedIndicator() &&
            this.reinvestmentsExecutedIndicator == other.isReinvestmentsExecutedIndicator() &&
            this.revised == other.isRevised();
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
        _hashCode += getShareClassCode();
        _hashCode += getDistributionNumber();
        if (getPeriodStartDate() != null) {
            _hashCode += getPeriodStartDate().hashCode();
        }
        if (getPeriodEndDate() != null) {
            _hashCode += getPeriodEndDate().hashCode();
        }
        _hashCode += new Double(getIncomeTaxRate()).hashCode();
        _hashCode += new Double(getVATRate()).hashCode();
        if (getReinvestmentDate() != null) {
            _hashCode += getReinvestmentDate().hashCode();
        }
        if (getPaymentDate() != null) {
            _hashCode += getPaymentDate().hashCode();
        }
        if (getDepositFundTransferDate() != null) {
            _hashCode += getDepositFundTransferDate().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getUsername() != null) {
            _hashCode += getUsername().hashCode();
        }
        if (getUnitisedFundTransferDate() != null) {
            _hashCode += getUnitisedFundTransferDate().hashCode();
        }
        _hashCode += new Double(getDistributionAmount()).hashCode();
        _hashCode += new Double(getTaxReliefRate()).hashCode();
        _hashCode += getPaymentHeaderNumber();
        _hashCode += (isPaymentsExecutedIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRetainedExecutedIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isTransfersExecutedIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isReinvestmentsExecutedIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isRevised() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Distribution.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Distribution"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FundCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shareClassCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClassCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("periodStartDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PeriodStartDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("periodEndDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PeriodEndDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incomeTaxRate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeTaxRate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VATRate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "VATRate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reinvestmentDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ReinvestmentDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PaymentDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("depositFundTransferDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositFundTransferDate"));
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
        elemField.setFieldName("username");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unitisedFundTransferDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "UnitisedFundTransferDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("distributionAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DistributionAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxReliefRate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "TaxReliefRate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentHeaderNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PaymentHeaderNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentsExecutedIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PaymentsExecutedIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retainedExecutedIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "RetainedExecutedIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transfersExecutedIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "TransfersExecutedIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reinvestmentsExecutedIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ReinvestmentsExecutedIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("revised");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Revised"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
