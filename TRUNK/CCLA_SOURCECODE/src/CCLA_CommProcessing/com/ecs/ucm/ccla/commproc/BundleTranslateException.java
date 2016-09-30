package com.ecs.ucm.ccla.commproc;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;

/** Special Exception for dealing with errors when translating UCM document bundles
 *  into equivalent Comm/Instruction instances
 *  
 * @author Tom
 *
 */
public class BundleTranslateException extends ServiceException {
	
	private DocumentBundleTranslator.FailReason failReason;
	private String failedDocName;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BundleTranslateException(String msg, Throwable e, 
	 DocumentBundleTranslator.FailReason failReason, String failedDocName) {
		super(msg, e);
		
		this.setFailReason(failReason);
		this.setFailedDocName(failedDocName);
	}
	
	public BundleTranslateException(String msg, 
	 DocumentBundleTranslator.FailReason failReason, String failedDocName) {
		super(msg);
		
		this.setFailReason(failReason);
		this.setFailedDocName(failedDocName);
	}

	public void setFailReason(DocumentBundleTranslator.FailReason failReason) {
		this.failReason = failReason;
	}

	public DocumentBundleTranslator.FailReason getFailReason() {
		return failReason;
	}

	public void setFailedDocName(String failedDocName) {
		this.failedDocName = failedDocName;
	}

	public String getFailedDocName() {
		return failedDocName;
	}

	public void addFieldsToBinder(DataBinder binder) {
		// TODO Auto-generated method stub
		binder.putLocal("BundleTranslateException", "1");
		
		binder.putLocal("FailReason", this.getFailReason().getLabel());
		
		if (this.getMessage() != null)
			binder.putLocal("FailMessage", this.getMessage().toString());
		
		if (this.getFailedDocName() != null)
			binder.putLocal("FailedDocName", this.getFailedDocName());
	}

}
