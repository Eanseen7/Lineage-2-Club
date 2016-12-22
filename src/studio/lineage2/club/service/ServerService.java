package studio.lineage2.club.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.lineage2.club.CmsApplication;
import studio.lineage2.club.model.Server;
import studio.lineage2.club.model.ServerShow;
import studio.lineage2.club.repository.ServerRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 Obi-Wan
 23.07.2016
 */
@Service public class ServerService
{
	@Autowired private ServerRepository serverRepository;

	public void save(Server server)
	{
		serverRepository.save(server);
	}

	public void delete(long serverId)
	{
		serverRepository.delete(serverId);
	}

	public List<Server> findAll()
	{
		return serverRepository.findAll();
	}

	public Server findOne(long serverId)
	{
		return serverRepository.findOne(serverId);
	}

	public List<ServerShow> findUnActiveShow()
	{
		List<ServerShow> servers = new ArrayList<>();
		for(Server server : findAll())
		{
			if(!server.isActive())
			{
				servers.add(serverShow(server));
			}
		}
		return servers;
	}

	public ServerShow serverShow(Server server)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
		ServerShow serverShow = new ServerShow();
		serverShow.setId(server.getId());
		serverShow.setDomain(server.getDomain().toLowerCase());
		serverShow.setDescription(server.getDescription());
		serverShow.setVersion(CmsApplication.getVersions().get(server.getVersion()).getName());
		serverShow.setRate(server.getRate());
		serverShow.setDate(simpleDateFormat.format(server.getDate()));
		serverShow.setPartner(server.isPartner());
		serverShow.setVip(server.isVip());
		return serverShow;
	}
}