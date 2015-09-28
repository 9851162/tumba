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

    public void createUser(String phone, String email, String password, String name, String passconfirm, String role) {
        PhoneEditor phe = new PhoneEditor();
        phone = phe.getPhone(phone);

        if (email != null && !email.equals("") && password != null && password.length() > 3 && name != null && !name.equals("")/*&&phone!=null&&!phone.equals("")*/) {
            if (passconfirm.equals(password)) {
                if (role.equals(User.ROLEADMIN) || role.equals(User.ROLEUSER)) {
                    User existingUser = userDao.getUserByLogin(email);
                    if (existingUser == null) {
                        User u = new User();
                        u.setEmail(email);
                        u.setName(name);
                        u.setUserRole(role);
                        u.setPhone(phone);
                        u.setRegistrationDate(new Date());
                        u.setPassword(AuthManager.md5Custom(password));
                        if (validate(u)) {
                            userDao.save(u);
                        }

                    } else {
                        addError("Пользователь с такой почтой уже зарегистрирован");
                    }
                } else {
                    addError("Роль пользователя указана не верно");
                }
            } else {
                addError("Пароли не совпадают");
            }

        } else {
            if (email == null || email.equals("")) {
                addError("Необходимо указать электронный адрес");
            }
            if (password == null || password.length() < 4) {
                addError("Необходимо ввести пароль длиной более 3 символов");
            }
            if (name == null || name.equals("")) {
                addError("Необходимо указать Ваше имя");
            }
            /*if(phone==null||phone.equals("")){
             addError("Необходимо указать телефон");
             }*/
        }
    }

    public User registerAndNotifyUser(String email) {
        createUser("", email, "0000", "Новый пользователь", "0000",User.ROLEUSER);
        User user = null;
        if (getErrors().isEmpty()) {
            notifyAboutRegistration(email);
            user = getUserByMail(email);
        }
        return user;
    }

    public User getUserByMail(String email) {
        return userDao.getUserByLogin(email);
    }

    public void notifyAboutRegistration(String email) {
        //TO DO
    }

}
