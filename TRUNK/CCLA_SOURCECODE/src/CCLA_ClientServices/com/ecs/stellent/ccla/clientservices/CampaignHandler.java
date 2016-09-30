package com.ecs.stellent.ccla.clientservices;

import com.ecs.ucm.ccla.data.Campaign;
import com.ecs.utils.ClassLoaderUtils;
import com.ecs.utils.Log;
import intradoc.common.ServiceException;

import java.lang.reflect.Constructor;

import com.ecs.stellent.ccla.clientservices.processhandler.ClientProcessHandler;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Used for generating ClientProcessHandler instances. These may
 *  use a special subclass of the ClientProcessHandler class, depending
 *  on the associated Campaign data.
 *  
 * @author Tom Marchant
 *
 */
public class CampaignHandler {
	
	protected Campaign campaign;
	protected String userName;
	
	protected FWFacade facade;
	
	/** Default Client Process Handler class to use, if one isn't specified
	 *  for this campaign.
	 */
	protected Class DEFAULT_CLIENT_PROCESS_HANDLER = 
	 ClientProcessHandler.class;
	
	/** Default Person Process Handler class to use, if one isn't specified
	 *  for this campaign.
	 */
	protected Class DEFAULT_PERSON_PROCESS_HANDLER = 
	 PersonProcessHandler.class;
	
	public CampaignHandler(Campaign campaign, String userName, FWFacade facade) {
		this.campaign = campaign;
		this.userName = userName;
		
		this.facade = facade;
	}
	
	/** Creates a new process handler, specific to the current campaign.
	 *  
	 * @return
	 * @throws ServiceException
	 */
	public ClientProcessHandler createClientProcessHandler() 
	 throws ServiceException {
		
		ClientProcessHandler processHandler = null;
		
		try {
			Class processHandlerClass = null;
			
			String className = this.campaign.getProcessHandler();
			
			if (StringUtils.stringIsBlank(className)) {
				Log.info("Using default client process handler");
				processHandlerClass = DEFAULT_CLIENT_PROCESS_HANDLER;
			} else {
				Log.info("Loading client process handler: " + className);
				processHandlerClass =
					ClassLoaderUtils.getComponentClassLoader().loadClass(className);			
			}
			
			// ClientProcessHandler constructor parameters and values
			Class[] params = new Class[] {
				Campaign.class, String.class, FWFacade.class
			};
			
			Object[] values = new Object[] {
				this.campaign, this.userName, this.facade
			};
			
			Constructor constructor = 
			 processHandlerClass.getConstructor(params);
			
			processHandler = (ClientProcessHandler)constructor.newInstance(values);
			
		} catch (Exception e) {
			throw new ServiceException("Unable to create Client Process Handler: " +
			 e.toString());
		}

		return processHandler;
	}
	
	/** Creates a new process handler, specific to the current campaign.
	 *  
	 * @return
	 * @throws ServiceException
	 */
	public PersonProcessHandler createPersonProcessHandler() 
	 throws ServiceException {
		
		PersonProcessHandler processHandler = null;
		
		try {
			Class processHandlerClass = null;
			
			String className = this.campaign.getProcessHandler();
			
			if (StringUtils.stringIsBlank(className)) {
				Log.info("Using default person process handler");
				processHandlerClass = DEFAULT_PERSON_PROCESS_HANDLER;
			} else {
				Log.info("Loading person process handler: " + className);
				processHandlerClass =
					ClassLoaderUtils.getComponentClassLoader().loadClass(className);			

			}
			
			// ClientProcessHandler constructor parameters and values
			Class[] params = new Class[] {
				Campaign.class, String.class, FWFacade.class
			};
			
			Object[] values = new Object[] {
				this.campaign, this.userName, this.facade
			};
			
			Constructor constructor = 
			 processHandlerClass.getConstructor(params);
			
			processHandler = (PersonProcessHandler)constructor.newInstance(values);
			
		} catch (Exception e) {
			throw new ServiceException("Unable to create Person Process Handler: " +
			 e.toString());
		}

		return processHandler;
	}
}
