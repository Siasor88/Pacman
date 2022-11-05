package program.view.graphic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import program.controller.LoginController;
import program.controller.SettingController;

import java.io.IOException;

public class DeleteUserController {
    @FXML
    private Button returnButton;
    @FXML
    private PasswordField passwordField;


    public void delete(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setResizable(false);

        if (LoginController.getCurrentUser().getPassword().equals(passwordField.getText())) {
            try {
                AnchorPane root = (AnchorPane) WelcomeController.getRoot("AcceptPopUp");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                WelcomeController.showError();
            }

        } else WelcomeController.showPopup(new Popup(), "Invalid Password");
    }

    public void returnToMenu(ActionEvent actionEvent) {
        ((Stage) returnButton.getScene().getWindow()).close();
    }

    public void deleteUser(ActionEvent actionEvent) {
        SettingController.deleteUser(LoginController.getCurrentUser());
        returnToMenu(null);
        MainMenuController.logout();
    }


}
