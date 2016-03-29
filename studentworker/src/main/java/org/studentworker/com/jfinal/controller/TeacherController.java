package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import org.studentworker.com.model.Arrears;
import org.studentworker.com.model.Insurance;
import org.studentworker.com.model.Teacher;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class TeacherController extends BaseController{

	public void list(){
		Page<Model> pager = getList(Teacher.class,null,null);
		setAttr("pager", pager);
		render("list.html");
	}

	public void edit() throws Exception{
		doEdit("edit.html",Teacher.class);
	}
	
	public void del() throws Exception{
		doDel(Teacher.class);
	}
	
}
