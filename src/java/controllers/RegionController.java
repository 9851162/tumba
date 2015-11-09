/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import entities.Region;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.RegionService;

/**
 *
 * @author bezdatiuzer
 */
@RequestMapping("/Regions")
@Controller
public class RegionController extends WebController {
    
    @Autowired
    private RegionService regionService;
    
    @RequestMapping("/select")
    public String showRegions(Map<String, Object> model,
            HttpServletRequest request,
            RedirectAttributes ras) throws Exception {
        List<String>errors = (List<String>)model.get(ERRORS_LIST_NAME);
        if(errors==null){
            errors=new ArrayList();
        }
        
        Region region = (Region)request.getSession().getAttribute(MOUNTED_REGION_SESSION_NAME);
        User u = authManager.getCurrentUser();
        Long userId = null;
        if (u != null) {
            userId = u.getId();
            if(u.isHomeSet()){
                model.put("homeSet", u.getHomeSet());
            }
        }
        
        if(region==null){
            region=regionService.getDefaultRegion(userId);
            request.getSession().setAttribute(MOUNTED_REGION_SESSION_NAME, region);
        }
        errors.addAll(regionService.getErrors());
        
        List<Region> availableRegions = regionService.getAvailableRegions(region,u);
        model.put("availableRegions", availableRegions);
        model.put("states",regionService.getNotEmptyStates());
        model.put(ERRORS_LIST_NAME,errors);
        
        
        return "region4Users";
    }
    
}
