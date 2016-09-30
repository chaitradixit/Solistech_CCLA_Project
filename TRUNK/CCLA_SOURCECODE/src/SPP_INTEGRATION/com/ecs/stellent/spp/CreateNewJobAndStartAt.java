/**
 * CreateNewJobAndStartAt.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobAndStartAt  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String businessProcessId;

    private java.lang.String businessProcessName;

    private java.lang.String nodeName;

    private com.ecs.stellent.spp.Variable[] initialisationParms;

    public CreateNewJobAndStartAt() {
    }

    public CreateNewJobAndStartAt(
           java.lang.String sessionId,
           java.lang.String businessProcessId,
           java.lang.String businessProcessName,
           java.lang.String nodeName,
           com.ecs.stellent.spp.Variable[] initialisationParms) {
           this.sessionId = sessionId;
           this.businessProcessId = businessProcessId;
           this.businessProcessName = businessProcessName;
           this.nodeName = nodeName;
           this.initialisationParms = initialisationParms;
    }


    /**
     * Gets the sessionId value for this CreateNewJobAndStartAt.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this CreateNewJobAndStartAt.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the businessProcessId value for this CreateNewJobAndStartAt.
     * 
     * @return businessProcessId
     */
    public java.lang.String getBusinessProcessId() {
        return businessProcessId;
    }


    /**
     * Sets the businessProcessId value for this CreateNewJobAndStartAt.
     * 
     * @param businessProcessId
     */
    public void setBusinessProcessId(java.lang.String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }


    /**
     * Gets the businessProcessName value for this CreateNewJobAndStartAt.
     * 
     * @return businessProcessName
     */
    public java.lang.String getBusinessProcessName() {
        return businessProcessName;
    }


    /**
     * Sets the businessProcessName value for this CreateNewJobAndStartAt.
     * 
     * @param businessProcessName
     */
    public void setBusinessProcessName(java.lang.String businessProcessName) {
        this.businessProcessName = businessProcessName;
    }


    /**
     * Gets the nodeName value for this CreateNewJobAndStartAt.
     * 
     * @return nodeName
     */
    public java.lang.String getNodeName() {
        return nodeName;
    }


    /**
     * Sets the nodeName value for this CreateNewJobAndStartAt.
     * 
     * @param nodeName
     */
    public void setNodeName(java.lang.String nodeName) {
        this.nodeName = nodeName;
    }


    /**
     * Gets the initialisationParms value for this CreateNewJobAndStartAt.
     * 
     * @return initialisationParms
     */
    public com.ecs.stellent.spp.Variable[] getInitialisationParms() {
        return initialisationParms;
    }


    /**
     * Sets the initialisationParms value for this CreateNewJobAndStartAt.
     * 
     * @param initialisationParms
     */
    public void setInitialisationParms(com.ecs.stellent.spp.Variable[] initialisationParms) {
        this.initialisationParms = initialisationParms;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobAndStartAt)) return false;
        CreateNewJobAndStartAt other = (CreateNewJobAndStartAt) obj;
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
        new org.apache.axis.description.TypeDesc(CreateNewJobAndStartAt.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndStartAt"));
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
