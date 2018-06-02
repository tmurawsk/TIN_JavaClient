package TIN.appWindow;

import TIN.ConnectionManager;
import TIN.Converter;
import TIN.Encryptor;
import TIN.menu.MenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class AppController {
    private ConnectionManager connectionManager;

    @FXML
    private Text filePathText;

    @FXML
    private ImageView imageView;

    @FXML
    private Button sendButton;

    @FXML
    private Button loadButton;

    @FXML
    private Button disconnectButton;

    private Image sendingImage;

    private FileChooser fileChooser;


    public AppController() {
        FileChooser.ExtensionFilter imageFilter
                = new FileChooser.ExtensionFilter("Image Files", "*.bmp");

        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
    }

    @FXML
    private void onDisconnectButtonClicked() {
        try {
            connectionManager.disconnect();
        } catch (SecurityException e) {
            showAlertDialog(
                    "Error interrupting thread",
                    e.getMessage()
            );
        } catch (Exception e) {
            showAlertDialog(
                    "Error disconnecting",
                    e.getMessage()
            );
        }

        Stage stage = (Stage) disconnectButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onLoadButtonClicked() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(disconnectButton.getScene().getWindow());

        if (file != null) {
            String fileName = file.getName();
            String fileExtension = fileName.substring(fileName.indexOf(".") + 1, fileName.length());

            if (!fileExtension.equals("bmp")) {
                showAlertDialog("Bad file extension", "Please choose .bmp file!");
                return;
            }

            filePathText.setText(file.getPath());
            sendingImage = new Image(file.toURI().toString());
        }
    }

    @FXML
    private void onSendButtonClicked() {
        String filePath = filePathText.getText();

        if (!validateFilePathText(filePath)) {
            showAlertDialog(
                    "No file to send",
                    "Please choose a file before sending!"
            );
            return;
        }

        ///------------TEST--------------//TODO remove this block
        byte[] before = Converter.getBytesFromImage(sendingImage);
        ByteArrayInputStream in = new ByteArrayInputStream(before);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Encryptor encryptor = new Encryptor();
        try {
            encryptor.encrypt(in, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] after = out.toByteArray();

        ByteArrayInputStream in2 = new ByteArrayInputStream(after);
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        try {
            encryptor.decrypt(in2, out2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView.setImage(Converter.getImageFromBytes(out2.toByteArray()));

        ///--------------------------
//        imageView.setImage(sendingImage); //TODO remove
//        connectionManager.send(Converter.getImageFromBytes(sendingImage)); //TODO uncomment
    }

    @FXML
    private void onImageViewClicked() {
        imageView.setImage(
                Converter.getImageFromBytes(
                        connectionManager.getNextImage()));
    }

    private void showAlertDialog(String headerMsg, String contextMsg) {
        MenuController.showAlertDialog(headerMsg, contextMsg);
    }

    private boolean validateFilePathText(String filePath) {
        return !filePath.equals("*none*");
    }

    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
}
