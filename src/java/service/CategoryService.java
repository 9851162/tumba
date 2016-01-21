/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.CategoryDao;
import dao.ParamCategoryLinkDao;
import dao.ParametrDao;
import dao.ParametrSelOptionDao;
import dao.ParametrValueDao;
import entities.Ad;
import entities.Category;
import entities.ParamCategoryLink;
import entities.Parametr;
import entities.ParametrSelOption;
import entities.ParametrValue;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
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
    @Autowired
    ParamCategoryLinkDao linkDao;

    public void create(Long parentId, String name) throws Exception {
        List<String> unavailableNames = catDao.getUnderCatNames(parentId);
        if (name != null && !name.equals("") && !unavailableNames.contains(name) && parentId != null) {
            Category cat = new Category();
            String idPath = "";
            cat.setName(name);
            cat.setParentId(parentId);
            //наследуем параметры и путь
            //List<Parametr> params = new ArrayList();
            Set<ParamCategoryLink> paramLinks = new HashSet();
            if (!parentId.equals(Category.BASEID)) {
                Category parent = catDao.find(parentId);
                if (parent.getParamLinks() != null) {
                    paramLinks = new HashSet(parent.getParamLinks());
                    //params=paramDao.getParamsFromCat(parentId);
                }
                idPath = parent.getIdPath().substring(0, parent.getIdPath().length() - 1);
            }
            //cat.setParamLinks(params);

            idPath += "_" + parentId + "_";
            Integer nestingLevel = idPath.split("_").length - 1;
            cat.setIdPath(idPath);
            cat.setNestingLevel(nestingLevel);
            if (validate(cat)) {
                catDao.save(cat);
                for (ParamCategoryLink l : paramLinks) {
                    ParamCategoryLink link = new ParamCategoryLink();
                    link.setCat(cat);
                    link.setParam(l.getParam());
                    if (l.isReq()) {
                        link.setReq();
                    } else {
                        link.setNotReq();
                    }
                    if (validate(link)) {
                        linkDao.save(link);
                    }
                }
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
                    underCat.setParamLinks(new HashSet());
                    catDao.update(underCat);
                    catDao.delete(underCat);
                }
                cat.setParamLinks(new HashSet());
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
    public HashMap<Long, Category> getCatMap() {
        HashMap<Long, Category> res = new HashMap();
        for (Category c : catDao.getAll()) {
            res.put(c.getId(), c);
        }
        return res;
    }

    //??? не нужен может?
    public String getCatName(Long catId) {
        if (catId == null) {
            return "Не выбрана";
        } else {
            return catDao.find(catId).getName();
        }
    }

    /*public List<Parametr> getParams(Long catId) {
     List<Parametr> params = new ArrayList();
     if (catId != null) {
     Category cat = catDao.find(catId);
     for(ParamCategoryLink link:cat.getParamLinks()){
     params.add(link.getParam());
     }
     //params.addAll(cat.getParams());
     Collections.sort(params, new paramComparator());
     }
     return params;
     }
    
     private class paramComparator implements Comparator<Parametr> {

     @Override
     public int compare(Parametr a, Parametr b) {
     return a.getName().compareTo(b.getName());
     }
     }*/
    public List<ParamCategoryLink> getParamLinks(Long catId) {
        List<ParamCategoryLink> res = new ArrayList();
        if (catId != null) {
            Category cat = catDao.find(catId);
            for (ParamCategoryLink link : cat.getParamLinks()) {
                res.add(link);
            }
            //params.addAll(cat.getParams());
            Collections.sort(res, new linkComparator());
        }
        return res;
    }

    private class linkComparator implements Comparator<ParamCategoryLink> {

        @Override
        public int compare(ParamCategoryLink a, ParamCategoryLink b) {
            return a.getParam().getName().compareTo(b.getParam().getName());
        }
    }

    public void createParam(String name, Integer paramType) throws Exception {
        Parametr p = new Parametr();
        p.setName(name);
        p.setParamType(paramType);
        if (validate(p)) {
            paramDao.save(p);
        }
    }

    public void addParam(Long catId, String req, Long paramId) throws Exception {
        if (paramId != null) {
            if (catId != null) {
                Category c = catDao.find(catId);
                if (catDao.isAddebleParam(paramId, catId)) {
                    Parametr p = paramDao.find(paramId);
                    ParamCategoryLink link = new ParamCategoryLink();
                    link.setParam(p);
                    link.setCat(c);
                    if (req != null) {
                        link.setReq();
                    } else {
                        link.setNotReq();
                    }
                    if (validate(link)) {
                        linkDao.save(link);
                    }
                }
            } else {
                addError("Категория не указана");
            }
        } else {
            addError("Параметр не указан");
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
        res.put(ParamCategoryLink.REQUIRED, "об.");
        res.put(ParamCategoryLink.NOTREQUIRED, "необ.");
        return res;
    }

    public void deleteParamFromCat(Long paramId, Long catId) throws Exception {
        if (catId != null && paramId != null) {
            linkDao.delete(paramId, catId);
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

    public HashMap<Long, Parametr> getParamsMap() {
        HashMap<Long, Parametr> res = new HashMap();
        List<Parametr> params = paramDao.getAllParams();
        for (Parametr p : params) {
            res.put(p.getId(), p);
        }
        return res;
    }

    public void deleteParam(Long paramId) {
        if (paramId != null) {
            Parametr p = paramDao.find(paramId);
            addMessage("Параметр удален, также были удалены связи параметра с категориями в количестве: " + paramDao.deleteFromCats(paramId) + ";");
            paramValueDao.deleteParamValues(paramId);
            paramDao.delete(p);
        } else {
            addError("Параметр не указан");
        }
    }

    public void addParamOption(Long paramId, String optName) {
        Parametr p = paramDao.find(paramId);
        if (p.getParamType().equals(Parametr.SELECTING) || p.getParamType().equals(Parametr.MULTISELECTING)) {
            if (!paramDao.getUnavailableOptionNames(paramId).contains(optName)) {
                ParametrSelOption opt = new ParametrSelOption();
                opt.setName(optName);
                opt.setParametr(p);
                if (validate(opt)) {
                    optionDao.save(opt);
                }
            } else {
                addError("Опция с таким наименованием уже есть у данного параметра");
            }
        }
    }

    public void deleteParamOption(Long paramOptionId) {
        optionDao.delete(optionDao.find(paramOptionId));
    }

    public LinkedHashMap<Parametr, String[]> getSortedParamsAndValsForComparison(List<Ad> ads) {
        LinkedHashMap<Parametr, String[]> res = new LinkedHashMap();
        HashMap<Long, Integer> countMap = new HashMap();
        HashMap<Long, String[]> valMap = new HashMap();
        if (ads != null && !ads.isEmpty()) {
            int i = 0;
            for (Ad ad : ads) {
                Set<Long> paramIds = new HashSet();
                for (ParametrValue pv : ad.getValues()) {
                    Parametr p = pv.getParametr();
                    Integer pt = p.getParamType();
                    Long pid = p.getId();

                    String[] o = valMap.get(pid);
                    if (o == null) {
                        int m = ads.size();
                        o = new String[m];
                        for (int n = 0; n < m; n++) {
                            o[n] = "";
                        }
                    }

                    if (Objects.equals(Parametr.MULTISELECTING, pt)) {
                        o[i] += pv.getStringVal() + "; ";
                    } else {
                        o[i] = pv.getStringVal();
                    }
                    valMap.put(pid, o);
                    paramIds.add(pid);
                }
                for (Long pid : paramIds) {
                    Integer count = countMap.get(pid);
                    if (count == null) {
                        count = 0;
                    }
                    countMap.put(pid, count + 1);
                }
                i++;
            }

            List<Object[]> supList4Sort = new ArrayList();
            for (Long pid : countMap.keySet()) {
                Object[] idCount = new Object[2];
                idCount[0] = pid;
                idCount[1] = countMap.get(pid);
                supList4Sort.add(idCount);
            }
            Collections.sort(supList4Sort, new countComparator());

            for (Object[] o : supList4Sort) {
                Long pid = (Long) o[0];
                res.put(paramDao.find(pid), valMap.get(pid));
            }

        }
        return res;
    }

    private class countComparator implements Comparator<Object[]> {

        @Override
        public int compare(Object[] a, Object[] b) {
            return (Integer) b[1] - (Integer) a[1];
        }
    }

    public List<Category> getSelectedCats(List<Long> catIds) {
        return catDao.getSelectedCats(catIds);
    }

    public List<Category> getNotSelectedCats(List<Long> catIds) {
        //return catDao.getNotSelectedCats(catIds);
        Set<Long>set = new HashSet();
        List<Category> res = new ArrayList();
        HashMap<Long, List<Category>> catMap = getNestingMapOfCats();
        Long i = (long) 0;
        if (!catMap.isEmpty() && catMap.get(i) != null) {
            res.addAll(getCatsWithRecursionAndExceptions(i, catMap,catIds));
        }
        return res;
    }
    
    private List<Category> getCatsWithRecursionAndExceptions(Long i, HashMap<Long, List<Category>> catMap,List<Long> catIds) {
        List<Category> res = new ArrayList();
        for (Category c : catMap.get(i)) {
            Long id = c.getId();
            if(!catIds.contains(id)){
                res.add(c);
            }
            if (catMap.get(id) != null) {
                res.addAll(getCatsWithRecursionAndExceptions(id, catMap,catIds));
            }
        }
        return res;
    }

    public List<Parametr> getMutualParams(List<Long> catIds) {
        List<Parametr> res = new ArrayList();
        if (catIds != null && !catIds.isEmpty()) {
            //тип цена
            res.add(new Parametr());
            List<Object[]> preRes = paramDao.getParamIdsAndCountsByCatIds(catIds);
            Integer neededCount = catIds.size();
            for (Object[] o : preRes) {
                Long paramId = ((BigInteger) o[0]).longValue();
                Integer count = ((BigInteger) o[1]).intValue();
                if (Objects.equals(count, neededCount)) {
                    res.add(paramDao.find(paramId));
                } else {
                    break;
                }
            }
        }
        return res;
    }

    public HashMap<Long, List<Parametr>> getCatIdParamsMap() {
        HashMap<Long, List<Parametr>> res = new HashMap();
        HashMap<Long, Parametr> paramMap = getParamsMap();
        for (Category c : getCatList()) {
            res.put(c.getId(), new ArrayList());
        }
        for (Object[] o : paramDao.getCatsParamsAsObjects()) {
            Long catId = ((BigInteger) o[0]).longValue();
            Long paramId = ((BigInteger) o[1]).longValue();
            Parametr p = paramMap.get(paramId);
            List<Parametr> params = res.get(catId);
            if (params == null) {
                params = new ArrayList();
            }
            params.add(p);
            res.put(catId, params);
        }
        return res;
    }
    
    public List<HashMap<String,Object>> getParamsForDraw(Long catId){
        List<HashMap<String,Object>>res=new ArrayList();
        
        List<Object[]>rawParamsAndNeeds=paramDao.getParamsAndNeedsFromCat(catId);
        List<HashMap<String,Object>>reqBool=new ArrayList();
        List<HashMap<String,Object>>bool=new ArrayList();
        List<HashMap<String,Object>>reqStr=new ArrayList();
        List<HashMap<String,Object>>str=new ArrayList();
        List<HashMap<String,Object>>reqNum=new ArrayList();
        List<HashMap<String,Object>>num=new ArrayList();
        List<HashMap<String,Object>>reqDate=new ArrayList();
        List<HashMap<String,Object>>date=new ArrayList();
        List<HashMap<String,Object>>reqSel=new ArrayList();
        List<HashMap<String,Object>>sel=new ArrayList();
        List<HashMap<String,Object>>reqMsel=new ArrayList();
        List<HashMap<String,Object>>msel=new ArrayList();
        for(Object[] o:rawParamsAndNeeds){
            Parametr p = (Parametr)o[0];
            Integer req = (Integer)o[1];
            
            HashMap<String,Object>param=new HashMap();
            param.put("name", p.getName());
            param.put("id", p.getId());
            param.put("req", req);
            param.put("type", p.getParamType());
            
            if(Objects.equals(p.getParamType(), Parametr.BOOL)){
                if(1==req){
                    reqBool.add(param);
                }else{
                    bool.add(param);
                }
            }else if(Objects.equals(p.getParamType(), Parametr.TEXT)){
                if(1==req){
                    reqStr.add(param);
                }else{
                    str.add(param);
                }
            }else if(Objects.equals(p.getParamType(), Parametr.NUM)){
                if(1==req){
                    reqNum.add(param);
                }else{
                    num.add(param);
                }
            }else if(Objects.equals(p.getParamType(), Parametr.DATE)){
                if(1==req){
                    reqDate.add(param);
                }else{
                    date.add(param);
                }
            }else if(Objects.equals(p.getParamType(), Parametr.SELECTING)){
                List<ParametrSelOption>opts=p.getOptions();
                if(!opts.isEmpty()){
                    LinkedHashMap<Long,String>optMap=new LinkedHashMap();
                    for(ParametrSelOption opt:opts){
                        optMap.put(opt.getId(), opt.getName());
                    }
                    param.put("options",optMap);
                    if(1==req){
                        reqSel.add(param);
                    }else{
                        sel.add(param);
                    }
                }
            }else if(Objects.equals(p.getParamType(), Parametr.MULTISELECTING)){
                List<ParametrSelOption>opts=p.getOptions();
                if(!opts.isEmpty()){
                    LinkedHashMap<Long,String>optMap=new LinkedHashMap();
                    for(ParametrSelOption opt:opts){
                        optMap.put(opt.getId(), opt.getName());
                    }
                    param.put("options",optMap);
                    if(1==req){
                        reqMsel.add(param);
                    }else{
                        msel.add(param);
                    }
                }
            }
        }
        res.addAll(reqBool);
        res.addAll(bool);
        res.addAll(reqStr);
        res.addAll(str);
        res.addAll(reqNum);
        res.addAll(num);
        res.addAll(reqDate);
        res.addAll(date);
        res.addAll(reqSel);
        res.addAll(sel);
        res.addAll(reqMsel);
        res.addAll(msel);
        return res;
    }

    /*public ParametrValue setValue(Parametr p,Object val){
        
     }*/
}
