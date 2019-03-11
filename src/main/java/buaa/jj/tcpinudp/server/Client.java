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
    boolean debug = false;
    boolean state;
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
            while (!isInterrupted()) {
                while (is.available() != 0) {
                    ByteBuf bb = PooledByteBufAllocator.DEFAULT.buffer(1500);
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    bb.writeBytes(bytes);
                    String s = "make ide happy";
                    if (debug)
                        System.out.println("send:" + bb.toString());
                    server.send(bb);
                    //bb.release();
                }
            }
            is.close();
            socket.close();
            clients.remove(this);
            if (state) {
                ByteBuf bb = PooledByteBufAllocator.DEFAULT.buffer(1500);
                bb.writeBytes("server is closed".getBytes());
                server.send(bb);
            }
        } catch (IOException e) {
            try {
                socket.close();
                clients.remove(this);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
