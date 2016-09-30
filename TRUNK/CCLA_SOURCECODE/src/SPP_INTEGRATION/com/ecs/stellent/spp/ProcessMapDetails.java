/**
 * ProcessMapDetails.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ecs.stellent.spp;

public class ProcessMapDetails  implements java.io.Serializable {
    private java.lang.String processId;

    private java.lang.String processName;

    private double version;

    private java.lang.String resourceName;

    private java.lang.String helpText;

    private java.lang.String lastModifiedBy;

    private short algorithm;

    private short arrowType;

    private short arrowWeighting;

    private java.lang.String category;

    private int targetInSecs;

    private short priority;

    private short transactional;

    private int background;

    private double cost;

    private java.util.Calendar createDate;

    private java.util.Calendar changeDate;

    private java.lang.String description;

    private short statusInDB;

    private java.lang.String authorId;

    private boolean straightThrough;

    private java.lang.String workQueueDefinition;

    private double budget;

    private java.lang.String dynaminBudget;

    private short processType;

    private java.lang.String associatedCaseID;

    private short variablePrefix;

    public ProcessMapDetails() {
    }

    public ProcessMapDetails(
           java.lang.String processId,
           java.lang.String processName,
           double version,
           java.lang.String resourceName,
           java.lang.String helpText,
           java.lang.String lastModifiedBy,
           short algorithm,
           short arrowType,
           short arrowWeighting,
           java.lang.String category,
           int targetInSecs,
           short priority,
           short transactional,
           int background,
           double cost,
           java.util.Calendar createDate,
           java.util.Calendar changeDate,
           java.lang.String description,
           short statusInDB,
           java.lang.String authorId,
           boolean straightThrough,
           java.lang.String workQueueDefinition,
           double budget,
           java.lang.String dynaminBudget,
           short processType,
           java.lang.String associatedCaseID,
           short variablePrefix) {
           this.processId = processId;
           this.processName = processName;
           this.version = version;
           this.resourceName = resourceName;
           this.helpText = helpText;
           this.lastModifiedBy = lastModifiedBy;
           this.algorithm = algorithm;
           this.arrowType = arrowType;
           this.arrowWeighting = arrowWeighting;
           this.category = category;
           this.targetInSecs = targetInSecs;
           this.priority = priority;
           this.transactional = transactional;
           this.background = background;
           this.cost = cost;
           this.createDate = createDate;
           this.changeDate = changeDate;
           this.description = description;
           this.statusInDB = statusInDB;
           this.authorId = authorId;
           this.straightThrough = straightThrough;
           this.workQueueDefinition = workQueueDefinition;
           this.budget = budget;
           this.dynaminBudget = dynaminBudget;
           this.processType = processType;
           this.associatedCaseID = associatedCaseID;
           this.variablePrefix = variablePrefix;
    }


    /**
     * Gets the processId value for this ProcessMapDetails.
     * 
     * @return processId
     */
    public java.lang.String getProcessId() {
        return processId;
    }


    /**
     * Sets the processId value for this ProcessMapDetails.
     * 
     * @param processId
     */
    public void setProcessId(java.lang.String processId) {
        this.processId = processId;
    }


    /**
     * Gets the processName value for this ProcessMapDetails.
     * 
     * @return processName
     */
    public java.lang.String getProcessName() {
        return processName;
    }


    /**
     * Sets the processName value for this ProcessMapDetails.
     * 
     * @param processName
     */
    public void setProcessName(java.lang.String processName) {
        this.processName = processName;
    }


    /**
     * Gets the version value for this ProcessMapDetails.
     * 
     * @return version
     */
    public double getVersion() {
        return version;
    }


    /**
     * Sets the version value for this ProcessMapDetails.
     * 
     * @param version
     */
    public void setVersion(double version) {
        this.version = version;
    }


    /**
     * Gets the resourceName value for this ProcessMapDetails.
     * 
     * @return resourceName
     */
    public java.lang.String getResourceName() {
        return resourceName;
    }


    /**
     * Sets the resourceName value for this ProcessMapDetails.
     * 
     * @param resourceName
     */
    public void setResourceName(java.lang.String resourceName) {
        this.resourceName = resourceName;
    }


    /**
     * Gets the helpText value for this ProcessMapDetails.
     * 
     * @return helpText
     */
    public java.lang.String getHelpText() {
        return helpText;
    }


    /**
     * Sets the helpText value for this ProcessMapDetails.
     * 
     * @param helpText
     */
    public void setHelpText(java.lang.String helpText) {
        this.helpText = helpText;
    }


    /**
     * Gets the lastModifiedBy value for this ProcessMapDetails.
     * 
     * @return lastModifiedBy
     */
    public java.lang.String getLastModifiedBy() {
        return lastModifiedBy;
    }


    /**
     * Sets the lastModifiedBy value for this ProcessMapDetails.
     * 
     * @param lastModifiedBy
     */
    public void setLastModifiedBy(java.lang.String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }


    /**
     * Gets the algorithm value for this ProcessMapDetails.
     * 
     * @return algorithm
     */
    public short getAlgorithm() {
        return algorithm;
    }


    /**
     * Sets the algorithm value for this ProcessMapDetails.
     * 
     * @param algorithm
     */
    public void setAlgorithm(short algorithm) {
        this.algorithm = algorithm;
    }


    /**
     * Gets the arrowType value for this ProcessMapDetails.
     * 
     * @return arrowType
     */
    public short getArrowType() {
        return arrowType;
    }


    /**
     * Sets the arrowType value for this ProcessMapDetails.
     * 
     * @param arrowType
     */
    public void setArrowType(short arrowType) {
        this.arrowType = arrowType;
    }


    /**
     * Gets the arrowWeighting value for this ProcessMapDetails.
     * 
     * @return arrowWeighting
     */
    public short getArrowWeighting() {
        return arrowWeighting;
    }


    /**
     * Sets the arrowWeighting value for this ProcessMapDetails.
     * 
     * @param arrowWeighting
     */
    public void setArrowWeighting(short arrowWeighting) {
        this.arrowWeighting = arrowWeighting;
    }


    /**
     * Gets the category value for this ProcessMapDetails.
     * 
     * @return category
     */
    public java.lang.String getCategory() {
        return category;
    }


    /**
     * Sets the category value for this ProcessMapDetails.
     * 
     * @param category
     */
    public void setCategory(java.lang.String category) {
        this.category = category;
    }


    /**
     * Gets the targetInSecs value for this ProcessMapDetails.
     * 
     * @return targetInSecs
     */
    public int getTargetInSecs() {
        return targetInSecs;
    }


    /**
     * Sets the targetInSecs value for this ProcessMapDetails.
     * 
     * @param targetInSecs
     */
    public void setTargetInSecs(int targetInSecs) {
        this.targetInSecs = targetInSecs;
    }


    /**
     * Gets the priority value for this ProcessMapDetails.
     * 
     * @return priority
     */
    public short getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this ProcessMapDetails.
     * 
     * @param priority
     */
    public void setPriority(short priority) {
        this.priority = priority;
    }


    /**
     * Gets the transactional value for this ProcessMapDetails.
     * 
     * @return transactional
     */
    public short getTransactional() {
        return transactional;
    }


    /**
     * Sets the transactional value for this ProcessMapDetails.
     * 
     * @param transactional
     */
    public void setTransactional(short transactional) {
        this.transactional = transactional;
    }


    /**
     * Gets the background value for this ProcessMapDetails.
     * 
     * @return background
     */
    public int getBackground() {
        return background;
    }


    /**
     * Sets the background value for this ProcessMapDetails.
     * 
     * @param background
     */
    public void setBackground(int background) {
        this.background = background;
    }


    /**
     * Gets the cost value for this ProcessMapDetails.
     * 
     * @return cost
     */
    public double getCost() {
        return cost;
    }


    /**
     * Sets the cost value for this ProcessMapDetails.
     * 
     * @param cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }


    /**
     * Gets the createDate value for this ProcessMapDetails.
     * 
     * @return createDate
     */
    public java.util.Calendar getCreateDate() {
        return createDate;
    }


    /**
     * Sets the createDate value for this ProcessMapDetails.
     * 
     * @param createDate
     */
    public void setCreateDate(java.util.Calendar createDate) {
        this.createDate = createDate;
    }


    /**
     * Gets the changeDate value for this ProcessMapDetails.
     * 
     * @return changeDate
     */
    public java.util.Calendar getChangeDate() {
        return changeDate;
    }


    /**
     * Sets the changeDate value for this ProcessMapDetails.
     * 
     * @param changeDate
     */
    public void setChangeDate(java.util.Calendar changeDate) {
        this.changeDate = changeDate;
    }


    /**
     * Gets the description value for this ProcessMapDetails.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ProcessMapDetails.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the statusInDB value for this ProcessMapDetails.
     * 
     * @return statusInDB
     */
    public short getStatusInDB() {
        return statusInDB;
    }


    /**
     * Sets the statusInDB value for this ProcessMapDetails.
     * 
     * @param statusInDB
     */
    public void setStatusInDB(short statusInDB) {
        this.statusInDB = statusInDB;
    }


    /**
     * Gets the authorId value for this ProcessMapDetails.
     * 
     * @return authorId
     */
    public java.lang.String getAuthorId() {
        return authorId;
    }


    /**
     * Sets the authorId value for this ProcessMapDetails.
     * 
     * @param authorId
     */
    public void setAuthorId(java.lang.String authorId) {
        this.authorId = authorId;
    }


    /**
     * Gets the straightThrough value for this ProcessMapDetails.
     * 
     * @return straightThrough
     */
    public boolean isStraightThrough() {
        return straightThrough;
    }


    /**
     * Sets the straightThrough value for this ProcessMapDetails.
     * 
     * @param straightThrough
     */
    public void setStraightThrough(boolean straightThrough) {
        this.straightThrough = straightThrough;
    }


    /**
     * Gets the workQueueDefinition value for this ProcessMapDetails.
     * 
     * @return workQueueDefinition
     */
    public java.lang.String getWorkQueueDefinition() {
        return workQueueDefinition;
    }


    /**
     * Sets the workQueueDefinition value for this ProcessMapDetails.
     * 
     * @param workQueueDefinition
     */
    public void setWorkQueueDefinition(java.lang.String workQueueDefinition) {
        this.workQueueDefinition = workQueueDefinition;
    }


    /**
     * Gets the budget value for this ProcessMapDetails.
     * 
     * @return budget
     */
    public double getBudget() {
        return budget;
    }


    /**
     * Sets the budget value for this ProcessMapDetails.
     * 
     * @param budget
     */
    public void setBudget(double budget) {
        this.budget = budget;
    }


    /**
     * Gets the dynaminBudget value for this ProcessMapDetails.
     * 
     * @return dynaminBudget
     */
    public java.lang.String getDynaminBudget() {
        return dynaminBudget;
    }


    /**
     * Sets the dynaminBudget value for this ProcessMapDetails.
     * 
     * @param dynaminBudget
     */
    public void setDynaminBudget(java.lang.String dynaminBudget) {
        this.dynaminBudget = dynaminBudget;
    }


    /**
     * Gets the processType value for this ProcessMapDetails.
     * 
     * @return processType
     */
    public short getProcessType() {
        return processType;
    }


    /**
     * Sets the processType value for this ProcessMapDetails.
     * 
     * @param processType
     */
    public void setProcessType(short processType) {
        this.processType = processType;
    }


    /**
     * Gets the associatedCaseID value for this ProcessMapDetails.
     * 
     * @return associatedCaseID
     */
    public java.lang.String getAssociatedCaseID() {
        return associatedCaseID;
    }


    /**
     * Sets the associatedCaseID value for this ProcessMapDetails.
     * 
     * @param associatedCaseID
     */
    public void setAssociatedCaseID(java.lang.String associatedCaseID) {
        this.associatedCaseID = associatedCaseID;
    }


    /**
     * Gets the variablePrefix value for this ProcessMapDetails.
     * 
     * @return variablePrefix
     */
    public short getVariablePrefix() {
        return variablePrefix;
    }


    /**
     * Sets the variablePrefix value for this ProcessMapDetails.
     * 
     * @param variablePrefix
     */
    public void setVariablePrefix(short variablePrefix) {
        this.variablePrefix = variablePrefix;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProcessMapDetails)) return false;
        ProcessMapDetails other = (ProcessMapDetails) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.processId==null && other.getProcessId()==null) || 
             (this.processId!=null &&
              this.processId.equals(other.getProcessId()))) &&
            ((this.processName==null && other.getProcessName()==null) || 
             (this.processName!=null &&
              this.processName.equals(other.getProcessName()))) &&
            this.version == other.getVersion() &&
            ((this.resourceName==null && other.getResourceName()==null) || 
             (this.resourceName!=null &&
              this.resourceName.equals(other.getResourceName()))) &&
            ((this.helpText==null && other.getHelpText()==null) || 
             (this.helpText!=null &&
              this.helpText.equals(other.getHelpText()))) &&
            ((this.lastModifiedBy==null && other.getLastModifiedBy()==null) || 
             (this.lastModifiedBy!=null &&
              this.lastModifiedBy.equals(other.getLastModifiedBy()))) &&
            this.algorithm == other.getAlgorithm() &&
            this.arrowType == other.getArrowType() &&
            this.arrowWeighting == other.getArrowWeighting() &&
            ((this.category==null && other.getCategory()==null) || 
             (this.category!=null &&
              this.category.equals(other.getCategory()))) &&
            this.targetInSecs == other.getTargetInSecs() &&
            this.priority == other.getPriority() &&
            this.transactional == other.getTransactional() &&
            this.background == other.getBackground() &&
            this.cost == other.getCost() &&
            ((this.createDate==null && other.getCreateDate()==null) || 
             (this.createDate!=null &&
              this.createDate.equals(other.getCreateDate()))) &&
            ((this.changeDate==null && other.getChangeDate()==null) || 
             (this.changeDate!=null &&
              this.changeDate.equals(other.getChangeDate()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            this.statusInDB == other.getStatusInDB() &&
            ((this.authorId==null && other.getAuthorId()==null) || 
             (this.authorId!=null &&
              this.authorId.equals(other.getAuthorId()))) &&
            this.straightThrough == other.isStraightThrough() &&
            ((this.workQueueDefinition==null && other.getWorkQueueDefinition()==null) || 
             (this.workQueueDefinition!=null &&
              this.workQueueDefinition.equals(other.getWorkQueueDefinition()))) &&
            this.budget == other.getBudget() &&
            ((this.dynaminBudget==null && other.getDynaminBudget()==null) || 
             (this.dynaminBudget!=null &&
              this.dynaminBudget.equals(other.getDynaminBudget()))) &&
            this.processType == other.getProcessType() &&
            ((this.associatedCaseID==null && other.getAssociatedCaseID()==null) || 
             (this.associatedCaseID!=null &&
              this.associatedCaseID.equals(other.getAssociatedCaseID()))) &&
            this.variablePrefix == other.getVariablePrefix();
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
        if (getProcessId() != null) {
            _hashCode += getProcessId().hashCode();
        }
        if (getProcessName() != null) {
            _hashCode += getProcessName().hashCode();
        }
        _hashCode += new Double(getVersion()).hashCode();
        if (getResourceName() != null) {
            _hashCode += getResourceName().hashCode();
        }
        if (getHelpText() != null) {
            _hashCode += getHelpText().hashCode();
        }
        if (getLastModifiedBy() != null) {
            _hashCode += getLastModifiedBy().hashCode();
        }
        _hashCode += getAlgorithm();
        _hashCode += getArrowType();
        _hashCode += getArrowWeighting();
        if (getCategory() != null) {
            _hashCode += getCategory().hashCode();
        }
        _hashCode += getTargetInSecs();
        _hashCode += getPriority();
        _hashCode += getTransactional();
        _hashCode += getBackground();
        _hashCode += new Double(getCost()).hashCode();
        if (getCreateDate() != null) {
            _hashCode += getCreateDate().hashCode();
        }
        if (getChangeDate() != null) {
            _hashCode += getChangeDate().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += getStatusInDB();
        if (getAuthorId() != null) {
            _hashCode += getAuthorId().hashCode();
        }
        _hashCode += (isStraightThrough() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getWorkQueueDefinition() != null) {
            _hashCode += getWorkQueueDefinition().hashCode();
        }
        _hashCode += new Double(getBudget()).hashCode();
        if (getDynaminBudget() != null) {
            _hashCode += getDynaminBudget().hashCode();
        }
        _hashCode += getProcessType();
        if (getAssociatedCaseID() != null) {
            _hashCode += getAssociatedCaseID().hashCode();
        }
        _hashCode += getVariablePrefix();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProcessMapDetails.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessMapDetails"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resourceName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ResourceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("helpText");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "HelpText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastModifiedBy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "LastModifiedBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("algorithm");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Algorithm"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arrowType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrowType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("arrowWeighting");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ArrowWeighting"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("category");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Category"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetInSecs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "TargetInSecs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("priority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Priority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactional");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Transactional"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("background");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Background"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cost");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Cost"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "CreateDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("changeDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ChangeDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusInDB");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StatusInDB"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authorId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AuthorId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("straightThrough");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "StraightThrough"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workQueueDefinition");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "WorkQueueDefinition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("budget");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "Budget"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dynaminBudget");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "DynaminBudget"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("processType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "ProcessType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("associatedCaseID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "AssociatedCaseID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("variablePrefix");
        elemField.setXmlName(new javax.xml.namespace.QName("http://singularity.co.uk/webservices/spp", "VariablePrefix"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "short"));
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
