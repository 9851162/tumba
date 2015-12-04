/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static javax.management.Query.value;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.UserService;
import support.AuthManager;

/**
 *
 * @author bezdatiuzer
 */
@Controller
@RequestMapping("/User")
public class UserController extends WebController {

    @Autowired
    private UserService userService;

    @RequestMapping("/me")
    public String getMyOptions(Map<String, Object> model,
            HttpServletRequest request,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();

        model.put("user", u);

        return "profile";
    }

    @RequestMapping("/change")
    public String change(Map<String, Object> model,
            @RequestParam(value = "paramName", required = false) String param,
            @RequestParam(value = "paramValue", required = false) String value,
            HttpServletRequest request,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {
            userService.changeUserParam(param, value, u.getId());
        }
        ras.addFlashAttribute(ERRORS_LIST_NAME, userService.getErrors());
        return "redirect:/User/me";
    }

    @RequestMapping("/changePass")
    public String changePass(Map<String, Object> model,
            @RequestParam(value = "oldPass", required = false) String oldPass,
            @RequestParam(value = "newPass", required = false) String newPass,
            @RequestParam(value = "checkPass", required = false) String checkPass,
            HttpServletRequest request,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        User u = authManager.getCurrentUser();
        if (u != null) {
            if (u.getPassword().equals(AuthManager.md5Custom(oldPass))) {
                if (newPass.equals(checkPass)) {
                    userService.changeUserPass(AuthManager.md5Custom(newPass),u);
                } else {
                    errors.add("новый пароль не совпадает");
                }
            } else {
                errors.add("введен не верный пароль");
            }
        }
        errors.addAll(userService.getErrors());
        ras.addFlashAttribute(ERRORS_LIST_NAME, errors);
        return "redirect:/User/me";
    }

}
