package moc.ogop.ahsp.net.server;

import moc.ogop.ahsp.net.io.RPacket;
import moc.ogop.ahsp.net.io.WPacket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jameszheng on 2016-07-04.
 */
public class ProxyPacketProcessor<I, T extends I> implements PacketProcessor {

    private final Class<I> serviceInterface;

    private final Log log;

    private final T serviceProvider;

    public ProxyPacketProcessor(Class<I> serviceInterface, T serviceProvider) {
        this.serviceInterface = serviceInterface;
        this.log  = LogFactory.getLog(serviceInterface);
        this.serviceProvider = serviceProvider;
    }

    @Override
    public WPacket process(RPacket rp) {
        try {
            WPacket wp = new WPacket();
            wp.setRequestId(rp.getRequestId());
            List<Class> argTypes = new ArrayList<>();
            for (String r : rp.getArgTypes()) {
                argTypes.add(Class.forName(r));
            }
            Method method = serviceProvider.getClass().getMethod(rp.getMethodName(), argTypes.toArray(new Class[0]));
            wp.setResult(method.invoke(serviceProvider, rp.getArgs()));
            return wp;
        } catch (Exception e) {
            log.error("Exception happened",e);
            throw new ServiceException(e);
        }
    }
}
