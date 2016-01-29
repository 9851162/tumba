/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Locality;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class LocalityDao extends Dao<Locality>  {
    
    @Override
    public Class getSupportedClass() {
        return Locality.class;
    }
    
    public boolean isAvailableName(String name,Long stateId){
        String hql = "from Locality where name=:name and state.id=:stateId";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("name", name);
        query.setParameter("stateId", stateId);
        return query.list().isEmpty();
    }
    
    public List<Locality>getLocs(Long locIds[]){
        String hql = "from Locality where id in (:locIds)";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameterList("locIds", locIds);
        return query.list();
    }
    
    public List<Long>getAllLocIds(){
        String hql = "select id from Locality";
        Query query = getCurrentSession().createQuery(hql);
        return query.list();
    }
    
    public List<Long>getLocIds(Long regId,Long userId){
        String sql = "select locality_id from locals_at_region where region_id=:regId and exists (select region_id from region where region_id=:regId and user_id=:userId)";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("regId", regId);
        query.setParameter("userId", userId);
        return query.list();
    }
    
}
