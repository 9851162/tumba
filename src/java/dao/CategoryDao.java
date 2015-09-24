/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Category;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class CategoryDao extends Dao<Category>  {
    
    @Override
    public Class getSupportedClass() {
        return Category.class;
    }
    
    public List<Category> getUnderCats(Long parentId){
        String hql = "from Category c where c.parentId=:parentId order by c.name";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("parentId", parentId);
        return query.list();
    }
    
    public List<Category> getAllUnderCats(Long parentId){
        String hql = "from Category c where c.idPath like :parentId order by c.nestingLevel";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("parentId", "%_"+parentId+"_%");
        return query.list();
    }
    
}
