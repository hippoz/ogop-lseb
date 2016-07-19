package moc.ogop.ahsp.net.client.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import moc.ogop.ahsp.net.client.AbstractClientDelegate;
import moc.ogop.ahsp.net.io.RPacket;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * Created by Heisenberg on 2016/7/18.
 */
public class NioClientDelegate<I> extends AbstractClientDelegate {

    private final static EventLoopGroup nioEventLoop = new NioEventLoopGroup(10);

    private final static Bootstrap clientBootStrap = new Bootstrap();

    private final Channel channel;

    public NioClientDelegate(String hostName, int portNumber, Class<I> serviceInterface) throws IOException, InterruptedException {
        super(serviceInterface);
        clientBootStrap.group(nioEventLoop)
                .channel(NioSocketChannel.class)
                .handler(new NioClientChannelInitializer(pending));
        channel = clientBootStrap.connect(new InetSocketAddress(hostName, portNumber)).sync().channel();
    }

    protected void sendToServer(RPacket rp) throws IOException {
        channel.writeAndFlush(rp);
    }

    public static <I> I remoteService(String hostName, int portNumber, Class<I> serviceInterface) throws IOException, InterruptedException {
        return (I) Proxy.newProxyInstance(NioClientDelegate.class.getClassLoader(), new Class[]{serviceInterface}, new NioClientDelegate<>(hostName, portNumber, serviceInterface));
    }
}
