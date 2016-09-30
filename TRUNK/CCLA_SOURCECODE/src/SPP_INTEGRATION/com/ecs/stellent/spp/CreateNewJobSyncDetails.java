/**
 * CreateNewJobSyncDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class CreateNewJobSyncDetails  implements java.io.Serializable {
    private com.ecs.stellent.spp.VariableValueInfo[] variableValueInfo;

    private java.lang.String jobId;

    public CreateNewJobSyncDetails() {
    }

    public CreateNewJobSyncDetails(
           com.ecs.stellent.spp.VariableValueInfo[] variableValueInfo,
           java.lang.String jobId) {
           this.variableValueInfo = variableValueInfo;
           this.jobId = jobId;
    }


    /**
     * Gets the variableValueInfo value for this CreateNewJobSyncDetails.
     * 
     * @return variableValueInfo
     */
    public com.ecs.stellent.spp.VariableValueInfo[] getVariableValueInfo() {
        return variableValueInfo;
    }


    /**
     * Sets the variableValueInfo value for this CreateNewJobSyncDetails.
     * 
     * @param variableValueInfo
     */
    public void setVariableValueInfo(com.ecs.stellent.spp.VariableValueInfo[] variableValueInfo) {
        this.variableValueInfo = variableValueInfo;
    }


    /**
     * Gets the jobId value for this CreateNewJobSyncDetails.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this CreateNewJobSyncDetails.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateNewJobSyncDetails)) return false;
        CreateNewJobSyncDetails other = (CreateNewJobSyncDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.variableValueInfo==null && other.getVariableValueInfo()==null) || 
             (this.variableValueInfo!=null &&
              java.util.Arrays.equals(this.variableValueInfo, other.getVariableValueInfo()))) &&
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId())));
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
        if (getVariableValueInfo() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getVariableValueInfo());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getVariableValueInfo(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateNewJobSyncDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobSyncDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("variableValueInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "variableValueInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableValueInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableValueInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"));
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
