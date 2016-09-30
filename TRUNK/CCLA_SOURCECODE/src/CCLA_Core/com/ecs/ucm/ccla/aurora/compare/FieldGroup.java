package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.aurora.webservice.Address;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Wrapper for a single named set of associated name-value data fields. 
 * 
 * @param <C> the Central DB entity the field group is based on. Used to calculate
 * 			  amendment dates.
 * @param <A> the Aurora entity the group is associated with. Used to populate or apply
 * 			  values against the Aurora entity.
 **/
public abstract class FieldGroup<C extends AuroraEntitySource,A> {
	
	protected FieldGroupConfig config;
	
	protected FieldGroupAmendmentDates<C> amendmentDates;
	
	protected FieldGroupAttributes<A> attributes;

	protected FieldGroup(FieldGroupConfig config) throws DataException {
		this.config = config;
	}
	
	public FieldGroup(FieldGroupConfig config, FieldGroupAttributes<A> attrs) 
	 throws DataException {
		this(config);
		this.attributes = attrs;
	}

	public FieldGroupConfig getFieldGroupConfig() {
		return config;
	}
	
	public String getName() {
		return config.getName();
	}

	public String getLabel() {
		return config.getLabel();
	}
	
	public boolean isCritical() {
		return config.isCritical();
	}

	public FieldGroupAttributes<A> getFieldGroupAttributes() {
		return this.attributes;
	}
	
	/** Must be implemented by subclasses to calculate and store the amendment 
	 *  dates. */
	public abstract void calculateAmendmentDates
	 (C dbEntity, Company company, FWFacade facade) throws DataException;

	/** Fetches the amendment dates, calculating them if required. */
	public FieldGroupAmendmentDates<C> getAmendmentDates() throws DataException {
		if (this.amendmentDates == null)
			throw new DataException("Amendment Dates not yet calculated!");
		
		return this.amendmentDates;
	}
}
