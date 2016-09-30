package com.ecs.stellent.spp.data;

import intradoc.data.DataBinder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;

/** Wrapper class for storing Error/Breach/Complaint/Query job data, required
 *  when using the QUERY workflow profile for job submission.
 *  
 * @author Tom
 *
 */
public class QueryJobData {
	

	public String company;
	public String clientNumber;

	public String accountNumber;
	public String fundCode;
	
	public String documentClass;
	public Date date;
	
	public Integer workflowId;
	
	public boolean isComplaint;
	public boolean isError;
	public boolean isBreach;
	public boolean isClientQuery;
	
	public Integer causeId;
	public Integer subCauseId;
	public String summary;
	public String requiredAction;
	public String howIdentified;
	
	public Date dateIdentified;
	public Date dateOccurred;
	public Date dateResolved;
	
	public String createdBy;
	public Integer ucmJobId;
	
	public Integer instructionId;
	public Date xPaymentDate;
	
	/** Generates a name-value mapping for all query job parameters.
	 *  
	 *  This is required by the getSppVariables() method to convert all the
	 *  attributes into a Variable array which can be passed to the SPP
	 *  web service.
	 *  
	 * @return
	 */
	public Hashtable<String, String> getAttributes() {
		Hashtable<String, String> attribs = new Hashtable<String, String>();
		
		// Add 'core' attributes first, common to all SPP jobs.
		if (company != null)
			attribs.put("xCompany", company);
		if (clientNumber != null)
			attribs.put("xClientNumber", clientNumber);
		if (accountNumber != null)
			attribs.put("xAccountNumber", accountNumber);
		
		if (fundCode != null)
			attribs.put("xFund", fundCode);
		if (documentClass != null)
			attribs.put("xDocumentClass", documentClass);
		if (date != null)
			attribs.put("xDocumentDate", DateFormatter.getTimeStamp(date));
		
		if (workflowId != null)
			attribs.put("xBatchNumber", workflowId.toString());
		
		if (ucmJobId != null)
			attribs.put("xJobId", ucmJobId.toString());
			
		// Now add Query Job-specific attributes
		if (isComplaint)
			attribs.put("isComplaint", "1");
		if (isBreach)
			attribs.put("isBreach", "1");
		if (isError)
			attribs.put("isError", "1");
		if (isClientQuery)
			attribs.put("isClientQuery", "1");
		
		if (causeId != null)
			attribs.put("causeId", causeId.toString());
		if (subCauseId != null)
			attribs.put("subCauseId", subCauseId.toString());
		if (summary != null)
			attribs.put("summary", summary);
		if (requiredAction != null)
			attribs.put("requiredAction", requiredAction);
		if (howIdentified != null)
			attribs.put("howIdentified", howIdentified);

		if (dateIdentified != null)
			attribs.put("dateIdentified", 
			DateFormatter.getTimeStamp(dateIdentified));	
		if (dateOccurred != null)
			attribs.put("dateOccurred", 
			DateFormatter.getTimeStamp(dateOccurred));
		if (dateResolved != null)
			attribs.put("dateResolved", 
			DateFormatter.getTimeStamp(dateResolved));

		if (createdBy != null)
			attribs.put("createdBy", createdBy);
		
		if (instructionId!=null) {
			attribs.put("instructionId", instructionId.toString());
		}

		if (xPaymentDate != null)
			attribs.put("xPaymentDate", 
			DateFormatter.getTimeStamp(xPaymentDate));

		return attribs;
	}
	
	/** Applies values to a limited set of attributes, based on
	 *  similarly-named values from the passed DataBinder.
	 *  
	 * @param binder
	 * @throws ParseException 
	 */
	public void setAttributes(DataBinder binder) throws ParseException {
		
		String thisCauseId 		= binder.getLocal("causeId");
		String thisSubCauseId	= binder.getLocal("subCauseId");
		
		if (!StringUtils.stringIsBlank(thisCauseId))
			causeId = Integer.parseInt(thisCauseId);
		
		if (!StringUtils.stringIsBlank(thisSubCauseId))
			subCauseId = Integer.parseInt(thisSubCauseId);
		
		if (!StringUtils.stringIsBlank(binder.getLocal("summary")))
			summary = binder.getLocal("summary");
		
		if (!StringUtils.stringIsBlank(binder.getLocal("requiredAction")))
			requiredAction = binder.getLocal("requiredAction");
		
		if (!StringUtils.stringIsBlank(binder.getLocal("howIdentified")))
			howIdentified = binder.getLocal("howIdentified");
		
		String dateIdentifiedStr = 
		 binder.getLocal("dateIdentified");
		
		SimpleDateFormat dateOnly = new SimpleDateFormat("dd/MM/yy");
		
		if (!StringUtils.stringIsBlank(dateIdentifiedStr))
			dateIdentified = 
			dateOnly.parse(dateIdentifiedStr);
		
		String dateOccurredStr = 
		 binder.getLocal("dateOccurred");
		
		if (!StringUtils.stringIsBlank(dateOccurredStr))
			dateOccurred = 
			 dateOnly.parse(dateOccurredStr);
		
		String dateResolvedStr = 
		 binder.getLocal("dateResolved");
		
		if (!StringUtils.stringIsBlank(dateResolvedStr))
			dateResolved = 
			 dateOnly.parse(dateResolvedStr);
		
		String createdByStr = binder.getLocal("createdBy");
		
		if (!StringUtils.stringIsBlank(createdByStr))
			createdBy = createdByStr;
	}

	@Override
	public String toString() {
		return "QueryJobData [accountNumber=" + accountNumber + "\\n, causeId="
				+ causeId + "\\n, clientNumber=" + clientNumber
				+ "\\n, company=" + company + "\\n, date=" + date
				+ "\\n, dateIdentified=" + dateIdentified
				+ "\\n, dateOccurred=" + dateOccurred + "\\n, dateResolved="
				+ dateResolved + "\\n, documentClass=" + documentClass
				+ "\\n, fundCode=" + fundCode + "\\n, howIdentified="
				+ howIdentified + "\\n, isBreach=" + isBreach
				+ "\\n, isClientQuery=" + isClientQuery + "\\n, isComplaint="
				+ isComplaint + "\\n, isError=" + isError
				+ "\\n, requiredAction=" + requiredAction + "\\n, summary="
				+ summary + "\\n, workflowId=" + workflowId + "]";
	}
	
	/**
	 * Check if the object contains any data.
	 * @return
	 */
	public boolean isEmpty() {
		return (
				StringUtils.stringIsBlank(company) &&
				StringUtils.stringIsBlank(clientNumber) &&
				StringUtils.stringIsBlank(accountNumber) &&
				StringUtils.stringIsBlank(fundCode) &&
				StringUtils.stringIsBlank(documentClass) &&
				date==null &&
				workflowId==null &&
				!(isComplaint || isError || isBreach || isClientQuery) &&
				causeId==null &&
				subCauseId==null &&
				StringUtils.stringIsBlank(summary) &&
				StringUtils.stringIsBlank(requiredAction) &&
				StringUtils.stringIsBlank(howIdentified) &&
				dateIdentified==null &&
				dateOccurred==null &&
				dateResolved==null &&
				StringUtils.stringIsBlank(createdBy)
				);
				
	}
}
