package moc.ogop.ahsp.net.client.oio;

import moc.ogop.ahsp.net.RPacket;
import moc.ogop.ahsp.net.Utils;
import moc.ogop.ahsp.net.WPacket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jameszheng on 2016-07-11.
 */
public class ClientDelegate<I> implements InvocationHandler {

    final Socket socket;

    final Class<I> serviceInterface;

    final AtomicInteger counter = new AtomicInteger(0);

    final ConcurrentHashMap<Integer, CompletableFuture<WPacket>> pending = new ConcurrentHashMap<>();

    public ClientDelegate(String hostName, int portNumber, Class<I> serviceInterface) throws IOException {
        this.socket = new Socket(hostName, portNumber);
        this.serviceInterface = serviceInterface;
    }

    ClientDelegate start() {
        Utils.startThread("Reader: " + socket.getLocalPort(), true, () -> {
            try {
                WPacket wp = readNext();
                while (wp != null) {
                    pending.remove(wp.getRequestId()).complete(wp);
                    wp = readNext();
                }
            } catch (IOException e) {
                throw new RuntimeException("IOException happened", e);
            }
        });
        return this;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPacket request = makeRPacket(method, args);
        Future<WPacket> f = sendToRemote(request);
        return f.get().getResult(); // this should have a timeout setting
    }

    private Future<WPacket> sendToRemote(RPacket rp) throws IOException {
        int rid = counter.incrementAndGet();
        rp.setRequestId(rid);
        CompletableFuture<WPacket> f = new CompletableFuture<WPacket>();
        pending.put(rid, f);
        sendToServer(rp);
        return f;
    }

    private synchronized void sendToServer(RPacket rp) throws IOException {
        Utils.outputToStream(socket.getOutputStream(), rp);
    }

    private WPacket readNext() throws IOException {
        return Utils.readNext(socket.getInputStream());
    }

    private RPacket makeRPacket(Method method, Object[] args) {
        List<String> types = new ArrayList<>();
        for (Class<?> clazz : method.getParameterTypes()) {
            types.add(clazz.getName());
        }
        RPacket ret = new RPacket();
        ret.setMethodName(method.getName());
        ret.setArgTypes(types);
        ret.setArgs(args);
        return ret;
    }

    public static <I> I remoteService(String hostName, int portNumber, Class<I> serviceInterface) throws IOException {
        return (I) Proxy.newProxyInstance(ClientDelegate.class.getClassLoader(),new Class[]{serviceInterface}, new ClientDelegate<>(hostName, portNumber, serviceInterface).start());
    }
}
