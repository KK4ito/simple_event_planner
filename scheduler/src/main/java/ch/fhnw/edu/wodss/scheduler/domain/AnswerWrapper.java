package ch.fhnw.edu.wodss.scheduler.domain;

/**
 * Wrapper-class for an answer that is sent back from the mailer-microservice.
 */
public class AnswerWrapper {

    /**
     * The resultmessage of the request.
     */
    private String result;

    public AnswerWrapper() {};

    public AnswerWrapper(String r) {
        this.result = r;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
