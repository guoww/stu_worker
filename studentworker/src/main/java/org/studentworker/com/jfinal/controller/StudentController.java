package org.studentworker.com.jfinal.controller;

import org.studentworker.com.model.Student;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class StudentController extends BaseController{
	
	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = Student.dao.paginate(pageNumber, pageSize, "select * ", "from student order by id desc");
		setAttr("pager",page);
		render("list.html");
	}
	
	public void importData(){
		
	}
}
