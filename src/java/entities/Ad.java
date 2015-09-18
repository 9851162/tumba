/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.parent.PrimEntity;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author bezdatiuzer
 */
@Entity
@Table(name = "ad")
public class Ad extends PrimEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Long id;
    
    @Column(name = "insert_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date insertDate;
    
    @Column(name = "price")
    @NotNull(message = "Необходимо добавить цену")
    private Double price;
    
    @Column(name = "show_count")
    @NotNull(message = "Количество показов не может быть пустым")
    private Long showCount;
    
    @Column(name = "description", columnDefinition="TEXT")
    @NotNull(message = "Необходимо добавить описание")
    private String description;
    
    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    //@NotNull(message = "Автор не указан")
    //@Index(name="authorIndex")
    private User author;
    
    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Необходимо указать категорию")
    @Index(name="catIndex")
    private Category cat;
    
    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "ad")
    private Set<ParametrValue> values;
    
    @Override
    public Long getId() {
        return id;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getShowCount() {
        return showCount;
    }

    public void setShowCount(Long showCount) {
        /*if(showCount==null){
            showCount=(long)0;
        }*/
        this.showCount = showCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Category getCat() {
        return cat;
    }

    public void setCat(Category cat) {
        this.cat = cat;
    }

    public Set<ParametrValue> getValues() {
        return values;
    }

    public void setValues(Set<ParametrValue> values) {
        this.values = values;
    }
    
    
    
}
