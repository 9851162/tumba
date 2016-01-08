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
public class BilateralCondition {
    
    private Long id;
    private Object conditionFrom;
    private Object conditionTo;

    public BilateralCondition(Long id, Object conditionFrom, Object conditionTo) {
        this.id = id;
        this.conditionFrom = conditionFrom;
        this.conditionTo = conditionTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getFrom() {
        return conditionFrom;
    }

    public void setFrom(Object conditionFrom) {
        this.conditionFrom = conditionFrom;
    }

    public Object getTo() {
        return conditionTo;
    }

    public void setTo(Object conditionTo) {
        this.conditionTo = conditionTo;
    }
    
    
    
}
