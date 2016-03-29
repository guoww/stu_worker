package org.studentworker.com.event;

import java.io.Serializable;

import org.studentworker.com.jfinal.plugin.event.core.ApplicationEvent;

public class testEvent extends ApplicationEvent implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8608707242652149128L;

	public testEvent(Object source) {
		super(source);
	}

}
