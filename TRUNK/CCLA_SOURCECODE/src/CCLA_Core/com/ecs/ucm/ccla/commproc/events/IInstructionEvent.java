package com.ecs.ucm.ccla.commproc.events;

import com.ecs.utils.stellent.embedded.FWFacade;


public interface IInstructionEvent {

	public void setInstructionId(int instrId);
	public int getInstructionId();
	
	public void setUserName(String userName);
	public String getUserName();
	
	public void setFacade(FWFacade facade);
	public FWFacade getFacade();
	
}
