/**
 * QASearch.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class QASearch  implements java.io.Serializable {
    private java.lang.String country;

    private com.experian.webservice.EngineType engine;

    private java.lang.String layout;

    private com.experian.webservice.QAConfigType QAConfig;

    private java.lang.String search;

    private com.experian.webservice.SearchTerm[] searchSpec;

    public QASearch() {
    }

    public QASearch(
           java.lang.String country,
           com.experian.webservice.EngineType engine,
           java.lang.String layout,
           com.experian.webservice.QAConfigType QAConfig,
           java.lang.String search,
           com.experian.webservice.SearchTerm[] searchSpec) {
           this.country = country;
           this.engine = engine;
           this.layout = layout;
           this.QAConfig = QAConfig;
           this.search = search;
           this.searchSpec = searchSpec;
    }


    /**
     * Gets the country value for this QASearch.
     * 
     * @return country
     */
    public java.lang.String getCountry() {
        return country;
    }


    /**
     * Sets the country value for this QASearch.
     * 
     * @param country
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }


    /**
     * Gets the engine value for this QASearch.
     * 
     * @return engine
     */
    public com.experian.webservice.EngineType getEngine() {
        return engine;
    }


    /**
     * Sets the engine value for this QASearch.
     * 
     * @param engine
     */
    public void setEngine(com.experian.webservice.EngineType engine) {
        this.engine = engine;
    }


    /**
     * Gets the layout value for this QASearch.
     * 
     * @return layout
     */
    public java.lang.String getLayout() {
        return layout;
    }


    /**
     * Sets the layout value for this QASearch.
     * 
     * @param layout
     */
    public void setLayout(java.lang.String layout) {
        this.layout = layout;
    }


    /**
     * Gets the QAConfig value for this QASearch.
     * 
     * @return QAConfig
     */
    public com.experian.webservice.QAConfigType getQAConfig() {
        return QAConfig;
    }


    /**
     * Sets the QAConfig value for this QASearch.
     * 
     * @param QAConfig
     */
    public void setQAConfig(com.experian.webservice.QAConfigType QAConfig) {
        this.QAConfig = QAConfig;
    }


    /**
     * Gets the search value for this QASearch.
     * 
     * @return search
     */
    public java.lang.String getSearch() {
        return search;
    }


    /**
     * Sets the search value for this QASearch.
     * 
     * @param search
     */
    public void setSearch(java.lang.String search) {
        this.search = search;
    }


    /**
     * Gets the searchSpec value for this QASearch.
     * 
     * @return searchSpec
     */
    public com.experian.webservice.SearchTerm[] getSearchSpec() {
        return searchSpec;
    }


    /**
     * Sets the searchSpec value for this QASearch.
     * 
     * @param searchSpec
     */
    public void setSearchSpec(com.experian.webservice.SearchTerm[] searchSpec) {
        this.searchSpec = searchSpec;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QASearch)) return false;
        QASearch other = (QASearch) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.country==null && other.getCountry()==null) || 
             (this.country!=null &&
              this.country.equals(other.getCountry()))) &&
            ((this.engine==null && other.getEngine()==null) || 
             (this.engine!=null &&
              this.engine.equals(other.getEngine()))) &&
            ((this.layout==null && other.getLayout()==null) || 
             (this.layout!=null &&
              this.layout.equals(other.getLayout()))) &&
            ((this.QAConfig==null && other.getQAConfig()==null) || 
             (this.QAConfig!=null &&
              this.QAConfig.equals(other.getQAConfig()))) &&
            ((this.search==null && other.getSearch()==null) || 
             (this.search!=null &&
              this.search.equals(other.getSearch()))) &&
            ((this.searchSpec==null && other.getSearchSpec()==null) || 
             (this.searchSpec!=null &&
              java.util.Arrays.equals(this.searchSpec, other.getSearchSpec())));
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
        if (getCountry() != null) {
            _hashCode += getCountry().hashCode();
        }
        if (getEngine() != null) {
            _hashCode += getEngine().hashCode();
        }
        if (getLayout() != null) {
            _hashCode += getLayout().hashCode();
        }
        if (getQAConfig() != null) {
            _hashCode += getQAConfig().hashCode();
        }
        if (getSearch() != null) {
            _hashCode += getSearch().hashCode();
        }
        if (getSearchSpec() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSearchSpec());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSearchSpec(), i);
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
        new org.apache.axis.description.TypeDesc(QASearch.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QASearch"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("country");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Country"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("engine");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Engine"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "EngineType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("layout");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Layout"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("QAConfig");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAConfig"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAConfigType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("search");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Search"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchSpec");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "SearchSpec"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "SearchTerm"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "SearchTerm"));
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
