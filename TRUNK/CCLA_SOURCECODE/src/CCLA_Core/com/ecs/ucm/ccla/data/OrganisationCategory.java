package com.ecs.ucm.ccla.data;

import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** Contains methods for handling data in the REF_ORG_CATEGORY table.
 * 
 * @author Tom
 *
 */
public class OrganisationCategory {
	
	private int categoryId;
	private Integer parentCategoryId;
	private int nameId;
	
	// A few hard-coded IDs from the REF_ORG_CATEGORY table, used by Entity/Identity
	// checking etc.
	public static class CategoryIds {
		public static final int NOMINEE = 1051;
		public static final int TTLA = 1056;
		public static final int ANONYMOUS_DONOR = 1057;
	}
	
	public OrganisationCategory(int categoryId, Integer parentCategoryId,
	 int nameId) {
		this.categoryId = categoryId;
		this.parentCategoryId = parentCategoryId;
		this.setNameId(nameId);
	}
	
	public static OrganisationCategory getCategory
	 (int categoryId, FWFacade facade) throws DataException {
		
		DataResultSet rsOrgCategory = getCategoryData(categoryId, facade);
		
		if (rsOrgCategory.isEmpty())
			return null;
		else 
			return get(rsOrgCategory);
	}
	
	public static OrganisationCategory get(DataResultSet rs) 
	 throws DataException {
		
		return new OrganisationCategory(
			CCLAUtils.getResultSetIntegerValue
			 (rs, "CATEGORY_ID"),
			 CCLAUtils.getResultSetIntegerValue
			 (rs, "PARENT_CATEGORY_ID"),
			 CCLAUtils.getResultSetIntegerValue
			 (rs, "CATEGORY_NAME_ID")
		);			
	}
	
	public static DataResultSet getCategoryData
	 (int categoryId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "CATEGORY_ID", categoryId);
		
		return facade.createResultSet
		 ("qClientServices_GetOrgCategory", binder);
		
	}

	/** Fetches all top-level categories, i.e. those with a null 
	 *  PARENT_CATEGORY_ID.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getCategories(FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PARENT_CATEGORY_ID", null);
		
		return facade.createResultSet
		 ("qClientServices_GetOrgCategories", binder);
	}
	
	public static DataResultSet getSubCategories
	 (int parentCategoryId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PARENT_CATEGORY_ID", parentCategoryId);
		
		return facade.createResultSet
		 ("qClientServices_GetOrgSubCategories", binder);
	}
	
	/** Fetches the selected hierarchy of categories for a given
	 *  Category ID. 
	 *  
	 *  i.e. if the passed Category ID is a 3rd level category,
	 *  the ResultSet will contain three rows, one for each selected
	 *  category.
	 * 
	 * @param categoryId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getSelectedCategories
	 (Integer categoryId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "CATEGORY_ID", categoryId);
		
		return facade.createResultSet
		 ("qClientServices_GetSelectedOrgCategories", binder);
	}
	

	/** Fetches the selected hierarchy of categories for a given
	 *  Category ID. 
	 *  
	 *  Each String corresponds to the Category name. They are loaded into the
	 *  list starting with the top-most parent Category.
	 *  
	 * @throws DataException 
	 *  
	 */  
	public static Vector<String> getCategoryTree(int categoryId, FWFacade facade) 
	 throws DataException {
		Vector<String> v = new Vector<String>();
		
		DataResultSet rs = getSelectedCategories(categoryId, facade);
		
		if (rs.first()) {
			do {
				v.add(rs.getStringValueByName("BASE_CAT_NAME"));
			} while (rs.next());
		}
		
		return v;
	}
	
	/** Adds up to 3 ResultSets to the given DataBinder:
	 * 
	 *  rsCategories: the current category 'level'. If the passed categoryId
	 *  is non-null, this list will contain the selected Org. Category. If
	 *  the passed categoryId is null, this will contain the top-level
	 *  category list.
	 *  
	 *  rsSubCategories: the current category sub-level. If the passed
	 *  categoryId is non-null and sub-categories exist for it, the list will
	 *  contain all those sub-categories. If the passed categoryId is null,
	 *  or no sub-categories exist for the given categoryId, this ResultSet
	 *  won't be present.
	 *  
	 * @param categoryId
	 * @param facade
	 * @param binder
	 * @throws DataException 
	 */
	public static void addCategoryListsToBinder
	 (Integer categoryId, FWFacade facade, DataBinder binder) 
	 throws DataException {
		
		DataResultSet rsCategories;
		DataResultSet rsSubCategories;
		
		OrganisationCategory selCategory = null;

		if (categoryId != null) {
			selCategory  = 
			 OrganisationCategory.getCategory(categoryId, facade);
			
			// Add the full selected category tree.
			DataResultSet rsSelectedCategories = 
			 getSelectedCategories(categoryId, facade);
		
			binder.addResultSet
			 ("rsSelectedCategories", rsSelectedCategories);
		}
		
		if (selCategory == null || selCategory.getParentCategoryId() == null) {
			// Load in top-level categories list.
			rsCategories = OrganisationCategory.getCategories(facade);
		} else {
			int parentCategoryId = selCategory.getParentCategoryId();
			
			// Determine all sub-categories for the selected category's
			// parent category.
			rsCategories = OrganisationCategory.getSubCategories
			 (parentCategoryId, facade);
		}
		
		binder.addResultSet("rsCategories", rsCategories);
		
		if (selCategory != null) {
			// Fetch any sub-categories for the currently-selected category
			// (could be empty)
			rsSubCategories = 
			 getSubCategories(selCategory.getCategoryId(), facade);
			
			binder.addResultSet("rsSubCategories", rsSubCategories);
		}
	}	
	
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Integer parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public void setNameId(int nameId) {
		this.nameId = nameId;
	}

	public int getNameId() {
		return nameId;
	}
	
}
