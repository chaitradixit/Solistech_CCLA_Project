package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents a single entry from the REF_TITLE table in the CCLA tablespace.
 * 
 *  Each Title represents a person's name title, e.g. Mr, Mrs, Dr
 *  
 *  A full set of these instances are accessible via the CacheManager.
 *  
 *  TODO: create object type/caching for Title Category, rather than just the
 *  indirect ID reference. Not sure if we'll really need this or not.
 *  
 *  TODO: implement Persistable methods
 *  
 * @author Tom
 *
 */
public class PersonTitle implements Persistable {

	private int titleId;
	
	/** Implied gender of the person title. E.g. Mr = Male. Can be null when
	 *  the title has an ambiguous gender (e.g. Dr.)
	 */
	private Gender gender;

	private String title;
	private int titleCategoryId;
	
	private String salutation;
	private String salutationSuffix;

	public static class Cols {
		public static final String ID = "TITLE_ID";
	}
	
	public static class Ids {
		public static final int ANONYMOUS = 64;
	}
	
	public PersonTitle(int titleId, Gender gender, String title,
			int titleCategoryId, String salutation,
			String salutationSuffix) {
		this.titleId = titleId;
		this.gender = gender;
		this.title = title;
		this.setTitleCategoryId(titleCategoryId);
		this.salutation = salutation;
		this.salutationSuffix = salutationSuffix;
	}
	
	public static PersonTitle get(DataResultSet rs) throws DataException {
		
		Gender gender = null;
		
		if (!StringUtils.stringIsBlank(rs.getStringValueByName("GENDER_ID")))
			gender = Gender.get(rs);
		
		return new PersonTitle(
			CCLAUtils.getResultSetIntegerValue(rs, "TITLE_ID"),
			gender,
			rs.getStringValueByName("TITLE"),
			CCLAUtils.getResultSetIntegerValue
			 (rs, "TITLE_CATEGORY_ID"),
			rs.getStringValueByName("SALUTATION"),
			rs.getStringValueByName("SALUTATION_SUFFIX")
		);
	}
	
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

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getSalutationSuffix() {
		return salutationSuffix;
	}

	public void setSalutationSuffix(String salutationSuffix) {
		this.salutationSuffix = salutationSuffix;
	}

	public void setTitleCategoryId(int titleCategoryId) {
		this.titleCategoryId = titleCategoryId;
	}

	public int getTitleCategoryId() {
		return titleCategoryId;
	}

	@Override
	public String toString() {
		return "PersonTitle [gender=" + gender + ", salutation=" + salutation
				+ ", salutationSuffix=" + salutationSuffix + ", title=" + title
				+ ", titleCategoryId=" + titleCategoryId + ", titleId="
				+ titleId + "]";
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, PersonTitle> getCache() {
		return CACHE;
	}
	
	/** DataType cache implementor.
	 *  
	 *  Maps DataType names against DataType instances
	 *  
	 **/
	private static class Cache extends Cachable<Integer, PersonTitle> {

		public Cache() {
			super("Person Title");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			// Fetch entire REF_TITLE table from db
			DataResultSet rsTitles = facade.createResultSet
			("qClientServices_GetAllTitles", new DataBinder());
			
			if (rsTitles == null || rsTitles.isEmpty())
				throw new DataException("No titles found.");
			
			HashMap<Integer, PersonTitle> newCache = 
			 new HashMap<Integer, PersonTitle>();
			
			do {
				PersonTitle thisTitle = PersonTitle.get(rsTitles);
				newCache.put(thisTitle.getTitleId(), thisTitle);
				
			} while (rsTitles.next());
			
			Log.debug("Cached " + rsTitles.getNumRows() + " titles.");
			
			this.CACHE_MAP = newCache;
		}
	}
}
