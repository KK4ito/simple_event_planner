package ch.fhnw.edu.wodss.eventmgmt.models;

/**
 * Wrapper-class for getting data from the frontend to the backend during
 * a resetPassword-Request.
 */
public class ResetPasswordWrapper {

    /**
     * The email of the user asking to reset his password.
     */
    public String email;

    /**
     * A message.
     */
    public String message;

    /**
     * The token to reset the password.
     */
    public String token;

    /**
     * The new password that should be set.
     */
    public String password;

    public boolean resetPassword;

    public ResetPasswordWrapper() {}
    public ResetPasswordWrapper(String message) {
        this.message = message;
    }
    public String getEmail() {
        return this.email;
    }
    public String getToken() {
        return this.token;
    }
    public String getPassword() {
        return this.password;
    }
    public boolean isResetPassword() { return this.resetPassword; }
}
