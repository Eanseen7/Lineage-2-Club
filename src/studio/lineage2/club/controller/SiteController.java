package studio.lineage2.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import studio.lineage2.club.CmsApplication;
import studio.lineage2.club.model.Server;
import studio.lineage2.club.other.ValidationResult;
import studio.lineage2.club.service.ReCaptchaService;
import studio.lineage2.club.service.ServerService;
import studio.lineage2.club.util.MailUtil;
import studio.lineage2.club.xml.Version;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

/**
 Obi-Wan
 29.05.2016
 */
@Controller @RequestMapping("/") public class SiteController
{
	@Autowired private ApiController apiController;

	@Autowired private ServerService serverService;

	public static void addAttributes(ModelMap model, String page)
	{
		model.addAttribute("title", "Все Lineage 2 Сервера");

		String s = "";
		for(Version version : CmsApplication.getVersions().values())
		{
			if(version.getId() < 2)
			{
				continue;
			}
			s+= version.getName() + ", ";
		}
		model.addAttribute("keywords", s.substring(0, s.length() - 2));
		s += ", lineage, lineage 2, сервер lineage, lineage 2 сервера";
		model.addAttribute("keywordsAdd", s);
		model.addAttribute("description", "Тут вы сможете выбрать качественный сервер lineage 2");

		model.addAttribute("header", "header.vm");
		model.addAttribute("menu", "menu.vm");
		model.addAttribute("page", page);
		model.addAttribute("footer", "footer.vm");
	}

	@RequestMapping(method = {
			RequestMethod.GET,
			RequestMethod.HEAD
	}) public String index(ModelMap model)
	{
		Map<Integer, Version> versions = new TreeMap<>();
		for(Version version : CmsApplication.getVersions().values())
		{
			versions.put(version.getId(), version);
		}

		model.addAttribute("versions", versions);

		model.addAttribute("opentoday", apiController.getServers(1, 0));
		model.addAttribute("opentomorrow", apiController.getServers(2, 0));
		model.addAttribute("notopen", apiController.getServers(3, 0));
		model.addAttribute("open", apiController.getServers(4, 0));

		addAttributes(model, "/pages/index.vm");
		return "main";
	}

	@RequestMapping(value = "/top", method = { RequestMethod.GET }) public String top(ModelMap model)
	{
		addAttributes(model, "/pages/lastPost.vm");
		model.addAttribute("title", "Популярное");
		return "main";
	}

	@RequestMapping(value = "/page/{page}", method = { RequestMethod.GET }) public String page(ModelMap model, @PathVariable String page)
	{
		addAttributes(model, "/pages/" + page + ".vm");
		return "main";
	}

	@RequestMapping(value = "/sitemap.xml", method = RequestMethod.GET) @ResponseBody public XmlUrlSet main()
	{
		XmlUrlSet xmlUrlSet = new XmlUrlSet();
		create(xmlUrlSet, "", XmlUrl.Priority.HIGH);

		create(xmlUrlSet, "/top", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/server/add", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/pay/vip", XmlUrl.Priority.HIGH);
		create(xmlUrlSet, "/mail/send", XmlUrl.Priority.HIGH);

		for(Server server : serverService.findAll())
		{
			create(xmlUrlSet, "/server/show/"+server.getId(), XmlUrl.Priority.HIGH);
		}

		return xmlUrlSet;
	}

//	@RequestMapping(value = "/robots.txt", method = RequestMethod.GET) @ResponseBody public String getRobots(HttpServletRequest request)
//	{
//		return "User-agent: *\n" + "Disallow:";
//	}

	private void create(XmlUrlSet xmlUrlSet, String link, XmlUrl.Priority priority)
	{
		xmlUrlSet.addUrl(new XmlUrl("http://lineage2.town" + link, priority));
	}
}