package TIN;

import TIN.menu.MenuController;

import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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

                    byte[] message = connection.read(Converter.maxImageSize);

                    System.out.println("Incoming message! Total size: " + message.length + " bytes.");

                    byte[] header = Arrays.copyOfRange(message, 0, 4);
                    byte[] buffer = Arrays.copyOfRange(message, 4, message.length);

                    int size = ByteBuffer.wrap(header).getInt();

                    if (size != buffer.length)
                        throw new Exception("Wrong amount of data read from socket");

//                    ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
//                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

//                    byte[] decrypted = encryptor.decrypt(buffer);
//                    messageQueue.add(decrypted);
                    messageQueue.add(buffer);
                }
            } catch (NoSuchAlgorithmException | NoSuchPaddingException
                    | InvalidKeyException e) {
                MenuController.showAlertDialog(
                        "Error encrypting message",
                        e.getMessage()
                );
            } catch (SocketException e) {
                System.out.println("Shutting down receiver...");
            } catch (Exception e) {
                MenuController.showAlertDialog(
                        "Error reading from socket",
                        e.getMessage()
                );
            }
        }
    }

    private class Sender implements Runnable {
        private byte[] buffer;

        Sender(byte[] buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
//                ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                System.out.println("Sending image...");

//                byte[] encrypted = encryptor.encrypt(buffer);
                byte[] encrypted = buffer;

                ByteBuffer byteBuf = ByteBuffer.allocate(4);
                byteBuf.putInt(encrypted.length);
                byte[] header = byteBuf.array();

                byte[] message = new byte[encrypted.length + header.length];
                System.arraycopy(header, 0, message, 0, header.length);
                System.arraycopy(encrypted, 0, message, header.length, encrypted.length);

                connection.send(message);

                System.out.println("Image sent. Total size: " + message.length + " bytes.");

            } catch (NoSuchAlgorithmException | NoSuchPaddingException
                    | InvalidKeyException e) {
                System.err.println("Error encrypting message!");
            } catch (SocketException e) {
                System.out.println("Shutting down sender...");
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
