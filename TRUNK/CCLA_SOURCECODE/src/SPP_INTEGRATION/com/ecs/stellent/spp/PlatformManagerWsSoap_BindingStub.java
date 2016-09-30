/**
 * PlatformManagerWsSoap_BindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class PlatformManagerWsSoap_BindingStub extends org.apache.axis.client.Stub implements com.ecs.stellent.spp.PlatformManagerWsSoap_PortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[148];
        _initOperationDesc1();
        _initOperationDesc2();
        _initOperationDesc3();
        _initOperationDesc4();
        _initOperationDesc5();
        _initOperationDesc6();
        _initOperationDesc7();
        _initOperationDesc8();
        _initOperationDesc9();
        _initOperationDesc10();
        _initOperationDesc11();
        _initOperationDesc12();
        _initOperationDesc13();
        _initOperationDesc14();
        _initOperationDesc15();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJob");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TakePendingActivity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EPCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PendingActivity"));
        oper.setReturnClass(com.ecs.stellent.spp.PendingActivity.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakePendingActivityResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TakePendingActivity4");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EPCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PendingActivity"));
        oper.setReturnClass(com.ecs.stellent.spp.PendingActivity.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakePendingActivity4Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetSubJobID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetSubJobIDResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("Logon");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UnConditional"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.LogonDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("LogonUsingPassword");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonPassword"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UnConditional"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.LogonDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonUsingPasswordResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobUsingMapName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingMapNameResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewCase");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CaseReference"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewCaseResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewCaseUsingMapName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CaseReference"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewCaseUsingMapNameResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateJobInCase");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CaseJobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateJobInCaseResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateJobInCaseUsingMapName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CaseJobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateJobInCaseUsingMapNameResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateJobInCaseUsingRef");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CaseReference"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateJobInCaseUsingRefResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateJobInCaseUsingRefMapName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CaseReference"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateJobInCaseUsingRefMapNameResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobAndProgress");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProgressDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.ProgressDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgressResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobAndProgressUsingName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProgressDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.ProgressDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgressUsingNameResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("LogOff");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ActivityComplete");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OutputVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("SaveActivity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InputVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OutputVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ActivityCompleteAndProgress");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OutputVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProgressDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.ProgressDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityCompleteAndProgressResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[18] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PendingActivityComplete");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OutputVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[19] = oper;

    }

    private static void _initOperationDesc3(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PendingActivityCompleteUsingActivityName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OutputVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[20] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobProps");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDatabase"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties"));
        oper.setReturnClass(com.ecs.stellent.spp.JobProperties.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobPropsResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[21] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobProps3");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDatabase"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties3"));
        oper.setReturnClass(com.ecs.stellent.spp.JobProperties3.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobProps3Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[22] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobProps4");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDatabase"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties4"));
        oper.setReturnClass(com.ecs.stellent.spp.JobProperties4.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobProps4Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[23] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobProps6");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AssociatedJobs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties6"));
        oper.setReturnClass(com.ecs.stellent.spp.JobProperties6.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobProps6Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[24] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CancelActivity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[25] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AddNote");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Note"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[26] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UpdateNote");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DateNoteEntered"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Note"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[27] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UpdateJobOwner");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobOwnerID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[28] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UpdateJobOwners");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobOwnerID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[29] = oper;

    }

    private static void _initOperationDesc4(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ChangeJobState");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "State"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[30] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DeleteNote");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DateNoteEntered"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[31] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetActivitiesCompletedByResource");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDatabase"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivityHistory"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivityHistory[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetActivitiesCompletedByResourceResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[32] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PerformAction");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActionType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[33] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PerformActions");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActionType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[34] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetUserCount");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        oper.setReturnClass(short.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetUserCountResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[35] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AccessPermitted");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Action"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AccessPermittedResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[36] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetInitVarList");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"));
        oper.setReturnClass(com.ecs.stellent.spp.Variable[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetInitVarListResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[37] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetTrustedDomains");
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString"));
        oper.setReturnClass(java.lang.String[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetTrustedDomainsResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[38] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DeclineOfferedActivity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[39] = oper;

    }

    private static void _initOperationDesc5(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TakeAnyActivity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivityInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeAnyActivityResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[40] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TakeAnyActivity4");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivityInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeAnyActivity4Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[41] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetResourcesWithSkillAndSecurity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseSkillLevel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SkillLevel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseSecurityLevel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SecurityLevel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseGroupResourceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GroupResourceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfMTSResourceDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.MTSResourceDetails[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetResourcesWithSkillAndSecurityResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MTSResourceDetails"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[42] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetResourcesWithSkillAndSecurityUsingGroupName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseSkillLevel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SkillLevel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseSecurityLevel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SecurityLevel"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GroupResourceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfMTSResourceDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.MTSResourceDetails[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetResourcesWithSkillAndSecurityUsingGroupNameResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MTSResourceDetails"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[43] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetNumberOfActivitiesAssignedToResource");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourcesIncluded"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetNumberOfActivitiesAssignedToResourceResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[44] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetNumberOfActivitiesAssignedToResourceUsingResName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourcesIncluded"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetNumberOfActivitiesAssignedToResourceUsingResNameResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[45] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMinJobDetails");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDatabase"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MinJobDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.MinJobDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMinJobDetailsResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[46] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RetrieveAllCategories");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfCategoryDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.CategoryDetails[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RetrieveAllCategoriesResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryDetails"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[47] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMapsForUser");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AccessType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AllVersions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfProcessMapDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.ProcessMapDetails[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMapsForUserResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapDetails"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[48] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMapsForUserUsingCategory");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AccessType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AllVersions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfProcessMapDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.ProcessMapDetails[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMapsForUserUsingCategoryResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapDetails"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[49] = oper;

    }

    private static void _initOperationDesc6(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMinMapsForUser");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfProcessMapData"));
        oper.setReturnClass(com.ecs.stellent.spp.ProcessMapData[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMinMapsForUserResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapData"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[50] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMinMapsForUserUsingCategory");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfProcessMapData"));
        oper.setReturnClass(com.ecs.stellent.spp.ProcessMapData[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMinMapsForUserUsingCategoryResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapData"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[51] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMinMapsForUserUsingCategory2");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfProcessMapData"));
        oper.setReturnClass(com.ecs.stellent.spp.ProcessMapData[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMinMapsForUserUsingCategory2Result"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapData"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[52] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RestartJobUsingNodeName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EPList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfEmbeddedProcessList"), com.ecs.stellent.spp.EmbeddedProcessList[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessList"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RestartType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[53] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RestartJob");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EPList"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfEmbeddedProcessList"), com.ecs.stellent.spp.EmbeddedProcessList[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessList"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RestartType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[54] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetVariablesWithCategory");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OwnerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariableWithCategory"));
        oper.setReturnClass(com.ecs.stellent.spp.VariableWithCategory[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetVariablesWithCategoryResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableWithCategory"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[55] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TakeActivityFromJob");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivityInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityFromJobResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[56] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TakeActivityUsingProcess");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivityInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityUsingProcessResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[57] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TakeActivityUsingActivityName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivityInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityUsingActivityNameResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[58] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("TakeActivityUsingActivityNameAndProcess");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivityInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityUsingActivityNameAndProcessResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[59] = oper;

    }

    private static void _initOperationDesc7(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetActivitiesInJobWithStatus");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivitiesForStatus"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivitiesForStatus[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetActivitiesInJobWithStatusResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[60] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetActivitiesInJobWithStatus2");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivitiesForStatus2"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivitiesForStatus2[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetActivitiesInJobWithStatus2Result"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus2"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[61] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetActivitiesInJobWithStatus3");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivitiesForStatus3"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivitiesForStatus3[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetActivitiesInJobWithStatus3Result"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus3"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[62] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ProcessAutomaticActivityFailure");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExceptionCode"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AutoActivityType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CancelActivity"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[63] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobsUsingSearchCriteria");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkerResource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseOriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MaxNumToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcTemplateVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfJobDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.JobDetails[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteriaResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDetails"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[64] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetExtendedJobsUsingSearchCriteria");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkerResource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseOriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MaxNumToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Priority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobs"));
        oper.setReturnClass(com.ecs.stellent.spp.ExtendedJobs.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedJobsUsingSearchCriteriaResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[65] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobsUsingSearchCriteria4");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkerResource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseOriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MaxNumToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Priority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobOwner"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobOwnerID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobState"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobStateName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobs"));
        oper.setReturnClass(com.ecs.stellent.spp.ExtendedJobs.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteria4Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[66] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UpdateJobOwnerUsingSearchCriteria");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkerResource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseOriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MaxNumToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Priority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobOwner"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "sJobOwnerID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "newJobOwnerID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobState"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobStateName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[67] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetNumberOfActivitiesUsingCriteria");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsingJobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsingStatus"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetNumberOfActivitiesUsingCriteriaResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[68] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetEngineList");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfEngineDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.EngineDetails[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetEngineListResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EngineDetails"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[69] = oper;

    }

    private static void _initOperationDesc8(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetWorkersForServer");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfWorkersInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.WorkersInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkersForServerResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkersInfo"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[70] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetWorkerGroupsForServer");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfWorkersInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.WorkersInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkerGroupsForServerResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkersInfo"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[71] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMembersInGroupForServer");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfWorkersInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.WorkersInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMembersInGroupForServerResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkersInfo"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[72] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetVariables");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OwnerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"));
        oper.setReturnClass(com.ecs.stellent.spp.Variable[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetVariablesResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[73] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RecoverJobs");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString"));
        oper.setReturnClass(java.lang.String[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RecoverJobsResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[74] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RecoverJob");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[75] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("VerifyNTUserandPassword");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UserName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DomainName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Password"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VerifyNTUserandPasswordResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[76] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetServerID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetServerIDResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[77] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ValidateSession");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ValidateSessionResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[78] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ValidateSession2");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        oper.setReturnClass(boolean.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ValidateSession2Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[79] = oper;

    }

    private static void _initOperationDesc9(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UpdateVariables");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OwnerID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UpdatedVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariableUpdate"), com.ecs.stellent.spp.VariableUpdate[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableUpdate"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[80] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetXMLMapControlData");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetXMLMapControlDataResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[81] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetEmbeddedXMLMapControlData");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LoadLatestVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetEmbeddedXMLMapControlDataResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[82] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetXMLMapControlJobDataOnly");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetXMLMapControlJobDataOnlyResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[83] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetWorkQueue");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivities"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDateDue"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityTypeFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueue"));
        oper.setReturnClass(com.ecs.stellent.spp.WorkQueue.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkQueueResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[84] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetWorkQueueUsingActivityName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivities"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDateDue"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityTypeFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueue"));
        oper.setReturnClass(com.ecs.stellent.spp.WorkQueue.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkQueueUsingActivityNameResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[85] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetIndividualWorkQueue");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivitiesRq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityTypeFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueue"));
        oper.setReturnClass(com.ecs.stellent.spp.WorkQueue.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetIndividualWorkQueueResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[86] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AddSchedule");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeSpan"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeSpanPeriod"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NoOfOccurances"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NoOfJobsToCreate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseEndDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EndDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitialisationParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfScheduleVariable"), com.ecs.stellent.spp.ScheduleVariable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleVariable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Monthly"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseWeekDays"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DaysOfWeek"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OneActiveAtTime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleActive"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseBusinessCalendar"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobScheduleDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.JobScheduleDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AddScheduleResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[87] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ModifySchedule");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeSpan"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimeSpanPeriod"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NoOfOccurances"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NoOfJobsToCreate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleActive"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseEndDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EndDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitialisationParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfScheduleVariable"), com.ecs.stellent.spp.ScheduleVariable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleVariable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Monthly"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseWeekDays"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DaysOfWeek"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OneActiveAtTime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseBusinessCalendar"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        oper.setReturnClass(java.util.Calendar.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ModifyScheduleResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[88] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DeleteSchedule");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[89] = oper;

    }

    private static void _initOperationDesc10(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RetrieveSchedule");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobScheduleInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.JobScheduleInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RetrieveScheduleResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[90] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RetrieveSchedulePage");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PageSize"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SchedulePageInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.SchedulePageInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RetrieveSchedulePageResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[91] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobVariablesValues");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VarIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariableValueInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.VariableValueInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobVariablesValuesResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableValueInfo"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[92] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetServerVariablesValues");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VarIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariableValueInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.VariableValueInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetServerVariablesValuesResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableValueInfo"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[93] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobSync");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReturnParamsIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobSyncDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.CreateNewJobSyncDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobSyncResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[94] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJaggedArray");
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfArrayOfAnyType"));
        oper.setReturnClass(java.lang.Object[][].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJaggedArrayResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfAnyType"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[95] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetExtendedWorkQueue");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivities"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDateDue"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityTypeFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WrkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReturnJobIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrioritiesToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseLastDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue"));
        oper.setReturnClass(com.ecs.stellent.spp.ExtendedWorkQueue.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueueResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[96] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetExtendedWorkQueue3");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivities"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityTypeFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WrkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReturnJobIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrioritiesToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CombinedWorkqueue"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "bUseJobRAGStatus"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobRAGStatus"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "bUseJobState"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobState"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PageDirection"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue3"));
        oper.setReturnClass(com.ecs.stellent.spp.ExtendedWorkQueue3.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueue3Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[97] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetExtendedWorkQueue2");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivities"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityTypeFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WrkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReturnJobIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrioritiesToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CombinedWorkqueue"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PageDirection"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue2"));
        oper.setReturnClass(com.ecs.stellent.spp.ExtendedWorkQueue2.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueue2Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[98] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetExtendedWorkQueue4");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivities"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityTypeFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"), long.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WrkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReturnJobIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrioritiesToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CombinedWorkqueue"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "bUseJobRAGStatus"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobRAGStatus"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "bUseJobState"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobState"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PageDirection"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "bUseActivityRAGStatus"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityRAGStatus"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue4"));
        oper.setReturnClass(com.ecs.stellent.spp.ExtendedWorkQueue4.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueue4Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[99] = oper;

    }

    private static void _initOperationDesc11(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetExtendedWorkQueueUsingActivityName");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivities"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDateDue"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityTypeFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WrkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReturnJobIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PrioritiesToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseLastDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue"));
        oper.setReturnClass(com.ecs.stellent.spp.ExtendedWorkQueue.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueueUsingActivityNameResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[100] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetExtendedIndividualWorkQueue");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NumberOfActivitiesRq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityTypeFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WrkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReturnJobIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PriorityToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseLastDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastDueDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue"));
        oper.setReturnClass(com.ecs.stellent.spp.ExtendedWorkQueue.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedIndividualWorkQueueResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[101] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobUsingStartDate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitialisationParms"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingStartDateResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[102] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobAndStartAt");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitialisationParms"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndStartAtResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[103] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ActivityCompleteWithTimeAndCost");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OPVars"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Cost"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[104] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobUsingVersion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingVersionResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[105] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobSyncUsingVersion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReturnParamsIds"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobSyncDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.CreateNewJobSyncDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobSyncUsingVersionResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[106] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMapVersions");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapStatus"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AccessType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfMapVersionDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.MapVersionDetails[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMapVersionsResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersionDetails"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[107] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetInitVarListUsingVersion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"));
        oper.setReturnClass(com.ecs.stellent.spp.Variable[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetInitVarListUsingVersionResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[108] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetInitVarList3");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariableDisplayName"));
        oper.setReturnClass(com.ecs.stellent.spp.VariableDisplayName[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetInitVarList3Result"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableDisplayName"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[109] = oper;

    }

    private static void _initOperationDesc12(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobAndProgressUsingVersion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProgressDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.ProgressDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgressUsingVersionResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[110] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobAndProgressUsingNameAndVersion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProgressDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.ProgressDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgressUsingNameAndVersionResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[111] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobUsingMapNameAndVersion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitParams"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingMapNameAndVersionResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[112] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobUsingStartDateUsingVersion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitialisationParms"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingStartDateUsingVersionResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[113] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("CreateNewJobAndStartAtUsingVersion");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "BusinessProcessName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InitialisationParms"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndStartAtUsingVersionResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[114] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobVariableHistory");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EPC"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePerformDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PerformDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfJobVarHistoryDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.JobVarHistoryDetails[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobVariableHistoryResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobVarHistoryDetails"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[115] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetInstallationType");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        oper.setReturnClass(short.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetInstallationTypeResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[116] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobVariableValue");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VarId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        oper.setReturnClass(java.lang.Object.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobVariableValueResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[117] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PlaceJobOnHold");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "HoldType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivationDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivationPeriodWeeks"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivationPeriodDays"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivationPeriodHours"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReasonForHold"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[118] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ResetJobVariables");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseLatestHistory"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SetTime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SetTimeMilliSecs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[119] = oper;

    }

    private static void _initOperationDesc13(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetSubJobXMLMapControlData");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubjobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetSubJobXMLMapControlDataResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[120] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobsUsingSearchCriteria5");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkerResource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseOriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MaxNumToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Priority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobOwner"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobOwnerID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobState"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobStateName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessTemplateVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobs"));
        oper.setReturnClass(com.ecs.stellent.spp.ExtendedJobs.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteria5Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[121] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetJobsUsingSearchCriteria6");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CaseRef"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCaseRef"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseFinishTimeTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCreatorId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkerResource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Status"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseProcessId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseOriginServerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MaxNumToRetrieve"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseCategoryId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Priority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UsePriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateFrom"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDateTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseDueDateTo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueueDefnName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilter"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig"), com.ecs.stellent.spp.FieldFilterConfig.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobOwner"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobOwnerID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobState"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobStateName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseJobType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessTemplateVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable"), com.ecs.stellent.spp.Variable[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobs2"));
        oper.setReturnClass(com.ecs.stellent.spp.ExtendedJobs2.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteria6Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[122] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetSubJobDetails");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubProcessID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobVersion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDatabase"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.SubJobDetails.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetSubJobDetailsResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[123] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UpdateJobPriority");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobPriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[124] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UpdateActivityPriority");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityPriority"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[125] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("UpdateSpend");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Value"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"), double.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[126] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetProcessHistory");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AssociatedJobs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivityHistory2"));
        oper.setReturnClass(com.ecs.stellent.spp.ActivityHistory2[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetProcessHistoryResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory2"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[127] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMilestones");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfMilestones"));
        oper.setReturnClass(com.ecs.stellent.spp.Milestones[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMilestonesResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Milestones"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[128] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetRoles");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfRoleInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.RoleInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetRolesResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleInfo"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[129] = oper;

    }

    private static void _initOperationDesc14(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetEvents");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfEvents"));
        oper.setReturnClass(com.ecs.stellent.spp.Events[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetEventsResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Events"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[130] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetNotes");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfNotes"));
        oper.setReturnClass(com.ecs.stellent.spp.Notes[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetNotesResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Notes"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[131] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetAssociatedJobs");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfJobInfo"));
        oper.setReturnClass(com.ecs.stellent.spp.JobInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetAssociatedJobsResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobInfo"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[132] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RaiseEvent");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "jobid"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Event"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EventSource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[133] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RaiseEventUpdVars");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "jobid"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Event"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EventSource"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "vVariables"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfRaiseEventVars"), com.ecs.stellent.spp.RaiseEventVars[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RaiseEventVars"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[134] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("RevaluatePreconditon");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "jobid"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[135] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AddToNumericVarValue");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OwnerId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VarId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VarValueToAdd"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"), java.lang.Object.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[136] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AddRoleMember");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[137] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("DeleteRoleMember");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[138] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ReplaceRoleMembers");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceIDs"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString"), java.lang.String[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[139] = oper;

    }

    private static void _initOperationDesc15(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ModifyMilestone");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MilestoneName"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NewTargerDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[140] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetStateChangeHistory");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfStateHistory"));
        oper.setReturnClass(com.ecs.stellent.spp.StateHistory[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetStateChangeHistoryResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StateHistory"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[141] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetWorkqueueOverviewForResource");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetIndividualWorkLoad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetCombinedWorkLoad"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkqueueDefinitionWorkLoads"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseStartDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseEndDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EndDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkqueueOverview"));
        oper.setReturnClass(com.ecs.stellent.spp.WorkqueueOverview.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkqueueOverviewForResourceResult"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[142] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AllocateActivity");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EPCount"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[143] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("AllocateActivities");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Activities"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivityRuntimeInfo"), com.ecs.stellent.spp.ActivityRuntimeInfo[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityRuntimeInfo"));
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[144] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetAllProcessMapStates");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "sessionID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString"));
        oper.setReturnClass(java.lang.String[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetAllProcessMapStatesResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[145] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("GetMapsForUserBasedOnType");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SessionId"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AccessType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessType"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"), short.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AllVersions"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfProcessMapDetails"));
        oper.setReturnClass(com.ecs.stellent.spp.ProcessMapDetails[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMapsForUserBasedOnTypeResult"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapDetails"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[146] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("PerformAutoWorkAllocation");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EndDate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"), java.util.Calendar.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[147] = oper;

    }

    public PlatformManagerWsSoap_BindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PlatformManagerWsSoap_BindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PlatformManagerWsSoap_BindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
        addBindings0();
        addBindings1();
        addBindings2();
        addBindings3();
        addBindings4();
    }

    private void addBindings0() {
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
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AccessPermitted");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AccessPermitted.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AccessPermittedResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AccessPermittedResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ActivityComplete");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityComplete.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ActivityCompleteAndProgress");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityCompleteAndProgress.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ActivityCompleteAndProgressResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityCompleteAndProgressResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ActivityCompleteResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityCompleteResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ActivityCompleteWithTimeAndCost");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityCompleteWithTimeAndCost.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ActivityCompleteWithTimeAndCostResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityCompleteWithTimeAndCostResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AddNote");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AddNote.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AddNoteResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AddNoteResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AddRoleMember");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AddRoleMember.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AddRoleMemberResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AddRoleMemberResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AddSchedule");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AddSchedule.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AddScheduleResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AddScheduleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AddToNumericVarValue");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AddToNumericVarValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AddToNumericVarValueResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AddToNumericVarValueResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AllocateActivities");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AllocateActivities.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AllocateActivitiesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AllocateActivitiesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AllocateActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AllocateActivity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">AllocateActivityResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.AllocateActivityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CancelActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CancelActivity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CancelActivityResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CancelActivityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ChangeJobState");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ChangeJobState.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ChangeJobStateResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ChangeJobStateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateJobInCase");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateJobInCase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateJobInCaseResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateJobInCaseResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateJobInCaseUsingMapName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateJobInCaseUsingMapName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateJobInCaseUsingMapNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateJobInCaseUsingMapNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateJobInCaseUsingRef");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateJobInCaseUsingRef.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateJobInCaseUsingRefMapName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateJobInCaseUsingRefMapName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateJobInCaseUsingRefMapNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateJobInCaseUsingRefMapNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateJobInCaseUsingRefResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateJobInCaseUsingRefResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewCase");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewCase.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewCaseResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewCaseResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewCaseUsingMapName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewCaseUsingMapName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewCaseUsingMapNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewCaseUsingMapNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJob");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJob.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgress");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndProgress.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgressResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndProgressResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgressUsingName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndProgressUsingName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgressUsingNameAndVersion");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndProgressUsingNameAndVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgressUsingNameAndVersionResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndProgressUsingNameAndVersionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgressUsingNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndProgressUsingNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgressUsingVersion");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndProgressUsingVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndProgressUsingVersionResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndProgressUsingVersionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndStartAt");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndStartAt.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndStartAtResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndStartAtResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndStartAtUsingVersion");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndStartAtUsingVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobAndStartAtUsingVersionResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobAndStartAtUsingVersionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobSync");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobSync.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobSyncResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobSyncResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobSyncUsingVersion");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobSyncUsingVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobSyncUsingVersionResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobSyncUsingVersionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingMapName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobUsingMapName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingMapNameAndVersion");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobUsingMapNameAndVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingMapNameAndVersionResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobUsingMapNameAndVersionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingMapNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobUsingMapNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingStartDate");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobUsingStartDate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingStartDateResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobUsingStartDateResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingStartDateUsingVersion");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobUsingStartDateUsingVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingStartDateUsingVersionResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobUsingStartDateUsingVersionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingVersion");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobUsingVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">CreateNewJobUsingVersionResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobUsingVersionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">DeclineOfferedActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.DeclineOfferedActivity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">DeclineOfferedActivityResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.DeclineOfferedActivityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">DeleteNote");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.DeleteNote.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">DeleteNoteResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.DeleteNoteResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">DeleteRoleMember");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.DeleteRoleMember.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">DeleteRoleMemberResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.DeleteRoleMemberResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">DeleteSchedule");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.DeleteSchedule.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">DeleteScheduleResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.DeleteScheduleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetActivitiesCompletedByResource");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetActivitiesCompletedByResource.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetActivitiesCompletedByResourceResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetActivitiesCompletedByResourceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetActivitiesInJobWithStatus");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetActivitiesInJobWithStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetActivitiesInJobWithStatus2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetActivitiesInJobWithStatus2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetActivitiesInJobWithStatus2Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetActivitiesInJobWithStatus2Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetActivitiesInJobWithStatus3");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetActivitiesInJobWithStatus3.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetActivitiesInJobWithStatus3Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetActivitiesInJobWithStatus3Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetActivitiesInJobWithStatusResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetActivitiesInJobWithStatusResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetAllProcessMapStates");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetAllProcessMapStates.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetAllProcessMapStatesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetAllProcessMapStatesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetAssociatedJobs");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetAssociatedJobs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetAssociatedJobsResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetAssociatedJobsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetEmbeddedXMLMapControlData");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetEmbeddedXMLMapControlData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetEmbeddedXMLMapControlDataResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetEmbeddedXMLMapControlDataResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetEngineList");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetEngineList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetEngineListResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetEngineListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetEvents");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetEvents.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetEventsResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetEventsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedIndividualWorkQueue");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedIndividualWorkQueue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedIndividualWorkQueueResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedIndividualWorkQueueResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedJobsUsingSearchCriteria");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedJobsUsingSearchCriteria.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedJobsUsingSearchCriteriaResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedJobsUsingSearchCriteriaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedWorkQueue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedWorkQueue2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue2Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedWorkQueue2Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue3");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedWorkQueue3.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue3Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedWorkQueue3Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue4");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedWorkQueue4.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }
    private void addBindings1() {
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
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueue4Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedWorkQueue4Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueueResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedWorkQueueResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueueUsingActivityName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedWorkQueueUsingActivityName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetExtendedWorkQueueUsingActivityNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetExtendedWorkQueueUsingActivityNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetIndividualWorkQueue");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetIndividualWorkQueue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetIndividualWorkQueueResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetIndividualWorkQueueResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetInitVarList");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetInitVarList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetInitVarList3");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetInitVarList3.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetInitVarList3Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetInitVarList3Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetInitVarListResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetInitVarListResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetInitVarListUsingVersion");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetInitVarListUsingVersion.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetInitVarListUsingVersionResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetInitVarListUsingVersionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetInstallationType");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetInstallationType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetInstallationTypeResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetInstallationTypeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJaggedArray");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJaggedArray.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJaggedArrayResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJaggedArrayResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobProps.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps3");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobProps3.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps3Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobProps3Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps4");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobProps4.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps4Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobProps4Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps6");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobProps6.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobProps6Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobProps6Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobPropsResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobPropsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobsUsingSearchCriteria.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria4");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobsUsingSearchCriteria4.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria4Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobsUsingSearchCriteria4Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria5");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobsUsingSearchCriteria5.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria5Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobsUsingSearchCriteria5Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria6");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobsUsingSearchCriteria6.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteria6Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobsUsingSearchCriteria6Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobsUsingSearchCriteriaResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobsUsingSearchCriteriaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobVariableHistory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobVariableHistory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobVariableHistoryResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobVariableHistoryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobVariablesValues");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobVariablesValues.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobVariablesValuesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobVariablesValuesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobVariableValue");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobVariableValue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetJobVariableValueResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetJobVariableValueResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMapsForUser");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMapsForUser.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMapsForUserBasedOnType");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMapsForUserBasedOnType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMapsForUserBasedOnTypeResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMapsForUserBasedOnTypeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMapsForUserResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMapsForUserResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMapsForUserUsingCategory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMapsForUserUsingCategory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMapsForUserUsingCategoryResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMapsForUserUsingCategoryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMapVersions");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMapVersions.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMapVersionsResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMapVersionsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMembersInGroupForServer");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMembersInGroupForServer.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMembersInGroupForServerResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMembersInGroupForServerResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMilestones");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMilestones.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMilestonesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMilestonesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMinJobDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMinJobDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMinJobDetailsResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMinJobDetailsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMinMapsForUser");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMinMapsForUser.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMinMapsForUserResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMinMapsForUserResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMinMapsForUserUsingCategory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMinMapsForUserUsingCategory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMinMapsForUserUsingCategory2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMinMapsForUserUsingCategory2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMinMapsForUserUsingCategory2Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMinMapsForUserUsingCategory2Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetMinMapsForUserUsingCategoryResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetMinMapsForUserUsingCategoryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNotes");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetNotes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNotesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetNotesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNumberOfActivitiesAssignedToResource");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetNumberOfActivitiesAssignedToResource.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNumberOfActivitiesAssignedToResourceResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetNumberOfActivitiesAssignedToResourceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNumberOfActivitiesAssignedToResourceUsingResName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetNumberOfActivitiesAssignedToResourceUsingResName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNumberOfActivitiesAssignedToResourceUsingResNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetNumberOfActivitiesAssignedToResourceUsingResNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNumberOfActivitiesUsingCriteria");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetNumberOfActivitiesUsingCriteria.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetNumberOfActivitiesUsingCriteriaResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetNumberOfActivitiesUsingCriteriaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetProcessHistory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetProcessHistory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetProcessHistoryResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetProcessHistoryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetResourcesWithSkillAndSecurity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetResourcesWithSkillAndSecurity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetResourcesWithSkillAndSecurityResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetResourcesWithSkillAndSecurityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetResourcesWithSkillAndSecurityUsingGroupName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetResourcesWithSkillAndSecurityUsingGroupName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetResourcesWithSkillAndSecurityUsingGroupNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetResourcesWithSkillAndSecurityUsingGroupNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetRoles");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetRoles.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetRolesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetRolesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetServerID");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetServerID.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetServerIDResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetServerIDResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetServerVariablesValues");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetServerVariablesValues.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetServerVariablesValuesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetServerVariablesValuesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetStateChangeHistory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetStateChangeHistory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetStateChangeHistoryResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetStateChangeHistoryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetSubJobDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetSubJobDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetSubJobDetailsResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetSubJobDetailsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetSubJobID");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetSubJobID.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetSubJobIDResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetSubJobIDResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetSubJobXMLMapControlData");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetSubJobXMLMapControlData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetSubJobXMLMapControlDataResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetSubJobXMLMapControlDataResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetTrustedDomains");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetTrustedDomains.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetTrustedDomainsResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetTrustedDomainsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetUserCount");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetUserCount.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetUserCountResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetUserCountResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetVariables");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetVariables.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetVariablesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetVariablesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetVariablesWithCategory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetVariablesWithCategory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetVariablesWithCategoryResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetVariablesWithCategoryResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkerGroupsForServer");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetWorkerGroupsForServer.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkerGroupsForServerResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetWorkerGroupsForServerResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkersForServer");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetWorkersForServer.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkersForServerResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetWorkersForServerResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkQueue");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetWorkQueue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkqueueOverviewForResource");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetWorkqueueOverviewForResource.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }
    private void addBindings2() {
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
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkqueueOverviewForResourceResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetWorkqueueOverviewForResourceResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkQueueResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetWorkQueueResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkQueueUsingActivityName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetWorkQueueUsingActivityName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetWorkQueueUsingActivityNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetWorkQueueUsingActivityNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetXMLMapControlData");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetXMLMapControlData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetXMLMapControlDataResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetXMLMapControlDataResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetXMLMapControlJobDataOnly");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetXMLMapControlJobDataOnly.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">GetXMLMapControlJobDataOnlyResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.GetXMLMapControlJobDataOnlyResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">LogOff");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.LogOff.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">LogOffResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.LogOffResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">Logon");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.Logon.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">LogonResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.LogonResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">LogonUsingPassword");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.LogonUsingPassword.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">LogonUsingPasswordResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.LogonUsingPasswordResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ModifyMilestone");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ModifyMilestone.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ModifyMilestoneResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ModifyMilestoneResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ModifySchedule");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ModifySchedule.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ModifyScheduleResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ModifyScheduleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PendingActivityComplete");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PendingActivityComplete.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PendingActivityCompleteResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PendingActivityCompleteResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PendingActivityCompleteUsingActivityName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PendingActivityCompleteUsingActivityName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PendingActivityCompleteUsingActivityNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PendingActivityCompleteUsingActivityNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PerformAction");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PerformAction.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PerformActionResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PerformActionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PerformActions");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PerformActions.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PerformActionsResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PerformActionsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PerformAutoWorkAllocation");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PerformAutoWorkAllocation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PerformAutoWorkAllocationResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PerformAutoWorkAllocationResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PlaceJobOnHold");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PlaceJobOnHold.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">PlaceJobOnHoldResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PlaceJobOnHoldResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ProcessAutomaticActivityFailure");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ProcessAutomaticActivityFailure.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ProcessAutomaticActivityFailureResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ProcessAutomaticActivityFailureResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RaiseEvent");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RaiseEvent.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RaiseEventResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RaiseEventResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RaiseEventUpdVars");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RaiseEventUpdVars.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RaiseEventUpdVarsResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RaiseEventUpdVarsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RecoverJob");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RecoverJob.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RecoverJobResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RecoverJobResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RecoverJobs");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RecoverJobs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RecoverJobsResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RecoverJobsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ReplaceRoleMembers");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ReplaceRoleMembers.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ReplaceRoleMembersResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ReplaceRoleMembersResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ResetJobVariables");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ResetJobVariables.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ResetJobVariablesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ResetJobVariablesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RestartJob");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RestartJob.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RestartJobResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RestartJobResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RestartJobUsingNodeName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RestartJobUsingNodeName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RestartJobUsingNodeNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RestartJobUsingNodeNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RetrieveAllCategories");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RetrieveAllCategories.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RetrieveAllCategoriesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RetrieveAllCategoriesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RetrieveSchedule");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RetrieveSchedule.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RetrieveSchedulePage");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RetrieveSchedulePage.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RetrieveSchedulePageResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RetrieveSchedulePageResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RetrieveScheduleResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RetrieveScheduleResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RevaluatePreconditon");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RevaluatePreconditon.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">RevaluatePreconditonResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RevaluatePreconditonResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">SaveActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.SaveActivity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">SaveActivityResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.SaveActivityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityFromJob");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeActivityFromJob.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityFromJobResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeActivityFromJobResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityUsingActivityName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeActivityUsingActivityName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityUsingActivityNameAndProcess");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeActivityUsingActivityNameAndProcess.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityUsingActivityNameAndProcessResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeActivityUsingActivityNameAndProcessResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityUsingActivityNameResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeActivityUsingActivityNameResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityUsingProcess");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeActivityUsingProcess.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeActivityUsingProcessResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeActivityUsingProcessResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeAnyActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeAnyActivity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeAnyActivity4");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeAnyActivity4.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeAnyActivity4Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeAnyActivity4Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakeAnyActivityResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakeAnyActivityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakePendingActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakePendingActivity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakePendingActivity4");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakePendingActivity4.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakePendingActivity4Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakePendingActivity4Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">TakePendingActivityResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TakePendingActivityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateActivityPriority");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateActivityPriority.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateActivityPriorityResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateActivityPriorityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateJobOwner");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateJobOwner.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateJobOwnerResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateJobOwnerResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateJobOwners");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateJobOwners.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateJobOwnersResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateJobOwnersResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateJobOwnerUsingSearchCriteria");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateJobOwnerUsingSearchCriteria.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateJobOwnerUsingSearchCriteriaResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateJobOwnerUsingSearchCriteriaResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateJobPriority");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateJobPriority.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateJobPriorityResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateJobPriorityResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateNote");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateNote.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateNoteResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateNoteResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateSpend");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateSpend.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateSpendResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateSpendResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateVariables");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateVariables.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">UpdateVariablesResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.UpdateVariablesResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ValidateSession");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ValidateSession.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ValidateSession2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ValidateSession2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ValidateSession2Response");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ValidateSession2Response.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">ValidateSessionResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ValidateSessionResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">VerifyNTUserandPassword");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.VerifyNTUserandPassword.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", ">VerifyNTUserandPasswordResponse");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.VerifyNTUserandPasswordResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivitiesForStatus.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivitiesForStatus2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus3");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivitiesForStatus3.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityHistory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }
    private void addBindings3() {
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
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityHistory2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityRuntimeInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityRuntimeInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivitiesForStatus");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivitiesForStatus[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivitiesForStatus2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivitiesForStatus2[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus2");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus2");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivitiesForStatus3");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivitiesForStatus3[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus3");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivitiesForStatus3");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivityHistory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityHistory[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivityHistory2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityHistory2[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory2");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityHistory2");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfActivityRuntimeInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ActivityRuntimeInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityRuntimeInfo");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityRuntimeInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfAnyType");
            cachedSerQNames.add(qName);
            cls = java.lang.Object[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "anyType");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfArrayOfAnyType");
            cachedSerQNames.add(qName);
            cls = java.lang.Object[][].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfAnyType");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfAnyType");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfCategoryDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CategoryDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfCNJActivityDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CNJActivityDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CNJActivityDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CNJActivityDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfEmbeddedProcessList");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.EmbeddedProcessList[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessList");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessList");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfEngineDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.EngineDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EngineDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EngineDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfEvents");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.Events[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Events");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Events");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfExtendedJobDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedJobDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfExtendedJobDetails2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedJobDetails2[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobDetails2");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobDetails2");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfExtendedMetaFieldDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedMetaFieldDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedMetaFieldDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedMetaFieldDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfExtendedWorkQActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedWorkQActivity[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfExtendedWorkQActivity2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedWorkQActivity2[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity2");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity2");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfExtendedWorkQActivity3");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedWorkQActivity3[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity3");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity3");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfFieldFilterInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.FieldFilterInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterInfo");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfJobDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfJobInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobInfo");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfJobVarHistoryDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobVarHistoryDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobVarHistoryDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobVarHistoryDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfMapVersionDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.MapVersionDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersionDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersionDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfMilestones");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.Milestones[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Milestones");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Milestones");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfMTSResourceDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.MTSResourceDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MTSResourceDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MTSResourceDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfNotes");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.Notes[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Notes");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Notes");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfProcessMapData");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ProcessMapData[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapData");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapData");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfProcessMapDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ProcessMapDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfRaiseEventVars");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RaiseEventVars[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RaiseEventVars");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RaiseEventVars");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfResourceDetail");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ResourceDetail[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceDetail");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceDetail");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfRoleInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RoleInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleInfo");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfRoleMember");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RoleMember[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleMember");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleMember");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfSchedulePageDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.SchedulePageDetails[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SchedulePageDetails");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SchedulePageDetails");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfScheduleVariable");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ScheduleVariable[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleVariable");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleVariable");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfStateHistory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.StateHistory[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StateHistory");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StateHistory");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfString");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfTransactionalInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TransactionalInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TransactionalInfo");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TransactionalInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariable");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.Variable[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariableDisplayName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.VariableDisplayName[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableDisplayName");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableDisplayName");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariableUpdate");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.VariableUpdate[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableUpdate");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableUpdate");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariableValueInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.VariableValueInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableValueInfo");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableValueInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfVariableWithCategory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.VariableWithCategory[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableWithCategory");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableWithCategory");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfWorkersInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.WorkersInfo[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkersInfo");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkersInfo");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfWorkQActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.WorkQActivity[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQActivity");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQActivity");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrayOfWorkqueueOverviewData");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.WorkqueueOverviewData[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkqueueOverviewData");
            qName2 = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkqueueOverviewData");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CategoryDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CategoryDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CNJActivityDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CNJActivityDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobSyncDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.CreateNewJobSyncDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessList");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.EmbeddedProcessList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EngineDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.EngineDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Events");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.Events.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedJobDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobDetails2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedJobDetails2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobs");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedJobs.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedJobs2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedJobs2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedMetaFieldDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedMetaFieldDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedWorkQActivity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedWorkQActivity2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQActivity3");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedWorkQActivity3.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedWorkQueue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue2");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedWorkQueue2.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue3");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedWorkQueue3.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ExtendedWorkQueue4");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ExtendedWorkQueue4.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterConfig");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.FieldFilterConfig.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "FieldFilterInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.FieldFilterInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobProperties.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties3");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobProperties3.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties4");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobProperties4.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobProperties6");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobProperties6.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobScheduleDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobScheduleDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobScheduleInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobScheduleInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobVarHistoryDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.JobVarHistoryDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.LogonDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MapVersionDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.MapVersionDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Milestones");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.Milestones.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MinJobDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.MinJobDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MTSResourceDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.MTSResourceDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Notes");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.Notes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PendingActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.PendingActivity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapData");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ProcessMapData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ProcessMapDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProgressDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ProgressDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RaiseEventVars");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RaiseEventVars.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceDetail");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ResourceDetail.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RoleInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RoleMember");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.RoleMember.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SchedulePageDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.SchedulePageDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SchedulePageInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.SchedulePageInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScheduleVariable");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.ScheduleVariable.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StateHistory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.StateHistory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SubJobDetails");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.SubJobDetails.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TimePeriod");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TimePeriod.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TransactionalInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.TransactionalInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.Variable.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }
    private void addBindings4() {
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
            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableDisplayName");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.VariableDisplayName.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableUpdate");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.VariableUpdate.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableValueInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.VariableValueInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariableWithCategory");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.VariableWithCategory.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkersInfo");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.WorkersInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQActivity");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.WorkQActivity.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueue");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.WorkQueue.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkqueueOverview");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.WorkqueueOverview.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkqueueOverviewData");
            cachedSerQNames.add(qName);
            cls = com.ecs.stellent.spp.WorkqueueOverviewData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

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

    public java.lang.String createNewJob(java.lang.String sessionId, java.lang.String processId, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJob");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.PendingActivity takePendingActivity(java.lang.String sessionId, java.lang.String jobId, short EPCount, short nodeId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/TakePendingActivity");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakePendingActivity"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(EPCount), new java.lang.Short(nodeId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.PendingActivity) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.PendingActivity) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.PendingActivity.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.PendingActivity takePendingActivity4(java.lang.String sessionId, java.lang.String jobId, short EPCount, short nodeId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/TakePendingActivity4");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakePendingActivity4"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(EPCount), new java.lang.Short(nodeId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.PendingActivity) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.PendingActivity) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.PendingActivity.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String getSubJobID(java.lang.String sessionId, java.lang.String jobId, short embeddedProcessCount) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetSubJobID");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetSubJobID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(embeddedProcessCount)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.LogonDetails logon(java.lang.String logonName, short logonType, boolean unConditional) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/Logon");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Logon"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {logonName, new java.lang.Short(logonType), new java.lang.Boolean(unConditional)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.LogonDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.LogonDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.LogonDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.LogonDetails logonUsingPassword(java.lang.String logonName, java.lang.String logonPassword, short logonType, boolean unConditional) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/LogonUsingPassword");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogonUsingPassword"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {logonName, logonPassword, new java.lang.Short(logonType), new java.lang.Boolean(unConditional)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.LogonDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.LogonDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.LogonDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createNewJobUsingMapName(java.lang.String sessionId, java.lang.String processName, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobUsingMapName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingMapName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processName, initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createNewCase(java.lang.String sessionId, java.lang.String processId, java.lang.String caseReference, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewCase");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewCase"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, caseReference, initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createNewCaseUsingMapName(java.lang.String sessionId, java.lang.String processName, java.lang.String caseReference, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewCaseUsingMapName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewCaseUsingMapName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processName, caseReference, initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createJobInCase(java.lang.String sessionId, java.lang.String processId, java.lang.String caseJobId, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateJobInCase");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateJobInCase"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, caseJobId, initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createJobInCaseUsingMapName(java.lang.String sessionId, java.lang.String processName, java.lang.String caseJobId, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateJobInCaseUsingMapName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateJobInCaseUsingMapName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processName, caseJobId, initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createJobInCaseUsingRef(java.lang.String sessionId, java.lang.String processId, java.lang.String caseReference, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateJobInCaseUsingRef");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateJobInCaseUsingRef"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, caseReference, initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createJobInCaseUsingRefMapName(java.lang.String sessionId, java.lang.String processName, java.lang.String caseReference, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateJobInCaseUsingRefMapName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateJobInCaseUsingRefMapName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processName, caseReference, initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProgressDetails createNewJobAndProgress(java.lang.String sessionId, java.lang.String processId, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobAndProgress");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgress"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProgressDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProgressDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProgressDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressUsingName(java.lang.String sessionId, java.lang.String processName, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobAndProgressUsingName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgressUsingName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processName, initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProgressDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProgressDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProgressDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void logOff(java.lang.String sessionId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/LogOff");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LogOff"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void activityComplete(java.lang.String sessionId, java.lang.String jobId, com.ecs.stellent.spp.Variable[] outputVariables, short embeddedProcessCount, short nodeId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ActivityComplete");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityComplete"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, outputVariables, new java.lang.Short(embeddedProcessCount), new java.lang.Short(nodeId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void saveActivity(java.lang.String sessionId, java.lang.String jobId, short nodeId, short embeddedProcessCount, com.ecs.stellent.spp.Variable[] inputVariables, com.ecs.stellent.spp.Variable[] outputVariables) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/SaveActivity");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "SaveActivity"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(nodeId), new java.lang.Short(embeddedProcessCount), inputVariables, outputVariables});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProgressDetails activityCompleteAndProgress(java.lang.String sessionId, java.lang.String jobId, com.ecs.stellent.spp.Variable[] outputVariables, short embeddedProcessCount, short nodeId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ActivityCompleteAndProgress");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityCompleteAndProgress"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, outputVariables, new java.lang.Short(embeddedProcessCount), new java.lang.Short(nodeId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProgressDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProgressDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProgressDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void pendingActivityComplete(java.lang.String sessionId, java.lang.String jobId, com.ecs.stellent.spp.Variable[] outputVariables, short embeddedProcessCount, short nodeId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[19]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/PendingActivityComplete");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PendingActivityComplete"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, outputVariables, new java.lang.Short(embeddedProcessCount), new java.lang.Short(nodeId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void pendingActivityCompleteUsingActivityName(java.lang.String sessionId, java.lang.String jobId, com.ecs.stellent.spp.Variable[] outputVariables, java.lang.String activityName) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[20]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/PendingActivityCompleteUsingActivityName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PendingActivityCompleteUsingActivityName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, outputVariables, activityName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.JobProperties getJobProps(java.lang.String sessionId, java.lang.String jobId, short jobDatabase) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[21]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobProps");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobProps"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(jobDatabase)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.JobProperties) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.JobProperties) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.JobProperties.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.JobProperties3 getJobProps3(java.lang.String sessionId, java.lang.String jobId, short jobDatabase) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[22]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobProps3");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobProps3"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(jobDatabase)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.JobProperties3) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.JobProperties3) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.JobProperties3.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.JobProperties4 getJobProps4(java.lang.String sessionId, java.lang.String jobId, short jobDatabase) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[23]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobProps4");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobProps4"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(jobDatabase)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.JobProperties4) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.JobProperties4) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.JobProperties4.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.JobProperties6 getJobProps6(java.lang.String sessionId, java.lang.String jobId, boolean associatedJobs) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[24]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobProps6");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobProps6"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Boolean(associatedJobs)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.JobProperties6) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.JobProperties6) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.JobProperties6.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void cancelActivity(java.lang.String sessionId, short nodeId, short embeddedProcessCount, java.lang.String jobId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[25]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CancelActivity");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CancelActivity"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Short(nodeId), new java.lang.Short(embeddedProcessCount), jobId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void addNote(java.lang.String sessionId, java.lang.String jobId, java.lang.String note) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[26]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/AddNote");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AddNote"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, note});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void updateNote(java.lang.String sessionId, java.lang.String jobId, java.util.Calendar dateNoteEntered, java.lang.String note) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[27]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/UpdateNote");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UpdateNote"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, dateNoteEntered, note});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void updateJobOwner(java.lang.String sessionId, java.lang.String jobId, java.lang.String jobOwnerID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[28]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/UpdateJobOwner");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UpdateJobOwner"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, jobOwnerID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void updateJobOwners(java.lang.String sessionId, java.lang.String[] jobIds, java.lang.String jobOwnerID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[29]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/UpdateJobOwners");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UpdateJobOwners"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobIds, jobOwnerID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void changeJobState(java.lang.String sessionId, java.lang.String jobId, java.lang.String state) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[30]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ChangeJobState");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ChangeJobState"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, state});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void deleteNote(java.lang.String sessionId, java.lang.String jobId, java.util.Calendar dateNoteEntered) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[31]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/DeleteNote");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DeleteNote"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, dateNoteEntered});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivityHistory[] getActivitiesCompletedByResource(java.lang.String sessionId, java.lang.String resourceId, short jobDatabase) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[32]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetActivitiesCompletedByResource");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetActivitiesCompletedByResource"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, resourceId, new java.lang.Short(jobDatabase)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivityHistory[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivityHistory[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivityHistory[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void performAction(java.lang.String sessionId, java.lang.String jobId, short actionType) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[33]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/PerformAction");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PerformAction"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(actionType)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void performActions(java.lang.String sessionId, short actionType, java.lang.String[] jobIds) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[34]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/PerformActions");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PerformActions"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Short(actionType), jobIds});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public short getUserCount(java.lang.String sessionId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[35]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetUserCount");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetUserCount"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Short) _resp).shortValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Short) org.apache.axis.utils.JavaUtils.convert(_resp, short.class)).shortValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean accessPermitted(java.lang.String sessionId, java.lang.String jobId, short action) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[36]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/AccessPermitted");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AccessPermitted"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(action)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.Variable[] getInitVarList(java.lang.String sessionId, java.lang.String processId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[37]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetInitVarList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetInitVarList"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.Variable[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.Variable[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.Variable[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String[] getTrustedDomains() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[38]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetTrustedDomains");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetTrustedDomains"));

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
  throw axisFaultException;
}
    }

    public void declineOfferedActivity(java.lang.String sessionId, java.lang.String jobId, short processId, short nodeId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[39]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/DeclineOfferedActivity");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DeclineOfferedActivity"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(processId), new java.lang.Short(nodeId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivityInfo takeAnyActivity(java.lang.String sessionId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[40]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/TakeAnyActivity");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeAnyActivity"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivityInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivityInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivityInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivityInfo takeAnyActivity4(java.lang.String sessionId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[41]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/TakeAnyActivity4");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeAnyActivity4"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivityInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivityInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivityInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.MTSResourceDetails[] getResourcesWithSkillAndSecurity(java.lang.String sessionId, boolean useSkillLevel, short skillLevel, boolean useSecurityLevel, short securityLevel, boolean useGroupResourceId, java.lang.String groupResourceId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[42]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetResourcesWithSkillAndSecurity");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetResourcesWithSkillAndSecurity"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Boolean(useSkillLevel), new java.lang.Short(skillLevel), new java.lang.Boolean(useSecurityLevel), new java.lang.Short(securityLevel), new java.lang.Boolean(useGroupResourceId), groupResourceId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.MTSResourceDetails[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.MTSResourceDetails[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.MTSResourceDetails[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.MTSResourceDetails[] getResourcesWithSkillAndSecurityUsingGroupName(java.lang.String sessionId, boolean useSkillLevel, short skillLevel, boolean useSecurityLevel, short securityLevel, java.lang.String groupResourceName) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[43]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetResourcesWithSkillAndSecurityUsingGroupName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetResourcesWithSkillAndSecurityUsingGroupName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Boolean(useSkillLevel), new java.lang.Short(skillLevel), new java.lang.Boolean(useSecurityLevel), new java.lang.Short(securityLevel), groupResourceName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.MTSResourceDetails[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.MTSResourceDetails[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.MTSResourceDetails[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public int getNumberOfActivitiesAssignedToResource(java.lang.String sessionId, java.lang.String resourceId, boolean useActivityName, java.lang.String activityName, boolean useProcessId, java.lang.String processId, short resourcesIncluded) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[44]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetNumberOfActivitiesAssignedToResource");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetNumberOfActivitiesAssignedToResource"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, resourceId, new java.lang.Boolean(useActivityName), activityName, new java.lang.Boolean(useProcessId), processId, new java.lang.Short(resourcesIncluded)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Integer) _resp).intValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public int getNumberOfActivitiesAssignedToResourceUsingResName(java.lang.String sessionId, java.lang.String resourceName, boolean useActivityName, java.lang.String activityName, boolean useProcessId, java.lang.String processId, short resourcesIncluded) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[45]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetNumberOfActivitiesAssignedToResourceUsingResName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetNumberOfActivitiesAssignedToResourceUsingResName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, resourceName, new java.lang.Boolean(useActivityName), activityName, new java.lang.Boolean(useProcessId), processId, new java.lang.Short(resourcesIncluded)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Integer) _resp).intValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.MinJobDetails getMinJobDetails(java.lang.String sessionId, short jobDatabase, java.lang.String jobId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[46]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetMinJobDetails");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMinJobDetails"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Short(jobDatabase), jobId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.MinJobDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.MinJobDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.MinJobDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.CategoryDetails[] retrieveAllCategories(java.lang.String sessionId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[47]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/RetrieveAllCategories");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RetrieveAllCategories"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.CategoryDetails[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.CategoryDetails[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.CategoryDetails[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProcessMapDetails[] getMapsForUser(java.lang.String sessionId, short accessType, boolean allVersions) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[48]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetMapsForUser");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMapsForUser"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Short(accessType), new java.lang.Boolean(allVersions)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProcessMapDetails[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProcessMapDetails[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProcessMapDetails[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProcessMapDetails[] getMapsForUserUsingCategory(java.lang.String sessionId, short accessType, boolean allVersions, java.lang.String categoryId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[49]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetMapsForUserUsingCategory");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMapsForUserUsingCategory"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Short(accessType), new java.lang.Boolean(allVersions), categoryId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProcessMapDetails[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProcessMapDetails[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProcessMapDetails[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProcessMapData[] getMinMapsForUser(java.lang.String sessionId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[50]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetMinMapsForUser");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMinMapsForUser"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProcessMapData[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProcessMapData[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProcessMapData[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProcessMapData[] getMinMapsForUserUsingCategory(java.lang.String sessionId, java.lang.String categoryId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[51]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetMinMapsForUserUsingCategory");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMinMapsForUserUsingCategory"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, categoryId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProcessMapData[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProcessMapData[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProcessMapData[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProcessMapData[] getMinMapsForUserUsingCategory2(java.lang.String sessionId, java.lang.String categoryId, short processType) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[52]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetMinMapsForUserUsingCategory2");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMinMapsForUserUsingCategory2"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, categoryId, new java.lang.Short(processType)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProcessMapData[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProcessMapData[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProcessMapData[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void restartJobUsingNodeName(java.lang.String sessionId, java.lang.String jobId, java.lang.String nodeName, com.ecs.stellent.spp.EmbeddedProcessList[] EPList, short restartType) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[53]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/RestartJobUsingNodeName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RestartJobUsingNodeName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, nodeName, EPList, new java.lang.Short(restartType)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void restartJob(java.lang.String sessionId, java.lang.String jobId, short nodeId, com.ecs.stellent.spp.EmbeddedProcessList[] EPList, short restartType) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[54]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/RestartJob");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RestartJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(nodeId), EPList, new java.lang.Short(restartType)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.VariableWithCategory[] getVariablesWithCategory(java.lang.String sessionId, java.lang.String ownerId, double version) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[55]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetVariablesWithCategory");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetVariablesWithCategory"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, ownerId, new java.lang.Double(version)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.VariableWithCategory[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.VariableWithCategory[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.VariableWithCategory[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivityInfo takeActivityFromJob(java.lang.String sessionId, java.lang.String jobId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[56]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/TakeActivityFromJob");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityFromJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivityInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivityInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivityInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivityInfo takeActivityUsingProcess(java.lang.String sessionId, java.lang.String jobId, java.lang.String processId, short nodeId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[57]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/TakeActivityUsingProcess");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityUsingProcess"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, processId, new java.lang.Short(nodeId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivityInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivityInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivityInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivityInfo takeActivityUsingActivityName(java.lang.String sessionId, java.lang.String jobId, java.lang.String activityName) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[58]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/TakeActivityUsingActivityName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityUsingActivityName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, activityName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivityInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivityInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivityInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivityInfo takeActivityUsingActivityNameAndProcess(java.lang.String sessionId, java.lang.String jobId, java.lang.String processId, java.lang.String activityName) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[59]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/TakeActivityUsingActivityNameAndProcess");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TakeActivityUsingActivityNameAndProcess"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, processId, activityName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivityInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivityInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivityInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivitiesForStatus[] getActivitiesInJobWithStatus(java.lang.String sessionId, java.lang.String jobId, short status) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[60]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetActivitiesInJobWithStatus");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetActivitiesInJobWithStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(status)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivitiesForStatus[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivitiesForStatus[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivitiesForStatus[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivitiesForStatus2[] getActivitiesInJobWithStatus2(java.lang.String sessionId, java.lang.String jobId, short status) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[61]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetActivitiesInJobWithStatus2");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetActivitiesInJobWithStatus2"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(status)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivitiesForStatus2[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivitiesForStatus2[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivitiesForStatus2[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivitiesForStatus3[] getActivitiesInJobWithStatus3(java.lang.String sessionId, java.lang.String jobId, short status) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[62]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetActivitiesInJobWithStatus3");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetActivitiesInJobWithStatus3"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(status)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivitiesForStatus3[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivitiesForStatus3[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivitiesForStatus3[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void processAutomaticActivityFailure(java.lang.String exceptionCode, java.lang.String jobId, short nodeId, short embeddedProcessCount, short autoActivityType, boolean cancelActivity) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[63]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ProcessAutomaticActivityFailure");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessAutomaticActivityFailure"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {exceptionCode, jobId, new java.lang.Short(nodeId), new java.lang.Short(embeddedProcessCount), new java.lang.Short(autoActivityType), new java.lang.Boolean(cancelActivity)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.JobDetails[] getJobsUsingSearchCriteria(java.lang.String sessionId, java.util.Calendar startTimeFrom, boolean useStartTimeFrom, java.util.Calendar startTimeTo, boolean useStartTimeTo, java.util.Calendar finishTimeFrom, boolean useFinishTimeFrom, java.util.Calendar finishTimeTo, boolean useFinishTimeTo, java.lang.String creatorId, boolean useCreatorId, boolean workerResource, short status, java.lang.String processId, boolean useProcessId, double version, boolean useVersion, java.lang.String originServerId, boolean useOriginServerId, int maxNumToRetrieve, com.ecs.stellent.spp.Variable[] procTemplateVariables, java.lang.String categoryId, boolean useCategoryId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[64]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobsUsingSearchCriteria");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteria"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, startTimeFrom, new java.lang.Boolean(useStartTimeFrom), startTimeTo, new java.lang.Boolean(useStartTimeTo), finishTimeFrom, new java.lang.Boolean(useFinishTimeFrom), finishTimeTo, new java.lang.Boolean(useFinishTimeTo), creatorId, new java.lang.Boolean(useCreatorId), new java.lang.Boolean(workerResource), new java.lang.Short(status), processId, new java.lang.Boolean(useProcessId), new java.lang.Double(version), new java.lang.Boolean(useVersion), originServerId, new java.lang.Boolean(useOriginServerId), new java.lang.Integer(maxNumToRetrieve), procTemplateVariables, categoryId, new java.lang.Boolean(useCategoryId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.JobDetails[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.JobDetails[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.JobDetails[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ExtendedJobs getExtendedJobsUsingSearchCriteria(java.lang.String sessionId, java.util.Calendar startTimeFrom, boolean useStartTimeFrom, java.util.Calendar startTimeTo, boolean useStartTimeTo, java.util.Calendar finishTimeFrom, boolean useFinishTimeFrom, java.util.Calendar finishTimeTo, boolean useFinishTimeTo, java.lang.String creatorId, boolean useCreatorId, boolean workerResource, short status, java.lang.String processId, boolean useProcessId, double version, boolean useVersion, java.lang.String originServerId, boolean useOriginServerId, int maxNumToRetrieve, java.lang.String categoryId, boolean useCategoryId, short priority, boolean usePriority, java.util.Calendar dueDateFrom, boolean useDueDateFrom, java.util.Calendar dueDateTo, boolean useDueDateTo, java.lang.String workQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[65]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetExtendedJobsUsingSearchCriteria");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedJobsUsingSearchCriteria"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, startTimeFrom, new java.lang.Boolean(useStartTimeFrom), startTimeTo, new java.lang.Boolean(useStartTimeTo), finishTimeFrom, new java.lang.Boolean(useFinishTimeFrom), finishTimeTo, new java.lang.Boolean(useFinishTimeTo), creatorId, new java.lang.Boolean(useCreatorId), new java.lang.Boolean(workerResource), new java.lang.Short(status), processId, new java.lang.Boolean(useProcessId), new java.lang.Double(version), new java.lang.Boolean(useVersion), originServerId, new java.lang.Boolean(useOriginServerId), new java.lang.Integer(maxNumToRetrieve), categoryId, new java.lang.Boolean(useCategoryId), new java.lang.Short(priority), new java.lang.Boolean(usePriority), dueDateFrom, new java.lang.Boolean(useDueDateFrom), dueDateTo, new java.lang.Boolean(useDueDateTo), workQueueDefnName, fieldFilter});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ExtendedJobs) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ExtendedJobs) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ExtendedJobs.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ExtendedJobs getJobsUsingSearchCriteria4(java.lang.String sessionId, java.util.Calendar startTimeFrom, boolean useStartTimeFrom, java.util.Calendar startTimeTo, boolean useStartTimeTo, java.util.Calendar finishTimeFrom, boolean useFinishTimeFrom, java.util.Calendar finishTimeTo, boolean useFinishTimeTo, java.lang.String creatorId, boolean useCreatorId, boolean workerResource, short status, java.lang.String processId, boolean useProcessId, double version, boolean useVersion, java.lang.String originServerId, boolean useOriginServerId, int maxNumToRetrieve, java.lang.String categoryId, boolean useCategoryId, short priority, boolean usePriority, java.util.Calendar dueDateFrom, boolean useDueDateFrom, java.util.Calendar dueDateTo, boolean useDueDateTo, java.lang.String workQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter, boolean useJobOwner, java.lang.String jobOwnerID, boolean useJobState, java.lang.String jobStateName) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[66]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobsUsingSearchCriteria4");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteria4"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, startTimeFrom, new java.lang.Boolean(useStartTimeFrom), startTimeTo, new java.lang.Boolean(useStartTimeTo), finishTimeFrom, new java.lang.Boolean(useFinishTimeFrom), finishTimeTo, new java.lang.Boolean(useFinishTimeTo), creatorId, new java.lang.Boolean(useCreatorId), new java.lang.Boolean(workerResource), new java.lang.Short(status), processId, new java.lang.Boolean(useProcessId), new java.lang.Double(version), new java.lang.Boolean(useVersion), originServerId, new java.lang.Boolean(useOriginServerId), new java.lang.Integer(maxNumToRetrieve), categoryId, new java.lang.Boolean(useCategoryId), new java.lang.Short(priority), new java.lang.Boolean(usePriority), dueDateFrom, new java.lang.Boolean(useDueDateFrom), dueDateTo, new java.lang.Boolean(useDueDateTo), workQueueDefnName, fieldFilter, new java.lang.Boolean(useJobOwner), jobOwnerID, new java.lang.Boolean(useJobState), jobStateName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ExtendedJobs) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ExtendedJobs) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ExtendedJobs.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void updateJobOwnerUsingSearchCriteria(java.lang.String sessionId, java.util.Calendar startTimeFrom, boolean useStartTimeFrom, java.util.Calendar startTimeTo, boolean useStartTimeTo, java.util.Calendar finishTimeFrom, boolean useFinishTimeFrom, java.util.Calendar finishTimeTo, boolean useFinishTimeTo, java.lang.String creatorId, boolean useCreatorId, boolean workerResource, short status, java.lang.String processId, boolean useProcessId, double version, boolean useVersion, java.lang.String originServerId, boolean useOriginServerId, int maxNumToRetrieve, java.lang.String categoryId, boolean useCategoryId, short priority, boolean usePriority, java.util.Calendar dueDateFrom, boolean useDueDateFrom, java.util.Calendar dueDateTo, boolean useDueDateTo, java.lang.String workQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter, boolean useJobOwner, java.lang.String sJobOwnerID, java.lang.String newJobOwnerID, boolean useJobState, java.lang.String jobStateName) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[67]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/UpdateJobOwnerUsingSearchCriteria");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UpdateJobOwnerUsingSearchCriteria"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, startTimeFrom, new java.lang.Boolean(useStartTimeFrom), startTimeTo, new java.lang.Boolean(useStartTimeTo), finishTimeFrom, new java.lang.Boolean(useFinishTimeFrom), finishTimeTo, new java.lang.Boolean(useFinishTimeTo), creatorId, new java.lang.Boolean(useCreatorId), new java.lang.Boolean(workerResource), new java.lang.Short(status), processId, new java.lang.Boolean(useProcessId), new java.lang.Double(version), new java.lang.Boolean(useVersion), originServerId, new java.lang.Boolean(useOriginServerId), new java.lang.Integer(maxNumToRetrieve), categoryId, new java.lang.Boolean(useCategoryId), new java.lang.Short(priority), new java.lang.Boolean(usePriority), dueDateFrom, new java.lang.Boolean(useDueDateFrom), dueDateTo, new java.lang.Boolean(useDueDateTo), workQueueDefnName, fieldFilter, new java.lang.Boolean(useJobOwner), sJobOwnerID, newJobOwnerID, new java.lang.Boolean(useJobState), jobStateName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public int getNumberOfActivitiesUsingCriteria(java.lang.String sessionId, boolean usingJobId, java.lang.String jobId, boolean usingStatus, short status) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[68]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetNumberOfActivitiesUsingCriteria");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetNumberOfActivitiesUsingCriteria"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Boolean(usingJobId), jobId, new java.lang.Boolean(usingStatus), new java.lang.Short(status)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Integer) _resp).intValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.EngineDetails[] getEngineList(java.lang.String sessionId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[69]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetEngineList");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetEngineList"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.EngineDetails[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.EngineDetails[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.EngineDetails[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.WorkersInfo[] getWorkersForServer(java.lang.String sessionId, java.lang.String serverId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[70]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetWorkersForServer");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkersForServer"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, serverId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.WorkersInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.WorkersInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.WorkersInfo[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.WorkersInfo[] getWorkerGroupsForServer(java.lang.String sessionId, java.lang.String serverId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[71]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetWorkerGroupsForServer");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkerGroupsForServer"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, serverId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.WorkersInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.WorkersInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.WorkersInfo[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.WorkersInfo[] getMembersInGroupForServer(java.lang.String sessionId, java.lang.String resourceId, java.lang.String serverId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[72]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetMembersInGroupForServer");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMembersInGroupForServer"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, resourceId, serverId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.WorkersInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.WorkersInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.WorkersInfo[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.Variable[] getVariables(java.lang.String sessionId, java.lang.String ownerId, double version) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[73]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetVariables");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetVariables"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, ownerId, new java.lang.Double(version)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.Variable[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.Variable[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.Variable[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String[] recoverJobs(java.lang.String sessionId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[74]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/RecoverJobs");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RecoverJobs"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId});

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
  throw axisFaultException;
}
    }

    public void recoverJob(java.lang.String sessionId, java.lang.String jobId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[75]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/RecoverJob");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RecoverJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean verifyNTUserandPassword(java.lang.String userName, java.lang.String domainName, java.lang.String password) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[76]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/VerifyNTUserandPassword");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VerifyNTUserandPassword"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userName, domainName, password});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String getServerID(java.lang.String sessionId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[77]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetServerID");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetServerID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean validateSession(java.lang.String sessionId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[78]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ValidateSession");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ValidateSession"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public boolean validateSession2(java.lang.String sessionId, javax.xml.rpc.holders.StringHolder resourceId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[79]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ValidateSession2");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ValidateSession2"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, resourceId.value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                resourceId.value = (java.lang.String) _output.get(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId"));
            } catch (java.lang.Exception _exception) {
                resourceId.value = (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceId")), java.lang.String.class);
            }
            try {
                return ((java.lang.Boolean) _resp).booleanValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(_resp, boolean.class)).booleanValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void updateVariables(java.lang.String sessionId, java.lang.String ownerID, com.ecs.stellent.spp.VariableUpdate[] updatedVariables) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[80]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/UpdateVariables");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UpdateVariables"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, ownerID, updatedVariables});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String getXMLMapControlData(java.lang.String sessionId, java.lang.String jobId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[81]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetXMLMapControlData");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetXMLMapControlData"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String getEmbeddedXMLMapControlData(java.lang.String sessionId, java.lang.String processId, double version, boolean loadLatestVersion) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[82]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetEmbeddedXMLMapControlData");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetEmbeddedXMLMapControlData"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, new java.lang.Double(version), new java.lang.Boolean(loadLatestVersion)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String getXMLMapControlJobDataOnly(java.lang.String sessionId, java.lang.String jobId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[83]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetXMLMapControlJobDataOnly");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetXMLMapControlJobDataOnly"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.WorkQueue getWorkQueue(java.lang.String sessionId, int numberOfActivities, boolean useStartDueDate, java.util.Calendar startDateDue, short activityTypeFilter) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[84]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetWorkQueue");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkQueue"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(numberOfActivities), new java.lang.Boolean(useStartDueDate), startDateDue, new java.lang.Short(activityTypeFilter)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.WorkQueue) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.WorkQueue) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.WorkQueue.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.WorkQueue getWorkQueueUsingActivityName(java.lang.String sessionId, int numberOfActivities, boolean useStartDueDate, java.util.Calendar startDateDue, short activityTypeFilter, java.lang.String activityName) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[85]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetWorkQueueUsingActivityName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkQueueUsingActivityName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(numberOfActivities), new java.lang.Boolean(useStartDueDate), startDateDue, new java.lang.Short(activityTypeFilter), activityName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.WorkQueue) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.WorkQueue) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.WorkQueue.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.WorkQueue getIndividualWorkQueue(java.lang.String sessionId, int numberOfActivitiesRq, boolean useStartDueDate, java.util.Calendar startDueDate, short activityTypeFilter, boolean useActivityName, java.lang.String activityName) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[86]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetIndividualWorkQueue");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetIndividualWorkQueue"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(numberOfActivitiesRq), new java.lang.Boolean(useStartDueDate), startDueDate, new java.lang.Short(activityTypeFilter), new java.lang.Boolean(useActivityName), activityName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.WorkQueue) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.WorkQueue) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.WorkQueue.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.JobScheduleDetails addSchedule(java.lang.String sessionId, java.lang.String scheduleName, java.lang.String businessProcessId, int timeSpan, short timeSpanPeriod, short noOfOccurances, short noOfJobsToCreate, boolean useStartDate, java.util.Calendar startDate, boolean useEndDate, java.util.Calendar endDate, com.ecs.stellent.spp.ScheduleVariable[] initialisationParams, boolean monthly, boolean useWeekDays, short daysOfWeek, boolean oneActiveAtTime, boolean scheduleActive, boolean useBusinessCalendar) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[87]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/AddSchedule");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AddSchedule"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, scheduleName, businessProcessId, new java.lang.Integer(timeSpan), new java.lang.Short(timeSpanPeriod), new java.lang.Short(noOfOccurances), new java.lang.Short(noOfJobsToCreate), new java.lang.Boolean(useStartDate), startDate, new java.lang.Boolean(useEndDate), endDate, initialisationParams, new java.lang.Boolean(monthly), new java.lang.Boolean(useWeekDays), new java.lang.Short(daysOfWeek), new java.lang.Boolean(oneActiveAtTime), new java.lang.Boolean(scheduleActive), new java.lang.Boolean(useBusinessCalendar)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.JobScheduleDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.JobScheduleDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.JobScheduleDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.util.Calendar modifySchedule(java.lang.String sessionId, int scheduleId, java.lang.String businessProcessId, java.lang.String scheduleName, int timeSpan, short timeSpanPeriod, short noOfOccurances, short noOfJobsToCreate, boolean scheduleActive, boolean useStartDate, java.util.Calendar startDate, boolean useEndDate, java.util.Calendar endDate, com.ecs.stellent.spp.ScheduleVariable[] initialisationParams, boolean monthly, boolean useWeekDays, short daysOfWeek, boolean oneActiveAtTime, boolean useBusinessCalendar) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[88]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ModifySchedule");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ModifySchedule"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(scheduleId), businessProcessId, scheduleName, new java.lang.Integer(timeSpan), new java.lang.Short(timeSpanPeriod), new java.lang.Short(noOfOccurances), new java.lang.Short(noOfJobsToCreate), new java.lang.Boolean(scheduleActive), new java.lang.Boolean(useStartDate), startDate, new java.lang.Boolean(useEndDate), endDate, initialisationParams, new java.lang.Boolean(monthly), new java.lang.Boolean(useWeekDays), new java.lang.Short(daysOfWeek), new java.lang.Boolean(oneActiveAtTime), new java.lang.Boolean(useBusinessCalendar)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.util.Calendar) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.util.Calendar) org.apache.axis.utils.JavaUtils.convert(_resp, java.util.Calendar.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void deleteSchedule(java.lang.String sessionId, int scheduleId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[89]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/DeleteSchedule");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DeleteSchedule"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(scheduleId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.JobScheduleInfo retrieveSchedule(java.lang.String sessionId, int scheduleId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[90]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/RetrieveSchedule");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RetrieveSchedule"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(scheduleId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.JobScheduleInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.JobScheduleInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.JobScheduleInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.SchedulePageInfo retrieveSchedulePage(java.lang.String sessionId, int scheduleId, short pageSize) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[91]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/RetrieveSchedulePage");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RetrieveSchedulePage"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(scheduleId), new java.lang.Short(pageSize)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.SchedulePageInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.SchedulePageInfo) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.SchedulePageInfo.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.VariableValueInfo[] getJobVariablesValues(java.lang.String sessionId, java.lang.String jobId, java.lang.String[] varIds) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[92]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobVariablesValues");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobVariablesValues"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, varIds});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.VariableValueInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.VariableValueInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.VariableValueInfo[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.VariableValueInfo[] getServerVariablesValues(java.lang.String sessionId, java.lang.String[] varIds) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[93]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetServerVariablesValues");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetServerVariablesValues"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, varIds});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.VariableValueInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.VariableValueInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.VariableValueInfo[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.CreateNewJobSyncDetails createNewJobSync(java.lang.String sessionId, java.lang.String processId, com.ecs.stellent.spp.Variable[] initParams, java.lang.String[] returnParamsIds) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[94]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobSync");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobSync"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, initParams, returnParamsIds});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.CreateNewJobSyncDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.CreateNewJobSyncDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.CreateNewJobSyncDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.Object[][] getJaggedArray() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[95]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJaggedArray");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJaggedArray"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.Object[][]) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.Object[][]) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.Object[][].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ExtendedWorkQueue getExtendedWorkQueue(java.lang.String sessionId, int numberOfActivities, boolean useStartDueDate, java.util.Calendar startDateDue, short activityTypeFilter, java.lang.String wrkQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter, boolean returnJobIds, short usePriority, short prioritiesToRetrieve, boolean useLastDueDate, java.util.Calendar lastDueDate) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[96]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetExtendedWorkQueue");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueue"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(numberOfActivities), new java.lang.Boolean(useStartDueDate), startDateDue, new java.lang.Short(activityTypeFilter), wrkQueueDefnName, fieldFilter, new java.lang.Boolean(returnJobIds), new java.lang.Short(usePriority), new java.lang.Short(prioritiesToRetrieve), new java.lang.Boolean(useLastDueDate), lastDueDate});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ExtendedWorkQueue) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ExtendedWorkQueue) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ExtendedWorkQueue.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ExtendedWorkQueue3 getExtendedWorkQueue3(java.lang.String sessionId, int numberOfActivities, boolean useStartDueDate, java.util.Calendar startDueDate, long activityTypeFilter, java.lang.String wrkQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter, boolean returnJobIds, short usePriority, short prioritiesToRetrieve, java.lang.String activityName, java.lang.String resourceID, boolean combinedWorkqueue, boolean bUseJobRAGStatus, short jobRAGStatus, boolean bUseJobState, java.lang.String jobState, short pageDirection) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[97]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetExtendedWorkQueue3");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueue3"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(numberOfActivities), new java.lang.Boolean(useStartDueDate), startDueDate, new java.lang.Long(activityTypeFilter), wrkQueueDefnName, fieldFilter, new java.lang.Boolean(returnJobIds), new java.lang.Short(usePriority), new java.lang.Short(prioritiesToRetrieve), activityName, resourceID, new java.lang.Boolean(combinedWorkqueue), new java.lang.Boolean(bUseJobRAGStatus), new java.lang.Short(jobRAGStatus), new java.lang.Boolean(bUseJobState), jobState, new java.lang.Short(pageDirection)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ExtendedWorkQueue3) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ExtendedWorkQueue3) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ExtendedWorkQueue3.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ExtendedWorkQueue2 getExtendedWorkQueue2(java.lang.String sessionId, int numberOfActivities, boolean useStartDueDate, java.util.Calendar startDueDate, short activityTypeFilter, java.lang.String wrkQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter, boolean returnJobIds, short usePriority, short prioritiesToRetrieve, java.lang.String activityName, java.lang.String resourceID, boolean combinedWorkqueue, short pageDirection) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[98]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetExtendedWorkQueue2");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueue2"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(numberOfActivities), new java.lang.Boolean(useStartDueDate), startDueDate, new java.lang.Short(activityTypeFilter), wrkQueueDefnName, fieldFilter, new java.lang.Boolean(returnJobIds), new java.lang.Short(usePriority), new java.lang.Short(prioritiesToRetrieve), activityName, resourceID, new java.lang.Boolean(combinedWorkqueue), new java.lang.Short(pageDirection)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ExtendedWorkQueue2) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ExtendedWorkQueue2) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ExtendedWorkQueue2.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ExtendedWorkQueue4 getExtendedWorkQueue4(java.lang.String sessionId, int numberOfActivities, boolean useStartDueDate, java.util.Calendar startDueDate, long activityTypeFilter, java.lang.String wrkQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter, boolean returnJobIds, short usePriority, short prioritiesToRetrieve, java.lang.String activityName, java.lang.String resourceID, boolean combinedWorkqueue, boolean bUseJobRAGStatus, short jobRAGStatus, boolean bUseJobState, java.lang.String jobState, short pageDirection, boolean bUseActivityRAGStatus, short activityRAGStatus) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[99]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetExtendedWorkQueue4");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueue4"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(numberOfActivities), new java.lang.Boolean(useStartDueDate), startDueDate, new java.lang.Long(activityTypeFilter), wrkQueueDefnName, fieldFilter, new java.lang.Boolean(returnJobIds), new java.lang.Short(usePriority), new java.lang.Short(prioritiesToRetrieve), activityName, resourceID, new java.lang.Boolean(combinedWorkqueue), new java.lang.Boolean(bUseJobRAGStatus), new java.lang.Short(jobRAGStatus), new java.lang.Boolean(bUseJobState), jobState, new java.lang.Short(pageDirection), new java.lang.Boolean(bUseActivityRAGStatus), new java.lang.Short(activityRAGStatus)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ExtendedWorkQueue4) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ExtendedWorkQueue4) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ExtendedWorkQueue4.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ExtendedWorkQueue getExtendedWorkQueueUsingActivityName(java.lang.String sessionId, int numberOfActivities, boolean useStartDueDate, java.util.Calendar startDateDue, short activityTypeFilter, java.lang.String activityName, java.lang.String wrkQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter, boolean returnJobIds, short usePriority, short prioritiesToRetrieve, boolean useLastDueDate, java.util.Calendar lastDueDate) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[100]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetExtendedWorkQueueUsingActivityName");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedWorkQueueUsingActivityName"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(numberOfActivities), new java.lang.Boolean(useStartDueDate), startDateDue, new java.lang.Short(activityTypeFilter), activityName, wrkQueueDefnName, fieldFilter, new java.lang.Boolean(returnJobIds), new java.lang.Short(usePriority), new java.lang.Short(prioritiesToRetrieve), new java.lang.Boolean(useLastDueDate), lastDueDate});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ExtendedWorkQueue) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ExtendedWorkQueue) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ExtendedWorkQueue.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ExtendedWorkQueue getExtendedIndividualWorkQueue(java.lang.String sessionId, int numberOfActivitiesRq, boolean useStartDueDate, java.util.Calendar startDueDate, short activityTypeFilter, boolean useActivityName, java.lang.String activityName, java.lang.String wrkQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter, boolean returnJobIds, short usePriority, short priorityToRetrieve, boolean useLastDueDate, java.util.Calendar lastDueDate) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[101]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetExtendedIndividualWorkQueue");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetExtendedIndividualWorkQueue"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Integer(numberOfActivitiesRq), new java.lang.Boolean(useStartDueDate), startDueDate, new java.lang.Short(activityTypeFilter), new java.lang.Boolean(useActivityName), activityName, wrkQueueDefnName, fieldFilter, new java.lang.Boolean(returnJobIds), new java.lang.Short(usePriority), new java.lang.Short(priorityToRetrieve), new java.lang.Boolean(useLastDueDate), lastDueDate});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ExtendedWorkQueue) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ExtendedWorkQueue) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ExtendedWorkQueue.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createNewJobUsingStartDate(java.lang.String sessionId, java.lang.String businessProcessId, java.util.Calendar startDate, com.ecs.stellent.spp.Variable[] initialisationParms) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[102]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobUsingStartDate");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingStartDate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, businessProcessId, startDate, initialisationParms});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createNewJobAndStartAt(java.lang.String sessionId, java.lang.String businessProcessId, java.lang.String businessProcessName, java.lang.String nodeName, com.ecs.stellent.spp.Variable[] initialisationParms) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[103]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobAndStartAt");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndStartAt"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, businessProcessId, businessProcessName, nodeName, initialisationParms});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void activityCompleteWithTimeAndCost(java.lang.String sessionId, java.lang.String jobId, com.ecs.stellent.spp.Variable[] OPVars, short embeddedProcessCount, short nodeId, java.util.Calendar startTime, double cost) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[104]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ActivityCompleteWithTimeAndCost");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityCompleteWithTimeAndCost"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, OPVars, new java.lang.Short(embeddedProcessCount), new java.lang.Short(nodeId), startTime, new java.lang.Double(cost)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createNewJobUsingVersion(java.lang.String sessionId, java.lang.String processId, double mapVersion, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[105]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobUsingVersion");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, new java.lang.Double(mapVersion), initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.CreateNewJobSyncDetails createNewJobSyncUsingVersion(java.lang.String sessionId, java.lang.String processId, double mapVersion, com.ecs.stellent.spp.Variable[] initParams, java.lang.String[] returnParamsIds) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[106]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobSyncUsingVersion");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobSyncUsingVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, new java.lang.Double(mapVersion), initParams, returnParamsIds});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.CreateNewJobSyncDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.CreateNewJobSyncDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.CreateNewJobSyncDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.MapVersionDetails[] getMapVersions(java.lang.String sessionId, java.lang.String processId, short mapStatus, short accessType) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[107]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetMapVersions");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMapVersions"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, new java.lang.Short(mapStatus), new java.lang.Short(accessType)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.MapVersionDetails[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.MapVersionDetails[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.MapVersionDetails[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.Variable[] getInitVarListUsingVersion(java.lang.String sessionId, java.lang.String processId, double mapVersion) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[108]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetInitVarListUsingVersion");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetInitVarListUsingVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, new java.lang.Double(mapVersion)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.Variable[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.Variable[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.Variable[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.VariableDisplayName[] getInitVarList3(java.lang.String sessionId, java.lang.String processId, double mapVersion) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[109]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetInitVarList3");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetInitVarList3"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, new java.lang.Double(mapVersion)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.VariableDisplayName[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.VariableDisplayName[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.VariableDisplayName[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressUsingVersion(java.lang.String sessionId, java.lang.String processId, double version, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[110]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobAndProgressUsingVersion");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgressUsingVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processId, new java.lang.Double(version), initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProgressDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProgressDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProgressDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProgressDetails createNewJobAndProgressUsingNameAndVersion(java.lang.String sessionId, java.lang.String processName, double version, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[111]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobAndProgressUsingNameAndVersion");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndProgressUsingNameAndVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processName, new java.lang.Double(version), initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProgressDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProgressDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProgressDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createNewJobUsingMapNameAndVersion(java.lang.String sessionId, java.lang.String processName, double version, com.ecs.stellent.spp.Variable[] initParams) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[112]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobUsingMapNameAndVersion");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingMapNameAndVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, processName, new java.lang.Double(version), initParams});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createNewJobUsingStartDateUsingVersion(java.lang.String sessionId, java.lang.String businessProcessId, double version, java.util.Calendar startDate, com.ecs.stellent.spp.Variable[] initialisationParms) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[113]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobUsingStartDateUsingVersion");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobUsingStartDateUsingVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, businessProcessId, new java.lang.Double(version), startDate, initialisationParms});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String createNewJobAndStartAtUsingVersion(java.lang.String sessionId, java.lang.String businessProcessId, java.lang.String businessProcessName, java.lang.String nodeName, double version, com.ecs.stellent.spp.Variable[] initialisationParms) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[114]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/CreateNewJobAndStartAtUsingVersion");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobAndStartAtUsingVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, businessProcessId, businessProcessName, nodeName, new java.lang.Double(version), initialisationParms});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.JobVarHistoryDetails[] getJobVariableHistory(java.lang.String sessionId, java.lang.String jobID, short nodeID, short EPC, boolean usePerformDate, java.util.Calendar performDate) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[115]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobVariableHistory");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobVariableHistory"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobID, new java.lang.Short(nodeID), new java.lang.Short(EPC), new java.lang.Boolean(usePerformDate), performDate});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.JobVarHistoryDetails[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.JobVarHistoryDetails[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.JobVarHistoryDetails[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public short getInstallationType() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[116]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetInstallationType");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetInstallationType"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Short) _resp).shortValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Short) org.apache.axis.utils.JavaUtils.convert(_resp, short.class)).shortValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.Object getJobVariableValue(java.lang.String sessionId, java.lang.String jobId, java.lang.String varId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[117]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobVariableValue");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobVariableValue"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, varId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.Object) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.Object) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.Object.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void placeJobOnHold(java.lang.String sessionID, java.lang.String jobID, short holdType, java.util.Calendar activationDate, short activationPeriodWeeks, short activationPeriodDays, short activationPeriodHours, java.lang.String reasonForHold) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[118]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/PlaceJobOnHold");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PlaceJobOnHold"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, jobID, new java.lang.Short(holdType), activationDate, new java.lang.Short(activationPeriodWeeks), new java.lang.Short(activationPeriodDays), new java.lang.Short(activationPeriodHours), reasonForHold});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void resetJobVariables(java.lang.String sessionID, java.lang.String jobID, java.lang.String subJobID, short nodeID, short embeddedProcessCount, boolean useLatestHistory, java.util.Calendar setTime, short setTimeMilliSecs) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[119]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ResetJobVariables");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResetJobVariables"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, jobID, subJobID, new java.lang.Short(nodeID), new java.lang.Short(embeddedProcessCount), new java.lang.Boolean(useLatestHistory), setTime, new java.lang.Short(setTimeMilliSecs)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String getSubJobXMLMapControlData(java.lang.String sessionId, java.lang.String subjobID, java.lang.String processID, double version) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[120]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetSubJobXMLMapControlData");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetSubJobXMLMapControlData"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, subjobID, processID, new java.lang.Double(version)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ExtendedJobs getJobsUsingSearchCriteria5(java.lang.String sessionId, java.util.Calendar startTimeFrom, boolean useStartTimeFrom, java.util.Calendar startTimeTo, boolean useStartTimeTo, java.util.Calendar finishTimeFrom, boolean useFinishTimeFrom, java.util.Calendar finishTimeTo, boolean useFinishTimeTo, java.lang.String creatorId, boolean useCreatorId, boolean workerResource, short status, java.lang.String processId, boolean useProcessId, double version, boolean useVersion, java.lang.String originServerId, boolean useOriginServerId, int maxNumToRetrieve, java.lang.String categoryId, boolean useCategoryId, short priority, boolean usePriority, java.util.Calendar dueDateFrom, boolean useDueDateFrom, java.util.Calendar dueDateTo, boolean useDueDateTo, java.lang.String workQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter, boolean useJobOwner, java.lang.String jobOwnerID, boolean useJobState, java.lang.String jobStateName, com.ecs.stellent.spp.Variable[] processTemplateVariables) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[121]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobsUsingSearchCriteria5");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteria5"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, startTimeFrom, new java.lang.Boolean(useStartTimeFrom), startTimeTo, new java.lang.Boolean(useStartTimeTo), finishTimeFrom, new java.lang.Boolean(useFinishTimeFrom), finishTimeTo, new java.lang.Boolean(useFinishTimeTo), creatorId, new java.lang.Boolean(useCreatorId), new java.lang.Boolean(workerResource), new java.lang.Short(status), processId, new java.lang.Boolean(useProcessId), new java.lang.Double(version), new java.lang.Boolean(useVersion), originServerId, new java.lang.Boolean(useOriginServerId), new java.lang.Integer(maxNumToRetrieve), categoryId, new java.lang.Boolean(useCategoryId), new java.lang.Short(priority), new java.lang.Boolean(usePriority), dueDateFrom, new java.lang.Boolean(useDueDateFrom), dueDateTo, new java.lang.Boolean(useDueDateTo), workQueueDefnName, fieldFilter, new java.lang.Boolean(useJobOwner), jobOwnerID, new java.lang.Boolean(useJobState), jobStateName, processTemplateVariables});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ExtendedJobs) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ExtendedJobs) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ExtendedJobs.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ExtendedJobs2 getJobsUsingSearchCriteria6(java.lang.String sessionId, java.lang.String caseRef, boolean useCaseRef, java.util.Calendar startTimeFrom, boolean useStartTimeFrom, java.util.Calendar startTimeTo, boolean useStartTimeTo, java.util.Calendar finishTimeFrom, boolean useFinishTimeFrom, java.util.Calendar finishTimeTo, boolean useFinishTimeTo, java.lang.String creatorId, boolean useCreatorId, boolean workerResource, short status, java.lang.String processId, boolean useProcessId, double version, boolean useVersion, java.lang.String originServerId, boolean useOriginServerId, int maxNumToRetrieve, java.lang.String categoryId, boolean useCategoryId, short priority, boolean usePriority, java.util.Calendar dueDateFrom, boolean useDueDateFrom, java.util.Calendar dueDateTo, boolean useDueDateTo, java.lang.String workQueueDefnName, com.ecs.stellent.spp.FieldFilterConfig fieldFilter, boolean useJobOwner, java.lang.String jobOwnerID, boolean useJobState, java.lang.String jobStateName, boolean useJobType, short jobType, com.ecs.stellent.spp.Variable[] processTemplateVariables) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[122]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetJobsUsingSearchCriteria6");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetJobsUsingSearchCriteria6"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, caseRef, new java.lang.Boolean(useCaseRef), startTimeFrom, new java.lang.Boolean(useStartTimeFrom), startTimeTo, new java.lang.Boolean(useStartTimeTo), finishTimeFrom, new java.lang.Boolean(useFinishTimeFrom), finishTimeTo, new java.lang.Boolean(useFinishTimeTo), creatorId, new java.lang.Boolean(useCreatorId), new java.lang.Boolean(workerResource), new java.lang.Short(status), processId, new java.lang.Boolean(useProcessId), new java.lang.Double(version), new java.lang.Boolean(useVersion), originServerId, new java.lang.Boolean(useOriginServerId), new java.lang.Integer(maxNumToRetrieve), categoryId, new java.lang.Boolean(useCategoryId), new java.lang.Short(priority), new java.lang.Boolean(usePriority), dueDateFrom, new java.lang.Boolean(useDueDateFrom), dueDateTo, new java.lang.Boolean(useDueDateTo), workQueueDefnName, fieldFilter, new java.lang.Boolean(useJobOwner), jobOwnerID, new java.lang.Boolean(useJobState), jobStateName, new java.lang.Boolean(useJobType), new java.lang.Short(jobType), processTemplateVariables});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ExtendedJobs2) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ExtendedJobs2) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ExtendedJobs2.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.SubJobDetails getSubJobDetails(java.lang.String sessionID, java.lang.String jobID, java.lang.String subJobID, java.lang.String subProcessID, double subJobVersion, short jobDatabase) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[123]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetSubJobDetails");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetSubJobDetails"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID, jobID, subJobID, subProcessID, new java.lang.Double(subJobVersion), new java.lang.Short(jobDatabase)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.SubJobDetails) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.SubJobDetails) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.SubJobDetails.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void updateJobPriority(java.lang.String sessionId, java.lang.String jobId, short jobPriority) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[124]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/UpdateJobPriority");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UpdateJobPriority"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(jobPriority)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void updateActivityPriority(java.lang.String sessionId, java.lang.String jobId, short nodeId, short embeddedProcessCount, short activityPriority) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[125]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/UpdateActivityPriority");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UpdateActivityPriority"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, new java.lang.Short(nodeId), new java.lang.Short(embeddedProcessCount), new java.lang.Short(activityPriority)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void updateSpend(java.lang.String sessionId, java.lang.String jobID, double value) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[126]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/UpdateSpend");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UpdateSpend"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobID, new java.lang.Double(value)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ActivityHistory2[] getProcessHistory(java.lang.String sessionId, java.lang.String jobID, boolean associatedJobs) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[127]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetProcessHistory");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetProcessHistory"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobID, new java.lang.Boolean(associatedJobs)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ActivityHistory2[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ActivityHistory2[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ActivityHistory2[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.Milestones[] getMilestones(java.lang.String sessionId, java.lang.String jobID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[128]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetMilestones");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMilestones"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.Milestones[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.Milestones[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.Milestones[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.RoleInfo[] getRoles(java.lang.String sessionId, java.lang.String jobID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[129]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetRoles");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetRoles"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.RoleInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.RoleInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.RoleInfo[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.Events[] getEvents(java.lang.String sessionId, java.lang.String jobID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[130]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetEvents");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetEvents"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.Events[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.Events[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.Events[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.Notes[] getNotes(java.lang.String sessionId, java.lang.String jobID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[131]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetNotes");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetNotes"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.Notes[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.Notes[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.Notes[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.JobInfo[] getAssociatedJobs(java.lang.String sessionId, java.lang.String jobId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[132]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetAssociatedJobs");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetAssociatedJobs"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.JobInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.JobInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.JobInfo[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void raiseEvent(java.lang.String sessionId, java.lang.String jobid, java.lang.String event, java.lang.String eventSource) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[133]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/RaiseEvent");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RaiseEvent"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobid, event, eventSource});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void raiseEventUpdVars(java.lang.String sessionId, java.lang.String jobid, java.lang.String event, java.lang.String eventSource, com.ecs.stellent.spp.RaiseEventVars[] vVariables) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[134]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/RaiseEventUpdVars");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RaiseEventUpdVars"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobid, event, eventSource, vVariables});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void revaluatePreconditon(java.lang.String sessionId, java.lang.String jobid) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[135]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/RevaluatePreconditon");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "RevaluatePreconditon"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobid});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void addToNumericVarValue(java.lang.String sessionId, java.lang.String ownerId, java.lang.String varId, java.lang.Object varValueToAdd) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[136]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/AddToNumericVarValue");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AddToNumericVarValue"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, ownerId, varId, varValueToAdd});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void addRoleMember(java.lang.String sessionId, java.lang.String jobID, java.lang.String roleID, java.lang.String resourceID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[137]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/AddRoleMember");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AddRoleMember"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobID, roleID, resourceID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void deleteRoleMember(java.lang.String sessionId, java.lang.String jobID, java.lang.String roleID, java.lang.String resourceID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[138]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/DeleteRoleMember");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DeleteRoleMember"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobID, roleID, resourceID});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void replaceRoleMembers(java.lang.String sessionId, java.lang.String jobID, java.lang.String roleID, java.lang.String[] resourceIDs) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[139]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ReplaceRoleMembers");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ReplaceRoleMembers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobID, roleID, resourceIDs});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void modifyMilestone(java.lang.String sessionId, java.lang.String jobId, java.lang.String milestoneName, java.util.Calendar newTargerDate) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[140]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/ModifyMilestone");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ModifyMilestone"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId, milestoneName, newTargerDate});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.StateHistory[] getStateChangeHistory(java.lang.String sessionId, java.lang.String jobId) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[141]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetStateChangeHistory");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetStateChangeHistory"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, jobId});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.StateHistory[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.StateHistory[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.StateHistory[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.WorkqueueOverview getWorkqueueOverviewForResource(java.lang.String sessionId, java.lang.String resourceId, boolean getIndividualWorkLoad, boolean getCombinedWorkLoad, boolean getWorkqueueDefinitionWorkLoads, boolean useStartDate, java.util.Calendar startDate, boolean useEndDate, java.util.Calendar endDate) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[142]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetWorkqueueOverviewForResource");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetWorkqueueOverviewForResource"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, resourceId, new java.lang.Boolean(getIndividualWorkLoad), new java.lang.Boolean(getCombinedWorkLoad), new java.lang.Boolean(getWorkqueueDefinitionWorkLoads), new java.lang.Boolean(useStartDate), startDate, new java.lang.Boolean(useEndDate), endDate});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.WorkqueueOverview) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.WorkqueueOverview) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.WorkqueueOverview.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void allocateActivity(java.lang.String sessionId, java.lang.String resourceId, java.lang.String jobId, short nodeId, short EPCount) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[143]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/AllocateActivity");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AllocateActivity"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, resourceId, jobId, new java.lang.Short(nodeId), new java.lang.Short(EPCount)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void allocateActivities(java.lang.String sessionId, java.lang.String resourceId, com.ecs.stellent.spp.ActivityRuntimeInfo[] activities) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[144]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/AllocateActivities");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AllocateActivities"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, resourceId, activities});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String[] getAllProcessMapStates(java.lang.String sessionID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[145]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetAllProcessMapStates");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetAllProcessMapStates"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionID});

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
  throw axisFaultException;
}
    }

    public com.ecs.stellent.spp.ProcessMapDetails[] getMapsForUserBasedOnType(java.lang.String sessionId, short accessType, short processType, boolean allVersions) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[146]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/GetMapsForUserBasedOnType");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "GetMapsForUserBasedOnType"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {sessionId, new java.lang.Short(accessType), new java.lang.Short(processType), new java.lang.Boolean(allVersions)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.ecs.stellent.spp.ProcessMapDetails[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.ecs.stellent.spp.ProcessMapDetails[]) org.apache.axis.utils.JavaUtils.convert(_resp, com.ecs.stellent.spp.ProcessMapDetails[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public void performAutoWorkAllocation(java.util.Calendar startDate, java.util.Calendar endDate) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[147]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://singularity.co.uk/webservices/spp/PerformAutoWorkAllocation");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PerformAutoWorkAllocation"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {startDate, endDate});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
