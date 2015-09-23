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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author bezdatiuzer
 */
@Entity
@Table(name = "category")
public class Category extends PrimEntity {
    
    public final static Long BASEID = (long)0;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    
    @Column(name = "name")
    @NotNull(message = "Необходимо указать Имя")
    private String name;
    
    @Column(name = "parent_id")
    @NotNull(message = "Ид родительской категории не указано")
    private Long parentId;
    
    //TO DO LAZY NEEDED
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "params_in_categories",
            joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "parametr_id", referencedColumnName = "parametr_id"))
    private Set<Parametr> params;
    
    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Set<Parametr> getParams() {
        return params;
    }

    public void setParams(Set<Parametr> params) {
        this.params = params;
    }
    
    
    
}
