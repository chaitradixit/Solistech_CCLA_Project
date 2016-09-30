package com.ecs.ucm.ccla.data;

import java.util.Date;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Abstract implementor for the base Persistable interface.
 *  
 *  Provides getters/setters for four commonly-used attributes:
 *  
 *  - id
 *  - dateAdded
 *  - lastUpdated
 *  - lastUpdatedBy
 * 
 *  The ID attribute should correspond to the DAO's primary key value.
 *  
 *  The other attributes are self-explanatory.
 *  
 *  Various methods in the Persistable interface have been implemented to apply/set the
 *  four attributes.
 *  
 *  The getNewKey() method will yield a new unique key for this DAO, providing that the
 *  getSequenceName() method has been implemented to return the corresponding DB
 *  sequence name.
 * 
 * @author Tom
 *
 */
public abstract class EnhancedPersistable implements Persistable {
	
	/** Unique Identitifer (Primary Key) value for this DAO instance. */
	protected Integer id;
	
	/** Date the DAO instance was created in the DB */
	protected Date dateAdded;
	/** Date the DAO instance was last updated */
	protected Date lastUpdated;
	/** User who last updated, or added, the DAO instance to the DB */
	protected String lastUpdatedBy;
	
	public EnhancedPersistable(Integer id, Date dateAdded, Date lastUpdated,
			String lastUpdatedBy) {
		this.id = id;
		this.dateAdded = dateAdded;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/** Fetches a new key/ID value for this DAO instance.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public int getNewKey(FWFacade facade) throws DataException {
		String sql = "SELECT " + getSequenceName() + ".NEXTVAL AS KEY FROM DUAL";
		
		DataResultSet rs = facade.createResultSetSQL(sql);
		return CCLAUtils.getResultSetIntegerValue(rs, "KEY");
	}
	
	/** Returns the name of the DB sequence used to allocate key numbers to this 
	 *  EnhancedPersistable implementor.
	 *  
	 * @return the relevant sequence name, e.g. 'SEQ_ELEMENT'
	 */
	public abstract String getSequenceName();
	
	/** Returns the name of the DAO Entity, as it will be used in Audit logs etc.
	 * 
	 *  Returns the Java class name (without package scope) by default.
	 **/
	public String getEntityName() {
		return this.getClass().getSimpleName();
	}
	
	/** Returns the unique key previously assigned to this UniqueKey implementor 
	 *  instance */
	public Integer getId() {
		return this.id;
	}
	
	/** Sets the unique key for this UniqueKey implementor instance */
	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/** Adds the dateAdded, lastUpdated and lastUpdatedBy fields to the binder. */
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.DATE_ADDED, this.getDateAdded());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.LAST_UPDATED, this.getLastUpdated());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, SharedCols.LAST_UPDATED_BY, this.getLastUpdatedBy());
	}

	/** Sets the lastUpdatedBy field to match the passed username and runs the validate
	 *  method.
	 */
	public void persist(FWFacade facade, String username) throws DataException {
		this.setLastUpdatedBy(username);
		this.validate(facade);
	}

	/** Sets the dateAdded, lastUpdated and lastUpdatedBy attributes */
	public void setAttributes(DataBinder binder) throws DataException {
		this.setDateAdded(CCLAUtils.getBinderDateValue
		 (binder, SharedCols.DATE_ADDED));
		this.setLastUpdated(CCLAUtils.getBinderDateValue
		 (binder, SharedCols.LAST_UPDATED));
		this.setLastUpdatedBy(binder.getLocal
		 (SharedCols.LAST_UPDATED_BY));
	}
	
	/** Ensures the lastUpdatedBy and ID fields have non-null values 
	 * @throws DataException */
	public void validate(FWFacade facade) throws DataException {
		if (StringUtils.stringIsBlank(this.getLastUpdatedBy()))
			throw new DataException("'Last Updated By' username missing");
		
		if (this.getId() == null)
			throw new DataException("Primary Key/ID value missing");
	}
}
