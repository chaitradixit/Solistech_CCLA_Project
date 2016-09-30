package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class AuroraCorrespondent implements Persistable {

	private int mapId; 		/* PERSON_AURORA_MAP_ID */
	private int personId;	/* PERSON_ID */
	
	private Integer corrId;			/* Aurora Correspondent ID */
	private Company company;	/* Aurora Company */
	
	private Integer reportUsage;
	private boolean reportMailIndicator;
	private boolean reportEmailIndicator;
	
	private Date lastUpdated;
	private String lastUpdatedBy;

	public static class Cols {
		public static final String ID = "PERSON_AURORA_MAP_ID";
		public static final String PERSON = "PERSON_ID";
		public static final String CORR = "CORR_ID";
		public static final String COMPANY = "COMPANY_ID";
		public static final String REPORT_USAGE = "REPORT_USAGE";
		public static final String REPORT_MAIL_INDICATOR = "REPORT_MAIL_INDICATOR";
		public static final String REPORT_EMAIL_INDICATOR = "REPORT_EMAIL_INDICATOR";
		
	}
	
	public AuroraCorrespondent(int mapId, int personId, Integer corrId,
			Company company, Integer reportUsage, boolean reportMailIndicator,
			boolean reportEmailIndicator, Date lastUpdated, String lastUpdatedBy) {
		this.mapId = mapId;
		this.personId = personId;
		this.corrId = corrId;
		this.company = company;
		this.reportUsage = reportUsage;
		this.reportMailIndicator = reportMailIndicator;
		this.reportEmailIndicator = reportEmailIndicator;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public AuroraCorrespondent() {
	}

	public static AuroraCorrespondent add
	 (DataBinder binder, FWFacade facade, String username) throws DataException {
		
		AuroraCorrespondent corr = new AuroraCorrespondent();
		corr.setAttributes(binder);
		
		return add(corr.getPersonId(), corr.getCorrId(), corr.getCompany(), 
		 corr.getReportUsage(), 
		 corr.isReportMailIndicator(), corr.isReportEmailIndicator(), facade, username);
	}
	
	public static AuroraCorrespondent add
	 (int personId, Integer corrId, Company company, 
	 Integer reportUsage, boolean reportMailIndicator, boolean reportEmailIndicator,
	  FWFacade facade, String username) throws DataException {
		
		int mapId = Integer.parseInt
		 (CCLAUtils.getNewKey("PersonAuroraMap", facade));	
		
		if (corrId == null) {
			// Auto-allocate Correspondent Code. Based on either:
			// -existing Correspondent Code, if the Person has one
			// -their PERSON_ID/ELEMENT_ID.
			Vector<AuroraCorrespondent> existingCorrs = getCorrespondentsByPersonId
			 (personId, facade);
			
			if (!existingCorrs.isEmpty()) {
				corrId = existingCorrs.get(0).getCorrId();
			} else {
				corrId = personId;
			}
		}
		
		AuroraCorrespondent corr = 
		 new AuroraCorrespondent(mapId, personId, corrId, company, 
		 reportUsage, reportMailIndicator, reportEmailIndicator, null, username);
		
		DataBinder binder = new DataBinder();
		
		corr.validate(facade);
		corr.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddPersonAuroraMap", binder);
		
		Log.debug("Generated new Aurora Correspondent map: " + 
		 corr.toString());
		
		// Add audit record
		DataResultSet newData = AuroraCorrespondent.getData(mapId, facade);
		
		HashMap<Integer, String> auditRelations = 
		 new HashMap<Integer, String>();
		
		auditRelations.put
		 (mapId, ElementType.SecondaryElementType.PersonAuroraMap.toString());
		
		auditRelations.put
		 (personId, ElementType.PERSON.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.PersonAuroraMap.toString(), 
		 null, newData, auditRelations);

		return corr;
	}
	
	/** Removes an existing Aurora Correspondent mapping.
	 * 
	 * @param mapId
	 * @throws DataException 
	 */
	public static void remove(int mapId, FWFacade facade, String userName) 
	 throws DataException {
		
		AuroraCorrespondent remCorr = get(mapId, facade);
		
		if (remCorr == null)
			throw new DataException("Unable to remove Aurora Correspondent - " +
			 "doesn't exist");
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, mapId);

		DataResultSet remData = AuroraCorrespondent.getData(mapId, facade);
		
		facade.execute("qClientServices_DeletePersonAuroraMap", binder);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = 
		 new HashMap<Integer, String>();
		
		auditRelations.put
		 (mapId, ElementType.SecondaryElementType.PersonAuroraMap.toString());
		auditRelations.put(remCorr.getPersonId(), ElementType.PERSON.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.DELETE.toString(), 
		 ElementType.SecondaryElementType.PersonAuroraMap.toString(), 
		 remData, null, auditRelations);
	}
	
	
	public static Vector<AuroraCorrespondent>
	 getCorrespondentsByPersonId(int personId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsPersonAuroraMap = 
		 getCorrespondentsDataByPersonId(personId, facade);
		
		return get(rsPersonAuroraMap);
	}

	public static Vector<AuroraCorrespondent>
	 getCorrespondentsByPersonIdAndCompanyId(int personId, int companyId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsPersonAuroraMap = 
		 getCorrespondentsDataByPersonIdAndCompanyId(personId, companyId, facade);
		
		return get(rsPersonAuroraMap);
	}	
	
	/** Searches through the passed list of Aurora correspondents and returns one with
	 *  the matching Company, null otherwise.
	 *  
	 * @param corrsMap
	 * @return
	 */
	public static AuroraCorrespondent getByCompany
	 (Vector<AuroraCorrespondent> corrsMap, Company company) {
		
		AuroraCorrespondent corrMap = null;
		
		for (AuroraCorrespondent thisCorrMap : corrsMap) {
			if (thisCorrMap.getCompany().equals(company)) {
				return thisCorrMap;
			}
		}
		
		return corrMap;
	}
	
	/** Fetches a ResultSet of mapped Aurora Correspondent IDs 
	 *  for the given Person ID.
	 *  
	 * @param personId
	 * @param facade
	 * @throws DataException
	 */
	public static DataResultSet getCorrespondentsDataByPersonId
	 (int personId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PERSON_ID", personId);
		
		DataResultSet rsPersonAuroraMap = facade.createResultSet
		 ("qClientServices_GetAuroraCorrespondentsByPersonId", binder);
		
		return rsPersonAuroraMap;
	}
	
	/** Fetches a ResultSet of mapped Aurora Correspondent IDs 
	 *  for the given Person ID.
	 *  
	 * @param personId
	 * @param facade
	 * @throws DataException
	 */
	public static DataResultSet getCorrespondentsDataByPersonIdAndCompanyId
	 (int personId, int companyId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PERSON_ID", personId);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "COMPANY_ID", companyId);
		
		DataResultSet rsPersonAuroraMap = facade.createResultSet
		 ("qClientServices_GetAuroraCorrespondentsByPersonIdAndCompanyId", binder);
		
		return rsPersonAuroraMap;
	}
	
	/** Returns a list of AuroraCorrespondent instances from the passed
	 *  ResultSet, which is expected to contain a set of entries from the
	 *  CCLA_PERSON_AURORA_MAP table.
	 *  
	 * @param rs
	 * @return
	 * @throws DataException 
	 */
	public static Vector<AuroraCorrespondent> get(DataResultSet rs) 
	 throws DataException {
		
		Vector<AuroraCorrespondent> correspondents = 
		 new Vector<AuroraCorrespondent>();
		
		if (rs.isEmpty())
			return correspondents;
		
		do {
			int companyId = CCLAUtils.getResultSetIntegerValue
			 (rs, Cols.COMPANY);
			
			Company company = 
			 Company.getCache().getCachedInstance(companyId);
			
			correspondents.add(
				new AuroraCorrespondent(
				CCLAUtils.getResultSetIntegerValue
				 (rs, Cols.ID),
				 CCLAUtils.getResultSetIntegerValue
				 (rs, Cols.PERSON), 
				 CCLAUtils.getResultSetIntegerValue
				 (rs, Cols.CORR), 
				 company,
				 CCLAUtils.getResultSetIntegerValue(rs, Cols.REPORT_USAGE),
				 CCLAUtils.getResultSetBoolValue(rs, Cols.REPORT_MAIL_INDICATOR),
				 CCLAUtils.getResultSetBoolValue(rs, Cols.REPORT_EMAIL_INDICATOR),
				 rs.getDateValueByName(SharedCols.LAST_UPDATED),
				 rs.getStringValueByName(SharedCols.LAST_UPDATED_BY)
				)
			);
		
		} while (rs.next());
		
		return correspondents;
	}
	
	public static AuroraCorrespondent get(int mapId, FWFacade facade) 
	 throws DataException {
		Vector<AuroraCorrespondent> corrs = get(getData(mapId, facade));
		
		if (corrs.isEmpty())
			return null;
		
		return corrs.get(0);
	}
	
	public static DataResultSet getData(int mapId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, mapId);
		
		return facade.createResultSet
		 ("qClientServices_GetAuroraPerson", binder);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {

		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getMapId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.PERSON, this.getPersonId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.COMPANY, this.getCompany().getCompanyId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.CORR, this.getCorrId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.REPORT_USAGE, this.getReportUsage());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.REPORT_MAIL_INDICATOR, this.isReportMailIndicator());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.REPORT_EMAIL_INDICATOR, this.isReportEmailIndicator());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.LAST_UPDATED, this.getLastUpdated());
		CCLAUtils.addQueryParamToBinder
		 (binder, SharedCols.LAST_UPDATED_BY, this.getLastUpdatedBy());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		
		this.setLastUpdatedBy(username);
		
		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		// Fetch before-data for auditing
		DataResultSet beforeData = getData(this.getMapId(), facade);
		
		facade.execute("qClientServices_UpdatePersonAuroraMap", binder);
		
		// Fetch after-data for auditing
		DataResultSet afterData = getData(this.getMapId(), facade);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = 
		 new HashMap<Integer, String>();
		
		auditRelations.put
		 (mapId, ElementType.SecondaryElementType.PersonAuroraMap.toString());
		auditRelations.put(personId, ElementType.PERSON.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.PersonAuroraMap.toString(), 
		 beforeData, afterData, auditRelations);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setCompany(Company.getCache().getCachedInstance(
		 CCLAUtils.getBinderIntegerValue(binder, Cols.COMPANY)));
		
		this.setCorrId(CCLAUtils.getBinderIntegerValue(binder, Cols.CORR));
		this.setPersonId(CCLAUtils.getBinderIntegerValue(binder, Cols.PERSON));
		
		this.setReportUsage(CCLAUtils.getBinderIntegerValue(binder, Cols.REPORT_USAGE));

		this.setReportMailIndicator
		 (CCLAUtils.getBinderBoolValue(binder, Cols.REPORT_MAIL_INDICATOR));
		
		this.setReportEmailIndicator
		 (CCLAUtils.getBinderBoolValue(binder, Cols.REPORT_EMAIL_INDICATOR));
		
		this.setLastUpdatedBy(binder.getLocal(SharedCols.LAST_UPDATED_BY));
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public Integer getCorrId() {
		return corrId;
	}

	public void setCorrId(Integer corrId) {
		this.corrId = corrId;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Company getCompany() {
		return company;
	}

	public Integer getReportUsage() {
		return reportUsage;
	}

	public void setReportUsage(Integer reportUsage) {
		this.reportUsage = reportUsage;
	}

	public boolean isReportMailIndicator() {
		return reportMailIndicator;
	}

	public void setReportMailIndicator(boolean reportMailIndicator) {
		this.reportMailIndicator = reportMailIndicator;
	}

	public boolean isReportEmailIndicator() {
		return reportEmailIndicator;
	}

	public void setReportEmailIndicator(boolean reportEmailIndicator) {
		this.reportEmailIndicator = reportEmailIndicator;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	@Override
	public String toString() {
		return "AuroraCorrespondent [company=" + company + ", corrId=" + corrId
				+ ", lastUpdated=" + lastUpdated + ", lastUpdatedBy="
				+ lastUpdatedBy + ", mapId=" + mapId + ", personId=" + personId
				+ ", reportEmailIndicator=" + reportEmailIndicator
				+ ", reportMailIndicator=" + reportMailIndicator
				+ ", reportUsage=" + reportUsage + "]";
	}

	/**
	 * Generates a new correspondent number.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static int getNewCorrespondentNumber(FWFacade facade) 
	 throws DataException {
		throw new DataException("Correspondent Number sequence is not in use. " +
		 "Use the person's existing Correspondent Code, or their PERSON_ID instead.");
		
		//return Integer.parseInt(CCLAUtils.getNewKey("AuroraCorrespondentNumber", facade));
	}
	
	/** Fetches Aurora Companies linked to the Person Id via the mapping table.
	 * 
	 * @param personId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getAuroraCompaniesDataByPersonId
	 (Integer personId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Person.Cols.ID, personId);
		
		return facade.createResultSet("qCore_GetAuroraCompaniesByPersonId", binder);
	}
}
