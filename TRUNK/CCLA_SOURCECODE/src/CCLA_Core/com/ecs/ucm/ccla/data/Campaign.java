package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Wrapper class for Client Services campaigns */

public @Deprecated class Campaign {
	
	protected int campaignId;
	
	protected String name;
	protected String description;
	
	protected Date startDate;
	protected Date endDate;

	protected String status;
	protected String target;
	protected String fundCode;
	
	protected String processHandler;
	
	protected Vector rowValues;
	
	public Campaign(int campaignId, String name, String description,
			Date startDate, Date endDate, String status, String target, String fundCode,
			String processHandler, Vector rowValues) {
		super();
		this.campaignId = campaignId;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
		this.target = target;
		this.processHandler = processHandler;
		this.fundCode = fundCode;
		
		this.rowValues = rowValues;
	}
	
	public static Campaign get(int campaignId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsCampaign = getData(campaignId, facade);
		
		if (rsCampaign.isEmpty())
			return null;
		
		// Store null values for the Date fields, if the DB values are empty.
		Date thisStartDate = null;
		Date thisEndDate   = null;
		
		if (!StringUtils.stringIsBlank(rsCampaign.getStringValueByName("START_DATE")))
			thisStartDate = rsCampaign.getDateValueByName("START_DATE");
		
		if (!StringUtils.stringIsBlank(rsCampaign.getStringValueByName("END_DATE")))
			thisStartDate = rsCampaign.getDateValueByName("END_DATE");
		
		Campaign campaign = new Campaign(
			campaignId,
			rsCampaign.getStringValueByName("NAME"),
			rsCampaign.getStringValueByName("DESCRIPTION"),
			thisStartDate,
			thisEndDate,
			rsCampaign.getStringValueByName("STATUS"),
			rsCampaign.getStringValueByName("TARGET"),
			rsCampaign.getStringValueByName("FUND_CODE"),
			rsCampaign.getStringValueByName("PROCESS_HANDLER_CLASS"),
			rsCampaign.getRowValues(rsCampaign.getCurrentRow())
		);
		
		return campaign;
	}
	
	public static DataResultSet getData(int campaignId, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("CAMPAIGN_ID", Integer.toString(campaignId));
		
		DataResultSet rsCampaign =
		 facade.createResultSet("qClientServices_GetCampaign", binder);

		return rsCampaign;
	}

	public int getCampaignId() {
		return campaignId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getStatus() {
		return status;
	}

	public String getTarget() {
		return target;
	}

	public String getProcessHandler() {
		return processHandler;
	}

	public Vector getRowValues() {
		return rowValues;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	
}
