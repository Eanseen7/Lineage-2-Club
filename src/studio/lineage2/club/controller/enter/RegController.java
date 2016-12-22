package studio.lineage2.club.controller.enter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import studio.lineage2.club.model.MAccount;
import studio.lineage2.club.other.ValidationResult;
import studio.lineage2.club.service.MAccountService;
import studio.lineage2.club.service.ReCaptchaService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 Obi-Wan
 30.05.2016
 */
@Controller @RequestMapping("/enter") public class RegController
{
	@Autowired private ReCaptchaService reCaptchaService;
	@Autowired private HttpServletRequest request;
	@Autowired private MAccountService mAccountService;

	@RequestMapping(value = "/reg", method = { RequestMethod.GET }) public String index(ModelMap model)
	{
		model.addAttribute("mAccount", new MAccount());
		return "pages/reg";
	}

	@RequestMapping(value = "/reg", method = { RequestMethod.POST }) public String post(ModelMap model, @ModelAttribute(value = "mAccount") @Valid MAccount mAccount, BindingResult result)
	{
		ValidationResult validationResult = reCaptchaService.check(request.getParameter("g-recaptcha-response"), request.getRemoteAddr());
		if(!validationResult.isSuccess())
		{
			model.addAttribute("recaptchaError", "Пройдите проверку");
			model.addAttribute("mAccount", mAccount);
			return "pages/reg";
		}
		if(result.hasErrors())
		{
			model.addAttribute("mAccount", mAccount);
			return "pages/reg";
		}
		if(!mAccount.getPassword().equals(mAccount.getRepeatPassword()))
		{
			result.rejectValue("repeatPassword", "mAccount.repeatPassword", "Пароли не совпадают");
			model.addAttribute("mAccount", mAccount);
			return "pages/reg";
		}
		if(mAccountService.containsByUsername(mAccount.getUsername()))
		{
			result.rejectValue("username", "mAccount.username", "Аккаунт уже существует");
			model.addAttribute("mAccount", mAccount);
			return "pages/reg";
		}

		mAccount.setUsername(mAccount.getUsername().toLowerCase());
		mAccount.setPassword(new BCryptPasswordEncoder().encode(mAccount.getPassword()));
		mAccount.setAdmin(false);
		mAccountService.save(mAccount);

		model.addAttribute("success", true);
		return index(model);
	}
}