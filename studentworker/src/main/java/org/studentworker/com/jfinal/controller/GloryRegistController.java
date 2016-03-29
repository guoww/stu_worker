package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import org.studentworker.com.model.Glory;
import org.studentworker.com.model.GloryRegist;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class GloryRegistController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = GloryRegist.dao.paginate(pageNumber, pageSize,
		"select a.id as id,a.class_name as class_name,a.prize as prize,b.name as name,a.regist_time as regist_time", 
		"from glory_regist as a left join glory as b on a.glory_id=b.id where a.del_time is null order by id desc");
		for(Model m : page.getList()){
			m.set("regist_time", DateUtil.format(m.getLong("regist_time"), "yyyy-MM-dd"));
		}
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("update".equals(op)){
			GloryRegist rd = new GloryRegist();
			//String id = getPara("id");
			rd.BindModel(this, rd);
			rd.set("regist_time", DateUtil.dateFormat(getPara("regist_time"), "yyyy-MM-dd"));
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
			Model m = GloryRegist.dao.findById(id);
			m.set("regist_time", DateUtil.format(m.getLong("regist_time"), "yyyy-MM-dd"));
			setAttr("glorys",Glory.dao.find("select * from glory where del_time is null"));
			setAttr("rd", m);
			render("edit.html");
		}else {
			setAttr("glorys",Glory.dao.find("select * from glory where del_time is null"));
			setAttr("rd",new GloryRegist());
			render("edit.html");
		}
		
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = GloryRegist.dao.findById(id);
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
