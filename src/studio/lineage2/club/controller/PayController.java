package studio.lineage2.club.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import studio.lineage2.club.model.IMessage;
import studio.lineage2.club.model.Server;
import studio.lineage2.club.service.ServerService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static studio.lineage2.club.controller.SiteController.addAttributes;

/**
 Obi-Wan
 16.06.2016
 */
@Controller @RequestMapping("/pay") public class PayController
{
	@Value("${unitpayMerchantId}") private int unitpayMerchantId;

	@Value("${unitpaySecretKey1}") private String unitpaySecretKey1;

	@Value("${unitpaySecretKey2}") private String unitpaySecretKey2;

	@Autowired private ServerService serverService;

	public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map)
	{
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, (o1, o2) -> (o1.getKey()).compareTo(o2.getKey()));

		Map<K, V> result = new LinkedHashMap<>();
		for(Map.Entry<K, V> entry : list)
		{
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

	@RequestMapping(value = "/vip", method = { RequestMethod.GET }) public String index(ModelMap model)
	{
		addAttributes(model, "/pages/pay.vm");
		model.addAttribute("title", "Получить VIP");
		return "main";
	}

	@RequestMapping(value = "/init/{serverId}", method = { RequestMethod.GET }) public @ResponseBody IMessage init(@PathVariable int serverId)
	{
		if(serverService.findOne(serverId) == null)
		{
			return new IMessage(IMessage.Type.SUCCESS, "/pay/vip");
		}

		StringBuilder signature = new StringBuilder();
		signature.append(serverId);
		signature.append("{up}").append("RUB");
		signature.append("{up}").append("Lineage2.Town");
		signature.append("{up}").append(300);
		signature.append("{up}").append(unitpaySecretKey2);

		StringBuilder sb = new StringBuilder();
		sb.append(unitpaySecretKey1);
		sb.append("?sum=").append(300);
		sb.append("&account=").append(serverId);
		sb.append("&desc=").append("Lineage2.Town");
		sb.append("&currency=").append("RUB");
		sb.append("&signature=").append(DigestUtils.sha256Hex(signature.toString()));

		return new IMessage(IMessage.Type.SUCCESS, "https://unitpay.ru/pay/" + sb.toString());
	}

	@RequestMapping(value = "/checkunitpay", method = { RequestMethod.GET }) public @ResponseBody String checkunitpay(HttpServletRequest request)
	{
		Map<String, String[]> params = request.getParameterMap();

		String method = params.get("method")[0];

		String signature = method + "{up}";

		for(Map.Entry<String, String[]> param : sortByKey(params).entrySet())
		{
			if(param.getKey().equalsIgnoreCase("method") || param.getKey().equalsIgnoreCase("params[sign]") || param.getKey().equalsIgnoreCase("params[signature]"))
			{
				continue;
			}
			signature += param.getValue()[0] + "{up}";
		}
		signature += unitpaySecretKey2;
		signature = DigestUtils.sha256Hex(signature);

		if(!signature.equals(params.get("params[signature]")[0]))
		{
			return "{\"error\": {\"message\": \"Ошибка\"}}";
		}

		if(method.equals("pay"))
		{
			int serverId = Integer.parseInt(params.get("params[sum]")[0]);
			Server server = serverService.findOne(serverId);
			server.setVip(true);
			serverService.save(server);
		}

		return "{\"result\": {\"message\": \"Запрос успешно обработан\"}}";
	}
}