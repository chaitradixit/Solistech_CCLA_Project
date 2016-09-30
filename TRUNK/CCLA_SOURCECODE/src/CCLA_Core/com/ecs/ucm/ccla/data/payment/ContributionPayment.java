package com.ecs.ucm.ccla.data.payment;

import java.math.BigDecimal;
import java.util.Date;

import com.ecs.ucm.ccla.data.EnhancedPersistable;

/** Models entries in the CCLA.CONTRIBUTION_PAYMENT table
 * 
 * @author Tom
 *
 */
public class ContributionPayment extends EnhancedPersistable {
	
	private BigDecimal subscriptionPaymentId;
	private BigDecimal amount;
	
	public static class Cols {
		public static final String ID = "CONTRIBUTION_PAYMENT_ID";
		public static final String AMOUNT = "CONTRIBUTION_PAYMENT_AMOUNT"; 
	}
	
	public ContributionPayment(Integer id, Date dateAdded, Date lastUpdated,
			String lastUpdatedBy) {
		super(id, dateAdded, lastUpdated, lastUpdatedBy);
	}

	@Override
	public String getSequenceName() {
		return "SEQ_CONTRIBUTION_PAYMENT";
	}
}
