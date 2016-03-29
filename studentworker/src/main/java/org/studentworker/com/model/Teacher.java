package org.studentworker.com.model;


public class Teacher  extends BaseModel{
	public static final Teacher dao = new Teacher();

	public interface Global_Setting{
		public static String html_list_title = "教工信息列表";
		
		public static String html_edit_title = "教工信息编辑";
		
		public static String url = "/teacher";
		
	}
	
	public interface field_Setting{
		
	}
}
