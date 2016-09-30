package com.ecs.ucm.ccla.data.instruction;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.Auditable;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the INSTRUCTION_DATA_APPLIED table.
 *  
 *  Each instance contains an Instruction Data field value, which encapsulates the raw
 *  and typed values for the instruction field.
 *  
 *  Bear in mind this table doesn't have its own Primary Key.
 * 
 * @author Tom
 *
 */
public class InstructionDataApplied implements Persistable, Auditable {
	
	public static final String LABEL = "InstructionDataApplied";
	
	private int instructionId;
	
	public static class Cols {
		public static final String INSTRUCTION_VALUE = "INSTRUCTION_VALUE";
		
		public static final String INSTRUCTION_NUM_VALUE = "INSTRUCTION_NUM_VALUE";
		public static final String INSTRUCTION_DATE_VALUE = "INSTRUCTION_DATE_VALUE";
		public static final String INSTRUCTION_STRING_VALUE = "INSTRUCTION_STRING_VALUE";
	}
	
	/** Holds reference to the APPLICABLE_INSTRUCTION_DATA_ID and InstructionData
	 *  instance.
	 */
	private ApplicableInstructionData applicableInstructionData;
	
	/** Stores the raw and typed value for this applied data field.
	 */
	private InstructionDataFieldValue dataFieldValue;
	
	public InstructionDataApplied(int instructionId,
	 ApplicableInstructionData applInstrData, 
	 InstructionDataFieldValue dataFieldValue) {
		
		this.instructionId = instructionId;
		this.applicableInstructionData = applInstrData;
		
		this.dataFieldValue = dataFieldValue;
	}

	public static InstructionDataApplied get
	 (int instructionId, int applicableInstructionDataId,  FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getData(instructionId, applicableInstructionDataId, facade);
		return get(rs);
	}
	
	public static InstructionDataApplied get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		
		ApplicableInstructionData applInstrData = 
		 ApplicableInstructionData.getCache().getCachedInstance(
		 DataResultSetUtils.getResultSetIntegerValue
		 (rs, "APPLICABLE_INSTRUCTION_DATA_ID"));

		return new InstructionDataApplied(
			DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_ID"),
			applInstrData,
			InstructionDataFieldValue.get
			 (rs, applInstrData.getInstructionData().getDataType())
		);
	}

	public static DataResultSet getData
	 (int instructionId, int applicableInstructionDataId,  FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, "INSTRUCTION_ID", instructionId);
		BinderUtils.addIntParamToBinder(binder, "APPLICABLE_INSTRUCTION_DATA_ID", 
		 applicableInstructionDataId);
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetInstructionDataAppliedById", binder);
		
		return rs;
	}
	
	/** Updates the InstructionDataFieldValue variable, taking the Instruction Data
	 *  data type into consideration.
	 *  
	 * @param value
	 * @throws ParseException 
	 * @throws DataException 
	 */
	public void setValue(String value) throws DataException {
		
		try {
			this.getDataFieldValue().setValue(value);
		} catch (Exception e) {
			throw new DataException("Unable to set value for Instruction Data field " +
			 this.getApplicableInstructionData().getInstructionData().getName() +
			 ": " + e.getMessage(), e);
		}
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_ID", this.getInstructionId());
		
		BinderUtils.addIntParamToBinder(binder, "APPLICABLE_INSTRUCTION_DATA_ID", 
		 this.getApplicableInstructionData().getApplicableInstructionDataId());
		
		this.getDataFieldValue().addFieldsToBinder(binder);
	}

	public void persist(FWFacade facade, String username) throws DataException {
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet rsBeforeData = facade.createResultSet
		 ("qCore_GetInstructionDataApplied", binder);
		
		facade.execute("qCore_UpdateInstructionDataApplied", binder);
		
		DataResultSet rsAfterData = facade.createResultSet
		 ("qCore_GetInstructionDataApplied", binder);
		
		// Auditing
		
		// Audit against the Instruction ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(
		 this.getInstructionId(), 
		 ElementType.SecondaryElementType.Instruction.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, 
		 username, Globals.AuditActions.UPDATE.toString(), LABEL, 
		 rsBeforeData, rsAfterData, auditRelations);
		
		//Log.debug("Updated InstructionDataApplied: " + this.toString());
	}
	
	public static void add(InstructionDataApplied instrDataApplied,
	 FWFacade facade, String userName) throws DataException {
		DataBinder binder = new DataBinder();
		instrDataApplied.validate(facade);
		
		instrDataApplied.addFieldsToBinder(binder);
		facade.execute("qCore_CreateInstructionDataApplied", binder);
		
		//Log.debug("Added new InstructionDataApplied: " + instrDataApplied.toString());
		
		// Auditing
		DataResultSet rsNewData = getData(
		 instrDataApplied.getInstructionId(),
		 instrDataApplied.getApplicableInstructionData().
		  getApplicableInstructionDataId(), facade);
		
		// Audit against the Instruction ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(
		 instrDataApplied.getInstructionId(), 
		 ElementType.SecondaryElementType.Instruction.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, 
		 userName, Globals.AuditActions.ADD.toString(), LABEL, 
		 null, rsNewData, auditRelations);
	}

	/** Removes the given applied data instance from the DB.
	 * 
	 * @param dataApplied
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	public static void remove(InstructionDataApplied dataApplied, 
	 FWFacade facade, String userName) throws DataException {
		DataBinder binder = new DataBinder();
		
		// Auditing
		DataResultSet rsOldData = getData(
		 dataApplied.getInstructionId(),
		 dataApplied.getApplicableInstructionData().
		  getApplicableInstructionDataId(), facade);
		
		dataApplied.addFieldsToBinder(binder);
		facade.execute("qCore_RemoveInstructionDataApplied", binder);
		
		// Audit against the Instruction ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(
		 dataApplied.getInstructionId(), 
		 ElementType.SecondaryElementType.Instruction.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, 
		 userName, Globals.AuditActions.DELETE.toString(), LABEL, 
		 rsOldData, null, auditRelations);
	}
	
	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {

		if (this.getApplicableInstructionData() == null)
			throw new DataException("Applicable Instruction Data ID missing");
		
		// Ensure an incorrect data type wasn't set.
		// TODO
	}

	public int getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(int instructionId) {
		this.instructionId = instructionId;
	}
	
	public InstructionDataFieldValue getDataFieldValue() {
		return dataFieldValue;
	}

	public void setDataFieldValue(InstructionDataFieldValue dataFieldValue) {
		this.dataFieldValue = dataFieldValue;
	}

	public ApplicableInstructionData getApplicableInstructionData() {
		return applicableInstructionData;
	}

	public void setApplicableInstructionData(
	 ApplicableInstructionData applicableInstructionData) {
		this.applicableInstructionData = applicableInstructionData;
	}
	
	/** Adds/updates a single Applied Instruction Data instance.
	 * 
	 * @param dataApplied
	 * @throws DataException 
	 */
	public static void addOrUpdate(InstructionDataApplied dataApplied, 
	 FWFacade facade, String userName) throws DataException {
		
		DataBinder binder = new DataBinder();
		dataApplied.addFieldsToBinder(binder);
		
		// Check if this data field was already applied to this Instruction
		InstructionDataApplied currentDataApplied = get(facade.createResultSet
		 ("qCore_GetInstructionDataApplied", binder));
		
		if (currentDataApplied == null) {
			// Applied Instruction Data field not yet in DB. Add new.
			add(dataApplied, facade, userName);
		} else if (currentDataApplied.equals(dataApplied)) {
			// Data value has changed, apply update
			if (!currentDataApplied.getDataFieldValue().equals
					(dataApplied.getDataFieldValue())) {
					// Value was changed.
					//Log.debug("Updating existing data value");
					dataApplied.persist(facade, userName);
			}	
		}
	}
	
	/** Adds/updates a single Applied Instruction Data instance. Does not require an
	 *  instantiated InstructionDataApplied instance - this will be generated within
	 *  the method. 
	 *  
	 *  If the given InstructionData field isn't applicable for the given
	 *  Instruction's type, returns false, otherwise returns true.
	 * 
	 * @param dataApplied
	 * @throws DataException 
	 */
	public static boolean addOrUpdate(Instruction instruction, 
	 InstructionData instructionData, Object value,
	 FWFacade facade, String userName) throws DataException {
		
		InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
		 (instructionData.getDataType(), value);
		
		return addOrUpdate(instruction, instructionData, fieldValue, facade, userName);
	}
	
	/** Adds/updates a single Applied Instruction Data instance. Does not require an
	 *  instantiated InstructionDataApplied instance - this will be generated within
	 *  the method. 
	 *  
	 *  If the given InstructionData field isn't applicable for the given
	 *  Instruction's type, returns false, otherwise returns true.
	 * 
	 * @param dataApplied
	 * @throws DataException 
	 */
	public static boolean addOrUpdate(Instruction instruction, 
	 InstructionData instructionData, InstructionDataFieldValue value,
	 FWFacade facade, String userName) throws DataException {
		
		// First obtain the ApplicableInstructionData for this instruction type/data
		// field (may not exist)
		Vector<ApplicableInstructionData> applInstrDatas = 
		 ApplicableInstructionData.getInstructionTypeCache().getCachedInstance
		 (instruction.getType().getInstructionTypeId());
		
		ApplicableInstructionData reqApplInstrData = null;
		
		for (ApplicableInstructionData applInstrData : applInstrDatas) {
			if (applInstrData.getInstructionData().equals(instructionData)) {
				reqApplInstrData = applInstrData;
				break;
			}
		}
		
		if (reqApplInstrData == null) {
			// The given data field wasn't applicable for the instruction type, do
			// nothing.
			return false;
		}
		
		InstructionDataApplied instrDataApplied = new InstructionDataApplied
		 (instruction.getInstructionId(), reqApplInstrData, value);
		
		addOrUpdate(instrDataApplied, facade, userName);
		return true;
	}
	
	/** Adds/updates Applied Instruction Data entries in the DB.
	 * 

	 *  TODO: drop unused data fields
	 * 
	 * @param dataFields
	 * @throws DataException 
	 */
	public static void addOrUpdateAll
	 (Vector<InstructionDataApplied> dataFields, FWFacade facade, String userName) 
	 throws DataException {
		
		Vector<InstructionDataApplied> currentDataFields = null;
		
		if (dataFields.size() > 0) {
			currentDataFields = Instruction.getInstructionDataApplied
			 (dataFields.get(0).getInstructionId(), facade);
		}
		
		addOrUpdateAll(dataFields, currentDataFields, facade, userName);
	}
	
	/** Adds/updates Applied Instruction Data entries in the DB.
	 *  
	 *  Drops any 'invalid' applied data fields, i.e. any which are mapped to different
	 *  Instruction Types. If an Instruction Type is updated, all existing applied
	 *  data values will be dropped.
	 *  
	 * @param dataFields
	 * @throws DataException 
	 */
	public static void addOrUpdateAll
	 (Vector<InstructionDataApplied> dataFields, 
	 Vector<InstructionDataApplied> currentDataFields, 
	 FWFacade facade, String userName) throws DataException {
		
		// Generate a linked list to store references to existing data values for this
		// instruction. This allows them to be quickly removed as they are matched
		// against newly-calculated data values, and determine which ones need to be
		// deleted at the end.
		LinkedList<InstructionDataApplied> existingDataFields = 
		 new LinkedList<InstructionDataApplied>();
		
		if (currentDataFields != null)
			existingDataFields.addAll(currentDataFields);
		
		// Fetch the Instruction Type from the first new data field, if one exists.
		InstructionType instructionType = null;
		
		if (currentDataFields != null && !currentDataFields.isEmpty()) {
			instructionType = dataFields.get(0).getInstructionType();
		}
			
		// Remove invalid mappings relating to old Instruction Type
		for (int i=0; i<existingDataFields.size(); i++) {
			InstructionDataApplied existingDataField = existingDataFields.get(i);
			if (instructionType == null
				||
				!existingDataField.getInstructionType().equals(instructionType)) {
				
				Log.debug("Removing invalid data value: " + 
				 existingDataField.getInstructionData().getName());
				
				 InstructionDataApplied.remove(existingDataField, facade, userName);
				 existingDataFields.remove(i); i--;
			}
		}
		
		// Step through new data values, updating or adding as neccessary
		for (InstructionDataApplied dataField : dataFields) {
			boolean foundExisting = false;
			
			for (int i=0; i<existingDataFields.size(); i++) {
				InstructionDataApplied existingDataField = existingDataFields.get(i);
				
				if (dataField.equals(existingDataField)) {
					// Found an existing applied value for this data field. Update 
					// it only if the value has been changed

					if (!existingDataField.getDataFieldValue().equals
						(dataField.getDataFieldValue())) {
						// Value was changed.
						//Log.debug("Updating existing data value");
						dataField.persist(facade, userName);
					}
					
					foundExisting = true;
					
					existingDataFields.remove(i); // remove existing entry
					break;
				}
			}
					
			if (!foundExisting) {
				// No existing applied data value, add one now
				add(dataField, facade, userName);
				
				//Log.debug("Added new data value: " 
				// + dataField.getInstructionData().getName());	
			}
		}
		
		// Clear up unused data mappings
		for (InstructionDataApplied dataField : existingDataFields) {
			//Log.debug("Removing unused data value: " + 
			// dataField.getInstructionData().getName());
			InstructionDataApplied.remove(dataField, facade, userName);
		}
	}
	
	/**
	 *  @deprecated this is dangerous, remove any existing references. This will only 
	 *  return a raw data value which may not always match to what you need. Use
	 *  getDataApplied instead. 
	 *  <br/>
	 *  Gets the raw value of an Instruction Data field for the Instruction ID, or null 
	 *  if no such data value exists
	 * 
	 * @return String
     * @throws DataException
	 */	
	public static String getDataValueByName(int instructionId, String dataName, 
	 FWFacade facade) throws DataException {
		
		Vector<InstructionDataApplied> instrDataApplied = 
		 Instruction.getInstructionDataApplied(instructionId, facade);
		
		// Search for a matching Applied Instruction Data field
		for (InstructionDataApplied instrData : instrDataApplied) {
			if (instrData.getApplicableInstructionData().
				getInstructionData().getName().equals(dataName)) {
				return instrData.getDataFieldValue().getRawValue();
			}
		}
		
		return null;
	}
	
	/**
	 *  Gets the InstructionDataApplied value of an Instruction Data field for the given
	 *  Instruction ID and Instruction Data Id, or null if no such applied data value 
	 *  exists
	 *  
	 * @return String
     * @throws DataException
	 */	
	public static InstructionDataApplied getDataApplied
	 (int instructionId, int instructionDataId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.INSTRUCTION, instructionId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, InstructionData.Cols.ID, instructionDataId);
		
		return get(facade.createResultSet
		 ("qCore_GetInstructionDataAppliedByInstructionAndInstructionData", binder));
	}
	
	/**
	 *  Updates the value of an Instruction Data field for the given Instruction ID.
	 *  
	 *  If no data value exists then it will add one.
	 * 
	 * @return void
	 * @throws DataException 
	 * @throws ServiceException 
	 */		
	public static void updateDataValueByName(int instructionId, String dataName, 
	 String dataValue, String username, FWFacade facade) 
	 throws DataException, ServiceException {
		
		Instruction instruction = Instruction.get(instructionId, facade);
		InstructionType instrType = instruction.getType();
		
		Vector<ApplicableInstructionData> instrData = 
		 instrType.getApplicableInstructionData();
		
		ApplicableInstructionData applInstrData = null;
		
		// Search for a matching Applicable Instruction Data field for this Instruction
		// Type
		for (ApplicableInstructionData thisApplInstrData : instrData) {
			if (thisApplInstrData.getInstructionData().getName().equals(dataName)) {
				applInstrData = thisApplInstrData;
				break;
			}
		}
		
		if (applInstrData == null) {
			// The passed Data Field isn't applicable for this Instruction Type.
			
			String msg = "Cannot find applicable instruction data with name " + 
			 dataName + " for instruction " + instructionId;
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		// Check if this Instruction Data already has an applied value for this
		// Instruction.
		InstructionDataApplied dataApplied = InstructionDataApplied.get
		 (instructionId, applInstrData.getApplicableInstructionDataId(), facade);
		
		if (dataApplied == null) {
			// Create new InstructionDataApplied instance.
			dataApplied = new InstructionDataApplied
			 (instructionId, applInstrData, new InstructionDataFieldValue
			  (applInstrData.getInstructionData().getDataType()));
			
			dataApplied.setValue(dataValue);
			add(dataApplied, facade, username);
			
		} else {
			// Update existing InstructionDataApplied instance and persist to DB
			// need to update value
			dataApplied.setValue(dataValue);
			dataApplied.persist(facade, username);
		}
	}

	/**
	 *  Updates the value of an Instruction Data field for the given Instruction ID.
	 *  
	 *  If no data value exists then it will add one.
	 * 
	 * @return void
	 * @throws DataException 
	 * @throws ServiceException 
	 */		
	public static void updateDataValueById(int instructionId, int applicableInstructionDataId, 
	 String dataValue, String username, FWFacade facade) 
	 throws DataException, ServiceException {
		
		Instruction instruction = Instruction.get(instructionId, facade);
		InstructionType instrType = instruction.getType();
		
		Vector<ApplicableInstructionData> instrData = 
		 instrType.getApplicableInstructionData();
		
		ApplicableInstructionData applInstrData = null;
		
		// Search for a matching Applicable Instruction Data field for this Instruction
		// Type
		for (ApplicableInstructionData thisApplInstrData : instrData) {
			if (thisApplInstrData.getInstructionData().getInstructionDataId() 
				== applicableInstructionDataId) {
				applInstrData = thisApplInstrData;
				break;
			}
		}
		
		if (applInstrData == null) {
			// The passed Data Field isn't applicable for this Instruction Type.
			
			String msg = "Cannot find applicable instruction data with id " + 
			applicableInstructionDataId + " for instruction " + instructionId;
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		// Check if this Instruction Data already has an applied value for this
		// Instruction.
		InstructionDataApplied dataApplied = InstructionDataApplied.get
		 (instructionId, applInstrData.getApplicableInstructionDataId(), facade);
		
		if (dataApplied == null) {
			// Create new InstructionDataApplied instance.
			dataApplied = new InstructionDataApplied
			 (instructionId, applInstrData, new InstructionDataFieldValue
			  (applInstrData.getInstructionData().getDataType()));
			
			dataApplied.setValue(dataValue);
			add(dataApplied, facade, username);
			
		} else {
			// Update existing InstructionDataApplied instance and persist to DB
			// need to update value
			dataApplied.setValue(dataValue);
			dataApplied.persist(facade, username);
		}
	}	
	
	
	public String getAuditLabel() {
		return LABEL;
	}

	// Delegate methods
	
	public InstructionData getInstructionData() {
		return applicableInstructionData.getInstructionData();
	}

	public InstructionType getInstructionType() {
		return applicableInstructionData.getInstructionType();
	}
	
	/** Returns a matching InstructionDataFieldValue instance from the passed set
	 *  of InstructionDataApplied instances.
	 *  
	 * @return null if the data field isn't present.
	 * @param instrDataApplied
	 * @param instructionDataId
	 */
	public static InstructionDataFieldValue getFieldValue
	 (Vector<InstructionDataApplied> instrDataApplied, int instructionDataId) {
		
		for (InstructionDataApplied dataField : instrDataApplied) {
			if (dataField.getInstructionData().getInstructionDataId() 
				== 
				instructionDataId) {
				return dataField.getDataFieldValue();
			}
		}
		
		return null; // no matched data field
	}
	
	/** Returns a matching InstructionDataFieldValue instance from the passed set
	 *  of InstructionDataApplied instances.
	 *  
	 * @return null if the data field isn't present.
	 * @param instrDataApplied
	 * @param instructionDataId
	 */
	public static InstructionDataApplied getDataAppled
	 (Vector<InstructionDataApplied> instrDataApplied, int instructionDataId) {
		
		for (InstructionDataApplied dataField : instrDataApplied) {
			if (dataField.getInstructionData().getInstructionDataId() 
				== instructionDataId) {
				return dataField;
			}
		}
		
		return null; // no matched data field
	}
	
	
	@Override
	public String toString() {
		if (getInstructionData()!=null && getDataFieldValue()!=null) {
		return "InstructionDataApplied [" +
				"InstructionId: "+this.getInstructionId()+
				", InstructionType: "+this.getInstructionType().getName()+
				", DataName: "+this.getInstructionData().getName()+
				", Values: "+getDataFieldValue().toString()+
				"]";
		}
		return "";
	}
	
	/** Returns true if the passed instance has the same Instruction ID and Applicable 
	 *  Instruction Data reference. Does not compare the actual data values.
	 *  
	 *  For value comparisons, use this.getDataFieldValue().equals(...)
	 *  
	 * @param dataApplied
	 * @return
	 */
	public boolean equals(InstructionDataApplied dataApplied) {
		return 
		 (this.getInstructionId() == dataApplied.getInstructionId()
		 &&
		 this.getApplicableInstructionData().equals
		  (dataApplied.getApplicableInstructionData()));
	}
	
	/** Constructs an InstructionDataApplied instance for the given Instruction and
	 *  Applicable Data field, sourcing the value from the passed DataBinder.
	 *  
	 *  Fails if the field is mandatory for the given Instruction's type and no value
	 *  is present in the binder.
	 *  
	 * @param instr
	 * @param applicableData
	 * @param binder
	 * @return
	 * @throws DataException
	 */
	public static InstructionDataApplied getDataAppliedFromBinder
	 (Instruction instr, ApplicableInstructionData applicableData, DataBinder binder) 
	 throws DataException {
		
		InstructionData instrData = applicableData.getInstructionData();
		
		InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
		 (instrData.getDataType());
		
		if (instrData.getDataType().equals(DataType.FLOAT)) {
			BigDecimal fValue = CCLAUtils.getBinderBigDecimalValue
			 (binder, instrData.getName());
			fieldValue.setBigDecimalValue(fValue);
		} else if (instrData.getDataType().equals(DataType.INT)) {
			Integer iValue = CCLAUtils.getBinderIntegerValue
			 (binder, instrData.getName());
			fieldValue.setIntValue(iValue);
		} else if (instrData.getDataType().equals(DataType.BOOL)) {
			boolean bValue = CCLAUtils.getBinderBoolValue
			 (binder, instrData.getName());
			fieldValue.setBoolValue(bValue);
		} else if (instrData.getDataType().equals(DataType.DATE)) {
			Date dValue = CCLAUtils.getBinderDateValue
			 (binder, instrData.getName());
			fieldValue.setDateValue(dValue);				
		} else if (instrData.getDataType().equals(DataType.STRING)) {
			String sValue = binder.getLocal(instrData.getName());
			fieldValue.setStringValue(sValue);
		} else {
			//highly unlikely.
			throw new DataException("Unknown datatype.");
		}
		
		if (!applicableData.isOptional() && fieldValue.isEmpty()) {
			String msg = "Missing mandatory value for Instruction Data Field: "
			 +instrData.getName();
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		InstructionDataApplied dataApplied = new InstructionDataApplied
		 (instr.getInstructionId(), applicableData, fieldValue);
		
		return dataApplied;
	}
}
