/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.parent.PrimEntity;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Index;

/**
 *
 * @author bezdatiuzer
 */
@Entity
@Table(name = "parametr")
public class Parametr extends PrimEntity {
    
    public final static Integer SELECTING = 1;
    public final static Integer INSERTING = 2;
    
    public final static Integer NOTREQUIRED = 0;
    public final static Integer REQUIRED = 1;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parametr_id")
    private Long id;
    
    @ManyToMany(mappedBy = "params")
    private Set<Category> cats;
    
    //enum lu4we? SELECTING/INSERTING
    @Column(name = "combo_type")
    @NotNull(message = "Необходимо указать будет ли параметр вводиться или выбираться")
    private Integer comboType;
    
    @Column(name = "req_type")
    @NotNull(message = "Необходимо указать является ли параметр необходимым")
    private Integer reqType;
    
    @Override
    public Long getId() {
        return id;
    }

    public Integer getComboType() {
        return comboType;
    }

    public void setComboType(Integer comboType) {
        this.comboType = comboType;
    }

    public Integer getReqType() {
        return reqType;
    }

    public void setReqType(Integer reqType) {
        this.reqType = reqType;
    }
    
    

    
}
