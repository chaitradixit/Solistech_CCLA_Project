/**
 * IncomeAccrual.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class IncomeAccrual  implements java.io.Serializable {
    private java.lang.String fundCode;

    private int shareClass;

    private int accrualNumber;

    private java.util.Calendar periodEndDate;

    private java.lang.Double dividendRate;

    private double expensesRate;

    private java.lang.String status;

    public IncomeAccrual() {
    }

    public IncomeAccrual(
           java.lang.String fundCode,
           int shareClass,
           int accrualNumber,
           java.util.Calendar periodEndDate,
           java.lang.Double dividendRate,
           double expensesRate,
           java.lang.String status) {
           this.fundCode = fundCode;
           this.shareClass = shareClass;
           this.accrualNumber = accrualNumber;
           this.periodEndDate = periodEndDate;
           this.dividendRate = dividendRate;
           this.expensesRate = expensesRate;
           this.status = status;
    }


    /**
     * Gets the fundCode value for this IncomeAccrual.
     * 
     * @return fundCode
     */
    public java.lang.String getFundCode() {
        return fundCode;
    }


    /**
     * Sets the fundCode value for this IncomeAccrual.
     * 
     * @param fundCode
     */
    public void setFundCode(java.lang.String fundCode) {
        this.fundCode = fundCode;
    }


    /**
     * Gets the shareClass value for this IncomeAccrual.
     * 
     * @return shareClass
     */
    public int getShareClass() {
        return shareClass;
    }


    /**
     * Sets the shareClass value for this IncomeAccrual.
     * 
     * @param shareClass
     */
    public void setShareClass(int shareClass) {
        this.shareClass = shareClass;
    }


    /**
     * Gets the accrualNumber value for this IncomeAccrual.
     * 
     * @return accrualNumber
     */
    public int getAccrualNumber() {
        return accrualNumber;
    }


    /**
     * Sets the accrualNumber value for this IncomeAccrual.
     * 
     * @param accrualNumber
     */
    public void setAccrualNumber(int accrualNumber) {
        this.accrualNumber = accrualNumber;
    }


    /**
     * Gets the periodEndDate value for this IncomeAccrual.
     * 
     * @return periodEndDate
     */
    public java.util.Calendar getPeriodEndDate() {
        return periodEndDate;
    }


    /**
     * Sets the periodEndDate value for this IncomeAccrual.
     * 
     * @param periodEndDate
     */
    public void setPeriodEndDate(java.util.Calendar periodEndDate) {
        this.periodEndDate = periodEndDate;
    }


    /**
     * Gets the dividendRate value for this IncomeAccrual.
     * 
     * @return dividendRate
     */
    public java.lang.Double getDividendRate() {
        return dividendRate;
    }


    /**
     * Sets the dividendRate value for this IncomeAccrual.
     * 
     * @param dividendRate
     */
    public void setDividendRate(java.lang.Double dividendRate) {
        this.dividendRate = dividendRate;
    }


    /**
     * Gets the expensesRate value for this IncomeAccrual.
     * 
     * @return expensesRate
     */
    public double getExpensesRate() {
        return expensesRate;
    }


    /**
     * Sets the expensesRate value for this IncomeAccrual.
     * 
     * @param expensesRate
     */
    public void setExpensesRate(double expensesRate) {
        this.expensesRate = expensesRate;
    }


    /**
     * Gets the status value for this IncomeAccrual.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this IncomeAccrual.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IncomeAccrual)) return false;
        IncomeAccrual other = (IncomeAccrual) obj;
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
            this.accrualNumber == other.getAccrualNumber() &&
            ((this.periodEndDate==null && other.getPeriodEndDate()==null) || 
             (this.periodEndDate!=null &&
              this.periodEndDate.equals(other.getPeriodEndDate()))) &&
            ((this.dividendRate==null && other.getDividendRate()==null) || 
             (this.dividendRate!=null &&
              this.dividendRate.equals(other.getDividendRate()))) &&
            this.expensesRate == other.getExpensesRate() &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        _hashCode += getAccrualNumber();
        if (getPeriodEndDate() != null) {
            _hashCode += getPeriodEndDate().hashCode();
        }
        if (getDividendRate() != null) {
            _hashCode += getDividendRate().hashCode();
        }
        _hashCode += new Double(getExpensesRate()).hashCode();
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IncomeAccrual.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncomeAccrual"));
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
        elemField.setFieldName("accrualNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccrualNumber"));
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
        elemField.setFieldName("dividendRate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DividendRate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expensesRate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ExpensesRate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Status"));
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
