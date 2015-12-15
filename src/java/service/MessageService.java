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
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public void create(User sender,String subject,String text, Ad ad) {
        if (text != null && !text.equals("")&& ad!=null) {
            Integer l = text.length();
            BigDecimal t = BigDecimal.valueOf(l.longValue());
            BigDecimal messageCount = t.divide(BigDecimal.valueOf(1000L), 0, RoundingMode.UP);
            int i = 0;
            while(i<messageCount.intValue()){
                i++;
                int lastSymbol = (i*1000);
                int firstSymbol = lastSymbol-1000;
                if(i==messageCount.intValue()){
                    lastSymbol=l;
                }
                Message msg = new Message();
                msg.setInsertDate(new Date());
                if(subject==null||subject.equals("")){
                    subject="не указана";
                }
                if(messageCount.intValue()>1){
                    msg.setSubject(subject+"("+i+")");
                }else{
                    msg.setSubject(subject);
                }
                msg.setSender(sender);
                msg.setReceiver(ad.getAuthor());
                msg.setAd(ad);
                msg.setText(text.substring(firstSymbol, lastSymbol));
                if (validate(msg)) {
                    msgDao.save(msg);
                    if(i==1){
                        User receiver = ad.getAuthor();
                        //to do отправка сообщения если автор не на сайте только, а не всегда.
                        String url = "http://185.22.232.79/seller";
                        String mailText = sender.getName()+" оставил Вам сообщение. Тема: "+subject+", текст: "+text+", в ответ на объявление "+ad.getName()+" ("+ad.getDescription()+") на сайте "+url;
                        mailSender.sendMail(receiver.getEmail(), mailText);
                    }
                }
            }
        }
    }
    
    public List<Message>getDialog(Long senderId,Long recieverId){
        return msgDao.getDialog(senderId,recieverId);
    }

}
