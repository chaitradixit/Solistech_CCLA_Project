/**
 * Correspondent.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class Correspondent  implements java.io.Serializable {
    private int correspondentCode;

    private java.lang.String typeCode;

    private java.lang.String name;

    private java.lang.String salutation;

    private com.aurora.webservice.Address address;

    private java.lang.String envelope;

    private int reportUsage;

    private boolean reportAndAccountsMailIndicator;

    private boolean reportAndAccountsEmailIndicator;

    public Correspondent() {
    }

    public Correspondent(
           int correspondentCode,
           java.lang.String typeCode,
           java.lang.String name,
           java.lang.String salutation,
           com.aurora.webservice.Address address,
           java.lang.String envelope,
           int reportUsage,
           boolean reportAndAccountsMailIndicator,
           boolean reportAndAccountsEmailIndicator) {
           this.correspondentCode = correspondentCode;
           this.typeCode = typeCode;
           this.name = name;
           this.salutation = salutation;
           this.address = address;
           this.envelope = envelope;
           this.reportUsage = reportUsage;
           this.reportAndAccountsMailIndicator = reportAndAccountsMailIndicator;
           this.reportAndAccountsEmailIndicator = reportAndAccountsEmailIndicator;
    }


    /**
     * Gets the correspondentCode value for this Correspondent.
     * 
     * @return correspondentCode
     */
    public int getCorrespondentCode() {
        return correspondentCode;
    }


    /**
     * Sets the correspondentCode value for this Correspondent.
     * 
     * @param correspondentCode
     */
    public void setCorrespondentCode(int correspondentCode) {
        this.correspondentCode = correspondentCode;
    }


    /**
     * Gets the typeCode value for this Correspondent.
     * 
     * @return typeCode
     */
    public java.lang.String getTypeCode() {
        return typeCode;
    }


    /**
     * Sets the typeCode value for this Correspondent.
     * 
     * @param typeCode
     */
    public void setTypeCode(java.lang.String typeCode) {
        this.typeCode = typeCode;
    }


    /**
     * Gets the name value for this Correspondent.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Correspondent.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the salutation value for this Correspondent.
     * 
     * @return salutation
     */
    public java.lang.String getSalutation() {
        return salutation;
    }


    /**
     * Sets the salutation value for this Correspondent.
     * 
     * @param salutation
     */
    public void setSalutation(java.lang.String salutation) {
        this.salutation = salutation;
    }


    /**
     * Gets the address value for this Correspondent.
     * 
     * @return address
     */
    public com.aurora.webservice.Address getAddress() {
        return address;
    }


    /**
     * Sets the address value for this Correspondent.
     * 
     * @param address
     */
    public void setAddress(com.aurora.webservice.Address address) {
        this.address = address;
    }


    /**
     * Gets the envelope value for this Correspondent.
     * 
     * @return envelope
     */
    public java.lang.String getEnvelope() {
        return envelope;
    }


    /**
     * Sets the envelope value for this Correspondent.
     * 
     * @param envelope
     */
    public void setEnvelope(java.lang.String envelope) {
        this.envelope = envelope;
    }


    /**
     * Gets the reportUsage value for this Correspondent.
     * 
     * @return reportUsage
     */
    public int getReportUsage() {
        return reportUsage;
    }


    /**
     * Sets the reportUsage value for this Correspondent.
     * 
     * @param reportUsage
     */
    public void setReportUsage(int reportUsage) {
        this.reportUsage = reportUsage;
    }


    /**
     * Gets the reportAndAccountsMailIndicator value for this Correspondent.
     * 
     * @return reportAndAccountsMailIndicator
     */
    public boolean isReportAndAccountsMailIndicator() {
        return reportAndAccountsMailIndicator;
    }


    /**
     * Sets the reportAndAccountsMailIndicator value for this Correspondent.
     * 
     * @param reportAndAccountsMailIndicator
     */
    public void setReportAndAccountsMailIndicator(boolean reportAndAccountsMailIndicator) {
        this.reportAndAccountsMailIndicator = reportAndAccountsMailIndicator;
    }


    /**
     * Gets the reportAndAccountsEmailIndicator value for this Correspondent.
     * 
     * @return reportAndAccountsEmailIndicator
     */
    public boolean isReportAndAccountsEmailIndicator() {
        return reportAndAccountsEmailIndicator;
    }


    /**
     * Sets the reportAndAccountsEmailIndicator value for this Correspondent.
     * 
     * @param reportAndAccountsEmailIndicator
     */
    public void setReportAndAccountsEmailIndicator(boolean reportAndAccountsEmailIndicator) {
        this.reportAndAccountsEmailIndicator = reportAndAccountsEmailIndicator;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Correspondent)) return false;
        Correspondent other = (Correspondent) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.correspondentCode == other.getCorrespondentCode() &&
            ((this.typeCode==null && other.getTypeCode()==null) || 
             (this.typeCode!=null &&
              this.typeCode.equals(other.getTypeCode()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.salutation==null && other.getSalutation()==null) || 
             (this.salutation!=null &&
              this.salutation.equals(other.getSalutation()))) &&
            ((this.address==null && other.getAddress()==null) || 
             (this.address!=null &&
              this.address.equals(other.getAddress()))) &&
            ((this.envelope==null && other.getEnvelope()==null) || 
             (this.envelope!=null &&
              this.envelope.equals(other.getEnvelope()))) &&
            this.reportUsage == other.getReportUsage() &&
            this.reportAndAccountsMailIndicator == other.isReportAndAccountsMailIndicator() &&
            this.reportAndAccountsEmailIndicator == other.isReportAndAccountsEmailIndicator();
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
        _hashCode += getCorrespondentCode();
        if (getTypeCode() != null) {
            _hashCode += getTypeCode().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getSalutation() != null) {
            _hashCode += getSalutation().hashCode();
        }
        if (getAddress() != null) {
            _hashCode += getAddress().hashCode();
        }
        if (getEnvelope() != null) {
            _hashCode += getEnvelope().hashCode();
        }
        _hashCode += getReportUsage();
        _hashCode += (isReportAndAccountsMailIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isReportAndAccountsEmailIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Correspondent.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Correspondent"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correspondentCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "CorrespondentCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "TypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("salutation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Salutation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("address");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Address"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Address"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("envelope");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Envelope"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportUsage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ReportUsage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportAndAccountsMailIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ReportAndAccountsMailIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportAndAccountsEmailIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ReportAndAccountsEmailIndicator"));
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Correspondent [correspondentCode=");
		builder.append(correspondentCode);
		builder.append("\ntypeCode=");
		builder.append(typeCode);
		builder.append("\nname=");
		builder.append(name);
		builder.append("\nsalutation=");
		builder.append(salutation);
		builder.append("\nenvelope=");
		builder.append(envelope);
		builder.append("\nreportUsage=");
		builder.append(reportUsage);
		builder.append("\nreportAndAccountsMailIndicator=");
		builder.append(reportAndAccountsMailIndicator);
		builder.append("\nreportAndAccountsEmailIndicator=");
		builder.append(reportAndAccountsEmailIndicator);
		builder.append("\naddress=");
		builder.append(address);
		builder.append("]");
		return builder.toString();
	}
}
