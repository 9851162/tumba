/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import entities.Parametr;
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
        
        model.put("catMap", catService.getFullCatMap());
        model.put("catName", catService.getCatName(catId));
        //model.put("paramTypeMap",catService.getParamTypes());
        model.put("catParams", catService.getParams(catId));
        model.put("params", catService.getAllParams());
           
        return "cats";
    }
    
    @RequestMapping("/params")
    public String showParams (Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
            
        model.put("reqTypeMap",catService.getReqTypes());
        model.put("paramTypeMap",catService.getParamTypes());
        model.put("params", catService.getAllParams());
            
        return "params";
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
        ras.addAttribute("catId", catId);
        return "redirect:./administrating";
    }
    
    @RequestMapping("/createParam")
    public String createParam (Map<String, Object> model,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "paramType", required = false) Integer paramType,
            @RequestParam(value = "reqType", required = false) String reqType,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        int req = Parametr.NOTREQUIRED;
        if(reqType!=null){
            req=Parametr.REQUIRED;
        }
        
        catService.createParam(name, req, paramType);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        return "redirect:./params";
    }
    
    @RequestMapping("/deleteParam")
    public String deleteParam (Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "paramId", required = false) Long paramId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        catService.deleteParam(paramId, catId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        ras.addAttribute("catId", catId);
        return "redirect:./cats";
    }
    
    @RequestMapping("/addParam")
    public String addParam (Map<String, Object> model,
            @RequestParam(value = "catId", required = false) Long catId,
            @RequestParam(value = "paramId", required = false) Long paramId,
            HttpServletRequest request,RedirectAttributes ras) throws Exception {
        
        catService.addParam(catId, paramId);
        ras.addFlashAttribute(ERRORS_LIST_NAME, catService.getErrors());
        ras.addAttribute("catId", catId);
        return "redirect:./cats";
    }
    
}
