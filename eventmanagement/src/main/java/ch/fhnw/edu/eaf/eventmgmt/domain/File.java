package ch.fhnw.edu.eaf.eventmgmt.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import static javax.persistence.FetchType.LAZY;

import javax.persistence.*;
import java.util.Date;

@Entity
public class File {
	@Id
	@GeneratedValue
	private Long id;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;

	private String name;

	/*
	@Basic(fetch=LAZY)
	@Lob
	private byte[] data;
	*/

	public Long getId() {
		return id;
	}

	public Date getCreated() {
		return created;
	}

	public Date getUpdated() {
		return updated;
	}

	/*
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	*/
}
