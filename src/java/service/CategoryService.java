/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.CategoryDao;
import entities.Category;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.parent.PrimService;

/**
 *
 * @author bezdatiuzer
 */
@Service
@Transactional
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CategoryService extends PrimService {
    
    @Autowired
    CategoryDao catDao;
    
    public void create(Long parentId,String name){
        if(name!=null&&!name.equals("")){
            Category cat = new Category();
            cat.setName(name);
            cat.setParentId(parentId);
            if(validate(cat)){
                catDao.save(cat);
            }
        }else{
            addError("Необходимо указать наименование категории");
        }
    }
    
    public List<Category> getUnderCats(Long parentId){
        List<Category> cats = new ArrayList();
        if(parentId!=null){
            cats=catDao.getUnderCats(parentId);
        }else{
            addError("Ид родительской категории не указан");
        }
        return cats;
    }
    
    public void delete(Long categoryId){
        
    }
    
}
