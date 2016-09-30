package com.ecs.ucm.ccla.data.comm;

import java.util.HashMap;
import java.util.Vector;

import intradoc.common.Log;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the COMM_GROUP table.
 * 
 * @author Tom
 *
 */
public class CommGroup implements Persistable {

	private int commGroupId;
	
	/** UCM/Iris Document Bundle Reference.
	 *  
	 *  Will only be set if the Comm Group corresponds to a document bundle in Iris.
	 *  
	 *  Stored in the xBundleRef field in the UCM metadata model.
	 */
	private String ucmBatchRef;
	
	/** Batch Number used by UCM/SPP to identify a single batch of triggered
	 *  jobs. Does not uniquely identify a single triggered job.
	 *  
	 *  This is set when one of the instructions belonging to this Comm Group 
	 *  triggers an SPP job, and it hasn't been set already.
	 *
	 *  Stored in the xBatchNumber field in the UCM metadata model.
	 */
	private Integer sppBatchRef;

	public static final class Cols {
		public static final String ID = "COMM_GROUP_ID";
		public static final String UCM_BATCH_REF = "UCM_BATCH_REF";
		public static final String SPP_BATCH_REF = "SPP_BATCH_REF";
	}
	
	/** Creates and returns a new CommGroup instance. 
	 * 
	 *  The passed UCM Bundle Ref can be null.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static CommGroup add(String bundleRef, Integer sppBatchId, 
	 FWFacade facade, String userName) throws DataException {
		
		int newCommGroupId = Integer.parseInt
		 (CCLAUtils.getNewKey("CommGroup", facade));
		
		CommGroup commGroup = new CommGroup(newCommGroupId, bundleRef, sppBatchId);
		DataBinder binder = new DataBinder();
		
		commGroup.addFieldsToBinder(binder);
		
		facade.execute("qCore_AddCommGroup", binder);
		
		
		// Add audit record
		DataResultSet newData = CommGroup.getData(newCommGroupId, facade);
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(newCommGroupId, 
		 ElementType.SecondaryElementType.CommGroup.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.CommGroup.toString(), 
		 null, newData, auditRelations);
		
		return new CommGroup(newCommGroupId);
	}
	

	public static CommGroup get(Integer groupId, FWFacade facade) throws DataException {
		return get(getData(groupId,facade));		
	}
	
	public static CommGroup getByUCMBatchRef(String batchRef, FWFacade facade)
	 throws DataException {
		return get(getDataByUCMBatchRef(batchRef, facade));
	}

	public static DataResultSet getData(Integer groupId, FWFacade facade) 
	 throws DataException{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, groupId);
		
		return facade.createResultSet("qCore_GetCommGroup", binder);
	}
	
	public static DataResultSet getDataByUCMBatchRef(String bundleRef, FWFacade facade) 
	 throws DataException{
		DataBinder binder = new DataBinder();
		binder.putLocal(Cols.UCM_BATCH_REF, bundleRef);
		
		return facade.createResultSet("qCore_GetCommGroupByUCMBatchRef", binder);
	}
	
	private static CommGroup get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new CommGroup(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getStringValueByName(Cols.UCM_BATCH_REF),
			CCLAUtils.getResultSetIntegerValue(rs,Cols.SPP_BATCH_REF)
		);
	}
	
	public CommGroup(int commGroupId, String ucmBundleRef, Integer sppBatchId) {
		this.commGroupId = commGroupId;
		this.setUcmBatchRef(ucmBundleRef);
		this.setSppBatchRef(sppBatchId);
	}
	
	public static Vector<Comm> getComms(int commGroupId, FWFacade facade) 
	 throws DataException {
		Vector<Comm> comms = new Vector<Comm>();
		
		DataResultSet rs = getCommsData(commGroupId, facade);
		
		if (!rs.isEmpty()) {
			do {
				comms.add(Comm.get(rs));
			} while (rs.next());
		}
		
		return comms;
	}
	
	/**
	 * Method to obtain all Comms data in a given Comm Group.
	 * 
	 * @param commGroupId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getCommsData
	 (int commGroupId, FWFacade facade) throws DataException {

		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, commGroupId);
		
		return facade.createResultSet("qCore_GetCommsInGroup", binder);
	}
	
	/**
	 * Method to obtain all Instructions data in a given Comm Group.
	 * 
	 * @param commGroupId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getInstructionsData
	 (int commGroupId, FWFacade facade) throws DataException {

		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, commGroupId);
		
		return facade.createResultSet("qCore_GetInstructionsInCommGroup", binder);
	}
	
	/**
	 * Returns a vector of instruction objects from a given commGroupId
	 * @param commGroupId
	 * @param facade
	 * @return
	 * @throws ServiceException
	 */
	public static Vector<Instruction> getInstructions(int commGroupId, FWFacade facade) 
	 throws ServiceException {
		
		Vector <Instruction> instrs = new Vector<Instruction>();
		
		try {
			DataResultSet rsInstructions = getInstructionsData(commGroupId, facade);	
			if(!rsInstructions.isEmpty()){
				do{
					Instruction instr = Instruction.get(rsInstructions);
					instrs.add(instr);
				}while (rsInstructions.next());
			}
			
		} catch (Exception e) {
			Log.error("Unable to getInstructions: "+e.getMessage());
			throw new ServiceException("Unable to getInstructions: "+e.getMessage(),e);
		}
		
		return instrs;
	}
	
	/** Creates a temporary CommGroup with an ID of zero. Used for testing creation
	 *  of comm groups without changing the database.
	 */
	public CommGroup() {
		this.commGroupId = 0;
	}
	
	public CommGroup(int commGroupId) {
		this.commGroupId = commGroupId;
	}

	public void setCommGroupId(int commGroupId) {
		this.commGroupId = commGroupId;
	}

	public int getCommGroupId() {
		return commGroupId;
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {

		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getCommGroupId());
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.UCM_BATCH_REF, this.getUcmBatchRef());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.SPP_BATCH_REF, this.getSppBatchRef());
	}

	public void persist(FWFacade facade, String username) throws DataException {

		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = CommGroup.getData(this.getCommGroupId(), facade);
		
		facade.execute("qCore_UpdateCommGroup", binder);
		
		DataResultSet newData = CommGroup.getData(this.getCommGroupId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getCommGroupId(), 
		 ElementType.SecondaryElementType.CommGroup.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.CommGroup.toString(), 
		 beforeData, newData, auditRelations);
		
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public String toString() {
		return ("[ID: " + this.getCommGroupId() + ", UCM Bundle Ref: " + 
		 this.getUcmBatchRef() + ", SPP Batch Ref: " + this.getSppBatchRef() + "]");
	}


	public void setUcmBatchRef(String ucmBatchRef) {
		this.ucmBatchRef = ucmBatchRef;
	}


	public String getUcmBatchRef() {
		return ucmBatchRef;
	}


	public void setSppBatchRef(Integer sppBatchRef) {
		this.sppBatchRef = sppBatchRef;
	}


	public Integer getSppBatchRef() {
		return sppBatchRef;
	}
}
