/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import entities.User;
import java.util.ArrayList;
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
            RedirectAttributes ras) throws Exception {
        
        model.put("adList",adService.getAds());
        model.put("shortName", shortName);
        model.put("desc", desc);
        model.put("price", price);
        model.put("catList", catService.getCatList());
        ArrayList<String> ers = new ArrayList();
        /*for(String er:(List<String>)model.get("errors")){
            ers.add(er);
        }*/
        return "main";
    }
    
    @RequestMapping("/authorize")
    public String showRegisterResult (Map<String, Object> model,
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
    
}
