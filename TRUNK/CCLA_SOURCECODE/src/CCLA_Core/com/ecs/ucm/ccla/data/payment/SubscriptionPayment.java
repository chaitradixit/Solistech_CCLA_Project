package com.ecs.ucm.ccla.data.payment;

import java.math.BigDecimal;
import java.util.Date;

import com.ecs.ucm.ccla.data.EnhancedPersistable;

/** Models entries in the CCLA.SUBSCRIPTION_PAYMENT table
 * 
 * @author Tom
 *
 */
public class SubscriptionPayment extends EnhancedPersistable {
	
	private int paymentId;
	private int subscriptionId;
	private BigDecimal amount;
	
	public static class Cols {
		public static final String ID = "SUBSCRIPTION_PAYMENT_ID";
		public static final String AMOUNT = "SUBSCRIPTION_PAYMENT_AMOUNT"; 
	}
	
	public static class Queries {
		public static final String ADD = "qCore_AddSubscriptionPayment";
		public static final String UPDATE = "qCore_UpdateSubscriptionPayment";
		public static final String GET = "qCore_GetSubscriptionPayment";
		
		public static final String GET_ALL_BY_PAYMENT_ID = 
		 "qCore_GetAllSubscriptionPaymentsByPaymentId";
		public static final String GET_ALL_BY_SUBSCRIPTION_ID = 
		 "qCore_GetAllSubscriptionPaymentsBySubscriptionId";
	}
	
	public SubscriptionPayment(Integer id, Date dateAdded, Date lastUpdated,
			String lastUpdatedBy) {
		super(id, dateAdded, lastUpdated, lastUpdatedBy);
	}

	@Override
	public String getSequenceName() {
		return "SEQ_SUBSCRIPTION_PAYMENT";
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public int getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
