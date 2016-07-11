package moc.ogop.ahsp.net.server.oio;

import moc.ogop.ahsp.net.RPacket;
import moc.ogop.ahsp.net.Utils;
import moc.ogop.ahsp.net.WPacket;
import moc.ogop.ahsp.net.server.PacketProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Created by jameszheng on 2016/7/10.
 */
class ConnectionWorker<I, T extends I> {

    final static Log log = LogFactory.getLog(ConnectionWorker.class);

    final Socket clientSocket;

    final PacketProcessor processor;

    private ExecutorService executorService;

    ConnectionWorker(Socket clientSocket, PacketProcessor processor, ExecutorService workerThreads) {
        this.clientSocket = clientSocket;
        this.processor = processor;
        this.executorService = workerThreads;
    }

    public void start() {
        Utils.startThread("Reader: " + clientSocket.getLocalPort(), false, () -> {
            try {
                RPacket rp = readPacket();
                while (rp != null) {
                    executorService.submit(new PacketRunnable(rp));
                    rp = readPacket();
                }
            } catch (IOException e) {
                log.info("Client Done");
            }
        });
    }

    RPacket readPacket() throws IOException {
        return Utils.readNext(clientSocket.getInputStream());
    }

    synchronized void writePacket(WPacket wp) throws IOException {
        Utils.outputToStream(clientSocket.getOutputStream(), wp);
    }

    class PacketRunnable implements Runnable {
        final RPacket rp;
        PacketRunnable(RPacket rp) {
            this.rp = rp;
        }
        @Override
        public void run() {
            try {
                writePacket(processor.process(rp));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
