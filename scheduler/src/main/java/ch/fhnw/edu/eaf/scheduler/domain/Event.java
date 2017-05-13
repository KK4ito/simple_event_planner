package ch.fhnw.edu.eaf.scheduler.domain;

import java.util.Date;

public class Event {
    private Long Id;
    private Date startTime;
    private Date closingTime;
    private Date endTime;
    private boolean closingMailSend;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public boolean isClosingMailSend() {
        return closingMailSend;
    }

    public void setClosingMailSend(boolean closingMailSend) {
        this.closingMailSend = closingMailSend;
    }
}
