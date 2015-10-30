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
    RegionService regService;
    
    @RequestMapping("/administrating")
    public String administrating (Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        /*model.put("catMap", catService.getFullCatMap());
        model.put("catName", catService.getCatName(catId));
        model.put("paramTypeMap",catService.getParamTypes());
        model.put("reqTypeMap",catService.getReqTypes());
        model.put("params", catService.getParams(catId))*/;
           
        return "admin";
    }
    
    @RequestMapping("/cats")
    public String showCats (Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        model.put("nestingCatsMap", catService.getNestingMapOfCats());
        model.put("catName", catService.getCatName(catId));
        model.put("paramTypeMap",catService.getParamTypes());
        model.put("reqTypeMap",catService.getReqTypes());
        model.put("catParamLinks", catService.getParamLinks(catId));
        model.put("params", catService.getAllParams());
           
        return "cats";
    }
    
    @RequestMapping("/params")
    public String showParams (Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
            
        //model.put("reqTypeMap",catService.getReqTypes());
        model.put("paramTypeMap",catService.getParamTypes());
        model.put("params", catService.getAllParams());
            
        return "params";
    }
    
    @RequestMapping("/regions")
    public String showRegions (Map<String, Object> model,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        
           
        return "regions";
    }
    
    
    
    @RequestMapping("/cre8Country")
    public String cre8Country (Map<String, Object> model,
            HttpServletRequest request,
            @RequestParam(value = "name", required = false) String name,
            RedirectAttributes ras) throws Exception {
        
            User u = authManager.getCurrentUser();
            if(u!=null){
                regService.createCountry(name);
                if(!regService.getErrors().isEmpty()){
                    ras.addFlashAttribute(ERRORS_LIST_NAME, regService.getErrors());
                }
            }
        return "redirect:/Admin/regions";
    }
    
    @RequestMapping("/addCat")
    public String addCat (Map<String, Object> model,
            @RequestParam(value = "parentId", required = false) Long parentId,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "name", required = false) String name,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        catService.create(parentId, name);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        ras.addAttribute("catId", catId);
        return "redirect:./cats";
    }
    
    @RequestMapping("/deleteCat")
    public String deleteCat (Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        catService.delete(catId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        //ras.addAttribute("catId", catId);
        return "redirect:./cats";
    }
    
    @RequestMapping("/createParam")
    public String createParam (Map<String, Object> model,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "paramType", required = false) Integer paramType,
            /*@RequestParam(value = "reqType", required = false) String reqType,*/
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        /*int req = Parametr.NOTREQUIRED;
        if(reqType!=null){
            req=Parametr.REQUIRED;
        }*/
        
        catService.createParam(name, paramType);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        return "redirect:./params";
    }
    
    @RequestMapping("/deleteParam")
    public String deleteParam (Map<String, Object> model,
            @RequestParam(value = "paramId", required = false) Long paramId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        catService.deleteParam(paramId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        return "redirect:./params";
    }
    
    @RequestMapping("/deleteParamFromCat")
    public String deleteParamFromCat (Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "paramId", required = false) Long paramId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        catService.deleteParamFromCat(paramId, catId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        ras.addAttribute("catId", catId);
        return "redirect:./cats";
    }
    
    @RequestMapping("/addParam")
    public String addParam (Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "req", required = false) String req,
            @RequestParam(value = "paramId", required = false) Long paramId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        catService.addParam(catId,req,paramId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        ras.addAttribute("catId", catId);
        return "redirect:./cats";
    }
    
    @RequestMapping("/addParamOption")
    public String addParamOption (Map<String, Object> model,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "paramId", required = false) Long paramId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        catService.addParamOption(paramId, name);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        return "redirect:./params";
    }
    
    @RequestMapping("/deleteParamOption")
    public String deleteParamOption (Map<String, Object> model,
            @RequestParam(value = "paramOptionId", required = false) Long paramOptionId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        catService.deleteParamOption(paramOptionId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        return "redirect:./params";
    }
    
}
