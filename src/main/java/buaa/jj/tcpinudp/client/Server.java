package buaa.jj.tcpinudp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    Client client;
    Socket socket;
    boolean state;
    boolean debug = false;

    Server(String server, int port, Socket socket,int localport) throws IOException {
        this.socket = socket;
        client = new Client(server, port, this,localport);
    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            while (!interrupted()) {
                while (is.available() != 0) {
                    ByteBuf bb = PooledByteBufAllocator.DEFAULT.buffer(1500);
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    bb.writeBytes(bytes);
                    if (debug)
                        System.out.println("recv:" + bb.toString());
                    client.send(bb);
                }
            }

            is.close();
            socket.close();
            if (state) {
                ByteBuf bb = PooledByteBufAllocator.DEFAULT.buffer(1500);
                bb.writeBytes("client is closed".getBytes());
                client.send(bb);
                System.out.println("客户端已断开连接，已告知远端服务器");
            }
            client.tryToClose();
        } catch (IOException e) {
            try {
                socket.close();
                System.out.println("无法连接到服务器端，本地连接已断开");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
