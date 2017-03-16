package ch.fhnw.edu.eaf.eventmgmt.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long Id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    private String name;
    private String description;


    /*
    @ManyToMany
    @JoinTable(
            name = "speakers",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID", referencedColumnName = "ID"))


    private List<User> speakers;
    */


    public Collection<User> getAttendees() {
        return attendees;
    }

    public void setAttendees(Collection<User> attendees) {
        this.attendees = attendees;
    }

    /*
        @JoinTable(name="Speaker",
                joinColumns=@JoinColumn(name="SPEAKER_ID", referencedColumnName="ID"),
                inverseJoinColumns=@JoinColumn(name="EVENT_ID", referencedColumnName="ID"))
                */
    @ManyToMany
    @JoinTable(name = "attendees", joinColumns = @JoinColumn(name = "idevent"), inverseJoinColumns = @JoinColumn(name = "iduser"))
    private Collection<User> attendees;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private List<File> files;

    private Date startTime;
    private Date closingTime;
    private Date endTime;


    public Event() {
    }

    public Long getId() {
        return Id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
