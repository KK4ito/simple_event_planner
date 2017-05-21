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
public class CustomizablePathMatcher implements Matcher {

    private final static Logger logger = LoggerFactory.getLogger(ExcludedPathMatcher.class);
    private final SecurePath[] securePaths;

    public CustomizablePathMatcher(SecurePath[] securePaths) {
        this.securePaths = securePaths;
    }

    @Override
    public boolean matches(WebContext context) throws HttpAction {

        for(SecurePath securePath : securePaths){
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
                }
                if(result) return result;
            }
        }
       return false;
    }
}
