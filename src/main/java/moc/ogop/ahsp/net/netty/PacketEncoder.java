package moc.ogop.ahsp.net.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import moc.ogop.ahsp.net.Utils;
import moc.ogop.ahsp.net.io.IPacket;

/**
 * Created by Heisenberg on 2016/7/18.
 */
@ChannelHandler.Sharable
public class PacketEncoder<I extends IPacket> extends MessageToByteEncoder<IPacket> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IPacket obj, ByteBuf dout) throws Exception {
        byte[] data = Utils.toBytes(obj);
        dout.writeInt(data.length);
        dout.writeBytes(data);
    }
}
