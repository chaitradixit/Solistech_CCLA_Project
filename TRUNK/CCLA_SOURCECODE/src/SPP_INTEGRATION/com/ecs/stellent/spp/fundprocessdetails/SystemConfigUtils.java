package com.ecs.stellent.spp.fundprocessdetails;

import java.util.Date;
import java.util.HashMap;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public @Deprecated class SystemConfigUtils {

	/* ****** Constants ******* */
	public static final String NAME_COLUMN 			= "SYSTEM_CONFIG_NAME";
	public static final String VALUE_COLUMN 		= "SYSTEM_CONFIG_SETTING";
	public static final String DESCRIPTION_COLUMN 	= "SYSTEM_CONFIG_DESCRIPTION";
	
	
	/** cache expiry time before forcing a reload, default is 5 mins **/
	public static long EXPIRY_TIME_MILLIS = 
	 (long)SharedObjects.getEnvironmentInt("SPP_ConfigSettingCacheExpiry", 5) * 1000 * 60;
	
	
	/* ********************* Methods *************************** */	
	private static Date lastUpdated = null; //Last updated value to determine refresh
	private static HashMap<String,String> sysMap = null;
	private static boolean isCacheInitialised = false;
	
	/**
	 * Refreshes the system config cache
	 * @param facade
	 * @throws DataException
	 */
	public @Deprecated static void refreshSystemConfigCache(FWFacade facade) 
	 throws DataException {
		
		//First set the initialised to false;
		isCacheInitialised = false;
		
		if (sysMap!=null) {
			sysMap.clear();
		} else { 
			sysMap = new HashMap<String,String>();
		}

		DataBinder binder = new DataBinder();
		
		DataResultSet rs = 
		 facade.createResultSet("qGetSystemConfigSetting", binder);
		
		if (!rs.isEmpty()) {
			do {
				sysMap.put(rs.getStringValueByName(NAME_COLUMN),
						rs.getStringValueByName(VALUE_COLUMN));				
			} while(rs.next());
		} 
		
		isCacheInitialised = true;
		lastUpdated = DateUtils.getNow();
	}
	
	public @Deprecated static synchronized void checkSystemConfigCache(FWFacade facade) 
	 throws DataException {
		
		long currentTime = System.currentTimeMillis();
		
		if (!isCacheInitialised() ||
			(lastUpdated!=null && currentTime - lastUpdated.getTime() < EXPIRY_TIME_MILLIS)) {
			
			refreshSystemConfigCache(facade);
		}
	}
	
	public @Deprecated static boolean isCacheInitialised() {
		return isCacheInitialised;
	}
	

	public @Deprecated static String getSystemConfigValue(String propName)
	throws DataException {
		
		if (sysMap!=null && !sysMap.isEmpty()) {
			return (String)sysMap.get(propName);
		} else { 
			return null;
		}
	}
	
}
