/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.parent.PrimEntity;
import java.util.HashSet;
import java.util.Objects;
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

/**
 *
 * @author bezdatiuzer
 */
@Entity
@Table(name = "region")
public class Region extends PrimEntity {
    
    public final static Integer HOMEREGION = 1;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "locals_at_region",
            joinColumns = @JoinColumn(name = "region_id", referencedColumnName = "region_id"),
            inverseJoinColumns = @JoinColumn(name = "locality_id", referencedColumnName = "locality_id"))
    private Set<Locality> localities;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "states_at_region",
            joinColumns = @JoinColumn(name = "region_id", referencedColumnName = "region_id"),
            inverseJoinColumns = @JoinColumn(name = "state_id", referencedColumnName = "state_id"))
    private Set<State> states;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "home_region")
    private Integer homeRegion;
    
    private boolean allRussia;
    
    @Override
    public Long getId() {
        return id;
    }

    public Set<Locality> getLocalities() {
        if(localities==null){
            return new HashSet();
        }
        return localities;
    }

    public void setLocalities(Set<Locality> localities) {
        this.localities = localities;
    }

    public Set<State> getStates() {
        if(states==null){
            return new HashSet();
        }
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean isHomeRegion() {
        return Objects.equals(HOMEREGION, homeRegion);
    }

    public void setHomeRegion(Boolean set) {
        if(true==set){
            this.homeRegion = HOMEREGION;
        }else{
            this.homeRegion = null;
        }
    }

    public boolean isAllRussia() {
        return allRussia;
    }

    public void setAllRussia(Boolean set) {
        if(true==set){
            this.allRussia = set;
        }else{
            this.allRussia = !set;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
