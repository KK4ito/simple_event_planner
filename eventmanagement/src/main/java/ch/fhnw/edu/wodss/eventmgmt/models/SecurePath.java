package ch.fhnw.edu.wodss.eventmgmt.models;

/**
 * Helper class for setting correct permissions for http-methods.
 *
 * Created by lukasschonbachler on 21.05.17.
 */
public class SecurePath {
    public final String path;
    public final boolean GET;
    public final boolean PUT;
    public final boolean POST;
    public final boolean DELETE;
    public final boolean OPTIONS;
    public final boolean PATCH;


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
