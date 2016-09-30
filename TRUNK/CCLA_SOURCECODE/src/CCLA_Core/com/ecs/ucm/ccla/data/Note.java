package com.ecs.ucm.ccla.data;

import java.util.Date;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Wrapper class for an entry in the NOTES table.
 */
public class Note implements Persistable {
	
	public static final String NOTE_ID = "NOTE_ID";
	public static final String NOTE_CONTENT = "NOTE_CONTENT";
	
	public static final String ADD_QUERY_NAME = "qCore_AddNote";
	public static final String GET_BY_ID_QUERY_NAME = "qCore_GetNoteById";
	public static final String UPDATE_QUERY_NAME = "qCore_UpdateNote";
	
	private int noteId;
	private String userId;
	private Date dateAdded;
	private String lastUpdatedBy;
	private Date lastUpdated;
	private String message;
	
	public Note(int noteId, String userId, Date dateAdded, 
	 String lastUpdatedBy, Date lastUpdated, String message) {
		this.noteId = noteId;
		this.userId = userId;
		this.dateAdded = dateAdded;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdated = lastUpdated;
		this.message = message;
	}
	
	public static Note add(String message, String userId, FWFacade facade) 
	 throws DataException {
		
		String noteIdStr 	= CCLAUtils.getNewKey("Note", facade);
		int noteId			= Integer.parseInt(noteIdStr);
		Note note 			= new Note(noteId, userId, null, null, null, message);
		
		note.validate(facade);
		
		DataBinder binder = new DataBinder();
		note.addFieldsToBinder(binder);
		
		facade.execute(ADD_QUERY_NAME, binder);
		Log.debug("Added note: ID=" + noteIdStr + ", message='" + note + "'");
		
		return get(noteId, facade);
	}
	
	public static Note get(int noteId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsNote = getData(noteId, facade);
		return get(rsNote);
	}
	
	public static Note get(DataResultSet rsNote) throws DataException {
		
		if (rsNote.isEmpty())
			return null;
		
		return new Note(
		 CCLAUtils.getResultSetIntegerValue(rsNote, NOTE_ID),
		 rsNote.getStringValueByName(SharedCols.USER),
		 rsNote.getDateValueByName(SharedCols.DATE_ADDED),
		 rsNote.getStringValueByName(SharedCols.LAST_UPDATED_BY),
		 rsNote.getDateValueByName(SharedCols.LAST_UPDATED),
		 rsNote.getStringValueByName(NOTE_CONTENT));
	}
	
	public static DataResultSet getData(int noteId, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal(NOTE_ID, Integer.toString(noteId));
		
		return facade.createResultSet(GET_BY_ID_QUERY_NAME, binder);
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date datedUpdated) {
		this.lastUpdated = datedUpdated;
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {

		CCLAUtils.addQueryIntParamToBinder
		 (binder, NOTE_ID, this.getNoteId());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, SharedCols.USER, this.getUserId());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.DATE_ADDED, this.getDateAdded());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, SharedCols.LAST_UPDATED_BY, this.getLastUpdatedBy());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.LAST_UPDATED, this.getLastUpdated());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, NOTE_CONTENT, this.getMessage());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		this.setLastUpdatedBy(username);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute(UPDATE_QUERY_NAME, binder);
		
	}

	public void setAttributes(DataBinder binder) throws DataException {
		this.setMessage(binder.getLocal(NOTE_CONTENT));
		this.setLastUpdatedBy(binder.getLocal(SharedCols.LAST_UPDATED_BY));
	}

	public void validate(FWFacade facade) throws DataException {
		
		if (this.getUserId() == null)
			throw new DataException("User name missing");
		
		if (this.getMessage() == null)
			throw new DataException("Note content missing");
	}
	
}
