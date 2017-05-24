package ch.fhnw.edu.eaf.scheduler.domain;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class EventAttendee {

    public enum FoodType {
        VEGI,
        NORMAL,
        NONE
    }

    public Long Id;
    public Event event;
    public User user;
    public FoodType foodType;
    public boolean drink;


}
