/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.parent.PrimEntity;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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

    public static Integer NEW = 0;
    public static Integer WAITING = 1;
    public static Integer PAID = 2;
    public static Integer DELIVERED = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Long id;

    @Column(name = "insert_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date insertDate;

    //3 dates of status changings poka
    //NEW->WAITING
    @Column(name = "sale_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date saleDate;

    //WAITING->PAID
    @Column(name = "pay_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date payDate;

    @Column(name = "delivery_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date deliveryDate;

    @Column(name = "price")
    @NotNull(message = "Необходимо указать цену числом")
    private Double price;

    @Column(name = "show_count")
    @NotNull(message = "Количество показов не может быть пустым")
    private Long showCount;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private String phone;

    @Column(name = "name")
    @NotNull(message = "Необходимо указать наименование")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    @NotNull(message = "Необходимо добавить описание")
    private String description;

    @Column(name = "status")
    @NotNull(message = "Статус не установлен")
    private Integer status;

    @JoinColumn(name = "author_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Автор не указан")
    @Index(name = "authorIndex")
    private User author;

    @JoinColumn(name = "buyer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User buyer;

    //TO DO LAzy. pridumat' kak sdelat. esli prosto lazy, to posle 1 vizova getlocs, locs obnulyautsya
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ads_at_locals",
            joinColumns = @JoinColumn(name = "ad_id", referencedColumnName = "ad_id"),
            inverseJoinColumns = @JoinColumn(name = "locality_id", referencedColumnName = "locality_id"))
    private Set<Locality> localities;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Необходимо указать категорию")
    @Index(name = "catIndex")
    private Category cat;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "ad")
    private Set<ParametrValue> values;

    @ElementCollection
    @CollectionTable(name = "ads_from_ips",
            joinColumns = @JoinColumn(name = "ad_id", referencedColumnName = "ad_id"))
    @Column(name = "ip")
    @Cascade(CascadeType.ALL)
    private Set<String> ips;

    @Column(name = "date_from")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @NotNull(message = "Необходимо указать даты размещения объявления")
    private Date dateFrom;

    @Column(name = "date_to")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @NotNull(message = "Необходимо указать даты размещения объявления")
    private Date dateTo;

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
        this.showCount = showCount;
    }

    public String getDescription() {
        return description;
    }

    public String getSmallDesc() {
        if (description.length() > 19) {
            return description.substring(0, 17) + "...";
        }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Set<Locality> getLocalities() {
        return localities;
    }

    public void setLocalities(Set<Locality> localities) {
        this.localities = localities;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Set<String> getIps() {
        if (ips == null) {
            return new HashSet();
        }
        return ips;
    }

    public void setIps(Set<String> ips) {
        this.ips = ips;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public List<String> getPreviewpaths() {
        List<String> res = new ArrayList();
        int i = 0;
        while (i < 10) {
            File f = new File("/usr/local/seller/preview/" + this.id + "/" + i);
            if (f.exists()) {
                res.add("../imgs/" + this.id + "/" + (i++));
            } else {
                if (i == 0) {
                    res.add("../img/no-photo.png");
                }
                break;
            }
        }
        return res;
    }

    public String getFirstPreviewPath() {
        File f = new File("/usr/local/seller/preview/" + this.id + "/" + 0);
        if (f.exists()) {
            return "../imgs/" + this.id + "/0";
        }
        return "../img/no-photo.png";
    }

}
