package com.ecs.ucm.ccla.commproc;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.commproc.filter.UpdateFilter;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CommUtils {
	
	/** Adds a document bundle with the given bundle/batch reference to the 
	 *  Comm/Instruction registers.
	 *  
	 * @param bundleRef
	 * @param persist 		determines whether the DB is updated
	 * @param facade
	 * @return list of generated Instructions
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static Vector<Instruction> addDocumentBundle
	 (String bundleRef, boolean persist, boolean append, 
	 FWFacade facade, String userName) throws DataException, ServiceException {
		
		DocumentBundleTranslator bundleTranslator = new DocumentBundleTranslator
		 (bundleRef, persist, append, facade, userName);
		
		return bundleTranslator.translate();
	}
	
	/** Outputs the passed Comm -> Instruction mapping to log4j.
	 * 
	 * @param map
	 */
	public static void debugCommInstructionMap
	 (HashMap<Comm, Vector<Instruction>> map) {
		
		Log.debug("Comm -> Instruction Mapping: ");
		Log.debug(map.size() + " Communications");
		
		for (Comm comm : map.keySet()) {
			Log.debug(comm.toString());
			
			Vector<Instruction> instrs = map.get(comm);
			
			Log.debug(instrs.size() + " linked Instructions");
			
			for (Instruction instr : instrs) {
				Log.debug(" -> " + instr.toString());
			}
		}
	}
	
	/** Prepares the given CommGroup instance so that its instructions can trigger
	 *  jobs.
	 *  
	 *  Allocates an SPP Batch Number to the CommGroup, if one isn't already set.

	 *  If the CommGroup has a UCM bundle/batch ref set, the corresponding UCM bundle 
	 *  item will also be updated with the SPP Batch Number.
	 *  
	 * @param commGroup
	 * @param cdbFacade Central DB facade
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void prepareCommGroupForSpp(CommGroup commGroup, FWFacade cdbFacade)
	 throws DataException, ServiceException {
		
		Log.debug("Preparing Comm Group ID " + commGroup.getCommGroupId() + " for SPP");
		
		//The pass in facade will always be CDB
		FWFacade ucmFacade = CCLAUtils.getFacade(false);
		
		if (commGroup.getSppBatchRef() != null) {
			return; // Comm Group already has an SPP batch ID allocated
		}
			
		Log.debug("Checking FWFacade transaction state");
		
		// Fail immediately if we aren't in a transaction block
		if (!cdbFacade.isInTransaction()) {
			String msg = "Unable to submit Instruction to SPP: not inside " +
			 "a transaction block";
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		// Grab new workflow ID
		int newWorkflowId = Integer.parseInt(CCLAUtils.getNextWorkflowId(cdbFacade));
		
		Log.debug("Allocating new SPP Batch Number " + newWorkflowId +
		 " to CommGroup: " + commGroup.toString());
		
		commGroup.setSppBatchRef(newWorkflowId);
		commGroup.persist(cdbFacade, Globals.Users.System);
		Log.debug("Finshed setting sppBatchRef:"
		 +newWorkflowId+" to commGroup "+commGroup.getCommGroupId());
		
		// Set the workflow ID on the UCM parent bundle item
		if (!StringUtils.stringIsBlank(commGroup.getUcmBatchRef())) {
			
			Log.debug("Updating UCM parent bundle item with bundle ref:"
			 + commGroup.getUcmBatchRef());
			
			try {
				LWDocument parentDoc = 
				 BatchDocumentServices.getParentBatchDoc
				 (commGroup.getUcmBatchRef(), ucmFacade);
				
				parentDoc.setUseTransaction(true);
				
				parentDoc.setAttribute
				 (UCMFieldNames.BatchNumber, Integer.toString(newWorkflowId));
				// Shouldn't be a linked instruction against a bundle item, we'll
				// prevent the update anyway to avoid potential deadlock.
				parentDoc.setAttribute(UpdateFilter.PREVENT_INSTRUCTION_UPDATE, "1");
				parentDoc.update();
				
				Log.debug("Successfully allocated new batch number to bundle: " + 
				 newWorkflowId);
				
				// Add an audit entry to the UCM Document bundle, indicating the 
				// allocated SPP Batch ID
				Vector<String> params = new Vector<String>();
				params.add(commGroup.getSppBatchRef().toString());
				
				AuditUtils.addAuditEntry
					("IRIS", "SPP-BATCH-ALLOC", 
					parentDoc.getDocName(), parentDoc.getTitle(), 
					Globals.Users.System,
					"Instruction Register allocated SPP batch no: " 
					 + commGroup.getSppBatchRef().toString(), 
					params);
				
			} catch (Exception e) {
				String msg = "Failed to set SPP Batch Number on parent bundle item " + 
				 commGroup.getUcmBatchRef() + ": " + e.getMessage();
				 
				Log.error(msg, e);
				throw new ServiceException(msg, e);
			}
		}
	}
	
}
