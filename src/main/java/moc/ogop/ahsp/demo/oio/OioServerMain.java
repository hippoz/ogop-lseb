package moc.ogop.ahsp.demo.oio;

import moc.ogop.ahsp.net.IOgopService;
import moc.ogop.ahsp.net.server.OgopServiceComponent;
import moc.ogop.ahsp.net.server.oio.OioServer;

import java.io.IOException;

/**
 * Created by jameszheng on 2016-07-11.
 */
public class OioServerMain {
    public static void main(String[] args) throws IOException {
        new OioServer<>(1234, IOgopService.class, new OgopServiceComponent()).start();
    }
}
