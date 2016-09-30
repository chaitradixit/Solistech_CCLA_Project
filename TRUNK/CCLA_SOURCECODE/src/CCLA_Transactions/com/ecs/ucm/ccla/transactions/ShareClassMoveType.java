package com.ecs.ucm.ccla.transactions;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ShareClassMoveType implements Persistable{

	private int moveTypeId;
	private String moveTypeName;


	public ShareClassMoveType(int moveTypeId, String moveTypeName)
	{
		this.moveTypeId = moveTypeId;
		this.moveTypeName = moveTypeName;
	}
	
	public ShareClassMoveType(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static ShareClassMoveType get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new ShareClassMoveType(
					DataResultSetUtils.getResultSetIntegerValue(rs, "MOVE_TYPE_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "MOVE_TYPE_NAME"));
		
	}

	public static DataResultSet getData(int moveTypeId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "MOVE_TYPE_ID", moveTypeId);
		return facade.createResultSet("", binder);
	}
	
	public static ShareClassMoveType get(int moveTypeId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(moveTypeId, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "MOVE_TYPE_ID", this.getMoveTypeId());
		BinderUtils.addParamToBinder(binder, "MOVE_TYPE_NAME", this.getMoveTypeName());
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qTransactions_UpdateShareClassMovementType", binder);
	}
	
	public void setAttributes(DataBinder binder) throws DataException {		
		this.setMoveTypeName(BinderUtils.getBinderStringValue(binder, "MOVE_TYPE_NAME"));
	}
	
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public int getMoveTypeId() {
		return moveTypeId;
	}

	public void setMoveTypeId(int moveTypeId) {
		this.moveTypeId = moveTypeId;
	}

	public String getMoveTypeName() {
		return moveTypeName;
	}

	public void setMoveTypeName(String moveTypeName) {
		this.moveTypeName = moveTypeName;
	}




}
