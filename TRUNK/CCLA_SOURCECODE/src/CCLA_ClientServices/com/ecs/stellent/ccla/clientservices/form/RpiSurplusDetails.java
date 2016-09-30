package com.ecs.stellent.ccla.clientservices.form;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.subscription.RpiDrawdown;

/** Wrapper class for all RPI surplus data that gets printed on an RPI surplus drawdown
 *  form, for a single organisation.
 *  
 * @author tm
 *
 */
public class RpiSurplusDetails {
	
	private Entity org;
	
	/** All LCF donors (Person/Org instances) who may have a drawdown entry on the form. 
	 *  Expected to be sorted in ascending order by person/org name, as their ordering
	 *  here determines how they are printed on the form.
	 **/
	private List<Element> donors;
	
	/** Mapping of Donor Element IDs to their drawdown values */
	private Map<Integer, RpiDrawdown> donorDrawdowns;

	/** Mapping of Donor Element IDs to their designated deposit account */
	private Map<Integer, Account> donorAccounts;
	
	/** Correspondent - who the form will be addressed to */
	private Person correspondent;
	/** Postal address. Generally will match the correspondent's address */
	private Address address;
	
	private Date calcDate;
	private Date reportDate;
	
	private Date deadlineDate;
	private Date saleDate;
	private Date secondReportDate;
	
	public RpiSurplusDetails(
		Entity org, 
		List<Element> donors, 
		Map<Integer, RpiDrawdown> donorDrawdowns, 
		Map<Integer, Account> donorAccounts,
		Person correspondent, 
		Address address) {
		super();
		this.org = org;
		this.donors = donors;
		
		this.donorDrawdowns = donorDrawdowns;
		this.donorAccounts = donorAccounts;
		
		this.correspondent = correspondent;
		this.address = address;
	}
	
	public RpiDrawdown getDonorDrawdown(Integer donorElementId) {
		return donorDrawdowns.get(donorElementId);
	}
	
	public Account getDonorAccount(Integer donorElementId) {
		return donorAccounts.get(donorElementId);
	}
	
	public Entity getOrg() {
		return this.org;
	}
	
	public Person getCorrespondent() {
		return correspondent;
	}
	public Address getAddress() {
		return address;
	}
	
	public List<Element> getDonors() {
		return donors;
	}
	public Date getCalcDate() {
		return calcDate;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public Date getDeadlineDate() {
		return deadlineDate;
	}
	public Date getSaleDate() {
		return saleDate;
	}
	public Date getSecondReportDate() {
		return secondReportDate;
	}

	public void setCalcDate(Date calcDate) {
		this.calcDate = calcDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public void setDeadlineDate(Date deadlineDate) {
		this.deadlineDate = deadlineDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public void setSecondReportDate(Date secondReportDate) {
		this.secondReportDate = secondReportDate;
	}
}
