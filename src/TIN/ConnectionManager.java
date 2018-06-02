package TIN;

import javafx.scene.control.Alert;

public class ConnectionManager {
    private Connection connection;
    private Thread receiveThread;
    private Thread sendThread;

    private class Receiver implements Runnable {

        @Override
        public void run() {
            byte[] buffer = new byte[786486];

            try {
                while (true) {
                    connection.read(buffer);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error reading from socket");
                alert.setContentText(e.getMessage());
                alert.showAndWait();

                try {
                    disconnect();
                } catch (Exception ex) {
                    alert.setHeaderText("Error closing socket");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            }
        }
    }

    public ConnectionManager() {
        connection = new Connection();
    }

    public void connect(String ipAddress, int port) throws Exception {
        connection.connect(ipAddress, port);
    }

    public void disconnect() throws Exception {
        connection.disconnect();
    }


}
