package studio.lineage2.club.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import studio.lineage2.club.service.ReCaptchaService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 Obi-Wan
 20.07.2016
 */
@Component public class RecaptchaInterceptor extends HandlerInterceptorAdapter
{
	@Autowired private ReCaptchaService reCaptchaService;

	@Override public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
	{
		//		switch(request.getMethod())
		//		{
		//			case "POST":
		//				switch(request.getRequestURI())
		//				{
		//					case "/enter/reg":
		//					case "/enter/auth":
		//					case "/enter/recover":
		//						ValidationResult validationResult = reCaptchaService.check(request.getParameter("g-recaptcha-response"), request.getRemoteAddr());
		//						if(!validationResult.isSuccess())
		//						{
		//							response.sendRedirect(request.getRequestURI());
		//						}
		//						break;
		//				}
		//				break;
		//		}
	}
}