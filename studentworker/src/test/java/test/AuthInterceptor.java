package test;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.dudupark.model.Model;
import cn.dudupark.util.StringUtil;
import cn.dudupark.web.jfinal.SpringKit;
import cn.dudupark.web.jfinal.controller.BaseController;
import cn.dudupark.web.service.RolerService;
import cn.dudupark.web.service.UserService;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 权限拦截器
 * 
 * @author daniel.yan
 * @date 2014-8-1 下午2:57:30
 */
public class AuthInterceptor implements Interceptor {
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

	@Override
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
				Model userInfo = controller.getCurrentLoginUser();
				UserService userService = SpringKit.getBean(UserService.class);
				String userId = userInfo.getString(userService.getPKName());
				Model user = userService.get(userId);
				if (user.getInt("isAdmin") == 1) {
					ai.invoke();
					return;
				}
				String permission = SpringKit.getBean(RolerService.class).getPermissionByUri(actionKey);
				if (StringUtil.isNotBlank(permission)) {
					List<Model> list = userService.getResourceByUserId(userId);
					for (Model model : list) {
						String per = model.getString("permission");
						if (permission.equals(per)) {
							ai.invoke();
							return;
						}
					}
				}
				ctr.renderError(403);
				return;
			}
		}
		ai.invoke();
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
