package com.ecs.ucm.ccla.utils;

import intradoc.data.DataException;

import java.util.Iterator;
import java.util.Map;

import org.apache.axis.client.Call;
import org.apache.commons.beanutils.PropertyUtils;

import com.ecs.utils.Log;

public class ObjectUtils {

	public static Map<String, Object> describeData(Object dataObj) 
	 throws DataException {
		
		if (dataObj==null)
			return null;
		
		try {
			Map<String, Object> map = PropertyUtils.describe(dataObj);
			
			Log.debug("Describing "+dataObj.getClass().getName());
			
			for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) 
			{			
				String key = (String) iter.next();
				//Object dataType = PropertyUtils.getPropertyType(dataObj, key);
				
				Object value = (Object)PropertyUtils.getProperty(dataObj, key);
				
				if (value instanceof String) {
					Log.debug(value.getClass().getName()+
							": [key:"+key+", value:"+(String)value+"]");
				} else if (value instanceof Integer) {
					Log.debug(value.getClass().getName()+
							": [key:"+key+", value:"+((Integer)value).toString()+"]");						
				} else if (value instanceof Float) {
					Log.debug(value.getClass().getName()+
							": [key:"+key+", value:"+((Float)value).toString()+"]");
				} else if (value instanceof Long) {
					Log.debug(value.getClass().getName()+
							": [key:"+key+", value:"+((Long)value).toString()+"]");	
				} else if (value instanceof Double) {
					Log.debug(value.getClass().getName()+
							": [key:"+key+", value:"+((Double)value).toString()+"]");						
				} else if (value instanceof Boolean) {
					Log.debug(value.getClass().getName()+
							": [key:"+key+", value:"+((Boolean)value).toString()+"]");
				} else if (value instanceof Class) {
					Log.debug(value.getClass().getName()+
							": [key:"+key+", value:"+value.getClass().getName()+"]");
				} else {
					describeData(value);
				}
			}
			
			return map;
			
		} catch (Exception e) {
			String msg = "Unable to describe object: " + e.getMessage();
			
			Log.error(msg, e);
			throw new DataException(msg, e);
		}
	}
	
	/** Compares two sets of name-value pairs acquired from the method above.
	 *  
	 *  Only reports differences.
	 *  
	 * @param obj1
	 * @param obj2
	 */
	public static void compare(Map<String, Object> obj1, Map<String, Object> obj2) {
		
		Log.debug("Comparing " + obj1.size() + " name-value pairs against " 
		 + obj2.size() + " name-value pairs");
		
		int numComparisons = 0;
		
		for (Map.Entry<String, Object> nameValue1 : obj1.entrySet()) {
			
			// Search for matching name in second name-value set
			for (Map.Entry<String, Object> nameValue2 : obj2.entrySet()) {
				
				if (nameValue1.getKey().equals(nameValue2.getKey())) {
					Object value1 = nameValue1.getValue();
					Object value2 = nameValue2.getValue();
					
					if ((value1 == null && value2 == null)
						||
						((value1 != null && value2 != null)
						&&
						(value1.equals(value2)))) {
						Log.debug("Matched field: " + nameValue1.getKey());
					} else {
						Log.debug("Mismatched field: " + nameValue1.getKey() +
						 " (" + value1 + ", " + value2 + ")");
					}
					
					numComparisons++;
					
					break;
				}
			}
		}
		
		Log.debug("Made " + numComparisons + " comparisons");
	}
	
    /**
     * 
     * @param _call
     */
	public static void logSoapMessage(org.apache.axis.client.Call _call) {
		
		if (_call == null)
			return;
		
		try {
			String requestXML = _call.getMessageContext().getRequestMessage().getSOAPPartAsString();  
			String responseXML = _call.getMessageContext().getResponseMessage().getSOAPPartAsString();

			Log.info("---RequestMsg: "+requestXML);
			Log.info("---ResponseMsg: "+responseXML);
		} catch (Exception e) {
			Log.error("Error logging messages "+e.getMessage());
		}
	}	
}
