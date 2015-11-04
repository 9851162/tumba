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
import service.CategoryService;
import service.RegionService;
import service.UserService;

/**
 *
 * @author bezdatiuzer
 */
@RequestMapping("/Admin")
@Controller
public class AdminController extends WebController {

    @Autowired
    CategoryService catService;
    
    @Autowired
    UserService userService;

    @Autowired
    RegionService regService;

    @RequestMapping("/administrating")
    public String administrating(Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {
        /*model.put("catMap", catService.getFullCatMap());
         model.put("catName", catService.getCatName(catId));
         model.put("paramTypeMap",catService.getParamTypes());
         model.put("reqTypeMap",catService.getReqTypes());
         model.put("params", catService.getParams(catId))*/;

        return "admin";
    }

    @RequestMapping("/cats")
    public String showCats(Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        model.put("nestingCatsMap", catService.getNestingMapOfCats());
        model.put("catName", catService.getCatName(catId));
        model.put("paramTypeMap", catService.getParamTypes());
        model.put("reqTypeMap", catService.getReqTypes());
        model.put("catParamLinks", catService.getParamLinks(catId));
        model.put("params", catService.getAllParams());

        return "cats";
    }

    @RequestMapping("/params")
    public String showParams(Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        //model.put("reqTypeMap",catService.getReqTypes());
        model.put("paramTypeMap", catService.getParamTypes());
        model.put("params", catService.getAllParams());

        return "params";
    }
    
    @RequestMapping("/users")
    public String showUsers(Map<String, Object> model,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        model.put("users", userService.getUsers());

        return "users";
    }
    
    @RequestMapping("/setRole")
    public String setRole(Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {
        
        
        
        return "redirect:/Admin/users";
    }

    @RequestMapping("/regions")
    public String showRegions(Map<String, Object> model,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "countryId", required = false) Long countryId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        model.put("countries", regService.getCountries());
        model.put("states", regService.getStates(countryId));
        model.put("localities", regService.getLocalities(stateId));
        if (countryId != null) {
            model.put("country", regService.getCountry(countryId));
        }
        if (stateId != null) {
            model.put("state", regService.getState(stateId));
        }
        return "regions";
    }

    @RequestMapping("/addCountry")
    public String addCountry(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "countryId", required = false) Long countryId,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {
            regService.createCountry(name);
            if (!regService.getErrors().isEmpty()) {
                ras.addFlashAttribute(ERRORS_LIST_NAME, regService.getErrors());
            }
        }
        ras.addAttribute("stateId", stateId);
        ras.addAttribute("countryId", countryId);
        return "redirect:/Admin/regions";
    }

    @RequestMapping("/addState")
    public String addState(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "countryId", required = false) Long countryId,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {
            regService.createState(name, countryId);
            if (!regService.getErrors().isEmpty()) {
                ras.addFlashAttribute(ERRORS_LIST_NAME, regService.getErrors());
            }
        }
        ras.addAttribute("stateId", stateId);
        ras.addAttribute("countryId", countryId);
        return "redirect:/Admin/regions";
    }

    @RequestMapping("/addLocality")
    public String addLocality(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "countryId", required = false) Long countryId,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {
            regService.createLocality(name, stateId);
            if (!regService.getErrors().isEmpty()) {
                ras.addFlashAttribute(ERRORS_LIST_NAME, regService.getErrors());
            }
        }
        ras.addAttribute("stateId", stateId);
        ras.addAttribute("countryId", countryId);
        return "redirect:/Admin/regions";
    }

    @RequestMapping("/deleteCountry")
    public String deleteCountry(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "countryId", required = false) Long countryId,
            @RequestParam(value = "deletingCountryId", required = false) Long deletingCountryId,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {
            regService.deleteCountry(deletingCountryId);
            if (!regService.getErrors().isEmpty()) {
                ras.addFlashAttribute(ERRORS_LIST_NAME, regService.getErrors());
            }
        }
        ras.addAttribute("stateId", stateId);
        ras.addAttribute("countryId", countryId);
        return "redirect:/Admin/regions";
    }

    @RequestMapping("/deleteState")
    public String deleteState(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "deletingStateId", required = false) Long deletingStateId,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "countryId", required = false) Long countryId,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {
            regService.deleteState(deletingStateId);
            if (!regService.getErrors().isEmpty()) {
                ras.addFlashAttribute(ERRORS_LIST_NAME, regService.getErrors());
            }
        }
        ras.addAttribute("stateId", stateId);
        ras.addAttribute("countryId", countryId);
        return "redirect:/Admin/regions";
    }

    @RequestMapping("/deleteLocality")
    public String deleteLocality(Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "stateId", required = false) Long stateId,
            @RequestParam(value = "countryId", required = false) Long countryId,
            @RequestParam(value = "deletingLocalityId", required = false) Long deletingLocalityId,
            RedirectAttributes ras) throws Exception {

        User u = authManager.getCurrentUser();
        if (u != null) {
            regService.deleteLocality(deletingLocalityId);
            if (!regService.getErrors().isEmpty()) {
                ras.addFlashAttribute(ERRORS_LIST_NAME, regService.getErrors());
            }
        }
        ras.addAttribute("stateId", stateId);
        ras.addAttribute("countryId", countryId);
        return "redirect:/Admin/regions";
    }

    @RequestMapping("/addCat")
    public String addCat(Map<String, Object> model,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "name", required = false) String name,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        catService.create(parentId, name);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        ras.addAttribute("catId", catId);
        return "redirect:./cats";
    }

    @RequestMapping("/deleteCat")
    public String deleteCat(Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        catService.delete(catId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        //ras.addAttribute("catId", catId);
        return "redirect:./cats";
    }

    @RequestMapping("/createParam")
    public String createParam(Map<String, Object> model,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "paramType", required = false) Integer paramType,
            /*@RequestParam(value = "reqType", required = false) String reqType,*/
            HttpServletRequest request, RedirectAttributes ras) throws Exception {
        /*int req = Parametr.NOTREQUIRED;
         if(reqType!=null){
         req=Parametr.REQUIRED;
         }*/

        catService.createParam(name, paramType);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        return "redirect:./params";
    }

    @RequestMapping("/deleteParam")
    public String deleteParam(Map<String, Object> model,
            @RequestParam(value = "paramId", required = false) Long paramId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        catService.deleteParam(paramId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        return "redirect:./params";
    }

    @RequestMapping("/deleteParamFromCat")
    public String deleteParamFromCat(Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "paramId", required = false) Long paramId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        catService.deleteParamFromCat(paramId, catId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        ras.addAttribute("catId", catId);
        return "redirect:./cats";
    }

    @RequestMapping("/addParam")
    public String addParam(Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "req", required = false) String req,
            @RequestParam(value = "paramId", required = false) Long paramId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        catService.addParam(catId, req, paramId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        ras.addAttribute("catId", catId);
        return "redirect:./cats";
    }

    @RequestMapping("/addParamOption")
    public String addParamOption(Map<String, Object> model,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "paramId", required = false) Long paramId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        catService.addParamOption(paramId, name);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        return "redirect:./params";
    }

    @RequestMapping("/deleteParamOption")
    public String deleteParamOption(Map<String, Object> model,
            @RequestParam(value = "paramOptionId", required = false) Long paramOptionId,
            HttpServletRequest request, RedirectAttributes ras) throws Exception {

        catService.deleteParamOption(paramOptionId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        return "redirect:./params";
    }

}
