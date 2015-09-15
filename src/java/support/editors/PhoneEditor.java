/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package support.editors;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import support.StringAdapter;
import support.enums.ValidatorTypes;
import support.filterValidator.ChainValidator;

/**
 *
 * @author bezdatiuzer
 */
@Component
public class PhoneEditor extends PropertyEditorSupport {

    public List<String> error = new ArrayList();
    
    public String getPhone(Object ob) {
        /*if(ob.getClass().equals(Double.class)){
            BigDecimal bd = BigDecimal.valueOf((double)ob);
            ob=bd.longValue();
        }*/
        ChainValidator ch = ChainValidator.getInstance(ValidatorTypes.DIGITSFILTER);
        ch.addChain(ValidatorTypes.PHONEFILTER);
        ch.execute(ob);
        error.addAll(ch.getErrors());
        return StringAdapter.getString(ch.getData());
    }

}
