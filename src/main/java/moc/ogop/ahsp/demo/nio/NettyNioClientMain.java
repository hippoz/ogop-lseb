package moc.ogop.ahsp.demo.nio;

import moc.ogop.ahsp.demo.ServiceRunner;
import moc.ogop.ahsp.net.IOgopService;
import moc.ogop.ahsp.net.client.nio.NioClientDelegate;

/**
 * Created by Heisenberg on 2016/7/18.
 */
public class NettyNioClientMain {
    public static void main(String[] args) throws Exception {
        IOgopService remote = NioClientDelegate.remoteService("127.0.0.1", 1234, IOgopService.class);
        try {
            ServiceRunner.runService(remote);
        } finally {
            System.exit(0);
        }
    }
}
