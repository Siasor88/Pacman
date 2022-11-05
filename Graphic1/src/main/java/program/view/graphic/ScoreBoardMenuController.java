package program.view.graphic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import program.controller.ScoreBoardController;
import program.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static program.view.graphic.WelcomeController.*;

public class ScoreBoardMenuController implements Initializable {

    @FXML
    private TableView<User> scoreBoard;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<User> users = ScoreBoardController.getSortedUsers();
        TableColumn<User,String> usernames = new TableColumn<>("Usernames");
        usernames.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User,String> nicknames = new TableColumn<>("Nicknames");
        nicknames.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        TableColumn<User,String> scores = new TableColumn<>("Score");
        scores.setCellValueFactory(new PropertyValueFactory<>("score"));

        scoreBoard.getColumns().add(usernames);
        scoreBoard.getColumns().add(nicknames);
        scoreBoard.getColumns().add(scores);

        for (int i = users.size()-1; i >= 0 ; --i) {
            scoreBoard.getItems().add(users.get(i));
        }
    }

    public void back(ActionEvent actionEvent) {
        try {
            Pane root =(Pane)getRoot("MainMenu");
            showSceneWithDefaultBackground(root);

        } catch (IOException e) {
            showError();
        }
    }

    public void close(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void goToSetting(ActionEvent actionEvent) {
        MainMenuController.goToSetting();
    }
}
