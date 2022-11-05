package program.view.graphic;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import program.Main;
import program.controller.LoginController;
import program.model.User;
import program.view.responses.LoginMenuResponses;

import java.io.IOException;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeController {


    @FXML
    private TextField nicknameField;
    @FXML
    private PasswordField passFiledLogin;
    @FXML
    private TextField usernameFieldLogin;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordAgainField;
    @FXML
    private TextField usernameField;

    public static Parent getRoot(String sceneName) throws IOException {
        return FXMLLoader.load(Main.class.getResource("Scenes/" + sceneName + ".fxml"));
    }

    protected static void showPopup(Popup popup, LoginMenuResponses respond) {
        if (!respond.equals(LoginMenuResponses.USER_LOGIN_SUCCESSFUL)) {
            String message = respond.toString().replace("_", " ");
            showPopup(popup, message);
        }
    }

    protected static void showPopup(Popup popup, String message) {
        Label label = new Label();
        label.setText(message);
        label.setTextFill(Color.BLACK);
        label.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        popup.getContent().add(label);
        popup.setAutoHide(true);
        popup.show(Main.stage);
    }

    protected static void showSceneWithDefaultBackground(Pane root) {
        Stage stage = Main.stage;
        Scene scene = getSceneWithDefaultBackground(root);
        stage.setScene(scene);
        stage.show();
    }

    private static Scene getSceneWithDefaultBackground(Pane root) {
        BackgroundImage backgroundImage = new BackgroundImage(Main.deaFaultBackgroundImage, BackgroundRepeat.NO_REPEAT, null, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));
        Scene scene = new Scene(root);
        return scene;
    }

    protected static void showError() {
        showPopup(new Popup(), "Error!");
    }

    protected static void goToMainMenu() {
        try {
            BorderPane root = (BorderPane) getRoot("MainMenu");
            Scene scene = new Scene(root);
            Main.stage.setScene(scene);
            Main.stage.show();
        } catch (IOException ignored) {
            showError();
            ignored.printStackTrace();
        }
    }


    public void goToSignupMenu(ActionEvent actionEvent) {
        goToSignupMenu();
    }

    public static void goToSignupMenu() {
        try {
            AnchorPane root = (AnchorPane) getRoot("SignUpMenu");
            showSceneWithDefaultBackground(root);
        } catch (IOException ignored) {
            System.out.println("k");
        }
    }

    public void goToLoginMenu(ActionEvent actionEvent) {
        goToLoginMenu();
    }

    public static void goToLoginMenu() {
        try {
            GridPane root = (GridPane) getRoot("WelcomeMenu");
            showSceneWithDefaultBackground(root);
        } catch (IOException ignored) {
            showError();
        }
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void signUp(ActionEvent actionEvent) {
        Popup popup = new Popup();
        String password = passwordField.getText();
        String passwordAgain = passwordAgainField.getText();
        String username = usernameField.getText();
        String nickname = nicknameField.getText();
        LoginMenuResponses respond;
        if (!(nickname.equals("") || password.equals("") || passwordAgain.equals("") || username.equals(""))) {
            if (password.equals(passwordAgain)) {
                respond = LoginController.createUser(username, nickname, password);
            } else respond = LoginMenuResponses.PASSWORDS_DIDNT_MATCH;
        } else respond = LoginMenuResponses.PLEASE_FILL_ALL_OF_THE_FIELDS;
        showPopup(popup, respond);
        nicknameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        passwordAgainField.setText("");
    }

    public void login(ActionEvent actionEvent) {
        Popup popup = new Popup();
        String password = passFiledLogin.getText();
        String username = usernameFieldLogin.getText();
        LoginMenuResponses respond;
        if (!(password.equals("") || username.equals("")))
            respond = LoginController.login(username, password);
        else respond = LoginMenuResponses.PLEASE_FILL_ALL_OF_THE_FIELDS;
        showPopup(popup, respond);
        if (respond == LoginMenuResponses.USER_LOGIN_SUCCESSFUL) {
            goToMainMenu();
        }
    }

    public void playAsGuest(ActionEvent actionEvent) {
        LoginController.setCurrentUser(new User("guest","123",Integer.toString((int) System.currentTimeMillis()),null));
        LoginController.setIsGuest(true);
        goToMainMenu();
    }
}
