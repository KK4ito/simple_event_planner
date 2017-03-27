package ch.fhnw.edu.eaf.eventmgmt.security;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.matching.ExcludedPathMatcher;
import org.pac4j.core.matching.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lukasschonbachler on 24.03.17.
 */
public class CustomMatcher implements Matcher {

    private final static Logger logger = LoggerFactory.getLogger(ExcludedPathMatcher.class);

    private final String path;
    private final boolean GET;
    private final boolean PUT;
    private final boolean POST;
    private final boolean DELETE;

    public CustomMatcher(String path, boolean GET, boolean PUT, boolean POST, boolean DELETE) {
        System.err.println("Path: " + path);
        this.path = path;
        this.GET = GET;
        this.PUT = PUT;
        this.POST = POST;
        this.DELETE = DELETE;
    }

    @Override
    public boolean matches(WebContext context) throws HttpAction {
        if(!context.getPath().equalsIgnoreCase(this.path)){
            logger.info("Path " + context.getPath() + " didn't match");
            return false;
        }

        boolean result = false;
        switch (context.getRequestMethod()){
            case "GET":
                result = this.GET;
                break;
            case "PUT":
                result = this.PUT;
                break;
            case "POST":
                result = this.POST;
                break;
            case "DELETE":
                result = this.DELETE;
                break;
        }
        logger.info("Path " + context.getPath() + " matched, METHOD " + context.getRequestMethod() + " matched: " + result);
        return result;
    }
}
