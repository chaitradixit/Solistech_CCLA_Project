package com.ecs.ucm.ccla.data.subscription;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementAttributeType;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.EnhancedPersistable;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.TransactionType;
import com.ecs.ucm.ccla.data.product.Product;
import com.ecs.ucm.ccla.data.product.Product.Ids;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the SUBSCRIPTION table.
 * 
 *  BEWARE: you should always use the pooled DB connector to make queries against this
 *  table, as there is a core UCM table called Subscription.
 * 
 * @author Tom
 *
 */
public class Subscription extends EnhancedPersistable {
	
	private int accountId;
	private BigDecimal subscriptionAmount;
	private Date subscriptionDate;
	private String paymentRef;
	private Integer statusId;
	private Date dateFormReceived;
	
	/** Process date of the latest cash payment */
	private Date dateLatestCashProcessed;
	/** Completion date, i.e. process date of the final cash payment */
	private Date dateCompleted;
	
	private Integer productId;
	private Date startDate;
	private Date endDate;
	
	private boolean clientConfirmed;
	
	/** Used to detect a Payment Ref code in transaction narratives etc. */
	public static final Pattern CONTRIBUTION_PAYMENT_REF_PATTERN = 
	 Pattern.compile("([A-Z]{4}\\d{8})(-)\\d{5}");
	
	public static class Cols {
		public static final String ID = "SUBSCRIPTION_ID";
		public static final String SUBSCRIPTION_AMOUNT = "SUBSCRIPTION_AMOUNT";
		public static final String SUBSCRIPTION_DATE = "SUBSCRIPTION_DATE";
		public static final String SUBSCRIPTION_PAYMENT_REF = "SUBSCRIPTION_PAYMENT_REF";
		public static final String STATUS = "SUBSCRIPTION_STATUS_ID";
		public static final String DATE_FORM_RECEIVED = "DATE_FORM_RECEIVED";
		public static final String DATE_LATEST_CASH_PROCESSED = "DATE_LATEST_CASH_PROCESSED";
		public static final String DATE_COMPLETED = "DATE_COMPLETED";
		public static final String CLIENT_CONFIRMED = "CLIENT_CONFIRMED";
	}
	
	public static class Queries {
		public static final String ADD = "qCore_AddSubscription";
		public static final String UPDATE = "qCore_UpdateSubscription";
		public static final String GET = "qCore_GetSubscription";
		public static final String REMOVE = "qCore_RemoveSubscription";
		
		public static final String GET_BY_PAYMENT_REF = 
		 "qCore_GetSubscriptionByPaymentRef";
		public static final String GET_ALL_BY_ACCOUNT_ID =
		 "qCore_GetAllSubscriptionsByAccountId";
	}
	
	/** Wrapper class for storing the payment status of a given Subscription.
	 *  
	 * @author Tom
	 */
	public static class SubscriptionPaymentStatus {
		private final Subscription subscription;
		private final Form confirmationForm;
		private final BigDecimal totalPaymentAmount;
		private final BigDecimal totalInvestmentAmount;
		private final BigDecimal totalGovMatchAmount;
		private final PaymentStatus paymentStatus;
		private final String message;
		
		public SubscriptionPaymentStatus(Subscription subscription,
				Form confirmationForm, BigDecimal totalPaymentAmount,
				BigDecimal totalInvestmentAmount, BigDecimal totalGovMatchAmount,
				PaymentStatus paymentStatus, String message) {
			this.subscription = subscription;
			this.confirmationForm = confirmationForm;
			this.totalPaymentAmount = totalPaymentAmount;
			this.totalInvestmentAmount = totalInvestmentAmount;
			this.totalGovMatchAmount = totalGovMatchAmount;
			this.paymentStatus = paymentStatus;
			this.message = message;
		}

		public Subscription getSubscription() {
			return subscription;
		}

		public Form getConfirmationForm() {
			return confirmationForm;
		}

		public BigDecimal getTotalPaymentAmount() {
			return totalPaymentAmount;
		}

		public String getPaymentStatusString() {
			return paymentStatus.toString();
		}

		public String getMessage() {
			return message;
		}

		public BigDecimal getTotalInvestmentAmount() {
			return totalInvestmentAmount;
		}

		public BigDecimal getTotalGovMatchAmount() {
			return totalGovMatchAmount;
		}
	}

	enum PaymentStatus {
		RED,
		AMBER,
		GREEN
	}
	
	/** Determines the payment/returned form status of the given Subscription,
	 *  taking into account a 'latest payment amount' (can be null/zero), which refers to
	 *  the amount on the document/instruction in context.
	 *  
	 *  This yield a SubscriptionPaymentStatus wrapper object, that contains the
	 *  Subscription, Form, total cash payment amount plus a traffic light status value
	 *  and message to display the reason for the derived status.
	 *  
	 * @param subscription
	 * @param latestPaymentAmount deposit amount in context. Will always be null for
	 * 							  non-deposits.
	 * @param docGUID if present, any previous payment instruction with this GUID will
	 * 				  be excluded from the 'previous count' to avoid double counting.
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static SubscriptionPaymentStatus getSubscriptionPaymentStatus
	 (Subscription subscription, 
	 BigDecimal latestPaymentAmount, 
	 String docGUID, FWFacade facade) 
	 throws DataException, ServiceException {
		
		if (subscription == null) {
			throw new ServiceException("Unable to determine subscription status - " + 
			 "Subscription was null");
		}
		
		Log.debug("Calculating Subscription Payment Status for Subscription ID: " +
		 subscription.getId() + ", Payment Ref: " + subscription.getPaymentRef() + 
		 ", latest payment amount: " + 
		 (latestPaymentAmount != null ? latestPaymentAmount.toPlainString() : "null") 
		 + ", docGUID: " + docGUID);
		
		PaymentStatus paymentStatus = null;
		String paymentRef = subscription.getPaymentRef();
		String msg = "";
		
		// Next few chunks of code will set the flags below, and add an explanation
		// to the msg String where appropriate
		boolean validSubscriptionStatus = true;
		boolean matchingTotalPaymentAmount = false;
		boolean acceptableFormStatus = false;
		boolean formReturnedAndValidated = false;
		
		// First check for invalid Subscription statuses.
		if (subscription.getStatusId() 
			== Subscription.SubscriptionStatusIds.CANCELLED) {
			msg =
			 "Subscription is in invalid state for receiving payment (Cancelled)";
			validSubscriptionStatus = false;
		} else if (subscription.getStatusId()
			== Subscription.SubscriptionStatusIds.NEW) {
			msg =
			 "Subscription is in invalid state for receiving payment (New)";
			validSubscriptionStatus = false;
		} else if (subscription.getStatusId()
			== Subscription.SubscriptionStatusIds.COMPLETED) {
			msg =
			 "Subscription is in invalid state for receiving payment (Completed)";
			validSubscriptionStatus = false;
		}
		
		BigDecimal totalPaymentAmount = BigDecimal.ZERO;
		
		if (latestPaymentAmount != null)
			totalPaymentAmount = latestPaymentAmount;
		
		// Subscription is in valid state for receiving payment.
		// Add up all deposits, investments and gov matches for this Subscription.
		
		// Total cash value of all deposits.
		BigDecimal totalPreviousPayments = BigDecimal.ZERO;
		
		// Total cash value of all donation investments
		BigDecimal totalPreviousInvestments = BigDecimal.ZERO;
		
		// Total cash value of all government match payments.
		BigDecimal totalPreviousGovMatches = BigDecimal.ZERO;
		
		// Fetch all instructions with a matching Payment Ref
		Vector<Instruction> instrsWithPaymentRef = Instruction
		 .getInstructionsByAppliedStringValue
		 (InstructionData.Fields.NARRATIVE, paymentRef, facade);
		
		Log.debug("Found " + instrsWithPaymentRef.size() + " instructions with " +
		 "Payment Ref set");
		
		for (Instruction instr : instrsWithPaymentRef) {
			boolean isTerminated = 
			 instr.getStatus().getInstructionStatusId() 
			 ==
			 InstructionStatus.StatusID.TERMINATED;
			
			boolean isCurrentDoc = 
			 (docGUID != null 
			 && instr.getInstructionDocGuid() != null
			 && docGUID.equals(instr.getInstructionDocGuid()));
			
			TransactionType transType = instr.getType().getTransactionType();
			
			Log.debug("Instruction ID " + instr.getInstructionId() + ": " +
			 "isTerminated? " + isTerminated + ", isCurrentDoc? " + isCurrentDoc);

			// Ensure this Instruction is a Transaction, and isn't terminated.
			if(transType != null && !isTerminated) {
				
				// All transactions should have cash/unit value set.
				InstructionDataApplied instrCashDataValue = 
				 InstructionDataApplied.getDataApplied
				 (instr.getInstructionId(), InstructionData.Fields.CASH, facade);
				
				// All transactions should have source Account ID set.
				InstructionDataApplied instrAccountId = 
				 InstructionDataApplied.getDataApplied
				 (instr.getInstructionId(), InstructionData.Fields.SOURCE_ACCOUNT_ID, 
				 facade);
				
				BigDecimal thisCashAmount = null;
				
				if (instrCashDataValue != null) {
					thisCashAmount = instrCashDataValue
					 .getDataFieldValue().getBigDecimalValue();
				}
				
				Integer sourceAccountId = null;
				
				if (instrAccountId != null)
					sourceAccountId = instrAccountId.getDataFieldValue().getIntValue();
				
				if (transType.equals(TransactionType.BUY) && !isCurrentDoc) {
					// Buy/Deposit instruction.
					Log.debug("Found existing BUY instruction, Type=" 
					 + instr.getType().getName() + ", ID=" 
					 + instr.getInstructionId());
					
					if (thisCashAmount != null) {
						Log.debug("Found a cash deposit against instruction: " 
						 + thisCashAmount.toPlainString());
						
						totalPreviousPayments = 
						 totalPreviousPayments.add(thisCashAmount);
					}
					
				} else if (transType.equals(TransactionType.TRANSFER)) {
					// Transfer/investment instruction. Could be Gov Match payment!
					Log.debug("Found existing TRANSFER instruction, Type=" 
					 + instr.getType().getName() + ", ID=" 
					 + instr.getInstructionId());
		
					if (thisCashAmount != null) {
						Log.debug("Found a transfer against instruction: " 
						 + thisCashAmount.toPlainString());
						
						// Is this a donation investment, or government match?
						
						if (sourceAccountId != null) {
							Vector<ElementAttributeApplied> elemAttrsAppl = 
							 ElementAttributeApplied.getAll
							 (sourceAccountId, 
							 ElementAttributeType.getCache().getCachedInstance
							  (ElementAttributeType.MISC_ACCOUNT_DETAILS),
							 false, facade);
							
							if (!elemAttrsAppl.isEmpty()) {
								Log.debug("Found " + elemAttrsAppl.size() + 
								 " applied Element Attribute against " +
								 "source account!");
								for (ElementAttributeApplied elemAttr : elemAttrsAppl) {
									Log.debug("Attr ID: " + elemAttr.getAttributeId() + 
									 ", Value: " + elemAttr.getAttrValue());
								}
							}
							
							totalPreviousInvestments = 
							 totalPreviousInvestments.add(thisCashAmount);
							
						} else {
							Log.warn("Unable to find Account ID value against " +
							 "instruction! Can't determine transfer type");
						}
					}
					
				} else if (transType.equals(TransactionType.SELL)) {
					// Sell/Withdrawal instruction.
					Log.debug("Found existing SELL instruction, Type=" 
					 + instr.getType().getName() + ", ID=" 
					 + instr.getInstructionId());
					
					if (thisCashAmount != null) {
						Log.debug("Found a cash withdrawal against instruction: " 
						 + thisCashAmount.toPlainString());
						
						totalPreviousPayments = 
						 totalPreviousPayments.subtract(thisCashAmount);
					}
				}
				
			} else {
				Log.debug("Ignoring existing/current/terminated instruction, " +
				 "Type=" 
				 + instr.getType().getName() + ", ID=" 
				 + instr.getInstructionId());
			}
		}
			
		Log.debug("Total previous cash payments came to: " 
		 + totalPreviousPayments.toPlainString());
		
		Log.debug("Total previous cash investments came to: " 
		 + totalPreviousInvestments.toPlainString());
		
		Log.debug("Total previous gov matches came to: " 
		 + totalPreviousGovMatches.toPlainString());
		
		// Add on the payment amount in context.
		totalPaymentAmount = totalPaymentAmount.add(totalPreviousPayments);
		
		Log.debug("Including latest payment, total subscription payments: " 
		 + totalPaymentAmount.toPlainString());
		
		if (validSubscriptionStatus) {
			if (totalPaymentAmount.compareTo(BigDecimal.ZERO) == 0) {
				// Total payments match to zero. Likely event if we are fetching the
				// subscription status against the returned form, and no payments
				// have been received and validated yet!
				msg = "No payments received yet";
				
			} else {
			
				int compareResult = totalPaymentAmount.compareTo
				 (subscription.getSubscriptionAmount());
				
				BigDecimal diff = totalPaymentAmount.subtract
				 (subscription.getSubscriptionAmount()).abs();
				
				if (compareResult < 0) {
					// Total payment too low.
					msg = "Total payments are less than expected amount.";
					/*
					 " Previous payments: "  
					 + CCLAUtils.DECIMAL_FORMAT.format
					 (totalPaymentAmount) + ", expected: " + 
					 CCLAUtils.DECIMAL_FORMAT.format(subscription.getSubscriptionAmount()) +
					 ", difference: " + CCLAUtils.DECIMAL_FORMAT.format(diff);
					 */
					 Log.debug(msg);
					 
				} else if (compareResult > 0) {
					// Total payment too high.
					msg = "Total payment is more than expected amount.";
					
					/*
					" Previous payments: "  
					 + CCLAUtils.DECIMAL_FORMAT.format
					 (totalPaymentAmount) + ", expected: " + 
					 CCLAUtils.DECIMAL_FORMAT.format(subscription.getSubscriptionAmount()) +
					 ", difference: " + CCLAUtils.DECIMAL_FORMAT.format(diff);
					*/
					
					 Log.debug(msg);
					 
				} else {
					// Woohoo - we got a match
					Log.debug("Total payment is exactly the expected amount");
					matchingTotalPaymentAmount = true;
				}
			}
		}
		
		// Fetch latest confirmation form (may be null!)
		Form confirmForm = 
		 Form.getLatestFormBySubscriptionId(subscription.getId(), facade);
		
		if (validSubscriptionStatus) {
			if (confirmForm == null) {
				msg = "No confirmation form found - please check Subscription Info";
				Log.debug(msg);

			} else {
				// Check the state of the latest confirmation form. Must be both valid and 
				// returned
				Log.debug("Found latest Confirmation Form, ID: " 
				 + confirmForm.getFormId());
				
				if ((confirmForm.getFormStatusId() == Form.FormStatus.CANCELLED.id)
					||
					(confirmForm.getFormStatusId() == Form.FormStatus.OLD.id)) {
					
					msg = "Latest confirmation form is marked as old/cancelled";
					
				} else if (confirmForm.getFormStatusId() 
					== Form.FormStatus.VALIDATED.id) {
					// Form is returned AND validated! 
					
					acceptableFormStatus = true;
					formReturnedAndValidated = true;
					
				} else {
					// Form is in any other state.
					acceptableFormStatus = true;
					
					// Append this message
					if (msg.length() > 0)
						msg += "\n";

					msg += "Confirmation Form is pending return/validation";
				}
			}
		}
		
		// Now determine red/amber/green status.
		if (!validSubscriptionStatus || !acceptableFormStatus) 
			// Fail conditions
			paymentStatus = PaymentStatus.RED;
		else if (formReturnedAndValidated && matchingTotalPaymentAmount) 
			// Success conditions
			paymentStatus = PaymentStatus.GREEN;
		else
			// Other (amber) 
			paymentStatus = PaymentStatus.AMBER;
		
		return new SubscriptionPaymentStatus(
			subscription,
			confirmForm,
			totalPaymentAmount,
			totalPreviousInvestments,
			totalPreviousGovMatches,
			paymentStatus,
			msg
		);
	}
		
	/** Enumeration of key values from REF_SUBSCRIPTION_TYPE table.
	 * 
	 * @author Tom
	 *
	 */
	public static class SubscriptionTypeIds {
		public static final int ENDOWMENT_ELIGIBLE_FOR_GOV_MATCH = 1;
		public static final int ENDOWMENT_NOT_ELIGIBLE_FOR_GOV_MATCH = 2;
		public static final int GIFT_AID_FOR_GRANTS_AND_SOCIAL_INVESTMENTS = 3;
		public static final int GOVERNMENT_MATCH = 4;
	}

	private static final Integer DEFAULT_STATUS_ID = SubscriptionStatusIds.NEW;
	
	/** Enumeration of key values from REF_SUBSCRIPTION_STATUS table.
	 * 
	 * @author Tom
	 *
	 */
	public static class SubscriptionStatusIds {
		public static final int NEW = 1;
		public static final int FORM_GENERATED = 2;
		public static final int FORM_RETURNED = 3;
		public static final int FAILED_VALIDATION = 4;
		public static final int COMPLETED = 5;
		public static final int CANCELLED = 6;
		public static final int CASH_RECEIVED = 7;
		
		public static final int FORM_DISPATCHED = 8;
		public static final int PARTIAL_CASH_RECEIVED = 9;
		public static final int EXCESS_CASH_RECEIVED = 10;
		public static final int DIFFERENT_CASH_RECEIVED = 11;
		public static final int CLIENT_CONFIRMED = 12;
		public static final int CLIENT_CONFIRMATION_INVALID = 13;
		public static final int PENDING_INVESTMENT_DECISION = 14;
	}

	public Subscription(Integer id, int accountId,
			BigDecimal subscriptionAmount,
			Date subscriptionDate, String paymentRef, Integer statusId,
			Date dateFormReceived, 
			Date dateLatestCashProcessed, Date dateCompleted,
			Integer productId,
			Date startDate, Date endDate, boolean clientConfirmed,
			Date dateAdded, Date lastUpdated, String lastUpdatedBy) {
		
		super(id, dateAdded, lastUpdated, lastUpdatedBy);
		
		this.accountId = accountId;
		this.subscriptionAmount = subscriptionAmount;
		this.subscriptionDate = subscriptionDate;
		this.paymentRef = paymentRef;
		this.setStatusId(statusId);
		this.dateFormReceived = dateFormReceived;
		this.dateLatestCashProcessed = dateLatestCashProcessed;
		this.dateCompleted = dateCompleted;
		
		this.productId = productId;
		this.startDate = startDate;
		this.endDate = endDate;
		
		this.clientConfirmed = clientConfirmed;
	}
	
	public static Subscription add(int accountId,
	 BigDecimal subscriptionAmount,
	 Date subscriptionDate, String paymentRef, Integer statusId,
	 Date dateFormReceived, Date dateLatestCashProcessed, Date dateCompleted,
	 Integer productId, Date startDate, Date endDate,
	 FWFacade facade, String userName) 
	 throws DataException {
		
		Subscription subscription = new Subscription(
			null, 
			accountId, 
			subscriptionAmount, 
			subscriptionDate, 
			paymentRef,
			statusId,
			dateFormReceived,
			dateLatestCashProcessed,
			dateCompleted,
			productId,
			startDate,
			endDate,
			false,
			null, 
			null, 
			userName
		);
		
		// Prevent subscriptions against unopened accounts.
		Account acc = Account.get(accountId, facade);
		if (acc.getStatus() != Account.AccountStatus.OPEN) {
			String msg = "Unable to create Subscription - " +
					 "associated Account isn't open";
			Log.error(msg);
			throw new DataException(msg);
		}
		
		if (StringUtils.stringIsBlank(subscription.getPaymentRef()))
			subscription.setPaymentRef
			 (getNewPaymentRef(subscription.getAccountId(), facade));
		
		if (subscription.getStatusId() == null)
			subscription.setStatusId(DEFAULT_STATUS_ID);
		
		int newId = subscription.getNewKey(facade);
		subscription.setId(newId);
		
		DataBinder binder = new DataBinder();
		subscription.addFieldsToBinder(binder);
		
		facade.execute(Queries.ADD, binder);
		
		// Audit the Add action
		
		// Link to the new Subscription ID and Account ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(newId, subscription.getEntityName());
		auditRelations.put(accountId, ElementType.ACCOUNT.getName());
		
		DataResultSet rsNewData = getData(newId, facade);
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.ADD.toString(), subscription.getEntityName(),
		 null, rsNewData, auditRelations);
		
		return get(rsNewData);
	}
	
	/** Returns a new (unique) Payment Reference string.
	 * 
	 *  This is in the form [Org Account Code]-[Account Investment Number], where the
	 *  Account Investment Number is the number of investments for the given Account ID
	 *  + 1.
	 *  
	 *  The Account Investment Number is zero-padded to 5 characters, so the entire
	 *  string is always 18 characters long.
	 *  
	 *  E.g AURC00001234-00012
	 *  
	 * @param accountId2
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	private static String getNewPaymentRef(int accountId, FWFacade facade) 
	 throws DataException {

		Integer orgId = Account.getOwnerOrganisationId(accountId, facade);
		Entity org = Entity.get(orgId, facade);
		
		String newPaymentRef = org.getOrgAccountCode() + "-";
		
		DataResultSet rsExistingSubscriptions = getAllDataByAccountId(accountId, facade);
		int accountSubscriptionNumber = rsExistingSubscriptions.getNumRows() + 1;
		
		newPaymentRef += CCLAUtils.padString
		 (Integer.toString(accountSubscriptionNumber), '0', 5);
		
		Log.debug("Generated new Subscription Payment Ref: " + newPaymentRef);
		return newPaymentRef;
	}

	/** Updates the Investment Amount, based on the sum of associated Donations.
	 * 
	 *  Only persists the amount against the DB if it has changed from the currently-
	 *  stored amount.
	 * 
	 * @return 
	 * @throws DataException 
	 */
	public static BigDecimal updateSubscriptionAmount
	 (Subscription subscription, FWFacade facade, String userName) throws DataException {
		
		Vector<Contribution> contributions = 
		 Contribution.getAllBySubscriptionId(subscription.getId(), facade);
		
		BigDecimal totalContributionAmount = Contribution.getTotalContributionAmount
		 (contributions);
		
		Log.debug("Current Subscription Amount: " + 
		 subscription.getSubscriptionAmount());
		
		if (totalContributionAmount != null) {
			
			// Only update the Subscription Amount if it has changed.
			if (subscription.getSubscriptionAmount() == null
				||
				subscription.getSubscriptionAmount().compareTo(totalContributionAmount) != 0) {
				
				Log.debug("Updating Subscription Amount to " + totalContributionAmount);
				
				subscription.setSubscriptionAmount(totalContributionAmount);
				subscription.persist(facade, userName);
			}
		} else if (subscription.getSubscriptionAmount() != null) {
			// Set the Subscription amount to null.
			Log.debug("Updating Subscription Amount to " + totalContributionAmount);
			
			subscription.setSubscriptionAmount(totalContributionAmount);
			subscription.persist(facade, userName);
		}
		
		return totalContributionAmount;
	}
	
	public static Subscription add(DataBinder binder, FWFacade facade, String userName) 
	 throws DataException {
		
		Subscription subscription = new Subscription(userName);
		subscription.setAttributes(binder);

		return add(
			subscription.getAccountId(), 
			subscription.getSubscriptionAmount(), 
			subscription.getSubscriptionDate(),
			subscription.getPaymentRef(),
			subscription.getStatusId(),
			subscription.getDateFormReceived(),
			subscription.getDateLatestCashProcessed(),
			subscription.getDateCompleted(),
			subscription.getProductId(),
			subscription.getStartDate(),
			subscription.getEndDate(),
			facade,
			userName
		);
	}
	
	public static Subscription get(int id, FWFacade facade) throws DataException {
		
		DataResultSet rs = getData(id, facade);
		
		if (rs.first())
			return get(rs);
		else
			return null;
	}
	
	public static Subscription getByPaymentRef(String paymentRef, FWFacade facade) 
	 throws DataException {
		return get(getDataByPaymentRef(paymentRef, facade));
	}
	
	private static DataResultSet getDataByPaymentRef(String paymentRef,
			FWFacade facade) throws DataException {

		DataBinder binder = new DataBinder();
		binder.putLocal(Cols.SUBSCRIPTION_PAYMENT_REF, paymentRef);
		
		return facade.createResultSet(Queries.GET_BY_PAYMENT_REF, binder);
	}

	public static Subscription get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new Subscription(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			CCLAUtils.getResultSetIntegerValue(rs, Account.Cols.ID),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.SUBSCRIPTION_AMOUNT),
			rs.getDateValueByName(Cols.SUBSCRIPTION_DATE),
			rs.getStringValueByName(Cols.SUBSCRIPTION_PAYMENT_REF),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.STATUS),
			rs.getDateValueByName(Cols.DATE_FORM_RECEIVED),
			rs.getDateValueByName(Cols.DATE_LATEST_CASH_PROCESSED),
			rs.getDateValueByName(Cols.DATE_COMPLETED),
			CCLAUtils.getResultSetIntegerValue(rs, Product.Cols.ID),
			rs.getDateValueByName(SharedCols.START_DATE),
			rs.getDateValueByName(SharedCols.END_DATE),
			CCLAUtils.getResultSetBoolValue(rs, Cols.CLIENT_CONFIRMED),
			rs.getDateValueByName(SharedCols.DATE_ADDED),
			rs.getDateValueByName(SharedCols.LAST_UPDATED),
			rs.getStringValueByName(SharedCols.LAST_UPDATED_BY)
		);
	}
	
	public static DataResultSet getData(int id, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, id);
		
		return facade.createResultSet(Queries.GET, binder);
	}
	
	/** Returns a ResultSet of all Investments mapped to the given Account ID. 
	 * 
	 * @throws DataException */
	public static DataResultSet getAllDataByAccountId(int accountId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Account.Cols.ID, accountId);
		
		return facade.createResultSet(Queries.GET_ALL_BY_ACCOUNT_ID, binder);
	}
	
	public Subscription(String userName) {
		super(null, null, null, userName);
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		super.addFieldsToBinder(binder);
		
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, this.getId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Account.Cols.ID, this.getAccountId());
		
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.SUBSCRIPTION_AMOUNT, this.getSubscriptionAmount());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Cols.SUBSCRIPTION_DATE, this.getSubscriptionDate());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.SUBSCRIPTION_PAYMENT_REF, this.getPaymentRef());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.STATUS, this.getStatusId());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Cols.DATE_FORM_RECEIVED, this.getDateFormReceived());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Cols.DATE_LATEST_CASH_PROCESSED, this.getDateLatestCashProcessed());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Cols.DATE_COMPLETED, this.getDateCompleted());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Product.Cols.ID, this.getProductId());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.START_DATE, this.getStartDate());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.END_DATE, this.getEndDate());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.CLIENT_CONFIRMED, this.isClientConfirmed());
	}

	public void persist(FWFacade facade, String username) throws DataException {

		super.persist(facade, username);
		
		// Ensure 2-decimal precision is always used on the amount.
		if (this.getSubscriptionAmount() != null)
			this.setSubscriptionAmount
			(subscriptionAmount.setScale(2, RoundingMode.HALF_UP));
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet rsBeforeData = getData(this.getId(), facade);
		
		Subscription pre = Subscription.get(rsBeforeData);
		
		facade.execute("qCore_UpdateSubscription", binder);
		DataResultSet rsAfterData = getData(this.getId(), facade);
		
		// Run status transaction checks/updates
		doStatusTransitionUpdates(pre, facade, username);
		
		// Audit the Update action
		
		// Link to the new Investment ID and Account ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getId(), this.getEntityName());
		auditRelations.put(this.getAccountId(), ElementType.ACCOUNT.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), this.getEntityName(),
		 rsBeforeData, rsAfterData, auditRelations);
	}
	
	private void doStatusTransitionUpdates
	 (Subscription before, FWFacade facade, String userName) throws DataException {
		
		if (before.getStatusId() != this.getStatusId()) {
			// Status has been updated.
			Log.debug("Detected change in Subscription Status during update (" 
			 + before.getStatusId() + "->" + this.getStatusId());
			
			if (this.getStatusId() == SubscriptionStatusIds.CANCELLED) {
				// Subscription has been cancelled.
				Log.debug("Subscription has been marked as Cancelled");
				
				// Mark latest form as Cancelled as well, if applicable
				Form latestForm = Form.getLatestFormBySubscriptionId
				 (this.getId(), facade);
				
				if (latestForm != null) {
					Log.debug("Setting latest form status to Cancelled");
					latestForm.setFormStatusId(Form.FormStatus.CANCELLED.id);
					latestForm.persist(facade, userName);
				}
			}
			
			if (this.getStatusId() == SubscriptionStatusIds.FORM_DISPATCHED) {
				// Subscription manually marked as 'Form dispatched'
				Log.debug("Subscription has been marked as Form Dispatched");
				
				// Set the form status/print date to match, if applicable
				Form latestForm = Form.getLatestFormBySubscriptionId
				 (this.getId(), facade);
			
				if (latestForm != null) {
					Log.debug("Setting latest form status to Dispatched");
					latestForm.setFormStatusId(Form.FormStatus.DISPATCHED.id);
					latestForm.setDatePrinted(new Date());
					
					latestForm.persist(facade, userName);
				}
			}
			
		}
		
	}

	public void setAttributes(DataBinder binder) throws DataException {

		super.setAttributes(binder);
		
		this.setId(CCLAUtils.getBinderIntegerValue(binder, Cols.ID));
		this.setAccountId(CCLAUtils.getBinderIntegerValue(binder, Account.Cols.ID));
		
		this.setSubscriptionAmount
		 (CCLAUtils.getBinderBigDecimalValue(binder, Cols.SUBSCRIPTION_AMOUNT));
		
		this.setSubscriptionDate
		 (CCLAUtils.getBinderDateValue(binder, Cols.SUBSCRIPTION_DATE));
		this.setPaymentRef
		 (binder.getLocal(Cols.SUBSCRIPTION_PAYMENT_REF));
		
		this.setStatusId
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.STATUS));
		
		this.setDateFormReceived(CCLAUtils.getBinderDateValue
		 (binder, Cols.DATE_FORM_RECEIVED));
		this.setDateLatestCashProcessed(CCLAUtils.getBinderDateValue
		 (binder, Cols.DATE_LATEST_CASH_PROCESSED));
		this.setDateCompleted(CCLAUtils.getBinderDateValue
		 (binder, Cols.DATE_COMPLETED));
		
		this.setProductId(CCLAUtils.getBinderIntegerValue(binder, Product.Cols.ID));
		this.setStartDate(CCLAUtils.getBinderDateValue
				 (binder, SharedCols.START_DATE));
		this.setEndDate(CCLAUtils.getBinderDateValue
				 (binder, SharedCols.END_DATE));
		
		this.setClientConfirmed
		 (CCLAUtils.getBinderBoolValue(binder, Cols.CLIENT_CONFIRMED));
	}

	public void validate(FWFacade facade) throws DataException {
		super.validate(facade);
		
		// Ensure the amount is stored with a scale of 2 decimal places.
		if (this.getSubscriptionAmount() != null) {
			this.setSubscriptionAmount
			 (this.getSubscriptionAmount().setScale(2, RoundingMode.HALF_UP));
		}
	}

	@Override
	public String getSequenceName() {
		return "SEQ_SUBSCRIPTION";
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getSubscriptionAmount() {
		return subscriptionAmount;
	}

	public void setSubscriptionAmount(BigDecimal subscriptionAmount) {
		this.subscriptionAmount = subscriptionAmount;
	}

	public Date getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(Date subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public String getPaymentRef() {
		return paymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setDateFormReceived(Date dateFormReceived) {
		this.dateFormReceived = dateFormReceived;
	}

	public Date getDateFormReceived() {
		return dateFormReceived;
	}

	public void setDateLatestCashProcessed(Date dateLatestCashProcessed) {
		this.dateLatestCashProcessed = dateLatestCashProcessed;
	}

	public Date getDateLatestCashProcessed() {
		return dateLatestCashProcessed;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setClientConfirmed(boolean clientConfirmed) {
		this.clientConfirmed = clientConfirmed;
	}

	public boolean isClientConfirmed() {
		return clientConfirmed;
	}

	/** Returns a DataResultSet of funds, sorted by the order appropriate to the Product
	 *  ID.
	 * 
	 * @throws DataException 
	 *
	 */
	public static DataResultSet getSortedFundsData
	 (Integer productId, DataResultSet rsFunds) 
	 throws DataException {
		
		if (productId != Product.Ids.COMMUNITY_FIRST || rsFunds.isEmpty())
			return rsFunds;
		
		String[] fieldNames = new String[rsFunds.getNumFields()];
		int displayNameIndex = -1;
		
		for (int i=0; i<rsFunds.getNumFields(); i++) 
		{	
			if (rsFunds.getFieldName(i).equals(Fund.COL_DISPLAY_NAME)) 
					displayNameIndex = i;
		
			fieldNames[i]=rsFunds.getFieldName(i);
		}
		
		DataResultSet rsSubscriptionFunds = new DataResultSet(fieldNames);	 
		
		@SuppressWarnings("unchecked")
		List<String> orderList = SharedObjects.getEnvValueAsList
		 ("CF_FUND_INVESTMENT_ALLOCATION_ORDER");
		
		if (orderList!=null && !orderList.isEmpty()) {
			boolean firstLoop = true;
			for (String fundCodeAllocation:orderList) {
				
				rsFunds.first();
				do {
					String fundCode = rsFunds.getStringValueByName(Fund.COL_FUND_CODE);						
					if (firstLoop) {
						String displayName = SharedObjects.getEnvironmentValue
						 ("CF_FUND_DESC_"+fundCode);
						
						rsFunds.getStringValue(displayNameIndex);
						if (!StringUtils.stringIsBlank(displayName))
							rsFunds.setCurrentValue(displayNameIndex, displayName);
					}
					
					if (fundCode.equals(fundCodeAllocation)) {
						rsSubscriptionFunds.addRow(rsFunds.getCurrentRowValues());
						rsFunds.deleteCurrentRow();
					}
					
				} while (rsFunds.next());
				
				firstLoop=false;
			}
			
			if (!rsFunds.isEmpty()) {
				rsFunds.first();
				do {
					rsSubscriptionFunds.addRow(rsFunds.getCurrentRowValues());
					rsFunds.deleteCurrentRow();
				} while (rsFunds.next());
			}
		}
		
		return rsSubscriptionFunds;
	}
}
