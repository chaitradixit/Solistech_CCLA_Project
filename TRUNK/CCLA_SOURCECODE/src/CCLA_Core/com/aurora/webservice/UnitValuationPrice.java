/**
 * UnitValuationPrice.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class UnitValuationPrice  implements java.io.Serializable {
    private java.lang.String fundCode;

    private java.util.Calendar valuationPoint;

    private java.lang.String status;

    private java.lang.String userName;

    private java.math.BigDecimal bidPrice;

    private java.math.BigDecimal offerPrice;

    private java.math.BigDecimal basicPrice;

    public UnitValuationPrice() {
    }

    public UnitValuationPrice(
           java.lang.String fundCode,
           java.util.Calendar valuationPoint,
           java.lang.String status,
           java.lang.String userName,
           java.math.BigDecimal bidPrice,
           java.math.BigDecimal offerPrice,
           java.math.BigDecimal basicPrice) {
           this.fundCode = fundCode;
           this.valuationPoint = valuationPoint;
           this.status = status;
           this.userName = userName;
           this.bidPrice = bidPrice;
           this.offerPrice = offerPrice;
           this.basicPrice = basicPrice;
    }


    /**
     * Gets the fundCode value for this UnitValuationPrice.
     * 
     * @return fundCode
     */
    public java.lang.String getFundCode() {
        return fundCode;
    }


    /**
     * Sets the fundCode value for this UnitValuationPrice.
     * 
     * @param fundCode
     */
    public void setFundCode(java.lang.String fundCode) {
        this.fundCode = fundCode;
    }


    /**
     * Gets the valuationPoint value for this UnitValuationPrice.
     * 
     * @return valuationPoint
     */
    public java.util.Calendar getValuationPoint() {
        return valuationPoint;
    }


    /**
     * Sets the valuationPoint value for this UnitValuationPrice.
     * 
     * @param valuationPoint
     */
    public void setValuationPoint(java.util.Calendar valuationPoint) {
        this.valuationPoint = valuationPoint;
    }


    /**
     * Gets the status value for this UnitValuationPrice.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this UnitValuationPrice.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the userName value for this UnitValuationPrice.
     * 
     * @return userName
     */
    public java.lang.String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this UnitValuationPrice.
     * 
     * @param userName
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }


    /**
     * Gets the bidPrice value for this UnitValuationPrice.
     * 
     * @return bidPrice
     */
    public java.math.BigDecimal getBidPrice() {
        return bidPrice;
    }


    /**
     * Sets the bidPrice value for this UnitValuationPrice.
     * 
     * @param bidPrice
     */
    public void setBidPrice(java.math.BigDecimal bidPrice) {
        this.bidPrice = bidPrice;
    }


    /**
     * Gets the offerPrice value for this UnitValuationPrice.
     * 
     * @return offerPrice
     */
    public java.math.BigDecimal getOfferPrice() {
        return offerPrice;
    }


    /**
     * Sets the offerPrice value for this UnitValuationPrice.
     * 
     * @param offerPrice
     */
    public void setOfferPrice(java.math.BigDecimal offerPrice) {
        this.offerPrice = offerPrice;
    }


    /**
     * Gets the basicPrice value for this UnitValuationPrice.
     * 
     * @return basicPrice
     */
    public java.math.BigDecimal getBasicPrice() {
        return basicPrice;
    }


    /**
     * Sets the basicPrice value for this UnitValuationPrice.
     * 
     * @param basicPrice
     */
    public void setBasicPrice(java.math.BigDecimal basicPrice) {
        this.basicPrice = basicPrice;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UnitValuationPrice)) return false;
        UnitValuationPrice other = (UnitValuationPrice) obj;
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
            ((this.valuationPoint==null && other.getValuationPoint()==null) || 
             (this.valuationPoint!=null &&
              this.valuationPoint.equals(other.getValuationPoint()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.userName==null && other.getUserName()==null) || 
             (this.userName!=null &&
              this.userName.equals(other.getUserName()))) &&
            ((this.bidPrice==null && other.getBidPrice()==null) || 
             (this.bidPrice!=null &&
              this.bidPrice.equals(other.getBidPrice()))) &&
            ((this.offerPrice==null && other.getOfferPrice()==null) || 
             (this.offerPrice!=null &&
              this.offerPrice.equals(other.getOfferPrice()))) &&
            ((this.basicPrice==null && other.getBasicPrice()==null) || 
             (this.basicPrice!=null &&
              this.basicPrice.equals(other.getBasicPrice())));
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
        if (getValuationPoint() != null) {
            _hashCode += getValuationPoint().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getUserName() != null) {
            _hashCode += getUserName().hashCode();
        }
        if (getBidPrice() != null) {
            _hashCode += getBidPrice().hashCode();
        }
        if (getOfferPrice() != null) {
            _hashCode += getOfferPrice().hashCode();
        }
        if (getBasicPrice() != null) {
            _hashCode += getBasicPrice().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UnitValuationPrice.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "UnitValuationPrice"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fundCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "fundCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valuationPoint");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "valuationPoint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "userName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bidPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "bidPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("offerPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "offerPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("basicPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "basicPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
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
