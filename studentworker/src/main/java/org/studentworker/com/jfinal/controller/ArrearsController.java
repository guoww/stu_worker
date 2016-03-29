package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;
import org.studentworker.com.model.Arrears;
import org.studentworker.com.util.DateUtil;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class ArrearsController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = Arrears.dao.paginate(pageNumber, pageSize, "select * ", "from arrears where del_time is null order by id desc");
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("save".equals(op)){
			Arrears record = new Arrears();
			Arrears.dao.BindModel(this,record);
			record.set("create_time", DateUtil.getCurrentTimeStamp());
			record.set("teacher_id", getLoginUserId());
			record.save();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code", 1);
			map.put("msg","操作成功！");
			renderJson(map);
		}else if("edit".equals(op)){
			int id = getParaToInt("id");
			setAttr("rd", Arrears.dao.findById(id));
			render("edit.html");
		}else {
			setAttr("rd",new Arrears());
			render("edit.html");
		}
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = Arrears.dao.findById(id);
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
