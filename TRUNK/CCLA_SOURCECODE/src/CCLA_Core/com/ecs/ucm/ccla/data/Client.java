package com.ecs.ucm.ccla.data;

import java.util.Date;

import intradoc.data.DataBinder;
import intradoc.data.DataException;

import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents an entry from the CCLA_CLIENT table, i.e. existing
 *  clients of CCLA.
 *  
 * @author Tom
 *
 */
public class Client implements Persistable {
	
	private int entityId;
	
	/** Displayable/readable client identifier code.
	 */
	private String accountCode;
	
	private String name;
	private String status;
	
	private Date dateEnrolled;
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub

	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateEnrolled() {
		return dateEnrolled;
	}

	public void setDateEnrolled(Date dateEnrolled) {
		this.dateEnrolled = dateEnrolled;
	}

}
