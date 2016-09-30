package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.audit.SDAudit;
import com.ecs.ucm.ccla.data.audit.SDAuditData;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Captures and stores a labelled list of amendment dates that corresponds to attributes
 *  and entities in the Central DB, that are related to a parent FieldGroup.
 *  
 *  As dates are calculated and captured, the instance keeps a record of the 'latest
 *  amendment date' - this will be used to determine the last time the Central DB
 *  attributes/entities linked to the parent FieldGroup were modified.
 *  
 *  There won't necessarily be a 1-1 correspondence between FieldGroup field values and
 *  FieldGroup amendment date entries.
 * 
 * @param C 	the DB entity that the amendment dates will be based from
 * @author tm
 *
 */
public abstract class FieldGroupAmendmentDates<C extends AuroraEntitySource> {
	
	protected C dbEntity;
	protected Company company;
	protected FWFacade facade;
	
	/** A list of labels and their associated date. The mapped date may be null.
	 * 
	 */
	protected LinkedHashMap<String, Date> amendmentDates;
	
	/** Key to the latest amendment date in the map. */
	protected String latestAmendmentLabel;
	
	public static SimpleDateFormat DATE_FORMAT =
	 new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	/** Outputs the latest amendment date, plus an ordered list of all captured amendment
	 *  dates and their labels.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if (this.amendmentDates == null)
			return "FieldSetAmendmentDates: [not initialised]";
		
		Date latest = null;
		try {
			latest = getLatestAmendmentDate();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sb.append("FieldSetAmendmentDates: latest amendment=" + 
		 (latest != null ? DATE_FORMAT.format(latest) : null) + ", label=" + 
		 latestAmendmentLabel + ", no. dates=" + amendmentDates.size());
		
		int line = 1;
		
		for (Map.Entry<String, Date> amendmentDate : amendmentDates.entrySet()) {
			sb.append("\n\t" + line + ": " + amendmentDate.getKey() + "=" + 
			 (amendmentDate.getValue() == null ? null : 
			 DATE_FORMAT.format(amendmentDate.getValue())));
			
			line++;
		}
		
		return sb.toString();
	}
	
	protected FieldGroupAmendmentDates() {}
	
	public FieldGroupAmendmentDates(C dbEntity, Company company, FWFacade facade) 
	 throws DataException {
		this.dbEntity = dbEntity;
		this.company = company;
		this.facade = facade;
		
		this.amendmentDates = new LinkedHashMap<String, Date>();
		this.latestAmendmentLabel = null;
		
		this.captureAmendmentDates();
	}
	
	/** Override this to capture the relevant amendment dates from the Central DB. */
	protected abstract void captureAmendmentDates() throws DataException;
	
	/** Used internally to append new amendment dates, and update the latest amendment
	 *  key if applicable.
	 *  
	 * @param label
	 * @param date
	 * @throws DataException 
	 */
	protected void addAmendmentDate(String label, Date date) throws DataException {
		amendmentDates.put(label, date);
		
		if ((latestAmendmentLabel == null && date != null)
			|| (date != null && date.after(getLatestAmendmentDate()))) {
			latestAmendmentLabel = label;
		}
	}
	
	/** Fetches the amendment dates belonging to the member entity. 
	 * 
	 **/
	public LinkedHashMap<String, Date> getAmendmentDates() throws DataException {
		return this.amendmentDates;
	}
	
	/** Returns the latest amendment date label 
	 * 
	 * @throws DataException */
	public String getLatestAmendmentLabel() throws DataException {
		if (this.amendmentDates == null)
			throw new DataException("Amendment Dates not calculated yet!");
		
		return latestAmendmentLabel;
	}
	
	/** Returns the latest amendment date. 
	 * 
	 * @throws DataException */
	public Date getLatestAmendmentDate() throws DataException {
		if (this.amendmentDates == null)
			throw new DataException("Amendment Dates not calculated yet!");
		
		if (latestAmendmentLabel != null) {
			return amendmentDates.get(latestAmendmentLabel);
		} else {
			return null;
		}
	}
}
