/**
 * DepositBatchItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.aurora.webservice;

public class DepositBatchItem  extends com.aurora.webservice.BatchItem  implements java.io.Serializable {
    private java.lang.String forwardDatedExecutionDate;

    private java.lang.String contractNumber;

    private int batchNumber;

    private int itemNumber;

    private int lineNumber;

    private int screenNumber;

    private java.lang.String lineType;

    private boolean chequeSupIndicator;

    private java.lang.String tradeTypeCode;

    private java.lang.String paymentMethodCode;

    private boolean paymentOverrideIndicator;

    private boolean deletedIndicator;

    private boolean cancelledIndicator;

    private double discount;

    private java.lang.String sourceCompany;

    private java.lang.String sourceAccountNumberExt;

    private double sourceAmount;

    private double sourceUnits;

    private int sourceDecimalPlaces;

    private java.lang.String destinationCompany;

    private java.lang.String destinationAccountNumberExt;

    private double destinationAmount;

    private double destinationUnits;

    private int destinationDecimalPlaces;

    private java.lang.String payeeName;

    private java.lang.String bankSortCode;

    private java.lang.String bankAccountNumber;

    private java.lang.String bankAccountName;

    private java.lang.String clientName;

    private java.lang.String bankSortCodeReceipt;

    private java.lang.String bankAccountNumberReceipt;

    private double amountReceipt;

    public DepositBatchItem() {
    }

    public DepositBatchItem(
           java.lang.String forwardDatedExecutionDate,
           java.lang.String contractNumber,
           int batchNumber,
           int itemNumber,
           int lineNumber,
           int screenNumber,
           java.lang.String lineType,
           boolean chequeSupIndicator,
           java.lang.String tradeTypeCode,
           java.lang.String paymentMethodCode,
           boolean paymentOverrideIndicator,
           boolean deletedIndicator,
           boolean cancelledIndicator,
           double discount,
           java.lang.String sourceCompany,
           java.lang.String sourceAccountNumberExt,
           double sourceAmount,
           double sourceUnits,
           int sourceDecimalPlaces,
           java.lang.String destinationCompany,
           java.lang.String destinationAccountNumberExt,
           double destinationAmount,
           double destinationUnits,
           int destinationDecimalPlaces,
           java.lang.String payeeName,
           java.lang.String bankSortCode,
           java.lang.String bankAccountNumber,
           java.lang.String bankAccountName,
           java.lang.String clientName,
           java.lang.String bankSortCodeReceipt,
           java.lang.String bankAccountNumberReceipt,
           double amountReceipt) {
        this.forwardDatedExecutionDate = forwardDatedExecutionDate;
        this.contractNumber = contractNumber;
        this.batchNumber = batchNumber;
        this.itemNumber = itemNumber;
        this.lineNumber = lineNumber;
        this.screenNumber = screenNumber;
        this.lineType = lineType;
        this.chequeSupIndicator = chequeSupIndicator;
        this.tradeTypeCode = tradeTypeCode;
        this.paymentMethodCode = paymentMethodCode;
        this.paymentOverrideIndicator = paymentOverrideIndicator;
        this.deletedIndicator = deletedIndicator;
        this.cancelledIndicator = cancelledIndicator;
        this.discount = discount;
        this.sourceCompany = sourceCompany;
        this.sourceAccountNumberExt = sourceAccountNumberExt;
        this.sourceAmount = sourceAmount;
        this.sourceUnits = sourceUnits;
        this.sourceDecimalPlaces = sourceDecimalPlaces;
        this.destinationCompany = destinationCompany;
        this.destinationAccountNumberExt = destinationAccountNumberExt;
        this.destinationAmount = destinationAmount;
        this.destinationUnits = destinationUnits;
        this.destinationDecimalPlaces = destinationDecimalPlaces;
        this.payeeName = payeeName;
        this.bankSortCode = bankSortCode;
        this.bankAccountNumber = bankAccountNumber;
        this.bankAccountName = bankAccountName;
        this.clientName = clientName;
        this.bankSortCodeReceipt = bankSortCodeReceipt;
        this.bankAccountNumberReceipt = bankAccountNumberReceipt;
        this.amountReceipt = amountReceipt;
    }


    /**
     * Gets the forwardDatedExecutionDate value for this DepositBatchItem.
     * 
     * @return forwardDatedExecutionDate
     */
    public java.lang.String getForwardDatedExecutionDate() {
        return forwardDatedExecutionDate;
    }


    /**
     * Sets the forwardDatedExecutionDate value for this DepositBatchItem.
     * 
     * @param forwardDatedExecutionDate
     */
    public void setForwardDatedExecutionDate(java.lang.String forwardDatedExecutionDate) {
        this.forwardDatedExecutionDate = forwardDatedExecutionDate;
    }


    /**
     * Gets the contractNumber value for this DepositBatchItem.
     * 
     * @return contractNumber
     */
    public java.lang.String getContractNumber() {
        return contractNumber;
    }


    /**
     * Sets the contractNumber value for this DepositBatchItem.
     * 
     * @param contractNumber
     */
    public void setContractNumber(java.lang.String contractNumber) {
        this.contractNumber = contractNumber;
    }


    /**
     * Gets the batchNumber value for this DepositBatchItem.
     * 
     * @return batchNumber
     */
    public int getBatchNumber() {
        return batchNumber;
    }


    /**
     * Sets the batchNumber value for this DepositBatchItem.
     * 
     * @param batchNumber
     */
    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }


    /**
     * Gets the itemNumber value for this DepositBatchItem.
     * 
     * @return itemNumber
     */
    public int getItemNumber() {
        return itemNumber;
    }


    /**
     * Sets the itemNumber value for this DepositBatchItem.
     * 
     * @param itemNumber
     */
    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }


    /**
     * Gets the lineNumber value for this DepositBatchItem.
     * 
     * @return lineNumber
     */
    public int getLineNumber() {
        return lineNumber;
    }


    /**
     * Sets the lineNumber value for this DepositBatchItem.
     * 
     * @param lineNumber
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }


    /**
     * Gets the screenNumber value for this DepositBatchItem.
     * 
     * @return screenNumber
     */
    public int getScreenNumber() {
        return screenNumber;
    }


    /**
     * Sets the screenNumber value for this DepositBatchItem.
     * 
     * @param screenNumber
     */
    public void setScreenNumber(int screenNumber) {
        this.screenNumber = screenNumber;
    }


    /**
     * Gets the lineType value for this DepositBatchItem.
     * 
     * @return lineType
     */
    public java.lang.String getLineType() {
        return lineType;
    }


    /**
     * Sets the lineType value for this DepositBatchItem.
     * 
     * @param lineType
     */
    public void setLineType(java.lang.String lineType) {
        this.lineType = lineType;
    }


    /**
     * Gets the chequeSupIndicator value for this DepositBatchItem.
     * 
     * @return chequeSupIndicator
     */
    public boolean isChequeSupIndicator() {
        return chequeSupIndicator;
    }


    /**
     * Sets the chequeSupIndicator value for this DepositBatchItem.
     * 
     * @param chequeSupIndicator
     */
    public void setChequeSupIndicator(boolean chequeSupIndicator) {
        this.chequeSupIndicator = chequeSupIndicator;
    }


    /**
     * Gets the tradeTypeCode value for this DepositBatchItem.
     * 
     * @return tradeTypeCode
     */
    public java.lang.String getTradeTypeCode() {
        return tradeTypeCode;
    }


    /**
     * Sets the tradeTypeCode value for this DepositBatchItem.
     * 
     * @param tradeTypeCode
     */
    public void setTradeTypeCode(java.lang.String tradeTypeCode) {
        this.tradeTypeCode = tradeTypeCode;
    }


    /**
     * Gets the paymentMethodCode value for this DepositBatchItem.
     * 
     * @return paymentMethodCode
     */
    public java.lang.String getPaymentMethodCode() {
        return paymentMethodCode;
    }


    /**
     * Sets the paymentMethodCode value for this DepositBatchItem.
     * 
     * @param paymentMethodCode
     */
    public void setPaymentMethodCode(java.lang.String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }


    /**
     * Gets the paymentOverrideIndicator value for this DepositBatchItem.
     * 
     * @return paymentOverrideIndicator
     */
    public boolean isPaymentOverrideIndicator() {
        return paymentOverrideIndicator;
    }


    /**
     * Sets the paymentOverrideIndicator value for this DepositBatchItem.
     * 
     * @param paymentOverrideIndicator
     */
    public void setPaymentOverrideIndicator(boolean paymentOverrideIndicator) {
        this.paymentOverrideIndicator = paymentOverrideIndicator;
    }


    /**
     * Gets the deletedIndicator value for this DepositBatchItem.
     * 
     * @return deletedIndicator
     */
    public boolean isDeletedIndicator() {
        return deletedIndicator;
    }


    /**
     * Sets the deletedIndicator value for this DepositBatchItem.
     * 
     * @param deletedIndicator
     */
    public void setDeletedIndicator(boolean deletedIndicator) {
        this.deletedIndicator = deletedIndicator;
    }


    /**
     * Gets the cancelledIndicator value for this DepositBatchItem.
     * 
     * @return cancelledIndicator
     */
    public boolean isCancelledIndicator() {
        return cancelledIndicator;
    }


    /**
     * Sets the cancelledIndicator value for this DepositBatchItem.
     * 
     * @param cancelledIndicator
     */
    public void setCancelledIndicator(boolean cancelledIndicator) {
        this.cancelledIndicator = cancelledIndicator;
    }


    /**
     * Gets the discount value for this DepositBatchItem.
     * 
     * @return discount
     */
    public double getDiscount() {
        return discount;
    }


    /**
     * Sets the discount value for this DepositBatchItem.
     * 
     * @param discount
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }


    /**
     * Gets the sourceCompany value for this DepositBatchItem.
     * 
     * @return sourceCompany
     */
    public java.lang.String getSourceCompany() {
        return sourceCompany;
    }


    /**
     * Sets the sourceCompany value for this DepositBatchItem.
     * 
     * @param sourceCompany
     */
    public void setSourceCompany(java.lang.String sourceCompany) {
        this.sourceCompany = sourceCompany;
    }


    /**
     * Gets the sourceAccountNumberExt value for this DepositBatchItem.
     * 
     * @return sourceAccountNumberExt
     */
    public java.lang.String getSourceAccountNumberExt() {
        return sourceAccountNumberExt;
    }


    /**
     * Sets the sourceAccountNumberExt value for this DepositBatchItem.
     * 
     * @param sourceAccountNumberExt
     */
    public void setSourceAccountNumberExt(java.lang.String sourceAccountNumberExt) {
        this.sourceAccountNumberExt = sourceAccountNumberExt;
    }


    /**
     * Gets the sourceAmount value for this DepositBatchItem.
     * 
     * @return sourceAmount
     */
    public double getSourceAmount() {
        return sourceAmount;
    }


    /**
     * Sets the sourceAmount value for this DepositBatchItem.
     * 
     * @param sourceAmount
     */
    public void setSourceAmount(double sourceAmount) {
        this.sourceAmount = sourceAmount;
    }


    /**
     * Gets the sourceUnits value for this DepositBatchItem.
     * 
     * @return sourceUnits
     */
    public double getSourceUnits() {
        return sourceUnits;
    }


    /**
     * Sets the sourceUnits value for this DepositBatchItem.
     * 
     * @param sourceUnits
     */
    public void setSourceUnits(double sourceUnits) {
        this.sourceUnits = sourceUnits;
    }


    /**
     * Gets the sourceDecimalPlaces value for this DepositBatchItem.
     * 
     * @return sourceDecimalPlaces
     */
    public int getSourceDecimalPlaces() {
        return sourceDecimalPlaces;
    }


    /**
     * Sets the sourceDecimalPlaces value for this DepositBatchItem.
     * 
     * @param sourceDecimalPlaces
     */
    public void setSourceDecimalPlaces(int sourceDecimalPlaces) {
        this.sourceDecimalPlaces = sourceDecimalPlaces;
    }


    /**
     * Gets the destinationCompany value for this DepositBatchItem.
     * 
     * @return destinationCompany
     */
    public java.lang.String getDestinationCompany() {
        return destinationCompany;
    }


    /**
     * Sets the destinationCompany value for this DepositBatchItem.
     * 
     * @param destinationCompany
     */
    public void setDestinationCompany(java.lang.String destinationCompany) {
        this.destinationCompany = destinationCompany;
    }


    /**
     * Gets the destinationAccountNumberExt value for this DepositBatchItem.
     * 
     * @return destinationAccountNumberExt
     */
    public java.lang.String getDestinationAccountNumberExt() {
        return destinationAccountNumberExt;
    }


    /**
     * Sets the destinationAccountNumberExt value for this DepositBatchItem.
     * 
     * @param destinationAccountNumberExt
     */
    public void setDestinationAccountNumberExt(java.lang.String destinationAccountNumberExt) {
        this.destinationAccountNumberExt = destinationAccountNumberExt;
    }


    /**
     * Gets the destinationAmount value for this DepositBatchItem.
     * 
     * @return destinationAmount
     */
    public double getDestinationAmount() {
        return destinationAmount;
    }


    /**
     * Sets the destinationAmount value for this DepositBatchItem.
     * 
     * @param destinationAmount
     */
    public void setDestinationAmount(double destinationAmount) {
        this.destinationAmount = destinationAmount;
    }


    /**
     * Gets the destinationUnits value for this DepositBatchItem.
     * 
     * @return destinationUnits
     */
    public double getDestinationUnits() {
        return destinationUnits;
    }


    /**
     * Sets the destinationUnits value for this DepositBatchItem.
     * 
     * @param destinationUnits
     */
    public void setDestinationUnits(double destinationUnits) {
        this.destinationUnits = destinationUnits;
    }


    /**
     * Gets the destinationDecimalPlaces value for this DepositBatchItem.
     * 
     * @return destinationDecimalPlaces
     */
    public int getDestinationDecimalPlaces() {
        return destinationDecimalPlaces;
    }


    /**
     * Sets the destinationDecimalPlaces value for this DepositBatchItem.
     * 
     * @param destinationDecimalPlaces
     */
    public void setDestinationDecimalPlaces(int destinationDecimalPlaces) {
        this.destinationDecimalPlaces = destinationDecimalPlaces;
    }


    /**
     * Gets the payeeName value for this DepositBatchItem.
     * 
     * @return payeeName
     */
    public java.lang.String getPayeeName() {
        return payeeName;
    }


    /**
     * Sets the payeeName value for this DepositBatchItem.
     * 
     * @param payeeName
     */
    public void setPayeeName(java.lang.String payeeName) {
        this.payeeName = payeeName;
    }


    /**
     * Gets the bankSortCode value for this DepositBatchItem.
     * 
     * @return bankSortCode
     */
    public java.lang.String getBankSortCode() {
        return bankSortCode;
    }


    /**
     * Sets the bankSortCode value for this DepositBatchItem.
     * 
     * @param bankSortCode
     */
    public void setBankSortCode(java.lang.String bankSortCode) {
        this.bankSortCode = bankSortCode;
    }


    /**
     * Gets the bankAccountNumber value for this DepositBatchItem.
     * 
     * @return bankAccountNumber
     */
    public java.lang.String getBankAccountNumber() {
        return bankAccountNumber;
    }


    /**
     * Sets the bankAccountNumber value for this DepositBatchItem.
     * 
     * @param bankAccountNumber
     */
    public void setBankAccountNumber(java.lang.String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }


    /**
     * Gets the bankAccountName value for this DepositBatchItem.
     * 
     * @return bankAccountName
     */
    public java.lang.String getBankAccountName() {
        return bankAccountName;
    }


    /**
     * Sets the bankAccountName value for this DepositBatchItem.
     * 
     * @param bankAccountName
     */
    public void setBankAccountName(java.lang.String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }


    /**
     * Gets the clientName value for this DepositBatchItem.
     * 
     * @return clientName
     */
    public java.lang.String getClientName() {
        return clientName;
    }


    /**
     * Sets the clientName value for this DepositBatchItem.
     * 
     * @param clientName
     */
    public void setClientName(java.lang.String clientName) {
        this.clientName = clientName;
    }


    /**
     * Gets the bankSortCodeReceipt value for this DepositBatchItem.
     * 
     * @return bankSortCodeReceipt
     */
    public java.lang.String getBankSortCodeReceipt() {
        return bankSortCodeReceipt;
    }


    /**
     * Sets the bankSortCodeReceipt value for this DepositBatchItem.
     * 
     * @param bankSortCodeReceipt
     */
    public void setBankSortCodeReceipt(java.lang.String bankSortCodeReceipt) {
        this.bankSortCodeReceipt = bankSortCodeReceipt;
    }


    /**
     * Gets the bankAccountNumberReceipt value for this DepositBatchItem.
     * 
     * @return bankAccountNumberReceipt
     */
    public java.lang.String getBankAccountNumberReceipt() {
        return bankAccountNumberReceipt;
    }


    /**
     * Sets the bankAccountNumberReceipt value for this DepositBatchItem.
     * 
     * @param bankAccountNumberReceipt
     */
    public void setBankAccountNumberReceipt(java.lang.String bankAccountNumberReceipt) {
        this.bankAccountNumberReceipt = bankAccountNumberReceipt;
    }


    /**
     * Gets the amountReceipt value for this DepositBatchItem.
     * 
     * @return amountReceipt
     */
    public double getAmountReceipt() {
        return amountReceipt;
    }


    /**
     * Sets the amountReceipt value for this DepositBatchItem.
     * 
     * @param amountReceipt
     */
    public void setAmountReceipt(double amountReceipt) {
        this.amountReceipt = amountReceipt;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DepositBatchItem)) return false;
        DepositBatchItem other = (DepositBatchItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.forwardDatedExecutionDate==null && other.getForwardDatedExecutionDate()==null) || 
             (this.forwardDatedExecutionDate!=null &&
              this.forwardDatedExecutionDate.equals(other.getForwardDatedExecutionDate()))) &&
            ((this.contractNumber==null && other.getContractNumber()==null) || 
             (this.contractNumber!=null &&
              this.contractNumber.equals(other.getContractNumber()))) &&
            this.batchNumber == other.getBatchNumber() &&
            this.itemNumber == other.getItemNumber() &&
            this.lineNumber == other.getLineNumber() &&
            this.screenNumber == other.getScreenNumber() &&
            ((this.lineType==null && other.getLineType()==null) || 
             (this.lineType!=null &&
              this.lineType.equals(other.getLineType()))) &&
            this.chequeSupIndicator == other.isChequeSupIndicator() &&
            ((this.tradeTypeCode==null && other.getTradeTypeCode()==null) || 
             (this.tradeTypeCode!=null &&
              this.tradeTypeCode.equals(other.getTradeTypeCode()))) &&
            ((this.paymentMethodCode==null && other.getPaymentMethodCode()==null) || 
             (this.paymentMethodCode!=null &&
              this.paymentMethodCode.equals(other.getPaymentMethodCode()))) &&
            this.paymentOverrideIndicator == other.isPaymentOverrideIndicator() &&
            this.deletedIndicator == other.isDeletedIndicator() &&
            this.cancelledIndicator == other.isCancelledIndicator() &&
            this.discount == other.getDiscount() &&
            ((this.sourceCompany==null && other.getSourceCompany()==null) || 
             (this.sourceCompany!=null &&
              this.sourceCompany.equals(other.getSourceCompany()))) &&
            ((this.sourceAccountNumberExt==null && other.getSourceAccountNumberExt()==null) || 
             (this.sourceAccountNumberExt!=null &&
              this.sourceAccountNumberExt.equals(other.getSourceAccountNumberExt()))) &&
            this.sourceAmount == other.getSourceAmount() &&
            this.sourceUnits == other.getSourceUnits() &&
            this.sourceDecimalPlaces == other.getSourceDecimalPlaces() &&
            ((this.destinationCompany==null && other.getDestinationCompany()==null) || 
             (this.destinationCompany!=null &&
              this.destinationCompany.equals(other.getDestinationCompany()))) &&
            ((this.destinationAccountNumberExt==null && other.getDestinationAccountNumberExt()==null) || 
             (this.destinationAccountNumberExt!=null &&
              this.destinationAccountNumberExt.equals(other.getDestinationAccountNumberExt()))) &&
            this.destinationAmount == other.getDestinationAmount() &&
            this.destinationUnits == other.getDestinationUnits() &&
            this.destinationDecimalPlaces == other.getDestinationDecimalPlaces() &&
            ((this.payeeName==null && other.getPayeeName()==null) || 
             (this.payeeName!=null &&
              this.payeeName.equals(other.getPayeeName()))) &&
            ((this.bankSortCode==null && other.getBankSortCode()==null) || 
             (this.bankSortCode!=null &&
              this.bankSortCode.equals(other.getBankSortCode()))) &&
            ((this.bankAccountNumber==null && other.getBankAccountNumber()==null) || 
             (this.bankAccountNumber!=null &&
              this.bankAccountNumber.equals(other.getBankAccountNumber()))) &&
            ((this.bankAccountName==null && other.getBankAccountName()==null) || 
             (this.bankAccountName!=null &&
              this.bankAccountName.equals(other.getBankAccountName()))) &&
            ((this.clientName==null && other.getClientName()==null) || 
             (this.clientName!=null &&
              this.clientName.equals(other.getClientName()))) &&
            ((this.bankSortCodeReceipt==null && other.getBankSortCodeReceipt()==null) || 
             (this.bankSortCodeReceipt!=null &&
              this.bankSortCodeReceipt.equals(other.getBankSortCodeReceipt()))) &&
            ((this.bankAccountNumberReceipt==null && other.getBankAccountNumberReceipt()==null) || 
             (this.bankAccountNumberReceipt!=null &&
              this.bankAccountNumberReceipt.equals(other.getBankAccountNumberReceipt()))) &&
            this.amountReceipt == other.getAmountReceipt();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getForwardDatedExecutionDate() != null) {
            _hashCode += getForwardDatedExecutionDate().hashCode();
        }
        if (getContractNumber() != null) {
            _hashCode += getContractNumber().hashCode();
        }
        _hashCode += getBatchNumber();
        _hashCode += getItemNumber();
        _hashCode += getLineNumber();
        _hashCode += getScreenNumber();
        if (getLineType() != null) {
            _hashCode += getLineType().hashCode();
        }
        _hashCode += (isChequeSupIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getTradeTypeCode() != null) {
            _hashCode += getTradeTypeCode().hashCode();
        }
        if (getPaymentMethodCode() != null) {
            _hashCode += getPaymentMethodCode().hashCode();
        }
        _hashCode += (isPaymentOverrideIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isDeletedIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isCancelledIndicator() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += new Double(getDiscount()).hashCode();
        if (getSourceCompany() != null) {
            _hashCode += getSourceCompany().hashCode();
        }
        if (getSourceAccountNumberExt() != null) {
            _hashCode += getSourceAccountNumberExt().hashCode();
        }
        _hashCode += new Double(getSourceAmount()).hashCode();
        _hashCode += new Double(getSourceUnits()).hashCode();
        _hashCode += getSourceDecimalPlaces();
        if (getDestinationCompany() != null) {
            _hashCode += getDestinationCompany().hashCode();
        }
        if (getDestinationAccountNumberExt() != null) {
            _hashCode += getDestinationAccountNumberExt().hashCode();
        }
        _hashCode += new Double(getDestinationAmount()).hashCode();
        _hashCode += new Double(getDestinationUnits()).hashCode();
        _hashCode += getDestinationDecimalPlaces();
        if (getPayeeName() != null) {
            _hashCode += getPayeeName().hashCode();
        }
        if (getBankSortCode() != null) {
            _hashCode += getBankSortCode().hashCode();
        }
        if (getBankAccountNumber() != null) {
            _hashCode += getBankAccountNumber().hashCode();
        }
        if (getBankAccountName() != null) {
            _hashCode += getBankAccountName().hashCode();
        }
        if (getClientName() != null) {
            _hashCode += getClientName().hashCode();
        }
        if (getBankSortCodeReceipt() != null) {
            _hashCode += getBankSortCodeReceipt().hashCode();
        }
        if (getBankAccountNumberReceipt() != null) {
            _hashCode += getBankAccountNumberReceipt().hashCode();
        }
        _hashCode += new Double(getAmountReceipt()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DepositBatchItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DepositBatchItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("forwardDatedExecutionDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ForwardDatedExecutionDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contractNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ContractNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("batchNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BatchNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("itemNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ItemNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "LineNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("screenNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ScreenNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lineType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "LineType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("chequeSupIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ChequeSupIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tradeTypeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "TradeTypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentMethodCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PaymentMethodCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentOverrideIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PaymentOverrideIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deletedIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DeletedIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cancelledIndicator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "CancelledIndicator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("discount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "Discount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceCompany");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "SourceCompany"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceAccountNumberExt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "SourceAccountNumberExt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "SourceAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceUnits");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "SourceUnits"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceDecimalPlaces");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "SourceDecimalPlaces"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationCompany");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DestinationCompany"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationAccountNumberExt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DestinationAccountNumberExt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationAmount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DestinationAmount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationUnits");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DestinationUnits"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("destinationDecimalPlaces");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "DestinationDecimalPlaces"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payeeName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "PayeeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankSortCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankSortCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankAccountNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankAccountNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankAccountName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankAccountName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clientName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "ClientName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankSortCodeReceipt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankSortCodeReceipt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankAccountNumberReceipt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "BankAccountNumberReceipt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amountReceipt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ccla.co.uk/AuroraWS", "AmountReceipt"));
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
