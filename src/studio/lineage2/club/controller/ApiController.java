package studio.lineage2.club.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import studio.lineage2.club.model.Server;
import studio.lineage2.club.model.ServerShow;
import studio.lineage2.club.service.ServerService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 Obi-Wan
 24.07.2016
 */
@RestController @RequestMapping("/api") public class ApiController
{
	@Autowired private ServerService serverService;

	@RequestMapping(value = "/getServers/{type}/{version}", method = RequestMethod.GET) List<ServerShow> getServers(@PathVariable int type, @PathVariable int version)
	{
		/*
		type:
		1 - открытие сегодня
		2 - открытие завтра
		3 - открытие скоро
		4 - уже открылись
		 */

		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());

		List<ServerShow> servers = new ArrayList<>();

		List<Server> allServers = serverService.findAll();

		Collections.sort(allServers, (o1, o2) ->
		{
			switch(type)
			{
				case 1:
				case 2:
				case 3:
					return o1.getDate().compareTo(o2.getDate());
				default:
					return o2.getDate().compareTo(o1.getDate());
			}
		});

		if(type != 4)
		{
			Collections.sort(allServers, (o1, o2) ->
			{
				if(o1.isVip() && o2.isVip())
				{
					return 0;
				}
				if(o1.isVip() && !o2.isVip())
				{
					return -1;
				}
				if(!o1.isVip() && o2.isVip())
				{
					return 1;
				}
				return 0;
			});
		}

		for(Server server : allServers)
		{
			if(!server.isActive())
			{
				continue;
			}

			Calendar openTime = Calendar.getInstance();
			openTime.setTime(server.getDate());

			boolean yes = false;
			switch(type)
			{
				case 1:
					if(now.get(Calendar.YEAR) == openTime.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) == openTime.get(Calendar.DAY_OF_YEAR))
					{
						yes = true;
					}
					break;
				case 2:
					if(now.get(Calendar.YEAR) == openTime.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) + 1 == openTime.get(Calendar.DAY_OF_YEAR))
					{
						yes = true;
					}
					break;
				case 3:
					if(now.get(Calendar.YEAR) == openTime.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) + 1 < openTime.get(Calendar.DAY_OF_YEAR))
					{
						yes = true;
					}
					break;
				case 4:
					if(now.get(Calendar.YEAR) == openTime.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) > openTime.get(Calendar.DAY_OF_YEAR))
					{
						yes = true;
					}
					break;
			}
			if(!yes)
			{
				continue;
			}

			if(version != 0 && server.getVersion() == version)
			{
				servers.add(serverService.serverShow(server));
			}
			else if(version == 0)
			{
				servers.add(serverService.serverShow(server));
			}
		}

		return servers;
	}
}