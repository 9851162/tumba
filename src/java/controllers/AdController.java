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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.AdService;

/**
 *
 * @author bezdatiuzer
 */
@RequestMapping("/Ad")
@Controller
public class AdController extends WebController {
    
    @Autowired
    private AdService adService;
    
    @RequestMapping("/add")
    public String add (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "short_name", required = false) String shortName,
            @RequestParam(value = "description", required = false) String desc,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "previews", required = false) MultipartFile previews[],
            RedirectAttributes ras) throws Exception {
        ArrayList<String> errors = new ArrayList();
        
        User authedUser = authManager.getCurrentUser();
        if(authManager.getCurrentUser()!=null){
            email = authedUser.getEmail();
        }
        
        adService.create(email,price,previews,shortName,desc,(long)0);
        for(String er:adService.getErrors()){
            errors.add(er);
        }
        
        if(!errors.isEmpty()){
            ras.addAttribute("short_name", shortName);
            ras.addAttribute("description", desc);
            ras.addAttribute("price", price);
            //ras.addFlashAttribute("errors", errors);
        }
        //errors.add("user="+authManager.getCurrentUser().getEmail()+", "+authManager.getCurrentUser().getName());
        ras.addFlashAttribute("errors", errors);
        
        return "redirect:/Main/";
    }
    
    /*@RequestMapping("/list")
    public String getList (Map<String, Object> model,
            HttpServletRequest request,
            RedirectAttributes ras) throws Exception {
            model.put("adList",adService.getAds());
        return "ads";
    }*/
    
}
