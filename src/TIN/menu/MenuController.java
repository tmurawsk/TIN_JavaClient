package TIN.menu;

import TIN.ConnectionManager;
import TIN.appWindow.AppController;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private TextField portTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button connectButton;

    @FXML
    private Button exitButton;

    private ConnectionManager connectionManager = new ConnectionManager();

    @FXML
    private void onExitButtonClicked() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onConnectButtonClicked() {

        try {
            validateConnectionData(addressTextField.getText(), portTextField.getText());

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../appWindow/appWindow.fxml"));
            Parent root = fxmlLoader.load();

            AppController controller = fxmlLoader.getController();
            controller.setConnectionManager(connectionManager);

            Scene scene = new Scene(root, 650, 630);

            Stage stage = new Stage();
            stage.setTitle("Java Client");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (NumberFormatException e) {
            showAlertDialog(
                    "Error port number format",
                    "Port number is supposed to be integer. Please insert valid value"
            );
        } catch (IOException e) {
            showAlertDialog(
                    "Error on input/output",
                    e.getMessage()
            );
        } catch (Exception e) {
            showAlertDialog(
                    "Error connecting to server",
                    e.getMessage()
//                    "Connection data invalid or server unreachable"
            );
        }
    }

    private void validateConnectionData(String address, String port) throws Exception {
        connectionManager.connect(address, Integer.parseInt(port));
    }

    public static void showAlertDialog(String headerMsg, String contextMsg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerMsg);
        alert.setContentText(contextMsg);
        alert.showAndWait();
    }
}
