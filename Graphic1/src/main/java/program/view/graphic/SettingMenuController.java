package program.view.graphic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Popup;
import program.view.responses.ProfileMenuResponses;
import program.controller.SettingController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static program.view.graphic.WelcomeController.getRoot;
import static program.view.graphic.WelcomeController.showError;
import static program.view.graphic.WelcomeController.showPopup;

public class SettingMenuController implements Initializable {

    @FXML
    private SplitPane settingMain;
    @FXML
    private TextField newNickname;
    @FXML
    private PasswordField passwordFieldChangeNickname;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private BorderPane settingArea;
    @FXML
    private ListView<String> settingOptions;

    private final String[] options = {"Change Password", "Change Nickname", "Delete Account", "Default"};



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (settingOptions != null) {
            settingOptions.getItems().addAll(options);
            settingOptions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    String menuName = settingOptions.getSelectionModel().getSelectedItem().replace(" ", "");
                    try {
                        BorderPane borderPane = (BorderPane) getRoot(menuName);
                        settingMain.getItems().remove(settingArea);
                        settingArea = borderPane;
                        settingMain.getItems().add(settingArea);
                    } catch (IOException e) {
                        showError();
                    }
                }
            });
        }
    }

    public void changePassword(ActionEvent actionEvent) {
        Popup popup = new Popup();
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        ProfileMenuResponses respond = SettingController.changePassword(oldPassword, newPassword);
        showPopup(popup, respond.toString().replace("_", " "));
    }

    public void changeNickname(ActionEvent actionEvent) {
        Popup popup = new Popup();
        String password = passwordFieldChangeNickname.getText();
        String nickname = newNickname.getText();
        ProfileMenuResponses respond = SettingController.changeNickname(nickname,password);
        showPopup(popup, respond.toString().replace("_"," "));
    }

    public void back(ActionEvent actionEvent) {
        WelcomeController.goToMainMenu();
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }


}
