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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.SubscriptionForm;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.NumberToWords;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.Element;
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
import com.ecs.ucm.ccla.data.subscription.ContributionAssetAllocation;
import com.ecs.ucm.ccla.data.subscription.ContributionTTLAAllocation;
import com.ecs.ucm.ccla.data.subscription.ContributionType;
import com.ecs.ucm.ccla.data.subscription.SubscriptionAssetAllocation;
import com.ecs.utils.Log;

/** Used for creation of new-style Donor Subscription forms. No mention of TTLAs here,
 *  and each donor may choose their own Fund allocations.
 *  
 * @author tm
 *
 */
public class CommunityFirstDonorSubscriptionSpoolFileGenerator extends
		SpoolFileGenerator {

	/** Values are always printed after this number of columns on each line. */
	public static final int	NAME_VALUE_PADDING = 28;
	
	public static final int	DEFAULT_NUM_SIGNATORIES = 2;
	
	private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat
	 ("#,###,###,##0.00");
	

	//# Principal Fund code. 
	private static final String PRINCIPAL_FUND_CODE =
	 SharedObjects.getEnvironmentValue("CF_PRINCIPAL_FUND");
	//# Deposit fund code. 
	private static final String DEPOSIT_FUND_CODE =
	 SharedObjects.getEnvironmentValue("CF_DEPOSIT_FUND");
	
	/** Prefix string for each CreateForm page template */
	private static final String PAGE_REPORT_ID_PREFIX = "CFEMCDSF";
	
	private static class ReportSections {
		static final String CLIENT_AND_PAYMENT_DETAILS 	= "S1";
		static final String DONOR_DETAILS 				= "S2";
		static final String AUTHORISATION				= "S3";
	}
	
	/** Ordered list of Fund Codes available for investment, and their spool file labels.
	 * 
	 */
	private static LinkedHashMap<String, String> FUND_CODE_LABELS;
	
	static {
		FUND_CODE_LABELS = new LinkedHashMap<String, String>();
		
		FUND_CODE_LABELS.put("T", "Inv");
		FUND_CODE_LABELS.put("AA", "EthInv");
		FUND_CODE_LABELS.put("U", "GloEquInc");
		FUND_CODE_LABELS.put("V", "Prop");
		FUND_CODE_LABELS.put("B", "FixInt");
		FUND_CODE_LABELS.put("C", "Dep");
	}
	
	/** Page counters, used for printing 'Page x of y' captions on each report page.
	 * 
	 */
	private int currentPage 	= 1;
	private int totalPages 		= 1;
	
	private SubscriptionForm subscriptionForm = null;

	/**
	 * 
	 * @param spoolHeader
	 * @param docName
	 * @param enrolmentId
	 * @param investmentForm
	 */
	public CommunityFirstDonorSubscriptionSpoolFileGenerator
	 (SpoolHeader spoolHeader, SubscriptionForm invForm) {
		super(spoolHeader, null, null);
		this.subscriptionForm = invForm;
	}

	@Override
	public ByteArrayOutputStream createSpoolFile() throws IOException,
			DataException, ServiceException {
		
		if (subscriptionForm == null)
			throw new SpoolFileException(
			 "Subscription Form instance was empty or not initialized");
		
		ByteArrayOutputStream outputStream 	= new ByteArrayOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		//Calculate the number of pages.
		this.setCalculateNumPages();
		
		this.header.setRecordCount(1);	
		this.writeNewHeader();
		
		this.writeClientDetails();

		int donorIndex = 1;
		for (Contribution contribution : subscriptionForm.getContributions()) {
			this.writeDonorDetails(contribution, donorIndex++);
		}

		this.writeSignatories();
		
		// EOF
		writer.write("#");
		
		writer.flush();
		writer.close();
		return outputStream;
	}

	
	// Write Section 1 - Organisation details and total payment amount/method.
	private void writeClientDetails() throws DataException, IOException 
	{
		this.addReportHeader(getReportId(ReportSections.CLIENT_AND_PAYMENT_DETAILS,null));				
		
		addNameValueString("OrgID", subscriptionForm.getOrganisation().getOrgAccountCode());
		addNameValueString("OrgName", subscriptionForm.getOrganisation().getOrganisationName());
		
		addNameValueString("PaymentMethod", null);
		addNameValueString("PaymentDate", null);
		
		String totalAmountStr = CURRENCY_FORMAT.format
		 (subscriptionForm.getSubscription().getSubscriptionAmount().doubleValue());
		
		// Break up the formatted total amount string to pounds/pence.
		String totalAmountPounds = totalAmountStr.substring
		 (0, totalAmountStr.indexOf("."));
		String totalAmountPence = totalAmountStr.substring
		 (totalAmountStr.indexOf(".")+1, totalAmountStr.length());
		
		addNameValueString("TotalAmountSentPounds", totalAmountPounds);
		addNameValueString("TotalAmountSentPence", totalAmountPence);
		
		addNameValueString("ReferenceNumber", 
		 subscriptionForm.getSubscription().getPaymentRef());
	}
	
	// Write Section 2 - investment details for a single Donor. Section is repeated
	// once for each Donor.
	private void writeDonorDetails(Contribution contribution, int donorIndex) 
	 throws DataException, IOException, SpoolFileException {
		
		this.addReportHeader(getReportId(ReportSections.DONOR_DETAILS,null));				
		
		addNameValueString("NumberOfDonors", String.valueOf
		 (subscriptionForm.getContributions().size()));
		addNameValueString("DonorNumber", String.valueOf(donorIndex));
		
		String donorName = "";

		Element donor = subscriptionForm.getDonorMap().get
		 (contribution.getContributorId());
		
		if (donor instanceof Person) {
			Person person = (Person)donor;
			if (person.getTitleId()==PersonTitle.Ids.ANONYMOUS)
				donorName = person.getAccountCode();
			else 
				donorName = person.getFullName();
		} else if (donor instanceof Entity) {
			Entity org = (Entity)donor;
			if (org.getCategoryId()==OrganisationCategory.CategoryIds.ANONYMOUS_DONOR)
				donorName = org.getOrgAccountCode();
			else	
				donorName = org.getOrganisationName();
		}
			
		addNameValueString("DonorName", donorName);
		
		List<Account> donorAccounts = 
		 subscriptionForm.getDonorAccounts().get(contribution.getContributorId());
		
		Account donorAccount = null;
		
		if (donorAccounts != null) {
			for (Account account : donorAccounts) {
				if (account.getFundCode().equals(DEPOSIT_FUND_CODE)) {
					donorAccount = account;
					break;
				}
			}
		}
		
		addNameValueString("DonationAccountNumber",
		 donorAccount != null ? donorAccount.getAccNumExt() : null);
		
		// Total donation amount.
		String totalAmountStr = CURRENCY_FORMAT.format(contribution.getAmount());
		
		// Break up the formatted total amount string to pounds/pence.
		String totalAmountPounds = totalAmountStr.substring
		 (0, totalAmountStr.indexOf("."));
		String totalAmountPence = totalAmountStr.substring
		 (totalAmountStr.indexOf(".")+1, totalAmountStr.length());
		
		addNameValueString("DonationAmountPounds", totalAmountPounds);
		addNameValueString("DonationAmountPence", totalAmountPence);
		
		addNameValueString("QualifyForGovernmentMatch", 
		 contribution.getContributionTypeId()
		 ==ContributionType.Ids.ELIGIBLE_FOR_GOVERNMENT_MATCH ? "Y":"N");
		
		// Determine investment decisions (if any!)
		boolean hasInvestmentDecision = false;
		
		// Must be at least 1 fund allocation against this donation, otherwise we'll
		// leave the whole section empty.
		List<ContributionAssetAllocation> fundAllocations = subscriptionForm
		 .getContributionAssetAllocations().get(contribution);
		
		hasInvestmentDecision = !fundAllocations.isEmpty();
		
		boolean isInvestInInvFund = false;
		boolean isInvestInDepFund = false;
		
		if (hasInvestmentDecision) {
			ContributionAssetAllocation principalFundAllocation = 
			 getAssetAllocationByFundCode(fundAllocations, PRINCIPAL_FUND_CODE);
			
			ContributionAssetAllocation depositFundAllocation =
			 getAssetAllocationByFundCode(fundAllocations, DEPOSIT_FUND_CODE);

			if (principalFundAllocation!=null && principalFundAllocation.getAmount()!=null && 
					principalFundAllocation.getAmount().compareTo(BigDecimal.ZERO)!=0 &&
					principalFundAllocation.getAmount().compareTo(contribution.getAmount())==0) {
				Log.debug("Invest in principal fund set");
				isInvestInInvFund = true;
			}
	
			if (depositFundAllocation!=null && depositFundAllocation.getAmount()!=null && 
					depositFundAllocation.getAmount().compareTo(BigDecimal.ZERO)!=0 &&
					depositFundAllocation.getAmount().compareTo(contribution.getAmount())==0) {
				Log.debug("Invest in deposit fund set");
				isInvestInDepFund = true;
			}
		}
		
		addNameValueString("ImmedInvestInInvFund", 
		 hasInvestmentDecision && isInvestInInvFund?"Y":"N");
		addNameValueString("ImmedInvestInDepFundPend", 
		 hasInvestmentDecision && isInvestInDepFund?"Y":"N");
		
		boolean customInvestment = false;
		
		if (hasInvestmentDecision && !(isInvestInInvFund || isInvestInDepFund)) {
			// Donor has chosen to allocate to one of the other available funds, or across 
			// multiple funds.
			customInvestment = true;
		}
		
		addNameValueString("InvestAsFollows", 
		 customInvestment ?"Y":"N");
		
		Set<String> investmentFundCodes = FUND_CODE_LABELS.keySet();
		
		for (String fundCode : investmentFundCodes) {
			String label = "Invest" + FUND_CODE_LABELS.get(fundCode) + "FundVal";
			String investmentAmount = null;
			
			if (customInvestment) {
				ContributionAssetAllocation alloc = 
				 getAssetAllocationByFundCode(fundAllocations, fundCode);
				
				if (alloc != null && alloc.getAmount().compareTo(BigDecimal.ZERO) != 0)
					investmentAmount = CURRENCY_FORMAT.format(alloc.getAmount());
			}
			
			addNameValueString(label, investmentAmount);
		}
		
		addNameValueString("TotalInvestmentVal", 
		 customInvestment ? totalAmountStr : null);
	}
	
	/** Returns a matching fund allocation from the list, with the passed Fund Code.
	 *  Returns null if no matching allocation is found. 
	 **/
	private static ContributionAssetAllocation getAssetAllocationByFundCode
	 (List<ContributionAssetAllocation> assetAllocations, String fundCode) {
		
		for (ContributionAssetAllocation assetAllocation : assetAllocations) {
			if (assetAllocation.getFundCode().equals(fundCode))
				return assetAllocation;
		}
		
		return null;
	}
	
	// Authorisation section. All blank, just lists boxes for adding signatories.
	private void writeSignatories() throws DataException, IOException 
	{
		addReportHeader(getReportId(ReportSections.AUTHORISATION,null));
		
		for (int i=0; i<=DEFAULT_NUM_SIGNATORIES; i++) {
			addNameValueString("Sig"+(i+1)+"Title", "");
			addNameValueString("Sig"+(i+1)+"Forename", "");
			addNameValueString("Sig"+(i+1)+"Surname", "");
			addNameValueString("Sig"+(i+1)+"Date", "");
		}
	}

	/**
	 * Calculates the total number of pages based on the number of donations.
	 */
	private void setCalculateNumPages() 
	{
		totalPages = 
			1 // 1 page for Client/Payment Details (S1)
			+ subscriptionForm.getContributions().size()  // 1 page per Donor (S2)
			+ 1; //1 page for Signatories (S3)
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
		 Integer.toString(subscriptionForm.getFormId()), '0', 9));
		
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
