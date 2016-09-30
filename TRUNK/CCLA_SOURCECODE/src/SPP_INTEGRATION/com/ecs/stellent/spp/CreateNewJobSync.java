/**
 * CreateNewJobSync.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobSync  implements java.io.Serializable {
    private java.lang.String sessionId;

    private java.lang.String processId;

    private com.ecs.stellent.spp.Variable[] initParams;

    private java.lang.String[] returnParamsIds;

    public CreateNewJobSync() {
    }

    public CreateNewJobSync(
           java.lang.String sessionId,
           java.lang.String processId,
           com.ecs.stellent.spp.Variable[] initParams,
           java.lang.String[] returnParamsIds) {
           this.sessionId = sessionId;
           this.processId = processId;
           this.initParams = initParams;
           this.returnParamsIds = returnParamsIds;
    }


    /**
     * Gets the sessionId value for this CreateNewJobSync.
     * 
     * @return sessionId
     */
    public java.lang.String getSessionId() {
        return sessionId;
    }


    /**
     * Sets the sessionId value for this CreateNewJobSync.
     * 
     * @param sessionId
     */
    public void setSessionId(java.lang.String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * Gets the processId value for this CreateNewJobSync.
     * 
     * @return processId
     */
    public java.lang.String getProcessId() {
        return processId;
    }


    /**
     * Sets the processId value for this CreateNewJobSync.
     * 
     * @param processId
     */
    public void setProcessId(java.lang.String processId) {
        this.processId = processId;
    }


    /**
     * Gets the initParams value for this CreateNewJobSync.
     * 
     * @return initParams
     */
    public com.ecs.stellent.spp.Variable[] getInitParams() {
        return initParams;
    }


    /**
     * Sets the initParams value for this CreateNewJobSync.
     * 
     * @param initParams
     */
    public void setInitParams(com.ecs.stellent.spp.Variable[] initParams) {
        this.initParams = initParams;
    }


    /**
     * Gets the returnParamsIds value for this CreateNewJobSync.
     * 
     * @return returnParamsIds
     */
    public java.lang.String[] getReturnParamsIds() {
        return returnParamsIds;
    }


    /**
     * Sets the returnParamsIds value for this CreateNewJobSync.
     * 
     * @param returnParamsIds
     */
    public void setReturnParamsIds(java.lang.String[] returnParamsIds) {
        this.returnParamsIds = returnParamsIds;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobSync)) return false;
        CreateNewJobSync other = (CreateNewJobSync) obj;
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
            ((this.processId==null && other.getProcessId()==null) || 
             (this.processId!=null &&
              this.processId.equals(other.getProcessId()))) &&
            ((this.initParams==null && other.getInitParams()==null) || 
             (this.initParams!=null &&
              java.util.Arrays.equals(this.initParams, other.getInitParams()))) &&
            ((this.returnParamsIds==null && other.getReturnParamsIds()==null) || 
             (this.returnParamsIds!=null &&
              java.util.Arrays.equals(this.returnParamsIds, other.getReturnParamsIds())));
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
        if (getProcessId() != null) {
            _hashCode += getProcessId().hashCode();
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
        if (getReturnParamsIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReturnParamsIds());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReturnParamsIds(), i);
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
        new org.apache.axis.description.TypeDesc(CreateNewJobSync.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobSync"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnParamsIds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReturnParamsIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
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
