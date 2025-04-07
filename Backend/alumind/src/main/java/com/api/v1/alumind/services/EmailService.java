package com.api.v1.alumind.services;

import com.api.v1.alumind.exceptions.BadRequestException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Async("taskExecutor")
    public void sendEmailWeekReports(List<String> emailList, String subject, String content) {
        logger.info("Thread: {} - Iniciando Processo de envio de email.", Thread.currentThread().getName());
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setBcc(emailList.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new BadRequestException("Error sending email", e);
        } finally {
            logger.info("Thread: {} - Finalizando Processo de envio de email.", Thread.currentThread().getName());
        }
    }
}
