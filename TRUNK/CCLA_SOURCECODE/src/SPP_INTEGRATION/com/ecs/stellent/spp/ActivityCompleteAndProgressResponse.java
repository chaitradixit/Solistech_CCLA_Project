/**
 * ActivityCompleteAndProgressResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ActivityCompleteAndProgressResponse  implements java.io.Serializable {
    private com.ecs.stellent.spp.ProgressDetails activityCompleteAndProgressResult;

    public ActivityCompleteAndProgressResponse() {
    }

    public ActivityCompleteAndProgressResponse(
           com.ecs.stellent.spp.ProgressDetails activityCompleteAndProgressResult) {
           this.activityCompleteAndProgressResult = activityCompleteAndProgressResult;
    }


    /**
     * Gets the activityCompleteAndProgressResult value for this ActivityCompleteAndProgressResponse.
     * 
     * @return activityCompleteAndProgressResult
     */
    public com.ecs.stellent.spp.ProgressDetails getActivityCompleteAndProgressResult() {
        return activityCompleteAndProgressResult;
    }


    /**
     * Sets the activityCompleteAndProgressResult value for this ActivityCompleteAndProgressResponse.
     * 
     * @param activityCompleteAndProgressResult
     */
    public void setActivityCompleteAndProgressResult(com.ecs.stellent.spp.ProgressDetails activityCompleteAndProgressResult) {
        this.activityCompleteAndProgressResult = activityCompleteAndProgressResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ActivityCompleteAndProgressResponse)) return false;
        ActivityCompleteAndProgressResponse other = (ActivityCompleteAndProgressResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.activityCompleteAndProgressResult==null && other.getActivityCompleteAndProgressResult()==null) || 
             (this.activityCompleteAndProgressResult!=null &&
              this.activityCompleteAndProgressResult.equals(other.getActivityCompleteAndProgressResult())));
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
        if (getActivityCompleteAndProgressResult() != null) {
            _hashCode += getActivityCompleteAndProgressResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ActivityCompleteAndProgressResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ActivityCompleteAndProgressResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activityCompleteAndProgressResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityCompleteAndProgressResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProgressDetails"));
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
