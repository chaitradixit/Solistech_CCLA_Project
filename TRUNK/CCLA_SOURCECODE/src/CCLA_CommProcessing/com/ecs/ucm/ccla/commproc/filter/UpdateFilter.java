package com.ecs.ucm.ccla.commproc.filter;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.commproc.InstructionUtils;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;

import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;

/** Hooked into the postDocHistoryInfo filter event.
 *  
 *  Guaranteed to execute AFTER a content item's metadata has been changed in some
 *  way.
 *  
 * @author Tom
 *
 */
public class UpdateFilter implements FilterImplementor {
	
	public static final String PREVENT_INSTRUCTION_UPDATE = "preventInstructionUpdate";
	
	/** Checks for a mapped Instruction instance.
	 *  
	 *  If one is present, it will be updated to match the UCM content item metadata.
	 * 
	 */
	public int doFilter(Workspace workspace, DataBinder binder, ExecutionContext exc)
	 throws DataException, ServiceException {

		
		// Ensure this is not a checkin service
		String idcService = binder.getLocal("IdcService");
		
		if (StringUtils.stringIsBlank(idcService) || idcService.indexOf("CHECKIN") != -1) {
			return FilterImplementor.CONTINUE;
		}
		
		FWFacade cdbFacade = null;
		Integer dID = CCLAUtils.getBinderIntegerValue(binder, UCMFieldNames.DocID);
		
		if (CCLAUtils.getBinderBoolValue(binder, PREVENT_INSTRUCTION_UPDATE)) {
			return FilterImplementor.CONTINUE;
		}
		
		if (dID != null) {
		
			Log.info("dRevisionId: "+binder.getLocal(UCMFieldNames.RevisionId) 
					+ ", dId: "+ binder.getLocal(UCMFieldNames.DocID));
			
			String docType = binder.getLocal(UCMFieldNames.DocType);
			
			if (StringUtils.stringIsBlank(docType)
				||
				!(docType.equals("Document") || docType.equals("ChildDocument"))) {
				return FilterImplementor.CONTINUE;
			}	
			long startTime = System.currentTimeMillis();
			
			// Check this id in ucm before updating the instruction register
			// Check if this document has already been converted to an instruction
			cdbFacade = CCLAUtils.getFacade(workspace, true);
			
			Log.debug("Checking for dID " + dID + " in Instruction Register");

			if (cdbFacade == null) {
				Log.warn("Failed to build FWFacade, unable to run UpdateFilter");
				return FilterImplementor.CONTINUE;
			}
			
			Instruction instr = null; 
			try {
				String docGuid = CCLAUtils.getDocGuidFromDid(dID);
				instr = Instruction.getByDocGuid(docGuid, cdbFacade);
				
			} catch (Exception e) {
				Log.error("unable to get instruction: " + e.getMessage());
				throw new ServiceException("unable to get instruction: " + e.getMessage());
			}
			
			if (instr != null) {
				Log.debug("Document " + dID + " had a mapped instruction " 
				+ instr.getInstructionId() + ". Updating mapped fields");
				
				try {
					InstructionUtils.updateInstructionFromDocMeta
					 (instr, dID, cdbFacade, binder.getLocal("dUser"));
				} catch (Exception e) {
					String msg = "Failed to update linked Instruction " +
					 "data: " + e.getMessage();
					
					Log.error(msg, e);
					throw new ServiceException(msg, e);
				}
			} else {
				Log.debug("Document " + dID + " had no mapped instruction");
			}
			
			long endTime = System.currentTimeMillis();
			
			Log.debug("CCLA_CommProcessing UpdateFilter completed, dDocName: "
			 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
			 (endTime-startTime)/1000D + "s");
		}
		return FilterImplementor.CONTINUE;
	}

}
