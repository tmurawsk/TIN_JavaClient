package TIN.appWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AppController {
    @FXML
    private Text filePathText;

    @FXML
    private Button sendButton;
    @FXML
    private Button loadButton;
    @FXML
    private Button disconnectButton;

    @FXML
    private void onDisconnectButtonClicked() {
        Stage stage = (Stage) disconnectButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onLoadButtonClicked() {

    }

    @FXML
    private void onSendButtonClicked() {
        String filePath = filePathText.getText();

        if(!validateFilePathText(filePath)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No file to send");
            alert.setContentText("Please choose a file before sending!");
            alert.showAndWait();
        }
    }

    private boolean validateFilePathText(String filePath) {
        return !filePath.equals("*none*");
    }
}
