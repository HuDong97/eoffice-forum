package com.eoffice.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dong964669546@qq.com"); // 发件人
        message.setTo(to);                      // 收件人
        message.setSubject(subject);            // 主题
        message.setText(body);                  // 邮件正文

        try {
            mailSender.send(message);           // 发送邮件
            return true;                        // 发送成功
        } catch (MailException e) {
            e.printStackTrace();                // 打印异常栈信息
            return false;                       // 发送失败
        }
    }
}

