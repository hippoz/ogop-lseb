package moc.ogop.ahsp.net.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import moc.ogop.ahsp.net.Utils;
import moc.ogop.ahsp.net.WPacket;

/**
 * Created by jameszheng on 2016-07-04.
 */
@ChannelHandler.Sharable
public class WPacketEncoder extends MessageToByteEncoder<WPacket> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, WPacket wp, ByteBuf dout) throws Exception {
        byte[] data = Utils.toBytes(wp);
        dout.writeInt( data.length);
        dout.writeBytes(data);
    }
}
