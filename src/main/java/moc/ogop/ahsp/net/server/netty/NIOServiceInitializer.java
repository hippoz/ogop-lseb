package moc.ogop.ahsp.net.server.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import moc.ogop.ahsp.net.io.WPacket;
import moc.ogop.ahsp.net.netty.PacketDecoder;
import moc.ogop.ahsp.net.netty.PacketEncoder;
import moc.ogop.ahsp.net.server.PacketProcessor;


/**
 * Created by jameszheng on 2016-07-01.
 */
public class NIOServiceInitializer extends ChannelInitializer<SocketChannel> {

    final PacketProcessor proc;

    final EventExecutorGroup workGroup;

    NIOServiceInitializer(PacketProcessor packetProcessor, EventExecutorGroup workGroup) {
        this.proc  = packetProcessor;
        this.workGroup = workGroup;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("WPacketBaseEncoder", new PacketEncoder<WPacket>());
        pipeline.addLast("RPacketBaseDecoder", new PacketDecoder());
        pipeline.addLast("PacketHandler", new PacketHandlerNetty(proc, workGroup));
    }
}
