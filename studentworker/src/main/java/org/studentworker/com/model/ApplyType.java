package org.studentworker.com.model;

import org.studentworker.com.util.Global;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.TableMapping;



public class ApplyType  extends BaseModel{
	public static final ApplyType dao = new ApplyType();
	
	public boolean doApproval(Model user,BaseModel approval){
		Record role = Role.dao.getApprovalRoleByUserId(user.getInt("id"), approval.getInt("apply_type"));
		StringBuffer sb = new StringBuffer();
		sb.append("select * from approval_process as a \n");
		sb.append(" inner join approval_process_role as b on a.id=b.approval_process_id \n");
		sb.append("where b.role_id="+role.getInt("id")+" and a.apply_type_id="+approval.getInt("apply_type")+" \n");
		Record approvalP = Db.findFirst(sb.toString());
		Model as = ApplyType.dao.findFirst("select * from approval_status where apply_type="+approval.getInt("apply_type")+" and apply_id="+approval.getInt("apply_id"));
		as.set("current_step", role.getInt("step"));
		if(approval.getInt("is_agree")==1){
			if(role.getInt("step")<as.getInt("all_step")){
				as.set("status", Global.ApplyStatus.Doing);
			}else{
				as.set("status", Global.ApplyStatus.Finish);
			}
		}else{
			as.set("status", Global.ApplyStatus.Fail);
		}
		as.update();
		return true;
	}
}
