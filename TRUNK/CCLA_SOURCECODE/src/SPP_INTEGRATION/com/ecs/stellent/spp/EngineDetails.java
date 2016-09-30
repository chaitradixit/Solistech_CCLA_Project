/**
 * EngineDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class EngineDetails  implements java.io.Serializable {
    private java.lang.String serverId;

    private java.lang.String serverName;

    private short localServer;

    private java.lang.String serverLocation;

    private short serverStatus;

    private short timeDiff;

    private java.util.Calendar downloadTime;

    public EngineDetails() {
    }

    public EngineDetails(
           java.lang.String serverId,
           java.lang.String serverName,
           short localServer,
           java.lang.String serverLocation,
           short serverStatus,
           short timeDiff,
           java.util.Calendar downloadTime) {
           this.serverId = serverId;
           this.serverName = serverName;
           this.localServer = localServer;
           this.serverLocation = serverLocation;
           this.serverStatus = serverStatus;
           this.timeDiff = timeDiff;
           this.downloadTime = downloadTime;
    }


    /**
     * Gets the serverId value for this EngineDetails.
     * 
     * @return serverId
     */
    public java.lang.String getServerId() {
        return serverId;
    }


    /**
     * Sets the serverId value for this EngineDetails.
     * 
     * @param serverId
     */
    public void setServerId(java.lang.String serverId) {
        this.serverId = serverId;
    }


    /**
     * Gets the serverName value for this EngineDetails.
     * 
     * @return serverName
     */
    public java.lang.String getServerName() {
        return serverName;
    }


    /**
     * Sets the serverName value for this EngineDetails.
     * 
     * @param serverName
     */
    public void setServerName(java.lang.String serverName) {
        this.serverName = serverName;
    }


    /**
     * Gets the localServer value for this EngineDetails.
     * 
     * @return localServer
     */
    public short getLocalServer() {
        return localServer;
    }


    /**
     * Sets the localServer value for this EngineDetails.
     * 
     * @param localServer
     */
    public void setLocalServer(short localServer) {
        this.localServer = localServer;
    }


    /**
     * Gets the serverLocation value for this EngineDetails.
     * 
     * @return serverLocation
     */
    public java.lang.String getServerLocation() {
        return serverLocation;
    }


    /**
     * Sets the serverLocation value for this EngineDetails.
     * 
     * @param serverLocation
     */
    public void setServerLocation(java.lang.String serverLocation) {
        this.serverLocation = serverLocation;
    }


    /**
     * Gets the serverStatus value for this EngineDetails.
     * 
     * @return serverStatus
     */
    public short getServerStatus() {
        return serverStatus;
    }


    /**
     * Sets the serverStatus value for this EngineDetails.
     * 
     * @param serverStatus
     */
    public void setServerStatus(short serverStatus) {
        this.serverStatus = serverStatus;
    }


    /**
     * Gets the timeDiff value for this EngineDetails.
     * 
     * @return timeDiff
     */
    public short getTimeDiff() {
        return timeDiff;
    }


    /**
     * Sets the timeDiff value for this EngineDetails.
     * 
     * @param timeDiff
     */
    public void setTimeDiff(short timeDiff) {
        this.timeDiff = timeDiff;
    }


    /**
     * Gets the downloadTime value for this EngineDetails.
     * 
     * @return downloadTime
     */
    public java.util.Calendar getDownloadTime() {
        return downloadTime;
    }


    /**
     * Sets the downloadTime value for this EngineDetails.
     * 
     * @param downloadTime
     */
    public void setDownloadTime(java.util.Calendar downloadTime) {
        this.downloadTime = downloadTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EngineDetails)) return false;
        EngineDetails other = (EngineDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.serverId==null && other.getServerId()==null) || 
             (this.serverId!=null &&
              this.serverId.equals(other.getServerId()))) &&
            ((this.serverName==null && other.getServerName()==null) || 
             (this.serverName!=null &&
              this.serverName.equals(other.getServerName()))) &&
            this.localServer == other.getLocalServer() &&
            ((this.serverLocation==null && other.getServerLocation()==null) || 
             (this.serverLocation!=null &&
              this.serverLocation.equals(other.getServerLocation()))) &&
            this.serverStatus == other.getServerStatus() &&
            this.timeDiff == other.getTimeDiff() &&
            ((this.downloadTime==null && other.getDownloadTime()==null) || 
             (this.downloadTime!=null &&
              this.downloadTime.equals(other.getDownloadTime())));
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
        if (getServerId() != null) {
            _hashCode += getServerId().hashCode();
        }
        if (getServerName() != null) {
            _hashCode += getServerName().hashCode();
        }
        _hashCode += getLocalServer();
        if (getServerLocation() != null) {
            _hashCode += getServerLocation().hashCode();
        }
        _hashCode += getServerStatus();
        _hashCode += getTimeDiff();
        if (getDownloadTime() != null) {
            _hashCode += getDownloadTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EngineDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EngineDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serverId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ServerId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serverName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ServerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localServer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LocalServer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serverLocation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ServerLocation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serverStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ServerStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timeDiff");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeDiff"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("downloadTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DownloadTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
