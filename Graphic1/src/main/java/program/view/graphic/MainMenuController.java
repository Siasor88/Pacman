package program.view.graphic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import program.Main;
import program.controller.LoginController;
import program.controller.ReadAndWriteDatabase;
import program.model.User;
import program.model.game.Game;
import program.model.game.Map;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static program.view.graphic.WelcomeController.*;


public class MainMenuController implements Initializable {

    @FXML
    private Label statusLabel;
    @FXML
    private BorderPane mainPane;
    @FXML
    private Pane mapPane;
    @FXML
    private Button nextMap;
    @FXML
    private Button previousMap;
    @FXML
    private Button saveMap;
    @FXML
    private Button startGame;
    @FXML
    private Button createRandom;

    private ArrayList<Game> maps;
    private int indexOfCurrentMap = 0;

    public static void logout() {
        LoginController.logout();
        WelcomeController.goToLoginMenu();
    }

    public static void goToSetting() {
        try {
            Stage stage = Main.stage;
            SplitPane root = (SplitPane) getRoot("SettingMenu");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ignored) {
            showError();
        }
    }

    public void startGame(ActionEvent actionEvent) {
        GameGraphicalController.setCurrentGame(new Game(((Game) mainPane.getCenter()).getMap().clone()));
        goToGameMenu();
    }

    public void showScoreBoard(ActionEvent actionEvent) {
        System.out.println("baaa");
        try {
            BorderPane root = (BorderPane) getRoot("ScoreBoardMenu");
            Main.stage.setScene(new Scene(root));
            Main.stage.show();
            System.out.println("kir");
        } catch (IOException ignored) {
            ignored.printStackTrace();
            showError();
        }
    }

    public void close(ActionEvent actionEvent) {
    }

    public void goToSetting(ActionEvent actionEvent) {
      goToSetting();
    }

    public static void goToGameMenu() {
        Stage stage = Main.stage;
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Scenes/Game.fxml"));
            BorderPane root = loader.load();
            Scene scene = new Scene(root);
            scene.setOnKeyPressed(loader.getController());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError();
        }

    }

    public void nextMap(ActionEvent actionEvent) {
        if (indexOfCurrentMap < maps.size() - 1) {
            indexOfCurrentMap++;
            setMap();
        }
        setMap();
    }

    public void previousMap(ActionEvent actionEvent) {
        if (indexOfCurrentMap > 0) {
            indexOfCurrentMap--;
            setMap();
        }
        setMap();
    }

    public void saveMap(ActionEvent actionEvent) {
        Game game = (Game) mainPane.getCenter();
        if (!maps.contains(game)) {
            maps.add(game);
            User user = LoginController.getCurrentUser();
            Map map = game.getMap();
            user.addMap(map);
            ReadAndWriteDatabase.updateUser(user);
            showPopup(new Popup(), "Saved!");
        }
    }

    public void deleteMap(ActionEvent actionEvent) {
        Game game = (Game) mainPane.getCenter();
        if (maps.contains(game)) {
            User user = LoginController.getCurrentUser();
            user.removeMap(game.getMap());
            ReadAndWriteDatabase.updateUser(user);
            if (indexOfCurrentMap > 0) {
                indexOfCurrentMap--;
                setMap();
                maps.remove(game);
            } else {
                if (maps.size() != 1) {
                    maps.remove(game);
                    setMap();
                } else {
                    randomMap(null);
                    maps.remove(game);
                }
            }
        } else setMap();
        showPopup(new Popup(), "Deleted!");

    }

    private void setMap() {
        if (indexOfCurrentMap < maps.size() && indexOfCurrentMap >= 0) setMap(maps.get(indexOfCurrentMap));
        else randomMap(null);

    }

    public void randomMap(ActionEvent actionEvent) {
        Map map = new Map();
        mainPane.setCenter(new Game(map));
        setMessage("Random Map");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        maps = new ArrayList<>();
        for (Map map : LoginController.getCurrentUser().getMaps()) {
            maps.add(new Game(map.clone()));
        }
        if (maps.size() != 0) {
            indexOfCurrentMap = 0;
            setMap();
        } else randomMap(null);
    }

    private void setMap(Game game) {
        mainPane.setCenter(game);
        setMessage((indexOfCurrentMap + 1) + "/" + maps.size());
    }

    private void setMessage(String message) {
        statusLabel.setText(message);
    }

    public void continuePreviousGame(ActionEvent actionEvent) {
        if (GameGraphicalController.getCurrentGame() != null) goToGameMenu();
        else showPopup(new Popup(), "There No Game!");
    }

    public void logout(ActionEvent actionEvent) {
        logout();
    }
}
