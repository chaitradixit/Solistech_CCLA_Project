package com.ecs.ucm.ccla;

import intradoc.shared.SharedObjects;

/** Global string variables.
 * 
 * @author Tom
 *
 */
public class Globals {
	
	public static final class Users {
		/** Username for auditing system actions, when no particular username is
		 *  relevant/available.
		 */
		public static final String System = "System";
		
		/** Username tagged to the 'Scan User' field on documents originating from the
		 *  DripFeed uploader tool.
		 */
		public static final String DripFeed = "DripFeed";
	}

	/** Externalization of UCM DocTypes */
	public static class UCMDocTypes {
		public static final String Document 			= "Document";
		public static final String ChildDocument 		= "ChildDocument";
	}
	
	/** Externalization of UCM metadata field names. */
	public static class UCMFieldNames {
		public static final String DocType				= "dDocType";
		public static final String DocName				= "dDocName";
		public static final String DocTitle				= "dDocTitle";
		public static final String DocID				= "dID";
		public static final String DocInDate			= "dInDate";
		public static final String DocUrl				= "DocUrl";
		public static final String SecurityGroup		= "dSecurityGroup";
		public static final String DocAccount			= "dDocAccount";
		public static final String DocAuthor			= "dDocAuthor";
		public static final String Company				= "xCompany";
		public static final String Source				= "xSource";
		public static final String ScanUser 			= "xScanUser";
		public static final String Fund					= "xFund";
		public static final String AccountNumber 		= "xAccountNumber";
		public static final String ClientNumber			= "xClientNumber";
		public static final String ClientName			= "xClientName";
		public static final String DocClass				= "xDocumentClass";
		public static final String DocumentDate			= "xDocumentDate";
		public static final String ProcessDate 			= "xProcessDate";
		public static final String OriginalProcessDate 	= "xOriginalProcessDate";
		public static final String ParentDocName		= "xParentDocName";
		public static final String PdfDocName			= "xPdfDocName";
		public static final String DependantDocName 	= "xDependantDocName";
		public static final String WithInstruction		= "xWithInstruction";
		public static final String BatchNumber			= "xBatchNumber";
		public static final String BundleRef			= "xBundleRef";
		public static final String Status				= "xStatus";
		public static final String Amount				= "xAmount";
		public static final String Units				= "xUnits";
		public static final String MultiDoc				= "xMultiDoc";
		public static final String Comments				= "xComments";
		public static final String PaymentRef			= "xPaymentRef";
		public static final String SignaturesValid		= "xSignaturesValid";
		public static final String DocumentAuthor		= "xDocumentAuthor";
		
		public static final String OrgAccountCode		= "xOrgAccountCode";
		public static final String DestinationOrgAccountCode 
		 = "xDestinationOrgAccountCode";
		
		public static final String FormID				= "xFormId";
		public static final String JobID				= "xJobId";
		
		public static final String BankAccountNumber	= "xBankAccountNumber";
		public static final String SortCode				= "xSortCode";

		public static final String DestinationAccount	= "xDestinationAccount";
		public static final String DestinationFund		= "xDestinationFund";
		
		public static final String WorkflowDate			= "xWorkflowDate";
		public static final String TransactionRef 		= "xTransactionRef";
		
		public static final String NominatedContactPoint = "xNominatedContactPoint";
		
		public static final String PartitionId			= "xPartitionId";
		public static final String FolderName			= "xFolderName";
		public static final String StorageRule			= "xStorageRule";
		
		public static final String RevisionId			= "dRevisionId";
		
		// Not actual DocMeta fields - these assist when building SPP job variable
		// mappings.
		public static final String InstructionId		= "instructionId";
	}
	
	/** Maximum number of Instructions fetched on Org/Account/etc. screens.
	 * 
	 */
	public static final int INSTRUCTIONS_MAX_RESULTS = 
	 SharedObjects.getEnvironmentInt("CCLA_CS_Instructions_MaxResults", 15);
	
	
	//Globals key values for SystemConfigVar
	public static final String VALIDATE_AURORA_ACCOUNTS = "ForceValidateAuroraAccount";
	public static final String CAP_INCOME_BANK_ACCOUNT_NUMBER = "CapIncomeAccountNumber";
	public static final String CAP_INCOME_BANK_ACCOUNT_SORTCODE = "CapIncomeSortCode";
	
}
