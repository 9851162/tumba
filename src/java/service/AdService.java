/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.AdDao;
import dao.CategoryDao;
import dao.ParametrDao;
import dao.ParametrValueDao;
import dao.UserDao;
import entities.Ad;
import entities.Category;
import entities.Parametr;
import entities.ParametrValue;
import entities.User;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @Autowired
    ParametrDao paramDao;

    @Autowired
    ParametrValueDao paramValueDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    public void create(Long catId, String email, Double price, MultipartFile previews[], String name, String desc,
            Long booleanIds[], String booleanVals[], Long stringIds[], String stringVals[], Long numIds[], Long numVals[],
            Long dateIds[], Date dateVals[], Long selIds[], Long selVals[], Long multyIds[], String multyVals[]) throws IOException {
        Boolean newUser = false;
        if (catId != null) {
            Category cat = catDao.find(catId);
            if (cat != null) {
                if (email != null && !email.equals("")) {

                    List<Long> reqParamIds = catDao.getRequiredParamsIds(catId);

                    User user = userService.getUserByMail(email);
                    if (user == null) {
                        user = userService.registerStandardUser(email);
                        newUser = true;
                        List<String> userErrors = userService.getErrors();
                        if (!userErrors.isEmpty()) {
                            for (String er : userErrors) {
                                addError("user_service: " + er + "; ");
                            }
                        }
                    }
                    Ad ad = new Ad();
                    ad.setInsertDate(new Date());
                    ad.setShowCount((long) 0);

                    ad.setAuthor(user);

                    ad.setCat(cat);

                    ad.setName(name);
                    ad.setDescription(desc);
                    ad.setPrice(price);
                    ad.setValues(new HashSet());
                    if (validate(ad)) {
                        adDao.save(ad);

                        Set<Parametr> catParams = cat.getParams();
                        int i = 0;
                        ArrayList<String> paramValsErrs = new ArrayList();
                        //обходим все массивы и создаем сет значений для сохранения, параллельно валидируя, если есть ошибки валидации
                        //удаляем ad

                        ArrayList<ParametrValue> list4Save = new ArrayList();

                        //не трогаем в плане рек не рек
                        if (booleanVals != null && booleanVals.length > 0) {
                            while (i < booleanIds.length) {
                                Parametr p = paramDao.find(booleanIds[i]);
                                if (catParams.contains(p) && Parametr.BOOL == p.getParamType()) {
                                    Long val = ParametrValue.NO;
                                    if (booleanVals[i] != null) {
                                        val = ParametrValue.YES;
                                    }
                                    ParametrValue pv = new ParametrValue();
                                    pv.setAd(ad);
                                    pv.setParametr(p);
                                    pv.setSelectVal(val);
                                    if (validate(pv)) {
                                        list4Save.add(pv);
                                    }
                                    i++;

                                }
                            }
                        }

                        if (stringVals != null && stringVals.length > 0) {
                            i = 0;
                            while (i < stringIds.length) {
                                Long paramId = stringIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.TEXT == p.getParamType()) {
                                    String val = stringVals[i];
                                    if (reqParamIds.contains(paramId) && val != null) {
                                        reqParamIds.remove(paramId);
                                    }

                                    ParametrValue pv = new ParametrValue();
                                    pv.setAd(ad);
                                    pv.setParametr(p);
                                    pv.setStringVal(val);
                                    if (validate(pv)) {
                                        list4Save.add(pv);
                                    }
                                    i++;
                                }
                            }
                        }

                        if (numVals != null && numVals.length > 0) {
                            i = 0;
                            while (i < numIds.length) {
                                Long paramId = numIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.NUM == p.getParamType()) {
                                    Long val = numVals[i];
                                    if (reqParamIds.contains(paramId) && val != null) {
                                        reqParamIds.remove(paramId);
                                    }
                                    ParametrValue pv = new ParametrValue();
                                    pv.setAd(ad);
                                    pv.setParametr(p);
                                    pv.setNumVal(val);
                                    if (validate(pv)) {
                                        list4Save.add(pv);
                                    }
                                    i++;
                                }
                            }
                        }

                        if (dateVals != null && dateVals.length > 0) {
                            i = 0;
                            while (i < dateIds.length) {
                                Long paramId = dateIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.DATE == p.getParamType()) {
                                    Date val = dateVals[i];
                                    if (reqParamIds.contains(paramId) && val != null) {
                                        reqParamIds.remove(paramId);
                                    }
                                    ParametrValue pv = new ParametrValue();
                                    pv.setAd(ad);
                                    pv.setParametr(p);
                                    pv.setDateVal(val);
                                    if (validate(pv)) {
                                        list4Save.add(pv);
                                    }
                                    i++;
                                }
                            }
                        }

                        if (selVals != null && selVals.length > 0) {
                            i = 0;

                            while (i < selIds.length) {
                                Long paramId = selIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.SELECTING == p.getParamType()) {
                                    Long val = selVals[i];
                                    if (reqParamIds.contains(paramId) && val != null) {
                                        reqParamIds.remove(paramId);
                                    }
                                    ParametrValue pv = new ParametrValue();
                                    pv.setAd(ad);
                                    pv.setParametr(p);
                                    pv.setSelectVal(val);
                                    if (validate(pv)) {
                                        list4Save.add(pv);
                                    }
                                    i++;
                                }
                            }
                        }

                        //вытягивание значений мультиселекта
                        //TO DO более тщательную валидацию и обработку ошибок мб(??)
                        if (multyVals != null && multyVals.length > 0) {
                            for (String rawVal : multyVals) {
                                String idValArr[] = rawVal.split("_");
                                if (idValArr.length == 2) {
                                    String strId = idValArr[0];
                                    String strVal = idValArr[1];
                                    Long paramId = Long.valueOf(strId);
                                    Long val = Long.valueOf(strVal);
                                    Parametr p = paramDao.find(paramId);
                                    if (catParams.contains(p) && Parametr.MULTISELECTING == p.getParamType()) {
                                        if (reqParamIds.contains(paramId) && val != null) {
                                            reqParamIds.remove(paramId);
                                        }
                                        ParametrValue pv = new ParametrValue();
                                        pv.setAd(ad);
                                        pv.setParametr(p);
                                        pv.setSelectVal(val);
                                        if (validate(pv)) {
                                            list4Save.add(pv);
                                        }
                                    }
                                }
                            }
                        }

                        //проверяем наконец есть ли ошибки и все ли требуемые параметры занесли
                        if (!reqParamIds.isEmpty()) {
                            for (Long id : reqParamIds) {
                                addError("необходимо указать значение параметра " + paramDao.find(id).getName() + "; ");
                            }
                            //?
                            adDao.delete(ad);
                        } else {

                            for (ParametrValue pv : list4Save) {
                                paramValueDao.save(pv);
                            }

                            File file = new File("/usr/local/seller/preview/" + ad.getId() + "/");
                            file.mkdirs();
                            if (previews != null && previews.length > 0) {
                                i = 0;
                                while (i < 7 && i < previews.length) {
                                    MultipartFile prev = previews[i];
                                    if (prev.getSize() <= (long) 1024 * 1024) {
                                        prev.transferTo(new File("/usr/local/seller/preview/" + ad.getId() + "/" + i));
                                    } else {
                                        addError("Изображение " + prev.getName() + " не было добавлено, так как его размер больше ограничения в 1мб.");

                                    }
                                    i++;
                                }
                            }

                            if (newUser) {
                                userService.notifyAboutRegistration(email);
                            }
                        }

                    }
                }
            } else {
                addError("Категория с ид " + catId + " не была найдена.");
            }
        } else {
            addError("Необходимо указать категорию");
        }
    }

    public List<Ad> getAds(String wish) {
        //if(wishes==null||wishes.equals("")){
        return adDao.getAll();
        /*}else{
         return adDao.getAds
         }*/
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

    public void setUnsetChosen(Long userId, Long adId) {
        if (userId != null && adId != null) {
            User u = userDao.find(userId);
            //Ad ad = adDao.find(adId);
            //есть - удалить, нет - добавить

            if (adDao.isChosenAd(userId, adId)) {
                adDao.unsetChosen(userId, adId);
            } else {
                adDao.setChosen(userId, adId);
            }
        } else {
            if (userId == null) {
                addError("Пользователь не передан");
            }
            if (adId == null) {
                addError("Объявление не передано");
            }
        }
    }

    public List<Ad> getChosenAds(Long userId) {
        if (userId != null) {
            return adDao.getChosenAds(userId);
        } else {
            addError("Пользователь не указан");
            return new ArrayList();
        }
    }

}
