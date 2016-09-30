/**
 * ValidatorPlusServiceSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.experian.validator.webservice;

public interface ValidatorPlusServiceSoap_PortType extends java.rmi.Remote {
    public com.experian.validator.webservice.DrivingLicenceResults doValidateDrivingLicence(java.lang.String drivingLicenceNumber, java.lang.String dateOfBirth, java.lang.String name, java.lang.String sex) throws java.rmi.RemoteException;
    public com.experian.validator.webservice.MailsortResults doValidateMailsort(java.lang.String dateOfIssue, java.lang.String mailsortCode, java.lang.String postcode) throws java.rmi.RemoteException;
    public com.experian.validator.webservice.MicronumberResults doValidateMicronumber(java.lang.String dateOfIssue, java.lang.String historicMailsort, java.lang.String micronumber, java.lang.String postcode) throws java.rmi.RemoteException;
    public com.experian.validator.webservice.PassportNumberResults doValidatePassport(java.lang.String dateOfBirth, java.lang.String passportNumber, java.lang.String sex, boolean isLongNumber) throws java.rmi.RemoteException;
}
