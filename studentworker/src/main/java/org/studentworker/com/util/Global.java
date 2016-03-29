package org.studentworker.com.util;

public class Global {

	public interface ApplyStatus{
		public static int Leave=1;
		public static int Off_Campus=1;
		public static int Stu_Card=1;
		public static int Arrears_Reg=1;
		public static int Arrears_Clear=1;
		
		/**
		 * 审批中
		 */
		public static int Doing = 1;
		/**
		 * 审批完成
		 */
		public static int Finish = 2;
		/**
		 * 审批不通过
		 */
		public static int Fail = 3;
	}
	
	/**
	 * 删除类型
	 * @author Administrator
	 *
	 */
	public interface DEL_TYPE{
		/**
		 * 物理删除
		 */
		public static int PHYSICAL = 1;
		/**
		 * 逻辑删除
		 */
		public static int LOGIC = 2;
	}
	
	public interface USER_TYPE{
		/**
		 * 学生用户
		 */
		public static int STUDENT=1;
		/**
		 * 教工用户
		 */
		public static int TEACHER = 2;
	}
}
