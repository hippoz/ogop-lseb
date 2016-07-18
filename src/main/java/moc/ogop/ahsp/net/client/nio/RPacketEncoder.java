package moc.ogop.ahsp.net.client.nio;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import moc.ogop.ahsp.net.RPacket;
import moc.ogop.ahsp.net.Utils;

/**
 * Created by Heisenberg on 2016/7/18.
 */
@ChannelHandler.Sharable
public class RPacketEncoder extends MessageToByteEncoder<RPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RPacket rp, ByteBuf dout) throws Exception {
        byte[] data = Utils.toBytes(rp);
        dout.writeInt(data.length);
        dout.writeBytes(data);
    }
}
