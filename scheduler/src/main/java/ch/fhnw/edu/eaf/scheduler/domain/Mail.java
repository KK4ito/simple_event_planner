package ch.fhnw.edu.eaf.scheduler.domain;

import java.util.Map;

/**
 * Created by lukasschonbachler on 13.05.17.
 */
public class Mail {
    public String token;
    public String from;
    public String to;
    public String cc;
    public String subject;
    public String body;
    public long eventId;
    public String[] keys;
    public String[] values;
    public Map<String, String> parameters;
}
