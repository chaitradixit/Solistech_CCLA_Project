package com.ecs.ucm.ccla.data.instruction;

/** 
 *  @deprecated replaced by InstructionStatus
 *  
 *  Represents an entry from the REF_COMM_STATUS table.
 *  
 *  Most status instances are hard-coded here for easy reference/access. These
 *  entries must of course match a subset of items in the REF_COMM_STATUS table.
 *  
 * @author Tom
 *
 */
public class CommStatus {
	
	private int statusId;
	private String name;
	
	/** Status for items ready for consumption by Duplicate Check module. */
	public static final CommStatus PENDING_DUPLICATE_CHECK = 
	 new CommStatus(1, "Pending Duplicate Check");
	/** Status for items ready for consumption by Verification module. */
	public static final CommStatus PENDING_VERIFICATION_CHECK = 
	 new CommStatus(2, "Pending Verification Check");
	
	/** Status for items suspected of being a duplicate. */
	public static final CommStatus SUSPECTED_DUPLICATE = 
	 new CommStatus(10, "Suspected Duplicate");
	/** Status for items positively marked as being duplicate. */
	public static final CommStatus DUPLICATE = 
	 new CommStatus(11, "Duplicate");
	
	public CommStatus(int statusId, String name) {
		super();
		this.statusId = statusId;
		this.name = name;
	}
	
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(CommStatus checkStatus) {
		return (this.getStatusId() == checkStatus.getStatusId());
	}
}
