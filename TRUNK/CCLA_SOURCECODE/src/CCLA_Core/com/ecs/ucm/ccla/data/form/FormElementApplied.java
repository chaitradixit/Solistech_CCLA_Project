package com.ecs.ucm.ccla.data.form;

import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents entries in the FORM_ELEMENT_APPLIED table and provides various
 *  helper methods for fetching the mapped Element entries.
 *  
 * @author Tom
 *
 */
public class FormElementApplied implements Persistable {

	private int formId;
	private int elementId;

	public FormElementApplied(int formId, int elementId) {
		this.formId = formId;
		this.elementId = elementId;
	}
	
	public static FormElementApplied add(int formId, int elementId, FWFacade facade) 
	 throws DataException {
		FormElementApplied fea = new FormElementApplied(formId, elementId);
		
		DataBinder binder = new DataBinder();
		fea.addFieldsToBinder(binder);
		
		facade.execute("qCore_AddFormElementApplied", binder);
		
		return fea;
	}

	/** Fetches a ResultSet of all mapped ELEMENT entries to the given Form ID.
	 * 
	 * @param formId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getFormElementsAppliedData
	 (int formId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.FORM, formId);
		
		return facade.createResultSet("qCore_GetFormElementApplied", binder);
	}
	
	/** Fetches all Element instances mapped to the given Form ID.
	 * 
	 * @param formId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<Element> getFormElementsApplied(int formId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getFormElementsAppliedData(formId, facade);
		Vector<Element> elements = new Vector<Element>();
		
		if (rs.first()) {
			do {
				elements.add(Element.get(rs));
			} while (rs.next());
		}
		
		return elements;
	}
	
	/** Fetches all Element instances mapped to the given Form ID, filters them by the
	 *  given ElementType and returns a list of Element subclasses, e.g. a list of
	 *  Accounts or a list of Persons.
	 *  
	 *  Currently only supports Accounts/Persons.
	 * 
	 * @param formId
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws DataException
	 */
	public static Vector<? extends Element> getFormElementsAppliedByType
	 (int formId, ElementType elementType, FWFacade facade) throws DataException {
		
		Vector<Element> elements = getFormElementsApplied(formId, facade);
		
		if (elementType.equals(ElementType.ACCOUNT)) {
			Vector<Account> accounts = new Vector<Account>();
			
			for (Element elem : elements) {
				if (elem.getType().equals(elementType))
					accounts.add(Account.get(elem.getElementId(), facade));
			}
			
			return accounts;
		}
		
		if (elementType.equals(ElementType.PERSON)) {
			Vector<Person> persons = new Vector<Person>();
			
			for (Element elem : elements) {
				if (elem.getType().equals(elementType))
					persons.add(Person.get(elem.getElementId(), facade));
			}
			
			return persons;
		}
		
		if (elementType.equals(ElementType.BANK_ACCOUNT)) {
			Vector<BankAccount> bankAccounts = new Vector<BankAccount>();
			
			for (Element elem : elements) {
				if (elem.getType().equals(elementType))
					bankAccounts.add(BankAccount.get(elem.getElementId(), facade));
			}
			
			return bankAccounts;
		}
		
		throw new DataException("Unsupported mapped ElementType: " 
		 + elementType.getName());
	}
	
	@SuppressWarnings("unchecked")
	public static Vector<Account> getFormAccounts
	 (int formId, ElementType elementType, FWFacade facade) throws DataException {
		
		return (Vector<Account>) 
		 getFormElementsAppliedByType(formId, elementType, facade);	
	}
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.FORM, this.getFormId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.ELEMENT, this.getElementId());
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

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public int getFormId() {
		return formId;
	}

	public void setElementId(int elementId) {
		this.elementId = elementId;
	}

	public int getElementId() {
		return elementId;
	}

}
