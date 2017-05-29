package ch.fhnw.edu.wodss.eventmgmt.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

/**
 * The event-entity
 */
@Entity
public class Event {

    /**
     * The id of an event.
     */
    @Id
    @GeneratedValue
    private Long Id;

    /**
     * The date the event was created on the platform.
     */
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    /**
     * The date the event was last updated.
     */
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    /**
     * The name of the event.
     */
    @NotNull
    @Size(min = 3, max = 1024)
    private String name;

    /**
     * The description of an event.
     */
    @NotNull
    @Size(max = 65535)
    private String description;

    /**
     * The location the event takes place.
     */
    @NotNull
    @Size(min = 3, max = 1024)
    private String location;

    /**
     * The time the event starts.
     */
    @NotNull
    private Date startTime;

    /**
     * The time the registration for the event closes.
     */
    @NotNull
    private Date closingTime;

    /**
     * The time the event finishes.
     */
    @NotNull
    private Date endTime;

    /**
     * Indicator wether mail was send or not
     */
    private boolean closingMailSend;

    /**
     * The image for the event.
     */
    @OneToOne
    private File image;

    /**
     * The files accompanying the event (e.g. power-point-presentation)
     */
    @OneToMany
    private Collection<File> files;

    /**
     * The speakers of the event.
     */
    @ManyToMany
    @JoinTable(name = "speakers", joinColumns = @JoinColumn(name = "idevent"), inverseJoinColumns = @JoinColumn(name = "iduser"))
    @JsonProperty
    private Collection<User> speakers;

    /*
    @ManyToMany
    @JoinTable(name = "attendees", joinColumns = @JoinColumn(name = "idevent"), inverseJoinColumns = @JoinColumn(name = "iduser"))
    private Collection<User> attendees;
    */

    /**
     * The users who attend the event.
     */
    @OneToMany(mappedBy = "event")
    //@JoinTable(name = "EVENT_ATTENDEE", joinColumns = @JoinColumn(name = "event"), inverseJoinColumns = @JoinColumn(name = "user"))
    private Collection<EventAttendee> attendees;

    public Event() {
    }

    public int getAttendeesCount(){
        return (this.attendees == null)? 0 : this.attendees.size();
    }

    public String getImageUri() {
        if (image == null) return "/default/no_content.png";
        // TODO Load base path from configuration
        return "/api/download/" + image.getId();
    }

    public File getImage() {
        return this.image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public Long getId() {
        return Id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Date closingTime) {
        this.closingTime = closingTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Collection<User> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(Collection<User> speakers) {
        this.speakers = speakers;
    }

    public Collection<EventAttendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(Collection<EventAttendee> attendees) {
        this.attendees = attendees;
    }

    public Collection<File> getFiles() {
        return files;
    }

    public void setFiles(Collection<File> files) {
        this.files = files;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Indicates whether or not the mails were sent after the registration-deadline
     */
    public boolean isClosingMailSend() {
        return closingMailSend;
    }


    public void setClosingMailSend(boolean closingMailSend) {
        this.closingMailSend = closingMailSend;
    }
}
