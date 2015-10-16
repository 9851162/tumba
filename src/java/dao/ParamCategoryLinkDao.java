/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.ParamCategoryLink;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class ParamCategoryLinkDao extends Dao<ParamCategoryLink>  {
    
    @Override
    public Class getSupportedClass() {
        return ParamCategoryLink.class;
    }
    
    public Integer delete(Long paramId,Long catId){
       //TO DO delete values!!!
        
        String sql = "delete from param_category_link where parametr_id=:paramId and category_id=:catId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("catId", catId);
        query.setParameter("paramId", paramId);
        return query.executeUpdate();
    }
    
}
