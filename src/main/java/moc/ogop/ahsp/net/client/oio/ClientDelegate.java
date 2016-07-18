package moc.ogop.ahsp.net.client.oio;

import moc.ogop.ahsp.net.RPacket;
import moc.ogop.ahsp.net.Utils;
import moc.ogop.ahsp.net.WPacket;
import moc.ogop.ahsp.net.client.AbstractClientDelegate;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * Created by jameszheng on 2016-07-11.
 */
public class ClientDelegate<I> extends AbstractClientDelegate<I> {

    final Socket socket;

    public ClientDelegate(String hostName, int portNumber, Class<I> serviceInterface) throws IOException {
        super(serviceInterface);
        this.socket = new Socket(hostName, portNumber);
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
    protected synchronized void sendToServer(RPacket rp) throws IOException {
        Utils.outputToStream(socket.getOutputStream(), rp);
    }

    private WPacket readNext() throws IOException {
        return Utils.readNext(socket.getInputStream());
    }

    public static <I> I remoteService(String hostName, int portNumber, Class<I> serviceInterface) throws IOException {
        return (I) Proxy.newProxyInstance(ClientDelegate.class.getClassLoader(),new Class[]{serviceInterface}, new ClientDelegate<>(hostName, portNumber, serviceInterface).start());
    }
}
