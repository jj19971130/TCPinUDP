package buaa.jj.tcpinudp.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String args[]) throws IOException {
        System.out.println(new String("client is closed".getBytes(),"UTF-8"));
        int localport = Integer.valueOf(args[0]);
        String server = args[1];
        int port = Integer.valueOf(args[2]);
        ServerSocket serverSocket = new ServerSocket(localport);
        System.out.println("已监听127.0.0.1:" + localport);
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("连接尝试打开");
            new Server(server,port,socket).start();
        }
    }
}
