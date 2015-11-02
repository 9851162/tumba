/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Country;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class CountryDao extends Dao<Country>  {
    
    @Override
    public Class getSupportedClass() {
        return Country.class;
    }
    
    public boolean isAvailableName(String name){
        String hql = "from Country where name=:name";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("name", name);
        return query.list().isEmpty();
    }
}
