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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.AdService;
import service.MessageService;

/**
 *
 * @author Алексей
 */
@RequestMapping("/Message")
@Controller
public class MessageController extends WebController {
    
    @Autowired
    MessageService msgService;
    
    @Autowired
    AdService adService;
    
    @RequestMapping("/send")
    public String administrating (Map<String, Object> model,
            @RequestParam(value = "adId", required = false) Long adId,
            @RequestParam(value = "message", required = false) String text,
            @RequestParam(value = "wish", required = false) String wish,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
            User u = authManager.getCurrentUser();
            if(u!=null){
                msgService.create(u, text, adService.getAd(adId));
                ras.addFlashAttribute(ERRORS_LIST_NAME, msgService.getErrors());
            }
            ras.addFlashAttribute("wish", wish);
        return "/Main/";
    }
    
}
