package moc.ogop.ahsp.net.client;

import moc.ogop.ahsp.net.RPacket;
import moc.ogop.ahsp.net.WPacket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Heisenberg on 2016/7/18.
 */
public abstract class AbstractClientDelegate<I> implements InvocationHandler {

    final AtomicInteger counter = new AtomicInteger(0);

    protected final Class<I> serviceInterface;

    protected final ConcurrentHashMap<Integer, CompletableFuture<WPacket>> pending = new ConcurrentHashMap<>();

    protected AbstractClientDelegate(Class<I> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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

    protected abstract void sendToServer(RPacket rp) throws IOException;

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
}
