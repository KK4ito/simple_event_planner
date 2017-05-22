package ch.fhnw.edu.eaf.eventmgmt.security;

/**
 * Created by lukasschonbachler on 21.05.17.
 */
public class SecurePath {
    final String path;
    final boolean GET;
    final boolean PUT;
    final boolean POST;
    final boolean DELETE;
    final boolean OPTIONS;

    public SecurePath(String path, boolean ALL, boolean OPTIONS){
        this.path = path.trim();
        this.GET = ALL;
        this.PUT = ALL;
        this.POST = ALL;
        this.DELETE = ALL;
        this.OPTIONS = OPTIONS;
    }

    public SecurePath(String path, boolean ALL){
        this.path = path.trim();
        this.GET = ALL;
        this.PUT = ALL;
        this.POST = ALL;
        this.DELETE = ALL;
        this.OPTIONS = ALL;
    }

    public SecurePath(String path, boolean GET, boolean PUT, boolean POST, boolean DELETE, boolean OPTIONS){
        this.path = path.trim();
        this.GET = GET;
        this.PUT = PUT;
        this.POST = POST;
        this.DELETE = DELETE;
        this.OPTIONS = OPTIONS;
    }
}
