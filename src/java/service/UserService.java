/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.UserDao;
import entities.User;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.parent.PrimService;
import support.AuthManager;
import support.editors.PhoneEditor;

/**
 *
 * @author bezdatiuzer
 */
@Service
@Transactional
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService extends PrimService {
    
    
    @Autowired
    private UserDao userDao;
    
    public void createUser(String phone,String email,String password,String name,String passconfirm){
                    PhoneEditor phe = new PhoneEditor();
                    phone = phe.getPhone(phone);
                    
            if(email!=null&&!email.equals("")&&password!=null&&password.length()>4&&name!=null&&!name.equals("")/*&&phone!=null&&!phone.equals("")*/){
                addError("jobdone0");
                if(passconfirm.equals(password)){
                    addError("jobdone1");
                    User existingUser = userDao.getUserByLogin(email);
                    if(existingUser==null){
                        addError("jobdone2");
                        User u = new User();
                        u.setEmail(email);
                        u.setName(name);
                        u.setPhone(phone);
                        u.setRegistrationDate(new Date());
                        u.setPassword(AuthManager.md5Custom(password));
                        addError("jobdone3");
                        if(validate(u)){
                            userDao.save(u);
                        }
                        addError("jobdone4");
                    }else{
                        addError("Пользователь с такой почтой уже зарегистрирован");
                    }
                }else{
                    addError("Пароли не совпадают");
                }
                    
            }else{
                if(email==null||email.equals("")){
                    addError("Необходимо указать электронный адрес");
                }
                if(password==null||password.length()<4){
                    addError("Необходимо ввести пароль длиной более 4 символов");
                }
                if(name==null||name.equals("")){
                    addError("Необходимо указать Ваше имя");
                }
                /*if(phone==null||phone.equals("")){
                    addError("Необходимо указать телефон");
                }*/
            }
    }
    
}
