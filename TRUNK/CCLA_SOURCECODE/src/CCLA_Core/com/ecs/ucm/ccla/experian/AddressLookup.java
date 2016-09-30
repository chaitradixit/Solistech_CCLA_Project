package com.ecs.ucm.ccla.experian;

import java.util.Vector;

import org.apache.axis.types.NonNegativeInteger;
import org.apache.axis.types.PositiveInteger;

import com.ecs.ucm.ccla.experian.ExperianGlobals;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.experian.webservice.Address;
import com.experian.webservice.AddressLineType;
import com.experian.webservice.Picklist;
import com.experian.webservice.PicklistEntryType;
import com.experian.webservice.QAFault;
import com.experian.webservice.QAGetAddress;
import com.experian.webservice.QAPicklistType;
import com.experian.webservice.QARefine;
import com.experian.webservice.QASearch;
import com.experian.webservice.QASearchResult;

import intradoc.common.ServiceException;
import intradoc.data.DataResultSet;

public class AddressLookup extends Search {
		
	/** Searches the dataset using the passed String. Expected to be
	 *  comma-delimited.
	 *  
	 *  TODO:
	 *  Check if the picklist only contains a single entry with the 
	 *  isWarnInformation flag set. If this is true, there was an error
	 *  fetching the address matches. Display the warning/error message
	 *  instead of a result list.
	 *  
	 * @throws ServiceException
	 */
	public void searchAddress() throws ServiceException {
		canSearch();
		Log.debug("after canSearch");
		String searchStr = "";
		
		String overrideString = m_binder.getLocal("overrideString");
		Log.debug("overrideString:"+overrideString);
		if (!StringUtils.stringIsBlank(overrideString))
		{
			searchStr = overrideString;
		} else 
		{
			searchStr = getAddressSearchString();	
		}
	
		try {
			QASearch search = new QASearch
			 (ExperianGlobals.COUNTRY_DATASET, 
			  ExperianGlobals.SINGLE_LINE_SEARCH_ENGINE_FLATTENED,
			  ExperianGlobals.PRO_WEB_LAYOUT, 
			  null, searchStr, null);
			
			QASearchResult results = getExperianWS().doSearch(search);
			
			QAPicklistType pickList = results.getQAPicklist();
			
			m_binder.putLocal("isLargePotential", 
			 Boolean.toString(pickList.isLargePotential()));
			
			m_binder.putLocal("pickListMoniker", pickList.getFullPicklistMoniker());
			
			if (pickList.getDisplayText() != null)
				m_binder.putLocal("displayText", pickList.getDisplayText());
			
			m_binder.putLocal("isAutoStepInSafe", 
			 Boolean.toString(pickList.isAutoStepinSafe()));
			
			PicklistEntryType[] entries = pickList.getPicklistEntry();
			String[] cols = new String[] {
					"Moniker","PartialAddress","Picklist","Score"
				};
			
			DataResultSet rsAddress = new DataResultSet(cols);
			
			m_binder.putLocal("numEntries", Integer.toString(entries.length));

			for (int i=0; i<entries.length; i++) {
				/** Is isWarnInformation set to true, check getPicklist() function
				 *  to see error/warning message
				 */
				Vector<String> v = new Vector<String>();
				v.add(entries[i].getMoniker());
				v.add(entries[i].getPartialAddress());
				v.add(entries[i].getPicklist());
				v.add(entries[i].getScore().toString());
				rsAddress.addRow(v);
				m_binder.putLocal("Entry" + i, entries[i].getPartialAddress() + 
				 ", Moniker: " + entries[i].getMoniker() + ", warn? " + 
				 entries[i].isWarnInformation() + ", pickListValue: " + entries[i].getPicklist());
			}
			
			m_binder.addResultSet("rsAddresses", rsAddress);
		} catch (QAFault e) {
			String msg = 
				"QAFault: code: " + e.getErrorCode() 
				 + ", message: " + e.getErrorMessage();
			
			Log.error(msg);
			
			throw new ServiceException(msg);
		} catch (Exception e) {
			Log.error("Failed to call Experian web service", e);
			throw new ServiceException(e);
		}
	}
	
	/** Searches the dataset using the passed String. Expected to be
	 *  comma-delimited.
	 *  
	 *  Returns tree-based results which can be expanded further by the
	 *  user.
	 *  
	 *  TODO:
	 *  Check if the picklist only contains a single entry with the 
	 *  isWarnInformation flag set. If this is true, there was an error
	 *  fetching the address matches. Display the warning/error message
	 *  instead of a result list.
	 *  
	 * @throws ServiceException
	 */
	public void searchAddressTree() throws ServiceException {
		canSearch();
		String searchStr = "";
		
		String overrideString = m_binder.getLocal("overrideString");
		Log.debug("overrideString:"+overrideString);
		if (!StringUtils.stringIsBlank(overrideString))
		{
			searchStr = overrideString;
		} else 
		{
			searchStr = getAddressSearchString();	
		}
	
		try {
			QASearch search = new QASearch
			 (ExperianGlobals.COUNTRY_DATASET, 
			  ExperianGlobals.SINGLE_LINE_SEARCH_ENGINE_TREE,
			  ExperianGlobals.PRO_WEB_LAYOUT, 
			  null, searchStr, null);
			
			QASearchResult results = getExperianWS().doSearch(search);
			
			QAPicklistType pickList = results.getQAPicklist();
			
			m_binder.putLocal("isLargePotential", 
			 Boolean.toString(pickList.isLargePotential()));
			
			m_binder.putLocal("pickListMoniker", pickList.getFullPicklistMoniker());
			
			if (pickList.getDisplayText() != null)
				m_binder.putLocal("displayText", pickList.getDisplayText());
			
			m_binder.putLocal("totalResults", pickList.getTotal().toString());
			
			m_binder.putLocal("isAutoStepInSafe", 
			 Boolean.toString(pickList.isAutoStepinSafe()));
			
			PicklistEntryType[] entries = pickList.getPicklistEntry();
			String[] cols = new String[] {
				"Moniker","PartialAddress","Picklist","Score","Multiples"
			};
			
			DataResultSet rsAddress = new DataResultSet(cols);
			
			m_binder.putLocal("numEntries", Integer.toString(entries.length));

			for (int i=0; i<entries.length; i++) {
				/** Is isWarnInformation set to true, check getPicklist() function
				 *  to see error/warning message
				 */
				Vector<String> v = new Vector<String>();
				
				v.add(entries[i].getMoniker());
				v.add(entries[i].getPartialAddress());
				v.add(entries[i].getPicklist());
				v.add(entries[i].getScore().toString());
				v.add(Boolean.toString(entries[i].isMultiples()));
				
				rsAddress.addRow(v);
				
				/*
				String debugStr = "Entry " + i + ": " +
				entries[i].getPartialAddress() + 
				 ", Moniker: " + entries[i].getMoniker() + ", warn? " + 
				 entries[i].isWarnInformation() 
				 + ", multiples? " + entries[i].isMultiples()
				 + ", pickListValue: " + entries[i].getPicklist();
				
				Log.debug(debugStr);
				*/
				
				m_binder.putLocal("Entry" + i, entries[i].getPartialAddress() + 
				 ", Moniker: " + entries[i].getMoniker() + ", warn? " + 
				 entries[i].isWarnInformation() 
				 + ", pickListValue: " + entries[i].getPicklist());
			}
			
			m_binder.addResultSet("rsAddresses", rsAddress);
			
		} catch (QAFault e) {
			String msg = 
				"QAFault: code: " + e.getErrorCode() 
				 + ", message: " + e.getErrorMessage();
			
			Log.error(msg);
			
			throw new ServiceException(msg);
		} catch (Exception e) {
			Log.error("Failed to call Experian web service", e);
			throw new ServiceException(e);
		}
	}
	
	/** Refines the search, based on the passed moniker of a tree-based
	 *  search result.
	 *  
	 *  Requires a 'moniker' present in the binder.
	 *  
	 *  TODO:
	 *  Check if the picklist only contains a single entry with the 
	 *  isWarnInformation flag set. If this is true, there was an error
	 *  fetching the address matches. Display the warning/error message
	 *  instead of a result list.
	 *  
	 * @throws ServiceException
	 */
	public void refineSearchAddress() throws ServiceException {
		canSearch();
		
		String moniker 		= m_binder.getLocal("moniker");
		String refinement 	= m_binder.getLocal("refinement");
	
		Log.debug("Refining QAS search: moniker=" + moniker + 
		 ", refinement=" + refinement);
		
		if (refinement == null)
			refinement 		= "";
		
		try {			
			QARefine refine = new QARefine(moniker, refinement, null, 
			 new PositiveInteger("80"), new NonNegativeInteger("300"));
			
			Picklist results = getExperianWS().doRefine(refine);
			QAPicklistType pickList = results.getQAPicklist();
			
			m_binder.putLocal("isLargePotential", 
			 Boolean.toString(pickList.isLargePotential()));
			
			m_binder.putLocal("pickListMoniker", pickList.getFullPicklistMoniker());
			
			if (pickList.getDisplayText() != null)
				m_binder.putLocal("displayText", pickList.getDisplayText());
			
			m_binder.putLocal("totalResults", pickList.getTotal().toString());
			
			m_binder.putLocal("isAutoStepInSafe", 
			 Boolean.toString(pickList.isAutoStepinSafe()));
			
			PicklistEntryType[] entries = pickList.getPicklistEntry();
			String[] cols = new String[] {
				"Moniker","PartialAddress","Picklist","Score","Multiples"
			};
			
			DataResultSet rsAddress = new DataResultSet(cols);
			
			m_binder.putLocal("numEntries", Integer.toString(entries.length));
			
			Log.debug("Found " + entries.length + " refined QAS results");
			
			for (int i=0; i<entries.length; i++) {
				/** Is isWarnInformation set to true, check getPicklist() function
				 *  to see error/warning message
				 */
				Vector<String> v = new Vector<String>();
				
				v.add(entries[i].getMoniker());
				v.add(entries[i].getPartialAddress());
				v.add(entries[i].getPicklist());
				v.add(entries[i].getScore().toString());
				v.add(Boolean.toString(entries[i].isMultiples()));

				rsAddress.addRow(v);
				
				/*
				String debugStr = "Entry " + i + ": " +
				entries[i].getPartialAddress() + 
				 ", Moniker: " + entries[i].getMoniker() + ", warn? " + 
				 entries[i].isWarnInformation() 
				 + ", multiples? " + entries[i].isMultiples()
				 + ", pickListValue: " + entries[i].getPicklist();
				
				Log.debug(debugStr);
				*/
				
				m_binder.putLocal("Entry" + i, entries[i].getPartialAddress() + 
				 ", Moniker: " + entries[i].getMoniker() + ", warn? " + 
				 entries[i].isWarnInformation() 
				 + ", pickListValue: " + entries[i].getPicklist());
			}
			
			m_binder.addResultSet("rsAddresses", rsAddress);
			
		} catch (QAFault e) {
			String msg = 
				"QAFault: code: " + e.getErrorCode() 
				 + ", message: " + e.getErrorMessage();
			
			Log.error(msg, e);
			
			throw new ServiceException(msg);
		} catch (Exception e) {
			Log.error("Failed to call Experian web service", e);
			throw new ServiceException(e);
		}
	}
	
	/** Fetches a name/address for a given QAS moniker. 
	 *  
	 *  Requires a 'moniker' value present in the binder. If not
	 *  present, no error is thrown.
	 *  
	 *  The address (and potentially name) data fetched from QAS
	 *  is placed into a ResultSet called rsQASData.
	 *   
	 **/
	public void getQASData() throws ServiceException {
		String moniker = m_binder.getLocal("moniker");
		
		if (!StringUtils.stringIsBlank(moniker)) {
			try {
				canSearch();
				QAGetAddress getAddress = 
				 new QAGetAddress("experian", moniker, null);
				
				Address address = getExperianWS().doGetAddress(getAddress);		
				AddressLineType[] addressLines = address.getQAAddress();
				String[]addCols = ExperianGlobals.addressCols;

				DataResultSet rsQASData = new DataResultSet(addCols);
				Vector<String> addV = new Vector<String>();
				
				for (int i=0; i<addressLines.length; i++) {
					Log.debug("adding:" + addressLines[i].getLine());
					Log.debug("adding:" + addressLines[i].getLabel());
					addV.add(addressLines[i].getLine());
				}
				rsQASData.addRow(addV);
				m_binder.addResultSet("rsQASData", rsQASData);
				
			} catch (QAFault e) {
				String msg = 
					"QAFault: code: " + e.getErrorCode() 
					 + ", message: " + e.getErrorMessage();
				
				Log.error(msg);
				throw new ServiceException(msg);
				
			} catch (Exception e) {
				Log.error("Failed to call Experian web service", e);
				throw new ServiceException(e);
			}
		} else {
			Log.debug("No QAS moniker passed, initializing empty form");
		}
	}
		
	public String getAddressSearchString() {

		String searchStr = "";
		String Comma = "";
		String useAddress = m_binder.getLocal("useAddress");
		Log.debug("useAddress is " + useAddress);
		if (useAddress == null || useAddress.length() <= 0)
		{	
			String FlatAddress = m_binder.getLocal("FLAT");
			if (FlatAddress != null && FlatAddress.length() >0 )
			{
				Comma = ",";
				searchStr = FlatAddress;
			}
			String HouseName = m_binder.getLocal("HOUSENAME");
			if (HouseName != null && HouseName.length() >0 )
			{
				Comma = ",";
				searchStr = searchStr + Comma + HouseName;
			}
			String HouseNumber = m_binder.getLocal("HOUSENUMBER");
			if (HouseNumber != null && HouseNumber.length() >0 )
			{
				Comma = ",";
				searchStr = searchStr + Comma + HouseNumber;
			}	
			String StreetAddress = m_binder.getLocal("STREET");
			if (StreetAddress != null && StreetAddress.length() >0 )
			{
				Comma = ",";
				searchStr = searchStr + Comma + StreetAddress;
			}
			String District = m_binder.getLocal("DISTRICT");
			if (District != null && District.length() >0 )
			{
				Comma = ",";
				searchStr = searchStr + Comma + District;
			}
			String Town = m_binder.getLocal("TOWN");
			if (Town != null && Town.length() >0 )
			{
				Comma = ",";
				searchStr = searchStr + Comma + Town;
			}
			String County = m_binder.getLocal("COUNTY");
			if (County != null && County.length() >0 )
			{
				Comma = ",";
				searchStr = searchStr + Comma +County;
			}
			String Postcode = m_binder.getLocal("POSTCODE");
			if (Postcode != null && Postcode.length() >0 )
			{
				Comma = ",";
				searchStr = searchStr + Comma + Postcode;
			}
		} else
		{
			searchStr = useAddress;
		}
		Log.debug("passing:" + searchStr);
		return searchStr;
	}


}
