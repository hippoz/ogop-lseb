package moc.ogop.ahsp.net.client.nio;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import moc.ogop.ahsp.net.io.RPacket;
import moc.ogop.ahsp.net.io.WPacket;
import moc.ogop.ahsp.net.netty.PacketDecoder;
import moc.ogop.ahsp.net.netty.PacketEncoder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Heisenberg on 2016/7/18.
 */
public class NioClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    final ConcurrentHashMap<Integer, CompletableFuture<WPacket>> pending;

    public NioClientChannelInitializer(ConcurrentHashMap<Integer, CompletableFuture<WPacket>> pending) {
        this.pending = pending;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        channel.pipeline()
                .addLast(new PacketDecoder())
                .addLast(new SimpleChannelInboundHandler<WPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, WPacket wp) throws Exception {
                        pending.get(wp.getRequestId()).complete(wp);
                    }
                })
                .addLast(new PacketEncoder<RPacket>());
    }
}
