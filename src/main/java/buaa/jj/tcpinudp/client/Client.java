package buaa.jj.tcpinudp.client;

import io.netty.buffer.ByteBuf;
import org.beykery.jkcp.KcpClient;
import org.beykery.jkcp.KcpOnUdp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Client extends KcpClient {

    private ServerSocket socket;
    private String server;
    private int port;
    Client(int localport,String server,int port) throws IOException {
        socket = new ServerSocket(localport);
        this.server = server;
        this.port = port;
    }

    public void handleReceive(ByteBuf byteBuf, KcpOnUdp kcpOnUdp) {

    }

    public void handleException(Throwable throwable, KcpOnUdp kcpOnUdp) {
        System.out.println(throwable);
    }
}
