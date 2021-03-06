package buaa.jj.tcpinudp.server;

import io.netty.buffer.ByteBuf;
import org.beykery.jkcp.KcpOnUdp;
import org.beykery.jkcp.KcpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

public class Server extends KcpServer {

    private boolean debug = false;
    private int serverPort;
    public Server(int port, int workerSize, int serverPort) {
        super(port, workerSize);
        setStream(true);
        this.serverPort = serverPort;
    }

    public void handleReceive(ByteBuf byteBuf, KcpOnUdp kcpOnUdp) {
        Client client = null;
        if (kcpOnUdp.getSessionId() == null) {
            kcpOnUdp.setSessionId(UUID.randomUUID().toString().replace("-",""));
            try {
                client = new Client(new Socket("127.0.0.1",serverPort), kcpOnUdp);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            client = Client.findClient(kcpOnUdp);
        }
        OutputStream os = null;
        try {
            os = client.socket.getOutputStream();
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.getBytes(0,bytes);
            String s = new String(bytes,"UTF-8");
            if (s.equals("client is closed")) {
                System.out.println("收到客户端断开的通知，断开本地连接");
                client.interrupt();
            } else {
                if (debug)
                    System.out.println("recv:" + byteBuf.toString());
                os.write(bytes);
                os.flush();
            }
        } catch (IOException e) {
            client.state = true;
            client.interrupt();
            System.out.println("服务器端已断开连接，已告知远端客户端");
        }
    }

    public void handleException(Throwable throwable, KcpOnUdp kcpOnUdp) {
        System.out.println(throwable);
    }

    public void handleClose(KcpOnUdp kcpOnUdp) {
        System.out.println(kcpOnUdp.getRemote().toString() + "的连接已经断开");
        Client client = Client.findClient(kcpOnUdp);
        client.interrupt();
        Client.clients.remove(client);
    }
}
