package com.ecs.ucm.ccla.commproc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.Users;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.FileService;
import intradoc.server.Service;

/** Ad-hoc administrator services used for repairing/updating portions of data. May also
 *  generate required documents/instructions to propagate changes to downstream systems
 *  e.g. Aurora.
 *  
 * @author tm
 *
 */
public class DataFixService extends FileService {
	
	/** Updates all Aurora Client Link entries with 'incorrect' default statement months 
	 *  to 'correct' default statement months, then generates the required SDU 
	 *  instructions to apply these changes to Aurora.
	 *  
	 *  It was identified in early 2014 that many new Aurora client records have incorrect
	 *  Statement Months configurations. This was caused by incorrect default values
	 *  in UCM being propagated to Aurora.
	 *  
	 *  To fix this, the incorrect records must be first identified in UCM, updated there,
	 *  then the required UPDATE_CLIENT SDU instructions generated to apply the changes
	 *  to the Aurora records.
	 *  
	 *  On completion, the service will serve up a CSV file containing all the affected
	 *  Client references.
	 *  
	 *  Accepts the following parameters:
	 *  
	 *  targetCompanyIds: 	CSV of target Aurora Companies, e.g. "1,2,3"
	 *  updateLimit:		Update loop will terminate after this many clients are 
	 *  					updated. Leave null to update all targeted clients
	 *  commit:				Determines whether client record updates are actually 
	 *  					persisted to the database, and UPDATE_CLIENT instructions
	 *  					are generated. If not, the service effectively runs in 'test
	 *  					mode' but you'll still get a CSV of the updated clients at
	 *  					the end.
	 *  sourceInstructionId:Used as a reference instruction to link the UPDATE_CLIENT
	 *  					instructions to. Must be to a valid Instruction ID if commit
	 *  					flag is set.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void updateAuroraStatementMonths() throws DataException, ServiceException {
		
		if (!m_userData.m_name.equals("weblogic")) {
			throw new ServiceException
			 ("This service is only available to the weblogic user");
		}
		
		Log.debug("Updating Aurora Statement Month configurations");
		
		String targetCompanyIds = m_binder.getLocal("targetCompanyIds");
		
		if (StringUtils.stringIsBlank(targetCompanyIds)) {
			throw new ServiceException("No target company IDs specified");
		}
		
		String[] targetCompanies = targetCompanyIds.split(",");
		
		// Check for commit flag. If not present, run all fixes in 'test' mode, so nothing
		// actually gets applied to the DB.
		boolean commit = CCLAUtils.getBinderBoolValue(m_binder, "commit");
		
		if (commit)
			Log.debug("Commit Flag detected - all changes will be permenantly" +
			 " applied to the database");
		else
			Log.debug("Commit Flag not detected - all changes will be run in " +
			 "test mode only");
	
		FWFacade cdbFacade = CCLAUtils.getFacade(true);
		
		// Source reference for generated UPDATE_CLIENT SDU instructions.
		// Only applicable when running in 'commit' mode.
		Integer sourceInstrId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "sourceInstructionId");
		Instruction sourceInstr = null;
		
		if (commit && sourceInstrId == null) {
			throw new ServiceException
			 ("Parameter sourceInstructionId missing from request. Must be " +
			 "supplied when running in Commit mode");
		} else if (commit) {
			// Verify passed parameters.
			sourceInstr = Instruction.get(sourceInstrId, cdbFacade);
			if (sourceInstr == null)
				throw new ServiceException
				 ("Unable to find source Instruction with ID " + sourceInstrId);
		}
		
		// Check for Update Limit. If non-null, the update routine will terminate after
		// this number of client records have been updated.
		Integer updateLimit = CCLAUtils.getBinderIntegerValue(m_binder, "updateLimit");
		
		if (updateLimit != null) {
			Log.debug("Update Limit found. Only the first " + updateLimit 
			 + " client records will be updated");
		}
		
		// 'Incorrect' Statement Month values we'll search for (Jan only)
		int[] targetMonthValues = {1, 0, 0, 0};
		// 'Correct' Statement Month values we'll update to (June/Dec)
		int[] correctMonthValues = {6, 12, 0, 0};
		
		DataBinder qBinder = new DataBinder();
		
		for (int i=0; i<targetMonthValues.length; i++)
			CCLAUtils.addQueryIntParamToBinder
			 (qBinder, "STATEMENTS_MONTH_" + (i+1), targetMonthValues[i]);
		
		int updated = 0;
		
		// Build a ResultSet as we go along, recording which clients were affected.
		DataResultSet rsAffectedClients = new DataResultSet(new String[] {
			"ORGANISATION_ID",
			"COMPANY_ID",
			"COMPANY_CODE",
			"CLIENT_NUMBER"
		});
		
		try {
			if (commit)
				cdbFacade.beginTransaction();
			
			// Loop through target Aurora companies in turn (4 companies total)
			for (int i = 0; i < targetCompanies.length; i++) {
				int companyId = Integer.parseInt(targetCompanies[i]);
				Company company = Company.getCache().getCachedInstance(companyId);
				
				Log.debug("Fixing all records belonging to Company: " + company.getCode());
				
				CCLAUtils.addQueryIntParamToBinder
				 (qBinder, Company.Cols.ID, company.getCompanyId());
				
				DataResultSet rsTargetClients = cdbFacade.createResultSet
				 ("qCommProc_GetAuroraClientLinksByStatementMonths", qBinder);
				
				Log.debug("Found " + rsTargetClients.getNumRows() + 
				 " target client records to update");
	
				if (rsTargetClients.first()) {
					do {
						int clientNumber = CCLAUtils.getResultSetIntegerValue
						 (rsTargetClients, AuroraClient.Cols.CLIENT_NUMBER);
						
						int orgId = CCLAUtils.getResultSetIntegerValue
						 (rsTargetClients, Entity.Cols.ID);
						
						Log.debug("Updating Client Number " + clientNumber + " (Org ID: " 
						 + orgId + ")");
	
						AuroraClient client = AuroraClient.get(rsTargetClients);
						
						Log.debug("Statement Months (Pre-Update): " 
						 + client.getStatementMonths1() + ", "
						 + client.getStatementMonths2() + ", "
						 + client.getStatementMonths3() + ", "
						 + client.getStatementMonths4());
						
						client.setStatementMonths1(correctMonthValues[0]);
						client.setStatementMonths2(correctMonthValues[1]);
						client.setStatementMonths3(correctMonthValues[2]);
						client.setStatementMonths4(correctMonthValues[3]);
						
						Log.debug("Statement Months (Post-Update): " 
						 + client.getStatementMonths1() + ", "
						 + client.getStatementMonths2() + ", "
						 + client.getStatementMonths3() + ", "
						 + client.getStatementMonths4());
						
						if (commit)
							client.persist(cdbFacade, Users.System);
						
						updated++;
						
						// Append to affected clients ResultSet.
						Vector<String> v = new Vector<String>();
						v.add(Integer.toString(client.getOrganisationId()));
						v.add(Integer.toString(company.getCompanyId()));
						v.add(company.getCode());
						v.add(Integer.toString(client.getClientNumber()));
						
						rsAffectedClients.addRow(v);
						
						// Generate required UPDATE_CLIENT instruction
						if (commit)
							addUpdateClientInstruction
							 (sourceInstr, orgId, company, cdbFacade, m_userData.m_name);
						
						if (updateLimit != null && (updated >= updateLimit)) {
							break;
						}
						
					} while (rsTargetClients.next());
				}
				
				if (updateLimit != null && (updated >= updateLimit)) {
					Log.debug("Update limit reached. Terminating update routine");
					break;
				}
				
				Log.debug("Finished updating record for Company: " + company.getCode());
			}
			
			Log.debug("================");
			Log.debug("Updated " + updated + " target client records");
			
			// Serve up CSV of affected clients.
			String tempDir = DataBinder.getTemporaryDirectory();
			
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy HHmm");
			
			String fileName = "Statement Month Fix - Affected Client List " 
			 + sdf.format(new Date()) + (commit ? " (no commit)" : "")
			 + ".csv";
			
			File csvFile = new File(tempDir + "/" + fileName);
			
			StringBuffer sb = CCLAUtils.convertToCSV(rsAffectedClients, null);
			
			FileWriter fw;
			try {
				fw = new FileWriter(csvFile);
				fw.write(sb.toString());
				fw.close();
			} catch (IOException e) {
				throw new ServiceException
				 ("Unable to generate CSV file: " + e.getMessage(), e);
			}
			
			Log.debug("CSV generated with file name " + fileName + ", serving to client");
			
			m_binder.addTempFile(tempDir + "/" + fileName);
	
			this.setSendFile(true);
			this.setFile(tempDir + "/" + fileName);
			this.setDownloadName(fileName);
	
			this.setDownloadFormat("text/csv");
			this.m_binder.putLocal("dExtension", "csv");
			
			if (commit) {
				cdbFacade.commitTransaction();
				Log.debug("Updates committed.");
			}
			
		} catch (Exception e) {
			if (commit)
				cdbFacade.rollbackTransaction();
			
			Log.error("Failed during transaction block", e);
			throw new ServiceException(e);
		}
	}
	
	private static void addUpdateClientInstruction
	 (Instruction parentInstruction, int orgId, Company company, 
	 FWFacade cdbFacade, String userName) 
	 throws DataException {
		
		Log.debug("Generating UPDATE_CLIENT instruction for Organisation ID: " + orgId);
		
		// Load SDU Instruction stuff from cache
		InstructionType instrType = InstructionType.getIdCache().getCachedInstance
		 (InstructionType.Ids.UPDATE_CLIENT);
		
		InstructionData orgIdData = InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.ORGANISATION_ID);
		InstructionData companyData = InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.COMPANY_ID);
		InstructionData authUserData = InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.AUTHORISED_USER);
		InstructionData isAuthData = InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.IS_AUTHORISED);
		InstructionData authDateData = InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.AUTHORISED_DATE);
		InstructionData sduFieldGroupsData = InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.SDU_FIELD_GROUPS);
		InstructionData parentInstrData = InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.PARENT_INSTR_REF);
		InstructionData commentData = InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.INSTRUCTION_COMMENTS);
		
		Date now = new Date();

		// Create the UPDATE_CLIENT instruction
		Instruction updateClientInstr = Instruction.add(
			parentInstruction.getCommId(), 
			InstructionStatus.getCache().getCachedInstance(
				InstructionStatus.StatusID.READY_FOR_STATIC_DATA_EXECUTION), 
			instrType,
			null, 
			null, 
			now, 
			userName, 
			cdbFacade
		);
		
		Log.debug("Instruction generated with ID: " 
		 + updateClientInstr.getInstructionId());
		
		// Set applied data fields against new instruction
		InstructionDataApplied.addOrUpdate
		 (updateClientInstr, orgIdData, orgId, cdbFacade, userName);
		InstructionDataApplied.addOrUpdate
		 (updateClientInstr, companyData, company.getCompanyId(), 
		 cdbFacade, userName);
		InstructionDataApplied.addOrUpdate
		 (updateClientInstr, authUserData, userName, 
		 cdbFacade, userName);
		InstructionDataApplied.addOrUpdate
		 (updateClientInstr, isAuthData, true, 
		 cdbFacade, userName);
		InstructionDataApplied.addOrUpdate
		 (updateClientInstr, authDateData, now, 
		 cdbFacade, userName);
		InstructionDataApplied.addOrUpdate
		 (updateClientInstr, sduFieldGroupsData, "StatementPreferences", 
		 cdbFacade, userName);
		InstructionDataApplied.addOrUpdate
		 (updateClientInstr, parentInstrData, parentInstruction.getInstructionId(), 
		 cdbFacade, userName);
		
		InstructionDataApplied.addOrUpdate
		 (updateClientInstr, commentData, 
		 "Generated by ECS to fix Statement Months configuration", 
		 cdbFacade, userName);
	}
}
