package moc.ogop.ahsp.demo.nio;

import moc.ogop.ahsp.net.IOgopService;
import moc.ogop.ahsp.net.client.oio.ClientDelegate;

import java.io.IOException;

/**
 * Created by Heisenberg on 2016/7/18.
 */
public class MultipleConnectionNioMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 0; i < 50; i++) {
            // this will create 50 Reader Thread, each thread for one channel
            IOgopService remote = ClientDelegate.remoteService("127.0.0.1", 1234, IOgopService.class);
            System.out.println(remote.reverse("Hello"));
        }
        Thread.sleep(1000000);
    }
}
