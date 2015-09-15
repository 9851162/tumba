/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Rice Pavel
 */
public class Converter {

    private final static String[] formats = {"dd.MM.yyyy HH:mm","dd.MM.yyyy", "yyyy-MM-dd"};

    public static Date getDate(String text) throws IllegalArgumentException {
        Date date = null;
        if (text != null) {
            for (String format : formats) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                try {
                    date = dateFormat.parse(text);
                } catch (ParseException ex) {
                }
                if (date != null) {
                    break;
                }
            }
        }
        return date;
    }

}
