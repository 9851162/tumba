/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.AdDao;
import dao.CategoryDao;
import dao.LocalityDao;
import dao.ParametrDao;
import dao.ParametrSelOptionDao;
import dao.ParametrValueDao;
import dao.UserDao;
import entities.Ad;
import entities.Category;
import entities.Locality;
import entities.Parametr;
import entities.ParametrValue;
import entities.Region;
import entities.User;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import service.parent.PrimService;
import support.BilateralCondition;
import support.DateAdapter;
import support.StringAdapter;
import support.editors.PhoneEditor;

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
    ParametrSelOptionDao paramSelDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    LocalityDao locDao;

    public void create(Boolean isAutherized, Long catId, String email, String phone, String price, MultipartFile previews[], String name, String desc,
            Long booleanIds[], String booleanVals[], Long stringIds[], String stringVals[], Long numIds[], String snumVals[],
            Long dateIds[], Date dateVals[], Long selIds[], Long selVals[], Long multyIds[], String multyVals[],
            Date dateFrom, Date dateTo, Long localIds[]) throws IOException {
        Boolean newUser = false;
        if (catId != null) {
            Category cat = catDao.find(catId);
            if (cat != null) {
                if (isAutherized || (!isAutherized && email != null && !email.equals(""))) {

                    PhoneEditor phe = new PhoneEditor();
                    phone = phe.getPhone(phone);
                    addError(phe.error);

                    if ((phone == null || phone.equals("")) && (email == null || email.equals(""))) {
                        addError("необходимо указать email или телефон в качестве контактов");
                    }

                    User user = userService.getUserByMail(email);
                    if (!isAutherized && user == null) {
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
                    ad.setStatus(Ad.NEW);
                    ad.setDateFrom(dateFrom);
                    ad.setDateTo(dateTo);
                    ad.setEmail(email);
                    ad.setPhone(phone);

                    ad.setAuthor(user);

                    ad.setCat(cat);

                    Set<Locality> locals = new HashSet();
                    /*if (region != null) {
                     if (region.isAllRussia()) {
                     locals.addAll(locDao.getAll());
                     } else {
                     locals.addAll(region.getLocalities());
                     }
                     }*/
                    if (localIds != null && localIds.length > 0) {
                        locals.addAll(locDao.getLocs(localIds));
                    } else {
                        addError("необходимо выбрать хотя бы один город");
                    }
                    ad.setLocalities(locals);
                    ad.setName(name);
                    ad.setDescription(desc);
                    ad.setPrice(getNumFromString(price, true));
                    ad.setValues(new HashSet());
                    if (validate(ad) && getErrors().isEmpty()) {
                        adDao.save(ad);

                        List<Long> reqParamIds = catDao.getRequiredParamsIds(catId);
                        List<Parametr> catParams = paramDao.getParamsFromCat(catId);
                        int i = 0;
                        ArrayList<String> paramValsErrs = new ArrayList();
                        //обходим все массивы и создаем сет значений для сохранения, параллельно валидируя, если есть ошибки валидации
                        //удаляем ad

                        ArrayList<ParametrValue> list4Save = new ArrayList();

                        //не трогаем в плане рек не рек
                        if (booleanIds != null) {
                            if (booleanVals == null) {
                                booleanVals = new String[booleanIds.length];
                            }
                            while (i < booleanIds.length) {
                                Parametr p = paramDao.find(booleanIds[i]);
                                if (catParams.contains(p) && Parametr.BOOL == p.getParamType()) {
                                    Long val = ParametrValue.NO;
                                    String sval = "нет";
                                    if (booleanVals[i] != null) {
                                        val = ParametrValue.YES;
                                        sval = "да";
                                    }
                                    ParametrValue pv = new ParametrValue();
                                    pv.setAd(ad);
                                    pv.setParametr(p);
                                    pv.setSelectVal(val);
                                    pv.setStringVal(sval);
                                    if (validate(pv)) {
                                        list4Save.add(pv);
                                    }

                                }
                                i++;
                            }
                        }

                        if (stringVals != null && stringVals.length > 0) {
                            i = 0;
                            while (i < stringIds.length) {
                                Long paramId = stringIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.TEXT == p.getParamType()) {
                                    String val = stringVals[i];
                                    if (val != null && !val.equals("")) {
                                        if (reqParamIds.contains(paramId)) {
                                            reqParamIds.remove(paramId);
                                        }

                                        ParametrValue pv = new ParametrValue();
                                        pv.setAd(ad);
                                        pv.setParametr(p);
                                        pv.setStringVal(val);
                                        if (validate(pv)) {
                                            list4Save.add(pv);
                                        }

                                    }
                                }
                                i++;
                            }
                        }

                        if (snumVals != null && snumVals.length > 0) {
                            i = 0;
                            while (i < numIds.length) {
                                Long paramId = numIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.NUM == p.getParamType()) {
                                    String sval = snumVals[i];
                                    if (sval != null && !sval.equals("")) {
                                        Double val = getNumFromString(sval, true);
                                        if (reqParamIds.contains(paramId)) {
                                            reqParamIds.remove(paramId);
                                        }
                                        ParametrValue pv = new ParametrValue();
                                        pv.setAd(ad);
                                        pv.setParametr(p);
                                        pv.setNumVal(val);
                                        pv.setStringVal(StringAdapter.getString(val));
                                        if (validate(pv)) {
                                            list4Save.add(pv);
                                        }

                                    }
                                }
                                i++;
                            }
                            if (!getErrors().isEmpty()) {
                                for (String e : getErrors()) {
                                    paramValsErrs.add(e);
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
                                    if (val != null) {
                                        if (reqParamIds.contains(paramId)) {
                                            reqParamIds.remove(paramId);
                                        }
                                        ParametrValue pv = new ParametrValue();
                                        pv.setAd(ad);
                                        pv.setParametr(p);
                                        pv.setDateVal(val);
                                        pv.setStringVal(DateAdapter.formatByDate(val, DateAdapter.SMALL_FORMAT));
                                        if (validate(pv)) {
                                            list4Save.add(pv);
                                        }

                                    }
                                }
                                i++;
                            }
                        }

                        if (selVals != null && selVals.length > 0) {
                            i = 0;

                            while (i < selIds.length) {
                                Long paramId = selIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.SELECTING == p.getParamType()) {
                                    Long val = selVals[i];
                                    if (val != null && !val.equals(0L)) {
                                        if (reqParamIds.contains(paramId)) {
                                            reqParamIds.remove(paramId);
                                        }
                                        ParametrValue pv = new ParametrValue();
                                        pv.setAd(ad);
                                        pv.setParametr(p);
                                        pv.setSelectVal(val);
                                        pv.setStringVal(paramSelDao.find(val).getName());
                                        if (validate(pv)) {
                                            list4Save.add(pv);
                                        }

                                    }
                                }
                                i++;
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
                                        pv.setStringVal(paramSelDao.find(val).getName());
                                        if (validate(pv)) {
                                            list4Save.add(pv);
                                        }
                                    }
                                }
                            }
                        }

                        //проверяем наконец есть ли ошибки и все ли требуемые параметры занесли
                        if (!reqParamIds.isEmpty() || !paramValsErrs.isEmpty()) {
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
                            if (file.exists()) {
                                for (File f : file.listFiles()) {
                                    f.delete();
                                }
                                file.delete();
                            }
                            file.mkdirs();
                            if (previews != null && previews.length > 0) {
                                i = 0;
                                while (i < 10 && i < previews.length) {
                                    MultipartFile prev = previews[i];
                                    if (prev != null && 0L < prev.getSize()) {
                                        if (prev.getSize() <= (long) 3 * 1024 * 1024) {
                                            File f = new File("/usr/local/seller/preview/" + ad.getId() + "/supPreview");
                                            if (f.exists()) {
                                                f.delete();
                                            }
                                            prev.transferTo(f);
                                            //to do мб побыстрее как-то сделать?
                                            try {
                                                BufferedImage bi = ImageIO.read(f);
                                                BigDecimal x = BigDecimal.valueOf(0);
                                                BigDecimal y = BigDecimal.valueOf(0);
                                                BigDecimal h = BigDecimal.valueOf(bi.getHeight());
                                                BigDecimal w = BigDecimal.valueOf(bi.getWidth());
                                                if (h.compareTo(w) > 0) {
                                                    y = (h.subtract(w)).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
                                                    h = w;
                                                } else if (h.compareTo(w) < 0) {
                                                    x = (w.subtract(h)).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
                                                    w = h;
                                                }
                                                bi = bi.getSubimage(x.intValue(), y.intValue(), w.intValue(), h.intValue());
                                                f.delete();
                                                f = new File("/usr/local/seller/preview/" + ad.getId() + "/" + i);
                                                ImageIO.write(bi, "png", f);
                                            } catch (Exception e) {
                                                addError("Не удалось прочитать изображение " + prev.getName() +/*"; s="+prev.getSize()+"; t="+prev.getContentType()+"; l="+previews.length+*/ "; " + StringAdapter.getStackTraceException(e));
                                            }
                                        } else {
                                            addError("Изображение " + prev.getName() + " не было добавлено, так как его размер больше ограничения в 3 мб.");
                                        }
                                    }
                                    i++;
                                }
                            }

                            if (newUser) {
                                userService.notifyAboutRegistration(email);
                            }
                        }

                    }/* else {
                     addError("user:" + user.getId() + " " + user.getName());
                     }*/

                } else {
                    addError("для подачи объявления без авторизации необходимо указать email");
                }
            } else {
                addError("Категория с ид " + catId + " не была найдена.");
            }
        } else {
            addError("Необходимо указать категорию");
        }
    }
    
    public List<Ad> getAds(String wish, List<Long> catIds, Region region, String order,
            List<Long> booleanIds, List<Long> booleanVals, Long stringIds[], String stringVals[],
            Long numIds[], String snumValsFrom[], String snumValsTo[], Long dateIds[], String dateValsFrom[], String dateValsTo[],
            List<Long> selIds, List<Long> selVals, Long multyIds[], String multyVals[], String stringPriceFrom, String stringPriceTo) {
        if (order != null) {
            if (order.equals("insert_date")) {
                order += " desc";
            } else if (order.equals("price")) {
                order += " asc";
            } else if (order.equals("show_count")) {
                order += " desc";
            } else {
                order = null;
            }
        }

        List<Long> stringIdsList = new ArrayList();
        List<String> stringValsList = new ArrayList();
        if (stringVals != null && stringVals.length > 0) {
            int i = 0;
            for (String s : stringVals) {
                if (s != null && !s.equals("")) {
                    stringValsList.add(s);
                    stringIdsList.add(stringIds[i]);
                }
                i++;
            }
        }

        List<BilateralCondition> numVals = new ArrayList();
        if (snumValsFrom != null && snumValsFrom.length > 0 || snumValsTo != null && snumValsTo.length > 0) {
            int i = 0;

            while (i < numIds.length) {
                Double from = null;
                Double to = null;
                if (snumValsFrom != null && snumValsFrom.length > 0) {
                    from = getNumFromString(snumValsFrom[i], true);
                }
                if (snumValsTo != null && snumValsTo.length > 0) {
                    to = getNumFromString(snumValsTo[i], true);
                }
                if (from != null || to != null) {
                    BilateralCondition c = new BilateralCondition(numIds[i], from, to);
                    numVals.add(c);
                }
                i++;
            }
        }

        List<BilateralCondition> dateVals = new ArrayList();
        if (dateValsFrom != null && dateValsFrom.length > 0 || dateValsTo != null && dateValsTo.length > 0) {
            int i = 0;

            while (i < dateIds.length) {
                Date from = null;
                Date to = null;
                if (dateValsFrom != null && dateValsFrom.length > 0) {
                    from = DateAdapter.getDateFromString(dateValsFrom[i]);
                }
                if (dateValsTo != null && dateValsTo.length > 0) {
                    to = DateAdapter.getDateFromString(dateValsTo[i]);
                }
                if (from != null || to != null) {
                    BilateralCondition c = new BilateralCondition(dateIds[i], from, to);
                    dateVals.add(c);
                }
                i++;
            }
        }

        Double priceFrom = null;
        if (stringPriceFrom != null && !stringPriceFrom.equals("")) {
            priceFrom = getNumFromString(stringPriceFrom, true);
        }
        Double priceTo = null;
        if (stringPriceTo != null && !stringPriceTo.equals("")) {
            priceTo = getNumFromString(stringPriceTo, true);
        }
        
        if(selVals!=null&&!selVals.isEmpty()){
            List<Long>clearedVals = new ArrayList();
            List<Long>cleardIds = new ArrayList();
            int i=0;
            for(Long val:selVals){
                if(val!=null){
                    Long id = selIds.get(i);
                    clearedVals.add(val);
                    cleardIds.add(id);
                }
                i++;
            }
            selVals=clearedVals;
            selIds=cleardIds;
        }
        
        if(booleanVals!=null&&!booleanVals.isEmpty()){
            List<Long>clearedVals = new ArrayList();
            List<Long>cleardIds = new ArrayList();
            int i=0;
            for(Long val:booleanVals){
                if(val!=null){
                    Long id = booleanIds.get(i);
                    clearedVals.add(val);
                    cleardIds.add(id);
                }
                i++;
            }
            booleanVals=clearedVals;
            booleanIds=cleardIds;
        }
        
        List<Ad> res = new ArrayList();
        try {
            res = adDao.getAdsByWishInNameOrDescription(wish, catIds, region, order, booleanIds, booleanVals,
                    stringIdsList, stringValsList, numVals, dateVals, selIds, selVals, multyVals, priceFrom, priceTo);
        } catch (Exception e) {
            addError(StringAdapter.getStackTraceException(e));
        }
        return res;
    }

    /*public List<Ad> getAds(String wish, List<Long> catIds, Region region, String order,
            Long booleanIds[], Long booleanVals[], Long stringIds[], String stringVals[],
            Long numIds[], String snumValsFrom[], String snumValsTo[], Long dateIds[], String dateValsFrom[], String dateValsTo[],
            Long selIds[], Long selVals[], Long multyIds[], String multyVals[], String stringPriceFrom, String stringPriceTo) {
        if (order != null) {
            if (order.equals("insert_date")) {
                order += " desc";
            } else if (order.equals("price")) {
                order += " asc";
            } else if (order.equals("show_count")) {
                order += " desc";
            } else {
                order = null;
            }
        }

        List<Long> stringIdsList = new ArrayList();
        List<String> stringValsList = new ArrayList();
        if (stringVals != null && stringVals.length > 0) {
            int i = 0;
            for (String s : stringVals) {
                if (s != null && !s.equals("")) {
                    stringValsList.add(s);
                    stringIdsList.add(stringIds[i]);
                }
                i++;
            }
        }

        List<BilateralCondition> numVals = new ArrayList();
        if (snumValsFrom != null && snumValsFrom.length > 0 || snumValsTo != null && snumValsTo.length > 0) {
            int i = 0;

            while (i < numIds.length) {
                Double from = null;
                Double to = null;
                if (snumValsFrom != null && snumValsFrom.length > 0) {
                    from = getNumFromString(snumValsFrom[i], true);
                }
                if (snumValsTo != null && snumValsTo.length > 0) {
                    to = getNumFromString(snumValsTo[i], true);
                }
                if (from != null || to != null) {
                    BilateralCondition c = new BilateralCondition(numIds[i], from, to);
                    numVals.add(c);
                }
                i++;
            }
        }

        List<BilateralCondition> dateVals = new ArrayList();
        if (dateValsFrom != null && dateValsFrom.length > 0 || dateValsTo != null && dateValsTo.length > 0) {
            int i = 0;

            while (i < dateIds.length) {
                Date from = null;
                Date to = null;
                if (dateValsFrom != null && dateValsFrom.length > 0) {
                    from = DateAdapter.getDateFromString(dateValsFrom[i]);
                }
                if (dateValsTo != null && dateValsTo.length > 0) {
                    to = DateAdapter.getDateFromString(dateValsTo[i]);
                }
                if (from != null || to != null) {
                    BilateralCondition c = new BilateralCondition(dateIds[i], from, to);
                    dateVals.add(c);
                }
                i++;
            }
        }

        Double priceFrom = null;
        if (stringPriceFrom != null && !stringPriceFrom.equals("")) {
            priceFrom = getNumFromString(stringPriceFrom, true);
        }
        Double priceTo = null;
        if (stringPriceTo != null && !stringPriceTo.equals("")) {
            priceTo = getNumFromString(stringPriceTo, true);
        }

        
        List<Ad> res = new ArrayList();
        try {
            res = adDao.getAdsByWishInNameOrDescription(wish, catIds, region, order, booleanIds, booleanVals,
                    stringIdsList, stringValsList, numVals, dateVals, selIds, selVals, multyVals, priceFrom, priceTo);
        } catch (Exception e) {
            addError(StringAdapter.getStackTraceException(e));
        }
        return res;
    }*/
    /*addError("priceFrom= "+StringAdapter.getString(priceFrom));
         addError("; priceTo= "+StringAdapter.getString(priceTo));
         addError("; wish="+wish);
         String bi="";
         if(booleanIds!=null&&booleanIds.length>0){
         for(Long id:booleanIds){
         bi+=id+"; ";
         }
         }
         String bv="";
         if(booleanVals!=null&&booleanVals.length>0){
         for(Long v:booleanVals){
         bv+=v+"; ";
         }
         }
         addError("BI:"+bi);
         addError("BV:"+bv);
        
         String seli="";
         String selv="";
         if(selIds!=null&&selIds.length>0){
         for(Long id:selIds){
         seli+=id+"; ";
         }
         }
         if(selVals!=null&&selVals.length>0){
         for(Long v:selVals){
         selv+=v+"; ";
         }
         }
         addError("SI:"+seli);
         addError("SV:"+selv);*/

    public Ad getAd(Long adId) {
        return adDao.find(adId);
    }

    public LinkedHashMap<String, Integer> getCatsWithCountsBySearch(String wish, List<Long> catIds, Region region,
            List<Long> booleanIds, List<Long> booleanVals, Long stringIds[], String stringVals[],
            Long numIds[], String snumValsFrom[], String snumValsTo[], Long dateIds[], String dateValsFrom[], String dateValsTo[],
            List<Long> selIds, List<Long> selVals, Long multyIds[], String multyVals[], String stringPriceFrom, String stringPriceTo) {

        List<Long> stringIdsList = new ArrayList();
        List<String> stringValsList = new ArrayList();
        if (stringVals != null && stringVals.length > 0) {
            int i = 0;
            for (String s : stringVals) {
                if (s != null && !s.equals("")) {
                    stringValsList.add(s);
                    stringIdsList.add(stringIds[i]);
                }
                i++;
            }
        }

        List<BilateralCondition> numVals = new ArrayList();
        if (snumValsFrom != null && snumValsFrom.length > 0 || snumValsTo != null && snumValsTo.length > 0) {
            int i = 0;

            while (i < numIds.length) {
                Double from = null;
                Double to = null;
                if (snumValsFrom != null && snumValsFrom.length > 0) {
                    from = getNumFromString(snumValsFrom[i], false);
                }
                if (snumValsTo != null && snumValsTo.length > 0) {
                    to = getNumFromString(snumValsTo[i], false);
                }
                if (from != null || to != null) {
                    BilateralCondition c = new BilateralCondition(numIds[i], from, to);
                    numVals.add(c);
                }
                i++;
            }
        }

        List<BilateralCondition> dateVals = new ArrayList();
        if (dateValsFrom != null && dateValsFrom.length > 0 || dateValsTo != null && dateValsTo.length > 0) {
            int i = 0;

            while (i < dateIds.length) {
                Date from = null;
                Date to = null;
                if (dateValsFrom != null && dateValsFrom.length > 0) {
                    from = DateAdapter.getDateFromString(dateValsFrom[i]);
                }
                if (dateValsTo != null && dateValsTo.length > 0) {
                    to = DateAdapter.getDateFromString(dateValsTo[i]);
                }
                if (from != null || to != null) {
                    BilateralCondition c = new BilateralCondition(dateIds[i], from, to);
                    dateVals.add(c);
                }
                i++;
            }
        }

        Double priceFrom = null;
        if (stringPriceFrom != null && !stringPriceFrom.equals("")) {
            priceFrom = getNumFromString(stringPriceFrom, false);
        }
        Double priceTo = null;
        if (stringPriceTo != null && !stringPriceTo.equals("")) {
            priceTo = getNumFromString(stringPriceTo, false);
        }
        if(selVals!=null&&!selVals.isEmpty()){
            List<Long>clearedVals = new ArrayList();
            List<Long>cleardIds = new ArrayList();
            int i=0;
            for(Long val:selVals){
                if(val!=null){
                    Long id = selIds.get(i);
                    clearedVals.add(val);
                    cleardIds.add(id);
                }
                i++;
            }
            selVals=clearedVals;
            selIds=cleardIds;
        }
        
        if(booleanVals!=null&&!booleanVals.isEmpty()){
            List<Long>clearedVals = new ArrayList();
            List<Long>cleardIds = new ArrayList();
            int i=0;
            for(Long val:booleanVals){
                if(val!=null){
                    Long id = booleanIds.get(i);
                    clearedVals.add(val);
                    cleardIds.add(id);
                }
                i++;
            }
            booleanVals=clearedVals;
            booleanIds=cleardIds;
        }
        LinkedHashMap<String, Integer> res = adDao.getCatsWithCountsBySearch(wish, catIds, region, booleanIds, booleanVals,
                stringIdsList, stringValsList, numVals, dateVals, selIds, selVals, multyVals, priceFrom, priceTo);
        /*for(String s:res.keySet()){
         addError(s+":"+res.get(s));
         }*/
        return res;
    }

    public void delete(Long adId) {
        if (adId != null) {
            Ad ad = adDao.find(adId);
            if (ad != null) {
                for (ParametrValue val : ad.getValues()) {
                    valDao.delete(val);
                }
                adDao.clearIps(adId);
                adDao.clearLocs(adId);
                adDao.clearChosens(adId);
                adDao.deleteRelatedMsgs(adId);
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
            //есть - удалить, нет - добавить

            if (adDao.isChosenAd(userId, adId)) {
                adDao.unsetChosen(userId, adId);
            } else {
                adDao.setChosen(userId, adId);
            }
        } else {
            if (adId == null) {
                addError("Объявление не передано");
            }
        }
    }

    public List<Ad> getChosenAds(Long userId) {
        if (userId != null) {
            return adDao.getChosenAds(userId);
        } else {
            return new ArrayList();
        }
    }

    public HashMap<Long, Ad> getChosenAdMap(Long userId) {
        HashMap<Long, Ad> res = new HashMap();
        if (userId != null) {
            for (Ad ad : getChosenAds(userId)) {
                res.put(ad.getId(), ad);
            }
        }
        return res;
    }

    public List<Ad> getPurchases(Long userId) {
        if (userId != null) {
            return adDao.getPurchases(userId);
        } else {
            return new ArrayList();
        }
    }

    public List<Ad> getSales(Long userId) {
        if (userId != null) {
            return adDao.getSales(userId);
        } else {
            return new ArrayList();
        }
    }

    public void buy(User u, Long adId) {
        Ad ad = adDao.find(adId);
        if (ad != null) {
            ad.setBuyer(u);
            ad.setSaleDate(new Date());
            ad.setStatus(Ad.WAITING);
            if (validate(ad)) {
                adDao.update(ad);
            }
        }
    }

    public void changeStatus(Integer status, Long adId) {
        Ad ad = adDao.find(adId);
        if (ad != null) {
            ad.setStatus(status);
            if (Ad.PAID == status) {
                ad.setPayDate(new Date());
                if (validate(ad)) {
                    adDao.update(ad);
                }
            } else if (Ad.DELIVERED == status) {
                ad.setDeliveryDate(new Date());
                if (validate(ad)) {
                    adDao.update(ad);
                }
            }
        }
    }

    public void addWatchFromIp(Long adId, String ip) {
        if (adDao.isIpNotWatched(adId, ip)) {
            Ad ad = adDao.find(adId);
            if (ad != null) {
                Set<String> ips = ad.getIps();
                ips.add(ip);
                ad.setIps(ips);
                ad.setShowCount(Long.valueOf(ips.size()));
                if (validate(ad)) {
                    adDao.update(ad);
                }
            }
        }
    }

    private Double getNumFromString(String val, Boolean throwError) {
        Double numVal = null;
        if (val != null && !val.equals("")) {
            try {
                numVal = Double.valueOf(val.replace(",", ".").replaceAll(" ", ""));
            } catch (Exception e) {
                if (throwError) {
                    addError("Введенное значение " + val + " не является числом");
                }
            }
        }
        return numVal;
    }

    public void changeAd(Long adId, String shortName, String description, String price, Date dateFrom, Date dateTo, Integer status, Long locIds[], String email, String phone, Long catId,
            Long booleanIds[], Long booleanVals[], Long stringIds[], String stringVals[], Long numIds[], String snumVals[],
            Long dateIds[], Date dateVals[], Long selIds[], Long selVals[], Long multyIds[], String multyVals[]) {
        if (adId != null) {
            Ad ad = adDao.find(adId);
            Category cat = catDao.find(catId);
            //Region r = 
            if (ad != null) {
                PhoneEditor phe = new PhoneEditor();
                phone = phe.getPhone(phone);
                addError(phe.error);
                List<Locality> prelocs = new ArrayList();
                if (locIds != null) {
                    prelocs = locDao.getLocs(locIds);
                }
                Set<Locality> locs = new HashSet(prelocs);
                if (cat != null) {
                    ad.setEmail(email);
                    ad.setPhone(phone);
                    ad.setDateFrom(dateFrom);
                    ad.setDateTo(dateTo);
                    ad.setDescription(description);
                    ad.setName(shortName);
                    ad.setPrice(getNumFromString(price, true));
                    ad.setLocalities(locs);
                    if (status != null) {
                        ad.setStatus(status);
                    }
                    ad.setCat(cat);
                    if (validate(ad) && getErrors().isEmpty()) {
                        Set<ParametrValue> oldVals = ad.getValues();
                        List<Long> reqParamIds = catDao.getRequiredParamsIds(catId);
                        List<Parametr> catParams = paramDao.getParamsFromCat(catId);
                        int i = 0;
                        ArrayList<String> paramValsErrs = new ArrayList();
                        ArrayList<ParametrValue> newVals = new ArrayList();

                        if (booleanVals != null && booleanVals.length > 0) {
                            i = 0;

                            while (i < booleanIds.length) {
                                Boolean invalid = false;
                                Long paramId = booleanIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.BOOL == p.getParamType()) {
                                    Long val = booleanVals[i];
                                    if (val != null) {
                                        if (reqParamIds.contains(paramId)) {
                                            reqParamIds.remove(paramId);
                                        }
                                        String sval = "";
                                        if(val.equals(ParametrValue.NO)){
                                            sval = "нет";
                                        }else if(val.equals(ParametrValue.YES)){
                                            sval = "да";
                                        }else{
                                            invalid=true;
                                        }
                                        if(!invalid){
                                            ParametrValue pv = new ParametrValue();
                                            pv.setAd(ad);
                                            pv.setParametr(p);
                                            pv.setSelectVal(val);
                                            pv.setStringVal(sval);
                                            if (validate(pv)) {
                                                newVals.add(pv);
                                            }
                                        }
                                    }
                                }
                                i++;
                            }
                        }

                        if (stringVals != null && stringVals.length > 0) {
                            i = 0;
                            while (i < stringIds.length) {
                                Long paramId = stringIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.TEXT == p.getParamType()) {
                                    String val = stringVals[i];
                                    if (val != null && !val.equals("")) {
                                        if (reqParamIds.contains(paramId)) {
                                            reqParamIds.remove(paramId);
                                        }

                                        ParametrValue pv = new ParametrValue();
                                        pv.setAd(ad);
                                        pv.setParametr(p);
                                        pv.setStringVal(val);
                                        if (validate(pv)) {
                                            newVals.add(pv);
                                        }

                                    }
                                }
                                i++;
                            }
                        }

                        if (snumVals != null && snumVals.length > 0) {
                            i = 0;
                            while (i < numIds.length) {
                                Long paramId = numIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.NUM == p.getParamType()) {
                                    String sval = snumVals[i];
                                    if (sval != null && !sval.equals("")) {
                                        Double val = getNumFromString(sval, true);
                                        if (reqParamIds.contains(paramId)) {
                                            reqParamIds.remove(paramId);
                                        }
                                        ParametrValue pv = new ParametrValue();
                                        pv.setAd(ad);
                                        pv.setParametr(p);
                                        pv.setNumVal(val);
                                        pv.setStringVal(StringAdapter.getString(val));
                                        if (validate(pv)) {
                                            newVals.add(pv);
                                        }

                                    }
                                }
                                i++;
                            }
                            //???????TO DO check?delete?
                            if (!getErrors().isEmpty()) {
                                for (String e : getErrors()) {
                                    paramValsErrs.add(e);
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
                                    if (val != null) {
                                        if (reqParamIds.contains(paramId)) {
                                            reqParamIds.remove(paramId);
                                        }
                                        ParametrValue pv = new ParametrValue();
                                        pv.setAd(ad);
                                        pv.setParametr(p);
                                        pv.setDateVal(val);
                                        pv.setStringVal(DateAdapter.formatByDate(val, DateAdapter.SMALL_FORMAT));
                                        if (validate(pv)) {
                                            newVals.add(pv);
                                        }

                                    }
                                }
                                i++;
                            }
                        }

                        if (selVals != null && selVals.length > 0) {
                            i = 0;

                            while (i < selIds.length) {
                                Long paramId = selIds[i];
                                Parametr p = paramDao.find(paramId);
                                if (catParams.contains(p) && Parametr.SELECTING == p.getParamType()) {
                                    Long val = selVals[i];
                                    if (val != null && !val.equals(0L)) {
                                        if (reqParamIds.contains(paramId)) {
                                            reqParamIds.remove(paramId);
                                        }
                                        ParametrValue pv = new ParametrValue();
                                        pv.setAd(ad);
                                        pv.setParametr(p);
                                        pv.setSelectVal(val);
                                        pv.setStringVal(paramSelDao.find(val).getName());
                                        if (validate(pv)) {
                                            newVals.add(pv);
                                        }

                                    }
                                }
                                i++;
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
                                        pv.setStringVal(paramSelDao.find(val).getName());
                                        if (validate(pv)) {
                                            newVals.add(pv);
                                        }
                                    }
                                }
                            }
                        }

                        if (!reqParamIds.isEmpty() || !paramValsErrs.isEmpty()) {
                            for (Long id : reqParamIds) {
                                addError("необходимо указать значение параметра " + paramDao.find(id).getName() + "; ");
                            }
                        } else {
                            adDao.update(ad);
                            for (ParametrValue pv : oldVals) {
                                paramValueDao.delete(pv);
                            }
                            for (ParametrValue pv : newVals) {
                                paramValueDao.save(pv);
                            }
                        }

                    }
                }

            }
        }
    }

    public HashMap<Long, Long> getUserAds(Long userId) {
        List<Ad> ads = adDao.getAdsByUser(userId);
        HashMap<Long, Long> adMap = new HashMap();
        for (Ad ad : ads) {
            adMap.put(ad.getId(), ad.getId());
        }
        return adMap;
    }

    /*public List<String>getPreviews(Long adId){
     List<String>res = new ArrayList();
     int i = 0;
     while(i<7){
     File f = new File("/usr/local/seller/preview/"+adId+"/"+i);
     if(f.exists()){
     res.add("../imgs/"+adId+"/"+(i++));
     }else{
     if(i==0){
     res.add("../img/no-photo.png");
     }
     break;
     }
     }
     return res;
     }*/
    /*public void delete(Long adId){
     if(adId!=null){
     Ad ad = adDao.find(adId);
     }
     }*/
}
