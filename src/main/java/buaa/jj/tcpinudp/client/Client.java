package buaa.jj.tcpinudp.client;

import io.netty.buffer.ByteBuf;
import org.beykery.jkcp.KcpClient;
import org.beykery.jkcp.KcpOnUdp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client extends KcpClient {

    private Socket socket;

    Client(String server,int port,Socket socket) throws IOException {
        this.socket = socket;
        connect(new InetSocketAddress(server,port));
        start();
    }

    public void handleReceive(ByteBuf byteBuf, KcpOnUdp kcpOnUdp) {
        try {
            OutputStream os = socket.getOutputStream();
            os.write(byteBuf.array());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleException(Throwable throwable, KcpOnUdp kcpOnUdp) {
        System.out.println(throwable);
    }

    @Override
    public void run() {
        super.run();
    }
}
