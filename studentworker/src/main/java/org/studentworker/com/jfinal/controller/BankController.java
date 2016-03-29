package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import org.studentworker.com.model.BankCard;
import org.studentworker.com.model.StudentFunding;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class BankController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = BankCard.dao.paginate(pageNumber, pageSize, "select * ", "from student_bank_card where del_time is null order by id desc");
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("update".equals(op)){
			BankCard record = new BankCard();
			//String id = getPara("id");
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
			Model m = BankCard.dao.findById(id);
			setAttr("rd", m);
			render("edit.html");
		}else {
			setAttr("rd",new BankCard());
			render("edit.html");
		}
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = BankCard.dao.findById(id);
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
