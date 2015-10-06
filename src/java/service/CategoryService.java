/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.CategoryDao;
import dao.ParametrDao;
import dao.ParametrSelOptionDao;
import dao.ParametrValueDao;
import entities.Category;
import entities.Parametr;
import entities.ParametrSelOption;
import java.math.BigInteger;
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
    @Autowired
    ParametrValueDao paramValueDao;
    @Autowired
    ParametrSelOptionDao optionDao;

    public void create(Long parentId, String name) throws Exception {
        List<String> unavailableNames = catDao.getUnderCatNames(parentId);
        if (name != null && !name.equals("") && !unavailableNames.contains(name) && parentId != null) {
            Category cat = new Category();
            String idPath = "";
            cat.setName(name);
            cat.setParentId(parentId);
            //наследуем параметры и путь
            Set<Parametr> params = new HashSet();
            if (!parentId.equals(Category.BASEID)) {
                Category parent = catDao.find(parentId);
                if (parent.getParams() != null) {
                    params = new HashSet(parent.getParams());
                }
                idPath = parent.getIdPath().substring(0, parent.getIdPath().length() - 1);
            }
            cat.setParams(params);

            idPath += "_" + parentId + "_";
            Integer nestingLevel = idPath.split("_").length - 1;
            cat.setIdPath(idPath);
            cat.setNestingLevel(nestingLevel);
            if (validate(cat)) {
                catDao.save(cat);
            }
        } else {
            if (name == null || name.equals("")) {
                addError("Необходимо указать наименование категории, отличное от уже существующих в данном каталоге");
            }
            if (parentId == null) {
                addError("Ид родительской категории не указан");
            }
        }
    }

    public void delete(Long categoryId) {
        if (categoryId != null) {
            if (!categoryId.equals(Category.BASEID)) {
                Category cat = catDao.find(categoryId);
                List<Category> underCats = catDao.getAllUnderCats(categoryId);
                for (Category underCat : underCats) {
                    underCat.setParams(new HashSet());
                    catDao.update(underCat);
                    catDao.delete(underCat);
                }
                cat.setParams(new HashSet());
                catDao.update(cat);
                catDao.delete(cat);
            } else {
                addError("Базовая категория с ИД 0 не может быть удалена");
            }
        } else {
            addError("ИД категории не указан");
        }
    }

    //map отражающая вложенности категорий <parentId,children>
    public HashMap<Long, List<Category>> getNestingMapOfCats() {
        HashMap<Long, List<Category>> result = new HashMap();
        List<Category> allCats = catDao.getAll();
        for (Category cat : allCats) {
            List<Category> supList = result.get(cat.getParentId());
            if (supList == null) {
                supList = new ArrayList();
            }
            supList.add(cat);
            result.put(cat.getParentId(), supList);
        }
        if (allCats.isEmpty()) {
            result.put(Category.BASEID, new ArrayList());
        }
        //сортировка по именам
        for (Long id : result.keySet()) {
            List<Category> supList = result.get(id);
            Collections.sort(supList, new nameComparator());
        }
        return result;
    }

    /*public HashMap<Long, List<Long>> getFullCatIdMap() {
        HashMap<Long, List<Long>> result = new HashMap();
        List<Category> allCats = catDao.getAll();
        for (Category cat : allCats) {
            List<Long> supList = result.get(cat.getParentId());
            if (supList == null) {
                supList = new ArrayList();
            }
            supList.add(cat.getId());
            result.put(cat.getParentId(), supList);
        }
        if (allCats.isEmpty()) {
            result.put(Category.BASEID, new ArrayList());
        }
        return result;
    }*/

    //????
    public List<Category> getCatList() {
        List<Category> res = new ArrayList();
        HashMap<Long, List<Category>> catMap = getNestingMapOfCats();
        Long i = (long) 0;
        if (!catMap.isEmpty() && catMap.get(i) != null) {
            res.addAll(getCatsWithRecursion(i, catMap));
        }
        return res;
    }

    //sup метод для выстраивания листа категорий рекурсией
    private List<Category> getCatsWithRecursion(Long i, HashMap<Long, List<Category>> catMap) {
        List<Category> res = new ArrayList();
        for (Category c : catMap.get(i)) {
            res.add(c);
            if (catMap.get(c.getId()) != null) {
                res.addAll(getCatsWithRecursion(c.getId(), catMap));
            }
        }
        return res;
    }

    private class nameComparator implements Comparator<Category> {

        @Override
        public int compare(Category a, Category b) {
            return a.getName().compareTo(b.getName());
        }
    }
    
    //Мап со всеми категориями <id,cat>
    public HashMap<Long,Category>getCatMap(){
        HashMap<Long,Category>res=new HashMap();
        for(Category c:catDao.getAll()){
            res.put(c.getId(), c);
        }
        return res;
    }

    //??? не нужен?
    public String getCatName(Long catId) {
        if (catId == null) {
            return "Не выбрана";
        } else {
            return catDao.find(catId).getName();
        }
    }

    public List<Parametr> getParams(Long catId) {
        List<Parametr> params = new ArrayList();
        if (catId != null) {
            Category cat = catDao.find(catId);
            params.addAll(cat.getParams());
            Collections.sort(params, new paramComparator());
        }
        return params;
    }

    private class paramComparator implements Comparator<Parametr> {

        @Override
        public int compare(Parametr a, Parametr b) {
            return a.getName().compareTo(b.getName());
        }
    }

    public void createParam(String name, Integer reqType, Integer paramType) throws Exception {
        Parametr p = new Parametr();
        p.setName(name);
        p.setParamType(paramType);
        p.setReqType(reqType);
        if (validate(p)) {
            paramDao.save(p);
        }
    }

    public void addParam(Long catId, Long paramId) throws Exception {
        if (paramId != null) {
            if (catId != null) {
                Category c = catDao.find(catId);
                Parametr p = paramDao.find(paramId);
                Set<Parametr> supSet = c.getParams();
                supSet.add(p);
                c.setParams(supSet);
                if (validate(c)) {
                    catDao.update(c);
                }
            } else {
                addError("Категория не указана");
            }
        }
    }

    public LinkedHashMap<Integer, String> getParamTypes() {
        LinkedHashMap<Integer, String> res = new LinkedHashMap();
        res.put(Parametr.TEXT, "текст");
        res.put(Parametr.NUM, "число");
        res.put(Parametr.DATE, "дата");
        res.put(Parametr.BOOL, "да/нет");
        res.put(Parametr.SELECTING, "выбор");
        res.put(Parametr.MULTISELECTING, "множ. выбор");
        return res;
    }

    public LinkedHashMap<Integer, String> getReqTypes() {
        LinkedHashMap<Integer, String> res = new LinkedHashMap();
        res.put(Parametr.REQUIRED, "об.");
        res.put(Parametr.NOTREQUIRED, "необ.");
        return res;
    }

    //to do delete from sql iz table??
    public void deleteParamFromCat(Long paramId, Long catId) throws Exception {
        if (catId != null && paramId != null) {
            Category c = catDao.find(catId);
            Set<Parametr> params = c.getParams();
            Set<Parametr> supSet = new HashSet();
            for (Parametr p : params) {
                if (!p.getId().equals(paramId)) {
                    supSet.add(p);
                }
            }
            c.setParams(supSet);
            catDao.update(c);
            //Parametr p = paramDao.find(paramId);

            //to do it doesn't work!
            /*Boolean check = paramDao.hasCats(paramId);
            if (!check) {
                paramDao.delete(p);
            }*/
            //addError("res = " + check);
        } else {
            if (catId == null) {
                addError("Ид категории не передан");
            }
            if (paramId == null) {
                addError("Ид параметра не передан");
            }
        }
    }

    public List<Parametr> getAllParams() {
        return paramDao.getAllParams();
    }
    
    public HashMap<Long,Parametr>getParamsMap(){
        HashMap<Long,Parametr>res=new HashMap();
        List<Parametr>params=paramDao.getAllParams();
        for(Parametr p:params){
            res.put(p.getId(),p);
        }
        return res;
    }
    
    public void deleteParam(Long paramId){
        if(paramId!=null){
            Parametr p = paramDao.find(paramId);
            addMessage("Параметр удален, также были удалены связи параметра с категориями в количестве: "+paramDao.deleteFromCats(paramId)+";");
            paramValueDao.deleteParamValues(paramId);
            paramDao.delete(p);
        }else{
            addError("Параметр не указан");
        }
    }
    
    public HashMap<Long,List<Parametr>>getCatIdParamsMap(){
        HashMap<Long,List<Parametr>>res = new HashMap();
        HashMap<Long,Parametr>paramMap=getParamsMap();
        for(Category c:getCatList()){
            res.put(c.getId(), new ArrayList());
        }
        for(Object[] o:paramDao.getCatsParamsAsObjects()){
            Long catId = ((BigInteger)o[0]).longValue();
            Long paramId = ((BigInteger)o[1]).longValue();
            Parametr p = paramMap.get(paramId);
            List<Parametr> params = res.get(catId);
            if(params==null){
                params=new ArrayList();
            }
            params.add(p);
            res.put(catId, params);
        }
        return res;
    }
    
    public void addParamOption(Long paramId,String optName){
        Parametr p = paramDao.find(paramId);
        if(p.getParamType().equals(Parametr.SELECTING)||p.getParamType().equals(Parametr.MULTISELECTING)){
            if(!paramDao.getUnavailableOptionNames(paramId).contains(optName)){
                ParametrSelOption opt = new ParametrSelOption();
                opt.setName(optName);
                opt.setParametr(p);
                if(validate(opt)){
                    optionDao.save(opt);
                }
            }else{
                addError("Опция с таким наименованием уже есть у данного параметра");
            }
        }
    }
    
    public void deleteParamOption(Long paramOptionId){
        optionDao.delete(optionDao.find(paramOptionId));
    }
    
    /*public ParametrValue setValue(Parametr p,Object val){
        
    }*/
    
}
