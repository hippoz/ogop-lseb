package moc.ogop.ahsp.net;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jameszheng on 2016-07-11.
 */
public class DefaultThreadFactory implements ThreadFactory {

    static final AtomicInteger count = new AtomicInteger(0);

    private final String prefix;

    private final boolean daemon;

    public DefaultThreadFactory(String prefix, boolean daemon) {
        this.prefix = prefix;
        this.daemon = daemon;
    }

    public DefaultThreadFactory(String prefix) {
        this(prefix, false);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(prefix + ":" + count.incrementAndGet());
        t.setDaemon(daemon);
        return t;
    }
}
