/**
 * QALicensedSet.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class QALicensedSet  implements java.io.Serializable {
    private java.lang.String ID;

    private java.lang.String description;

    private java.lang.String copyright;

    private java.lang.String version;

    private java.lang.String baseCountry;

    private java.lang.String status;

    private java.lang.String server;

    private com.experian.webservice.LicenceWarningLevel warningLevel;

    private org.apache.axis.types.NonNegativeInteger daysLeft;

    private org.apache.axis.types.NonNegativeInteger dataDaysLeft;

    private org.apache.axis.types.NonNegativeInteger licenceDaysLeft;

    public QALicensedSet() {
    }

    public QALicensedSet(
           java.lang.String ID,
           java.lang.String description,
           java.lang.String copyright,
           java.lang.String version,
           java.lang.String baseCountry,
           java.lang.String status,
           java.lang.String server,
           com.experian.webservice.LicenceWarningLevel warningLevel,
           org.apache.axis.types.NonNegativeInteger daysLeft,
           org.apache.axis.types.NonNegativeInteger dataDaysLeft,
           org.apache.axis.types.NonNegativeInteger licenceDaysLeft) {
           this.ID = ID;
           this.description = description;
           this.copyright = copyright;
           this.version = version;
           this.baseCountry = baseCountry;
           this.status = status;
           this.server = server;
           this.warningLevel = warningLevel;
           this.daysLeft = daysLeft;
           this.dataDaysLeft = dataDaysLeft;
           this.licenceDaysLeft = licenceDaysLeft;
    }


    /**
     * Gets the ID value for this QALicensedSet.
     * 
     * @return ID
     */
    public java.lang.String getID() {
        return ID;
    }


    /**
     * Sets the ID value for this QALicensedSet.
     * 
     * @param ID
     */
    public void setID(java.lang.String ID) {
        this.ID = ID;
    }


    /**
     * Gets the description value for this QALicensedSet.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this QALicensedSet.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the copyright value for this QALicensedSet.
     * 
     * @return copyright
     */
    public java.lang.String getCopyright() {
        return copyright;
    }


    /**
     * Sets the copyright value for this QALicensedSet.
     * 
     * @param copyright
     */
    public void setCopyright(java.lang.String copyright) {
        this.copyright = copyright;
    }


    /**
     * Gets the version value for this QALicensedSet.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this QALicensedSet.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }


    /**
     * Gets the baseCountry value for this QALicensedSet.
     * 
     * @return baseCountry
     */
    public java.lang.String getBaseCountry() {
        return baseCountry;
    }


    /**
     * Sets the baseCountry value for this QALicensedSet.
     * 
     * @param baseCountry
     */
    public void setBaseCountry(java.lang.String baseCountry) {
        this.baseCountry = baseCountry;
    }


    /**
     * Gets the status value for this QALicensedSet.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this QALicensedSet.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the server value for this QALicensedSet.
     * 
     * @return server
     */
    public java.lang.String getServer() {
        return server;
    }


    /**
     * Sets the server value for this QALicensedSet.
     * 
     * @param server
     */
    public void setServer(java.lang.String server) {
        this.server = server;
    }


    /**
     * Gets the warningLevel value for this QALicensedSet.
     * 
     * @return warningLevel
     */
    public com.experian.webservice.LicenceWarningLevel getWarningLevel() {
        return warningLevel;
    }


    /**
     * Sets the warningLevel value for this QALicensedSet.
     * 
     * @param warningLevel
     */
    public void setWarningLevel(com.experian.webservice.LicenceWarningLevel warningLevel) {
        this.warningLevel = warningLevel;
    }


    /**
     * Gets the daysLeft value for this QALicensedSet.
     * 
     * @return daysLeft
     */
    public org.apache.axis.types.NonNegativeInteger getDaysLeft() {
        return daysLeft;
    }


    /**
     * Sets the daysLeft value for this QALicensedSet.
     * 
     * @param daysLeft
     */
    public void setDaysLeft(org.apache.axis.types.NonNegativeInteger daysLeft) {
        this.daysLeft = daysLeft;
    }


    /**
     * Gets the dataDaysLeft value for this QALicensedSet.
     * 
     * @return dataDaysLeft
     */
    public org.apache.axis.types.NonNegativeInteger getDataDaysLeft() {
        return dataDaysLeft;
    }


    /**
     * Sets the dataDaysLeft value for this QALicensedSet.
     * 
     * @param dataDaysLeft
     */
    public void setDataDaysLeft(org.apache.axis.types.NonNegativeInteger dataDaysLeft) {
        this.dataDaysLeft = dataDaysLeft;
    }


    /**
     * Gets the licenceDaysLeft value for this QALicensedSet.
     * 
     * @return licenceDaysLeft
     */
    public org.apache.axis.types.NonNegativeInteger getLicenceDaysLeft() {
        return licenceDaysLeft;
    }


    /**
     * Sets the licenceDaysLeft value for this QALicensedSet.
     * 
     * @param licenceDaysLeft
     */
    public void setLicenceDaysLeft(org.apache.axis.types.NonNegativeInteger licenceDaysLeft) {
        this.licenceDaysLeft = licenceDaysLeft;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QALicensedSet)) return false;
        QALicensedSet other = (QALicensedSet) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ID==null && other.getID()==null) || 
             (this.ID!=null &&
              this.ID.equals(other.getID()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.copyright==null && other.getCopyright()==null) || 
             (this.copyright!=null &&
              this.copyright.equals(other.getCopyright()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.baseCountry==null && other.getBaseCountry()==null) || 
             (this.baseCountry!=null &&
              this.baseCountry.equals(other.getBaseCountry()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.server==null && other.getServer()==null) || 
             (this.server!=null &&
              this.server.equals(other.getServer()))) &&
            ((this.warningLevel==null && other.getWarningLevel()==null) || 
             (this.warningLevel!=null &&
              this.warningLevel.equals(other.getWarningLevel()))) &&
            ((this.daysLeft==null && other.getDaysLeft()==null) || 
             (this.daysLeft!=null &&
              this.daysLeft.equals(other.getDaysLeft()))) &&
            ((this.dataDaysLeft==null && other.getDataDaysLeft()==null) || 
             (this.dataDaysLeft!=null &&
              this.dataDaysLeft.equals(other.getDataDaysLeft()))) &&
            ((this.licenceDaysLeft==null && other.getLicenceDaysLeft()==null) || 
             (this.licenceDaysLeft!=null &&
              this.licenceDaysLeft.equals(other.getLicenceDaysLeft())));
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
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getCopyright() != null) {
            _hashCode += getCopyright().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getBaseCountry() != null) {
            _hashCode += getBaseCountry().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getServer() != null) {
            _hashCode += getServer().hashCode();
        }
        if (getWarningLevel() != null) {
            _hashCode += getWarningLevel().hashCode();
        }
        if (getDaysLeft() != null) {
            _hashCode += getDaysLeft().hashCode();
        }
        if (getDataDaysLeft() != null) {
            _hashCode += getDataDaysLeft().hashCode();
        }
        if (getLicenceDaysLeft() != null) {
            _hashCode += getLicenceDaysLeft().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(QALicensedSet.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QALicensedSet"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("copyright");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Copyright"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("baseCountry");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "BaseCountry"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("server");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Server"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("warningLevel");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "WarningLevel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "LicenceWarningLevel"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("daysLeft");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "DaysLeft"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "nonNegativeInteger"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dataDaysLeft");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "DataDaysLeft"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "nonNegativeInteger"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("licenceDaysLeft");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "LicenceDaysLeft"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "nonNegativeInteger"));
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
