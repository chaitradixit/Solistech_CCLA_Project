/**
 * IncomeDistribution.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class IncomeDistribution  implements java.io.Serializable {
    private java.lang.String fundCode;

    private int shareClass;

    private int distributionNumber;

    private java.util.Calendar periodEndDate;

    private java.lang.String status;

    private java.util.Calendar paymentDate;

    private boolean allowNonStandardPaymentDate;

    private java.util.Calendar reinvestDate;

    private boolean allowNonStandardReinvestmentDate;

    public IncomeDistribution() {
    }

    public IncomeDistribution(
           java.lang.String fundCode,
           int shareClass,
           int distributionNumber,
           java.util.Calendar periodEndDate,
           java.lang.String status,
           java.util.Calendar paymentDate,
           boolean allowNonStandardPaymentDate,
           java.util.Calendar reinvestDate,
           boolean allowNonStandardReinvestmentDate) {
           this.fundCode = fundCode;
           this.shareClass = shareClass;
           this.distributionNumber = distributionNumber;
           this.periodEndDate = periodEndDate;
           this.status = status;
           this.paymentDate = paymentDate;
           this.allowNonStandardPaymentDate = allowNonStandardPaymentDate;
           this.reinvestDate = reinvestDate;
           this.allowNonStandardReinvestmentDate = allowNonStandardReinvestmentDate;
    }


    /**
     * Gets the fundCode value for this IncomeDistribution.
     * 
     * @return fundCode
     */
    public java.lang.String getFundCode() {
        return fundCode;
    }


    /**
     * Sets the fundCode value for this IncomeDistribution.
     * 
     * @param fundCode
     */
    public void setFundCode(java.lang.String fundCode) {
        this.fundCode = fundCode;
    }


    /**
     * Gets the shareClass value for this IncomeDistribution.
     * 
     * @return shareClass
     */
    public int getShareClass() {
        return shareClass;
    }


    /**
     * Sets the shareClass value for this IncomeDistribution.
     * 
     * @param shareClass
     */
    public void setShareClass(int shareClass) {
        this.shareClass = shareClass;
    }


    /**
     * Gets the distributionNumber value for this IncomeDistribution.
     * 
     * @return distributionNumber
     */
    public int getDistributionNumber() {
        return distributionNumber;
    }


    /**
     * Sets the distributionNumber value for this IncomeDistribution.
     * 
     * @param distributionNumber
     */
    public void setDistributionNumber(int distributionNumber) {
        this.distributionNumber = distributionNumber;
    }


    /**
     * Gets the periodEndDate value for this IncomeDistribution.
     * 
     * @return periodEndDate
     */
    public java.util.Calendar getPeriodEndDate() {
        return periodEndDate;
    }


    /**
     * Sets the periodEndDate value for this IncomeDistribution.
     * 
     * @param periodEndDate
     */
    public void setPeriodEndDate(java.util.Calendar periodEndDate) {
        this.periodEndDate = periodEndDate;
    }


    /**
     * Gets the status value for this IncomeDistribution.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this IncomeDistribution.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the paymentDate value for this IncomeDistribution.
     * 
     * @return paymentDate
     */
    public java.util.Calendar getPaymentDate() {
        return paymentDate;
    }


    /**
     * Sets the paymentDate value for this IncomeDistribution.
     * 
     * @param paymentDate
     */
    public void setPaymentDate(java.util.Calendar paymentDate) {
        this.paymentDate = paymentDate;
    }


    /**
     * Gets the allowNonStandardPaymentDate value for this IncomeDistribution.
     * 
     * @return allowNonStandardPaymentDate
     */
    public boolean isAllowNonStandardPaymentDate() {
        return allowNonStandardPaymentDate;
    }


    /**
     * Sets the allowNonStandardPaymentDate value for this IncomeDistribution.
     * 
     * @param allowNonStandardPaymentDate
     */
    public void setAllowNonStandardPaymentDate(boolean allowNonStandardPaymentDate) {
        this.allowNonStandardPaymentDate = allowNonStandardPaymentDate;
    }


    /**
     * Gets the reinvestDate value for this IncomeDistribution.
     * 
     * @return reinvestDate
     */
    public java.util.Calendar getReinvestDate() {
        return reinvestDate;
    }


    /**
     * Sets the reinvestDate value for this IncomeDistribution.
     * 
     * @param reinvestDate
     */
    public void setReinvestDate(java.util.Calendar reinvestDate) {
        this.reinvestDate = reinvestDate;
    }


    /**
     * Gets the allowNonStandardReinvestmentDate value for this IncomeDistribution.
     * 
     * @return allowNonStandardReinvestmentDate
     */
    public boolean isAllowNonStandardReinvestmentDate() {
        return allowNonStandardReinvestmentDate;
    }


    /**
     * Sets the allowNonStandardReinvestmentDate value for this IncomeDistribution.
     * 
     * @param allowNonStandardReinvestmentDate
     */
    public void setAllowNonStandardReinvestmentDate(boolean allowNonStandardReinvestmentDate) {
        this.allowNonStandardReinvestmentDate = allowNonStandardReinvestmentDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IncomeDistribution)) return false;
        IncomeDistribution other = (IncomeDistribution) obj;
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
            this.shareClass == other.getShareClass() &&
            this.distributionNumber == other.getDistributionNumber() &&
            ((this.periodEndDate==null && other.getPeriodEndDate()==null) || 
             (this.periodEndDate!=null &&
              this.periodEndDate.equals(other.getPeriodEndDate()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.paymentDate==null && other.getPaymentDate()==null) || 
             (this.paymentDate!=null &&
              this.paymentDate.equals(other.getPaymentDate()))) &&
            this.allowNonStandardPaymentDate == other.isAllowNonStandardPaymentDate() &&
            ((this.reinvestDate==null && other.getReinvestDate()==null) || 
             (this.reinvestDate!=null &&
              this.reinvestDate.equals(other.getReinvestDate()))) &&
            this.allowNonStandardReinvestmentDate == other.isAllowNonStandardReinvestmentDate();
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
        _hashCode += getShareClass();
        _hashCode += getDistributionNumber();
        if (getPeriodEndDate() != null) {
            _hashCode += getPeriodEndDate().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getPaymentDate() != null) {
            _hashCode += getPaymentDate().hashCode();
        }
        _hashCode += (isAllowNonStandardPaymentDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getReinvestDate() != null) {
            _hashCode += getReinvestDate().hashCode();
        }
        _hashCode += (isAllowNonStandardReinvestmentDate() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IncomeDistribution.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeDistribution"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "FundCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shareClass");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClass"));
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
        elemField.setFieldName("periodEndDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PeriodEndDate"));
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
        elemField.setFieldName("paymentDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PaymentDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allowNonStandardPaymentDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AllowNonStandardPaymentDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reinvestDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ReinvestDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allowNonStandardReinvestmentDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AllowNonStandardReinvestmentDate"));
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
