/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.User;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class UserDao extends Dao<User>  {
    
    @Override
    public Class getSupportedClass() {
        return User.class;
    }
    
    public User getUserByLogin(String login) {
        String queryString = "from User U where U.email = :email";
        Query query = getCurrentSession().createQuery(queryString);
        query.setParameter("email", login);
        return (User) query.uniqueResult();
    }


}
