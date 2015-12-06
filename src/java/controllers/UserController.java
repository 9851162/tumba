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
import org.springframework.web.multipart.MultipartFile;
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
        model.put("avatarPath", UserService.getAvatarPath(u.getId()));

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
                    if (newPass.length() > 3) {
                        userService.changeUserPass(AuthManager.md5Custom(newPass), u);
                    } else {
                        errors.add("Длина пароля должна быть не менее 4 символов");
                    }
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

    @RequestMapping("/uploadAvatar")
    public String uploadAvatar(Map<String, Object> model,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            HttpServletRequest request,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        User u = authManager.getCurrentUser();

        if (avatar != null && !avatar.isEmpty()) {
            userService.uploadAvatar(u, avatar);
        }

        errors.addAll(userService.getErrors());
        ras.addFlashAttribute(ERRORS_LIST_NAME, errors);
        return "redirect:/User/me";
    }

    @RequestMapping("/passRecovery")
    public String passRecovery(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "hash", required = false) String hash,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        List<String> msgs = new ArrayList();

        if (email != null) {
            User u = userService.getUserByMail(email);
            if (u != null) {
                    if (hash != null&&!hash.equals("")) {
                        if(!hash.equals(u.getHash())){
                            errors.add("возможно ссылка по которой Вы прошли устарела или была изменена, попробуйте повторить попытку восстановления");
                            model.put(ERRORS_LIST_NAME, errors);
                        }else{
                            ras.addAttribute("email", email);
                            ras.addAttribute("hash", hash);
                            return "redirect:/User/passRecovery";
                        }
                    }else{
                        userService.sendPassRecoveryMail(u);
                        errors.addAll(userService.getErrors());
                        if (errors.isEmpty()) {
                            msgs.add("на указанный Вами email было выслано письмо с дальнейшей инструкцией по восстановлению.");
                            model.put("messages", msgs);
                        }
                    }
            } else {
                errors.add("Не удалось найти пользователя с таким email "+email);
            }
        }

        return "passRecovery";
    }

    @RequestMapping("/passUpdate")
    public String passUpdate(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "hash", required = false) String hash,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "checkPassword", required = false) String checkPassword,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        List<String> msgs = new ArrayList();
        User u = userService.getUserByMail(email);
        if (u != null) {
            userService.updatePassword(u,hash,password,checkPassword);
            errors.addAll(userService.getErrors());
            if(errors.isEmpty()){
                msgs.add("пароль был успешно обновлен");
            }else{
                ras.addAttribute("email", email);
                ras.addAttribute("hash", hash);
                return "redirect:/User/passRecovery";
            }
        } else {
            errors.add("Не удалось найти пользователя с таким email "+email);
        }
        ras.addFlashAttribute("messages", msgs);
        ras.addFlashAttribute(ERRORS_LIST_NAME, errors);
        return "redirect:/Main/";
    }

    @RequestMapping("/activation")
    public String activation(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "hash", required = false) String hash,
            RedirectAttributes ras) throws Exception {
        List<String> errors = new ArrayList();
        List<String> msgs = new ArrayList();

        User u = userService.getUserByMail(email);
        if (u != null) {
            if (hash.equals(u.getHash())) {
                userService.activate(u);
                msgs.add("вы успешно активировали свой аккаунт");
                ras.addFlashAttribute("messages", msgs);
            } else if (!u.isActive()) {
                errors.add("не удалось выполнить активацию, возможно ссылка по которой Вы прошли была изменена");
            }
        } else {
            errors.add("пользователь с email " + email + " не существует, попробуйте его зарегистрировать");
        }
        ras.addFlashAttribute(ERRORS_LIST_NAME, errors);
        return "redirect:/Main/";
    }
}
