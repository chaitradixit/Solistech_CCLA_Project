package com.ecs.ucm.ccla.aurora;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aurora.webservice.Client;
import com.ecs.ucm.ccla.NumberToWords;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntitySource;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Helper methods for getting/setting Aurora entity signatory fields.
 * 
 *  Contains utility methods for converting a list of Person instances to their equivalent
 *  Signatories, and populating the signatory fields of an Aurora Entity, including
 *  overflow handling.
 * 
 * @author tm
 *
 */
public class AuroraSignatories {
	
	private AuroraSignatories() {}
	
	public static class HeaderFields {
		public boolean multipleSigIndicator;
		public String signatoryInfo;
		public HeaderFields(boolean multipleSigIndicator, String signatoryInfo) {
			super();
			this.multipleSigIndicator = multipleSigIndicator;
			this.signatoryInfo = signatoryInfo;
		}
	}
	
	/** Characters used to split up multiple signatory names when they are crammed into
	 *  a single field on the Aurora entity.
	 */
	private final static String[] NAME_DELIMITER_CHARS = new String[] { ",", "/", "\\"};
	
	/** RegExp matcher used to determine likely person names.
	 *  
	 *  Checks for presence of space-delimited character sequences (between 2 and 5) that 
	 *  always start with a capital letter.
	 *  
	 *  No longer used - see one below.
	 */
	//private final static Pattern NAME_MATCHER = Pattern.compile
	// ("^(([A-Z]+[a-z]*)(.)?([\\s[-]])){1,5}([A-Z]+[a-z]*)$");
	
	
	/** Regex matcher used to determine and split up likely person names.
	 *  
	 *  Essentially checks for the following:
	 *  
	 *  [Title](First Name)(Middle Names)[Surname]
	 * 
	 *  Where Title/Surname are required, First Name/Middle Names are optional. Each name 
	 *  token must start with at least 1 capital letter. The Title/First Name/Middle Names
	 *  are permitted to end with a full-stop and First/Middle Names may contain a hyphen
	 *  character.
	 *  
	 *  Providing there is a match, the group indexes can be translated as follows:
	 *  
	 *  1. Title
	 *  2. First Name (optional)
	 *  3. Middle Names (optional, must be trimmed to remove leading space)
	 *  4. (Ignore)
	 *  5. Surname
	 *  
	 *  The Regex isn't smart enough to deal with multiple-token Titles, e.g. 'Rt Rev',
	 *  and will assume 'Rev' as the First Name.
	 */
	private static Pattern NAME_MATCHER = Pattern.compile
	(	
		// Title
		"^([A-Z][\\-A-Za-z]*[\\.]?)" + 
		
		// First Name
		"([\\s][A-Z][\\-A-Za-z]*[\\.]?)?" + 
		
		// Middle Names
		"(([\\s][A-Z][\\-A-Za-z]*[\\.]?)*)" +
		
		"[\\s]" + 
		
		// Last Name
		"([A-Z][\\-A-Za-z]*)$"
	);
	
	public static void main(String[] args) {
		
		String[] testNames = new String[] {
			"Not a Hyphen-Name",
			"Dot.Name",
			"Hyphen-Name",
			"Hyphen-hyphen-Name",
			"HyphenName",
			"Hyphen Name",
			"Mr",
			"Mr.",
			"Mr T Marchant",
			"T S Elliot",
			"Mr T N Marchant",
			"Mr Thomas Nigel Marchant Esq.",
			"Mr Thomas Nigel Marchant",
			"MR CAPITAL NAME",
			"see list",
			"Mr Grimes",
			"Mr F Grimes",
			"Rev. Jones",
			"Rory Celland-Jones",
			"Mr R Celland-Jones",
			"Mr H J Wells",
			"Mr H J K Wells",
			"Mr J H M K K Wells",
			"Mr George RR Martin",
			"Mr G RR Martin",
			"colonel clink",
			"Jim",
			"Mr H J Kronkite",
			"Rt Rev James Joyce",
			"Please see signature list on file",
			"See list"
		};
		
		for (String testName : testNames) {
			Matcher m = NAME_MATCHER.matcher(testName);
			
			System.out.println(testName + ": " + 
			 m.matches());
			
			if (m.matches()) {
				for (int i=0; i<=m.groupCount(); i++) {
					System.out.println("\tGroup " + i + ": " + m.group(i));
				}
			}
		}
	}
	
	public static Matcher getPersonNameMatcher(String name) {
		return NAME_MATCHER.matcher(name);
	}
	
	public static final class FieldNamePrefixes {
		public static final String NAME = "SigName";
		public static final String POSTCODE = "SigPostcode";
		public static final String TELEPHONE = "SigTelephone";
	}
	
	public static final class FieldNames {
		public static final String MULTI_SIGS_INDICATOR = "MultipleSigsIndicator";
		public static final String MULTI_SIGS_INFO = "MultipleSigsInformation";
	}

	/** Sets the two 'required signatory' fields, indicating the number of signatures
	 *  required to authorise an instruction.
	 * 
	 *  MultipleSignaturesIndicator: true if there is 1 or more req. signatures.
	 *  MultipleSignatoryInformation: if the above is true, shows the number of 
	 *  signatories in plain text, i.e. 5 = "five"
	 *  
	 * @param auroraEntity an Aurora Account ro 
	 * @param numSigs
	 */
	public static void applyReqSigInfoToAuroraEntity(Object auroraEntity, int numSigs) {
		
		HeaderFields headerFields = getHeaderFieldValues(numSigs);
		
		boolean multipleSigs = 	headerFields.multipleSigIndicator;
		String numSigsStr = 	headerFields.signatoryInfo;
		
		if (auroraEntity instanceof com.aurora.webservice.Account) {
			com.aurora.webservice.Account acc = 
			 (com.aurora.webservice.Account)auroraEntity;
			
			acc.setMultipleSignaturesIndicator(multipleSigs);
			acc.setMultipleSignaturesInformation(numSigsStr);
		} else if (auroraEntity instanceof com.aurora.webservice.Client) {
			com.aurora.webservice.Client cl = 
			 (com.aurora.webservice.Client)auroraEntity;
			
			cl.setMultipleSignatoryIndicator(multipleSigs);
			cl.setMultipleSignatoryInformation(numSigsStr);
		}
	}

	/**
	 * Fetches a full list of Person instances who are marked as signatories against
	 * the passed DB Entity (with contact details loaded).
	 * 
	 * The DB entity must be a type Account or Entity
	 * 
	 * @param cdbEntity
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<Person> getPersonSignatoryList
	 (AuroraEntitySource cdbEntity, FWFacade facade) throws DataException {
		
		Vector<Person> cdbSigs = new Vector<Person>();
		
		if (cdbEntity instanceof Account) {
			Account acc = (Account)cdbEntity;
			
			Vector<Integer> relNameIds = new Vector<Integer>();
			relNameIds.add(RelationName.PersonAccountRelation.SIGNATORY);
			
			cdbSigs = Relation.getRelatedPersons(
				acc.getAccountId(), 
				ElementType.ACCOUNT, 
				relNameIds,
				facade
			);
			
		} else if (cdbEntity instanceof Entity) {
			Entity org = (Entity)cdbEntity;
			
			Vector<Integer> relNameIds = new Vector<Integer>();
			relNameIds.add(RelationName.OrganisationPersonRelation.SIGNATORY);
			
			cdbSigs = Relation.getRelatedPersons(
				org.getOrganisationId(), 
				ElementType.ORGANISATION, 
				relNameIds,
				facade
			);
			
		} else {
			throw new DataException("Unable to fetch Signatories for DB entity type: " +
			 cdbEntity.getClass().getName());
		}
		
		// Load in contact details.
		for (Person p : cdbSigs)
			p.setContacts(Contact.getElementContacts(p.getPersonId(), facade));
		
		return cdbSigs;
	}

	/** Builds a list of Aurora Signatories from the passed list of Persons.
	 *  
	 *  It is expected that each Person instance will have contact details loaded/set.
	 *  
	 *  TODO: sort alphabetically!
	 *  
	 * @param persons
	 * @return
	 */
	public static Vector<AuroraSignatory> getSignatories(Vector<Person> persons) 
	 throws DataException {
		Vector<AuroraSignatory> auroraSigs = new Vector<AuroraSignatory>();
		
		// Sort the sig list alphabetically first.
		Collections.sort(persons);
		
		for (Person p : persons) {
			Contact defaultAddrressContact = Contact.getDefaultContact
			 (p.getContacts(), Contact.ADDRESS);
			
			Contact defaultTelephoneContact = Contact.getDefaultContact
			 (p.getContacts(), Contact.PHONE);
			
			String sigName = null;
			
			// Determine whether to use the Full Name for signatories, or their
			// abbreviated version.
			SystemConfigVar useSigFullName = 
			 SystemConfigVar.getCache().getCachedInstance("SDU_UseSignatoryFullName");
	
			if (useSigFullName != null && useSigFullName.getBoolValue()) {
				sigName = p.getFullName();
			} else {
				sigName = p.getAbbrevatedFullName();
			}
			
			AuroraSignatory sig = new AuroraSignatory(
				sigName, 
				defaultAddrressContact != null 
				? defaultAddrressContact.getAddress().getPostCode() : null, 
				defaultTelephoneContact != null
				? defaultTelephoneContact.getValue() : null
			);
			
			auroraSigs.add(sig);
		}
		
		return auroraSigs;
	}

	/** Applies the list of signatories to the Aurora entity's signatory fields.
	 *  
	 *  If there are more than the maximum allowed number of signatories passed in,
	 *  the list isn't populated with names/contact details - instead the very first
	 *  name slot is set to a special String constant.
	 *  
	 *  
	 * @param auroraSigs
	 * @param auroraEntity
	 */
	public static void applyToAuroraEntity(Vector<AuroraSignatory> auroraSigs,
		Object auroraEntity) {
		
		try {
			if (auroraSigs.size() > AuroraWebServiceHandler.MAXIMUM_AURORA_SIGNATORIES) {
				// Too many signatories - place a message in the first sig name
				// box and we're done
				applyToAuroraEntity
				 (AuroraWebServiceHandler.SIGNATORY_LIST_OVERFLOW_MESSAGE, null, null, 
				 auroraEntity, 1);
			} else {
				// Populate as many signatory details as we have.
				for (int i=1; i<=AuroraWebServiceHandler.MAXIMUM_AURORA_SIGNATORIES; i++) {
					
					if (auroraSigs.size() >= i) {
						AuroraSignatory sig = auroraSigs.get(i-1);
						
						applyToAuroraEntity
						 (sig.getName(), sig.getPostcode(), sig.getTelephone(), 
						 auroraEntity, i);
					} else {
						// Add empty sig data rows
						applyToAuroraEntity(null, null, null, auroraEntity, i);
					}
				}
			}
	
		} catch (Exception e) {
			Log.error("Failed assignment of Signatory List values", e);
		}
	}

	/** Uses reflection to assign a single set of signatory data values to the passed
	 *  Aurora entity instance, at the given field index.
	 *  
	 * @param auroraEntity
	 * @param sigIndex		a number between 1 and 10
	 * @throws Exception
	 */
	public static void applyToAuroraEntity
	 (String sigName, String sigPostcode, String sigTelephone,
	 Object auroraEntity, int sigIndex) throws Exception {
		
		Class<? extends Object> cl = auroraEntity.getClass();
		
		// Use reflection to extract the setter methods by name
		Method setNameMethod = 
		 cl.getMethod("setAdditionalSignatoryName" + sigIndex, String.class);
		
		setNameMethod.invoke(
		 auroraEntity, 
		 sigName != null ? sigName : ""
		);
		
		Method setPostcodeMethod = 
		 cl.getMethod("setAdditionalSignatoryPostcode" + sigIndex, String.class);
		
		setPostcodeMethod.invoke(
		 auroraEntity, 
		 sigPostcode != null ? sigPostcode : ""
		);
		
		Method setTelephoneMethod = 
		 cl.getMethod("setAdditionalSignatoryTelephone" + sigIndex, String.class);
		
		setTelephoneMethod.invoke(
		 auroraEntity, 
		 sigTelephone != null ? sigTelephone : ""
		);
	}

	public static AuroraSignatory getFromAuroraEntity(Object auroraEntity, int sigIndex)
	 throws Exception {
		Class<? extends Object> cl = auroraEntity.getClass();
		
		// Use reflection to extract the setter methods by name
		Method getNameMethod = 
		 cl.getMethod("getAdditionalSignatoryName" + sigIndex);
		Method getPostcodeMethod = 
		 cl.getMethod("getAdditionalSignatoryPostcode" + sigIndex);
		Method getTelephoneMethod = 
		 cl.getMethod("getAdditionalSignatoryTelephone" + sigIndex);
		
		return new AuroraSignatory(
			(String)getNameMethod.invoke(auroraEntity),
			(String)getPostcodeMethod.invoke(auroraEntity),
			(String)getTelephoneMethod.invoke(auroraEntity)
		);
	}

	public static HeaderFields getHeaderFieldValues(int numSigs) {
		boolean multipleSigs = (numSigs > 1);
		String numSigsStr = "";
		
		if (multipleSigs)
			numSigsStr = NumberToWords.convert(numSigs);
		
		return new HeaderFields(multipleSigs, numSigsStr);
	}
	
	/** Attempts to extract person names (and their telephone/postcode) from the Aurora
	 *  signatory fields.
	 *  
	 *  An Aurora Account/Client object has 10 slots for signatory names. However, 
	 *  sometimes 2 or 3 names have been crammed into a single 'slot', separated by a 
	 *  comma or slash.
	 *  
	 *  Sometimes the signatory slots is used as a free-form notepad instead and doesn't
	 *  contain any signatory names!
	 *  
	 *  It is the responsibility of this method to attempt extraction of individual
	 *  person names from the 10 slots.
	 * @throws DataException 
	 */
	public static Vector<CandidatePersonSignatory> 
	 extractSignatoryNamesFromAuroraEntity(Object auroraEntity) 
	 throws DataException {
		
		if (!(auroraEntity instanceof Client 
			|| auroraEntity instanceof com.aurora.webservice.Account))
			throw new DataException("Unable to extract signatory names from entity: " +
			 auroraEntity.getClass().getName());
		
		Vector<CandidatePersonSignatory> candidatePersons = 
		 new Vector<CandidatePersonSignatory>();
		
		for (int i = 1; 
			i <= AuroraWebServiceHandler.MAXIMUM_AURORA_SIGNATORIES; i++) {
			
			try {
				AuroraSignatory sig = 
				 AuroraSignatories.getFromAuroraEntity(auroraEntity, i);
				
				if (!StringUtils.stringIsBlank(sig.getName())) {
					// Some text in the name field. See what we can find in there.
					Vector<String> personNames = extractPersonNamesFromString
					 (sig.getName());
					
					if (personNames.isEmpty())
						continue; 	// Could happen if the name field only contains
									// delimiters/spaces
					
					if (personNames.size() == 1) {
						// Just 1 name found. We'll carry over the postcode/telephone
						// in this case.
						candidatePersons.add(
						 new CandidatePersonSignatory
						  (personNames.get(0), sig.getPostcode(), sig.getTelephone()));
					} else {
						// Add all split up names, without contact details
						for (String personName : personNames) {
							candidatePersons.add(
							 new CandidatePersonSignatory(personName, null, null));		
						}
					}
					
				}
				
			} catch (Exception e) {
				Log.error("Failed capture of Signatory List values", e);
			}
		}
		
		return candidatePersons;
	}
	
	/** Attempts to extract (possible) person names from the passed String.
	 * 
	 *  Multiple names are identified by the presence of delimiter characters.
	 * 
	 *  All returned names are cleaned up with a trim() call.
	 *  
	 * @param s
	 * @return
	 */
	private static Vector<String> extractPersonNamesFromString(String s) {
		Vector<String> names = new Vector<String>();
		
		for (String delimiterChar : NAME_DELIMITER_CHARS) {
			if (s.indexOf(delimiterChar) >= 0) {
				String[] splitNames = s.split(delimiterChar);
				
				for (String splitName : splitNames) {
					String trimName = splitName.trim();
					
					if (trimName.length() > 0)
						names.add(trimName);
				}
					
				break;
			}
		}
		
		// No split chars found. Assume the passed String is a single name.
		if (names.isEmpty())
			names.add(s.trim());
		
		return names;
	}
	
	private static final String[] CANDIDATE_SIG_RS_COLUMN_NAMES = new String[] {
		"IMPORT_SIGNATORY",
		"SIGNATORY_NAME",
		"SIGNATORY_TELEPHONE",
		"SIGNATORY_POSTCODE",
		"DUPLICATE_PERSON_ID",
		"DUPLICATE_PERSON_NAME",
		"LOOKS_LIKE_PERSON_NAME",
		"RELATION_EXISTS",
		
		// Extracted portions of valid-looking names.
		"SIGNATORY_TITLE",
		"SIGNATORY_FIRST_NAME",
		"SIGNATORY_MIDDLE_NAMES",
		"SIGNATORY_LAST_NAME"
	};
	
	public static DataResultSet getCandidatePersonSignatoryResultSet(
			Vector<CandidatePersonSignatory> candidateSigs) {
		
		DataResultSet rs = new DataResultSet(CANDIDATE_SIG_RS_COLUMN_NAMES);
		
		for (CandidatePersonSignatory candidateSig : candidateSigs) {
			Vector<String> v = new Vector<String>();
			
			// Default the signatory for import if the person name appears legit, and
			// the relation doesn't already exist.
			boolean importSig = 
			 candidateSig.isLooksLikeAPersonName() && !candidateSig.isExistingRelation();
			 
			v.add(importSig ? "1" : "0");
			
			v.add(candidateSig.getName());
			v.add(candidateSig.getTelephone());
			v.add(candidateSig.getPostcode());
			
			Person dupePerson = candidateSig.getDuplicatePerson();
			
			v.add(dupePerson != null ? Integer.toString(dupePerson.getPersonId()) : null);
			v.add(dupePerson != null ? dupePerson.getFullName() : null);
			
			v.add(candidateSig.isLooksLikeAPersonName() ? "1" : "0");
			v.add(candidateSig.isExistingRelation() ? "1" : "0");
			
			v.add(candidateSig.getTitle());
			v.add(candidateSig.getFirstName());
			v.add(candidateSig.getMiddleNames());
			v.add(candidateSig.getLastName());
			
			rs.addRow(v);
		}
		
		return rs;
	}
}
