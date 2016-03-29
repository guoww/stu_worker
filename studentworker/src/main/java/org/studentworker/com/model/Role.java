package org.studentworker.com.model;

import java.util.List;

import org.studentworker.com.util.Global;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;



public class Role  extends BaseModel{
	public static final Role dao = new Role();
	
	public Record getApprovalRoleByUserId(int userId,int applyTypeId){
		StringBuffer roleSb = new StringBuffer();
		roleSb.append(" select a.id as id,c.step as step from Role as a  \n");
		roleSb.append(" inner join user_role as b on b.role_id=a.id and b.type="+Global.USER_TYPE.TEACHER+" and b.user_id="+userId+" \n");
		roleSb.append(" inner join approval_process_role as c on c.role_id=b.role_id \n");
		roleSb.append(" inner join approval_process as d on d.id=c.approval_process_id and d.apply_type_id="+applyTypeId+" \n");
		Record role = Db.findFirst(roleSb.toString());
		return role;
	}
}
