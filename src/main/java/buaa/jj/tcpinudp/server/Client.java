package buaa.jj.tcpinudp.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.beykery.jkcp.KcpOnUdp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Client extends Thread {

    Socket socket;
    KcpOnUdp server;
    static List<Client> clients = new ArrayList<Client>();

    Client(Socket socket, KcpOnUdp server) {
        this.socket = socket;
        this.server = server;
        clients.add(this);
    }

    static Client findClient(KcpOnUdp server) {
        Client client = null;
        ListIterator<Client> iterator = clients.listIterator();
        while (iterator.hasNext()) {
            Client tmp = iterator.next();
            if (tmp.server.equals(server)) {
                client = tmp;
                break;
            }
        }
        return client;
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            ByteBuf bb = PooledByteBufAllocator.DEFAULT.buffer(1500);
            while (!isInterrupted()) {
                while (is.available() != 0) {
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    bb.writeBytes(bytes);
                    String s = "make ide happy";
                    server.send(bb);
                }
            }
            is.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
