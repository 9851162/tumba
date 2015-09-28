/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.parent.WebController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author bezdatiuzer
 */
@RequestMapping("/Images")
@Controller
public class ImageController extends WebController {
    
    /*@RequestMapping("/")
    public File getImg (Map<String, Object> model,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "id",required = false) String id,
            HttpServletRequest request,
            RedirectAttributes ras) throws Exception {
        
        File file = null;
        if(name!=null&&!name.equals("")&&id!=null&&!id.equals("")){
            file = new File("/usr/local/seller/preview/"+id+"/"+name);
            if(!file.exists()){
                file=null;
            }
        }
        return file;
    }*/
    
    @RequestMapping("/")
    public ResponseEntity<byte[]> getImage(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "id",required = false) String id) throws IOException {
        File file = new File("/usr/local/seller/preview/"+id+"/"+name);
        if(!file.exists()){
            return null;
        }
        InputStream in = new FileInputStream(file);
                //new File("/usr/local/seller/preview/"+id+"/"+name).;
                //servletContext.getResourceAsStream("/usr/local/seller/preview/"+id+"/"+name);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
    }
    
}
