package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import org.studentworker.com.model.Arrears;
import org.studentworker.com.model.Awards;
import org.studentworker.com.model.AwardsApply;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class AwardsApplyController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = AwardsApply.dao.paginate(pageNumber, pageSize,
		"select a.id as id,a.stu_no as stu_no,a.stu_name as stu_name,b.name as name,a.reason as reason,performance as performance",
		"from awards_apply as a left join awards as b on a.awards_id=b.id where a.del_time is null order by id desc");
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("update".equals(op)){
			AwardsApply record = new AwardsApply();
			//String id = getPara("id");
			record.BindModel(this, record);
			/*record.set("stu_no", getPara("stu_no"));
			record.set("stu_name",getPara("stu_name"));
			record.set("awards_id",getPara("awards_id"));
			record.set("reason",getPara("reason"));
			record.set("performance",getPara("performance"));*/
			record.set("create_time", DateUtil.getCurrentTimeStamp());
			record.set("create_id", getLoginUserId());
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
			setAttr("rd", AwardsApply.dao.findById(id));
			setAttr("awards",Awards.dao.find("select * from awards where is_publish=1 and del_time is null"));
			render("edit.html");
		}else {
			setAttr("rd",new AwardsApply());
			setAttr("awards",Awards.dao.find("select * from awards where is_publish=1 and del_time is null"));
			render("edit.html");
		}
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = AwardsApply.dao.findById(id);
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
