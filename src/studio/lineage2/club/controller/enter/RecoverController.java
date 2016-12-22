package studio.lineage2.club.controller.enter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 Obi-Wan
 30.05.2016
 */
@Controller @RequestMapping("/enter") public class RecoverController
{
	@RequestMapping(value = "/recover", method = { RequestMethod.GET }) public String index()
	{
		return "pages/recover";
	}
}