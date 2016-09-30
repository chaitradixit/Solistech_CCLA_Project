/**
 * ArrayOfShareClassMovement.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class ArrayOfShareClassMovement  implements java.io.Serializable {
    private java.lang.String dummy;

    private com.aurora.webservice.ShareClassMovement[] shareClassMovement;

    public ArrayOfShareClassMovement() {
    }

    public ArrayOfShareClassMovement(
           java.lang.String dummy,
           com.aurora.webservice.ShareClassMovement[] shareClassMovement) {
           this.dummy = dummy;
           this.shareClassMovement = shareClassMovement;
    }


    /**
     * Gets the dummy value for this ArrayOfShareClassMovement.
     * 
     * @return dummy
     */
    public java.lang.String getDummy() {
        return dummy;
    }


    /**
     * Sets the dummy value for this ArrayOfShareClassMovement.
     * 
     * @param dummy
     */
    public void setDummy(java.lang.String dummy) {
        this.dummy = dummy;
    }


    /**
     * Gets the shareClassMovement value for this ArrayOfShareClassMovement.
     * 
     * @return shareClassMovement
     */
    public com.aurora.webservice.ShareClassMovement[] getShareClassMovement() {
        return shareClassMovement;
    }


    /**
     * Sets the shareClassMovement value for this ArrayOfShareClassMovement.
     * 
     * @param shareClassMovement
     */
    public void setShareClassMovement(com.aurora.webservice.ShareClassMovement[] shareClassMovement) {
        this.shareClassMovement = shareClassMovement;
    }

    public com.aurora.webservice.ShareClassMovement getShareClassMovement(int i) {
        return this.shareClassMovement[i];
    }

    public void setShareClassMovement(int i, com.aurora.webservice.ShareClassMovement _value) {
        this.shareClassMovement[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ArrayOfShareClassMovement)) return false;
        ArrayOfShareClassMovement other = (ArrayOfShareClassMovement) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dummy==null && other.getDummy()==null) || 
             (this.dummy!=null &&
              this.dummy.equals(other.getDummy()))) &&
            ((this.shareClassMovement==null && other.getShareClassMovement()==null) || 
             (this.shareClassMovement!=null &&
              java.util.Arrays.equals(this.shareClassMovement, other.getShareClassMovement())));
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
        if (getDummy() != null) {
            _hashCode += getDummy().hashCode();
        }
        if (getShareClassMovement() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getShareClassMovement());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getShareClassMovement(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ArrayOfShareClassMovement.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ArrayOfShareClassMovement"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dummy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "dummy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shareClassMovement");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClassMovement"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ShareClassMovement"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
