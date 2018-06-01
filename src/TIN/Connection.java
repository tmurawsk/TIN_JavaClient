package TIN;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private String ipAddress;
    private int port;

    private DataInputStream in;
    private DataOutputStream out;

    public Connection(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        socket = new Socket(ipAddress, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void disconnect() {
        socket.close();
    }

    public int send(byte[] message) {
        return out.write(message);
    }

    public int read(byte[] message) {
        return in.readFully(message);
    }
}
