/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import entities.User;
import java.util.ArrayList;
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
    
    private final static String USER_ID_SESSION_NAME = "userId";
    private final static String USER_NAME_SESSION_NAME = "userName";
    
    
    @RequestMapping("/")
    public String getMain (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        
        User u = authManager.getCurrentUser();
        Long userId = null;
        if(u!=null){
            userId=u.getId();
        }
        
        model.put("adList",adService.getAds(wish));
        model.put("chosenAdsMap",adService.getChosenAdMap(userId));
        model.put("shortName", shortName);
        model.put("description", desc);
        model.put("price", price);
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap",catService.getCatIdParamsMap());
        model.put("reqTypeMap",catService.getReqTypes());
        model.put("wish",wish);
        //model.put("paramMap",catService.getParamsMap());
        ArrayList<String> ers = (ArrayList<String>)model.get("errors");
        if(ers==null){
            ers = new ArrayList();
        }
        ers.addAll(adService.getErrors());
        ers.addAll(catService.getErrors());
        //ers.add("err");
        model.put(ERRORS_LIST_NAME,ers);
        return "main";
    }
    
    @RequestMapping("/chosen")
    public String getChosen (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            //@RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        
        User u = authManager.getCurrentUser();
        
        model.put("adList",adService.getChosenAds(u.getId()));
        model.put("chosenAdsMap",adService.getChosenAdMap(u.getId()));
        //model.put("chosenList",adService.getChosenAds(u.getId()));
        model.put("shortName", shortName);
        model.put("description", desc);
        model.put("price", price);
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap",catService.getCatIdParamsMap());
        model.put("reqTypeMap",catService.getReqTypes());
        //model.put("wish",wish);
        //model.put("paramMap",catService.getParamsMap());
        ArrayList<String> ers = (ArrayList<String>)model.get("errors");
        if(ers==null){
            ers = new ArrayList();
        }
        ers.addAll(adService.getErrors());
        ers.addAll(catService.getErrors());
        //ers.add("err");
        model.put(ERRORS_LIST_NAME,ers);
        return "main";
    }
    
    @RequestMapping("/sales")
    public String getSales (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        
        User u = authManager.getCurrentUser();
        
        model.put("adList",adService.getSales(u.getId()));
        model.put("chosenAdsMap",adService.getChosenAdMap(u.getId()));
        //model.put("salesList",adService.getSales(u.getId()));
        model.put("shortName", shortName);
        model.put("description", desc);
        model.put("price", price);
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap",catService.getCatIdParamsMap());
        model.put("reqTypeMap",catService.getReqTypes());
        model.put("wish",wish);
        //model.put("paramMap",catService.getParamsMap());
        ArrayList<String> ers = (ArrayList<String>)model.get("errors");
        if(ers==null){
            ers = new ArrayList();
        }
        ers.addAll(adService.getErrors());
        ers.addAll(catService.getErrors());
        //ers.add("err");
        model.put(ERRORS_LIST_NAME,ers);
        return "main";
    }
    
    @RequestMapping("/purchases")
    public String getPurchases (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "shortName", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "wish", required = false) String wish,
            RedirectAttributes ras) throws Exception {
        
        User u = authManager.getCurrentUser();
        
        model.put("adList",adService.getPurchases(u.getId()));
        //model.put("purchasesList",adService.getPurchases(u.getId()));
        model.put("shortName", shortName);
        model.put("description", desc);
        model.put("price", price);
        model.put("catList", catService.getCatList());
        model.put("catMap", catService.getCatMap());
        model.put("catParamsMap",catService.getCatIdParamsMap());
        model.put("reqTypeMap",catService.getReqTypes());
        model.put("wish",wish);
        //model.put("paramMap",catService.getParamsMap());
        ArrayList<String> ers = (ArrayList<String>)model.get("errors");
        if(ers==null){
            ers = new ArrayList();
        }
        ers.addAll(adService.getErrors());
        ers.addAll(catService.getErrors());
        //ers.add("err");
        model.put(ERRORS_LIST_NAME,ers);
        return "main";
    }
    
    @RequestMapping("/authorize")
    public String authorize (Map<String, Object> model,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
            User user = authManager.getCurrentUser();
            
            request.getSession().setAttribute(USER_NAME_SESSION_NAME, user.getName());
            request.getSession().setAttribute(USER_ID_SESSION_NAME, user.getId());
            request.getSession().setAttribute("role", user.getUserRole());
        //ras.addAttribute("role", "admin");
           
        return "redirect:/Main/";
    }
    
    @RequestMapping("/registration")
    public String register (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "passconfirm", required = false) String passconfirm,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            RedirectAttributes ras) throws Exception {
        
        userService.createUser(phone, email, password, name, passconfirm,User.ROLEADMIN);
        
        /*model.put("name", name);
        model.put("phone", phone);
        model.put("email", email);*/
        /*ras.addAttribute("name", name);
        ras.addAttribute("phone", phone);
        ras.addAttribute("email", email);*/
        ras.addAttribute("errors", userService.getErrors());
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
