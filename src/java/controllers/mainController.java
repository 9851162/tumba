/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import entities.Ad;
import entities.Locality;
import entities.Message;
import entities.Region;
import entities.User;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.AdService;
import service.CategoryService;
import service.MessageService;
import service.RegionService;
import service.UserService;
import support.DateAdapter;

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
    @Autowired
    private MessageService msgService;

    public static String MAINACTIONNAME = "main";
    public static String ONEITEMACTIONNAME = "showoneitem";
    public static String PURCHASESACTIONNAME = "purchases";
    public static String SALESACTIONNAME = "sales";
    public static String CHOSENACTIONNAME = "chosen";
    public static String MESSAGESACTIONNAME = "messages";
    public static String ONEMESSAGEACTIONNAME = "showMessage";
    public static String REGIONSACTIONNAME = "regions";

    @RequestMapping("/")
    public String getMain(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "regionForShowId", required = false) Long regionForShowId,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "wish", required = false) String wish,
            @RequestParam(value = "dateFrom", required = false) Date dateFrom,
            @RequestParam(value = "dateTo", required = false) Date dateTo,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "messageId", required = false) Long msgId,
            @RequestParam(value = "booleanIds", required = false) Long booleanIds[],
            @RequestParam(value = "booleanVals", required = false) Long booleanVals[],
            @RequestParam(value = "stringIds", required = false) Long stringIds[],
            @RequestParam(value = "stringVals", required = false) String stringVals[],
            @RequestParam(value = "numIds", required = false) Long numIds[],
            @RequestParam(value = "numValsFrom", required = false) String numValsFrom[],
            @RequestParam(value = "numValsTo", required = false) String numValsTo[],
            @RequestParam(value = "dateIds", required = false) Long dateIds[],
            @RequestParam(value = "dateValsFrom", required = false) String dateValsFrom[],
            @RequestParam(value = "dateValsTo", required = false) String dateValsTo[],
            @RequestParam(value = "selIds", required = false) Long selIds[],
            @RequestParam(value = "selVals", required = false) Long selVals[],
            @RequestParam(value = "multyIds", required = false) Long multyIds[],
            @RequestParam(value = "multyVals", required = false) String multyVals[],
            @RequestParam(value = "searchPriceFrom", required = false) String searchPriceFrom,
            @RequestParam(value = "searchPriceTo", required = false) String searchPriceTo,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "adId", required = false) Long adId,
            RedirectAttributes ras) throws Exception {

        List<String> ers = (List) model.get(ERRORS_LIST_NAME);
        if (ers == null) {
            ers = new ArrayList();
        }

        if (action == null) {
            action = MAINACTIONNAME;
        }
        //обработка для расстановки значений фильтра поиска
        HashMap<Long, Object> filtr = new HashMap();
        int i;
        int size;
        HashMap<String,String>filtrVal;
        if (numIds != null && numIds.length > 0) {
            i = 0;
            size = numIds.length;
            while (i < size) {
                Long id = numIds[i];
                filtrVal = new HashMap();
                String vFrom="";
                String vTo="";
                if (id != null) {
                    if (numValsFrom != null && numValsFrom.length > 0) {
                        vFrom=numValsFrom[i];
                    }
                    if (numValsTo != null && numValsTo.length > 0) {
                        vTo=numValsTo[i];
                    }
                }
                filtrVal.put("from", vFrom);
                filtrVal.put("to", vTo);
                filtr.put(id,filtrVal);
                i++;
            }
        }
        if (dateIds != null && dateIds.length > 0) {
            i = 0;
            size = dateIds.length;
            while (i < size) {
                Long id = dateIds[i];
                filtrVal = new HashMap();
                String vFrom="";
                String vTo="";
                if (id != null) {
                    if (dateValsFrom != null && dateValsFrom.length > 0) {
                        vFrom=dateValsFrom[i];
                    }
                    if (dateValsTo != null && dateValsTo.length > 0) {
                        vTo=dateValsTo[i];
                    }
                }
                filtrVal.put("from", vFrom);
                filtrVal.put("to", vTo);
                filtr.put(id,filtrVal);
                i++;
            }
        }
        if (stringVals != null && stringVals.length > 0) {
            i = 0;
            size = stringVals.length;
            String val;
            Long id;
            while (i < size) {
                val = stringVals[i];
                if (val != null && !val.equals("")) {
                    id = stringIds[i];
                    if (id != null) {
                        filtr.put(id, val);
                    }
                }
                i++;
            }
        }
        if(booleanVals!=null&&booleanVals.length>0){
            i = 0;
            size = booleanVals.length;
            Long val;
            Long id;
            while (i < size) {
                val = booleanVals[i];
                if (val != null) {
                    id = booleanIds[i];
                    if (id != null) {
                        filtr.put(id, val);
                    }
                }
                i++;
            }
        }
        if(selVals!=null&&selVals.length>0){
            i = 0;
            size = selVals.length;
            Long val;
            Long id;
            while (i < size) {
                val = selVals[i];
                if (val != null) {
                    id = selIds[i];
                    if (id != null) {
                        filtr.put(id, val);
                    }
                }
                i++;
            }
        }
        /*if(multyIds!=null&&multyIds.length>0){
            i = 0;
            size = multyVals.length;
            Long val;
            Long id;
            while (i < size) {
                val = selVals[i];
                if (val != null) {
                    id = selIds[i];
                    if (id != null) {
                        filtr.put(id, val);
                    }
                }
                i++;
            }
        }*/
        model.put("searchPriceFrom",searchPriceFrom);
        model.put("searchPriceTo",searchPriceTo);
        model.put("filtr",filtr);
        

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
            model.put("userAds", adService.getUserAds(userId));
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

        HashMap<Long, Ad> chosenMap = adService.getChosenAdMap(userId);
        List<Ad> chosenAds = new ArrayList();
        if (userId == null) {
            chosenAds = (List<Ad>) request.getSession().getAttribute(CHOSEN_ADS_LIST_SESSION_NAME);
            if (chosenAds == null) {
                chosenAds = new ArrayList();
            }
            for (Ad ad : chosenAds) {
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
                        stringIds, stringVals, numIds, numValsFrom, numValsTo, dateIds, dateValsFrom,
                        dateValsTo, selIds, selVals, multyIds, multyVals, searchPriceFrom, searchPriceTo);
            }
        } else if (action.equals(SALESACTIONNAME)) {
            if (userId != null) {
                ads = adService.getSales(userId);
            } else {
                ads = adService.getAds(wish, catIds, region, order, booleanIds, booleanVals,
                        stringIds, stringVals, numIds, numValsFrom, numValsTo, dateIds, dateValsFrom,
                        dateValsTo, selIds, selVals, multyIds, multyVals, searchPriceFrom, searchPriceTo);
            }
        } else if (action.equals(ONEITEMACTIONNAME) && adId != null) {
            Ad ad = adService.getAd(adId);
            ads.add(ad);
        } else if (action.equals(MESSAGESACTIONNAME)) {
            if (userId != null) {
                List<Message> messages = msgService.getInbox(userId);
                model.put("inboxMessages", messages);
                model.put("msgCount", messages.size());
            }
        } else if (action.equals(ONEMESSAGEACTIONNAME)) {
            if (userId != null && msgId != null) {
                Message msg = msgService.getMsg(userId, msgId);
                Integer msgCount = 0;
                if (msg != null) {
                    model.put("inboxMessage", msg);
                    msgCount = 1;
                }
                model.put("msgCount", msgCount);
            }
        } else if (action.equals(REGIONSACTIONNAME)) {
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

        } else {
            ads = adService.getAds(wish, catIds, region, order, booleanIds, booleanVals,
                    stringIds, stringVals, numIds, numValsFrom, numValsTo, dateIds,
                    dateValsFrom, dateValsTo, selIds, selVals, multyIds, multyVals, searchPriceFrom, searchPriceTo);

            if ((wish != null && !wish.equals("")) || (catIds != null && !catIds.isEmpty())) {
                model.put("catNamesWithCountsMap", adService.getCatsWithCountsBySearch(wish, catIds, region, booleanIds, booleanVals,
                        stringIds, stringVals, numIds, numValsFrom, numValsTo, dateIds,
                        dateValsFrom, dateValsTo, selIds, selVals, multyIds, multyVals, searchPriceFrom, searchPriceTo));
            }
        }
        List<Region> availableRegions = regionService.getAvailableRegions(region, u);
        if (u == null && !region.isAllRussia()) {
            availableRegions.add(region);
        }

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
        if ((phone == null || phone.equals("")) && u != null) {
            phone = u.getPhone();
        }
        if ((email == null || email.equals("")) && u != null) {
            email = u.getEmail();
        }
        model.put("dateFrom", DateAdapter.formatByDate(dateFrom, DateAdapter.SMALL_FORMAT));
        model.put("dateTo", DateAdapter.formatByDate(dateTo, DateAdapter.SMALL_FORMAT));
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap", catService.getCatIdParamsMap());
        model.put("phone", phone);
        model.put("email", email);
        model.put("wish", wish);
        model.put("order", order);
        model.put("action", action);

        model.put("myNewMsgCount", msgService.getNewMsgCount(userId));

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
        Long userId = null;
        if (u != null) {
            userId = u.getId();
        }
        List<Ad> ads = (List) request.getSession().getAttribute(COMPARISON);

        Region region = (Region) request.getSession().getAttribute(MOUNTED_REGION_SESSION_NAME);
        //установка региона, если нет еще
        if (region == null) {
            region = regionService.getDefaultRegion(userId);
            request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, region);
        }
        model.put("regionSet", region.getId());
        model.put("states", regionService.getNotEmptyStates());

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

        List<String> errors = userService.getErrors();
        if (!errors.isEmpty()) {
            ras.addFlashAttribute("errors", errors);
        } else {
            List<String> msgs = userService.getErrors();
            msgs.add("Регистрация успешно завершена. На указанный Вами email отправлено письмо для активации.");
            ras.addFlashAttribute("messages", msgs);
        }
        return "redirect:/Main/";
    }

    /*@RequestMapping("/createRegion")
     public String createRegion(Map<String, Object> model,
     HttpServletRequest request,
     @RequestParam(value = "name", required = false) String name,
     @RequestParam(value = "localIds", required = false) Long localIds[],
     @RequestParam(value = "all", required = false) Integer all,
     @RequestParam(value = "wish", required = false) String wish,
     RedirectAttributes ras) throws Exception {
     List<String> errors = new ArrayList();
     User user = authManager.getCurrentUser();
     Region r = null;
     Long regId = null;
     r = regionService.getRegion(localIds, user, name);
     //}
     if (user != null) {
     regId = regionService.addRegion(user, r);
     }
     errors.addAll(regionService.getErrors());

     request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, r);
     ras.addAttribute("regionForShowId", regId);
     ras.addAttribute("wish", wish);
     ras.addFlashAttribute(ERRORS_LIST_NAME, errors);
     return "redirect:/Main/?action=regions";
     }*/
    @RequestMapping("/createRegion")
    public String createRegion(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "localIds", required = false) Long localIds[],
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        User user = authManager.getCurrentUser();
        ras.addAttribute("wish", wish);
        Region r = regionService.getRegion(localIds, user, name);
        if (regionService.getErrors().isEmpty()) {
            request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, r);
            //точка входа в регионах, туда и возвращаемся
            if (user != null) {
                regionService.addRegion(user, r);
                Long regId = regionService.addRegion(user, r);
                ras.addFlashAttribute(ERRORS_LIST_NAME, regionService.getErrors());
                ras.addAttribute("regionForShowId", regId);
                return "redirect:/Main/?action=regions";
            }
        }
        ras.addFlashAttribute(ERRORS_LIST_NAME, regionService.getErrors());
        return "redirect:/Main/";
    }

    @RequestMapping("/mountRegion")
    public String mountRegion(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "regionId", required = false) Long regionId,
            @RequestParam(value = "all", required = false) Integer all,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        Region r = null;
        User user = authManager.getCurrentUser();
        if (user == null || Objects.equals(1, all)) {
            r = new Region();
            r.setAllRussia(Boolean.TRUE);
        } else {
            //проверку на юзера в регионе to do чтоб чужие нельзя было выбрать
            r = regionService.getRegion(regionId);
        }
        request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, r);
        errors.addAll(regionService.getErrors());
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute(ERRORS_LIST_NAME, errors);
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
        return "redirect:/Main/?action=regions";
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
        return "redirect:/Main/?action=regions";
    }

    /*@RequestMapping("/regions")
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
     }*/
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
        return "redirect:/Main/?action=regions";
    }

    @RequestMapping("/messages")
    public String showMessages(Map<String, Object> model,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {

        }
        return "messages";
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
