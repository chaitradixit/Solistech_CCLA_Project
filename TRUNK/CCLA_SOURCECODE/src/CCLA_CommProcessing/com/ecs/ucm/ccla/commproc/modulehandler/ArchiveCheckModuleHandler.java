package com.ecs.ucm.ccla.commproc.modulehandler;

import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.commproc.InstructionLockUtils;
import com.ecs.ucm.ccla.commproc.filter.UpdateFilter;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Archive Check Module Handler.
 *  
 *  Accepts most instructions. If the item doesn't require further processing in UCM
 *  or other third-party system, it will evaluate to 'PASSED' here, applying the
 *  Archived status. No other module will pick up Archived instructions. If the 
 *  instruction needs further processing the 
 *  
 *  The Pass and Skip statuses will match and simply forward the instruction to the
 *  next step.
 * 
 * @author Tom
 *
 */
public class ArchiveCheckModuleHandler extends RoutingModuleHandler {

	// enum mapping of mandate types
	private static enum MandateTypes{MAND,APP,AUTOMAND,AUTOAPP}
	
	private static final EvalOutcome ARCHIVE_INSTRUCTION = EvalOutcome.PASS;
	private static final EvalOutcome DO_NOT_ARCHIVE = EvalOutcome.FAIL;
	


	/**
	 * Method that checks if the given type is in the enum mapping
	 * of mandate types
	 * @param type
	 * @return
	 */
	private boolean checkMandTypes(String type){
		for (MandateTypes mandType: MandateTypes.values()) {
			if(mandType.name().equals(type)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected EvalOutcome eval(Instruction instr, FWFacade facade)
			throws DataException {

		if (!instr.getType().isSubmitToSpp()){
			return ARCHIVE_INSTRUCTION;
		}
		
		if (instr.getType().isSupporting()) {
			// This may be a candidate for archival check to see if any documents
			// in the group are a mandate, if there are fail else pass
			Log.info("Archive module processing instruction: " + instr.getInstructionId() +
						" [type:"+instr.getType().getName()+"]");
			try {
				Comm comm = Comm.get(instr.getCommId(), facade);
				int commGroupId = comm.getGroupId();
				
				Vector<Instruction> instrs = CommGroup.getInstructions(commGroupId, facade);
				Log.info("Number of instrutions: "+instrs.size()+", in commGroupId:"+commGroupId);
				
				for (Instruction instruction : instrs) {
					if (checkMandTypes(instruction.getType().getName())){
						Log.info("bundle contains a "+instruction.getType().getName()
								+" applying: "+ ARCHIVE_INSTRUCTION.toString());
						return ARCHIVE_INSTRUCTION;
					}
				}
				
			} catch (ServiceException e) {
				Log.error("Unable to evaluate instruction: " + 
						instr.getInstructionId()+" ,"+e.getMessage());
				throw new DataException("Unable to evaluate instruction: " + 
						instr.getInstructionId()+" ,"+e.getMessage());
			} 
			Log.info("Document is supporting and bundle does not contain a mand, " +
					"applying: " + DO_NOT_ARCHIVE.toString());
			return DO_NOT_ARCHIVE;
		} else
			return DO_NOT_ARCHIVE;
		
	}
	
	
	/** Sets the 'Pass status' on the instruction (expected to be 'Archived')
	 * 
	 *  Also updates the related UCM document xStatus value to 'Archived', if 
	 *  applicable.
	 *  
	 * @param instr
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	protected void applyPassAction(Instruction instr, FWFacade facade, String userName) 
	 throws DataException {
		
		InstructionStatus passStatus = this.getRoutingModule().getPassStatus();
		
		if (passStatus == null)
			throw new DataException("Module Pass Status missing");
		
		instr.setStatus(passStatus);
		instr.persist(facade, userName);
		
		if (instr.getInstructionDocGuid() != null) {
			// Update the source document status to Archived.
			try {
				LWDocument lwDoc = 
					CCLAUtils.getLatestLwdFromDocGuid(instr.getInstructionDocGuid());
				lwDoc.setAttribute(UCMFieldNames.Status, 
				 InstructionStatus.ARCHIVED.getInstructionStatusName());
				
				lwDoc.setAttribute(UpdateFilter.PREVENT_INSTRUCTION_UPDATE, "1");
				lwDoc.setUseTransaction(true);
				
				lwDoc.update();
				
			} catch (Exception e) {
				throw new DataException("Failed to apply 'Archived' status to source "
				 + "document with docGuid: " + instr.getInstructionDocGuid());
			}
		}
		
		InstructionLockUtils.unlockInstruction
		 (instr.getInstructionId(), this.getRoutingModule(), facade, 
	     userName);
	}
}
