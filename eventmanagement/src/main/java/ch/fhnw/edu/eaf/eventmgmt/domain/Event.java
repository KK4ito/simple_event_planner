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
	private Long id;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;

	private String title;
	private String description;

	/*
	@ManyToMany(mappedBy = "speakerEvents")
	private List<User> speakers;

	@ManyToMany(mappedBy = "attendeeEvents")
	private List<User> attendees;
	*/

	private Date startTime;
	private Date closingTime;
	private Date endTime;

	@OneToMany
	private
	Collection<File> files;

	public Long getId() {
		return id;
	}

	public Date getCreated() {
		return created;
	}

	public Date getUpdated() {
		return updated;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Collection<File> getFiles() {
		return files;
	}

	public void setFiles(Collection<File> files) {
		this.files = files;
	}
}
