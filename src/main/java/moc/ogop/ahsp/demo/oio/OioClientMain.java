package moc.ogop.ahsp.demo.oio;

import moc.ogop.ahsp.demo.ServiceRunner;
import moc.ogop.ahsp.net.IOgopService;
import moc.ogop.ahsp.net.client.oio.ClientDelegate;

/**
 * Created by jameszheng on 2016-07-11.
 */
public class OioClientMain {
    public static void main(String[] args) throws Exception {
        IOgopService remote = ClientDelegate.remoteService("127.0.0.1", 1234, IOgopService.class);
        try {
            ServiceRunner.runService(remote);
        } finally {
            System.exit(0);
        }
    }
}
