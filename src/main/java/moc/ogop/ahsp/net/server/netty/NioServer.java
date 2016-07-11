package moc.ogop.ahsp.net.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import moc.ogop.ahsp.net.server.ProxyPacketProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;

/**
 * Created by jameszheng on 2016-07-11.
 */
public class NioServer<I, T extends I> {

    private static final  Log logger = LogFactory.getLog(NioServer.class);

    final int port;

    final Class<I> serviceClazz;

    final T serviceProvider;

    public NioServer(int port, Class<I> svcClass, T serviceProvider) {
        this.port = port;
        this.serviceClazz = svcClass;
        this.serviceProvider = serviceProvider;
    }

    public void start() throws Exception {
        EventLoopGroup pGroup        = new NioEventLoopGroup(1,  new DefaultThreadFactory("IO-PARENT"));
        EventLoopGroup cGroup        = new NioEventLoopGroup(5,  new DefaultThreadFactory("IO-CHILD"));
        EventExecutorGroup wGroup     = new DefaultEventExecutorGroup(50, new DefaultThreadFactory("NETTY-WORKER"));
        ServerBootstrap b  = new ServerBootstrap();
        b.group(pGroup, cGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new NIOServiceInitializer(new ProxyPacketProcessor<>(serviceClazz, serviceProvider), wGroup))
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture f = b.bind();
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("NIO Server bound to port:" + port);
                } else {
                    logger.fatal("Bound Attempted Failed; port=" + port, channelFuture.cause());
                }
            }
        });
    }

}
