package com.ecs.ucm.ccla.data.form;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.utils.OSUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.cmdline.SysCommandExecutor;

/** Thread subclass which is used to delay execution of Aurora Form
 *  Printer jobs.
 *  
 *  The delay time (in seconds) is controlled by an environment variable
 *  called AURORA_PrintDelayTime.
 *  
 * @author Tom Marchant
 *
 */
public class FormPrinter extends Thread {
		
	String auroraFormPrinter;
	String fileName;
	
	int printDelayTime = 5;
	
	public FormPrinter(String auroraFormPrinter, String fileName) {
		super();
		this.auroraFormPrinter = auroraFormPrinter;
		this.fileName = fileName;
		
		String delayTime =
		 SharedObjects.getEnvironmentValue("AURORA_PrintDelayTime");
		
		if (!StringUtils.stringIsBlank(delayTime))
			this.printDelayTime = Integer.parseInt(delayTime);
	}

	public void run() {
		super.run();
		
		Log.debug("Commencing delayed execution of print command (" +
		 (printDelayTime) + " seconds)");
		
		try {
			Thread.sleep(printDelayTime*1000);
		} catch (InterruptedException e) {}

		
		if (OSUtils.isWindows()) {
		
			// Execute print command directly
			Log.debug("Print thread now executing for Windows environment");
			String printCmd = "print /D:" + auroraFormPrinter + " " + fileName;
			
			Log.debug("Executing command: " + printCmd);
	
			SysCommandExecutor cmdExecutor = new SysCommandExecutor();
			cmdExecutor.setRunWindowsCommand();
			try {
				int exitCode = cmdExecutor.runCommand(printCmd);
			
				Log.debug("Command exit code: " + exitCode);
				Log.debug("Command output: " + cmdExecutor.getCommandOutput());
				if (!StringUtils.stringIsBlank(cmdExecutor.getCommandError()))
					Log.debug("Command error: " + cmdExecutor.getCommandError());
			} catch (Exception e) {
				Log.error("Error invoking command: " + e);
			}
		} else if (OSUtils.isUnixOrLinux()){
			//Execute lpr -P [PRINTER_NAME] [SPOOL_FILE]
			Log.debug("Print thread now executing for Unix/Linux environment");
			String printCmd = "lpr -P " + auroraFormPrinter + " " + fileName;
			try {
				Process process = Runtime.getRuntime().exec(printCmd);
				process.waitFor(); 

				BufferedReader reader = new BufferedReader(
											new InputStreamReader(
												process.getInputStream()));

				String line=reader.readLine(); 
				while(line!=null) { 
					Log.debug("Command output: "+line); 
					line=reader.readLine(); 
				} 
				
				Log.debug("Command finished"); 
				
			} catch (Exception e) {
				Log.error("Error invoking command: " + e);
			}
		} else {
			Log.error("Cannot work out print command for operating system: "+OSUtils.getOsName());
		}
	}
	
}
