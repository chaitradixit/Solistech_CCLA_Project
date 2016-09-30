package com.ecs.ucm.ccla.data;

import java.util.Date;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Wrapper class for an entry in the CCLA_NOTES table.
 *  
 *  Stores the 
 * 
 * @author Tom Marchant
 * @deprecated Use Note Instead (Legacy Only).
 */
public class UCMNote implements Persistable {
	
	private int noteId;
	
	private String userId;
	private Date dateAdded;
	
	private String updateUserId;
	private Date lastUpdated;
	
	private String message;
	
	public UCMNote(int noteId, String userId, Date dateAdded, 
	 String updateUserId, Date lastUpdated, String message) {
		this.noteId = noteId;
		this.userId = userId;
		this.dateAdded = dateAdded;
		this.updateUserId = updateUserId;
		this.lastUpdated = lastUpdated;
		this.message = message;
	}
	
	public static UCMNote add(String message, String userId, FWFacade facade) 
	 throws DataException {
		
		String noteIdStr 	= CCLAUtils.getNewKey("Note", facade);
		int noteId			= Integer.parseInt(noteIdStr);
		UCMNote note 			= new UCMNote(noteId, userId, null, null, null, message);
		
		note.validate(facade);
		
		DataBinder binder = new DataBinder();
		note.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddNote", binder);
		Log.debug("Added note: ID=" + noteIdStr + ", message='" + note + "'");
		
		return get(noteId, facade);
	}
	
	public static UCMNote get(int noteId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsNote = getData(noteId, facade);
		return get(rsNote);
	}
	
	public static UCMNote get(DataResultSet rsNote) throws DataException {
		
		if (rsNote.isEmpty())
			return null;
		
		return new UCMNote(
		 CCLAUtils.getResultSetIntegerValue(rsNote, "NOTE_ID"),
		 rsNote.getStringValueByName("USER_ID"),
		 rsNote.getDateValueByName("DATE_ADDED"),
		 rsNote.getStringValueByName("UPDATE_USER_ID"),
		 rsNote.getDateValueByName("LAST_UPDATED"),
		 rsNote.getStringValueByName("NOTE"));
	}
	
	public static DataResultSet getData(int noteId, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("NOTE_ID", Integer.toString(noteId));
		
		return facade.createResultSet("qClientServices_GetNote", binder);
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

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date datedUpdated) {
		this.lastUpdated = datedUpdated;
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {

		CCLAUtils.addQueryIntParamToBinder
		 (binder, "NOTE_ID", this.getNoteId());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "USER_ID", this.getUserId());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "DATE_ADDED", this.getDateAdded());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "UPDATE_USER_ID", this.getUpdateUserId());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "LAST_UPDATED", this.getLastUpdated());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "NOTE", this.getMessage());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(null);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateNote", binder);
		
	}

	public void setAttributes(DataBinder binder) throws DataException {
		this.setMessage(binder.getLocal("NOTE"));
		this.setUpdateUserId(binder.getLocal("UPDATE_USER_ID"));
	}

	public void validate(FWFacade facade) throws DataException {
		
		if (this.getUserId() == null)
			throw new DataException("User name missing");
		
		if (this.getMessage() == null)
			throw new DataException("Note content missing");
	}
	
}
