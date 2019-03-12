package buaa.jj.tcpinudp.server;

public class Main {
    public static void main(String args[]) {
        Server server = new Server(Integer.valueOf(args[0]),Integer.valueOf(args[1]), Integer.valueOf(args[2]));
        server.start();
    }
}
