package ch.fhnw.edu.eaf.eventmgmt.domain;

import java.io.Serializable;

/**
 * Created by lukasschonbachler on 27.03.17.
 */
public class EventAttendeeFlat implements Serializable {
    public String firstName;
    public String lastName;
    public boolean internal;
    public String foodType;
    public boolean drink;
}
