package studio.lineage2.club.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import studio.lineage2.club.CmsApplication;
import studio.lineage2.club.model.MAccount;
import studio.lineage2.club.xml.Version;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.TreeMap;

/**
 Obi-Wan
 20.07.2016
 */
@Component public class MainInterceptor extends HandlerInterceptorAdapter
{
	@Value("${recaptcha.siteKey}") private String recaptchaSiteKey;

	@Override public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		if(modelAndView == null)
		{
			return;
		}

		modelAndView.addObject("recaptchaSiteKey", recaptchaSiteKey);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication != null)
		{
			Object o = authentication.getPrincipal();
			if(o instanceof MAccount)
			{
				MAccount mAccount = (MAccount) o;
				modelAndView.addObject("mAccount", mAccount);
			}
		}
	}
}