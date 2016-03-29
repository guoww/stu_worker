package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import org.studentworker.com.model.Awards;
import org.studentworker.com.model.AwardsGrant;
import org.studentworker.com.model.Glory;
import org.studentworker.com.model.GloryRegist;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class AwardsGrantController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = AwardsGrant.dao.paginate(pageNumber, pageSize,
		"select a.id as id,a.stu_name as stu_name,a.stu_no as stu_no,b.name as name,a.grant_time as grant_time,a.prize as prize", 
		"from awards_grant as a left join awards as b on a.awards_id=b.id where a.del_time is null order by id desc");
		for(Model m : page.getList()){
			m.set("grant_time", DateUtil.format(m.getLong("grant_time"), "yyyy-MM-dd"));
		}
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("update".equals(op)){
			AwardsGrant rd = new AwardsGrant();
			rd.BindModel(this, rd);
			rd.set("grant_time", DateUtil.dateFormat(getPara("grant_time"), "yyyy-MM-dd"));
			rd.set("create_time", DateUtil.getCurrentTimeStamp());
			rd.set("teacher_id", getLoginUserId());
			rd.save();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code", 1);
			map.put("msg","操作成功！");
			renderJson(map);
		}else if("edit".equals(op)){
			int id = getParaToInt("id");
			Model m = AwardsGrant.dao.findById(id);
			m.set("grant_time", DateUtil.format(m.getLong("grant_time"), "yyyy-MM-dd"));
			setAttr("awards",Awards.dao.find("select * from awards where del_time is null"));
			setAttr("rd", m);
			render("edit.html");
		}else {
			setAttr("awards",Awards.dao.find("select * from awards where del_time is null"));
			setAttr("rd",new AwardsGrant());
			render("edit.html");
		}
		
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = AwardsGrant.dao.findById(id);
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
