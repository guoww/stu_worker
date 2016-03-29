package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.studentworker.com.model.BaseModel;
import org.studentworker.com.model.Menu;
import org.studentworker.com.util.DateUtil;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public class MenuController extends BaseController{

	public void list(){
		Page<Model> pager = getList(Menu.class,
				"select a.id as id,a.name as name,a.level as level ,a.url as url ,a.parent_id as parent_id,b.name as parent_name",
				"from menu as a left join menu b on a.parent_id=b.id where a.level=1 and a.del_time is null order by a.id");
		setAttr("pager", pager);
		render("list.html");
	}

	public void edit() throws Exception{
		String op = getPara("op");
		if("update".equals(op)){
			doUpdate(Menu.class);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code", 1);
			map.put("msg","操作成功！");
			renderJson(map);
		}else if("edit".equals(op)){
			doEdit(Menu.class);
			List<Model> parents = Menu.dao.find("select * from menu where level=1 and del_time is null order by id");
			setAttr("parents",parents);
			render("edit.html");
		}else {
			doNew(Menu.class);
			List<Model> parents = Menu.dao.find("select * from menu where level=1 and del_time is null order by id");
			setAttr("parents",parents);
			render("edit.html");
		}
	}
	
	public void del() throws Exception{
		doDel(Menu.class);
	}
	
	public void treeTable(){
		int id = getParaToInt("id");
		List<Model> data = Menu.dao.find("select * from menu where parent_id="+id+" and del_time is null order by id");
		setAttr("rows", data);
		render("treeTableSub.html");
	}
}
