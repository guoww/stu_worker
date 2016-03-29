package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import org.studentworker.com.model.StudentFunding;
import org.studentworker.com.model.WorkStudy;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class FundingController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = StudentFunding.dao.paginate(pageNumber, pageSize, "select * ", "from student_funding where del_time is null order by id desc");
		for(Model m : page.getList()){
			m.set("date", DateUtil.format(m.getLong("date"), "yyyy-MM-dd"));
		}
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("save".equals(op)){
			StudentFunding record = new StudentFunding();
			//String id = getPara("id");
			record.BindModel(this, record);
			record.set("date",DateUtil.dateFormat(getPara("date"), "yyyy-MM-dd"));
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
			Model m = StudentFunding.dao.findById(id);
			m.set("date", DateUtil.format(m.getLong("date"), "yyyy-MM-dd"));
			setAttr("rd", m);
			render("edit.html");
		}else {
			setAttr("rd",new StudentFunding());
			render("edit.html");
		}
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = StudentFunding.dao.findById(id);
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
