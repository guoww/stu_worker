package org.studentworker.com.jfinal.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.PortableServer.ImplicitActivationPolicyValue;
import org.studentworker.com.jfinal.controller.BaseController;
import org.studentworker.com.model.BaseModel;
import org.studentworker.com.model.Role;
import org.studentworker.com.util.StringUtil;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Model;

public class AuthInterceptor implements Interceptor{

	private String[] notCheckUrls;
	private String[] authUrls;

	public AuthInterceptor() {

	}

	public String[] getNotCehckUrls() {
		return notCheckUrls;
	}

	public void setNotCehckUrls(String[] notCheckUrls) {
		this.notCheckUrls = notCheckUrls;
	}

	public String[] getAuthUrls() {
		return authUrls;
	}

	public void setAuthUrls(String[] authUrls) {
		this.authUrls = authUrls;
	}

	public void intercept(Invocation ai) {
		Controller ctr = ai.getController();
		if (ctr instanceof BaseController) {
			BaseController controller = (BaseController) ctr;
			controller.getRequest().setAttribute("Controller", controller);
			String actionKey = ai.getActionKey();
			if (!isAllow(actionKey) && isAuth(actionKey)) {
				if (!controller.isLogined()) {
					ctr.renderError(401);
					return;
				}
				Model user = controller.getCurrentLoginUser();
				if (user.getInt("isAdmin") == 1) {
					ai.invoke();
					return;
				}
				if (checkPermission(user.getInt("type"),user.getInt("id"),getUrl(ctr.getRequest(),ctr.getResponse()))) {
					ai.invoke();
					return;
				}
				ctr.renderError(403);
				return;
			}
		}
		ai.invoke();
	}

	private boolean checkPermission(int type,int userId,String url){
		List<Model> list = Role.dao.find(appendCheckPermissionSql(type,userId,url));
		if(list!=null&&list.size()>0){
			return true;
		}
		return false;
	}
	
	private String appendCheckPermissionSql(int type,int userId,String url){
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from ( \n");
		sb.append(" select * from user_role where type=").append(type).append(" and user_id=").append(userId).append("\n");
		sb.append(" ) as a left join role as b on a.role_id=b.id \n");
		sb.append(" inner join role_resource as c on b.id=c.role_id \n");
		sb.append(" left join resource as d on d.id=c.resource_id \n");
		sb.append(" where d.url= \n").append(url);
		return sb.toString();
	}
	
	private String getUrl(HttpServletRequest request, HttpServletResponse response) {
		String queryString = request.getQueryString();
		return request.getRequestURI() + (queryString == null ? "" : "?" + queryString);
	}

	private boolean isAuth(String actionKey) {
		if (authUrls != null && authUrls.length > 0) {
			for (String uri : authUrls) {
				if (matchUri(uri, actionKey)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	private boolean isAllow(String actionKey) {
		if (notCheckUrls != null) {
			for (String uri : notCheckUrls) {
				if (matchUri(uri, actionKey)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean matchUri(String uri, String actionKey) {
		if (uri.equals(actionKey)) {
			return true;
		} else if (uri.endsWith("/*")) {
			return actionKey.startsWith(uri.substring(0, uri.length() - 1)) || actionKey.equals(uri.substring(0, uri.length() - 2));
		} else if (uri.endsWith("*")) {
			return actionKey.startsWith(uri.substring(0, uri.length() - 1));
		}
		return false;
	}

}
