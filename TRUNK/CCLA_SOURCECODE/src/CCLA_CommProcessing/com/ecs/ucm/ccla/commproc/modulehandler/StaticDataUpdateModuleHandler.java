package com.ecs.ucm.ccla.commproc.modulehandler;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.shared.SharedObjects;

import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

import com.aurora.webservice.Client;
import com.aurora.webservice.Correspondent;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.aurora.AuroraAccountHandler;
import com.ecs.ucm.ccla.aurora.AuroraClientHandler;
import com.ecs.ucm.ccla.aurora.AuroraCorrespondentHandler;
import com.ecs.ucm.ccla.aurora.compare.AccountFieldSet;
import com.ecs.ucm.ccla.aurora.compare.ClientFieldSet;
import com.ecs.ucm.ccla.aurora.compare.ComparisonUtils;
import com.ecs.ucm.ccla.aurora.compare.CorrespondentFieldSet;
import com.ecs.ucm.ccla.commproc.InstructionUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.InstructionDataValue;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.staticdataupdate.StaticDataUpdate;
import com.ecs.ucm.ccla.data.staticdataupdate.StaticDataUpdateApplied;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Handles generation of Static Data Update instructions that propagate data to
 *  Aurora when executed, e.g. UPDATE_PERSON.
 *  
 *  Doesn't actually perform Aurora execution of given instructions - see 
 *  StaticDataExecutionModuleHandler for this.
 * 
 * @author tm
 *
 */
public class StaticDataUpdateModuleHandler extends RoutingModuleHandler {
	
	/** Whether or not the presence of a pre-existing 'Create' instruction for the same 
	 *  Aurora entity will convert a subsequent 'Create' to an 'Update' instead.
	 *  
	 *  If true, the duplicate 'Create' is converted to an equivalent 'Update'. If false,
	 *  no duplicate instruction is generated.
	 */
	public static final boolean CONVERT_DUPLICATE_CREATION_INSTRUCTIONS_TO_UPDATE 
	 = false;
	
	/** Delay period in seconds added to each call to the eval() method. Used to simulate
	 *  long running times in test environments.
	 */
	private static Integer EVAL_DELAY_PERIOD;
	
	static {
		String delayPeriodStr = SharedObjects.getEnvironmentValue
		 ("CCLA_CP_StaticDataUpdateModuleEvalDelayPeriod");
		
		if (!StringUtils.stringIsBlank(delayPeriodStr))
			EVAL_DELAY_PERIOD = Integer.parseInt(delayPeriodStr);
	}
	

	/**
	 * only accepts instructions marked for static data processing, 
	 * i.e. exist StaticDataUpdateApplied table 
	 */
	protected boolean accept(Instruction instr, FWFacade facade)
	throws DataException 
	{
		return InstructionUtils.isEligibleToGenerateStaticDataUpdates
		 (instr, facade);
	}
	
	protected EvalOutcome eval(Instruction instr, FWFacade facade)
	 throws DataException 
	 {
		try {
			if (EVAL_DELAY_PERIOD != null) {
				Log.debug("Delaying eval execution for " 
				 + EVAL_DELAY_PERIOD + " seconds");
				Thread.sleep(EVAL_DELAY_PERIOD * 1000);
			}
			
			int instrTypeId = instr.getType().getInstructionTypeId();
			
			Vector<StaticDataUpdateApplied> sduaVec = 
				StaticDataUpdateApplied.getIdCache().getCachedInstance(instrTypeId);
			
			//Quick check if see if allowed.
			if (sduaVec==null) {
				Log.error("Suspended instruction as StaticDataUpdateApplied doesn't " +
						"contain this instruction type: "+instrTypeId);
				return EvalOutcome.SUSPEND;
			}
			
			// Preload instruction data
			instr.loadDataApplied(facade);
			
			//Stores instruction in order of execution
			TreeMap<Integer,Vector<Instruction>> map = 
			 new TreeMap<Integer,Vector<Instruction>>(); 
			
			for (StaticDataUpdateApplied sdua: sduaVec) {
				switch(sdua.getSduId()) {
					case StaticDataUpdate.Ids.AURORA_CLIENT:
						Instruction clientInstr = processAuroraClient(sdua, instr, facade);
						if (clientInstr!=null) {
							Vector<Instruction> v = new Vector<Instruction>();
							v.add(clientInstr);
							
							map.put(sdua.getExecutionOrder(), v);
						}
						
						break;
					case StaticDataUpdate.Ids.AURORA_CORRESPONDENT:
						Vector<Instruction> corrInstrs = processAuroraCorrespondents
						 (sdua, instr, facade);
						
						if (!corrInstrs.isEmpty())
							map.put(sdua.getExecutionOrder(), corrInstrs);
						
						break;
					case StaticDataUpdate.Ids.AURORA_ACCOUNT:
						Vector<Instruction> accInstrs = processAuroraAccount(sdua, instr, facade);
						
						if (!accInstrs.isEmpty())
							map.put(sdua.getExecutionOrder(), accInstrs);
						
						break;
					default:
						Log.warn("Processing of this StaticDataApplied: "+sdua.getSduId());
						break;
				}
			}
			
			//Set dependent instruction ids
			Integer previousInstrId = null;
			for (Vector<Instruction> genInstrs: map.values()) {
				
				for (Instruction genInstr : genInstrs) {
					if (previousInstrId!=null) {
						genInstr.setDependentInstructionId(previousInstrId);
					}
		
					if (genInstr.getInstructionId()!=0) {
						previousInstrId = genInstr.getInstructionId();
					}
				}
			}

			// Finally, add all the instructions and generated data to the DB
			for (Vector<Instruction> genInstrs : map.values()) 
			{
				for (Instruction genInstr : genInstrs) {
					Instruction.add(genInstr, Globals.Users.System, facade);	
					
					InstructionDataApplied.addOrUpdateAll
					 (genInstr.getDataApplied(), facade, Globals.Users.System);
				}
			}
			
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			
			// Set ERROR_MESSAGE field on the SDU instruction and suspend.
			InstructionDataFieldValue errMsg = new InstructionDataFieldValue
			 (DataType.STRING);
			
			errMsg.setStringValue(e.getMessage());
			
			InstructionDataApplied.addOrUpdate
			 (instr, InstructionData.getCache().getCachedInstance
			  (InstructionData.Fields.ERROR_MESSAGE), errMsg, facade, Globals.Users.System);
			
			return EvalOutcome.SUSPEND;
		}

		return EvalOutcome.PASS;	
	}
	
	@Override
	protected void applyPassAction(Instruction instr, FWFacade facade,
			String userName) throws DataException {
		
		// Remove any applied Error message
		InstructionDataApplied.addOrUpdate
		 (instr, InstructionData.getCache().getCachedInstance
		  (InstructionData.Fields.ERROR_MESSAGE), 
		  new InstructionDataFieldValue(DataType.STRING), facade, Globals.Users.System);
		
		super.applyPassAction(instr, facade, userName);
	}

	/**
	 * Generates a Client SDU Instruction.
	 * 
	 * @param sdua
	 * @param sourceInstr
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private static Instruction processAuroraClient
	 (StaticDataUpdateApplied sdua, Instruction sourceInstr, FWFacade facade) 
	 throws DataException{

		Instruction clientInstr = null;
		
		if (!sdua.isAllowCreate() && !sdua.isAllowUpdate()) {
			Log.debug("Not allowed to create/update AuroraClient for instructionID :"
			 +sourceInstr.getInstructionId());
			return clientInstr;
		}

		InstructionDataApplied orgDataApplied = 
			InstructionDataApplied.getDataApplied(
					sourceInstr.getInstructionId(), 
					InstructionData.Fields.ORGANISATION_ID, 
					facade);
		
		InstructionDataApplied accDataApplied = 
			InstructionDataApplied.getDataApplied(
					sourceInstr.getInstructionId(), 
					InstructionData.Fields.SOURCE_ACCOUNT_ID, 
					facade);
		
		if ((orgDataApplied!=null && !orgDataApplied.getDataFieldValue().isEmpty()) 
			&& (accDataApplied!=null && !accDataApplied.getDataFieldValue().isEmpty())) {
			// OK
		} else {
			// Required instr. data values missing!
			String err = "Unable to generate Aurora Client SDU instruction, " +
			 "ORGANISATION_ID or SOURCE_ACCOUNT_ID is missing from source instruction:"
			 +sourceInstr.getInstructionId();
			
			Log.error(err);
			throw new DataException(err);
		}

		Entity org = Entity.get
		 (orgDataApplied.getDataFieldValue().getIntValue(), true, facade);
		Account acc = Account.get
		 (accDataApplied.getDataFieldValue().getIntValue(), facade);
		
		if (org==null || acc==null) {
			String err = "Unable to generate Aurora Client SDU instruction, " +
			 "Org ID or Account ID references invalid";
			
			Log.error(err);
			throw new DataException(err);
		}

		Fund fund = Fund.getCache().getCachedInstance(acc.getFundCode());
		Company company = fund.getCompany();

		Client client = null;
		
		AuroraClientHandler auroraHandler = new AuroraClientHandler();
		
		try {
			client = auroraHandler.getExistingAuroraEntity(org, company, facade);
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
		}
		
		Vector<InstructionDataValue> extraDataValues = null;
		boolean existsInAurora = client!=null;
		
		// Duplicate check.
		boolean sduInstructionExists = isExistingSDUInstruction(
			sdua, 
			company, 
			existsInAurora, 
			org.getOrganisationId(), 
			null, 
			null,
			facade
		);
		
		if (sduInstructionExists) {
			// Terminate early.
			Log.debug("Early termination from processAuroraClient: "
				+ "SDU instruction already pending");
			return clientInstr; 
		}
		
		Vector<String> updatedFieldGroups = null;
		
		if (existsInAurora) {
			// The entity already exists in Aurora - in this case, we'll build
			// a list of recently modified field groups. This will be queried
			// later to determine whether or not the Update instruction is
			// needed at all.
			ClientFieldSet cfs = new ClientFieldSet();
			cfs.calculateGroupAmendmentDates(org, company, facade);
			
			Date limitDate = ComparisonUtils.getAmendmentLimitDate
			 (sourceInstr.getDateAdded());
			
			updatedFieldGroups = 
			 cfs.getFilteredGroupNamesByAmendmentDate(limitDate);
		}
		
		clientInstr = createAddOrUpdateInstruction
		 (sourceInstr, company, existsInAurora, 
		 sdua, extraDataValues, updatedFieldGroups, facade);
		
		return clientInstr;
	}	
	
	/**
	 * Generates required SDU Correspondent instructions (CREATE_CORR, UPDATE_CORR).
	 * 
	 * Returns a Vector, as up to 2 distinct instructions may be generated - one for the
	 * account correspondent and one for the client correspondent, if they differ.
	 * 
	 * @param sdua
	 * @param sourceInstr
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private static Vector<Instruction> processAuroraCorrespondents
	 (StaticDataUpdateApplied sdua, Instruction sourceInstr, FWFacade facade) 
	 throws DataException {

		Log.debug("Processing Aurora Correspondents for SDU Instruction ID: " 
		 + sourceInstr.getInstructionId());
		
		Person personAccountCorr = null;
		Company company = null;
		
		Vector<Instruction> corrInstructions = new Vector<Instruction>();
		
		if (!sdua.isAllowCreate() && !sdua.isAllowUpdate()) {
			Log.debug("Not allowed to create/update Aurora Correspondent for " +
			 "Instruction ID: "+sourceInstr.getInstructionId());
			return corrInstructions;
		}
		
		AuroraCorrespondentHandler corrHandler = new AuroraCorrespondentHandler();
		
		InstructionData personIdDataField = InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.PERSON_ID);

		InstructionDataApplied accDataApplied = 
			InstructionDataApplied.getDataApplied(
					sourceInstr.getInstructionId(), 
					InstructionData.Fields.SOURCE_ACCOUNT_ID, 
					facade);
		
		InstructionDataApplied orgDataApplied = 
			InstructionDataApplied.getDataApplied(
					sourceInstr.getInstructionId(), 
					InstructionData.Fields.ORGANISATION_ID, 
					facade);
		
		Integer orgId = null;
		Integer accountId = null;
				
		if (orgDataApplied != null && !orgDataApplied.getDataFieldValue().isEmpty())
			orgId = orgDataApplied.getDataFieldValue().getIntValue();
		
		
		if (accDataApplied!=null && !accDataApplied.getDataFieldValue().isEmpty())
			accountId = accDataApplied.getDataFieldValue().getIntValue();
		
		if (accountId != null) {
			Account acc = Account.get
			 (accDataApplied.getDataFieldValue().getIntValue(), facade);

			if (acc==null) 
				throw new DataException("Cannot resolve Account Correspondent, " +
				 "account reference is missing from instruction");

			personAccountCorr = Account.getNominatedCorrespondent
			 (acc.getAccountId(), true, facade);
			
			if (personAccountCorr==null) 
				throw new DataException("Cannot resolve Account Correspondent. Ensure "+
				 "at least 1 person is set as correspondent. If there are multiple " +
				 "correspondents set, ensure one is marked as nominated");	
			else {
				Log.debug("Resolved Account Correspondent: " + personAccountCorr.getFullName());	
			}
			
			if (!StringUtils.stringIsBlank(acc.getFundCode())) {
				Fund fund = Fund.getCache().getCachedInstance(acc.getFundCode());
				
				if (fund==null)
					throw new DataException("Cannot resolve Account Aurora Correspondent, " +
					 "Fund is missing for fund code :"+acc.getFundCode());	
				
				company = fund.getCompany();
				
				if (company==null) 
					throw new DataException("Cannot resolve Account Aurora Correspondent, " +
					 "Company is missing for fund code :"+acc.getFundCode());
				
				Correspondent correspondent = corrHandler.getExistingAuroraEntity
				 (personAccountCorr, company, facade);

				// Need to pass down the PERSON_ID belonging to the Correspondent.
				Vector<InstructionDataValue> extraDataValues = 
				 new Vector<InstructionDataValue>();
				
				extraDataValues.add(new InstructionDataValue
				 (personIdDataField, personAccountCorr.getPersonId()));
				
				boolean existsInAurora = correspondent!=null;
				Vector<String> updatedFieldGroups = null;
				
				// Duplicate check.
				boolean sduInstructionExists = isExistingSDUInstruction(
					sdua, 
					company, 
					existsInAurora, 
					orgId,
					accountId,
					personAccountCorr.getPersonId(),
					facade
				);
				
				if (!sduInstructionExists && existsInAurora) {
					// The entity already exists in Aurora - in this case, we'll build
					// a list of recently modified field groups. This will be queried
					// later to determine whether or not the Update instruction is
					// needed at all.
					CorrespondentFieldSet cfs = new CorrespondentFieldSet();
					cfs.calculateGroupAmendmentDates(personAccountCorr, company, facade);
					
					Date limitDate = ComparisonUtils.getAmendmentLimitDate
					 (sourceInstr.getDateAdded());
					
					updatedFieldGroups = 
					 cfs.getFilteredGroupNamesByAmendmentDate(limitDate);
				}
				
				Instruction corrInstr = null;

				if (!sduInstructionExists) {
					corrInstr = createAddOrUpdateInstruction
					 (sourceInstr, company, existsInAurora, 
					 sdua, extraDataValues, updatedFieldGroups, facade);
				}
				
				if (corrInstr != null)
					corrInstructions.add(corrInstr);
				
			} else 
				throw new DataException("Cannot work out AuroraCorrespondent, " +
				 "FundCode is missing from account :"+acc.getAccountId());	
		} else  {
			Log.error("Cannot create SDU for Aurora Account Correspondent, " +
			 "SOURCE_ACCOUNT_ID is missing from instruction:"+sourceInstr.getInstructionId());
		}
		
		// Now check the Correspondent attached to the Organisation. If this 
		// Correspondent differs from the Account one, we need 2 SDU instructions,
		// 1 for each distinct correspondent.
		if (orgDataApplied!=null && !orgDataApplied.getDataFieldValue().isEmpty()) {
			
			Person personClientCorr = Entity.getNominatedCorrespondent
			 (orgId, true, facade);
			
			if (personClientCorr==null) 
				throw new DataException("Cannot resolve Client Correspondent. Ensure "+
				 "at least 1 person is set as correspondent. If there are multiple " +
				 "correspondents set, ensure one is marked as nominated");	
			else {
				Log.debug("Resolved Client Correspondent: " + 
				 personClientCorr.getFullName());	
			}
			
			if (personClientCorr.equals(personAccountCorr)) {
				Log.info("Client correspondent matches Account correspondent - no need "+
						 "for additional Static Data Update instruction");
			} else {
				Correspondent correspondent = corrHandler.getExistingAuroraEntity
				 (personClientCorr, company, facade);

				// Need to pass down the PERSON_ID belonging to the Correspondent.
				Vector<InstructionDataValue> extraDataValues = 
				 new Vector<InstructionDataValue>();
				
				extraDataValues.add(new InstructionDataValue
				 (personIdDataField, personClientCorr.getPersonId()));
				
				boolean existsInAurora = correspondent!=null;
				Vector<String> updatedFieldGroups = null;
				
				// Duplicate check.
				boolean sduInstructionExists = isExistingSDUInstruction(
					sdua, 
					company, 
					existsInAurora, 
					orgId,
					accountId,
					personClientCorr.getPersonId(), 
					facade
				);
				
				if (!sduInstructionExists && existsInAurora) {
					// The entity already exists in Aurora - in this case, we'll build
					// a list of recently modified field groups. This will be queried
					// later to determine whether or not the Update instruction is
					// needed at all.
					CorrespondentFieldSet cfs = new CorrespondentFieldSet();
					cfs.calculateGroupAmendmentDates(personClientCorr, company, facade);
					
					Date limitDate = ComparisonUtils.getAmendmentLimitDate
					 (sourceInstr.getDateAdded());
					
					updatedFieldGroups = 
					 cfs.getFilteredGroupNamesByAmendmentDate(limitDate);
				}
				
				Instruction corrInstr = null;
				
				if (!sduInstructionExists) {
					corrInstr = createAddOrUpdateInstruction
					 (sourceInstr, company, correspondent!=null, sdua, 
					 extraDataValues, updatedFieldGroups, facade);
				}
				
				if (corrInstr != null)
					corrInstructions.add(corrInstr);
			} 
		} else {
			Log.error("Cannot create SDU for Aurora Client Correspondent, " +
			 "ORGANISATION_ID is missing from instruction:"+
			 sourceInstr.getInstructionId());
		}
		
		return corrInstructions;
	}
	
	/**
	 *  Generates required SDU Account instructions (CREATE_ACCOUNT, UPDATE_ACCOUNT).
	 * 
	 * Returns a Vector, as up to 2 distinct instructions may be generated - one for the
	 * primary Account attached to the source instruction, and one for its mandated 
	 * account, if it exists.
	 * 
	 * @param sdua
	 * @param sourceInstr
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException  
	 */
	private static Vector<Instruction> processAuroraAccount
	 (StaticDataUpdateApplied sdua, Instruction sourceInstr, FWFacade facade) 
	 throws DataException, ServiceException {
		
		Vector<Instruction> accInstrs = new Vector<Instruction>();

		if (!sdua.isAllowCreate() && !sdua.isAllowUpdate()) {
			Log.debug("Not allowed to create/update Aurora Account for " +
			 "Instruction ID: " + sourceInstr.getInstructionId());
			return accInstrs;
		}

		InstructionDataApplied accDataApplied = 
			InstructionDataApplied.getDataApplied(
			sourceInstr.getInstructionId(), 
			InstructionData.Fields.SOURCE_ACCOUNT_ID, 
			facade);
		
		if (accDataApplied!=null && !accDataApplied.getDataFieldValue().isEmpty()) {
			// OK
		} else {
			String msg = "Cannot create SDU for AuroraAccount, SOURCE_ACCOUNT_ID is " +
			 "missing from instruction:"+sourceInstr.getInstructionId();
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		int accountId = accDataApplied.getDataFieldValue().getIntValue();
		
		InstructionDataFieldValue orgIdDataValue = 
			sourceInstr.getDataApplied(InstructionData.Fields.ORGANISATION_ID);
		
		Integer orgId = orgIdDataValue != null ? orgIdDataValue.getIntValue() : null;
		
		Log.debug("Generating SDU instructions for Account ID " + accountId);
		Account account = Account.get(accountId, facade);

		if (account==null) 
			throw new DataException
			 ("Unable to create SDU instruction, Account ID reference " + 
			 accDataApplied.getDataFieldValue().getIntValue() + " invalid");

		if (!account.isAuroraAccount()) {
			Log.debug
			 ("Not an Aurora Account - no need to create static update instructions");
			return accInstrs;
		}
		
		Company company = Fund.getCache().getCachedInstance
		 (account.getFundCode()).getCompany();
		
		AuroraAccountHandler handler = new AuroraAccountHandler();
		
		com.aurora.webservice.Account auroraAccount = null;
		auroraAccount = handler.getExistingAuroraEntity(account, company, facade);
		
		boolean existsInAurora = (auroraAccount != null);
		Vector<String> updatedFieldGroups = null;
		
		// Duplicate Check.
		boolean sduInstructionExists = isExistingSDUInstruction(
			sdua, 
			company, 
			existsInAurora, 
			orgId,
			accountId,
			null,
			facade
		);
		
		if (sduInstructionExists) {
			// Terminate early.
			Log.debug("Early termination from processAuroraAccount: "
				+ "SDU instruction already pending");
			return accInstrs; 
		}
		
		if (existsInAurora) {
			// The entity already exists in Aurora - in this case, we'll build
			// a list of recently modified field groups. This will be queried
			// later to determine whether or not the Update instruction is
			// needed at all.
			AccountFieldSet afs = new AccountFieldSet();
			afs.calculateGroupAmendmentDates(account, company, facade);
			
			Date limitDate = ComparisonUtils.getAmendmentLimitDate
			 (sourceInstr.getDateAdded());
			
			updatedFieldGroups = 
			 afs.getFilteredGroupNamesByAmendmentDate(limitDate);
		}
		
		// Look for Mandated Account reference.
		Account mandatedAcc = null;
		com.aurora.webservice.Account mandatedAuroraAccount = null;

		if (account.getMandatedAccId()!=null) {
			// Mandated Account is set. May need an SDU create instruction for this 
			// account, if it doesn't exist yet.
			mandatedAcc = Account.get(account.getMandatedAccId(), facade);
			
			Log.debug("Mandated Account set to Account ID " + mandatedAcc.getAccountId()
			 + ". Checking if SDU Create instruction is required. ");
			
			// Check if it exists in Aurora.
			mandatedAuroraAccount = handler.getExistingAuroraEntity
			 (mandatedAcc, company, facade);
			
			if (mandatedAuroraAccount==null) {	
				Log.debug("Mandated Account doesn't yet exist in Aurora! " +
				 "Generating additional SDU Create instruction");

				Vector<InstructionDataValue> dataValues = 
				 new Vector<InstructionDataValue>();
				
				// Add the Mandated Account ID.
				InstructionDataValue dataValue = new InstructionDataValue(
					InstructionData.getCache().getCachedInstance
					 (InstructionData.Fields.SOURCE_ACCOUNT_ID),
					String.valueOf(account.getMandatedAccId())
				); 
				dataValues.add(dataValue);

				// Add a comment explaining the reason for this SDU instruction.
				dataValue = new InstructionDataValue(
					InstructionData.getCache().getCachedInstance
					 (InstructionData.Fields.INSTRUCTION_COMMENTS),
					 "Auto-generated SDU Instruction, as this is the mandated " +
					  "account for Account ID: "+account.getAccountId()
				); 
				dataValues.add(dataValue);
				
				// Add a create-account instruction.
				Instruction mandatedAccountInstr = createAddOrUpdateInstruction
				 (sourceInstr, company, false, sdua, dataValues, null, facade);
				
				if (mandatedAccountInstr != null)
					accInstrs.add(mandatedAccountInstr);
			}
		}
		
		Instruction accountInstr = createAddOrUpdateInstruction
		 (sourceInstr, company, auroraAccount!=null, sdua, null, updatedFieldGroups, facade);
		
		if (accountInstr != null)
			accInstrs.add(accountInstr);
		
		// Check for the awkward set of circumstances:
		// 1. Primary Account A has a Mandated Account B
		// 2. Mandated Account B doesn't yet exist in Aurora
		// 3. Mandated Account B has Primary Account A set as its Mandated Account, i.e.
		//    they both refer to each other.
		//
		// In this case, the Mandated Account must be first created with the SDU instr.
		// added previously, with the Mandated Account reference missing.
		//
		// A subsequent SDU update instruction is required on this mandated account, which
		// will set the mandated account. The branch below creates this instruction.
		if (account.getMandatedAccId()!=null 
			&& mandatedAuroraAccount==null
			&& (mandatedAcc.getMandatedAccId()!=null && 
				mandatedAcc.getMandatedAccId().equals(account.getAccountId()))) {
			
			Log.debug("Mandated Account ID " + account.getMandatedAccId() + 
			 " refers to an account which doesn't yet exist in Aurora.");
			Log.debug("Creating extra UPDATE_ACCOUNT instruction which will set the" +
			 " mandated account details, after the mandated account has been " +
			 "created in Aurora");
			
			Vector<InstructionDataValue> dataValues = new Vector<InstructionDataValue>();

			InstructionDataValue dataValue = new InstructionDataValue(
			 InstructionData.getCache().getCachedInstance
			 (InstructionData.Fields.SOURCE_ACCOUNT_ID),
			 String.valueOf(account.getMandatedAccId())
			); 
			dataValues.add(dataValue);

			dataValue = new InstructionDataValue(
			 InstructionData.getCache().getCachedInstance
			 (InstructionData.Fields.INSTRUCTION_COMMENTS),
			 "Auto-generated SDU Instruction, for setting Mandated Account reference to " +
			 "source account ID: " + account.getAccountId()
			); 
			dataValues.add(dataValue);
			
			// Set the Inc. Dist. Prefs field group as the only group to be updated.
			Vector<String> mandatedAccFieldGroupName = new Vector<String>();
			mandatedAccFieldGroupName.add(AccountFieldSet.AccountFieldGroupConfig
			 .IncomeDistributionPreferences.getName());
			
			Instruction mandatedAccountInstr = createAddOrUpdateInstruction
			 (sourceInstr, company, true, sdua, dataValues, mandatedAccFieldGroupName, facade);
			
			if (mandatedAccountInstr != null)
				accInstrs.add(mandatedAccountInstr);
		}
		
		return accInstrs;
	}	
	
	/** Determines whether an SDU Instruction that corresponds to the given
	 *  instruction data values exists in the Instruction Register.
	 *  
	 *  This is used to terminate creation of the instruction early on, saving
	 *  on expensive processing tasks e.g. field group update checks.
	 *  
	 * @param sdua
	 * @param company
	 * @param existsInAurora
	 * @param sourceInstr
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private static boolean isExistingSDUInstruction(
			StaticDataUpdateApplied sdua,
			Company company,
			boolean existsInAurora,
			Integer orgId,
			Integer accountId,
			Integer personId,
			FWFacade facade) throws DataException {
		
		// Determine the type of Instruction we need.
		StaticDataUpdate sdu = StaticDataUpdate.getCache().getCachedInstance
			(sdua.getSduId());
				
		if (sdu==null) {
			String msg = "Cannot find StaticDataUpdate with id: "+sdua.getSduId();
			Log.error(msg);
			throw new DataException(msg);
		}
		
		InstructionType instrType;
		
		if (existsInAurora)
			instrType = InstructionType.getIdCache().getCachedInstance(sdu.getUpdateInstrTypeId());
		else
			instrType = InstructionType.getIdCache().getCachedInstance(sdu.getCreateInstrTypeId());
		
		Integer existingSDUInstrId = InstructionUtils.getLatestSDUInstructionIdByValues(
			instrType, 
			null, 
			company.getCompanyId(), 
			orgId, 
			accountId, 
			personId, 
			facade
		);
		
		return (existingSDUInstrId != null);
	}
	
	/**
	 * Generates an Static Data Add/Update instruction. Whether the instruction is
	 * add/update depends on the existsInAurora paramter - if false, it will be an add,
	 * update otherwise.
	 * 
	 * The passed StaticDataUpdateApplied instance will determine whether the required
	 * add/update is permitted.
	 * 
	 * If an add is required and permitted, a duplicate check takes place to ensure
	 * there isn't a pending add instruction for the same data object. If there is,
	 * the instruction is marked as a Duplicate, or converted to an update instruction,
	 * depending on the CONVERT_DUPLICATE_CREATION_INSTRUCTIONS_TO_UPDATE flag.
	 * 
	 * If an update instruction is required, the list of updatedFieldGroups becomes 
	 * relevant. If the list is null/empty, generation of the instruction is skipped, as 
	 * there are no recent DB changes to propagate over. If the list is non-empty, a 
	 * duplicate check takes place to ensure there isn't a pending update instruction for
	 * the same data object. If there is, the instruction is marked as a Duplicate.
	 * 
	 * The sourceInstr parameter should correspond to the original instruction that
	 * yielded the static data update instructions. This is used as a basis for
	 * generating the new instruction.

	 * The optional extraDataValues parameter can be used to explicitly set values
	 * against the new instruction, overwriting values taken from the source 
	 * instruction.
	 * 
	 * @param sourceInstr
	 * @param company
	 * @param existsInAurora
	 * @param sdua
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private static Instruction createAddOrUpdateInstruction(Instruction sourceInstr, 
	 Company company, boolean existsInAurora, StaticDataUpdateApplied sdua, 
	 Vector<InstructionDataValue> extraDataValues, 
	 Vector<String> updatedFieldGroups, FWFacade facade) 
	 throws DataException {	
		
		Log.debug("Creating Add/Update Instruction for Source Instruction ID: "
		 + sourceInstr.getInstructionId());
		
		Instruction instruction = null;
		Vector<InstructionDataApplied> dataApplied = null;
		
		if (company == null) {
			String msg = "Aurora Company missing";
			Log.error(msg);
			throw new DataException(msg);
		}	
		
		StaticDataUpdate sdu = StaticDataUpdate.getCache().getCachedInstance
		 (sdua.getSduId());
		
		if (sdu==null) {
			String msg = "Cannot find StaticDataUpdate with id: "+sdua.getSduId();
			Log.error(msg);
			throw new DataException(msg);
		}
		
		// Do a few validity checks first to see if we can generate the appropriate
		// instruction.
		if (!existsInAurora && !sdua.isAllowCreate()) {
			// Aurora entity doesn't exist, but SDU config doesn't allow creations.
			String msg = "Aurora entity doesn't exist, but Instruction Type "
			 + sourceInstr.getType().getName() + " doesn't allow creation of entities";
			
			Log.warn(msg);
			return null;
			
		} else if (existsInAurora && !sdua.isAllowUpdate()) {
			// Aurora entity already exists, but SDU config doesn't allow updates.
			String msg = "Aurora entity already exists, but Instruction Type "
			 + sourceInstr.getType().getName() + " doesn't allow update of entities";
			
			Log.warn(msg);
			return null;
		}
			
		if (!existsInAurora) {
			// No existing Aurora entity - We need a Create instruction.	
			
			InstructionType instrType = null;
			if  (sdu.getCreateInstrTypeId()!=null) {
				instrType = InstructionType.getIdCache().getCachedInstance
				 (sdu.getCreateInstrTypeId());
			} else {
				Log.warn("StaticDataUpdate doesn't have an Instruction Type ID " +
				 "for creation operations, Id: "+sdu.getId());
			}
			
			if (instrType!=null) {
				instruction = InstructionUtils.generateInstruction(
					sourceInstr, 
					instrType, 
					InstructionStatus.PENDING_STATIC_DATA_AUTHORISATION, 
					true, 
					Globals.Users.System, 
					facade
				);
				
				dataApplied = getAppliedData
				 (sourceInstr, instruction, company, extraDataValues, null, facade);
				instruction.setDataApplied(dataApplied);
				
				// Duplicate checking, need to check if an SDU instruction exists
				// with the same data. 
				Integer dupId = InstructionUtils.getDuplicateSDUInstructionId
				 (instruction, facade);
				
				if (dupId != null) {
					// Duplicate found!
					//Convert to update instruction if applicable.
					Log.debug("Duplicate 'Create' instruction found for Aurora entity.");
					
					// Used as the Comment data field value for this instruction.
					String duplicateComment = "Duplicate of create instruction "+dupId;

					if (CONVERT_DUPLICATE_CREATION_INSTRUCTIONS_TO_UPDATE) {
						Log.debug("Attempting to create 'Update' instruction instead");

						InstructionDataValue idvComment = new InstructionDataValue
								 (InstructionData.getCache().getCachedInstance
								 (InstructionData.Fields.INSTRUCTION_COMMENTS), 
								 duplicateComment);
						
						extraDataValues.add(idvComment);
						
						// Call this method again, with existsInAurora=true
						createAddOrUpdateInstruction(sourceInstr, company, true, 
						 sdua, extraDataValues, updatedFieldGroups, facade);
						
					} else {
						// Don't do anything for duplicate instructions any more.
						Log.debug("Cancelling generation of duplicate create instruction");
						return null;
						/*
						Log.debug("Marking 'Create' instruction as duplicate");
						
						instruction.getDataApplied
						 (InstructionData.Fields.INSTRUCTION_COMMENTS)
						 .setStringValue(duplicateComment);
						
						instruction.setStatus(InstructionStatus.DUPLICATE);
						*/
					}
				}
			}	
		} else {
			// Existing Aurora entity - we need an Update instruction.
			InstructionType instrType = null;
			
			if  (sdu.getUpdateInstrTypeId()!=null) {
				instrType = InstructionType.getIdCache().getCachedInstance
				 (sdu.getUpdateInstrTypeId());
			} else {
				Log.warn("StaticDataUpdate doesn't have an instruction type id for "
				 + "update, Id:"+sdu.getId());
			}
			
			if (instrType!=null) {
				if (updatedFieldGroups == null || updatedFieldGroups.isEmpty()) {
					Log.info("No Field Groups for this entity have been recently " +
					 "updated. Skipping generation of " + instrType.getName());
				} else {
					// Generate the Update instruction
					instruction = InstructionUtils.generateInstruction(
						sourceInstr, 
						instrType, 
						InstructionStatus.PENDING_STATIC_DATA_AUTHORISATION, 
						true, 
						Globals.Users.System, 
						facade
					);

					dataApplied = getAppliedData
					 (sourceInstr, instruction, company, 
					 extraDataValues, updatedFieldGroups, facade);
					
					instruction.setDataApplied(dataApplied);
					
					// Check for duplicate Update instruction.
					// No need for this check now - its done prior to calling this method.
					//Integer dupId = InstructionUtils.getDuplicateSDUInstructionId
					// (instruction, facade);
					
					/*
					if (dupId != null) {
						// Duplicate found!
						Log.debug
						 ("Duplicate 'Update' instruction found for Aurora entity.");
						
						// Used as the Comment data field value for this instruction.
						String duplicateComment = 
						 "Duplicate of update instruction "+dupId;
						
						Log.debug("Marking 'Update' instruction as duplicate");
						
						instruction.getDataApplied
						 (InstructionData.Fields.INSTRUCTION_COMMENTS)
						 .setStringValue(duplicateComment);
						
						instruction.setStatus(InstructionStatus.DUPLICATE);
					}
					*/
				}
			}
		}
		
		if (instruction != null) {
			// Copy over priority from source instr.
			instruction.setPriority(sourceInstr.getPriority());
			
			// Validate applied data.
			InstructionUtils.validateAppliedData(instruction, dataApplied);
		}
		
		return instruction;
	}
	
	/** Builds the list of Applied Data Values for the given SDU Instruction destInstr,
	 *  using the previously-applied data from sourceInstr as a basis.
	 *  
	 *  The extraDataValues parameter is used to pass in extra data values which may
	 *  not be present or different to those applied to the source instruction.
	 *  
	 *  The following data values are always set:
	 *  -COMPANY_ID: will match the passed Company's ID.
	 *  -PARENT_INSTRUCTION_ID: matches the source instruction's ID.
	 *  -ERROR_MESSAGE: always blanked out
	 *  
	 * @param sourceInstr
	 * @param destInstr
	 * @param company
	 * @param extraDataValues
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private static Vector<InstructionDataApplied> getAppliedData
	 (Instruction sourceInstr, Instruction destInstr, Company company, 
	 Vector<InstructionDataValue> extraDataValues, Vector<String> updatedFieldGroups,
	 FWFacade facade) 
	 throws DataException {
		
		Vector<InstructionDataApplied> destDataAppl;
		
		Vector<InstructionDataApplied> sourceDataAppl = sourceInstr.getDataApplied();
		
		destDataAppl = InstructionUtils.copyInstructionData
		 (sourceDataAppl, destInstr, null, true, facade);
		
		// Copy in extra data values.
		if (extraDataValues == null)
			extraDataValues = new Vector<InstructionDataValue>();
		
		// Append Parent Instruction ID field value
		InstructionData parentRefDataField = InstructionData.getCache()
		 .getCachedInstance(InstructionData.Fields.PARENT_INSTR_REF);
		
		extraDataValues.add(new InstructionDataValue
		 (parentRefDataField, sourceInstr.getInstructionId()));
		
		// Append Company ID field value
		InstructionData companyIdDataField = InstructionData.getCache()
		 .getCachedInstance(InstructionData.Fields.COMPANY_ID);
		
		extraDataValues.add(new InstructionDataValue
		 (companyIdDataField, company.getCompanyId()));
		
		// Append/overwrite Error message value (make sure some error message from
		// the source instruction doesn't get copied over)
		InstructionData errorMessageField = InstructionData.getCache()
		 .getCachedInstance(InstructionData.Fields.ERROR_MESSAGE);
		
		extraDataValues.add(new InstructionDataValue(errorMessageField));
		
		// Append SDU Field Groups list, if present.
		InstructionData sduFieldGroupsField = InstructionData.getCache()
		 .getCachedInstance(InstructionData.Fields.SDU_FIELD_GROUPS);
		
		String fieldGroupsList = null;
		
		if (updatedFieldGroups != null)
			fieldGroupsList = CCLAUtils.getCSVFromStringList(updatedFieldGroups, true);
		
		extraDataValues.add(new InstructionDataValue
		 (sduFieldGroupsField, fieldGroupsList));
		
		InstructionUtils.appendDataApplied
		 (destDataAppl, InstructionDataValue.convertToAppliedData
		 (destInstr, extraDataValues), true);
		
		Log.debug("GetAppliedData for Instruction Type: " + 
		 destInstr.getType().getName());
		
		Log.debug("Returning " + destDataAppl.size() + " applied data fields: ");
		
		StringBuffer s = new StringBuffer();
		
		for (InstructionDataApplied dataAppl : destDataAppl) {
			s.append("\n" + dataAppl.getInstructionData().getName() + "=" 
			 + dataAppl.getDataFieldValue().getRawValue());
		}
		
		Log.debug(s.toString());
		
		return destDataAppl;
	}

	/**
	 * Gets the suspend message unique to the module;
	 * @return
	 */
	public String getSuspendMessage() {
		return "Unable to create static data update instructions, check the logs for more details";
	}	
}
