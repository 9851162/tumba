/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Region;
import entities.User;
import java.util.List;
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
    
    public List<User>getUsersByNameOrMail(String keyWord){
        String hql = "from User u where u.email like :keyWord or u.name like :keyWord";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("keyWord", "%"+keyWord+"%");
        return query.list();
    }
    
    public Region getHomeRegion(Long userId){
        String hql = "from Region where user.id=:userId and homeRegion is not null";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("userId", userId);
        List<Region>res = query.list();
        if(!res.isEmpty()){
            return res.get(0);
        }
        return null;
    }


}
