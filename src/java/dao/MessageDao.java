/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Message;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Алексей
 */
@Repository
public class MessageDao extends Dao<Message> {
    
    @Override
    public Class getSupportedClass() {
        return Message.class;
    }
    
    /*public List<Message>getDialog(Long senderId,Long receiverId){
        String hql = "from Message where sender.id=:senderId and receiver.id=:receiverId order by insertDate";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("senderId", senderId);
        query.setParameter("receiverId", receiverId);
        return query.list();
    }*/
    
    public List<Message>getInbox(Long receiverId){
        String hql = "from Message where receiver.id=:receiverId order by insertDate desc";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("receiverId", receiverId);
        return query.list();
    }
    
    public Message getMsg(Long receiverId,Long msgId){
        String hql = "from Message where receiver.id=:receiverId and id=:msgId";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("receiverId", receiverId);
        query.setParameter("msgId", msgId);
        return (Message)query.uniqueResult();
    }
    
    public Integer getNewMsgCount(Long receiverId){
        String hql = "select count(id) from Message where receiver.id=:receiverId and newOne=1";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("receiverId", receiverId);
        //query.setParameter("new", Message.NEW);
        return ((Long)query.uniqueResult()).intValue();
    }
    
}
