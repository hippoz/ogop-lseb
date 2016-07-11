package moc.ogop.ahsp.net.server;

import moc.ogop.ahsp.net.IOgopService;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * Created by Heisenberg on 2016/7/10.
 */
public class OgopServiceComponent implements IOgopService {

    static final Random rand = new Random(System.currentTimeMillis());

    @Override
    public String reverse(String msg) {
        try {
            Thread.sleep(rand.nextInt(50));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return StringUtils.reverse(msg);
    }
}
