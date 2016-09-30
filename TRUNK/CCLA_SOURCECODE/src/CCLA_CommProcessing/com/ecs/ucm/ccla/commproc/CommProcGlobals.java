package com.ecs.ucm.ccla.commproc;

import com.ecs.utils.StringUtils;

import intradoc.shared.SharedObjects;

public class CommProcGlobals {
	
	/** Determines whether UCM document bundles are converted into Comm/Instruction
	 *  instances upon completion in Iris.
	 *  
	 */
	public static final boolean ENABLE_DOC_BUNDLE_CONVERSION =
	 !StringUtils.stringIsBlank(
		SharedObjects.getEnvironmentValue("CCLA_CommProc_EnableDocBundleConversion")
	);
}
