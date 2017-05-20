package ch.fhnw.edu.eaf.mailer;

import org.stringtemplate.v4.ST;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MailHelper {

    static final long ONE_MINUTE_IN_MILLISECONDS=60000;

    /**
     * Parametrizes a given text with the passed parameters.
     * The parameters are passed in a map with the key being the placeholder in the text that is to be replaced by
     * the value.
     *
     * @param body          Text to process
     * @param data          Body-Wrapper-object containing all the data for an event
     * @return              Processed text
     */
    public static String prepareText(String body, Map<String, String> parameters){
        ST template = new ST(body);

        Iterator it = parameters.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if(Boolean.getBoolean(entry.getValue().toString())) {
                template.add(entry.getKey().toString(), Boolean.parseBoolean(entry.getValue().toString()));
            } else {
                template.add(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        String text = template.render();
        return text;
    }
}
