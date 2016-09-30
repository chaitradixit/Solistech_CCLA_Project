package com.ecs.stellent.spp.service;

import com.ecs.stellent.spp.fundprocessdetails.FundProcessDetailsManager;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.server.Service;

public class SystemConfigVarService extends Service {

	
	public void addSystemConfigVar() throws DataException, ServiceException {

		String configName = m_binder.get("CONFIG_VAR_NAME");
		String configDesc = m_binder.get("CONFIG_VAR_DESCRIPTION");
		String configDataType = m_binder.get("CONFIG_VAR_DATA_TYPE");
		String configType = m_binder.get("CONFIG_VAR_TYPE");
		
		if (StringUtils.stringIsBlank(configName) || StringUtils.stringIsBlank(configDataType)) {
			throw new DataException("SystemConfigVar data is blank. Cannot add!");
		}
		
		//Check if system var already exist
		SystemConfigVar systemConfigVar = SystemConfigVar.getCache().getCachedInstance(configName);
		if (systemConfigVar!=null) {
			throw new DataException("SystemConfigVar with name already exist "+configName);
		}
		Object value = null;
		

		if (configDataType.equals(SystemConfigVar.DataType.BOOL.toString())) {
				value = CCLAUtils.getBinderBoolValue(m_binder, "BOOL_VALUE");						
		} else if (configDataType.equals(SystemConfigVar.DataType.DATE.toString())) {
				value = CCLAUtils.getBinderDateValue(m_binder, "DATE_VALUE");
		} else if (configDataType.equals(SystemConfigVar.DataType.STRING.toString())) {
				value = m_binder.getLocal("STRING_VALUE");
		} else if (configDataType.equals(SystemConfigVar.DataType.INT.toString())) {
				value = CCLAUtils.getBinderIntegerValue(m_binder, "INT_VALUE");
		} else if (configDataType.equals(SystemConfigVar.DataType.FLOAT.toString())) {
				value = CCLAUtils.getBinderFloatValue(m_binder, "FLOAT_VALUE");
		}
		
		if (value==null) {
			throw new DataException("SystemConfigVar data value is null");
		}
			
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		SystemConfigVar.add(configName, configDesc, SystemConfigVar.DataType.valueOf(configDataType), configType, value, facade, m_userData.m_name);
	
		//finally update the SystemConfigVarCache
		SystemConfigVar.getCache().buildCache(facade);
		SystemConfigVar.getTypeCache().buildCache(facade);
		
	}
	
	public void updateSystemConfigVar() throws DataException, ServiceException 
	{
		String configName = m_binder.get("CONFIG_VAR_NAME");

		if (StringUtils.stringIsBlank(configName)) {
			throw new DataException("SystemConfigVar name is blank");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		SystemConfigVar systemConfigVar = SystemConfigVar.getCache().getCachedInstance(configName);
		
		if (systemConfigVar==null)
			throw new DataException("Cannot find SystemConfigVar with name :"+configName);
		
		systemConfigVar.setAttributes(m_binder);
		systemConfigVar.persist(facade, m_userData.m_name);
		
		//finally update the SystemConfigVarCache
		SystemConfigVar.getCache().buildCache(facade);
		SystemConfigVar.getTypeCache().buildCache(facade);
		
		if (systemConfigVar.getType().equals(FundProcessDetailsManager.PROCESS_DATES_GROUP_NAME) ||
				systemConfigVar.getType().equals(FundProcessDetailsManager.PROCESS_DATES_CONFIG_GROUP_NAME)) 
			FundProcessDetailsManager.refreshProcessDetailsCache(facade);
	}
}
