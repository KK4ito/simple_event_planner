package ch.fhnw.edu.wodss.eventmgmt.models;

/**
 * Object to return as an answer for a resetPassword-Request.
 *
 * This is mainly a helper-class since we cannot just pass void (ResponseEntity<void>), we then
 * experience some converter-issues. So we use this helper-class.
 */
public class ResetPasswordAnswerMessage {
    /**
     * A message to return.
     */
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