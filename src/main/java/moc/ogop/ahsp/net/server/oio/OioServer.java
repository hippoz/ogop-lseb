package moc.ogop.ahsp.net.server.oio;

import moc.ogop.ahsp.net.Utils;
import moc.ogop.ahsp.net.server.ProxyPacketProcessor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jameszheng on 2016/7/10.
 */
public class OioServer<I, T extends I> {

    final int port;

    final Class<I> serviceClazz;

    final T serviceProvider;

    protected ServerSocket socket;

    public OioServer(int port, Class<I> svcClass, T serviceProvider) {
        this.port = port;
        this.serviceClazz = svcClass;
        this.serviceProvider = serviceProvider;
    }

    public void start() throws IOException {
        socket = new ServerSocket(port);
        final ExecutorService executorService = new ThreadPoolExecutor(20, 50,
                1, TimeUnit.HOURS,
                new LinkedBlockingQueue<>(1000),
                (Runnable r, ThreadPoolExecutor exec) -> System.err.println("Server Overloaded"));
        Utils.startThread("Boss Thread " + serviceClazz.getName(), false, () -> {
            while(true) {
                final Socket clientSocket;
                try {
                    clientSocket = socket.accept();
                    new ConnectionWorker(clientSocket, new ProxyPacketProcessor(serviceClazz, serviceProvider), executorService).start();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                System.out.println("Accepted connection from " + clientSocket);

            }
        });
        System.out.println("Server Listening on port " + port);
    }
}
