/**
 * ExtendedWorkQueue4.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ExtendedWorkQueue4  implements java.io.Serializable {
    private java.lang.String[] jobIds;

    private int lastPriority;

    private int firstPriority;

    private com.ecs.stellent.spp.ExtendedWorkQActivity3[] workqActivity3;

    private java.util.Calendar lastDueDate;

    private java.util.Calendar firstDueDate;

    private int numActivitiesObtained;

    public ExtendedWorkQueue4() {
    }

    public ExtendedWorkQueue4(
           java.lang.String[] jobIds,
           int lastPriority,
           int firstPriority,
           com.ecs.stellent.spp.ExtendedWorkQActivity3[] workqActivity3,
           java.util.Calendar lastDueDate,
           java.util.Calendar firstDueDate,
           int numActivitiesObtained) {
           this.jobIds = jobIds;
           this.lastPriority = lastPriority;
           this.firstPriority = firstPriority;
           this.workqActivity3 = workqActivity3;
           this.lastDueDate = lastDueDate;
           this.firstDueDate = firstDueDate;
           this.numActivitiesObtained = numActivitiesObtained;
    }


    /**
     * Gets the jobIds value for this ExtendedWorkQueue4.
     * 
     * @return jobIds
     */
    public java.lang.String[] getJobIds() {
        return jobIds;
    }


    /**
     * Sets the jobIds value for this ExtendedWorkQueue4.
     * 
     * @param jobIds
     */
    public void setJobIds(java.lang.String[] jobIds) {
        this.jobIds = jobIds;
    }


    /**
     * Gets the lastPriority value for this ExtendedWorkQueue4.
     * 
     * @return lastPriority
     */
    public int getLastPriority() {
        return lastPriority;
    }


    /**
     * Sets the lastPriority value for this ExtendedWorkQueue4.
     * 
     * @param lastPriority
     */
    public void setLastPriority(int lastPriority) {
        this.lastPriority = lastPriority;
    }


    /**
     * Gets the firstPriority value for this ExtendedWorkQueue4.
     * 
     * @return firstPriority
     */
    public int getFirstPriority() {
        return firstPriority;
    }


    /**
     * Sets the firstPriority value for this ExtendedWorkQueue4.
     * 
     * @param firstPriority
     */
    public void setFirstPriority(int firstPriority) {
        this.firstPriority = firstPriority;
    }


    /**
     * Gets the workqActivity3 value for this ExtendedWorkQueue4.
     * 
     * @return workqActivity3
     */
    public com.ecs.stellent.spp.ExtendedWorkQActivity3[] getWorkqActivity3() {
        return workqActivity3;
    }


    /**
     * Sets the workqActivity3 value for this ExtendedWorkQueue4.
     * 
     * @param workqActivity3
     */
    public void setWorkqActivity3(com.ecs.stellent.spp.ExtendedWorkQActivity3[] workqActivity3) {
        this.workqActivity3 = workqActivity3;
    }


    /**
     * Gets the lastDueDate value for this ExtendedWorkQueue4.
     * 
     * @return lastDueDate
     */
    public java.util.Calendar getLastDueDate() {
        return lastDueDate;
    }


    /**
     * Sets the lastDueDate value for this ExtendedWorkQueue4.
     * 
     * @param lastDueDate
     */
    public void setLastDueDate(java.util.Calendar lastDueDate) {
        this.lastDueDate = lastDueDate;
    }


    /**
     * Gets the firstDueDate value for this ExtendedWorkQueue4.
     * 
     * @return firstDueDate
     */
    public java.util.Calendar getFirstDueDate() {
        return firstDueDate;
    }


    /**
     * Sets the firstDueDate value for this ExtendedWorkQueue4.
     * 
     * @param firstDueDate
     */
    public void setFirstDueDate(java.util.Calendar firstDueDate) {
        this.firstDueDate = firstDueDate;
    }


    /**
     * Gets the numActivitiesObtained value for this ExtendedWorkQueue4.
     * 
     * @return numActivitiesObtained
     */
    public int getNumActivitiesObtained() {
        return numActivitiesObtained;
    }


    /**
     * Sets the numActivitiesObtained value for this ExtendedWorkQueue4.
     * 
     * @param numActivitiesObtained
     */
    public void setNumActivitiesObtained(int numActivitiesObtained) {
        this.numActivitiesObtained = numActivitiesObtained;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExtendedWorkQueue4)) return false;
        ExtendedWorkQueue4 other = (ExtendedWorkQueue4) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.jobIds==null && other.getJobIds()==null) || 
             (this.jobIds!=null &&
              java.util.Arrays.equals(this.jobIds, other.getJobIds()))) &&
            this.lastPriority == other.getLastPriority() &&
            this.firstPriority == other.getFirstPriority() &&
            ((this.workqActivity3==null && other.getWorkqActivity3()==null) || 
             (this.workqActivity3!=null &&
              java.util.Arrays.equals(this.workqActivity3, other.getWorkqActivity3()))) &&
            ((this.lastDueDate==null && other.getLastDueDate()==null) || 
             (this.lastDueDate!=null &&
              this.lastDueDate.equals(other.getLastDueDate()))) &&
            ((this.firstDueDate==null && other.getFirstDueDate()==null) || 
             (this.firstDueDate!=null &&
              this.firstDueDate.equals(other.getFirstDueDate()))) &&
            this.numActivitiesObtained == other.getNumActivitiesObtained();
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
        if (getJobIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getJobIds());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getJobIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getLastPriority();
        _hashCode += getFirstPriority();
        if (getWorkqActivity3() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWorkqActivity3());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getWorkqActivity3(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLastDueDate() != null) {
            _hashCode += getLastDueDate().hashCode();
        }
        if (getFirstDueDate() != null) {
            _hashCode += getFirstDueDate().hashCode();
        }
        _hashCode += getNumActivitiesObtained();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExtendedWorkQueue4.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue4"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobIds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastPriority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastPriority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firstPriority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FirstPriority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workqActivity3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "workqActivity3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity3"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity3"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastDueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastDueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firstDueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FirstDueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numActivitiesObtained");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumActivitiesObtained"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
