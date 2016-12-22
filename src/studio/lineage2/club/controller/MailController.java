package studio.lineage2.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import studio.lineage2.club.model.SendMail;
import studio.lineage2.club.other.ValidationResult;
import studio.lineage2.club.service.ReCaptchaService;
import studio.lineage2.club.util.MailUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static studio.lineage2.club.controller.SiteController.addAttributes;

/**
 Obi-Wan
 25.07.2016
 */
@Controller @RequestMapping("/mail") public class MailController
{
	@Autowired private ReCaptchaService reCaptchaService;
	@Autowired private HttpServletRequest request;
	@Autowired private MailUtil mailUtil;

	@RequestMapping(value = "/send", method = { RequestMethod.GET }) public String index(ModelMap model)
	{
		model.addAttribute("sendMail", new SendMail());
		addAttributes(model, "/pages/contacts.vm");
		model.addAttribute("title", "Контакты");
		return "main";
	}

	@RequestMapping(value = "/send", method = { RequestMethod.POST }) public String post(ModelMap model, @ModelAttribute(value = "sendMail") @Valid SendMail sendMail, BindingResult result)
	{
		ValidationResult validationResult = reCaptchaService.check(request.getParameter("g-recaptcha-response"), request.getRemoteAddr());
		if(!validationResult.isSuccess())
		{
			model.addAttribute("sendMail", sendMail);
			model.addAttribute("recaptchaError", "Пройдите проверку");
			addAttributes(model, "/pages/contacts.vm");
			return "main";
		}
		if(result.hasErrors())
		{
			model.addAttribute("sendMail", sendMail);
			addAttributes(model, "/pages/contacts.vm");
			return "main";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Email: ").append(sendMail.getEmail()).append("<br><br>");
		sb.append("Сообщение: ").append("<br><br>").append(sendMail.getContent());

		mailUtil.send("eanseen@gmail.com", "Info", sb.toString());

		model.addAttribute("success", true);
		return index(model);
	}
}