/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Category;
import entities.Parametr;
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
    
    public List<String> getUnderCatNames(Long parentId){
        String sql = "select name from category where parent_id=:parentId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("parentId", parentId);
        return query.list();
    }
    
    public List<Category> getAllUnderCats(Long parentId){
        String hql = "from Category c where c.idPath like :parentId order by c.nestingLevel";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("parentId", "%_"+parentId+"_%");
        return query.list();
    }
    
    public List<String> getUnavailableNames(Long catId){
        String sql = "select p.name from params_in_categories pic left join parametr p on pic.parametr_id=p.parametr_id where pic.category_id=:catId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("catId", catId);
        return query.list();
    }
    
    public List<Long>getRequiredParamsIds(Long catId){
        String sql = "select p.parametr_id from params_in_categories pic left join parametr p on pic.parametr_id=p.parametr_id where pic.category_id=:catId and p.req_type=:req and p.param_type!=:bool";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("catId", catId);
        query.setParameter("req", Parametr.REQUIRED);
        query.setParameter("bool", Parametr.BOOL);
        return query.list();
    }
    
}
