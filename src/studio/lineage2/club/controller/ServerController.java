package studio.lineage2.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import studio.lineage2.club.CmsApplication;
import studio.lineage2.club.controller.ApiController;
import studio.lineage2.club.model.MAccount;
import studio.lineage2.club.model.Server;
import studio.lineage2.club.model.ServerShow;
import studio.lineage2.club.other.ValidationResult;
import studio.lineage2.club.service.ReCaptchaService;
import studio.lineage2.club.service.ServerService;
import studio.lineage2.club.util.MailUtil;
import studio.lineage2.club.xml.Version;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Map;
import java.util.TreeMap;

import static studio.lineage2.club.controller.SiteController.addAttributes;

/**
 Obi-Wan
 23.07.2016
 */
@Controller @RequestMapping("/server") public class ServerController
{
	@Autowired private ReCaptchaService reCaptchaService;
	@Autowired private HttpServletRequest request;
	@Autowired private ServerService serverService;
	@Autowired private MailUtil mailUtil;

	@RequestMapping(value = "/add", method = { RequestMethod.GET }) public String index(ModelMap model)
	{

		Map<Integer, Version> versions = new TreeMap<>();
		for(Version version : CmsApplication.getVersions().values())
		{
			if(version.getId() == 0)
			{
				continue;
			}
			versions.put(version.getId(), version);
		}

		model.addAttribute("versions", versions);

		model.addAttribute("server", new Server());
		addAttributes(model, "/pages/addServer.vm");
		model.addAttribute("title", "Добавить сервер");
		return "main";
	}

	@RequestMapping(value = "/show/{serverId}", method = { RequestMethod.GET }) public String index(ModelMap model, @PathVariable long serverId)
	{
		Server server = serverService.findOne(serverId);
		if(server == null)
		{
			addAttributes(model, "/pages/index.vm");
			return "main";
		}

		ServerShow serverShow = serverService.serverShow(server);
		addAttributes(model, "/pages/showServer.vm");
		model.addAttribute("title", serverShow.getDomain() + " - " + serverShow.getVersion() + " - x" + serverShow.getRate());
		model.addAttribute("server", serverShow);
		return "main";
	}

	@RequestMapping(value = "/add", method = { RequestMethod.POST }) public String post(ModelMap model, @ModelAttribute(value = "server") @Valid Server server, BindingResult result)
	{
		boolean check = true;

		try
		{
			MAccount mAccount = (MAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(mAccount.isAdmin())
			{
				check = false;
			}
		}
		catch(Exception ignored)
		{
		}

		ValidationResult validationResult = reCaptchaService.check(request.getParameter("g-recaptcha-response"), request.getRemoteAddr());

		if(check && !validationResult.isSuccess())
		{
			model.addAttribute("recaptchaError", "Пройдите проверку");
			model.addAttribute("server", server);
			addAttributes(model, "/pages/addServer.vm");
			return "main";
		}

		if(result.hasErrors())
		{
			model.addAttribute("server", server);
			addAttributes(model, "/pages/addServer.vm");
			return "main";
		}

		server.setDomain(server.getDomain().replace("http://", "").replace("https://", "").toLowerCase());
		server.setActive(false);
		server.setPartner(false);
		server.setVip(false);
		serverService.save(server);

		StringBuilder sb = new StringBuilder();
		sb.append("Новый сервер ожидает модерации");

		mailUtil.send("e@gmail.com", "New Server", sb.toString());

		model.addAttribute("success", true);
		return index(model);
	}
}