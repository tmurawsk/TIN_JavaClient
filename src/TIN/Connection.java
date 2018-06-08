package TIN;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

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
        in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

    void disconnect() throws Exception {
        in.close();
        out.close();
        socket.close();
    }

    void send(byte[] encrypted) throws Exception {
        ByteBuffer byteBuf = ByteBuffer.allocate(4);
        byteBuf.putInt(encrypted.length);
        byte[] header = byteBuf.array();

        byte[] message = new byte[encrypted.length + header.length];
        System.arraycopy(header, 0, message, 0, header.length);
        System.arraycopy(encrypted, 0, message, header.length, encrypted.length);

        out.write(message);
        out.flush();
    }

    byte[] read() throws Exception {
        byte[] message = {};
        byte[] buffer = new byte[Converter.maxImageSize];
        boolean waitingForTheRest = false;
        int bytesRead = 0, size = 0;

        do {
            int n = in.read(buffer);
            bytesRead += n;

            if(bytesRead == size)
                waitingForTheRest = false;

            if(bytesRead > Converter.maxImageSize)
                throw new Exception("Message too large!");

            if(!waitingForTheRest) {
                byte[] header = Arrays.copyOfRange(buffer, 0, 4);
                byte[] payload = Arrays.copyOfRange(message, 4, n);

                size = ByteBuffer.wrap(header).getInt();

                message = new byte[size];

                System.arraycopy(payload, 0, message, 0, n);

                bytesRead -= 4;

                if(bytesRead < size) {
                    waitingForTheRest = true;
                    continue;
                } else
                    return message;
            }

            System.arraycopy(buffer, 0, message, bytesRead - n, n);

        } while (waitingForTheRest);

        return message;

//        int n = in.read(message);


//        if (size == buffer.length)
//            return buffer;
//            throw new Exception("Wrong amount of data read from socket");

//        System.arraycopy(header, 0, message, 0, header.length);
//        System.arraycopy(encrypted, 0, message, header.length, encrypted.length);

//        return message;
    }
}
