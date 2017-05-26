package ch.fhnw.edu.eaf.eventmgmt.domain;

public class AnswerWrapper {
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
