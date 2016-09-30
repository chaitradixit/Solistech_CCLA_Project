package com.ecs.ucm.ccla.data.payment;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.math.BigDecimal;
import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.EnhancedPersistable;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the CCLA.PAYMENT table.
 *  
 *  Captures individual payments at the bank account level.
 *  
 * @author Tom
 *
 */
public class Payment extends EnhancedPersistable {

	private BigDecimal amount;
	private Date dateReceived;
	private Date dateProcessed;
	
	public static class Cols {
		public static final String ID = "PAYMENT_ID";
		public static final String AMOUNT = "PAYMENT_AMOUNT";
		public static final String RECEIVED_DATE = "PAYMENT_RECEIVED_DATE";
		public static final String PROCESSED_DATE = "PAYMENT_PROCESSED_DATE";
	}
	
	public static class Queries {
		public static final String ADD = "qCore_AddPayment";
		public static final String UPDATE = "qCore_UpdatePayment";
		public static final String GET = "qCore_GetPayment";
	}
	
	public static Payment add(
		BigDecimal amount, 
		Date dateReceived,
		Date dateProcessed,
		FWFacade facade, String userName) throws DataException {
		
		Payment payment = new Payment
		 (null, null, null, userName, amount, dateReceived, dateProcessed);
		
		int newId = payment.getNewKey(facade);
		payment.setId(newId);
		
		DataBinder binder = new DataBinder();
		payment.addFieldsToBinder(binder);
		
		facade.execute(Queries.ADD, binder);
	
		// TODO: audit
		
		return get(facade.createResultSet(Queries.GET, binder));
	}
	
	@Override
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		super.addFieldsToBinder(binder);
		
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.AMOUNT, this.getAmount());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Cols.RECEIVED_DATE, this.getDateReceived());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Cols.PROCESSED_DATE, this.getDateProcessed());
	}

	@Override
	public void persist(FWFacade facade, String username) throws DataException {
		super.persist(facade, username);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		// TODO: audit
		
		facade.execute(Queries.UPDATE, binder);
	}

	@Override
	public void setAttributes(DataBinder binder) throws DataException {
		super.setAttributes(binder);
		
		this.setAmount(CCLAUtils.getBinderBigDecimalValue(binder, Cols.AMOUNT));
		this.setDateReceived(CCLAUtils.getBinderDateValue(binder, Cols.RECEIVED_DATE));
		this.setDateProcessed(CCLAUtils.getBinderDateValue(binder, Cols.PROCESSED_DATE));
	}
	
	public static Payment get(DataResultSet rs) throws DataException {
		return new Payment(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			
			rs.getDateValueByName(SharedCols.DATE_ADDED),
			rs.getDateValueByName(SharedCols.LAST_UPDATED),
			rs.getStringValueByName(SharedCols.LAST_UPDATED_BY),
			
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.AMOUNT),
			rs.getDateValueByName(Cols.RECEIVED_DATE),
			rs.getDateValueByName(Cols.PROCESSED_DATE)
		);
	}
	
	public Payment(Integer id, Date dateAdded, Date lastUpdated,
			String lastUpdatedBy, BigDecimal amount, Date dateReceived,
			Date dateProcessed) {
		super(id, dateAdded, lastUpdated, lastUpdatedBy);
		this.amount = amount;
		this.dateReceived = dateReceived;
		this.dateProcessed = dateProcessed;
	}
	
	public Payment(Integer id, Date dateAdded, Date lastUpdated,
			String lastUpdatedBy) {
		super(id, dateAdded, lastUpdated, lastUpdatedBy);
	}

	@Override
	public String getSequenceName() {
		return "SEQ_PAYMENT";
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public Date getDateProcessed() {
		return dateProcessed;
	}

	public void setDateProcessed(Date dateProcessed) {
		this.dateProcessed = dateProcessed;
	}
}
