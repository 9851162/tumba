/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
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

/**
 *
 * @author bezdatiuzer
 */
@RequestMapping("/Main")
@Controller
public class mainController extends WebController {
    
    @Autowired
    private AdService adService;
    
    @RequestMapping("/")
    public String getMain (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "short_name", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            RedirectAttributes ras) throws Exception {
        
        model.put("adList",adService.getAds());
        model.put("short_name", shortName);
        model.put("desc", desc);
        model.put("price", price);
        ArrayList<String> ers = new ArrayList();
        /*for(String er:(List<String>)model.get("errors")){
            ers.add(er);
        }*/
        return "main";
    }
    
    @RequestMapping("/addAd")
    public String add (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "short_name") String shortName,
            @RequestParam(value = "description") String desc,
            @RequestParam(value = "price") Double price,
            RedirectAttributes ras) throws Exception {
        ArrayList<String> errors = new ArrayList();
        
        
        adService.create(price,shortName,desc,(long)0);
        for(String er:adService.getErrors()){
            errors.add(er);
        }
        errors.add("error;");

        ras.addAttribute("short_name", shortName);
        ras.addAttribute("desc", desc);
        ras.addAttribute("price", price);
        ras.addFlashAttribute("errors", errors);
        ras.addAttribute("errors", errors);
        return "redirect:/Main/";
    }
    
    @RequestMapping("/registrationResult")
    public String showRegisterResult (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            RedirectAttributes ras) throws Exception {
        
        model.put("name", name);
        model.put("phone", phone);
        model.put("email", email);
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
        
        
        
        model.put("name", name);
        model.put("phone", phone);
        model.put("email", email);
        return "redirect:/Main/";
    }
    
}
