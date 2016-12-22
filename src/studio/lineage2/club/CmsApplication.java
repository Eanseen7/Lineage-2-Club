package studio.lineage2.club;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import studio.lineage2.club.xml.Rate;
import studio.lineage2.club.xml.Version;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

@SpringBootApplication @EnableAutoConfiguration @ComponentScan @EnableScheduling public class CmsApplication
{
	private static @Getter Map<Integer, Version> versions = new TreeMap<>();
	private static @Getter Map<Integer, Rate> rates = new TreeMap<>();

	public static void main(String[] args)
	{
		loadVerions();
		loadRates();
		SpringApplication.run(CmsApplication.class, args);
	}

	private static void loadVerions()
	{
		try
		{
			File fXmlFile = new File("./public/data/verions.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList root = doc.getElementsByTagName("version");

			for(int i = 0; i < root.getLength(); i++)
			{
				Node node = root.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) node;
					Version version = new Version();
					version.setId(Integer.parseInt(element.getAttribute("id")));
					version.setName(element.getAttribute("name"));
					versions.put(version.getId(), version);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static void loadRates()
	{
		try
		{
			File fXmlFile = new File("./public/data/rates.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList root = doc.getElementsByTagName("rate");

			for(int i = 0; i < root.getLength(); i++)
			{
				Node node = root.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE)
				{
					Element element = (Element) node;
					Rate rate = new Rate();
					rate.setId(Integer.parseInt(element.getAttribute("id")));
					rate.setName(element.getAttribute("name"));
					rates.put(rate.getId(), rate);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}