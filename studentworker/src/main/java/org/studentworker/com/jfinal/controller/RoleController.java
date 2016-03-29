package org.studentworker.com.jfinal.controller;

import java.util.List;

import org.studentworker.com.model.BaseModel;
import org.studentworker.com.model.Module;
import org.studentworker.com.model.Role;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class RoleController extends BaseController{

	public void list(){
		Page<Model> pager = getList(Role.class,null,null);
		setAttr("pager", pager);
		render("list.html");
	}

	public void edit() throws Exception{
		doEdit("edit.html",Role.class);
	}
	
	public void del() throws Exception{
		doDel(Role.class);
	}
	
	public void permission(){
		int role_id = getParaToInt("id");
		List<Model> modules = Module.dao.find("select *,(case when (select count(*) from role_module as b "
				+ "where b.module_id=a.id and b.role_id="+role_id+")>0 then 1 else 0 end) as isHas from module as a where "
				+ "a.level=1 and a.del_time is null order by a.id");
		for(Model m : modules){
			List<Model> subs = Module.dao.find("select *,(case when (select count(*) from role_module as b "
					+ "where b.module_id=a.id and b.role_id="+role_id+")>0 then 1 else 0 end) as isHas "
					+ "from module as a where a.parent_id="+m.getInt("id")+" and a.del_time is null order by a.id");
			m.put("sub", subs);
		}
		setAttr("modules", modules);
		setAttr("role", Role.dao.findById(getPara("id")));
		List<Object> Role_Module = Db.query("select * from role_module where role_id="+role_id);
		setAttr("role_module",Role_Module);
		render("permission.html");
	}
	
	public void setPermission(){
		String op = getPara("op");
		int role_id = getParaToInt("role_id");
		int module_id = getParaToInt("module_id");
		if(op.equals("add")){
			String sql = "insert into role_module values (null,"+role_id+","+module_id+")";
			System.out.println(sql);
			Db.update(sql);
		}else{
			Db.update("delete from role_module where role_id="+role_id+" and module_id="+module_id);
		}
		renderJson("{}");
	}
}
