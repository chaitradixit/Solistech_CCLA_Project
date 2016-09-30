package com.ecs.ucm.ccla.data.form;


/** Wrapper for form configurations, based on form type/subtype. */
public class FormConfig {
	
	private String docClass;
	private Class<UCMFormHandler>  handlerClass;
	
	public FormConfig(Class<UCMFormHandler> handlerClass, String docClass) {
		this.handlerClass = handlerClass;
		this.docClass = docClass;
	}

	public String getDocClass() {
		return docClass;
	}

	public void setDocClass(String docClass) {
		this.docClass = docClass;
	}

	public Class<UCMFormHandler> getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(Class<UCMFormHandler> handlerClass) {
		this.handlerClass = handlerClass;
	}
}
