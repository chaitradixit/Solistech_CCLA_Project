/**
 * SubJobDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class SubJobDetails  implements java.io.Serializable {
    private java.lang.String jobID;

    private java.lang.String subJobID;

    private java.lang.String subProcessID;

    private double subJobVersion;

    private com.ecs.stellent.spp.Variable[] vars;

    private com.ecs.stellent.spp.ActivityHistory[] history;

    public SubJobDetails() {
    }

    public SubJobDetails(
           java.lang.String jobID,
           java.lang.String subJobID,
           java.lang.String subProcessID,
           double subJobVersion,
           com.ecs.stellent.spp.Variable[] vars,
           com.ecs.stellent.spp.ActivityHistory[] history) {
           this.jobID = jobID;
           this.subJobID = subJobID;
           this.subProcessID = subProcessID;
           this.subJobVersion = subJobVersion;
           this.vars = vars;
           this.history = history;
    }


    /**
     * Gets the jobID value for this SubJobDetails.
     * 
     * @return jobID
     */
    public java.lang.String getJobID() {
        return jobID;
    }


    /**
     * Sets the jobID value for this SubJobDetails.
     * 
     * @param jobID
     */
    public void setJobID(java.lang.String jobID) {
        this.jobID = jobID;
    }


    /**
     * Gets the subJobID value for this SubJobDetails.
     * 
     * @return subJobID
     */
    public java.lang.String getSubJobID() {
        return subJobID;
    }


    /**
     * Sets the subJobID value for this SubJobDetails.
     * 
     * @param subJobID
     */
    public void setSubJobID(java.lang.String subJobID) {
        this.subJobID = subJobID;
    }


    /**
     * Gets the subProcessID value for this SubJobDetails.
     * 
     * @return subProcessID
     */
    public java.lang.String getSubProcessID() {
        return subProcessID;
    }


    /**
     * Sets the subProcessID value for this SubJobDetails.
     * 
     * @param subProcessID
     */
    public void setSubProcessID(java.lang.String subProcessID) {
        this.subProcessID = subProcessID;
    }


    /**
     * Gets the subJobVersion value for this SubJobDetails.
     * 
     * @return subJobVersion
     */
    public double getSubJobVersion() {
        return subJobVersion;
    }


    /**
     * Sets the subJobVersion value for this SubJobDetails.
     * 
     * @param subJobVersion
     */
    public void setSubJobVersion(double subJobVersion) {
        this.subJobVersion = subJobVersion;
    }


    /**
     * Gets the vars value for this SubJobDetails.
     * 
     * @return vars
     */
    public com.ecs.stellent.spp.Variable[] getVars() {
        return vars;
    }


    /**
     * Sets the vars value for this SubJobDetails.
     * 
     * @param vars
     */
    public void setVars(com.ecs.stellent.spp.Variable[] vars) {
        this.vars = vars;
    }


    /**
     * Gets the history value for this SubJobDetails.
     * 
     * @return history
     */
    public com.ecs.stellent.spp.ActivityHistory[] getHistory() {
        return history;
    }


    /**
     * Sets the history value for this SubJobDetails.
     * 
     * @param history
     */
    public void setHistory(com.ecs.stellent.spp.ActivityHistory[] history) {
        this.history = history;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SubJobDetails)) return false;
        SubJobDetails other = (SubJobDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.jobID==null && other.getJobID()==null) || 
             (this.jobID!=null &&
              this.jobID.equals(other.getJobID()))) &&
            ((this.subJobID==null && other.getSubJobID()==null) || 
             (this.subJobID!=null &&
              this.subJobID.equals(other.getSubJobID()))) &&
            ((this.subProcessID==null && other.getSubProcessID()==null) || 
             (this.subProcessID!=null &&
              this.subProcessID.equals(other.getSubProcessID()))) &&
            this.subJobVersion == other.getSubJobVersion() &&
            ((this.vars==null && other.getVars()==null) || 
             (this.vars!=null &&
              java.util.Arrays.equals(this.vars, other.getVars()))) &&
            ((this.history==null && other.getHistory()==null) || 
             (this.history!=null &&
              java.util.Arrays.equals(this.history, other.getHistory())));
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
        if (getJobID() != null) {
            _hashCode += getJobID().hashCode();
        }
        if (getSubJobID() != null) {
            _hashCode += getSubJobID().hashCode();
        }
        if (getSubProcessID() != null) {
            _hashCode += getSubProcessID().hashCode();
        }
        _hashCode += new Double(getSubJobVersion()).hashCode();
        if (getVars() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getVars());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getVars(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getHistory() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getHistory());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getHistory(), i);
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
        new org.apache.axis.description.TypeDesc(SubJobDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subJobID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subProcessID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubProcessID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subJobVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vars");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "vars"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("history");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "history"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory"));
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
