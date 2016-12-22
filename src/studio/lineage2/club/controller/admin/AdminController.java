package studio.lineage2.club.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import studio.lineage2.club.model.Server;
import studio.lineage2.club.service.ServerService;

import static studio.lineage2.club.controller.SiteController.addAttributes;

/**
 Obi-Wan
 25.07.2016
 */
@Controller @RequestMapping("/admin") public class AdminController
{
	@Autowired private ServerService serverService;

	@RequestMapping(method = { RequestMethod.GET }) public String index(ModelMap model)
	{
		addAttributes(model, "/admin/index.vm");
		model.addAttribute("servers", serverService.findUnActiveShow());
		return "main";
	}

	@RequestMapping(value = "/restart", method = { RequestMethod.GET })
	public void restart()
	{
		System.exit(2);
	}

	@RequestMapping(value = "/success/{serverId}", method = { RequestMethod.GET }) public String success(ModelMap model, @PathVariable long serverId)
	{
		Server server = serverService.findOne(serverId);
		server.setActive(true);
		serverService.save(server);
		return index(model);
	}

	@RequestMapping(value = "/fail/{serverId}", method = { RequestMethod.GET }) public String fail(ModelMap model, @PathVariable long serverId)
	{
		serverService.delete(serverId);
		return index(model);
	}
}