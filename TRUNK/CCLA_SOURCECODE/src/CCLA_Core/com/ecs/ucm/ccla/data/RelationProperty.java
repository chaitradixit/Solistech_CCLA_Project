package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_RELATION_PROPERTY table.
 *  
 *  This is used to map allowed Property entries in REF_PROPERTY against
 *  RelatioName entries in REF_RELATION_NAME.
 *  
 *  The RELATION_PROPERTY_APPLIED table has a foreign key reference to this table.
 *  
 * @author Tom
 *
 */
public class RelationProperty implements Persistable {
	
	private int relationPropertyId;
	
	private int relationNameId;
	private Property property;
	
	private Integer singletonScopeId;
	
	public static class SingletonScope {
		public static final int ELEMENT_ID1 = 1;
		public static final int ELEMENT_ID2 = 2;
		public static final int BOTH = 3;
	}
	
	public static final class Cols {
		public static final String ID = "RELATION_PROPERTY_ID";
		public static final String SINGLETON_SCOPE = "SINGLETON_SCOPE";
	}
	
	/** Relation-Property IDs are hard-coded here for easy reference. Should match 
	 * with REF_RELATION_PROPERTY table entries. */
	
	// Account - Bank Account relation properties
	public static final int RELATION_PROPERTY_WITH_DEFAULT 	= 1;	
	public static final int RELATION_PROPERTY_INC_DEFAULT 	= 2;
	
	// Org - Person relation properties
	public static final int RELATION_PROPERTY_ORG_CORR_DEFAULT = 31;
	// Person - Account relation properties
	public static final int RELATION_PROPERTY_ACC_CORR_DEFAULT = 32;
	
	public static RelationProperty get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new RelationProperty(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			CCLAUtils.getResultSetIntegerValue(rs, RelationName.Cols.ID),
			Property.getCache().getCachedInstance(
			 CCLAUtils.getResultSetIntegerValue(rs, Property.Cols.ID)
			),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.SINGLETON_SCOPE)
		);
	}
	
	public RelationProperty(int relationPropertyId, int relationNameId,
	 Property property, Integer singletonScopeId) {
		this.setRelationPropertyId(relationPropertyId);
		this.setRelationNameId(relationNameId);
		this.setProperty(property);
		this.setSingletonScopeId(singletonScopeId);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}

	public void setRelationPropertyId(int relationPropertyId) {
		this.relationPropertyId = relationPropertyId;
	}

	public int getRelationPropertyId() {
		return relationPropertyId;
	}

	public void setRelationNameId(int relationNameId) {
		this.relationNameId = relationNameId;
	}

	public int getRelationNameId() {
		return relationNameId;
	}

	public static RelationProperty get(int relationNameId, int propertyId,
	 FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, RelationName.Cols.ID, relationNameId);
		CCLAUtils.addQueryIntParamToBinder(binder, Property.Cols.ID, propertyId);
	
		return get(facade.createResultSet
		 ("qClientServices_GetRelationPropertyByValues", binder));
	}
	
	public void setSingletonScopeId(Integer singletonScopeId) {
		this.singletonScopeId = singletonScopeId;
	}

	public Integer getSingletonScopeId() {
		return singletonScopeId;
	}
	
	public boolean equals(RelationProperty prop) {
		return (prop.getRelationPropertyId() == prop.getRelationPropertyId());
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}

}
