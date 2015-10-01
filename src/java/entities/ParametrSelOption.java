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
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author bezdatiuzer
 */
@Entity
@Table(name = "parametr_sel_option")
public class ParametrSelOption extends PrimEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parametr_sel_option_id")
    private Long id;
    
    @JoinColumn(name = "parametr_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Необходимо указать параметр")
    @Index(name="paramIndex")
    private Parametr parametr;
    
    @Column(name = "name")
    @NotNull(message = "Наименование не передано")
    @NotBlank(message = "Необходимо указать наименование")
    private String name;
    
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
