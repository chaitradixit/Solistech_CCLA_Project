package com.ecs.stellent.ccla.clientservices.spool;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.shared.SharedObjects;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.SubscriptionForm;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.NumberToWords;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.OrganisationCategory;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.PersonTitle;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileException;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.ucm.ccla.data.subscription.ContributionTTLAAllocation;
import com.ecs.ucm.ccla.data.subscription.ContributionType;
import com.ecs.ucm.ccla.data.subscription.SubscriptionAssetAllocation;
import com.ecs.utils.Log;

/** Old-style Donor Subscription form, featuring TTLA data and aggregated fund 
 * allocations. Preserved for legacy purposes, in case an old form needs printing off.
 * 
 * @author tm
 *
 */
public class CommunityFirstDonorTTLASubscriptionSpoolFileGenerator extends
		SpoolFileGenerator {

	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	public static final int	DEFAULT_NUM_SIGNATORIES = 2;
	
	private static final DecimalFormat CURRENCY_FORMAT = 
		new DecimalFormat("#,###,###,###.00");

	private static final int firstPageTTLACount = 
		SharedObjects.getEnvironmentInt("CF_DONOR_FORM_TTLA_COUNT_FIRST_PAGE", 20);
	private static final int otherPageTTLACount = 
		SharedObjects.getEnvironmentInt("CF_DONOR_FORM_TTLA_COUNT_OTHER_PAGE", 30);

	//# Principal Fund code. 
	private static final String principalFund = 
		SharedObjects.getEnvironmentValue("CF_PRINCIPAL_FUND");
	//# Deposit fund code. 
	private static final String depositFund = 
		SharedObjects.getEnvironmentValue("CF_DEPOSIT_FUND");
	
	/** Prefix string for each CreateForm page template */
	private static final String PAGE_REPORT_ID_PREFIX = "CFEMCDSF";
	
	private static class ReportSections {
		static final String CLIENT_DETAILS 				= "S1";
		static final String DONOR_DETAILS 				= "S2";
		static final String PAYMENT_DETAILS 			= "S3";
		static final String INVESTMENT_DETAILS 			= "S4";
		static final String SIGNATORIES					= "S5";
	}
	
	/** Page counters, used for printing 'Page x of y' captions on each report page.
	 * 
	 */
	private int currentPage 	= 1;
	private int totalPages 		= 1;
	
	
	private SubscriptionForm invForm = null;

	/**
	 * 
	 * @param spoolHeader
	 * @param docName
	 * @param enrolmentId
	 * @param investmentForm
	 */
	public CommunityFirstDonorTTLASubscriptionSpoolFileGenerator(SpoolHeader spoolHeader, SubscriptionForm invForm) 
	{
		super(spoolHeader, null, null);
		this.invForm = invForm;
	}

	@Override
	public ByteArrayOutputStream createSpoolFile() throws IOException,
			DataException, ServiceException 
	{
		if (invForm == null)
			throw new SpoolFileException(
			 "Subs Form instance was empty or not initialized");
		
		ByteArrayOutputStream outputStream 	= new ByteArrayOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		//Calculate the number of pages.
		this.setCalculateNumPages();
		
		this.header.setRecordCount(1);	
		this.writeNewHeader();
		
		//write section 1
		this.writeClientDetails();
		
		//write section 2
		this.writeDonorDetails();
		
		//write section 3
		this.writePaymentDetails();
		
		//write section 4
		this.writeInvestmentDetails();
		
		//write section 5
		this.writeSignatories();
		
		// EOF
		writer.write("#");
		
		writer.flush();
		writer.close();
		return outputStream;
	}

	
	//write section 1
	private void writeClientDetails() throws DataException, IOException 
	{
		this.addReportHeader(getReportId(ReportSections.CLIENT_DETAILS,null));				
		// Add postal address with correspondent name at the top
		addNameValueString("Correspondent", 
				invForm.getCorrespondent().getCompleteName());
		
		// Output postal address
		Address postalAddress = invForm.getContact().getAddress();
		
		Vector<String> postalAddressLines = 
		 postalAddress.getPrintableAddress(true, false);
		
		// Print the postal address over 8 lines.
		for (int j=0; j<8; j++) {
			String addressPiece = null;
			
			if (postalAddressLines.size() > j)
				addressPiece = postalAddressLines.get(j);
			
			addNameValueString("AddressLine" + (j+1), addressPiece);
		}
		
		Integer contributionTypeId = null;
		
		// Determine contribution type ID for the form by inspecting the first one
		// (they should all match)
		contributionTypeId = invForm.getContributions().get(0).getContributionTypeId();
		
		addNameValueString("OrgID", invForm.getOrganisation().getOrgAccountCode());
		addNameValueString("OrgName", invForm.getOrganisation().getOrganisationName());
		
		addNameValueString("QualifyForGovernmentMatch", 
		 contributionTypeId==ContributionType.Ids.ELIGIBLE_FOR_GOVERNMENT_MATCH
		 ?"Y":"N");
		
		addNameValueString("AddedToEndowment", 
		 contributionTypeId==ContributionType.Ids.GIFT_AID_ENDOWMENT
		 ?"Y":"N");
		
		addNameValueString("AccForGrantsAndSocialInv", 
		 contributionTypeId==ContributionType.Ids.GIFT_AID_GRANTS_AND_SOCIAL_INV
		 ?"Y":"N");
	}
	
	//write section 2
	private void writeDonorDetails() throws DataException, IOException, SpoolFileException 
	{
		//Calculate (S2) between 1 to 6 pages depending on the ttla	
		HashMap<Integer, Vector<ContributionTTLAAllocation>> allocationMap = invForm.getTtlaAllocationMap();
		Vector<Contribution> donationVec = invForm.getContributions();
		Vector<Entity> ttlaEntityVec = invForm.getTtlaEntityVec();
		
		if (Entity.getTTLACache().isEmpty()) {
			throw new SpoolFileException("TTLA Cache not initialised");
		}

		for (int i=0; i<donationVec.size(); i++) {			
			ContributionTTLAAllocation undecided = null;
			boolean isFirstPage = true;
			Contribution donation = donationVec.get(i);
			addReportHeader(getReportId(ReportSections.DONOR_DETAILS,1));
			addNameValueString("NumberOfDonors", String.valueOf(donationVec.size()));
			addNameValueString("DonorNumber", String.valueOf(i+1));
			
			String donorName = "";
			if (invForm.getDonorMap().containsKey(donation.getContributorId())) {
				Object element = invForm.getDonorMap().get(donation.getContributorId());
				if (element instanceof Person) {
					Person person = (Person)element;
					if (person.getTitleId()==PersonTitle.Ids.ANONYMOUS)
						donorName = person.getAccountCode();
					else 
						donorName = person.getFullName();
				} else if (element instanceof Entity) {
					Entity org = (Entity)element;
					if (org.getCategoryId()==OrganisationCategory.CategoryIds.ANONYMOUS_DONOR)
						donorName = org.getOrgAccountCode();
					else	
						donorName = org.getOrganisationName();
				}
			}
			
			addNameValueString("DonorName", donorName);
			
			BigDecimal donorAmount = donation.getAmount();
			
			addNameValueString("DonationAmount", 
					(donation.getAmount()!=null?
							CURRENCY_FORMAT.format(donorAmount):""));
			
			addNameValueString("DonationAmountInWords", 
					(donation.getAmount()!=null?
							NumberToWords.convertToGBP(donorAmount).toUpperCase():""));

			/* BigDecimal totalTTLAAmount = new BigDecimal("0"); */
			int count = 1;
			int totalTTLA = 0;

			if (allocationMap.containsKey(donation.getId())) 
			{
				Vector<ContributionTTLAAllocation> ttlaVec = allocationMap.get(donation.getId());
				boolean hasUndecided = false;
				
				for (ContributionTTLAAllocation ttla: ttlaVec) {
					if (ttla.getTTLAOrgId()==null)
						hasUndecided = true;
				}
		
				totalTTLA = ttlaVec.size()+(hasUndecided?1:2); //+1 or 2 for the undecided total field
				addNameValueString("TTLAsContinuedOverleaf", (totalTTLA>firstPageTTLACount?"Y":"N"));

				for (ContributionTTLAAllocation ttla: ttlaVec) 
				{
					//This is the undecide special case. Store for now and append later.
					if (ttla.getTTLAOrgId()==null) {
						undecided = ttla; 
					} else {					
						Entity ttlaOrg = Entity.getTTLACache().getCachedInstance(ttla.getTTLAOrgId());
						if (ttlaOrg==null)
							throw new SpoolFileException("Cannot find TTLA Organisation with id: "+ttla.getTTLAOrgId());
						
						addNameValueString("TTLAArea"+count, ttlaOrg.getOrganisationName());
						
						if (donorAmount!=null && ttla.getAllocationPercent()!=null && ttla.getAllocationAmount()!=null) {
							
							//Always use the amount value
							BigDecimal ttlaAmount = ttla.getAllocationAmount();
							
							// totalTTLAAmount = totalTTLAAmount.add(ttlaAmount);
							addNameValueString("TTLAAmount"+count, CURRENCY_FORMAT
							 .format(ttlaAmount));
							
						} else {
							addNameValueString("TTLAAmount"+count, "");							
						}
						totalTTLA--;
						
						if (isFirstPage) {
							if (count%firstPageTTLACount==0) {
								isFirstPage=false;
								count=0;
							}
						} else {
							if (count%otherPageTTLACount==0) {
								count=0;
							}
						}	
						
						if (count==0) {
							addReportHeader(getReportId(ReportSections.DONOR_DETAILS,2));
							addNameValueString("NumberOfDonors", String.valueOf(donationVec.size()));
							addNameValueString("DonorNumber", String.valueOf(i+1));
							addNameValueString("TTLAsContinuedOverleaf", (totalTTLA>otherPageTTLACount?"Y":"N"));							
						}
						count++;
					}
				}
			} else {				
				if (!donationVec.isEmpty()) 
				{
					totalTTLA = donationVec.size() + 2;  //+2 for the total and unknown field
					addNameValueString("TTLAsContinuedOverleaf", (totalTTLA>firstPageTTLACount?"Y":"N"));
					
					for (Entity org: ttlaEntityVec) 
					{
						addNameValueString("TTLAArea"+count, org.getOrganisationName());
						addNameValueString("TTLAAmount"+count, "");						
						totalTTLA--;
						
						if (isFirstPage) {
							if (count%firstPageTTLACount==0) {
								isFirstPage=false;
								count=0;
							}
						} else {
							if (count%otherPageTTLACount==0) {
								count=0;
							}
						}
						
						if (count==0) {
							addReportHeader(getReportId(ReportSections.DONOR_DETAILS,2));
							addNameValueString("NumberOfDonors", String.valueOf(donationVec.size()));
							addNameValueString("DonorNumber", String.valueOf(i+1));
							addNameValueString("TTLAsContinuedOverleaf", (totalTTLA>otherPageTTLACount?"Y":"N"));				
						}						
						count++;
					}
				} 
			}
			
			//Append Undecided Amount
			BigDecimal undecidedAmount = null;
			if (undecided!=null && undecided.getAllocationPercent()!=null && undecided.getAllocationAmount()!=null) {
				undecidedAmount = undecided.getAllocationAmount();
				
				/*
				(donorAmount.divide(hundred)).multiply(undecided.getIncomeAllocationPercent());
				totalTTLAAmount = totalTTLAAmount.add(undecidedAmount);	
				*/				
			}
			
			addNameValueString("TTLAArea"+count, "Undecided");
			addNameValueString("TTLAAmount"+count, 
					undecidedAmount!=null?CURRENCY_FORMAT.format(undecidedAmount):"");
			
			//Append Total Amount
			count++;			
			addNameValueString("TTLAArea"+count, "TOTAL");
			
			/*
			addNameValueString("TTLAAmount"+count, 
			 totalTTLAAmount.compareTo(zero)==0?"":CURRENCY_FORMAT.format(totalTTLAAmount));
			*/
			
			if (donorAmount != null && 
				(donorAmount.compareTo(BigDecimal.ZERO)!=0)) {
				addNameValueString("TTLAAmount"+count, 
				 donorAmount.compareTo(BigDecimal.ZERO)==0?"":CURRENCY_FORMAT.format(donorAmount));
			}
		}
	}

	//write section 3
	private void writePaymentDetails() throws DataException, IOException 
	{
		addReportHeader(getReportId(ReportSections.PAYMENT_DETAILS, null));
		addNameValueString("PaymentMethod", "");
		addNameValueString("PaymentDate", "");
		addNameValueString("ReferenceNumber", invForm.getSubscription().getPaymentRef());
	}
	
	//write section 4
	private void writeInvestmentDetails() throws DataException, IOException 
	{
		addReportHeader(getReportId(ReportSections.INVESTMENT_DETAILS, null));	

		HashMap<Fund, SubscriptionAssetAllocation> fundAllocationMap = 
			invForm.getFundAllocationMap();
		

		boolean allowPopulate = true;
		//loop through all donations and see if we are allowed to populate the values.
		Vector<Contribution> donationVec = invForm.getContributions();
		for (Contribution donation: donationVec) {
			if (donation.getAmount()==null) {
				allowPopulate = false;
				break;
			}
		}
		
		BigDecimal totalInvestment = invForm.getSubscription().getSubscriptionAmount();
		if (totalInvestment==null)
			totalInvestment = new BigDecimal("0");
		
		SubscriptionAssetAllocation principalFundAllocation = 
			fundAllocationMap.get(Fund.getCache().getCachedInstance(principalFund));
		
		SubscriptionAssetAllocation depositFundAllocation = 
			fundAllocationMap.get(Fund.getCache().getCachedInstance(depositFund));
		
		boolean isInvestInInvFund = false;
		boolean isInvestInDepFund = false;
		
		if (principalFundAllocation!=null && principalFundAllocation.getAmount()!=null && 
				principalFundAllocation.getAmount().compareTo(BigDecimal.ZERO)!=0 &&
				principalFundAllocation.getAmount().compareTo(totalInvestment)==0) {
			Log.debug("Invest in principal fund set");
			isInvestInInvFund = true;
		}

		if (depositFundAllocation!=null && depositFundAllocation.getAmount()!=null && 
				depositFundAllocation.getAmount().compareTo(BigDecimal.ZERO)!=0 &&
				depositFundAllocation.getAmount().compareTo(totalInvestment)==0) {
			Log.debug("Invest in deposit fund set");
			isInvestInDepFund = true;
		}
		
		BigDecimal investmentAllocationTotal = new BigDecimal("0");
		//Loop through to get all values
		for (SubscriptionAssetAllocation allocation: fundAllocationMap.values()) {
			if (allocation.getAmount()!=null)  {
				investmentAllocationTotal = 
					investmentAllocationTotal.add(allocation.getAmount());
			}
		}
		
		addNameValueString("ImmedInvestInInvFund", allowPopulate && isInvestInInvFund?"Y":"N");
		addNameValueString("ImmedInvestInDepFundPend", allowPopulate && isInvestInDepFund?"Y":"N");
		
		boolean investAsFollows = allowPopulate && (!isInvestInInvFund && !isInvestInDepFund);
		
		addNameValueString("InvestAsFollows", 
				(investAsFollows && (investmentAllocationTotal.compareTo(BigDecimal.ZERO)!=0))?"Y":"N");
		
		SubscriptionAssetAllocation allocation = fundAllocationMap.get(Fund.getCache().getCachedInstance("T"));
		
		addNameValueString("InvestInvFundVal", 
				(investAsFollows && 
				(allocation!=null && allocation.getAmount()!=null))?
						CURRENCY_FORMAT.format(allocation.getAmount()):"");
		
		allocation = fundAllocationMap.get(Fund.getCache().getCachedInstance("AA"));
		
		addNameValueString("InvestEthInvFundVal", 
				(investAsFollows &&
				(allocation!=null && allocation.getAmount()!=null))?
						CURRENCY_FORMAT.format(allocation.getAmount()):"");
		
		allocation = fundAllocationMap.get(Fund.getCache().getCachedInstance("U"));
		
		addNameValueString("InvestGloEquIncFundVal", 
				(investAsFollows &&
				(allocation!=null && allocation.getAmount()!=null))?
						CURRENCY_FORMAT.format(allocation.getAmount()):"");

		allocation = fundAllocationMap.get(Fund.getCache().getCachedInstance("V"));
		
		addNameValueString("InvestPropFundVal", 			
				(investAsFollows &&
				(allocation!=null && allocation.getAmount()!=null))?
						CURRENCY_FORMAT.format(allocation.getAmount()):"");
		
		allocation = fundAllocationMap.get(Fund.getCache().getCachedInstance("B"));
		
		addNameValueString("InvestFixIntFundVal", 
				(investAsFollows &&
				(allocation!=null && allocation.getAmount()!=null))?
						CURRENCY_FORMAT.format(allocation.getAmount()):"");
		
		allocation = fundAllocationMap.get(Fund.getCache().getCachedInstance("C"));
		
		addNameValueString("InvestDepFundVal", 
				(investAsFollows &&
				(allocation!=null && allocation.getAmount()!=null))?
						CURRENCY_FORMAT.format(allocation.getAmount()):"");

		addNameValueString("TotalInvestmentVal", 
				(investAsFollows &&
				(investmentAllocationTotal.compareTo(BigDecimal.ZERO)!=0))?CURRENCY_FORMAT.format(investmentAllocationTotal):"");
	}
	
	//write section 5
	private void writeSignatories() throws DataException, IOException 
	{
		addReportHeader(getReportId(ReportSections.SIGNATORIES,null));
		
		for (int i=0; i<=DEFAULT_NUM_SIGNATORIES; i++) {
			addNameValueString("Sig"+(i+1)+"Title", "");
			addNameValueString("Sig"+(i+1)+"Forename", "");
			addNameValueString("Sig"+(i+1)+"Surname", "");
			addNameValueString("Sig"+(i+1)+"Date", "");
		}
	}

	/**
	 * Calculates the total number of pages based on the donations and ttlaAllocations.
	 */
	private void setCalculateNumPages() 
	{
		//1 page for Client Details (S1)
		//1 page for payment Details (S3)
		//1 page for Investment Details (S4)
		//1 page for Signatories (S5)
		totalPages = 4;
		
		//Calculate (S2) between 1 to 6 pages depending on the ttla	
		HashMap<Integer, Vector<ContributionTTLAAllocation>> allocationMap = invForm.getTtlaAllocationMap();
		Vector<Contribution> donationVec = invForm.getContributions();
		
		for (Contribution donation: donationVec) 
		{
			float addedPages = 1f;
			float numEntries = 0f;
			
			if (allocationMap.containsKey(donation.getId())) {
				Vector<ContributionTTLAAllocation> vec = allocationMap.get(donation.getId());
				//Loop through to see if the there an undecided entry.
				boolean hasUndecided = false;
				
				for (ContributionTTLAAllocation ttla: vec) {
					if (ttla.getTTLAOrgId()==null)
						hasUndecided = true;
				}
				//Adding 1 or 2 for TOTAL and Undecided entries.
				numEntries = vec.size() + (hasUndecided?1:2);
			} else {
				if (!Entity.getTTLACache().isEmpty()) {
					//Adding 2 for TOTAL and Undecided entries.
					numEntries = Entity.getTTLACache().getCache().size() + 2;	 
				}
			}
			//The first page allows for 20 TTLA, followed by the next page of 30 TTLA and so on..
			if (numEntries>firstPageTTLACount) {
				numEntries = numEntries - firstPageTTLACount;
				addedPages = (int)Math.ceil(numEntries/otherPageTTLACount) + 1; //+1 for the first page;
			} 
			Log.debug("Adding to total pages :"+addedPages);
			totalPages+=addedPages;	
			Log.debug("current total pages :"+totalPages);
			
		}
	}
	
	
	/** Adds a report header to the BufferedWriter. */
	protected void addReportHeader(String reportId) 
	 throws IOException, DataException {

		writer.write(SpoolFileUtils.REPORT_DELIMITER);
		writer.newLine();
		addNameValueString("ReportID", reportId);

		// Add Form ID and PageNumber under every page delimiter.
		addNameValueString("FormID", 
		 CCLAUtils.padString(
		 Integer.toString(invForm.getFormId()), '0', 9));
		
		addNameValueString
		 ("PageNumber", "Page " + (currentPage++) + " of " + totalPages);
	}
	
	/**
	 * 
	 * @param sectionRef
	 * @param pageNumber
	 * @return
	 */
	public String getReportId(String sectionRef, Integer pageNumber) {
		return PAGE_REPORT_ID_PREFIX + sectionRef + 
				(pageNumber != null ? "P" + pageNumber : ""); 
	}
	
	/** Adds a name-value line to the BufferedWriter. */
	public void addNameValueString(String name, String value) 
	 throws DataException, IOException {
		addNameValueString(name, value, NAME_VALUE_PADDING);
	}
}
