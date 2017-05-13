package ch.fhnw.edu.eaf.mailer;

import java.util.Map;

/**
 * Created by lukasschonbachler on 13.05.17.
 */
public class Mail {
    public String to;
    public String cc;
    public String subject;
    public String body;
    Map<String, String> parameters;
}
