package TIN;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

class Connection {
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

    void disconnect() throws Exception {
        socket.close();
    }

    void send(byte[] message) throws Exception {
        out.write(message);
    }

    byte[] read(int size) throws Exception {
        byte[] message = new byte[size];
        in.readFully(message);
        return message;
    }
}
