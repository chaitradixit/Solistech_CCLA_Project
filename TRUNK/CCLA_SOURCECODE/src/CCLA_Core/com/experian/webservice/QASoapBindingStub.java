/**
 * QASoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.webservice;

public class QASoapBindingStub extends org.apache.axis.client.Stub implements com.experian.webservice.QAPortType_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[11];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoSearch");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QASearch"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QASearch"), com.experian.webservice.QASearch.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QASearchResult"));
        oper.setReturnClass(com.experian.webservice.QASearchResult.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QASearchResult"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoRefine");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QARefine"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QARefine"), com.experian.webservice.QARefine.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">Picklist"));
        oper.setReturnClass(com.experian.webservice.Picklist.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Picklist"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoGetAddress");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAGetAddress"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetAddress"), com.experian.webservice.QAGetAddress.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">Address"));
        oper.setReturnClass(com.experian.webservice.Address.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Address"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoGetRawAddress");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAGetRawAddress"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetRawAddress"), com.experian.webservice.QAGetRawAddress.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">RawAddress"));
        oper.setReturnClass(com.experian.webservice.RawAddressLine[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "RawAddress"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QARawAddress"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoGetData");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAData"));
        oper.setReturnClass(com.experian.webservice.QADataSet[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAData"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "DataSet"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoGetLicenseInfo");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QALicenceInfo"));
        oper.setReturnClass(com.experian.webservice.QALicenceInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QALicenceInfo"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoGetSystemInfo");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QASystemInfo"));
        oper.setReturnClass(java.lang.String[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QASystemInfo"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "SystemInfo"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoGetExampleAddresses");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAGetExampleAddresses"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetExampleAddresses"), com.experian.webservice.QAGetExampleAddresses.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAExampleAddresses"));
        oper.setReturnClass(com.experian.webservice.QAExampleAddress[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAExampleAddresses"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "ExampleAddress"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoGetLayouts");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAGetLayouts"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetLayouts"), com.experian.webservice.QAGetLayouts.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QALayouts"));
        oper.setReturnClass(com.experian.webservice.QALayout[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QALayouts"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Layout"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoGetPromptSet");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAGetPromptSet"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetPromptSet"), com.experian.webservice.QAGetPromptSet.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAPromptSet"));
        oper.setReturnClass(com.experian.webservice.PromptLine[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAPromptSet"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Line"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DoCanSearch");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QACanSearch"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QACanSearch"), com.experian.webservice.QACanSearch.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QASearchOk"));
        oper.setReturnClass(com.experian.webservice.QASearchOk.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QASearchOk"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAFault"),
                      "com.experian.webservice.QAFault",
                      new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault"), 
                      true
                     ));
        _operations[10] = oper;

    }

    public QASoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public QASoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public QASoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">Address");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.Address.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">Picklist");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.Picklist.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QACanSearch");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QACanSearch.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAData");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QADataSet[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QADataSet");
            qName2 = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "DataSet");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAExampleAddresses");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QAExampleAddress[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAExampleAddress");
            qName2 = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "ExampleAddress");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAFault");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QAFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetAddress");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QAGetAddress.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetExampleAddresses");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QAGetExampleAddresses.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetLayouts");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QAGetLayouts.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetPromptSet");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QAGetPromptSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAGetRawAddress");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QAGetRawAddress.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QALayouts");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QALayout[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QALayout");
            qName2 = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Layout");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QALicenceInfo");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QALicenceInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QAPromptSet");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.PromptLine[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PromptLine");
            qName2 = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "Line");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QARefine");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QARefine.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QASearch");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QASearch.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QASearchOk");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QASearchOk.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QASearchResult");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QASearchResult.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">QASystemInfo");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "SystemInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", ">RawAddress");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.RawAddressLine[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "RawAddressLine");
            qName2 = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QARawAddress");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "AddressLineType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.AddressLineType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "EngineEnumType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.EngineEnumType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "EngineIntensityType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.EngineIntensityType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "EngineType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.EngineType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "ISOType");
            cachedSerQNames.add(qName);
            cls = java.lang.String.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "LicenceWarningLevel");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.LicenceWarningLevel.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "LineContentType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.LineContentType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PicklistEntryType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.PicklistEntryType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PromptLine");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.PromptLine.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "PromptSetType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.PromptSetType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAAddressType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.AddressLineType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "AddressLineType");
            qName2 = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "AddressLine");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAConfigType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QAConfigType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QADataSet");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QADataSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAExampleAddress");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QAExampleAddress.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QALayout");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QALayout.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QALicensedSet");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QALicensedSet.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "QAPicklistType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.QAPicklistType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "RawAddressLine");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.RawAddressLine.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "SearchTerm");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.SearchTerm.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "SearchType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.SearchTerm[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "SearchTerm");
            qName2 = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "SearchTerm");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "ThresholdType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.types.PositiveInteger.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "TimeoutType");
            cachedSerQNames.add(qName);
            cls = org.apache.axis.types.NonNegativeInteger.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.qas.com/web-2005-10", "VerifyLevelType");
            cachedSerQNames.add(qName);
            cls = com.experian.webservice.VerifyLevelType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public com.experian.webservice.QASearchResult doSearch(com.experian.webservice.QASearch body) throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoSearch");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoSearch"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.experian.webservice.QASearchResult) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.experian.webservice.QASearchResult) org.apache.axis.utils.JavaUtils.convert(_resp, com.experian.webservice.QASearchResult.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.experian.webservice.Picklist doRefine(com.experian.webservice.QARefine body) throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoRefine");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoRefine"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.experian.webservice.Picklist) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.experian.webservice.Picklist) org.apache.axis.utils.JavaUtils.convert(_resp, com.experian.webservice.Picklist.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.experian.webservice.Address doGetAddress(com.experian.webservice.QAGetAddress body) throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoGetAddress");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoGetAddress"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.experian.webservice.Address) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.experian.webservice.Address) org.apache.axis.utils.JavaUtils.convert(_resp, com.experian.webservice.Address.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.experian.webservice.RawAddressLine[] doGetRawAddress(com.experian.webservice.QAGetRawAddress body) throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoGetRawAddress");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoGetRawAddress"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.experian.webservice.RawAddressLine[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.experian.webservice.RawAddressLine[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.experian.webservice.RawAddressLine[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.experian.webservice.QADataSet[] doGetData() throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoGetData");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoGetData"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.experian.webservice.QADataSet[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.experian.webservice.QADataSet[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.experian.webservice.QADataSet[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.experian.webservice.QALicenceInfo doGetLicenseInfo() throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoGetLicenseInfo");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoGetLicenseInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.experian.webservice.QALicenceInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.experian.webservice.QALicenceInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.experian.webservice.QALicenceInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public java.lang.String[] doGetSystemInfo() throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoGetSystemInfo");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoGetSystemInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String[]) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.experian.webservice.QAExampleAddress[] doGetExampleAddresses(com.experian.webservice.QAGetExampleAddresses body) throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoGetExampleAddresses");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoGetExampleAddresses"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.experian.webservice.QAExampleAddress[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.experian.webservice.QAExampleAddress[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.experian.webservice.QAExampleAddress[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.experian.webservice.QALayout[] doGetLayouts(com.experian.webservice.QAGetLayouts body) throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoGetLayouts");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoGetLayouts"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.experian.webservice.QALayout[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.experian.webservice.QALayout[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.experian.webservice.QALayout[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.experian.webservice.PromptLine[] doGetPromptSet(com.experian.webservice.QAGetPromptSet body) throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoGetPromptSet");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoGetPromptSet"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.experian.webservice.PromptLine[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.experian.webservice.PromptLine[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.experian.webservice.PromptLine[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.experian.webservice.QASearchOk doCanSearch(com.experian.webservice.QACanSearch body) throws java.rmi.RemoteException, com.experian.webservice.QAFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://www.qas.com/web-2005-10/DoCanSearch");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "DoCanSearch"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {body});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.experian.webservice.QASearchOk) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.experian.webservice.QASearchOk) org.apache.axis.utils.JavaUtils.convert(_resp, com.experian.webservice.QASearchOk.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.experian.webservice.QAFault) {
              throw (com.experian.webservice.QAFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
