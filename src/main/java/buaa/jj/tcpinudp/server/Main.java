package buaa.jj.tcpinudp.server;

public class Main {
    public static void main(String args[]) {
        Server server = new Server(25565,100, 25565);
        server.start();
    }
}
