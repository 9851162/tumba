/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package support;

/**
 *
 * @author bezdatiuzer
 */
public class JsonResponse {
    private Boolean status = null;
    private String message = "";
    
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
    
}
