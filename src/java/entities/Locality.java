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
@Table(name = "locality")
public class Locality extends PrimEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "locality_id")
    private Long id;
    
    @Column(name = "name", length = 100)
    @NotNull(message = "Необходимо указать название")
    private String name;
    
    @JoinColumn(name = "state_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Необходимо указать регион")
    @Index(name="stateIndex")
    private State state;
    
    @JoinColumn(name = "country_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Необходимо указать страну")
    @Index(name="countryIndex")
    private Country country;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ads_at_locals",
            joinColumns = @JoinColumn(name = "locality_id", referencedColumnName = "locality_id"),
            inverseJoinColumns = @JoinColumn(name = "ad_id", referencedColumnName = "ad_id"))
    private Set<Ad> ads;
    
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Ad> getAds() {
        return ads;
    }

    public void setAds(Set<Ad> ads) {
        this.ads = ads;
    }
    
    
    
}
