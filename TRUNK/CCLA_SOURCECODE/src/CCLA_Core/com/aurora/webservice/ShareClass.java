/**
 * ShareClass.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class ShareClass  implements java.io.Serializable {
    private int shareClassCode;

    private java.lang.String shareClassName;

    private java.lang.String fundCode;

    private java.util.Calendar openingDate;

    private double lowerThreshold;

    private double upperThreshold;

    private double cash;

    private double units;

    private double accruedIncome;

    private double balance3;

    private double incForDis;

    private double balance5;

    private double balance6;

    private double balance7;

    private double balance8;

    private double mgtExp;

    private double VATMgtExp;

    private boolean activeIndicator;

    public ShareClass() {
    }

    public ShareClass(
           int shareClassCode,
           java.lang.String shareClassName,
           java.lang.String fundCode,
           java.util.Calendar openingDate,
           double lowerThreshold,
           double upperThreshold,
           double cash,
           double units,
           double accruedIncome,
           double balance3,
           double incForDis,
           double balance5,
           double balance6,
           double balance7,
           double balance8,
           double mgtExp,
           double VATMgtExp,
           boolean activeIndicator) {
           this.shareClassCode = shareClassCode;
           this.shareClassName = shareClassName;
           this.fundCode = fundCode;
           this.openingDate = openingDate;
           this.lowerThreshold = lowerThreshold;
           this.upperThreshold = upperThreshold;
           this.cash = cash;
           this.units = units;
           this.accruedIncome = accruedIncome;
           this.balance3 = balance3;
           this.incForDis = incForDis;
           this.balance5 = balance5;
           this.balance6 = balance6;
           this.balance7 = balance7;
           this.balance8 = balance8;
           this.mgtExp = mgtExp;
           this.VATMgtExp = VATMgtExp;
           this.activeIndicator = activeIndicator;
    }


    /**
     * Gets the shareClassCode value for this ShareClass.
     * 
     * @return shareClassCode
     */
    public int getShareClassCode() {
        return shareClassCode;
    }


    /**
     * Sets the shareClassCode value for this ShareClass.
     * 
     * @param shareClassCode
     */
    public void setShareClassCode(int shareClassCode) {
        this.shareClassCode = shareClassCode;
    }


    /**
     * Gets the shareClassName value for this ShareClass.
     * 
     * @return shareClassName
     */
    public java.lang.String getShareClassName() {
        return shareClassName;
    }


    /**
     * Sets the shareClassName value for this ShareClass.
     * 
     * @param shareClassName
     */
    public void setShareClassName(java.lang.String shareClassName) {
        this.shareClassName = shareClassName;
    }


    /**
     * Gets the fundCode value for this ShareClass.
     * 
     * @return fundCode
     */
    public java.lang.String getFundCode() {
        return fundCode;
    }


    /**
     * Sets the fundCode value for this ShareClass.
     * 
     * @param fundCode
     */
    public void setFundCode(java.lang.String fundCode) {
        this.fundCode = fundCode;
    }


    /**
     * Gets the openingDate value for this ShareClass.
     * 
     * @return openingDate
     */
    public java.util.Calendar getOpeningDate() {
        return openingDate;
    }


    /**
     * Sets the openingDate value for this ShareClass.
     * 
     * @param openingDate
     */
    public void setOpeningDate(java.util.Calendar openingDate) {
        this.openingDate = openingDate;
    }


    /**
     * Gets the lowerThreshold value for this ShareClass.
     * 
     * @return lowerThreshold
     */
    public double getLowerThreshold() {
        return lowerThreshold;
    }


    /**
     * Sets the lowerThreshold value for this ShareClass.
     * 
     * @param lowerThreshold
     */
    public void setLowerThreshold(double lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
    }


    /**
     * Gets the upperThreshold value for this ShareClass.
     * 
     * @return upperThreshold
     */
    public double getUpperThreshold() {
        return upperThreshold;
    }


    /**
     * Sets the upperThreshold value for this ShareClass.
     * 
     * @param upperThreshold
     */
    public void setUpperThreshold(double upperThreshold) {
        this.upperThreshold = upperThreshold;
    }


    /**
     * Gets the cash value for this ShareClass.
     * 
     * @return cash
     */
    public double getCash() {
        return cash;
    }


    /**
     * Sets the cash value for this ShareClass.
     * 
     * @param cash
     */
    public void setCash(double cash) {
        this.cash = cash;
    }


    /**
     * Gets the units value for this ShareClass.
     * 
     * @return units
     */
    public double getUnits() {
        return units;
    }


    /**
     * Sets the units value for this ShareClass.
     * 
     * @param units
     */
    public void setUnits(double units) {
        this.units = units;
    }


    /**
     * Gets the accruedIncome value for this ShareClass.
     * 
     * @return accruedIncome
     */
    public double getAccruedIncome() {
        return accruedIncome;
    }


    /**
     * Sets the accruedIncome value for this ShareClass.
     * 
     * @param accruedIncome
     */
    public void setAccruedIncome(double accruedIncome) {
        this.accruedIncome = accruedIncome;
    }


    /**
     * Gets the balance3 value for this ShareClass.
     * 
     * @return balance3
     */
    public double getBalance3() {
        return balance3;
    }


    /**
     * Sets the balance3 value for this ShareClass.
     * 
     * @param balance3
     */
    public void setBalance3(double balance3) {
        this.balance3 = balance3;
    }


    /**
     * Gets the incForDis value for this ShareClass.
     * 
     * @return incForDis
     */
    public double getIncForDis() {
        return incForDis;
    }


    /**
     * Sets the incForDis value for this ShareClass.
     * 
     * @param incForDis
     */
    public void setIncForDis(double incForDis) {
        this.incForDis = incForDis;
    }


    /**
     * Gets the balance5 value for this ShareClass.
     * 
     * @return balance5
     */
    public double getBalance5() {
        return balance5;
    }


    /**
     * Sets the balance5 value for this ShareClass.
     * 
     * @param balance5
     */
    public void setBalance5(double balance5) {
        this.balance5 = balance5;
    }


    /**
     * Gets the balance6 value for this ShareClass.
     * 
     * @return balance6
     */
    public double getBalance6() {
        return balance6;
    }


    /**
     * Sets the balance6 value for this ShareClass.
     * 
     * @param balance6
     */
    public void setBalance6(double balance6) {
        this.balance6 = balance6;
    }


    /**
     * Gets the balance7 value for this ShareClass.
     * 
     * @return balance7
     */
    public double getBalance7() {
        return balance7;
    }


    /**
     * Sets the balance7 value for this ShareClass.
     * 
     * @param balance7
     */
    public void setBalance7(double balance7) {
        this.balance7 = balance7;
    }


    /**
     * Gets the balance8 value for this ShareClass.
     * 
     * @return balance8
     */
    public double getBalance8() {
        return balance8;
    }


    /**
     * Sets the balance8 value for this ShareClass.
     * 
     * @param balance8
     */
    public void setBalance8(double balance8) {
        this.balance8 = balance8;
    }


    /**
     * Gets the mgtExp value for this ShareClass.
     * 
     * @return mgtExp
     */
    public double getMgtExp() {
        return mgtExp;
    }


    /**
     * Sets the mgtExp value for this ShareClass.
     * 
     * @param mgtExp
     */
    public void setMgtExp(double mgtExp) {
        this.mgtExp = mgtExp;
    }


    /**
     * Gets the VATMgtExp value for this ShareClass.
     * 
     * @return VATMgtExp
     */
    public double getVATMgtExp() {
        return VATMgtExp;
    }


    /**
     * Sets the VATMgtExp value for this ShareClass.
     * 
     * @param VATMgtExp
     */
    public void setVATMgtExp(double VATMgtExp) {
        this.VATMgtExp = VATMgtExp;
    }


    /**
     * Gets the activeIndicator value for this ShareClass.
     * 
     * @return activeIndicator
     */
    public boolean isActiveIndicator() {
        return activeIndicator;
    }


    /**
     * Sets the activeIndicator value for this ShareClass.
     * 
     * @param activeIndicator
     */
    public void setActiveIndicator(boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ShareClass)) return false;
        ShareClass other = (ShareClass) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.shareClassCode == other.getShareClassCode() &&
            ((this.shareClassName==null && other.getShareClassName()==null) || 
             (this.shareClassName!=null &&
              this.shareClassName.equals(other.getShareClassName()))) &&
            ((this.fundCode==null && other.getFundCode()==null) || 
             (this.fundCode!=null &&
              this.fundCode.equals(other.getFundCode()))) &&
            ((this.openingDate==null && other.getOpeningDate()==null) || 
             (this.openingDate!=null &&
              this.openingDate.equals(other.getOpeningDate()))) &&
            this.lowerThreshold == other.getLowerThreshold() &&
            this.upperThreshold == other.getUpperThreshold() &&
            this.cash == other.getCash() &&
            this.units == other.getUnits() &&
            this.accruedIncome == other.getAccruedIncome() &&
            this.balance3 == other.getBalance3() &&
            this.incForDis == other.getIncForDis() &&
            this.balance5 == other.getBalance5() &&
            this.balance6 == other.getBalance6() &&
            this.balance7 == other.getBalance7() &&
            this.balance8 == other.getBalance8() &&
            this.mgtExp == other.getMgtExp() &&
            this.VATMgtExp == other.getVATMgtExp() &&
            this.activeIndicator == other.isActiveIndicator();
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
        _hashCode += getShareClassCode();
        if (getShareClassName() != null) {
            _hashCode += getShareClassName().hashCode();
        }
        if (getFundCode() != null) {
            _hashCode += getFundCode().hashCode();
        }
        if (getOpeningDate() != null) {
            _hashCode += getOpeningDate().hashCode();
        }
        _hashCode += new Double(getLowerThreshold()).hashCode();
        _hashCode += new Double(getUpperThreshold()).hashCode();
        _hashCode += new Double(getCash()).hashCode();
        _hashCode += new Double(getUnits()).hashCode();
        _hashCode += new Double(getAccruedIncome()).hashCode();
        _hashCode += new Double(getBalance3()).hashCode();
        _hashCode += new Double(getIncForDis()).hashCode();
        _hashCode += new Double(getBalance5()).hashCode();
        _hashCode += new Double(getBalance6()).hashCode();
        _hashCode += new Double(getBalance7()).hashCode();
        _hashCode += new Double(getBalance8()).hashCode();
        _hashCode += new Double(getMgtExp()).hashCode();
        _hashCode += new Double(getVATMgtExp()).hashCode();
        _hashCode += (isActiveIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ShareClass.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClass"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shareClassCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClassCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shareClassName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClassName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("openingDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "OpeningDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lowerThreshold");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "LowerThreshold"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("upperThreshold");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "UpperThreshold"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cash");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Cash"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("units");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Units"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("accruedIncome");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AccruedIncome"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Balance3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("incForDis");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "IncForDis"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance5");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Balance5"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance6");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Balance6"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance7");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Balance7"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("balance8");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Balance8"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mgtExp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "MgtExp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VATMgtExp");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "VATMgtExp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activeIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ActiveIndicator"));
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
