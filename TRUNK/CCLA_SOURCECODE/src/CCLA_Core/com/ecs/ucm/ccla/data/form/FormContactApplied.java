package com.ecs.ucm.ccla.data.form;

import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the FORM_CONTACT_APPLIED table. This is used to store a mapping
 *  of Form IDs to Contact Point IDs.
 *  
 *  E.g. an Email Indemnity form will store any email address contacts here which are 
 *  pre-populated on the form.
 *  
 *  These can only be added (update methods won't work).
 * 
 * @author Tom
 *
 */
public class FormContactApplied implements Persistable {

	private int formId;
	private int contactId;
	
	public FormContactApplied(int formId, int contactId) {
		this.formId = formId;
		this.contactId = contactId;
	}
	
	public static void add(int formId, int contactId, FWFacade facade) 
	 throws DataException {
		FormContactApplied contactAppl = new FormContactApplied(formId, contactId);
		
		DataBinder binder = new DataBinder();
		contactAppl.addFieldsToBinder(binder);
		
		facade.execute("qCore_AddFormContactApplied", binder);
	}
	
	public static FormContactApplied get(DataResultSet rs) throws DataException {
		return new FormContactApplied(
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.FORM),
			CCLAUtils.getResultSetIntegerValue(rs, Contact.Cols.ID)
		);
	}
	
	/** Fetches a list of all Contacts which are mapped to the given Form ID. */
	public static Vector<Contact> getByFormId(int formId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.FORM, formId);
		
		DataResultSet rsFormContacts = facade.createResultSet(
		 "qCore_GetFormContactsByFormId", binder);
		
		Vector<Contact> formContacts = new Vector<Contact>();
		
		if (rsFormContacts.first()) {
			do {
				formContacts.add(Contact.get(rsFormContacts, facade));
			} while (rsFormContacts.next());
		}
		
		return formContacts;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.FORM, this.getFormId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Contact.Cols.ID, this.getContactId());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		throw new DataException("Not implemented");
	}

	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public int getFormId() {
		return formId;
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public int getContactId() {
		return contactId;
	}
}
