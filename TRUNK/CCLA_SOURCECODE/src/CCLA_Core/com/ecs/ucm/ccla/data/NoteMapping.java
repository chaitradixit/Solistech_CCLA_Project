package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class NoteMapping implements Persistable {

	private int noteMappingId;
	
	private Integer interactionId;
	private int noteId;
	
	private UCMNote note;
	
	public NoteMapping(int noteMappingId, Integer interactionId, 
	 int noteId, UCMNote note) {
		this.noteMappingId = noteMappingId;
		this.interactionId = interactionId;
		this.noteId = noteId;
		this.note = note;
	}
	
	public static NoteMapping get(int noteMappingId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsNoteMapping = getData(noteMappingId, facade);
		
		if (!rsNoteMapping.isEmpty())
			return null;
		
		return get(rsNoteMapping);
	}
	
	/** Creates a NoteMapping instance, with a nested Note instance.
	 *  
	 *  Requires a ResultSet containing a NOTE_MAPPING table, 
	 *  inner-joined to to the NOTES table.
	 *  
	 * @param rsNoteMapping
	 * @return
	 * @throws DataException 
	 */
	public static NoteMapping get(DataResultSet rsNoteMapping) 
	 throws DataException {
		
		UCMNote note = UCMNote.get(rsNoteMapping);
		
		// Create the Note instance first.
		return new NoteMapping(
		 CCLAUtils.getResultSetIntegerValue
		  (rsNoteMapping, "NOTE_MAPPING_ID"),
		 CCLAUtils.getResultSetIntegerValue
		  (rsNoteMapping, "INTERACTION_ID"),
		  CCLAUtils.getResultSetIntegerValue
		  (rsNoteMapping, "NOTE_ID"),
		  note
		 );
	}
	
	/** Fetches a single row from the NOTE_MAPPING table, inner-joined to
	 *  to the NOTES table, for the given note mapping ID.
	 *  
	 * @param noteMappingId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int noteMappingId, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "NOTE_MAPPING_ID", noteMappingId);
		
		return facade.createResultSet
		 ("qClientServices_GetNoteMapping", binder);
	}
	
	/** Adds a mapping entry to the CCLA_NOTE_MAPPING table.
	 *  
	 *  This is used to store links between Interactions/Activities
	 *  and Note items.
	 *  
	 * @param interactionId
	 * @param activityId
	 * @param noteId
	 * @param facade
	 * @throws DataException
	 */
	public static int add(
	 Integer interactionId,
	 Integer activityId,
	 int noteId,
	 FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		String noteMappingIdStr = 
		 CCLAUtils.getNewKey("NoteMapping", facade);
		
		Integer noteMappingId = Integer.parseInt(noteMappingIdStr);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "NOTE_MAPPING_ID", noteMappingId);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "INTERACTION_ID", interactionId);

		CCLAUtils.addQueryIntParamToBinder
		 (binder, "NOTE_ID", noteId);
		
		facade.execute("qClientServices_AddNoteMapping", binder);
		
		Log.debug("Generated new note mapping: " + noteMappingId);
		return noteMappingId.intValue();
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public int getNoteMappingId() {
		return noteMappingId;
	}

	public void setNoteMappingId(int noteMappingId) {
		this.noteMappingId = noteMappingId;
	}

	public Integer getInteractionId() {
		return interactionId;
	}

	public void setInteractionId(Integer interactionId) {
		this.interactionId = interactionId;
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public UCMNote getNote() {
		return note;
	}

	public void setNote(UCMNote note) {
		this.note = note;
	}
	
}
