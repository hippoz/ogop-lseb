package moc.ogop.ahsp.net.client.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import moc.ogop.ahsp.net.DefaultThreadFactory;
import moc.ogop.ahsp.net.RPacket;
import moc.ogop.ahsp.net.client.AbstractClientDelegate;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by Heisenberg on 2016/7/18.
 */
public class NioClientDelegate<I> extends AbstractClientDelegate {

    final static EventLoopGroup nioEventLoop = new NioEventLoopGroup(5, new DefaultThreadFactory("NioClient", true));

    private final CompletableFuture<Channel> channelFuture = new CompletableFuture<>();

    public NioClientDelegate(String hostName, int portNumber, Class<I> serviceInterface) throws IOException, InterruptedException {
        super(serviceInterface);
        Bootstrap b = new Bootstrap();
        b.group(nioEventLoop)
                .channel(NioSocketChannel.class)
                .handler(new NioClientChannelInitializer(pending));
        ChannelFuture f = b.connect(new InetSocketAddress(hostName, portNumber));
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture cf) throws Exception {
                if (cf.isSuccess()) {
                    System.out.println("Connected");
                    channelFuture.complete(cf.channel());
                } else {
                    System.err.println("Connection attempt failed");
                    cf.cause().printStackTrace();
                }
            }
        });
    }

    protected void sendToServer(RPacket rp) throws IOException {
        try {
            channelFuture.get().writeAndFlush(rp);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static <I> I remoteService(String hostName, int portNumber, Class<I> serviceInterface) throws IOException, InterruptedException {
        return (I) Proxy.newProxyInstance(NioClientDelegate.class.getClassLoader(), new Class[]{serviceInterface}, new NioClientDelegate<>(hostName, portNumber, serviceInterface));
    }
}
