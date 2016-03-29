package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import org.studentworker.com.model.StudentFunding;
import org.studentworker.com.model.StudentLoan;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class LoanController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = StudentLoan.dao.paginate(pageNumber, pageSize, "select * ", "from student_loan where del_time is null order by id desc");
		for(Model m : page.getList()){
			m.set("begin_time", DateUtil.format(m.getLong("begin_time"), "yyyy-MM-dd"));
		}
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("save".equals(op)){
			StudentLoan record = new StudentLoan();
			//String id = getPara("id");
			/*record.set("stu_no", getPara("stu_no"));
			record.set("stu_name",getPara("stu_name"));
			record.set("term",getPara("term"));
			record.set("money",getPara("money"));
			record.set("year",getPara("year"));*/
			record.BindModel(this, record);
			record.set("begin_time",DateUtil.dateFormat(getPara("begin_time"), "yyyy-MM-dd"));
			record.set("create_time", DateUtil.getCurrentTimeStamp());
			record.set("teacher_id", getLoginUserId());
			/*if(id==null||"".equals(id)){
				record.save();
			}else{
				record.set("id", id);
				record.update();
			}*/
			record.save();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code", 1);
			map.put("msg","操作成功！");
			renderJson(map);
		}else if("edit".equals(op)){
			int id = getParaToInt("id");
			Model m = StudentLoan.dao.findById(id);
			m.set("begin_time", DateUtil.format(m.getLong("begin_time"), "yyyy-MM-dd"));
			setAttr("rd", m);
			render("edit.html");
		}else {
			setAttr("rd",new StudentLoan());
			render("edit.html");
		}
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = StudentLoan.dao.findById(id);
			del.set("del_id", getLoginUserId());
			del.set("del_time", DateUtil.getCurrentTimeStamp());
			del.update();
		}
		Map<String , Object> map = new HashMap<String,Object>();
		map.put("code", 1);
		map.put("msg", "操作成功!");
		renderJson(map);
	}
	
}
