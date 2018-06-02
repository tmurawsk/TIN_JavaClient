package TIN;

import TIN.menu.MenuController;

import javax.crypto.NoSuchPaddingException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class ConnectionManager {
    private Connection connection;
    private Encryptor encryptor;
    private Thread receiveThread;
    private Thread sendThread;
    private LinkedList<byte[]> messageQueue;

    private class Receiver implements Runnable {

        @Override
        public void run() {

            try {
                while (true) {
                    //TODO read header and then buffer
                    byte[] buffer = connection.read(Converter.maxImageSize);

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                    encryptor.decrypt(inputStream, outputStream);

                    messageQueue.add(outputStream.toByteArray());
                }
            } catch (NoSuchAlgorithmException | NoSuchPaddingException
                    | InvalidKeyException e) {
                MenuController.showAlertDialog(
                        "Error encrypting message",
                        e.getMessage()
                );
            } catch (Exception e) {
                MenuController.showAlertDialog(
                        "Error reading from socket",
                        e.getMessage()
                );
            } finally {
                try {
                    disconnect();
                } catch (Exception ex) {
                    MenuController.showAlertDialog(
                            "Error closing socket",
                            ex.getMessage()
                    );
                }
            }
        }
    }

    private class Sender implements Runnable {
        private byte[] buffer;
        //TODO encrypt message

        Sender(byte[] buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                connection.send(buffer);
            } catch (Exception e) {
                MenuController.showAlertDialog(
                        "Error sending to socket",
                        e.getMessage()
                );
            }
        }
    }

    public ConnectionManager() {
        encryptor = new Encryptor();
        messageQueue = new LinkedList<>();
    }

    public void connect(String ipAddress, int port) throws Exception {
        connection = new Connection();
        connection.connect(ipAddress, port);

        receiveThread = new Thread(new Receiver());
        receiveThread.start();
    }

    public void disconnect() throws Exception {
        sendThread.interrupt();
        receiveThread.interrupt();

        connection.disconnect();
        messageQueue.clear();
    }

    public byte[] getNextImage() {
        return messageQueue.poll();
    }

    public void send(byte[] buffer) {
        sendThread = new Thread(new Sender(buffer));
        sendThread.start();
    }

}
