package studio.lineage2.club.controller;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 Obi-Wan
 23.08.2016
 */
@XmlAccessorType(value = XmlAccessType.NONE) @XmlRootElement(name = "url") public class XmlUrl
{
	@XmlElement private String loc;
	@XmlElement private String lastmod = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	@XmlElement private String changefreq = "daily";
	@XmlElement private String priority;

	public XmlUrl()
	{
	}

	public XmlUrl(String loc, Priority priority)
	{
		this.loc = loc;
		this.priority = priority.getValue();
	}

	public String getLoc()
	{
		return loc;
	}

	public String getPriority()
	{
		return priority;
	}

	public String getChangefreq()
	{
		return changefreq;
	}

	public String getLastmod()
	{
		return lastmod;
	}

	public enum Priority
	{
		HIGH("1.0"),
		MEDIUM("0.5");

		private String value;

		Priority(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}
	}
}