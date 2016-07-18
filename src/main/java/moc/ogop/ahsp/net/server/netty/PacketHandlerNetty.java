package moc.ogop.ahsp.net.server.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import moc.ogop.ahsp.net.io.RPacket;
import moc.ogop.ahsp.net.server.PacketProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by jameszheng on 2016-07-04.
 */
public class PacketHandlerNetty extends SimpleChannelInboundHandler<RPacket> {

    final static Log logger = LogFactory.getLog(PacketHandlerNetty.class);

    final PacketProcessor func;

    final EventExecutorGroup workers;

    public PacketHandlerNetty(PacketProcessor func) {
        this(func, null);
    }

    public PacketHandlerNetty(PacketProcessor proc, EventExecutorGroup workers) {
        this.func = proc;
        this.workers = workers;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final RPacket packet) throws Exception {
        if (workers != null) {
            workers.submit(() -> {
                ctx.writeAndFlush(func.process(packet));
            });
        } else {
            ctx.writeAndFlush(func.process(packet));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Exception happened " + cause.getMessage());
    }
}
