/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.AdDao;
import dao.CategoryDao;
import dao.ParametrValueDao;
import entities.Ad;
import entities.ParametrValue;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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

    public void create(Double price,MultipartFile previews[],String name, String desc, Long categoryId) throws IOException {
        if (categoryId != null) {
            Ad ad = new Ad();
            ad.setInsertDate(new Date());
            ad.setShowCount((long) 0);

            //to do not null
            ad.setAuthor(null);
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
                file=null;
                int i=0;
                //String types=""+previews.length+";types:";
                for(MultipartFile prev:previews){
                    prev.transferTo(new File("/usr/local/seller/preview/"+ad.getId()+"/"+(i++)));
                }
                //addError(types);
                
                /*InputStream fis = preview.getInputStream();
                
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream("/usr/local/seller/preview/"+ad.getId()+"/"+preview.getName()));*/
                
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
