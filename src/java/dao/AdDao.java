/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Ad;
import entities.Locality;
import entities.Region;
import entities.State;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import support.BilateralCondition;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class AdDao extends Dao<Ad> {

    @Override
    public Class getSupportedClass() {
        return Ad.class;
    }

    public boolean isChosenAd(Long userId, Long adId) {
        String sql = "select ad_id from chosen_ads where user_id=:userId and ad_id=:adId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("adId", adId);
        List<Object> res = query.list();
        return !res.isEmpty();
    }

    public int setChosen(Long userId, Long adId) {
        String sql = "insert into chosen_ads (ad_id,user_id) values(:adId,:userId)";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("adId", adId);
        return query.executeUpdate();
    }

    public int unsetChosen(Long userId, Long adId) {
        String sql = "delete from chosen_ads where ad_id=:adId and user_id=:userId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("adId", adId);
        return query.executeUpdate();
    }

    public List<Long> getChosenIds(Long userId) {
        String sql = "select ad_id from chosen_ads where user_id=:userId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        List<BigInteger> preres = query.list();
        List<Long> res = new ArrayList();
        for (BigInteger id : preres) {
            res.add(id.longValue());
        }
        return res;
    }
    
    public List<Ad> getAdsByUser(Long userId) {
        String sql = "select * from ad where author_id=:userId and date_to>:now and :now>date_from";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("now", new Date());
        query.addEntity(Ad.class);
        return query.list();
    }

    public List<Ad> getChosenAds(Long userId) {
        String sql = "select a.* from chosen_ads ca left join ad a on ca.ad_id=a.ad_id where ca.user_id=:userId and a.date_to>:now and :now>a.date_from";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("now", new Date());
        query.addEntity(Ad.class);
        return query.list();
    }

    public List<Ad> getSales(Long userId) {
        String sql = "select * from ad where author_id=:userId";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.addEntity(Ad.class);
        return query.list();
    }

    public List<Ad> getPurchases(Long userId) {
        String sql = "select * from ad where buyer_id=:userId order by sale_date";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.addEntity(Ad.class);
        return query.list();
    }

    /*public List<Ad>getStrictlyNameByWish(String wish){
     String sql = "select * from ad where name=:wish order by sale_date";
     SQLQuery query = getCurrentSession().createSQLQuery(sql);
     query.setParameter("wish", wish);
     query.addEntity(Ad.class);
     return query.list();
     }
    
     public List<Ad>getStrictlyDescByWish(String wish){
     String sql = "select * from ad where description=:wish order by sale_date";
     SQLQuery query = getCurrentSession().createSQLQuery(sql);
     query.setParameter("wish", wish);
     query.addEntity(Ad.class);
     return query.list();
     }
    
     public List<Ad>getStrictlyCatByWish(String wish){
     String sql = "select a.* from ad a left join category c on a.category_id=c.category_id where c.name=:wish order by a.sale_date";
     SQLQuery query = getCurrentSession().createSQLQuery(sql);
     query.setParameter("wish", wish);
     query.addEntity(Ad.class);
     return query.list();
     }
    
     public List<Ad>getNonStrictlyNameByWish(String wish){
     String sql = "select * from ad where name like :wish order by sale_date";
     SQLQuery query = getCurrentSession().createSQLQuery(sql);
     query.setParameter("wish", "%"+wish+"%");
     query.addEntity(Ad.class);
     return query.list();
     }
    
     public List<Ad>getNonStrictlyDescByWish(String wish){
     String sql = "select * from ad where description like :wish order by sale_date";
     SQLQuery query = getCurrentSession().createSQLQuery(sql);
     query.setParameter("wish", "%"+wish+"%");
     query.addEntity(Ad.class);
     return query.list();
     }*/
    
    
    
   /* public LinkedHashMap<String,Integer> getCatsWithCountsBySearch(String wish, List<Long> catIds, Region region,
            Long booleanIds[], Long booleanVals[], List<Long> stringIds, List<String> stringVals,
            List<Long> numIds, List<Double> numVals, List<Integer> numConditions, List<Long> dateIds,
            List<Date> dateVals, List<Integer> dateConditions,Long selIds[], Long selVals[], String multyVals[]){
        String sql = "select c.name,count(sel.ad_id) cc from (select ad.ad_id,ad.category_id from ad where ad.date_from<:now and :now<ad.date_to";
        if (wish == null) {
            wish = "";
        }
        List<String> splitted = splitted(wish);
        if (!splitted.isEmpty()) {
            sql += " and (1!=1";
            for (String st : splitted) {
                sql += " or (ad.name like :wish" + splitted.indexOf(st) + ")";
            }
            for (String st : splitted) {
                sql += " or (ad.description like :wish" + splitted.indexOf(st) + ")";
            }
            sql += ")";
        }

        if (!catIds.isEmpty()) {
            sql += " and (1!=1";
            for (Long id : catIds) {
                sql += " or ad.category_id=:catId" + catIds.indexOf(id);
            }
            sql += ")";
        }

        if (region != null && !region.isAllRussia()) {
            sql += " and ad.ad_id in (select ad_id from ads_at_locals where locality_id in (:localIds))";
        }

        
        
        Integer paramsCount = 0;
        Boolean queryWithParams = false;
        if ((stringVals != null && !stringVals.isEmpty()) || (booleanVals != null && booleanVals.length > 0)
                || (numVals != null && !numVals.isEmpty()) || (dateVals != null && !dateVals.isEmpty())
                || (selVals != null && selVals.length > 0) || (multyVals != null && multyVals.length > 0)) {

            queryWithParams = true;
            sql += " and exists(select 1 from (select count(pv.ad_id) cnt,pv.ad_id id from parametr_value pv where (1!=1)";
            int i = 0;

            if (booleanVals != null && booleanVals.length > 0) {
                i = 0;
                while (i < booleanIds.length) {
                    Long paramId = booleanIds[i];
                    Long val = booleanVals[i];
                    if (val != null) {
                        sql += " or (parametr_id=" + paramId + " and select_value=" + val + ")";
                        paramsCount++;
                    }
                    i++;
                }
            }

            if (stringVals != null && !stringVals.isEmpty()) {
                i = 0;
                for (String val : stringVals) {
                    sql += " or (parametr_id=:stringId" + i + " and string_value like '%:stringVal" + i + "%')";
                    paramsCount++;
                    i++;
                }

            }

            if (numVals != null && !numVals.isEmpty()) {
                i = 0;
                for (Double val : numVals) {
                    sql += " or (parametr_id=:numId" + i + " and number_value " + getStringCondition(numConditions.get(i)) + " :numVal" + i + ")";
                    paramsCount++;
                    i++;
                }

            }

            if (dateVals != null && !dateVals.isEmpty()) {
                i = 0;
                for (Date val : dateVals) {
                    sql += " or (parametr_id=:dateId" + i + " and date_value " + getStringCondition(dateConditions.get(i)) + " :dateVal" + i + ")";
                    paramsCount++;
                    i++;
                }
            }

            if (selVals != null && selVals.length > 0) {
                i = 0;
                while (i < selIds.length) {
                    Long paramId = selIds[i];
                    Long val = selVals[i];
                    if (val != null) {
                        sql += " or (parametr_id=" + paramId + " and select_value=" + val + ")";
                        paramsCount++;
                    }
                    i++;
                }
            }

            if (multyVals != null && multyVals.length > 0) {

                for (String rawVal : multyVals) {
                    String idValArr[] = rawVal.split("_");
                    if (idValArr.length == 2) {
                        String strId = idValArr[0];
                        String strVal = idValArr[1];
                        Long paramId = Long.valueOf(strId);
                        Long val = Long.valueOf(strVal);
                        if (val != null) {
                            sql += " or (parametr_id=" + paramId + " and select_value=" + val + ")";
                            paramsCount++;
                        }
                    }
                }

            }

            sql += " group by pv.ad_id) as tmp where tmp.cnt=:paramsCount and tmp.id=ad.ad_id)";
            
            
        }

        sql+=") sel left join category c on sel.category_id=c.category_id group by sel.category_id order by cc desc";
        
        
        
        
        SQLQuery query = getCurrentSession().createSQLQuery(sql);

        if (!splitted.isEmpty()) {
            for (String st : splitted) {
                query.setParameter("wish" + splitted.indexOf(st), st);
            }
        }
        if (!catIds.isEmpty()) {
            for (Long id : catIds) {
                query.setParameter("catId" + catIds.indexOf(id), id);
            }
        }
        if(stringVals!=null&&!stringVals.isEmpty()){
            int i=0;
            for(String s:stringVals){
                query.setParameter("stringId"+i, stringIds.get(i));
                query.setParameter("stringVal"+i, stringVals.get(i));
                i++;
            }
        }
        if(numVals!=null&&!numVals.isEmpty()){
            int i=0;
            for(Double d:numVals){
                query.setParameter("numId"+i, numIds.get(i));
                //query.setParameter("numCondition"+i, getStringCondition(numConditions.get(i)));
                query.setParameter("numVal"+i, numVals.get(i));
                i++;
            }
        }
        if(dateVals!=null&&!dateVals.isEmpty()){
            int i=0;
            for(Date d:dateVals){
                query.setParameter("dateId"+i, dateIds.get(i));
                //query.setParameter("dateCondition"+i, getStringCondition(dateConditions.get(i)));
                query.setParameter("dateVal"+i, dateVals.get(i));
                i++;
            }
        }
        if (queryWithParams) {
            query.setParameter("paramsCount", paramsCount);
        }
        query.setParameter("now", new Date());
        if (region != null && !region.isAllRussia()) {
            query.setParameterList("localIds", getLocIds(region));
        }
        List<Object[]>rawRes=query.list();
        LinkedHashMap<String,Integer>res = new LinkedHashMap();
        if(!rawRes.isEmpty()){
            for(Object[]o:rawRes){
                String catName=(String)o[0];
                Integer count=((BigInteger)o[1]).intValue();
                if(0!=count){
                    res.put(catName,count);
                }
            }
        }
        return res;
    }*/
    
    public LinkedHashMap<String,Integer> getCatsWithCountsBySearch(String wish, List<Long> catIds, Region region,
            List<Long> booleanIds, List<Long> booleanVals, List<Long> stringIds, List<String> stringVals,
            List<BilateralCondition>numVals, List<BilateralCondition> dateVals,
            List<Long> selIds, List<Long> selVals, String multyVals[],Double priceFrom,Double priceTo){
        String sql = "select c.name,count(sel.ad_id) cc from (select ad.ad_id,ad.category_id from ad where ad.date_from<:now and :now<ad.date_to";
        if (wish == null) {
            wish = "";
        }
        List<String> splitted = splitted(wish);
        if (!splitted.isEmpty()) {
            sql += " and (1!=1";
            for (String st : splitted) {
                sql += " or (ad.name like :wish" + splitted.indexOf(st) + ")";
            }
            for (String st : splitted) {
                sql += " or (ad.description like :wish" + splitted.indexOf(st) + ")";
            }
            sql += ")";
        }

        if (!catIds.isEmpty()) {
            sql += " and (1!=1";
            for (Long id : catIds) {
                sql += " or ad.category_id=:catId" + catIds.indexOf(id);
            }
            sql += ")";
        }

        if (region != null && !region.isAllRussia()) {
            sql += " and ad.ad_id in (select ad_id from ads_at_locals where locality_id in (:localIds))";
        }

        /**
         * Условия для параметров*
         */
        
        if(priceFrom!=null){
            sql+=" and ad.price>=:priceFrom";
        }
        if(priceTo!=null){
            sql+=" and ad.price<=:priceTo";
        }
        Integer paramsCount = 0;
        Boolean queryWithParams = false;
        if ((stringVals != null && !stringVals.isEmpty()) || (booleanVals != null && !booleanVals.isEmpty())
                || (numVals != null && !numVals.isEmpty()) || (dateVals != null && !dateVals.isEmpty())
                || (selVals != null && !selVals.isEmpty()) || (multyVals != null && multyVals.length > 0)) {

            queryWithParams = true;
            sql += " and exists(select 1 from (select count(pv.ad_id) cnt,pv.ad_id id from parametr_value pv where (1!=1)";
            int i = 0;

            if (stringVals != null && !stringVals.isEmpty()) {
                i = 0;
                for (String val : stringVals) {
                    sql += " or (parametr_id=:stringId" + i + " and string_value like '%:stringVal" + i + "%')";
                    paramsCount++;
                    i++;
                }

            }

            if (numVals != null && !numVals.isEmpty()) {
                i = 0;
                for (BilateralCondition c : numVals) {
                    Double numFrom =(Double)c.getFrom();
                    Double numTo =(Double)c.getTo();
                    if(numFrom!=null||numTo!=null){
                        
                        if(numFrom!=null&&numTo!=null){
                            sql += " or (parametr_id=:numId" + i + " and number_value >= :numFrom" + i + " and number_value <= :numTo" + i + ")";
                        }else{
                            if(numFrom!=null){
                                sql += " or (parametr_id=:numId" + i + " and number_value >= :numFrom" + i + ")";
                            }
                            if(numTo!=null){
                                sql += " or (parametr_id=:numId" + i + " and number_value <= :numTo" + i + ")";
                            }
                        }
                        paramsCount++;
                        i++;
                    }
                }

            }
            
            if (dateVals != null && !dateVals.isEmpty()) {
                i = 0;
                for (BilateralCondition c : dateVals) {
                    Date dateFrom =(Date)c.getFrom();
                    Date dateTo =(Date)c.getTo();
                    if(dateFrom!=null||dateTo!=null){
                        if(dateFrom!=null&&dateTo!=null){
                            sql += " or (parametr_id=:dateId" + i + " and date_value >= :dateFrom" + i + " and date_value <= :dateTo" + i + ")";
                        }else{
                            if(dateFrom!=null){
                                sql += " or (parametr_id=:dateId" + i + " and date_value >= :dateFrom" + i + ")";
                            }
                            if(dateTo!=null){
                                sql += " or (parametr_id=:dateId" + i + " and date_value <= :dateTo" + i + ")";
                            }
                        }
                        paramsCount++;
                        i++;
                    }
                }
            }

            if (booleanVals != null && !booleanVals.isEmpty()) {
                i = 0;
                for(Long val:booleanVals) {
                    if (val != null) {
                        sql += " or (parametr_id=:booleanId" + i + " and select_value=:booleanVal" + i + ")";
                        paramsCount++;
                        //ex+=i+":"+val+"; ";
                    }
                    i++;
                }
            }

            if (selVals != null && !selVals.isEmpty()) {
                i = 0;
                for(Long val:selVals) {
                    if (val != null) {
                        sql += " or (parametr_id=:selId" + i + " and select_value=:selVal" + i + ")";
                        paramsCount++;
                        //ex+=i+":"+val+"; ";
                    }
                    i++;
                }
            }

            if (multyVals != null && multyVals.length > 0) {

                for (String rawVal : multyVals) {
                    String idValArr[] = rawVal.split("_");
                    if (idValArr.length == 2) {
                        String strId = idValArr[0];
                        String strVal = idValArr[1];
                        Long paramId = Long.valueOf(strId);
                        Long val = Long.valueOf(strVal);
                        if (val != null) {
                            sql += " or (parametr_id=" + paramId + " and select_value=" + val + ")";
                            paramsCount++;
                        }
                    }
                }

            }

            sql += " group by pv.ad_id) as tmp where tmp.cnt=:paramsCount and tmp.id=ad.ad_id)";
        }
        /**
         * \Условия для параметров*
         */
        sql+=") sel left join category c on sel.category_id=c.category_id group by sel.category_id order by cc desc";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);

        if (!splitted.isEmpty()) {
            for (String st : splitted) {
                query.setParameter("wish" + splitted.indexOf(st), st);
            }
        }
        if (!catIds.isEmpty()) {
            for (Long id : catIds) {
                query.setParameter("catId" + catIds.indexOf(id), id);
            }
        }
        if(priceFrom!=null){
            query.setParameter("priceFrom", priceFrom);
        }
        if(priceTo!=null){
            query.setParameter("priceTo", priceTo);
        }
        if(stringVals!=null&&!stringVals.isEmpty()){
            int i=0;
            for(String s:stringVals){
                query.setParameter("stringId"+i, stringIds.get(i));
                query.setParameter("stringVal"+i, stringVals.get(i));
                i++;
            }
        }
        if(numVals!=null&&!numVals.isEmpty()){
            int i=0;
            for(BilateralCondition c:numVals){
                Long id = c.getId();
                Double numFrom =(Double)c.getFrom();
                Double numTo =(Double)c.getTo();
                query.setParameter("numId"+i, id);
                if(numFrom!=null){
                    query.setParameter("numFrom"+i, numFrom);
                }
                if(numTo!=null){
                    query.setParameter("numTo"+i, numTo);
                }
                i++;
            }
        }
        if(dateVals!=null&&!dateVals.isEmpty()){
            int i=0;
            for(BilateralCondition c:dateVals){
                Long id = c.getId();
                Date dateFrom =(Date)c.getFrom();
                Date dateTo =(Date)c.getTo();
                query.setParameter("dateId"+i, id);
                if(dateFrom!=null){
                    query.setParameter("dateFrom"+i, dateFrom);
                }
                if(dateTo!=null){
                    query.setParameter("dateTo"+i, dateTo);
                }
                i++;
            }
        }
        if(booleanVals!=null&&!booleanVals.isEmpty()){
            int i=0;
            for(Long v:booleanVals){
                if(v!=null){
                    query.setParameter("booleanId"+i, booleanIds.get(i));
                    query.setParameter("booleanVal"+i, v);
                }
                i++;
            }
            
        }
        if(selVals!=null&&!selVals.isEmpty()){
            int i=0;
            for(Long v:selVals){
                if(v!=null){
                    query.setParameter("selId"+i, selIds.get(i));
                    query.setParameter("selVal"+i, v);
                }
                i++;
            }
            
        }
        if (queryWithParams) {
            query.setParameter("paramsCount", paramsCount);
        }
        query.setParameter("now", new Date());
        if (region != null && !region.isAllRussia()) {
            query.setParameterList("localIds", getLocIds(region));
        }
        List<Object[]>rawRes=query.list();
        LinkedHashMap<String,Integer>res = new LinkedHashMap();
        if(!rawRes.isEmpty()){
            for(Object[]o:rawRes){
                String catName=(String)o[0];
                Integer count=((BigInteger)o[1]).intValue();
                if(0!=count){
                    res.put(catName,count);
                }
            }
        }
        return res;
    }
    
    public List<Ad> getAdsByWishInNameOrDescription(String wish, List<Long> catIds, Region region, String order,
            List<Long> booleanIds, List<Long> booleanVals, List<Long> stringIds, List<String> stringVals,
            List<BilateralCondition>numVals, List<BilateralCondition> dateVals,
            List<Long> selIds, List<Long> selVals, String multyVals[],Double priceFrom,Double priceTo) throws Exception {
        if (order == null) {
            order = "show_count desc";
        }  
        String sql = "select * from ad where date_from<:now and :now<date_to";
        if (wish == null) {
            wish = "";
        }
        List<String> splitted = splitted(wish);
        if (!splitted.isEmpty()) {
            sql += " and (1!=1";
            for (String st : splitted) {
                sql += " or (name like :wish" + splitted.indexOf(st) + ")";
            }
            for (String st : splitted) {
                sql += " or (description like :wish" + splitted.indexOf(st) + ")";
            }
            sql += ")";
        }

        if (!catIds.isEmpty()) {
            sql += " and (1!=1";
            for (Long id : catIds) {
                sql += " or category_id=:catId" + catIds.indexOf(id);
            }
            sql += ")";
        }

        if (region != null && !region.isAllRussia()) {
            sql += " and ad_id in (select ad_id from ads_at_locals where locality_id in (:localIds))";
        }

        /**
         * Условия для параметров*
         */
        
        if(priceFrom!=null){
            sql+=" and price>=:priceFrom";
        }
        if(priceTo!=null){
            sql+=" and price<=:priceTo";
        }
        Integer paramsCount = 0;
        Boolean queryWithParams = false;
        if ((stringVals != null && !stringVals.isEmpty()) || (booleanVals != null && !booleanVals.isEmpty())
                || (numVals != null && !numVals.isEmpty()) || (dateVals != null && !dateVals.isEmpty())
                || (selVals != null && !selVals.isEmpty()) || (multyVals != null && multyVals.length > 0)) {

            queryWithParams = true;
            sql += " and exists(select 1 from (select count(pv.ad_id) cnt,pv.ad_id id from parametr_value pv where (1!=1)";
            int i = 0;

            if (stringVals != null && !stringVals.isEmpty()) {
                i = 0;
                for (String val : stringVals) {
                    sql += " or (parametr_id=:stringId" + i + " and string_value like '%:stringVal" + i + "%')";
                    paramsCount++;
                    i++;
                }

            }

            if (numVals != null && !numVals.isEmpty()) {
                i = 0;
                for (BilateralCondition c : numVals) {
                    Double numFrom =(Double)c.getFrom();
                    Double numTo =(Double)c.getTo();
                    if(numFrom!=null||numTo!=null){
                        
                        if(numFrom!=null&&numTo!=null){
                            sql += " or (parametr_id=:numId" + i + " and number_value >= :numFrom" + i + " and number_value <= :numTo" + i + ")";
                        }else{
                            if(numFrom!=null){
                                sql += " or (parametr_id=:numId" + i + " and number_value >= :numFrom" + i + ")";
                            }
                            if(numTo!=null){
                                sql += " or (parametr_id=:numId" + i + " and number_value <= :numTo" + i + ")";
                            }
                        }
                        paramsCount++;
                        i++;
                    }
                }

            }
            
            if (dateVals != null && !dateVals.isEmpty()) {
                i = 0;
                for (BilateralCondition c : dateVals) {
                    Date dateFrom =(Date)c.getFrom();
                    Date dateTo =(Date)c.getTo();
                    if(dateFrom!=null||dateTo!=null){
                        if(dateFrom!=null&&dateTo!=null){
                            sql += " or (parametr_id=:dateId" + i + " and date_value >= :dateFrom" + i + " and date_value <= :dateTo" + i + ")";
                        }else{
                            if(dateFrom!=null){
                                sql += " or (parametr_id=:dateId" + i + " and date_value >= :dateFrom" + i + ")";
                            }
                            if(dateTo!=null){
                                sql += " or (parametr_id=:dateId" + i + " and date_value <= :dateTo" + i + ")";
                            }
                        }
                        paramsCount++;
                        i++;
                    }
                }
            }

            /*if (booleanVals != null && booleanVals.length > 0) {
                i = 0;
                while (i < booleanIds.length) {
                    Long paramId = booleanIds[i];
                    Long val = booleanVals[i];
                    if (val != null) {
                        sql += " or (parametr_id=" + paramId + " and select_value=" + val + ")";
                        paramsCount++;
                    }
                    i++;
                }
            }

            if (selVals != null && selVals.length > 0) {
                i = 0;
                while (i < selIds.length) {
                    Long paramId = selIds[i];
                    Long val = selVals[i];
                    if (val != null) {
                        sql += " or (parametr_id=" + paramId + " and select_value=" + val + ")";
                        paramsCount++;
                    }
                    i++;
                }
            }*/
            
            if (booleanVals != null && !booleanVals.isEmpty()) {
                i = 0;
                for(Long val:booleanVals) {
                    if (val != null) {
                        sql += " or (parametr_id=:booleanId" + i + " and select_value=:booleanVal" + i + ")";
                        paramsCount++;
                        //ex+=i+":"+val+"; ";
                    }
                    i++;
                }
            }

            if (selVals != null && !selVals.isEmpty()) {
                i = 0;
                for(Long val:selVals) {
                    if (val != null) {
                        sql += " or (parametr_id=:selId" + i + " and select_value=:selVal" + i + ")";
                        paramsCount++;
                        //ex+=i+":"+val+"; ";
                    }
                    i++;
                }
            }

            if (multyVals != null && multyVals.length > 0) {

                for (String rawVal : multyVals) {
                    String idValArr[] = rawVal.split("_");
                    if (idValArr.length == 2) {
                        String strId = idValArr[0];
                        String strVal = idValArr[1];
                        Long paramId = Long.valueOf(strId);
                        Long val = Long.valueOf(strVal);
                        if (val != null) {
                            sql += " or (parametr_id=" + paramId + " and select_value=" + val + ")";
                            paramsCount++;
                        }
                    }
                }

            }

            sql += " group by pv.ad_id) as tmp where tmp.cnt=:paramsCount and tmp.id=ad.ad_id)";
        }
        /**
         * \Условия для параметров*
         */
        sql += " order by status asc," + order;
        
        SQLQuery query = getCurrentSession().createSQLQuery(sql);

        if (!splitted.isEmpty()) {
            for (String st : splitted) {
                query.setParameter("wish" + splitted.indexOf(st), st);
            }
        }
        if (!catIds.isEmpty()) {
            for (Long id : catIds) {
                query.setParameter("catId" + catIds.indexOf(id), id);
            }
        }
        if(priceFrom!=null){
            query.setParameter("priceFrom", priceFrom);
        }
        if(priceTo!=null){
            query.setParameter("priceTo", priceTo);
        }
        if(stringVals!=null&&!stringVals.isEmpty()){
            int i=0;
            for(String s:stringVals){
                query.setParameter("stringId"+i, stringIds.get(i));
                query.setParameter("stringVal"+i, stringVals.get(i));
                i++;
            }
        }
        if(booleanVals!=null&&!booleanVals.isEmpty()){
            int i=0;
            for(Long v:booleanVals){
                if(v!=null){
                    query.setParameter("booleanId"+i, booleanIds.get(i));
                    query.setParameter("booleanVal"+i, v);
                }
                i++;
            }
            
        }
        if(selVals!=null&&!selVals.isEmpty()){
            int i=0;
            for(Long v:selVals){
                if(v!=null){
                    query.setParameter("selId"+i, selIds.get(i));
                    query.setParameter("selVal"+i, v);
                }
                i++;
            }
            
        }
        if(numVals!=null&&!numVals.isEmpty()){
            int i=0;
            for(BilateralCondition c:numVals){
                Long id = c.getId();
                Double numFrom =(Double)c.getFrom();
                Double numTo =(Double)c.getTo();
                query.setParameter("numId"+i, id);
                if(numFrom!=null){
                    query.setParameter("numFrom"+i, numFrom);
                }
                if(numTo!=null){
                    query.setParameter("numTo"+i, numTo);
                }
                i++;
            }
        }
        if(dateVals!=null&&!dateVals.isEmpty()){
            int i=0;
            for(BilateralCondition c:dateVals){
                Long id = c.getId();
                Date dateFrom =(Date)c.getFrom();
                Date dateTo =(Date)c.getTo();
                query.setParameter("dateId"+i, id);
                if(dateFrom!=null){
                    query.setParameter("dateFrom"+i, dateFrom);
                }
                if(dateTo!=null){
                    query.setParameter("dateTo"+i, dateTo);
                }
                i++;
            }
        }
        if (queryWithParams) {
            query.setParameter("paramsCount", paramsCount);
        }
        query.setParameter("now", new Date());
        if (region != null && !region.isAllRussia()) {
            query.setParameterList("localIds", getLocIds(region));
        }
        query.addEntity(Ad.class);
        if(1==1){
            //throw new Exception("e="+ex+"; bv:"+booleanVals.size()+"; sv:"+selVals.size()+"; pc:"+paramsCount);
        }
        //throw new Exception(sql);
       
        return query.list();
    }

    /*public List<Ad> getAdsByWishInDesc(String wish, List<Long> catIds, Region region,String order) {
     if(order==null){
     order="show_count desc";
     }
     String sql = "select * from ad";
     if (wish == null) {
     wish = "";
     }
     List<String> splitted = splitted(wish);

     if (!splitted.isEmpty()) {
     sql += " where 1!=1";
     for (String st : splitted) {
     sql += " or (description like :wish" + splitted.indexOf(st) + ")";
     }

     }

     if (!catIds.isEmpty()) {
     if (splitted.isEmpty()) {
     sql += " where 1=1";
     }
     sql += " and (1!=1";
     for (Long id : catIds) {
     sql += " or category_id=:catId" + catIds.indexOf(id);
     }
     sql += ")";
     //sql+=" and category_id in (:catIds)";
     }

     if (region != null && !region.isAllRussia()) {
     sql += " and ad_id in (select ad_id from ads_at_locals where locality_id in (:localIds))";
     }

     sql += " order by status asc,"+order;
     SQLQuery query = getCurrentSession().createSQLQuery(sql);

     if (!splitted.isEmpty()) {
     for (String st : splitted) {
     query.setParameter("wish" + splitted.indexOf(st), st);
     }

     }

     if (!catIds.isEmpty()) {
     for (Long id : catIds) {
     query.setParameter("catId" + catIds.indexOf(id), id);
     }
     }

     if (region != null && !region.isAllRussia()) {
     query.setParameterList("localIds", getLocIds(region));
     }
     //query.setParameter("order", order);
     query.addEntity(Ad.class);
     return query.list();
     }*/
    private List<String> splitted(String request) {
        List<String> split = new ArrayList();
        if (request != null) {
            String[] splittedTest = request.split("\\s+");
            int cnt = 0;
            for (String st : splittedTest) {
                String res = "";
                for (int i = 0; i <= cnt; i++) {
                    res += "%" + splittedTest[i] + "%";
                }
                split.add(res);
                cnt++;
            }
        }
        return split;
    }

    private List<Long> getLocIds(Region r) {
        ArrayList<Long> res = new ArrayList();
        if (r != null) {
           /* for (State s : r.getStates()) {
                for (Locality l : s.getLocalities()) {
                    res.add(l.getId());
                }
            }*/
            for (Locality l : r.getLocalities()) {
                res.add(l.getId());
            }
        }
        return res;
    }

    public boolean isIpNotWatched(Long adId, String ip) {
        String sql = "select * from ads_from_ips where ad_id=:adId and ip=:ip";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("adId", adId);
        query.setParameter("ip", ip);
        return query.list().isEmpty();
    }
    
    private String getStringCondition(Integer condition) {
        if ((-1) == condition) {
            return ("<");
        } else if (1 == condition) {
            return ">";
        } else {
            return "=";
        }
    }
    
    public Integer clearIps(Long adId){
        String sql = "delete from ads_from_ips where ad_id=:adId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("adId", adId);
        return query.executeUpdate();
    }
    
    public Integer clearLocs(Long adId){
        String sql = "delete from ads_at_locals where ad_id=:adId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("adId", adId);
        return query.executeUpdate();
    }
    
    public Integer clearChosens(Long adId){
        String sql = "delete from chosen_ads where ad_id=:adId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("adId", adId);
        return query.executeUpdate();
    }
    
    public Integer deleteRelatedMsgs(Long adId){
        String sql = "delete from message where ad_id=:adId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("adId", adId);
        return query.executeUpdate();
    }

}
