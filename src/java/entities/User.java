/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import entities.parent.PrimEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author bezdatiuzer
 */
@Entity
@Table(name = "user")
public class User extends PrimEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    
    @Email
    @Column(name = "email")
    @NotNull(message = "Необходимо указать Email")
    //@Index(name="emailIndex")
    private String email;
    
    @Size(min = 4, message = "Пароль от 4 символов")
    @Column(name = "password")
    @NotNull(message = "Необходимо указать Пароль")
    private String password;
    
    /*@Column(name = "surname")
    @NotNull(message = "Необходимо указать Фамилию")
    private String surname;*/
    
    @Column(name = "name")
    @NotNull(message = "Необходимо указать Имя")
    private String name;
    
    @Column(name = "registration_date")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date registrationDate;
    
    
    @Column(name = "phone")
    private String phone;

    @Override
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    
    
}
