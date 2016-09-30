/**
 * ActivityInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ActivityInfo  implements java.io.Serializable {
    private java.lang.String jobId;

    private java.lang.String embeddedProcessId;

    private short embeddedProcessCount;

    private short nodeId;

    private com.ecs.stellent.spp.Variable[] inputVariables;

    private com.ecs.stellent.spp.Variable[] outputVariables;

    private short activityType;

    private java.lang.String activityName;

    private java.util.Calendar dueDate;

    private short priority;

    private short methodNo;

    private java.lang.String script;

    private java.lang.String progId;

    private java.lang.String createNewJobId;

    private java.lang.String CNJVarId;

    private java.lang.String CNJJobType;

    private java.lang.String CNJRefType;

    private com.ecs.stellent.spp.Variable[] createNewJobVars;

    private com.ecs.stellent.spp.Variable[] createNewJobOutVars;

    private java.lang.String scriptProgId;

    private boolean useScriptPreLoad;

    private java.lang.String preloadScript;

    private java.lang.String assocFile;

    private com.ecs.stellent.spp.TransactionalInfo[] transactionalInfo;

    private java.lang.String[] references;

    private java.lang.String startMethod;

    private java.lang.String classNamespace;

    private java.lang.String currentProcessID;

    private double currentVersion;

    public ActivityInfo() {
    }

    public ActivityInfo(
           java.lang.String jobId,
           java.lang.String embeddedProcessId,
           short embeddedProcessCount,
           short nodeId,
           com.ecs.stellent.spp.Variable[] inputVariables,
           com.ecs.stellent.spp.Variable[] outputVariables,
           short activityType,
           java.lang.String activityName,
           java.util.Calendar dueDate,
           short priority,
           short methodNo,
           java.lang.String script,
           java.lang.String progId,
           java.lang.String createNewJobId,
           java.lang.String CNJVarId,
           java.lang.String CNJJobType,
           java.lang.String CNJRefType,
           com.ecs.stellent.spp.Variable[] createNewJobVars,
           com.ecs.stellent.spp.Variable[] createNewJobOutVars,
           java.lang.String scriptProgId,
           boolean useScriptPreLoad,
           java.lang.String preloadScript,
           java.lang.String assocFile,
           com.ecs.stellent.spp.TransactionalInfo[] transactionalInfo,
           java.lang.String[] references,
           java.lang.String startMethod,
           java.lang.String classNamespace,
           java.lang.String currentProcessID,
           double currentVersion) {
           this.jobId = jobId;
           this.embeddedProcessId = embeddedProcessId;
           this.embeddedProcessCount = embeddedProcessCount;
           this.nodeId = nodeId;
           this.inputVariables = inputVariables;
           this.outputVariables = outputVariables;
           this.activityType = activityType;
           this.activityName = activityName;
           this.dueDate = dueDate;
           this.priority = priority;
           this.methodNo = methodNo;
           this.script = script;
           this.progId = progId;
           this.createNewJobId = createNewJobId;
           this.CNJVarId = CNJVarId;
           this.CNJJobType = CNJJobType;
           this.CNJRefType = CNJRefType;
           this.createNewJobVars = createNewJobVars;
           this.createNewJobOutVars = createNewJobOutVars;
           this.scriptProgId = scriptProgId;
           this.useScriptPreLoad = useScriptPreLoad;
           this.preloadScript = preloadScript;
           this.assocFile = assocFile;
           this.transactionalInfo = transactionalInfo;
           this.references = references;
           this.startMethod = startMethod;
           this.classNamespace = classNamespace;
           this.currentProcessID = currentProcessID;
           this.currentVersion = currentVersion;
    }


    /**
     * Gets the jobId value for this ActivityInfo.
     * 
     * @return jobId
     */
    public java.lang.String getJobId() {
        return jobId;
    }


    /**
     * Sets the jobId value for this ActivityInfo.
     * 
     * @param jobId
     */
    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }


    /**
     * Gets the embeddedProcessId value for this ActivityInfo.
     * 
     * @return embeddedProcessId
     */
    public java.lang.String getEmbeddedProcessId() {
        return embeddedProcessId;
    }


    /**
     * Sets the embeddedProcessId value for this ActivityInfo.
     * 
     * @param embeddedProcessId
     */
    public void setEmbeddedProcessId(java.lang.String embeddedProcessId) {
        this.embeddedProcessId = embeddedProcessId;
    }


    /**
     * Gets the embeddedProcessCount value for this ActivityInfo.
     * 
     * @return embeddedProcessCount
     */
    public short getEmbeddedProcessCount() {
        return embeddedProcessCount;
    }


    /**
     * Sets the embeddedProcessCount value for this ActivityInfo.
     * 
     * @param embeddedProcessCount
     */
    public void setEmbeddedProcessCount(short embeddedProcessCount) {
        this.embeddedProcessCount = embeddedProcessCount;
    }


    /**
     * Gets the nodeId value for this ActivityInfo.
     * 
     * @return nodeId
     */
    public short getNodeId() {
        return nodeId;
    }


    /**
     * Sets the nodeId value for this ActivityInfo.
     * 
     * @param nodeId
     */
    public void setNodeId(short nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Gets the inputVariables value for this ActivityInfo.
     * 
     * @return inputVariables
     */
    public com.ecs.stellent.spp.Variable[] getInputVariables() {
        return inputVariables;
    }


    /**
     * Sets the inputVariables value for this ActivityInfo.
     * 
     * @param inputVariables
     */
    public void setInputVariables(com.ecs.stellent.spp.Variable[] inputVariables) {
        this.inputVariables = inputVariables;
    }


    /**
     * Gets the outputVariables value for this ActivityInfo.
     * 
     * @return outputVariables
     */
    public com.ecs.stellent.spp.Variable[] getOutputVariables() {
        return outputVariables;
    }


    /**
     * Sets the outputVariables value for this ActivityInfo.
     * 
     * @param outputVariables
     */
    public void setOutputVariables(com.ecs.stellent.spp.Variable[] outputVariables) {
        this.outputVariables = outputVariables;
    }


    /**
     * Gets the activityType value for this ActivityInfo.
     * 
     * @return activityType
     */
    public short getActivityType() {
        return activityType;
    }


    /**
     * Sets the activityType value for this ActivityInfo.
     * 
     * @param activityType
     */
    public void setActivityType(short activityType) {
        this.activityType = activityType;
    }


    /**
     * Gets the activityName value for this ActivityInfo.
     * 
     * @return activityName
     */
    public java.lang.String getActivityName() {
        return activityName;
    }


    /**
     * Sets the activityName value for this ActivityInfo.
     * 
     * @param activityName
     */
    public void setActivityName(java.lang.String activityName) {
        this.activityName = activityName;
    }


    /**
     * Gets the dueDate value for this ActivityInfo.
     * 
     * @return dueDate
     */
    public java.util.Calendar getDueDate() {
        return dueDate;
    }


    /**
     * Sets the dueDate value for this ActivityInfo.
     * 
     * @param dueDate
     */
    public void setDueDate(java.util.Calendar dueDate) {
        this.dueDate = dueDate;
    }


    /**
     * Gets the priority value for this ActivityInfo.
     * 
     * @return priority
     */
    public short getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this ActivityInfo.
     * 
     * @param priority
     */
    public void setPriority(short priority) {
        this.priority = priority;
    }


    /**
     * Gets the methodNo value for this ActivityInfo.
     * 
     * @return methodNo
     */
    public short getMethodNo() {
        return methodNo;
    }


    /**
     * Sets the methodNo value for this ActivityInfo.
     * 
     * @param methodNo
     */
    public void setMethodNo(short methodNo) {
        this.methodNo = methodNo;
    }


    /**
     * Gets the script value for this ActivityInfo.
     * 
     * @return script
     */
    public java.lang.String getScript() {
        return script;
    }


    /**
     * Sets the script value for this ActivityInfo.
     * 
     * @param script
     */
    public void setScript(java.lang.String script) {
        this.script = script;
    }


    /**
     * Gets the progId value for this ActivityInfo.
     * 
     * @return progId
     */
    public java.lang.String getProgId() {
        return progId;
    }


    /**
     * Sets the progId value for this ActivityInfo.
     * 
     * @param progId
     */
    public void setProgId(java.lang.String progId) {
        this.progId = progId;
    }


    /**
     * Gets the createNewJobId value for this ActivityInfo.
     * 
     * @return createNewJobId
     */
    public java.lang.String getCreateNewJobId() {
        return createNewJobId;
    }


    /**
     * Sets the createNewJobId value for this ActivityInfo.
     * 
     * @param createNewJobId
     */
    public void setCreateNewJobId(java.lang.String createNewJobId) {
        this.createNewJobId = createNewJobId;
    }


    /**
     * Gets the CNJVarId value for this ActivityInfo.
     * 
     * @return CNJVarId
     */
    public java.lang.String getCNJVarId() {
        return CNJVarId;
    }


    /**
     * Sets the CNJVarId value for this ActivityInfo.
     * 
     * @param CNJVarId
     */
    public void setCNJVarId(java.lang.String CNJVarId) {
        this.CNJVarId = CNJVarId;
    }


    /**
     * Gets the CNJJobType value for this ActivityInfo.
     * 
     * @return CNJJobType
     */
    public java.lang.String getCNJJobType() {
        return CNJJobType;
    }


    /**
     * Sets the CNJJobType value for this ActivityInfo.
     * 
     * @param CNJJobType
     */
    public void setCNJJobType(java.lang.String CNJJobType) {
        this.CNJJobType = CNJJobType;
    }


    /**
     * Gets the CNJRefType value for this ActivityInfo.
     * 
     * @return CNJRefType
     */
    public java.lang.String getCNJRefType() {
        return CNJRefType;
    }


    /**
     * Sets the CNJRefType value for this ActivityInfo.
     * 
     * @param CNJRefType
     */
    public void setCNJRefType(java.lang.String CNJRefType) {
        this.CNJRefType = CNJRefType;
    }


    /**
     * Gets the createNewJobVars value for this ActivityInfo.
     * 
     * @return createNewJobVars
     */
    public com.ecs.stellent.spp.Variable[] getCreateNewJobVars() {
        return createNewJobVars;
    }


    /**
     * Sets the createNewJobVars value for this ActivityInfo.
     * 
     * @param createNewJobVars
     */
    public void setCreateNewJobVars(com.ecs.stellent.spp.Variable[] createNewJobVars) {
        this.createNewJobVars = createNewJobVars;
    }


    /**
     * Gets the createNewJobOutVars value for this ActivityInfo.
     * 
     * @return createNewJobOutVars
     */
    public com.ecs.stellent.spp.Variable[] getCreateNewJobOutVars() {
        return createNewJobOutVars;
    }


    /**
     * Sets the createNewJobOutVars value for this ActivityInfo.
     * 
     * @param createNewJobOutVars
     */
    public void setCreateNewJobOutVars(com.ecs.stellent.spp.Variable[] createNewJobOutVars) {
        this.createNewJobOutVars = createNewJobOutVars;
    }


    /**
     * Gets the scriptProgId value for this ActivityInfo.
     * 
     * @return scriptProgId
     */
    public java.lang.String getScriptProgId() {
        return scriptProgId;
    }


    /**
     * Sets the scriptProgId value for this ActivityInfo.
     * 
     * @param scriptProgId
     */
    public void setScriptProgId(java.lang.String scriptProgId) {
        this.scriptProgId = scriptProgId;
    }


    /**
     * Gets the useScriptPreLoad value for this ActivityInfo.
     * 
     * @return useScriptPreLoad
     */
    public boolean isUseScriptPreLoad() {
        return useScriptPreLoad;
    }


    /**
     * Sets the useScriptPreLoad value for this ActivityInfo.
     * 
     * @param useScriptPreLoad
     */
    public void setUseScriptPreLoad(boolean useScriptPreLoad) {
        this.useScriptPreLoad = useScriptPreLoad;
    }


    /**
     * Gets the preloadScript value for this ActivityInfo.
     * 
     * @return preloadScript
     */
    public java.lang.String getPreloadScript() {
        return preloadScript;
    }


    /**
     * Sets the preloadScript value for this ActivityInfo.
     * 
     * @param preloadScript
     */
    public void setPreloadScript(java.lang.String preloadScript) {
        this.preloadScript = preloadScript;
    }


    /**
     * Gets the assocFile value for this ActivityInfo.
     * 
     * @return assocFile
     */
    public java.lang.String getAssocFile() {
        return assocFile;
    }


    /**
     * Sets the assocFile value for this ActivityInfo.
     * 
     * @param assocFile
     */
    public void setAssocFile(java.lang.String assocFile) {
        this.assocFile = assocFile;
    }


    /**
     * Gets the transactionalInfo value for this ActivityInfo.
     * 
     * @return transactionalInfo
     */
    public com.ecs.stellent.spp.TransactionalInfo[] getTransactionalInfo() {
        return transactionalInfo;
    }


    /**
     * Sets the transactionalInfo value for this ActivityInfo.
     * 
     * @param transactionalInfo
     */
    public void setTransactionalInfo(com.ecs.stellent.spp.TransactionalInfo[] transactionalInfo) {
        this.transactionalInfo = transactionalInfo;
    }


    /**
     * Gets the references value for this ActivityInfo.
     * 
     * @return references
     */
    public java.lang.String[] getReferences() {
        return references;
    }


    /**
     * Sets the references value for this ActivityInfo.
     * 
     * @param references
     */
    public void setReferences(java.lang.String[] references) {
        this.references = references;
    }


    /**
     * Gets the startMethod value for this ActivityInfo.
     * 
     * @return startMethod
     */
    public java.lang.String getStartMethod() {
        return startMethod;
    }


    /**
     * Sets the startMethod value for this ActivityInfo.
     * 
     * @param startMethod
     */
    public void setStartMethod(java.lang.String startMethod) {
        this.startMethod = startMethod;
    }


    /**
     * Gets the classNamespace value for this ActivityInfo.
     * 
     * @return classNamespace
     */
    public java.lang.String getClassNamespace() {
        return classNamespace;
    }


    /**
     * Sets the classNamespace value for this ActivityInfo.
     * 
     * @param classNamespace
     */
    public void setClassNamespace(java.lang.String classNamespace) {
        this.classNamespace = classNamespace;
    }


    /**
     * Gets the currentProcessID value for this ActivityInfo.
     * 
     * @return currentProcessID
     */
    public java.lang.String getCurrentProcessID() {
        return currentProcessID;
    }


    /**
     * Sets the currentProcessID value for this ActivityInfo.
     * 
     * @param currentProcessID
     */
    public void setCurrentProcessID(java.lang.String currentProcessID) {
        this.currentProcessID = currentProcessID;
    }


    /**
     * Gets the currentVersion value for this ActivityInfo.
     * 
     * @return currentVersion
     */
    public double getCurrentVersion() {
        return currentVersion;
    }


    /**
     * Sets the currentVersion value for this ActivityInfo.
     * 
     * @param currentVersion
     */
    public void setCurrentVersion(double currentVersion) {
        this.currentVersion = currentVersion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ActivityInfo)) return false;
        ActivityInfo other = (ActivityInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.jobId==null && other.getJobId()==null) || 
             (this.jobId!=null &&
              this.jobId.equals(other.getJobId()))) &&
            ((this.embeddedProcessId==null && other.getEmbeddedProcessId()==null) || 
             (this.embeddedProcessId!=null &&
              this.embeddedProcessId.equals(other.getEmbeddedProcessId()))) &&
            this.embeddedProcessCount == other.getEmbeddedProcessCount() &&
            this.nodeId == other.getNodeId() &&
            ((this.inputVariables==null && other.getInputVariables()==null) || 
             (this.inputVariables!=null &&
              java.util.Arrays.equals(this.inputVariables, other.getInputVariables()))) &&
            ((this.outputVariables==null && other.getOutputVariables()==null) || 
             (this.outputVariables!=null &&
              java.util.Arrays.equals(this.outputVariables, other.getOutputVariables()))) &&
            this.activityType == other.getActivityType() &&
            ((this.activityName==null && other.getActivityName()==null) || 
             (this.activityName!=null &&
              this.activityName.equals(other.getActivityName()))) &&
            ((this.dueDate==null && other.getDueDate()==null) || 
             (this.dueDate!=null &&
              this.dueDate.equals(other.getDueDate()))) &&
            this.priority == other.getPriority() &&
            this.methodNo == other.getMethodNo() &&
            ((this.script==null && other.getScript()==null) || 
             (this.script!=null &&
              this.script.equals(other.getScript()))) &&
            ((this.progId==null && other.getProgId()==null) || 
             (this.progId!=null &&
              this.progId.equals(other.getProgId()))) &&
            ((this.createNewJobId==null && other.getCreateNewJobId()==null) || 
             (this.createNewJobId!=null &&
              this.createNewJobId.equals(other.getCreateNewJobId()))) &&
            ((this.CNJVarId==null && other.getCNJVarId()==null) || 
             (this.CNJVarId!=null &&
              this.CNJVarId.equals(other.getCNJVarId()))) &&
            ((this.CNJJobType==null && other.getCNJJobType()==null) || 
             (this.CNJJobType!=null &&
              this.CNJJobType.equals(other.getCNJJobType()))) &&
            ((this.CNJRefType==null && other.getCNJRefType()==null) || 
             (this.CNJRefType!=null &&
              this.CNJRefType.equals(other.getCNJRefType()))) &&
            ((this.createNewJobVars==null && other.getCreateNewJobVars()==null) || 
             (this.createNewJobVars!=null &&
              java.util.Arrays.equals(this.createNewJobVars, other.getCreateNewJobVars()))) &&
            ((this.createNewJobOutVars==null && other.getCreateNewJobOutVars()==null) || 
             (this.createNewJobOutVars!=null &&
              java.util.Arrays.equals(this.createNewJobOutVars, other.getCreateNewJobOutVars()))) &&
            ((this.scriptProgId==null && other.getScriptProgId()==null) || 
             (this.scriptProgId!=null &&
              this.scriptProgId.equals(other.getScriptProgId()))) &&
            this.useScriptPreLoad == other.isUseScriptPreLoad() &&
            ((this.preloadScript==null && other.getPreloadScript()==null) || 
             (this.preloadScript!=null &&
              this.preloadScript.equals(other.getPreloadScript()))) &&
            ((this.assocFile==null && other.getAssocFile()==null) || 
             (this.assocFile!=null &&
              this.assocFile.equals(other.getAssocFile()))) &&
            ((this.transactionalInfo==null && other.getTransactionalInfo()==null) || 
             (this.transactionalInfo!=null &&
              java.util.Arrays.equals(this.transactionalInfo, other.getTransactionalInfo()))) &&
            ((this.references==null && other.getReferences()==null) || 
             (this.references!=null &&
              java.util.Arrays.equals(this.references, other.getReferences()))) &&
            ((this.startMethod==null && other.getStartMethod()==null) || 
             (this.startMethod!=null &&
              this.startMethod.equals(other.getStartMethod()))) &&
            ((this.classNamespace==null && other.getClassNamespace()==null) || 
             (this.classNamespace!=null &&
              this.classNamespace.equals(other.getClassNamespace()))) &&
            ((this.currentProcessID==null && other.getCurrentProcessID()==null) || 
             (this.currentProcessID!=null &&
              this.currentProcessID.equals(other.getCurrentProcessID()))) &&
            this.currentVersion == other.getCurrentVersion();
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
        if (getJobId() != null) {
            _hashCode += getJobId().hashCode();
        }
        if (getEmbeddedProcessId() != null) {
            _hashCode += getEmbeddedProcessId().hashCode();
        }
        _hashCode += getEmbeddedProcessCount();
        _hashCode += getNodeId();
        if (getInputVariables() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getInputVariables());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getInputVariables(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOutputVariables() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOutputVariables());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOutputVariables(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getActivityType();
        if (getActivityName() != null) {
            _hashCode += getActivityName().hashCode();
        }
        if (getDueDate() != null) {
            _hashCode += getDueDate().hashCode();
        }
        _hashCode += getPriority();
        _hashCode += getMethodNo();
        if (getScript() != null) {
            _hashCode += getScript().hashCode();
        }
        if (getProgId() != null) {
            _hashCode += getProgId().hashCode();
        }
        if (getCreateNewJobId() != null) {
            _hashCode += getCreateNewJobId().hashCode();
        }
        if (getCNJVarId() != null) {
            _hashCode += getCNJVarId().hashCode();
        }
        if (getCNJJobType() != null) {
            _hashCode += getCNJJobType().hashCode();
        }
        if (getCNJRefType() != null) {
            _hashCode += getCNJRefType().hashCode();
        }
        if (getCreateNewJobVars() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCreateNewJobVars());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCreateNewJobVars(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCreateNewJobOutVars() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCreateNewJobOutVars());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCreateNewJobOutVars(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getScriptProgId() != null) {
            _hashCode += getScriptProgId().hashCode();
        }
        _hashCode += (isUseScriptPreLoad() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getPreloadScript() != null) {
            _hashCode += getPreloadScript().hashCode();
        }
        if (getAssocFile() != null) {
            _hashCode += getAssocFile().hashCode();
        }
        if (getTransactionalInfo() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTransactionalInfo());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTransactionalInfo(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getReferences() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReferences());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReferences(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getStartMethod() != null) {
            _hashCode += getStartMethod().hashCode();
        }
        if (getClassNamespace() != null) {
            _hashCode += getClassNamespace().hashCode();
        }
        if (getCurrentProcessID() != null) {
            _hashCode += getCurrentProcessID().hashCode();
        }
        _hashCode += new Double(getCurrentVersion()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ActivityInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "JobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("embeddedProcessId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("embeddedProcessCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "EmbeddedProcessCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nodeId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "NodeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputVariables");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "InputVariables"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outputVariables");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "OutputVariables"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activityType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activityName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ActivityName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("priority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Priority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("methodNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "MethodNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("script");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Script"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("progId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProgId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CNJVarId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CNJVarId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CNJJobType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CNJJobType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CNJRefType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CNJRefType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobVars");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobVars"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createNewJobOutVars");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateNewJobOutVars"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Variable"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scriptProgId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ScriptProgId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("useScriptPreLoad");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "UseScriptPreLoad"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("preloadScript");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "PreloadScript"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("assocFile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AssocFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionalInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "transactionalInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TransactionalInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TransactionalInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("references");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "References"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startMethod");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StartMethod"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("classNamespace");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ClassNamespace"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentProcessID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CurrentProcessID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("currentVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CurrentVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
