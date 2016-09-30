package com.ecs.ucm.ccla.commproc;

import intradoc.server.Service;

public class InstructionErrorService extends Service{
	
	
	/**
	 * Method that fetches the errorSpooler and puts it in a result set 
	 * on the binder.
	 */
	public void getInstructionErrorSpool(){
		InstructionErrorSpooler ies = 
			RoutingModuleManager.getManager().getInstructionErrorSpooler();
		
		m_binder.addResultSet("rsErrorSpool", ies.getErrorSpoolResultSet());
		
	}
	
	
	/**
	 * Simple method that clears the error spool
	 */
	public void clearErrorSpool(){
		RoutingModuleManager.getManager().getInstructionErrorSpooler().clearErrorSpool();
	}

}
