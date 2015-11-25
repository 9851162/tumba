/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Ad;
import entities.Locality;
import entities.Parametr;
import entities.ParametrValue;
import entities.Region;
import entities.State;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import support.StringAdapter;

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

    public List<Ad> getChosenAds(Long userId) {
        String sql = "select a.* from chosen_ads ca left join ad a on ca.ad_id=a.ad_id where ca.user_id=:userId";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.addEntity(Ad.class);
        return query.list();
    }

    public List<Ad> getSales(Long userId) {
        String sql = "select * from ad where author_id=:userId and buyer_id is not null";
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
    public List<Ad> getAdsByWishInNameOrDescription(String wish, List<Long> catIds, Region region, String order,
            Long booleanIds[], String booleanVals[], Long stringIds[], String stringVals[],
            Long numIds[], Double numVals[], Integer numConditions[], Long dateIds[], Date dateVals[], Integer dateConditions[],
            Long selIds[], Long selVals[], Long multyIds[], String multyVals[]) {
        if (order == null) {
            order = "show_count desc";
        }
        String sql = "select * from ad";
        if (wish == null) {
            wish = "";
        }
        List<String> splitted = splitted(wish);
        if (!splitted.isEmpty()) {
            sql += " where 1!=1";
            for (String st : splitted) {
                sql += " or (name like :wish" + splitted.indexOf(st) + ")";
            }
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
        }

        if (region != null && !region.isAllRussia()) {
            sql += " and ad_id in (select ad_id from ads_at_locals where locality_id in (:localIds))";
        }

        /**
         * Условия для параметров*
         */
        /*Boolean queryWithParams = false;
        int i = 0;
        if (stringVals != null && stringVals.length > 0) {
            i = 0;
            if (!queryWithParams) {
                sql += " and ad_id in (select ad_id from parametr_value where 1=1";
                queryWithParams = true;
            }
            while (i < stringIds.length) {
                Long paramId = stringIds[i];
                String val = stringVals[i];
                if (val != null && !val.equals("")) {
                    sql += " and (parametr_id=" + paramId + " and string_value like '%" + val + "%')";
                }
                i++;
            }
        }

        if (numVals != null && numVals.length > 0) {
            i = 0;
            if (!queryWithParams) {
                sql += " and ad_id in (select ad_id from parametr_value where 1=1";
                queryWithParams = true;
            }
            while (i < numIds.length) {
                Long paramId = numIds[i];
                String c = getStringCondition(numConditions[i]);
                Double val = numVals[i];
                if (val != null) {
                    sql += " and (parametr_id=" + paramId + " and number_value" + c + val+")";
                }
                i++;
            }
        }
        
        if (dateVals != null && dateVals.length > 0) {
            i = 0;
            if (!queryWithParams) {
                sql += " and ad_id in (select ad_id from parametr_value where 1=1";
                queryWithParams = true;
            }
            while (i < dateVals.length) {
                Long paramId = dateIds[i];
                String c = getStringCondition(dateConditions[i]);
                Date val = dateVals[i];
                if (val != null) {
                    sql += " and (parametr_id=" + paramId + " and date_value" + c + val+")";
                }
                i++;
            }
        }
        
        if (selVals != null && selVals.length > 0) {
            i = 0;
            if (!queryWithParams) {
                sql += " and ad_id in (select ad_id from parametr_value where 1=1";
                queryWithParams = true;
            }
            while (i < selIds.length) {
                Long paramId = selIds[i];
                Long val = selVals[i];
                if (val != null) {
                    sql += " and (parametr_id=" + paramId + " and select_value=" + val+")";
                }
                i++;
            }
        }
        
        if (multyVals != null && multyVals.length > 0) {
            if (!queryWithParams) {
                sql += " and ad_id in (select ad_id from parametr_value where 1=1";
                queryWithParams = true;
            }
            
        }

        if (queryWithParams) {
            sql += ")";
        }*/
        /**
         * \Условия для параметров*
         */

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

    private Set<Long> getLocIds(Region r) {
        Set<Long> res = new HashSet();
        if (r != null) {
            for (State s : r.getStates()) {
                for (Locality l : s.getLocalities()) {
                    res.add(l.getId());
                }
            }
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

}
