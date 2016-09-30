package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Vector;

import com.ecs.ucm.ccla.data.Company;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Implemented by Central DB entities that have a corresponding Aurora entity e.g.
 *  Person, Organisation.
 * 
 * @author tm
 *
 */
public interface AuroraEntitySource {

	/** Returns a ResultSet of Aurora companies associated with this entity.
	 *  
	 *  Ensure this doesn't return non-Aurora companies, e.g. the 'CCLA' one!
	 *  
	 * @return
	 */
	public DataResultSet getAuroraCompaniesData(FWFacade facade) throws DataException;
	
	/** Returns a list of Aurora companies associated with this entity.
	 *  
	 *  Ensure this doesn't return non-Aurora companies, e.g. the 'CCLA' one!
	 *  
	 * @return
	 */
	public Vector<Company> getAuroraCompanies(FWFacade facade) throws DataException;
	
	/** Returns the unique DB identifier (i.e. primary key) associated with this entity.
	 * 
	 * @return
	 */
	public String getId();
	
	/** Returns a label that is used to head comparison reports */
	public String getComparisonLabel();
}
