package TIN.menu;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MenuController {
    @FXML
    private TextField portTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button connectButton;

    @FXML
    private Button exitButton;

    @FXML
    private void onExitButtonClicked() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onConnectButtonClicked() throws Exception {
        //TODO new ConnectionManager

        if(!validateConnectionData(addressTextField.getText(), portTextField.getText()))
            return;

        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../appWindow/appWindow.fxml"));
        //TODO give root ConnectionManager
        Scene scene = new Scene(root, 650, 630);
        stage.setTitle("Java Client");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private boolean validateConnectionData(String address, String port){
        return true; //TODO try to connect from Client
    }
}
