/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.parent.PrimEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Length;

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
    
    @Column(name = "insert_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date insertDate;
    
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
    @Length(max = 1001,message="Максимальная длина сообщения - 1000 символов")
    private String text;
    
    @Column(name = "subject")
    @Length(max = 256,message="Максимальная длина темы - 255 символов")
    private String subject;
    
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public String getSubject() {
        return subject;
    }
    
    public String getShortSubject(){
        if(subject==null){
            return "без темы";
        }else{
            if(subject.length()>72){
                return subject.substring(0, 70)+"...";
            }else{
                return subject;
            }
        }
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    
    
}
