/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package support;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author bezdatiuzer
 */
public class JsonResponse {
    private Boolean status = null;
    private String message = "";
    private HashMap<String,Object> data = new HashMap();
    
    public static JsonResponse getInstance(){
        return new JsonResponse();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void addMessage(String msg){
        this.message+=";"+msg;
    }

    public HashMap<String,Object> getData() {
        return data;
    }

    public void setData(HashMap<String,Object> data) {
        this.data = data;
    }
    
    public void addData(String name,Object o){
        this.data.put(name,o);
    }
    
}
