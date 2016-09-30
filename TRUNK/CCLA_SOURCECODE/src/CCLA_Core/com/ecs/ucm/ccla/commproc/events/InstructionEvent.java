package com.ecs.ucm.ccla.commproc.events;

import java.util.EventObject;

import com.ecs.utils.stellent.embedded.FWFacade;

public class InstructionEvent extends EventObject implements
		IInstructionEvent {

	//Class name
	public final static String CLASS_NAME = "InstructionEvent";
	
	private static final long serialVersionUID = 1L;
	private int instrId;
	private FWFacade facade;
	private String userName;
	
	public InstructionEvent(int instrId, String userName, FWFacade facade, Object source) {
		super(source);
		this.instrId = instrId;
		this.userName = userName;
		this.facade = facade;
	}

	public void setInstructionId(int instrId) {
		this.instrId = instrId;
	}

	public int getInstructionId() {
		return instrId;
	}

	public void setFacade(FWFacade facade) {
		this.facade = facade;
	}

	public FWFacade getFacade() {
		return facade;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}
