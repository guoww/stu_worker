package org.studentworker.com.jfinal.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.studentworker.com.model.Insurance;
import org.studentworker.com.model.InsuranceRegist;
import org.studentworker.com.model.Reward;
import org.studentworker.com.model.RewardRegist;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class RewardController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = Reward.dao.paginate(pageNumber, pageSize, "select * ", "from reward where del_time is null order by id desc");
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("save".equals(op)){
			Reward record = new Reward();
			//String id = getPara("id");
			int type = getParaToInt("type");
			/*record.set("name", getPara("name"));
			record.set("type",type);
			record.set("remark",getPara("remark"));
			record.set("condition",getPara("condition"));*/
			record.BindModel(this, record);
			record.set("create_time", DateUtil.getCurrentTimeStamp());
			record.set("teacher_id", getLoginUserId());
			if(type==1){
				record.set("rwd_class", getPara("class"));
			}else if(type==2){
				record.set("punish_class", getPara("class"));
			}
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
			setAttr("rd", Reward.dao.findById(id));
			render("edit.html");
		}else {
			setAttr("rd",new Reward());
			render("edit.html");
		}
		render("edit.html");
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = Reward.dao.findById(id);
			del.set("del_id", getLoginUserId());
			del.set("del_time", DateUtil.getCurrentTimeStamp());
			del.update();
		}
		Map<String , Object> map = new HashMap<String,Object>();
		map.put("code", 1);
		map.put("msg", "操作成功!");
		renderJson(map);
	}
	
	public void regist_list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = RewardRegist.dao.paginate(pageNumber, pageSize, 
		"select a.id as id,a.stu_no as stu_no,a.stu_name as stu_name,b.name as name,b.type as type,b.punish_class as punish_class,"
		+ "b.rwd_class as rwd_class,a.regist_time as regist_time,begin_time as begin_time,a.end_time as end_time", 
		"from reward_regist as a left join reward as b on a.rwd_id=b.id where a.del_time is null order by id desc");
		for(Model m : page.getList()){
			long regist_time = m.getLong("regist_time");
			long begin_time = m.getLong("begin_time");
			long end_time = m.getLong("end_time");
			String regist_time_str = DateUtil.format(regist_time,"yyyy-MM-dd");
			String begin_time_str = DateUtil.format(begin_time,"yyyy-MM-dd");
			m.set("regist_time", regist_time_str);
			m.set("begin_time", begin_time_str);
			if(end_time>0){
				String end_time_str = DateUtil.format(end_time,"yyyy-MM-dd");
				m.set("end_time", end_time_str);
			}else{
				m.set("end_time", "");
			}
		}
		setAttr("pager",page);
		render("regist_list.html");
	}
	
	public void regist_edit(){
		String op = getPara("op");
		if("update".equals(op)){
			RewardRegist record = new RewardRegist();
			String id = getPara("id");
			record.set("stu_no", getPara("stu_no"));
			record.set("stu_name", getPara("stu_name"));
			record.set("rwd_id", getPara("rwd_id"));
			record.set("regist_time", DateUtil.dateFormat(getPara("regist_time"), "yyyy-MM-dd"));
			record.set("begin_time", DateUtil.dateFormat(getPara("begin_time"), "yyyy-MM-dd"));
			String end_time_str = getPara("end_time");
			if(end_time_str!=null&&!"".equals(end_time_str)){
				record.set("end_time", DateUtil.dateFormat(end_time_str, "yyyy-MM-dd"));
			}
			record.set("create_time", DateUtil.getCurrentTimeStamp());
			record.set("teacher_id", getLoginUserId());
			if(id==null||"".equals(id)){
				record.save();
			}else{
				record.set("id", id);
				record.update();
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code", 1);
			map.put("msg","操作成功！");
			renderJson(map);
		}else if("edit".equals(op)){
			int id = getParaToInt("id");
			Model m = RewardRegist.dao.findById(id);
			m.set("regist_time", DateUtil.format(m.getLong("regist_time"), "yyyy-MM-dd"));
			m.set("begin_time", DateUtil.format(m.getLong("begin_time"), "yyyy-MM-dd"));
			long end_time = m.getLong("end_time");
			if(end_time>0){
				m.set("end_time", DateUtil.format(m.getLong("end_time"), "yyyy-MM-dd"));
			}
			setAttr("record", m);
			List<Model> rewards = Reward.dao.find("select * from reward");
			setAttr("rewards",rewards);
			render("regist_edit.html");
		}else {
			setAttr("record",new RewardRegist());
			List<Model> rewards = Reward.dao.find("select * from reward");
			setAttr("rewards",rewards);
			render("regist_edit.html");
		}
		
	}
	public void regist_del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = RewardRegist.dao.findById(id);
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
