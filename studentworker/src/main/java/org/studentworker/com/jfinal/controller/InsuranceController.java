package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.studentworker.com.model.Insurance;
import org.studentworker.com.model.InsurancePayment;
import org.studentworker.com.model.InsuranceRegist;
import org.studentworker.com.model.Student;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class InsuranceController extends BaseController{

	public void list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = Insurance.dao.paginate(pageNumber, pageSize, "select * ", "from insurance where del_time is null order by id desc");
		setAttr("pager",page);
		render("list.html");
	}
	
	public void edit(){
		String op = getPara("op");
		if("update".equals(op)){
			Insurance rd = new Insurance();
			rd.set("name", getPara("name"));
			rd.set("create_time", DateUtil.getCurrentTimeStamp());
			rd.set("teacher_id", getLoginUserId());
			rd.save();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code", 1);
			map.put("msg","操作成功！");
			renderJson(map);
		}else if("edit".equals(op)){
			int id = getParaToInt("id");
			setAttr("rd", Insurance.dao.findById(id));
			render("edit.html");
		}else {
			setAttr("rd",new Insurance());
			render("edit.html");
		}
		
	}
	
	public void del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = Insurance.dao.findById(id);
			del.set("del_id", getLoginUserId());
			del.set("del_time", DateUtil.getCurrentTimeStamp());
			del.update();
		}
		Map<String , Object> map = new HashMap<String,Object>();
		map.put("code", 1);
		map.put("msg", "操作成功!");
		renderJson(map);
	}
	
	public void payment_list(){
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = InsurancePayment.dao.paginate(pageNumber, pageSize, 
			"select a.id as id,a.stu_no as stu_no,b.name as name,a.stu_name as stu_name,a.pay_time as pay_time,a.money as money"
			, "from insurance_payment as a left join insurance as b on a.insurance_id=b.id where a.del_time is null order by id desc");
		for(Model m : page.getList()){
			long pay_time = m.getLong("pay_time");
			String pay_time_str = DateUtil.format(pay_time,"yyyy-MM-dd");
			m.set("pay_time", pay_time_str);
		}
		setAttr("pager",page);
		render("payment_list.html");
	}
	
	public void payment_edit(){
		String op = getPara("op");
		if("update".equals(op)){
			InsurancePayment record = new InsurancePayment();
			String id = getPara("id");
			record.set("stu_no", getPara("stu_no"));
			record.set("is_pay", getPara("is_pay"));
			record.set("insurance_id", getPara("insurance_id"));
			record.set("money", getPara("money"));
			record.set("detail", getPara("detail"));
			record.set("pay_time", DateUtil.dateFormat(getPara("pay_time"), "yyyy-MM-dd"));
			record.set("stu_name", getPara("stu_name"));
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
			Model m = InsurancePayment.dao.findById(id);
			m.set("pay_time", DateUtil.format(m.getLong("pay_time"), "yyyy-MM-dd"));
			setAttr("record", m);
			List<Model> insurances = Insurance.dao.find("select * from insurance ");
			setAttr("insurances",insurances);
			render("payment_edit.html");
		}else {
			setAttr("record",new InsurancePayment());
			List<Model> insurances = Insurance.dao.find("select * from insurance ");
			setAttr("insurances",insurances);
			render("payment_edit.html");
		}
	}
	
	public void payment_del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = InsurancePayment.dao.findById(id);
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
		Page<Model> page = InsuranceRegist.dao.paginate(pageNumber, pageSize, "select a.id as id,a.stu_no as stu_no,b.name as name,a.stu_name as stu_name,a.regist_time as regist_time ", 
				"from insurance_regist as a left join insurance as b on a.insurance_id=b.id where a.del_time is null order by a.id desc");
		for(Model m : page.getList()){
			long regist_time = m.getLong("regist_time");
			String regist_time_str = DateUtil.format(regist_time,"yyyy-MM-dd");
			m.set("regist_time", regist_time_str);
		}
		setAttr("pager",page);
		render("regist_list.html");
	}
	
	public void regist_edit(){
		String op = getPara("op");
		if("update".equals(op)){
			InsuranceRegist record = new InsuranceRegist();
			String id = getPara("id");
			record.set("stu_no", getPara("stu_no"));
			record.set("stu_name", getPara("stu_name"));
			record.set("insurance_id", getPara("insurance_id"));
			record.set("regist_time", DateUtil.dateFormat(getPara("regist_time"), "yyyy-MM-dd"));
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
			setAttr("record", InsuranceRegist.dao.findById(id));
			List<Model> insurances = Insurance.dao.find("select * from insurance");
			setAttr("insurances",insurances);
			render("regist_edit.html");
		}else {
			setAttr("record",new InsuranceRegist());
			List<Model> insurances = Insurance.dao.find("select * from insurance");
			setAttr("insurances",insurances);
			render("regist_edit.html");
		}
		
	}
	
	public void regist_del(){
		int id = getParaToInt("id");
		if(id>0){
			Model del = InsuranceRegist.dao.findById(id);
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
