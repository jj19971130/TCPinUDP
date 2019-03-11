package buaa.jj.tcpinudp.server;

public class Main {
    public static void main(String args[]) {
        Server server = new Server(25566,100, 25566);
        server.start();
    }
}
