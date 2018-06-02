package TIN;

import TIN.menu.MenuController;

import java.util.LinkedList;

public class ConnectionManager {
    private Connection connection;
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
                    messageQueue.add(buffer);
                }
            } catch (Exception e) {
                MenuController.showAlertDialog(
                        "Error reading from socket",
                        e.getMessage()
                );

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
