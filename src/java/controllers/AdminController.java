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
import org.springframework.web.bind.annotation.RequestParam;
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
        
        model.put("catMap", catService.getFullCatIdMap());
           
        return "admin";
    }
    
    @RequestMapping("/addCat")
    public String addCat (Map<String, Object> model,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "name", required = false) String name,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        catService.create(parentId, name);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        return "redirect:./administrating";
    }
    
}
