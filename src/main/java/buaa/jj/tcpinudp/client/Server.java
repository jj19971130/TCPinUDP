package buaa.jj.tcpinudp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    Client client;
    Socket socket;

    Server(String server, int port, Socket socket) throws IOException {
        client = new Client(server, port, socket);
        this.socket = socket;
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
                    client.send(bb);
                    //bb.release();
                }
                /*try {
                    socket.sendUrgentData(1);
                } catch (IOException e) {
                    System.out.println("本地客户端已断开连接");
                    is.close();
                    socket.close();
                    client.close();
                    break;
                }*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
