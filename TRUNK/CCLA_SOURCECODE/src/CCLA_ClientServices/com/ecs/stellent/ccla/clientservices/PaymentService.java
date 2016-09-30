package com.ecs.stellent.ccla.clientservices;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstPaymentHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.instruction.TransactionType;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.ucm.ccla.data.subscription.ContributionType;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.ucm.ccla.data.subscription.Subscription.SubscriptionPaymentStatus;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class PaymentService extends Service {
	
	/** Attempts to return Subscription/doc indexing data for a given Payment Reference.
	 * 
	 *  Expects the following values in the binder:
	 *  -paymentRef: the value of the payment ref field. Mandatory
	 *  -docClass: indexed document class, if its available
	 *  -formId: indexed Form ID, if its available
	 *  -amount: the currently-entered cash amount. Mandatory
	 *  
	 *  Providing the payment ref is valid, i.e. it matches to an entry in the 
	 *  SUBSCRIPTION table, the Subscriptions status is inspected to ensure it is in
	 *  a valid state to accept a payment with its reference.
	 *  
	 *  If any of the previous checks fail, an error message will be returned and
	 *  displayed.
	 *  
	 *  If the payment ref appears valid, a whole bunch of stuff is added to the binder, 
	 *  indicating the Subscription status plus a ResultSet of metadata values, if
	 *  applicable.
	 *  
	 *  Currently, a non-null Form ID will prevent any metadata population, as it will
	 *  be assumed this is the returned subscription form/PREADVICE and not an actual
	 *  payment! Similarly, if the doc class/instruction type is explicitly set and 
	 *  not a Deposit.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 * 
	 */
	public void getDocMetaByPaymentRef() throws ServiceException, DataException {
		
		String paymentRef = m_binder.getLocal("paymentRef");
		
		if (StringUtils.stringIsBlank(paymentRef)) {
			throw new ServiceException("No Payment Ref value present");
		}
		
		// Presence of a form ID indicates this isn't an actual payment instruction,
		// but actually the PREADVICE. We won't consider the indexed amount here to
		// be a payment, or return any indexing data, as this should have been handled
		// by the Form ID lookup.
		String formIdStr = m_binder.getLocal("formId");
		
		// Determine the Doc GUID for the payment instruction doc. This is used later
		// to avoid 'double-counting' this payment instruction when aggregating previous
		// payments.
		String dID = m_binder.getLocal(UCMFieldNames.DocID);
		String paymentDocGUID = null;
		
		if (StringUtils.stringIsBlank(formIdStr) && !StringUtils.stringIsBlank(dID)) {
			paymentDocGUID = CCLAUtils.getDocGuidFromDid(Integer.parseInt(dID));
		}
		
		String docClassStr 			= m_binder.getLocal("docClass");
		InstructionType instrType 	= null;
		
		// Assume we're dealing with the initial deposit for now, unless the Doc Class
		// has been explicitly set to a non-Deposit type. We'll assume a DICONDIN is a
		// deposit.
		boolean isDeposit = true;
		
		if (!StringUtils.stringIsBlank(docClassStr)) {
			instrType = InstructionType.getNameCache().getCachedInstance(docClassStr);
			
			if (instrType != null) {
				// If a valid Instruction Type is set, confirm it is either a Deposit
				// type, or a DICONDIN.
				isDeposit = (
				 instrType.getTransactionType() != null
				 &&
				 instrType.getTransactionType().equals(TransactionType.BUY)
				)
				||
				instrType.getInstructionTypeId() == InstructionType.Ids.DICONDIN;
			}
		}
			
		String amountStr = m_binder.getLocal("amount");
		BigDecimal amount = null;

		// Determines whether subscription/payment indexing data will be returned.
		boolean returnIndexingData = false;
		
		// If the doc didn't have a Form ID, and assumed to be a Deposit instruction,
		// assume Cash Payment and parse cash amount.
		if (StringUtils.stringIsBlank(formIdStr) && isDeposit) { 
			if (!StringUtils.stringIsBlank(amountStr)) {
				try {
					amount = CCLAUtils.getBinderBigDecimalValue(m_binder, "amount");
					returnIndexingData = true;
					
				} catch (DataException de) {
					throw new ServiceException
					 ("Unable to parse Cash Amount value: " + amountStr);
				}
			} else {
				// Amount must be passed or we can't accurately determine subscription
				// payment status!
				throw new ServiceException
				 ("Unable to determine Subscription Status, no cash amount specified");
			}
		}
		
		FWFacade facade = CCLAUtils.getFacade(true);
		
		// Lookup associated Subscription
		Subscription subscription = Subscription.getByPaymentRef(paymentRef, facade);
	
		if (subscription == null)
			throw new ServiceException
			 ("No record found for this Payment Reference");
		
		// Build wrapper object that describes the full status of the subscription.
		SubscriptionPaymentStatus subscriptionPaymentStatus = Subscription
		 .getSubscriptionPaymentStatus(subscription, amount, paymentDocGUID, facade);
		
		// Add subscription payment status info to binder.
		m_binder.putLocal("SubscriptionPaymentStatusMessage", 
		 subscriptionPaymentStatus.getMessage());
		
		m_binder.putLocal("SubscriptionPaymentStatus", 
		 subscriptionPaymentStatus.getPaymentStatusString());
		
		m_binder.putLocal("TotalPaymentAmount", 
		 CCLAUtils.DECIMAL_FORMAT.format
		 (subscriptionPaymentStatus.getTotalPaymentAmount()));
		
		m_binder.putLocal("SubscriptionAmount", 
		 CCLAUtils.DECIMAL_FORMAT.format
		 (subscription.getSubscriptionAmount()));
		
		m_binder.putLocal("PaymentDifference", 
		 CCLAUtils.DECIMAL_FORMAT.format
		 (subscriptionPaymentStatus.getTotalPaymentAmount().subtract
		 (subscription.getSubscriptionAmount())));
		
		m_binder.putLocal(Subscription.Cols.ID, subscription.getId().toString());
		
		if (returnIndexingData) {
	 		// Add doc metadata indexing values.
			// Fetch doc meta mapping and add to DataBinder as ResultSet
			CommunityFirstPaymentHandler paymentHandler = 
			 new CommunityFirstPaymentHandler();
			
			Hashtable<String, String> paymentRefMeta = 
			 paymentHandler.getSubscriptionPaymentDocMeta
			 (subscription, docClassStr, facade);
			
			DataResultSet rs = CCLAUtils.getResultSetFromMap(paymentRefMeta);
			m_binder.addResultSet("rsPaymentRefMeta", rs);
		}
	}
}