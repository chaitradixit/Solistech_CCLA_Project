package com.ecs.ucm.ccla.commproc.listener;

import com.ecs.ucm.ccla.commproc.events.IInstructionEvent;

public interface IInstructionListener {

	public void receivedEvent(IInstructionEvent event);
}
