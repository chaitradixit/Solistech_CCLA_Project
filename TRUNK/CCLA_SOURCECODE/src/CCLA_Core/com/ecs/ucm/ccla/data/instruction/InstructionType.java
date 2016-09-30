package com.ecs.ucm.ccla.data.instruction;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_INSTRUCTION_TYPE table.
 * 
 * 
 * @author Tom
 *
 */
public class InstructionType implements Persistable {
	
	//add any new instructions type id here....
	public static class Ids {
		public static final int AMLCHASER 		= 1;
		public static final int AMLLETTER 		= 2;
		public static final int APP 			= 3;
		public static final int APPSHORT 		= 4;
		public static final int APPWTH 			= 5;
		public static final int AUDCERT 		= 6;
		public static final int AUTOAPP 		= 7;		
		public static final int AUTOMAND 		= 8;
		public static final int BANKCERT 		= 9;
		public static final int BANKST 			= 10;
		public static final int BANKSLP 		= 11;
		public static final int BREACH 			= 12;
		public static final int BSOC 			= 13;
		public static final int BUYBNK 			= 14;
		public static final int BUYCHQ 			= 15;
		public static final int BUYCHQNONBANK 	= 16;
		public static final int BUYCHQTP 		= 17;
		public static final int BUYDF 			= 18;
		public static final int BUYSHX 			= 19;
		public static final int BUYSHXSALE 		= 20;
		public static final int BUYU 			= 21;
		public static final int CANBUYCHQ 		= 22;
		public static final int CANDEPBNK 		= 23;
		public static final int CANDEPCHQ 		= 24;
		public static final int CANORWTHBACS 	= 25;
		public static final int CANORWTHCHPS 	= 26;
		public static final int CANWTHBACS 		= 27;
		public static final int	CANWTHCHPS		= 28;
		public static final int	CANWTHCHQ		= 29;
		public static final int	CASMOVNONDAR	= 30;
		public static final int	CERTINC			= 31;
		public static final int	CHECK			= 32;
		public static final int	CHECKLIST		= 33;
		public static final int	CHQCL			= 34;
		public static final int	CLIENTCONF		= 35;
		public static final int	CLIENTQUERY		= 36;
		public static final int	CLOSEACS		= 37;
		public static final int	COMPAYABLE		= 38;
		public static final int	COMPL			= 39;
		public static final int	CONCORR			= 40;
		public static final int	CONDINS			= 41;
		public static final int	CONTRACTS		= 42;
		public static final int	CORRECTINGADJ	= 43;
		public static final int	CRMCORR			= 44;
		public static final int	CSHMOVNONDAR	= 45;
		public static final int	CSLETTERS		= 46;
		public static final int	DBFTXN			= 47;
		public static final int	DCERT			= 48;
		public static final int	DEPBNK			= 49;
		public static final int	DEPCHQ			= 50;
		public static final int	DEPCHQNONBANK	= 51;
		public static final int	DEPCHQTP		= 52;
		public static final int	DIODEP			= 53;
		public static final int	DIOLOANS		= 54;
		public static final int	DIOWITH			= 55;
		public static final int	DIRECTORY		= 56;
		public static final int	EMAILINDEMNITY	= 57;
		public static final int	ENT				= 58;
		public static final int	ERROR			= 59;
		public static final int	FUNDSPLIT		= 60;
		public static final int	GEN				= 61;
		public static final int	INTCHQ			= 62;
		public static final int	INTERTRANS		= 63;
		public static final int	INV				= 64;
		public static final int	IVS				= 65;
		public static final int	LAMAPP			= 66;
		public static final int	LAMLETT			= 67;
		public static final int	LLPMAND			= 68;
		public static final int	LOOKUP			= 69;
		public static final int	LTR2			= 70;
		public static final int	MAND			= 71;
		public static final int	MANDSHORT		= 72;
		public static final int	MCERT			= 73;
		public static final int	MINS			= 74;
		public static final int	MKTCORR			= 75;
		public static final int	MMCONFIRM		= 76;
		public static final int	ORSALEBACS		= 77;
		public static final int	ORSALECHQ		= 78;
		public static final int	ORWTHBACS		= 79;
		public static final int	ORWTHCHPS		= 80;
		public static final int	ORWTHCHQ		= 81;
		public static final int	OTHERS			= 82;
		public static final int	PASSP		 	= 83;
		public static final int	PREADVICE		= 84;
		public static final int	PREFEMAIL		= 85;
		public static final int	PROPCORR		= 86;
		public static final int	PURORD			= 87;
		public static final int	REJINT			= 88;
		public static final int	REJWTH			= 89;
		public static final int	REPACC			= 90;
		public static final int	RETCHQ			= 91;
		public static final int	RETMAIL			= 92;
		public static final int	RETURNEDDIV		= 93;
		public static final int	RNA				= 94;
		public static final int	SALEBACS		= 95;
		public static final int	SALECHPS		= 96;
		public static final int	SALECHQ			= 97;
		public static final int	SEGAPP			= 98;
		public static final int	SEGMAND			= 99;
		public static final int	SIGLIST			= 100;
		public static final int	STATORD			= 101;
		public static final int	STOCKTF			= 102;
		public static final int	STORD			= 103;
		public static final int	TOPANEL		 	= 104;
		public static final int	TRANSOUT		= 105;
		public static final int	WTHBACS			= 106;
		public static final int	WTHCHPS			= 107;
		public static final int	WTHCHQ			= 108;
		public static final int	CLIMONCONF		= 109;
		public static final int	ORSALEIAT		= 110;
		public static final int	SEEDFUND		= 111;
		public static final int	TMS				= 112;
		public static final int	DICONDIN		= 113;
		public static final int	CSMAND			= 114;
		public static final int	INVHIST			= 115;
		public static final int	REINDIV			= 116;
		public static final int	DIOAPP			= 117;
		public static final int	DIOWTHCHPS		= 118;
		public static final int	DIOWTHBACS		= 119;
		public static final int	DIOCAPREPAY		= 120;
		public static final int	DIOPREADVICE	= 121;
		public static final int	DIODEPBNK		= 122;
		public static final int BNKCONDIN		= 123;
		public static final int IAT				= 124;
		public static final int RETFUNDS		= 125;
		
		
		public static final int CREATE_CLIENT			= 126;
		public static final int UPDATE_CLIENT			= 127;
		public static final int CREATE_CORRESPONDENT	= 128;
		public static final int UPDATE_CORRESPONDENT	= 129;
		public static final int CREATE_ACCOUNT			= 130;
		public static final int UPDATE_ACCOUNT			= 131;
		
		//add any new instructions type id here....
	}
	
	private int instructionTypeId;
	private String name;
	private String description;
	
	private TransactionType transactionType;
	
	private boolean financialTransaction;
	private boolean submitToSpp;
	private boolean requireSignatures;
	private boolean supporting;
	
	private Date dateAdded;
	private Date lastUpdated;
	
	private int settlementOffset = 0;
	private boolean isStaticDataUpdate;
	
	public InstructionType(int instructionTypeId, String instructionTypeName, 
	 TransactionType transactionType, String instructionDescription, 
	 boolean isFinancialTransaction, boolean submitToSpp, boolean requireSignatures,
	 boolean isSupporting, Date dateAdded, Date dateUpdated, int settlementOffset, boolean isStaticDataUpdate) {
		this.instructionTypeId = instructionTypeId;
		this.name = instructionTypeName;
		this.setTransactionType(transactionType);
		this.description = instructionDescription;
		this.financialTransaction = isFinancialTransaction;
		this.submitToSpp = submitToSpp;
		this.requireSignatures = requireSignatures;
		this.supporting = isSupporting;
		this.dateAdded = dateAdded;
		this.lastUpdated = dateUpdated;
		this.settlementOffset = settlementOffset;
		this.isStaticDataUpdate = isStaticDataUpdate;
	}

	public String toString() {
		return "InstructionType[name=" + this.getName() + "]";
	}
	
	public InstructionType get(int instructionTypeId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(instructionTypeId, facade);
		return get(rs);
	}
	
	public static InstructionType get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;

		Integer tranTypeId = DataResultSetUtils.getResultSetIntegerValue
		 (rs, "TRANSACTION_TYPE_ID");
		
		TransactionType transactionType = null;
		
		if (tranTypeId != null)
			transactionType = TransactionType.getCache().getCachedInstance(tranTypeId);
		
		return new InstructionType(
			DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_TYPE_ID"),
			DataResultSetUtils.getResultSetStringValue(rs, "INSTRUCTION_TYPE_NAME"),
			transactionType,
			DataResultSetUtils.getResultSetStringValue(rs, "INSTRUCTION_DESCRIPTION"),
			DataResultSetUtils.getResultSetBoolValue(rs, "IS_FINANCIAL_TRANSACTION"),
			DataResultSetUtils.getResultSetBoolValue(rs, "SUBMIT_TO_SPP"),
			DataResultSetUtils.getResultSetBoolValue(rs, "REQUIRES_SIGNATURES"),
			DataResultSetUtils.getResultSetBoolValue(rs, "IS_SUPPORTING"),
			
			rs.getDateValueByName("DATE_ADDED"),
			rs.getDateValueByName("DATE_UPDATED"),
			DataResultSetUtils.getResultSetIntegerValue(rs,"SETTLEMENT_OFFSET"),
			DataResultSetUtils.getResultSetBoolValue(rs,"IS_STATIC_DATA_UPDATE")
			
		);
	}
	
	/** Fetch all InstructionType instances from the database.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<InstructionType> getAll(FWFacade facade) throws DataException {
		
		DataResultSet rs = facade.createResultSet("qCore_GetAllInstructionTypes", 
		 new DataBinder());
		
		Vector<InstructionType> instrTypes = new Vector<InstructionType>();
		
		if (rs.first()) {
			do {
				instrTypes.add(get(rs));
			} while (rs.next());
		}
		
		return instrTypes;
	}
	
	public static DataResultSet getData(int instructionId, FWFacade facade)
	 throws DataException {
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "INSTRUCTION_TYPE_ID", instructionId);
		DataResultSet rsComm = facade.createResultSet("", binder);
		return rsComm;
	}
	
	/** Fetches applicable instruction data fields for this instruction type, using the
	 *  cache implementor.
	 *  
	 * @return null if there are no mapped fields.
	 * @throws DataException 
	 */
	public Vector<ApplicableInstructionData> getApplicableInstructionData() 
	 throws DataException {
		
		return ApplicableInstructionData.getInstructionTypeCache().getCachedInstance
		 (this.getInstructionTypeId());
	}
	
	/** Fetches a particular applicable instruction data field for this instruction 
	 *  type, using the cache implementor.
	 *  
	 * @return null if the given field isn't mapped to this Instruction Type.
	 * @throws DataException 
	 */
	public ApplicableInstructionData getApplicableInstructionData
	 (InstructionData instrData) throws DataException {
		
		Vector<ApplicableInstructionData> applInstrDataFields =
		 ApplicableInstructionData.getInstructionTypeCache().getCachedInstance
		 (this.getInstructionTypeId());
		
		for (ApplicableInstructionData applInstrData: applInstrDataFields) {
			if (instrData.equals(applInstrData.getInstructionData())) {
				return applInstrData;
			}
		}
		
		return null;
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

	public int getInstructionTypeId() {
		return instructionTypeId;
	}

	public void setInstructionTypeId(int instructionTypeId) {
		this.instructionTypeId = instructionTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstructionDescription() {
		return description;
	}

	public void setInstructionDescription(String instructionDescription) {
		this.description = instructionDescription;
	}

	public boolean isFinancialTransaction() {
		return financialTransaction;
	}

	public void setFinancialTransaction(boolean isFinancialTransaction) {
		this.financialTransaction = isFinancialTransaction;
	}

	public boolean isSubmitToSpp() {
		return submitToSpp;
	}

	public void setSubmitToSpp(boolean submitToSpp) {
		this.submitToSpp = submitToSpp;
	}

	public boolean isRequireSignatures() {
		return requireSignatures;
	}

	public void setRequireSignatures(boolean requireSignatures) {
		this.requireSignatures = requireSignatures;
	}

	public boolean isSupporting() {
		return supporting;
	}

	public void setSupporting(boolean isSupporting) {
		this.supporting = isSupporting;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getDateUpdated() {
		return lastUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.lastUpdated = dateUpdated;
	}
	
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}
	
	public int getSettlementOffset() {
		return settlementOffset;
	}
	
	public void setSettlementOffset(int settlementOffset) {
		this.settlementOffset = settlementOffset;
	}
	
	public boolean isStaticDataUpdate() {
		return isStaticDataUpdate;
	}

	public void setStaticDataUpdate(boolean isStaticDataUpdate) {
		this.isStaticDataUpdate = isStaticDataUpdate;
	}	
	
	public boolean equals(InstructionType type) {
		return this.getInstructionTypeId() == type.getInstructionTypeId();
	}
	
	/*
	 *  Caching stuff
	 */
	private static IdCache ID_CACHE = new IdCache();
	
	public static Cachable<Integer, InstructionType> getIdCache() {
		return ID_CACHE;
	}
	

	/** InstructionType cache implementor.
	 *  
	 *  Maps Instruction Type IDs againsts Instruction Type instances.
	 *  
	 *  */
	private static class IdCache extends Cachable<Integer, InstructionType> {

		public IdCache() {
			super("Instruction Type ID");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionType> instrTypes = InstructionType.getAll(facade);
			
			HashMap<Integer, InstructionType> newCache = 
			 new HashMap<Integer, InstructionType>();
			
			for (InstructionType instrType : instrTypes) {
				newCache.put(instrType.getInstructionTypeId(), instrType);
			}
			
			this.CACHE_MAP = newCache;
		}
	}

	private static NameCache NAME_CACHE = new NameCache();
	
	public static Cachable<String, InstructionType> getNameCache() {
		return NAME_CACHE;
	}
	
	/** InstructionType cache implementor.
	 *  
	 *  Maps Instruction Type Names againsts Instruction Type instances.
	 *  
	 *  */
	private static class NameCache extends Cachable<String, InstructionType> {

		public NameCache() {
			super("Instruction Type Name");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionType> instrTypes = InstructionType.getAll(facade);
			
			HashMap<String, InstructionType> newCache = 
			 new HashMap<String, InstructionType>();
			
			for (InstructionType instrType : instrTypes) {
				newCache.put(instrType.getName(), instrType);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
