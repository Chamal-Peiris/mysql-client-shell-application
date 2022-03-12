package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

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
                InputStream es = mysql.getErrorStream();
                byte[] buffer=new byte[es.available()];
                es.read(buffer);
                es.close();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Failure");
                alert.setHeaderText("Cannot Establish Connection");
                alert.setContentText(new String(buffer));
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.show();
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/MainUI.fxml"));
                AnchorPane root = fxmlLoader.load();
                Scene shellScene = new Scene(root);
                Stage stage = (Stage) txtUsername.getScene().getWindow();
                stage.setScene(shellScene);
                MainUIController controller = fxmlLoader.getController();
                controller.initData(txtHost.getText().trim(),
                        txtPort.getText().trim(),
                        txtUsername.getText().trim(),
                        txtPassword.getText().trim());
                stage.centerOnScreen();
                stage.setTitle("MySQL Client Shell");
                Platform.runLater(stage::sizeToScene);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void btnExit_OnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
