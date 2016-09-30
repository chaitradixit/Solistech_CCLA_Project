package com.ecs.ucm.ccla.data.form.spool;

import intradoc.shared.SharedObjects;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ecs.utils.Log;

/** Wrapper class for all name-value pairs included in the header
 *  of every spool file.
 *  
 * @author Tom Marchant
 *
 */
public class SpoolHeader {
	
	/** Format used for 'Working Date' internal spool file
	 *  variable
	 */
	static final SimpleDateFormat WORKING_DATE_FORMAT = 
	 new SimpleDateFormat("yyyyMMdd");
	
	/** Format used for 'Print Date' internal spool file
	 *  variable
	 */
	static final SimpleDateFormat PRINT_DATE_FORMAT = 
	 new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public String company;
	public String clientNumber; // Aurora client number
	public String workingDate;
	public String userName;  
	public String workstation;      
	public String printDate;       
	public int records;  
	public String templatePath;
	public String releaseVersion;
	
	public String anacompDocType;
	public String anacompRetGrpNo;
	
	public Integer orgId;
	public Integer formId;
	
	public SpoolHeader(String company, String clientNumber, 
	 String userName, int records, Integer formId,
	 Integer orgId, String templatePath) {
		
		this.company 		= company;
		this.clientNumber   = clientNumber;
		this.userName		= userName;
		this.records		= records;
		this.templatePath	= templatePath;
		this.formId			= formId;
		this.orgId 			= orgId;
		
		Date date = new Date();
		
		this.workingDate 	= WORKING_DATE_FORMAT.format(date);
		this.printDate		= PRINT_DATE_FORMAT.format(date);
		
		// Obtain current hostname
		try {
			this.workstation	= InetAddress.getLocalHost().getHostName();
			String idcName		= SharedObjects.getEnvironmentValue("IDC_Name");
			
			if (idcName.indexOf("live") > -1)
				this.releaseVersion = "LIVE";
			else
				this.releaseVersion = "TEST";
			
		} catch (UnknownHostException e) {
			this.workstation 	= "Unknown Host";
			this.releaseVersion	= "Unknown";
		}
		
		// Legacy Anacomp variables
		this.anacompDocType = "";
		this.anacompRetGrpNo = "000";
	}
	
	/** Sets the spool file record count (i.e. number of forms) */
	public void setRecordCount(int records) {
		this.records = records;
	}
}
