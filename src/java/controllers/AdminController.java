/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.CategoryService;

/**
 *
 * @author bezdatiuzer
 */
@RequestMapping("/Admin")
@Controller
public class AdminController extends WebController {
    
    @Autowired
    CategoryService catService;
    
    @RequestMapping("/administrating")
    public String administrating (Map<String, Object> model,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        model.put("catMap", catService.getFullCatMap());
           
        return "admin";
    }
    
    
    
}
