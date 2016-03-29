package org.studentworker.com.jfinal.controller;

import org.studentworker.com.model.ApplyType;
import org.studentworker.com.model.Approval;
import org.studentworker.com.model.ApprovalStatus;
import org.studentworker.com.model.BaseModel;
import org.studentworker.com.model.Role;
import org.studentworker.com.util.DateUtil;
import org.studentworker.com.util.Global;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class ApprovalController extends BaseController{

	public void list(){
		Model user = getCurrentLoginUser();
		String select = "select *";
		StringBuffer sb = new StringBuffer();
		sb.append("from approval as a left join teacher as b on a.approvaler=b.id where");
		sb.append(" left join Appry_type as c on c.id=a.apply_type where a.approvaler="+user.getInt("id")+" \n");
		Page<Model> pager = getList(Approval.class, select, sb.toString());
		for(Model m : pager.getList()){
			m.put("create_time", DateUtil.format(m.getLong("create_time"), "yyyy-MM-dd"));
		}
		setAttr("pager",pager);
		render("list.html");
	}
	
	public void edit() throws Exception{
		String op = getPara("op");
		Model user = getCurrentLoginUser();
		if("update".equals(op)){
			Approval rd = new Approval();
			rd.BindModel(this, rd);
			rd.set("apply_id", Integer.valueOf(getPara("apply_id")));
			rd.set("approvaler", user.getInt("id"));
			rd.set("is_right", 1);
			rd.set("create_time", DateUtil.getCurrentTimeStamp());
			rd.save();
			boolean b= Approval.dao.doApproval(user,rd);
			renderJson(getRtn(b));
		}else if("edit".equals(op)){
			setAttr("teacher",user);
			int id = getParaToInt("id");
			Model approval = Approval.dao.findById(id);
			Model apply_type = ApplyType.dao.findById(approval.getInt("apply_type"));
			approval.put("apply_type_name", apply_type.getStr("name"));
			render("edit.html");
		}else{
			int apply_type = getParaToInt("apply_type");
			int apply_id = getParaToInt("apply_id");
			setAttr("apply_id",apply_id);
			setAttr("teacher",user);
			setAttr("apply_type",ApplyType.dao.findById(apply_type));
			doNew(Approval.class);
			render("edit.html");
		}
		
	}
	
	public void del() throws Exception{
		boolean b = doDel(Approval.class,Global.DEL_TYPE.PHYSICAL);
		renderJson(getRtn(b));
	}

}
