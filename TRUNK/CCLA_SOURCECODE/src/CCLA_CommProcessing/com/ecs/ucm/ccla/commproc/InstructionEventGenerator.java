package com.ecs.ucm.ccla.commproc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.ecs.ucm.ccla.commproc.events.IInstructionEvent;
import com.ecs.ucm.ccla.commproc.events.ITransactionReferenceEvent;
import com.ecs.ucm.ccla.commproc.events.InstructionEvent;
import com.ecs.ucm.ccla.commproc.events.TransactionReferenceEvent;
import com.ecs.ucm.ccla.commproc.listener.IInstructionListener;
import com.ecs.ucm.ccla.data.instruction.RoutingModule;
import com.ecs.utils.stellent.embedded.FWFacade;

public class InstructionEventGenerator {

	//Singleton object
	private static InstructionEventGenerator instance = null;
	
	//Flag to determine if modules need 
	private boolean isModulesListStale = false;
	
	//List of listeners to send events to.
	private List<IInstructionListener> listeners = new ArrayList<IInstructionListener>();
	
	/**
	 * Gets the singleton instance
	 * @return
	 */
	public static InstructionEventGenerator getInstance() 
	{
		if (instance==null) {
			instance = new InstructionEventGenerator();
			instance.buildListeners();
		}
		
		if (instance.isModulesListStale()) {
			instance.clearListener();
			instance.buildListeners();
		}
		return instance;
	}

	private void buildListeners() 
	{
		if (instance!=null) {
			HashMap<Integer, RoutingModule> moduleCache = 
				 RoutingModule.getCache().getCache();
			
			for (RoutingModule module : moduleCache.values()) {
				instance.addListener(module.getModuleHandler());
			}
			instance.setIsModulesListStale(false);
		}
	}
	
	/**
	 * Set "isModulesListStale" flag to determine whether they need to be refreshed
	 * @param isModulesListStale
	 */
	public void setIsModulesListStale(boolean isModulesListStale) {
		this.isModulesListStale = isModulesListStale;
	}

	/**
	 * Get "isModulesListStale" flag to determine whether they need to be refreshed
	 * @return
	 */
	public boolean isModulesListStale() {
		return isModulesListStale;
	}

	/**
	 * Add a listener to send events to
	 * @param l
	 */
    private synchronized void addListener(IInstructionListener l) {
        listeners.add(l);
    }
    
    /**
     * Remove listener from the list
     * @param l
     */
    private synchronized void removeListener(IInstructionListener l) {
        listeners.remove(l);
    }
     
    private synchronized void clearListener() {
        listeners.clear();
    }
    
    /**
     * Fires event to all listeners
     * @param event
     */
    public synchronized void fireEvent(IInstructionEvent event) 
    {  	
    	Iterator<IInstructionListener> instrListeners = listeners.iterator();
        while(instrListeners.hasNext()) {
            ((IInstructionListener)instrListeners.next()).receivedEvent(event);
        }
    }	
    
    /**
     * Trigger an instruction event
     * @param instrId
     */
    public synchronized void triggerInstructionEvent(int instrId, String userId, FWFacade facade) 
    {
    	IInstructionEvent event = 
    		new InstructionEvent(instrId, userId, facade, this);
    	fireEvent(event);
    }
    
    /**
     * Triggers an transaction reference event
     * @param instrId
     * @param transRef
     */
    public synchronized void triggerTransactionRefEvent(int instrId, String userId, FWFacade facade, String transRef) 
    {
    	ITransactionReferenceEvent event = 
    		new TransactionReferenceEvent(instrId, userId, facade, this, transRef);
    	fireEvent(event);
    }
}
