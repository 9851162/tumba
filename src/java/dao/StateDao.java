/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.State;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class StateDao extends Dao<State>  {
    
    @Override
    public Class getSupportedClass() {
        return State.class;
    }
    
    public boolean isAvailableName(String name,Long countryId){
        String hql = "from State where name=:name and country.id=:countryId";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("name", name);
        query.setParameter("countryId", countryId);
        return query.list().isEmpty();
    }
    
}