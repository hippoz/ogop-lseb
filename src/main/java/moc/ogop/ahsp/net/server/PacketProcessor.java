package moc.ogop.ahsp.net.server;

import moc.ogop.ahsp.net.io.RPacket;
import moc.ogop.ahsp.net.io.WPacket;

/**
 * Created by jameszheng on 2016-07-04.
 */
public interface PacketProcessor {
    WPacket process(RPacket rp) throws ServiceException;
}
