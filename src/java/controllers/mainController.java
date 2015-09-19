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
        return "./main.jsp";
    }
    
}
