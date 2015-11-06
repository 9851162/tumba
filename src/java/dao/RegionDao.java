/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Region;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class RegionDao extends Dao<Region>  {
    
    @Override
    public Class getSupportedClass() {
        return Region.class;
    }
    
    public int clearHome(Long userId){
        String sql = "update region set home_region = null where user_id=:userId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("userId", userId);
        return query.executeUpdate();
    }
    
}
