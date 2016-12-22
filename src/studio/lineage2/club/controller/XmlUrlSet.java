package studio.lineage2.club.controller;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 Obi-Wan
 23.08.2016
 */
@XmlRootElement(name = "urlset") public class XmlUrlSet
{
	@XmlElements({ @XmlElement(name = "url", type = XmlUrl.class) }) private Collection<XmlUrl> xmlUrls = new ArrayList<XmlUrl>();

	@XmlAttribute(name="xmlns")
	private String xmlns = "http://www.sitemaps.org/schemas/sitemap/0.9";

	public void addUrl(XmlUrl xmlUrl)
	{
		xmlUrls.add(xmlUrl);
	}

	public Collection<XmlUrl> getXmlUrls()
	{
		return xmlUrls;
	}
}