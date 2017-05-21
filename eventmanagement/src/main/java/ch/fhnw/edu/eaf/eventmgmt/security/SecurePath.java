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

    public SecurePath(String path, boolean ALL){
        this.path = path.trim();
        this.GET = ALL;
        this.PUT = ALL;
        this.POST = ALL;
        this.DELETE = ALL;
    }

    public SecurePath(String path, boolean GET, boolean PUT, boolean POST, boolean DELETE){
        this.path = path.trim();
        this.GET = GET;
        this.PUT = PUT;
        this.POST = POST;
        this.DELETE = DELETE;
    }
}
