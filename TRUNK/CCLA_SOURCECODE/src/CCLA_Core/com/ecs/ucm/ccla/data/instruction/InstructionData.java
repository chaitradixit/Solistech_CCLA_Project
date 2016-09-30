package com.ecs.ucm.ccla.data.instruction;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.ClassLoaderUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_INSTRUCTION_DATA table.
 * 
 *  Each entry represents an Instruction Data field that can be mapped against an
 *  Instruction Type.
 * 
 * @author Tom
 *
 */
public class InstructionData implements Persistable {
	
	private int instructionDataId;
	private String name;
	private DataType dataType;
	
	private String label;
	private String description;
	
	public static final class Cols {
		public static final String ID = "INSTRUCTION_DATA_ID";
		public static final String NAME = "INSTRUCTION_DATA_NAME";
	}
	
	/** Only set for dynamically-calculated field values.
	 * 
	 */
	private FieldValueHandler fieldValueHandler;

	/** Instruction Data ID references.
	 * 
	 * @author Tom
	 *
	 */
	public static class Fields {
		public static final int SOURCE_ACCOUNT_ID 			= 1;
		public static final int SOURCE_BANK_ACCOUNT_ID 		= 2;
		public static final int CASH						= 3;
		public static final int UNITS						= 4;
		public static final int ORIGINAL_VALUE				= 5;
		public static final int DEST_ACCOUNT_ID				= 6;
		public static final int DEST_BANK_ACCOUNT_ID		= 7;
		public static final int IFT							= 8;
		public static final int ORGANISATION_ID				= 9;
		public static final int PERSON_ID					= 10;
		public static final int BANKING_DATE				= 11;
		public static final int SETTLEMENT_DATE				= 12;
		public static final int EOD_ID						= 13;
		public static final int SLEEPING_REF				= 14;
		public static final int IS_SUPPORTING				= 15;
		public static final int DOC_DATE					= 16;
		public static final int FORM_ID						= 17;
		public static final int INSTRUCTION_COMMENTS		= 18;
		public static final int UCM_JOB_ID					= 19;
		public static final int DEST_ACCNUMEXT				= 20;
		public static final int WITH_INSTRUCTION			= 21;
		public static final int WORKFLOW_DATE				= 22;
		public static final int SIGNATURES_VALID			= 23;
		public static final int QUERY_CAUSE_ID				= 24;
		public static final int QUERY_SUB_CAUSE_ID			= 25;
		public static final int QUERY_SUMMARY				= 26;
		public static final int QUERY_HOW_IDENTIFIED		= 27;
		public static final int QUERY_REQUIRED_ACTION		= 28;
		public static final int QUERY_DATE_IDENTIFED		= 29;
		public static final int QUERY_DATE_OCCURRED			= 30;
		public static final int QUERY_DATE_RESOLVED			= 31;
		public static final int TRANSACTION_REFERENCE		= 32;
		public static final int DICONDIN_REF 				= 33;
		public static final int DICONDIN_MATCHED 			= 34;
		public static final int NARRATIVE 					= 35;
		public static final int BANK_TRANS_TYPE 			= 36;
		public static final int TRANS_BATCH_REFERENCE		= 37;
		public static final int FUND_CODE					= 38;
		public static final int IAT_ID						= 39;
		public static final int DEST_FUND_CODE				= 40;
		public static final int SOURCE_BANK_ACCOUNT_NUMBER 	= 41;
		public static final int SOURCE_SORT_CODE			= 42;
		public static final int DEST_BANK_ACCOUNT_NUMBER 	= 43;
		public static final int DEST_SORT_CODE				= 44;	
		public static final int IS_AUTHORISED				= 45; //aurora sdu
		public static final int AUTHORISED_USER				= 46; //aurora sdu
		public static final int AUTHORISED_DATE				= 47; //aurora sdu
		public static final int REJECT_REASON				= 48; //aurora sdu
		public static final int ERROR_MESSAGE				= 49; //aurora sdu
		public static final int COMPANY_ID					= 50; //aurora sdu
		public static final int IS_EXECUTION_SUCCESS		= 51; //aurora sdu
		public static final int PARENT_INSTR_REF			= 52; //aurora sdu
		public static final int EXECUTION_DATE				= 53;
		public static final int LIABILITY					= 62;
		public static final int SDU_FIELD_GROUPS			= 70; //aurora sdu
	}
	
	public InstructionData
	 (int instructionDataId, String name, DataType dataType, 
	  String label, String description, FieldValueHandler fieldValueHandler) {
		this.instructionDataId = instructionDataId;
		this.name = name;
		this.dataType = dataType;
		
		this.setLabel(label);
		this.setDescription(description);
		
		this.setFieldValueHandler(fieldValueHandler);
	}

	public InstructionData get(String instructionDataName, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(instructionDataName, facade);
		return get(rs);
	}
	
	@SuppressWarnings("unchecked")
	public static InstructionData get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;

		// Check for a non-null FieldValueHandler class name
		String className = rs.getStringValueByName("INSTRUCTION_DATA_HANDLER_CLASS");
		FieldValueHandler fieldValueHandler = null;
		
		if (!StringUtils.stringIsBlank(className)) {
			try {
				// Attempt to load the Class from its name.
				Class<? extends FieldValueHandler> fieldValueHandlerClass =  
				 (Class<? extends FieldValueHandler>)
				 ClassLoaderUtils.getComponentClassLoader().loadClass(className);			

				fieldValueHandler = fieldValueHandlerClass.newInstance();
				
			} catch (Exception e) {
				String msg = "Failed to load Instruction Data handler class: " + 
				 className + ". Ensure the Java class is visible.";
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}
		}
		
		return new InstructionData(
			DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_DATA_ID"),
			DataResultSetUtils.getResultSetStringValue(rs, "INSTRUCTION_DATA_NAME"),
			DataType.getCache().getCachedInstance(
			 DataResultSetUtils.getResultSetStringValue(rs, "INSTRUCTION_DATA_TYPE")),
			 
			 rs.getStringValueByName("INSTRUCTION_DATA_LABEL"),
			 rs.getStringValueByName("INSTRUCTION_DATA_DESCRIPTION"),
			 fieldValueHandler
		);
	}
	
	public static Vector<InstructionData> getAll(FWFacade facade) throws DataException {
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllInstructionData", new DataBinder());
		
		Vector<InstructionData> instrData = new Vector<InstructionData>();
		
		if (rs.first()) {
			do {
				instrData.add(get(rs));
			} while (rs.next());
		}
		
		return instrData;
	}
	
	public static DataResultSet getData(String instructionDataName, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder
		 (binder, "INSTRUCTION_DATA_NAME", instructionDataName);
		
		DataResultSet rsComm = facade.createResultSet
		 ("qCore_GetRefInstructionData", binder);
		
		return rsComm;
	}
	
	/** Returns the mapped UCMMetadataTranslation instance for this Instruction Data
	 *  Field, if it has one.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public UCMMetadataTranslation getUCMMetadataTranslation() throws DataException {
		return UCMMetadataTranslation.getCache().getCachedInstance
		 (this.getInstructionDataId());
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}


	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
	}


	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}


	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public int getInstructionDataId() {
		return instructionDataId;
	}

	public void setInstructionDataId(int instructionDataId) {
		this.instructionDataId = instructionDataId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void seDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, InstructionData> getCache() {
		return CACHE;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setFieldValueHandler(FieldValueHandler fieldValueHandler) {
		this.fieldValueHandler = fieldValueHandler;
	}

	public FieldValueHandler getFieldValueHandler() {
		return fieldValueHandler;
	}
	
	public boolean equals(InstructionData instrData) {
		return (this.getInstructionDataId() == instrData.getInstructionDataId());
	}
	
	/** Determines whether or not the field value is dynamically calculated using a 
	 *  FieldValueHandler instance.
	 *  
	 * @return
	 */
	public boolean isDynamic() {
		return (fieldValueHandler != null);
	}

	/** InstructionStatus cache implementor */
	private static class Cache extends Cachable<Integer, InstructionData> {

		public Cache() {
			super("Instruction Data");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionData> instrDataFields = InstructionData.getAll(facade);
			
			HashMap<Integer, InstructionData> newCache = 
			 new HashMap<Integer, InstructionData>();
			
			for (InstructionData instrData : instrDataFields) {
				newCache.put(instrData.getInstructionDataId(), instrData);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
