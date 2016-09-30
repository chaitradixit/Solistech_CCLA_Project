package com.ecs.stellent.ccla.signature;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;
import intradoc.shared.UserData;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.ecs.stellent.ccla.signature.data.InstructionSignature;
import com.ecs.stellent.ccla.signature.data.PersonSignature;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

//This class handles all the services that are needed for signature image functionality
public class SignatureServices extends Service {
	
	/** Stores a list of all Relation Name IDs who are considered to be 'signatories'
	 *  for an account.
	 */
	public static Vector<Integer> SIGNATORY_RELATION_NAME_IDS = new Vector<Integer>();
	
	static {
		String sigRelNameIds = SharedObjects.getEnvironmentValue
		 ("CCLA_SC_SIGNATORY_RELATION_NAME_IDS");
		
		if (!StringUtils.stringIsBlank(sigRelNameIds)) {
			String[] ids = sigRelNameIds.split(",");
			
			for (String id : ids) {
				SIGNATORY_RELATION_NAME_IDS.add(Integer.parseInt(id));
			}
		}
	}
	
	/**
	 *  This method will perform two actions dependant on the sigAction binder value
	 *  if sigAction = update then it will up-revision a signature image for a specific 
	 *  user else it will check in a new image
	 *  
	 *  Called by service CCLA_SC_SIG_MODIFY_SERVICE
	 *  
	 * @throws ServiceException 
	 */
	public void modifySignature() throws ServiceException, DataException{

		String personId="";
		String sigDocGuid="";
		String author = m_binder.getLocal("dDocAuthor");
		if (StringUtils.stringIsBlank(author)){
			throw new ServiceException ("updateSignature: author not on binder");
		}
		
		String sigAction = m_binder.getLocal("sigAction");
		if (StringUtils.stringIsBlank(sigAction)){
			throw new ServiceException ("updateSignature: sigAction not on binder");
		}
		
		String sourceDocIdStr = m_binder.getLocal("sourceDocId");
		Integer sourceDocId = null;
		
		if (StringUtils.stringIsBlank(sourceDocIdStr)) {
			if (PersonSignature.REQUIRE_SOURCE_DOC_ID)
				throw new ServiceException
				 ("Unable to add or update Person Signature: no Source Doc ID");
			
		} else {
			sourceDocId = Integer.parseInt(sourceDocIdStr);
		}

		
		
		boolean updateSig = false;
    	if (sigAction.equals("update")){
    		updateSig=true;
    	}

		String RedirectUrl = m_binder.getLocal("RedirectUrl");
		if (StringUtils.stringIsBlank(RedirectUrl)){
			throw new ServiceException ("updateSignature: RedirectUrl not on binder");
		}
	    
		
		String filename = m_binder.getLocal("datafile");
	    if (filename != null && filename.length() > 0) {
	    	filename = (new File(filename)).getName();
	    }
	    
	    Vector binderAttachments = m_binder.getTempFiles();
	    File file = null;
	    for (int i=0; i<binderAttachments.size(); i++) {
	    	String fileAttachment = (String)binderAttachments.get(i);
	    	file = new File(fileAttachment);

	    	if (filename == null || filename.length() == 0) {
	    		filename = (new File(fileAttachment)).getName();
	    	}
	    }
	    
	    
    	BufferedImage bimg = null;
		try {
			bimg = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("Unable to read image, "+e.getMessage());			
		}
    	
    	if (PersonSignature.MAX_IMAGE_HEIGHT < bimg.getHeight()){
    		Log.info("updateSignature: image too tall");
			if (updateSig){
				m_binder.putLocal("RedirectUrl",RedirectUrl + 
				 "&sigAction=update&imageTooTall=1");
			} else {
				m_binder.putLocal("RedirectUrl",RedirectUrl +
				 "&sigAction=add&imageTooTall=1");
			}
    	}
    	else if (PersonSignature.MAX_IMAGE_WIDTH < bimg.getWidth()){
    		
    		Log.info("updateSignature: image too wide");
			if (updateSig){
				m_binder.putLocal("RedirectUrl",RedirectUrl + 
				 "&sigAction=update&imageTooWide=1");
			} else {
				m_binder.putLocal("RedirectUrl",RedirectUrl + 
				 "&sigAction=add&imageTooWide=1");
			}

		} else {
   		 	// TODO if checked out, undo checkout?
			LWDocument lwDoc = null;
			FWFacade cdbFacade = CCLAUtils.getFacade(m_workspace, true);
			
			if (updateSig){
				//update existing signature document
				sigDocGuid = m_binder.getLocal("sigDocGuid");
    			if (StringUtils.stringIsBlank(sigDocGuid)){
    				throw new ServiceException
    				 ("updateSignature: sigDocGuid not on binder");
    			}
    			try {
					lwDoc = CCLAUtils.getLatestLwdFromDocGuid(sigDocGuid);
					
	    			personId = lwDoc.getAttribute("xPersonId");

	    			lwDoc.checkout();
	    			lwDoc.setAuthor(author);
					lwDoc.checkin(file);

    			} catch (Exception e) {
    				String msg = "Unable to update signature with sigDocGuid: "
    				 +sigDocGuid+", "+e.getMessage();
    				
     				Log.error(msg, e);
     				throw new ServiceException(msg, e);				
				}
    			
    			try {
    				cdbFacade.beginTransaction();
    				
    				PersonSignature.update
    				 (Integer.parseInt(personId), sourceDocId, author, cdbFacade);
    				
            		m_binder.putLocal("RedirectUrl",RedirectUrl + "&sigAction=updated");
            		
            		cdbFacade.commitTransaction();
    			} catch (Exception e) {
    				cdbFacade.rollbackTransaction();
    				String msg = "Unable to update signature: " + e.getMessage();
   				
    				Log.error(msg, e);
    				throw new ServiceException(msg, e);		
    			}
				
			} else {
				// create a new signature document	
    			lwDoc = new LWDocument();
    			lwDoc.useDatabase();
    			lwDoc.setAttributes(m_binder.getLocalData());
    			
        		m_binder.putLocal("RedirectUrl",RedirectUrl + "&sigAction=added");
        		String docName = "";
        		try {
         			personId = lwDoc.getAttribute("xPersonId");
     				docName = lwDoc.checkin(file);
     			} catch (Exception e) {
     				String msg = "Unable to checkin signature. "+e.getMessage();
     				
     				Log.error(msg, e);
     				throw new ServiceException(msg, e);			
     			}
     			
     			//Now add the docname to the PERSON_SIGNATURE table
				try {
    				cdbFacade.beginTransaction();
    				
    				PersonSignature.add(Integer.parseInt(personId),docName, sourceDocId, 
    				 author, CCLAUtils.getFacade(m_workspace));
            		
            		cdbFacade.commitTransaction();
    			} catch (Exception e) {
    				cdbFacade.rollbackTransaction();
    				String msg = "Unable to add signature: " + e.getMessage();
   				
    				Log.error(msg, e);
    				throw new ServiceException(msg, e);		
    			}
			}
    	}
		   
	}

	
	/**
	 * This method will return a file object from the file in the binder
	 * @param binder
	 * @return
	 */
	private File getFileFromBinder(DataBinder binder) {
	    File file = null;

		String filename = binder.getLocal("datafile");
	    if (filename != null && filename.length() > 0) {
	    	filename = (new File(filename)).getName();
	    }
	    Vector binderAttachments = binder.getTempFiles();

	    for (int i=0; i<binderAttachments.size(); i++) {
	    	String fileAttachment = (String)binderAttachments.get(i);
	    	file = new File(fileAttachment);
	    	if (filename == null || filename.length() == 0) {
	    		filename = (new File(fileAttachment)).getName();
	    	}
	    }
	    return file;
	}

	
	// This method expires a specific content item instantly
	// Called by service CCLA_SC_SIG_DELETE_SERVICE
	public void deleteSignature() throws ServiceException, DataException{
		String sigDocGuid = m_binder.getLocal("sigDocGuid");
		if (StringUtils.stringIsBlank(sigDocGuid)){
			throw new ServiceException ("deleteSignature: sigDocGuid not on binder");
		}
		
		//Now remove the personId row in the PERSON_SIGNATURE table
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			
			String currentTime = DateFormatter.getTimeStamp();
			String personId = "";
			
			LWDocument lwDoc;
			try {
				lwDoc = CCLAUtils.getLatestLwdFromDocGuid(sigDocGuid);
				personId = lwDoc.getAttribute("xPersonId");
				PersonSignature.remove(Integer.parseInt(personId),
						facade, m_userData.m_name);
				
				lwDoc.setAttribute("dOutDate", currentTime);
				lwDoc.update();
			} catch (Exception e) {
				String msg = "Unable to expire document. "+e.getMessage();
				
				Log.error(msg, e);
				throw new ServiceException(msg, e);			
			}		
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Failed to remove Person Signature record: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Service method which will place two flag variables in the binder:
	 * 
	 *  'signatureCheckRequired' if the given DOC_CLASS requires signature checking.
	 *  'signatureCaptureAllowed' if the given DOC_CLASS is not a transaction document.
	 *  Capturing signatures against transaction docs is forbidden. This behaviour will
	 *  only apply if the PersonSignature.PREVENT_TRANSACTION_CAPTURE flag is set.
	 *  
	 *  Called by UCM service CCLA_SC_GET_SIGS_FOR_ACCOUNT
	 *  
	 * @throws DataException 
	 *  
	 */
	public void getIsSignatureCheckRequired() throws DataException {
		
		String docClassName	  = m_binder.getLocal("DOC_CLASS");
		
		if (!StringUtils.stringIsBlank(docClassName)) {
			DocumentClass docClass =
			DocumentClass.getCache().getCachedInstance(docClassName);
			
			if (docClass != null) {
				if (docClass.isSignaturesRequired())
					m_binder.putLocal("signatureCheckRequired", "1");
				else
					m_binder.putLocal("signatureCheckRequired", "");
				
				if (!docClass.isTransaction() || 
					!PersonSignature.PREVENT_TRANSACTION_CAPTURE)
					m_binder.putLocal("signatureCaptureAllowed", "1");
				else
					m_binder.putLocal("signatureCaptureAllowed", "");
				
			} else {
				throw new DataException("Unknown Document Class: " + docClassName);
			}
		}
	}
	
	/**
	 * This method will get a list of dDocNames of signature content items 
	 * from a given ACCOUNT_ID on the binder. 
	 * 
	 * Called by UCM service CCLA_SC_GET_SIGS_FOR_ACCOUNT 
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void getSigsForAccount() throws ServiceException, DataException{
		Integer accountID = CCLAUtils.getBinderIntegerValue(m_binder, Account.Cols.ID);
		if (accountID == null){
			throw new ServiceException ("getAllSigsForAccount: ACCOUNT_ID not on binder");
		}

		FWFacade cdbFacade = CCLAUtils.getFacade(m_workspace, true);
		
		// Get all people who are signatories of the given account
		Vector<Relation> relations = null;

		// The line below will appear to be in error in the Eclipse IDE. It isn't.
		// Fetch all Person-Account relations for the account.
		relations = 
		 Relation.getRelations(null, accountID, 
		  RelationType.getCache().getCachedInstance(RelationType.PERSON_ACCOUNT), 
		  null, cdbFacade);
		
	    // if is not empty get the signature docNames
		if (!relations.isEmpty()){
			
			// Loop through the relations and extract the Person IDs for the 'signatory'
			// relations
			String signatoryIds = "";
			for (Relation relation : relations) {
				boolean sigRelation = false;
				
				for (Integer sigRelationNameId : SIGNATORY_RELATION_NAME_IDS) {
					if (sigRelationNameId 
						== relation.getRelationName().getRelationNameId()) {
						sigRelation = true;
						break;
					}
				}
				
				if (sigRelation) {
					if(signatoryIds.length()!=0)
						signatoryIds = signatoryIds + ",";
					signatoryIds = signatoryIds + relation.getElementId1();
				}
			}
			
			Log.info("signatoryIds: "+signatoryIds);
			// Now add the list of person ids to the binder and execute the following
			// query to get a result set of sigDocNames and personId's
			m_binder.putLocal("signatoryIds", signatoryIds);
			DataResultSet sigDocNames = cdbFacade.createResultSet
			 ("qSignatureChecking_GetSigDocNames", m_binder);
			
			m_binder.addResultSet("sigDocNames", sigDocNames);
			
			// This will return all people related with a specific account
			// TODO however we only need to return the signatories, i.e. those who
			// have a 'signatory' relation defined in SIGNATURE_RELATION_NAMES.
			
			// Add persons related to this entity
			DataResultSet rsPersonsInAccount = Relation.getRelatedPersonsData
			 (accountID, ElementType.ACCOUNT, cdbFacade);
			
			m_binder.addResultSet("rsPersonsInAccount", rsPersonsInAccount);
		}
	}
	
	
	/**
	 * This method will update the approved signatures for a document
	 * 
	 * Called by Service: CCLA_SC_UPDATE_APPROVED_SIGS
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void updateApprovedSigsForDoc() throws ServiceException, DataException {
		
		String docIdStr 	= m_binder.getLocal("DOC_ID");
		String accountIdStr = m_binder.getLocal("ACCOUNT_ID");
		String docGuidStr 		= m_binder.getLocal(SharedCols.GUID);
		
		if (StringUtils.stringIsBlank(docIdStr) && StringUtils.stringIsBlank(docGuidStr)){
			throw new ServiceException("DOC_ID or DOC_GUID is not on binder");
		}
		
		if (StringUtils.stringIsBlank(accountIdStr)){
			throw new ServiceException("ACCOUNT_ID not on binder");
		}

		Integer docId = null;
		String docGuid = null;
		
		//Work out docGuid and docId values from either the DOC_GUID or DOC_ID
		
		if (!StringUtils.stringIsBlank(docGuidStr)) {
			
			try {
				LWDocument lwDoc = CCLAUtils.getLatestLwdFromDocGuid(docGuidStr);	
				
				if (lwDoc==null) {
					String err = "No document found with guid: "+docGuidStr;
					Log.error(err);
					throw new ServiceException(err);				
				}
				
				docGuid = docGuidStr;
				docId = Integer.parseInt(lwDoc.getId());

			} catch (Exception e) {
				String err = "unable to generate DOC_GUID: " + e.getMessage();
				Log.error(err);
				throw new ServiceException(err);				
			}
			
		} else if (!StringUtils.stringIsBlank(docIdStr)) {
			
			try {
				docId = Integer.parseInt(docIdStr);	
				docGuid = CCLAUtils.getDocGuidFromDid(docId);
			} catch (Exception e1) {
				String err = "unable to generate DOC_GUID: " + e1.getMessage();
				Log.error(err);
				throw new ServiceException(err);
			}
			Log.debug("Updating approved signatures for DOC_GUID: " + docGuid);
		}
		
		int accountId		= Integer.parseInt(accountIdStr);
		
		// Comma-separated list of Person IDs
		String sigsApprovedStr = m_binder.getLocal("sigsApproved");
		String[] sigsApproved = null;
		
		if (!StringUtils.stringIsBlank(sigsApprovedStr))
			sigsApproved = sigsApprovedStr.split(",");
		else
			sigsApproved = new String[0];	
		
		FWFacade f = CCLAUtils.getFacade(m_workspace, true);
		
		// Fetch existing list of approved/checked signatures for this doc
		Vector<InstructionSignature> existingSigs = 
		 InstructionSignature.getAllByDocGuid(docGuid, f);
		
		Log.debug("Found " + existingSigs.size() + " existing approved signatures");
		Log.debug("Found " + sigsApproved.length + " approved signatures in binder");

	   	try {
	   		f.beginTransaction();
	   		
	   		// Add any newly-approved signatures to the DB
			for (int i=0; i<sigsApproved.length; i++) {
				int thisPersonId = Integer.parseInt(sigsApproved[i]);
				
				boolean isNew = true;
				
				for (InstructionSignature existingSig : existingSigs) {
					if (existingSig.getPersonId() == thisPersonId) {
						// Signature was already approved, do nothing.
						isNew = false;
						break;
					}
				}
				
				if (isNew) {
					Log.debug("Adding new approved signature for Person ID: " 
					 + thisPersonId);
					
					InstructionSignature.addByDocGuid(docGuid,thisPersonId, 
					 f, m_userData.m_name);
				}
			}
			
			// Delete any unchecked signatures which were previously approved.
			for (InstructionSignature existingSig : existingSigs) {
				boolean isDeleted = true;
				
				for (int i=0; i<sigsApproved.length; i++) {
					int thisPersonId = Integer.parseInt(sigsApproved[i]);
					
					if (existingSig.getPersonId() == thisPersonId) {
						isDeleted = false;
					}
				}
				
				if (isDeleted) {
					Log.debug("Removing approved signature for Person ID: " 
					 + existingSig.getPersonId());
					
					InstructionSignature.removeByDocGuid(docGuid, 
					 existingSig.getPersonId(), f,  m_userData.m_name);
				}
			}
			
			f.commitTransaction();
			
		} catch (Exception e) {
			f.rollbackTransaction();
			
			String msg = "Error updating items in COMM_SIGNATURE table " +
			 "for document " + docId + ": " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
		
		// Now determine whether there is a sufficient/valid number of signatures.
		boolean signaturesValid = isSignaturesValid(sigsApproved.length, accountId, f);
		
		// Update the SignaturesValid metadata field on the associated document.
		try {
			LWDocument lwDoc = new LWDocument(docId, true);
			
			if (signaturesValid)
				lwDoc.setAttribute(UCMFieldNames.SignaturesValid, "1");
			else
				lwDoc.setAttribute(UCMFieldNames.SignaturesValid, "0");
			
			lwDoc.update();
		} catch (Exception e) {
			String msg = "Error updating SignaturesValid field on document  " +
			 docId;
		
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Returns whether or not the passed number of signatures is more than or
	 *  equal to the required signatures for the passed accout.
	 *  
	 * @param numSigs
	 * @param accountId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	private static boolean isSignaturesValid
	 (Integer numSigs, int accountId, FWFacade facade) throws DataException {
		
		Account account 		= Account.get(accountId, facade);
		Integer reqSignatures 	= account.getRequiredSignatures();
		
		return (reqSignatures != null && numSigs >= reqSignatures);
	}
	
	/**
	 * This method will get all approved signatures for a given DOC_ID/DOC_GUID
	 * and adds the resultset rsApprovedSigs to the binder
	 * CALLED BY SERVICE: CCLA_SC_GET_APPROVED_SIGS
	 * 
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public void getApprovedSigsForDoc() throws ServiceException, DataException{
		String docIdStr = m_binder.getLocal(SharedCols.DOC);
		String docGuidStr = m_binder.getLocal(SharedCols.GUID);
		
		if (StringUtils.stringIsBlank(docIdStr) && StringUtils.stringIsBlank(docGuidStr))
			throw new ServiceException ("DOC_ID or DOC_GUID not on binder");
		
		FWFacade cdbFacade = CCLAUtils.getFacade(m_workspace, true);
		
		String docGuid = null;
		
		if (!StringUtils.stringIsBlank(docGuidStr)) {
			try {
				LWDocument lwDoc = CCLAUtils.getLatestLwdFromDocGuid(docGuidStr);	
				
				if (lwDoc==null) {
					String err = "No document found with guid: "+docGuidStr;
					Log.error(err);
					throw new ServiceException(err);				
				}

				docGuid = docGuidStr;
				Log.info("docGuid: " + docGuid);
				DataResultSet rsApprovedSigs = InstructionSignature.getAllDataByDocGuid(docGuid, cdbFacade);
				
				m_binder.addResultSet("rsApprovedSigs",rsApprovedSigs);
				
			} catch (Exception e) {
				String msg = "Unable to execute query: " +
				 "qSignatureChecking_GetApprovedSigsForDoc";
				
				Log.error(msg, e);
				throw new ServiceException(msg, e); 			
			}	
		} else if (!StringUtils.stringIsBlank(docIdStr)) {
			try {
				int docId = Integer.parseInt(docIdStr);
				docGuid = CCLAUtils.getDocGuidFromDid(docId);
				Log.info("docGuid: " + docGuid);
				DataResultSet rsApprovedSigs = InstructionSignature.getAllDataByDocGuid(docGuid, cdbFacade);
				
				m_binder.addResultSet("rsApprovedSigs",rsApprovedSigs);
				
			} catch (Exception e) {
				String msg = "Unable to execute query: " +
				 "qSignatureChecking_GetApprovedSigsForDoc";
				
				Log.error(msg, e);
				throw new ServiceException(msg, e); 
			}
		}
	}
	
	/**
     * This method will get the recent documents related to a particular account
     * It is called as part of the CCLA_SC_GET_SIGS_FOR_ACCOUNT. It is called last in the stack
     * and assumes various parameters are on the binder, if they are not exceptions are thrown   
	 * @throws DataException 
	 * @throws Exception
	 */
	public void getRecentSigFormsForAcc() throws ServiceException, DataException{
		
		String RECENT_SIG_DOCS_APP_TYPES = SharedObjects.getEnvironmentValue("CCLA_SC_SIG_RECENT_SIG_DOCS_APP_TYPES");
		String RECENT_SIG_DOCS_MAND_TYPES = SharedObjects.getEnvironmentValue("CCLA_SC_SIG_RECENT_SIG_DOCS_MAND_TYPES");

		 
		String CLIENT_NUMBER = m_binder.getLocal("CLIENT_NUMBER");
		if (StringUtils.stringIsBlank(CLIENT_NUMBER)){
			throw new ServiceException ("getRecentSigFormsForAcc: CLIENT_NUMBER not on binder");
		}
		String ACCOUNT_NUMBER = m_binder.getLocal("ACCOUNT_NUMBER");
		if (StringUtils.stringIsBlank(ACCOUNT_NUMBER)){
			throw new ServiceException ("getRecentSigFormsForAcc: ACCOUNT_NUMBER not on binder");
		}
		String DOCNAME = m_binder.getLocal("DOCNAME");
		if (StringUtils.stringIsBlank(DOCNAME)){
			throw new ServiceException ("getRecentSigFormsForAcc: DOCNAME not on binder");
		}

		Log.info( "RECENT_SIG_DOCS_APP_TYPES:"+RECENT_SIG_DOCS_APP_TYPES+
				"RECENT_SIG_DOCS_MAND_TYPES:"+RECENT_SIG_DOCS_MAND_TYPES+
				"CLIENT_NUMBER:"+CLIENT_NUMBER+
				"ACCOUNT_NUMBER:"+ACCOUNT_NUMBER+
				"DOCNAME:"+DOCNAME);
		
		// I can not work out a way to pass a csv to a canned ucm query without it escaping the '
		String sqlQuery =  "SELECT * FROM ( "+
				"SELECT XFOLDERNAME,XDOCUMENTCLASS,XPDFDOCNAME,XPARENTDOCNAME,DINDATE,DDOCNAME,DDOCTYPE,DPROCESSINGSTATE,DREVLABEL,DSECURITYGROUP,DWEBEXTENSION,DDOCACCOUNT FROM REVISIONS R "+
				"INNER JOIN DOCMETA DM ON (R.DID = DM.DID) "+
				"WHERE R.DREVRANK = 0 "+
				"AND R.DSTATUS = 'RELEASED' "+
				"AND DM.XDOCUMENTCLASS IN ("+convertCSVtoQueryFormat(RECENT_SIG_DOCS_APP_TYPES)+") "+
				"AND DM.XCLIENTNUMBER = '"+CLIENT_NUMBER+"' "+
				"AND DM.XACCOUNTNUMBER = '"+ACCOUNT_NUMBER+"' "+
				"AND R.DDOCNAME <> '"+DOCNAME+"' "+
				"ORDER BY DINDATE DESC "+
				") where rownum <=3 "+
				"UNION  "+
				"SELECT * FROM ( "+
				"SELECT XFOLDERNAME,XDOCUMENTCLASS,XPDFDOCNAME,XPARENTDOCNAME,DINDATE,DDOCNAME,DDOCTYPE,DPROCESSINGSTATE,DREVLABEL,DSECURITYGROUP,DWEBEXTENSION,DDOCACCOUNT FROM REVISIONS R "+
				"INNER JOIN DOCMETA DM ON (R.DID = DM.DID) "+
				"WHERE R.DREVRANK = 0 "+
				"AND R.DSTATUS = 'RELEASED' "+
				"AND DM.XDOCUMENTCLASS IN ("+convertCSVtoQueryFormat(RECENT_SIG_DOCS_MAND_TYPES)+") "+
				"AND DM.XCLIENTNUMBER = '"+CLIENT_NUMBER+"' "+
				"AND DM.XACCOUNTNUMBER = '"+ACCOUNT_NUMBER+"' "+
				"AND R.DDOCNAME <> '"+DOCNAME+"' "+
				"ORDER BY DINDATE DESC "+
				") where rownum <=3 ";
		 
		
		Log.info("sqlQuery: "+sqlQuery);
		
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace);
		
		try {
			DataResultSet rsRecentSigForms = ucmFacade.createResultSetSQL(sqlQuery);
			m_binder.addResultSet("rsRecentSigForms",rsRecentSigForms);
		} catch (Exception e) {
			Log.error("Unable to execute query in: getRecentSigFormsForAcc", e);
			throw new ServiceException
			 ("Unable to execute query in getRecentSigFormsForAcc", e); 
		}
		
	}
	
	/**
	 * This method will convert a csv to a string with each value surrounded by '
	 * for use in queries
	 * @param csv
	 */
	private static String convertCSVtoQueryFormat(String csv){
		String queryCSV = "'";
		
		String csvArray[] = csv.split(",");
		
		for (int i = 0; i < csvArray.length; i++) {
			if (i==csvArray.length-1){
				queryCSV += csvArray[i]+"'";
			} else {
				queryCSV += csvArray[i]+"','";
			}
		}
		
		return queryCSV;
	}
	
	
	
	/*
	 * This method is used to save the signature that is posted via the applet. 
	 * 
	 * Required Binder Params
	 *  - userName
	 *  - action
	 *  - sourceDocId
	 *  - image
	 *  - image:path
	 * 
	 */
	public void signatureSaver() throws ServiceException, DataException{
		Log.info("signatureSaver >>");
		
		// Obtain binder details
		String userName = m_binder.getLocal("userName");
		String action = m_binder.getLocal("action");
		String sourceDocId = m_binder.getLocal("sourceDocId");
		String imageName = m_binder.getLocal("image");
		String imagePath = m_binder.getLocal("image:path");
		
		if (StringUtils.stringIsBlank(userName) || StringUtils.stringIsBlank(action) || 
				StringUtils.stringIsBlank(sourceDocId) || StringUtils.stringIsBlank(imagePath) || 
				StringUtils.stringIsBlank(imageName))
			throw new ServiceException(
					"userName, action and sourceDocId and image must be on the binder");
		
		Log.info("Saving signature with details: " + "userName:" + userName + 
				" |action:" + action + " |imageName:" + imageName + 
				" |imagePath:" + imagePath + " |sourceDocId:" + sourceDocId);
		
		
		// If dUser is anonymous, we need to set the UserData to the passed username
		String dUser = m_binder.getLocal("dUser");
		if (dUser.equals("anonymous")) {
			UserData ud = this.m_userData;
			ud.setName(userName);
		} 
		
		
		// UCM seems to have an issue unpacking the files in the multipart/form
		// post's it receives - it always adds an extra OD OA (CR/NL) to the
		// front of the file.  To get round this we need to make a copy of the
		// whole file.
		File oFile = null;
		try {
			String filePathOut = null;
			int extension = imagePath.lastIndexOf(".");
			if (extension > 0) {
				filePathOut =
					imagePath.substring(0, extension) + "_clean" +
					imagePath.substring(extension);
			} else {
				filePathOut = imagePath + "_clean";
			}
			
			File file = new File(imagePath);
			BufferedInputStream input = 
				new BufferedInputStream(new FileInputStream(file));
			input.read(new byte[2]);
			
			oFile = new File(filePathOut);
			BufferedOutputStream output = 
				new BufferedOutputStream(new FileOutputStream(oFile));
	        
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = input.read(buffer)) != -1) {
				output.write(buffer, 0, count);
			}

			input.close();
			
			output.flush();
			output.close();
			
			imagePath = filePathOut;
			file.delete();
			
			m_binder.addTempFile(imagePath);
		} catch (Exception ioe) {
			throw new ServiceException(
				"Unable to process file, " + ioe.getMessage(), ioe);
		}
		
		
		
		LWDocument lwDoc = new LWDocument();
		FWFacade cdbFacade = CCLAUtils.getFacade(m_workspace, true);
		String docName = "";
		
		if (action.equals("add")){
			try {
				//Strip the .jpg off the image name to obtain personID
				String personId = imageName.substring(0, imageName.lastIndexOf('.'));
				lwDoc.setTitle("Signature for: "+personId);
				lwDoc.setAttribute("xPersonId", personId);
				lwDoc.setDocType("Signature");
				lwDoc.setSecurityGroup("Public");
				lwDoc.setAuthor(userName);
				docName = lwDoc.checkin(new File(imagePath));
				
				//Now update the PERSON_SIGNATURE table
				PersonSignature.add(Integer.parseInt(personId), docName, 
						Integer.parseInt(sourceDocId), userName, cdbFacade);	
			}  catch (Exception e2) {
				Log.info("adding person signature "+docName+": "+e2.getMessage());
			}

		
		} else if (action.equals("update")){
			//remove .jpg from file name and extract docName and docValues
			docName = imageName.substring(0,imageName.length()-4);		
						
			Log.info("Updating Docname: "+docName+ " |docAuthor:"+userName );
			

			try {
				lwDoc.useDatabase();
				lwDoc.setDocName(docName);
				String personId = lwDoc.getAttribute("xPersonId");
				lwDoc.checkout();
				lwDoc.setAuthor(userName);
				lwDoc.checkin(new File(imagePath));	
				
				//Now update the PERSON_SIGNATURE table
				PersonSignature.update(Integer.parseInt(personId), 
					Integer.parseInt(sourceDocId), userName, cdbFacade);
			} catch (Exception e) {
				Log.info("error with checking out docName "+docName+", " +
						"carrying on anyway! "+e.getMessage());
			}
		}

		//remove temp file
		try {
			if (oFile.exists()) {
				boolean isDeleted = oFile.delete();
				if (!isDeleted) {
					Log.info("Cannot delete Signature File "+oFile.getName());
				} else {
					Log.info("Deleted Signature File");
				}
			}
		} catch (Exception e) {
			Log.info("Error deleting Signature File "+oFile.getName());
		}
			
		Log.info("<< signatureSaver");
	}
}
	
	
	

