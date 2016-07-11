package moc.ogop.ahsp.demo.nio;

import moc.ogop.ahsp.net.IOgopService;
import moc.ogop.ahsp.net.server.OgopServiceComponent;
import moc.ogop.ahsp.net.server.netty.NioServer;

/**
 * Created by jameszheng on 2016-07-11.
 */
public class NettyNioServerMain {
    public static void main(String[] args) throws Exception {
        new NioServer<>(1234, IOgopService.class, new OgopServiceComponent()).start();
    }
}
