package com.ecs.ucm.ccla.data.instruction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.ClassLoaderUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the REF_UCM_METADATA_TRANSLATION table.
 *  
 *  Each entry corresponds to an Instruction Data field. There will be at most
 *  one translator per Instruction Data field.
 * 
 * @author Tom
 *
 */
public class UCMMetadataTranslation implements Persistable {

	private InstructionData instructionData;
	
	private String ucmFieldName;
	private UCMFieldTranslator translator;
	
	public UCMMetadataTranslation(InstructionData instructionData,
	 Class<? extends UCMFieldTranslator> translatorClass, String ucmFieldName) 
	 throws DataException {
		
		this.instructionData = instructionData;
		this.ucmFieldName = ucmFieldName;
		
		if (translatorClass == null)
			this.translator = null;
		else {
			// Create an instance of the UCMFieldTranslator class
			try {
				this.setTranslator(translatorClass.newInstance());
				
			} catch (Exception e) {
				String msg = "Failed to instantiate UCM Field Translator class " +
				translatorClass.getName();
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}	
		}
	}
	
	/** Fetches all UCMMetadataTranslation instances from the database.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<UCMMetadataTranslation> getAll(FWFacade facade) 
	 throws DataException {
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllUCMMetadataTranslations", new DataBinder());
		
		Vector<UCMMetadataTranslation> translations = 
		 new Vector<UCMMetadataTranslation>();
		
		if (rs.first()) {
			do {
				translations.add(get(rs));
			} while (rs.next());
		}
		
		return translations;
	}
	
	@SuppressWarnings("unchecked")
	public static UCMMetadataTranslation get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		// Check for a non-null Translation Handler Class name
		String className = rs.getStringValueByName("TRANSLATION_HANDLER_CLASS");
		Class<? extends UCMFieldTranslator> translatorClass = null;
		
		if (!StringUtils.stringIsBlank(className)) {
			try {
				// Attempt to load the Class from its name.
				translatorClass =  (Class<? extends UCMFieldTranslator>)
				 ClassLoaderUtils.getComponentClassLoader().loadClass(className);
				
			} catch (Exception e) {
				String msg = "Unable to load UCMFieldTranslator class: " + className +
				 ", " + e.getMessage();
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}
		}
		
		return new UCMMetadataTranslation(
			InstructionData.getCache().getCachedInstance(
			 CCLAUtils.getResultSetIntegerValue(rs, "INSTRUCTION_DATA_ID")
			),
			translatorClass,
			rs.getStringValueByName("UCM_FIELD_NAME")
		);
	}
	
	/** Translates a field from the passed UCM Document instance, using either
	 *  the bound UCMFieldTranslator class, or the UCM metadata field directly.
	 *  
	 * @param lwDoc
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public InstructionDataFieldValue translate
	 (LWDocument lwDoc, FWFacade facade) throws DataException {
		
		if (this.getTranslator() != null) {
			// Use bound UCMFieldTranslator class instance
			return translator.getInstructionFieldValue
			 (lwDoc, this.getUcmFieldName(), facade);
				
		} else if (!StringUtils.stringIsBlank(this.getUcmFieldName())) {
			// Return the UCM metadata field value in its raw form
			String rawValue = null;
			
			try {
				rawValue = lwDoc.getAttribute(this.getUcmFieldName());
			} catch (Exception e) {
				String msg = "Failed to fetch UCM document attribute '" +
				 this.getUcmFieldName();
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}
			
			InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
			 (this.getInstructionData().getDataType());
			
			fieldValue.setValue(rawValue);
			return fieldValue;
				
		} else {
			throw new DataException("Unable to translate field, no bound " +
			 "translator method for Instruction Data Field: " + 
			 this.getInstructionData().getName());
		}
	}
	
	/** Translates a field from the passed UCM Document instance, using either
	 *  the bound UCMFieldTranslator class, or the UCM metadata field directly.
	 *  
	 * @param lwDoc
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public InstructionDataFieldValue translate
	 (DataResultSet rsDoc, FWFacade facade) throws DataException {
		
		if (this.getTranslator() != null) {
			// Use bound UCMFieldTranslator class instance
			return this.getTranslator().getInstructionFieldValue
			 (rsDoc, this.getUcmFieldName(), facade);
				
		} else if (!StringUtils.stringIsBlank(this.getUcmFieldName())) {

			InstructionDataFieldValue fieldValue = null;
			
			//11g changes
			if (this.getInstructionData().getDataType().equals(DataType.DATE)) {
				
				Date dateValue = rsDoc.getDateValueByName(this.getUcmFieldName());
				fieldValue = new InstructionDataFieldValue
				 (this.getInstructionData().getDataType());			
				
				fieldValue.setDateValue(dateValue);
				
			} else {
				// Return the UCM metadata field value in its raw form
				String rawValue = rsDoc.getStringValueByName(this.getUcmFieldName());
			
				fieldValue = new InstructionDataFieldValue
				 (this.getInstructionData().getDataType());			
				fieldValue.setValue(rawValue);
			}
			return fieldValue;
				
		} else {
			throw new DataException("Unable to translate field, no bound " +
			 "translator method for Instruction Data Field: " + 
			 this.getInstructionData().getName());
		}
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");
	}

	public void persist(FWFacade facade, String username) throws DataException {
		throw new DataException("Not implemented");

	}

	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");

	}

	public void validate(FWFacade facade) throws DataException {
		throw new DataException("Not implemented");
	}
	

	public InstructionData getInstructionData() {
		return instructionData;
	}

	public void setInstructionData(InstructionData instructionData) {
		this.instructionData = instructionData;
	}

	public void setUcmFieldName(String ucmFieldName) {
		this.ucmFieldName = ucmFieldName;
	}

	public String getUcmFieldName() {
		return ucmFieldName;
	}
	
	public void setTranslator(UCMFieldTranslator translator) {
		this.translator = translator;
	}

	public UCMFieldTranslator getTranslator() {
		return translator;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, UCMMetadataTranslation> getCache() {
		return CACHE;
	}

	/** UCMMetadataTranslation cache implementor.
	 *  
	 *  Maps Instruction Data IDs to their MetadataTranslation instances
	 *  
	 **/
	public static class Cache extends Cachable<Integer, UCMMetadataTranslation> {

		public Cache() {
			super("Instruction Data ID -> UCM Metadata Translation");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<UCMMetadataTranslation> transList = 
			 UCMMetadataTranslation.getAll(facade);
			
			HashMap<Integer, UCMMetadataTranslation> newCache = 
			 new HashMap<Integer, UCMMetadataTranslation>();
			
			for (UCMMetadataTranslation trans : transList) {
				newCache.put(trans.getInstructionData().getInstructionDataId(), trans);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	
	/*
	 *  Caching stuff
	 */
	private static UCMFieldCache UCM_CACHE = new UCMFieldCache();
	
	public static Cachable<String, List<UCMMetadataTranslation>> getUCMFieldCache() {
		return UCM_CACHE;
	}

	/** UCMMetadataTranslation cache implementor.
	 *  
	 *  Maps non-null UCM Metadata Field entries to their MetadataTranslation instances.
	 *  
	 **/
	public static class UCMFieldCache extends Cachable<String, List<UCMMetadataTranslation>> {

		public UCMFieldCache() {
			super("UCM Field Name -> UCM Metadata Translation");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<UCMMetadataTranslation> transList = 
			 UCMMetadataTranslation.getAll(facade);
			
			HashMap<String, List<UCMMetadataTranslation>> newCache = 
			 new HashMap<String, List<UCMMetadataTranslation>>();
			
			for (UCMMetadataTranslation trans : transList) {
				if (trans.getUcmFieldName() != null) {
					List<UCMMetadataTranslation> translations =
					 newCache.get(trans.getUcmFieldName());
					
					if (translations == null) {
						translations = new ArrayList<UCMMetadataTranslation>();
						newCache.put(trans.getUcmFieldName(), translations);
					}
					
					translations.add(trans);
				}
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
