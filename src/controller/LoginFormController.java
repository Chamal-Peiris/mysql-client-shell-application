package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginFormController {
    public TextField txtHost;
    public TextField txtPort;
    public TextField txtUsername;
    public TextField txtPassword;
    public Button btnConnect;
    public void initialize() {
        Platform.runLater(()->txtUsername.requestFocus());
    }

    public void btnConnect_OnAction(ActionEvent actionEvent) {

    }
}
