package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginFormController {
    public TextField txtHost;
    public TextField txtPort;
    public TextField txtUsername;
    public TextField txtPassword;
    public Button btnConnect;

    public void initialize() {
        Platform.runLater(() -> txtUsername.requestFocus());
    }

    public void btnConnect_OnAction(ActionEvent actionEvent) {

        /* Let's validate some inputs */
        if (txtHost.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Host can't be empty").show();
            txtHost.requestFocus();
            txtHost.selectAll();
            return;
        } else if (!txtPort.getText().matches("\\d+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid port").show();
            txtPort.requestFocus();
            txtPort.selectAll();
            return;
        } else if (txtUsername.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Username can't be empty").show();
            txtUsername.requestFocus();
            txtUsername.selectAll();
            return;
        }


        try {
//            String command = String.format("mysql -h %s -u %s -p%s --port %s -e exit",
//                    txtHost.getText(),
//                    txtUserName.getText(),
//                    txtPassword.getText(),
//                    txtPort.getText());
//            String[] commands = {"mysql",           //usage of arrays
//                    "-h", txtHost.getText(),
//                    "-u", txtUserName.getText(),
//                    "--port", txtPort.getText(),
//                    "-p" + txtPassword.getText(),
//                    "-e", "exit"};
//            Process mysql = Runtime.getRuntime().exec(commands);

            Process mysql = new ProcessBuilder("mysql",    //usage of process builder is best
                    "-h", txtHost.getText(),
                    "-u", txtUsername.getText(),
                    "--port", txtPort.getText(),
                    "-p",
                    "-e", "exit").start();

            mysql.getOutputStream().write(txtPassword.getText().getBytes());
            mysql.getOutputStream().close();

            int exitCode = mysql.waitFor();
            if (exitCode != 0) {
                new Alert(Alert.AlertType.ERROR, "Can't establish the connection, try again").show();
                txtUsername.requestFocus();
                txtUsername.selectAll();
            } else {
                System.out.println("Wade Goda!");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void btnExit_OnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
