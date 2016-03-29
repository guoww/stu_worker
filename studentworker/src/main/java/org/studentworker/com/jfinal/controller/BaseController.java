package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.studentworker.com.event.testEvent;
import org.studentworker.com.jfinal.plugin.event.EventKit;
import org.studentworker.com.model.BaseModel;
import org.studentworker.com.model.Insurance;
import org.studentworker.com.model.Student;
import org.studentworker.com.model.Teacher;
import org.studentworker.com.util.DateUtil;
import org.studentworker.com.util.StringUtil;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

public class BaseController extends Controller{
	
	public int getLoginUserId(){
		Model user = getCurrentLoginUser();
		return user.getInt("id");
	}
	
	public Page<Model> getList(Class<? extends BaseModel> modelClass, String select, String where) {
		Table table = TableMapping.me().getTable(modelClass);
		if(select==null||"".equals(select)) select = "select * ";
		if(where==null||"".equals(where)) where = "from "+table.getName()+" where del_time is null order by "+table.getPrimaryKey()[0]+" desc ";
		int pageNumber = getPara("pageNum")==null?1:getParaToInt("pageNum");
		int pageSize = getPara("pageSize")==null?10:getParaToInt("pageSize");
		Page<Model> page = null;
		try {
			page = modelClass.newInstance().paginate(pageNumber, pageSize, select, where);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//EventKit.postEvent(new testEvent(modelClass));
		return page;
	}
	
	public BaseModel doUpdate(Class<? extends BaseModel> modelClass){
		BaseModel rd;
		try {
			rd = modelClass.newInstance();
			rd.BindModel(this, rd);
			rd.set("create_time", DateUtil.getCurrentTimeStamp());
			rd.set("teacher_id", getLoginUserId());
			rd.save();
			return rd;
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}
	
	public void doEdit(Class<? extends BaseModel> modelClass){
		try {
			int id = getParaToInt("id");
			setAttr("rd", modelClass.newInstance().findById(id));
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void doNew(Class<? extends BaseModel> modelClass){
		try {
			setAttr("rd",modelClass.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	public void doEdit(String view,Class<? extends BaseModel> modelClass) throws Exception{
		String op = getPara("op");
		if("update".equals(op)){
			BaseModel rd = modelClass.newInstance();
			updateBeforeHandle(this,modelClass.newInstance());
			rd.BindModel(this, rd);
			rd.set("create_time", DateUtil.getCurrentTimeStamp());
			rd.set("teacher_id", getLoginUserId());
			rd.save();
			afterUpdateHandle(this,modelClass.newInstance());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code", 1);
			map.put("msg","操作成功！");
			renderJson(map);
		}else if("edit".equals(op)){
			int id = getParaToInt("id");
			setAttr("rd", modelClass.newInstance().findById(id));
			editHandle(this,modelClass.newInstance());
			render(view);
		}else {
			addHandle(this,modelClass.newInstance());
			setAttr("rd",modelClass.newInstance());
			render(view);
		}
	}
	
	private void addHandle(BaseController bc, BaseModel model) {
		// TODO Auto-generated method stub
		String className = model.getClass().getName();
		String modelName = className.substring(className.lastIndexOf(".")+1);
		System.out.println(modelName);
	}

	private void editHandle(BaseController bc, BaseModel model) {
		// TODO Auto-generated method stub
		String className = model.getClass().getName();
		String modelName = className.substring(className.lastIndexOf(".")+1);
		System.out.println(modelName);
	}

	private void afterUpdateHandle(BaseController bc,
			BaseModel model) {
		// TODO Auto-generated method stub
		String className = model.getClass().getName();
		System.out.println(className);
	}

	private void updateBeforeHandle(BaseController bc,
			BaseModel model) {
		String className = model.getClass().getName();
		System.out.println(className);
		
	}

	private void serviceHandle(BaseController ai,
			Class<? extends BaseModel> modelClass) throws Exception {
		BaseModel model = modelClass.newInstance();
		String className = model.getClass().getName();
		if(className.equals("Menu")){
			System.out.println(true);
		}
		
	}

	public void doDel(Class<? extends BaseModel> modelClass) throws Exception{
		int id = getParaToInt("id");
		if(id>0){
			Model del = modelClass.newInstance().findById(id);
			del.set("del_id", getLoginUserId());
			del.set("del_time", DateUtil.getCurrentTimeStamp());
			del.update();
		}
		Map<String , Object> map = new HashMap<String,Object>();
		map.put("code", 1);
		map.put("msg", "操作成功!");
		renderJson(map);
	}

	public boolean doDel(Class<? extends BaseModel> modelClass,int DEL_TYPE) throws Exception{
		int id = getParaToInt("id");
		return modelClass.newInstance().deleteById(id);
	}
	
	public boolean isLogined() {
		String userInfo = getCookie("_eca_", "");
		if (StringUtil.isNotBlank(userInfo)) {
			return true;
		}
		return false;
	}

	public void saveLoginInfo(int type,String username){
		Cookie k = new Cookie("_eca_", type+"_"+username);
		setCookie(k);
	}
	
	public Model getCurrentLoginUser() {
		try {
			String userInfo = getCookie("_eca_", "");
			String[] infos = userInfo.split("_");
			int type = Integer.valueOf(infos[0]);
			String username = infos[1];
			Map<String,Object> map = new HashMap<String,Object>();
			if(type==1){
				map.put("stu_no", username);
				Model stu = Student.dao.findFirstByAttrs(map, Student.class);
				stu.put("type", type);
				return stu;
			}else{
				map.put("teacher_no", username);
				Model teacher = Teacher.dao.findFirstByAttrs(map, Teacher.class);
				teacher.put("type", type);
				return teacher;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public Map<String , Object> getRtn(boolean b){
		Map<String , Object> rtn = new HashMap<String , Object>();
		if(b){
			rtn.put("code", 1);
			rtn.put("msg", "操作成功!");
		}else{
			rtn.put("code", 0);
			rtn.put("msg", "操作失败!");
		}
		return rtn;
	}
}
