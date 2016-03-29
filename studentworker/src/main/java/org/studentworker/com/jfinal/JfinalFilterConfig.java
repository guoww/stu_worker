package org.studentworker.com.jfinal;

import org.studentworker.com.jfinal.controller.ApprovalController;
import org.studentworker.com.jfinal.controller.ArrearsController;
import org.studentworker.com.jfinal.controller.AwardsApplyController;
import org.studentworker.com.jfinal.controller.AwardsController;
import org.studentworker.com.jfinal.controller.AwardsGrantController;
import org.studentworker.com.jfinal.controller.BankController;
import org.studentworker.com.jfinal.controller.FundingController;
import org.studentworker.com.jfinal.controller.GloryController;
import org.studentworker.com.jfinal.controller.GloryRegistController;
import org.studentworker.com.jfinal.controller.IndexController;
import org.studentworker.com.jfinal.controller.InsuranceController;
import org.studentworker.com.jfinal.controller.LeaveApplyController;
import org.studentworker.com.jfinal.controller.LoanController;
import org.studentworker.com.jfinal.controller.MenuController;
import org.studentworker.com.jfinal.controller.ModuleController;
import org.studentworker.com.jfinal.controller.PoorController;
import org.studentworker.com.jfinal.controller.RewardController;
import org.studentworker.com.jfinal.controller.RoleController;
import org.studentworker.com.jfinal.controller.StudentController;
import org.studentworker.com.jfinal.controller.TeacherController;
import org.studentworker.com.jfinal.controller.WorkStudyController;
import org.studentworker.com.model.ApplyType;
import org.studentworker.com.model.Approval;
import org.studentworker.com.model.ApprovalProcess;
import org.studentworker.com.model.ApprovalProcessRole;
import org.studentworker.com.model.ApprovalStatus;
import org.studentworker.com.model.Arrears;
import org.studentworker.com.model.Awards;
import org.studentworker.com.model.AwardsApply;
import org.studentworker.com.model.AwardsGrant;
import org.studentworker.com.model.BankCard;
import org.studentworker.com.model.Glory;
import org.studentworker.com.model.GloryRegist;
import org.studentworker.com.model.Insurance;
import org.studentworker.com.model.InsurancePayment;
import org.studentworker.com.model.InsuranceRegist;
import org.studentworker.com.model.LeaveApply;
import org.studentworker.com.model.Menu;
import org.studentworker.com.model.Module;
import org.studentworker.com.model.PoorStudentFile;
import org.studentworker.com.model.PunishRegist;
import org.studentworker.com.model.Reward;
import org.studentworker.com.model.RewardRegist;
import org.studentworker.com.model.Role;
import org.studentworker.com.model.RoleResource;
import org.studentworker.com.model.Student;
import org.studentworker.com.model.StudentFunding;
import org.studentworker.com.model.StudentLoan;
import org.studentworker.com.model.Teacher;
import org.studentworker.com.model.WorkStudy;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class JfinalFilterConfig extends JFinalConfig{

	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setDevMode(true);
		me.setBaseViewPath("/WEB-INF/admin");
		/*Configuration config = new Configuration();
		config.setLocale(Locale.CHINA);
		config.setDefaultEncoding("utf-8");
		config.setEncoding(Locale.CHINA, "utf-8");
		try {
			config.setDirectoryForTemplateLoading(new File("main/src/webapp/WEB-INF/admin"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}

	@Override 
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		loadPropertyFile("jfinal.txt");
		System.out.println(getProperty("jdbcUrl"));
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("username"), getProperty("password"));
		me.add(c3p0Plugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("arrears", Arrears.class);
		arp.addMapping("insurance", Insurance.class);
		arp.addMapping("insurance_payment", InsurancePayment.class);
		arp.addMapping("insurance_regist", InsuranceRegist.class);
		arp.addMapping("punish_regist", PunishRegist.class);
		arp.addMapping("reward", Reward.class);
		arp.addMapping("reward_regist", RewardRegist.class);
		arp.addMapping("student", Student.class);
		arp.addMapping("poor_student_file", PoorStudentFile.class);
		arp.addMapping("work_study", WorkStudy.class);
		arp.addMapping("student_funding", StudentFunding.class);
		arp.addMapping("student_loan", StudentLoan.class);
		arp.addMapping("student_bank_card", BankCard.class);
		arp.addMapping("awards", Awards.class);
		arp.addMapping("awards_apply", AwardsApply.class);
		arp.addMapping("glory", Glory.class);
		arp.addMapping("glory_regist", GloryRegist.class);
		arp.addMapping("awards_grant", AwardsGrant.class);
		arp.addMapping("teacher", Teacher.class);
		arp.addMapping("menu", Menu.class);
		arp.addMapping("role", Role.class);
		arp.addMapping("role_module", RoleResource.class);
		arp.addMapping("module", Module.class);
		arp.addMapping("leave_apply", LeaveApply.class);
		arp.addMapping("approval_process", ApprovalProcess.class);
		arp.addMapping("approval_process_role", ApprovalProcessRole.class);
		arp.addMapping("approval", Approval.class);
		arp.addMapping("apply_type", ApplyType.class);
		arp.addMapping("approval_status", ApprovalStatus.class);
		
		/*EventPlugin eventPlugin = new EventPlugin();
		eventPlugin.scanJar();
		eventPlugin.async();
		me.add(eventPlugin);*/
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", IndexController.class);
		me.add("/reward",RewardController.class);
		me.add("/insurance",InsuranceController.class);
		me.add("/student",StudentController.class);
		me.add("/arrears",ArrearsController.class);
		me.add("/poor",PoorController.class);
		me.add("/workstudy",WorkStudyController.class);
		me.add("/funding",FundingController.class);
		me.add("/loan",LoanController.class);
		me.add("/bank",BankController.class);
		me.add("/awards",AwardsController.class);
		me.add("/awardsapply",AwardsApplyController.class);
		me.add("/glory",GloryController.class);
		me.add("/gloryregist",GloryRegistController.class);
		me.add("/grant",AwardsGrantController.class);
		me.add("/teacher",TeacherController.class);
		me.add("/menu",MenuController.class);
		me.add("/role",RoleController.class);
		me.add("/module",ModuleController.class);
		me.add("/leave",LeaveApplyController.class);
		me.add("/approval",ApprovalController.class);
	}

}
