package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.Map;
import java.util.Vector;

import com.ecs.ucm.ccla.aurora.AuroraSignatories;
import com.ecs.ucm.ccla.aurora.AuroraSignatory;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationProperty;
import com.ecs.ucm.ccla.data.RelationPropertyApplied;
import com.ecs.ucm.ccla.data.audit.SDAudit;
import com.ecs.ucm.ccla.data.audit.SDAuditData;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Base class that must be extended by each Aurora Entity you need to implement
 *  comparison checks/partial updates for.
 *  
 *  The list of associated FieldGroups for the entity is initialised in the constructor.
 *  
 * @author tm
 *
 * @param <C> Central DB entity type e.g. Person
 * @param <A> Aurora entity type e.g. Correspondent
 */
public abstract class AuroraFieldSet<C extends AuroraEntitySource,A> {
	
	/** Populated in constructor. */
	protected Vector<FieldGroup<C,A>> fieldGroups;
	
	protected boolean attributesSet = false;
	protected boolean amendmentDatesCalculated = false;
	
	/** Generates a FieldSet instance and initialises the Field Groups.
	 * 
	 * @throws DataException
	 */
	public AuroraFieldSet()throws DataException {
		createFieldGroups();
	}
	
	/** Initialises the Field Groups and then allocates their attribute values based on 
	 *  the passed Aurora entity.
	 *  
	 * @param auroraEntity
	 * @throws DataException
	 */
	public AuroraFieldSet(A auroraEntity) throws DataException {
		this();
		
		if (auroraEntity != null)
			setAttributeValuesFromEntity(auroraEntity);
	}
	
	/** Called in constructor - used to construct the list of field groups for the Aurora 
	 *  entity.
	 *  
	 *  Doesn't actually populate them with data. Call setAttributeValuesFromEntity
	 *  to do this.
	 *  
	 * @return
	 */
	protected abstract Vector<FieldGroup<C,A>> createFieldGroups() throws DataException;
	
	/** Stores the attribute values of the passed Aurora entity against the constituent 
	 *  FieldGroup instances.
	 * 
	 * @param auroraEntity
	 * @throws DataException
	 */
	public void setAttributeValuesFromEntity(A auroraEntity) throws DataException {
		if (attributesSet)
			throw new DataException("Attributes already set!");

		for (FieldGroup<C,A> fieldGroup : this.fieldGroups) {
			fieldGroup.getFieldGroupAttributes().setValuesFromEntity(auroraEntity);
		}
		
		attributesSet = true;
	}
	
	/** Applies the attribute values stored within ALL the constituent FieldGroup 
	 *  instances against the passed Aurora entity.
	 * 
	 * @param auroraEntity
	 * @throws DataException
	 */
	public void applyAttributeValuesToEntity(A auroraEntity) throws DataException {
		if (!attributesSet)
			throw new DataException("Attributes not set against Field Groups, " +
			 "nothing to apply!");
		
		for (FieldGroup<C,A> fieldGroup : this.fieldGroups) {
			fieldGroup.getFieldGroupAttributes().applyValuesToEntity(auroraEntity);
		}
	}
	
	/** Applies the attribute values stored within the list of FieldGroup instances
	 *  against the passed Aurora entity.
	 * 
	 * @param auroraEntity
	 * @throws DataException
	 */
	public void applyAttributeValuesToEntity(A auroraEntity, 
	 Vector<FieldGroup<C,A>> fieldGroups) throws DataException {
		if (!attributesSet)
			throw new DataException("Attributes not set against Field Groups, " +
			 "nothing to apply!");
		
		for (FieldGroup<C,A> fieldGroup : fieldGroups) {
			fieldGroup.getFieldGroupAttributes().applyValuesToEntity(auroraEntity);
		}
	}
	
	public Vector<FieldGroup<C,A>> getFieldGroups() {
		return this.fieldGroups;
	}
	
	/** Calculates the amendment dates for the passed Central DB entity.
	 *  
	 * @param dbEntity
	 * @param company
	 * @param facade
	 * @throws DataException
	 */
	public void calculateGroupAmendmentDates
	 (C dbEntity, Company company, FWFacade facade) 
	 throws DataException {
		Log.debug("Calculating FieldGroup amendment dates...");
		
		for (FieldGroup<C,A> fieldGroup : fieldGroups) {
			fieldGroup.calculateAmendmentDates(dbEntity, company, facade);
		}
		
		Log.debug("Finished calculating FieldGroup amendment dates:");
		StringBuffer sb = new StringBuffer();
		
		for (FieldGroup<C,A> fieldGroup : fieldGroups) {
			FieldGroupAmendmentDates<C> dates = fieldGroup.getAmendmentDates();
			
			sb.append("\nFieldGroup: " + fieldGroup.getName() + 
			 ", captured dates: " + dates.getAmendmentDates().size() + ", latest date: " +
			 dates.getLatestAmendmentDate() + "\n");
			
			for (Map.Entry<String, Date> amendmentDate 
				: dates.getAmendmentDates().entrySet()) {
				sb.append("\t" + amendmentDate.getKey() + ": " + amendmentDate.getValue()
				 + "\n");
			}
		}
		
		Log.debug(sb.toString());
		
		amendmentDatesCalculated = true;
	}
	
	/** Builds a new list of FieldGroups, filtered down to only include groups amended 
	 *  after the passed limit date.
	 *  
	 * @param amendmentDates2
	 * @param limitDate
	 * @return
	 * @throws DataException 
	 */
	public Vector<FieldGroup<C,A>>
	 getFilteredGroupsByAmendmentDate(Date limitDate) throws DataException {
		if (!amendmentDatesCalculated)
			throw new DataException("Amendment Dates haven't been calculated!");
		
		Log.debug("Filtering list of " + this.fieldGroups.size() + 
		 " Field Groups, using limit date: " + 
		 FieldGroupAmendmentDates.DATE_FORMAT.format(limitDate));
		
		Vector<FieldGroup<C,A>> filteredGroups = new Vector<FieldGroup<C,A>>();
		
		for (FieldGroup<C,A> fieldGroup : this.fieldGroups) {
			Date latestDate = fieldGroup.getAmendmentDates().getLatestAmendmentDate();
			
			if (latestDate != null && latestDate.after(limitDate)) {
				Log.debug(fieldGroup.getName() + " (last amendment: " + 
				 FieldGroupAmendmentDates.DATE_FORMAT.format(latestDate) + ")");
				filteredGroups.add(fieldGroup);
			}
		}
		
		Log.debug("Generated new filtered list of " + filteredGroups.size() +
		 " Field Groups");
		
		return filteredGroups;
	}
	
	/** Builds a list of FieldGroup Names, filtered down to only include groups 
	 *  amended after the passed limit date.
	 *  
	 * @param amendmentDates2
	 * @param limitDate
	 * @return
	 * @throws DataException 
	 */
	public Vector<String> getFilteredGroupNamesByAmendmentDate(Date limitDate) 
	 throws DataException {
		Vector<FieldGroup<C,A>> filteredGroups = getFilteredGroupsByAmendmentDate
		 (limitDate);
		
		Vector<String> v = new Vector<String>();
		
		for (FieldGroup<C,A> fieldGroup : filteredGroups) {
			v.add(fieldGroup.getName());
		}
		
		return v;
	}
	
	/** Returns a new list of FieldGroups with names matching those in the passed list.
	 * 
	 * @param fieldNames
	 * @return
	 */
	public Vector<FieldGroup<C,A>> getFilteredGroupsByNames(Vector<String> fieldNames) {
		Vector<FieldGroup<C,A>> filteredGroups = new Vector<FieldGroup<C,A>>();
		
		for (String fieldName : fieldNames) {
			for (FieldGroup<C,A> fieldGroup : this.getFieldGroups()) {
				if (fieldGroup.getName().equals(fieldName))
					filteredGroups.add(fieldGroup);
			}
		}
		
		return filteredGroups;
	}
	
	/** Returns a ResultSet listing all the Field Group configuration details */
	public DataResultSet getFieldGroupsResultSet() {
		DataResultSet rs = new DataResultSet(
			new String[] {
				"FIELDGROUP_NAME", 
				"FIELDGROUP_LABEL", 
				"FIELDGROUP_ISCRITICAL"
			}
		);
		
		for (FieldGroup<C,A> fieldGroup : this.getFieldGroups()) {
			Vector<String> v = new Vector<String>();
			
			v.add(fieldGroup.getName());
			v.add(fieldGroup.getLabel());
			v.add(fieldGroup.isCritical() ? "1" : "0");
			
			rs.addRow(v);
		}
		
		return rs;	
	}
	
	public DataResultSet getFieldGroupAmendmentDatesResultSet() throws DataException {
		if (!amendmentDatesCalculated)
			throw new DataException("Amendment Dates haven't been calculated!");
		
		DataResultSet rs = new DataResultSet(
			new String[] {
				"FIELDGROUP_NAME", 
				"LATEST_AMENDMENT_DATE"
			}
		);
		
		for (FieldGroup<C,A> fieldGroup : this.getFieldGroups()) {
			Vector<String> v = new Vector<String>();
			
			Date latestAmendmentDate = fieldGroup.getAmendmentDates()
			 .getLatestAmendmentDate();
			
			v.add(fieldGroup.getName());
			v.add(
				latestAmendmentDate != null ?
				FieldGroupAmendmentDates.DATE_FORMAT.format(latestAmendmentDate)
				: "");
			
			rs.addRow(v);
		}
		
		return rs;	
	}
	
	/** Reusable Address amendment date calculator.
	 *  
	 * @author tm
	 *
	 */
	public class AddressAmendmentDates extends FieldGroupAmendmentDates<C> {
		
		public AddressAmendmentDates(C dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}
		
		protected Vector<Contact> getContacts() {
			if (dbEntity instanceof Person)
				return ((Person)dbEntity).getContacts();
			else if (dbEntity instanceof Entity)
				return ((Entity)dbEntity).getContacts();
			else
				return null;
		}

		@Override
		protected void captureAmendmentDates() throws DataException {
			Vector<Contact> contacts = getContacts();
			
			// Check for updates to:
			// The nominated/correspondence address record
			// When the address record was actually set as nominated/default
			
			Contact corrAddressContact = Contact.getDefaultContact
			 (contacts, Contact.ADDRESS, true);
			
			addAmendmentDate("Address record", 
			 corrAddressContact != null ?
			 corrAddressContact.getAddress().getLastUpdated() : null);
			
			// Check for latest update to IS_DEFAULT column in Contact Point
			Date contactDefaultFlagLastUpdate = null;
			if (corrAddressContact != null) {
				contactDefaultFlagLastUpdate = 
				 SDAuditData.getLatestDataFieldUpdateDate
				 (corrAddressContact.getContactId(), "ContactPoint", 
				 Contact.Cols.IS_DEFAULT, facade);
			}
			
			addAmendmentDate("Contact Point Default Flag", contactDefaultFlagLastUpdate);
			
			// Check for latest audit event against the Address
			SDAudit latestAddressAuditEvent = null;
			if (corrAddressContact != null) {
				latestAddressAuditEvent = SDAudit
				 .getLatestSDAuditByEventAndRelationId(
				  ElementType.SecondaryElementType.Address.toString(), 
				  corrAddressContact.getAddress().getAddressId(), 
				  facade);
			}
			
			addAmendmentDate
			 ("Last action on Address record", 
			 latestAddressAuditEvent != null ? 
			 latestAddressAuditEvent.getAuditDate() : null);
		}
	}
	
	/** Reusable other contact details amendment date calculator
	 * 
	 * @author tm
	 *
	 */
	public class OtherContactDetailsAmendmentDates extends FieldGroupAmendmentDates<C> {

		public OtherContactDetailsAmendmentDates(C dbEntity,
		 Company company, FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}
		
		protected Vector<Contact> getContacts() {
			if (dbEntity instanceof Person)
				return ((Person)dbEntity).getContacts();
			else if (dbEntity instanceof Entity)
				return ((Entity)dbEntity).getContacts();
			else
				return null;
		}
		
		@Override
		protected void captureAmendmentDates() throws DataException {
			Vector<Contact> contacts = getContacts();
			
			// Check for updates to default Telephone/Fax/Email/Website contact
			// points.
			
			// Just check LAST_UPDATED column for any default contact points we
			// find.
			
			// TODO: what if a contact point has been deleted - how do you detect this?
			{
				Contact contact = 
				 Contact.getDefaultContact(contacts, Contact.PHONE, false);

				addAmendmentDate("Default Phone contact", 
				 contact != null ? contact.getLastUpdated() : null);
			}
			
			{
				Contact contact = 
				 Contact.getDefaultContact(contacts, Contact.FAX, false);

				addAmendmentDate("Default Fax contact", 
				 contact != null ? contact.getLastUpdated() : null);
			}
			
			{
				Contact contact = 
				 Contact.getDefaultContact(contacts, Contact.EMAIL, false);

				addAmendmentDate("Default Email contact", 
				 contact != null ? contact.getLastUpdated() : null);
			}
			
			{
				Contact contact = 
				 Contact.getDefaultContact(contacts, Contact.WEB, false);

				addAmendmentDate("Default Web contact", 
				 contact != null ? contact.getLastUpdated() : null);
			}
		}
	}
	
	/** Reusable sig list amendment date calculator
	 * 
	 * @author tm
	 *
	 */
	public class SignatoryAmendmentDates extends FieldGroupAmendmentDates<C> {
		public SignatoryAmendmentDates(C dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}
		
		@Override
		protected void captureAmendmentDates() throws DataException {
			Date lastSigChanged = null, lastSigPersonUpdated = null;

			Vector<Person> sigs = AuroraSignatories.getPersonSignatoryList
			 ((AuroraEntitySource)dbEntity, facade);
			
			// Fetch the most recent 'Last Updated' time across the signatory Person
			// records.
			// TODO: what about the last time their address/tel number was updated!
			for (Person sig : sigs) {
				if (lastSigPersonUpdated == null
					||
					lastSigPersonUpdated.before(sig.getLastUpdated())) {
					lastSigPersonUpdated = sig.getLastUpdated();
				}
			}
			
			int elementId;
			RelationName sigRel;
			
			// Fetch last time a sig relation was added/removed.
			if (this.dbEntity instanceof Entity) {
				Entity org = (Entity)dbEntity;
				elementId = org.getOrganisationId();
				
				sigRel = RelationName.getCache().getCachedInstance
				 (RelationName.OrganisationPersonRelation.SIGNATORY);
				
			} else if (this.dbEntity instanceof Account) {
				Account account = (Account)dbEntity;
				elementId = account.getAccountId();
				
				sigRel = RelationName.getCache().getCachedInstance
				 (RelationName.PersonAccountRelation.SIGNATORY);
				
			} else {
				throw new DataException("Unable to capture sig amendment dates for " +
				 this.dbEntity.getClass().getName());
			}
	
			SDAudit lastSigChangedAudit = 
			 SDAudit.getLatestRelationChangeByElementAndRelationName
			 (elementId, sigRel, facade);
			
			if (lastSigChangedAudit != null)
				lastSigChanged = lastSigChangedAudit.getAuditDate();
			
			this.addAmendmentDate("Last Signatory Added/Removed", lastSigChanged);
			this.addAmendmentDate("Last Signatory Person Updated", lastSigPersonUpdated);
		}
	}
	
	/** Reusable Nominated Correspondent amendment date calculator
	 *  
	 *  Works for Orgs and Accounts.
	 *  
	 * @author tm
	 *
	 */
	public class NominatedCorrespondentAmendmentDates 
	 extends FieldGroupAmendmentDates<C> {
		public NominatedCorrespondentAmendmentDates(C dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}
		
		private Relation getCorrRelation() throws DataException {
			Person nomCorr = null;
			
			// Fetch nominated correspondent Person (if any!)
			if (dbEntity instanceof Entity) {
				nomCorr = Entity.getNominatedCorrespondent
				 (((Entity)dbEntity).getOrganisationId(), false, facade);
			} else if (dbEntity instanceof Account) {
				nomCorr = Account.getNominatedCorrespondent
				 (((Account)dbEntity).getAccountId(), false, facade);
			}
			
			if (nomCorr == null)
				return null;
			
			Vector<Relation> rels = null;
			
			// Extract the correspondent Relation
			if (dbEntity instanceof Entity) {
				rels = Relation.getRelations
				 (((Entity)dbEntity).getOrganisationId(), 
				 nomCorr.getPersonId(), 
				 null,
				 RelationName.getCache().getCachedInstance(
					RelationName.OrganisationPersonRelation.CORRESPONDENT), 
				 facade);
			} else if (dbEntity instanceof Account) {
				rels = Relation.getRelations
				 (nomCorr.getPersonId(),
				 ((Account)dbEntity).getAccountId(), 
				 null,
				 RelationName.getCache().getCachedInstance(
					RelationName.PersonAccountRelation.CORRESPONDENT), 
				 facade);
			}
			
			// Can only be one returned Relation above.
			return rels.get(0);
		}
		
		@Override
		protected void captureAmendmentDates() throws DataException {
			Date nomCorrRelAdded = null, nomCorrPropSet = null;
			
			// Can only be one returned Relation above.
			Relation corrRel = getCorrRelation();
			
			if (corrRel != null) {
				nomCorrRelAdded = corrRel.getRelationDate();
				
				// Now check for the Default/Nominated property
				Vector<RelationPropertyApplied> relProps = 
				 RelationPropertyApplied.getByRelation
				 (corrRel.getRelationId(), facade);
				
				for (RelationPropertyApplied relProp : relProps) {
					if (relProp.getRelationProperty().getRelationPropertyId()
						== RelationProperty.RELATION_PROPERTY_ORG_CORR_DEFAULT) {
						// Found the prop. Now determine when it was added.
						nomCorrPropSet = relProp.getDateAdded();
					}
				}
			}
			
			this.addAmendmentDate("Nominated Flag set", nomCorrPropSet);
			this.addAmendmentDate("Correspondent Relation created", nomCorrRelAdded);
		}
	}
}
