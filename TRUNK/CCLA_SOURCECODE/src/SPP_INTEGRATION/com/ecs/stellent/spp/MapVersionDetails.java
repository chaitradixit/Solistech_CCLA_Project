/**
 * MapVersionDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class MapVersionDetails  implements java.io.Serializable {
    private double version;

    private java.lang.String processName;

    private java.lang.String lastModifiedByName;

    private java.util.Calendar lastModifiedDate;

    public MapVersionDetails() {
    }

    public MapVersionDetails(
           double version,
           java.lang.String processName,
           java.lang.String lastModifiedByName,
           java.util.Calendar lastModifiedDate) {
           this.version = version;
           this.processName = processName;
           this.lastModifiedByName = lastModifiedByName;
           this.lastModifiedDate = lastModifiedDate;
    }


    /**
     * Gets the version value for this MapVersionDetails.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this MapVersionDetails.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the processName value for this MapVersionDetails.
     * 
     * @return processName
     */
    public java.lang.String getProcessName() {
        return processName;
    }


    /**
     * Sets the processName value for this MapVersionDetails.
     * 
     * @param processName
     */
    public void setProcessName(java.lang.String processName) {
        this.processName = processName;
    }


    /**
     * Gets the lastModifiedByName value for this MapVersionDetails.
     * 
     * @return lastModifiedByName
     */
    public java.lang.String getLastModifiedByName() {
        return lastModifiedByName;
    }


    /**
     * Sets the lastModifiedByName value for this MapVersionDetails.
     * 
     * @param lastModifiedByName
     */
    public void setLastModifiedByName(java.lang.String lastModifiedByName) {
        this.lastModifiedByName = lastModifiedByName;
    }


    /**
     * Gets the lastModifiedDate value for this MapVersionDetails.
     * 
     * @return lastModifiedDate
     */
    public java.util.Calendar getLastModifiedDate() {
        return lastModifiedDate;
    }


    /**
     * Sets the lastModifiedDate value for this MapVersionDetails.
     * 
     * @param lastModifiedDate
     */
    public void setLastModifiedDate(java.util.Calendar lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MapVersionDetails)) return false;
        MapVersionDetails other = (MapVersionDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.version == other.getVersion() &&
            ((this.processName==null && other.getProcessName()==null) || 
             (this.processName!=null &&
              this.processName.equals(other.getProcessName()))) &&
            ((this.lastModifiedByName==null && other.getLastModifiedByName()==null) || 
             (this.lastModifiedByName!=null &&
              this.lastModifiedByName.equals(other.getLastModifiedByName()))) &&
            ((this.lastModifiedDate==null && other.getLastModifiedDate()==null) || 
             (this.lastModifiedDate!=null &&
              this.lastModifiedDate.equals(other.getLastModifiedDate())));
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
        _hashCode += new Double(getVersion()).hashCode();
        if (getProcessName() != null) {
            _hashCode += getProcessName().hashCode();
        }
        if (getLastModifiedByName() != null) {
            _hashCode += getLastModifiedByName().hashCode();
        }
        if (getLastModifiedDate() != null) {
            _hashCode += getLastModifiedDate().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MapVersionDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersionDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastModifiedByName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastModifiedByName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastModifiedDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastModifiedDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
