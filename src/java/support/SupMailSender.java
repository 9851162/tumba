/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import service.UserService;

/**
 *
 * @author bezdatiuzer
 */
@Component
public class SupMailSender {
    
    @Autowired
    private org.springframework.mail.MailSender mailSender;

    @Autowired
    private SimpleMailMessage templateMessage;

    public void setMailSender(org.springframework.mail.MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }

    public void sendMail(String email, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setText(text);
        mailSender.send(message);
    }
    
}
