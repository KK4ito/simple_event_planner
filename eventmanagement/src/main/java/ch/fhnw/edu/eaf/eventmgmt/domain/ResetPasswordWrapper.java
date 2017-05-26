package ch.fhnw.edu.eaf.eventmgmt.domain;

/**
 * Created by apple on 25.05.17.
 */
public class ResetPasswordWrapper {
    public String email;
    public String message;
    public String token;
    public String password;
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
}
