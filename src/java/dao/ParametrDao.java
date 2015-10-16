/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Parametr;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class ParametrDao  extends Dao<Parametr>  {
    
    @Override
    public Class getSupportedClass() {
        return Parametr.class;
    }
    
    public boolean hasCats(Long paramId) throws Exception{
        String sql = "select count(*) from param_category_link where parametr_id=:paramId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("paramId", paramId);
        BigInteger res = (BigInteger)query.uniqueResult();
        //throw new Exception("res="+res);
        return res.compareTo(BigInteger.valueOf(0))>0;
    }
    
    public List<Parametr> getAllParams(){
        String hql = "from Parametr order by name";
        Query query = getCurrentSession().createQuery(hql);
        return query.list();
    }
    
    public Integer deleteFromCats(Long paramId){
        String sql = "delete from param_category_link where parametr_id=:paramId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("paramId", paramId);
        return query.executeUpdate();
    }
    
    public List<Object[]> getCatsParamsAsObjects(){
        String sql = "select pic.category_id,pic.parametr_id from param_category_link pic left join parametr p on pic.parametr_id=p.parametr_id order by p.req_type,p.name,pic.category_id";
        Query query = getCurrentSession().createSQLQuery(sql);
        return query.list();
    }
    
    public List<String> getUnavailableOptionNames(Long paramId){
        String sql = "select name from parametr_sel_option where parametr_id=:paramId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("paramId", paramId);
        return query.list();
    }
    
    public List<Parametr>getParamsFromCat(Long catId){
        String sql = "select p.* from param_category_link l left join parametr p on l.parametr_id=p.parametr_id where l.category_id=:catId";
        SQLQuery query = getCurrentSession().createSQLQuery(sql);
        query.addEntity(Parametr.class);
        query.setParameter("catId", catId);
        return query.list();
    }
    
}
