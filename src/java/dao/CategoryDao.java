/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Category;
import entities.ParamCategoryLink;
import entities.Parametr;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class CategoryDao extends Dao<Category> {

    @Override
    public Class getSupportedClass() {
        return Category.class;
    }

    public List<Category> getUnderCats(Long parentId) {
        String hql = "from Category c where c.parentId=:parentId order by c.name";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("parentId", parentId);
        return query.list();
    }

    public List<String> getUnderCatNames(Long parentId) {
        String sql = "select name from category where parent_id=:parentId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("parentId", parentId);
        return query.list();
    }

    public List<Category> getAllUnderCats(Long parentId) {
        String hql = "from Category c where c.idPath like :parentId order by c.nestingLevel";
        Query query = getCurrentSession().createQuery(hql);
        query.setParameter("parentId", "%_" + parentId + "_%");
        return query.list();
    }

    public List<String> getUnavailableNames(Long catId) {
        String sql = "select p.name from param_category_link pic left join parametr p on pic.parametr_id=p.parametr_id where pic.category_id=:catId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("catId", catId);
        return query.list();
    }

    public boolean isAddebleParam(Long paramId, Long catId) {
        boolean res = true;
        String sql = "select p_c_link_id from param_category_link where parametr_id=:paramId and category_id=:catId";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("catId", catId);
        query.setParameter("paramId", paramId);
        List<Object> list = query.list();
        if (list.size() > 0) {
            res = false;
        }
        return res;
    }

    /* public List<Long>getRequiredParamsIds(Long catId){
     String sql = "select p.parametr_id from param_category_link pic left join parametr p on pic.parametr_id=p.parametr_id where pic.category_id=:catId and p.req_type=:req and p.param_type!=:bool";
     Query query = getCurrentSession().createSQLQuery(sql);
     query.setParameter("catId", catId);
     query.setParameter("req", Parametr.REQUIRED);
     query.setParameter("bool", Parametr.BOOL);
     List<BigInteger>rawRes=query.list();
     ArrayList<Long>res=new ArrayList();
     for(BigInteger id:rawRes){
     res.add(id.longValue());
     }
     return res;
     }*/
    public List<Long> getRequiredParamsIds(Long catId) {
        String sql = "select p.parametr_id from param_category_link l left join parametr p on l.parametr_id=p.parametr_id where l.category_id=:catId and l.req_type=:req";
        Query query = getCurrentSession().createSQLQuery(sql);
        query.setParameter("catId", catId);
        query.setParameter("req", ParamCategoryLink.REQUIRED);
        //query.setParameter("bool", Parametr.BOOL);
        List<BigInteger> rawRes = query.list();
        ArrayList<Long> res = new ArrayList();
        for (BigInteger id : rawRes) {
            res.add(id.longValue());
        }
        return res;
    }

    public List<Category> getSelectedCats(List<Long> catIds) {
        if (catIds != null && !catIds.isEmpty()) {
            String hql = "from Category where id in (:catIds)";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameterList("catIds", catIds);
            return query.list();
        } else {
            return new ArrayList();
        }
    }

    public List<Category> getNotSelectedCats(List<Long> catIds) {
        if (catIds != null && !catIds.isEmpty()) {
            String hql = "from Category where id not in (:catIds)";
            Query query = getCurrentSession().createQuery(hql);
            query.setParameterList("catIds", catIds);
            return query.list();
        } else {
            return getAll();
        }
    }

}
