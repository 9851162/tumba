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
import entities.Category;
import entities.ParametrValue;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public void createAd(Double price, String desc, Long categoryId) {
        if (categoryId != null) {
            Category cat = catDao.find(categoryId);
            Ad ad = new Ad();
            ad.setInsertDate(new Date());
            ad.setShowCount((long) 0);

            //to do not null
            ad.setAuthor(null);

            ad.setCat(cat);
            ad.setDescription(desc);
            ad.setPrice(price);
            ad.setValues(new HashSet());
            if (validate(ad)) {
                adDao.save(ad);
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
