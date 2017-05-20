package ch.fhnw.edu.eaf.mailer;

import ch.fhnw.edu.eaf.externalInterfaces.Body;

import java.util.Map;

/**
 * Created by lukasschonbachler on 13.05.17.
 */
public class Mail {
    public String to;
    public String cc;
    public String subject;
    public Body body;
    Map<String, String> parameters;
}
