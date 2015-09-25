/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.CategoryDao;
import dao.ParametrDao;
import entities.Category;
import entities.Parametr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
    @Autowired
    ParametrDao paramDao;
    
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
    
    public String getCatName(Long catId){
        if(catId==null){
            return "Не выбрана";
        }else{
            return catDao.find(catId).getName();
        }
    }
    
    public List<Parametr>getParams(Long catId){
        List<Parametr>params=new ArrayList();
        if(catId!=null){
            Category cat = catDao.find(catId);
            params.addAll(cat.getParams());
            Collections.sort(params,new paramComparator());
        }
        return params;
    }
    
    private class paramComparator implements Comparator<Parametr> {
        @Override
        public int compare(Parametr a, Parametr b) {
                return a.getName().compareTo(b.getName());
        }
    }
    
    public void addParam(Long catId,String name,Integer reqType,Integer paramType) throws Exception{
        /*if(catId!=null&&name!=null&&!name.equals("")&&reqType!=null&&paramType!=null){
            
        }else{
            if(catId)
        }*/
        if(catId!=null){
            Category c = catDao.find(catId);
            List<String>unavailableNames=catDao.getUnavailableNames(catId);
            if(!unavailableNames.contains(name)){
                Parametr p = new Parametr();
                p.setName(name);
                p.setParamType(paramType);
                p.setReqType(reqType);
                if(validate(p)){
                    paramDao.save(p);
                    Set<Parametr> pset = c.getParams();
                    pset.add(p);
                    c.setParams(pset);
                    if(validate(c)){
                        //throw new Exception("id="+c.getId()+"; p="+c.getIdPath()+"; size="+c.getParams().size()+"; name="+c.getName()+"; ");
                        catDao.update(c);
                    }
                }else{
                    addError("qwe");
                }
            }else{
                addError("Параметр с таким наименованием уже присутствует в данной категории, выберите другое");
            }
        }
    }
    
    public LinkedHashMap<Integer,String>getParamTypes(){
        LinkedHashMap<Integer,String>res = new LinkedHashMap();
        res.put(Parametr.TEXT,"текст");
        res.put(Parametr.NUM,"число");
        res.put(Parametr.DATE,"дата");
        res.put(Parametr.YESNO,"да/нет");
        res.put(Parametr.SELECTING,"выбор");
        res.put(Parametr.MULTISELECTING,"множественный выбор");
        return res;
    }
    
    public LinkedHashMap<Integer,String>getReqTypes(){
        LinkedHashMap<Integer,String>res = new LinkedHashMap();
        res.put(Parametr.REQUIRED,"обязательный");
        res.put(Parametr.NOTREQUIRED,"необязательный");
        return res;
    }
    
    
    
    /*public void addParamOption(Long catId,String name,Integer reqType,Integer paramType){
        
        if(catId!=null){
            Category c = catDao.find(catId);
            Parametr p = new Parametr();
            p.setName(name);
            p.setParamType(paramType);
            p.setReqType(reqType);
            if(validate(p)){
                paramDao.save(p);
                Set<Parametr> pset = c.getParams();
                pset.add(p);
                c.setParams(pset);
                if(validate(c)){
                    catDao.update(c);
                }
            }
        }
    }*/
    
}
