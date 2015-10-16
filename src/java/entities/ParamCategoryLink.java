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

/**
 *
 * @author bezdatiuzer
 */
@Entity
@Table(name = "param_category_link")
public class ParamCategoryLink extends PrimEntity {
    
    public final static Integer NOTREQUIRED = 0;
    public final static Integer REQUIRED = 1;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_c_link_id")
    private Long id;
    
    @JoinColumn(name = "parametr_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Необходимо указать параметр")
    private Parametr param;
    
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Необходимо указать категорию")
    private Category cat;
    
    @Column(name = "req_type")
    @NotNull(message = "Необходимо указать является ли параметр необходимым")
    private Integer reqType;
    
    @Override
    public Long getId() {
        return id;
    }

    public Parametr getParam() {
        return param;
    }

    public void setParam(Parametr param) {
        this.param = param;
    }

    public Category getCat() {
        return cat;
    }

    public void setCat(Category cat) {
        this.cat = cat;
    }

    public boolean isReq() {
        return reqType==REQUIRED;
    }

    public void setReq() {
        this.reqType = REQUIRED;
    }
    
    public void setNotReq() {
        this.reqType = NOTREQUIRED;
    }

    public Integer getReqType() {
        return reqType;
    }
    
    
    
}
