package com.ecs.ucm.ccla.commproc;

import java.util.Date;

import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.RoutingModule;

/** Wrapper for instruction errors caused by Routing Modules.
 * 
 * @author Tom
 *
 */
public class InstructionError {
	
	private Instruction instruction;
	private RoutingModule module;
	private String errorMessage;
	private String stackTrace;
	
	private Date date;
	
	public InstructionError(Instruction instruction, RoutingModule module,
			String errorMessage, String stackTrace) {
		this.instruction = instruction;
		this.module = module;
		this.errorMessage = errorMessage;
		this.stackTrace = stackTrace;
		this.setDate(new Date());
	}

	public RoutingModule getModule() {
		return module;
	}

	public void setModule(RoutingModule module) {
		this.module = module;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getStackTrace() {
		return stackTrace;
	}
		
}
