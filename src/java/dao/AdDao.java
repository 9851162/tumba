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
    
}
