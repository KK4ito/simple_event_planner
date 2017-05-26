package ch.fhnw.edu.eaf.mailer;

import java.util.Map;

/**
 * A mail-instance.
 *
 * Created by lukasschonbachler on 13.05.17.
 */
public class Mail {

    /**
     * The token for the mail-microservice. Without this token
     * you cannot send any mails.
     */
    public String token;

    /**
     * The sender of the mail.
     */
    public String from;

    /**
     * The recipients of the mail as comma-separated list of valid emai-addresses.
     */
    public String to;

    /**
     * The cc of the mail as comma-separated list of valid emai-addresses.
     */
    public String cc;

    /**
     * The subject of the email.
     */
    public String subject;

    /**
     * The text of the email, in html. The body may contain placeholders in the format $placeholder$.
     * The mail then has to contain a list of keys and values. The keys containing all the names of the
     * used placeholders, the values containing the values with which the placeholders are to be replaced.
     * IMPORTANT: the keys and values have to be in the same order, that is: key[i] will be replace by
     * value[i]
     */
    public String body;

    /**
     * The eventId the mail concerns.
     */
    public long eventId;

    /**
     * The names of the placeholders that were used in the mail body.
     */
    public String[] keys;

    /**
     * The values with which to replace the keys.
     */
    public String[] values;
    public Map<String, String> parameters;
}
