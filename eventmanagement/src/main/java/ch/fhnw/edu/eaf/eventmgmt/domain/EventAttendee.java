package ch.fhnw.edu.eaf.eventmgmt.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lukasschonbachler on 27.03.17.
 */
@Entity
public class EventAttendee implements Serializable{

    public enum FoodType {
        VEGI,
        NORMAL,
        NONE
    }

    @Id
    @GeneratedValue
    private Long Id;

    @ManyToOne
    @JoinColumn
    private Event event;

    @ManyToOne
    @JoinColumn
    private User user;

    public Long getId() {
        return Id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

}
