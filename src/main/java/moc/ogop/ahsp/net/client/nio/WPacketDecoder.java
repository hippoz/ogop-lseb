package moc.ogop.ahsp.net.client.nio;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import moc.ogop.ahsp.net.Utils;
import moc.ogop.ahsp.net.WPacket;

import java.util.List;

/**
 * Created by Heisenberg on 2016/7/18.
 */
public class WPacketDecoder extends ByteToMessageDecoder {

    private static enum State {HEADER, BODY}

    State state = State.HEADER;

    int totalBodySize = -1;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf din, List<Object> out) throws Exception {
        switch (state) {
            case HEADER:
                if (din.readableBytes() < 4) {
                    return;
                }
                totalBodySize = din.readInt();
                state = State.BODY;
            case BODY:
                if (din.readableBytes() < totalBodySize) {
                    return; // until we have the entire payload return
                }
                ByteBufAllocator allocator = ctx.alloc();
                ByteBuf body = allocator.directBuffer(totalBodySize);
                din.readBytes(body, totalBodySize);
                WPacket packet = Utils.fromBytes(ByteBufUtil.getBytes(body));
                ReferenceCountUtil.safeRelease(body);
                out.add(packet);
                state = State.HEADER;
        }
    }


}
