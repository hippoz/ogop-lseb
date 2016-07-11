package moc.ogop.ahsp.net.server;

/**
 * Created by Heisenberg on 2016/7/10.
 */
public class ServiceException extends RuntimeException {

    public ServiceException(Exception e) {
        super(e);
    }

    public ServiceException(String s) {
        super(s);
    }

    public ServiceException(String s, Exception e) {
        super(s, e);
    }
}
