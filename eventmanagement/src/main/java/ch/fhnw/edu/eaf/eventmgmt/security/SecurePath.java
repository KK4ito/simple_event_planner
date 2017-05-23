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
    final boolean PATCH;


    public SecurePath(String path, boolean ALL, boolean OPTIONS, boolean PATCH){
        this.path = path.trim();
        this.GET = ALL;
        this.PUT = ALL;
        this.POST = ALL;
        this.DELETE = ALL;
        this.OPTIONS = OPTIONS;
        this.PATCH = PATCH;
    }

    public SecurePath(String path, boolean ALL){
        this.path = path.trim();
        this.GET = ALL;
        this.PUT = ALL;
        this.POST = ALL;
        this.DELETE = ALL;
        this.OPTIONS = ALL;
        this.PATCH = ALL;
    }

    public SecurePath(String path, boolean GET, boolean PUT, boolean POST, boolean DELETE, boolean OPTIONS, boolean PATCH){
        this.path = path.trim();
        this.GET = GET;
        this.PUT = PUT;
        this.POST = POST;
        this.DELETE = DELETE;
        this.OPTIONS = OPTIONS;
        this.PATCH = PATCH;
    }
}
