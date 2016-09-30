package com.ecs.ucm.ccla.data.form.spool;

import intradoc.common.ServiceException;

public class SpoolFileException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public SpoolFileException(int arg0, String arg1) {
		super(arg0, arg1);
	}

	public SpoolFileException(String string) {
		super(string);
	}
}
