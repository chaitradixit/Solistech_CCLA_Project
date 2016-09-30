package com.ecs.stellent.ccla;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSet;
import intradoc.server.FileService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** All service methods relating to UCM reporting functions */
public class UcmReporting extends FileService {
	
	private final static Map<String, String> jobTypeToStatusMapping = 
	 new HashMap<String, String>(10);

	/** Maximum number of results to display. If the query returns more rows than this, an
	 *  exception is thrown. */
	private static final int MAX_RESULTS_DISPLAY = 5000;
	
	private static LinkedHashMap<String, String> contentReportColMapping = 
	 new LinkedHashMap<String, String>();
	
	private static LinkedHashMap<String, String> documentVolumesColMapping = 
	 new LinkedHashMap<String, String>();
	
	private static enum ViewNames {
		V_DOCUMENT_STATUS,
		V_DOCUMENT_STATUS_WF
	}
	
	static {
		// Add new job statuses here..
		jobTypeToStatusMapping.put("job_jobStarted", "Job Started");
		jobTypeToStatusMapping.put("job_jobNotStarted", "Job Not Started");
		jobTypeToStatusMapping.put("job_jobNotStartedUpload", "Job Not Started (User Uploaded)");
		jobTypeToStatusMapping.put("job_jobNotStartedError", "Job Not Started (ERROR)");
		jobTypeToStatusMapping.put("job_jobNotStartedSharepoint", "Migrated From Sharepoint");
		jobTypeToStatusMapping.put("job_missingIndexValues", "Missing Index Values (ERROR)");
		jobTypeToStatusMapping.put("job_missingBundle", "Missing Bundle (ERROR)");
		jobTypeToStatusMapping.put("job_jobPendingInIris", "Pending In Iris");
		jobTypeToStatusMapping.put("job_jobInLot", "In Parking Lot");
		jobTypeToStatusMapping.put("job_inScanParkingLot", "In 05/03/2009 Parking Lot");
		jobTypeToStatusMapping.put("job_archived", "Archived");
		jobTypeToStatusMapping.put("job_duplicate", "Duplicate");
		jobTypeToStatusMapping.put("job_validation", "Validation");
		jobTypeToStatusMapping.put("job_jobNotStartedLeft", "Job Not Started (Left Behind)");
		jobTypeToStatusMapping.put("job_flagged", "Flagged");
		jobTypeToStatusMapping.put("job_mismatch", "Bundle/Document Mismatch");
		
		// Add column label mappings here..
		contentReportColMapping.put("xBundleRef", "Envelope ID");
		contentReportColMapping.put("xBatchNumber", "Workflow ID");
		contentReportColMapping.put("xSource", "Source");
		contentReportColMapping.put("xDocumentClass", "Doc Class");
		contentReportColMapping.put("xCompany", "Company");
		contentReportColMapping.put("xClientNumber", "Client No.");
		contentReportColMapping.put("xAccountNumber", "Account");
		contentReportColMapping.put("xFund", "Fund");
		contentReportColMapping.put("xAmount", "Cash Amount");
		contentReportColMapping.put("xUnits", "Unit Amount");
		contentReportColMapping.put("dInDate", "Creation Date");
		contentReportColMapping.put("xProcessDate", "Process Date");
		contentReportColMapping.put("xWorkflowDate", "Workflow Date");
		contentReportColMapping.put("ENTERDETAILS_APPROVE_DATE", "EnterDetails Date");
		contentReportColMapping.put("ENTERDETAILS_APPROVE_USER", "EnterDetails User");
		contentReportColMapping.put("VALIDATION_APPROVE_DATE", "Validation Date");
		contentReportColMapping.put("VALIDATION_APPROVE_USER", "Validation User");
		contentReportColMapping.put("STATUS_DESC", "Item Status");
		contentReportColMapping.put("BUNDLE_STATUS", "Bundle Status");
		contentReportColMapping.put("dDocName", "Doc ID");
		contentReportColMapping.put("xComments", "Comments");
		
		documentVolumesColMapping.put("dStatus", "Doc Status");
		documentVolumesColMapping.put("dDocType", "Doc Type");
		documentVolumesColMapping.put("xDocumentClass", "Doc Class");
		documentVolumesColMapping.put("xSource", "Source");
		documentVolumesColMapping.put("xStatus", "Process Status");
		documentVolumesColMapping.put("xCompany", "Company");
		documentVolumesColMapping.put("NUM_DOCS", "No. Docs");
		documentVolumesColMapping.put("CREATE_DATE", "Create Date");
		documentVolumesColMapping.put("CREATE_MONTH", "Create Month");
		documentVolumesColMapping.put("CREATE_YEAR", "Create Year");
	}
	
	/** Dynamically builds the Extended Content Report View query.
	 * 
	 *  Determines which document class checkboxes have been
	 *  selected from the UI and builds the query based on this
	 *  list.
	 * 
	 * @throws Exception
	 */
	public void runExtendedReportViewQuery() throws Exception {
		
		String[] docClasses = StringUtils.stringToArray
		 (m_binder.getLocal("docClasses"));
		String[] jobStatuses = StringUtils.stringToArray
		 (m_binder.getLocal("jobStatuses"));
		
		// Determine whether all document classes/statuses have been selected. 
		// This means we dont need to bother adding complicated OR clauses to the SQL 
		// query
		
		boolean allDocClasses = !StringUtils.stringIsBlank
		 (m_binder.getLocal("allDocumentClasses"));
		boolean allJobStatuses = !StringUtils.stringIsBlank
		 (m_binder.getLocal("allJobStatuses"));
		
		String viewName = ViewNames.V_DOCUMENT_STATUS.toString();
		
		// Determines whether the extended view is used, which contains bundle workflow
		// data.
		boolean includeWorkflowData = !StringUtils.stringIsBlank
		 (m_binder.getLocal("includeWorkflowData"));
		
		boolean processDateIsNull = !StringUtils.stringIsBlank
		 (m_binder.getLocal("nullProcessDate"));
		
		if (includeWorkflowData)
			viewName = ViewNames.V_DOCUMENT_STATUS_WF.toString();
		
		String submitType = m_binder.getLocal("submitType");
		
		String sql = "";
		
		sql += "SELECT * FROM " + viewName + " ";
		sql += "WHERE ";
		
		//Exclude old revisions
		sql += "DREVRANK=0 ";
		
		if (!allDocClasses) {			
			sql += " AND (";
			
			//Populate document class query clause
			for(int i=0; i<docClasses.length; i++){
				if(docClasses[i].length() > 0){
					if(i > 0)
						sql += " OR ";
					
					if (docClasses[i].equals("unclassified"))
						sql += "XDOCUMENTCLASS IS NULL";
					else
						sql += "XDOCUMENTCLASS='" + docClasses[i] + "'";
				}
			}
			
			sql += ") ";
		}

		
		// Check for Source clause
		String source = m_binder.getLocal("source");
		if (!StringUtils.stringIsBlank(source)) {
			sql += " AND XSOURCE='" + source + "'";
		}
		
		// Populate dInDate query clause
		String strDateStart = m_binder.getLocal("startDate");
		String strDateEnd = m_binder.getLocal("endDate");
		
		if(strDateStart.length() > 0){
			checkDateString(strDateStart, QUERY_DATE_LONG_FORMAT);
			
			sql += " AND DINDATE >= TO_DATE('"+ strDateStart +"','dd/MM/yyyy HH24:MI')";
		}
		
		if(strDateEnd.length() > 0){
			checkDateString(strDateEnd, QUERY_DATE_LONG_FORMAT);
			
			sql += " AND DINDATE <= TO_DATE('"+ strDateEnd +"','dd/MM/yyyy HH24:MI')";
		}
		
		// Populate xProcessDate query clause
		if (processDateIsNull) {
			sql += " AND XPROCESSDATE IS NULL";
		} else {
			String strStartProcessDate 	= m_binder.getLocal("startProcessDate");
			String strEndProcessDate 	= m_binder.getLocal("endProcessDate");
			
			if(strStartProcessDate.length() > 0) {
				checkDateString(strStartProcessDate, QUERY_DATE_SHORT_FORMAT);
				
				sql += " AND XPROCESSDATE >= TO_DATE('"
				 + strStartProcessDate +"','dd/MM/yyyy')";
			}
			
			if(strEndProcessDate.length() > 0){
				checkDateString(strEndProcessDate, QUERY_DATE_SHORT_FORMAT);
				
				sql += " AND XPROCESSDATE <= TO_DATE('"
				 + strEndProcessDate +"','dd/MM/yyyy')";
			}
		}
		String duplicateOption = m_binder.getLocal("duplicateOption");
		
		if(!StringUtils.stringIsBlank(duplicateOption)){
			if(duplicateOption.equals("exclude")){
				sql += " AND (XSTATUS!='Duplicate')";
			}else if(duplicateOption.equals("include")){
				//Do nothing, included by default
			}if(duplicateOption.equals("only")){
				sql += " AND (XSTATUS='Duplicate')";
			}
		}
		
		// Document Type (dDocType) filter
		String documentType = m_binder.getLocal("documentTypes");
		
		if(StringUtils.stringIsBlank(documentType) || 
		   documentType.equals("all")){
			//by default all documents in the view are 'Document' and 'ChildDocument'
		}else if(documentType.equals("document")){
			sql += " AND (DDOCTYPE='Document')";
		}else if(documentType.equals("childdocument")){
			sql += " AND (DDOCTYPE='ChildDocument')";
		}
	
		
		if (!allJobStatuses) {
			sql += " AND (";			
			//Populate document class query clause
			
			for(int i=0; i<jobStatuses.length; i++){
				if(jobStatuses[i].length() > 0){
					if(i > 0)
						sql += " OR ";
					
					if (!StringUtils.stringIsBlank
						((String)jobTypeToStatusMapping.get(jobStatuses[i]))) {
						sql += "STATUS_DESC='" + 
						 jobTypeToStatusMapping.get(jobStatuses[i])+"'"; 
					}
				}
			}			
			sql += ") ";
		}
		
		// Sorting 
		String sortColumn = m_binder.getLocal("sortColumn");
		String sortOrder = m_binder.getLocal("sortOrder");
		
		if (!(StringUtils.stringIsBlank(sortColumn) || sortColumn.equals("None")) 
			&& !StringUtils.stringIsBlank(sortOrder)) {
			
			sql +=" ORDER BY "+sortColumn+" "+sortOrder;  
		}
		
		try{
			if(sql.length() > 0){
				Log.info("Executing View SQL: " + sql);
				
				m_binder.putLocal("sqlQuery", sql);				
				ResultSet rsQueryDocs = m_workspace.createResultSetSQL(sql);

				DataResultSet drs = new DataResultSet();
				drs.copy(rsQueryDocs);
				Log.info("Got results: " + drs.getNumRows());
				
				if (drs.getNumRows() > MAX_RESULTS_DISPLAY)
					throw new ServiceException("Too many results returned (" 
					 + drs.getNumRows() + 
					 ", max: " + MAX_RESULTS_DISPLAY + 
					 "). Try and reduce the date range, or make the query more specific");
				
				// Check which submission button the user clicked.
				if (!StringUtils.stringIsBlank(submitType) 
					&& submitType.equals("Generate CSV File")) {
					// User opted to generate CSV File.
					
					String tempDir = DataBinder.getTemporaryDirectory();
					
					SimpleDateFormat fmt = new SimpleDateFormat("ddMMyyyy-HHmmss");
					
					String fileName = 
					 "extContentReport-" + fmt.format(new Date()) + ".csv";
					
					File csvFile = new File(tempDir + "/" + fileName);
					
					StringBuffer sb = CCLAUtils.convertToCSV(drs, contentReportColMapping);
					
					FileWriter fw = new FileWriter(csvFile);
					fw.write(sb.toString());
					fw.close();
						
					m_binder.addTempFile(tempDir + "/" + fileName);

					this.setSendFile(true);
					this.setFile(tempDir + "/" + fileName);
					this.setDownloadName(fileName);

					this.setDownloadFormat("text/csv");
					this.m_binder.putLocal("dExtension", "csv");
					
				} else {
					// Dump the ResultSet onto the binder for display
					m_binder.addResultSet("rsQueryDocs", drs);
				}
			}
		}catch(Exception e){
			Log.info("Unable to execute query: " + e.getMessage(), e);
			throw new Exception("Unable to execute query: " + e.getMessage(), e);
		}
	}

	public static final SimpleDateFormat QUERY_DATE_SHORT_FORMAT =
	 new SimpleDateFormat("dd/MM/yyyy");
	
	public static final SimpleDateFormat QUERY_DATE_LONG_FORMAT = 
	 new SimpleDateFormat("dd/MM/yyyy HH:mm");
	
	/** Throws error if the passed string doesn't match the passed date format.
	 * 
	 * @param dateStr
	 * @throws ServiceException 
	 */
	private static void checkDateString(String dateStr, SimpleDateFormat format) 
	 throws ServiceException {
		
		try {
			format.parse(dateStr);
		} catch (ParseException e) {
			throw new ServiceException("Invalid date format passed: " + dateStr 
			 + ", requires format: " + format.toPattern());
		}
	}
	
	/** TODO
	 * @throws ParseException 
	 * 
	 */
	public void getUserDocumentProcessCounts() throws DataException, ServiceException, 
	 ParseException {
		
		boolean isSubmit = CCLAUtils.getBinderBoolValue(m_binder, "isSubmit");
		
		if (!isSubmit)
			return;
		
		Date startDate = CCLAUtils.getBinderDateValue(m_binder, "startDate");
		Date endDate = CCLAUtils.getBinderDateValue(m_binder, "endDate");
		
		if (startDate==null || endDate==null) {
			throw new ServiceException
			 ("Cannot run report, startDate or endDate is missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryDateParamToBinder(binder, "startDate", startDate);
		CCLAUtils.addQueryDateParamToBinder(binder, "endDate", endDate);
		
		// Raw data - no. docs processed by date/user/hour/step/doc class
		DataResultSet rsDocProcessCountsByHour = facade.createResultSet
		 ("qDocumentsProcessedByHourAndUserAndStepNameAndDocClass", binder);
		
		m_binder.addResultSet("rsDocProcessCountsByHour", rsDocProcessCountsByHour);	
		
		// No. docs processed by date/user/doc class/step
		DataResultSet rsDocProcessCounts = facade.createResultSet
		 ("qDocumentsProcessedByUserAndStepNameAndDocClass", binder);
		
		m_binder.addResultSet("rsDocProcessCounts", rsDocProcessCounts);	
		
		
		// Fetch min/max process hours by date and step name
		DataResultSet rsMinMaxProcessHours = facade.createResultSet
		 ("qMinMaxProcessHoursByStepName", binder);
		
		m_binder.addResultSet("rsMinMaxProcessHours", rsMinMaxProcessHours);
		
		// Create ResultSet containing all required hourly increments for each step
		// name.
		DataResultSet rsProcessHours = new DataResultSet(
		 new String[] {"PROCESS_DATE", "dWfStepName", "ProcessHour"});

		Vector<MinMaxHours> minMaxHoursByDate = new Vector<MinMaxHours>();
		
		if (rsMinMaxProcessHours.first()) {
			do {
				Date processDate = QUERY_DATE_SHORT_FORMAT.parse
				 (rsMinMaxProcessHours.getStringValueByName("PROCESS_DATE"));
				
				String stepName = rsMinMaxProcessHours.getStringValueByName
				 ("dWfStepName");
				
				String minHour = rsMinMaxProcessHours.getStringValueByName
				 ("MIN_PROCESS_HOUR");
				
				String maxHour = rsMinMaxProcessHours.getStringValueByName
				 ("MAX_PROCESS_HOUR");
				
				MinMaxHours minMaxHours = new MinMaxHours();
				
				minMaxHours.setProcessDate(processDate);
				minMaxHours.setStepName(stepName);
				minMaxHours.setMinHour(minHour);
				minMaxHours.setMaxHour(maxHour);
				
				minMaxHoursByDate.add(minMaxHours);
				
			} while (rsMinMaxProcessHours.next());
		}

		NumberFormat hourFormat = NumberFormat.getInstance();
		hourFormat.setMinimumIntegerDigits(2);
		
		for (MinMaxHours minMaxHours : minMaxHoursByDate) {
			// Generate all required hour increments per date/step.
			int minHour = Integer.parseInt(minMaxHours.getMinHour().substring(0, 2));
			int maxHour = Integer.parseInt(minMaxHours.getMaxHour().substring(0, 2));

			for (int i = minHour; i<= maxHour; i++) {
				Vector<String> v = new Vector<String>();
				
				String thisHour = hourFormat.format(i) + ":00";
				
				v.add(QUERY_DATE_SHORT_FORMAT.format(minMaxHours.getProcessDate()));
				v.add(minMaxHours.getStepName());
				v.add(thisHour);
				
				rsProcessHours.addRow(v);
			}
		}
		
		m_binder.addResultSet("rsProcessHours", rsProcessHours);
	}
	
	public static class MinMaxHours {
		
		private String stepName;
		private Date processDate;
		
		private String minHour;
		private String maxHour;
		
		public String getMinHour() {
			return minHour;
		}
		public void setMinHour(String minHour) {
			this.minHour = minHour;
		}
		public String getMaxHour() {
			return maxHour;
		}
		public void setMaxHour(String maxHour) {
			this.maxHour = maxHour;
		}
		public void setProcessDate(Date processDate) {
			this.processDate = processDate;
		}
		public Date getProcessDate() {
			return processDate;
		}
		public void setStepName(String stepName) {
			this.stepName = stepName;
		}
		public String getStepName() {
			return stepName;
		}
	}
	
	public void getContentSummaryReport() throws DataException, ServiceException {
		
		Date fromDate = CCLAUtils.getBinderDateValue(m_binder, "fromDate");
		Date toDate = CCLAUtils.getBinderDateValue(m_binder, "toDate");
		
		if (fromDate==null || toDate==null) {
			throw new ServiceException
			 ("Cannot run content summary report, fromDate or toDate is missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryDateParamToBinder(binder, "fromDate", fromDate);
		CCLAUtils.addQueryDateParamToBinder(binder, "toDate", toDate);
		
		DataResultSet rsReport = facade.createResultSet
		 ("qIrisSummary", binder);
		
		m_binder.addResultSet("rsReport", rsReport);	
	}
	
	/** Fetches 2 ResultSets:
	 * 
	 *  -rsODCBundleDocCounts: aggregate data from the ODC ecAudit table
	 *  -rsUCMBundleDocCounts: aggregate data from UCM document tables
	 *  
	 *  These are used to build and display a 'reconcilliation report' between ODC
	 *  and UCM.
	 *  
	 *  Each query requires a date parameter - this is truncated and used a limit date 
	 *  when fetching data. If this isn't present in the binder, the current date is 
	 *  used instead.
	 * @throws ParseException 
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void getODCRecReport() throws ParseException, DataException, ServiceException {
		
		String dateStr = m_binder.getLocal("reportDate");
		Date checkDate = new Date();
		
		if (StringUtils.stringIsBlank(dateStr)) {
			m_binder.putLocalDate("reportDate", checkDate);
		}
		
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace);
		
		// UCM committed bundle/doc counts
		DataResultSet rsUCMBundleDocCounts = 
		 ucmFacade.createResultSet("qReports_GetUCMBundleDocCounts", m_binder);
		
		m_binder.addResultSet("rsUCMBundleDocCounts", rsUCMBundleDocCounts);
	
		// ODC committed bundle/doc counts
		FWFacade odcFacade = CCLAUtils.getFacade(OdcReporting.getOdcWorkspace());
		DataResultSet rsODCBundleDocCounts = 
		 odcFacade.createResultSet("qReports_GetODCBundleDocCounts", m_binder);
			
		m_binder.addResultSet("rsODCBundleDocCounts", rsODCBundleDocCounts);
		
		// Full list of 'Commit Document' custom audit messages from ODC ecAudit table,
		// for the given date
		DataResultSet rsODCCommitDocumentAudits = 
		 odcFacade.createResultSet("qReports_GetODCCommitDocumentAudits", m_binder);
		
		m_binder.addResultSet("rsODCCommitDocumentAudits", rsODCCommitDocumentAudits);
		
		// Fetch list of File Cabinet names
		DataResultSet rsODCFileCabinetNames = odcFacade.createResultSet
		 ("qReports_GetODCFileCabinetNames", m_binder);
		
		m_binder.addResultSet("rsODCFileCabinetNames", rsODCFileCabinetNames);
		
		// Fetch aggregate report of oustanding ODC batches, grouped by Filing Cabinet
		// and Status
		DataResultSet rsODCBatchesByFilingCabinetAndStatus = 
		 odcFacade.createResultSet
		 ("qReports_GetODCBatchesByFilingCabinetAndStatus", m_binder);
	
		m_binder.addResultSet("rsODCBatchesByFilingCabinetAndStatus", 
		 rsODCBatchesByFilingCabinetAndStatus);	
	}
	
	/** Fetches a single ResultSet of document volumes for a given time report.
	 *  
	 *  Requires a startDate and endDate present in the binder.
	 *  
	 *  If the 'submitType' var is 'Generate CSV File', the ResultSet is converted to
	 *  a CSV representation and served to the user as a file download
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void getDocumentVolumesReport() throws ServiceException, DataException {
		
		String submitType = m_binder.getLocal("submitType");
		
		if (StringUtils.stringIsBlank(submitType)) {
			// Report has not been submitted. Insert default values into binder
			
			// Default start/end dates: start of previous month/start of current month.
			Calendar cal = new GregorianCalendar();
			
			int curMonth = cal.get(Calendar.MONTH)+1;
			int curYear = cal.get(Calendar.YEAR);
			
			cal.add(Calendar.MONTH, -1);
			int prevMonth = cal.get(Calendar.MONTH)+1;
			int prevYear = cal.get(Calendar.YEAR);
			
			NumberFormat monthFormat = NumberFormat.getInstance();
			monthFormat.setMinimumIntegerDigits(2);
			
			m_binder.putLocal("startDateStr",
				"01/" 
				+ monthFormat.format(prevMonth) + "/" 
				+ prevYear
			);
			
			m_binder.putLocal("endDateStr",
				"01/" 
				+ monthFormat.format(curMonth) + "/" 
				+ curYear
			);
			
			return;
		}
		
		Date startDate, endDate;
		
		try {
			startDate = CCLAUtils.getBinderDateValue(m_binder, "startDateStr");
			endDate = CCLAUtils.getBinderDateValue(m_binder, "endDateStr");
		} catch (DataException e) {
			throw new ServiceException("Report Start/End Date has invalid format. " +
			 "Ensure they are both in dd/mm/yyyy format");
		}

		
		if (startDate == null || endDate == null) {
			throw new ServiceException("Report Start/End Date missing");
		}
		
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace);
		
		CCLAUtils.addQueryDateParamToBinder(m_binder, "startDate", startDate);
		CCLAUtils.addQueryDateParamToBinder(m_binder, "endDate", endDate);
		
		// UCM committed bundle/doc counts
		DataResultSet rsDocumentVolumes = 
		 ucmFacade.createResultSet("qReports_GetDocumentVolumesByDateRange", m_binder);
		
		if (submitType.equals("Generate CSV File")) {
			// User opted to generate CSV File.
			String tempDir = DataBinder.getTemporaryDirectory();
			
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			
			String fileName = 
			 "UCM Document Volumes " 
			 + sdf.format(startDate) 
			 + "-" 
			 + sdf.format(endDate) 
			 + ".csv";
			
			File csvFile = new File(tempDir + "/" + fileName);
			
			StringBuffer sb = CCLAUtils.convertToCSV
			 (rsDocumentVolumes, documentVolumesColMapping);
			
			LWDocument reportDoc = new LWDocument();
			
			reportDoc.setDocType("Report");
			reportDoc.setTitle(fileName);
			reportDoc.setSecurityGroup("Public");
			reportDoc.setAuthor(m_userData.m_name);
			
			FileWriter fw;
			try {
				fw = new FileWriter(csvFile);
				fw.write(sb.toString());
				fw.close();
				
			} catch (IOException e) {
				throw new ServiceException
				 ("Unable to generate CSV file: " + e.getMessage(), e);
			}
			
			String docName;
			
			try {
				// Check the file into the Content Server
				docName = reportDoc.checkin(csvFile);
			} catch (Exception e) {
				throw new ServiceException
					("Failed to checkin report document, " + e.getMessage());
			}
			
			m_binder.putLocal("reportDocName", docName);
	
			/*
			m_binder.addTempFile(tempDir + "/" + fileName);

			this.setSendFile(true);
			this.setFile(tempDir + "/" + fileName);
			this.setDownloadName(fileName);

			this.setDownloadFormat("text/csv");
			this.m_binder.putLocal("dExtension", "csv");
			*/
			
		} else {
			// Dump the ResultSet onto the binder for display
			m_binder.addResultSet("rsDocumentVolumes", rsDocumentVolumes);
		}
	}
	
	/** Shared flag which provides a basic lock for getClientDataExport calls. These are
	 *  very system-intensive so we use this lock to prevent concurrent executions.
	 */
	private static boolean isExportingClientData = false;
	
	private static String clientDataExportUser = null;
	private static Date clientDataExportDate = null;

	public void getClientDataExportStatus() {
		// Add current client data fetch state info, if available.
		CCLAUtils.addQueryBooleanParamToBinder
		 (m_binder, "isExportingClientData", isExportingClientData);
		
		CCLAUtils.addQueryParamToBinder
		 (m_binder, "clientDataExportUser", clientDataExportUser);
		
		CCLAUtils.addQueryDateParamToBinder
		 (m_binder, "clientDataExportDate", clientDataExportDate);
	}
	
	/** Fetches a single ResultSet containing a full extract of client/person details for 
	 *  a given Company.
	 *  
	 *  There are various flags that can be set on the form that affect the returned
	 *  results.
	 *  
	 *  The service call is expected to take a few minutes to complete in the worst case.
	 *  
	 *  The ResultSet is converted to a CSV representation and served to the user as a 
	 *  file download.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void getClientDataExport() throws ServiceException, DataException {
		
		String submitType = m_binder.getLocal("submitType");
		
		if (StringUtils.stringIsBlank(submitType)) {
			// Form hasn't been submitted yet. Assign any default form values and input
			// lists here.
			getClientDataExportStatus();
			
			return;
		}
		
		if (isExportingClientData) {
			// Export in progress - fail.
			throw new ServiceException("Client Data Export already in progress. " +
			 "Please wait for it to complete.");
		}
		
		synchronized (this) {
			// Switch on lock.
			isExportingClientData = true;
			
			clientDataExportUser = m_userData.m_name;
			clientDataExportDate = new Date();
			
			try {
			
				Integer companyId = CCLAUtils.getBinderIntegerValue(m_binder, "companyId");
				Company company = Company.getCache().getCachedInstance(companyId);
				
				// Fetch query parameters. Each one should map to one of the 3 values:
				// 0, 1, %
				String includeDataProtection = m_binder.getLocal("includeDataProtection");
				String includeDeceased = m_binder.getLocal("includeDeceased");
				String includeReturnedMail = m_binder.getLocal("includeReturnedMail");
				
				Log.debug("User " + m_userData.m_name + " Generating Client Data Export CSV");
				Log.debug(
				 "Company: " + company.getCode() +
				 ", includeDataProtection? " + includeDataProtection +
				 ", includeDeceased? " + includeDeceased +
				 ", includeReturnedMail? " + includeReturnedMail);
				
				// Audit report request.
				/*
				AuditUtils.addAuditEntry("CCLA", "CLIENT-DATA-EXPORT", 
				 company.getCode(), 
				 company.getCode(), 
				 m_userData.m_name,
				 "Executed Client Data Report for Company: " + company.getCode(),
				 new Vector<String>());
				*/
				
				FWFacade cdbFacade = CCLAUtils.getFacade(true);
				
				Log.debug("Determining total record count...");
				
				// Fetch total record count, based on passed filter params.
				DataResultSet rsRecordCount = 
				 cdbFacade.createResultSet("qReports_GetClientDataExportRecordCount", m_binder);
				
				int numRows = CCLAUtils.getResultSetIntegerValue(rsRecordCount, "ROW_COUNT");
				Log.debug("Total no. of records: " + numRows);
				
				Log.debug("Running fetch query...");
				
				// Fetch client data in paged sets.
				DataResultSet rsClientData = 
				 cdbFacade.createResultSet("qReports_GetClientDataExport", m_binder);
				
				Log.debug("Client Data fetched. " + rsClientData.getNumRows() + 
				 " total rows. Building CSV...");
				
				String tempDir = DataBinder.getTemporaryDirectory();
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy HHmm");
				
				String fileName = company.getCode() + " Client Data Export " 
				 + sdf.format(new Date()) 
				 + ".csv";
				
				File csvFile = new File(tempDir + "/" + fileName);
				
				StringBuffer sb = CCLAUtils.convertToCSV(rsClientData, null);
				
				FileWriter fw;
				try {
					fw = new FileWriter(csvFile);
					fw.write(sb.toString());
					fw.close();
				} catch (IOException e) {
					throw new ServiceException
					 ("Unable to generate CSV file: " + e.getMessage(), e);
				}
				
				Log.debug("CSV generated with file name " + fileName + 
					", checking in to Content Server...");
				
				LWDocument reportDoc = new LWDocument();
				
				reportDoc.setDocType("Report");
				reportDoc.setTitle(fileName);
				reportDoc.setSecurityGroup("Public");
				reportDoc.setAuthor(m_userData.m_name);
				
				String docName;
				
				try {
					// Check the file into the Content Server
					docName = reportDoc.checkin(csvFile);
				} catch (Exception e) {
					throw new ServiceException
						("Failed to checkin report document, " + e.getMessage());
				}
				
				m_binder.putLocal("reportDocName", docName);
				
				/*
				m_binder.addTempFile(tempDir + "/" + fileName);
		
				this.setSendFile(true);
				this.setFile(tempDir + "/" + fileName);
				this.setDownloadName(fileName);
		
				this.setDownloadFormat("text/csv");
				this.m_binder.putLocal("dExtension", "csv");
				*/
				
			} finally {
				// Remove lock
				Log.debug("Removing client data export lock");
				isExportingClientData = false;
			}
		}
	}
}
