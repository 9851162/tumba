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

/**
 *
 * @author bezdatiuzer
 */
@Entity
@Table(name = "parametr_value")
public class ParametrValue extends PrimEntity {
    
    public final static Long YES = (long)1;
    public final static Long NO = (long)0;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parametr_value_id")
    private Long id;
    
    @JoinColumn(name = "parametr_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Необходимо указать параметр")
    @Index(name="paramIndex")
    private Parametr parametr;
    
    @Column(name = "string_value")
    private String stringVal;
    
    @Column(name = "select_value")
    private Long selectVal;
    
    @Column(name = "number_value")
    private Long numVal;
    
    @Column(name = "date_value")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateVal;
    
    @JoinColumn(name = "ad_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Необходимо указать параметр")
    @Index(name="adIndex")
    private Ad ad;
    
    @Override
    public Long getId() {
        return id;
    }

    public Parametr getParametr() {
        return parametr;
    }

    public void setParametr(Parametr parametr) {
        this.parametr = parametr;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public String getStringVal() {
        return stringVal;
    }

    public void setStringVal(String stringVal) {
        this.stringVal = stringVal;
    }

    public Long getSelectVal() {
        return selectVal;
    }

    public void setSelectVal(Long selectVal) {
        this.selectVal = selectVal;
    }

    public Long getNumVal() {
        return numVal;
    }

    public void setNumVal(Long numVal) {
        this.numVal = numVal;
    }

    public Date getDateVal() {
        return dateVal;
    }

    public void setDateVal(Date dateVal) {
        this.dateVal = dateVal;
    }
    
    
    
}
