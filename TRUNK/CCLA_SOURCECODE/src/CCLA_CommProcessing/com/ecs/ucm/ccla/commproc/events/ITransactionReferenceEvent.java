package com.ecs.ucm.ccla.commproc.events;

public interface ITransactionReferenceEvent extends IInstructionEvent {
	
	public void setTransactionReference(String transRef);
	public String getTransactionRefence();
}
