package TIN.appWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AppController {
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

    private Image image;
    private FileChooser fileChooser;

    public AppController() {
        FileChooser.ExtensionFilter imageFilter
                = new FileChooser.ExtensionFilter("Image Files", "*.bmp");

        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
    }

    @FXML
    private void onDisconnectButtonClicked() {
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
            image = new Image(file.toURI().toString());
        }
    }

    @FXML
    private void onSendButtonClicked() {
        String filePath = filePathText.getText();

        if (!validateFilePathText(filePath)) {
            showAlertDialog("No file to send", "Please choose a file before sending!");
            return;
        }

        imageView.setImage(image);

        //TODO ask Client to send image
    }

    private void showAlertDialog(String headerMsg, String contextMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contextMsg);
        alert.showAndWait();
    }

    private boolean validateFilePathText(String filePath) {
        return !filePath.equals("*none*");
    }
}
