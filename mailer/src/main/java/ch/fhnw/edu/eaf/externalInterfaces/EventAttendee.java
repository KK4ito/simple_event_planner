package ch.fhnw.edu.eaf.externalInterfaces;

/**
 * Created by apple on 19.05.17.
 */
public interface EventAttendee {

    Long getId();
    Event getEvent();
    User getUser();
    FoodType getFoodType();

}
