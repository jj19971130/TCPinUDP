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

    private Server server;
    private Socket socket;
    private boolean debug=false;
    private boolean state=false;

    Client(String serverip,int port,Server server) throws IOException {
        setStream(true);
        this.socket = server.socket;
        this.server = server;
        connect(new InetSocketAddress(serverip,port),new InetSocketAddress("::1",50000));
        start();
        System.out.println("和" + serverip + ":" + port + "的连接建立成功");
    }

    public void handleReceive(ByteBuf byteBuf, KcpOnUdp kcpOnUdp) {
        state = false;
        try {
            OutputStream os = socket.getOutputStream();
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.getBytes(0,bytes);
            String s = new String(bytes,"UTF-8");
            if (s.equals("server is closed")) {
                System.out.println("收到服务端断开的通知，断开本地连接");
                this.close();
                server.interrupt();
            } else {
                if (debug)
                    System.out.println("send:" + byteBuf.toString());
                os.write(bytes);
                os.flush();
            }
            //byteBuf.release();
            return;
        } catch (IOException e) {
            if (!e.getMessage().equals("Socket is closed")) {
                server.state = true;
                server.interrupt();
            }
        }
    }

    public void tryToClose() throws InterruptedException {
        while (!state) {
            state = true;
            Thread.sleep(5);
        }
        System.out.println("确认远端服务器已断开，本地连接断开");
        close();
    }

    public void handleException(Throwable throwable, KcpOnUdp kcpOnUdp) {
        System.out.println(throwable);
    }
}
