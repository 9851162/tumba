/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import entities.Ad;
import entities.Region;
import entities.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.AdService;
import service.RegionService;
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
    
    @RequestMapping("/add")
    public String add (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "previews", required = false) MultipartFile previews[],
            
            @RequestParam(value = "booleanIds", required = false) Long booleanIds[],
            @RequestParam(value = "booleanVals", required = false) String booleanVals[],
            @RequestParam(value = "stringIds", required = false) Long stringIds[],
            @RequestParam(value = "stringVals", required = false) String stringVals[],
            @RequestParam(value = "numIds", required = false) Long numIds[],
            @RequestParam(value = "numVals", required = false) Double numVals[],
            @RequestParam(value = "dateIds", required = false) Long dateIds[],
            @RequestParam(value = "dateVals", required = false) Date dateVals[],
            @RequestParam(value = "selIds", required = false) Long selIds[],
            @RequestParam(value = "selVals", required = false) Long selVals[],
            
            @RequestParam(value = "regionId", required = false) Long regionId,
            //@RequestParam(value = "localIds", required = false) Long localIds[],
            
            @RequestParam(value = "multyIds", required = false) Long multyIds[],
            @RequestParam(value = "multyVals", required = false) String multyVals[],
            
            RedirectAttributes ras) throws Exception {
        ArrayList<String> errors = new ArrayList();
        
        User authedUser = authManager.getCurrentUser();
        if(authedUser!=null){
            email = authedUser.getEmail();
        }
        
        Region region = (Region)request.getSession().getAttribute(MOUNTED_REGION_SESSION_NAME);
        if(regionId!=null){
            if(regionId.equals(0L)){
                region.setAllRussia(Boolean.TRUE);
            }else{
                region = regionService.getRegion(regionId);
            }
        }
        //Long localIds[] = regionService.getLocIds(regionId,region);
        
        adService.create(catId,email,price,previews,shortName,desc,booleanIds,booleanVals,
                stringIds,stringVals,numIds,numVals,dateIds,dateVals,selIds,selVals,multyIds,multyVals,region);
        /*for(String er:adService.getErrors()){
            errors.add(er);
        }*/
        errors.addAll(adService.getErrors());
        
        if(!errors.isEmpty()){
            ras.addFlashAttribute("errors", errors);
            ras.addFlashAttribute("shortName", shortName);
            ras.addFlashAttribute("description", desc);
            ras.addFlashAttribute("price", price);
            ras.addFlashAttribute("catId", catId);
            //ras.addAttribute("previews", previews);
        }
        //errors.add("user="+authManager.getCurrentUser().getEmail()+", "+authManager.getCurrentUser().getName());
        //errors.add("s:"+errors.size());
        //ras.addFlashAttribute(ERRORS_LIST_NAME, errors);
        
        return "redirect:/Main/";
    }
    
    @RequestMapping("/buy")
    public String buy (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        
        User u = authManager.getCurrentUser();
            if(u!=null){
                adService.buy(u,adId);
                ras.addFlashAttribute(ERRORS_LIST_NAME, adService.getErrors());
            }
        ras.addAttribute("wish", wish);
        return "redirect:/Main/";
    }
    
    @RequestMapping("/changeStatus")
    public String changeStatus (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        ArrayList<String> errors = new ArrayList();
        User u = authManager.getCurrentUser();
            if(u!=null&&User.ROLEADMIN.equals(u.getUserRole())){
                adService.changeStatus(status,adId);
            }
        errors.addAll(adService.getErrors());
        ras.addAttribute("wish", wish);
        ras.addFlashAttribute(ERRORS_LIST_NAME, errors);
        return "redirect:/Main/";
    }
    
    @RequestMapping("/setChosenUnchosen")
    @ResponseBody
    public JsonResponse setChosen (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            RedirectAttributes ras) throws Exception {
        
            User u = authManager.getCurrentUser();
            if(u!=null){
                adService.setUnsetChosen(u.getId(), adId);
            }
        JsonResponse res = new JsonResponse();
        res.setStatus(Boolean.TRUE);
        if(!adService.getErrors().isEmpty()){
            res.setMessage(adService.getErrorsAsString());
            res.setStatus(Boolean.FALSE);
        }
        return res;
    }
    
    @RequestMapping("/addToComparison")
    @ResponseBody
    public JsonResponse addToComparison (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            RedirectAttributes ras) throws Exception {
        
            User u = authManager.getCurrentUser();
            if(u!=null){
                List<Ad> ads = (List)request.getSession().getAttribute(COMPARISON);
                if(ads==null){
                    ads = new ArrayList();
                }
                Ad ad = adService.getAd(adId);
                if(!ads.contains(ad)){
                    if(ads.size()<20){
                        ads.add(ad);
                    }
                }
                request.getSession().setAttribute(COMPARISON, ads);
            }
        JsonResponse res = new JsonResponse();
        res.setStatus(Boolean.TRUE);
        if(!adService.getErrors().isEmpty()){
            res.setMessage(adService.getErrorsAsString());
            res.setStatus(Boolean.FALSE);
        }
        return res;
    }
    
    @RequestMapping("/removeFromComparison")
    public String removeFromComparison (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            @RequestParam(value = "wish", required = false) Long wish,
            RedirectAttributes ras) throws Exception {
        
            User u = authManager.getCurrentUser();
            if(u!=null){
                List ads = (List)request.getSession().getAttribute(COMPARISON);
                if(ads==null){
                    ads = new ArrayList();
                }
                Ad ad = adService.getAd(adId);
                if(ads.contains(ad)){
                    ads.remove(ad);
                }
                request.getSession().setAttribute(COMPARISON, ads);
            }
            ras.addAttribute("wish", wish);
        return "redirect:/Main/comparison";
    }
    
    @RequestMapping("/watch")
    @ResponseBody
    public JsonResponse watch (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "adId", required = false) Long adId,
            RedirectAttributes ras) throws Exception {
        
            
                String ip = request.getRemoteAddr();
                adService.addWatchFromIp(adId, ip);
            
        JsonResponse res = new JsonResponse();
        res.setStatus(Boolean.TRUE);
        if(!adService.getErrors().isEmpty()){
            res.setMessage(adService.getErrorsAsString());
            res.setStatus(Boolean.FALSE);
        }
        return res;
    }
    
}
