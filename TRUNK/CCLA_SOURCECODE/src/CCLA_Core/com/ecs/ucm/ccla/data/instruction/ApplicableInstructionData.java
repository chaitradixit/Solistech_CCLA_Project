package com.ecs.ucm.ccla.data.instruction;

import java.util.HashMap;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the APPLICABLE_INSTRUCTION_DATA table.
 *  
 *  Each entry is a mapping between an Instruction Type and an Instruction Field, i.e.
 *  which fields apply to each instruction type.
 * 
 * @author Tom
 *
 */
public class ApplicableInstructionData implements Persistable {
	
	private int applicableInstructionDataId;
	
	private InstructionData instructionData;
	private InstructionType instructionType;
	
	private boolean optional;

	public ApplicableInstructionData(int applicableInstructionData, 
	 InstructionData instrData, InstructionType instrType, boolean isOptional) {
		this.applicableInstructionDataId = applicableInstructionData;
		
		this.setInstructionData(instrData);
		this.setInstructionType(instrType);
		
		this.optional = isOptional;
	}

	public static ApplicableInstructionData get
	 (int applicableInstructionData, FWFacade facade) throws DataException {
		DataResultSet rs = getData(applicableInstructionData, facade);
		return get(rs);
	}
	
	public static ApplicableInstructionData get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		
		return new ApplicableInstructionData(
			DataResultSetUtils.getResultSetIntegerValue
			 (rs, "APPLICABLE_INSTRUCTION_DATA_ID"),
			
			InstructionData.getCache().getCachedInstance(
			 DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_DATA_ID")
			),
			
			InstructionType.getIdCache().getCachedInstance(
			 DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_TYPE_ID")
			),
			
			DataResultSetUtils.getResultSetBoolValue(rs, "IS_OPTIONAL")
		);
	}
	
	/** Fetches all ApplicableInstructionData instances from the database.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<ApplicableInstructionData> getAll(FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllApplicableInstructionData", new DataBinder());
		
		Vector<ApplicableInstructionData> applInstrData = 
		 new Vector<ApplicableInstructionData>();
		
		if (rs.first()) {
			do {
				applInstrData.add(get(rs));
			} while (rs.next());
		}
		
		return applInstrData;
	}
	
	public static DataResultSet getData(int applicableInstructionData, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, 
		 "APPLICABLE_INSTRUCTION_DATA_ID", applicableInstructionData);
		
		DataResultSet rsComm = facade.createResultSet("?", binder);
		return rsComm;
	}
	
	/** Returns a ResultSet of Applicable Instruction Data entries for the given 
	 *  Instruction Type.
	 *  
	 *  The query is joined to the REF_INSTRUCTION_DATA table, so it contains field
	 *  names, types etc.
	 *  
	 * @param applicableInstructionData
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getDataByInstructionType
	 (InstructionType type, boolean extendedData, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, 
		 "INSTRUCTION_TYPE_ID", type.getInstructionTypeId());
		
		String queryName = null;
		
		if (extendedData)
			queryName = "qCore_GetApplicableInstructionDataByTypeExt";
		else
			queryName = "qCore_GetApplicableInstructionDataByType";
		
		DataResultSet rs = facade.createResultSet(queryName, binder);
		return rs;
	}
	
	/** Gets the applicable instruction data for a particular instruction
	 * (not values)
	 *
	 * @deprecated 	use getDataByInstructionType or 
	 * 				instructionType.getApplicableInstructionData() instead.
	 *
	 * @return Vector<ApplicableInstructionData>()
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 */	
	public static Vector<ApplicableInstructionData> getApplicableInstructionData
	 (int instructionId, FWFacade facade) throws DataException, ServiceException {
		
		
		return null;
		
		/*
		Vector<ApplicableInstructionData> vector = 
		 new Vector<ApplicableInstructionData>();
		
		Instruction instruction = Instruction.get(instructionId, facade);
		
		int instructionTypeId = instruction.getInstructionType();
		DataBinder binder = new DataBinder();
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetApplicableInstructionDataByType", binder);
		
		if (rs.isEmpty())
			throw new ServiceException("No applicable instruction data found for" +
			 " instruction id:" + instructionId);
		else {
			do {
				ApplicableInstructionData aid = ApplicableInstructionData.get(rs);
				vector.add(aid);
			} while (rs.next());
			return vector;
		}
		*/
	}
	/**
	 * gets applicable instruction data for given instruction data id and data name
	 * 
	 * @return ApplicableInstructionData or null
	 * @throws DataException
	 **/
	public static ApplicableInstructionData getApplicableInstructionDataByName
	 (int instructionTypeId, String dataName, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_TYPE_ID", instructionTypeId);
		BinderUtils.addParamToBinder
		 (binder, "INSTRUCTION_DATA_NAME", dataName);
		
		DataResultSet rsAID = facade.createResultSet
		 ("qCore_GetApplicableInstructionDatabyName", binder);
		
		if (rsAID.isEmpty()) {
			String msg = "Data column" + dataName + " not allowed for instruction type " 
			 + instructionTypeId;
			
			Log.error(msg);
			return null;
		} else {
			ApplicableInstructionData aid = ApplicableInstructionData.get(rsAID);
			return aid;
		}
	}

	
	/**
	 * gets applicable instruction data for given instruction data id and data id
	 * 
	 * @return ApplicableInstructionData or null
	 * @throws DataException
	 **/
	public static ApplicableInstructionData getApplicableInstructionDataById
	 (int instructionTypeId, int dataId) throws DataException {
		
		Vector<ApplicableInstructionData> applInstrDataFields =
		 ApplicableInstructionData.getInstructionTypeCache().getCachedInstance
		 (instructionTypeId);
		
		for (ApplicableInstructionData applInstrData : applInstrDataFields) {
			if (applInstrData.getInstructionData().getInstructionDataId() == dataId)
				return applInstrData;
		}
		
		String msg = "Data column" + dataId + " not allowed for instruction type " 
		 + instructionTypeId;
		Log.error(msg);
		
		return null;
		
		/*
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_TYPE_ID", instructionTypeId);
		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_DATA_ID", dataId);
		
		DataResultSet rsAID = facade.createResultSet
		 ("qCore_GetApplicableInstructionDatabyId", binder);
		
		if (rsAID.isEmpty()) {
			String msg = "Data column" + dataId + " not allowed for instruction type " 
			 + instructionTypeId;
			Log.error(msg);
			return null;
		} else {
			ApplicableInstructionData aid = ApplicableInstructionData.get(rsAID);
			return aid;
		}
		*/
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

	public int getApplicableInstructionDataId() {
		return applicableInstructionDataId;
	}

	public void setApplicableInstructionDataId(int applicableInstructionData) {
		this.applicableInstructionDataId = applicableInstructionData;
	}
	
	public void setInstructionData(InstructionData instructionData) {
		this.instructionData = instructionData;
	}

	public InstructionData getInstructionData() {
		return instructionData;
	}

	public void setInstructionType(InstructionType instructionType) {
		this.instructionType = instructionType;
	}

	public InstructionType getInstructionType() {
		return instructionType;
	}
	
	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean isOptional) {
		this.optional = isOptional;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, ApplicableInstructionData> getCache() {
		return CACHE;
	}
	
	private static InstructionTypeCache INSTRUCTION_TYPE_CACHE = 
	 new InstructionTypeCache();
	
	public static Cachable<Integer, Vector<ApplicableInstructionData>> 
	 getInstructionTypeCache() {
		return INSTRUCTION_TYPE_CACHE;
	}

	/** ApplicableInstructionData cache implementor.
	 * 
	 *  Maps Applicable Instruction Data IDs against corresponding 
	 *  ApplicableInstructionData instances.
	 *  
	 **/
	private static class Cache 
	 extends Cachable<Integer, ApplicableInstructionData> {
		
		public Cache() {
			super("Applicable Instruction Data");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<ApplicableInstructionData> applInstrFields  = 
			 ApplicableInstructionData.getAll(facade);
			
			HashMap<Integer, ApplicableInstructionData> newCache = 
			 new HashMap<Integer, ApplicableInstructionData>();
			
			for (ApplicableInstructionData applInstrData : applInstrFields) {
				newCache.put
				 (applInstrData.getApplicableInstructionDataId(), applInstrData);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	
	/** InstructionType -> ApplicableInstructionData cache implementor.
	 * 
	 *  Maps Instruction Type IDs against sets of ApplicableInstructionData instances.
	 *  
	 **/
	private static class InstructionTypeCache 
	 extends Cachable<Integer, Vector<ApplicableInstructionData>> {
		
		public InstructionTypeCache() {
			super("Instruction Type -> Applicable Instruction Data");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<ApplicableInstructionData> applInstrFields  = 
			 ApplicableInstructionData.getAll(facade);
			
			HashMap<Integer, Vector<ApplicableInstructionData>> newCache = 
			 new HashMap<Integer, Vector<ApplicableInstructionData>>();
			
			for (ApplicableInstructionData applInstrData : applInstrFields) {
				
				int instrTypeId = 
				 applInstrData.getInstructionType().getInstructionTypeId();
				
				if (newCache.containsKey(instrTypeId)) {
					Vector<ApplicableInstructionData> applInstrFieldSet =
					 newCache.get(instrTypeId);
					
					applInstrFieldSet.add(applInstrData);
				} else {
					Vector<ApplicableInstructionData> applInstrFieldSet = 
					 new Vector<ApplicableInstructionData>();
					
					newCache.put(instrTypeId, applInstrFieldSet);
					applInstrFieldSet.add(applInstrData);
				}
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	
	public boolean equals(ApplicableInstructionData applInstrData) {
		return (this.getApplicableInstructionDataId() 
				== 
				applInstrData.getApplicableInstructionDataId());
	}
}
