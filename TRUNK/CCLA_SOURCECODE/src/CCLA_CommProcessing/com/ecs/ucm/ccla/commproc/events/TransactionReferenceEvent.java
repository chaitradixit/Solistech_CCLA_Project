package com.ecs.ucm.ccla.commproc.events;

import com.ecs.utils.stellent.embedded.FWFacade;

public class TransactionReferenceEvent extends InstructionEvent 
implements ITransactionReferenceEvent{

	//Class name
	public final static String CLASS_NAME = "TransactionReferenceEvent";
	
	private static final long serialVersionUID = 1L;
	private String transRef = null;
	
	public TransactionReferenceEvent(int instrId, String userName, 
			FWFacade facade, Object source, String transRef) {
		super(instrId, userName, facade, source);
		this.transRef = transRef;
	}

	public void setTransactionReference(String transRef) {
		this.transRef = transRef;
	}

	public String getTransactionRefence() {
		return transRef;
	}

}
