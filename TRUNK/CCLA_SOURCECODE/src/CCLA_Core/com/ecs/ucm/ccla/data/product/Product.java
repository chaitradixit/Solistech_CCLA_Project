package com.ecs.ucm.ccla.data.product;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.stellent.embedded.FWFacade;

public class Product implements Persistable {
	
	private int id;
	private String name;
	
	public static class Ids {
		public static final int COMMUNITY_FIRST = 1;
	}
	
	public static class Cols {
		public static final String ID = "PRODUCT_ID";
		public static final String NAME = "PRODUCT_NAME";
	}
	
	public static class Queries {
		public static final String GET_ALL = "qCore_GetAllProducts";
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
	
	public static DataResultSet getAllData(FWFacade facacde) throws DataException {
		return facacde.createResultSet(Queries.GET_ALL, new DataBinder());
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
