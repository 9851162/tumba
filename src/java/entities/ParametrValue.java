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
 * @author bezdatiuzer
 */
@Entity
@Table(name = "parametr_value")
public class ParametrValue extends PrimEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parametr_value_id")
    private Long id;
    
    @JoinColumn(name = "parametr_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Необходимо указать параметр")
    @Index(name="paramIndex")
    private Parametr parametr;
    
    @Column(name = "value")
    private String value;
    
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }
    
    
    
}
