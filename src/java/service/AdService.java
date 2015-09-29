/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.AdDao;
import dao.CategoryDao;
import dao.ParametrValueDao;
import dao.UserDao;
import entities.Ad;
import entities.ParametrValue;
import entities.User;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import service.parent.PrimService;

/**
 *
 * @author bezdatiuzer
 */
@Service
@Transactional
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AdService extends PrimService {

    @Autowired
    AdDao adDao;

    @Autowired
    CategoryDao catDao;

    @Autowired
    ParametrValueDao valDao;
    
    /*@Autowired
    UserDao userDao;*/
    
    @Autowired
    UserService userService;

    public void create(String email,Double price,MultipartFile previews[],String name, String desc, Long categoryId) throws IOException {
        if (categoryId != null) {
            if(email!=null&&!email.equals("")){
                User user = userService.getUserByMail(email);
                if(user==null){
                    user = userService.registerStandardUser(email);
                    List<String> userErrors = userService.getErrors();
                    if(!userErrors.isEmpty()){
                        for(String er:userErrors){
                            addError("user_service: "+er+"; ");
                        }
                    }
                }
                Ad ad = new Ad();
                ad.setInsertDate(new Date());
                ad.setShowCount((long) 0);

                ad.setAuthor(user);
                
                //Category cat = catDao.find(categoryId);
                ad.setCat(null);
                

                ad.setName(name);
                ad.setDescription(desc);
                ad.setPrice(price);
                ad.setValues(new HashSet());
                if (validate(ad)) {
                    adDao.save(ad);
                    File file = new File("/usr/local/seller/preview/"+ad.getId()+"/");
                    file.mkdirs();
                    if(previews!=null&&previews.length>0){
                        int i=0;
                        while(i<7&&i<previews.length){
                            MultipartFile prev = previews[i];
                            if(prev.getSize()<=(long)1024*1024){
                                prev.transferTo(new File("/usr/local/seller/preview/"+ad.getId()+"/"+i));
                            }else{
                                addError("Изображение "+prev.getName()+" не было добавлено, так как его размер больше ограничения в 1мб.");

                            }
                            i++;
                        }
                    }
                    userService.notifyAboutRegistration(email);
                    /*for(MultipartFile prev:previews){
                        prev.transferTo(new File("/usr/local/seller/preview/"+ad.getId()+"/"+(i++)));
                    }*/
                    //addError(types);

                    /*InputStream fis = preview.getInputStream();

                    BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream("/usr/local/seller/preview/"+ad.getId()+"/"+preview.getName()));*/

                }
            }
        } else {
            addError("Необходимо указать категорию");
        }
    }

    public List<Ad> getAds() {
        return adDao.getAll();
    }

    public void delete(Long adId) {
        if (adId != null) {
            Ad ad = adDao.find(adId);
            if (ad != null) {
                for (ParametrValue val : ad.getValues()) {
                    valDao.delete(val);
                }
                adDao.delete(ad);
            } else {
                addError("Объявление не найдено по указанному ИД: " + adId + "; ");
            }
        } else {
            addError("Ид объявления не передан");
        }
    }

}
