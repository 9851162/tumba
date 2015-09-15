/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package support.editors;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import support.Converter;
import support.Converter;

/**
 *
 * @author Rice Pavel
 */
@Component
public class DateEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(Converter.getDate(text));
    }

}
