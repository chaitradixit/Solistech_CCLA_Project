package com.ecs.ucm.ccla.data.subscription;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.EnhancedPersistable;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models an entry from the RPI_DRAWDOWN table - this is a helper table used to store
 *  the market/RPI values for given Donor investments in a given financial period (as
 *  defined by the rpiDate field)
 *  
 *  Currently this table is used to support the annual Community First RPI Drawdown 
 *  process (specifically the creation of RPI Drawdown forms) but it could be adapted to 
 *  suit other campaigns.
 *  
 *  The table data is read-only from UCM perspective so there is no implementation of
 *  add() or persist(). The data will be loaded from an external spreadsheet and some
 *  columns are modified via a separate ADF indexing screen.
 *  
 * @author tm
 *
 */
public class RpiDrawdown extends EnhancedPersistable {
	
	private Integer campaignId;
	
	/** The RPI base date where all figures for a particular year/financial date are
	 *  calculated from. 
	 */
	private Date rpiDate;
	
	/** In the case of Comm First RPI Drawdown, this is the LCF Element ID */
	private Integer ownerId;
	/** In the case of Comm First RPI Drawdown, this corresponds to the donating 
	 *  org/person Element ID */
	private Integer donorId;

	/** Market value of donor's investments at the RPI date. */
	private BigDecimal marketValue;
	/** Equivalent market value of donor's initial investment against the RPI rate */
	private BigDecimal rpiValue;
	/** MarketValue - RpiValue */
	private BigDecimal surplusValue;
	
	//private BigDecimal surplusValueRequested;
	
	public static class Cols {
		public static final String RPI_DRAWDOWN_ID 		= "RPI_DRAWDOWN_ID";
		public static final String RPI_DATE 			= "RPI_DATE";
		public static final String CAMPAIGN_ID 			= "CAMPAIGN_ID";
		public static final String OWNER_ID 			= "OWNER_ID";
		public static final String DONOR_ID 			= "DONOR_ID";
		public static final String MARKET_VALUE 		= "MARKET_VALUE";
		public static final String RPI_VALUE 			= "RPI_VALUE";
		public static final String SURPLUS_VALUE 		= "SURPLUS_VALUE";
	}
	
	public RpiDrawdown(Integer id, Date dateAdded, Date lastUpdated,
			String lastUpdatedBy) {
		super(id, dateAdded, lastUpdated, lastUpdatedBy);
	}

	@Override
	public String getSequenceName() {
		return "SEQ_RPI_DRAWDOWN";
	}
	
	public RpiDrawdown(Integer id, Date dateAdded, Date lastUpdated,
			String lastUpdatedBy, Integer campaignId, Date rpiDate,
			Integer ownerId, Integer donorId, BigDecimal marketValue,
			BigDecimal rpiValue, BigDecimal surplusValue) {
		super(id, dateAdded, lastUpdated, lastUpdatedBy);
		this.campaignId = campaignId;
		this.rpiDate = rpiDate;
		this.ownerId = ownerId;
		this.donorId = donorId;
		this.marketValue = marketValue;
		this.rpiValue = rpiValue;
		this.surplusValue = surplusValue;
	}
	
	public static RpiDrawdown get(DataResultSet rs) throws DataException {
		return new RpiDrawdown(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.RPI_DRAWDOWN_ID),
			
			// Don't care about timestamp/audit field values
			null,
			null,
			null,
			
			CCLAUtils.getResultSetIntegerValue(rs, Cols.CAMPAIGN_ID),
			rs.getDateValueByName(Cols.RPI_DATE),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.OWNER_ID),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.DONOR_ID),
			
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.MARKET_VALUE),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.RPI_VALUE),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.SURPLUS_VALUE)
		);
	}
	
	public static DataResultSet getAllDataByCampaignAndDateAndOwner
		(int campaignId, Date rpiDate, int ownerId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.CAMPAIGN_ID, campaignId);
		CCLAUtils.addQueryDateParamToBinder(binder, Cols.RPI_DATE, rpiDate);
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.OWNER_ID, ownerId);
		
		return facade.createResultSet
		 ("qCore_GetAllRpiDrawdownsByCampaignDateOwner", binder);
	}
	
	public static List<RpiDrawdown> getAll(DataResultSet rs) throws DataException {
		List<RpiDrawdown> rpiDrawdowns = new ArrayList<RpiDrawdown>();
		
		if (rs.first()) {
			do {
				rpiDrawdowns.add(get(rs));
			} while (rs.next());
		}
		
		return rpiDrawdowns;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public Date getRpiDate() {
		return rpiDate;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public Integer getDonorId() {
		return donorId;
	}

	public BigDecimal getMarketValue() {
		return marketValue;
	}

	public BigDecimal getRpiValue() {
		return rpiValue;
	}

	public BigDecimal getSurplusValue() {
		return surplusValue;
	}
}
