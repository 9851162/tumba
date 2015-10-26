/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.parent.PrimEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Index;

/**
 *
 * @author Алексей
 */
@Entity
@Table(name = "message")
public class Message extends PrimEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;
    
    @JoinColumn(name = "sender_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Отправитель не указан")
    @Index(name="senderIndex")
    private User sender;
    
    @JoinColumn(name = "receiver_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Получатель не указан")
    @Index(name="receiverIndex")
    private User receiver;
    
    @JoinColumn(name = "ad_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ad ad;
    
    @Column(name = "text", columnDefinition="TEXT")
    @NotNull(message = "Необходимо добавить текст")
    private String msg;
    
    @Override
    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    
    
}
