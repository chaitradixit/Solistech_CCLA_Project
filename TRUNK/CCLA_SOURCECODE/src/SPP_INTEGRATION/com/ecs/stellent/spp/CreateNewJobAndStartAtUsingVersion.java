/**
 * CreateNewJobAndStartAtUsingVersion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobAndStartAtUsingVersion  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String businessProcessId;

    private java.lang.String businessProcessName;

    private java.lang.String nodeName;

    private double version;

    private com.ecs.stellent.spp.Variable[] initialisationParms;

    public CreateNewJobAndStartAtUsingVersion() {
    }

    public CreateNewJobAndStartAtUsingVersion(
           java.lang.String sessionId,
           java.lang.String businessProcessId,
           java.lang.String businessProcessName,
           java.lang.String nodeName,
           double version,
           com.ecs.stellent.spp.Variable[] initialisationParms) {
           this.sessionId = sessionId;
           this.businessProcessId = businessProcessId;
           this.businessProcessName = businessProcessName;
           this.nodeName = nodeName;
           this.version = version;
           this.initialisationParms = initialisationParms;
    }


    /**
     * Gets the sessionId value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the businessProcessId value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @return businessProcessId
     */
    public java.lang.String getBusinessProcessId() {
        return businessProcessId;
    }


    /**
     * Sets the businessProcessId value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @param businessProcessId
     */
    public void setBusinessProcessId(java.lang.String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }


    /**
     * Gets the businessProcessName value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @return businessProcessName
     */
    public java.lang.String getBusinessProcessName() {
        return businessProcessName;
    }


    /**
     * Sets the businessProcessName value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @param businessProcessName
     */
    public void setBusinessProcessName(java.lang.String businessProcessName) {
        this.businessProcessName = businessProcessName;
    }


    /**
     * Gets the nodeName value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @return nodeName
     */
    public java.lang.String getNodeName() {
        return nodeName;
    }


    /**
     * Sets the nodeName value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @param nodeName
     */
    public void setNodeName(java.lang.String nodeName) {
        this.nodeName = nodeName;
    }


    /**
     * Gets the version value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the initialisationParms value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @return initialisationParms
     */
    public com.ecs.stellent.spp.Variable[] getInitialisationParms() {
        return initialisationParms;
    }


    /**
     * Sets the initialisationParms value for this CreateNewJobAndStartAtUsingVersion.
     * 
     * @param initialisationParms
     */
    public void setInitialisationParms(com.ecs.stellent.spp.Variable[] initialisationParms) {
        this.initialisationParms = initialisationParms;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobAndStartAtUsingVersion)) return false;
        CreateNewJobAndStartAtUsingVersion other = (CreateNewJobAndStartAtUsingVersion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sessionId==null && other.getSessionId()==null) || 
             (this.sessionId!=null &&
              this.sessionId.equals(other.getSessionId()))) &&
            ((this.businessProcessId==null && other.getBusinessProcessId()==null) || 
             (this.businessProcessId!=null &&
              this.businessProcessId.equals(other.getBusinessProcessId()))) &&
            ((this.businessProcessName==null && other.getBusinessProcessName()==null) || 
             (this.businessProcessName!=null &&
              this.businessProcessName.equals(other.getBusinessProcessName()))) &&
            ((this.nodeName==null && other.getNodeName()==null) || 
             (this.nodeName!=null &&
              this.nodeName.equals(other.getNodeName()))) &&
            this.version == other.getVersion() &&
            ((this.initialisationParms==null && other.getInitialisationParms()==null) || 
             (this.initialisationParms!=null &&
              java.util.Arrays.equals(this.initialisationParms, other.getInitialisationParms())));
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
        if (getSessionId() != null) {
            _hashCode += getSessionId().hashCode();
        }
        if (getBusinessProcessId() != null) {
            _hashCode += getBusinessProcessId().hashCode();
        }
        if (getBusinessProcessName() != null) {
            _hashCode += getBusinessProcessName().hashCode();
        }
        if (getNodeName() != null) {
            _hashCode += getNodeName().hashCode();
        }
        _hashCode += new Double(getVersion()).hashCode();
        if (getInitialisationParms() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInitialisationParms());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInitialisationParms(), i);
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
        new org.apache.axis.description.TypeDesc(CreateNewJobAndStartAtUsingVersion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndStartAtUsingVersion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessProcessId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessProcessName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("initialisationParms");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitialisationParms"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
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
