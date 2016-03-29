package org.studentworker.com.jfinal.controller;

import org.studentworker.com.model.ApprovalStatus;
import org.studentworker.com.model.ApprovalProcess;
import org.studentworker.com.model.LeaveApply;
import org.studentworker.com.model.Role;
import org.studentworker.com.util.DateUtil;
import org.studentworker.com.util.Global;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class LeaveApplyController extends BaseController{

	public void list(){
		Model user = getCurrentLoginUser();
		int type = user.getInt("type");
		String select = null;
		String from = null;
		if(type==Global.USER_TYPE.TEACHER){
			select = "select a.*,b.status as status,b.current_step as step";
			from = "from leave_apply as a left join approval_status as b on a.id=b.apply_id left join "
					+ "student c on c.stu_no=a.stu_no"
					+ " left join class d on d.id=c.class_id  where d.instructor="+user.getInt("id");
		}else{
			select = "select a.*,b.status as status,b.current_step as step";
			from = "from leave_apply as a left join approval_status as b on a.id=b.apply_id where stu_no='"+user.getStr("stu_no")+"'";
		}
		Record record = Role.dao.getApprovalRoleByUserId(getLoginUserId(), Global.ApplyStatus.Leave);
		Page<Model> pager = getList(LeaveApply.class,select,from);
		for(Model m : pager.getList()){
			m.put("begin_time", DateUtil.format(m.getLong("begin_time"), "yyyy-MM-dd"));
			m.put("end_time", DateUtil.format(m.getLong("end_time"), "yyyy-MM-dd"));
			m.put("apply_time", DateUtil.format(m.getLong("apply_time"), "yyyy-MM-dd"));
			boolean canApproval = false;
			if(record!=null&&record.getInt("step")==(m.getInt("step")+1)&&type==Global.USER_TYPE.TEACHER){
				canApproval = true;
			}
			m.put("canApproval", canApproval);
		}
		setAttr("pager", pager);
		render("list.html");
	}

	public void edit() throws Exception{
		String op = getPara("op");
		if("update".equals(op)){
			LeaveApply leaveApply = new LeaveApply();
			leaveApply.BindModel(this, leaveApply);
			String beginTime = getPara("begin_time");
			String endTime = getPara("end_time");
			String applyTime = getPara("apply_time");
			leaveApply.set("begin_time", DateUtil.dateFormat(beginTime, "yyyy-MM-dd"));
			leaveApply.set("end_time", DateUtil.dateFormat(endTime, "yyyy-MM-dd"));
			leaveApply.set("apply_time", DateUtil.dateFormat(applyTime, "yyyy-MM-dd"));
			leaveApply.set("apply_type", 1);
			boolean b = leaveApply.save();
			ApprovalStatus as = new ApprovalStatus();
			as.set("apply_type", Global.ApplyStatus.Leave);
			as.set("status", Global.ApplyStatus.Doing);
			as.set("apply_id", leaveApply.get("id"));
			as.set("times", 1);
			as.set("current_step", 0);
			ApprovalProcess ap = (ApprovalProcess) ApprovalProcess.dao.findFirst("select * from approval_process where apply_type_id="+1+" and del_time is null");
			as.set("all_step", ap.getInt("steps"));
			as.set("is_right", 1);
			as.save();
			renderJson(getRtn(b));
		}else if("edit".equals(op)){
			Model student = getCurrentLoginUser();
			setAttr("student",student);
			doEdit(LeaveApply.class);
			render("edit");
		}else{
			Model student = getCurrentLoginUser();
			setAttr("student",student);
			doNew(LeaveApply.class);
		}
	}
	
	public void del() throws Exception{
		int id = getParaToInt("id");
		boolean b = LeaveApply.dao.deleteById(id);
		renderJson(getRtn(b));
	}
	
}
