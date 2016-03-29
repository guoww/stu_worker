package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import org.studentworker.com.model.Arrears;
import org.studentworker.com.model.PoorStudentFile;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class PoorController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = PoorStudentFile.dao.paginate(pageNumber, pageSize, "select * ", "from poor_student_file where del_time is null order by id desc");
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("save".equals(op)){
			PoorStudentFile record = new PoorStudentFile();
			//String id = getPara("id");
			/*record.set("stu_no", getPara("stu_no"));
			record.set("stu_name",getPara("stu_name"));
			record.set("poor_level",getPara("poor_level"));
			record.set("year_income",getPara("year_income"));
			record.set("mon_income",getPara("mon_income"));*/
			record.BindModel(this, record);
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
			setAttr("rd", PoorStudentFile.dao.findById(id));
			render("edit.html");
		}else {
			setAttr("rd",new PoorStudentFile());
			render("edit.html");
		}
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = PoorStudentFile.dao.findById(id);
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
