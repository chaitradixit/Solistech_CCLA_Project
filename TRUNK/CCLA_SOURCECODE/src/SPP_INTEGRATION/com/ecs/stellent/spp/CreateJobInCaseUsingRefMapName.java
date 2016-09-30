/**
 * CreateJobInCaseUsingRefMapName.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateJobInCaseUsingRefMapName  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String processName;

    private java.lang.String caseReference;

    private com.ecs.stellent.spp.Variable[] initParams;

    public CreateJobInCaseUsingRefMapName() {
    }

    public CreateJobInCaseUsingRefMapName(
           java.lang.String sessionId,
           java.lang.String processName,
           java.lang.String caseReference,
           com.ecs.stellent.spp.Variable[] initParams) {
           this.sessionId = sessionId;
           this.processName = processName;
           this.caseReference = caseReference;
           this.initParams = initParams;
    }


    /**
     * Gets the sessionId value for this CreateJobInCaseUsingRefMapName.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this CreateJobInCaseUsingRefMapName.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the processName value for this CreateJobInCaseUsingRefMapName.
     * 
     * @return processName
     */
    public java.lang.String getProcessName() {
        return processName;
    }


    /**
     * Sets the processName value for this CreateJobInCaseUsingRefMapName.
     * 
     * @param processName
     */
    public void setProcessName(java.lang.String processName) {
        this.processName = processName;
    }


    /**
     * Gets the caseReference value for this CreateJobInCaseUsingRefMapName.
     * 
     * @return caseReference
     */
    public java.lang.String getCaseReference() {
        return caseReference;
    }


    /**
     * Sets the caseReference value for this CreateJobInCaseUsingRefMapName.
     * 
     * @param caseReference
     */
    public void setCaseReference(java.lang.String caseReference) {
        this.caseReference = caseReference;
    }


    /**
     * Gets the initParams value for this CreateJobInCaseUsingRefMapName.
     * 
     * @return initParams
     */
    public com.ecs.stellent.spp.Variable[] getInitParams() {
        return initParams;
    }


    /**
     * Sets the initParams value for this CreateJobInCaseUsingRefMapName.
     * 
     * @param initParams
     */
    public void setInitParams(com.ecs.stellent.spp.Variable[] initParams) {
        this.initParams = initParams;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateJobInCaseUsingRefMapName)) return false;
        CreateJobInCaseUsingRefMapName other = (CreateJobInCaseUsingRefMapName) obj;
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
            ((this.processName==null && other.getProcessName()==null) || 
             (this.processName!=null &&
              this.processName.equals(other.getProcessName()))) &&
            ((this.caseReference==null && other.getCaseReference()==null) || 
             (this.caseReference!=null &&
              this.caseReference.equals(other.getCaseReference()))) &&
            ((this.initParams==null && other.getInitParams()==null) || 
             (this.initParams!=null &&
              java.util.Arrays.equals(this.initParams, other.getInitParams())));
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
        if (getProcessName() != null) {
            _hashCode += getProcessName().hashCode();
        }
        if (getCaseReference() != null) {
            _hashCode += getCaseReference().hashCode();
        }
        if (getInitParams() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInitParams());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInitParams(), i);
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
        new org.apache.axis.description.TypeDesc(CreateJobInCaseUsingRefMapName.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateJobInCaseUsingRefMapName"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("caseReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CaseReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("initParams");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"));
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
