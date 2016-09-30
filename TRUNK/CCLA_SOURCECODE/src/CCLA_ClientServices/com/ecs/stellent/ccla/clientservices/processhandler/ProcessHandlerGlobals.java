package com.ecs.stellent.ccla.clientservices.processhandler;

public class ProcessHandlerGlobals  {

	// **** PSDF GLOBALS ****//
	
	public static class Statuses {
		public static final String ENROLLED 			= "Enrolled";
		public static final String PENDING 			= "Pending";
		public static final String INTERESTED 		= "Interested";
		public static final String NOT_INTERESTED 	= "Not interested";
		public static final String UNDECIDED 		= "Undecided";
		public static final String COMPLETED 		= "Completed";
		public static final String ON_HOLD			= "On hold";
		
		public static final String EXCLUDED 		= "Excluded";
		
		public static final String FORMS_GENERATED 	= "Forms generated";
		public static final String FORMS_RETURNED 	= "Forms returned";
		
		public static final String ACCOUNTS_CREATED	= "Account template(s) created";
	}
	
	public static class Actions {
		
		public static final String ENROLL			= "Enroll";
		public static final String EXCLUDE			= "Exclude";
		
		public static final String MARK_AS_INTERESTED 		= "Mark as Interested";
		public static final String MARK_AS_UNDECIDED		= "Mark as Undecided";
		public static final String MARK_AS_NOT_INTERESTED 	= "Mark as Not Interested";
		public static final String MARK_AS_COMPLETED 		= "Mark as Completed";
		public static final String MARK_AS_ON_HOLD			= "Mark as On Hold";
		
		// Form actions
		public static final String GEN_FORMS 		= "Generate forms";
		public static final String REGEN_FORMS 		= "Regenerate forms";
		public static final String PRINT_FORMS 		= "Print forms";
		public static final String REPRINT_FORMS 	= "Reprint forms";
		
		public static final String GEN_AND_PRINT_FORMS = "Generate and print forms";
		public static final String REGEN_AND_PRINT_FORMS = "Regenerate and print forms";
		
		public static final String FORMS_RETURNED	= "Forms returned";
		
		// Account actions
		public static final String CREATE_ACCOUNTS		= "Create account template(s)";
	}
	
	public static class Activities {
		public static final String STATUS_UPDATED	= "Status Update";
		
		public static final String EXCLUSION 		= "Exclusion";
		public static final String ENROLLMENT 		= "Enrolment";
		
		// Account intentions
		public static final String INTENTION_ADD 	= "Add Intention";
		public static final String INTENTION_UPDATE = "Update Intention";
		public static final String INTENTION_REMOVE = "Remove Intention";
		
		// Account creations
		public static final String ACCOUNT_CREATE 	= "Account Template Creation";
	}
	
	public static class ActivityDescriptions {
		public static final String CLIENT_EXCLUDED	= "Client excluded from campaign";
		public static final String CLIENT_ENROLLED	= "Client enrolled to campaign";
		
		public static final String STATUS_UPDATED_PREFIX = "Status updated to ";
	}
	
	public static final String CCLA_INTERESTED_SHORT_LABEL = "Interested";
	public static final String CCLA_INTERESTED_ACTIVITY_TYPE_PSDF = "Client interested";
	public static final String CCLA_INTERESTED_ACTIVITY_DESCRIPTION_PSDF = "Status changed to 'Interested' for PSDF";

	public static final String CCLA_NOT_INTERESTED_SHORT_LABEL = "Not interested";
	public static final String CCLA_NOT_INTERESTED_ACTIVITY_TYPE_PSDF = "Client not interested";
	public static final String CCLA_NOT_INTERESTED_ACTIVITY_DESCRIPTION_PSDF = "Status changed to 'Not interested' for PSDF";
	
	public static final String CCLA_UNDECIDED_SHORT_LABEL = "Undecided";
	public static final String CCLA_UNDECIDED_ACTIVITY_TYPE_PSDF = "Client undecided";
	public static final String CCLA_UNDECIDED_ACTIVITY_DESCRIPTION_PSDF = "Status changed to 'Undecided' for PSDF";

	public static final String CCLA_ENROLL_SHORT_LABEL = "Enroll";
	public static final String CCLA_ENROLL_ACTIVITY_TYPE_PSDF = "Enrollment";
	public static final String CCLA_ENROLL_ACTIVITY_DESCRIPTION_PSDF = "Client enrolled to PSDF";
	
	public static final String CCLA_PRINT_FORMS_SHORT_LABEL = "Create and print form";
	public static final String CCLA_PRINT_FORMS_ACTIVITY_TYPE_PSDF = "Form printed";
	public static final String CCLA_PRINT_FORMS_ACTIVITY_DESCRIPTION_PSDF = "PSDF forms printed for client";
		
	public static final String CCLA_REPRINT_FORMS_SHORT_LABEL = "Reprint forms";
	public static final String CCLA_REPRINT_FORMS_ACTIVITY_TYPE = "Forms printed";
	public static final String CCLA_REPRINT_FORMS_ACTIVITY_DESCRIPTION_PSDF = "PSDF forms reprinted for client";
		
	public static final String CCLA_EXCLUDE_SHORT_LABEL = "Exclude";
	public static final String CCLA_EXCLUDE_ACTIVITY_TYPE = "Exclusion";
	public static final String CCLA_EXCLUDE_ACTIVITY_DESCRIPTION_PSDF = "Client excluded from PSDF";
	
	public static final String CCLA_INTENTION_ADD_ACTIVITY_TYPE = "Intention Added";
	public static final String CCLA_INTENTION_ADD_ACTIVITY_DESCRIPTION = "Intention Added:";
	
	public static final String CCLA_INTENTION_UPDATE_ACTIVITY_TYPE = "Intention Update";
	public static final String CCLA_INTENTION_UPDATE_ACTIVITY_DESCRIPTION = "Intention updated:";
	
	public static final String CCLA_INTENTION_DELETE_ACTIVITY_TYPE = "Intention Removal";
	public static final String CCLA_INTENTION_DELETE_ACTIVITY_DESCRIPTION = "Intention removed";
		

	
}
