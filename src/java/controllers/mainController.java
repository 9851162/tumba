/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import entities.Ad;
import entities.Locality;
import entities.Region;
import entities.State;
import entities.User;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import support.DateAdapter;
import support.StringAdapter;

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

    public static String MAINACTIONNAME = "main";
    public static String PURCHASESACTIONNAME = "purchases";
    public static String SALESACTIONNAME = "sales";
    public static String CHOSENACTIONNAME = "chosen";

    @RequestMapping("/")
    public String getMain(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "wish", required = false) String wish,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "dateFrom", required = false) Date dateFrom,
            @RequestParam(value = "dateTo", required = false) Date dateTo,
            @RequestParam(value = "booleanIds", required = false) Long booleanIds[],
            @RequestParam(value = "booleanVals", required = false) Long booleanVals[],
            @RequestParam(value = "stringIds", required = false) Long stringIds[],
            @RequestParam(value = "stringVals", required = false) String stringVals[],
            @RequestParam(value = "numIds", required = false) Long numIds[],
            @RequestParam(value = "numVals", required = false) String numVals[],
            @RequestParam(value = "numCondition", required = false) Integer numConditions[],
            @RequestParam(value = "dateIds", required = false) Long dateIds[],
            @RequestParam(value = "dateVals", required = false) Date dateVals[],
            @RequestParam(value = "dateCondition", required = false) Integer dateConditions[],
            @RequestParam(value = "selIds", required = false) Long selIds[],
            @RequestParam(value = "selVals", required = false) Long selVals[],
            @RequestParam(value = "multyIds", required = false) Long multyIds[],
            @RequestParam(value = "multyVals", required = false) String multyVals[],
            @RequestParam(value = "action", required = false) String action,
            RedirectAttributes ras) throws Exception {

        List<String> ers = (List) model.get(ERRORS_LIST_NAME);
        if (ers == null) {
            ers = new ArrayList();
        }

        if (action == null) {
            action = MAINACTIONNAME;
        }

        if (dateFrom == null) {
            dateFrom = (Date) model.get("dateFrom");
            if (dateFrom != null) {
                dateFrom = DateAdapter.getStartOfDate(dateFrom);
            } else {
                dateFrom = DateAdapter.getStartOfDate(new Date());
            }
        }
        if (dateTo == null) {
            dateTo = (Date) model.get("dateTo");
            if (dateTo != null) {
                dateTo = DateAdapter.getEndOfDate(dateTo);
            } else {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, 14);
                dateTo = DateAdapter.getEndOfDate(c);
            }
        }

        List<Long> catIds = (List<Long>) request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
        if (catIds == null) {
            catIds = new ArrayList();
        }
        List<Ad> compAds = (List) request.getSession().getAttribute(COMPARISON);
        if (compAds == null) {
            compAds = new ArrayList();
        }

        User u = authManager.getCurrentUser();
        Long userId = null;
        if (u != null) {
            userId = u.getId();
            if (u.isHomeSet()) {
                model.put("homeSet", u.getHomeSet());
            }
        }

        Region region = (Region) request.getSession().getAttribute(MOUNTED_REGION_SESSION_NAME);
        //установка региона, если нет еще
        if (region == null) {
            region = regionService.getDefaultRegion(userId);
            request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, region);
        }

        HashMap<Long, Long> locsInRegMap = new HashMap();
        HashMap<Long, Integer> statesInRegMap = new HashMap();
        for (Locality l : region.getLocalities()) {
            locsInRegMap.put(l.getId(), l.getId());
            Long StateId = l.getState().getId();
            Integer locksInState = statesInRegMap.get(StateId);
            if (locksInState == null) {
                locksInState = 0;
            }
            statesInRegMap.put(StateId, ++locksInState);
        }
        model.put("locsInRegMap", locsInRegMap);
        model.put("statesInRegMap", statesInRegMap);
        /*ers.add("reg1:"+region.getName());
        
         ers.add("locIds:"+AdDao.getIdsAsString(AdDao.getLocIds(region)));*/

        HashMap<Long, Ad> chosenMap = adService.getChosenAdMap(userId);
        List<Ad>chosenAds=new ArrayList();
        if(userId==null){
            chosenAds = (List<Ad>) request.getSession().getAttribute(CHOSEN_ADS_LIST_SESSION_NAME);
            if(chosenAds==null){
                chosenAds=new ArrayList();
            }
            for(Ad ad:chosenAds){
                chosenMap.put(ad.getId(), ad);
            }
        }
        
        HashMap<Long, Ad> comparingMap = new HashMap();
        for (Ad ad : compAds) {
            comparingMap.put(ad.getId(), ad);
        }

        List<Ad> ads = new ArrayList();

        if (action.equals(CHOSENACTIONNAME)) {
            if (userId != null) {
                ads = adService.getChosenAds(userId);
            } else {
                ads = chosenAds;
            }
        } else if (action.equals(PURCHASESACTIONNAME)) {
            if (userId != null) {
                ads = adService.getPurchases(userId);
            } else {
                ads = adService.getAds(wish, catIds, region, order, booleanIds, booleanVals,
                        stringIds, stringVals, numIds, numVals, numConditions, dateIds, dateVals, dateConditions, selIds, selVals, multyIds, multyVals);
            }
        } else if (action.equals(SALESACTIONNAME)) {
            if (userId != null) {
                ads = adService.getSales(userId);
            } else {
                ads = adService.getAds(wish, catIds, region, order, booleanIds, booleanVals,
                        stringIds, stringVals, numIds, numVals, numConditions, dateIds, dateVals, dateConditions, selIds, selVals, multyIds, multyVals);
            }
        } else {
            ads = adService.getAds(wish, catIds, region, order, booleanIds, booleanVals,
                    stringIds, stringVals, numIds, numVals, numConditions, dateIds, dateVals, dateConditions, selIds, selVals, multyIds, multyVals);
        }
        List<Region> availableRegions = regionService.getAvailableRegions(region, u);

        model.put("states", regionService.getNotEmptyStates());
        model.put("selectedCats", catService.getSelectedCats(catIds));
        model.put("notSelectedCats", catService.getNotSelectedCats(catIds));
        model.put("advancedSearchParams", catService.getMutualParams(catIds));
        model.put("adList", ads);
        model.put("chosenAdsMap", chosenMap);
        model.put("comparingAdsMap", comparingMap);
        model.put("resCount", ads.size());
        model.put("chosenCount", chosenMap.size());
        model.put("compareCount", compAds.size());
        model.put("availableRegions", availableRegions);
        model.put("regionSet", region.getId());

        //to do srazu polu4enie 4isla iz bazi?
        List<Ad> mySales = adService.getSales(userId);
        List<Ad> myPurchases = adService.getPurchases(userId);
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
        model.put("dateFrom", DateAdapter.formatByDate(dateFrom, DateAdapter.SMALL_FORMAT));
        model.put("dateTo", DateAdapter.formatByDate(dateTo, DateAdapter.SMALL_FORMAT));
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap", catService.getCatIdParamsMap());
        model.put("wish", wish);
        model.put("order", order);
        model.put("action", action);

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

        List<Long> catList = (List<Long>) request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
        if (catList == null) {
            catList = new ArrayList();
        }
        if (!catList.contains(catId) && catList.size() < 20) {
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

        List<Long> catList = (List<Long>) request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
        if (catList == null) {
            catList = new ArrayList();
        }
        catList.remove(catId);
        request.getSession().setAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME, catList);
        ras.addAttribute("wish", wish);
        return "redirect:/Main/";
    }

    @RequestMapping("/comparison")
    public String getComparison(Map<String, Object> model,
            HttpServletRequest request,
            /*@RequestParam(value = "shortName", required = false) String shortName,
             @RequestParam(value = "description", required = false) String desc,
             @RequestParam(value = "price", required = false) Double price,*/
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {
            List<Ad> ads = (List) request.getSession().getAttribute(COMPARISON);

            Region region = (Region) request.getSession().getAttribute(MOUNTED_REGION_SESSION_NAME);
            //установка региона, если нет еще
            if (region == null) {
                region = regionService.getDefaultRegion(u.getId());
                request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, region);
            }

            HashMap<Long, Long> locsInRegMap = new HashMap();
            HashMap<Long, Integer> statesInRegMap = new HashMap();
            for (Locality l : region.getLocalities()) {
                locsInRegMap.put(l.getId(), l.getId());
                Long StateId = l.getState().getId();
                Integer locksInState = statesInRegMap.get(StateId);
                if (locksInState == null) {
                    locksInState = 0;
                }
                statesInRegMap.put(StateId, ++locksInState);
            }
            model.put("locsInRegMap", locsInRegMap);
            model.put("statesInRegMap", statesInRegMap);
            model.put("availableRegions", regionService.getAvailableRegions(region, u));

            List<Long> catIds = (List) request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
            if (catIds == null) {
                catIds = new ArrayList();
            }

            model.put("compMap", catService.getSortedParamsAndValsForComparison(ads));
            model.put("selectedCats", catService.getSelectedCats(catIds));
            model.put("notSelectedCats", catService.getNotSelectedCats(catIds));
            model.put("compAds", ads);
            model.put("catList", catService.getCatList());
            model.put("catMap", catService.getCatMap());
            model.put("catParamsMap", catService.getCatIdParamsMap());
            model.put("wish", wish);

        }
        ArrayList<String> ers = (ArrayList<String>) model.get("errors");
        if (ers == null) {
            ers = new ArrayList();

        }
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

        Region region = regionService.getDefaultRegion(user.getId());
        request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, region);
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
        List<String> errors = new ArrayList();
        User user = authManager.getCurrentUser();
        Region r = null;
        if (all != null && 1 == all) {
            r = regionService.getDefaultRegion(null);
        } else {
            r = regionService.getRegion(localIds, stateIds, user, name);
            if (user != null) {
                regionService.addRegion(user, r);
            }
        }
        errors.addAll(regionService.getErrors());

        request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, r);
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute("errors", errors);
        return "redirect:/Main/";
    }

    @RequestMapping("/chooseRegion")
    public String chooseRegion(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "regionId", required = false) Long regionId,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
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
            @RequestParam(value = "regionForShowId", required = false) Long regionForShowId,
            @RequestParam(value = "regionId", required = false) Long regionId,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        User user = authManager.getCurrentUser();

        if (user != null) {
            regionService.setHomeRegion(regionId, user.getId());
        }

        //request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, regionService.getDefaultRegion(user.getId()));
        errors.addAll(regionService.getErrors());
        ras.addAttribute("regionForShowId", regionForShowId);
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute("errors", errors);
        return "redirect:/Main/regions";
    }

    @RequestMapping("/deleteRegion")
    public String deleteRegion(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "regionForShowId", required = false) Long regionForShowId,
            @RequestParam(value = "regionId", required = false) Long regionId,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        User user = authManager.getCurrentUser();

        if (user != null) {
            regionService.deleteRegion(regionId, user.getId());
        }

        //request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, regionService.getDefaultRegion(user.getId()));
        errors.addAll(regionService.getErrors());
        ras.addAttribute("regionForShowId", regionForShowId);
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute("errors", errors);
        return "redirect:/Main/regions";
    }

    @RequestMapping("/regions")
    public String showRegions(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "regionForShowId", required = false) Long regionForShowId,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        User user = authManager.getCurrentUser();

        if (user != null) {

            //to do получение региона по юзеру и рег ид, а не только по рег ид
            //Смысл state в регионе вообще следовало бы переделать.
            Region regionForShow = new Region();
            if (regionForShowId != null) {
                regionForShow = regionService.getRegion(regionForShowId);
                if (regionForShow != null) {
                    model.put("regionForShow", regionForShow);
                    HashMap<Long, Long> locsInReg4ShowMap = new HashMap();
                    HashMap<Long, Integer> statesInReg4ShowMap = new HashMap();

                    for (Locality l : regionForShow.getLocalities()) {
                        locsInReg4ShowMap.put(l.getId(), l.getId());
                        Long StateId = l.getState().getId();
                        Integer locksInState = statesInReg4ShowMap.get(StateId);
                        if (locksInState == null) {
                            locksInState = 0;
                        }
                        statesInReg4ShowMap.put(StateId, ++locksInState);
                    }
                    model.put("locsInReg4ShowMap", locsInReg4ShowMap);
                    model.put("statesInReg4ShowMap", statesInReg4ShowMap);
                }
            }

            List<String> ers = (List) model.get(ERRORS_LIST_NAME);
            if (ers == null) {
                ers = new ArrayList();
            }
            Region region = (Region) request.getSession().getAttribute(MOUNTED_REGION_SESSION_NAME);
            //установка региона, если нет еще
            if (region == null) {
                region = regionService.getDefaultRegion(user.getId());
                request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, region);
            }
            HashMap<Long, Long> locsInRegMap = new HashMap();
            HashMap<Long, Integer> statesInRegMap = new HashMap();

            for (Locality l : region.getLocalities()) {
                locsInRegMap.put(l.getId(), l.getId());
                Long StateId = l.getState().getId();
                Integer locksInState = statesInRegMap.get(StateId);
                if (locksInState == null) {
                    locksInState = 0;
                }
                statesInRegMap.put(StateId, ++locksInState);
            }
            model.put("locsInRegMap", locsInRegMap);
            model.put("statesInRegMap", statesInRegMap);

            if (user.isHomeSet()) {
                model.put("homeSet", user.getHomeSet());
            }

            List<Long> catIds = (List<Long>) request.getSession().getAttribute(CATEGORY_SEARCH_LIST_SESSION_NAME);
            if (catIds == null) {
                catIds = new ArrayList();
            }
            model.put("states", regionService.getNotEmptyStates());
            model.put("selectedCats", catService.getSelectedCats(catIds));
            model.put("notSelectedCats", catService.getNotSelectedCats(catIds));

            HashMap<Long, Ad> chosenMap = adService.getChosenAdMap(user.getId());
            List<Ad> compAds = (List) request.getSession().getAttribute(COMPARISON);
            if (compAds == null) {
                compAds = new ArrayList();
            }

            List<Ad> mySales = adService.getSales(user.getId());
            List<Ad> myPurchases = adService.getPurchases(user.getId());
            List<Region> availableRegions = regionService.getAvailableRegions(region, user);

            model.put("catList", catService.getCatList());
            model.put("catMap", catService.getCatMap());
            model.put("catParamsMap", catService.getCatIdParamsMap());
            model.put("wish", wish);

            model.put("chosenCount", chosenMap.size());
            model.put("compareCount", compAds.size());
            model.put("availableRegions", availableRegions);
            model.put("regionSet", region.getId());

            //to do srazu polu4enie 4isla iz bazi
            model.put("mySellCount", mySales.size());
            model.put("myBuyCount", myPurchases.size());

            ers.addAll(adService.getErrors());
            ers.addAll(catService.getErrors());
            model.put(ERRORS_LIST_NAME, ers);

        }
        return "regions4Users";
    }

    @RequestMapping("/createRegion")
    public String createRegion(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "localIds", required = false) Long localIds[],
            @RequestParam(value = "stateIds", required = false) Long stateIds[],
            @RequestParam(value = "all", required = false) Integer all,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        User user = authManager.getCurrentUser();
        Region r = null;
        Long regId = null;
        if (all != null && 1 == all) {
            r = regionService.getDefaultRegion(null);
        } else {
            r = regionService.getRegion(localIds, stateIds, user, name);
        }
        if (user != null) {
            regId = regionService.addRegion(user, r);
        }
        errors.addAll(regionService.getErrors());

        request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, r);
        ras.addAttribute("regionForShowId", regId);
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute("errors", errors);
        return "redirect:/Main/regions";
    }

    @RequestMapping("/changeRegionStructure")
    public String changeRegionStructure(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "regionId", required = false) Long regionId,
            @RequestParam(value = "localIds", required = false) Long localIds[],
            @RequestParam(value = "stateIds", required = false) Long stateIds[],
            @RequestParam(value = "all", required = false) Integer all,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        User user = authManager.getCurrentUser();

        if (user != null) {
            regionService.changeRegionStructure(regionId, localIds, stateIds, user);
        }

        //request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, regionService.getDefaultRegion(user.getId()));
        errors.addAll(regionService.getErrors());
        ras.addAttribute("regionForShowId", regionId);
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute("errors", errors);
        return "redirect:/Main/regions";
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
