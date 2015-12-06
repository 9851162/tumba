/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.UserDao;
import entities.User;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import service.parent.PrimService;
import support.AuthManager;
import support.Constants;
import support.Random;
import support.SupMailSender;
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
    
    @Autowired
    private SupMailSender mailSender;

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
                            u.setActive(User.OFF);
                            u.setHash(AuthManager.md5Custom(Random.getString("qwertyuiopasdfghjklzxcvbnm", 10)));
                            if (validate(u)) {
                                String text = "Для активации Вашего аккаунта на сайте "+Constants.projectUrl+", пройдите по ссылке: "+Constants.projectUrl+"/User/activation?email="+u.getEmail()+"&hash="+u.getHash();
                                userDao.save(u);
                                mailSender.sendMail(email, text);
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

    public User registerStandardUser(String email) {
        String standardPass = "0000";
        createUser("", email, standardPass, "Новый пользователь", standardPass, User.ROLEUSER);
        User user = null;
        if (getErrors().isEmpty()) {
            //notifyAboutRegistration(email);
            user = getUserByMail(email);
        }
        return user;
    }

    public User getUserByMail(String email) {
        return userDao.getUserByLogin(email);
    }

    public void notifyAboutRegistration(String email) {
        String text = "Здравствуйте! На нашем сайте "+Constants.projectUrl+", было подано объявление с указанием этого email."+
                " Для Вашего удобства, нами была создана учетная запись для просмотра и управления Вашими объявлениями."+
                "В качестве логина был использован Ваш email: "+email+", пароль: 0000 "+
                " Пароль Вы можете изменить в любой момент зайдя на сайт и авторизировавшись в Вашем личном кабинете."+
                "Приятной Вам работы";
                //или пройдя по ссылке
                
        mailSender.sendMail(email, text);
        
        //TO DO
    }
    
    public List<User>getUsers(String keyWord){
        if(keyWord==null||keyWord.equals("")){
            return userDao.getAll();
        }else{
            return userDao.getUsersByNameOrMail(keyWord);
        }
    }
    
    public void delete(Long userId){
        if(userId!=null){
            User u = userDao.find(userId);
            if(u!=null){
                //for(Ad ad:u.)
                userDao.delete(u);
            }else{
                addError("Пользователь с ид "+userId+"не найден");
            }
        }
    }
    
    public void setRole(Long userId,String role){
        if(userId!=null){
            User u = userDao.find(userId);
            if(u!=null){
                if(User.ROLEADMIN.equals(role)){
                    u.setUserRole(role);
                } else if(User.ROLEUSER.equals(role)){
                    u.setUserRole(role);
                }else{
                    addError("Не удалось разобрать параметр роль");
                }
                userDao.save(u);
            }else{
                addError("Пользователь с ид "+userId+"не найден");
            }
        }
    }
    
    public void changeUserParam(String param,String value,Long userId){
        if(userId!=null){
            User u = userDao.find(userId);
            if(u!=null){
                if("name".equals(param)){
                    u.setName(value);
                } else if("email".equals(param)){
                    u.setEmail(value);
                }else if("phone".equals(param)){
                    PhoneEditor phe = new PhoneEditor();
                    value = phe.getPhone(value);
                    u.setPhone(value);
                }
                if(validate(u)){
                    userDao.update(u);
                }
            }else{
                addError("Пользователь с ид "+userId+"не найден");
            }
        }
    }

    public void changeUserPass(String newPass,User u){
        u.setPassword(newPass);
        if(validate(u)){
            userDao.update(u);
        }
    }
    
    public static String getAvatarPath(Long userId){
        String path = "../img/no-image.png";
        File f = new File("/usr/local/seller/preview/users/"+userId+"/avatar");
        if(f.exists()){
            path = "../imgs/users/"+userId+"/avatar";
        }
        return path;
    }
    
    public void uploadAvatar(User u,MultipartFile avatar) throws IOException{
        if(u!=null&&avatar!=null){
            if (avatar.getSize() <= (long) 3 * 1024 * 1024) {
                File f = new File("/usr/local/seller/preview/users/" + u.getId() + "/avatar");
                if(!f.exists()){
                    f.mkdirs();
                }else{
                    f.delete();
                }
                avatar.transferTo(f);
            }else{
                addError("фото должно быть размером до 3мб");
            }
        }
    }
    
    public void activate(User u){
        u.setActive(User.ON);
        u.setHash(null);
        if(validate(u)){
            userDao.update(u);
        }
    }
    
}
