/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Locality;
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
    
}
