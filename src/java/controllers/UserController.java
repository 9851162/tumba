/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
    
}
