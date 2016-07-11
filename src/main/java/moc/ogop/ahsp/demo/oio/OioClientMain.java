package moc.ogop.ahsp.demo.oio;

import moc.ogop.ahsp.net.IOgopService;
import moc.ogop.ahsp.net.client.oio.ClientDelegate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jameszheng on 2016-07-11.
 */
public class OioClientMain {
    public static void main(String[] args) throws Exception {
        IOgopService remote = ClientDelegate.remoteService("127.0.0.1", 1234, IOgopService.class);
        final int count = 50000;
        final int threadCount = 100;
        ExecutorService exec = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0 ; i < count; i++) {
            final int id = i;
            exec.submit(() -> {
                try {
                    remote.reverse("Hello");
                    System.out.println(id + " done!");
                } catch (Exception e) {
                    System.err.println(id + " error!");
                } finally {
                    latch.countDown();
                }
            });
        }
        exec.shutdown();
        latch.await();
        System.exit(0);
    }
}
