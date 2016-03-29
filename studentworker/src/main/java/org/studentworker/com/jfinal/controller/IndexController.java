package org.studentworker.com.jfinal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.studentworker.com.model.Student;
import org.studentworker.com.model.Teacher;

import com.jfinal.plugin.activerecord.Model;

public class IndexController extends BaseController{
	
	public void index(){
		render("index.html");
	}
	
	public void table(){
		render("table.html");
	}
	
	public void toLogin(){
		render("login.html");
	}
	
	public void login(){
		int type = getParaToInt("type");
		String userName = getPara("username");
		String password = getPara("password");
		Map<String,Object> map = new HashMap<String,Object>();
		//学生用户登录
		if(type==1){
			List<Model> stus = Student.dao.find("select * from student where stu_no="+userName);
			if(stus!=null&&stus.size()>0){
				String stu_pass = (String) stus.get(0).get("password");
				if(password.equals(stu_pass)){
					map.put("code", 1);
					map.put("msg", "登录成功！");
				}else{
					map.put("code", 0);
					map.put("msg", "密码错误！");
				}
			}else{
				map.put("code", 1);
				map.put("msg", "该学生用户不存在！");
			}
		//教工用户登录
		}else{
			List<Model> teas = Teacher.dao.find("select * from teacher where teacher_no="+userName);
			if(teas!=null&&teas.size()>0){
				String tea_pass = (String) teas.get(0).get("password");
				if(password.equals(tea_pass)){
					map.put("code", 1);
					map.put("msg", "登录成功！");
				}else{
					map.put("code", 0);
					map.put("msg", "密码错误！");
				}
			}else{
				map.put("code", 1);
				map.put("msg", "该教工用户不存在！");
			}
		}
		saveLoginInfo(type,userName);
		renderJson(map);
	}
	
	public void logout(){
		removeCookie("_eca_");
		render("login.html");
	}
}
