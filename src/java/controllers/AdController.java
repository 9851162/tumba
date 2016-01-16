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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.AdService;
import service.CategoryService;
import service.RegionService;
import support.DateAdapter;
import support.JsonResponse;

/**
 *
 * @author bezdatiuzer
 */
@RequestMapping("/Ad")
@Controller
public class AdController extends WebController {

    @Autowired
    private AdService adService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private CategoryService catService;

    @RequestMapping("/add")
    public String add(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "dateFrom", required = false) Date dateFrom,
            @RequestParam(value = "dateTo", required = false) Date dateTo,
            @RequestParam(value = "previews", required = false) MultipartFile previews[],
            @RequestParam(value = "regionId", required = false) Long regionId,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "booleanIds", required = false) Long booleanIds[],
            @RequestParam(value = "booleanVals", required = false) String booleanVals[],
            @RequestParam(value = "stringIds", required = false) Long stringIds[],
            @RequestParam(value = "stringVals", required = false) String stringVals[],
            @RequestParam(value = "numIds", required = false) Long numIds[],
            @RequestParam(value = "numVals", required = false) String numVals[],
            @RequestParam(value = "dateIds", required = false) Long dateIds[],
            @RequestParam(value = "dateVals", required = false) Date dateVals[],
            @RequestParam(value = "selIds", required = false) Long selIds[],
            @RequestParam(value = "selVals", required = false) Long selVals[],
            @RequestParam(value = "multyIds", required = false) Long multyIds[],
            @RequestParam(value = "multyVals", required = false) String multyVals[],
            //@RequestParam(value = "localIds", required = false) Long localIds[],

            RedirectAttributes ras) throws Exception {
        ArrayList<String> errors = new ArrayList();
        Boolean isAutherized = false;
        User authedUser = authManager.getCurrentUser();
        if (authedUser != null) {
            isAutherized = true;
        }

        if (dateFrom == null) {
            dateFrom = DateAdapter.getStartOfDate(new Date());
        }
        if (dateTo == null) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, 14);
            dateTo = DateAdapter.getEndOfDate(c);
        }

        Region region = (Region) request.getSession().getAttribute(MOUNTED_REGION_SESSION_NAME);
        if (regionId != null) {
            if (regionId.equals(0L)) {
                region.setAllRussia(Boolean.TRUE);
            } else {
                region = regionService.getRegion(regionId);
            }
        }
        //Long localIds[] = regionService.getLocIds(regionId,region);

        adService.create(isAutherized, catId, email, phone, price, previews, shortName, description, booleanIds, booleanVals,
                stringIds, stringVals, numIds, numVals, dateIds, dateVals, selIds, selVals, multyIds, multyVals, dateFrom, dateTo, region);

        errors.addAll(regionService.getErrors());
        errors.addAll(adService.getErrors());

        if (!errors.isEmpty()) {
            ras.addFlashAttribute("errors", errors);
            ras.addFlashAttribute("shortName", shortName);
            ras.addFlashAttribute("description", description);
            ras.addFlashAttribute("price", price);
            ras.addFlashAttribute("catId", catId);
            ras.addFlashAttribute("dateFrom", dateFrom);
            ras.addFlashAttribute("dateTo", dateTo);
            ras.addFlashAttribute("email", email);
            ras.addFlashAttribute("phone", phone);
            //ras.addAttribute("previews", previews);
        }
        //errors.add("user="+authManager.getCurrentUser().getEmail()+", "+authManager.getCurrentUser().getName());
        //errors.add("s:"+errors.size());
        //ras.addFlashAttribute(ERRORS_LIST_NAME, errors);

        return "redirect:/Main/";
    }

    @RequestMapping("/buy")
    public String buy(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {
            adService.buy(u, adId);
            ras.addFlashAttribute(ERRORS_LIST_NAME, adService.getErrors());
        }
        ras.addAttribute("wish", wish);
        return "redirect:/Main/";
    }

    @RequestMapping("/changeStatus")
    public String changeStatus(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        ArrayList<String> errors = new ArrayList();
        User u = authManager.getCurrentUser();
        if (u != null && User.ROLEADMIN.equals(u.getUserRole())) {
            adService.changeStatus(status, adId);
        }
        errors.addAll(adService.getErrors());
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute(ERRORS_LIST_NAME, errors);
        return "redirect:/Main/";
    }

    @RequestMapping("/setChosenUnchosen")
    @ResponseBody
    public JsonResponse setChosen(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {
            adService.setUnsetChosen(u.getId(), adId);
        } else {
            List<Ad> chosenAds = (List<Ad>) request.getSession().getAttribute(CHOSEN_ADS_LIST_SESSION_NAME);
            if (chosenAds == null) {
                chosenAds = new ArrayList();
            }
            if (chosenAds.size() < 20) {
                Ad ad = adService.getAd(adId);
                if (!chosenAds.contains(ad)) {
                    chosenAds.add(ad);
                } else {
                    chosenAds.remove(ad);
                }
            }
            request.getSession().setAttribute(CHOSEN_ADS_LIST_SESSION_NAME, chosenAds);
        }
        JsonResponse res = new JsonResponse();
        res.setStatus(Boolean.TRUE);
        if (!adService.getErrors().isEmpty()) {
            res.setMessage(adService.getErrorsAsString());
            res.setStatus(Boolean.FALSE);
        }
        return res;
    }

    @RequestMapping("/addToComparison")
    @ResponseBody
    public JsonResponse addToComparison(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            RedirectAttributes ras) throws Exception {

        List<Ad> ads = (List) request.getSession().getAttribute(COMPARISON);
        if (ads == null) {
            ads = new ArrayList();
        }
        Ad ad = adService.getAd(adId);
        if (!ads.contains(ad)) {
            if (ads.size() < 20) {
                ads.add(ad);
            }
        }
        request.getSession().setAttribute(COMPARISON, ads);
        JsonResponse res = new JsonResponse();
        res.setStatus(Boolean.TRUE);
        if (!adService.getErrors().isEmpty()) {
            res.setMessage(adService.getErrorsAsString());
            res.setStatus(Boolean.FALSE);
        }
        return res;
    }

    @RequestMapping("/removeFromComparison")
    public String removeFromComparison(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            @RequestParam(value = "wish", required = false) Long wish,
            RedirectAttributes ras) throws Exception {

        List ads = (List) request.getSession().getAttribute(COMPARISON);
        if (ads == null) {
            ads = new ArrayList();
        }
        Ad ad = adService.getAd(adId);
        if (ads.contains(ad)) {
            ads.remove(ad);
        }
        request.getSession().setAttribute(COMPARISON, ads);
        ras.addAttribute("wish", wish);
        return "redirect:/Main/comparison";
    }

    @RequestMapping("/watch")
    @ResponseBody
    public JsonResponse watch(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            RedirectAttributes ras) throws Exception {

        String ip = request.getRemoteAddr();
        adService.addWatchFromIp(adId, ip);

        JsonResponse res = new JsonResponse();
        res.setStatus(Boolean.TRUE);
        if (!adService.getErrors().isEmpty()) {
            res.setMessage(adService.getErrorsAsString());
            res.setStatus(Boolean.FALSE);
        }
        return res;
    }

    @RequestMapping("/getAd")
    @ResponseBody
    public JsonResponse getAd4Change(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        JsonResponse res = new JsonResponse();
        res.setStatus(Boolean.TRUE);
        Ad ad = adService.getAd(adId);
        HashMap<Long, Long> locsInReg4ShowMap = new HashMap();
        HashMap<Long, Integer> statesInReg4ShowMap = new HashMap();
        if (ad != null) {
            for (Locality l : ad.getLocalities()) {
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

            if (!adService.getErrors().isEmpty()) {
                res.setMessage(adService.getErrorsAsString());
                res.setStatus(Boolean.FALSE);
            }
            if (ad.getAuthor().getId().equals(u.getId()) || u.getUserRole().equals(User.ROLEADMIN)) {
                res.addData("shortName", ad.getName());
                res.addData("description", ad.getDescription());
                res.addData("email", ad.getEmail());
                res.addData("phone", ad.getPhone());
                res.addData("price", ad.getPrice());
                res.addData("dateFrom", DateAdapter.formatByDate(ad.getDateFrom(), DateAdapter.SMALL_FORMAT));
                res.addData("dateTo", DateAdapter.formatByDate(ad.getDateTo(), DateAdapter.SMALL_FORMAT));
                res.addData("locsInReg4ShowMap", locsInReg4ShowMap);
                res.addData("statesInReg4ShowMap", statesInReg4ShowMap);
            }
        }
        return res;
    }

    @RequestMapping("/changeAd")
    public String changeAd(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            @RequestParam(value = "wish", required = false) String wish,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            /*@RequestParam(value = "catId", required = false) Long catId,*/
            @RequestParam(value = "dateFrom", required = false) Date dateFrom,
            @RequestParam(value = "dateTo", required = false) Date dateTo,
            @RequestParam(value = "locIds", required = false) Long locIds[],
            /*@RequestParam(value = "previews", required = false) MultipartFile previews[],
             @RequestParam(value = "regionId", required = false) Long regionId,*/
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        Ad ad = adService.getAd(adId);

        if (ad.getAuthor().getId().equals(u.getId()) || u.getUserRole().equals(User.ROLEADMIN)) {
            adService.changeAd(adId, shortName, description, price, dateFrom, dateTo,locIds,email,phone);
        }

        ras.addAttribute("errors", adService.getErrors());
        ras.addAttribute("wish", wish);
        ras.addAttribute("action", action);
        return "redirect:/Main/";
    }

    @RequestMapping("/delete")
    public String delete(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            @RequestParam(value = "wish", required = false) String wish,
            @RequestParam(value = "action", required = false) String action,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        Ad ad = adService.getAd(adId);
        if (User.ROLEADMIN.equals(u.getUserRole()) || Objects.equals(ad.getAuthor().getId(), u.getId())) {
            adService.delete(adId);
        }

        ras.addAttribute("errors", adService.getErrors());
        ras.addAttribute("wish", wish);
        ras.addAttribute("action", action);
        return "redirect:/Main/";
    }

}
