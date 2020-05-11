package com.wzy.Emails;

import com.wzy.Emails.dto.Meeting;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.naming.*;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailsApplicationTests {

	@Autowired
	private JavaMailSender javaMailSender;

	@Test
	public void sendSimpleMail() {
		SimpleMailMessage emailMsg = new SimpleMailMessage();
		//邮件主题
		emailMsg.setSubject("简单邮件主题");
		//发件人
		emailMsg.setFrom("jerome_wzy@163.com");
		//收件人
		emailMsg.setTo("461402931@qq.com");
		//抄送人
		//emailMsg.setCc("xxxxx1@qq.com");
		//密送人
		//emailMsg.setBcc("xxxxx2@qq.com");
		//发送日志
		emailMsg.setSentDate(new Date());
		//文本正文
		emailMsg.setText("这是测试邮件的正文文本");

		//发送邮件
		javaMailSender.send(emailMsg);
	}

	@Test
	public void sendAttachMail() {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
			//邮件主题
			mimeMessageHelper.setSubject("附件邮件主题");
			//发件人
			mimeMessageHelper.setFrom("jerome_wzy@163.com");
			//收件人
			mimeMessageHelper.setTo("461402931@qq.com");
			//抄送人
			//mimeMessageHelper.setCc("xxxxx1@qq.com");
			//密送人
			//mimeMessageHelper.setBcc("xxxxx2@qq.com");
			//发送日志
			mimeMessageHelper.setSentDate(new Date());
			//文本正文
			mimeMessageHelper.setText("附件邮件正文");

			//添加附件
			File file = new File("E:\\git\\git_src\\SpringBoot-learning-samples\\Emails-2.png");
			mimeMessageHelper.addAttachment("Emails-2.png",file);
			//发送邮件
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void sendResourceMail() {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
			//邮件主题
			mimeMessageHelper.setSubject("内嵌资源邮件主题");
			//发件人
			mimeMessageHelper.setFrom("jerome_wzy@163.com");
			//收件人
			mimeMessageHelper.setTo("461402931@qq.com");
			//抄送人
			//mimeMessageHelper.setCc("xxxxx1@qq.com");
			//密送人
			//mimeMessageHelper.setBcc("xxxxx2@qq.com");
			//发送日志
			mimeMessageHelper.setSentDate(new Date());


			//添加附件
			File file = new File("E:\\git\\git_src\\SpringBoot-learning-samples\\Emails-2.png");
			mimeMessageHelper.addInline("context-id123",file);

			//内嵌资源时，注意设置文本属性时，通过cid先占位，然后在设置图片
			mimeMessageHelper.setText("<html><body><p>图片显示如下：</p><img src='cid:context-id123'></body></html>", true);
			//发送邮件
			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void sendMailWithFreemarker() throws MessagingException, IOException, TemplateException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setSubject("FreeMarker邮件主题");
		mimeMessageHelper.setFrom("jerome_wzy@163.com");
		mimeMessageHelper.setTo("461402931@qq.com");
		mimeMessageHelper.setSentDate(new Date());
		//Freemarker配置
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
		// 加载模板位置，这里父类层级使用springBoot默认的templates
		ClassLoader classLoader = EmailsApplication.class.getClassLoader();
		configuration.setClassLoaderForTemplateLoading(classLoader, "templates");
		//加载模板
		Template template = configuration.getTemplate("meeting.ftl");
		Meeting meeting = new Meeting();
		meeting.setMeetName("关于市政停车位收费的讨论会议");
		meeting.setMeetUser("jerome");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		meeting.setMeetDate(simpleDateFormat.format(new Date()));
		StringWriter out = new StringWriter();
		template.process(meeting, out);
		//模板渲染
		mimeMessageHelper.setText(out.toString(),true);
		javaMailSender.send(mimeMessage);
	}

	@Autowired
	private TemplateEngine templateEngine;

	@Test
	public void sendMailWithThyemeleaf() throws MessagingException, IOException, TemplateException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setSubject("Thyemeleaf邮件主题");
		mimeMessageHelper.setFrom("jerome_wzy@163.com");
		mimeMessageHelper.setTo("461402931@qq.com");
		mimeMessageHelper.setSentDate(new Date());

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();

		context.setVariable("meetName", "关于市政停车位收费的讨论会议");
		context.setVariable("meetUser","jerome");
		context.setVariable("meetDate", simpleDateFormat.format(new Date()));
		String process = templateEngine.process("meeting.html", context);
		mimeMessageHelper.setText(process,true);

		javaMailSender.send(mimeMessage);
	}





}
