package moc.ogop.ahsp.net.server;

import moc.ogop.ahsp.net.IOgopService;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

/**
 * Created by jameszheng on 2016/7/10.
 */
public class OgopServiceComponent implements IOgopService {

    static final Random rand = new Random(System.currentTimeMillis());

    static final String bigMessage;

    static {
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i < 16 ; i++) {
            sb.append((char)('a' + i));
        }
        for (int i = 0; i < 20; i++) {
            sb.append(sb.toString());
        }
        bigMessage = sb.toString();
        System.out.println("Big Message Size(MB): " + bigMessage.length() / 1024 / 1024);
    }

    @Override
    public String reverse(String msg) {
        try {
            Thread.sleep(rand.nextInt(50));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return StringUtils.reverse(msg);
    }

    @Override
    public String bigMessageTest() {
        return bigMessage;
    }
}
