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
        String sql = "select count(*) from params_in_categories where parametr_id=:paramId";
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
        String sql = "delete from params_in_categories where parametr_id=:paramId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("paramId", paramId);
        return query.executeUpdate();
    }
    
}
