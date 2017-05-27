package ch.fhnw.edu.eaf.eventmgmt.security;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.matching.ExcludedPathMatcher;
import org.pac4j.core.matching.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper method to process an array of paths to match.
 *
 * Created by lukasschonbachler on 24.03.17.
 */
public class CustomizablePathMatcher implements Matcher {

    private final static Logger logger = LoggerFactory.getLogger(CustomizablePathMatcher.class);
    private final SecurePath[] securePaths;

    public CustomizablePathMatcher(final SecurePath[] securePaths) {
        this.securePaths = securePaths;
    }

    @Override
    public boolean matches(WebContext context) throws HttpAction {

        logger.debug("Received match request for '" + context.getPath() + "' with method " + context.getRequestMethod());

        for(SecurePath securePath : this.securePaths){
            if(context.getPath().startsWith(securePath.path)){
                boolean result = false;
                switch (context.getRequestMethod()){
                    case "GET":
                        result = securePath.GET;
                        break;
                    case "PUT":
                        result = securePath.PUT;
                        break;
                    case "POST":
                        result = securePath.POST;
                        break;
                    case "DELETE":
                        result = securePath.DELETE;
                        break;
                    case "OPTIONS":
                        result = securePath.OPTIONS;
                    case "PATCH":
                        result = securePath.PATCH;
                }
                logger.debug("Match found for '" + context.getPath() + "' -> '" + securePath.path + "' with method " + context.getRequestMethod());
                return result;
            }
        }
        logger.debug("No match found for '" + context.getPath() + "' with method " + context.getRequestMethod());
        return false;
    }
}
