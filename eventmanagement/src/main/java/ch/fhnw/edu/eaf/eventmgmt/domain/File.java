package ch.fhnw.edu.eaf.eventmgmt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static javax.persistence.FetchType.LAZY;

/**
 * The file-entity.
 */
@Entity
public class File {
    /**
     * The id of the file.
     */
    @Id
    @GeneratedValue
    private Long Id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    /**
     * The name of the file.
     */
    @NotNull
    @Size(min = 3, max = 1024)
    private String name;

    /**
     * The content-type of the file.
     */
    @Size(max = 255)
    private String contentType;

    /**
     * The actual data.
     */
    @JsonIgnore
    @Basic(fetch = LAZY)
    @Lob
    @NotNull
    private byte[] data;

    public File() {
    }

    public File(Long Id) {
        this.Id = Id;
    }

    public File(String name, byte[] data, String contentType) {
        this.name = name;
        this.data = data;
        this.contentType = contentType;
    }

    public String getUri() {
        // TODO Load base path from configuration
        return "/api/download/" + getId();
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
