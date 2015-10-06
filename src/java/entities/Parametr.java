/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.parent.PrimEntity;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author bezdatiuzer
 */
@Entity
@Table(name = "parametr")
public class Parametr extends PrimEntity {
    
    
    
    public final static Integer TEXT = 1;
    public final static Integer NUM = 2;
    public final static Integer SELECTING = 3;
    public final static Integer MULTISELECTING = 4;
    public final static Integer BOOL = 5;
    public final static Integer DATE = 6;
    
    public final static Integer NOTREQUIRED = 0;
    public final static Integer REQUIRED = 1;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parametr_id")
    private Long id;
    
    @Column(name = "name")
    @NotNull(message = "Наиенование не передано")
    @NotBlank(message = "Необходимо указать наименование")
    private String name;
    
    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "parametr", orphanRemoval = true)
    @OrderBy("name")
    private List<ParametrSelOption>options;
    
    /*@ManyToMany(mappedBy = "params", fetch = FetchType.LAZY)
    private Set<Category> cats;*/
    
    //enum lu4we? SELECTING/INSERTING
    @Column(name = "param_type")
    @NotNull(message = "Необходимо указать будет ли параметр вводиться или выбираться")
    private Integer paramType;
    
    @Column(name = "req_type")
    @NotNull(message = "Необходимо указать является ли параметр необходимым")
    private Integer reqType;
    
    @Override
    public Long getId() {
        return id;
    }

    public Integer getReqType() {
        return reqType;
    }

    public void setReqType(Integer reqType) {
        this.reqType = reqType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParamType() {
        return paramType;
    }

    public void setParamType(Integer paramType) {
        this.paramType = paramType;
    }
    
    
    
    //to do описание и ед.изм. мб

    /*public Set<Category> getCats() {
        return cats;
    }

    public void setCats(Set<Category> cats) {
        this.cats = cats;
    }*/

    public List<ParametrSelOption> getOptions() {
        return options;
    }

    public void setOptions(List<ParametrSelOption> options) {
        this.options = options;
    }
    
    public Boolean isReq(){
        return REQUIRED==this.reqType;
    }

    
}
