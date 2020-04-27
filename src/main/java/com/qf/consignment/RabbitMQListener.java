package com.qf.consignment;

import com.qf.entity.Email;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

//TODO I10 监听交换机并发送邮件
@Component
public class RabbitMQListener {
    //发送邮件的对象，干活的对象
    @Autowired
    private JavaMailSender javaMailSender;

    //通过@Value从配置文件中读取发送者
    @Value("${spring.mail.username}")
    private String from;

    @RabbitListener(queuesToDeclare = @Queue(name = "mail_queue"))
    public void msgHandler(Email email) throws MessagingException {
        //1.创建一封邮件，这个mimeMessage不是Spring提供的，而是Java提供的
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //2.MimeMessageHelper是Spring提供的一个便捷的邮件设置对象
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        //3.设置邮件的内容
        //3.1标题
        mimeMessageHelper.setSubject(email.getSubject());
        //3.2发送方
        mimeMessageHelper.setFrom(from);
        //3.3接收方
        mimeMessageHelper.setTo(email.getTo());//接收者
        //mimeMessageHelper.setCc("xiaohong@qq.com");//抄送
        //mimeMessageHelper.setBcc("xiaogang@qq.com");//密送
        //3.4设置内容
        mimeMessageHelper.setText(email.getContext(), true);
        //3.5发送附件
        //mimeMessageHelper.addAttachment("我的附件.jpg", new File("C:\\Users\\Ken\\Pictures\\Saved Pictures\\联盟.jpg"));
        //3.6设置当前时间
        mimeMessageHelper.setSentDate(email.getSendTime());

        //4.发送邮件
        javaMailSender.send(mimeMessage);

        System.out.println("邮件发送成功！");
    }
}
