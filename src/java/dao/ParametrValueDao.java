/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.ParametrValue;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class ParametrValueDao extends Dao<ParametrValue>  {
    
    @Override
    public Class getSupportedClass() {
        return ParametrValue.class;
    }
    
    public Integer deleteParamValues(Long paramId){
        String sql = "delete from parametr_value where parametr_id=:paramId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("paramId", paramId);
        return query.executeUpdate();
    }
    
    public List<String> getMultiSelOptionNames(Long paramId,Long adId){
        String sql="select name from parametr_sel_option where parametr_id=:paramId and ad_id=:adId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("paramId", paramId);
        query.setParameter("adId", adId);
        return query.list();
    }
    
}
