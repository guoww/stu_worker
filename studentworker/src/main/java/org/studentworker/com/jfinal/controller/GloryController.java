package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.studentworker.com.model.Awards;
import org.studentworker.com.model.Glory;
import org.studentworker.com.model.Insurance;
import org.studentworker.com.model.InsurancePayment;
import org.studentworker.com.model.InsuranceRegist;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class GloryController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = Glory.dao.paginate(pageNumber, pageSize, "select * ", "from glory where del_time is null order by id desc");
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("update".equals(op)){
			Glory rd = new Glory();
			//String id = getPara("id");
			/*rd.set("name", getPara("name"));
			rd.set("level", getPara("level"));
			rd.set("condition", getPara("condition"));*/
			rd.BindModel(this, rd);
			rd.set("create_time", DateUtil.getCurrentTimeStamp());
			rd.set("teacher_id", getLoginUserId());
			/*if(id==null||"".equals(id)){
				rd.save();
			}else{
				rd.set("id", id);
				rd.update();
			}*/
			rd.save();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code", 1);
			map.put("msg","操作成功！");
			renderJson(map);
		}else if("edit".equals(op)){
			int id = getParaToInt("id");
			Model m = Glory.dao.findById(id);
			setAttr("rd", m);
			render("edit.html");
		}else {
			setAttr("rd",new Awards());
			render("edit.html");
		}
		
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = Glory.dao.findById(id);
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
