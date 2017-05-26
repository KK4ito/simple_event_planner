package ch.fhnw.edu.eaf.eventmgmt;

/**
 * Created by apple on 25.05.17.
 */
public class ResetPasswordAnswerMessage {
    private String message;
    public ResetPasswordAnswerMessage() {}
    public ResetPasswordAnswerMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}