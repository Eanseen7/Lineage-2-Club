package studio.lineage2.club.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import studio.lineage2.club.model.SendMail;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 Obi-Wan
 24.11.2015
 */
@Component public class MailUtil
{
	@Value("${mail.enabled}") private boolean enabled;

	@Value("${mail.smtp}") private String smtp;

	@Value("${mail.port}") private int port;

	@Value("${mail.email}") private String email;

	@Value("${mail.password}") private String password;

	@Value("${mail.ssl}") private boolean ssl;

	@Value("${mail.title}") private String title;

	public boolean isEnabled()
	{
		return enabled;
	}

	public String getTitle()
	{
		return title;
	}

	private JavaMailSenderImpl mailSender;

	public void send(String to, String title, String content)
	{
		if(!enabled)
		{
			return;
		}

		if(mailSender == null)
		{
			mailSender = new JavaMailSenderImpl();
			mailSender.setProtocol("smtp");
			mailSender.setHost(smtp);
			mailSender.setPort(port);
			mailSender.setUsername(email);
			mailSender.setPassword(password);

			Properties mailProps = new Properties();
			mailProps.put("mail.smtp.auth", "true");
			mailProps.put("mail.smtp.ssl.enable", ssl);

			mailSender.setJavaMailProperties(mailProps);
		}

		try
		{
			MimeMessage mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			helper.setFrom(email);
			helper.setTo(to);
			helper.setSubject(title);

			mimeMessage.setHeader("Content-Type", "text/html; charset=\"utf-8\"");
			mimeMessage.setContent(content, "text/html; charset=utf-8");

			mailSender.send(mimeMessage);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private List<SendMail> sendMails = new CopyOnWriteArrayList<>();

	@Scheduled(initialDelay = 1000, fixedRate = 3000)
	public void load()
	{
		if(sendMails.isEmpty())
		{
			return;
		}
		SendMail sendMail = sendMails.remove(0);
		send(sendMail.getEmail(), "Info", sendMail.getContent());
	}
}