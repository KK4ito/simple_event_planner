package ch.fhnw.edu.eaf.eventmgmt.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by lukasschonbachler on 27.03.17.
 */
@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"user_id", "event_id"})
)
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
    @NotNull
    private Event event;

    @ManyToOne
    @JoinColumn
    @NotNull
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
    @NotNull
    private FoodType foodType;

    public boolean isDrink() {
        return drink;
    }

    public void setDrink(boolean drink) {
        this.drink = drink;
    }

    private boolean drink;


}
