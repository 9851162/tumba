/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Ad;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class AdDao extends Dao<Ad>  {
    
    @Override
    public Class getSupportedClass() {
        return Ad.class;
    }
    
    public boolean isChosenAd(Long userId,Long adId){
        String sql = "select ad_id from chosen_ads where user_id=:userId and ad_id=:adId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("adId", adId);
        List<Object>res=query.list();
        return !res.isEmpty();
    }
    
    public int setChosen(Long userId,Long adId){
        String sql = "insert into chosen_ads (ad_id,user_id) values(:adId,:userId)";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("adId", adId);
        return query.executeUpdate();
    }
    
    public int unsetChosen(Long userId,Long adId){
        String sql = "delete from chosen_ads where ad_id=:adId and user_id=:userId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("adId", adId);
        return query.executeUpdate();
    }
    
    public List<Long>getChosenIds(Long userId){
        String sql = "select ad_id from chosen_ads where user_id=:userId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        List<BigInteger>preres=query.list();
        List<Long>res=new ArrayList();
        for(BigInteger id:preres){
            res.add(id.longValue());
        }
        return res;
    }
    
    public List<Ad>getChosenAds(Long userId){
        String sql = "select a.* from chosen_ads ca left join ad a on ca.ad_id=a.ad_id where ca.user_id=:userId";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.addEntity(Ad.class);
        return query.list();
    }
    
    public List<Ad>getSales(Long userId){
        String sql = "select * from ad where user_id=:userId and buyer_id is not null";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        query.addEntity(Ad.class);
        return query.list();
    }
    
    public List<Ad>getPurchases(Long userId){
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
    
    public List<Ad>getAdsByWishInName(String wish){
        String sql = "select * from ad";
        List<String> splitted=splitted(wish);
        if(!splitted.isEmpty()){
            sql+=" where 1!=1";
            for(String st:splitted){
                sql+=" or (name like :wish"+splitted.indexOf(st)+")";
            }
            
        }
        sql+=" order by sale_date desc";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        if(!splitted.isEmpty()){
            for(String st:splitted){
                query.setParameter("wish"+splitted.indexOf(st),st);
            }
            
        }
        query.addEntity(Ad.class);
        return query.list();
    }
    
    public List<Ad>getAdsByWishInDesc(String wish){
        String sql = "select * from ad";
        List<String> splitted=splitted(wish);
        if(!splitted.isEmpty()){
            sql+=" where 1!=1";
            for(String st:splitted){
                sql+=" or (description like :wish"+splitted.indexOf(st)+")";
            }
            
        }
        sql+=" order by sale_date desc";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        if(!splitted.isEmpty()){
            for(String st:splitted){
                query.setParameter("wish"+splitted.indexOf(st),st);
            }
            
        }
        query.addEntity(Ad.class);
        return query.list();
    }
    
    /*public List<Ad>getNonStrictlyCatByWish(String wish){
        String sql = "select a.* from ad a left join category c on a.category_id=c.category_id where 1=1";
        List<String> splitted=splitted(wish);
        if(!splitted.isEmpty()){
            for(String st:splitted){
                sql+=" or (c.name like :wish"+splitted.indexOf(st)+")";
            }
            
        }
        sql+=" order by a.sale_date desc";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        if(!splitted.isEmpty()){
            for(String st:splitted){
                query.setParameter("wish"+splitted.indexOf(st),st);
            }
            
        }
        query.addEntity(Ad.class);
        return query.list();
    }*/
    
    
    private List<String> splitted(String request){
        List<String> split=new ArrayList();
        if(request!=null){
            String[] splittedTest=request.split("\\s+");
            int cnt=0;
            for(String st:splittedTest){      
                String res="";
                for(int i=0;i<=cnt;i++){
                    res+="%"+splittedTest[i]+"%";
                }
                split.add(res);
                cnt++;
            }
        }
        return split;
    }
}
