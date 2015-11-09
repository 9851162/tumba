/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import dao.AdDao;
import entities.Ad;
import entities.Region;
import entities.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.AdService;
import service.CategoryService;
import service.RegionService;
import service.UserService;

/**
 *
 * @author bezdatiuzer
 */
@RequestMapping("/Main")
@Controller
public class mainController extends WebController {

    @Autowired
    private AdService adService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService catService;
    @Autowired
    private RegionService regionService;

    
    

    @RequestMapping("/")
    public String getMain(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        
        List<String> ers = (List) model.get(ERRORS_LIST_NAME);
        if (ers == null) {
            ers = new ArrayList();
        }
        
        Region region = (Region)request.getSession().getAttribute(MOUNTED_REGION_SESSION_NAME);
        List<Ad> compAds = (List)request.getSession().getAttribute(COMPARISON);
        List<Long>catIds = (List<Long>)request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
        if(catIds==null){
            catIds=new ArrayList();
        }
        if(compAds==null){
            compAds=new ArrayList();
        }
        
        User u = authManager.getCurrentUser();
        Long userId = null;
        if (u != null) {
            userId = u.getId();
            if(u.isHomeSet()){
                model.put("homeSet", u.getHomeSet());
            }
        }
        
        //установка региона, если нет еще
        if(region==null){
            region=regionService.getDefaultRegion(userId);
            request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, region);
        }
        
        /*ers.add("reg1:"+region.getName());
        
        ers.add("locIds:"+AdDao.getIdsAsString(AdDao.getLocIds(region)));*/
        
        HashMap<Long,Ad> chosenMap = adService.getChosenAdMap(userId);
        HashMap<Long,Ad> comparingMap = new HashMap();
        for(Ad ad:compAds){
            comparingMap.put(ad.getId(),ad);
        }
        List<Ad>ads=adService.getAds(wish,catIds,region);
        List<Ad> mySales = adService.getSales(userId);
        List<Ad> myPurchases = adService.getPurchases(userId);
        //List<Region> availableRegions = regionService.getAvailableRegions(region,u);
        
        //model.put("states",regionService.getNotEmptyStates());
        model.put("selectedCats",catService.getSelectedCats(catIds));
        model.put("notSelectedCats",catService.getNotSelectedCats(catIds));
        model.put("adList", ads);
        model.put("chosenAdsMap", chosenMap);
        model.put("comparingAdsMap", comparingMap);
        model.put("resCount", ads.size());
        model.put("chosenCount", chosenMap.size());
        model.put("compareCount", compAds.size());
        //model.put("availableRegions", availableRegions);
        
        //to do srazu polu4enie 4isla iz bazi
        model.put("mySellCount", mySales.size());
        model.put("myBuyCount", myPurchases.size());
        
        if (shortName != null) {
            model.put("shortName", shortName);
        }
        if (desc != null) {
            model.put("description", desc);
        }
        if (price != null) {
            model.put("price", price);
        }
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap", catService.getCatIdParamsMap());
        model.put("wish", wish);
        
        ers.addAll(adService.getErrors());
        ers.addAll(catService.getErrors());
        model.put(ERRORS_LIST_NAME, ers);
        return "main";
    }
    
    @RequestMapping("/addCat4Search")
    public String addCat4Search(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "wish", required = false) String wish,
            @RequestParam(value = "catId", required = false) Long catId,
            RedirectAttributes ras) throws Exception {
        
        List<Long>catList = (List<Long>)request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
        if(catList==null){
            catList=new ArrayList();
        }
        if(!catList.contains(catId)&&catList.size()<20){
            catList.add(catId);
        }
        request.getSession().setAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME, catList);
        ras.addAttribute("wish", wish);
        return "redirect:/Main/";
    }
    
    @RequestMapping("/removeCat4Search")
    public String removeCat4Search(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "wish", required = false) String wish,
            @RequestParam(value = "catId", required = false) Long catId,
            RedirectAttributes ras) throws Exception {
        
        List<Long>catList = (List<Long>)request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
        if(catList==null){
            catList=new ArrayList();
        }
        catList.remove(catId);
        request.getSession().setAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME, catList);
        ras.addAttribute("wish", wish);
        return "redirect:/Main/";
    }

    @RequestMapping("/chosen")
    public String getChosen(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            //@RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        Long userId = null;
        if (u != null) {
            userId = u.getId();
        }

        List<Ad> compAds = (List)request.getSession().getAttribute(COMPARISON);
        List<Long>catIds = (List<Long>)request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
        if(catIds==null){
            catIds=new ArrayList();
        }
        if(compAds==null){
            compAds=new ArrayList();
        }
        
        HashMap<Long,Ad> chosenMap = adService.getChosenAdMap(userId);
        List<Ad> mySales = adService.getSales(userId);
        List<Ad> myPurchases = adService.getPurchases(userId);
        
        model.put("adList", adService.getChosenAds(userId));
        model.put("chosenAdsMap", chosenMap);
        model.put("shortName", shortName);
        model.put("description", desc);
        model.put("price", price);
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap", catService.getCatIdParamsMap());
        
        model.put("selectedCats",catService.getSelectedCats(catIds));
        model.put("notSelectedCats",catService.getNotSelectedCats(catIds));
        model.put("resCount", chosenMap.size());
        model.put("chosenCount", chosenMap.size());
        model.put("compareCount", compAds.size());
        
        model.put("mySellCount", mySales.size());
        model.put("myBuyCount", myPurchases.size());
        
        ArrayList<String> ers = (ArrayList<String>) model.get("errors");
        if (ers == null) {
            ers = new ArrayList();
        }
        ers.addAll(adService.getErrors());
        ers.addAll(catService.getErrors());
        //ers.add("err");
        model.put(ERRORS_LIST_NAME, ers);
        return "main";
    }

    @RequestMapping("/sales")
    public String getSales(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        Long userId = null;
        if (u != null) {
            userId = u.getId();
        }

        List<Ad> compAds = (List)request.getSession().getAttribute(COMPARISON);
        List<Long>catIds = (List<Long>)request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
        if(catIds==null){
            catIds=new ArrayList();
        }
        if(compAds==null){
            compAds=new ArrayList();
        }
        
        HashMap<Long,Ad> chosenMap = adService.getChosenAdMap(userId);
        
        List<Ad> mySales = adService.getSales(userId);
        List<Ad> myPurchases = adService.getPurchases(userId);

        model.put("adList", mySales);
        model.put("chosenAdsMap", adService.getChosenAdMap(userId));
        model.put("shortName", shortName);
        model.put("description", desc);
        model.put("price", price);
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap", catService.getCatIdParamsMap());
        model.put("wish", wish);
        
        model.put("selectedCats",catService.getSelectedCats(catIds));
        model.put("notSelectedCats",catService.getNotSelectedCats(catIds));
        model.put("resCount", mySales.size());
        model.put("chosenCount", chosenMap.size());
        model.put("compareCount", compAds.size());
        
        model.put("mySellCount", mySales.size());
        model.put("myBuyCount", myPurchases.size());
        
        ArrayList<String> ers = (ArrayList<String>) model.get("errors");
        if (ers == null) {
            ers = new ArrayList();
        }
        ers.addAll(adService.getErrors());
        ers.addAll(catService.getErrors());
        model.put(ERRORS_LIST_NAME, ers);
        return "main";
    }

    @RequestMapping("/purchases")
    public String getPurchases(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        
        Long userId = null;
        if (u != null) {
            userId = u.getId();
        }

        List<Ad> compAds = (List)request.getSession().getAttribute(COMPARISON);
        List<Long>catIds = (List<Long>)request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
        if(catIds==null){
            catIds=new ArrayList();
        }
        if(compAds==null){
            compAds=new ArrayList();
        }
        
        HashMap<Long,Ad> chosenMap = adService.getChosenAdMap(userId);
        
        List<Ad> mySales = adService.getSales(userId);
        List<Ad> myPurchases = adService.getPurchases(userId);

        model.put("adList", myPurchases);
        model.put("shortName", shortName);
        model.put("description", desc);
        model.put("price", price);
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap", catService.getCatIdParamsMap());
        model.put("wish", wish);
        
        model.put("selectedCats",catService.getSelectedCats(catIds));
        model.put("notSelectedCats",catService.getNotSelectedCats(catIds));
        model.put("resCount", myPurchases.size());
        model.put("chosenCount", chosenMap.size());
        model.put("compareCount", compAds.size());
        
        model.put("mySellCount", mySales.size());
        model.put("myBuyCount", myPurchases.size());
        
        ArrayList<String> ers = (ArrayList<String>) model.get("errors");
        if (ers == null) {
            ers = new ArrayList();
        }
        ers.addAll(adService.getErrors());
        ers.addAll(catService.getErrors());
        model.put(ERRORS_LIST_NAME, ers);
        return "main";
    }

    @RequestMapping("/comparison")
    public String getComparison(Map<String, Object> model,
            HttpServletRequest request,
            /*@RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,*/
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {

        List<Ad> ads = (List) request.getSession().getAttribute(COMPARISON);
        
        List<Long>catIds = (List)request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
        if(catIds==null){
            catIds=new ArrayList();
        }

        model.put("compMap", catService.getSortedParamsAndValsForComparison(ads));
        model.put("selectedCats",catService.getSelectedCats(catIds));
        model.put("notSelectedCats",catService.getNotSelectedCats(catIds));
        //model.put("purchasesList",adService.getPurchases(u.getId()));
        model.put("compAds",ads);
        /*model.put("shortName", shortName);
        model.put("description", desc);
        model.put("price", price);*/
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap", catService.getCatIdParamsMap());
        /*model.put("reqTypeMap",catService.getReqTypes());*/
        model.put("wish", wish);
        //model.put("paramMap",catService.getParamsMap());
        ArrayList<String> ers = (ArrayList<String>) model.get("errors");
        if (ers == null) {
            ers = new ArrayList();

        }

        /*List<String>supList = new ArrayList();
         for(int i=0;i<5;i++){
         supList.add(null);
         }
         for(String s:supList){
         ers.add(s);
         }
         ers.add("qwe");
            
         ers.add("list:"+supList.size()+";");*/
        ers.addAll(catService.getErrors());
        //ers.add("err");
        model.put(ERRORS_LIST_NAME, ers);
        return "comparison";
    }

    @RequestMapping("/authorize")
    public String authorize(Map<String, Object> model,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        User user = authManager.getCurrentUser();

        request.getSession().setAttribute(USER_NAME_SESSION_NAME, user.getName());
        request.getSession().setAttribute(USER_ID_SESSION_NAME, user.getId());
        request.getSession().setAttribute("role", user.getUserRole());
        //ras.addAttribute("role", "admin");

        return "redirect:/Main/";
    }

    @RequestMapping("/registration")
    public String register(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "passconfirm", required = false) String passconfirm,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            RedirectAttributes ras) throws Exception {

        userService.createUser(phone, email, password, name, passconfirm, User.ROLEUSER);

        /*model.put("name", name);
         model.put("phone", phone);
         model.put("email", email);*/
        /*ras.addAttribute("name", name);
         ras.addAttribute("phone", phone);
         ras.addAttribute("email", email);*/
        ras.addAttribute("errors", userService.getErrors());
        return "redirect:/Main/";
    }
    
    @RequestMapping("/createAndMountRegion")
    public String createAndMountRegion(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "localIds", required = false) Long localIds[],
            @RequestParam(value = "stateIds", required = false) Long stateIds[],
            @RequestParam(value = "all", required = false) Integer all,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String>errors=new ArrayList();
        User user = authManager.getCurrentUser();
        Region r = null;
        if(all!=null&&1==all){
            r = regionService.getDefaultRegion(null);
        }else{
            r = regionService.getRegion(localIds, stateIds, user,name);
            if(user!=null){
                regionService.addRegion(user,r);
            }
        }
        errors.addAll(regionService.getErrors());
        if(errors.isEmpty()){
            request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, r);
            ras.addAttribute("wish", wish);
            return "redirect:/Main/";
        }else{
            ras.addFlashAttribute("errors", errors);
            return "redirect:/Regions/select";
        }
    }
    
    @RequestMapping("/chooseRegion")
    public String chooseRegion(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "regionId", required = false) Long regionId,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String>errors=new ArrayList();
        User user = authManager.getCurrentUser();
        
        //проверку на юзера в регионе to do чтоб чужие нельзя было выбрать
        request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, regionService.getRegion(regionId));
        
        //request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, regionService.getDefaultRegion(user.getId()));
        errors.addAll(regionService.getErrors());
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute("errors", errors);
        return "redirect:/Main/";
    }
    
    @RequestMapping("/setHomeRegion")
    public String setHomeRegion(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "regionId", required = false) Long regionId,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String>errors=new ArrayList();
        User user = authManager.getCurrentUser();
        
        if(user!=null){
            regionService.setHomeRegion(regionId, user.getId());
        }
        
        //request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, regionService.getDefaultRegion(user.getId()));
        errors.addAll(regionService.getErrors());
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute("errors", errors);
        return "redirect:/Main/";
    }
    
    @RequestMapping("/deleteRegion")
    public String deleteRegion(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "regionId", required = false) Long regionId,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String>errors=new ArrayList();
        User user = authManager.getCurrentUser();
        
        if(user!=null){
            regionService.deleteRegion(regionId, user.getId());
        }
        
        //request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, regionService.getDefaultRegion(user.getId()));
        errors.addAll(regionService.getErrors());
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute("errors", errors);
        return "redirect:/Main/";
    }

    /*@RequestMapping("/recoveryPassword")
     public String recoveryPassword(Map<String, Object> model, HttpServletRequest request,
     @RequestParam(value = "email", required = false) String email, String submit) throws Exception {

     if (submit != null) {
     String recoverHash = userService.recoveryPassword(email);
     if (userService.getErrors().isEmpty()) {
     String link = "http://dialogpl.com/User/recoverPassword";
     String text = "Вы восcтнавливаете пароль от Seller. Пройдите по ссылке для восстановления: " + link + "?hash=" + recoverHash;
     sendMail.sendMail(email, text);
     model.put("message", "Ссылка с востановлением отправлена на почту");
     }
     model.put("email", email);
     model.put("errors", userService.getErrors());
     }
     return "recoveryPassword";
     }

     @RequestMapping("/recoverPassword")
     public String recoverPassword(Map<String, Object> model, HttpServletRequest request,
     @RequestParam(value = "hash", required = false) String hash,
     @RequestParam(value = "newPassword", required = false) String password,
     @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
     String submit) throws Exception {

     model.put("hash", hash);
        
     if (submit != null) {
     userService.recoverPassword(hash, password, confirmPassword);
     if (userService.getErrors().isEmpty()) {
     return "redirect:/login";
     }
     model.put("errors", userService.getErrors());

     }

     return "recoverPassword";

     }*/
}
