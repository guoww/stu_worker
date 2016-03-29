package org.studentworker.com.event.listener;

import org.studentworker.com.event.testEvent;
import org.studentworker.com.jfinal.plugin.event.core.ApplicationListener;
import org.studentworker.com.jfinal.plugin.event.core.Listener;
import org.studentworker.com.model.BaseModel;

@Listener
public class testListener implements ApplicationListener<testEvent>{

	public void onApplicationEvent(testEvent event) {
		BaseModel model = (BaseModel)event.getSource();
		System.out.println(model.getClass().getName());
	}

}
