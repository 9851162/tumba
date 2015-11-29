/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import entities.User;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.UserService;

/**
 *
 * @author bezdatiuzer
 */
@Controller
@RequestMapping("/User")
public class UserController extends WebController  {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping("/me")
    public String getMyOptions (Map<String, Object> model,
            HttpServletRequest request,
            RedirectAttributes ras) throws Exception {
            
        User u = authManager.getCurrentUser();
        
        model.put("user", u);
        
        return "profile";
    }
    
}
