/**
 * JobVarHistoryDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class JobVarHistoryDetails  implements java.io.Serializable {
    private java.lang.String VAR_ID;

    private short VAR_TYPE;

    private java.util.Calendar SET_TIME;

    private short SET_MILLI_SECOND;

    private java.lang.String VAR_VALUE_BEFORE;

    private java.lang.String VAR_VALUE_AFTER;

    public JobVarHistoryDetails() {
    }

    public JobVarHistoryDetails(
           java.lang.String VAR_ID,
           short VAR_TYPE,
           java.util.Calendar SET_TIME,
           short SET_MILLI_SECOND,
           java.lang.String VAR_VALUE_BEFORE,
           java.lang.String VAR_VALUE_AFTER) {
           this.VAR_ID = VAR_ID;
           this.VAR_TYPE = VAR_TYPE;
           this.SET_TIME = SET_TIME;
           this.SET_MILLI_SECOND = SET_MILLI_SECOND;
           this.VAR_VALUE_BEFORE = VAR_VALUE_BEFORE;
           this.VAR_VALUE_AFTER = VAR_VALUE_AFTER;
    }


    /**
     * Gets the VAR_ID value for this JobVarHistoryDetails.
     * 
     * @return VAR_ID
     */
    public java.lang.String getVAR_ID() {
        return VAR_ID;
    }


    /**
     * Sets the VAR_ID value for this JobVarHistoryDetails.
     * 
     * @param VAR_ID
     */
    public void setVAR_ID(java.lang.String VAR_ID) {
        this.VAR_ID = VAR_ID;
    }


    /**
     * Gets the VAR_TYPE value for this JobVarHistoryDetails.
     * 
     * @return VAR_TYPE
     */
    public short getVAR_TYPE() {
        return VAR_TYPE;
    }


    /**
     * Sets the VAR_TYPE value for this JobVarHistoryDetails.
     * 
     * @param VAR_TYPE
     */
    public void setVAR_TYPE(short VAR_TYPE) {
        this.VAR_TYPE = VAR_TYPE;
    }


    /**
     * Gets the SET_TIME value for this JobVarHistoryDetails.
     * 
     * @return SET_TIME
     */
    public java.util.Calendar getSET_TIME() {
        return SET_TIME;
    }


    /**
     * Sets the SET_TIME value for this JobVarHistoryDetails.
     * 
     * @param SET_TIME
     */
    public void setSET_TIME(java.util.Calendar SET_TIME) {
        this.SET_TIME = SET_TIME;
    }


    /**
     * Gets the SET_MILLI_SECOND value for this JobVarHistoryDetails.
     * 
     * @return SET_MILLI_SECOND
     */
    public short getSET_MILLI_SECOND() {
        return SET_MILLI_SECOND;
    }


    /**
     * Sets the SET_MILLI_SECOND value for this JobVarHistoryDetails.
     * 
     * @param SET_MILLI_SECOND
     */
    public void setSET_MILLI_SECOND(short SET_MILLI_SECOND) {
        this.SET_MILLI_SECOND = SET_MILLI_SECOND;
    }


    /**
     * Gets the VAR_VALUE_BEFORE value for this JobVarHistoryDetails.
     * 
     * @return VAR_VALUE_BEFORE
     */
    public java.lang.String getVAR_VALUE_BEFORE() {
        return VAR_VALUE_BEFORE;
    }


    /**
     * Sets the VAR_VALUE_BEFORE value for this JobVarHistoryDetails.
     * 
     * @param VAR_VALUE_BEFORE
     */
    public void setVAR_VALUE_BEFORE(java.lang.String VAR_VALUE_BEFORE) {
        this.VAR_VALUE_BEFORE = VAR_VALUE_BEFORE;
    }


    /**
     * Gets the VAR_VALUE_AFTER value for this JobVarHistoryDetails.
     * 
     * @return VAR_VALUE_AFTER
     */
    public java.lang.String getVAR_VALUE_AFTER() {
        return VAR_VALUE_AFTER;
    }


    /**
     * Sets the VAR_VALUE_AFTER value for this JobVarHistoryDetails.
     * 
     * @param VAR_VALUE_AFTER
     */
    public void setVAR_VALUE_AFTER(java.lang.String VAR_VALUE_AFTER) {
        this.VAR_VALUE_AFTER = VAR_VALUE_AFTER;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof JobVarHistoryDetails)) return false;
        JobVarHistoryDetails other = (JobVarHistoryDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.VAR_ID==null && other.getVAR_ID()==null) || 
             (this.VAR_ID!=null &&
              this.VAR_ID.equals(other.getVAR_ID()))) &&
            this.VAR_TYPE == other.getVAR_TYPE() &&
            ((this.SET_TIME==null && other.getSET_TIME()==null) || 
             (this.SET_TIME!=null &&
              this.SET_TIME.equals(other.getSET_TIME()))) &&
            this.SET_MILLI_SECOND == other.getSET_MILLI_SECOND() &&
            ((this.VAR_VALUE_BEFORE==null && other.getVAR_VALUE_BEFORE()==null) || 
             (this.VAR_VALUE_BEFORE!=null &&
              this.VAR_VALUE_BEFORE.equals(other.getVAR_VALUE_BEFORE()))) &&
            ((this.VAR_VALUE_AFTER==null && other.getVAR_VALUE_AFTER()==null) || 
             (this.VAR_VALUE_AFTER!=null &&
              this.VAR_VALUE_AFTER.equals(other.getVAR_VALUE_AFTER())));
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
        if (getVAR_ID() != null) {
            _hashCode += getVAR_ID().hashCode();
        }
        _hashCode += getVAR_TYPE();
        if (getSET_TIME() != null) {
            _hashCode += getSET_TIME().hashCode();
        }
        _hashCode += getSET_MILLI_SECOND();
        if (getVAR_VALUE_BEFORE() != null) {
            _hashCode += getVAR_VALUE_BEFORE().hashCode();
        }
        if (getVAR_VALUE_AFTER() != null) {
            _hashCode += getVAR_VALUE_AFTER().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(JobVarHistoryDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobVarHistoryDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VAR_ID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VAR_ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VAR_TYPE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VAR_TYPE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SET_TIME");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SET_TIME"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SET_MILLI_SECOND");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SET_MILLI_SECOND"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VAR_VALUE_BEFORE");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VAR_VALUE_BEFORE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("VAR_VALUE_AFTER");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VAR_VALUE_AFTER"));
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
