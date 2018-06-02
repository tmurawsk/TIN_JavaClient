package TIN;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private String ipAddress;
    private int port;

    private DataInputStream in;
    private DataOutputStream out;

    Connection() {
        socket = new Socket();
    }

    void connect(String ipAddress, int port) throws Exception {
        this.ipAddress = ipAddress;
        this.port = port;
        socket.connect(new InetSocketAddress(ipAddress, port));
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public void disconnect() throws Exception {
        socket.close();
    }

    public void send(byte[] message) throws Exception {
        out.write(message);
    }

    public void read(byte[] message) throws Exception {
        in.readFully(message);
    }
}
