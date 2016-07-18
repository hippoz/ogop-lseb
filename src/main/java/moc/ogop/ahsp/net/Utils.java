package moc.ogop.ahsp.net;

import moc.ogop.ahsp.net.io.IPacket;
import org.apache.commons.io.IOUtils;

import java.io.*;

/**
 * Created by jameszheng on 2016/7/10.
 */
public class Utils {

    private Utils() {}

    public static byte[] toBytes(Serializable obj) throws IOException {
        ByteArrayOutputStream bis = new ByteArrayOutputStream();
        try {
            ObjectOutputStream ois = new ObjectOutputStream(bis);
            ois.writeObject(obj);
            ois.flush();
            return bis.toByteArray();
        } finally {
            bis.close();
        }
    }

    public static <T extends IPacket> T fromBytes(byte[] bytes) throws IOException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        try {
            return  (T) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            ois.close();
        }
    }


    public static void startThread(String name, boolean daemon, Runnable r) {
        Thread t = new Thread(r, name);
        t.setDaemon(daemon);
        t.start();
    }

    public static <T extends IPacket> T readNext(InputStream is) throws IOException {
        DataInputStream dis = new DataInputStream(is);
        int length = dis.readInt();
        byte[] array = new byte[length];
        IOUtils.readFully(is, array);
        return Utils.fromBytes(array);
    }


    public static void outputToStream(OutputStream os, Serializable obj) throws IOException {
        byte[] bytes = Utils.toBytes(obj);
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(bytes.length);
        dos.write(bytes);
        dos.flush();
    }
}
