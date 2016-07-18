package moc.ogop.ahsp.demo;

import moc.ogop.ahsp.net.IOgopService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Heisenberg on 2016/7/18.
 */
public class ServiceRunner {

    public static void runService(IOgopService service) throws InterruptedException {
        final int count = 50000;
        final int threadCount = 100;
        final long startTime = System.currentTimeMillis();
        ExecutorService exec = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            final int id = i;
            exec.submit(() -> {
                try {
                    service.reverse("Hello");
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
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("Total Time Used:" + TimeUnit.MILLISECONDS.toSeconds(totalTime));
    }

}
