package buaa.jj.tcpinudp.client;

import java.io.IOException;
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

    }
}
