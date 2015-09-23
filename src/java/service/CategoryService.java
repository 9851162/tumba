/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.CategoryDao;
import entities.Category;
import entities.Parametr;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        if(name!=null&&!name.equals("")&&parentId!=null){
            Category cat = new Category();
            cat.setName(name);
            cat.setParentId(parentId);
            //наследуем параметры
            Set<Parametr>params=new HashSet();
            if(!parentId.equals((long)0)){
                Category parent=catDao.find(parentId);
                if(parent.getParams()!=null){
                    params=parent.getParams();
                }
            }
            cat.setParams(params);
            
            if(validate(cat)){
                catDao.save(cat);
            }
        }else{
            if(name==null||name.equals("")){
                addError("Необходимо указать наименование категории");
            }
            if(parentId==null){
                addError("Ид родительской категории не указан");
            }
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
    
    public List<Category> getAllChildrenCatStack(Long catId,Set<Long>passedIds){
        if(passedIds==null){
            passedIds=new HashSet();
        }
        passedIds.add(catId);
        List<Category> cats = new ArrayList();
        if(catId!=null){
            List<Category>children = catDao.getUnderCats(catId);
            cats.addAll(children);
            for(Category c:children){
                Long chId = c.getId();
                if(!passedIds.contains(chId)&&getErrors().isEmpty()){
                    cats.addAll(getAllChildrenCatStack(chId,passedIds));
                }else{
                    addError("Возникла ошибка построения списка категорий, категория с ИД "+chId+" присутствует на разных уровнях иерархии категорий.");
                    break;
                }
            }
        }else{
            addError("Ид родительской категории не указан");
        }
        if(!cats.isEmpty()){
            int i = cats.size()-1;
            List<Category>supList = new ArrayList();
            while(i>=0){
                supList.add(cats.get(i--));
            }
            cats=supList;
        }
        return cats;
    }
    
}
