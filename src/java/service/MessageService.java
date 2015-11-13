/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.MessageDao;
import entities.Ad;
import entities.Message;
import entities.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.parent.PrimService;
import support.SupMailSender;

/**
 *
 * @author Алексей
 */
@Service
@Transactional
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MessageService extends PrimService {

    @Autowired
    private MessageDao msgDao;
    
    @Autowired
    private SupMailSender mailSender;

    public void create(User sender,String text, Ad ad) {
        if (text != null && !text.equals("")&& ad!=null) {
            Message msg = new Message();
            msg.setInsertDate(new Date());
            msg.setSender(sender);
            msg.setReceiver(ad.getAuthor());
            msg.setAd(ad);
            msg.setText(text);
            if (validate(msg)) {
                msgDao.save(msg);
            }
            User receiver = ad.getAuthor();
            //to do отправка сообщения если автор не на сайте только, а не всегда.
            String url = "http://185.22.232.79/seller";
            String mailText = sender.getName()+" оставил Вам сообщение: "+text+" в ответ на объявление "+ad.getName()+" ("+ad.getDescription()+") на сайте "+url;
            mailSender.sendMail(receiver.getEmail(), mailText);
        }
    }
    
    public List<Message>getDialog(Long senderId,Long recieverId){
        return msgDao.getDialog(senderId,recieverId);
    }

}
