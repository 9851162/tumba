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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
    
    public void create(Long parentId,String name) throws Exception{
        List<String>unavailableNames = catDao.getUnderCatNames(parentId);
        if(name!=null&&!name.equals("")&&!unavailableNames.contains(name)&&parentId!=null){
            Category cat = new Category();
            String idPath="";
            cat.setName(name);
            cat.setParentId(parentId);
            //наследуем параметры и путь
            Set<Parametr>params=new HashSet();
            if(!parentId.equals(Category.BASEID)){
                Category parent=catDao.find(parentId);
                if(parent.getParams()!=null){
                    params=new HashSet(parent.getParams());
                }
                idPath=parent.getIdPath().substring(0, parent.getIdPath().length()-1);
            }
            cat.setParams(params);
            
            idPath+="_"+parentId+"_";
            Integer nestingLevel = idPath.split("_").length-1;
            cat.setIdPath(idPath);
            cat.setNestingLevel(nestingLevel);
            if(validate(cat)){
                catDao.save(cat);
            }
        }else{
            if(name==null||name.equals("")){
                addError("Необходимо указать наименование категории, отличное от уже существующих в данном каталоге");
            }
            if(parentId==null){
                addError("Ид родительской категории не указан");
            }
        }
    }
    
    /*public List<Category> getUnderCats(Long parentId){
        List<Category> cats = new ArrayList();
        if(parentId!=null){
            cats=catDao.getUnderCats(parentId);
        }else{
            addError("Ид родительской категории не указан");
        }
        return cats;
    }*/
    
    public void delete(Long categoryId){
        if(categoryId!=null){
            if(!categoryId.equals(Category.BASEID)){
                Category cat = catDao.find(categoryId);
                List<Category>underCats = catDao.getAllUnderCats(categoryId);
                for(Category underCat:underCats){
                    underCat.setParams(new HashSet());
                    catDao.update(underCat);
                    catDao.delete(underCat);
                }
                cat.setParams(new HashSet());
                catDao.update(cat);
                catDao.delete(cat);
            }else{
                addError("Базовая категория с ИД 0 не может быть удалена");
            }
        }else{
            addError("ИД категории не указан");
        }
    }
    
    public HashMap<Long,List<Category>> getFullCatMap(){
        HashMap<Long,List<Category>>result = new HashMap();
        List<Category>allCats=catDao.getAll();
        for(Category cat:allCats){
            List<Category> supList = result.get(cat.getParentId());
            if(supList==null){
                supList = new ArrayList();
            }
            supList.add(cat);
            result.put(cat.getParentId(), supList);
        }
        if(allCats.isEmpty()){
            result.put(Category.BASEID,new ArrayList());
        }
        //сортировка по именам
        for(Long id:result.keySet()){
            List<Category>supList=result.get(id);
            Collections.sort(supList,new nameComparator());
        }
        return result;
    }
    
    public HashMap<Long,List<Long>> getFullCatIdMap(){
        HashMap<Long,List<Long>>result = new HashMap();
        List<Category>allCats=catDao.getAll();
        for(Category cat:allCats){
            List<Long> supList = result.get(cat.getParentId());
            if(supList==null){
                supList = new ArrayList();
            }
            supList.add(cat.getId());
            result.put(cat.getParentId(), supList);
        }
        if(allCats.isEmpty()){
            result.put(Category.BASEID,new ArrayList());
        }
        //сортировка по именам
        /*for(Long id:result.keySet()){
            List<Long>supList=result.get(id);
            Collections.sort(supList,new nameComparator());
        }*/
        return result;
    }
            
    private class nameComparator implements Comparator<Category> {
        @Override
        public int compare(Category a, Category b) {
                return a.getName().compareTo(b.getName());
        }
    }
    
    /*public List<Category> getAllChildrenCatStack(Long catId,Set<Long>passedIds){
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
                    addError("Возникла ошибка построения списка категорий, категория с ИД "+chId+" присутствует на разных уровнях иерархии.");
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
    }*/
    
}
