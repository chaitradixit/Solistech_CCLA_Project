package com.ecs.ucm.ccla.data;

import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;

public interface Persistable {
	
	/** Extracts values from the given binder and maps them
	 *  to corresponding fields in the instance.
	 *  
	 *  Should be capable of handling missing values and
	 *  performing validation.
	 *  
	 *  You should only implement this method if the corresponding table data will
	 *  be add/updated directly from an HTML form, otherwise its not worth the hassle.
	 *  
	 * @param binder
	 */
	public void setAttributes(DataBinder binder) throws DataException;
	
	/** Adds all instance field values to the given binder.
	 *  The names used for binder values should preferably
	 *  match those used in the setAttributes method for
	 *  consistency.
	 *  
	 *  This method should be used while executing the
	 *  persist method to ensure all fields have been copied
	 *  to the binder.
	 *  
	 * @param binder
	 */
	public void addFieldsToBinder(DataBinder binder) throws DataException;
	
	/** Updates the corresponding database entry for this
	 *  instance. Should call addFieldsToBinder method before
	 *  using the FWFacade to execute the required update
	 *  query.
	 *  
	 * @param facade
	 * @param username TODO
	 */
	public void persist(FWFacade facade, String username) throws DataException;
	
	/** Checks the validity of all instance attributes. Does nothing
	 *  if the instance is considered valid, otherwise it throws
	 *  a DataException with a message indicating the error, e.g.
	 *  'Account Number missing'.
	 *  
	 *  This method should be called within the persist() method
	 *  prior to SQL update, and the new/insert method prior to SQL
	 *  insert of new objects.
	 *  
	 * @param facade use this if comparison queries need to be performed
	 * 				 as part of validation.
	 *  
	 * @throws DataException
	 */
	public void validate(FWFacade facade) throws DataException;
}
